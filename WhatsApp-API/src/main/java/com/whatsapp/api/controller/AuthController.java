package com.whatsapp.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.whatsapp.api.config.TokenProvider;
import com.whatsapp.api.exception.UserException;
import com.whatsapp.api.modal.User;
import com.whatsapp.api.repository.UserRepository;
import com.whatsapp.api.request.LoginRequest;
import com.whatsapp.api.response.AuthResponse;
import com.whatsapp.api.service.CustomUserService;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private TokenProvider tokenProvider;
	
	@Autowired
	private CustomUserService customUserService;
	
	
	@PostMapping("/signup")
	public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws UserException{
		
		User isUser = this.userRepository.findByEmail(user.getEmail());
		if(isUser!=null) {
			throw new UserException("User Already Exist with email "+user.getEmail());
		}
		User newUser=new User();
		newUser.setEmail(user.getEmail());
		newUser.setProfile_picture(user.getProfile_picture());
		newUser.setUsername(user.getUsername());
		newUser.setPassword(passwordEncoder.encode(user.getPassword()));
		
		this.userRepository.save(newUser);
		Authentication authentication=new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		String jwt = this.tokenProvider.generateToken(authentication);
		
		AuthResponse res=new AuthResponse();
		res.setAuth(true);
		res.setJwt(jwt);
		return new ResponseEntity<AuthResponse>(res,HttpStatus.ACCEPTED);
	}
	
	
	@PostMapping("/signin")
	public ResponseEntity<AuthResponse> loginHandler(@RequestBody LoginRequest req){
		String email=req.getEmail();
		String password=req.getPassword();
		
		Authentication authentication=authenticate(email, password);
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		String jwt = tokenProvider.generateToken(authentication);
		
		AuthResponse res=new AuthResponse();
		res.setAuth(true);
		res.setJwt(jwt);
		
		return new ResponseEntity<AuthResponse>(res,HttpStatus.ACCEPTED);
	}
	
	public Authentication authenticate(String username,String password)
	{
		UserDetails userDetails=this.customUserService.loadUserByUsername(username);
		
		if(userDetails==null) {
			throw new BadCredentialsException("Invalid UserName");
		}
		
		if(!passwordEncoder.matches(password, userDetails.getPassword())) {
			throw new BadCredentialsException("Incorrect Password.");
		}
		
		return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	}
	
	
	
}
