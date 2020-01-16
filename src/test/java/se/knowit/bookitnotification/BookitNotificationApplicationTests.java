package se.knowit.bookitnotification;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@EmbeddedKafka(partitions = 1,
		topics = {"registrations", "events"},
		brokerProperties = "listeners=PLAINTEXT://localhost:9092")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
class BookitNotificationApplicationTests {

	@Test
	void contextLoads() {
	}

}
