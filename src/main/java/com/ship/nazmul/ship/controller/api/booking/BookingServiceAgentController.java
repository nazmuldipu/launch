package com.ship.nazmul.ship.controller.api.booking;

import com.ship.nazmul.ship.config.security.SecurityConfig;
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

@RestController
@RequestMapping("/api/v1/serviceAgent/booking")
public class BookingServiceAgentController {
    private final BookingService bookingService;
    private final SeatService seatService;
    private final TicketMapper ticketMapper;

    @Autowired
    public BookingServiceAgentController(BookingService bookingService, SeatService seatService, TicketMapper ticketMapper) {
        this.bookingService = bookingService;
        this.seatService = seatService;
        this.ticketMapper = ticketMapper;
    }

    @GetMapping("/{bookingId}")
    private ResponseEntity getBookingById(@PathVariable("bookingId")Long bookingId) throws ForbiddenException {
        TicketDto ticketDto = ticketMapper.toTicket(this.bookingService.getServiceAgentBooking(bookingId));
        return ResponseEntity.ok(ticketDto);
    }

    @PostMapping("/sell")
    private ResponseEntity createBooking(@RequestBody Booking booking) throws ParseException, NotFoundException, ForbiddenException, UserAlreadyExistsException, NullPasswordException, UserInvalidException, javassist.NotFoundException {
        TicketDto ticketDto = ticketMapper.toTicket(this.bookingService.createServiceAgentBooking(booking));
        return ResponseEntity.ok(ticketDto);
    }

    @PutMapping("/confirmReservation/{bookingId}")
    private ResponseEntity confirmReservation(@PathVariable("bookingId")Long bookingId) throws NullPasswordException, UserAlreadyExistsException, UserInvalidException, NotFoundException, ParseException, ForbiddenException {
        return ResponseEntity.ok(this.bookingService.confirmReservation(bookingId));
    }

    @DeleteMapping("/cancelBooking/{bookingId}")
    private ResponseEntity cancelBooking(@PathVariable("bookingId")Long bookingId) throws NotFoundException, ForbiddenException, ParseException, javassist.NotFoundException {
        Booking booking = this.bookingService.getOne(bookingId);
        if(!SecurityConfig.getCurrentUser().getId().equals(booking.getCreatedBy().getId()))
            throw new ForbiddenException("Access denied, this booking doesn't belongs to you");
        
        this.bookingService.cancelBooking(bookingId);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/cancelReservation/{bookingId}")
    private ResponseEntity cancelReservation(@PathVariable("bookingId")Long bookingId) throws UserInvalidException, ForbiddenException, ParseException, NullPasswordException, NotFoundException, UserAlreadyExistsException {
        Booking booking = this.bookingService.getOne(bookingId);
        if(!SecurityConfig.getCurrentUser().getId().equals(booking.getCreatedBy().getId()))
            throw new ForbiddenException("Access denied, this booking doesn't belongs to you");
        
        this.bookingService.cancelReservation(bookingId);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/mySells")
    private ResponseEntity getAdminAgentMySells( @RequestParam(value = "page", defaultValue = "0") Integer page){
        return ResponseEntity.ok(this.bookingService.getMySells(page));
    }
}

