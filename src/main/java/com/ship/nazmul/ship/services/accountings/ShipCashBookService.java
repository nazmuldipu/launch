//package com.ship.nazmul.ship.services.accountings;
//
//import com.ship.nazmul.ship.entities.accountings.ShipCashBook;
//import com.ship.nazmul.ship.exceptions.forbidden.ForbiddenException;
//import com.ship.nazmul.ship.exceptions.notfound.NotFoundException;
//import org.springframework.data.domain.Page;
//
//import java.util.List;
//
//public interface ShipCashBookService {
//    ShipCashBook save(ShipCashBook shipCashBook);
//
//    ShipCashBook getShipCashBook(Long id);
//
//    List<ShipCashBook> getAllShipCashBook();
//
//    Page<ShipCashBook> getAllShipCashBook(int page);
//
//    List<ShipCashBook> getShipCashbookForServiceAdmin(Long shipId) throws ForbiddenException;
//
//    Page<ShipCashBook> getShipCashbookForServiceAdmin(Long shpId, Integer page) throws ForbiddenException;
//
//    void deleteShipCashBook(Long id);
//
//    boolean isExists(Long id);
//
//    ShipCashBook debitAmount(ShipCashBook shipCashBook);
//
//    ShipCashBook creditAmount(ShipCashBook shipCashBook);
//
//    ShipCashBook addShipCashbookEntry(Long shipId, Integer debit, Integer credit, String explanation) throws ForbiddenException, NotFoundException;
//}
