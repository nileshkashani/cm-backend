package com.clubmatrix.demo.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clubmatrix.demo.model.Club;
import com.clubmatrix.demo.model.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer>{
	List<Member> findByEmail(String email);
	List<Member> findByClub(Club club);
	List<Member> findByClub_Id(int clubId);

}
