package com.example.projectx.controllers;

import com.example.projectx.models.Project;
import com.example.projectx.repository.ProjectRepository;
import com.example.projectx.services.ProjectService;
import com.example.projectx.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Objects;
import java.util.Optional;

@Controller
public class ProjectController extends UserModelController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/projects/new")
    public String showCreateForm(Model model) {
        model.addAttribute("project", new Project());
        model.addAttribute("activeFunction", "create-project");
        addUserToModel(model);
        return "create-project";
    }

    @PostMapping("/projects")
    public String createProject(@ModelAttribute("project") Project project, Model model) {
        addUserToModel(model);
        String username = Objects.requireNonNull(model.getAttribute("username")).toString();
        projectService.createProject(project, username);
        return "redirect:/projects";
    }

    @GetMapping("/projects")
    public String listProjects(Model model) {
        addUserToModel(model);
        String username = Objects.requireNonNull(model.getAttribute("username")).toString();
        model.addAttribute("projects", projectService.getProjectsForUser(username));
        model.addAttribute("activeFunction", "projects");
        return "projects";
    }

    @GetMapping("/projects/update/{projectId}")
    public String showUpdateForm(@PathVariable Long projectId, Model model) {
        Optional<Project> projectOptional = projectService.getProjectById(projectId);
        if (projectOptional.isPresent()) {
            model.addAttribute("project", projectOptional.get());
            addUserToModel(model);
            model.addAttribute("activeFunction", "update-project");
            return "update-project";
        } else {
            return "redirect:/projects";
        }
    }


    @PostMapping("/projects/update/{id}")
    public String updateProject(@PathVariable Long id, @ModelAttribute("project") Project project, Model model) {
        addUserToModel(model);
        projectService.updateProject(id, project);
        return "redirect:/projects";
    }


    @PostMapping("/projects/delete/{id}")
    public String deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return "redirect:/projects";
    }

}
