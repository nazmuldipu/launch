package com.ship.nazmul.ship.repositories.accountings;

import com.ship.nazmul.ship.entities.accountings.ShipAdminLedger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShipAdminLedgerRepository extends JpaRepository<ShipAdminLedger, Long> {
    List<ShipAdminLedger> findByUserId(Long userId);

    Page<ShipAdminLedger> findByUserId(Long userId, Pageable pageable);

    List<ShipAdminLedger> findByShipId(Long hotelId);

    Page<ShipAdminLedger> findByShipId(Long hotelId, Pageable pageable);

    Long countByShipId(Long hotelId);

    Long countByUserId(Long userId);

    ShipAdminLedger findFirstByShipIdOrderByIdDesc(Long hotelId);

    ShipAdminLedger findFirstByUserIdOrderByIdDesc(Long userId);
}
