package com.ship.nazmul.ship.repositories.accountings;

import com.ship.nazmul.ship.entities.accountings.AdminCashbook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminCashbookRepository extends JpaRepository<AdminCashbook, Long> {
    AdminCashbook findFirstByOrderByIdDesc();
}
