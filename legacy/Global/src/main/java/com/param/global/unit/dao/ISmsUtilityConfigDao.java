package com.param.global.unit.dao;

import java.util.List;

import com.param.global.common.Response;
import com.param.global.unit.dto.SmsUtilityConfigInfoDto;
import com.param.global.unit.dto.SmsUtilityConfigurationDto;

import in.param.exception.ApplicationException;

@SuppressWarnings({ "rawtypes" })
public interface ISmsUtilityConfigDao {

	public Response saveSmsUtilityConfig(SmsUtilityConfigurationDto smsUtilityConfigurationDto);

	public Response getTriggerMaster();

	public Response getCategoryMaster();

	public Response getSmsUtilityConfigDetails(Integer organizationId, Integer unitId);

	public Response getSmsUtilityByTriggerId(Integer triggerId, SmsUtilityConfigInfoDto smsInfo);

	public Response getRequiredDataList(Integer triggerId);

	public List<SmsUtilityConfigInfoDto> getListOfMobileNoForScheduleSmsUtility(Integer orgId,Integer unitId,Integer triggerId)throws ApplicationException;

	public Response getSmsTemplateDetails(List<Integer> unitIds)throws ApplicationException;

	public Response getCollectionSMSDetails(Integer organizationId, Integer unitId)throws ApplicationException;

	public Response updateStatus(SmsUtilityConfigurationDto smsUtilityConfigurationDto)throws ApplicationException;

	public Response getEmailUtilityByTriggerId(Integer triggerId, SmsUtilityConfigInfoDto smsInfo)throws ApplicationException;

	public Response getEmailBulkUtilityByTriggerId(Integer triggerId, SmsUtilityConfigInfoDto smsInfo,
			Integer categoryId)throws ApplicationException;

	public Response getMailUtilityByUnitId(String execution, Integer integer);
	public Response getWhatsAppTemplateMaster(Integer organizationId, Integer unitId);

	

}
