package com.param.global.service;

import java.util.List;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.dto.BillStatDto;

import in.param.exception.ApplicationException;

@Transactional
@SuppressWarnings("rawtypes")
public interface ICEODashboardService {
	
	Response getDashboardBedOccupancyList(BillStatDto billStatDto) throws ApplicationException;
	
	Response getBillingListByVisitType(BillStatDto billStatDto)throws ApplicationException;

	Response getOperationCountList(BillStatDto billStatDto)throws ApplicationException;
	
	Response getAdmissionDischargeDeathCount(BillStatDto billStatDto)throws ApplicationException;

	Response getOPVisitsCount(BillStatDto billStatDto)throws ApplicationException;

	Response getFinanceDetailsCount(BillStatDto billStatDto)throws ApplicationException;

	Response postDashboardCounts(List<Integer> unitIds)throws ApplicationException;

	Response pendingBills(BillStatDto billStatDto)throws ApplicationException;
}
