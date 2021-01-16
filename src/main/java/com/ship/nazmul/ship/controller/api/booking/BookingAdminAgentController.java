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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/api/v1/adminAgent/booking")
public class BookingAdminAgentController {
    private final BookingService bookingService;
    private final TicketMapper ticketMapper;

    @Autowired
    public BookingAdminAgentController(BookingService bookingService, TicketMapper ticketMapper) {
        this.bookingService = bookingService;
        this.ticketMapper = ticketMapper;
    }

    @GetMapping("/mySells")
    private ResponseEntity getAdminAgentMySells( @RequestParam(value = "page", defaultValue = "0") Integer page){
        return ResponseEntity.ok(this.bookingService.getMySells(page));
    }

    @PostMapping("/sell")
    private ResponseEntity createAdminAgentBooking(@RequestBody Booking booking) throws ParseException, NotFoundException, ForbiddenException, UserAlreadyExistsException, NullPasswordException, UserInvalidException, javassist.NotFoundException {
        TicketDto ticketDto = ticketMapper.toTicket(this.bookingService.createAdminAgentBooking(booking));
        return ResponseEntity.ok(ticketDto);
    }

    @PutMapping("/confirmReservation/{bookingId}")
    private ResponseEntity confirmReservation(@PathVariable("bookingId")Long bookingId) throws NullPasswordException, UserAlreadyExistsException, UserInvalidException, NotFoundException, ParseException, javassist.NotFoundException, ForbiddenException {

        return ResponseEntity.ok(this.bookingService.confirmReservation(bookingId));
    }

    @DeleteMapping("/cancelReservation/{bookingId}")
    private ResponseEntity cancelReservation(@PathVariable("bookingId")Long bookingId) throws UserInvalidException, ForbiddenException, ParseException, NullPasswordException, NotFoundException, UserAlreadyExistsException {

        this.bookingService.cancelReservation(bookingId);
        return ResponseEntity.ok().build();
    }
}
