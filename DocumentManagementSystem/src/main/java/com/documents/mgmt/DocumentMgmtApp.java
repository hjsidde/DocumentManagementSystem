package com.documents.mgmt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;




@SpringBootApplication

@ComponentScan("com.documents.mgmt.*")
public class DocumentMgmtApp {

	public static void main(String[] args) {
		SpringApplication.run(DocumentMgmtApp.class, args);
	}
}
