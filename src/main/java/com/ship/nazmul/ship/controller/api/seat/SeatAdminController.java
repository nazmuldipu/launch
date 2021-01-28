package com.ship.nazmul.ship.controller.api.seat;

import com.ship.nazmul.ship.entities.Seat;
import com.ship.nazmul.ship.exceptions.forbidden.ForbiddenException;
import com.ship.nazmul.ship.exceptions.invalid.InvalidException;
import com.ship.nazmul.ship.exceptions.notfound.NotFoundException;
import com.ship.nazmul.ship.services.SeatService;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/seats")
public class SeatAdminController {
    private final SeatService seatService;

    @Autowired
    public SeatAdminController(SeatService seatService) {
        this.seatService = seatService;
    }

    @PostMapping("/{shipId}")
    private ResponseEntity<Seat> saveSeat(@PathVariable("shipId") Long shipId, @RequestBody Seat seat) throws InvalidException, NotFoundException, ForbiddenException {
        return ResponseEntity.ok(this.seatService.save(shipId, seat));
    }

    @GetMapping("/all/{shipId}")
    private ResponseEntity getAllSeat(@PathVariable("shipId") Long shipId,
                                             @RequestParam(value = "page", defaultValue = "0") Integer page) {
        return ResponseEntity.ok(this.seatService.getSeatListByShipId(shipId, page));
    }

    @GetMapping("/list/{shipId}")
    private ResponseEntity<List<Seat>> getAllRoomList(@PathVariable("shipId") Long shipId) {
        return ResponseEntity.ok(this.seatService.getSeatListByShipId(shipId));
    }

    @GetMapping("/listByCategory/{categoryId}")
    private ResponseEntity getSeatListByCategoryId(@PathVariable("categoryId")Long categoryId){
        return ResponseEntity.ok(this.seatService.getSeatListByCategoryId(categoryId));
    }

    @GetMapping("/available/{shipId}")
    private ResponseEntity getAllAvailableSeatForAdmin(@PathVariable("shipId") Long shipId,
                                                      @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) throws NotFoundException, ParseException {
        return ResponseEntity.ok(this.seatService.getAvailableSeatByShipId(shipId, date));
    }

    @GetMapping("/statusList/{shipId}")
    private ResponseEntity getSeatBookingInfoByDate(@PathVariable("shipId") Long shipId,
                                                    @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)  LocalDate date
    ) throws NotFoundException, JSONException, ParseException {
        return ResponseEntity.ok(this.seatService.getSeatListWithBookingIdByShipId(shipId, date).toString());
    }

    @PutMapping("/update/{shipId}/{seatId}")
    private ResponseEntity<Seat> updateSeat(@PathVariable("shipId") Long shipId, @PathVariable("seatId") Long seatId, @RequestBody Seat seat) throws NotFoundException, ForbiddenException, InvalidException {
        seat.setId(seatId);
        return ResponseEntity.ok(this.seatService.save(shipId, seat));
    }
}
