package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "appointment")
@Data
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer activityId; // 活动id
    private Integer userId; // 预约人id
    private String project; // 预约内容
    private String time;    // 时间（字符串/日期均可）
    private String status;  // 状态

}