package com.ship.nazmul.ship.services;

import com.ship.nazmul.ship.entities.Booking;
import com.ship.nazmul.ship.entities.Seat;
import com.ship.nazmul.ship.exceptions.forbidden.ForbiddenException;
import com.ship.nazmul.ship.exceptions.invalid.InvalidException;
import com.ship.nazmul.ship.exceptions.notfound.NotFoundException;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.data.domain.Page;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface SeatService {
    Seat save(long shipId, Seat seat) throws NotFoundException, ForbiddenException, InvalidException;

    Seat getOne(long seatId) throws NotFoundException;

    List<Seat> getAll() throws ForbiddenException;

    Page<Seat> getAll(int page) throws ForbiddenException;

    boolean exists(long id);

    void delete(long id);

    List<Seat> getSeatListByShipId(long shipId);

    Page<Seat> getSeatListByCategoryId(long categoryId, int page);

    List<Seat> getSeatListByCategoryId(long categoryId);

    Page<Seat> getSeatListByShipId(long shipId, int page);

    List<Seat> findByCategoryId(Long categoryId);

    List<Seat> getAvailableSeatByShipId(Long shipId, LocalDate date) throws NotFoundException, ParseException;

    List<Seat> getAvailableSeatByCategoryAndShipId(Long shipId, Long categoryId, LocalDate date) throws NotFoundException, ParseException;

    List<JSONObject> getSeatListWithBookingIdByShipId(Long shipId, LocalDate date) throws JSONException, NotFoundException, ParseException;

    Map<LocalDate, Integer> getFareMap(Long roomId, LocalDate startDate, LocalDate endDate) throws NotFoundException;

    boolean checkSeatAvailability(Long seatId, LocalDate date) throws NotFoundException, ParseException;

    Seat updateSeatStatusAndBookingMap(Long seatId, LocalDate date, Seat.EStatus status, Booking booking) throws NotFoundException, ForbiddenException, ParseException;

    Seat updateStatusMap(Long seatId, LocalDate date, Seat.EStatus status) throws NotFoundException, ParseException;

    void clearSeatStatusAndBookingIdMap(Long seatId, LocalDate date, Booking booking) throws ForbiddenException, NotFoundException, ParseException;

    public void removeBookingMap(Long seatId, LocalDate date) throws NotFoundException;
}
