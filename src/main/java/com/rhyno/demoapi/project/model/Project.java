package com.rhyno.demoapi.project.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Project {
    private String tenantId;

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String description;
}
