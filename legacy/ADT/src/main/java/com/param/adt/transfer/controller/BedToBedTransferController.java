package com.param.adt.transfer.controller;

import in.param.exception.ApplicationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.param.adt.common.ICommonConstants;
import com.param.adt.common.Response;
import com.param.adt.master.global.dto.BedMasterDto;
import com.param.adt.transfer.dto.TransferDto;
import com.param.adt.transfer.service.IBedToBedTransferService;

@Controller
@RequestMapping("/adt")
@SuppressWarnings({"rawtypes","unchecked"})
public class BedToBedTransferController implements ICommonConstants{

	@Autowired
	IBedToBedTransferService iBedToBedTransferService;

	@RequestMapping(value = "/getInitiateTransferRequestList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getInitiateTransferRequestList(@RequestBody TransferDto transferDto) {
		try {
			return iBedToBedTransferService.getInitiateTransferRequestList(transferDto);
		} catch (Exception e) {

		}

		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/getInitiateTransferRequestCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getInitiateTransferRequestCount(@RequestBody TransferDto transferDto) {
		try {
			String searchKeyword = transferDto.getSearchKeyword();
			searchKeyword = (searchKeyword == null || searchKeyword.equalsIgnoreCase("null")) ? "" : searchKeyword;
			transferDto.setSearchKeyword(searchKeyword);

			return iBedToBedTransferService.getInitiateTransferRequestCount(transferDto);
		} catch (Exception e) {
			throw e;
		}

//		return new Response(ERROR, null, null, null, null);
	}


	@RequestMapping(value = "/getBedListByWardIdForBedtoBedTransfer", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getBedListByWardId(@RequestBody BedMasterDto bedMasterDto) {
		try {

			return iBedToBedTransferService.getBedListByWardId(bedMasterDto);
		} catch (Exception e) {

		}

		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/getTransferListByAdmissionId/{admissionId}", method = RequestMethod.GET)
	public @ResponseBody Response getTransferListByAdmissionId(@PathVariable(value="admissionId") Integer admissionId) {
		try {
			return iBedToBedTransferService.getTransferListByAdmissionId(admissionId);
		} catch (Exception e) {

		}

		return new Response(ERROR, null, null, null, null);
	}

}
