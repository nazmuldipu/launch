package com.ship.nazmul.ship.services.impl;

import com.ship.nazmul.ship.commons.PageAttr;
import com.ship.nazmul.ship.commons.Validator;
import com.ship.nazmul.ship.commons.utils.DateUtil;
import com.ship.nazmul.ship.config.security.SecurityConfig;
import com.ship.nazmul.ship.entities.*;
import com.ship.nazmul.ship.exceptions.forbidden.ForbiddenException;
import com.ship.nazmul.ship.exceptions.invalid.InvalidException;
import com.ship.nazmul.ship.exceptions.notfound.NotFoundException;
import com.ship.nazmul.ship.repositories.SeatRepository;
import com.ship.nazmul.ship.services.CategoryService;
import com.ship.nazmul.ship.services.SeatService;
import com.ship.nazmul.ship.services.ShipService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.*;


@Service
public class SeatServiceImpl implements SeatService {
    private final SeatRepository seatRepository;
    private final ShipService shipService;
    private final CategoryService categoryService;

    @Autowired
    public SeatServiceImpl(SeatRepository seatRepository, ShipService shipService, CategoryService categoryService) {
        this.seatRepository = seatRepository;
        this.shipService = shipService;
        this.categoryService = categoryService;
    }

    @Override
    public Seat save(long shipId, Seat seat) throws NotFoundException, ForbiddenException, InvalidException {
        Ship ship = this.shipService.getOne(shipId);
        if (ship == null) throw new NotFoundException("Ship with id : " + ship + " not found");

        Category category = this.categoryService.getOne(seat.getCategory().getId());
        if (ship.getId() != category.getShip().getId()) throw new ForbiddenException("Access denied");

        if (seat.getId() != null) {
            seat.setBookingIdMap(seat.getBookingIdMap());
            seat.setSeatStatusMap(seat.getSeatStatusMap());
        }
        seat.setShip(ship);
        return this.seatRepository.save(seat);
    }

    @Override
    public Seat getOne(long seatId) throws NotFoundException {
        Seat seat = this.seatRepository.getOne(seatId);
        if (seat == null) throw new NotFoundException("Seat with id : " + seatId + " not found");
        return seat;
    }

    @Override
    public List<Seat> getAll() throws ForbiddenException {
        User user = SecurityConfig.getCurrentUser();
        if (!user.isAdmin()) throw new ForbiddenException("Access denied");
        return this.seatRepository.findAll();
    }

    @Override
    public Page<Seat> getAll(int page) throws ForbiddenException {
        User user = SecurityConfig.getCurrentUser();
        if (!user.isAdmin()) throw new ForbiddenException("Access denied");

        return this.seatRepository.findAll(PageAttr.getPageRequest(page));
    }

    @Override
    public boolean exists(long id) {
        return this.seatRepository.exists(id);
    }

    @Override
    public void delete(long id) {
        this.seatRepository.delete(id);
    }

    @Override
    public List<Seat> getSeatListByShipId(long shipId) {
        return this.seatRepository.findByShipId(shipId);
    }

    @Override
    public Page<Seat> getSeatListByShipId(long shipId, int page) {
        return this.seatRepository.findByShipId(shipId, PageAttr.getPageRequest(page));
    }

    @Override
    public List<Seat> findByCategoryId(Long categoryId) {
        return this.seatRepository.findByCategoryId(categoryId);
    }

    @Override
    public Page<Seat> getSeatListByCategoryId(long categoryId, int page) {
        return this.seatRepository.findByCategoryId(categoryId, PageAttr.getPageRequest(page));
    }

    @Override
    public List<Seat> getSeatListByCategoryId(long categoryId) {
        return this.seatRepository.findByCategoryId(categoryId);
    }

