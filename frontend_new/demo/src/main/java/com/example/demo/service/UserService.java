package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    // 注册用户
    public User register(User user) {
        return userRepository.save(user);
    }

    // 登录验证
    public User login(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user == null) return null;
        if (user.getPassword() == null) return null; // 或提示“用户未设置密码”
        if (user.getPassword().equals(password)) return user;
        return null;
    }

    // 检查用户名是否已存在
    public boolean usernameExists(String username) {
        return userRepository.findByUsername(username) != null;
    }

    // 查全部用户
    public List<User> findAll() {
        return userRepository.findAll();
    }


// 查单个用户
    public User findById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    // 新增或修改用户
    public User save(User user) {
        return userRepository.save(user);
    }

    // 删除用户
    public void deleteById(Integer id) {
        userRepository.deleteById(id);
    }

    // 编辑用户
    // 通常直接用 save 即可，或如下写法更保险
    public User updateUser(Integer id, User userUpdate) {
        return userRepository.findById(id).map(user -> {
            user.setUsername(userUpdate.getUsername());
            user.setPassword(userUpdate.getPassword());
            user.setGender(userUpdate.getGender());
            user.setOrigin(userUpdate.getOrigin());
            user.setHobbies(userUpdate.getHobbies());
            user.setIntro(userUpdate.getIntro());
            return userRepository.save(user);
        }).orElse(null);
    }

    public User update(User user) {
        return null;
    }
}