package com.example.library2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class Library2Application {

	public static void main(String[] args) {
		SpringApplication.run(Library2Application.class, args);
	}

}
