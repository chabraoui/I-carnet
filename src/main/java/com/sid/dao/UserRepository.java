package com.sid.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sid.entities.UserEntity;
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

	UserEntity findByEmail(String email);
	
	UserEntity findByUserId(String userId);
	
	@Query(value="SELECT * FROM users ", nativeQuery=true)
	Page<UserEntity> findAllUsers(Pageable pageableRequest);
}
