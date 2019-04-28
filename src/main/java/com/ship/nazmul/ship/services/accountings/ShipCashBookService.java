package com.ship.nazmul.ship.services.accountings;

import com.ship.nazmul.ship.entities.accountings.ShipCashBook;
import com.ship.nazmul.ship.exceptions.forbidden.ForbiddenException;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ShipCashBookService {
    ShipCashBook save(ShipCashBook shipCashBook);

    ShipCashBook getShipCashBook(Long id);

    List<ShipCashBook> getAllShipCashBook();

    Page<ShipCashBook> getAllShipCashBook(int page);

    List<ShipCashBook> getAllShipCashbookForServiceAdmin() throws ForbiddenException;

    Page<ShipCashBook> getAllShipCashbookForServiceAdmin(Integer page) throws ForbiddenException;

    void deleteShipCashBook(Long id);

    boolean isExists(Long id);

    ShipCashBook debitAmount(ShipCashBook shipCashBook);

    ShipCashBook creditAmount(ShipCashBook shipCashBook);

    ShipCashBook addShipCashbookEntry(Integer debit, Integer credit, String explanation) throws ForbiddenException;
}
