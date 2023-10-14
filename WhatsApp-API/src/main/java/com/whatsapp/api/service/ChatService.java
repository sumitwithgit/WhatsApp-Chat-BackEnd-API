package com.whatsapp.api.service;

import java.util.List;

import com.whatsapp.api.exception.ChatException;
import com.whatsapp.api.exception.UserException;
import com.whatsapp.api.modal.Chat;
import com.whatsapp.api.modal.User;
import com.whatsapp.api.request.GroupChatRequest;

public interface ChatService {

	public Chat createChat(User reqUser, Integer userId2) throws UserException;
	
	public Chat findChatById(Integer chatId) throws ChatException;
	
	public List<Chat> findAllChatByUserId(Integer userId) throws UserException, ChatException;
	
	public Chat createGroup(GroupChatRequest req, User reqUser) throws UserException;
	
	public Chat addUserToGroup(Integer userId, Integer chatId, User reqUser) throws UserException, ChatException;
	
	public Chat renameGroup(Integer chatId,String groupName, User reqUser) throws ChatException, UserException;
	
	public Chat removeFromGroup(Integer chatId,Integer userId, User reqUser) throws UserException, ChatException;
	
	public void deleteChat(Integer chatId,Integer userId);
}
