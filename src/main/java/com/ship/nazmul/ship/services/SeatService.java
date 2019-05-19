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
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface SeatService {
    Seat save(long shipId, Seat seat) throws NotFoundException, ForbiddenException, InvalidException;

    Seat getOne(long seatId) throws NotFoundException;

    List<Seat> getAll();

    Page<Seat> getAll(int page) throws ForbiddenException;

    boolean exists(long id);

    void delete(long id);

    List<Seat> getSeatListByShipId(long shipId);

    Page<Seat> getSeatListByCategoryId(long categoryId, int page);

    List<Seat> getSeatListByCategoryId(long categoryId);

    Page<Seat> getSeatListByShipId(long shipId, int page);

    List<Seat> findByCategoryId(Long categoryId);

    List<Seat> getAvailableSeatByShipId(Long shipId, Date date) throws NotFoundException;

    List<Seat> getAvailableSeatByCategoryAndShipId(Long shipId, Long categoryId, Date date) throws NotFoundException;

    List<JSONObject> getSeatListWithBookingIdByShipId(Long shipId, Date date) throws JSONException, NotFoundException;

    Map<Date, Integer> getFareMap(Long roomId, Date startDate, Date endDate) throws NotFoundException;

    boolean checkSeatAvailability(Long seatId, Date date) throws NotFoundException;

    Seat updateSeatStatusAndBookingMap(Long seatId, Date date, Seat.EStatus status, Booking booking) throws NotFoundException, ForbiddenException, ParseException;

    Seat updateStatusMap(Long seatId, Date date, Seat.EStatus status) throws NotFoundException, ParseException;

    void clearSeatStatusAndBookingIdMap(Long seatId, Date date, Booking booking) throws ForbiddenException, NotFoundException, ParseException;

    public void removeBookingMap(Long seatId, Date date) throws NotFoundException;
}
