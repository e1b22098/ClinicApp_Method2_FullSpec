package com.unknownclinic.appointment.controller;

import com.unknownclinic.appointment.domain.Booking;
import com.unknownclinic.appointment.domain.BusinessDay;
import com.unknownclinic.appointment.domain.TimeSlot;
import com.unknownclinic.appointment.domain.User;
import com.unknownclinic.appointment.dto.BookingDisplayDto;
import com.unknownclinic.appointment.dto.BookingFormDto;
import com.unknownclinic.appointment.dto.UserRegistrationDto;
import com.unknownclinic.appointment.service.BookingService;
import com.unknownclinic.appointment.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private BookingService bookingService;

    @GetMapping("/login")
    public String login() {
        return "user/login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("userRegistrationDto", new UserRegistrationDto());
        return "user/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute UserRegistrationDto dto, 
                          BindingResult result, 
                          RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "user/register";
        }

        try {
            userService.register(dto);
            redirectAttributes.addFlashAttribute("successMessage", "登録が完了しました。ログインしてください。");
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/register";
        }
    }

    @GetMapping("/appointments")
    public String appointments(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String cardNumber = auth.getName();

        User user = userService.findByCardNumber(cardNumber);
        if (user == null) {
            return "redirect:/login";
        }

        List<BusinessDay> businessDays = bookingService.getAvailableBusinessDays();
        List<TimeSlot> timeSlots = bookingService.getAvailableTimeSlots();

        model.addAttribute("businessDays", businessDays);
        model.addAttribute("timeSlots", timeSlots);
        model.addAttribute("bookingFormDto", new BookingFormDto());
        model.addAttribute("user", user);

        return "user/appointments";
    }

    @PostMapping("/appointments")
    public String createBooking(@Valid @ModelAttribute BookingFormDto dto,
                               BindingResult result,
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "入力内容に誤りがあります");
            return "redirect:/appointments";
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String cardNumber = auth.getName();
        User user = userService.findByCardNumber(cardNumber);

        try {
            bookingService.createBooking(user.getId(), dto);
            redirectAttributes.addFlashAttribute("successMessage", "予約が完了しました");
            return "redirect:/appointments";
        } catch (IllegalArgumentException | IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/appointments";
        }
    }

    @GetMapping("/mypage")
    public String myPage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String cardNumber = auth.getName();
        User user = userService.findByCardNumber(cardNumber);

        List<Booking> bookings = bookingService.getBookingsByUserId(user.getId());
        List<BookingDisplayDto> bookingDtos = bookings.stream()
            .map(BookingDisplayDto::new)
            .collect(Collectors.toList());

        model.addAttribute("user", user);
        model.addAttribute("bookings", bookingDtos);

        return "user/mypage";
    }

    @PostMapping("/bookings/{id}/cancel")
    public String cancelBooking(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String cardNumber = auth.getName();
        User user = userService.findByCardNumber(cardNumber);

        try {
            bookingService.cancelBooking(id, user.getId());
            redirectAttributes.addFlashAttribute("successMessage", "予約をキャンセルしました");
        } catch (IllegalArgumentException | IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/mypage";
    }
}