    /*Get available Seat list by ship Id and date
    * @param: ShipId = ship id for seat info
    * @param date =  Date to for the list
    * @output: List<Seat>: list of seat with fare, category and availability info*/
    @Override
    public List<Seat> getAvailableSeatByShipId(Long shipId, LocalDate date) throws NotFoundException, ParseException {
        // If ship is not active then return null
        if (!this.shipService.isShipActive(shipId, date)) {
            return null;
        }

        List<Category> categoryList = this.categoryService.getCategoryByShipId(shipId);
        System.out.println("D1 > category list size :" + categoryList.size() );
        List<Seat> seatList = new ArrayList<>();
        for (Category category : categoryList) {
            List<Seat> seats = this.getAvailableSeatByCategoryAndShipId(shipId, category.getId(), date);
            System.out.println("D2 > seatList size : " + seats.size() );
            if (seats.size() > 0) {
                Integer discount = this.categoryService.getDiscount(seats.get(0).getCategory().getId(), date);
                System.out.println("D3 > discount " + discount);
                seats.get(0).getCategory().setShip(null);
                if (discount != null && discount > 0) {
                    System.out.println("D4 > fare " + seats.get(0).getCategory().getFare());
                    seats.get(0).getCategory().setFare(seats.get(0).getCategory().getFare() - discount);
                    System.out.println("D5 > fare " + seats.get(0).getCategory().getFare());
                }
            }
            seatList.addAll(seats);
        }
        return seatList;
//        return this.getSeatAvailabilityFormSeatList(seatList, date);
    }

    @Override
    public List<Seat> getAvailableSeatByCategoryAndShipId(Long shipId, Long categoryId, LocalDate date) throws NotFoundException, ParseException {
        List<Seat> seatList = this.seatRepository.findByShipIdAndCategoryIdOrderById(shipId, categoryId);
        return this.getSeatAvailabilityFormSeatList(seatList, date);
    }

    private List<Seat> getSeatAvailabilityFormSeatList(List<Seat> seatList, LocalDate date) throws NotFoundException, ParseException {
        for (int i = 0; i < seatList.size(); i++) {
            seatList.get(i).setAvailable(true);
        }
        for (int j = 0; j < seatList.size(); j++) {
            seatList.get(j).setShip(null);//clear all ship info before sending it to the front end
            if (!this.checkSeatAvailability(seatList.get(j).getId(), date)) {
                seatList.get(j).setAvailable(false);
            }
        }

        return seatList;
    }

    @Override
    public List<JSONObject> getSeatListWithBookingIdByShipId(Long shipId, LocalDate date) throws JSONException, NotFoundException, ParseException {
        List<Seat> seatList = this.seatRepository.findByShipIdOrderBySeatNumber(shipId);

        List<JSONObject> list = new ArrayList<JSONObject>();
        for (Seat seat : seatList) {
            JSONObject obj = new JSONObject();
            obj.put("id", seat.getId());
            obj.put("seatNumber", seat.getSeatNumber());
            JSONObject cat = new JSONObject();
            cat.put("name", seat.getCategory().getName());
            cat.put("priority", seat.getCategory().getPriority());
            obj.put("category", cat);
            if (!this.checkSeatAvailability(seat.getId(), date)) {
                //TODO: remove following condition after debug period
                Long bookingId = seat.getBookingIdMap().get(date);
//                if(bookingId == null){
//                    bookingId = seat.getBookingIdMap().get(date);
//                }
                obj.put("bookingId", bookingId);
                Seat.EStatus status = seat.getSeatStatusMap().get(date);
                if (status == null) {
                    status = seat.getSeatStatusMap().get(date);
                }
                obj.put("status", status);
            } else {
                obj.put("bookingId", 0);
                obj.put("status", Seat.EStatus.SEAT_FREE.toString());
            }
            list.add(obj);
        }
        return list;
    }

