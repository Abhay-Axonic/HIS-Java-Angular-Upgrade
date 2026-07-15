package com.param.adt.master.unit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.adt.master.unit.dao.INurseAllocationDao;
import com.param.adt.master.unit.dto.NurseAllocationDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings("rawtypes")
public class NurseAllocationServiceImpl implements INurseAllocationService,ICommonConstants{

	@Autowired
	private INurseAllocationDao iNurseAllocationDao;
	
	@Override
	public Response saveNurseAllocationMaster(NurseAllocationDto nurseAllocationDto) throws ApplicationException {
		try {
			Response res=iNurseAllocationDao.getNurseAllocationByUserId(nurseAllocationDto);
			if(res.getStatus().equals("success")){
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
			return iNurseAllocationDao.saveNurseAllocationMaster(nurseAllocationDto);
			}
			} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		
	}

	@Override
	public Response getNurseAllocationMasterList(NurseAllocationDto nurseAllocationDto) throws ApplicationException {
		try {
			return iNurseAllocationDao.getNurseAllocationMasterList(nurseAllocationDto);
		}catch (Exception e) {
				
			}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateNurseAllocationMaster(NurseAllocationDto nurseAllocationDto) throws ApplicationException {
		try {
			Response res=iNurseAllocationDao.getNurseAllocationMasterByNameNotId(nurseAllocationDto);
			if(res.getStatus().equals("success")){
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
			return iNurseAllocationDao.updateNurseAllocationMaster(nurseAllocationDto);
			}
		}catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateNurseAllocationMasterStatus(NurseAllocationDto nurseAllocationDto)
			throws ApplicationException {
		try {
			return iNurseAllocationDao.updateNurseAllocationMasterStatus(nurseAllocationDto);
		}catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getNurseAllocationMasterCount(NurseAllocationDto nurseAllocationDto) throws ApplicationException {
		try {
			return iNurseAllocationDao.getNurseAllocationMasterCount(nurseAllocationDto);
		}catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getNurseAllocationMasterById(Integer nurseAllocationId, Integer organizationId, Integer unitId)
			throws ApplicationException {
		try {
			return iNurseAllocationDao.getNurseAllocationMasterById(nurseAllocationId,organizationId,unitId);
		}catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActiveNurseAllocationMasterByWardId(Integer wardId, Integer organizationId, Integer unitId)
			throws ApplicationException {
		try {
			return iNurseAllocationDao.getActiveNurseAllocationMasterByWardId(wardId,organizationId,unitId);
		}catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActiveNurseAllocationMasterByNurseId(Integer nurseId, Integer organizationId, Integer unitId) {
		try {
			return iNurseAllocationDao.getActiveNurseAllocationMasterByNurseId(nurseId,organizationId,unitId);
		}catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}
