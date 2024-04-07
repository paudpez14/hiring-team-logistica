package com.funiber.inventory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.funiber.inventory.infrastructure")
public class DomainLayerApplication {

	public static void main(String[] args) {
		SpringApplication.run(DomainLayerApplication.class, args);
	}

}
