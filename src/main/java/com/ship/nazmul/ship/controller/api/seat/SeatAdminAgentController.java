package com.ship.nazmul.ship.controller.api.seat;

import com.ship.nazmul.ship.exceptions.notfound.NotFoundException;
import com.ship.nazmul.ship.services.SeatService;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/v1/adminAgent/seats")
public class SeatAdminAgentController {
    private final SeatService seatService;

    @Autowired
    public SeatAdminAgentController(SeatService seatService) {
        this.seatService = seatService;
    }

    @GetMapping("/available/{shipId}")
    private ResponseEntity getAllAvailableSeatForUser(@PathVariable("shipId") Long shipId,
                                                      @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) throws NotFoundException {
        return ResponseEntity.ok(this.seatService.getAvailableSeatByShipId(shipId, date));
    }

    @GetMapping("/statusList/{shipId}")
    private ResponseEntity getSeatBookingInfoByDate(@PathVariable("shipId") Long shipId,
                                                    @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date
    ) throws NotFoundException, JSONException {
        return ResponseEntity.ok(this.seatService.getSeatListWithBookingIdByShipId(shipId, date).toString());
    }
}
