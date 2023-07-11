package org.openapitools.repository;

import java.util.Optional;

import org.openapitools.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findUserByUserName(String userName);
}