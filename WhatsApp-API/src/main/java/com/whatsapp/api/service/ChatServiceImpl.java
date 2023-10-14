package com.whatsapp.api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.whatsapp.api.exception.ChatException;
import com.whatsapp.api.exception.UserException;
import com.whatsapp.api.modal.Chat;
import com.whatsapp.api.modal.User;
import com.whatsapp.api.repository.ChatRepository;
import com.whatsapp.api.request.GroupChatRequest;

@Service
public class ChatServiceImpl implements ChatService {
	
	@Autowired
	private ChatRepository chatRepository;
	
	@Autowired
	private UserService userService;
	

	@Override
	public Chat createChat(User reqUser, Integer userId2) throws UserException {
		User user = this.userService.findUserById(userId2);
		
		Chat isChatExist = this.chatRepository.findSingleChatByUserIds(user, reqUser);
		
		if(isChatExist!=null) {
			return isChatExist;
		}
		Chat chat=new Chat();
		chat.setCreatedBy(reqUser);
		chat.getUsers().add(user);
		chat.getUsers().add(reqUser);
		chat.setGroup(false);
		return this.chatRepository.save(chat);
	}

	@Override
	public Chat findChatById(Integer chatId) throws ChatException {
		Optional<Chat> chatOpt = this.chatRepository.findById(chatId);
		if(chatOpt.isPresent()) {
			return chatOpt.get();
		}
		throw new ChatException("Chat not found with id "+chatId);
	}

	@Override
	public List<Chat> findAllChatByUserId(Integer userId) throws UserException, ChatException {
		User user = this.userService.findUserById(userId);
		if(user!=null) {
			List<Chat> chats = this.chatRepository.findChatByUserId(user.getId());
			if(chats.size()>0) {
				return chats;
			}
			throw new ChatException("No Chat Found");
		}
		throw new UserException("User not Found");
	}

	@Override
	public Chat createGroup(GroupChatRequest req, User reqUser) throws UserException {
		Chat group=new Chat();
		group.setChat_image(req.getChat_image());
		group.setChat_name(req.getChat_name());
		group.setCreatedBy(reqUser);
		group.getAdmins().add(reqUser);
		group.setGroup(true);
		for(Integer userId:req.getUserIds()) {
			User user = this.userService.findUserById(userId);
			group.getUsers().add(user);
		}
		return this.chatRepository.save(group);
	}

	@Override
	public Chat addUserToGroup(Integer userId, Integer chatId, User reqUser) throws UserException, ChatException {
		Optional<Chat> chatOpt = this.chatRepository.findById(chatId);
		
		User user = this.userService.findUserById(userId);
		
		if(chatOpt.isPresent()) {
			Chat chat = chatOpt.get();
			if(chat.getAdmins().contains(reqUser)) {
				chat.getUsers().add(user);
				return this.chatRepository.save(chat);
			}else {
				throw new UserException("You are not admin");
			}
		}
		throw new ChatException("Chat not found with id : "+chatId);
	}

	@Override
	public Chat renameGroup(Integer chatId, String groupName, User reqUser) throws ChatException, UserException {
		Optional<Chat> chatOpt = this.chatRepository.findById(chatId);
		if(chatOpt.isPresent()) {
			Chat chat = chatOpt.get();
			if(chat.getUsers().contains(reqUser)) {
				chat.setChat_name(groupName);
				return this.chatRepository.save(chat);
			}
			throw new UserException("You are not member of this Group");
		}
		throw new ChatException("Chat not Found");
	}

	@Override
	public Chat removeFromGroup(Integer chatId, Integer userId, User reqUser) throws UserException, ChatException {
		Optional<Chat> chatOpt = this.chatRepository.findById(chatId);
		User user = this.userService.findUserById(userId);
		if(chatOpt.isPresent()) {
			Chat chat = chatOpt.get();
			if(chat.getAdmins().contains(reqUser)) {
				chat.getUsers().remove(user);
				return this.chatRepository.save(chat);
			}else if(chat.getUsers().contains(reqUser)) {
				if(user.getId().equals(reqUser.getId())) {
					chat.getUsers().remove(user);
					return this.chatRepository.save(chat);
				}
			}
			throw new UserException("You are not admin");
		}
		throw new ChatException("Chat not found with id : "+chatId);
	}

	@Override
	public void deleteChat(Integer chatId, Integer userId) {
		Optional<Chat> chatOpt = this.chatRepository.findById(chatId);
		if(chatOpt.isPresent()) {
			Chat chat = chatOpt.get();
			this.chatRepository.deleteById(chat.getId());
		}
	}

}
