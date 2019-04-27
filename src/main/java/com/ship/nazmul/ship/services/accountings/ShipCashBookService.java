package com.ship.nazmul.ship.services.accountings;

import com.ship.nazmul.ship.entities.accountings.ShipCashBook;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ShipCashBookService {
    ShipCashBook save(ShipCashBook shipCashBook);

    ShipCashBook getShipCashBook(Long id);

    List<ShipCashBook> getAllShipCashBook();

    Page<ShipCashBook> getAllShipCashBook(int page);

    List<ShipCashBook> getAllShipCashbookForServiceAdmin();

    Page<ShipCashBook> getAllShipCashbookForServiceAdmin(Integer page);

    void deleteShipCashBook(Long id);

    boolean isExists(Long id);

    ShipCashBook debitAmount(ShipCashBook shipCashBook);

    ShipCashBook creditAmount(ShipCashBook shipCashBook);

    ShipCashBook addShipCashbookEntry(Integer debit, Integer credit, String explanation);
}
