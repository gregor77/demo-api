package com.rhyno.demoapi.project;

import com.rhyno.demoapi.exception.NotFoundException;
import com.rhyno.demoapi.project.model.Project;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public Project getProject(String id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Project is not found with " + id));
    }
}
