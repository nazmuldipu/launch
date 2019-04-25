package com.ship.nazmul.ship.repositories;

import com.ship.nazmul.ship.entities.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    Page<Booking> findByUserId(Long userId, Pageable pageable);

    Page<Booking> findByCreatedById(Long userId, Pageable pageable);

    List<Booking> findByUserIdAndConfirmedFalse(Long userId);

    Page<Booking> findByUserIdAndConfirmedTrueAndPaidTrue(Long userId, Pageable pageable);

    Page<Booking> findByShipId(Long shipId, Pageable pageable);

    List<Booking> findByShipIdAndCreatedBetween(Long shipId, Date begin, Date end);

    List<Booking> findByCreatedBetween(Date begin, Date end);

}
