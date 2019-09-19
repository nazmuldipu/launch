//package com.ship.nazmul.ship.controller.api.accounting;
//
//import com.ship.nazmul.ship.config.security.SecurityConfig;
//import com.ship.nazmul.ship.services.accountings.ShipAgentLedgerService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/v1/serviceAgent/accounting")
//public class AccountingServiceAgentController {
//    private final ShipAgentLedgerService shipAgentLedgerService;
//
//    @Autowired
//    public AccountingServiceAgentController(ShipAgentLedgerService shipAgentLedgerService) {
//        this.shipAgentLedgerService = shipAgentLedgerService;
//    }
//
//    @GetMapping("/myLedger")
//    private ResponseEntity getHotelAgentLeger(@RequestParam(value = "page", defaultValue = "0") Integer page) {
//        return ResponseEntity.ok(this.shipAgentLedgerService.getShipAgentLedgerByAgentId(SecurityConfig.getCurrentUser().getId(), page));
//    }
//
//    @GetMapping("/myBalance")
//    private ResponseEntity getHotelAgentBalance(){
//        Map<String, String> response = new HashMap<>();
//        response.put("response", this.shipAgentLedgerService.getServiceAgentBalance(SecurityConfig.getCurrentUser().getId()) + "");
//        return ResponseEntity.ok(response);
//    }
//}
