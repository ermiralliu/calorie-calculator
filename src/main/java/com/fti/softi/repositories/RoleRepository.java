package com.fti.softi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.fti.softi.models.Role;

public interface RoleRepository extends JpaRepository<Role,Long>{
	Role findByName(String name);
}
