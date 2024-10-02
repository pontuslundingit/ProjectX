package com.example.projectx.integration;

import com.example.projectx.models.Project;
import com.example.projectx.models.User;
import com.example.projectx.repository.ProjectRepository;
import com.example.projectx.repository.UserRepository;
import com.example.projectx.services.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ProjectServiceIntegrationTest {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    public void setup() {
        testUser = new User();
        testUser.setUsername("testUser");
        userRepository.save(testUser);
    }

    @Test
    @Rollback
    public void testCreateAndGetProjectsForUser() {
        Project project = new Project();
        project.setName("Test Project");
        project.setDescription("A project for testing");
        project.setStatus("ACTIVE");

        projectService.createProject(project, testUser.getUsername());

        List<Project> projects = projectService.getProjectsForUser(testUser.getUsername());

        assertThat(projects).hasSize(1);
        assertThat(projects.get(0).getName()).isEqualTo("Test Project");
    }

    @Test
    @Rollback
    public void testCreateMultipleProjectsForUser() {
        Project project1 = new Project();
        project1.setName("Test Project 1");
        project1.setDescription("First test project");
        project1.setStatus("ACTIVE");

        Project project2 = new Project();
        project2.setName("Test Project 2");
        project2.setDescription("Second test project");
        project2.setStatus("ACTIVE");

        projectService.createProject(project1, testUser.getUsername());
        projectService.createProject(project2, testUser.getUsername());

        List<Project> projects = projectService.getProjectsForUser(testUser.getUsername());

        assertThat(projects).hasSize(2);
        assertThat(projects).extracting(Project::getName).contains("Test Project 1", "Test Project 2");
    }

    @Test
    @Rollback
    public void testGetProjectsForUserWithNoProjects() {
        List<Project> projects = projectService.getProjectsForUser(testUser.getUsername());

        assertThat(projects).isEmpty();
    }

    @Test
    @Rollback
    public void testUpdateProject() {
        String username = "validUser";
        User testUser = new User();
        testUser.setUsername(username);
        userRepository.save(testUser);


        Project project = new Project();
        project.setName("Old Project Name");
        project.setDescription("Old project description");
        project.setStatus("ACTIVE");
        project.setUser(testUser);
        projectRepository.save(project);

        Project updatedProject = new Project();
        updatedProject.setName("Updated Project Name");
        updatedProject.setDescription("Updated project description");
        updatedProject.setStatus("COMPLETED");

        projectService.updateProject(project.getId(), updatedProject);

        Project fetchedProject = projectRepository.findById(project.getId())
                .orElseThrow(() -> new RuntimeException("Project not found"));

        assertThat(fetchedProject.getName()).isEqualTo("Updated Project Name");
        assertThat(fetchedProject.getDescription()).isEqualTo("Updated project description");
        assertThat(fetchedProject.getStatus()).isEqualTo("COMPLETED");
    }


    @Test
    @Rollback
    public void testDeleteProject_ShouldDelete() {
        Project project = new Project();
        project.setName("Project to Delete");
        project.setDescription("A project that will be deleted");
        project.setStatus("ACTIVE");

        projectService.createProject(project, testUser.getUsername());

        List<Project> projectsBeforeDelete = projectService.getProjectsForUser(testUser.getUsername());
        assertThat(projectsBeforeDelete).hasSize(1);

        projectService.deleteProject(project.getId());

        List<Project> projectsAfterDelete = projectService.getProjectsForUser(testUser.getUsername());
        assertThat(projectsAfterDelete).isEmpty();
    }

}
