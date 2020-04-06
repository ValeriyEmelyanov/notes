package com.example.notes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Класс для запуска приложения
 */
@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.notes.persist.repositories")
@EntityScan(basePackages = "com.example.notes.persist.entities")
public class NotesApplication {

	/**
	 * Создает кодировщик паролей
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * Точка входа.
	 *
	 * @param args дополнительные параметры запуска (не используются)
	 */
	public static void main(String[] args) {
		SpringApplication.run(NotesApplication.class, args);
	}

}
