package com.param.global.otims.service;

import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.otims.dao.IProcedureRoleMasterDao;
import com.param.global.otims.dto.ProcedureRoleMasterDto;
@Service
@SuppressWarnings({"rawtypes"})
public class ProcedureRoleMasterServiceImpl implements IProcedureRoleMasterService, ICommonConstants{

	private static final Logger logger = Logger.getLogger(ProcedureRoleMasterServiceImpl.class);

	@Autowired
	IProcedureRoleMasterDao iProcedureRoleMasterDao;
	
	@Override
	public Response saveProcedureRoleMaster(ProcedureRoleMasterDto procedureRoleMasterDto) throws ApplicationException {
		try {
				Response response=iProcedureRoleMasterDao.getProcedureRoleMasterByName(procedureRoleMasterDto);
				
				if(response.getListObject().size()>0 && response.getListObject()!=null){
					
					return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
				}else{
					return iProcedureRoleMasterDao.saveProcedureRoleMaster(procedureRoleMasterDto);
				}
			} catch (Exception e) {
				logger.error("saveProcedureRoleMaster failed", e);
			}
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response updateProcedureRoleMaster(ProcedureRoleMasterDto procedureRoleMasterDto)
			throws ApplicationException {
		try {
				Response response=iProcedureRoleMasterDao.getProcedureRoleMasterByNameNotId(procedureRoleMasterDto);
				
				if(response.getListObject().size()>0 && response.getListObject()!=null){
					
					return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
				}else{
					return iProcedureRoleMasterDao.updateProcedureRoleMaster(procedureRoleMasterDto);
				}
			} catch (Exception e) {
				logger.error("updateProcedureRoleMaster failed", e);
			}
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response updateProcedureRoleMasterStatus(ProcedureRoleMasterDto procedureRoleMasterDto)
			throws ApplicationException {
		try {
				return iProcedureRoleMasterDao.updateProcedureRoleMasterStatus(procedureRoleMasterDto);

			} catch (Exception e) {
				logger.error("updateProcedureRoleMasterStatus failed", e);
			}
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	

	@Override
	public Response getProcedureRoleMasterList(ProcedureRoleMasterDto procedureRoleMasterDto)
			throws ApplicationException {
		try {
			return iProcedureRoleMasterDao.getProcedureRoleMasterList(procedureRoleMasterDto);

			} catch (Exception e) {
				logger.error("getProcedureRoleMasterList failed", e);
			}
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	

	@Override
	public Response getProcedureRoleMasterById(Integer procedureRoleId, Integer orgId, Integer unitId)
			throws ApplicationException {
		try {
			return iProcedureRoleMasterDao.getProcedureRoleMasterById(procedureRoleId, orgId, unitId);

			} catch (Exception e) {
				logger.error("getProcedureRoleMasterById failed", e);
			}
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	
	@Override
	public Response getProcedureRoleMasterCount(ProcedureRoleMasterDto procedureRoleMasterDto)
			throws ApplicationException {
		try {
			return iProcedureRoleMasterDao.getProcedureRoleMasterCount(procedureRoleMasterDto);

			} catch (Exception e) {
				logger.error("getProcedureRoleMasterCount failed", e);
			}
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	
	@Override
	public Response getActiveProcedureRoleMasterList(ProcedureRoleMasterDto procedureRoleMasterDto)
			throws ApplicationException {
		try {
			return iProcedureRoleMasterDao.getActiveProcedureRoleMasterList(procedureRoleMasterDto);

			} catch (Exception e) {
				logger.error("getActiveProcedureRoleMasterList failed", e);
			}
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@Override
	public Response getProcedureRoleMasterByName(ProcedureRoleMasterDto procedureRoleMasterDto)
			throws ApplicationException {
		try {
			return iProcedureRoleMasterDao.getProcedureRoleMasterByName(procedureRoleMasterDto);

			} catch (Exception e) {
				logger.error("getProcedureRoleMasterByName failed", e);
			}
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getProcedureRoleMasterByNameNotId(ProcedureRoleMasterDto procedureRoleMasterDto)
			throws ApplicationException {
		try {
			return iProcedureRoleMasterDao.getProcedureRoleMasterByNameNotId(procedureRoleMasterDto);

			} catch (Exception e) {
				logger.error("getProcedureRoleMasterByNameNotId failed", e);
			}
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}
