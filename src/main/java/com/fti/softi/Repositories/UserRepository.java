package com.fti.softi.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.fti.softi.Models.User;

public interface UserRepository extends JpaRepository<User, Long>{
	public User findByEmail(String email);
}
