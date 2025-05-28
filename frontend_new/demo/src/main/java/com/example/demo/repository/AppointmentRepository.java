package com.example.demo.repository;

import com.example.demo.dto.AppointmentDTO;
import com.example.demo.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {
    List<Appointment> findByUserId(Integer userId);

    // 查我的详情
    @Query("SELECT new com.example.demo.dto.AppointmentDTO(" +
            "a.id, a.userId, a.status, a.activityId, " +
            "u.username, ac.name, ac.price, ac.description, ac.startTime, ac.endTime, ac.stock" +
            ") FROM Appointment a " +
            "LEFT JOIN Activity ac ON a.activityId = ac.id " +
            "LEFT JOIN User u ON a.userId = u.id " +
            "WHERE a.userId = :userId")
    List<AppointmentDTO> findMyAppointments(@Param("userId") Integer userId);

    // 查全部并条件过滤（给管理后台）
    @Query("SELECT new com.example.demo.dto.AppointmentDTO(" +
            "a.id, a.userId, a.status, a.activityId, " +
            "u.username, ac.name, ac.price, ac.description, ac.startTime, ac.endTime, ac.stock" +
            ") FROM Appointment a " +
            "LEFT JOIN Activity ac ON a.activityId = ac.id " +
            "LEFT JOIN User u ON a.userId = u.id " +
            "WHERE (:username IS NULL OR u.username LIKE %:username%) " +
            "AND (:date IS NULL OR ac.startTime = :date)")
    List<AppointmentDTO> findAllWithFilter(@Param("username") String username, @Param("date") LocalDateTime date);

    boolean existsByUserIdAndActivityId(Integer userId, Integer activityId);
}