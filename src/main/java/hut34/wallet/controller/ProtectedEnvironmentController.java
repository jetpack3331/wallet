package hut34.wallet.controller;

import hut34.wallet.filter.ProtectedEnvironmentFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ConditionalOnBean(ProtectedEnvironmentFilter.class)
@Controller
public class ProtectedEnvironmentController {
    public static final String PROTECTED_AUTH_PATH = "/protected-auth";

    private final ProtectedEnvironmentFilter filter;

    public ProtectedEnvironmentController(ProtectedEnvironmentFilter filter) {
        this.filter = filter;
    }

    @PostMapping(PROTECTED_AUTH_PATH)
    public void authenticate(@RequestParam String password, HttpServletResponse resp) throws IOException {
        if (filter.passwordMatches(password)) {
            resp.addCookie(filter.newCookie());
        }
        resp.sendRedirect("/");
    }

}
