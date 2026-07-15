package com.param.utility;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.logging.Level;
import java.util.logging.Logger;
@SuppressWarnings("unchecked")
@Repository
public class TimeZoneDaoImpl implements ITimeZoneDao {

	private static final Logger logger = Logger.getLogger(TimeZoneDaoImpl.class.getName());

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public List<UnitMasterDto> getUnitTimeZones() {
		try {
			String query = "select unit_id as \"unitId\",time_zone as \"unitTimeZone\" from m_unit_master mum where mum.status='A' ";
			List<UnitMasterDto> timeZoneList = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setResultTransformer(Transformers.aliasToBean(UnitMasterDto.class)).list();
			return timeZoneList;
		} catch (Exception e) {
			logger.log(Level.SEVERE, "getUnitTimeZones failed", e);
			return null;
		}
	}

	@Override
	public List<TimeZoneDto> getTimeZones() {
		try {
			String query = "select tz_code as \"code\",tz_value as \"value\" from m_time_zones mum ";
			List<TimeZoneDto> timeZoneList = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setResultTransformer(Transformers.aliasToBean(TimeZoneDto.class)).list();
			return timeZoneList;
		} catch (Exception e) {
			logger.log(Level.SEVERE, "getTimeZones failed", e);
			return null;
		}
	}

}
