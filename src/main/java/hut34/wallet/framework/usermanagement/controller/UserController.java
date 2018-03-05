package hut34.wallet.framework.usermanagement.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import hut34.wallet.framework.usermanagement.model.User;
import hut34.wallet.framework.usermanagement.service.UserService;

import java.security.Principal;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static hut34.wallet.util.RestUtils.response;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(method = GET, path = "/me")
    public User user(Principal principal) {
        return response(userService.get(principal.getName()));
    }

}
