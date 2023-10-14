package com.whatsapp.api.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class GlobleException {

	@ExceptionHandler(UserException.class)
	public ResponseEntity<ErrorDetail> UserExceptionHandler(UserException ue,WebRequest req)
	{
		ErrorDetail err=new ErrorDetail(ue.getMessage(), req.getDescription(false), LocalDateTime.now());
		return new ResponseEntity<ErrorDetail>(err,HttpStatus.BAD_REQUEST);
	}
	
	
	@ExceptionHandler(MessageException.class)
	public ResponseEntity<ErrorDetail> MessageExceptionHandler(MessageException ue,WebRequest req)
	{
		ErrorDetail err=new ErrorDetail(ue.getMessage(), req.getDescription(false), LocalDateTime.now());
		return new ResponseEntity<ErrorDetail>(err,HttpStatus.BAD_REQUEST);
	}
	
	
	@ExceptionHandler(ChatException.class)
	public ResponseEntity<ErrorDetail> chatExceptionHandler(ChatException ue,WebRequest req)
	{
		ErrorDetail err=new ErrorDetail(ue.getMessage(), req.getDescription(false), LocalDateTime.now());
		return new ResponseEntity<ErrorDetail>(err,HttpStatus.BAD_REQUEST);
	}
	
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorDetail> MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException me,WebRequest req)
	{
		String error=me.getBindingResult().getFieldError().getDefaultMessage();
		ErrorDetail err=new ErrorDetail("Validation Error", error, LocalDateTime.now());
		return new ResponseEntity<ErrorDetail>(err,HttpStatus.BAD_REQUEST);
	}
	
	
	@ExceptionHandler(NoHandlerFoundException.class)
	public ResponseEntity<ErrorDetail> NoHandlerFoundExceptionHandler(NoHandlerFoundException e,WebRequest req)
	{
		ErrorDetail err=new ErrorDetail("EndPoint not Found", e.getMessage(), LocalDateTime.now());
		return new ResponseEntity<ErrorDetail>(err,HttpStatus.BAD_REQUEST);
	}
	
	
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorDetail> otherExceptionHandler(Exception ue,WebRequest req)
	{
		ErrorDetail err=new ErrorDetail(ue.getMessage(), req.getDescription(false), LocalDateTime.now());
		return new ResponseEntity<ErrorDetail>(err,HttpStatus.BAD_REQUEST);
	}
	
	
	
}
