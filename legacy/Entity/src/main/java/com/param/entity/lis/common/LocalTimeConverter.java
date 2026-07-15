package com.param.entity.lis.common;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.AttributeConverter;

public class LocalTimeConverter implements AttributeConverter<Long, Date> {

	private static final Logger logger = Logger.getLogger(LocalTimeConverter.class.getName());

	@Override
	public Date convertToDatabaseColumn(Long localTime) {
		Date date = null;

		try {

			if (localTime != null) {
				date = new Date(localTime);
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, "convertToDatabaseColumn failed", e);
		}
		return date;
	}

	@Override
	public Long convertToEntityAttribute(Date timeSlot) {
		Long localTime = null;
		try {
			if (timeSlot != null) {
				localTime = timeSlot.getTime();
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, "convertToEntityAttribute failed", e);
		}
		return localTime;
	}


}
