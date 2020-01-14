package se.knowit.bookitnotification;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@EmbeddedKafka(partitions = 1,
		topics = {"registrations", "event"},
		brokerProperties = "listeners=PLAINTEXT://localhost:9092")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class BookitNotificationApplicationTests {

	@Test
	void contextLoads() {
	}

}
