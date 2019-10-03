package com.datasoft.chat.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.datasoft.chat.model.ChatMessage;
import com.datasoft.chat.model.ChatMessage.MessageType;


@Component
public class WebSocketListener {

	private static final Logger logger = LoggerFactory.getLogger(WebSocketListener.class);

	@Autowired
	private SimpMessageSendingOperations messagingTemplate;

	@EventListener
	public void handleWebSocketConnectListener(SessionConnectEvent event) {
		logger.info("Un usuario se ha unido ");
	}

	@EventListener
	public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

		String username = (String) headerAccessor.getSessionAttributes().get("username");
		String roomId = (String) headerAccessor.getSessionAttributes().get("room");
		if (username != null) {
			logger.info(username + " Abandono el chat " + roomId);

			ChatMessage chatMessage = new ChatMessage();
			chatMessage.setType(MessageType.LEAVE);
			chatMessage.setSender(username);

			messagingTemplate.convertAndSend("/topic/" + roomId, chatMessage);
		}
	}
}
