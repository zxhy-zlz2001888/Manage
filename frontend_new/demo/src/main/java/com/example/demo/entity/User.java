package com.example.demo.entity;

import lombok.Data;

import jakarta.persistence.*;

@Entity
@Table(name = "user")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;
    private String password;
    private String gender;
    private String origin;
    private String hobbies;
    private String intro;
    private String role; // 增加的部分
    private String phone;   // 新增手机号字段
    private String email;   // 新增邮箱字段
}