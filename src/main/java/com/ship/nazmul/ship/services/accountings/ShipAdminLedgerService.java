package com.ship.nazmul.ship.services.accountings;

import com.ship.nazmul.ship.entities.accountings.ShipAdminLedger;
import com.ship.nazmul.ship.exceptions.forbidden.ForbiddenException;
import com.ship.nazmul.ship.exceptions.notfound.UserNotFoundException;
import javassist.NotFoundException;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ShipAdminLedgerService {
    ShipAdminLedger save(ShipAdminLedger shipAdminLedger);

    ShipAdminLedger getShipAdminLedger(Long id) throws NotFoundException;

    List<ShipAdminLedger> getAllShipAdminLedger();

    Page<ShipAdminLedger> getAllShipAdminLedger(int page);

    List<ShipAdminLedger> getShipAdminLedgerByAdminId(Long adminId);

    Page<ShipAdminLedger> getShipAdminLedgerByAdminId(Long adminId, Integer page);

    void deleteShipAdminLedger(Long id);

    boolean isExists(Long id);

    ShipAdminLedger addShipAdminLedger(Long adminId, ShipAdminLedger shipAdminLedger) throws NotFoundException;

    ShipAdminLedger payToShipAdmin(Long adminId, int amount) throws ForbiddenException, UserNotFoundException, NotFoundException;
}
