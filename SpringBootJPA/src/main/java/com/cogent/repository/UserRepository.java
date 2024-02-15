package com.cogent.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cogent.entity.NameEntity;
import com.cogent.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

	@Query(value = "from UserEntity where name.fname = :fname")
	Optional<List<UserEntity>> findByFname(String fname);

	@Query(value = "from UserEntity where name.fname like %:fname%")
	List<UserEntity> findByNameKeyword(String fname);

	@Query(value = "from UserEntity where email = :email and password = :password")
	Optional<UserEntity> findByEmailandPassword(String email, String password);

	Optional<UserEntity> findByEmail(String email);

}
