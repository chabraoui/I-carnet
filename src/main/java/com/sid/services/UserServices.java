package com.sid.services;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.sid.shared.dto.UserDto;

public interface UserServices extends UserDetailsService {

	UserDto saveUser(UserDto userDto);
	
	 UserDto getUser(String email);
	 
	 UserDto getUserByUserId(String userId);
	 
	 UserDto updateUser(String userId, UserDto userDto , String email);
	 
	 void deleteUser(String userId, String email);
	 
	 List<UserDto> getUsers(int page, int limit);
	 
	UserDto savePassword(String email,String newPasswors);
}
