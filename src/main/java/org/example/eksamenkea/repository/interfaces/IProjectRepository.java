package org.example.eksamenkea.repository.interfaces;
import org.example.eksamenkea.model.Project;
import org.example.eksamenkea.model.ProjectCostDTO;
import org.example.eksamenkea.service.Errorhandling;

import java.sql.Connection;
import java.util.List;

public interface IProjectRepository {

     void addProject(Project project) throws Errorhandling;

     Project getProjectFromProjectId(int projectId) throws Errorhandling;

     void updateProject(Project project) throws Errorhandling;

     void archiveProject(int projectId) throws Errorhandling;

     List<ProjectCostDTO> getArchivedProjects(int employeeId) throws Errorhandling;

     List<ProjectCostDTO>getProjectsDTOByEmployeeId(int employeeId) throws Errorhandling;

     int calculateTimeConsumptionProject(Connection connection, int subprojectId) throws Errorhandling; //kan måske slettes
}

