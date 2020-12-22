package com.sid.services.impl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.sid.dao.UserRepository;
import com.sid.entities.UserEntity;
import com.sid.exception.UserException;
import com.sid.responses.AddressResponse;
import com.sid.services.UserServices;
import com.sid.shared.Utils;
import com.sid.shared.dto.AddressDto;
import com.sid.shared.dto.UserDto;
@Service
public class UserImplementation implements UserServices {
	@Autowired
	UserRepository userRepository;
	@Autowired
	Utils util;
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public UserDto saveUser(UserDto userDto) {
		UserEntity searchUserByEmail = userRepository.findByEmail(userDto.getEmail());
		if(searchUserByEmail !=null) throw new UserException("user deja exist");
				
//		for(int i=0; i < userDto.getAddresses().size(); i++) {
//			
//			AddressDto address = userDto.getAddresses().get(i);
//			address.setUser(userDto);
//			address.setAddressId(util.genereteStringId(30));
//			userDto.getAddresses().set(i, address);
//		}

		
		ModelMapper modelMapper = new ModelMapper();
		UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);
		userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
		userEntity.setUserId(util.genereteStringId(32));
		UserEntity userSave = userRepository.save(userEntity);
		UserDto savedto = modelMapper.map(userSave, UserDto.class);
		return savedto;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserEntity userEntity = userRepository.findByEmail(email);

		if (userEntity == null)
			throw new UsernameNotFoundException(email);

		return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
	}

	@Override
	public UserDto getUser(String email) {

		UserEntity userEntity = userRepository.findByEmail(email);
		if (userEntity == null) throw new UsernameNotFoundException(email);
		 ModelMapper modelMapper = new ModelMapper();
		 UserDto userDto = modelMapper.map(userEntity, UserDto.class);
		return userDto;
	}

	@Override
	public UserDto getUserByUserId(String userId) {
		UserEntity userEntity = userRepository.findByUserId(userId);
		if (userEntity == null) throw new UsernameNotFoundException(userId);
		 ModelMapper modelMapper = new ModelMapper();
		 UserDto userDto = modelMapper.map(userEntity, UserDto.class);
		return userDto;
	}

	@Override
	public UserDto updateUser(String userId, UserDto userDto, String email) {
		UserEntity userEntity = userRepository.findByUserId(userId);
		if (userEntity == null) throw new UsernameNotFoundException(userId);
		
		//email=principal
		UserEntity curentUser = userRepository.findByEmail(email);
		UserEntity userSaveEntity;
		if(curentUser.getAdmin()  || userEntity.getEmail() ==  curentUser.getEmail()) {
			
	
	
		userEntity.setFirstName(userDto.getFirstName());
		userEntity.setLastName(userDto.getLastName());
		if( userDto.getFirstName().isEmpty()) throw new RuntimeException("merci de remplir le champs firstName");
		if( userDto.getLastName().isEmpty()) throw new RuntimeException("merci de remplir le champs lasttName");
		userSaveEntity = userRepository.save(userEntity);
		}
		else {
			throw new UserException("vous n'etes pas admin pour modifier");
		}
		 ModelMapper modelMapper = new ModelMapper();
		 UserDto userDtoSave = modelMapper.map(userSaveEntity, UserDto.class);
		return userDtoSave;
	}

	@Override
	public void deleteUser(String userId, String email) {
		UserEntity userEntity = userRepository.findByUserId(userId);
		if (userEntity == null) throw new UsernameNotFoundException(userId);
		//email=principal
		UserEntity curentUser = userRepository.findByEmail(email);
		
		if(curentUser.getAdmin() == true) {
		userRepository.delete(userEntity);}
		else {
			throw new UserException("vous n'etes pas un admin pour supprimé");
		}

	}

	@Override
	public List<UserDto> getUsers(int page, int limit) {
		
		if(page > 0) page = page - 1;
		
		List<UserDto> userDto = new ArrayList<>();
		
		Pageable pageableRequest = PageRequest.of(page, limit);
		
		Page <UserEntity>userPage=userRepository.findAllUsers(pageableRequest);
		
		List<UserEntity> users = userPage.getContent();
		
		for(UserEntity userEntity:users) {
		
        ModelMapper modelMapper = new ModelMapper();
		
        UserDto user = modelMapper.map(userEntity, UserDto.class);
        
		userDto.add(user);
	}
		
		return userDto;
	}

	
	
	@Override
	public UserDto savePassword(String email, String newPasswors) {
		UserEntity userEntity= userRepository.findByEmail(email);
		if (userEntity == null) throw new UserException("email n'existe pas");
		userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(newPasswors));
		UserEntity savenewpass=userRepository.save(userEntity);
        ModelMapper modelMapper = new ModelMapper();
        UserDto user = modelMapper.map(savenewpass, UserDto.class);
        
		return user;
	}

}
