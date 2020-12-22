package com.sid.services.impl;

import java.lang.reflect.Type;
import java.util.List;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sid.dao.AddressRepository;
import com.sid.dao.UserRepository;
import com.sid.entities.AddressEntity;
import com.sid.entities.UserEntity;
import com.sid.exception.UserException;
import com.sid.services.AddressService;
import com.sid.shared.Utils;
import com.sid.shared.dto.AddressDto;
import com.sid.shared.dto.UserDto;
@Service
public class AddressImplementation implements AddressService {
	@Autowired
	AddressRepository addressRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	Utils util;

	@Override
	public List<AddressDto> getAllAddresses(String email) {
		
	    UserEntity userCurent= userRepository.findByEmail(email);
	    List<AddressEntity> addressEntity;
	    if(userCurent.getAdmin()) 
	    {
	    addressEntity=	addressRepository.findAll();
	    }
	    else {
	     addressEntity=	addressRepository.findByUser(userCurent);
	    }
	
	Type listType = new TypeToken<List<AddressDto>>() {}.getType();
	
	List<AddressDto> addressDto = new ModelMapper().map(addressEntity, listType);
		return addressDto;
	}

	@Override
	public AddressDto createAddress(AddressDto address, String email) {
		
		   UserEntity userCurent= userRepository.findByEmail(email);
		   
			ModelMapper modelMapper = new ModelMapper();
			
			UserDto userDto = modelMapper.map(userCurent, UserDto.class);
			
			address.setAddressId(util.genereteStringId(30));
			address.setUser(userDto);
			
			AddressEntity addressEntity = modelMapper.map(address, AddressEntity.class);
			
			AddressEntity addressEntitySave = addressRepository.save(addressEntity);
		   
		   AddressDto addressDto = modelMapper.map(addressEntitySave, AddressDto.class);
		   
		return addressDto;
	}

	@Override
	public AddressDto getAddress(String addressId) {
		AddressEntity addressEntity=addressRepository.findByAddressId(addressId);
		 ModelMapper modelMapper = new ModelMapper();
		 AddressDto addressDto = modelMapper.map(addressEntity, AddressDto.class);
		return addressDto;
	}


	@Override
	public AddressDto updateAdresse(String addressId, AddressDto address , String email) {
		
		   UserEntity userCurent= userRepository.findByEmail(email);

		 AddressEntity addresses= addressRepository.findByAddressId(addressId);
		 if(addresses == null) throw new RuntimeException("Address not found");
		   if (userCurent.getAdmin() || addresses.getUser().getEmail() ==  userCurent.getEmail()) {
				 
				 addresses.setCity(address.getCity());
				 addresses.setStreet(address.getStreet());
				 addresses.setType(address.getType());
				 addresses.setCountry(address.getCountry());
				 AddressEntity saveadresse = addressRepository.save(addresses);
		   }
		   else {
				throw new UserException("vous n'etes ni admin ni l'addresse vous correspend pour modifier");
		   }
 
		ModelMapper modelMapper = new ModelMapper();
		AddressDto adressDto = modelMapper.map(addresses, AddressDto.class);
		
		return adressDto;
	}

	@Override
	public void deleteAddress(String addressId) {
		AddressEntity address = addressRepository.findByAddressId(addressId);
		
		if(address == null) throw new RuntimeException("Address not found");

			 addressRepository.delete(address);

	}
}
