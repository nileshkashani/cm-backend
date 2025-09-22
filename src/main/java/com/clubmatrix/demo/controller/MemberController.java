package com.clubmatrix.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.clubmatrix.demo.model.Club;
import com.clubmatrix.demo.model.Member;
import com.clubmatrix.demo.model.UserLogin;
import com.clubmatrix.demo.repository.ClubRepository;
import com.clubmatrix.demo.repository.MemberRepository;

@RestController
@RequestMapping("/member")
public class MemberController {
	@Autowired
	private MemberRepository memberRepository;

	@Autowired
	private ClubRepository clubRepository;

	@PostMapping("/add/{clubId}")
	public Member addMember(@PathVariable int clubId, @RequestBody UserLogin user) {
		Club club = clubRepository.findById(clubId).orElseThrow(() -> new RuntimeException("Club not found"));
		Member member = new Member();
		member.setAddress(user.getAddress());
		member.setMemberName(user.getName());
		member.setEmail(user.getEmail());
		member.setMemberMobile(user.getPhoneNo());
		member.setClub(club);
		return memberRepository.save(member);
	}

	@GetMapping("/email")
	public ResponseEntity<?> getMemberByEmail(@RequestParam(required = false) String email) {
		if (email == null || email.isEmpty()) {
			return ResponseEntity.badRequest().body(Map.of(
					"success", false,
					"message", "Missing required request parameter 'email'"));
		}
		System.out.println(email);
		try {
			List<Member> member = memberRepository.findByEmail(email); // Use local variable, not class field
			if (member == null || member.isEmpty()) {
				return ResponseEntity.status(404).body(Map.of(
						"success", false,
						"message", "failed to find member with this email!"));
			}

			return ResponseEntity.ok(Map.of(
					"success", true,
					"message", "user found",
					 "data", member));
		} catch (Exception e) {
			return ResponseEntity.status(500).body(Map.of(
					"success", false,
					"message", "error: " + e.getMessage()));
		}
	}
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteById(@PathVariable Integer id){
		try {			
			memberRepository.deleteById(id);
			return ResponseEntity.ok(Map.of(
					"status", true,
					"message", "member deleted successfully!"));
		}
		catch(Exception e){
			return ResponseEntity.badRequest().body(Map.of(
					"status", false,
					"message", "error: "+ e.getMessage()));
		}
	}
	
	@GetMapping("/club/{clubId}")
	public ResponseEntity<?> getMembersByClubId(@PathVariable int clubId) {
	    try {
	        List<Member> members = memberRepository.findByClub_Id(clubId);
	        return ResponseEntity.ok(Map.of(
	            "success", true,
	            "data", members
	        ));
	    } catch (Exception e) {
	        return ResponseEntity.status(500).body(Map.of(
	            "success", false,
	            "message", "Error: " + e.getMessage()
	        ));
	    }
	}

}
