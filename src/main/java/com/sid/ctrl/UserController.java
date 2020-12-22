package com.sid.ctrl;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sid.dao.UserRepository;
import com.sid.exception.UserException;
import com.sid.request.ForgetPasswordRequest;
import com.sid.request.UserRequest;
import com.sid.responses.ErrorMessages;
import com.sid.responses.UserResponse;
import com.sid.services.UserServices;
import com.sid.shared.dto.UserDto;
@CrossOrigin(origins="*")
@RestController
@RequestMapping("/users")
public class UserController {
	@Autowired
	UserRepository userRepository;
	@Autowired
	UserServices userService;
	
	@CrossOrigin(origins="*")
	@PostMapping
	public UserResponse createUser(@RequestBody @Valid UserRequest userRequest) {
		if(userRequest.getFirstName().isEmpty() ||
				userRequest.getLastName().isEmpty() ||
				userRequest.getEmail().isEmpty() ||
				userRequest.getPassword().isEmpty() ) 
			throw new UserException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
		
		ModelMapper modelMapper = new ModelMapper();
		UserDto userDto = modelMapper.map(userRequest, UserDto.class);
		UserDto createUserDto = userService.saveUser(userDto);
		UserResponse userresponse = modelMapper.map(createUserDto, UserResponse.class);
		return userresponse;
	}
	
	
	@GetMapping(path = "/{userId}")
	public  UserResponse getOneUser(@PathVariable String userId) {
		UserDto userDto = userService.getUserByUserId(userId);
		ModelMapper modelMapper = new ModelMapper();
		UserResponse userresponse = modelMapper.map(userDto, UserResponse.class);
		return userresponse;
	}
	
	@PatchMapping(path = "/{userId}")
	public UserResponse updateUser(@PathVariable (required=true) String userId,  @RequestBody  UserRequest userRequest , Principal principal) {
		 ModelMapper modelMapper = new ModelMapper();
		 UserDto userDto = modelMapper.map(userRequest, UserDto.class);
		UserDto userUpdate = userService.updateUser(userId, userDto,principal.getName());
		UserResponse userresponse = modelMapper.map(userUpdate, UserResponse.class);
		return userresponse;
	}
	
	@DeleteMapping(path = "/{userId}")
	public void deleteUser(@PathVariable (required=true) String userId , Principal principal) {
		userService.deleteUser(userId, principal.getName());
	}
	
	@GetMapping
	public List<UserResponse> getAllUsers(
			@RequestParam(value="page", defaultValue = "1") int page,
			@RequestParam(value="limit", defaultValue = "4")  int limit){
		
		List<UserResponse> usersResponse=new ArrayList<>();
		
		List<UserDto> users = userService.getUsers(page, limit);
		
		for(UserDto userDto:users) {

		      ModelMapper modelMapper = new ModelMapper();
		      UserResponse user = modelMapper.map(userDto, UserResponse.class);
		      
			usersResponse.add(user);
		}
		return usersResponse;
	}
	
	@PostMapping(path = "/forgetpassword")
	public UserResponse saveNewPassword(@RequestBody @Valid ForgetPasswordRequest forgetPasswordRequest) {
		String email =forgetPasswordRequest.getEmail();
		String pass=forgetPasswordRequest.getPassword();
		UserDto userdto=userService.savePassword(email, pass);
	      ModelMapper modelMapper = new ModelMapper();
	      UserResponse user = modelMapper.map(userdto, UserResponse.class);
		return user;
	}

}

