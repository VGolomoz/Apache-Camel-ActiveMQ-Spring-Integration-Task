package app.integration;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Entry point class for creating and initializing the application.
 *
 * @author Vadym
 *
 */
@SpringBootApplication
public class IntegrationTaskApplication {
	/**
	 * Start the application.
	 */
	public static void main(String[] args) {
		SpringApplication.run(IntegrationTaskApplication.class, args);
	}

	/**
	 * @return bean of connection factory with broker
	 */
	@Bean
	public ActiveMQConnectionFactory activeMQConnectionFactory() {
		return new ActiveMQConnectionFactory("vm://localhost?broker.persistent=false");
	}
}
