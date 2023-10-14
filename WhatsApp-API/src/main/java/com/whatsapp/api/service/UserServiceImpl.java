package com.whatsapp.api.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import com.whatsapp.api.config.TokenProvider;
import com.whatsapp.api.exception.UserException;
import com.whatsapp.api.modal.User;
import com.whatsapp.api.repository.UserRepository;
import com.whatsapp.api.request.UpdateUserRequest;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private TokenProvider tokenProvider;
	
	@Override
	public User findUserById(Integer id) throws UserException {
		Optional<User> optUser = this.userRepository.findById(id);
		
		if(optUser.isPresent()) {
			return optUser.get();
		}
		throw new UserException("User Not Found with id "+id);
	}

	@Override
	public User findUserProfile(String jwt) throws UserException {
		String email = this.tokenProvider.getEmailFromToken(jwt);
		if(email==null) {
			throw new BadCredentialsException("Invalid Token");
		}
		User user = this.userRepository.findByEmail(email);
		if(user==null) {
			throw new UserException("User not Found with email "+email);
		}
		return user;
	}

	@Override
	public User updateUser(Integer userId, UpdateUserRequest req) throws UserException {
		User user = findUserById(userId);
		if(req.getUsername()!=null) {
			user.setUsername(req.getUsername());
		}
		
		if(req.getProfile_picture()!=null) {
			user.setProfile_picture(req.getProfile_picture());
		}
		User saveUser = this.userRepository.save(user);
		return saveUser;
	}

	@Override
	public List<User> searchUser(String query){
		List<User> Users = this.userRepository.searchUser(query);
			return Users;
	}

}
