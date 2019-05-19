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
    private ResponseEntity getServiceAdminDashboardReport(@RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) throws ForbiddenException, JSONException {
        return ResponseEntity.ok(this.reportService.getServiceAdminDashboardReport(date).toString());
    }

    @GetMapping("/shipSells/{shipId}")
    private ResponseEntity getAdminBookingReportByHotelId(@PathVariable("shipId") Long shipId, @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) throws ForbiddenException, ParseException, JSONException {
        return ResponseEntity.ok(this.reportService.getServiceAdminSellsReport(shipId, date));
    }

    @GetMapping("/shipReservation/{shipId}")
    private ResponseEntity getAdminReservationReportByHotelId(@PathVariable("shipId") Long shipId, @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) throws JSONException, ForbiddenException{
        return ResponseEntity.ok(this.reportService.getServiceAdminReservationReport(shipId, date));
    }
}
