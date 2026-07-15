package com.param.adt.master.global.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.adt.common.ICommonConstants;
import com.param.adt.common.Response;
import com.param.adt.master.global.dao.IEncounterTypeMasterDao;
import com.param.adt.master.global.dto.EncounterTypeMasterDto;
import com.param.adt.master.global.model.EncounterTypeMaster;
import com.param.global.model.AreaMaster;
import com.param.rabbitmq.dto.base.Payload;
import com.param.rabbitmq.dto.base.Request;
import com.param.rabbitmq.service.EmrService.IEmrService;
import com.param.rabbitmq.util.constants.MasterKeyConstants;

import in.param.exception.ApplicationException;
@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class EncounterTypeMasterServiceImpl implements IEncounterTypeMasterService,ICommonConstants
{

	@Autowired
	private IEncounterTypeMasterDao iEncounterTypeMasterDao;
	
	@Autowired
	IEmrService EmrService;   

	
	@Override
	public Response addEncounterTypeMaster(EncounterTypeMasterDto encounterTypeMasterDto) throws ApplicationException {
		try {
			Response deptResponse = iEncounterTypeMasterDao.getEncounterTypeByName(encounterTypeMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {
				
					return new Response(ERROR, null, ALREADY_EXIST, null, null);
				
			} else {
				/*encounterTypeMasterDto
						.setCreatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););
				encounterTypeMasterDto
						.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););*/
				Response res = iEncounterTypeMasterDao.addEncounterTypeMaster(encounterTypeMasterDto);
				
				if(res.getObject() != null) {
					EncounterTypeMaster dto = new EncounterTypeMaster();
					dto = (EncounterTypeMaster) res.getObject();
					encounterTypeMasterDto.setEncounterTypeId(dto.getEncounterTypeId());
					Request request = new Request();
					Payload payload = new Payload();
					payload.setActionType("create");
					payload.setData(encounterTypeMasterDto);
					request.setKey(MasterKeyConstants.EncounterType.toString());
					request.setPayload(payload);
					//EmrService.publish(request);
				}
				return new Response(SUCCESS, null, COMMON_SUCCESS, null, null);
			}
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getEncounterTypeMasterList(EncounterTypeMasterDto encounterTypeMasterDto) throws ApplicationException {
		try {
			return iEncounterTypeMasterDao.getEncounterTypeMasterList(encounterTypeMasterDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateEncounterTypeMaster(EncounterTypeMasterDto encounterTypeMasterDto)
			throws ApplicationException {
		try {
			Response deptResponse = iEncounterTypeMasterDao.getEncounterTypeByNameNotId(encounterTypeMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {
				return new Response(ERROR, null, ALREADY_EXIST, null, null);

			} else {
				/*encounterTypeMasterDto
						.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););*/
				iEncounterTypeMasterDao.updateEncounterTypeMaster(encounterTypeMasterDto);
				
				encounterTypeMasterDto.setCreatedBy(encounterTypeMasterDto.getUpdatedBy());
				encounterTypeMasterDto.setCreatedDate(encounterTypeMasterDto.getUpdatedDate());
				Request request = new Request();
				Payload payload = new Payload();
				payload.setActionType("update");
				payload.setData(encounterTypeMasterDto);
				request.setKey(MasterKeyConstants.EncounterType.toString());
				request.setPayload(payload);
				//EmrService.publish(request);
				
				return new Response(SUCCESS, null, COMMON_UPDATE, null, null);
			}
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getEncounterTypeById(EncounterTypeMasterDto encounterTypeMasterDto) throws ApplicationException {
		try {
			return iEncounterTypeMasterDao.getEncounterTypeById(encounterTypeMasterDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateStatusForEncounterType(EncounterTypeMasterDto encounterTypeMasterDto)
			throws ApplicationException {
		try {
			Response deptResponse = iEncounterTypeMasterDao.getEncounterTypeById(encounterTypeMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {
				
				/*encounterTypeMasterDto
						.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););*/
				iEncounterTypeMasterDao.updateEncounterTypeStatus(encounterTypeMasterDto);
				return new Response(SUCCESS, null, COMMON_UPDATE, null, null);

			} else {
				return new Response(ERROR, null, ALREADY_EXIST, null, null);

			}
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getActiveEncounterTypeList() throws ApplicationException {
		try {
			return iEncounterTypeMasterDao.getActiveEncounterTypeList();
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getEncounterCount(EncounterTypeMasterDto encounterTypeMasterDto) throws ApplicationException {
		try {
			return iEncounterTypeMasterDao.getCount(encounterTypeMasterDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

}
