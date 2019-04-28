package com.ship.nazmul.ship.controller.api.accounting;

import com.ship.nazmul.ship.exceptions.forbidden.ForbiddenException;
import com.ship.nazmul.ship.exceptions.notfound.NotFoundException;
import com.ship.nazmul.ship.exceptions.notfound.UserNotFoundException;
import com.ship.nazmul.ship.services.accountings.AdminAgentLedgerService;
import com.ship.nazmul.ship.services.accountings.AdminCashbookService;
import com.ship.nazmul.ship.services.accountings.AdminShipLedgerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/accounting")
public class AccountingAdminController {
    private final AdminCashbookService adminCashbookService;
    private final AdminShipLedgerService adminShipLedgerService;
    private final AdminAgentLedgerService adminAgentLedgerService;

    @Autowired
    public AccountingAdminController(AdminCashbookService adminCashbookService, AdminShipLedgerService adminShipLedgerService, AdminAgentLedgerService adminAgentLedgerService) {
        this.adminCashbookService = adminCashbookService;
        this.adminShipLedgerService = adminShipLedgerService;
        this.adminAgentLedgerService = adminAgentLedgerService;
    }

    @PutMapping("/addAgentBalance/{agentId}")
    private ResponseEntity addAdminAgentBalance(@PathVariable("agentId") Long agentId, @RequestParam("amount") int amount) throws ForbiddenException, UserNotFoundException {
        return ResponseEntity.ok(this.adminAgentLedgerService.addBalanceToAdminAgent(agentId, amount));
    }

    @GetMapping("/adminAgentLedger/{userId}")
    private ResponseEntity getHotelAgentLeger(@PathVariable("userId") Long userId, @RequestParam(value = "page", defaultValue = "0") Integer page) {
        return ResponseEntity.ok(this.adminAgentLedgerService.getAdminAgentLedgerByAgentId(userId, page));
    }

    @GetMapping("/cashbook")
    private ResponseEntity getAdminCashbook(@RequestParam(value = "page", defaultValue = "0") Integer page) {
        //TODO: HIDE Hotel information from response info
        return ResponseEntity.ok(this.adminCashbookService.getAllAdminCashbook(page));
    }

    @GetMapping("/hotelLedger/{hotelId}")
    private ResponseEntity getHotelLedger(@PathVariable("hotelId") Long hotelId, @RequestParam(value = "page", defaultValue = "0") Integer page) {
        return ResponseEntity.ok(this.adminShipLedgerService.getAdminShipLedgerByShipId(hotelId, page));
    }

    @PutMapping("/addIncome")
    private ResponseEntity addIncome(@RequestParam("credit") Integer debit, @RequestParam("explanation") String explanation) throws ForbiddenException {
        return ResponseEntity.ok(this.adminCashbookService.addAdminCashbookEntry(debit, 0, explanation));
    }

    @PutMapping("/addExpense")
    private ResponseEntity addExpense(@RequestParam("credit") Integer credit, @RequestParam("explanation") String explanation) throws ForbiddenException {
        return ResponseEntity.ok(this.adminCashbookService.addAdminCashbookEntry(0, credit, explanation));
    }

    @PutMapping("/payHotel/{hotelId}")
    private ResponseEntity payToHotel(@PathVariable("hotelId") Long hotelId, @RequestParam("amount") Integer amount) throws NotFoundException, ForbiddenException {
        return ResponseEntity.ok(this.adminShipLedgerService.payToShip(hotelId, amount));
    }
}
