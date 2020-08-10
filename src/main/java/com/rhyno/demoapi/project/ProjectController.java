package com.rhyno.demoapi.project;

import com.rhyno.demoapi.project.model.Project;
import com.rhyno.demoapi.session.SessionContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/projects")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/{projectId}")
    public Project getProject(@PathVariable(name = "projectId") Long id) {
        return projectService.getProject(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Project createProject(@RequestBody Project project) {
        String tenantId = SessionContextHolder.getSession().getTenantId();
        projectService.createProject(tenantId, project);

        return project;
    }
}

