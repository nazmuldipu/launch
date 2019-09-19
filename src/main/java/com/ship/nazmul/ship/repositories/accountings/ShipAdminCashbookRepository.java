package com.ship.nazmul.ship.repositories.accountings;

import com.ship.nazmul.ship.entities.accountings.ShipAdminCashbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShipAdminCashbookRepository  extends JpaRepository<ShipAdminCashbook, Long> {
    ShipAdminCashbook findTopByOrderByIdDesc();

    List<ShipAdminCashbook> findByUserId(Long userId);

    Page<ShipAdminCashbook> findByUserIdOrderByIdAsc(Long userId, Pageable pageable);

    Long countByUserId(Long ShipId);

    ShipAdminCashbook findFirstByUserIdOrderByIdDesc(Long userId);
}
