package com.checkdynamo.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@SpringBootApplication
public class DemoApplication {
	@Bean
	public Function<String,String> finbyname(){ // functional interface  for lambda function //testing purpose only

		return (input)->"fnjabuabaufs";

	}
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
