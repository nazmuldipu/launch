package com.ship.nazmul.ship.controller.api.seat;

import com.ship.nazmul.ship.exceptions.notfound.NotFoundException;
import com.ship.nazmul.ship.services.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Date;

@RestController
@RequestMapping("/api/v1/serviceAgent/seats")
public class SeatServiceAgentController {
    private final SeatService seatService;

    @Autowired
    public SeatServiceAgentController(SeatService seatService) {
        this.seatService = seatService;
    }

    @GetMapping("/available/{shipId}")
    private ResponseEntity getAllAvailableSeatForUser(@PathVariable("shipId") Long shipId,
                                                      @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) throws NotFoundException, ParseException {
        return ResponseEntity.ok(this.seatService.getAvailableSeatByShipId(shipId, date));
    }
}
