package com.param.billing.global.transaction.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.param.billing.common.Response;
import com.param.billing.global.transaction.dao.IInsuranceMasterDao;
import com.param.billing.global.transaction.dto.InsuranceMasterDto;
import com.param.global.common.DocUploadUtility;
import com.param.global.common.ICommonConstants;
import com.param.global.org.dao.IGlobalOrganisationMasterDao;
import com.param.global.org.dto.OrganizationMasterDto;
import com.param.billing.global.transaction.model.InsuranceMaster;
import com.param.rabbitmq.dto.base.Payload;
import com.param.rabbitmq.dto.base.Request;
import com.param.rabbitmq.service.OtimsService.IOtimsService;
import com.param.rabbitmq.util.constants.MasterKeyConstants;

import in.param.exception.ApplicationException;
@Service
@SuppressWarnings({"rawtypes"})
public class InsuranceMasterServiceImpl implements IInsuranceMasterService, ICommonConstants{

	@Autowired
	IInsuranceMasterDao iInsuranceMasterDao;
	
	@Autowired
	IGlobalOrganisationMasterDao iGlobalOrganisationMasterDao;
	
	@Autowired 
	DocUploadUtility docUploadUtility;
	
	@Autowired
	private IOtimsService iOtimsService;   
	
	@Override
	public Response saveInsuranceMaster(InsuranceMasterDto insuranceMasterDto) throws ApplicationException{
		try {
			Response response=iInsuranceMasterDao.getInsuranceMasterByName(insuranceMasterDto);
			
			if(response.getListObject().size()>0 && response.getListObject()!=null){
				
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				Response res= iInsuranceMasterDao.saveInsuranceMaster(insuranceMasterDto);
				
				if(res.getObject()!=null) {
					InsuranceMaster dto = new InsuranceMaster();
					dto = (InsuranceMaster) res.getObject();
					insuranceMasterDto.setInsuranceId(dto.getInsuranceId());
					Request request = new Request();
					Payload payload = new Payload();
					payload.setActionType("create");
					payload.setData(insuranceMasterDto);
					request.setKey(MasterKeyConstants.Insurance_Company.toString());
					request.setPayload(payload);
					//iOtimsService.publish(request);
					
					return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, dto);
				}
				
			}
		} catch (Exception e) {
			
			throw e;
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
		
	@Override
	public Response updateInsuranceMaster(InsuranceMasterDto insuranceMasterDto) throws ApplicationException{
		try {
			Response response=iInsuranceMasterDao.getInsuranceMasterByNameNotId(insuranceMasterDto);
			
			if(response.getListObject().size()>0 && response.getListObject()!=null){
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				Response res = iInsuranceMasterDao.updateInsuranceMaster(insuranceMasterDto);
				
				Request request = new Request();
				Payload payload = new Payload();
				payload.setActionType("update");
				payload.setData(insuranceMasterDto);
				request.setKey(MasterKeyConstants.Insurance_Company.toString());
				request.setPayload(payload);
				//iOtimsService.publish(request);
				return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
			}
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
		
	
	@Override
	public Response updateInsuranceMasterStatus(InsuranceMasterDto insuranceMasterDto) throws ApplicationException{
		try {
			return iInsuranceMasterDao.updateInsuranceMasterStatus(insuranceMasterDto);
		}catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
		
	
	@Override
	public Response getInsuranceMasterById(Integer insuranceId, Integer orgId, Integer unitId) throws ApplicationException{
		try {
			return iInsuranceMasterDao.getInsuranceMasterById(insuranceId, orgId, unitId);
		}catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
		

	@Override
	public Response getInsuranceMasterList(InsuranceMasterDto insuranceMasterDto) throws ApplicationException{
		try {
			return iInsuranceMasterDao.getInsuranceMasterList(insuranceMasterDto);
		} catch (Exception e) {
			
			throw e;
		}
	//	return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
		

	@Override
	public Response getInsuranceMasterCount(InsuranceMasterDto insuranceMasterDto) throws ApplicationException{
		try {
			return iInsuranceMasterDao.getInsuranceMasterCount(insuranceMasterDto);
		}catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
		

	@Override
	public Response getInsuranceMasterByName(InsuranceMasterDto insuranceMasterDto) throws ApplicationException{
		try {
			return iInsuranceMasterDao.getInsuranceMasterByName(insuranceMasterDto);
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
		

	@Override
	public Response getInsuranceMasterByNameNotId(InsuranceMasterDto insuranceMasterDto) throws ApplicationException{
		try {
			return iInsuranceMasterDao.getInsuranceMasterByNameNotId(insuranceMasterDto);
		}catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getActiveInsuranceMasterList(InsuranceMasterDto insuranceMasterDto) {
		try {
			return iInsuranceMasterDao.getActiveInsuranceMasterList(insuranceMasterDto);
		}catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getMappedGradeToInsuerance(InsuranceMasterDto insuranceMasterDto) throws ApplicationException {
		try {
			return iInsuranceMasterDao.getMappedGradeToInsuerance(insuranceMasterDto);
		}catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response saveInsuranceImage(int orgId, int unitId, int insuranceId, MultipartFile inputFile)
			throws ApplicationException {
		try{
			if(orgId>0&&unitId>0&&insuranceId>0&&!inputFile.isEmpty())
		      {
				com.param.global.common.Response unitRes =  iGlobalOrganisationMasterDao.getOrganiazationFolderPath(orgId);
		        OrganizationMasterDto organizationMasterDto = (OrganizationMasterDto) unitRes.getObject();
		        String imagePath =  docUploadUtility.uploadInsuranceImage(inputFile, organizationMasterDto,unitId,insuranceId);
		        InsuranceMasterDto dto = new InsuranceMasterDto();
		        dto.setInsuranceId(insuranceId);
		        dto.setOrgId(orgId);
		        dto.setUnitId(unitId);
		        dto.setReportPath(imagePath);
		        return iInsuranceMasterDao.updateInsuranceImage(dto);
		      }
		    return null;
		}catch(Exception e){
			
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, null, null, null);
	}

	
}
