package com.param.global.procedure.service;

import javax.transaction.Transactional;

import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.procedure.dao.IProcedureTypeMasterDao;
import com.param.global.procedure.dto.ProcedureTypeMasterDto;
import com.param.otims.model.ProcedureTypeMaster;
import com.param.rabbitmq.dto.base.Payload;
import com.param.rabbitmq.dto.base.Request;
import com.param.rabbitmq.service.EmrService.IEmrService;
import com.param.rabbitmq.service.OtimsService.IOtimsService;
import com.param.rabbitmq.util.constants.MasterKeyConstants;

@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class ProcedureTypeMasterServiceImpl implements IProcedureTypeMasterService, ICommonConstants{

	private static final Logger logger = Logger.getLogger(ProcedureTypeMasterServiceImpl.class);

	@Autowired
	private IProcedureTypeMasterDao iProcedureTypeMasterDao;
	
	@Autowired
	private IOtimsService iOtimsService;
	
	@Autowired
	IEmrService EmrService;
	
	@Override
	@Transactional
	public Response getProcedureTypeList(int orgId, int unitId) throws ApplicationException {
		try{
			return iProcedureTypeMasterDao.getProcedureTypeList(orgId, unitId);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response saveProcedureTypeMaster(ProcedureTypeMasterDto procedureTypeMasterDto) throws ApplicationException {
		try {
			Response response=iProcedureTypeMasterDao.getProcedureTypeMasterByName(procedureTypeMasterDto);
			
			if(response.getListObject().size()>0 && response.getListObject()!=null){
				
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				Response res= iProcedureTypeMasterDao.saveProcedureTypeMaster(procedureTypeMasterDto);
				if(res.getObject()!=null) {
					ProcedureTypeMaster dto = new ProcedureTypeMaster();
					dto = (ProcedureTypeMaster) res.getObject();
					procedureTypeMasterDto.setProcedureTypeId(dto.getProcedureTypeId());
					Request request = new Request();
					Payload payload = new Payload();
					payload.setActionType("create");
					payload.setData(procedureTypeMasterDto);
					request.setKey(MasterKeyConstants.ProcedureType.toString());
					request.setPayload(payload);
					//iOtimsService.publish(request);
					//EmrService.publish(request);
					
				}
				
			}	
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (Exception e) {
			logger.error("saveProcedureTypeMaster failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}


	@Override
	public Response updateProcedureTypeMaster(ProcedureTypeMasterDto procedureTypeMasterDto) throws ApplicationException {
			
		try {
			Response response=iProcedureTypeMasterDao.getProcedureTypeMasterByNameNotId(procedureTypeMasterDto);
			
			if(response.getListObject().size()>0 && response.getListObject()!=null){
				
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				 
				iProcedureTypeMasterDao.updateProcedureTypeMaster(procedureTypeMasterDto);
				
				
				Request request = new Request();
				Payload payload = new Payload();
				payload.setActionType("update");
				payload.setData(procedureTypeMasterDto);
				request.setKey(MasterKeyConstants.ProcedureType.toString());
				request.setPayload(payload);
				//iOtimsService.publish(request);
				//EmrService.publish(request);
				
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);

			}

		} catch (Exception e) {
			logger.error("updateProcedureTypeMaster failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}

	@Override
	public Response updateProcedureTypeMasterStatus(ProcedureTypeMasterDto procedureTypeMasterDto) throws ApplicationException {
			
		try {
			return iProcedureTypeMasterDao.updateProcedureTypeMasterStatus(procedureTypeMasterDto);

		} catch (Exception e) {
			logger.error("updateProcedureTypeMasterStatus failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}

	@Override
	public Response getListProcedureTypeMaster(ProcedureTypeMasterDto procedureTypeMasterDto) throws ApplicationException {
			
		try {
			return iProcedureTypeMasterDao.getListProcedureTypeMaster(procedureTypeMasterDto);

		} catch (Exception e) {
			logger.error("getListProcedureTypeMaster failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@Override
	public Response getProcedureTypeMasterById(Integer procedureTypeId, Integer orgId, Integer unitId) throws ApplicationException {
			
		try {
			return iProcedureTypeMasterDao.getProcedureTypeMasterById(procedureTypeId, orgId, unitId);

		} catch (Exception e) {
			logger.error("getProcedureTypeMasterById failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}

	@Override
	public Response getProcedureTypeMasterCount(ProcedureTypeMasterDto procedureTypeMasterDto) throws ApplicationException {
			
		try {
			return iProcedureTypeMasterDao.getProcedureTypeMasterCount(procedureTypeMasterDto);

		} catch (Exception e) {
			logger.error("getProcedureTypeMasterCount failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}

	@Override
	public Response getProcedureTypeMasterByName(ProcedureTypeMasterDto procedureTypeMasterDto) throws ApplicationException {
			
		try {
			return iProcedureTypeMasterDao.getProcedureTypeMasterByName(procedureTypeMasterDto);

		} catch (Exception e) {
			logger.error("getProcedureTypeMasterByName failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}

	@Override
	public Response getProcedureTypeMasterByNameNotId(ProcedureTypeMasterDto procedureTypeMasterDto) throws ApplicationException {
			
		try {
			return iProcedureTypeMasterDao.getProcedureTypeMasterByNameNotId(procedureTypeMasterDto);

		} catch (Exception e) {
			logger.error("getProcedureTypeMasterByNameNotId failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}

}
