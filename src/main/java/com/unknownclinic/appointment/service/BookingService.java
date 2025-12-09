package com.unknownclinic.appointment.service;

import com.unknownclinic.appointment.domain.Booking;
import com.unknownclinic.appointment.domain.BusinessDay;
import com.unknownclinic.appointment.domain.TimeSlot;
import com.unknownclinic.appointment.dto.BookingFormDto;
import com.unknownclinic.appointment.repository.BookingMapper;
import com.unknownclinic.appointment.repository.BusinessDayMapper;
import com.unknownclinic.appointment.repository.TimeSlotMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private static final Logger logger = LoggerFactory.getLogger(BookingService.class);

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
        try {
            logger.debug("予約作成開始: userId = {}, businessDate = {}, timeSlotId = {}", 
                userId, dto.getBusinessDate(), dto.getTimeSlotId());

            // 営業日チェック
            BusinessDay businessDay = businessDayMapper.findByDate(dto.getBusinessDate());
            if (businessDay == null || !businessDay.getIsActive()) {
                logger.warn("営業日が見つからないか、無効です: businessDate = {}", dto.getBusinessDate());
                throw new IllegalArgumentException("選択された日付は予約可能日ではありません");
            }

            // 過去日チェック
            if (dto.getBusinessDate().isBefore(LocalDate.now())) {
                logger.warn("過去の日付が指定されました: businessDate = {}", dto.getBusinessDate());
                throw new IllegalArgumentException("過去の日付は予約できません");
            }

            // 時間枠チェック
            TimeSlot timeSlot = timeSlotMapper.findById(dto.getTimeSlotId());
            if (timeSlot == null || !timeSlot.getIsActive()) {
                logger.warn("時間枠が見つからないか、無効です: timeSlotId = {}", dto.getTimeSlotId());
                throw new IllegalArgumentException("選択された時間枠は利用できません");
            }

            // 二重予約防止チェック
            Booking existingBooking = bookingMapper.findByUserAndDateAndTimeSlot(
                userId, dto.getBusinessDate(), dto.getTimeSlotId());
            if (existingBooking != null) {
                logger.warn("既に予約済みです: userId = {}, businessDate = {}, timeSlotId = {}", 
                    userId, dto.getBusinessDate(), dto.getTimeSlotId());
                throw new IllegalStateException("この時間枠は既に予約済みです");
            }

            // 同一時間枠の予約数チェック（1枠1人まで）
            List<Booking> existingBookings = bookingMapper.findByBusinessDateAndTimeSlot(
                dto.getBusinessDate(), dto.getTimeSlotId());
            if (!existingBookings.isEmpty()) {
                logger.warn("時間枠が満席です: businessDate = {}, timeSlotId = {}", 
                    dto.getBusinessDate(), dto.getTimeSlotId());
                throw new IllegalStateException("この時間枠は既に満席です");
            }

            // 予約作成
            Booking booking = new Booking();
            booking.setUserId(userId);
            booking.setBusinessDate(dto.getBusinessDate());
            booking.setTimeSlotId(dto.getTimeSlotId());
            booking.setStatus(Booking.Status.PENDING);

            logger.debug("予約をデータベースに挿入します: booking = {}", booking);
            bookingMapper.insert(booking);
            logger.debug("予約が作成されました: bookingId = {}", booking.getId());

            Booking savedBooking = bookingMapper.findById(booking.getId());
            logger.debug("予約作成完了: bookingId = {}", savedBooking.getId());
            return savedBooking;
        } catch (IllegalArgumentException | IllegalStateException e) {
            logger.error("予約作成中にエラーが発生しました: userId = {}, businessDate = {}, timeSlotId = {}", 
                userId, dto.getBusinessDate(), dto.getTimeSlotId(), e);
            throw e;
        } catch (Exception e) {
            logger.error("予約作成中に予期しないエラーが発生しました: userId = {}, businessDate = {}, timeSlotId = {}", 
                userId, dto.getBusinessDate(), dto.getTimeSlotId(), e);
            throw new RuntimeException("予約作成中にエラーが発生しました", e);
        }
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

