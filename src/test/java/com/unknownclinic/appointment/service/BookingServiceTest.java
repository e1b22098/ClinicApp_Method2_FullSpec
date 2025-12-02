package com.unknownclinic.appointment.service;

import com.unknownclinic.appointment.domain.Booking;
import com.unknownclinic.appointment.domain.BusinessDay;
import com.unknownclinic.appointment.domain.TimeSlot;
import com.unknownclinic.appointment.dto.BookingFormDto;
import com.unknownclinic.appointment.repository.BookingMapper;
import com.unknownclinic.appointment.repository.BusinessDayMapper;
import com.unknownclinic.appointment.repository.TimeSlotMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingMapper bookingMapper;

    @Mock
    private BusinessDayMapper businessDayMapper;

    @Mock
    private TimeSlotMapper timeSlotMapper;

    @InjectMocks
    private BookingService bookingService;

    private BusinessDay businessDay;
    private TimeSlot timeSlot;
    private BookingFormDto bookingFormDto;
    private Long userId;

    @BeforeEach
    void setUp() {
        userId = 1L;
        
        businessDay = new BusinessDay();
        businessDay.setId(1L);
        businessDay.setBusinessDate(LocalDate.now().plusDays(1));
        businessDay.setIsActive(true);

        timeSlot = new TimeSlot();
        timeSlot.setId(1L);
        timeSlot.setStartTime(LocalTime.of(9, 0));
        timeSlot.setEndTime(LocalTime.of(9, 30));
        timeSlot.setIsActive(true);

        bookingFormDto = new BookingFormDto();
        bookingFormDto.setBusinessDate(LocalDate.now().plusDays(1));
        bookingFormDto.setTimeSlotId(1L);
    }

    @Test
    void testCreateBooking_Success() {
        // モックの設定
        when(businessDayMapper.findByDate(any(LocalDate.class))).thenReturn(businessDay);
        when(timeSlotMapper.findById(anyLong())).thenReturn(timeSlot);
        when(bookingMapper.findByUserAndDateAndTimeSlot(anyLong(), any(LocalDate.class), anyLong())).thenReturn(null);
        when(bookingMapper.findByBusinessDateAndTimeSlot(any(LocalDate.class), anyLong())).thenReturn(new ArrayList<>());
        
        Booking savedBooking = new Booking();
        savedBooking.setId(1L);
        savedBooking.setUserId(userId);
        savedBooking.setBusinessDate(bookingFormDto.getBusinessDate());
        savedBooking.setTimeSlotId(bookingFormDto.getTimeSlotId());
        savedBooking.setStatus(Booking.Status.PENDING);
        
        when(bookingMapper.findById(anyLong())).thenReturn(savedBooking);
        doNothing().when(bookingMapper).insert(any(Booking.class));

        // テスト実行
        Booking result = bookingService.createBooking(userId, bookingFormDto);

        // 検証
        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        verify(bookingMapper, times(1)).insert(any(Booking.class));
    }

    @Test
    void testCreateBooking_BusinessDayNotFound() {
        when(businessDayMapper.findByDate(any(LocalDate.class))).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> {
            bookingService.createBooking(userId, bookingFormDto);
        });
    }

    @Test
    void testCreateBooking_DuplicateBooking() {
        when(businessDayMapper.findByDate(any(LocalDate.class))).thenReturn(businessDay);
        when(timeSlotMapper.findById(anyLong())).thenReturn(timeSlot);
        
        Booking existingBooking = new Booking();
        when(bookingMapper.findByUserAndDateAndTimeSlot(anyLong(), any(LocalDate.class), anyLong()))
            .thenReturn(existingBooking);

        assertThrows(IllegalStateException.class, () -> {
            bookingService.createBooking(userId, bookingFormDto);
        });
    }

    @Test
    void testCreateBooking_TimeSlotFull() {
        when(businessDayMapper.findByDate(any(LocalDate.class))).thenReturn(businessDay);
        when(timeSlotMapper.findById(anyLong())).thenReturn(timeSlot);
        when(bookingMapper.findByUserAndDateAndTimeSlot(anyLong(), any(LocalDate.class), anyLong())).thenReturn(null);
        
        List<Booking> existingBookings = new ArrayList<>();
        existingBookings.add(new Booking());
        when(bookingMapper.findByBusinessDateAndTimeSlot(any(LocalDate.class), anyLong()))
            .thenReturn(existingBookings);

        assertThrows(IllegalStateException.class, () -> {
            bookingService.createBooking(userId, bookingFormDto);
        });
    }

    @Test
    void testCancelBooking_Success() {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setUserId(userId);
        booking.setStatus(Booking.Status.PENDING);

        when(bookingMapper.findById(anyLong())).thenReturn(booking);
        doNothing().when(bookingMapper).cancel(anyLong());

        assertDoesNotThrow(() -> {
            bookingService.cancelBooking(1L, userId);
        });

        verify(bookingMapper, times(1)).cancel(1L);
    }

    @Test
    void testCancelBooking_NotFound() {
        when(bookingMapper.findById(anyLong())).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> {
            bookingService.cancelBooking(1L, userId);
        });
    }

    @Test
    void testCancelBooking_Unauthorized() {
        Booking booking = new Booking();
        booking.setId(1L);
        booking.setUserId(2L); // 異なるユーザー

        when(bookingMapper.findById(anyLong())).thenReturn(booking);

        assertThrows(IllegalStateException.class, () -> {
            bookingService.cancelBooking(1L, userId);
        });
    }
}

