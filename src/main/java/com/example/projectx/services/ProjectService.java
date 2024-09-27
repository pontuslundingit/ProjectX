package com.example.projectx.services;


import com.example.projectx.models.Project;
import com.example.projectx.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Project createProject(Project project) {
        project.setCreatedAt(LocalDateTime.now());
        return projectRepository.save(project);
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
