package com.sid.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sid.entities.AddressEntity;
import com.sid.entities.UserEntity;

@Repository
public interface AddressRepository extends JpaRepository<AddressEntity, Long> {
	List<AddressEntity> findByUser(UserEntity currentUser);

	AddressEntity findByAddressId(String addressId);
}
