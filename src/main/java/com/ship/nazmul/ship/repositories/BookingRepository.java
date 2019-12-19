package com.ship.nazmul.ship.repositories;

import com.ship.nazmul.ship.entities.Booking;
import com.ship.nazmul.ship.entities.SubBooking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    Page<Booking> findByCancelledFalse(Pageable pageable);

    Page<Booking> findByUserId(Long userId, Pageable pageable);

    Page<Booking> findByCreatedByIdAndCancelledFalse(Long userId, Pageable pageable);

    List<Booking> findByUserIdAndConfirmedFalse(Long userId);

    Page<Booking> findByUserIdAndConfirmedTrueAndPaidTrue(Long userId, Pageable pageable);

    Page<Booking> findByShipIdAndCancelledFalse(Long shipId, Pageable pageable);

    List<Booking> findByShipIdAndCreatedBetweenAndCancelledFalse(Long shipId, Date begin, Date end);

    List<Booking> findByShipIdAndCreatedBetween(Long shipId, Date begin, Date end);

    List<Booking> findByCreatedBetweenAndCancelledFalse(Date begin, Date end);

    List<Booking> findDistinctByShipIdAndSubBookingListDateAndCancelledFalse(Long shipId, LocalDate date);

    List<Booking> findDistinctByCreatedByIdAndShipIdAndSubBookingListDateAndCancelledFalse(Long userId, Long shipId, LocalDate date);

}
