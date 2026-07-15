package com.param.global.org.service;

import com.param.global.dto.UserMasterDto;
import com.param.global.org.dto.UnitSubscriptionConfigDto;
import com.param.global.org.dto.UnitSubscriptionDetailsDto;
import in.param.exception.ApplicationException;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import com.param.common.dto.CommonResponse;
import com.param.global.common.Response;
import com.param.global.org.dto.UnitMasterDto;

import javax.transaction.Transactional;

@SuppressWarnings("rawtypes")
public interface IGlobalUnitMasterService {

	
	public CommonResponse saveUnitMaster(UnitMasterDto unitMasterDto)throws ApplicationException;
	public CommonResponse updateUnitMaster(UnitMasterDto unitMasterDto)throws ApplicationException;
	public Response getUnitById(Integer unitId)throws ApplicationException;
	public Response updateUnitStatusById(UnitMasterDto unitMasterDto)throws ApplicationException;
	public Response getAllUnitList(UnitMasterDto unitMasterDto)throws ApplicationException;
	public Response getUnitTotalCount(int orgId)throws ApplicationException;//minesh
	public Response saveUnitImage(int unitId,MultipartFile inputFile)throws ApplicationException;
	public String getUnitImage(int unitId)throws ApplicationException;
	
	
	/**@Code API for prefix numberGeneration */
	public String getGeneratedNo(int storeId,int unitId,int screenId)throws ApplicationException;
	
	public Response getUnitByOrganisation(int orgId)throws ApplicationException;
	public Response getActiveUnitListForFolderCreation()throws ApplicationException;
	public Response updateFolderPath(UnitMasterDto unitMasterDto)throws ApplicationException;
	public Response sendMailInvite(String doctorMail, String patientMail, String date, String time, String remark)throws ApplicationException;
	public Response gtMyLifeBaseUrl()throws ApplicationException;


	//Unit Subscription Logic
	Response getSubScriptionTypes() throws ApplicationException;
	Response addUpdateSubscriptionConfig(UnitSubscriptionConfigDto dto) throws ApplicationException;
	Response getSubScriptionConfig(UnitMasterDto Dto) throws ApplicationException;
	Response addSubScrtionDetails(UnitSubscriptionDetailsDto dto) throws ApplicationException;
	Response getSubcriptionDetails(UnitSubscriptionDetailsDto dto) throws ApplicationException;
	Response inactivateSubcriptions() throws ApplicationException;
	void reminderForSubscription() throws ApplicationException;
	Response checkUnitSubscription(UnitMasterDto Dto,Integer userId) throws ApplicationException;
	Response processUnitSubscription(String razorPayOrderId)throws ApplicationException;
	Response getSubscriptionById(UnitSubscriptionDetailsDto dto)throws ApplicationException;
}
