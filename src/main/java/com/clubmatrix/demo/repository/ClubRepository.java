package com.clubmatrix.demo.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clubmatrix.demo.model.Club;

@Repository
public interface ClubRepository extends JpaRepository<Club, Integer>{
	Club findByLeaderId(int leaderId);
}

