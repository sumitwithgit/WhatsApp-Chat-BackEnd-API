package com.whatsapp.api.service;

import java.util.List;

import com.whatsapp.api.exception.UserException;
import com.whatsapp.api.modal.User;
import com.whatsapp.api.request.UpdateUserRequest;

public interface UserService {

	public User findUserById(Integer id) throws UserException;
	
	public User findUserProfile(String jwt) throws UserException;
	
	public User updateUser(Integer userId, UpdateUserRequest req) throws UserException;
	
	public List<User> searchUser(String query);
}
