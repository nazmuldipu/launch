package com.ship.nazmul.ship.controller.api.booking;

import com.ship.nazmul.ship.dto.TicketDto;
import com.ship.nazmul.ship.entities.Booking;
import com.ship.nazmul.ship.exceptions.exists.UserAlreadyExistsException;
import com.ship.nazmul.ship.exceptions.forbidden.ForbiddenException;
import com.ship.nazmul.ship.exceptions.invalid.UserInvalidException;
import com.ship.nazmul.ship.exceptions.notfound.NotFoundException;
import com.ship.nazmul.ship.exceptions.nullpointer.NullPasswordException;
import com.ship.nazmul.ship.mapper.TicketMapper;
import com.ship.nazmul.ship.services.BookingService;
import com.ship.nazmul.ship.services.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/serviceAdmin/booking")
public class BookingServiceAdminController {
    private final BookingService bookingService;
    private final SeatService seatService;
    private final TicketMapper ticketMapper;

    @Autowired
    public BookingServiceAdminController(BookingService bookingService, SeatService seatService, TicketMapper ticketMapper) {
        this.bookingService = bookingService;
        this.seatService = seatService;
        this.ticketMapper = ticketMapper;
    }

    @PostMapping("/sell")
    private ResponseEntity createBooking(@RequestBody Booking booking) throws ParseException, NotFoundException, ForbiddenException, UserAlreadyExistsException, NullPasswordException, UserInvalidException, javassist.NotFoundException {
        TicketDto ticketDto = ticketMapper.toTicket(this.bookingService.createServiceAdminBooking(booking));
        return ResponseEntity.ok(ticketDto);
    }

    @GetMapping("/{bookingId}")
    private ResponseEntity getBookingById(@PathVariable("bookingId")Long bookingId){
        TicketDto ticketDto = ticketMapper.toTicket(this.bookingService.getServiceAdminBooking(bookingId));
        return ResponseEntity.ok(ticketDto);
    }

    @PutMapping("/confirmReservation/{bookingId}")
    private ResponseEntity confirmReservation(@PathVariable("bookingId")Long bookingId) throws NullPasswordException, UserAlreadyExistsException, UserInvalidException, NotFoundException, ParseException, javassist.NotFoundException, ForbiddenException {

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
