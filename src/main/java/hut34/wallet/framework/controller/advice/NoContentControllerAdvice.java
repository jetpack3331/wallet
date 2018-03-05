package hut34.wallet.framework.controller.advice;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * <p>Override default status code for methods returning void to be 204</p>
 * <p>If the method is annotated with response status it will instead use the response status value</p>
 *
 * <p>To override default value use {@code @ResponseStatus(HttpStatus.ACCEPTED) public void test() { ... }}</p>
 * @see ResponseStatus
 */
@ControllerAdvice
public class NoContentControllerAdvice implements ResponseBodyAdvice<Void> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return returnType.getParameterType().isAssignableFrom(void.class) && !returnType.hasMethodAnnotation(ResponseStatus.class);
    }

    @Override
    public Void beforeBodyWrite(Void body, MethodParameter returnType, MediaType mediaType,
            Class<? extends HttpMessageConverter<?>> converterType, ServerHttpRequest request, ServerHttpResponse response) {

        if(returnType.getParameterType().isAssignableFrom(void.class)) {
            response.setStatusCode(HttpStatus.NO_CONTENT);
        }

        return body;
    }
}
