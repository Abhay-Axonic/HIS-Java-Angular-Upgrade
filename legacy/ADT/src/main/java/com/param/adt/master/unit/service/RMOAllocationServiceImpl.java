package com.param.adt.master.unit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.adt.master.unit.dao.INurseAllocationDao;
import com.param.adt.master.unit.dao.IRMOAllocationDao;
import com.param.adt.master.unit.dto.NurseAllocationDto;
import com.param.adt.master.unit.dto.RmoAllocationDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings("rawtypes")
public class RMOAllocationServiceImpl implements IRMOAllocationService,ICommonConstants{

	@Autowired
	private IRMOAllocationDao iRMOAllocationDao;
	
	
	@Override
	public Response saveRmoAllocationMaster(RmoAllocationDto rmoAllocationDto) throws ApplicationException {
		try {
			Response res=iRMOAllocationDao.getRmoAllocationByRmoDoctorId(rmoAllocationDto);
			if(res.getStatus().equals("success")){
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
			return iRMOAllocationDao.saveRmoAllocationMaster(rmoAllocationDto);
			}
			} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		
	}

	@Override
	public Response getRmoAllocationMasterList(RmoAllocationDto rmoAllocationDto) throws ApplicationException {
		try {
			return iRMOAllocationDao.getRmoAllocationMasterList(rmoAllocationDto);
		}catch (Exception e) {
				
			}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateRmoAllocationMaster(RmoAllocationDto rmoAllocationDto) throws ApplicationException {
		try {
			Response res=iRMOAllocationDao.getRmoAllocationMasterByNameNotId(rmoAllocationDto);
			if(res.getStatus().equals("success")){
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
			return iRMOAllocationDao.updateRmoAllocationMaster(rmoAllocationDto);
			}
		}catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateRmoAllocationMasterStatus(RmoAllocationDto rmoAllocationDto)
			throws ApplicationException {
		try {
			return iRMOAllocationDao.updateRmoAllocationMasterStatus(rmoAllocationDto);
		}catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getRmoAllocationMasterCount(RmoAllocationDto rmoAllocationDto) throws ApplicationException {
		try {
			return iRMOAllocationDao.getRmoAllocationMasterCount(rmoAllocationDto);
		}catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getRmoAllocationMasterById(Integer rmoAllocationId, Integer organizationId, Integer unitId)
			throws ApplicationException {
		try {
			return iRMOAllocationDao.getRmoAllocationMasterById(rmoAllocationId,organizationId,unitId);
		}catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActiveRmoAllocationMasterByWardId(Integer wardId, Integer organizationId, Integer unitId)
			throws ApplicationException {
		try {
			return iRMOAllocationDao.getActiveRmoAllocationMasterByWardId(wardId,organizationId,unitId);
		}catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActiveRmoAllocationMasterByDoctorId(Integer doctorId, Integer organizationId, Integer unitId) {
		try {
			return iRMOAllocationDao.getActiveRmoAllocationMasterByDoctorId(doctorId,organizationId,unitId);
		}catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getDoctorListByRmoallocationId(RmoAllocationDto rmoAllocationDto) throws ApplicationException {
		try {
			return iRMOAllocationDao.getDoctorListByRmoallocationId(rmoAllocationDto);
		}catch (Exception e) {
				
			}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActiveRmoAllocationMasterByRMODoctorId(RmoAllocationDto rmoAllocationDto)
			throws ApplicationException {
		try {
			return iRMOAllocationDao.getActiveRmoAllocationMasterByRMODoctorId(rmoAllocationDto);
		}catch (Exception e) {
				
			}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getListOfDoctorMappedToRmo(RmoAllocationDto rmoAllocationDto) throws ApplicationException {
		try {
			return iRMOAllocationDao.getListOfDoctorMappedToRmo(rmoAllocationDto);
		}catch (Exception e) {
				
			}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


}
