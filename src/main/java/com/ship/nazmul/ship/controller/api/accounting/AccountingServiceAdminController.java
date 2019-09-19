package com.ship.nazmul.ship.controller.api.accounting;

import com.ship.nazmul.ship.config.security.SecurityConfig;
import com.ship.nazmul.ship.entities.User;
import com.ship.nazmul.ship.exceptions.forbidden.ForbiddenException;
import com.ship.nazmul.ship.exceptions.notfound.NotFoundException;
import com.ship.nazmul.ship.services.accountings.ShipAdminCashbookService;
import com.ship.nazmul.ship.services.accountings.ShipAdminLedgerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/serviceAdmin/accounting")
public class AccountingServiceAdminController {
    private final ShipAdminLedgerService shipAdminLedgerService;
    private final ShipAdminCashbookService shipAdminCashbookService;

    @Autowired
    public AccountingServiceAdminController(ShipAdminLedgerService shipAdminLedgerService, ShipAdminCashbookService shipAdminCashbookService) {
        this.shipAdminLedgerService = shipAdminLedgerService;
        this.shipAdminCashbookService = shipAdminCashbookService;
    }

//    @PutMapping("/addAgentBalance/{agentId}")
//    private ResponseEntity addAgentBalance(@PathVariable("agentId")Long agentId,@RequestParam("shipId")Long shipId, @RequestParam("amount")int amount) throws NotFoundException, ForbiddenException {
//        return ResponseEntity.ok(this.shipAgentLedgerService.addBalanceToShipAgent(agentId, shipId, amount));
//    }

    @GetMapping("/shipAdminCashbook")
    private ResponseEntity getHotelCashbook(@RequestParam(value = "page", defaultValue = "0") Integer page) throws ForbiddenException {
        User currentUser = SecurityConfig.getCurrentUser();
        return ResponseEntity.ok(this.shipAdminCashbookService.getShipAdminCashbookForServiceAdmin(currentUser.getId(), page));
    }

    @GetMapping("/hotelswaveLedger")
    private ResponseEntity getShipAdminLedger(@RequestParam(value = "page", defaultValue = "0") Integer page){
        User currentUser = SecurityConfig.getCurrentUser();
       return ResponseEntity.ok(this.shipAdminLedgerService.getShipAdminLedgerByAdminId(currentUser.getId(), page));
    }

//    @GetMapping("/shipAgentLedger/{userId}")
//    private ResponseEntity getHotelAgentLeger(@PathVariable("userId") Long userId, @RequestParam(value = "page", defaultValue = "0") Integer page) {
//        return ResponseEntity.ok(this.shipAgentLedgerService.getShipAgentLedgerByAgentId(userId, page));
//    }

    @PutMapping("/addIncome")
    private ResponseEntity addIncome(@RequestParam("debit")Integer debit, @RequestParam("explanation")String explanation) throws ForbiddenException, NotFoundException {
        User user = SecurityConfig.getCurrentUser();
        return ResponseEntity.ok(this.shipAdminCashbookService.addShipAdminCashbookEntry(user.getId(), debit, 0, explanation));
    }

    @PutMapping("/addExpense")
    private ResponseEntity addExpense(@RequestParam("credit")Integer credit, @RequestParam("explanation")String explanation) throws ForbiddenException, NotFoundException {
        User user = SecurityConfig.getCurrentUser();
        return ResponseEntity.ok(this.shipAdminCashbookService.addShipAdminCashbookEntry(user.getId(), 0, credit, explanation));
    }
}
