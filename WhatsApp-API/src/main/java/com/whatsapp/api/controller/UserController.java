package com.whatsapp.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.whatsapp.api.exception.UserException;
import com.whatsapp.api.modal.User;
import com.whatsapp.api.request.UpdateUserRequest;
import com.whatsapp.api.response.ApiResponse;
import com.whatsapp.api.service.UserService;

@RestController
@RequestMapping("/api/users/")
public class UserController {

	@Autowired
	private UserService userService;
	
	@GetMapping("/profile")
	public ResponseEntity<User> getUserProfileHandler(@RequestHeader("Authorization") String token) throws UserException{
		User user = this.userService.findUserProfile(token);
		return new ResponseEntity<User>(user,HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/search/{query}")
	public ResponseEntity<List<User>> searchUserHandler(@PathVariable("query") String query){
		List<User> searchUsers = this.userService.searchUser(query);
		return new ResponseEntity<List<User>>(searchUsers,HttpStatus.OK);
	}
	
	@PutMapping("/update")
	public ResponseEntity<ApiResponse> updateUserHandler(@RequestBody UpdateUserRequest req, @RequestHeader("Authorization") String token) throws UserException{
		User user = this.userService.findUserProfile(token);
		
		this.userService.updateUser(user.getId(), req);
		
		ApiResponse res=new ApiResponse();
		res.setMessage("User Updated Successfully.");
		res.setStatus(true);
		return new ResponseEntity<ApiResponse>(res,HttpStatus.OK);
	}
	
}
