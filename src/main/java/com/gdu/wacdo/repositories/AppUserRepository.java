package com.gdu.wacdo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gdu.wacdo.models.AppUser;

public interface AppUserRepository extends JpaRepository<AppUser, Long>{

	public AppUser findByEmail(String email);
;}
