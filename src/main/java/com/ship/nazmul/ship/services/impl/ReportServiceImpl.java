package com.ship.nazmul.ship.services.impl;

import com.ship.nazmul.ship.commons.utils.DateUtil;
import com.ship.nazmul.ship.config.security.SecurityConfig;
import com.ship.nazmul.ship.entities.*;
import com.ship.nazmul.ship.entities.pojo.ServiceAdminSellsReport;
import com.ship.nazmul.ship.entities.pojo.ServiceAdminSellsReportRange;
import com.ship.nazmul.ship.exceptions.forbidden.ForbiddenException;
import com.ship.nazmul.ship.services.BookingService;
import com.ship.nazmul.ship.services.CategoryService;
import com.ship.nazmul.ship.services.ReportService;
import com.ship.nazmul.ship.services.SeatService;
import org.apache.tomcat.jni.Local;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalDate;
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
    public List<ServiceAdminSellsReport> getAdminSellsReport(LocalDate date) throws ForbiddenException, ParseException {
        User user = SecurityConfig.getCurrentUser();
        if (!user.isAdmin()) throw new ForbiddenException("Access denied");

        List<Booking> bookingList = this.bookingService.getByCreateDateWithoutCanceled(DateUtil.convertToDateViaSqlDate(date));
        return this.getAdminBookingReportFromBookingList(bookingList);
    }

    /*GET Sells report for admin for a date range
     * @param startDate      report start date
     * @param endDate        report end date
     * @return List<ServiceAdminSellsReportRange>    list of Service admin sells report
     * */
    @Override
    public List<ServiceAdminSellsReportRange> getAdminSellsReportRange(LocalDate startDate, LocalDate endDate) throws ForbiddenException, ParseException {
        List<LocalDate> dateList = DateUtil.getLocalDatesBetween(startDate, endDate);
        List<ServiceAdminSellsReportRange> serviceAdminSellsReportRanges = new ArrayList<>();
        for (LocalDate date : dateList) {
            List<ServiceAdminSellsReport> sasrList = this.getAdminSellsReport(date);
            serviceAdminSellsReportRanges = this.populateServiceAdminSellsReportRangeList(sasrList, date, serviceAdminSellsReportRanges);
        }

        return serviceAdminSellsReportRanges;
    }

    //
    /*Populate ServiceAdminBookingReportRange using ServiceAdminBookingReport List and date
     * @param List<ServiceAdminSellsReport>          row data that needs to be organized
     * @param date                                   provided information date
     * @param List<ServiceAdminSellsReportRange>     existed ServiceAdminSellsReportRange list
     * @return List<ServiceAdminSellsReportRange>    populated ServiceAdminSellsReportRange list*/
    private List<ServiceAdminSellsReportRange> populateServiceAdminSellsReportRangeList(List<ServiceAdminSellsReport> sasrList, LocalDate date, List<ServiceAdminSellsReportRange> sasrrList) {
        for (ServiceAdminSellsReport sasr : sasrList) {
            if (sasr.getBookingStatus() != null && !sasr.getBookingStatus().toString().equals(Seat.EStatus.SEAT_SOLD.toString())) //if booking status is not sold then skip
                continue;

            ServiceAdminSellsReportRange sasrr = sasrrList.stream()
                    .filter(sa -> sasr.getShipName().equals(sa.getShipName()) && sa.getDate().compareTo(date) == 0)
                    .findAny()
                    .orElse(null);
            if (sasrr == null) {
                ServiceAdminSellsReportRange sasrN = new ServiceAdminSellsReportRange(date, sasr.getShipNumber(), sasr.getShipName(), sasr.getSeatNumbers().length, sasr.getPrice());
                sasrrList.add(sasrN);
            } else {
                int i = sasrrList.indexOf(sasrr);
                sasrr.setTotalNumberOfSeat(sasrr.getTotalNumberOfSeat() + sasr.getSeatNumbers().length);
                sasrr.setTotalFare(sasrr.getTotalFare() + sasr.getPrice());
                sasrrList.set(i, sasrr);
            }
        }
        return sasrrList;
    }

    @Override
    public List<ServiceAdminSellsReport> getAdminReservationReport(LocalDate date) throws ForbiddenException, ParseException {
        User user = SecurityConfig.getCurrentUser();
        if (!user.isAdmin()) throw new ForbiddenException("Access denied");
        List<Seat> seats = this.seatService.getAll();
        return this.getAdminBookingReportFromSeatList(seats, date, true);
    }

    /*GET Reservation report for admin for a date range
     * @param startDate      report start date
     * @param endDate        report end date
     * @return List<ServiceAdminSellsReportRange>    list of Service admin sells report
     * */
    @Override
    public List<ServiceAdminSellsReportRange> getAdminReservationReportRange(LocalDate startDate, LocalDate endDate) throws ForbiddenException, ParseException {
        List<LocalDate> dateList = DateUtil.getLocalDatesBetween(startDate, endDate);
        List<ServiceAdminSellsReportRange> serviceAdminSellsReportRanges = new ArrayList<>();

        for (LocalDate date : dateList) {
            List<ServiceAdminSellsReport> sasrList = this.getAdminReservationReport(date);
            serviceAdminSellsReportRanges = this.populateServiceAdminSellsReportRangeList(sasrList, date, serviceAdminSellsReportRanges);
        }

        return serviceAdminSellsReportRanges;
    }

    @Override
    public List<ServiceAdminSellsReport> getAdminSellsReportByShipId(LocalDate date, Long shipId) throws ParseException {
        List<Booking> bookingList = this.bookingService.getBookingsByShipIdAndCreateDate(shipId, DateUtil.convertToDateViaSqlDate(date));
        return this.getAdminBookingReportFromBookingList(bookingList);
    }

    @Override
    public List<ServiceAdminSellsReport> getAdminReservationReportByShipId(LocalDate date, Long shipId) throws ForbiddenException, ParseException {
        User user = SecurityConfig.getCurrentUser();
        if (!user.isAdmin()) throw new ForbiddenException("Access denied");
        List<Seat> seats = this.seatService.getSeatListByShipId(shipId);
        return this.getAdminBookingReportFromSeatList(seats, date, true);
    }

    @Override
    public List<ServiceAdminSellsReport> getAdminAgentReport(Long shipId, Long userId, LocalDate date) throws ParseException {
        List<Booking> bookingList = this.bookingService.getBookingListByCreatedIdAndShipIdAndDate(userId, shipId, DateUtil.convertToDateViaSqlDate(date));
        return this.getAdminBookingReportFromBookingList(bookingList);
    }

    @Override
    public List<ServiceAdminSellsReport> getShipAdminAgentReportRange(Long shipId, Long userId, LocalDate startDate, LocalDate endDate) throws ParseException {
        List<ServiceAdminSellsReport> reportList = new ArrayList<>();
        List<LocalDate> dateList = DateUtil.getLocalDatesBetween(startDate, endDate);
        for (LocalDate localDate : dateList) {
            reportList.addAll(this.getAdminAgentReport(shipId, userId, localDate));
        }
        return reportList;
    }

    @Override
    public List<ServiceAdminSellsReport> getAdminSellsReportRange(Long shipId, Long userId, LocalDate startDate, LocalDate endDate) throws ParseException {
        List<LocalDate> dateList = DateUtil.getLocalDatesBetween(startDate, endDate);
        List<ServiceAdminSellsReport> reportList = new ArrayList<>();

        for (LocalDate date : dateList) {
            List<ServiceAdminSellsReport> sasrList = this.getAdminAgentReport(shipId, userId, date);
            reportList.addAll(sasrList);
        }
        return reportList;
    }

    @Override
    public List<ServiceAdminSellsReport> getAdminReservationReport(Long userId, Long shipId, LocalDate date) throws ParseException {
        List<Booking> bookingList = this.bookingService.getReservationListByUserIdShipIdAndDate(userId, shipId, date);

        List<ServiceAdminSellsReport> reportList = this.getAdminBookingReportFromBookingList(bookingList);
        return reportList;
    }

    @Override
    public List<ServiceAdminSellsReport> getAdminReservationReportRange(Long userId, Long shipId, LocalDate startDate, LocalDate endDate) throws ParseException {
        List<LocalDate> dateList = DateUtil.getLocalDatesBetween(startDate, endDate);
        List<ServiceAdminSellsReport> reportList = new ArrayList<>();

        for (LocalDate date : dateList) {
            List<ServiceAdminSellsReport> sasrList = this.getAdminReservationReport(userId, shipId, date);
            reportList.addAll(sasrList);
        }
        return reportList;
    }

    @Override
    public List<ServiceAdminSellsReport> getServiceAdminSellsReport(Long shipId, LocalDate date) throws ForbiddenException, ParseException {
        User user = SecurityConfig.getCurrentUser();
        if (user.getShips() == null || !user.hasRole(Role.ERole.ROLE_SERVICE_ADMIN.toString()))
            throw new ForbiddenException("Access denied");

        List<Booking> bookingList = this.bookingService.getBookingsByShipIdAndCreateDate(shipId, DateUtil.convertToDateViaSqlDate(date));
        return this.getAdminBookingReportFromBookingList(bookingList);
    }

    /*Get service admin sell report range for ship admin
     * @param Long shipId                        required report for the ship id
     * @param startDate                          report starting date
     * @param endDate                            report ending date
     * @return ServiceAdminSellsReportRange      for the ship using provided id*/
    @Override
    public List<ServiceAdminSellsReportRange> getServiceAdminSellsReportRange(Long shipId, LocalDate startDate, LocalDate endDate) throws ForbiddenException, ParseException {
        List<LocalDate> dateList = DateUtil.getLocalDatesBetween(startDate, endDate);
        List<ServiceAdminSellsReportRange> serviceAdminSellsReportRanges = new ArrayList<>();

        for (LocalDate date : dateList) {
            List<ServiceAdminSellsReport> sasrList = this.getServiceAdminSellsReport(shipId, date);
            serviceAdminSellsReportRanges = this.populateServiceAdminSellsReportRangeList(sasrList, date, serviceAdminSellsReportRanges);
        }
        return serviceAdminSellsReportRanges;
    }

    /*Get service admin reservation report for  ship admin
     * @param shipId                         ship id that report required for
     * @param date                           report date
     * @return ServiceAdminSellsReport       final reservation report*/
    @Override
    public List<ServiceAdminSellsReport> getServiceAdminReservationReport(Long shipId, LocalDate date) throws ForbiddenException, ParseException {
        User user = SecurityConfig.getCurrentUser();
        if (user.getShips() == null || !user.hasRole(Role.ERole.ROLE_SERVICE_ADMIN.toString()))
            throw new ForbiddenException("Access denied");

        List<Seat> roomList = this.seatService.getSeatListByShipId(shipId);
        return this.getAdminBookingReportFromSeatList(roomList, date, false);
    }

    /*Get service admin reservation report range for ship admin
     * @param Long shipId                        required report for the ship id
     * @param startDate                          report starting date
     * @param endDate                            report ending date
     * @return ServiceAdminSellsReportRange      for the ship using provided id*/
    @Override
    public List<ServiceAdminSellsReportRange> getServiceAdminReservationReportRange(Long shipId, LocalDate startDate, LocalDate endDate) throws ForbiddenException, ParseException {
        List<LocalDate> dateList = DateUtil.getLocalDatesBetween(startDate, endDate);
        List<ServiceAdminSellsReportRange> serviceAdminSellsReportRanges = new ArrayList<>();

        for (LocalDate date : dateList) {
            List<ServiceAdminSellsReport> sasrList = this.getServiceAdminReservationReport(shipId, date);
            serviceAdminSellsReportRanges = this.populateServiceAdminSellsReportRangeList(sasrList, date, serviceAdminSellsReportRanges);
        }
        return serviceAdminSellsReportRanges;
    }

    @Override
    public List<ServiceAdminSellsReport> getBookingListReport(Long shipId, LocalDate date) throws ParseException {
        List<Booking> bookingList = this.bookingService.getBookingListByShipIdAndDate(shipId, date);
        List<ServiceAdminSellsReport> reportList = new ArrayList<>();

        for (Booking booking : bookingList) {
            for (SubBooking sb : booking.getSubBookingList()) {
                ServiceAdminSellsReport report = new ServiceAdminSellsReport();
                String[] seats = {sb.getSeat().getSeatNumber()};
                report.setJourneyDate(sb.getDate());
                report.setBookingDate(booking.getCreated());
                report.setBookingId(booking.getId());
                report.setBookingStatus(booking.geteStatus());
                report.setCustomerName(booking.getUser().getName());
                report.setCustomerPhone(booking.getUser().getPhoneNumber());
                report.setShipName(booking.getShip().getName());
                report.setShipNumber(booking.getShip().getShipNumber());
                report.setSeatNumbers(seats);
                report.setPrice(sb.getPayablePrice());
                report.setSoldBy(booking.getCreatedBy().getName());
                report.setRole(booking.getCreatedBy().getRoles().get(0).getRole());
                report.setPaid(booking.isPaid());
                report.setRoutes(sb.getSeat().getShip().getStartingPoint() + " - " + sb.getSeat().getShip().getDroppingPoint());

                reportList.add(report);
            }
        }
//        System.out.println("DD3 :" + "Booking list size :" + bookingList.size());
//        List<ServiceAdminSellsReport> reportList = this.getAdminBookingReportFromBookingList(bookingList);
//        System.out.println("DD4 : " + "Report list size : " + reportList.size());
        return reportList;
    }

    @Override
    public JSONObject getServiceAdminDashboardReport(LocalDate date) throws JSONException, ForbiddenException, ParseException {
        User currentUser = SecurityConfig.getCurrentUser();
        if (currentUser.getShips() == null || !currentUser.hasRole(Role.ERole.ROLE_SERVICE_ADMIN.toString()))
            throw new ForbiddenException("Access denied");

        JSONObject obj = new JSONObject();
        obj.put("numberOfShips", currentUser.getShips().size());
        obj.put("ships", new JSONArray(this.getUserShipsReportObjects(currentUser, date)));

        return obj;
    }

    List<JSONObject> getUserShipsReportObjects(User user, LocalDate date) throws JSONException, ParseException {
        if (user.getShips().size() < 1) return null;
        List<JSONObject> list = new ArrayList<JSONObject>();
        for (Ship ship : user.getShips()) {
            JSONObject obj = new JSONObject();
            obj.put("id", ship.getId());
            obj.put("name", ship.getName());
            obj.put("shipNumber", ship.getShipNumber());
            obj.put("categories", new JSONArray(this.getCategoryReportObjects(ship.getId(), date)));
            obj.put("numberOfSeats", this.seatService.getSeatListByShipId(ship.getId()).size());
            list.add(obj);
        }
        return list;
    }

    List<JSONObject> getCategoryReportObjects(Long shipId, LocalDate date) throws JSONException, ParseException {
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

    JSONObject getReservationReport(Category category, LocalDate date) throws JSONException, ParseException {
        int reserved = 0;
        int blocked = 0;
        int sold = 0;
        List<Seat> seatList = this.seatService.getSeatListByCategoryId(category.getId());
        for (Seat seat : seatList) {
            Seat.EStatus status = seat.getSeatStatusMap().get(date);
            if (status == null) {
                status = seat.getSeatStatusMap().get(date);
            }
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
    /*Generate ServiceAdminSellsReport from seatList
     * @param seatList                   list of seat that report to be generate
     * @param date                       report date
     * @param isAdmin                    if this report generating for admin then skip unsold seat
     * @report ServiceAdminSellsReport   report for provided seat list
     * */
    private List<ServiceAdminSellsReport> getAdminBookingReportFromSeatList(List<Seat> seatList, LocalDate date, boolean isAdmin) throws ParseException {
        List<ServiceAdminSellsReport> serviceAdminSellsReportList = new ArrayList<>();

        for (Seat seat : seatList) {
            //Generating and populating report object
            ServiceAdminSellsReport serviceAdminSellsReport = new ServiceAdminSellsReport();
            String[] seats = {seat.getSeatNumber()};
            serviceAdminSellsReport.setSeatNumbers(seats);
            serviceAdminSellsReport.setShipName(seat.getShip().getName());
            serviceAdminSellsReport.setShipNumber(seat.getShip().getShipNumber());
            serviceAdminSellsReport.setRoutes(seat.getShip().getStartingPoint() + " - " + seat.getShip().getDroppingPoint());
            //TODO: remove following after debug period
            Long bookingId = seat.getBookingIdMap().get(date);
//            if(bookingId == null) {
//                bookingId = seat.getBookingIdMap().get(DateUtil.removeTimeFromDate(date));
//            }
            if (bookingId != null) {
                Booking booking = this.bookingService.getOne(bookingId);
                if (!booking.isCancelled()) {
                    serviceAdminSellsReport.setJourneyDate(booking.getSubBookingList().get(0).getDate());
                    serviceAdminSellsReport.setBookingId(booking.getId());
                    serviceAdminSellsReport.setBookingStatus(booking.geteStatus());
                    serviceAdminSellsReport.setBookingDate(booking.getCreated());
                    serviceAdminSellsReport.setCustomerName(booking.getUser().getName());
                    serviceAdminSellsReport.setCustomerPhone(booking.getUser().getPhoneNumber());
                    int bookingSize = booking.getSubBookingList().size();
                    serviceAdminSellsReport.setPrice(booking.getTotalPayablePrice() / bookingSize);
                    serviceAdminSellsReport.setHotelswaveCommission(booking.getHotelswaveDiscount() / bookingSize);
                    serviceAdminSellsReport.setHotelswaveAgentCommission(booking.getHotelswaveAgentDiscount() / bookingSize);
                    serviceAdminSellsReport.setShipAgentCommission(booking.getAgentDiscount() / bookingSize);
                    serviceAdminSellsReport.setSoldBy(booking.getCreatedBy().getName());
                    serviceAdminSellsReport.setRole(booking.getCreatedBy().getRoles().get(0).getName());
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
        serviceAdminSellsReport.setBookingId(booking.getId());
        serviceAdminSellsReport.setBookingDate(booking.getCreated());
        serviceAdminSellsReport.setBookingStatus(booking.geteStatus());
        serviceAdminSellsReport.setCustomerName(booking.getUser().getName());
        serviceAdminSellsReport.setCustomerPhone(booking.getUser().getPhoneNumber());
        serviceAdminSellsReport.setShipName(booking.getShip().getName());
        serviceAdminSellsReport.setShipNumber(booking.getShip().getShipNumber());
        serviceAdminSellsReport.setRoutes(booking.getShip().getStartingPoint() + " - " + booking.getShip().getDroppingPoint());
        if (booking.getSubBookingList().size() > 0) {
            serviceAdminSellsReport.setJourneyDate(booking.getSubBookingList().get(0).getDate());
            String[] sets = new String[booking.getSubBookingList().size()];
            for (int i = 0; i < booking.getSubBookingList().size(); i++) {
                sets[i] = booking.getSubBookingList().get(i).getSeat().getSeatNumber();
            }
            serviceAdminSellsReport.setSeatNumbers(sets);
        }
        serviceAdminSellsReport.setPrice(booking.getTotalPayablePrice());
        serviceAdminSellsReport.setHotelswaveCommission(booking.getHotelswaveDiscount());
        serviceAdminSellsReport.setHotelswaveAgentCommission(booking.getHotelswaveAgentDiscount());
        serviceAdminSellsReport.setShipAgentCommission(booking.getAgentDiscount());
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
