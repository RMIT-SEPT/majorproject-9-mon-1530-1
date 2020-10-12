package sept.major.availability;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * The starting point and main class of the availability service. Using spring boot to load the dependencies and start as a spring application.
 * 
 * @author Abrar
 *
 */
@SpringBootApplication
public class AvailabilityApplication {

	/**
	 * Main function of the application. expects no argument so any passed argument is ignored.
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(AvailabilityApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}
}
