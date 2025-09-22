package com.clubmatrix.demo.controller;

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

import com.clubmatrix.demo.model.Announcement;
import com.clubmatrix.demo.model.Club;
import com.clubmatrix.demo.repository.AnnouncementRepository;
import com.clubmatrix.demo.repository.ClubRepository;

@RestController
@RequestMapping("/announcement")
public class AnnouncementController {
	@Autowired
    private AnnouncementRepository announcementRepository;

    @Autowired
    private ClubRepository clubRepository;

    @PostMapping("/add/{clubId}")
    public Announcement addAnnouncement(@PathVariable int clubId, @RequestBody Announcement announcement) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new RuntimeException("Club not found"));
        announcement.setClub(club);
        return announcementRepository.save(announcement);
    }
    @GetMapping("/get/all/{clubId}")
    public ResponseEntity<?> getAnnouncementsByClubId(@PathVariable int clubId){
    	return ResponseEntity.ok(Map.of(
    			"status", true,
    			"message", "all club announcements found!",
    			"data", announcementRepository.findByClub_Id(clubId)));
    }

}
