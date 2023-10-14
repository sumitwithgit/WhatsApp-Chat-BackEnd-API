package com.whatsapp.api.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whatsapp.api.exception.ChatException;
import com.whatsapp.api.exception.MessageException;
import com.whatsapp.api.exception.UserException;
import com.whatsapp.api.modal.Chat;
import com.whatsapp.api.modal.Message;
import com.whatsapp.api.modal.User;
import com.whatsapp.api.repository.MessageRepository;
import com.whatsapp.api.request.SendMessageRequest;


@Service
public class MessageServiceImpl implements MessageService{

	@Autowired
	private MessageRepository messageRepository;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ChatService chatService;
	
	@Override
	public Message sendMessage(SendMessageRequest req) throws UserException, ChatException {
		User user = this.userService.findUserById(req.getUserId());
		Chat chat = this.chatService.findChatById(req.getChatId());
		
		Message msg=new Message();
		msg.setChat(chat);
		msg.setUser(user);
		msg.setContent(req.getContent());
		msg.setTimeStamp(LocalDateTime.now());
		return this.messageRepository.save(msg);
	}

	@Override
	public List<Message> getChatMessages(Integer chatId, User reqUser) throws ChatException, MessageException, UserException {
		Chat chat = this.chatService.findChatById(chatId);
		if(!chat.getUsers().contains(reqUser)) {
			throw new UserException("You are not related to this chat");
		}
		List<Message> messages = this.messageRepository.findByChatId(chat.getId());
		return messages;
	}

	@Override
	public Message findMessageById(Integer messageId) throws MessageException {
		Optional<Message> opt = this.messageRepository.findById(messageId);
		if(opt.isPresent()) {
			return opt.get();
		}
		throw new MessageException("Message not found with id : "+messageId);
	}

	@Override
	public void deleteMessage(Integer messageId, User reqUser) throws MessageException, UserException {
		Message message = findMessageById(messageId);
		if(message.getUser().getId().equals(reqUser.getId())) {
			this.messageRepository.deleteById(messageId);
		}
		throw new UserException("You cann't delete another user's message");
	}

}
