package com.whatsapp.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.whatsapp.api.exception.ChatException;
import com.whatsapp.api.exception.MessageException;
import com.whatsapp.api.exception.UserException;
import com.whatsapp.api.modal.Message;
import com.whatsapp.api.modal.User;
import com.whatsapp.api.request.SendMessageRequest;
import com.whatsapp.api.response.ApiResponse;
import com.whatsapp.api.service.MessageService;
import com.whatsapp.api.service.UserService;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

	@Autowired
	private MessageService messageService;
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/create")
	public ResponseEntity<Message> sendMessageHandler(@RequestBody SendMessageRequest req, @RequestHeader("Authorization") String jwt) throws UserException, ChatException{
		User user = this.userService.findUserProfile(jwt);
		req.setUserId(user.getId());
		Message sendMessage = this.messageService.sendMessage(req);
		return new ResponseEntity<Message>(sendMessage,HttpStatus.OK);
	}
	
	@GetMapping("/chat/{chatId}")
	public ResponseEntity<List<Message>> getChatMessageHandler(@PathVariable("chatId") int chatId, @RequestHeader("Authorization") String jwt) throws UserException, ChatException, MessageException{
		User user = this.userService.findUserProfile(jwt);
		List<Message> messages = this.messageService.getChatMessages(chatId, user);
		return new ResponseEntity<List<Message>>(messages,HttpStatus.OK);
		
	}
	
	@DeleteMapping("/{messageId}")
	public ResponseEntity<ApiResponse> deleteMessageHandler(@PathVariable("messageId") int messageId, @RequestHeader("Authorization") String jwt) throws UserException, ChatException, MessageException{
		User user = this.userService.findUserProfile(jwt);
		this.messageService.deleteMessage(messageId, user);
		ApiResponse res=new ApiResponse();
		res.setMessage("Message deleted Successfully");
		res.setStatus(true);
		
		return new ResponseEntity<ApiResponse>(res,HttpStatus.OK);
		
	}
}
