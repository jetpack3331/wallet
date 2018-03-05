package hut34.wallet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    /**
     * BEWARE - this is not used unless you start spring from command-line. See {@link ServletInitializer} instead.
     */
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
