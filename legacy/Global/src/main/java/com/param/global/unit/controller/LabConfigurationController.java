package com.param.global.unit.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.billing.dto.POSMachineResponseMapperDto;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.unit.dao.IMedallRequestResponseDao;
import com.param.global.unit.dto.LabConfigurationDto;
import com.param.global.unit.dto.MedallRequestResponseDto;
import com.param.global.unit.service.ILabConfigurationService;

import scala.Array;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@SuppressWarnings({"rawtypes"})
@RequestMapping("/lab")
public class LabConfigurationController implements ICommonConstants {
	final static Logger logger = Logger.getLogger(LabConfigurationController.class);

	
	@Autowired
	private ILabConfigurationService iLabConfigurationService;
	
	@Autowired
	IMedallRequestResponseDao iMedallRequestResponseDao; 
	
	@RequestMapping(value="/config", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveLabConfiguration(@RequestBody LabConfigurationDto dto) {
		try {
			return iLabConfigurationService.saveLabConfiguration(dto);
		}catch (Exception e) {
			
			throw e;
		}
	}

	@RequestMapping(value="/config", method = RequestMethod.PUT,consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateLabConfiguration(@RequestBody LabConfigurationDto dto) {
		try {
			
			return iLabConfigurationService.updateLabConfiguration(dto);
		}catch (Exception e) {
			
			throw e;
		}
	}
	
	@RequestMapping(value="/config/{tLabConfigId}", method = RequestMethod.GET)
	public @ResponseBody Response getLabConfigurationById(@PathVariable Map<String,String> params) {
		try {
			int tLabConfigId = Integer.parseInt(params.get("tLabConfigId").trim());
			return iLabConfigurationService.getLabConfigurationById(tLabConfigId);
		}catch (Exception e) {
			
			throw e;
		}
	}
	@RequestMapping(value="/config/{tLabConfigId}/{status}", method = RequestMethod.GET)
	public @ResponseBody Response updateConfigStatus(@PathVariable Map<String,String> params) {
		try {
			int tLabConfigId = Integer.parseInt(params.get("tLabConfigId").trim());
			char status =params.get("status").trim().charAt(0);
			return iLabConfigurationService.updateConfigStatus(tLabConfigId,status);
		}catch (Exception e) {
			
			throw e;
		}
	}
	
	@RequestMapping(value="/config/list/{orgId}/{unitId}", method = RequestMethod.GET)
	public @ResponseBody Response getLabConfigurationList(@PathVariable Map<String,String> params) {
		try {
			int orgId = Integer.parseInt(params.get("orgId").trim());
			int unitId = Integer.parseInt(params.get("unitId").trim());
			
	return iLabConfigurationService.getLabConfigurationList(orgId,unitId);
		}catch (Exception e) {
			
			throw e;
		}
	}
	
	@RequestMapping(value="/configuration/{orgId}/{unitId}", method = RequestMethod.GET)
	public @ResponseBody Response getLabConfigurationByOrgUnit(@PathVariable Map<String,String> params) {
		try {
			int orgId = Integer.parseInt(params.get("orgId").trim());
			int unitId = Integer.parseInt(params.get("unitId").trim());
			return iLabConfigurationService.getLabConfigurationByOrgUnit(orgId,unitId);
		}catch (Exception e) {
			
			throw e;
		}
	}
	@RequestMapping(value="/checkpin/{orgId}/{unitId}/{pin}/{userId}", method = RequestMethod.GET)
	public @ResponseBody Response checkPinExistOrNot(@PathVariable Map<String,String> params) {
		try {
			int orgId = Integer.parseInt(params.get("orgId").trim());
			int unitId = Integer.parseInt(params.get("unitId").trim());
			String pin = params.get("pin").trim();
			int userId = params.get("userId")!=null && params.get("userId")!="" ? Integer.parseInt(params.get("userId").trim()): 0 ;
			return iLabConfigurationService.checkPinExistOrNot(orgId,unitId,pin,userId);
		}catch (Exception e) {
			
			throw e;
		}
	}
	
	@RequestMapping(value="/pinuser/{orgId}/{unitId}/{pin}", method = RequestMethod.GET)
	public @ResponseBody Response getUserByPin(@PathVariable Map<String,String> params) {
		try {
			int orgId = Integer.parseInt(params.get("orgId").trim());
			int unitId = Integer.parseInt(params.get("unitId").trim());
			String pin = params.get("pin").trim();
			return iLabConfigurationService.getUserByPin(orgId,unitId,pin);
		}catch (Exception e) {
			
			throw e;
		}
	}

	@RequestMapping(value="/saveMedallRequestResponse", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveMedallRequestResponse(@RequestBody MedallRequestResponseDto dto) {
		try {
			return iLabConfigurationService.saveMedallRequestResponse(dto);
		}catch (Exception e) {
			
			throw e;
		}
	}

	@RequestMapping(value = "/responseOfMedallMachine", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response responseOfMedallMachine(@RequestBody String medallMachineResponse) {
		
		
		Response<JSONObject, Integer> response = null;
		try {
			JSONObject jsonNew = new JSONObject(medallMachineResponse); 
			MedallRequestResponseDto medReqSaveDto=new MedallRequestResponseDto();
			medReqSaveDto.setAdmissionId(null);
			medReqSaveDto.setEncounterId(null);
			medReqSaveDto.setPatientId(null);
			medReqSaveDto.setMedallBillId(null);
			medReqSaveDto.setRequestedPayload(medallMachineResponse);
			medReqSaveDto.setReturnedResponse(null);
			medReqSaveDto.setStatus('A');
			medReqSaveDto.setOrganizationId(null);
			medReqSaveDto.setOrderId((Integer.parseInt((jsonNew.get("orderId").toString()))));
			medReqSaveDto.setUnitId(null);
			medReqSaveDto.setAddedBy(null);
			medReqSaveDto.setAddedDate(GlobalCommonDateUtils.getAvalDate(new Date(), "dd-M-yyyy HH:mm:ss"));
			medReqSaveDto.setUpdatedBy(null);
			medReqSaveDto.setUpdatedDate(GlobalCommonDateUtils.getAvalDate(new Date(), "dd-M-yyyy HH:mm:ss"));
			
			Response res = iLabConfigurationService.saveMedallRequestResponse(medReqSaveDto); 
			
			//to save Lab machine interface
			JSONObject json = new JSONObject(medallMachineResponse);  
			List<MedallRequestResponseDto> listMed=new ArrayList<MedallRequestResponseDto>();
			MedallRequestResponseDto med=new MedallRequestResponseDto();
			med.setRequestedPayload(medallMachineResponse);
			med.setMachineName(json.get("machineName") != null ? json.get("machineName").toString():"");
			med.setSampleNo(json.get("sampleNo") != null ? json.get("sampleNo").toString() : "");
			if(!json.get("orderId").equals(null) && !json.get("orderId").equals("")) {
				med.setOrderId((Integer.parseInt((json.get("orderId").toString()))));
			}else {
				return new Response(ERROR, ERROR_CODE, "Please enter order id.", null, null);
			}
			med.setOrderId(json.get("orderId") != null ? (Integer.parseInt((json.get("orderId").toString()))) : null);
			med.setObjectString(json.get("testDetailsList") != null ? json.get("testDetailsList").toString() : "");
			
			JSONArray servicesArray = (JSONArray) json.get("testDetailsList");
			if (servicesArray.length() > 0) {
				for (int i = 0; i < servicesArray.length(); i++) {
					
					JSONObject servicesObj = servicesArray.getJSONObject(i);
					
					MedallRequestResponseDto medDto=new MedallRequestResponseDto();
					medDto.setParameterName(servicesObj.get("parameterName") !=null ? servicesObj.get("parameterName").toString() :"");
					medDto.setParameterUnit(servicesObj.get("parameterUnit") != null ? servicesObj.get("parameterUnit").toString() : "");
					medDto.setResultDateTime(servicesObj.get("resultDateTime") != null ? servicesObj.get("resultDateTime").toString() :"");
					medDto.setCorpbillingid(servicesObj.get("corpbillingid") != null ? Integer.parseInt(servicesObj.get("corpbillingid").toString()): null);
					medDto.setParameterValue(servicesObj.get("parameterValue") != null ? servicesObj.get("parameterValue").toString() : "");
					medDto.setMinValue(servicesObj.get("minValue") != null ? Double.parseDouble(servicesObj.get("minValue").toString()):0.0);
					medDto.setMaxValue(servicesObj.get("maxValue") != null ? Double.parseDouble(servicesObj.get("maxValue").toString()): 0.0);
					medDto.setHighValue(servicesObj.get("highValue") != null ? Double.parseDouble(servicesObj.get("highValue").toString()) : 0.0);
					medDto.setLowValue(servicesObj.get("lowValue") != null ? Double.parseDouble(servicesObj.get("lowValue").toString()) :0.0);
					medDto.setParamResult(servicesObj.get("paramResult") != null ? servicesObj.get("paramResult").toString():"");
					medDto.setNote(servicesObj.get("note") != null ? servicesObj.get("note").toString() : "");
					listMed.add(medDto);
				}
				
			}
			med.setTestDetailsList(listMed);
			return iLabConfigurationService.getMedallRequestResponse(med);
			
		} catch (Exception e) {
		logger.error("method failed", e);
			return new Response(ERROR, ERROR_CODE, null, null, null);
		}
	}

}
