package com.example.projectx.services;


import com.example.projectx.models.Project;
import com.example.projectx.models.User;
import com.example.projectx.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock UserService userService;

    @InjectMocks
    private ProjectService projectService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createProject_ShouldSaveProject() {
        Project project = new Project();
        project.setName("New Project");
        project.setDescription("Testing testing");
        project.setCreatedAt(LocalDateTime.now());
        project.setStatus("Ongoing");
        String username = "testuser";
        User user = new User();
        user.setUsername(username);

        when(userService.registerUserIfNotExists(username)).thenReturn(user);

        projectService.createProject(project, username);

        assertNotNull(project.getUser());
        assertNotNull(project.getCreatedAt());
        verify(projectRepository, times(1)).save(project);
    }

    @Test
    void getProjectsForUser_ShouldReturnProjectsForUser() {
        String username = "testuser";
        User user = new User();
        user.setUsername(username);
        List<Project> projects = new ArrayList<>();
        projects.add(new Project());

        when(userService.findByUsername(username)).thenReturn(Optional.of(user));
        when(projectRepository.findByUser(user)).thenReturn(projects);

        List<Project> result = projectService.getProjectsForUser(username);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(projectRepository, times(1)).findByUser(user);
    }

    @Test
    void getProjectsForUSer_ShouldReturnEmptyListIfUserNotFound() {
        String username = "nonexistentuser";

        when(userService.findByUsername(username)).thenReturn(Optional.empty());

        List<Project> result = projectService.getProjectsForUser(username);

        assertTrue(result.isEmpty());
        verify(projectRepository, never()).findByUser(any());
    }

    @Test
    void getProjectById_ShouldReturnProject() {
        Long projectId = 1L;
        Project project = new Project();
        project.setId(projectId);

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));

        Optional<Project> result = projectService.getProjectById(projectId);

        assertTrue(result.isPresent());
        assertEquals(projectId, result.get().getId());
        verify(projectRepository, times(1)).findById(projectId);
    }

    @Test
    void updateProject_ShouldUpdateExistingProject() {
        Long projectId = 1L;
        Project existingProject = new Project();
        existingProject.setId(projectId);
        existingProject.setName("Old project");
        existingProject.setDescription("Old description");
        existingProject.setStatus("Ongoing");

        Project updatedProject = new Project();
        updatedProject.setName("Updated project");
        updatedProject.setDescription("Updated description");
        updatedProject.setStatus("Completed");

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(existingProject));

        projectService.updateProject(projectId, updatedProject);

        assertEquals("Updated project", existingProject.getName());
        assertEquals("Updated description", existingProject.getDescription());
        assertEquals("Completed", existingProject.getStatus());
        assertNotNull(existingProject.getCreatedAt());
        verify(projectRepository, times(1)).save(existingProject);
    }

    @Test
    void updateProject_ShouldThrowExceptionIfProjectNotFound() {
        Long projectId = 1L;
        Project updatedProject = new Project();

        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> projectService.updateProject(projectId, updatedProject));
        verify(projectRepository, never()).save(any());
    }

    @Test
    void deleteProject_ShouldDeleteProject() {
        Long projectId = 1L;

        projectService.deleteProject(projectId);

        verify(projectRepository, times(1)).deleteById(projectId);
    }

}
