package hr.fer.progi.satcom.services.impl;

import hr.fer.progi.satcom.dao.MessageRepository;
import hr.fer.progi.satcom.models.Message;
import hr.fer.progi.satcom.services.MessageService;
import hr.fer.progi.satcom.services.exceptions.EntityMissingException;
import io.jsonwebtoken.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepo;
    @Autowired
    private UserServiceImpl userService;
    @Override
    public Set<Message> listAll(Long userId) {
        return userService.fetch(userId).getLogData();
    }

    @Override
    public Message fetch(Long messageId) {
        return findById(messageId).orElseThrow(
                () -> new EntityMissingException(Message.class, messageId)
        );
    }

    @Override
    public Optional<Message> findById(Long messageId) {
        Assert.notNull(messageId, "MessageId must be given");
        return messageRepo.findById(messageId);
    }


    public Message createNewMessage(Message message) {
        validate(message);

        return messageRepo.save(message);
    }

    private void validate(Message message) {
        Assert.notNull(message, "Message object must be given");
        Assert.isNull(message.getMessageId(),
                "Message ID must be null, not: " + message.getMessageId());
        Assert.notNull(message.getUserMessageObj(), "User that is sending message must be given");
        Assert.notNull(message.getText(), "Text must be given");
        Assert.notNull(message.getBaud(), "Baud must be given");
        Assert.notNull(message.getFreq(), "Freq must be given");
        Assert.notNull(message.getDirection(), "Direction must be given");
        Assert.notNull(message.getMode(), "Mode must be given");
        Assert.notNull(message.getSatelliteName(), "Satellite name must be given");
        Assert.notNull(message.getStationName(), "Station name must be given");
    }

    @Override
    public void deleteMessage(Long messageId) {
        messageRepo.deleteMessage(messageId);
    }

    // Iterable<Long> would be alternative to query call
    @Override
    public void deleteAllMessages(Long userId) {
        messageRepo.deleteAllMessages(userId);
        // messageRepo.deleteAllByIdInBatch();
    }
}

