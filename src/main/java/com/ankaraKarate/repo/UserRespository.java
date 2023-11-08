package com.ankaraKarate.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ankaraKarate.entity.UserEntity;


public interface UserRespository extends JpaRepository<UserEntity, Integer> {

	Optional<UserEntity> findByUsername(String username);

	Optional<UserEntity> findByEmail(String email);

}
