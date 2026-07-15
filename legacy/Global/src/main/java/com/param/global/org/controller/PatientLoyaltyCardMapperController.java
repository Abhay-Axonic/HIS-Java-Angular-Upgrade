package com.param.global.org.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.org.common.dto.PatientLoyaltyCardMapperDto;
import com.param.global.org.common.service.IPatientLoyaltyCardMapperService;
import com.param.global.service.ILoyaltiCardMasterService;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping(value="/api/loyaltyCard")
@SuppressWarnings({"rawtypes"})
public class PatientLoyaltyCardMapperController  implements ICommonConstants {
	final static Logger logger = Logger.getLogger(PatientLoyaltyCardMapperController.class);

	
	@Autowired
	private IPatientLoyaltyCardMapperService iPatientLoyaltyCardMapperService;
	
	@Autowired
	ILoyaltiCardMasterService iLoyaltiCardMasterService;
	
	@RequestMapping(value = "/patientLoyaltyCard", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response savePatientLoyaltiCardMapper(@RequestBody PatientLoyaltyCardMapperDto dto){
		
		try {
			Response res = null;
			res =  iPatientLoyaltyCardMapperService.savePatientLoyaltiCardMapper(dto);
				if(res.getCode().equals(SUCCESS_CODE) && res.getObject()!=null)
				{
					PatientLoyaltyCardMapperDto mpper =  (PatientLoyaltyCardMapperDto) res.getObject();
					iPatientLoyaltyCardMapperService.updatePatientByLocaltiCard(mpper);
					//iPatientLoyaltyCardMapperService.updatePatientPatientCagtegory(mpper);
					return res;
				}
				return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		} catch (Exception e) {
		logger.error("savePatientLoyaltiCardMapper failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	
	
	@RequestMapping(value = "/getLoyaltiCard", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getAvailableActiveDepositWithCardNumber(@RequestBody PatientLoyaltyCardMapperDto dto){
		
		try {
			return iPatientLoyaltyCardMapperService.getAvailableActiveDepositWithCardNumber(dto);
			
		} catch (Exception e) {
		logger.error("getAvailableActiveDepositWithCardNumber failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/loyaltyCards/{orgId}/{unitId}", method = RequestMethod.GET)
	public Response getLoyaltyCardPatientList(@PathVariable Map<String,String> params){
		
		try {
			int orgId = Integer.parseInt(params.get("orgId").trim());
			int unitId = Integer.parseInt(params.get("unitId").trim());
			return iPatientLoyaltyCardMapperService.getLoyaltyCardPatientList(orgId,unitId);
			
		} catch (Exception e) {
		logger.error("getLoyaltyCardPatientList failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	

	@RequestMapping(value = "/getLoyaltiCardDetailsByPatientId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getLoyaltiCardDetailsByPatient(@RequestBody PatientLoyaltyCardMapperDto dto){
		
		try {
			return iPatientLoyaltyCardMapperService.getLoyaltiCardDetailsByPatient(dto);
			
		} catch (Exception e) {
		logger.error("getLoyaltiCardDetailsByPatient failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getPatientDetailsByLoyaltiCardNo", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getPatientDetailsByLoyaltiCardNo(@RequestBody PatientLoyaltyCardMapperDto dto){
		
		try {
			return iPatientLoyaltyCardMapperService.getPatientDetailsByLoyaltiCardNo(dto);
			
		} catch (Exception e) {
		logger.error("getPatientDetailsByLoyaltiCardNo failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	@RequestMapping(value = "/generateOTP", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response generateOTP(@RequestBody PatientLoyaltyCardMapperDto dto){
		
		try {
			return iPatientLoyaltyCardMapperService.generateOTP(dto);
			
		} catch (Exception e) {
		logger.error("generateOTP failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	@RequestMapping(value = "/validateOTP", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response validateOTP(@RequestBody PatientLoyaltyCardMapperDto dto){
		
		try {
			return iPatientLoyaltyCardMapperService.validateOTP(dto);
			
		} catch (Exception e) {
		logger.error("validateOTP failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
}
