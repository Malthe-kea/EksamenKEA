package org.example.eksamenkea.service;
import org.example.eksamenkea.model.Project;
import org.example.eksamenkea.model.ProjectCostDTO;
import org.example.eksamenkea.repository.interfaces.IProjectRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProjectService {
    private final IProjectRepository iProjectRepository;

    public ProjectService(ApplicationContext context, @Value("IPROJECTREPOSITORY") String impl) {
        this.iProjectRepository = (IProjectRepository) context.getBean(impl);
    }

    public void addProject(Project project) throws Errorhandling {
        iProjectRepository.addProject(project);
    }

    public void archiveProject(int projectId) throws Errorhandling {
        iProjectRepository.archiveProject(projectId);
    }

    public List<ProjectCostDTO> getArchivedProjects(int employeeId) throws Errorhandling {
        return iProjectRepository.getArchivedProjects(employeeId);
    }

    public Project getProjectFromProjectId(int projectId) throws Errorhandling {
        return iProjectRepository.getProjectFromProjectId(projectId);
    }

    public void updateProject(Project project) throws Errorhandling {
        iProjectRepository.updateProject(project);
    }

    public List<ProjectCostDTO> getProjectsDTOByEmployeeId(int employeeId) throws Errorhandling {
        return iProjectRepository.getProjectsDTOByEmployeeId(employeeId);
    }

}
