package com.ship.nazmul.ship.commons.utils;

import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;


@Component
public class DateUtil {

    private DateUtil() {
    }

    public static String SERVER_DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static String DATE_PATTERN_BACKWARDS = "yyyy-MM-dd";
    public static String DATE_TIME_PATTERN_BACKWARDS = "yyyy-MM-dd HH:mm:ss";

    public static String DATE_PATTERN_READABLE = "MMM dd, YYYY";
    public static String TIME_PATTERN_READABLE = "hh:mm a";
    public static String DATE_TIME_PATTERN_READABLE = "MMM dd, YYYY hh:mm a";

    public static SimpleDateFormat getParsableDateFormat() {
        return new SimpleDateFormat(DateUtil.DATE_PATTERN_BACKWARDS);
    }

    public static Date simpleDateFormat(Date date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = format.format(date );
        return format.parse( dateString);
    }

    public static boolean isSameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

    public static boolean containsDate(List<Date> bookingDateList, Date date) {
        for (Date bookingDate : bookingDateList) {
            if (DateUtil.isSameDay(bookingDate, date))
                return true;
        }
        return false;
    }

    public static String getReadableDate(Date date) {
        return getReadableDateFormat().format(date);
    }

    public static String getReadableTime(Date date) {
        return getReadableTimeFormat().format(date);
    }

    public static String getReadableDateTime(Date date) {
        return getReadableDateTimeFormat().format(date);
    }

    public static String getReadableDateTimeBackward(Date date) {
        return getReadableDateTimeFormat().format(date);
    }

    public static Date parseServerDateTime(String date) throws ParseException {
        DateFormat sdf = getServerDateTimeFormat();
        sdf.setTimeZone(TimeZone.getDefault());
        return sdf.parse(date);
    }

    public static DateFormat getReadableDateFormat() {
        return new SimpleDateFormat(DATE_PATTERN_READABLE);
    }

    public static DateFormat getReadableTimeFormat() {
        return new SimpleDateFormat(TIME_PATTERN_READABLE);
    }

    public static DateFormat getReadableDateTimeFormat() {
        return new SimpleDateFormat(DATE_TIME_PATTERN_READABLE);
    }

    public static DateFormat getServerDateTimeFormat() {
        return new SimpleDateFormat(SERVER_DATE_TIME_PATTERN);
    }

    public static DateFormat getServerDateTimeFormatBackward() {
        return new SimpleDateFormat(DATE_TIME_PATTERN_BACKWARDS);
    }


    public static java.time.Period getAge(Date date) {
        LocalDate today = LocalDate.now();
        LocalDate birthday = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        return java.time.Period.between(birthday, today);
    }

    public static List<Date> getDatesBetween(Date startDate, Date endDate) {
        List<Date> datesInRange = new ArrayList<>();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(startDate);

        Calendar endCalendar = new GregorianCalendar();
        endCalendar.setTime(endDate);
//        endCalendar.add(Calendar.DATE, 1);//so that last date includes to the list


        while (calendar.before(endCalendar)) {
            Date result = calendar.getTime();
            datesInRange.add(result);
//            System.out.println(result);
            calendar.add(Calendar.DATE, 1);
        }
//        System.out.println(datesInRange.size());
        return datesInRange;
    }

    public static Date addTimeZoneToDate(Date date){
        date = removeTimeFromDate(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR, 6);
        return calendar.getTime();
    }

    public static Date truncateTimeFromDate(Date date) throws ParseException {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.parse(formatter.format(date));
    }

    public static java.sql.Date convertUtilToSql(java.util.Date uDate) {
        java.sql.Date sDate = new java.sql.Date(uDate.getTime());
        return sDate;
    }

    public static Date removeTimeFromDate(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.ZONE_OFFSET, 0);
        return calendar.getTime();
    }



    public static Date getDayStart(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 1);
        calendar.set(Calendar.ZONE_OFFSET, 0);
        return calendar.getTime();
    }

    public static Date getDayEnd(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        calendar.setTime(getDayStart(calendar.getTime()));
        calendar.add(Calendar.SECOND, -2);
        calendar.set(Calendar.ZONE_OFFSET, 0);
        return calendar.getTime();
    }

