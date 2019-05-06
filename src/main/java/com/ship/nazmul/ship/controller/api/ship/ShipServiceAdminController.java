package com.ship.nazmul.ship.controller.api.ship;

import com.ship.nazmul.ship.entities.Ship;
import com.ship.nazmul.ship.services.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/serviceAdmin/ships")
public class ShipServiceAdminController {
    private final ShipService shipService;

    @Autowired
    public ShipServiceAdminController(ShipService shipService) {
        this.shipService = shipService;
    }

    @GetMapping("/myShips")
    private ResponseEntity getServiceAdminShips(){
        Set<Ship> ships = this.shipService.getMyShips();
//        System.out.println(ships.toString());
        return ResponseEntity.ok(ships);
    }
}
