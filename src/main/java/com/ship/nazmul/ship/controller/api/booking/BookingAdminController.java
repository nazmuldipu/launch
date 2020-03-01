package com.ship.nazmul.ship.controller.api.booking;

import com.ship.nazmul.ship.entities.Booking;
import com.ship.nazmul.ship.entities.Category;
import com.ship.nazmul.ship.entities.SubBooking;
import com.ship.nazmul.ship.exceptions.exists.UserAlreadyExistsException;
import com.ship.nazmul.ship.exceptions.forbidden.ForbiddenException;
import com.ship.nazmul.ship.exceptions.invalid.UserInvalidException;
import com.ship.nazmul.ship.exceptions.notfound.NotFoundException;
import com.ship.nazmul.ship.exceptions.nullpointer.NullPasswordException;
import com.ship.nazmul.ship.services.BookingService;
import com.ship.nazmul.ship.services.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Date;
//import java.util.Date;

@RestController
@RequestMapping("/api/v1/admin/booking")
public class BookingAdminController {
    private final BookingService bookingService;
    private final SeatService seatService;

    @Autowired
    public BookingAdminController(BookingService bookingService, SeatService seatService) {
        this.bookingService = bookingService;
        this.seatService = seatService;
    }

    @GetMapping("")
    private ResponseEntity<Page<Booking>> getAllBooking(@RequestParam(value = "page", defaultValue = "0") Integer page) {
        return ResponseEntity.ok(this.bookingService.getAllBookingsWithoutCanceled(page));
    }

    @PostMapping("/sell")
    private ResponseEntity createBooking(@RequestBody Booking booking) throws UserInvalidException, ForbiddenException, ParseException, NullPasswordException, NotFoundException, UserAlreadyExistsException, javassist.NotFoundException {
        booking = this.bookingService.createAdminBooking(booking);
        return ResponseEntity.ok(booking);
    }

    @PutMapping("/confirmReservation/{bookingId}")
    private ResponseEntity confirmReservation(@PathVariable("bookingId")Long bookingId) throws NullPasswordException, UserAlreadyExistsException, UserInvalidException, NotFoundException, ParseException, javassist.NotFoundException {

        return ResponseEntity.ok(this.bookingService.confirmReservation(bookingId));
    }

    @GetMapping("/{bookingId}")
    private ResponseEntity getBookingById(@PathVariable("bookingId")Long bookingId){
        return ResponseEntity.ok(this.bookingService.getOne(bookingId));
    }

    @GetMapping("/mySells")
    private ResponseEntity getAdminSells(@RequestParam(value = "page", defaultValue = "0") Integer page){
        return ResponseEntity.ok(this.bookingService.getMySells(page));
    }

    @GetMapping("/check/{seatId}")
    private ResponseEntity checkAvailability(@PathVariable("seatId")Long seatId, @RequestParam("date")@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) throws NotFoundException, ParseException {
        return ResponseEntity.ok(this.seatService.checkSeatAvailability(seatId, date));
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
}
