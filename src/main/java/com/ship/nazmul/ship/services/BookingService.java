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
import java.util.List;

public interface BookingService {
    Booking save(Booking booking) throws UserNotFoundException, UserInvalidException, UserAlreadyExistsException, NullPasswordException;

    Booking getOne(Long id);

    Page<Booking> getAllBookings(int page);

    List<Booking> getAllBookings();

    Page<Booking> getBookingsByShipId(Long shipId, int page);

    Booking createBooking(Booking booking);

    Booking createAdminBooking(Booking booking) throws ForbiddenException, NotFoundException, ParseException, UserAlreadyExistsException, NullPasswordException, UserInvalidException;

    Booking createAdminAgentBooking(Booking booking);

    Booking createServiceAdminBooking(Booking booking);

    Booking createServiceAgentBooking(Booking booking);

    Page<Booking> getMySells(int page);


}
