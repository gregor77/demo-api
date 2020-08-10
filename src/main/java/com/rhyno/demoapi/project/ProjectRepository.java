package com.rhyno.demoapi.project;

import com.rhyno.demoapi.project.model.Project;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends CrudRepository<Project, Long> {

    @Query("select p from Project p where p.id in :ids")
    List<Project> findByProjectIds(@Param("ids") List<Long> ids);
}
