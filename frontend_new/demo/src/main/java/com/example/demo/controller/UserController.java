package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (!userService.usernameExists(user.getUsername())) {
            // 如果没有传角色，强制设为"user"
            if (user.getRole() == null || user.getRole().isEmpty()) {
                user.setRole("user"); // 让普通注册用户默认就是普通用户
            }
            userService.register(user);
            return ResponseEntity.ok(Map.of("code", 0, "msg", "Register success"));
        } else {
            return ResponseEntity.badRequest().body(Map.of("code", 1, "msg", "Username already exists"));
        }
    }
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> params) {
        User user = userService.login(params.get("username"), params.get("password"));
        if(user != null) {
            return Map.of(
                    "code", 200,
                    "msg", "Login success",
                    "role", user.getRole() == null ? "" : user.getRole(),
                    "username", user.getUsername() == null ? "" : user.getUsername(),
                    "id", user.getId() == null ? 0 : user.getId()
            );
        } else {
            return Map.of("code", 401, "msg", "Invalid credentials");
        }
    }
    // 获取全部用户
    @GetMapping("/users")
    public ResponseEntity<?> listUsers(@RequestHeader(value = "X-Role", required = false) String role) {
        if (!"admin".equals(role)) {
            return ResponseEntity.status(403).body("Forbidden");
        }
        return ResponseEntity.ok(userService.findAll());
    }

    // 新增用户！！（对接你的Vue的add用户功能）
    @PostMapping("/users")
    public User addUser(@RequestBody User user) {
        // 只能由后端指定角色，比如普通页面或管理员页面各自用不同接口
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("user");
        }
        return userService.register(user);
    }

    // 删除用户
    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Integer id) {
        userService.deleteById(id);
    }

    // 修改用户
    @PutMapping("/users/{id}")
    public User editUser(@PathVariable Integer id, @RequestBody User user) {
        user.setId(id);
        return userService.updateUser(id, user);
    }
    // 通过id获取user
    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUser(@PathVariable Integer id) {
        User user = userService.findById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(404).body("用户不存在");
        }
    }
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/profile")
    public User getProfile(@RequestParam Integer userId) {
        return userRepository.findById(userId).orElse(null);
    }
}