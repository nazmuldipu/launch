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

    @PostMapping("/sell")
    private ResponseEntity createBooking(@RequestBody Booking booking) throws ParseException, NotFoundException, ForbiddenException, UserAlreadyExistsException, NullPasswordException, UserInvalidException, javassist.NotFoundException {
        TicketDto ticketDto = ticketMapper.toTicket(this.bookingService.createServiceAgentBooking(booking));
        return ResponseEntity.ok(ticketDto);
    }

    @GetMapping("/mySells")
    private ResponseEntity getAdminAgentMySells( @RequestParam(value = "page", defaultValue = "0") Integer page){
        return ResponseEntity.ok(this.bookingService.getMySells(page));
    }
}

