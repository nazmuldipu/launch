package com.ship.nazmul.ship.repositories;

import com.ship.nazmul.ship.entities.Ship;
import com.ship.nazmul.ship.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShipRepository extends JpaRepository<Ship, Long> {
//    Ship findByUser(User user);

    List<Ship> findByDeletedFalse();

    Page<Ship> findByDeletedFalse(Pageable pageable);

    Page<Ship> findDistinctByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseOrStartingPointContainingIgnoreCaseOrDroppingPointContainingIgnoreCaseAndDeletedFalse(String name, String description, String startingPoint,  String droppingPoint, Pageable pageable);


}
