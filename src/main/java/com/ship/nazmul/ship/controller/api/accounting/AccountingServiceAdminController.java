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

    @GetMapping("/shipAdminCashbook/{adminId}")
    private ResponseEntity getHotelCashbook(@PathVariable("adminId")Long adminId, @RequestParam(value = "page", defaultValue = "0") Integer page) throws ForbiddenException {
        //TODO: HIDE Hotel information from response info
        return ResponseEntity.ok(this.shipAdminCashbookService.getShipAdminCashbookForServiceAdmin(adminId, page));
    }

    @GetMapping("/shipAdminLedger/{adminId}")
    private ResponseEntity getShipAdminLedger(@PathVariable("adminId")Long adminId, @RequestParam(value = "page", defaultValue = "0") Integer page){
        User user = SecurityConfig.getCurrentUser();
        return ResponseEntity.ok(this.shipAdminLedgerService.getShipAdminLedgerByAdminId(adminId, page));
    }

//    @GetMapping("/shipAgentLedger/{userId}")
//    private ResponseEntity getHotelAgentLeger(@PathVariable("userId") Long userId, @RequestParam(value = "page", defaultValue = "0") Integer page) {
//        return ResponseEntity.ok(this.shipAgentLedgerService.getShipAgentLedgerByAgentId(userId, page));
//    }

    @PutMapping("/addIncome/{adminId}")
    private ResponseEntity addIncome(@PathVariable("adminId")Long adminId, @RequestParam("debit")Integer debit, @RequestParam("explanation")String explanation) throws ForbiddenException, NotFoundException {
        return ResponseEntity.ok(this.shipAdminCashbookService.addShipAdminCashbookEntry(adminId, debit, 0, explanation));
    }

    @PutMapping("/addExpense/{adminId}")
    private ResponseEntity addExpense(@PathVariable("adminId")Long adminId, @RequestParam("credit")Integer credit, @RequestParam("explanation")String explanation) throws ForbiddenException, NotFoundException {
        return ResponseEntity.ok(this.shipAdminCashbookService.addShipAdminCashbookEntry(adminId, 0, credit, explanation));
    }
}
