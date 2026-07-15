package com.param.billing.global.transaction.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.billing.global.transaction.dto.TDoctorShareProcessReqDto;
import com.param.billing.global.transaction.dto.TDoctorShareReqDto;
import com.param.billing.global.transaction.dto.TDoctorShareRespDto;
import com.param.billing.global.transaction.dto.TDoctorShareSplitLogsReqDto;
import com.param.billing.global.transaction.service.IDoctorSlabService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

@RestController
@RequestMapping(value="/api/billing/doctorSlab")
@SuppressWarnings({"rawtypes"})
public class DoctorSlabController implements ICommonConstants
{
	
	@Autowired
	IDoctorSlabService iDoctorSlabService;
	
	@RequestMapping(value = "/list", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response listForDoctorSlab(@RequestBody TDoctorShareReqDto tDoctorShareDto) {
		try {
			return iDoctorSlabService.getlistForDoctorSlab(tDoctorShareDto);
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/splitRevenue", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveDoctorShareSplit(@RequestBody TDoctorShareSplitLogsReqDto masterDto) {
		try {
			return iDoctorSlabService.splitRevenue(masterDto);
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/split/details/{id}", method = RequestMethod.GET)
	public Response getDocShareSplitDetails(@PathVariable("id") Integer id) {
		try {
			return iDoctorSlabService.getDoctorSlabSplitDetails(id);
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/doctor/change", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response changeDoctor(@RequestBody TDoctorShareRespDto masterDto) {
		try {
			return iDoctorSlabService.changeDoctor(masterDto);
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/doctor/inactive", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateStatus(@RequestBody TDoctorShareRespDto masterDto) {
		try {
			return iDoctorSlabService.inactiveSplitData(masterDto);
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/voucher", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveVoucher(@RequestBody TDoctorShareProcessReqDto tDoctorShareProcessReqDto) {
		try {
			return iDoctorSlabService.saveVoucher(tDoctorShareProcessReqDto);
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/voucherDetails", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getVoucherDetails(@RequestBody TDoctorShareReqDto tDoctorShareDto) {
		try {
			return iDoctorSlabService.getVoucherDetails(tDoctorShareDto);
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/voucherSlabConsumptionDetails/{docShareVoucherId}", method = RequestMethod.GET)
	public @ResponseBody Response voucherSlabConsumptionDetails(@PathVariable("docShareVoucherId") Integer docShareVoucherId) {
		try {
			return iDoctorSlabService.voucherSlabConsumptionDetails(docShareVoucherId);
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
}
