package hut34.wallet.framework.controller;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import hut34.wallet.framework.controller.dto.ComponentErrorDto;
import hut34.wallet.framework.controller.dto.ErrorDto;
import hut34.wallet.framework.controller.dto.ReduxErrorDto;

@RestController
public class ErrorController {
    private static final Logger logger = LoggerFactory.getLogger(ErrorController.class);

    @PostMapping("/api/error/redux")
    public void logReduxError(@RequestBody ReduxErrorDto reduxErrorDto, Authentication authentication) {
        logClientError(reduxErrorDto, authentication);

        if (!StringUtils.isEmpty(reduxErrorDto.getType())) {
            logger.info("Last action type: {}", reduxErrorDto.getType());
        }

        logger.info("Last action type: {}", reduxErrorDto.getLastAction());
    }

    @PostMapping("/api/error/component")
    public void logComponentError(@RequestBody ComponentErrorDto componentErrorDto, Authentication authentication) {
        logClientError(componentErrorDto, authentication);

        if (StringUtils.isNotBlank(componentErrorDto.getComponentStack())) {
            logger.info("Component Stack: {}", componentErrorDto.getComponentStack());
        }
    }

    @PostMapping("/api/error/unhandled")
    public void logUnhandledError(@RequestBody ErrorDto errorDto, Authentication authentication) {
        logClientError(errorDto, authentication);
    }

    private void logClientError(ErrorDto errorDto, Authentication authentication) {
        Throwable error = new Throwable(errorDto.getMessage());

        error.setStackTrace(errorDto.getStackTrace());

        logger.error(errorDto.getMessage(), error);

        if (authentication != null && authentication.isAuthenticated()) {
            logger.info("Principal: {}", authentication.getName());
        } else {
            logger.info("Principal: Anonymous");
        }
    }
}
