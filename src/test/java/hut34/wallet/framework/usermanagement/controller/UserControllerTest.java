package hut34.wallet.framework.usermanagement.controller;

import hut34.wallet.framework.usermanagement.service.UserService;
import hut34.wallet.testinfra.BaseControllerIntegrationTest;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;

import static hut34.wallet.framework.usermanagement.model.User.byEmail;
import static java.util.Optional.of;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest extends BaseControllerIntegrationTest {

    @MockBean
    private UserService userService;

    @WithMockUser("bob")
    @Test
    public void user_WillGetUserByCurrentPrincipal() throws Exception {
        given(userService.get("bob")).willReturn(of(byEmail("bob@email.com", "password")));

        mvc.perform(
            get("/api/users/me").contentType(APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("email", is("bob@email.com")));
    }

}
