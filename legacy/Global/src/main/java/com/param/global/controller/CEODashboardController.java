package com.param.global.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import org.apache.log4j.Logger;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.BillStatDto;
import com.param.global.service.ICEODashboardService;

@RestController
@RequestMapping("/api/global")
@SuppressWarnings({"rawtypes","unchecked"})
public class CEODashboardController implements ICommonConstants{
	private static final Logger logger = Logger.getLogger(CEODashboardController.class);

	@Autowired
	private ICEODashboardService iCEODashboardService;

	@RequestMapping(value="/getDashboardBedOccupancyList" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getDashboardBedOccupancyList(@RequestBody BillStatDto billStatDto){
		try{

			return iCEODashboardService.getDashboardBedOccupancyList(billStatDto);
		}catch(Exception e){
			logger.error("Error while fetching the dashboard bed occupancy list", e);
		}
	return new Response(ERROR, null, null, null,null);
	}

	@RequestMapping(value="/getBillingListByVisitType" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getBillingListByVisitType(@RequestBody BillStatDto billStatDto){
		try{

			return iCEODashboardService.getBillingListByVisitType(billStatDto);
		}catch(Exception e){
			logger.error("Error while fetching the billing list by visit type", e);
		}
	return new Response(ERROR, null, null, null,null);
	}

	@RequestMapping(value="/getOperationCountList" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getOperationCountList(@RequestBody BillStatDto billStatDto){
		try{

			return iCEODashboardService.getOperationCountList(billStatDto);
		}catch(Exception e){
			logger.error("Error while fetching the operation count list", e);
		}
	return new Response(ERROR, null, null, null,null);
	}

	@RequestMapping(value="/getAdmissionDischargeDeathCount" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getAdmissionDischargeDeathCount(@RequestBody BillStatDto billStatDto){
		try{

			return iCEODashboardService.getAdmissionDischargeDeathCount(billStatDto);
		}catch(Exception e){
			logger.error("Error while fetching the admission discharge death count", e);
		}
	return new Response(ERROR, null, null, null,null);
	}

	@RequestMapping(value="/getOPVisitsCount" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getOPVisitsCount(@RequestBody BillStatDto billStatDto){
		try{

			return iCEODashboardService.getOPVisitsCount(billStatDto);
		}catch(Exception e){
			logger.error("Error while fetching the OP visits count", e);
		}
	return new Response(ERROR, null, null, null,null);
	}

	@RequestMapping(value="/getFinanceDetailsCount" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getFinanceDetailsCount(@RequestBody BillStatDto billStatDto){
		try{
			return iCEODashboardService.getFinanceDetailsCount(billStatDto);
		}catch(Exception e){
			logger.error("Error while fetching the finance details count", e);
		}
	return new Response(ERROR, null, null, null,null);
	}

	@RequestMapping(value="/postceodashboarddata" , method = RequestMethod.GET)
	public @ResponseBody Response postDashboardCounts(List<Integer> unitIds){
		try{
			return iCEODashboardService.postDashboardCounts(unitIds);
		}catch(Exception e){
			logger.error("Error while posting the dashboard counts", e);
		}
	return new Response(ERROR, null, null, null,null);
	}

	@RequestMapping(value="/pendingBills" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response pendingBills(@RequestBody BillStatDto billStatDto){
		try{
			return iCEODashboardService.pendingBills(billStatDto);
		}catch(Exception e){
			logger.error("Error while fetching the pending bills", e);
		}
	return new Response(ERROR, null, null, null,null);
	}
}
