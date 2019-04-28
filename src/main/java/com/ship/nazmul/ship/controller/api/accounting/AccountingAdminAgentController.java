package com.ship.nazmul.ship.controller.api.accounting;

import com.ship.nazmul.ship.config.security.SecurityConfig;
import com.ship.nazmul.ship.services.accountings.AdminAgentLedgerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/adminAgent/accounting")
public class AccountingAdminAgentController {
    private final AdminAgentLedgerService adminAgentLedgerService;

    @Autowired
    public AccountingAdminAgentController(AdminAgentLedgerService adminAgentLedgerService) {
        this.adminAgentLedgerService = adminAgentLedgerService;
    }

    @GetMapping("/myLedger")
    private ResponseEntity getHotelAgentLeger(@RequestParam(value = "page", defaultValue = "0") Integer page) {
        return ResponseEntity.ok(this.adminAgentLedgerService.getAdminAgentLedgerByAgentId(SecurityConfig.getCurrentUser().getId(), page));
    }

    @GetMapping("/myBalance")
    private ResponseEntity getHotelAgentBalance(){
        Map<String, String> response = new HashMap<>();
        response.put("response", this.adminAgentLedgerService.getAdminAgentBalance(SecurityConfig.getCurrentUser().getId()) + "");
        return ResponseEntity.ok(response);
    }
}
