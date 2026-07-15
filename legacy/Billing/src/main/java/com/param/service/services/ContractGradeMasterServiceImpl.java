package com.param.service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.billing.common.Response;
import com.param.global.common.ICommonConstants;
import com.param.service.dao.IContractGradeMasterDao;
import com.param.service.dto.ContractGradeMasterDto;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({"rawtypes"})
public class ContractGradeMasterServiceImpl implements IContractGradeMasterService, ICommonConstants{

	@Autowired
	IContractGradeMasterDao iContractGradeMasterDao;
	
	@Override
	public Response saveContractGradeMaster(ContractGradeMasterDto contractGradeMasterDto) throws ApplicationException {
		try {
			Response response=iContractGradeMasterDao.getContractGradeMasterByName(contractGradeMasterDto);
			
			if(response.getListObject().size()>0 && response.getListObject()!=null){
				
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				return iContractGradeMasterDao.saveContractGradeMaster(contractGradeMasterDto);
			}
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response updateContractGradeMaster(ContractGradeMasterDto contractGradeMasterDto) throws ApplicationException {
		try {
			Response response=iContractGradeMasterDao.getContractGradeMasterByNameNotId(contractGradeMasterDto);
			
			if(response.getListObject().size()>0 && response.getListObject()!=null){
				
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				return iContractGradeMasterDao.updateContractGradeMaster(contractGradeMasterDto);
			}
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateContractGradeMasterStatus(ContractGradeMasterDto contractGradeMasterDto) throws ApplicationException{
		try {
			return iContractGradeMasterDao.updateContractGradeMasterStatus(contractGradeMasterDto);
			
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getContractGradeMasterById(Integer contractGradeMasterId, Integer orgId, Integer unitId) throws ApplicationException{
		try {
			return iContractGradeMasterDao.getContractGradeMasterById(contractGradeMasterId, orgId, unitId);
			
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	

	@Override
	public Response getContractGradeMasterList(ContractGradeMasterDto contractGradeMasterDto) throws ApplicationException{
		try {
			return iContractGradeMasterDao.getContractGradeMasterList(contractGradeMasterDto);
			
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	
	@Override
	public Response getContractGradeMasterCount(ContractGradeMasterDto contractGradeMasterDto) throws ApplicationException{
		try {
			return iContractGradeMasterDao.getContractGradeMasterCount(contractGradeMasterDto);
			
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	
	@Override
	public Response getContractGradeMasterByName(ContractGradeMasterDto contractGradeMasterDto) throws ApplicationException{
		try {
			return iContractGradeMasterDao.getContractGradeMasterByName(contractGradeMasterDto);
			
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	
	@Override
	public Response getContractGradeMasterByNameNotId(ContractGradeMasterDto contractGradeMasterDto) throws ApplicationException{
		try {
			return iContractGradeMasterDao.getContractGradeMasterByNameNotId(contractGradeMasterDto);
			
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getActiveContractGradeMasterList(ContractGradeMasterDto contractGradeMasterDto) {
		try {
			return iContractGradeMasterDao.getActiveContractGradeMasterList(contractGradeMasterDto);
			
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	
	@Override
	public Response getContractGradeMasterNotMappedInMapper(ContractGradeMasterDto contractGradeMasterDto) {
		try {
			return iContractGradeMasterDao.getContractGradeMasterNotMappedInMapper(contractGradeMasterDto);
			
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
}
