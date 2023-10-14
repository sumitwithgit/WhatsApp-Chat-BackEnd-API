package com.whatsapp.api.service;

import java.util.List;

import com.whatsapp.api.exception.ChatException;
import com.whatsapp.api.exception.MessageException;
import com.whatsapp.api.exception.UserException;
import com.whatsapp.api.modal.Message;
import com.whatsapp.api.modal.User;
import com.whatsapp.api.request.SendMessageRequest;

public interface MessageService {

	public Message sendMessage(SendMessageRequest req) throws UserException, ChatException;
	
	public List<Message> getChatMessages(Integer chatId, User reqUser) throws ChatException, MessageException, UserException;
	
	public Message findMessageById(Integer messageId) throws MessageException;
	
	public void deleteMessage(Integer messageId, User reqUser) throws MessageException, UserException;
	
	
}
