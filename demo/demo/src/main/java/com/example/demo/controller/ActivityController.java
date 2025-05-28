package com.example.demo.controller;

import com.example.demo.entity.Activity;
import com.example.demo.repository.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activities") // 注意这里是 activities！（和前端对齐）
public class ActivityController {

    @Autowired
    private ActivityRepository activityRepository;

    @GetMapping
    public List<Activity> list() {
        return activityRepository.findAll();
    }

    @PostMapping
    public Activity add(@RequestBody Activity activity) {
        return activityRepository.save(activity);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        activityRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    public Activity update(@PathVariable Integer id, @RequestBody Activity activity) {
        activity.setId(id);
        return activityRepository.save(activity);
    }
}