//package com.ship.nazmul.ship.repositories.accountings;
//
//import com.ship.nazmul.ship.entities.accountings.ShipCashBook;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public interface ShipCashBookRepository extends JpaRepository<ShipCashBook, Long> {
//    ShipCashBook findTopByOrderByIdDesc();
//
//    List<ShipCashBook> findByShipId(Long shipId);
//
//    Page<ShipCashBook> findByShipId(Long shipId, Pageable pageable);
//
//    Long countByShipId(Long ShipId);
//
//    ShipCashBook findFirstByShipIdOrderByIdDesc(Long shipId);
//
////    ShipCashBook findByShipIdAndTopByOrderByShipIdDesc(Long ShipId);
//}
