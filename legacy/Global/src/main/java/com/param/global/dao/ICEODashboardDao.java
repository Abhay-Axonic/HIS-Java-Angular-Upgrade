package com.param.global.dao;

import java.util.List;

import com.param.global.common.Response;
import com.param.global.dto.BillStatDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface ICEODashboardDao {
	Response getDashboardBedOccupancyList(BillStatDto billStatDto) throws ApplicationException;

	Response getBillingListByVisitType(BillStatDto billStatDto) throws ApplicationException;

	Response getOperationCountList(BillStatDto billStatDto) throws ApplicationException;

	Response getAdmissionDischargeDeathCount(BillStatDto billStatDto) throws ApplicationException;

	Response getOPVisitsCount(BillStatDto billStatDto)throws ApplicationException;

	Response getFinanceDetailsCount(BillStatDto billStatDto) throws ApplicationException;

	Response postDashboardCounts(String date, List<Integer> unitIds)throws ApplicationException;

	Response pendingBills(BillStatDto billStatDto);


}
