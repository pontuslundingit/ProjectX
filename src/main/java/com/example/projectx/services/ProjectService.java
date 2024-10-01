package com.example.projectx.services;


import com.example.projectx.models.Project;
import com.example.projectx.models.User;
import com.example.projectx.repository.ProjectRepository;
import com.example.projectx.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired UserService userService;

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public void createProject(Project project, String username) {
        User user = userService.registerUserIfNotExists(username); // Registrera användaren om den inte finns
        project.setUser(user); // Koppla projektet till användaren
        projectRepository.save(project); // Spara projektet
    }

    public List<Project> getProjectsForUser(String username) {
        Optional<User> userOptional = userService.findByUsername(username);
        if (userOptional.isPresent()) {
            return projectRepository.findByUser(userOptional.get()); // Hämta projekt för den existerande användaren
        }
        return new ArrayList<>(); // Returnera en tom lista om användaren inte finns
    }



    public Project getProjectById(Long id) {
        return projectRepository.findById(id).orElse(null);
    }

    public Project updateProject(Long id, Project projectDetails) {
        Project project = projectRepository.findById(id).orElse(null);
        if (project != null) {
            project.setName(projectDetails.getName());
            project.setDescription(projectDetails.getDescription());
            project.setStatus(projectDetails.getStatus());
            project.setLink(projectDetails.getLink());
            return projectRepository.save(project);
        }
        return null;
    }

    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }

}
