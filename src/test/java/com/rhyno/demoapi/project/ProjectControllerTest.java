package com.rhyno.demoapi.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rhyno.demoapi.project.model.Project;
import com.rhyno.demoapi.session.SessionContextHolder;
import com.rhyno.demoapi.session.UserSession;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProjectController.class)
class ProjectControllerTest {
    private static final String ANY_TENANT = "TNT_BASE";
    private static final Long PROJECT_ID = 1L;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private ProjectService mockProjectService;

    @Nested
    class getProject {
        @Test
        void thenReturnFoundProjectWithId() throws Exception {
            given(mockProjectService.getProject(PROJECT_ID))
                    .willReturn(Project.builder()
                            .id(PROJECT_ID)
                            .name("any-project")
                            .build());

            MockHttpServletResponse response = mockMvc.perform(get("/api/v1/projects/" + PROJECT_ID))
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse();

            Project foundProject = objectMapper.readValue(response.getContentAsString(), Project.class);
            assertThat(foundProject.getId()).isEqualTo(PROJECT_ID);
            assertThat(foundProject.getName()).isEqualTo("any-project");
        }
    }

    @Nested
    class createProject {
        @BeforeEach
        void setUp() {
            SessionContextHolder.setSession(UserSession.builder()
                    .tenantId("TNT_BASE")
                    .build());
        }

        @AfterEach
        void tearDown() {
            SessionContextHolder.clear();
        }

        @Test
        @DisplayName("then a project with tenantId from session")
        public void normalCase() throws Exception {
            Project project = Project.builder()
                    .name("any-name")
                    .description("any-description")
                    .build();

            String jsonProject = objectMapper.writeValueAsString(project);

            mockMvc.perform(post("/api/v1/projects")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(jsonProject))
                    .andExpect(status().isCreated())
                    .andReturn()
                    .getResponse();

            then(mockProjectService).should().createProject(ANY_TENANT, project);
        }
    }
}