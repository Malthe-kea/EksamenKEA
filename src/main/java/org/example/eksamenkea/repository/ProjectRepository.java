package org.example.eksamenkea.repository;

import org.example.eksamenkea.model.Project;
import org.example.eksamenkea.model.ProjectCostDTO;
import org.example.eksamenkea.repository.interfaces.IProjectRepository;
import org.example.eksamenkea.service.Errorhandling;
import org.example.eksamenkea.util.ConnectionManager;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository("IPROJECTREPOSITORY")
public class ProjectRepository implements IProjectRepository {
    private final TaskRepository taskRepository;
    private final EmployeeRepository employeeRepository;

    public ProjectRepository(TaskRepository taskRepository, EmployeeRepository employeeRepository) {
        this.taskRepository = taskRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override //Amalie
    public void addProject(Project project) throws Errorhandling {
        String sqlAddProject = "INSERT INTO project(project_name, budget, project_description, employee_id, material_cost,is_archived) VALUES (?, ?, ?, ?, ?,?)";
        System.out.println(project);
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement statement = con.prepareStatement(sqlAddProject)) {

            statement.setString(1, project.getProjectName());
            statement.setDouble(2, project.getBudget());
            statement.setString(3, project.getProjectDescription());
            statement.setInt(4, project.getEmployeeId());
            statement.setDouble(5, project.getMaterialCost());
            statement.setBoolean(6, false);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new Errorhandling("Failed to add project: " + e.getMessage());
        }
    }

