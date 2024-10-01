package com.example.projectx.controllers;


import com.example.projectx.models.Project;
import com.example.projectx.models.User;
import com.example.projectx.repository.ProjectRepository;
import com.example.projectx.repository.UserRepository;
import com.example.projectx.services.ProjectService;
import com.example.projectx.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

@Controller
public class ProjectController extends UserModelController{

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
        addUserToModel(model); // Lägg till användarinfo till modellen
        String username = model.getAttribute("username").toString(); // Hämta användarnamnet från modellen
        projectService.createProject(project, username); // Passera projektet och användarnamnet till service
        return "redirect:/projects";
    }

    @GetMapping("/projects")
    public String listProjects(Model model) {
        addUserToModel(model); // Lägg till användarinfo till modellen
        String username = model.getAttribute("username").toString(); // Hämta användarnamnet från modellen
        model.addAttribute("projects", projectService.getProjectsForUser(username)); // Hämta projekt för den inloggade användaren
        model.addAttribute("activeFunction", "projects"); // Sätt aktiv funktion för navigation
        return "projects"; // Återgå till vy för projekt
    }

}
