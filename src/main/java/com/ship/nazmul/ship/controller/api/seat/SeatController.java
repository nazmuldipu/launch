package com.ship.nazmul.ship.controller.api.seat;

import com.ship.nazmul.ship.entities.Seat;
import com.ship.nazmul.ship.exceptions.notfound.NotFoundException;
import com.ship.nazmul.ship.services.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/seats")
public class SeatController {
    private final SeatService seatService;

    @Autowired
    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    @GetMapping("/available/{shipId}")
    private ResponseEntity getAllAvailableSeatForUser(@PathVariable("shipId") Long shipId,
                                                      @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) throws NotFoundException, ParseException {
        List<Seat> seatList = this.seatService.getAvailableSeatByShipId(shipId, date);
            if(seatList == null ){
                Map<String, String> response = new HashMap<>();
                response.put("response", "No ship on " + date.toString());
                return ResponseEntity.ok(response);
            }
        return ResponseEntity.ok(this.seatService.getAvailableSeatByShipId(shipId, date));
    }
}
