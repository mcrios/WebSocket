package com.datasoft.chat.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import com.datasoft.chat.model.ChatMessage;

@Controller
public class ChatController {

	@MessageMapping("/chat/{room}/register")
	@SendTo("/topic/{room}")
	public ChatMessage register(@DestinationVariable String room, @Payload ChatMessage chatMessage, 
			SimpMessageHeaderAccessor headerAccessor) {
		headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
		headerAccessor.getSessionAttributes().put("room", room);
		return chatMessage;
	}
	
	@MessageMapping("/chat/{room}/send")
	@SendTo("/topic/{room}")
	public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
		return chatMessage;
	}

}
