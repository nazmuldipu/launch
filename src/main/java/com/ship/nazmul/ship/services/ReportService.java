package com.ship.nazmul.ship.services;

import com.ship.nazmul.ship.entities.pojo.ServiceAdminSellsReport;
import com.ship.nazmul.ship.entities.pojo.ServiceAdminSellsReportRange;
import com.ship.nazmul.ship.exceptions.forbidden.ForbiddenException;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public interface ReportService {
    /************************************Admin modules***************************************/
    List<ServiceAdminSellsReport> getAdminSellsReport(Date date) throws ForbiddenException, ParseException;

    List<ServiceAdminSellsReportRange> getAdminSellsReportRange(Date startDate, Date endDate) throws ForbiddenException, ParseException;

    List<ServiceAdminSellsReport> getAdminReservationReport(Date date) throws ForbiddenException, ParseException;

    List<ServiceAdminSellsReportRange> getAdminReservationReportRange(Date startDate, Date endDate) throws ForbiddenException, ParseException;

    List<ServiceAdminSellsReport> getAdminSellsReportByShipId(Date date, Long shipId) throws ParseException;

    List<ServiceAdminSellsReport> getAdminReservationReportByShipId(Date date, Long shipId) throws ForbiddenException, ParseException;

    /************************************Service Admin modules***************************************/
    List<ServiceAdminSellsReport> getServiceAdminSellsReport(Long shipId, Date date) throws ForbiddenException, ParseException;

    List<ServiceAdminSellsReportRange> getServiceAdminSellsReportRange(Long shipId, Date startDate, Date endDate) throws ForbiddenException, ParseException;

    List<ServiceAdminSellsReport> getServiceAdminReservationReport(Long shipId, Date date) throws ForbiddenException, ParseException;

    List<ServiceAdminSellsReportRange> getServiceAdminReservationReportRange(Long shipId, Date startDate, Date endDate) throws ForbiddenException, ParseException;

    JSONObject getServiceAdminDashboardReport(Date date) throws JSONException, ForbiddenException, ParseException;
}
