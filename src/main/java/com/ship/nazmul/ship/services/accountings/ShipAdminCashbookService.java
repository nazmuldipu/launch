package com.ship.nazmul.ship.services.accountings;

import com.ship.nazmul.ship.entities.accountings.ShipAdminCashbook;
import com.ship.nazmul.ship.exceptions.forbidden.ForbiddenException;
import com.ship.nazmul.ship.exceptions.notfound.UserNotFoundException;
import javassist.NotFoundException;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface ShipAdminCashbookService {
    ShipAdminCashbook save(ShipAdminCashbook shipAdminCashbook);

    ShipAdminCashbook getShipAdminCashBook(Long id);

    List<ShipAdminCashbook> getAllShipAdminCashBook();

    List<ShipAdminCashbook> getShipAdminCashBook(LocalDate date);

    Page<ShipAdminCashbook> getAllShipAdminCashBook(int page);

    List<ShipAdminCashbook> getShipAdminCashbookForServiceAdmin(Long userId);

    Page<ShipAdminCashbook> getShipAdminCashbookForServiceAdmin(Long userId, Integer page);

    void deleteShipAdminCashBook(Long id);

    boolean isExists(Long id);

    ShipAdminCashbook debitAmount(ShipAdminCashbook shipAdminCashbook);

    ShipAdminCashbook creditAmount(ShipAdminCashbook shipAdminCashbook);

    ShipAdminCashbook addShipAdminCashbookEntry(Long userId, Integer debit, Integer credit, String explanation) throws ForbiddenException, UserNotFoundException;

}