    @Override
    public Map<LocalDate, Integer> getFareMap(Long roomId, LocalDate startDate, LocalDate endDate) throws NotFoundException {
        Seat seat = this.getOne(roomId);
        List<LocalDate> dates = DateUtil.getLocalDatesBetween(startDate, endDate);
        Map<LocalDate, Integer> fareMap = new HashMap<>();
//        for (int i = 0; i < dates.size(); i++) {
//            Integer discount = seat.getDiscountMap().get(dates.get(i));
//            if (discount == null) {
//                discount = 0;
//            }
//            fareMap.put(dates.get(i), (seat.getFare() - discount));
//        }
        return fareMap;
    }


    /*Check if a seat is available on a particular date
    * @param seatId     id for required seat
    * @param date       date to be search
    * @return true      if no data is available of status is free
    * */
    @Override
    public boolean checkSeatAvailability(Long seatId, LocalDate date) throws NotFoundException, ParseException {
        Seat seat = this.getOne(seatId);
        //TODO: remove print command after testing period complete
        Seat.EStatus status = seat.getSeatStatusMap().get(date);
//        if(status == null) {
//            System.out.println("D3 date: Found null");
//            status = seat.getSeatStatusMap().get(date);
//        }
        if (status == null || status == Seat.EStatus.SEAT_FREE)
            return true;
        return false;
    }

    @Override
    public Seat updateSeatStatusAndBookingMap(Long roomId, LocalDate date, Seat.EStatus status, Booking booking) throws NotFoundException, ForbiddenException, ParseException {
        User user = SecurityConfig.getCurrentUser();
        Seat seat = this.getOne(roomId);
        if (user.isOnlyUser() && booking.getUser().getId() != user.getId())
            throw new ForbiddenException("Access denied");
//        date = DateUtil.truncateTimeFromDate(date);
        seat.getSeatStatusMap().put(date, status);
        seat.getBookingIdMap().put(date, booking.getId());
        return this.seatRepository.save(seat);
    }

    @Override
    public Seat updateStatusMap(Long seatId, LocalDate date, Seat.EStatus status) throws NotFoundException, ParseException {
        Seat seat = this.getOne(seatId);
//        date = DateUtil.truncateTimeFromDate(date);
        seat.getSeatStatusMap().put(date, status);
        seat = this.seatRepository.save(seat);
        return seat;
    }

    private void clearStatusMap(Long seatId, LocalDate date) throws NotFoundException, ParseException {
        Seat seat = this.getOne(seatId);
        //TODO: remove following one line after debug period
        final Map<LocalDate, Seat.EStatus> seatStatusMap = new HashMap<>();
        for (final Map.Entry<LocalDate, Seat.EStatus> entry : seat.getSeatStatusMap().entrySet()) {
            if (!entry.getKey().equals(date)) {
                seatStatusMap.put(entry.getKey(), entry.getValue());
            }
        }
        seat.getSeatStatusMap().clear();
        this.seatRepository.save(seat);
        for (Map.Entry<LocalDate, Seat.EStatus> entry : seatStatusMap.entrySet()) {
            seat.getSeatStatusMap().put(entry.getKey(), entry.getValue());
            this.seatRepository.save(seat);
        }
        this.seatRepository.save(seat);
    }

    @Override
    public void clearSeatStatusAndBookingIdMap(Long seatId, LocalDate date, Booking booking) throws ForbiddenException, NotFoundException, ParseException {
        User currentUser = SecurityConfig.getCurrentUser();
        if (!currentUser.isAdmin() && !currentUser.hasRole(Role.ERole.ROLE_AGENT.toString()) && !(currentUser.hasRole(Role.ERole.ROLE_SERVICE_ADMIN.toString()) && Validator.containsShip(currentUser.getShips(), booking.getShip())))
            throw new ForbiddenException("Access denied");
        this.removeBookingMap(seatId, date);
        this.clearStatusMap(seatId, date);
    }

    @Override
    public void removeBookingMap(Long seatId, LocalDate date) throws NotFoundException {
        Seat seat = this.getOne(seatId);
        seat.getBookingIdMap().remove(date);
        this.seatRepository.save(seat);
    }
}
