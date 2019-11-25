package se.knowit.bookitnotification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class BookitNotificationApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookitNotificationApplication.class, args);
	}

}
