package nl.kooi.camundarules;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class CamundarulesApplication {

	public static void main(String[] args) {
		SpringApplication.run(CamundarulesApplication.class, args);
	}

}
