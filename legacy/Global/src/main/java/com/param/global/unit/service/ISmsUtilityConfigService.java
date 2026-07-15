package com.param.global.unit.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.unit.dto.SmsUtilityConfigInfoDto;
import com.param.global.unit.dto.SmsUtilityConfigurationDto;

import in.param.exception.ApplicationException;

@Transactional
@SuppressWarnings("rawtypes")
public interface ISmsUtilityConfigService {

	public Response saveSmsUtilityConfig(SmsUtilityConfigurationDto smsUtilityConfigurationDto)throws ApplicationException;
	public Response getTriggerMaster() throws ApplicationException;
	public Response getCategoryMaster() throws ApplicationException;
	public Response getSmsUtilityConfigDetails(Integer organizationId, Integer unitId)throws ApplicationException;
	public Response getSmsUtilityByTriggerId(Integer triggerId, SmsUtilityConfigInfoDto smsInfo);
	public Response getRequiredDataList(Integer triggerId)throws ApplicationException;
	public void sendBatchSMS(List<Integer> unitIds)throws ApplicationException;
	public List<SmsUtilityConfigInfoDto> getMobileList(Integer organizationId, Integer unitId, Integer triggerId)throws ApplicationException;
	public Response updateStatus(SmsUtilityConfigurationDto smsUtilityConfigurationDto)throws ApplicationException;
	 public Response getEmailUtilityByTriggerId(Integer triggerId, SmsUtilityConfigInfoDto smsInfo) throws ApplicationException, FileNotFoundException, IOException;
	public Response getBulkEmailUtilityByTriggerId(Integer triggerId, SmsUtilityConfigInfoDto smsInfo,Integer categoryId)throws ApplicationException, FileNotFoundException, IOException;
	public void triggerScheduledMails(String execution, List<Integer> unitIds);
	public Response getWhatsAppTemplateMaster(Integer organizationId, Integer unitId) throws ApplicationException;
}
