package com.rhyno.demoapi.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rhyno.demoapi.project.model.Project;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProjectController.class)
class ProjectControllerTest {
    private static final String PROJECT_ID = "any-id";

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
}