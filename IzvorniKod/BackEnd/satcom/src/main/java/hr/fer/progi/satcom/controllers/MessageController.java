package hr.fer.progi.satcom.controllers;

import hr.fer.progi.satcom.models.Message;
import hr.fer.progi.satcom.models.User;
import hr.fer.progi.satcom.security.user_security_context.SecurityUserDetails;
import hr.fer.progi.satcom.services.impl.MessageServiceImpl;
import hr.fer.progi.satcom.services.impl.UserServiceImpl;
import hr.fer.progi.satcom.utils.SatelliteConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Set;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/messages")
public class MessageController {

    @Autowired
    private MessageServiceImpl messageService;

    @Autowired
    private UserServiceImpl userService;

    @GetMapping("")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('SATELLITE_ADMIN') or hasAuthority('SUPER_ADMIN')")
    public Set<Message> listMessages() {
        SecurityUserDetails userDetails = (SecurityUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userService.fetch(userDetails.getId()).getLogData();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('SATELLITE_ADMIN') or hasAuthority('SUPER_ADMIN')")
    public Message getMessage( @PathVariable("id") Long messageId) {

        SecurityUserDetails userDetails = (SecurityUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Message message = messageService.fetch(messageId);

        if(message.getUserMessageObj().getUserId() == userDetails.getId()) {
            return message;
        } else {
            throw new IllegalArgumentException("You are not this message's owner");
        }
    }

    @PostMapping("")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('SATELLITE_ADMIN') or hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        SecurityUserDetails userDetails = (SecurityUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        message.setUserMessage(userService.fetch(userDetails.getId()));
        message.setCreationDate(LocalDateTime.now());
        message.setDirection("UPLOAD");

        // konekcija prema satelitu i slanje poruke:
        SatelliteConnector connector = new SatelliteConnector();
        System.out.println("stvoren connector");
        // vracena poruka od satelita:
        Message receivedMessage = connector.sendMessage(message);

        Message saved;
        if(receivedMessage != null) {

            saved = messageService.createNewMessage(message);
            System.out.println(message);
            receivedMessage.setUserMessage(userService.fetch(userDetails.getId()));
            messageService.createNewMessage(receivedMessage);
        } else {
            message.setDirection("FAILED UPLOAD");
            saved = messageService.createNewMessage(message);
        }
        return ResponseEntity.created(URI.create("/messages")).body(saved);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('SATELLITE_ADMIN') or hasAuthority('SUPER_ADMIN')")
    public Message deleteMessage(@PathVariable("id") Long messageId) {
        SecurityUserDetails userDetails = (SecurityUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Message message = messageService.fetch(messageId);

        if(message.getUserMessageObj().getUserId() == userDetails.getId()) {
            messageService.deleteMessage(messageId);
            return message;
        } else {
            throw new IllegalArgumentException("You are not this message's owner");
        }
    }

    @DeleteMapping("")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('SATELLITE_ADMIN') or hasAuthority('SUPER_ADMIN')")
    public Set<Message> deleteMessages() {
        SecurityUserDetails userDetails = (SecurityUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.fetch(userDetails.getId());

        Set<Message> messages = user.getLogData();
        // Iterable<Long> messageIds = messages.stream().map(message -> message.getMessageId()).collect(Collectors.toList()); -> could be used to pass Iterable<Long> object to delete method
        messageService.deleteAllMessages(userDetails.getId());
        return messages;
    }
}



