package com.kristof.szteam;

import com.kristof.szteam.role.Role;
import com.kristof.szteam.role.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableAsync
@EntityScan({"com.kristof.szteam.user", "com.kristof.szteam.role", "com.kristof.szteam.game", "com.kristof.szteam.review", "com.kristof.szteam.history"})
@EnableJpaRepositories({"com.kristof.szteam.user", "com.kristof.szteam.role", "com.kristof.szteam.game", "com.kristof.szteam.review", "com.kristof.szteam.history"})
public class SzteamNetworkApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SzteamNetworkApiApplication.class, args);
	}

	@Bean
	public CommandLineRunner runner(RoleRepository roleRepository) {
		return args -> {
			if (roleRepository.findByName("USER").isEmpty()) {
				roleRepository.save(
						Role.builder().name("USER").build()
				);
			}

		};
	}
}
