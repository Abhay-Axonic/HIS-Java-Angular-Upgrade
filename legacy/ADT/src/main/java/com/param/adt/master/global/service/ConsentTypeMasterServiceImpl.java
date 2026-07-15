package com.param.adt.master.global.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.adt.common.ICommonConstants;
import com.param.adt.common.Response;
import com.param.adt.master.global.dao.IConsentTypeMasterDao;
import com.param.adt.master.global.dto.ConsentTypeMasterDto;
import com.param.adt.master.global.model.ConsentTypeMaster;
import com.param.global.model.AreaMaster;
import com.param.rabbitmq.dto.base.Payload;
import com.param.rabbitmq.dto.base.Request;
import com.param.rabbitmq.service.EmrService.IEmrService;
import com.param.rabbitmq.service.OtimsService.IOtimsService;
import com.param.rabbitmq.util.constants.MasterKeyConstants;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class ConsentTypeMasterServiceImpl implements ICommonConstants,IConsentTypeMasterService
{

	@Autowired
	IConsentTypeMasterDao iConsentTypeDao;
	
	@Autowired
	private IOtimsService iOtimsService; 
	
	@Autowired
	IEmrService EmrService;   

	
	@Override
	public Response addConsentTypeMaster(ConsentTypeMasterDto consentTypeMasterDto) throws ApplicationException {
		try {

			Response deptResponse = iConsentTypeDao.getConsentTypeByName(consentTypeMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {

				return new Response(ERROR, null, ALREADY_EXIST, null, null);

			} else {
				/*consentTypeMasterDto
						.setCreatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););
				consentTypeMasterDto
						.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););*/
				Response res = iConsentTypeDao.addConsentTypeMaster(consentTypeMasterDto);
				
				if(res.getObject() != null) {
					ConsentTypeMaster dto = new ConsentTypeMaster();
					dto = (ConsentTypeMaster) res.getObject();
					consentTypeMasterDto.setConsentTypeMasterId(dto.getConsentTypeMasterId());
					Request request = new Request();
					Payload payload = new Payload();
					payload.setActionType("create");
					payload.setData(consentTypeMasterDto);
					request.setKey(MasterKeyConstants.ConsentTypeMaster.toString());
					request.setPayload(payload);
					//EmrService.publish(request);
					//iOtimsService.publish(request);
				}
				return new Response(SUCCESS, null, COMMON_SUCCESS, null, null);
			}
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getConsentTypeMasterList(ConsentTypeMasterDto consentTypeMasterDto) throws ApplicationException {
		try {
			return iConsentTypeDao.getConsentTypeMasterList(consentTypeMasterDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateConsentTypeMaster(ConsentTypeMasterDto consentTypeMasterDto) throws ApplicationException {
		try {
			Response deptResponse = iConsentTypeDao.getConsentTypeByNameNotId(consentTypeMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {

				return new Response(ERROR, null, ALREADY_EXIST, null, null);

			} else {
				/*consentTypeMasterDto
						.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););*/
				iConsentTypeDao.updateConsentTypeMaster(consentTypeMasterDto);
				
				consentTypeMasterDto.setCreatedBy(consentTypeMasterDto.getUpdatedBy());
				consentTypeMasterDto.setCreatedDate(consentTypeMasterDto.getUpdatedDate());
				Request request = new Request();
				Payload payload = new Payload();
				payload.setActionType("update");
				payload.setData(consentTypeMasterDto);
				request.setKey(MasterKeyConstants.ConsentTypeMaster.toString());
				request.setPayload(payload);
				//EmrService.publish(request);
				//iOtimsService.publish(request);
				return new Response(SUCCESS, null, COMMON_UPDATE, null, null);
			}
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getConsentTypeById(ConsentTypeMasterDto consentTypeMasterDto) throws ApplicationException {
		try {
			return iConsentTypeDao.getConsentTypeById(consentTypeMasterDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateStatusForConsentType(ConsentTypeMasterDto consentTypeMasterDto) throws ApplicationException {
		try {
			Response deptResponse = iConsentTypeDao.getConsentTypeById(consentTypeMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {
			
				/*consentTypeMasterDto
						.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););*/
				iConsentTypeDao.updateConsentTypeStatus(consentTypeMasterDto);
				return new Response(SUCCESS, null, COMMON_UPDATE, null, null);

			} else {
				return new Response(ERROR, null, ALREADY_EXIST, null, null);

			}
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getActiveConsentTypeList(Integer orgId,Integer unitId) throws ApplicationException {
		try {
			return iConsentTypeDao.getActiveConsentTypeList(orgId,unitId);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getConsentTypeCount(ConsentTypeMasterDto consentTypeMasterDto) throws ApplicationException {
		try {
			return iConsentTypeDao.getCount(consentTypeMasterDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

}