    @Override //Malthe
    public Project getProjectFromProjectId(int projectId) throws Errorhandling {
        Project project = null;
        String query = "SELECT * FROM project WHERE project_id = ?";

        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = con.prepareStatement(query)) {

            preparedStatement.setInt(1, projectId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    project = new Project(
                            resultSet.getInt("project_id"),
                            resultSet.getString("project_name"),
                            resultSet.getDouble("budget"),
                            resultSet.getString("project_description"),
                            resultSet.getInt("employee_id"),
                            resultSet.getInt("material_cost")
                    );
                }
            }
        } catch (SQLException e) {
            throw new Errorhandling("Failed to fetch project for project ID " + projectId + ": " + e.getMessage());
        }
        return project;
    }

    @Override //Malthe
    public void updateProject(Project project) throws Errorhandling {
        String sqlAddProject = "UPDATE project SET project_name = ?, budget = ?, project_description = ?, employee_id = ?, material_cost = ? WHERE project_id = ?";
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement statement = con.prepareStatement(sqlAddProject)) {

            statement.setString(1, project.getProjectName());
            statement.setDouble(2, project.getBudget());
            statement.setString(3, project.getProjectDescription());
            statement.setInt(4, project.getEmployeeId());
            statement.setDouble(5, project.getMaterialCost());
            statement.setInt(6, project.getProjectId());
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new Errorhandling("Failed to update project: " + e.getMessage());
        }
    }

    @Override //Zuhur
    public List<ProjectCostDTO> getArchivedProjects(int employeeId) throws Errorhandling {
        List<ProjectCostDTO> projects = new ArrayList<>();
        String queryView = "SELECT project.*,SUM(task.actual_hours * employee.employee_rate) AS employee_cost FROM project " +
                "LEFT JOIN subproject ON project.project_id = subproject.project_id " +
                "LEFT JOIN task ON subproject.subproject_id = task.subproject_id " +
                "LEFT JOIN employee ON task.employee_id = employee.employee_id " +
                "WHERE project.employee_id = ? AND project.is_archived = TRUE Group by project.project_id";

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement1 = connection.prepareStatement(queryView)) {
            preparedStatement1.setInt(1, employeeId);

            ResultSet resultSet = preparedStatement1.executeQuery();
            while (resultSet.next()) {
                int projectId = resultSet.getInt("project_id");
                int totalTime = calculateTimeConsumptionProject(connection,projectId);

                projects.add(new ProjectCostDTO(
                        projectId,
                        resultSet.getString("project_name"),
                        resultSet.getDouble("budget"),
                        resultSet.getString("project_description"),
                        resultSet.getInt("employee_id"),
                        resultSet.getInt("material_cost"),
                        resultSet.getInt("employee_cost"),
                        totalTime
                ));
            }
        } catch (SQLException e) {
            throw new Errorhandling("Failed to get projects by employee ID: " + e.getMessage());
        }
        return projects;
    }

    @Override //Zuhur
    public void archiveProject(int projectId) throws Errorhandling {
        String archiveProjectQuery = "UPDATE project SET is_archived = TRUE WHERE project_id = ?";
        String archiveSubprojectQuery = "UPDATE subproject SET is_archived = TRUE WHERE project_id = ?";
        String archiveTaskQuery = "UPDATE task t " +
                "JOIN subproject sp ON t.subproject_id = sp.subproject_id " +
                "SET t.is_archived = TRUE " +
                "WHERE sp.project_id = ?";

        try (Connection connection = ConnectionManager.getConnection()) {
            connection.setAutoCommit(false); // Start transaktion

            try (PreparedStatement projectStmt = connection.prepareStatement(archiveProjectQuery);
                 PreparedStatement subprojectStmt = connection.prepareStatement(archiveSubprojectQuery);
                 PreparedStatement taskStmt = connection.prepareStatement(archiveTaskQuery)) {

                // Arkiver projekt
                projectStmt.setInt(1, projectId);
                int projectRows = projectStmt.executeUpdate();
                if (projectRows == 0) {
                    throw new Errorhandling("No project found with ID: " + projectId);
                }

                // Arkiver tilknyttede subprojekter
                subprojectStmt.setInt(1, projectId);
                subprojectStmt.executeUpdate();

                // Arkiver tilknyttede tasks
                taskStmt.setInt(1, projectId);
                taskStmt.executeUpdate();

                connection.commit(); // Udfør transaktionen
            } catch (SQLException e) {
                connection.rollback(); // Rul tilbage ved fejl
                throw new Errorhandling("Failed to archive project and related data: " + e.getMessage());
            }
        } catch (SQLException e) {
            throw new Errorhandling("Database error during archiving: " + e.getMessage());
        }
    }


    @Override
    public List<ProjectCostDTO> getProjectsDTOByEmployeeId(int employeeId) throws Errorhandling {
        List<ProjectCostDTO> projects = new ArrayList<>();
        String queryView =
                "SELECT project.project_id, project.project_name, project.budget, project.project_description, " +
                        "project.employee_id, project.material_cost, " +
                        "SUM(task.actual_hours * employee.employee_rate) AS employee_cost " +
                        "FROM project " +
                        "LEFT JOIN subproject ON project.project_id = subproject.project_id " +
                        "LEFT JOIN task ON subproject.subproject_id = task.subproject_id " +
                        "LEFT JOIN employee ON task.employee_id = employee.employee_id " +
                        "WHERE project.employee_id = ? AND project.is_archived = FALSE " +
                        "GROUP BY project.project_id";

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(queryView)) {
            preparedStatement.setInt(1, employeeId);

            ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    int projectId = resultSet.getInt("project_id");

                    // Brug den eksisterende forbindelse til at beregne tid
                    int totalTime = calculateTimeConsumptionProject(connection, projectId);

                    projects.add(new ProjectCostDTO(
                            projectId,
                            resultSet.getString("project_name"),
                            resultSet.getDouble("budget"),
                            resultSet.getString("project_description"),
                            resultSet.getInt("employee_id"),
                            resultSet.getInt("material_cost"),
                            resultSet.getInt("employee_cost"),
                            totalTime // Tilføj samlet tid
                    ));
                }

        } catch (SQLException e) {
            throw new Errorhandling("Failed to get projects by employee ID: " + e.getMessage());
        }
        return projects;
    }

    public int calculateTimeConsumptionProject(Connection connection, int projectId) throws Errorhandling {
        int totalTime = 0;
        String query =
                "SELECT SUM(task.estimated_hours) AS total_hours FROM task JOIN subproject ON task.subproject_id = subproject.subproject_id " +
                        "WHERE subproject.project_id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, projectId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    totalTime = resultSet.getInt("total_hours");
                }
            }
        } catch (SQLException e) {
            throw new Errorhandling("Failed to calculate time for project ID " + projectId + ": " + e.getMessage());
        }
        return totalTime;
    }


}


