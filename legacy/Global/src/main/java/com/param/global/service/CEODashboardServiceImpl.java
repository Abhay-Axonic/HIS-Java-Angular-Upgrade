package com.param.global.service;

import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.ICEODashboardDao;
import com.param.global.dto.BillStatDto;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class CEODashboardServiceImpl implements ICEODashboardService, ICommonConstants{
	final static Logger logger = Logger.getLogger(CEODashboardServiceImpl.class);

	
	@Autowired
	private ICEODashboardDao iCEODashboardDao;
	
	@Override
	public Response getDashboardBedOccupancyList(BillStatDto billStatDto) throws ApplicationException {
		try {
			return iCEODashboardDao.getDashboardBedOccupancyList(billStatDto);
		} catch (Exception e) {
		logger.error("getDashboardBedOccupancyList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getBillingListByVisitType(BillStatDto billStatDto) throws ApplicationException {
		try {
			return iCEODashboardDao.getBillingListByVisitType(billStatDto);
		} catch (Exception e) {
		logger.error("getBillingListByVisitType failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@Override
	public Response getOperationCountList(BillStatDto billStatDto) throws ApplicationException {
		try {
			return iCEODashboardDao.getOperationCountList(billStatDto);
		} catch (Exception e) {
		logger.error("getOperationCountList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	
	@Override
	public Response getAdmissionDischargeDeathCount(BillStatDto billStatDto) throws ApplicationException {
		try {
			return iCEODashboardDao.getAdmissionDischargeDeathCount(billStatDto);
		} catch (Exception e) {
		logger.error("getAdmissionDischargeDeathCount failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	@Override
	public Response getOPVisitsCount(BillStatDto billStatDto) throws ApplicationException {
		try {
			return iCEODashboardDao.getOPVisitsCount(billStatDto);
		} catch (Exception e) {
		logger.error("getOPVisitsCount failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@Override
	public Response getFinanceDetailsCount(BillStatDto billStatDto) throws ApplicationException {
		try {
			return iCEODashboardDao.getFinanceDetailsCount(billStatDto);
		} catch (Exception e) {
		logger.error("getFinanceDetailsCount failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response postDashboardCounts(List<Integer> unitIds) throws ApplicationException {
		try{
			String date = GlobalCommonDateUtils.getStringDate(DateUtils.addDays(TimeZoneComponent.getDateByzone(0), -1), "yyyy-MM-dd");
			return iCEODashboardDao.postDashboardCounts(date,unitIds);
		}catch(Exception e){

			throw e;
		}
	}

	@Override
	@Transactional
	public Response pendingBills(BillStatDto billStatDto) throws ApplicationException {
		try{
			String date = GlobalCommonDateUtils.getStringDate(DateUtils.addDays(TimeZoneComponent.getDateByzone(billStatDto.getUnitId()), -1), "yyyy-MM-dd");
			return iCEODashboardDao.pendingBills(billStatDto);
		}catch(Exception e){
			
			throw e;
		}
	}
}
