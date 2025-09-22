package com.clubmatrix.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clubmatrix.demo.model.Announcement;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Integer>{
	List<Announcement> findByClub_Id(int id);
}
