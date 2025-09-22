package com.clubmatrix.demo.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clubmatrix.demo.model.JoinRequest;
import com.clubmatrix.demo.model.Club;

@Repository
public interface JoinRequestRepository extends JpaRepository<JoinRequest, Integer>{
	List<JoinRequest> findByClub(Club club);
}
