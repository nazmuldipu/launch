package com.ship.nazmul.ship.controller.api.booking;

import com.ship.nazmul.ship.entities.Booking;
import com.ship.nazmul.ship.exceptions.exists.UserAlreadyExistsException;
import com.ship.nazmul.ship.exceptions.forbidden.ForbiddenException;
import com.ship.nazmul.ship.exceptions.invalid.UserInvalidException;
import com.ship.nazmul.ship.exceptions.notfound.NotFoundException;
import com.ship.nazmul.ship.exceptions.nullpointer.NullPasswordException;
import com.ship.nazmul.ship.services.BookingService;
import com.ship.nazmul.ship.services.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/serviceAdmin/booking")
public class BookingServiceAdminController {
    private final BookingService bookingService;
    private final SeatService seatService;

    @Autowired
    public BookingServiceAdminController(BookingService bookingService, SeatService seatService) {
        this.bookingService = bookingService;
        this.seatService = seatService;
    }

    @PostMapping("/sell")
    private ResponseEntity createBooking(@RequestBody Booking booking) throws ParseException, NotFoundException, ForbiddenException, UserAlreadyExistsException, NullPasswordException, UserInvalidException, javassist.NotFoundException {
        return ResponseEntity.ok(this.bookingService.createServiceAdminBooking(booking));
    }

    @GetMapping("/{bookingId}")
    private ResponseEntity getBookingById(@PathVariable("bookingId")Long bookingId){
        return ResponseEntity.ok(this.bookingService.getServiceAdminBooking(bookingId));
    }

    @PutMapping("/confirmReservation/{bookingId}")
    private ResponseEntity confirmReservation(@PathVariable("bookingId")Long bookingId) throws NullPasswordException, UserAlreadyExistsException, UserInvalidException, NotFoundException, ParseException, javassist.NotFoundException {

        return ResponseEntity.ok(this.bookingService.confirmReservation(bookingId));
    }

    @DeleteMapping("/cancelBooking/{bookingId}")
    private ResponseEntity cancelBooking(@PathVariable("bookingId")Long bookingId) throws NotFoundException, ForbiddenException, ParseException, javassist.NotFoundException {
        this.bookingService.cancelBooking(bookingId);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/cancelReservation/{bookingId}")
    private ResponseEntity cancelReservation(@PathVariable("bookingId")Long bookingId) throws UserInvalidException, ForbiddenException, ParseException, NullPasswordException, NotFoundException, UserAlreadyExistsException {
        this.bookingService.cancelReservation(bookingId);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/cancelReservationSeats/{bookingId}")
    private ResponseEntity cancelReservationSeat(@PathVariable("bookingId")Long bookingId, @RequestParam("seatIds") List<Long> seatIds) throws NotFoundException, ForbiddenException, ParseException, UserAlreadyExistsException, NullPasswordException, UserInvalidException {
        this.bookingService.cancelReservationSeats(bookingId, seatIds);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/cancelBookingSeats/{bookingId}")
    private ResponseEntity cancelBookingSeat(@PathVariable("bookingId")Long bookingId, @RequestParam("seatIds") List<Long> seatIds) throws NotFoundException, ForbiddenException, ParseException, UserAlreadyExistsException, NullPasswordException, UserInvalidException {
        this.bookingService.cancelBookingSeats(bookingId, seatIds);
        return ResponseEntity.ok().build();
    }
}
