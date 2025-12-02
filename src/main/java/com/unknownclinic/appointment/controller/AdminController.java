package com.unknownclinic.appointment.controller;

import com.unknownclinic.appointment.domain.Booking;
import com.unknownclinic.appointment.domain.BusinessDay;
import com.unknownclinic.appointment.dto.BookingDisplayDto;
import com.unknownclinic.appointment.service.BookingService;
import com.unknownclinic.appointment.service.BusinessDayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private BusinessDayService businessDayService;

    @GetMapping("/login")
    public String login(Model model) {
        return "admin/login";
    }

    @GetMapping("/bookings")
    public String bookings(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                          Model model) {
        if (date == null) {
            date = LocalDate.now();
        }

        List<Booking> bookings = bookingService.getBookingsByDate(date);
        List<BookingDisplayDto> bookingDtos = bookings.stream()
            .map(BookingDisplayDto::new)
            .collect(Collectors.toList());

        model.addAttribute("selectedDate", date);
        model.addAttribute("bookings", bookingDtos);

        return "admin/bookings";
    }

    @GetMapping("/business-days")
    public String businessDays(Model model) {
        List<BusinessDay> businessDays = businessDayService.getAllBusinessDays();
        model.addAttribute("businessDays", businessDays);
        return "admin/business-days";
    }

    @PostMapping("/business-days")
    public String createBusinessDay(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                   @RequestParam(required = false) String businessType,
                                   RedirectAttributes redirectAttributes) {
        try {
            businessDayService.createBusinessDay(date, businessType);
            redirectAttributes.addFlashAttribute("successMessage", "営業日を登録しました");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/business-days";
    }

    @PostMapping("/business-days/{id}/toggle")
    public String toggleBusinessDay(@PathVariable Long id,
                                   @RequestParam Boolean isActive,
                                   RedirectAttributes redirectAttributes) {
        try {
            businessDayService.updateBusinessDay(id, isActive, null);
            redirectAttributes.addFlashAttribute("successMessage", "営業日を更新しました");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/business-days";
    }

    @PostMapping("/business-days/{id}/delete")
    public String deleteBusinessDay(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            businessDayService.deleteBusinessDay(id);
            redirectAttributes.addFlashAttribute("successMessage", "営業日を削除しました");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/business-days";
    }
}

