package com.ship.nazmul.ship.controller.api.report;

import com.ship.nazmul.ship.exceptions.forbidden.ForbiddenException;
import com.ship.nazmul.ship.exceptions.notfound.NotFoundException;
import com.ship.nazmul.ship.services.ReportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Date;

@RestController
@RequestMapping("/api/v1/admin/reports")
public class AdminReportController {
    private final ReportService reportService;

    public AdminReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/sells")
    private ResponseEntity getAdminBookingReport(@RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) throws ForbiddenException, ParseException {
        return ResponseEntity.ok(this.reportService.getAdminSellsReport(date));
    }

    @GetMapping("/reservation")
    private ResponseEntity getAdminReservationReport(@RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) throws ForbiddenException {
        return ResponseEntity.ok(this.reportService.getAdminReservationReport(date));
    }

    @GetMapping("/shipSells/{shipId}")
    private ResponseEntity getAdminBookingReportByHotelId(@PathVariable("shipId")Long shipId, @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) throws ForbiddenException, ParseException {
        return ResponseEntity.ok(this.reportService.getAdminSellsReportByShipId(date, shipId));
    }

    @GetMapping("/shipReservation/{shipId}")
    private ResponseEntity getAdminReservationReportByHotelId(@PathVariable("shipId")Long shipId, @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) throws NotFoundException, ForbiddenException, ParseException {
        return ResponseEntity.ok(this.reportService.getAdminReservationReportByShipId(date, shipId));
    }
}
