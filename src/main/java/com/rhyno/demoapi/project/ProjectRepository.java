package com.rhyno.demoapi.project;

import com.rhyno.demoapi.project.model.Project;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends CrudRepository<Project, String> {
}
