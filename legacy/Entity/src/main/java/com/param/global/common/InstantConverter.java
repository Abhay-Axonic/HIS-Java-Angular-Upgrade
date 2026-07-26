package com.param.global.common;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Time;
import java.time.Instant;
import java.time.LocalTime;

@Converter(autoApply = true)  // autoApply = true applies this converter to all Instant types globally
public class InstantConverter implements AttributeConverter<Instant, Time> {

    @Override
    public Time convertToDatabaseColumn(Instant attribute) {
        if (attribute == null) {
            return null;
        }
        // Convert Instant to LocalTime (just the time part) in UTC
        LocalTime localTime = attribute.atZone(java.time.ZoneOffset.UTC).toLocalTime();
        return Time.valueOf(localTime);  // Convert LocalTime to SQL Time
    }

    @Override
    public Instant convertToEntityAttribute(Time dbData) {
        if (dbData == null) {
            return null;
        }
        // Convert SQL Time back to Instant
        LocalTime localTime = dbData.toLocalTime();
        return localTime.atDate(java.time.LocalDate.now()) // Use current date
                .atZone(java.time.ZoneOffset.UTC) // Match convertToDatabaseColumn's write-side zone, so load->save round-trips are idempotent
                .toInstant();
    }
}
