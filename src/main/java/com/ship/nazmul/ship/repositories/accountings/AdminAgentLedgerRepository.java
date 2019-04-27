package com.ship.nazmul.ship.repositories.accountings;

import com.ship.nazmul.ship.entities.accountings.AdminAgentLedger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminAgentLedgerRepository extends JpaRepository<AdminAgentLedger, Long> {
    List<AdminAgentLedger> findByAgentId(Long agentId);

    Page<AdminAgentLedger> findByAgentId(Long agentId, Pageable pageable);

    AdminAgentLedger findFirstByAgentIdOrderByIdDesc(Long agentId);
}