package com.ship.nazmul.ship.commons.utils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Converter
public class LocalDateTimeAttributeConverter implements AttributeConverter<LocalDateTime, Date> {

    @Override
    public Date convertToDatabaseColumn(LocalDateTime dateToConvert) {
        return java.util.Date
                .from(dateToConvert.atZone(ZoneId.systemDefault())
                        .toInstant());
    }

    @Override
    public LocalDateTime convertToEntityAttribute(Date dateToConvert) {
        return new java.sql.Timestamp(
                dateToConvert.getTime()).toLocalDateTime();
    }
}
