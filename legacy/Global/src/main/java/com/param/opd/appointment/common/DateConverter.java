package com.param.opd.appointment.common;

import java.util.Date;

import javax.persistence.AttributeConverter;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
public class DateConverter implements AttributeConverter<Long,Date> {
	final static Logger logger = Logger.getLogger(DateConverter.class);


	@Override
	public Date convertToDatabaseColumn(Long longDate) {
		Date date = null;
		try {
			if(longDate != null) 
				date=new Date(longDate);
		}catch (Exception e) {
		logger.error("convertToDatabaseColumn failed", e);
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
		logger.error("convertToEntityAttribute failed", e);
		}
		return longDate;
	}

}
