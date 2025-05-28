package com.example.demo.controller;

import com.example.demo.dto.AppointmentDTO;
import com.example.demo.entity.Appointment;
import com.example.demo.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/appointments")
@CrossOrigin // 跨域支持
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;
    // 查询预约列表（支持用户名和日期过滤，可选参数）
    @GetMapping
    public List<AppointmentDTO> list(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDateTime dateTime
    ) {
        return appointmentService.findAllWithFilter(username, dateTime);
    }
    // 审批（通过）
    @PutMapping("/{id}/approve")
    public Appointment approve(@PathVariable Integer id) {
        Appointment appointment = appointmentService.findById(id);
        if (appointment == null) throw new RuntimeException("预约不存在");
        appointment.setStatus("已通过");
        return appointmentService.save(appointment);
    }
    // 审批（拒绝）
    @PutMapping("/{id}/reject")
    public Appointment reject(@PathVariable Integer id) {
        Appointment appointment = appointmentService.findById(id);
        if (appointment == null) throw new RuntimeException("预约不存在");
        appointment.setStatus("已拒绝");
        return appointmentService.save(appointment);
    }

    // 获取当前用户的全部预约
    @GetMapping("/my")
    public List<AppointmentDTO> myAppointments(@RequestHeader("X-User-Id") Integer userId) {
        // 返回的就是全信息DTO列表
        System.out.println(userId);
        System.out.println(appointmentService.findMyAppointments(userId));
        return appointmentService.findMyAppointments(userId);
    }

    // 新增预约
    @PostMapping
    public Appointment createAppointment(@RequestBody Appointment appointment) {
        if (appointment.getUserId() == null || appointment.getActivityId() == null) {
            throw new IllegalArgumentException("userId和activityId不能为空");
        }
        if (appointmentService.existsByUserIdAndActivityId(appointment.getUserId(), appointment.getActivityId())) {
            throw new RuntimeException("您已报名过此项目");
        }
        // 补充：设置状态
        appointment.setStatus("待审核");
        // 可选：自动补全project/time字段，建议根据activityId查activity的name/time赋值
        return appointmentService.save(appointment);
    }
    // 取消预约，根据预约ID
    @DeleteMapping("/{id}")
    public void cancelAppointment(@PathVariable("id") Integer id) {
        appointmentService.deleteById(id);
    }

}