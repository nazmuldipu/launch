package com.ship.nazmul.ship.controller.api.accounting;

import com.ship.nazmul.ship.config.security.SecurityConfig;
import com.ship.nazmul.ship.entities.User;
import com.ship.nazmul.ship.exceptions.forbidden.ForbiddenException;
import com.ship.nazmul.ship.exceptions.notfound.NotFoundException;
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
    private ResponseEntity addAgentBalance(@PathVariable("agentId")Long agentId,@RequestParam("shipId")Long shipId, @RequestParam("amount")int amount) throws NotFoundException, ForbiddenException {
        return ResponseEntity.ok(this.shipAgentLedgerService.addBalanceToShipAgent(agentId, shipId, amount));
    }

    @GetMapping("/shipCashbook/{shipId}")
    private ResponseEntity getHotelCashbook(@PathVariable("shipId")Long shipId, @RequestParam(value = "page", defaultValue = "0") Integer page) throws ForbiddenException {
        //TODO: HIDE Hotel information from response info
        return ResponseEntity.ok(this.shipCashBookService.getShipCashbookForServiceAdmin(shipId, page));
    }

    @GetMapping("/hotelswaveLedger/{shipId}")
    private ResponseEntity getHotelswaveLedger(@PathVariable("shipId")Long shipId, @RequestParam(value = "page", defaultValue = "0") Integer page){
        User user = SecurityConfig.getCurrentUser();
        return ResponseEntity.ok((this.adminShipLedgerService.getAdminShipLedgerByShipId(shipId, page)));
    }

    @GetMapping("/shipAgentLedger/{userId}")
    private ResponseEntity getHotelAgentLeger(@PathVariable("userId") Long userId, @RequestParam(value = "page", defaultValue = "0") Integer page) {
        return ResponseEntity.ok(this.shipAgentLedgerService.getShipAgentLedgerByAgentId(userId, page));
    }

    @PutMapping("/addIncome/{shipId}")
    private ResponseEntity addIncome(@PathVariable("shipId")Long shipId, @RequestParam("debit")Integer debit, @RequestParam("explanation")String explanation) throws ForbiddenException, NotFoundException {
        return ResponseEntity.ok(this.shipCashBookService.addShipCashbookEntry(shipId,debit,0,explanation));
    }

    @PutMapping("/addExpense/{shipId}")
    private ResponseEntity addExpense(@PathVariable("shipId")Long shipId, @RequestParam("credit")Integer credit, @RequestParam("explanation")String explanation) throws ForbiddenException, NotFoundException {
        return ResponseEntity.ok(this.shipCashBookService.addShipCashbookEntry(shipId,0,credit,explanation));
    }
}