//    public static Map<DateRangeType, Calendar> buildDateRange(Period period) {
//        DateTimeUtil datetimeUtil = new DateTimeUtil(Calendar.getInstance());
//        Calendar dateFrom, dateTo;
//        switch (period) {
//            case THIS_MONTH:
//                dateFrom = datetimeUtil.getThisMonthStartDate();
//                dateTo = datetimeUtil.getThisMonthEndDate();
//                break;
//            case LAST_MONTH:
//                dateFrom = datetimeUtil.getLastMonthStartDate();
//                dateTo = datetimeUtil.getLastMonthEndDate();
//                break;
//            case THIS_YEAR:
//                dateFrom = datetimeUtil.getThisYearStartDate();
//                dateTo = datetimeUtil.getThisYearEndDate();
//                break;
//            case LAST_YEAR:
//                dateFrom = datetimeUtil.getLastYearStartDate();
//                dateTo = datetimeUtil.getLastYearEndDate();
//                break;
//            case ALL_TIME:
//            default:
//                dateFrom = datetimeUtil.getBeginningFromDate();
//                dateTo = datetimeUtil.getThisYearEndDate();
//                break;
//        }
//        Map<DateRangeType, Calendar> dateRangeMap = new HashMap<>();
//        dateRangeMap.put(DateRangeType.DATE_FROM, dateFrom);
//        dateRangeMap.put(DateRangeType.DATE_TO, dateTo);
//        return dateRangeMap;
//    }
//
//    public static Map<DateRangeType, Calendar> buildDateRangeWithMonth(String month, int year) {
//        DateTimeUtil datetimeUtil = new DateTimeUtil(Calendar.getInstance());
//        datetimeUtil.setYear(year);
//        Calendar dateFrom, dateTo;
//        switch (month.toLowerCase()) {
//            case "january":
//                datetimeUtil.setMonth(Calendar.JANUARY);
//                dateFrom = datetimeUtil.getThisMonthStartDate();
//                dateTo = datetimeUtil.getThisMonthEndDate();
//                break;
//            case "february":
//                datetimeUtil.setMonth(Calendar.FEBRUARY);
//                dateFrom = datetimeUtil.getThisMonthStartDate();
//                dateTo = datetimeUtil.getThisMonthEndDate();
//                break;
//            case "march":
//                datetimeUtil.setMonth(Calendar.MARCH);
//                dateFrom = datetimeUtil.getThisMonthStartDate();
//                dateTo = datetimeUtil.getThisMonthEndDate();
//                break;
//            case "april":
//                datetimeUtil.setMonth(Calendar.APRIL);
//                dateFrom = datetimeUtil.getThisMonthStartDate();
//                dateTo = datetimeUtil.getThisMonthEndDate();
//                break;
//            case "may":
//                datetimeUtil.setMonth(Calendar.MAY);
//                dateFrom = datetimeUtil.getThisMonthStartDate();
//                dateTo = datetimeUtil.getThisMonthEndDate();
//                break;
//            case "june":
//                datetimeUtil.setMonth(Calendar.JUNE);
//                dateFrom = datetimeUtil.getThisMonthStartDate();
//                dateTo = datetimeUtil.getThisMonthEndDate();
//                break;
//            case "july":
//                datetimeUtil.setMonth(Calendar.JULY);
//                dateFrom = datetimeUtil.getThisMonthStartDate();
//                dateTo = datetimeUtil.getThisMonthEndDate();
//                break;
//            case "august":
//                datetimeUtil.setMonth(Calendar.AUGUST);
//                dateFrom = datetimeUtil.getThisMonthStartDate();
//                dateTo = datetimeUtil.getThisMonthEndDate();
//                break;
//            case "september":
//                datetimeUtil.setMonth(Calendar.SEPTEMBER);
//                dateFrom = datetimeUtil.getThisMonthStartDate();
//                dateTo = datetimeUtil.getThisMonthEndDate();
//                break;
//            case "october":
//                datetimeUtil.setMonth(Calendar.OCTOBER);
//                dateFrom = datetimeUtil.getThisMonthStartDate();
//                dateTo = datetimeUtil.getThisMonthEndDate();
//                break;
//            case "november":
//                datetimeUtil.setMonth(Calendar.NOVEMBER);
//                dateFrom = datetimeUtil.getThisMonthStartDate();
//                dateTo = datetimeUtil.getThisMonthEndDate();
//                break;
//            case "december":
//                datetimeUtil.setMonth(Calendar.DECEMBER);
//                dateFrom = datetimeUtil.getThisMonthStartDate();
//                dateTo = datetimeUtil.getThisMonthEndDate();
//                break;
//            default:
//                Calendar cal = Calendar.getInstance();
//                cal.setTime(new Date());
//                datetimeUtil.setMonth(cal.get(Calendar.MONTH));
//                dateFrom = datetimeUtil.getThisMonthStartDate();
//                dateTo = datetimeUtil.getThisMonthEndDate();
//                break;
//
//        }
//        Map<DateRangeType, Calendar> dateRangeMap = new HashMap<>();
//        dateRangeMap.put(DateRangeType.DATE_FROM, dateFrom);
//        dateRangeMap.put(DateRangeType.DATE_TO, dateTo);
//        return dateRangeMap;
//    }

    public static boolean isInCurrentMonthYear(Date date) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(new Date());
        cal2.setTime(date);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
    }

    public static boolean isPast(Date date) {
        return date.before(new Date());
    }
//    public static String calculateAge(St){
//        LocalDate birthdate = new LocalDate(1970, 1, 20);
//        LocalDate now = new LocalDate();
//        Years age = Years.yearsBetween(birthdate, now);
//    }

    public enum DateRangeType {
        DATE_FROM("dateFrom"),
        DATE_TO("dateTo");

        private String value;


        DateRangeType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

    }

    public enum Period {
        THIS_MONTH, LAST_MONTH, THIS_YEAR, LAST_YEAR, ALL_TIME
    }

    public enum Months {
        JANUARY, FEBRUARY, MARCH, APRIL, MAY, JUNE, JULY, AUGUST, SEPTEMBER, OCTOBER, NOVEMBER, DECEMBER
    }

    public static Period getPeriod(String period) {
        switch (period) {
            case "this_month":
                return Period.THIS_MONTH;
            case "last_month":
                return Period.LAST_MONTH;
            case "this_year":
                return Period.THIS_YEAR;
            case "last_year":
                return Period.LAST_YEAR;
            default:
                return Period.ALL_TIME;
        }
    }

    public static int daysBetween(Date d1, Date d2){
        return (int)( (d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }

    public static boolean nonOverlapWithExisting(Date start1, Date end1, Date start2, Date end2){
        return (end1.getTime() > start1.getTime() && start2.getTime() > end1.getTime()) && (end2.getTime()  > start2.getTime());
    }

    public static boolean inTheRangeBetween(Date start, Date end, Date date){
        return date.getTime() > start.getTime() && date.getTime() < end.getTime();
    }

    public static int getMinutesDifferentBetweenTwoDate(Date date1, Date date2){
        long diffInMillies = Math.abs(date2.getTime() - date1.getTime());
        return (int) diffInMillies/60000;
    }
}
