package se.knowit.bookitnotification.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import se.knowit.bookitnotification.service.NotificationService;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class NotificationControllerTests {

    private static final String PATH = "/api/v1/notifications/registration/";
    private static final UUID DEFAULT_UUID = UUID.randomUUID();

    @InjectMocks
    private NotificationController controller;

    @Mock
    private NotificationService service;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void postRequest_sendRegistrationConfirm_ShouldReturn_ok() throws Exception {
        doNothing().when(service).sendRegistrationNotificationAsync(any());
        String incomingJson = "{\"eventId\" : \"" + DEFAULT_UUID + "\", \"participant\" : {}}";
        mockMvc.perform(post(PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(incomingJson))
                .andExpect(status().isOk());
    }
}
