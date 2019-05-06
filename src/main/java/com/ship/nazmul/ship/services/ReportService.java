package com.ship.nazmul.ship.services;

import com.ship.nazmul.ship.entities.pojo.ServiceAdminSellsReport;
import com.ship.nazmul.ship.exceptions.forbidden.ForbiddenException;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public interface ReportService {
    /************************************Admin modules***************************************/
    List<ServiceAdminSellsReport> getAdminSellsReport(Date date) throws ForbiddenException, ParseException;

    List<ServiceAdminSellsReport> getAdminReservationReport(Date date) throws ForbiddenException;

    List<ServiceAdminSellsReport> getAdminSellsReportByShipId(Date date, Long shipId) throws ParseException;

    List<ServiceAdminSellsReport> getAdminReservationReportByShipId(Date date, Long shipId) throws ForbiddenException;

    /************************************Service Admin modules***************************************/
    List<ServiceAdminSellsReport> getServiceAdminSellsReport(Long shipId, Date date) throws ForbiddenException, ParseException;

    List<ServiceAdminSellsReport> getServiceAdminReservationReport(Long shipId, Date date) throws ForbiddenException;
}
