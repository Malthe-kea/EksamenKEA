package org.example.eksamenkea.service;

import org.example.eksamenkea.model.Project;
import org.example.eksamenkea.model.Subproject;
import org.example.eksamenkea.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }


    public List<Project> getAllProjectsByEmployeeId(int employeeId) throws Errorhandling {
        return projectRepository.getProjectsByEmployeeId(employeeId);

    }


    public Project getWorkerProjectFromEmployeeId(int employeeId) throws Errorhandling {
        return projectRepository.getWorkerProjectFromEmployeeId(employeeId);
    }


    public int getProjectIdByProjectName(String projectName) throws Errorhandling {
        return projectRepository.getProjectIdByProjectName(projectName);
    }


    public List<Subproject> getSubjectsByProjectId(int projectId) throws Errorhandling {
        return projectRepository.getSubjectsByProjectId(projectId);
    }

        public void addProject(Project project) throws Errorhandling {
         projectRepository.addProject(project);
    }
}
