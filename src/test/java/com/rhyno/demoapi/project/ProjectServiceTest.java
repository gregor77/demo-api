package com.rhyno.demoapi.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rhyno.demoapi.exception.model.BizException;
import com.rhyno.demoapi.exception.model.NotFoundException;
import com.rhyno.demoapi.project.model.Project;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
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
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {
    private static final Long FIRST_PROJECT_ID = 1L;
    private static final Long SECOND_PROJECT_ID = 2L;

    private static final String ANY_TENANT = "any-tenant";
    private static final String FIRST_PROJECT_NAME = "first";
    private static final String SECOND_PROJECT_NAME = "second";

    @InjectMocks
    private ProjectService subject;

    @Mock
    private ProjectRepository mockProjectRepository;
    private byte[] jsonProject;

    static Project firstProject;
    static Project secondProject;

    @Captor
    private ArgumentCaptor<Project> projectCaptor;

    @BeforeAll
    static void beforeAll() {
        firstProject = Project.builder()
                .id(FIRST_PROJECT_ID)
                .name(FIRST_PROJECT_NAME)
                .build();

        secondProject = Project.builder()
                .id(SECOND_PROJECT_ID)
                .name(SECOND_PROJECT_NAME)
                .build();
    }

    @Nested
    class getProject {
        @Test
        void thenThrowNotFoundException() {
            NotFoundException exception = assertThrows(NotFoundException.class,
                    () -> subject.getProject(FIRST_PROJECT_ID));

            assertThat(exception.getMessage())
                    .isEqualTo("Project is not found with id=1");
        }

        @Test
        void thenReturnFoundProject() {
            given(mockProjectRepository.findById(FIRST_PROJECT_ID))
                    .willReturn(Optional.ofNullable(firstProject));

            Project foundProject = subject.getProject(FIRST_PROJECT_ID);
            assertThat(foundProject.getId()).isEqualTo(FIRST_PROJECT_ID);
            assertThat(foundProject.getName()).isEqualTo(FIRST_PROJECT_NAME);
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
            subject.createProject(ANY_TENANT, firstProject);

            then(mockProjectRepository).should().save(projectCaptor.capture());
            Project target = projectCaptor.getValue();
            assertThat(target.getTenantId()).isEqualTo(ANY_TENANT);
            assertThat(target.getId()).isEqualTo(FIRST_PROJECT_ID);
            assertThat(target.getName()).isEqualTo(FIRST_PROJECT_NAME);
        }
    }

    @Test
    void checkJsonString() throws IOException {
        String jsonProject = "{\n" +
                "  \"id\": \"1\",\n" +
                "  \"name\": \"any\"\n" +
                "}";
        ObjectMapper objectMapper = new ObjectMapper();
        Project project = objectMapper.readValue(jsonProject, Project.class);

        assertThat(project).isEqualTo(Project.builder()
                .id(FIRST_PROJECT_ID)
                .name("any")
                .build());
    }

    @Nested
    class updateProjects {
        @Test
        @DisplayName("when update not existed projects, then throw NotFoundException")
        public void errorCase() {
            NotFoundException notFoundException = assertThrows(
                    NotFoundException.class,
                    () -> subject.updateProjects(Lists.newArrayList(
                            Project.builder().id(1L).name("not-found").build()
                    ))
            );

            assertThat(notFoundException.getMessage())
                    .isEqualTo("Project is not found. id=1");
        }

        @Test
        @DisplayName("when update valid projects, then update multiple projects")
        public void normalCase() {
            given(mockProjectRepository.findById(FIRST_PROJECT_ID)).willReturn(Optional.ofNullable(firstProject));
            given(mockProjectRepository.findById(SECOND_PROJECT_ID)).willReturn(Optional.ofNullable(secondProject));

            subject.updateProjects(Lists.newArrayList(firstProject, secondProject));

            then(mockProjectRepository).should(times(2))
                    .save(projectCaptor.capture());

            assertThat(projectCaptor.getAllValues()).contains(firstProject, secondProject);
        }
    }
}