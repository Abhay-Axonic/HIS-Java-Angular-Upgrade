package com.param.adt.transfer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.param.adt.common.ICommonConstants;
import com.param.adt.common.Response;
import com.param.adt.templet.dto.AcceptedTransferCareDto;
import com.param.adt.transfer.dto.TransferDto;
import com.param.adt.transfer.service.ITransferRequestService;

@Controller
@RequestMapping("/adt")
@SuppressWarnings({"rawtypes","unchecked"})
public class TransferRequestController implements ICommonConstants{

	@Autowired
	ITransferRequestService iTransferRequestService;
	
	
	@RequestMapping(value = "/getTransferRequestList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getTransferRequestList(@RequestBody TransferDto transferDto) {
		try {
			return iTransferRequestService.getTransferRequestList(transferDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getTransferRequestListByAdmissionIdForB2B", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getTransferRequestListByAdmissionId(@RequestBody TransferDto transferDto) {
		try {
			return iTransferRequestService.getTransferRequestListByAdmissionId(transferDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/saveTransferRequest", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveTransferRequest(@RequestBody TransferDto transferDto) {
		try {
			return iTransferRequestService.saveTransferRequest(transferDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/acceptedTransferOfCareList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response acceptedTransferOfCareList(@RequestBody AcceptedTransferCareDto acceptedTransferCareDto) {
		try {
			return iTransferRequestService.acceptedTransferOfCareList(acceptedTransferCareDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/updateclinicatnotes", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateclinicatnotes(@RequestBody AcceptedTransferCareDto acceptedTransferCareDto) {
		try {
			return iTransferRequestService.updateclinicatnotes(acceptedTransferCareDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/allTransferOfcareNotes", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response allTransferOfcareNotes (@RequestBody AcceptedTransferCareDto acceptedTransferCareDto) {
		try {
			return iTransferRequestService.allTransferOfcareNotes(acceptedTransferCareDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/saveLaborTransferRequest", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveLaborTransferRequest (@RequestBody TransferDto transferDto) {
		try {
			return iTransferRequestService.saveLaborTransferRequest(transferDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/checkTransferRequestExistence", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response checkTransferRequestExistence (@RequestBody TransferDto transferDto) {
		try {
			return iTransferRequestService.checkTransferRequestExistence(transferDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
}
