package com.param.utility;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Level;
import java.util.logging.Logger;

import in.param.exception.ApplicationException;
@Transactional
@Service
public class TimeZoneServiceImpl implements ITimeZoneService
{
	private static final Logger logger = Logger.getLogger(TimeZoneServiceImpl.class.getName());

	@Autowired
	ITimeZoneDao iTimeZoneDao;

	@Override
	public List<UnitMasterDto> getUnitTimeZones() {
		try {
			return iTimeZoneDao.getUnitTimeZones();
		} catch (Exception e) {
			throw new ApplicationException(e.getMessage(), e);
		}
	}

	@Override
	public List<TimeZoneDto> getTimeZones() {
		try {
			return iTimeZoneDao.getTimeZones();
		} catch (Exception e) {
			throw new ApplicationException(e.getMessage(), e);
		}
	}
	
}
