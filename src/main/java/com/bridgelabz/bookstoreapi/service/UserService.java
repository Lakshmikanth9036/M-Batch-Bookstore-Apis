package com.bridgelabz.bookstoreapi.service;

import javax.validation.Valid;

import com.bridgelabz.bookstoreapi.dto.LoginDTO;
import com.bridgelabz.bookstoreapi.dto.RegisterDto;
import com.bridgelabz.bookstoreapi.entity.User;
import com.bridgelabz.bookstoreapi.response.Response;
import com.bridgelabz.bookstoreapi.response.UserResponse;

public interface UserService {

	User userRegistration(RegisterDto register);
	
	UserResponse loginByEmailOrMobile(LoginDTO login);

	boolean updateVerificationStatus(String token);

	
}
