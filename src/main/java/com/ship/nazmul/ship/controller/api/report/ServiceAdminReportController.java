package com.ship.nazmul.ship.controller.api.report;

import com.ship.nazmul.ship.exceptions.forbidden.ForbiddenException;
import com.ship.nazmul.ship.exceptions.notfound.NotFoundException;
import com.ship.nazmul.ship.services.ReportService;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Date;

@RestController
@RequestMapping("/api/v1/serviceAdmin/reports")
public class ServiceAdminReportController {
    private final ReportService reportService;

    @Autowired
    public ServiceAdminReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("")
    private ResponseEntity getServiceAdminDashboardReport(@RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) throws ForbiddenException, JSONException, ParseException {
        return ResponseEntity.ok(this.reportService.getServiceAdminDashboardReport(date).toString());
    }

    @GetMapping("/shipSells/{shipId}")
    private ResponseEntity getAdminBookingReportByHotelId(@PathVariable("shipId") Long shipId, @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) throws ForbiddenException, ParseException, JSONException {
        return ResponseEntity.ok(this.reportService.getServiceAdminSellsReport(shipId, date));
    }

    @GetMapping("/shipSellsRange/{shipId}")
    private ResponseEntity getAdminBookingReportRangeByHotelId(@PathVariable("shipId") Long shipId, @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate, @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) throws ForbiddenException, ParseException {
        return ResponseEntity.ok(this.reportService.getServiceAdminSellsReportRange(shipId, startDate, endDate));
    }

    @GetMapping("/shipReservation/{shipId}")
    private ResponseEntity getAdminReservationReportByHotelId(@PathVariable("shipId") Long shipId, @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) throws JSONException, ForbiddenException, ParseException {
        return ResponseEntity.ok(this.reportService.getServiceAdminReservationReport(shipId, date));
    }

    @GetMapping("/shipReservationRange/{shipId}")
    private ResponseEntity getAdminReservationReportRangeByHotelId(@PathVariable("shipId") Long shipId, @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate, @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) throws ForbiddenException, ParseException {
        return ResponseEntity.ok(this.reportService.getServiceAdminReservationReportRange(shipId, startDate, endDate));
    }

    @GetMapping("/agentReport/{shipId}")
    private ResponseEntity getShipAgentReportByShip(@PathVariable("shipId")Long shipId, @RequestParam("userId")Long userId,@RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) throws ParseException {
        return ResponseEntity.ok(this.reportService.getAdminAgentReport(shipId, userId, date));
    }

    @GetMapping("/agentReportRange/{shipId}")
    private ResponseEntity getShipAgentReportRangeByShip(@PathVariable("shipId")Long shipId,
                                                         @RequestParam("userId")Long userId,@RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                                         @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) throws ParseException {
        return ResponseEntity.ok(this.reportService.getShipAdminAgentReportRange(shipId, userId, startDate, endDate));
    }

    @GetMapping("/bookingList/{shipId}")
    private ResponseEntity getBookingList(@PathVariable("shipId")Long shipId,  @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) throws ParseException {
        return ResponseEntity.ok(this.reportService.getBookingListReport(shipId, date));
    }

    @GetMapping("/userSellsReportRange/{shipId}")
    private ResponseEntity getAgentSellsReportRangeByShip(@PathVariable("shipId")Long shipId, @RequestParam("userId")Long userId,
                                                          @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                                          @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) throws ParseException {
        return ResponseEntity.ok(this.reportService.getAdminSellsReportRange(shipId, userId, startDate, endDate));
    }

    @GetMapping("/userReservationReportRange/{shipId}")
    private ResponseEntity getAgentReservationReportRangeByShip(@PathVariable("shipId")Long shipId, @RequestParam("userId")Long userId,
                                                                @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                                                @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) throws ParseException {
        return ResponseEntity.ok(this.reportService.getAdminReservationReportRange(userId, shipId, startDate, endDate));
    }

}
