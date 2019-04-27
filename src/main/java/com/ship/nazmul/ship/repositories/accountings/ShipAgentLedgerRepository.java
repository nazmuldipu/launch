package com.ship.nazmul.ship.repositories.accountings;

import com.ship.nazmul.ship.entities.accountings.ShipAgentLedger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShipAgentLedgerRepository  extends JpaRepository<ShipAgentLedger, Long>{
    List<ShipAgentLedger> findByAgentId(Long agentId);

    Page<ShipAgentLedger> findByAgentId(Long agentId, Pageable pageable);

    ShipAgentLedger findFirstByAgentIdOrderByIdDesc(Long agentId);
}
