//package com.ship.nazmul.ship.repositories.accountings;
//
//import com.ship.nazmul.ship.entities.accountings.AdminShipLedger;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public interface AdminShipLedgerRepository extends JpaRepository<AdminShipLedger, Long> {
//
//    List<AdminShipLedger> findByShipId(Long hotelId);
//
//    Page<AdminShipLedger> findByShipId(Long hotelId, Pageable pageable);
//
//    Long countByShipId(Long hotelId);
//
//    AdminShipLedger findFirstByShipIdOrderByIdDesc(Long hotelId);
//}
