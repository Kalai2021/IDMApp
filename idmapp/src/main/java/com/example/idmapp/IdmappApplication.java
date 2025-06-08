package com.example.idmapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
    "com.example.idmapp",
    "com.example.usermanagement",
    "com.example.groupmanagement",
    "com.example.rolemanagement",
    "com.example.usergroupmanagement",
    "com.example.orgmanagement",
    "com.example.rolemembermanagement",
    "com.example.orgmembermanagement"
})

public class IdmappApplication {

	public static void main(String[] args) {
		SpringApplication.run(IdmappApplication.class, args);
	}
}
