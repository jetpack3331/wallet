package hut34.wallet.framework.usermanagement.controller;

import org.junit.Test;
import org.springframework.security.core.userdetails.UserDetails;
import hut34.wallet.framework.usermanagement.dto.AuthUser;
import hut34.wallet.testinfra.BaseControllerTest;

import static java.util.Collections.emptyList;
import static java.util.Optional.of;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static hut34.wallet.framework.usermanagement.model.User.byEmail;

public class UserControllerTest extends BaseControllerTest {

    @Test
    public void user_WillGetUserByCurrentPrincipal() throws Exception {
        given(userService.get("bob")).willReturn(of(byEmail("bob@email.com", "password")));

        UserDetails userDetails = new AuthUser("id", "bob", "password", emptyList());

        mvc.perform(
            get("/api/users/me").contentType(APPLICATION_JSON).with(user(userDetails)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("email", is("bob@email.com")));
    }

}
