package com.fti.softi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.fti.softi.models.User;

public interface UserRepository extends JpaRepository<User, Long>{
	public User findByEmail(String email);
}
