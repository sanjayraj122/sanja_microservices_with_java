package com.Zomato_App.Zomato;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ZomatoAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZomatoAppApplication.class, args);
	}

}
