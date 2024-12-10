package org.example.eksamenkea.service;
import org.example.eksamenkea.model.Project;
import org.example.eksamenkea.model.ProjectCostDTO;
import org.example.eksamenkea.repository.interfaces.IProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ApplicationContext context; // Mock ApplicationContext

    @Mock
    private IProjectRepository iProjectRepository; // Mock IProjectRepository

    @InjectMocks
    private ProjectService projectService; // Inject mocks

    private Project project;
    private ProjectCostDTO projectCostDTO;

    @BeforeEach
    void setup() {
        // Configure ApplicationContext to return the mocked repository
        when(context.getBean("IPROJECTREPOSITORY")).thenReturn(iProjectRepository);

        // Initialize ProjectService with the mocked ApplicationContext
        projectService = new ProjectService(context, "IPROJECTREPOSITORY");

        // Sample project for tests
        project = new Project(1, "testproject", 200, "testdescription", 1, 200);
        projectCostDTO = new ProjectCostDTO(1, "testproject", 200, "testdescription", 1, 200, 100, 100);
    }

    @Test
    void addProject() throws Exception {
        // Act
        projectService.addProject(project);

        // Assert
        verify(iProjectRepository).addProject(project); // Verify method call
    }

    @Test
    void getProjectFromProjectId() throws Exception {
        // Arrange
        when(iProjectRepository.getProjectFromProjectId(1)).thenReturn(project);

        // Act
        Project result = projectService.getProjectFromProjectId(1);

        // Assert
        assertNotNull(result);
        assertEquals("testproject", result.getProjectName());
        assertEquals(200, result.getBudget());
    }

    @Test
    void archiveProject() throws Exception {
        // Act
        projectService.archiveProject(1);

        // Assert
        verify(iProjectRepository).archiveProject(1);
    }

    @Test
    void getArchivedProjects() throws Exception {
        // Act
        projectService.getArchivedProjects(1);

        // Assert
        verify(iProjectRepository).getArchivedProjects(1);
    }

    @Test
    void updateProject() throws Exception {
        // Act
        projectService.updateProject(project);

        // Assert
        verify(iProjectRepository).updateProject(project);
    }

    @Test
    void getProjectsDTOByEmployeeId() throws Exception {
        // Act
        projectService.getProjectsDTOByEmployeeId(1);

        // Assert
        verify(iProjectRepository).getProjectsDTOByEmployeeId(1);
        assertNotNull(projectCostDTO);
        assertEquals("testproject", projectCostDTO.getProjectName());
        assertEquals(200, projectCostDTO.getBudget());
    }
}
