package com.ship.nazmul.ship.controller.api.ship;

import com.ship.nazmul.ship.entities.Ship;
import com.ship.nazmul.ship.exceptions.notfound.NotFoundException;
import com.ship.nazmul.ship.services.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ships")
public class ShipController {
    private final ShipService shipService;

    @Autowired
    public ShipController(ShipService shipService) {
        this.shipService = shipService;
    }

    @GetMapping("")
    public ResponseEntity<Page<Ship>> getAllShips(@RequestParam(value = "query", required = false, defaultValue = "") String query,
                                                   @RequestParam(value = "page", defaultValue = "0") Integer page) {
        return ResponseEntity.ok(this.shipService.searchShip(query, page));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ship> getShipWithId(@PathVariable("id") Long id) throws NotFoundException {
        return ResponseEntity.ok(this.shipService.getOne(id));
    }
}
