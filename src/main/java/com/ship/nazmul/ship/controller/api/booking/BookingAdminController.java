package com.ship.nazmul.ship.controller.api.booking;

import com.ship.nazmul.ship.entities.Booking;
import com.ship.nazmul.ship.entities.SubBooking;
import com.ship.nazmul.ship.exceptions.exists.UserAlreadyExistsException;
import com.ship.nazmul.ship.exceptions.forbidden.ForbiddenException;
import com.ship.nazmul.ship.exceptions.invalid.UserInvalidException;
import com.ship.nazmul.ship.exceptions.notfound.NotFoundException;
import com.ship.nazmul.ship.exceptions.nullpointer.NullPasswordException;
import com.ship.nazmul.ship.services.BookingService;
import com.ship.nazmul.ship.services.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Date;

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

    @PostMapping("/sell")
    private ResponseEntity createBooking(@RequestBody Booking booking) throws UserInvalidException, ForbiddenException, ParseException, NullPasswordException, NotFoundException, UserAlreadyExistsException {
        Booking myBooking = this.bookingService.createAdminBooking(booking);
        System.out.println(myBooking.getId());
        for(SubBooking sb : myBooking.getSubBookingList()){
            System.out.println(sb.toString());
            System.out.println(sb.getSeat().getId());
        }
        return ResponseEntity.ok(myBooking);
    }

    @GetMapping("/mySells")
    private ResponseEntity getAdminSells(@RequestParam(value = "page", defaultValue = "0") Integer page){
        return ResponseEntity.ok(this.bookingService.getMySells(page));
    }

    @GetMapping("/check/{seatId}")
    private ResponseEntity checkAvailability(@PathVariable("seatId")Long seatId, @RequestParam("date")@DateTimeFormat(pattern = "yyyy-MM-dd")Date date) throws NotFoundException {
        return ResponseEntity.ok(this.seatService.checkSeatAvailability(seatId, date));
    }
}
