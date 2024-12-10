package org.example.eksamenkea.service;

import org.example.eksamenkea.model.Subproject;
import org.example.eksamenkea.repository.interfaces.ISubprojectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SubprojectServiceTest {

    @Mock
    private ApplicationContext context; // Mock ApplicationContext

    @Mock
    private ISubprojectRepository iSubprojectRepository;

    @InjectMocks
    private SubprojectService subprojectService;

    private Subproject subproject;

    @BeforeEach
    void setup() {
        when(context.getBean("ISUBPROJECTREPOSITORY")).thenReturn(iSubprojectRepository);

        // Initialize ProjectService with the mocked ApplicationContext
        subprojectService = new SubprojectService(context, "ISUBPROJECTREPOSITORY");

        // Sample project for tests
        subproject = new Subproject(1,"subprojecttest", "testdescription",1);
    }

    @Test
    void getSubprojectBySubprojectId() throws Errorhandling{
        // Arrange
        when(iSubprojectRepository.getSubprojectBySubprojectId(1)).thenReturn(subproject);

        // Act
        Subproject result = subprojectService.getSubprojectBySubprojectId(1);

        // Assert
        assertNotNull(result);
        assertEquals("subprojecttest", result.getSubprojectName());
        assertEquals("testdescription", result.getSubprojectDescription());
    }

    @Test
    void updateSubproject() throws Errorhandling{
        // Act
        subprojectService.updateSubproject(subproject);

        // Assert
        verify(iSubprojectRepository).updateSubproject(subproject);
    }

    @Test
    void getSubjectsByProjectId() throws Errorhandling{
        // Act
        subprojectService.getSubjectsByProjectId(1);

        // Assert
        verify(iSubprojectRepository).getSubjectsByProjectId(1);
        assertNotNull(subproject);
    }
}