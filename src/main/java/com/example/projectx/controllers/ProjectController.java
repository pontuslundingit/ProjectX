package com.example.projectx.controllers;


import com.example.projectx.repository.ProjectRepository;
import com.example.projectx.services.ProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class ProjectController {

    public ProjectController(ProjectRepository projectRepository, ProjectService projectService) {}

//    @GetMapping("/home")
//    public String home(Model model) {
//        return "/home";
//    }

}
