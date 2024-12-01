package org.example.eksamenkea.repository;

import org.example.eksamenkea.model.Employee;
import org.example.eksamenkea.model.Role;
import org.example.eksamenkea.repository.interfaces.IEmployeeRepository;
import org.example.eksamenkea.service.Errorhandling;
import org.example.eksamenkea.util.ConnectionManager;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Repository("IUSERREPOSITORY")
@Lazy // Angiver, at denne bean kun bliver initialiseret, når den er nødvendig, og ikke ved opstart
public class EmployeeRepository implements IEmployeeRepository {

    public EmployeeRepository() {
    }

    @Override
    public Employee signIn(String email, String password) throws Errorhandling { // Tilpasses til at inkludere alle felter
        Employee employee = null;
        try (Connection con = ConnectionManager.getConnection()) {
            String SQLUser = "SELECT * FROM employee WHERE email = ? AND password = ?;";
            try (PreparedStatement pstmt = con.prepareStatement(SQLUser)) {
                pstmt.setString(1, email);
                pstmt.setString(2, password);
                try (ResultSet resultSet = pstmt.executeQuery()) {
                    if (resultSet.next()) {
                        int employee_id = resultSet.getInt("employee_id");
                        String roleString = resultSet.getString("role");
                        Role role = Role.valueOf(roleString); // Konvertering fra String til ENUM
                        int employee_rate = resultSet.getInt("employee_rate");
                        int max_hours = resultSet.getInt("max_hours");

                        // Opdateret constructor i Employee til at inkludere nye felter
                        employee = new Employee(employee_id, email, password, role, employee_rate, max_hours);
                    }
                }
            }
        } catch (SQLException e) {
            throw new Errorhandling("Sign-in error: " + e.getMessage()); // Brugerdefineret exception
        }
        return employee;
    }

    @Override
    public java.util.List<Employee> getAllWorkers() throws Errorhandling {
        java.util.List<Employee> workerList = new java.util.ArrayList<>();
        String query = "SELECT * FROM employee WHERE role = 'Worker'";

        try (Connection con = ConnectionManager.getConnection();
        PreparedStatement preSta = con.prepareStatement(query)) {
            try (ResultSet resultSet = preSta.executeQuery()) {
                while (resultSet.next()) {
                    workerList.add(new Employee(
                            resultSet.getInt("employee_id"),
                            resultSet.getString("email"),
                            resultSet.getString("password"),
                            Role.valueOf(resultSet.getString("role").toUpperCase()),
                            resultSet.getInt("employee_rate"),
                            resultSet.getInt("max_hours")
                    ));
                }
                return workerList;
            }

        } catch (SQLException e) {
            throw new Errorhandling("Failed to get all workers: " + e.getMessage());
        }
    }

    @Override
    public Employee getEmployeeByEmail(String email) throws Errorhandling {
        Employee employee = null;
        String query = "SELECT * FROM employee WHERE email = ?";
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement preSta = con.prepareStatement(query)) {
            preSta.setString(1, email);
            try (ResultSet resultSet = preSta.executeQuery()) {
                if (resultSet.next()) {
                    employee = new Employee(
                            resultSet.getInt("employee_id"),
                            resultSet.getString("email"),
                            resultSet.getString("password"),
                            Role.valueOf(resultSet.getString("role").toUpperCase()),
                            resultSet.getInt("employee_rate"),
                            resultSet.getInt("max_hours")
                    );
                }
                return employee;
            }
        } catch (SQLException e) {
            throw new Errorhandling("Failed to get worker: " + e.getMessage());
        }
    }
}
