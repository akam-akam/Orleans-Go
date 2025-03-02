
package com.orleansgo.support.controller;

import com.orleansgo.support.dto.MessageDTO;
import com.orleansgo.support.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets/{ticketId}/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @GetMapping
    public ResponseEntity<List<MessageDTO>> getMessagesByTicketId(@PathVariable Long ticketId) {
        return ResponseEntity.ok(messageService.getMessagesByTicketId(ticketId));
    }

    @PostMapping
    public ResponseEntity<MessageDTO> addMessage(@PathVariable Long ticketId, @RequestBody MessageDTO messageDTO) {
        return new ResponseEntity<>(messageService.addMessage(ticketId, messageDTO), HttpStatus.CREATED);
    }
}
