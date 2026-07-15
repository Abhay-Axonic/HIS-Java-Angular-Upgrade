package com.param.billing.global.transaction.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.billing.common.ICommonConstants;
import com.param.billing.common.Response;
import com.param.billing.global.transaction.dto.TDoctorShareProcessReqDto;
import com.param.billing.global.transaction.dto.TDoctorShareReqDto;
import com.param.billing.global.transaction.dto.TDoctorShareRespDto;
import com.param.billing.global.transaction.service.ITDoctorReferralShareService;

/**
 * DOCTOR REFERAL SHARE PROCESS
 * @author User
 *
 */
@RestController
@RequestMapping(value="api/billing")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class TDoctorReferralShareController implements ICommonConstants{
	
	@Autowired
	private ITDoctorReferralShareService iTDoctorReferralShareService;
	
	/**
	 * @author Suraj
	 *  Get Doctor Referral Share List
	 * @param tDoctorShareDto
	 * @return listDoctorReferralShareDtos
	 */
	@RequestMapping(value = "/getDoctorReferralShareList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getDoctorShareList(@RequestBody TDoctorShareReqDto  tDoctorShareDto) {
		Response<TDoctorShareRespDto, Integer> response = null;
		try {
			
			//JsonCheck.mapJsonToObject(tDoctorShareDto, TDoctorShareReqDto.class);
			response =iTDoctorReferralShareService.getDoctorReferralShareList(tDoctorShareDto);
			return response;
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}

	/**
	 * @author Suraj 
	 * Save Referral Doctor 
	 * @param reqDto
	 * @return
	 */
	@RequestMapping(value = "/saveReferralDoctorShareProcessList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveReferralDoctorShareProcessList(@RequestBody TDoctorShareProcessReqDto  reqDto) {
		Response<TDoctorShareProcessReqDto, Integer> response = null;
		try {
			response =iTDoctorReferralShareService.saveReferralDoctorShareProcessList(reqDto);
			return response;
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	/**
	 * @author Suraj
	 * Get Referral Share Voucher Deatils By Referral Share Id
	 * @param tDoctorShareDto
	 * @return
	 */
	@RequestMapping(value = "/getReferralShareVoucherDtlsByReferralShareId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getDoctorReferralSharePaymentVoucherDtlsByReferralShareId(@RequestBody TDoctorShareReqDto tDoctorShareDto) {
		Response<TDoctorShareRespDto, Integer> response = null;
		try {
			response =iTDoctorReferralShareService.getDoctorReferralSharePaymentVoucherDtlsByReferralShareId(tDoctorShareDto);
			return response;
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	

}
