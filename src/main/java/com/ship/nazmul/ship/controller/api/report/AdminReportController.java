package com.ship.nazmul.ship.controller.api.report;

import com.ship.nazmul.ship.exceptions.forbidden.ForbiddenException;
import com.ship.nazmul.ship.exceptions.notfound.NotFoundException;
import com.ship.nazmul.ship.services.ReportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLOutput;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Date;

@RestController
@RequestMapping("/api/v1/admin/reports")
public class AdminReportController {
    private final ReportService reportService;

    public AdminReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/sells")
    private ResponseEntity getAdminBookingReport(@RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) throws ForbiddenException, ParseException {
        return ResponseEntity.ok(this.reportService.getAdminSellsReport(date));
    }

    @GetMapping("/sellsRange")
    private ResponseEntity getAdminBookingReportDateRange(@RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate, @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) throws ForbiddenException, ParseException {
        return ResponseEntity.ok(this.reportService.getAdminSellsReportRange(startDate, endDate));
    }

    @GetMapping("/reservation")
    private ResponseEntity getAdminReservationReport(@RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) throws ForbiddenException, ParseException {
        return ResponseEntity.ok(this.reportService.getAdminReservationReport(date));
    }

    @GetMapping("/reservationRange")
    private ResponseEntity getAdminReservationReportRange(@RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate, @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) throws ForbiddenException, ParseException {
        return ResponseEntity.ok(this.reportService.getAdminReservationReportRange(startDate, endDate));
    }


    @GetMapping("/shipSells/{shipId}")
    private ResponseEntity getAdminBookingReportByHotelId(@PathVariable("shipId")Long shipId, @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) throws ForbiddenException, ParseException {
        return ResponseEntity.ok(this.reportService.getAdminSellsReportByShipId(date, shipId));
    }

    @GetMapping("/shipReservation/{shipId}")
    private ResponseEntity getAdminReservationReportByHotelId(@PathVariable("shipId")Long shipId, @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) throws NotFoundException, ForbiddenException, ParseException {
        return ResponseEntity.ok(this.reportService.getAdminReservationReportByShipId(date, shipId));
    }

    @GetMapping("/agentReport/{shipId}")
    private ResponseEntity getAgentReportByShip(@PathVariable("shipId")Long shipId, @RequestParam("userId")Long userId,@RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) throws ParseException {
        return ResponseEntity.ok(this.reportService.getAdminAgentReport(shipId, userId, date));
    }
}
