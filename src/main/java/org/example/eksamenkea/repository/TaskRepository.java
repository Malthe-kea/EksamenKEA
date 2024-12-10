package org.example.eksamenkea.repository;
import org.example.eksamenkea.model.Status;
import org.example.eksamenkea.model.Task;
import org.example.eksamenkea.repository.interfaces.ITaskRepository;
import org.example.eksamenkea.service.Errorhandling;
import org.example.eksamenkea.util.ConnectionManager;
import org.springframework.stereotype.Repository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository("ITASKREPOSITORY")
public class TaskRepository implements ITaskRepository {
    private final SubprojectRepository subprojectRepository;

    public TaskRepository(SubprojectRepository subprojectRepository) {
        this.subprojectRepository = subprojectRepository;
    }

    @Override //Amalie
    public void createTask(Task task) throws Errorhandling {
        String sqlAddTask = "INSERT INTO task(task_name, start_date, end_date, status, employee_id, estimated_hours, subproject_id, actual_hours) VALUES (?, ?, ?, ?, ?, ?, ?, 0)";
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement statement = con.prepareStatement(sqlAddTask)) {

            statement.setString(1, task.getTaskName());
            statement.setDate(2, Date.valueOf(task.getStartdate())); // Konverter LocalDate til java.sql.Date
            statement.setDate(3, Date.valueOf(task.getEnddate()));
            statement.setInt(4, 4); // Enum værdi 'notstarted' default
            statement.setInt(5, task.getEmployeeId());
            statement.setInt(6, task.getEstimatedHours());
            statement.setInt(7, task.getSubprojectId());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new Errorhandling("Failed to add task: " + e.getMessage());
        }
    }
    @Override //AM-ZU
    public List<Task> getTaskBySubprojectId(int subprojectId) throws Errorhandling {
        List<Task> tasks = new ArrayList<>();
        String query = "SELECT t.task_id, t.task_name, t.start_date, t.end_date, t.status, " +
                "t.subproject_id, t.estimated_hours, t.actual_hours , t.employee_id " +
                "FROM task t " +
                "WHERE t.subproject_id = ? AND t.status != 'COMPLETE'";


        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, subprojectId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    tasks.add(new Task(
                            resultSet.getInt("task_id"),
                            resultSet.getString("task_name"),
                            resultSet.getDate("start_date").toLocalDate(),
                            resultSet.getDate("end_date").toLocalDate(),
                            Status.valueOf(resultSet.getString("status").toUpperCase()),
                            resultSet.getInt("subproject_id"),
                            resultSet.getInt("estimated_hours"),
                            resultSet.getInt("actual_hours"),
                            resultSet.getInt("employee_id") // Fjern actual_hours
                    ));

                }
            }
        } catch (SQLException e) {
            throw new Errorhandling("Failed to fetch tasks for subproject ID " + subprojectId + ": " + e.getMessage());
        }

        return tasks;
    }

    @Override //Malthe
    public List<Task> getTasklistByEmployeeId(int employeeId) throws Errorhandling {
        List<Task> taskList = new ArrayList<>();
        String query = "SELECT task_id, task_name, start_date, end_date, estimated_hours, status, actual_hours, subproject_id, employee_id FROM task WHERE employee_id = ?";

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, employeeId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    taskList.add(new Task(
                            resultSet.getInt("task_id"),
                            resultSet.getString("task_name"),
                            resultSet.getDate("start_date").toLocalDate(),
                            resultSet.getDate("end_date").toLocalDate(),
                            Status.valueOf(resultSet.getString("status").toUpperCase()),
                            resultSet.getInt("subproject_id"),
                            resultSet.getInt("estimated_hours"),
                            resultSet.getInt("actual_hours"),
                            resultSet.getInt("employee_id")
                    ));
                }
                return taskList;
            }
        } catch (SQLException e) {
            throw new Errorhandling("Failed to fetch tasks for employee ID " + employeeId + ": " + e.getMessage());
        }
    }

    @Override //AM-ZU
    public Task getTaskByTaskId(int taskId) throws Errorhandling {
        String query = "SELECT * FROM task WHERE task_id = ?";
        Task task = null;

        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, taskId);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                task = new Task(
                        resultSet.getInt("task_id"),
                        resultSet.getString("task_name"),
                        resultSet.getDate("start_date").toLocalDate(),
                        resultSet.getDate("end_date").toLocalDate(),
                        Status.valueOf(resultSet.getString("status").toUpperCase()),
                        resultSet.getInt("subproject_id"),
                        resultSet.getInt("estimated_hours"),
                        resultSet.getInt("actual_hours"),
                        resultSet.getInt("employee_id")
                );

            }

        } catch (SQLException e) {
            throw new Errorhandling("Failed to get task: " + e.getMessage());
        }
        return task;
    }

    @Override //Amalie
    public void updateTask(Task task) throws Errorhandling {
        String updateSql = "UPDATE task SET status = ?, actual_hours = ? WHERE task_id = ?";
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateSql)) {
            preparedStatement.setString(1, task.getStatus().name());
            preparedStatement.setInt(2, task.getActualHours());
            preparedStatement.setInt(3, task.getTaskId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new Errorhandling("Failed to update task: " + e.getMessage());
        }
    }

    @Override //Malthe
    public void assignWorkerToTask(int taskId, int employeeId) throws Errorhandling {
        String updateSql = "UPDATE task SET employee_id = ? WHERE task_id = ?";
        try {
            Connection connection = ConnectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(updateSql);
            preparedStatement.setInt(1, employeeId);
            preparedStatement.setInt(2, taskId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new Errorhandling("Failed to assign worker to task: " + e.getMessage());
        }
    }

}