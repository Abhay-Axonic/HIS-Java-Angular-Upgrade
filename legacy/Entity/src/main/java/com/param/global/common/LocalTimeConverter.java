package com.param.global.common;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.AttributeConverter;

public class LocalTimeConverter implements AttributeConverter<LocalTime, Date>{

	private static final Logger logger = Logger.getLogger(LocalTimeConverter.class.getName());

	@Override
	public Date convertToDatabaseColumn(LocalTime localTime) {
		Date date = null;
		try {
			Instant instant = localTime.atDate(LocalDate.of(2016, 9, 12)).atZone(ZoneId.systemDefault()).toInstant();
			date = Date.from(instant);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "convertToDatabaseColumn failed", e);
		}

		return date;
	}

	@Override
	public LocalTime convertToEntityAttribute(Date timeSlot) {
		LocalTime localTime = null;
		try {
			Instant instant = Instant.ofEpochMilli(timeSlot.getTime());
			localTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalTime();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "convertToEntityAttribute failed", e);
		}
		return localTime;
	}

}
