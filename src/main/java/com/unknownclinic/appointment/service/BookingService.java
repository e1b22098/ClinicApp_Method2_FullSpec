package com.unknownclinic.appointment.service;

import com.unknownclinic.appointment.domain.Booking;
import com.unknownclinic.appointment.domain.BusinessDay;
import com.unknownclinic.appointment.domain.TimeSlot;
import com.unknownclinic.appointment.dto.BookingFormDto;
import com.unknownclinic.appointment.repository.BookingMapper;
import com.unknownclinic.appointment.repository.BusinessDayMapper;
import com.unknownclinic.appointment.repository.TimeSlotMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {

    @Autowired
    private BookingMapper bookingMapper;

    @Autowired
    private BusinessDayMapper businessDayMapper;

    @Autowired
    private TimeSlotMapper timeSlotMapper;

    public List<BusinessDay> getAvailableBusinessDays() {
        return businessDayMapper.findAllActive();
    }

    public List<TimeSlot> getAvailableTimeSlots() {
        return timeSlotMapper.findAllActive();
    }

    public List<Booking> getBookingsByDate(LocalDate date) {
        return bookingMapper.findByBusinessDate(date);
    }

    public List<Booking> getBookingsByUserId(Long userId) {
        return bookingMapper.findByUserId(userId);
    }

    public Booking getBookingById(Long id) {
        return bookingMapper.findById(id);
    }

    @Transactional
    public Booking createBooking(Long userId, BookingFormDto dto) {
        // 営業日チェック
        BusinessDay businessDay = businessDayMapper.findByDate(dto.getBusinessDate());
        if (businessDay == null || !businessDay.getIsActive()) {
            throw new IllegalArgumentException("選択された日付は予約可能日ではありません");
        }

        // 過去日チェック
        if (dto.getBusinessDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("過去の日付は予約できません");
        }

        // 時間枠チェック
        TimeSlot timeSlot = timeSlotMapper.findById(dto.getTimeSlotId());
        if (timeSlot == null || !timeSlot.getIsActive()) {
            throw new IllegalArgumentException("選択された時間枠は利用できません");
        }

        // 二重予約防止チェック
        Booking existingBooking = bookingMapper.findByUserAndDateAndTimeSlot(
            userId, dto.getBusinessDate(), dto.getTimeSlotId());
        if (existingBooking != null) {
            throw new IllegalStateException("この時間枠は既に予約済みです");
        }

        // 同一時間枠の予約数チェック（1枠1人まで）
        List<Booking> existingBookings = bookingMapper.findByBusinessDateAndTimeSlot(
            dto.getBusinessDate(), dto.getTimeSlotId());
        if (!existingBookings.isEmpty()) {
            throw new IllegalStateException("この時間枠は既に満席です");
        }

        // 予約作成
        Booking booking = new Booking();
        booking.setUserId(userId);
        booking.setBusinessDate(dto.getBusinessDate());
        booking.setTimeSlotId(dto.getTimeSlotId());
        booking.setStatus(Booking.Status.PENDING);

        bookingMapper.insert(booking);
        return bookingMapper.findById(booking.getId());
    }

    @Transactional
    public void confirmBooking(Long bookingId) {
        Booking booking = bookingMapper.findById(bookingId);
        if (booking == null) {
            throw new IllegalArgumentException("予約が見つかりません");
        }
        booking.setStatus(Booking.Status.CONFIRMED);
        bookingMapper.update(booking);
    }

    @Transactional
    public void cancelBooking(Long bookingId, Long userId) {
        Booking booking = bookingMapper.findById(bookingId);
        if (booking == null) {
            throw new IllegalArgumentException("予約が見つかりません");
        }
        if (!booking.getUserId().equals(userId)) {
            throw new IllegalStateException("この予約をキャンセルする権限がありません");
        }
        bookingMapper.cancel(bookingId);
    }

    public List<TimeSlot> getAvailableTimeSlotsForDate(LocalDate date) {
        List<TimeSlot> allSlots = timeSlotMapper.findAllActive();
        List<Booking> bookings = bookingMapper.findByBusinessDate(date);
        
        List<Long> bookedSlotIds = bookings.stream()
            .map(Booking::getTimeSlotId)
            .collect(Collectors.toList());

        return allSlots.stream()
            .filter(slot -> !bookedSlotIds.contains(slot.getId()))
            .collect(Collectors.toList());
    }
}

