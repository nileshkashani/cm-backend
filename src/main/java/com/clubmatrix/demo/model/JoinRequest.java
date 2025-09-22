package com.clubmatrix.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class JoinRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // Each join request belongs to one user
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserLogin user;

    // Each join request belongs to one club
    @ManyToOne
    @JoinColumn(name = "club_id", nullable = false)
    private Club club;

    private String status; // PENDING, APPROVED, REJECTED

    private LocalDateTime createdAt = LocalDateTime.now();

    // Getters and Setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public UserLogin getUser() {
        return user;
    }
    public void setUser(UserLogin user) {
        this.user = user;
    }

    public Club getClub() {
        return club;
    }
    public void setClub(Club club) {
        this.club = club;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
