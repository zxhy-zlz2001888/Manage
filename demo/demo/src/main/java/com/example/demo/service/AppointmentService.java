package com.example.demo.service;

import com.example.demo.dto.AppointmentDTO;
import com.example.demo.entity.Appointment;
import com.example.demo.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentService {
    @Autowired
    private AppointmentRepository appointmentRepository;

    // 查实体（如需直接查DTO不用本方法）
    public List<Appointment> findByUserId(Integer userId) {
        return appointmentRepository.findByUserId(userId);
    }

    // 查DTO推荐直接用findMyAppointments
    public List<AppointmentDTO> findMyAppointments(Integer userId) {
        return appointmentRepository.findMyAppointments(userId);
    }
    public void deleteById(Integer id) {
        appointmentRepository.deleteById(id);
    }
    // (如只查实体后要手动转DTO则如下)
    public List<AppointmentDTO> findDTOByUserId(Integer userId) {
        List<Appointment> appointments = appointmentRepository.findByUserId(userId);
        return appointments.stream().map(a -> {
            AppointmentDTO dto = new AppointmentDTO();
            dto.setId(a.getId());
            dto.setUserId(a.getUserId());
            dto.setActivityId(a.getActivityId());
            dto.setStatus(a.getStatus());
            // ...其他字段自己赋值
            return dto;
        }).collect(Collectors.toList());
    }

    public boolean existsByUserIdAndActivityId(Integer userId, Integer activityId) {
        return appointmentRepository.existsByUserIdAndActivityId(userId, activityId);
    }

    public Appointment save(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }
    public List<AppointmentDTO> findAllWithFilter(String username, LocalDateTime dateTime) {
        // 你可以在repository写复杂JPQL，或查全部后用stream过滤
        return appointmentRepository.findAllWithFilter(username, dateTime);
    }

    public Appointment findById(Integer id) {
        return appointmentRepository.findById(id).orElse(null);
    }
}