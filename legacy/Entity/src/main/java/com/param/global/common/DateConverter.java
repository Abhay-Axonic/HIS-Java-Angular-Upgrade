package com.param.global.common;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.AttributeConverter;

public class DateConverter implements AttributeConverter<Long,Date> {

	private static final Logger logger = Logger.getLogger(DateConverter.class.getName());

	@Override
	public Date convertToDatabaseColumn(Long longDate) {
		Date date = null;
		try {
			if(longDate != null)
				date=new Date(longDate);
		}catch (Exception e) {
			logger.log(Level.SEVERE, "convertToDatabaseColumn failed", e);
		}
		return date;
	}

	@Override
	public Long convertToEntityAttribute(Date date) {
		Long longDate = null;
		try {
			if(date != null)
				longDate = date.getTime();
		}catch (Exception e) {
			logger.log(Level.SEVERE, "convertToEntityAttribute failed", e);
		}
		return longDate;
	}

}
