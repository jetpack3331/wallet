package hut34.wallet.testinfra;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import hut34.wallet.framework.controller.advice.ExceptionHandlerAdvice;
import hut34.wallet.testinfra.rules.LocalServicesRule;
import hut34.wallet.util.DateTimeUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.jackson.JsonComponentModule;
import org.springframework.data.geo.GeoModule;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 * Test controller via {@link MockMvc} without requiring a full container load - faster. If you want to test
 * Spring Security, use {@link BaseControllerIntegrationTest} which sets up web application context.
 */
@RunWith(MockitoJUnitRunner.class)
public abstract class BaseControllerTest {

    @Rule
    public LocalServicesRule localServicesRule = new LocalServicesRule();

    protected MockMvc mvc;

    protected abstract Object controller();

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        // Try to set this up the same as we have it in Spring
        objectMapper = new ObjectMapper()
            .registerModules(
                new JavaTimeModule(),
                new JsonComponentModule(),
                new GeoModule(),
                new JodaModule()
            )
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mvc = MockMvcBuilders
            .standaloneSetup(controller())
            .setControllerAdvice(new ExceptionHandlerAdvice())
            .alwaysDo(print())
            .build();
    }

    protected String asString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Exception converting object to string for test", e);
        }
    }


}
