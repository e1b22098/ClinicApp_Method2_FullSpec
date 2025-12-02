package com.unknownclinic.appointment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;

public class UserRegistrationDto {
    @NotBlank(message = "診察券番号を入力してください")
    @Pattern(regexp = "^[0-9]+$", message = "診察券番号は数字のみです")
    private String cardNumber;

    @NotNull(message = "生年月日を入力してください")
    private LocalDate birthday;

    @NotBlank(message = "氏名を入力してください")
    private String name;

    @NotBlank(message = "電話番号を入力してください")
    @Pattern(regexp = "^[0-9-]+$", message = "電話番号の形式が正しくありません")
    private String phoneNumber;

    @NotBlank(message = "パスワードを入力してください")
    private String password;

    @NotBlank(message = "パスワード（確認）を入力してください")
    private String confirmPassword;

    public UserRegistrationDto() {
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}

