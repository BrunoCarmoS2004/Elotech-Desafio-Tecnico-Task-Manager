package br.com.elotech.desafio.backend.taskmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;


@SpringBootApplication
@EnableCaching
public class TaskmanegerApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskmanegerApplication.class, args);
	}

}
