package com.ship.nazmul.ship.controller.api.accounting;

import com.ship.nazmul.ship.config.security.SecurityConfig;
import com.ship.nazmul.ship.entities.User;
import com.ship.nazmul.ship.exceptions.forbidden.ForbiddenException;
import com.ship.nazmul.ship.exceptions.notfound.UserNotFoundException;
import com.ship.nazmul.ship.services.accountings.AdminShipLedgerService;
import com.ship.nazmul.ship.services.accountings.ShipAgentLedgerService;
import com.ship.nazmul.ship.services.accountings.ShipCashBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/serviceAdmin/accounting")
public class AccountingServiceAdminController {
    private final ShipCashBookService shipCashBookService;
    private final AdminShipLedgerService adminShipLedgerService;
    private final ShipAgentLedgerService shipAgentLedgerService;

    @Autowired
    public AccountingServiceAdminController(ShipCashBookService shipCashBookService, AdminShipLedgerService adminShipLedgerService, ShipAgentLedgerService shipAgentLedgerService) {
        this.shipCashBookService = shipCashBookService;
        this.adminShipLedgerService = adminShipLedgerService;
        this.shipAgentLedgerService = shipAgentLedgerService;
    }

    @PutMapping("/addAgentBalance/{agentId}")
    private ResponseEntity addAgentBalance(@PathVariable("agentId")Long agentId, @RequestParam("amount")int amount) throws UserNotFoundException, ForbiddenException {
        return ResponseEntity.ok(this.shipAgentLedgerService.addBalanceToShipAgent(agentId, amount));
    }

    @GetMapping("/shipCashbook")
    private ResponseEntity getHotelCashbook(@RequestParam(value = "page", defaultValue = "0") Integer page) throws ForbiddenException {
        //TODO: HIDE Hotel information from response info
        return ResponseEntity.ok(this.shipCashBookService.getAllShipCashbookForServiceAdmin(page));
    }

    @GetMapping("/hotelswaveLedger")
    private ResponseEntity getHotelswaveLedger(@RequestParam(value = "page", defaultValue = "0") Integer page){
        User user = SecurityConfig.getCurrentUser();
        return ResponseEntity.ok((this.adminShipLedgerService.getAdminShipLedgerByShipId(user.getShip().getId(), page)));
    }

    @GetMapping("/shipAgentLedger/{userId}")
    private ResponseEntity getHotelAgentLeger(@PathVariable("userId") Long userId, @RequestParam(value = "page", defaultValue = "0") Integer page) {
        return ResponseEntity.ok(this.shipAgentLedgerService.getShipAgentLedgerByAgentId(userId, page));
    }

    @PutMapping("/addIncome")
    private ResponseEntity addIncome(@RequestParam("credit")Integer debit, @RequestParam("explanation")String explanation) throws ForbiddenException {
        return ResponseEntity.ok(this.shipCashBookService.addShipCashbookEntry(debit,0,explanation));
    }

    @PutMapping("/addExpense")
    private ResponseEntity addExpense(@RequestParam("credit")Integer credit, @RequestParam("explanation")String explanation) throws ForbiddenException {
        return ResponseEntity.ok(this.shipCashBookService.addShipCashbookEntry(0,credit,explanation));
    }
}
