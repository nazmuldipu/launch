package com.ship.nazmul.ship.services.accountings;

import com.ship.nazmul.ship.entities.accountings.AdminCashbook;
import com.ship.nazmul.ship.exceptions.forbidden.ForbiddenException;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AdminCashbookService {
    AdminCashbook save(AdminCashbook adminCashbook);

    AdminCashbook getAdminCashbook(Long id);

    List<AdminCashbook> getAllAdminCashbook();

    Page<AdminCashbook> getAllAdminCashbook(int page);

    boolean isExists(Long id);

    AdminCashbook updateBalanceAndSave(AdminCashbook adminCashbook);

    AdminCashbook addAdminCashbookEntry(Integer debit, Integer credit, String explanation) throws ForbiddenException;

}
