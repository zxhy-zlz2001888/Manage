package com.example.demo.dto;

import java.time.LocalDateTime;

public class AppointmentDTO {
    private Integer id;
    private Integer userId;
    private String status;
    private Integer activityId;

    private String username;
    private String activityName;
    private Double price;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer stock;
    public AppointmentDTO() {}
    public AppointmentDTO(
            Integer id,
            Integer userId,
            String status,
            Integer activityId,
            String username,
            String activityName,
            Double price,
            String description,
            LocalDateTime startTime,
            LocalDateTime endTime,
            Integer stock
    ) {
        this.id = id;
        this.userId = userId;
        this.status = status;
        this.activityId = activityId;
        this.username = username;
        this.activityName = activityName;
        this.price = price;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.stock = stock;
    }

    // getter和setter略
}