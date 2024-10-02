package com.example.projectx.controllers;

import com.example.projectx.models.Project;
import com.example.projectx.models.User;
import com.example.projectx.repository.ProjectRepository;
import com.example.projectx.services.ProjectService;
import com.example.projectx.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@WebMvcTest(ProjectController.class)
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectService projectService;

    @MockBean
    private UserService userService;

    @MockBean
    private ProjectRepository projectRepository;

    @WithMockUser
    @Test
    void testShowCreateForm() throws Exception {
        User user = new User();
        user.setUsername("testUser");
        when(userService.findByUsername(any())).thenReturn(Optional.of(user));

        mockMvc.perform(get("/projects/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("project"))
                .andExpect(model().attributeExists("activeFunction"));
    }

    @WithMockUser
    @Test
    void testCreateProject() throws Exception {
        Project project = new Project();
        project.setName("New Project");
        project.setDescription("Project Description");

        when(userService.findByUsername(any())).thenReturn(Optional.of(new User()));
        doNothing().when(projectService).createProject(any(Project.class), any(String.class));

        mockMvc.perform(post("/projects")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", project.getName())
                        .param("description", project.getDescription()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects"));

        verify(projectService, times(1)).createProject(any(Project.class), any(String.class));
    }

    @WithMockUser
    @Test
    void testListProjects() throws Exception {
        when(userService.findByUsername(any())).thenReturn(Optional.of(new User()));
        when(projectService.getProjectsForUser(any())).thenReturn(Collections.singletonList(new Project()));

        mockMvc.perform(get("/projects"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("projects"))
                .andExpect(model().attributeExists("activeFunction"))
                .andExpect(view().name("projects"));
    }

    @WithMockUser
    @Test
    void testShowUpdateForm() throws Exception {
        Long projectId = 1L;
        Project project = new Project();
        project.setId(projectId);
        project.setName("Existing Project");
        project.setDescription("Existing Description");

        when(projectService.getProjectById(projectId)).thenReturn(Optional.of(project));

        mockMvc.perform(get("/projects/update/{projectId}", projectId))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("project"))
                .andExpect(model().attributeExists("activeFunction"))
                .andExpect(view().name("update-project"));
    }


    @WithMockUser
    @Test
    void testUpdateProject() throws Exception {
        Long projectId = 1L;
        Project project = new Project();
        project.setId(projectId);
        project.setName("Updated Project");
        project.setDescription("Updated Description");

        doNothing().when(projectService).updateProject(eq(projectId), any(Project.class));

        mockMvc.perform(post("/projects/update/{id}", projectId)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", project.getName())
                        .param("description", project.getDescription()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects"));

        verify(projectService, times(1)).updateProject(eq(projectId), any(Project.class));
    }

    @WithMockUser
    @Test
    void testDeleteProject() throws Exception {
        Long projectId = 1L;

        doNothing().when(projectService).deleteProject(projectId);

        mockMvc.perform(post("/projects/delete/{id}", projectId)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects"));

        verify(projectService, times(1)).deleteProject(projectId);
    }
}
