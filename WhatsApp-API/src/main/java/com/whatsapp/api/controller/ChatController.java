package com.whatsapp.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.whatsapp.api.exception.ChatException;
import com.whatsapp.api.exception.UserException;
import com.whatsapp.api.modal.Chat;
import com.whatsapp.api.modal.User;
import com.whatsapp.api.request.GroupChatRequest;
import com.whatsapp.api.request.SingleChatRequest;
import com.whatsapp.api.response.ApiResponse;
import com.whatsapp.api.service.ChatService;
import com.whatsapp.api.service.UserService;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

	@Autowired
	private ChatService chatService;
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/single")
	public ResponseEntity<Chat> createChatHandler(@RequestBody SingleChatRequest singlechatRequest, @RequestHeader("Authorization") String jwt) throws UserException{
		User reqUser = this.userService.findUserProfile(jwt);
		Chat createChat = this.chatService.createChat(reqUser, singlechatRequest.getUserId());
		return new ResponseEntity<Chat>(createChat,HttpStatus.OK);
	}
	
	
	@PostMapping("/group")
	public ResponseEntity<Chat> createGroupChatHandler(@RequestBody GroupChatRequest groupchatRequest, @RequestHeader("Authorization") String jwt) throws UserException{
		User reqUser = this.userService.findUserProfile(jwt);
		Chat createGroupChat = this.chatService.createGroup(groupchatRequest, reqUser);
		return new ResponseEntity<Chat>(createGroupChat,HttpStatus.OK);
	}
	
	
	@GetMapping("/{chatId}")
	public ResponseEntity<Chat> findChatByIdHandler(@PathVariable("chatId") Integer chatId, @RequestHeader("Authorization") String jwt) throws UserException, ChatException{
		Chat chat = this.chatService.findChatById(chatId);
		return new ResponseEntity<Chat>(chat,HttpStatus.OK);
	}
	
	@GetMapping("/user")
	public ResponseEntity<List<Chat>> findAllChatByIdsHandler(@RequestHeader("Authorization") String jwt) throws UserException, ChatException{
		User reqUser = this.userService.findUserProfile(jwt);
		List<Chat> chats = this.chatService.findAllChatByUserId(reqUser.getId());
		return new ResponseEntity<List<Chat>>(chats,HttpStatus.OK);
	}
	

	@PutMapping("/{chatId}/add/{userId}")
	public ResponseEntity<Chat> addNewUserToGroupHandler(@PathVariable("chatId") Integer chatId,@PathVariable("userId") Integer userId, @RequestHeader("Authorization") String jwt) throws UserException, ChatException{
		User reqUser = this.userService.findUserProfile(jwt);
		Chat chat = this.chatService.addUserToGroup(userId, chatId, reqUser);
		return new ResponseEntity<Chat>(chat,HttpStatus.OK);
	}
	
	
	@PutMapping("/{chatId}/remove/{userId}")
	public ResponseEntity<Chat> removeExistingUserFromGroupHandler(@PathVariable("chatId") Integer chatId,@PathVariable("userId") Integer userId, @RequestHeader("Authorization") String jwt) throws UserException, ChatException{
		User reqUser = this.userService.findUserProfile(jwt);
		Chat chat = this.chatService.removeFromGroup(userId, chatId, reqUser);
		return new ResponseEntity<Chat>(chat,HttpStatus.OK);
	}
	
	
	@DeleteMapping("/delete/{chatId}")
	public ResponseEntity<ApiResponse> deleteChatHandler(@PathVariable("chatId") Integer chatId,@RequestHeader("Authorization") String jwt) throws UserException, ChatException{
		User reqUser = this.userService.findUserProfile(jwt);
		this.chatService.deleteChat(chatId, reqUser.getId());
		ApiResponse res=new ApiResponse();
		res.setMessage("Chat successfully Deleted");
		res.setStatus(true);
		return new ResponseEntity<ApiResponse>(res,HttpStatus.OK);
	}
	
	
	
}
