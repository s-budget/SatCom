package hr.fer.progi.satcom.tests;

import hr.fer.progi.satcom.models.Message;
import hr.fer.progi.satcom.pullingFromApi.StationChecker;
import hr.fer.progi.satcom.services.impl.MessageServiceImpl;
import hr.fer.progi.satcom.services.impl.UserServiceImpl;
import hr.fer.progi.satcom.utils.SatelliteConnector;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TestCommunicationWithSatellite {
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private MessageServiceImpl messageService;

    @Test
    @DisplayName("Test missing message or communication parameters")
    void testSendMessageWithMissingParameters(){
        //For this test Server application must be running
        Message message = new Message("TestStatName","TestSatName", LocalDateTime.now(),
                "Testing creaion of message","UPLOAD",3000,"BPSK",150000);
        message.setUserMessage(userService.fetch(4L));

        SatelliteConnector connector = new SatelliteConnector();
        message.setSatelliteName(null);
        assertNull(connector.sendMessage(message));
        message.setSatelliteName("TestStatName");
        message.setStationName(null);
        assertNull(connector.sendMessage(message));
        message.setStationName("TestSatName");
        message.setText(null);
        assertNull(connector.sendMessage(message));
        message.setText("");
        assertNull(connector.sendMessage(message));
        message.setText("Testing parameters");
        message.setDirection(null);
        assertNull(connector.sendMessage(message));
        message.setDirection("UPLOAD");
        message.setFreq(null);
        assertNull(connector.sendMessage(message));
        message.setFreq((long) 3000);
        message.setMode(null);
        assertNull(connector.sendMessage(message));
        message.setMode("BPSK");
        message.setBaud(null);
        assertNull(connector.sendMessage(message));
        message.setBaud(150);
    }

    @Test
    @DisplayName("Test sending a valid message to erver")
    void testSendMessageWithAkkParameters(){
        //For this test Server application must be running
        Message message = new Message("TestStatName","TestSatName", LocalDateTime.now(),
                "Testing creaion of message","UPLOAD",3000,"BPSK",150000);
        message.setUserMessage(userService.fetch(4L));
        SatelliteConnector connector = new SatelliteConnector();
        Message receivedMessage=connector.sendMessage(message);
        assertNotNull(receivedMessage);
    }
}
