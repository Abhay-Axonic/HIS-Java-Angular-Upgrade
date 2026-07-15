package com.param.actionmaster.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import com.param.global.common.ServiceException;

public class DateUtils {
	private static final Logger logger = Logger.getLogger(DateUtils.class);

	public static Date getDate(String dateString, String pattern) {
		SimpleDateFormat format = null;
		Date date1 = null;
		try {
			format = new SimpleDateFormat(pattern);
			date1 = format.parse(dateString);

		} catch (ParseException e) {
			logger.error("Error while parsing date " + dateString + " with pattern " + pattern, e);
		}
		return date1;
	}

	public static Date truncate(Date now, int dayOfMonth) {
		// TODO Auto-generated method stub
		return null;
	}

}
