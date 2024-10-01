package com.example.projectx.controllers;


import com.example.projectx.models.Project;
import com.example.projectx.repository.ProjectRepository;
import com.example.projectx.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ProjectController extends UserModelController{

    @Autowired
    private ProjectService projectService;

    @GetMapping("/projects/new")
    public String showCreateForm(Model model) {
        model.addAttribute("project", new Project());
        model.addAttribute("activeFunction", "create-project");
        addUserToModel(model);
        return "create-project";
    }

    @PostMapping("/projects")
    public String createProject(@ModelAttribute("project") Project project) {
        projectService.createProject(project);
        return "redirect:/projects";
    }

    @GetMapping("/projects")
    public String listProjects(Model model) {
        model.addAttribute("projects", projectService.getAllProjects());
        model.addAttribute("activeFunction", "projects");
        addUserToModel(model);
        return "projects";
    }

}
