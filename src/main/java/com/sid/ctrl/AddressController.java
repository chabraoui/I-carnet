package com.sid.ctrl;

import java.lang.reflect.Type;
import java.security.Principal;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sid.request.AddressRequest;
import com.sid.responses.AddressResponse;
import com.sid.services.AddressService;
import com.sid.shared.dto.AddressDto;

@RestController
@RequestMapping("/addresses")
public class AddressController {

	@Autowired
	AddressService addressService;

	@GetMapping
	public List<AddressResponse> getAllAddress(Principal principal) {
		List<AddressDto> addressDto = addressService.getAllAddresses(principal.getName());
		Type listType = new TypeToken<List<AddressResponse>>() {
		}.getType();
		List<AddressResponse> addressesResponse = new ModelMapper().map(addressDto, listType);
		return addressesResponse;
	}

	@GetMapping(path = "/{addressId}")
	public AddressResponse getOneAddress(@PathVariable String addressId) {
		AddressDto addressDto = addressService.getAddress(addressId);
		ModelMapper modelMapper = new ModelMapper();
		AddressResponse addressResponse = modelMapper.map(addressDto, AddressResponse.class);
		return addressResponse;
	}

	@PostMapping
	public AddressResponse createAddress(@RequestBody AddressRequest addressRequest, Principal principal) {
		ModelMapper modelMapper = new ModelMapper();
		AddressDto addressDto = modelMapper.map(addressRequest, AddressDto.class);
		AddressDto addressDtoSave = addressService.createAddress(addressDto, principal.getName());
		AddressResponse addressResponse = modelMapper.map(addressDtoSave, AddressResponse.class);
		return addressResponse;
	}
	
	@PatchMapping(path = "/{addressId}")
	public AddressResponse updateAddress(@PathVariable String addressId,@RequestBody AddressRequest addressRequest, Principal principal) {
		ModelMapper modelMapper = new ModelMapper();
		AddressDto addressDto = modelMapper.map(addressRequest, AddressDto.class);
		AddressDto addressDtoSave=addressService.updateAdresse(addressId, addressDto, principal.getName());
		AddressResponse addressResponse = modelMapper.map(addressDtoSave, AddressResponse.class);
		return addressResponse;
	}
	
	@DeleteMapping(path = "/{addressId}")
		public void deleteAddress(@PathVariable String addressId) {
			addressService.deleteAddress(addressId);
		}
	
}
