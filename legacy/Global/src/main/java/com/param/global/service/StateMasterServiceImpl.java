package com.param.global.service;

import com.param.global.dto.PrefixMasterDto;
import com.param.global.model.PrefixMaster;
import com.param.global.utility.UserComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.ICityMasterDao;
import com.param.global.dao.IStateMasterDao;
import com.param.global.dto.DistrictMasterDto;
import com.param.global.dto.StateMasterDto;
import com.param.global.model.StateMaster;
import com.param.rabbitmq.dto.base.Payload;
import com.param.rabbitmq.dto.base.Request;
import com.param.rabbitmq.service.EmrService.IEmrService;
import com.param.rabbitmq.util.constants.MasterKeyConstants;

import in.param.exception.ApplicationException;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class StateMasterServiceImpl implements IStateMasterService, ICommonConstants {
	final static Logger logger = Logger.getLogger(StateMasterServiceImpl.class);


	@Autowired
	private IStateMasterDao iStateDao;

	@Autowired
	IEmrService EmrService;  
	
	@Autowired
	private ICityMasterDao iCityMasterDao;
	
	@Override
	public Response addStateMaster(StateMasterDto stateMasterDto) throws ApplicationException {
		try {
			Response deptResponse = iStateDao.getStateByName(stateMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {
				
					return new Response(ERROR, null, ALREADY_EXIST, null, null);
				
			} else {
				/*stateMasterDto
						.setCreatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););
				stateMasterDto
						.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););
				*/
				Response res = iStateDao.addStateMaster(stateMasterDto);
				
				if(res.getObject() != null) {
					StateMaster dto = new StateMaster();
					dto = (StateMaster) res.getObject();
					stateMasterDto.setStateId(dto.getStateId());

					Request request = new Request();
					Payload payload = new Payload();
					payload.setActionType("create");
					payload.setData(stateMasterDto);
					request.setKey(MasterKeyConstants.State.toString());
					request.setPayload(payload);
					//EmrService.publish(request);
				}
				return new Response(SUCCESS, null, COMMON_SUCCESS, null, res);
			}
		} catch (Exception e) {
		logger.error("addStateMaster failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getStateMasterList(StateMasterDto stateMasterDto) throws ApplicationException {
		try {
			return iStateDao.getStateMasterList(stateMasterDto);
		} catch (Exception e) {
		logger.error("getStateMasterList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateStateMaster(StateMasterDto stateMasterDto) throws ApplicationException {
		try {
			Response deptResponse = iStateDao.getStateByNameNotId(stateMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {
				return new Response(ERROR, null, ALREADY_EXIST, null, null);

			} else {
				/*stateMasterDto
						.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););
				*/

				iStateDao.updateStateMaster(stateMasterDto);

				stateMasterDto.setCreatedBy(stateMasterDto.getUpdatedBy());
				stateMasterDto.setCreatedDate(stateMasterDto.getUpdatedDate());
				
				Request request = new Request();
				Payload payload = new Payload();
				payload.setActionType("update");
				payload.setData(stateMasterDto);
				request.setKey(MasterKeyConstants.State.toString());
				request.setPayload(payload);
				//EmrService.publish(request);
				return new Response(SUCCESS, null, COMMON_UPDATE, null, null);
			}
		} catch (Exception e) {
		logger.error("updateStateMaster failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getStateById(StateMasterDto stateMasterDto) throws ApplicationException {
		try {
			return iStateDao.getStateById(stateMasterDto);
		} catch (Exception e) {
		logger.error("getStateById failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateStatusForState(StateMasterDto stateMasterDto) throws ApplicationException {
		try {
			Response deptResponse = iStateDao.getStateById(stateMasterDto);
			DistrictMasterDto dist =new DistrictMasterDto();
			dist.setStateId(stateMasterDto.getStateId());
			
			Response resp = iCityMasterDao.getActiveDistrictListByStateId(dist);
			
			if(resp.getListObject() != null && resp.getListObject().size() > 0) {
				return new Response(ERROR, ALREADY_EXIST_CODE, "This state already is in use! ", null, null);
			}
			
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {
				
				/*
				stateMasterDto
						.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););
				*/iStateDao.updateStateStatus(stateMasterDto);
				return new Response(SUCCESS, null, COMMON_UPDATE, null, null);

			} else {
				return new Response(ERROR, null, ALREADY_EXIST, null, null);

			}
		} catch (Exception e) {
		logger.error("updateStatusForState failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getActiveStateList(Integer organizationId) throws ApplicationException {
		try {
			return iStateDao.getActiveStateList(organizationId);
		} catch (Exception e) {
		logger.error("getActiveStateList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getStateCount(StateMasterDto stateMasterDto) throws ApplicationException {
		try {
			return iStateDao.getCount(stateMasterDto);
		} catch (Exception e) {
		logger.error("getStateCount failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getStateByCountryId(StateMasterDto stateMasterDto)throws ApplicationException {
		try {
			return iStateDao.getStateByCountryId(stateMasterDto);
		} catch (Exception e) {
		logger.error("getStateByCountryId failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
}
