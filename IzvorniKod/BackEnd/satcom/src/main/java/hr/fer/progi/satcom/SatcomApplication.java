package hr.fer.progi.satcom;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableJpaRepositories(basePackages = "hr.fer.progi.satcom.dao")

public class SatcomApplication {

	public static void main(String[] args) {
		SpringApplication.run(SatcomApplication.class, args);
	}

}
