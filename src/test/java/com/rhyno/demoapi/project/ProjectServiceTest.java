package com.rhyno.demoapi.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rhyno.demoapi.exception.model.BizException;
import com.rhyno.demoapi.exception.model.NotFoundException;
import com.rhyno.demoapi.project.model.Project;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {
    private static final String ANY_TENANT = "any-tenant";
    private static final Long ANY_PROJECT_ID = 1L;
    private static final String ANY_PROJECT_NAME = "any-project-name";

    @InjectMocks
    private ProjectService subject;

    @Mock
    private ProjectRepository mockProjectRepository;
    private byte[] jsonProject;

    static Project project;

    @Captor
    private ArgumentCaptor<Project> projectCaptor;

    @BeforeAll
    static void beforeAll() {
        project = Project.builder()
                .id(ANY_PROJECT_ID)
                .name(ANY_PROJECT_NAME)
                .build();
    }

    @Nested
    class getProject {
        @Test
        void thenThrowNotFoundException() {
            NotFoundException exception = assertThrows(NotFoundException.class,
                    () -> subject.getProject(ANY_PROJECT_ID));

            assertThat(exception.getMessage())
                    .isEqualTo("Project is not found with id=1");
        }

        @Test
        void thenReturnFoundProject() {
            given(mockProjectRepository.findById(ANY_PROJECT_ID))
                    .willReturn(Optional.ofNullable(project));

            Project foundProject = subject.getProject(ANY_PROJECT_ID);
            assertThat(foundProject.getId()).isEqualTo(ANY_PROJECT_ID);
            assertThat(foundProject.getName()).isEqualTo("any-project-name");
        }
    }

    @Nested
    class createProject {
        @Test
        @DisplayName("then throw exception when project does not exist")
        void error() {
            BizException exception = assertThrows(BizException.class,
                    () -> subject.createProject(ANY_TENANT, null));

            assertThat(exception.getMessage()).isEqualTo("Project is required");
        }

        @Test
        @DisplayName("then save a project with tenantId")
        public void normalCase() {
            subject.createProject(ANY_TENANT, project);

            then(mockProjectRepository).should().save(projectCaptor.capture());
            Project target = projectCaptor.getValue();
            assertThat(target.getTenantId()).isEqualTo(ANY_TENANT);
            assertThat(target.getId()).isEqualTo(ANY_PROJECT_ID);
            assertThat(target.getName()).isEqualTo(ANY_PROJECT_NAME);
        }
    }

    @Disabled
    @Test
    void checkJsonString() throws IOException {
//        String jsonProject = "{\n" +
//                "  \"id\": \"any-project-id\",\n" +
//                "  \"name\": \"any\"\n" +
//                "}";
        ObjectMapper objectMapper = new ObjectMapper();
        Project project = objectMapper.readValue(jsonProject, Project.class);

        assertThat(project).isEqualTo(Project.builder()
                .id(ANY_PROJECT_ID)
                .name("any")
                .build());
    }


}