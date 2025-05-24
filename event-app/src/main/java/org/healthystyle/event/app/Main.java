package org.healthystyle.event.app;

import java.util.TimeZone;

import org.healthystyle.event.model.status.Status;
import org.healthystyle.event.model.status.StatusType;
import org.healthystyle.event.repository.status.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"org.healthystyle.event", "org.healthystyle.util"})
@EnableJpaRepositories(basePackages = "org.healthystyle.event.repository")
@EntityScan(basePackages = "org.healthystyle.event.model")
public class Main {
	@Autowired
	private StatusRepository repository;

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		SpringApplication.run(Main.class, args);
	}

	@Bean
	public CommandLineRunner r() {
		return new CommandLineRunner() {

			@Override
			public void run(String... args) throws Exception {
				Status s = new Status(StatusType.ACCEPTED);
				Status s2 = new Status(StatusType.PENDING);
//				repository.save(s);
//				repository.save(s2);
			}
		};
	}
}
