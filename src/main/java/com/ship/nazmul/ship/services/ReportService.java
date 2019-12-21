package com.ship.nazmul.ship.services;

import com.ship.nazmul.ship.entities.pojo.ServiceAdminSellsReport;
import com.ship.nazmul.ship.entities.pojo.ServiceAdminSellsReportRange;
import com.ship.nazmul.ship.exceptions.forbidden.ForbiddenException;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
//import java.util.Date;
import java.time.LocalDate;
import java.util.List;

public interface ReportService {
    /************************************Admin modules***************************************/
    List<ServiceAdminSellsReport> getAdminSellsReport(LocalDate date) throws ForbiddenException, ParseException;

    List<ServiceAdminSellsReportRange> getAdminSellsReportRange(LocalDate startDate, LocalDate endDate) throws ForbiddenException, ParseException;

    List<ServiceAdminSellsReport> getAdminReservationReport(LocalDate date) throws ForbiddenException, ParseException;

    List<ServiceAdminSellsReportRange> getAdminReservationReportRange(LocalDate startDate, LocalDate endDate) throws ForbiddenException, ParseException;

    List<ServiceAdminSellsReport> getAdminSellsReportByShipId(LocalDate date, Long shipId) throws ParseException;

    List<ServiceAdminSellsReport> getAdminReservationReportByShipId(LocalDate date, Long shipId) throws ForbiddenException, ParseException;

    List<ServiceAdminSellsReport> getAdminAgentReport(Long shipId, Long userId, LocalDate date) throws ParseException;

    List<ServiceAdminSellsReportRange> getAdminAgentReportRange(Long shipId, Long userId, LocalDate startDate, LocalDate endDate) throws ParseException;

    /************************************Service Admin modules***************************************/
    List<ServiceAdminSellsReport> getServiceAdminSellsReport(Long shipId, LocalDate date) throws ForbiddenException, ParseException;

    List<ServiceAdminSellsReportRange> getServiceAdminSellsReportRange(Long shipId, LocalDate startDate, LocalDate endDate) throws ForbiddenException, ParseException;

    List<ServiceAdminSellsReport> getServiceAdminReservationReport(Long shipId, LocalDate date) throws ForbiddenException, ParseException;

    List<ServiceAdminSellsReportRange> getServiceAdminReservationReportRange(Long shipId, LocalDate startDate, LocalDate endDate) throws ForbiddenException, ParseException;

    List<ServiceAdminSellsReport> getBookingListReport(Long shipId, LocalDate date) throws ParseException;

    JSONObject getServiceAdminDashboardReport(LocalDate date) throws JSONException, ForbiddenException, ParseException;
}
