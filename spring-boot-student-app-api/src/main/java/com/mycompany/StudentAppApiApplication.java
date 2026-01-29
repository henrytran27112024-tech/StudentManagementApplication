package com.mycompany;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class StudentAppApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudentAppApiApplication.class, args);
	}

	@Bean
	ApplicationRunner init(StudentRepository studentRepository) {
		return (ApplicationArguments args) -> dataSetup(studentRepository);
	}

	private void dataSetup(StudentRepository studentRepository) {
		System.out.println("Adding sample student data");
		Student student = new Student("Nguyen Van", "An");
		studentRepository.save(student);
	}
}