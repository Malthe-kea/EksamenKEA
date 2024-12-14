package org.example.eksamenkea.repository.interfaces;
import org.example.eksamenkea.model.Employee;
import org.example.eksamenkea.Errorhandling;
import java.util.List;

public interface IEmployeeRepository {

    Employee signIn(String email, String password) throws Errorhandling;

    List<Employee> getAllWorkers() throws Errorhandling;

    Employee getEmployeeByEmail(String email) throws Errorhandling;

    List<List<Object>> getWorkloadByEmployeeId(int employeeId) throws Errorhandling;

}
