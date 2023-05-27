package hr.fer.progi.satcom.tests;

import hr.fer.progi.satcom.models.Message;
import hr.fer.progi.satcom.services.impl.MessageServiceImpl;
import hr.fer.progi.satcom.services.impl.UserServiceImpl;
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
public class TestMessageSaveToDB {
    @Autowired
    private MessageServiceImpl messageService;

    @Autowired
    private UserServiceImpl userService;

    @Test
    @DisplayName("Testing creation of message")
    public void TestMessageSaveToDBOK()
    {
        Message m = new Message("TestStatName","TestSatName", LocalDateTime.now(),
                "Testing creaion of message","UPLOAD",3000,"BPSK",150000);
        m.setUserMessage(userService.fetch(4L));
        messageService.createNewMessage(m);
        Long mId = m.getMessageId();
        assertEquals(m, messageService.fetch(mId));
        messageService.deleteMessage(mId);

    }
    @Test
    @DisplayName("Testing creation of message")
    public void TestMessageSaveToDBNoSateliteName()
    {
        Message m = new Message("TestStatName",null, LocalDateTime.now(),
                "Testing creaion of message","UPLOAD",3000,"BPSK",150000);
        m.setUserMessage(userService.fetch(4L));
        assertThrows(IllegalArgumentException.class, () -> messageService.createNewMessage(m));
    }
}
