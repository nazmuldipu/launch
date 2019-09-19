//package com.ship.nazmul.ship.services.accountings;
//
//import com.ship.nazmul.ship.entities.accountings.AdminShipLedger;
//import com.ship.nazmul.ship.exceptions.forbidden.ForbiddenException;
//import com.ship.nazmul.ship.exceptions.notfound.NotFoundException;
//import org.springframework.data.domain.Page;
//
//import java.util.List;
//
//public interface AdminShipLedgerService {
//    AdminShipLedger save(AdminShipLedger adminAgentLedger);
//
//    AdminShipLedger getAdminShipLedger(Long shipId);
//
//    List<AdminShipLedger> getAllAdminShipLedger();
//
//    Page<AdminShipLedger> getAllAdminShipLedger(int page);
//
//    List<AdminShipLedger> getAdminShipLedgerByShipId(Long shipId);
//
//    Page<AdminShipLedger> getAdminShipLedgerByShipId(Long shipId, Integer page);
//
//    void deleteAdminShipLedger(Long ShipId);
//
//    boolean isExists(Long id);
//
//    AdminShipLedger updateAdminShipLedger(Long ShipId, AdminShipLedger adminShipLedger);
//
//    AdminShipLedger payToShip(Long ShipId, int amount) throws NotFoundException, ForbiddenException;
//
//}
