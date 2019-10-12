package com.ship.nazmul.ship.controller.api.accounting;

import com.ship.nazmul.ship.exceptions.forbidden.ForbiddenException;
import com.ship.nazmul.ship.exceptions.notfound.NotFoundException;
import com.ship.nazmul.ship.exceptions.notfound.UserNotFoundException;
import com.ship.nazmul.ship.services.accountings.AdminAgentLedgerService;
import com.ship.nazmul.ship.services.accountings.AdminCashbookService;
//import com.ship.nazmul.ship.services.accountings.AdminShipLedgerService;
import com.ship.nazmul.ship.services.accountings.ShipAdminLedgerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/accounting")
public class AccountingAdminController {
    private final AdminCashbookService adminCashbookService;
    private final ShipAdminLedgerService shipAdminLedgerService;
//    private final AdminShipLedgerService adminShipLedgerService;
    private final AdminAgentLedgerService adminAgentLedgerService;

    @Autowired
    public AccountingAdminController(AdminCashbookService adminCashbookService, ShipAdminLedgerService shipAdminLedgerService, AdminAgentLedgerService adminAgentLedgerService) {
        this.adminCashbookService = adminCashbookService;
        this.shipAdminLedgerService = shipAdminLedgerService;
        this.adminAgentLedgerService = adminAgentLedgerService;
    }

    @PutMapping("/addAgentBalance/{agentId}")
    private ResponseEntity addAdminAgentBalance(@PathVariable("agentId") Long agentId, @RequestParam("amount") int amount) throws ForbiddenException, UserNotFoundException {
        return ResponseEntity.ok(this.adminAgentLedgerService.addBalanceToAdminAgent(agentId, amount));
    }

    @GetMapping("/adminAgentLedger/{userId}")
    private ResponseEntity getShipAgentLeger(@PathVariable("userId") Long userId, @RequestParam(value = "page", defaultValue = "0") Integer page) {
        return ResponseEntity.ok(this.adminAgentLedgerService.getAdminAgentLedgerByAgentId(userId, page));
    }

    @GetMapping("/cashbook")
    private ResponseEntity getAdminCashbook(@RequestParam(value = "page", defaultValue = "0") Integer page) {
        //TODO: HIDE Ship information from response info
//        this.adminCashbookService.updateFormat();
        return ResponseEntity.ok(this.adminCashbookService.getAllAdminCashbook(page));
    }

    @GetMapping("/shipAdminLedger/{adminId}")
    private ResponseEntity getShipLedger(@PathVariable("adminId") Long adminId, @RequestParam(value = "page", defaultValue = "0") Integer page) {
        return ResponseEntity.ok(this.shipAdminLedgerService.getShipAdminLedgerByAdminId(adminId, page));
    }

    @PutMapping("/addIncome")
    private ResponseEntity addIncome(@RequestParam("debit") Integer debit, @RequestParam("explanation") String explanation) throws ForbiddenException {
        return ResponseEntity.ok(this.adminCashbookService.addAdminCashbookEntry(debit, 0, explanation));
    }

    @PutMapping("/addExpense")
    private ResponseEntity addExpense(@RequestParam("credit") Integer credit, @RequestParam("explanation") String explanation) throws ForbiddenException {
        return ResponseEntity.ok(this.adminCashbookService.addAdminCashbookEntry(0, credit, explanation));
    }

    @PutMapping("/payShipAdmin/{adminId}")
    private ResponseEntity payToShip(@PathVariable("adminId") Long adminId, @RequestParam("amount") Integer amount) throws NotFoundException, ForbiddenException, javassist.NotFoundException {
        return ResponseEntity.ok(this.shipAdminLedgerService.payToShipAdmin(adminId, amount));
    }
}
