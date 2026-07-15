package com.param.global.unit.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.unit.dto.SmsUtilityConfigInfoDto;
import com.param.global.unit.dto.SmsUtilityConfigurationDto;
import com.param.global.unit.dto.whatsAppRequestDto;
import com.param.global.unit.service.ISmsUtilityConfigService;
import com.param.global.unit.service.IwhatsAppServiceImpl;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@SuppressWarnings({ "unchecked", "rawtypes" })
public class SmsUtilityConfigController implements ICommonConstants {
	final static Logger logger = Logger.getLogger(SmsUtilityConfigController.class);


	@Autowired
	ISmsUtilityConfigService iSmsUtilityConfigService;

	
	@Autowired
	IwhatsAppServiceImpl iwhatsAppServiceImpl;
	
	@RequestMapping(value = "/sms-utility-config", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveSmsUtilityConfig(@RequestBody SmsUtilityConfigurationDto smsUtilityConfigurationDto) {
		try {
			return iSmsUtilityConfigService.saveSmsUtilityConfig(smsUtilityConfigurationDto);
		} catch (Exception e) {
		logger.error("saveSmsUtilityConfig failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/sms-utility-config/{organizationId}/{unitId}", method = RequestMethod.GET)
	public Response getsaveSmsUtilityConfig(@PathVariable("organizationId") Integer organizationId,
			@PathVariable("unitId") Integer unitId) {
		try {
			return iSmsUtilityConfigService.getSmsUtilityConfigDetails(organizationId, unitId);
		} catch (Exception e) {
		logger.error("getsaveSmsUtilityConfig failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/trigger-master", method = RequestMethod.GET)
	public Response getTriggerMaster() {
		try {
			return iSmsUtilityConfigService.getTriggerMaster();
		} catch (Exception e) {
		logger.error("getTriggerMaster failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/category-master", method = RequestMethod.GET)
	public Response getCategoryMaster() {
		try {
			return iSmsUtilityConfigService.getCategoryMaster();
		} catch (Exception e) {
		logger.error("getCategoryMaster failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/requiredData/trigger/{triggerId}", method = RequestMethod.GET)
	public Response getRequiredDataList(@PathVariable("triggerId") Integer triggerId) {
		try {
			return iSmsUtilityConfigService.getRequiredDataList(triggerId);
		} catch (Exception e) {
		logger.error("getRequiredDataList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	@RequestMapping(value = "/getMobileList/trigger/{triggerId}/{organizationId}/{unitId}", method = RequestMethod.GET)
	public List<SmsUtilityConfigInfoDto> getMobileList(@PathVariable("triggerId") Integer triggerId,@PathVariable("organizationId") Integer organizationId,@PathVariable("unitId") Integer unitId) {
		try {
			return iSmsUtilityConfigService.getMobileList(organizationId,unitId,triggerId);
		} catch (Exception e) {
		logger.error("getMobileList failed", e);
		}
		return null;
	}
	
	@RequestMapping(value = "/update/status",  method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateStatus(@RequestBody SmsUtilityConfigurationDto smsUtilityConfigurationDto) {
		try {
			return iSmsUtilityConfigService.updateStatus(smsUtilityConfigurationDto);
		} catch (Exception e) {
		logger.error("updateStatus failed", e);
		}
		return null;
	}
	
	@RequestMapping(value = "/sendWhatsAppMessage",  method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response sendWhatsAppMessage(@RequestBody whatsAppRequestDto whatsAppRequestDto) {
		try {
			return iwhatsAppServiceImpl.sendWhatsAppMessage(whatsAppRequestDto);
		} catch (Exception e) {
		logger.error("sendWhatsAppMessage failed", e);
		}
		return null;
	}
	
	@RequestMapping(value = "/sendWhatsAppMessageTwilio",  method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response sendWhatsAppMessageTwilio(@RequestBody whatsAppRequestDto whatsAppRequestDto) {
		try {
			return iwhatsAppServiceImpl.sendWhatsAppMessageTwilio(whatsAppRequestDto);
		} catch (Exception e) {
		logger.error("sendWhatsAppMessageTwilio failed", e);
		}
		return null;
	}
	
	@RequestMapping(value = "/sendWhatsAppMessageVonage",  method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response sendWhatsAppMessageVonage(@RequestBody whatsAppRequestDto whatsAppRequestDto) {
		try {
			return iwhatsAppServiceImpl.sendWhatsAppMessageVonage(whatsAppRequestDto);
		} catch (Exception e) {
		logger.error("sendWhatsAppMessageVonage failed", e);
		}
		return null;
	}
	
	@RequestMapping(value = "/authontificationKeyGenerationForHeloAPI",  method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response authontificationKeyGenerationForHeloAPI(@RequestBody whatsAppRequestDto whatsAppRequestDto) {
		try {
			return iwhatsAppServiceImpl.authontificationKeyGenerationForHeloAPI(whatsAppRequestDto);
		} catch (Exception e) {
		logger.error("authontificationKeyGenerationForHeloAPI failed", e);
		}
		return null;
	}
	
	@RequestMapping(value = "/sendWhatsAppMessageHelo",  method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response sendWhatsAppMessageHelo(@RequestBody whatsAppRequestDto whatsAppRequestDto) {
		try {
			return iwhatsAppServiceImpl.sendWhatsAppMessageHelo(whatsAppRequestDto);
		} catch (Exception e) {
		logger.error("sendWhatsAppMessageHelo failed", e);
		}
		return null;
	}
	
	@RequestMapping(value = "/sendWhatsAppMessageHeloBulkMessages",  method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response sendWhatsAppMessageHeloBulkMessages(@RequestBody whatsAppRequestDto whatsAppRequestDto) {
		try {
			return iwhatsAppServiceImpl.sendWhatsAppMessageHeloBulkMessages(whatsAppRequestDto);
		} catch (Exception e) {
		logger.error("sendWhatsAppMessageHeloBulkMessages failed", e);
		}
		return null;
	}
	
	@RequestMapping(value = "/getSmsUtilityByTriggerIdForInPatientCheckInForm",  method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getSmsUtilityByTriggerIdForInPatientCheckIn(@RequestBody SmsUtilityConfigInfoDto smsUtilityConfigInfoDto) {
		try {
			Integer TriggerId = null;
			TriggerId = smsUtilityConfigInfoDto.getTriggerId();
			return iSmsUtilityConfigService.getSmsUtilityByTriggerId(TriggerId,smsUtilityConfigInfoDto);
		} catch (Exception e) {
		logger.error("getSmsUtilityByTriggerIdForInPatientCheckIn failed", e);
		}
		return null;
	}

	@RequestMapping(value = "/getWhatsAppTemplateList/{organizationId}/{unitId}", method = RequestMethod.GET)
	public Response getWhatsAppTemplateList(@PathVariable("organizationId") Integer organizationId,@PathVariable("unitId") Integer unitId) {
		try {
			return iSmsUtilityConfigService.getWhatsAppTemplateMaster(organizationId, unitId);
		} catch (Exception e) {
		logger.error("getWhatsAppTemplateList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
}
