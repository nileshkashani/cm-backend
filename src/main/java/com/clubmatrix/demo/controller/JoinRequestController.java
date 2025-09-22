package com.clubmatrix.demo.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.clubmatrix.demo.model.Club;
import com.clubmatrix.demo.model.JoinRequest;
import com.clubmatrix.demo.model.UserLogin;
import com.clubmatrix.demo.repository.ClubRepository;
import com.clubmatrix.demo.repository.JoinRequestRepository;
import com.clubmatrix.demo.repository.UserLoginRepository;

@RestController
@RequestMapping("/join")
public class JoinRequestController {

    @Autowired
    private JoinRequestRepository joinRequestRepository;

    @Autowired
    private UserLoginRepository userLoginRepository;

    @Autowired
    private ClubRepository clubRepository;

    // User requests to join a club
    @PostMapping("/request/{clubId}/{userId}")
    public ResponseEntity<?> joinRequests(@PathVariable int clubId, @PathVariable int userId) {
    	   System.out.println("Join request received: clubId=" + clubId + ", userId=" + userId);
        try {
            UserLogin user = userLoginRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Club club = clubRepository.findById(clubId)
                    .orElseThrow(() -> new RuntimeException("Club not found"));

            JoinRequest request = new JoinRequest();
            request.setUser(user);
            request.setClub(club);
            request.setStatus("PENDING");

            joinRequestRepository.save(request);

            return ResponseEntity.ok(Map.of(
                    "status", true,
                    "message", "Requested to join club successfully, please wait for leader response!"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", false,
                    "message", "Error: " + e.getMessage()
            ));
        }
    }

    // Club leader (or admin) deletes/cancels a join request
    @DeleteMapping("/delete/{requestId}")
    public ResponseEntity<?> deleteRequest(@PathVariable int requestId) {
    	
        try {
            joinRequestRepository.deleteById(requestId);
            return ResponseEntity.ok(Map.of(
                    "status", true,
                    "message", "Join request deleted successfully!"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", false,
                    "message", "Error: " + e.getMessage()
            ));
        }
    }
    @GetMapping("/requests/leader/{leaderId}")
    public ResponseEntity<?> getAllRequests(@PathVariable int leaderId) {
        try {
            // Fetch all clubs led by this leader
            Club leaderClubs = clubRepository.findByLeaderId(leaderId);

            // Fetch all join requests for these clubs
            List<JoinRequest> requests = joinRequestRepository.findByClub(leaderClubs);

            return ResponseEntity.ok(Map.of(
                "status", true,
                "message", "Requests fetched successfully",
                "data", requests
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "status", false,
                "message", "Error: " + e.getMessage()
            ));
        }
    }

}
