package com.ship.nazmul.ship.repositories;

import com.ship.nazmul.ship.entities.Seat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByShipAndSeatNumberContainingIgnoreCase(Seat hotel, String query);

    List<Seat> findByShipIdAndCategoryId(Long shipId, Long categoryId);

    List<Seat> findByCategoryId(Long categoryId);

    Page<Seat> findByCategoryId(Long categoryId, Pageable pageable);

    List<Seat> findByShipIdAndCategoryIdOrderBySeatNumber(Long shipId, Long categoryId);

    List<Seat> findByShipIdAndCategoryIdOrderById(Long shipId, Long categoryId);

    List<Seat> findByShipIdOrderBySeatNumberAsc(Long shipId);

    Page<Seat> findByShipId(Long shipId, Pageable pageable);

    List<Seat> findByShipIdOrderBySeatNumber(Long shipId);

    Page<Seat> findByShipIdOrderBySeatNumber(Long shipId, Pageable pageable);

    List<Seat> findByShipId(Long shipId);



}