package com.kristof.szteam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SzteamNetworkApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SzteamNetworkApiApplication.class, args);
	}

}
