package com.ship.nazmul.ship.services;

import com.ship.nazmul.ship.entities.Booking;
import com.ship.nazmul.ship.exceptions.exists.UserAlreadyExistsException;
import com.ship.nazmul.ship.exceptions.forbidden.ForbiddenException;
import com.ship.nazmul.ship.exceptions.invalid.UserInvalidException;
import com.ship.nazmul.ship.exceptions.notfound.NotFoundException;
import com.ship.nazmul.ship.exceptions.notfound.UserNotFoundException;
import com.ship.nazmul.ship.exceptions.nullpointer.NullPasswordException;
import org.springframework.data.domain.Page;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface BookingService {
    Booking save(Booking booking) throws UserNotFoundException, UserInvalidException, UserAlreadyExistsException, NullPasswordException;

    Booking getOne(Long id);

    Booking getAdminBooking(Long id);

    Booking getServiceAdminBooking(Long id);

    Booking getServiceAgentBooking(Long id) throws ForbiddenException;

    Page<Booking> getAllBookings(int page);

    List<Booking> getAllBookings();

    List<Booking> getByCreateDateWithoutCanceled(Date date);

    Page<Booking> getAllBookingsWithoutCanceled(int page);

    List<Booking> getBookingsByShipIdAndCreateDate(Long shipId, Date date);

    Page<Booking> getBookingsByShipId(Long shipId, int page);

    Booking createBooking(Booking booking);

    Booking createAdminBooking(Booking booking) throws ForbiddenException, NotFoundException, ParseException, UserAlreadyExistsException, NullPasswordException, UserInvalidException;

    Booking confirmReservation(Long bookingId) throws NotFoundException, UserAlreadyExistsException, NullPasswordException, UserInvalidException, ParseException, ForbiddenException;

    Booking createAdminAgentBooking(Booking booking) throws ForbiddenException, NotFoundException, ParseException, UserAlreadyExistsException, NullPasswordException, UserInvalidException;

    Booking createServiceAdminBooking(Booking booking) throws ForbiddenException, NotFoundException, ParseException, UserAlreadyExistsException, NullPasswordException, UserInvalidException;

    Booking createServiceAgentBooking(Booking booking) throws ForbiddenException, NotFoundException, ParseException, UserAlreadyExistsException, NullPasswordException, UserInvalidException;

    Page<Booking> getMySells(int page);

    void cancelReservation(Long bookingId) throws ParseException, NotFoundException, ForbiddenException, UserAlreadyExistsException, NullPasswordException, UserInvalidException;

    void cancelReservationSeat(Long seatId, Long bookingId) throws NotFoundException, ForbiddenException, ParseException, UserAlreadyExistsException, NullPasswordException, UserInvalidException;

    void cancelReservationSeats(Long bookingId, List<Long> seatIds) throws UserInvalidException, ForbiddenException, ParseException, NullPasswordException, NotFoundException, UserAlreadyExistsException;

    void cancelBooking(Long bookingId) throws ForbiddenException, NotFoundException, ParseException;

    void cancelBookingSeats(Long bookingId, List<Long> seatIds) throws ForbiddenException, UserInvalidException, NullPasswordException, ParseException, UserAlreadyExistsException, NotFoundException;

    List<Booking> getBookingListByShipIdAndDate(Long shipId, LocalDate date);

    List<Booking> getBookingListByCreatedIdAndShipIdAndDate(Long userId, Long shipId, Date date);

    List<Booking> getReservationListByUserIdShipIdAndDate(Long userId, Long shipId, LocalDate date);

    void updateBooking() throws ForbiddenException;
}
