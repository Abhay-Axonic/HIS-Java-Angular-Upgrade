package com.param.global.procedure.service;

import javax.transaction.Transactional;

import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.param.common.dto.CommonResponse;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dao.IGlobalUnitMasterDao;
import com.param.global.procedure.VO.ProcedureConfigurationVO;
import com.param.global.procedure.VO.ProcedureTypeMasterVO;
import com.param.global.procedure.VO.SpecialityMasterVO;
import com.param.global.procedure.dao.IProcedureConfigurationDao;
import com.param.global.procedure.dto.ProcedureConfigurationDto;
import com.param.rabbitmq.dto.base.Payload;
import com.param.rabbitmq.dto.base.Request;
import com.param.rabbitmq.service.EmrService.IEmrService;
import com.param.rabbitmq.service.OtimsService.IOtimsService;
import com.param.rabbitmq.util.constants.MasterKeyConstants;

@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class ProcedureConfigurationServiceImpl implements IProcedureConfigurationService, ICommonConstants{

	private static final Logger logger = Logger.getLogger(ProcedureConfigurationServiceImpl.class);

	@Autowired
	private IProcedureConfigurationDao iProcedureConfigurationDao;
	
	@Autowired
	private IOtimsService iOtimsService;
	
	@Autowired
	IGlobalUnitMasterDao iGlobalUnitMasterDao;
	
	
	@Autowired
	IEmrService EmrService;
	
	
	@Override
	@Transactional
	public CommonResponse saveProcedureConfiguration(ProcedureConfigurationDto dto) throws ApplicationException {
		try{
			//check if already exists
			boolean flag = iProcedureConfigurationDao.validProcConfByServiceOrgUnitConfigId(dto.getServiceId(), dto.getOrgId(), dto.getUnitId(),dto.getProcedureConfigId());
			if(flag)
				return new CommonResponse(ERROR, ERROR_CODE, ALREADY_EXIST, null, null);
			
			dto.setStatus(ACTIVE);
			CommonResponse response = new CommonResponse();
			response.setId(iProcedureConfigurationDao.saveProcedureConfiguration(dto).longValue());
			response.setMessage("ProcedureConfiguration Inserted Successfully");
			String ProcedureName = iProcedureConfigurationDao.getProcedureNameById(dto.getProcedureTypeId());
			String SpecialityName = iProcedureConfigurationDao.getSpecialityNameById(dto.getSpecialityId());
			
			
			ProcedureTypeMasterVO procedureTypeMasterVO = new ProcedureTypeMasterVO();
			procedureTypeMasterVO.set_id(dto.getProcedureTypeId().toString());
			procedureTypeMasterVO.setName(ProcedureName);
			
			SpecialityMasterVO specialityMasterVO =new SpecialityMasterVO();
			
			specialityMasterVO.set_id(dto.getSpecialityId().toString());
			specialityMasterVO.setName(SpecialityName);
			
			ProcedureConfigurationVO procedureConfigurationVO=new ProcedureConfigurationVO(response.getId(),dto.getCptCode(),
					dto.getCptDescription(),dto.getDuration(),dto.getAnesthesiaTypeId(),dto.getStatus(),dto.getCreatedBy(),dto.getOrgId(),
					dto.getUnitId(),dto.getUpdatedBy(),dto.getService(),dto.getCreatedDate(),dto.getUpdatedDate(),
					procedureTypeMasterVO,specialityMasterVO,dto.getAnesthesiaType());
			
			//System.out.println("procedureConfigurationVO             "+procedureConfigurationVO);
		
			Request request = new Request();
			Payload payload = new Payload();
			payload.setActionType("create");
			payload.setData(procedureConfigurationVO);
			request.setKey(MasterKeyConstants.ProcedureConfigurationHISKey.toString());
			request.setPayload(payload);
			//iOtimsService.publish(request);
			//EmrService.publish(request);
//			return response;
			return new CommonResponse (SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
			
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getProcedureConfigList(int orgId, int unitId) throws ApplicationException {
		try{
			return iProcedureConfigurationDao.getProcedureConfigList(orgId,unitId);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response updateProcedureConfigStatus(int id, char status) throws ApplicationException {
		try{
			return iProcedureConfigurationDao.updateProcedureConfigStatus(id,status);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	@Transactional
	public CommonResponse updateProcedureConfiguration(ProcedureConfigurationDto dto) throws ApplicationException {
		try{
			//check if already exists
			boolean flag = iProcedureConfigurationDao.validProcConfByServiceOrgUnitConfigId(dto.getServiceId(), dto.getOrgId(), dto.getUnitId(),dto.getProcedureConfigId());
			if(flag) {
				return new CommonResponse(ERROR, ERROR_CODE, ALREADY_EXIST, null, null);
			}
			CommonResponse response = new CommonResponse();
			response.setMessage("Generic Updated Successfully");
			response.setId(iProcedureConfigurationDao.updateProcedureConfiguration(dto).longValue());
			String ProcedureName = iProcedureConfigurationDao.getProcedureNameById(dto.getProcedureTypeId());
			String SpecialityName = iProcedureConfigurationDao.getSpecialityNameById(dto.getSpecialityId());
			
			
			ProcedureTypeMasterVO procedureTypeMasterVO = new ProcedureTypeMasterVO();
			procedureTypeMasterVO.set_id(dto.getProcedureTypeId().toString());
			procedureTypeMasterVO.setName(ProcedureName);
			
			SpecialityMasterVO specialityMasterVO =new SpecialityMasterVO();
			
			specialityMasterVO.set_id(dto.getSpecialityId().toString());
			specialityMasterVO.setName(SpecialityName);
			
			ProcedureConfigurationVO procedureConfigurationVO=new ProcedureConfigurationVO(response.getId(),dto.getCptCode(),
					dto.getCptDescription(),dto.getDuration(),dto.getAnesthesiaTypeId(),dto.getStatus(),dto.getCreatedBy(),dto.getOrgId(),
					dto.getUnitId(),dto.getUpdatedBy(),dto.getService(),dto.getCreatedDate(),dto.getUpdatedDate(),
					procedureTypeMasterVO,specialityMasterVO,dto.getAnesthesiaType());
			
			//System.out.println("procedureConfigurationVO             "+procedureConfigurationVO);
			
			Request request = new Request();
			Payload payload = new Payload();
			payload.setActionType("update");
			payload.setData(procedureConfigurationVO);
			request.setKey(MasterKeyConstants.ProcedureConfigurationHISKey.toString());
			request.setPayload(payload);
			//iOtimsService.publish(request);
			//EmrService.publish(request);
//			return response;
			return new CommonResponse (SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
			
			
		}catch(Exception e){
			
			throw e;
		}
		
	}

	@Override
	@Transactional
	public Response getFuturedOrderProcedureByEncounter(int patientId, int doctorId)
			throws ApplicationException {
		try{
			return iProcedureConfigurationDao.getFuturedOrderProcedureByEncounter(patientId, doctorId);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response convertProcedure(int admissionId, int visitTypeId, int proReqId) throws ApplicationException {
		try{
			return iProcedureConfigurationDao.convertProcedure(admissionId, visitTypeId, proReqId);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getorderDetailsIdByProcedureReqId(Integer proReqId) throws ApplicationException {
		try{
			return iProcedureConfigurationDao.getorderDetailsIdByProcedureReqId(proReqId);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getFuturedOrderProcedureByAdmission(Integer admissionId) throws ApplicationException {
		try{
			return iProcedureConfigurationDao.getFuturedOrderProcedureByAdmission(admissionId);
		}catch(Exception e){
			
			throw e;
		}	}

	@Override
	@Transactional
	public Response getNotesListByProConfigId(Integer orgId, Integer unitId, Integer proConfigId)
			throws ApplicationException {
		try {
			return iProcedureConfigurationDao.getNotesListByProConfigId(orgId,unitId,proConfigId);
	} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

}
