package com.ship.nazmul.ship.services.impl;

import com.ship.nazmul.ship.config.security.SecurityConfig;
import com.ship.nazmul.ship.entities.*;
import com.ship.nazmul.ship.entities.pojo.ServiceAdminSellsReport;
import com.ship.nazmul.ship.exceptions.forbidden.ForbiddenException;
import com.ship.nazmul.ship.services.BookingService;
import com.ship.nazmul.ship.services.CategoryService;
import com.ship.nazmul.ship.services.ReportService;
import com.ship.nazmul.ship.services.SeatService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {
    private final SeatService seatService;
    private final BookingService bookingService;
    private final CategoryService categoryService;

    @Autowired
    public ReportServiceImpl(SeatService seatService, BookingService bookingService, CategoryService categoryService) {
        this.seatService = seatService;
        this.bookingService = bookingService;
        this.categoryService = categoryService;
    }

    @Override
    public List<ServiceAdminSellsReport> getAdminSellsReport(Date date) throws ForbiddenException, ParseException {
        User user = SecurityConfig.getCurrentUser();
        if (!user.isAdmin()) throw new ForbiddenException("Access denied");

        List<Booking> bookingList = this.bookingService.getByCreateDateWithoutCanceled(date);
        return this.getAdminBookingReportFromBookingList(bookingList);
    }

    @Override
    public List<ServiceAdminSellsReport> getAdminReservationReport(Date date) throws ForbiddenException {
        User user = SecurityConfig.getCurrentUser();
        if (!user.isAdmin()) throw new ForbiddenException("Access denied");
        List<Seat> seats = this.seatService.getAll();
        return this.getAdminBookingReportFromSeatList(seats, date, true);
    }

    @Override
    public List<ServiceAdminSellsReport> getAdminSellsReportByShipId(Date date, Long shipId) throws ParseException {
        List<Booking> bookingList = this.bookingService.getBookingsByShipIdAndCreateDate(shipId, date);
        return this.getAdminBookingReportFromBookingList(bookingList);
    }

    @Override
    public List<ServiceAdminSellsReport> getAdminReservationReportByShipId(Date date, Long shipId) throws ForbiddenException {
        User user = SecurityConfig.getCurrentUser();
        if (!user.isAdmin()) throw new ForbiddenException("Access denied");
        List<Seat> seats = this.seatService.getSeatListByShipId(shipId);
        return this.getAdminBookingReportFromSeatList(seats, date, true);
    }

    @Override
    public List<ServiceAdminSellsReport> getServiceAdminSellsReport(Long shipId, Date date) throws ForbiddenException, ParseException {
        User user = SecurityConfig.getCurrentUser();
        if (user.getShips() == null || !user.hasRole(Role.ERole.ROLE_SERVICE_ADMIN.toString()))
            throw new ForbiddenException("Access denied");

        List<Booking> bookingList = this.bookingService.getBookingsByShipIdAndCreateDate(shipId, date);
        return this.getAdminBookingReportFromBookingList(bookingList);
    }

    @Override
    public List<ServiceAdminSellsReport> getServiceAdminReservationReport(Long shipId, Date date) throws ForbiddenException {
        User user = SecurityConfig.getCurrentUser();
        if (user.getShips() == null || !user.hasRole(Role.ERole.ROLE_SERVICE_ADMIN.toString()))
            throw new ForbiddenException("Access denied");

        List<Seat> roomList = this.seatService.getSeatListByShipId(shipId);
        return this.getAdminBookingReportFromSeatList(roomList, date, false);
    }

    @Override
    public JSONObject getServiceAdminDashboardReport(Date date) throws JSONException, ForbiddenException {
        User currentUser = SecurityConfig.getCurrentUser();
        if (currentUser.getShips() == null || !currentUser.hasRole(Role.ERole.ROLE_SERVICE_ADMIN.toString()))
            throw new ForbiddenException("Access denied");

        JSONObject obj = new JSONObject();
        obj.put("numberOfShips", currentUser.getShips().size());
        obj.put("ships", this.getUserShipsReportObjects(currentUser, date));

        return obj;
    }

    List<JSONObject> getUserShipsReportObjects(User user, Date date) throws JSONException {
        if (user.getShips().size() < 1) return null;
        List<JSONObject> list = new ArrayList<JSONObject>();
        for (Ship ship : user.getShips()) {
            JSONObject obj = new JSONObject();
            obj.put("id", ship.getId());
            obj.put("name", ship.getName());
            obj.put("shipNumber", ship.getShipNumber());
            obj.put("categories", this.getCategoryReportObjects(ship.getId(), date));
            obj.put("numberOfSeats", this.seatService.getSeatListByShipId(ship.getId()).size());
            list.add(obj);
        }
        return list;
    }

    List<JSONObject> getCategoryReportObjects(Long shipId, Date date) throws JSONException {
        List<Category> categoryList = this.categoryService.getCategoryByShipId(shipId);
        List<JSONObject> list = new ArrayList<JSONObject>();
        for (Category category : categoryList) {
            JSONObject obj = new JSONObject();
            obj.put("id", category.getId());
            obj.put("name", category.getName());
            obj.put("fare", category.getFare());
            obj.put("numberOfSeats", this.seatService.getSeatListByCategoryId(category.getId()).size());
            obj.put("sells", this.getReservationReport(category, date));
            list.add(obj);
        }
        return list;
    }

   JSONObject getReservationReport(Category category, Date date) throws JSONException{
        int reserved = 0;
        int blocked = 0;
        int sold = 0;
        List<Seat> seatList = this.seatService.getSeatListByCategoryId(category.getId());
        for (Seat seat : seatList) {
            Seat.EStatus status = seat.getSeatStatusMap().get(date);
            if (status == null || status.equals(Seat.EStatus.SEAT_FREE)) {

            } else if (status.equals(Seat.EStatus.SEAT_RESERVED)) {
                reserved++;
            } else if (status.equals(Seat.EStatus.SEAT_BLOCKED)) {
                blocked++;
            } else if (status.equals(Seat.EStatus.SEAT_SOLD)) {
                sold++;
            }
        }
        JSONObject obj = new JSONObject();
        obj.put("reserved", reserved);
        obj.put("blocked", blocked);
        obj.put("sold", sold);
        return obj;
    }

    //Generate booking report from RoomList
    private List<ServiceAdminSellsReport> getAdminBookingReportFromSeatList(List<Seat> seatList, Date date, boolean isAdmin) {
        List<ServiceAdminSellsReport> serviceAdminSellsReportList = new ArrayList<>();

        for (Seat seat : seatList) {
            ServiceAdminSellsReport serviceAdminSellsReport = new ServiceAdminSellsReport();
            String[] seats = {seat.getSeatNumber()};
            serviceAdminSellsReport.setSeatNumbers(seats);
            serviceAdminSellsReport.setShipName(seat.getShip().getName());
            serviceAdminSellsReport.setShipNumber(seat.getShip().getShipNumber());
            serviceAdminSellsReport.setRoutes(seat.getShip().getStartingPoint() + " - " + seat.getShip().getDroppingPoint());
            Long bookingId = seat.getBookingIdMap().get(date);
            if (bookingId != null) {
                Booking booking = this.bookingService.getOne(bookingId);
                if (!booking.isCancelled()) {
                    serviceAdminSellsReport.setJourneyDate(booking.getSubBookingList().get(0).getDate());
                    serviceAdminSellsReport.setBookingId(booking.getId());
                    serviceAdminSellsReport.setBookingStatus(booking.geteStatus());
                    serviceAdminSellsReport.setBookingDate(booking.getCreated());
                    serviceAdminSellsReport.setCustomerName(booking.getUser().getName());
                    serviceAdminSellsReport.setCustomerPhone(booking.getUser().getPhoneNumber());
                    serviceAdminSellsReport.setPrice(booking.getTotalPayablePrice());
                    serviceAdminSellsReport.setSoldBy(booking.getCreatedBy().getName());
                    serviceAdminSellsReport.setRole(booking.getCreatedBy().getRoles().get(0).getRole());
                    serviceAdminSellsReport.setPaid(booking.isPaid());
                }
            } else if (isAdmin) {
                continue;
            }
            serviceAdminSellsReportList.add(serviceAdminSellsReport);

        }
        return serviceAdminSellsReportList;
    }

    // Generate Booking report from booking list
    private List<ServiceAdminSellsReport> getAdminBookingReportFromBookingList(List<Booking> bookingList) throws ParseException {
        List<ServiceAdminSellsReport> serviceAdminBookingReportList = new ArrayList<>();
        for (Booking booking : bookingList) {
            if (!booking.isCancelled()) {
                serviceAdminBookingReportList.add(this.populateServiceAdminBookingReport(booking));
            }
        }
        return serviceAdminBookingReportList;
    }

    //Populate ServiceAdminBookingReport from SubBooking and Booking
    private ServiceAdminSellsReport populateServiceAdminBookingReport(Booking booking) throws ParseException {
        ServiceAdminSellsReport serviceAdminSellsReport = new ServiceAdminSellsReport();
        serviceAdminSellsReport.setJourneyDate(booking.getSubBookingList().get(0).getDate());
        serviceAdminSellsReport.setBookingId(booking.getId());
        serviceAdminSellsReport.setBookingDate(booking.getCreated());
        serviceAdminSellsReport.setBookingStatus(booking.geteStatus());
        serviceAdminSellsReport.setCustomerName(booking.getUser().getName());
        serviceAdminSellsReport.setCustomerPhone(booking.getUser().getPhoneNumber());
        serviceAdminSellsReport.setShipName(booking.getShip().getName());
        serviceAdminSellsReport.setShipNumber(booking.getShip().getShipNumber());
        serviceAdminSellsReport.setRoutes(booking.getShip().getStartingPoint() + " - " + booking.getShip().getDroppingPoint());
        String[] sets = new String[booking.getSubBookingList().size()];
        for (int i = 0; i < booking.getSubBookingList().size(); i++) {
            sets[i] = booking.getSubBookingList().get(i).getSeat().getSeatNumber();
        }
        serviceAdminSellsReport.setSeatNumbers(sets);
        serviceAdminSellsReport.setPrice(booking.getTotalPayablePrice());
        serviceAdminSellsReport.setSoldBy(booking.getCreatedBy().getName());
        serviceAdminSellsReport.setRole(booking.getCreatedBy().getRoles().get(0).getName());
        if (booking.isPaid()) {
            serviceAdminSellsReport.setPaid(true);
        } else {
            serviceAdminSellsReport.setPaid(false);
        }

        return serviceAdminSellsReport;
    }
}
