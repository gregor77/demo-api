package com.rhyno.demoapi.project;

import com.rhyno.demoapi.exception.model.BizException;
import com.rhyno.demoapi.exception.model.NotFoundException;
import com.rhyno.demoapi.project.model.Project;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public Project getProject(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Project is not found with id=" + id));
    }

    public void createProject(String tenantId, Project project) {
        if (project == null) {
            throw new BizException("Project is required");
        }

        project.setTenantId(tenantId);
        projectRepository.save(project);
    }

    public List<Project> updateProjects(List<Project> projects) {
        return projects.stream()
                .map(this::updateProject)
                .collect(Collectors.toList());
    }

    private Project updateProject(Project project) {
        Optional<Project> optionalProject = projectRepository.findById(project.getId());
        if (!optionalProject.isPresent()) {
            throw new NotFoundException("Project is not found. id=" + project.getId());
        }
        return projectRepository.save(optionalProject.get());
    }
}
