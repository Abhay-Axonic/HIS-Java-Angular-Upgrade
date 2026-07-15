package com.param.billing.global.transaction.controller;

import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.billing.common.ICommonConstants;
import com.param.billing.global.dto.BillListReqDto;
import com.param.billing.global.transaction.dto.DoctorAdvanceDto;
import com.param.billing.global.transaction.dto.DoctorShareCrNoteDetailsDto;
import com.param.billing.global.transaction.dto.DoctorShareCrNoteDto;
import com.param.billing.global.transaction.dto.TDoctorShareRespDto;
import com.param.billing.global.transaction.service.IDoctorShareCRNoteService;
import com.param.global.common.Response;

@RestController
@RequestMapping(value="api")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DoctorShareCRNoteController implements ICommonConstants{

	
	@Autowired
	private IDoctorShareCRNoteService iDoctorShareCRNoteService;
	
	@RequestMapping(value = "/docShare/cr/bills/pending", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getDoctorShareList(@RequestBody BillListReqDto reqDto) {
		Response<TDoctorShareRespDto, Integer> response = null;
		try {
			response =iDoctorShareCRNoteService.getPendingBills(reqDto);
			return response;
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/docShare/cr/bills/pendingSearch", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getDoctorShareListpendingSearch(@RequestBody BillListReqDto reqDto) {
		Response<TDoctorShareRespDto, Integer> response = null;
		try {
			response =iDoctorShareCRNoteService.getPendingBillsSearch(reqDto);
			return response;
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	
	@RequestMapping(value = "/docShare/cr/request/doctorCr", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response requestForDoctorCr(@RequestBody DoctorShareCrNoteDto doctorShareCrNoteDto) {
		Response<TDoctorShareRespDto, Integer> response = null;
		try {
			response =iDoctorShareCRNoteService.requestForDoctorCr(doctorShareCrNoteDto);
			return response;
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/docShare/cr/doctorCr/pending/requests", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getDoctorCrRequests(@RequestBody BillListReqDto reqDto) {
		Response<TDoctorShareRespDto, Integer> response = null;
		try {
			response =iDoctorShareCRNoteService.getDoctorCrRequests(reqDto);
			return response;
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/docShare/cr/doctorCr/requests/details/{doctorCrId}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getDoctorCrRequestDetails(@PathVariable("doctorCrId") Integer doctorCrId) {
		Response<TDoctorShareRespDto, Integer> response = null;
		try {
			response =iDoctorShareCRNoteService.getCrRequestDetails(doctorCrId);
			return response;
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/docShare/cr/doctorCrs/{doctorCrId}", method = RequestMethod.GET)
	public @ResponseBody Response getAcceptRequest(@PathVariable("doctorCrId") Integer doctorCrId) {
		Response<TDoctorShareRespDto, Integer> response = null;
		try {
			response =iDoctorShareCRNoteService.getCrRequestDetails(doctorCrId);
			return response;
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/docShare/cr/doctorCr/doctorsList/{billId}", method = RequestMethod.GET)
	public @ResponseBody Response doctorsListForDoctorCr(@PathVariable("billId") Integer billId) {
		Response<TDoctorShareRespDto, Integer> response = null;
		try {
			response =iDoctorShareCRNoteService.doctorsListForDoctorCr(billId);
			return response;
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/docShare/cr/doctorCr/accept/requests", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response acceptDoctorCrRequests(@RequestBody DoctorShareCrNoteDetailsDto doctorShareCrNoteDetailsDto) {
		Response<TDoctorShareRespDto, Integer> response = null;
		try {
			response =iDoctorShareCRNoteService.acceptDoctorCrRequests(doctorShareCrNoteDetailsDto);
			return response;
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/docShare/cr/doctorCr/reject/requests", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response rejectDoctorCrRequests(@RequestBody DoctorShareCrNoteDetailsDto doctorShareCrNoteDetailsDto) {
		Response<TDoctorShareRespDto, Integer> response = null;
		try {
			response =iDoctorShareCRNoteService.rejectDoctorCrRequests(doctorShareCrNoteDetailsDto);
			return response;
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/docShare/cr/inactive", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getInactiveCredit(@RequestBody DoctorShareCrNoteDetailsDto doctorShareCrNoteDetailsDto) {
		Response<TDoctorShareRespDto, Integer> response = null;
		try {
			response =iDoctorShareCRNoteService.getInactiveCredit(doctorShareCrNoteDetailsDto);
			return response;
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/docShare/cr/doctorSharePaymentVoucher/doctorCrDetails", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getDoctorCrDetailsForDoctorSharePaymentVoucher (@RequestBody DoctorShareCrNoteDetailsDto doctorShareCrNoteDetailsDto) {
		Response<TDoctorShareRespDto, Integer> response = null;
		try {
			response =iDoctorShareCRNoteService.getDoctorCrDetailsForDoctorSharePaymentVoucher(doctorShareCrNoteDetailsDto);
			return response;
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/doctor/advance/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveDoctorAdvance(@RequestBody DoctorAdvanceDto doctorAdvanceDto) {
		Response response = null;
		try {
			response =iDoctorShareCRNoteService.saveDoctorAdvance(doctorAdvanceDto);
			return response;
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/doctor/advance/get/list", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getDoctorAdvanceData(@RequestBody DoctorAdvanceDto doctorAdvanceDto) {
		Response response = null;
		try {
			response =iDoctorShareCRNoteService.getDoctorAdvanceData(doctorAdvanceDto);
			return response;
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	@RequestMapping(value = "/doctor/advance/update", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateDoctorAdvanceDetails(@RequestBody DoctorAdvanceDto doctorAdvanceDto) {
		try{
			
			return iDoctorShareCRNoteService.updateDoctorAdvanceDetails(doctorAdvanceDto);
		}catch(HibernateException e){
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/doctor/advance/status", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateStatusDoctorAdvanceDetails(@RequestBody DoctorAdvanceDto doctorAdvanceDto) {
		try{
			return iDoctorShareCRNoteService.updateStatusDoctorAdvanceDetails(doctorAdvanceDto);
		}catch(HibernateException e){
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/doctor/advance/accept", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response acceptDoctorAdvance(@RequestBody DoctorAdvanceDto doctorAdvanceDto) {
		Response response = null;
		try {
			response =iDoctorShareCRNoteService.acceptDoctorAdvance(doctorAdvanceDto);
			return response;
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/doctor/advance/reject", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response rejectDoctorAdvance(@RequestBody DoctorAdvanceDto doctorAdvanceDto) {
		Response response = null;
		try {
			response =iDoctorShareCRNoteService.rejectDoctorAdvance(doctorAdvanceDto);
			return response;
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
}
