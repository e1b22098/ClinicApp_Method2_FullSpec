package com.unknownclinic.appointment.service;

import com.unknownclinic.appointment.domain.User;
import com.unknownclinic.appointment.dto.UserRegistrationDto;
import com.unknownclinic.appointment.repository.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User findByCardNumber(String cardNumber) {
        return userMapper.findByCardNumber(cardNumber);
    }

    public User findById(Long id) {
        return userMapper.findById(id);
    }

    @Transactional
    public void register(UserRegistrationDto dto) {
        // 診察券番号の重複チェック
        User existingUser = userMapper.findByCardNumber(dto.getCardNumber());
        if (existingUser != null) {
            throw new IllegalArgumentException("この診察券番号は既に登録されています");
        }

        // パスワード確認
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new IllegalArgumentException("パスワードが一致しません");
        }

        // ユーザー登録
        User user = new User();
        user.setCardNumber(dto.getCardNumber());
        user.setBirthday(dto.getBirthday());
        user.setName(dto.getName());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        userMapper.insert(user);
    }

    @Transactional
    public void updatePassword(Long userId, String newPassword) {
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("ユーザーが見つかりません");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userMapper.update(user);
    }
}

