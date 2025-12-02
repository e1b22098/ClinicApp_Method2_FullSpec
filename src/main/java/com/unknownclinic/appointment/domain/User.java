package com.unknownclinic.appointment.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class User {
    private Long id;
    private String cardNumber;
    private LocalDate birthday;
    private String name;
    private String password;
    private String phoneNumber;
    private LocalDateTime createdAt;

    public User() {
    }

    public User(Long id, String cardNumber, LocalDate birthday, String name, 
                String password, String phoneNumber, LocalDateTime createdAt) {
        this.id = id;
        this.cardNumber = cardNumber;
        this.birthday = birthday;
        this.name = name;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

