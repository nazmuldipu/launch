package com.ship.nazmul.ship.controller.api.ship;

import com.ship.nazmul.ship.entities.Ship;
import com.ship.nazmul.ship.entities.User;
import com.ship.nazmul.ship.exceptions.forbidden.ForbiddenException;
import com.ship.nazmul.ship.exceptions.invalid.InvalidException;
import com.ship.nazmul.ship.exceptions.notfound.NotFoundException;
import com.ship.nazmul.ship.services.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/admin/ships")
public class ShipAdminController {
    private final ShipService shipService;

    @Autowired
    public ShipAdminController(ShipService shipService) {
        this.shipService = shipService;
    }

    @GetMapping("")
    private ResponseEntity<Page<Ship>> getAllHotels(@RequestParam(value = "page", defaultValue = "0") Integer page){
        return ResponseEntity.ok(this.shipService.getAll(page));
    }

    @PostMapping("")
    private ResponseEntity<Ship> saveHotel(@RequestBody Ship ship) throws InvalidException, NotFoundException, ForbiddenException {
        return ResponseEntity.ok(this.shipService.save(ship));
    }

    @GetMapping("/{id}")
    private ResponseEntity<Ship> getOneHotel(@PathVariable("id") Long id) throws NotFoundException {
        return ResponseEntity.ok(this.shipService.getOne(id));
    }

    @PutMapping("/{id}")
    private ResponseEntity<Ship> updateHotel(@RequestBody Ship ship,
                                              @PathVariable("id") Long id) throws InvalidException, NotFoundException, ForbiddenException {
        ship.setId(id);
        return ResponseEntity.ok(this.shipService.save(ship));
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<?> deleteHotel(@PathVariable("id") Long id) throws ForbiddenException, NotFoundException, InvalidException {
        this.shipService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/shipMap/{shipId}")
    private ResponseEntity getShipMap(@PathVariable("shipId") Long shipId,
                                        @RequestParam("startDate")@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                        @RequestParam("endDate")@DateTimeFormat(pattern = "yyyy-MM-dd")  LocalDate endDate) throws NotFoundException {
        return ResponseEntity.ok(this.shipService.getShipMap(shipId, startDate, endDate));
    }
    @GetMapping("/checkShip/{shipId}")
    private ResponseEntity isShipActive(@PathVariable("shipId") Long shipId,
                                      @RequestParam("date")@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) throws NotFoundException {
        return ResponseEntity.ok(this.shipService.isShipActive(shipId, date));
    }
    @PutMapping("/updateMap/{shipId}")
    private ResponseEntity updateShipMap(@PathVariable("shipId") Long shipId,
                                         @RequestParam("date")@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
                                         @RequestParam("value")Boolean value) throws InvalidException, NotFoundException, ForbiddenException {
        return ResponseEntity.ok(this.shipService.updateShipMap(shipId, date, value));
    }
}
