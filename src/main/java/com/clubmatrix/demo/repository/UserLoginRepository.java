package com.clubmatrix.demo.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clubmatrix.demo.model.UserLogin;

@Repository
public interface UserLoginRepository extends JpaRepository<UserLogin, Integer>{
	 UserLogin findByEmail(String email);
	 UserLogin findByPhoneNo(String phoneNo);
}
