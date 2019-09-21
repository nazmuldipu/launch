package com.ship.nazmul.ship.controller.api.ship;

import com.ship.nazmul.ship.entities.Ship;
import com.ship.nazmul.ship.services.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/serviceAgent/ships")
public class ShipServiceAgentController {
    private final ShipService shipService;

    @Autowired
    public ShipServiceAgentController(ShipService shipService) {
        this.shipService = shipService;
    }

    @GetMapping("/myShips")
    private ResponseEntity getServiceAdminShips(){
        List<Ship> ships = this.shipService.getMyShips();
        return ResponseEntity.ok(ships);
    }
}
