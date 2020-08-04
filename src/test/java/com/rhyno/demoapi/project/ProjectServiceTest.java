package com.rhyno.demoapi.project;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rhyno.demoapi.project.model.Project;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {
    private static final String PROJECT_ID = "any-project-id";

    @InjectMocks
    private ProjectService subject;

    @Mock
    private ProjectRepository mockProjectRepository;
    private byte[] jsonProject;

    @Nested
    class getProject {
        @Test
        void thenReturnFoundProject() {
            when(mockProjectRepository.findById(PROJECT_ID))
                    .thenReturn(Optional.ofNullable(Project.builder()
                            .id(PROJECT_ID)
                            .name("any-project-name")
                            .build()));

            Project foundProject = subject.getProject(PROJECT_ID);
            assertThat(foundProject.getId()).isEqualTo(PROJECT_ID);
            assertThat(foundProject.getName()).isEqualTo("any-project-name");
        }
    }

    @Test
    void checkJsonString() throws IOException {
//        String jsonProject = "{\n" +
//                "  \"id\": \"any-project-id\",\n" +
//                "  \"name\": \"any\"\n" +
//                "}";
        ObjectMapper objectMapper = new ObjectMapper();
        Project project = objectMapper.readValue(jsonProject, Project.class);

        assertThat(project).isEqualTo(Project.builder()
                .id(PROJECT_ID)
                .name("any")
                .build());
    }


}