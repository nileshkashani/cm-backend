package com.clubmatrix.demo.controller;
import com.clubmatrix.demo.model.Club;
import com.clubmatrix.demo.model.Member;
import com.clubmatrix.demo.repository.ClubRepository;
import com.clubmatrix.demo.repository.MemberRepository;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/club")
public class ClubController {
	@Autowired
	private ClubRepository clubRepository;
	
	@Autowired
	private MemberRepository memberRepository;
	
	@PostMapping("/add/{leaderId}")
	public ResponseEntity<?> addClub(@RequestBody Club club){
		try {			
			return ResponseEntity.ok(Map.of(
					"status", true, 
					"message", "club added successfully!",
					"data", clubRepository.save(club))); 
		}
		catch(Exception e) {
			return  ResponseEntity.badRequest().body(Map.of(
					"status", false, 
					"message", "error: "+ e.getMessage())); 
		}
	}
	@GetMapping("/getall")
	public List<Club> getAllClubs(){
		return clubRepository.findAll();
	}
	
	@GetMapping("/get/by/id/{clubId}")
	public ResponseEntity<?> getClubById(@PathVariable Integer clubId){
		 return ResponseEntity.ok(Map.of(
	                "status", true,
	                "message", "club found",
	                "data", clubRepository.findById(clubId)
	        ));
	}

	@GetMapping("/get/by/leader/{leaderId}")
	public ResponseEntity<?> getByLeader(@PathVariable Integer leaderId) {
	    Club club = clubRepository.findByLeaderId(leaderId);
	    return ResponseEntity.ok(club);
	}
	@GetMapping("/members/leader/{leaderId}")
	public ResponseEntity<?> getMembersByLeader(@PathVariable int leaderId) {
	    try {
	        // 1️⃣ Find the club led by this leader
	    	// If single club per leader 
	    	Club club = clubRepository.findByLeaderId(leaderId);
	    	

	        // 2️⃣ Fetch members of the club
	        List<Member> members = memberRepository.findByClub(club);

	        return ResponseEntity.ok(Map.of(
	                "status", true,
	                "message", "Members fetched successfully",
	                "data", members
	        ));
	    } catch (Exception e) {
	        return ResponseEntity.badRequest().body(Map.of(
	                "status", false,
	                "message", "Error: " + e.getMessage()
	        ));
	    }
	}
	
}
