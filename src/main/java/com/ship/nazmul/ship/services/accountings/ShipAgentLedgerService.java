package com.ship.nazmul.ship.services.accountings;

import com.ship.nazmul.ship.entities.accountings.ShipAgentLedger;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ShipAgentLedgerService {
    ShipAgentLedger save(ShipAgentLedger shipAgentLedger);

    ShipAgentLedger getShipAgentLedger(Long agentId);

    List<ShipAgentLedger> getAllShipAgentLedger();

    Page<ShipAgentLedger> getAllShipAgentLedger(int page);

    List<ShipAgentLedger> getShipAgentLedgerByAgentId(Long agentId);

    Page<ShipAgentLedger> getShipAgentLedgerByAgentId(Long agentId, int page);

    void deleteShipAgentLedger(Long agentId);

    boolean isExists(Long id);

    ShipAgentLedger updateBalanceAndSave(Long agentId, ShipAgentLedger shipAgentLedger);

    ShipAgentLedger addBalanceToShipAgent(Long agentId, int amount);

    ShipAgentLedger getAgentLastLedger(Long agentId);

    int getServiceAgentBalance(Long agentId);
}
