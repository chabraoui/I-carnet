package com.sid.services;

import java.util.List;

import com.sid.shared.dto.AddressDto;
import com.sid.shared.dto.UserDto;

public interface AddressService {
	List<AddressDto> getAllAddresses(String email);
	
	AddressDto createAddress(AddressDto address, String email);
	
    AddressDto getAddress(String addressId);
	
	void deleteAddress(String addressId);
	
	AddressDto updateAdresse(String addressId,AddressDto address, String email);
}
