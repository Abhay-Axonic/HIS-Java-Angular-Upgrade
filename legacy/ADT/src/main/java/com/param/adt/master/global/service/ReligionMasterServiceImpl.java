package com.param.adt.master.global.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.adt.common.ICommonConstants;
import com.param.adt.common.Response;
import com.param.adt.master.global.dao.IReligionMasterDao;
import com.param.adt.master.global.dto.ReligionMasterDto;
import com.param.adt.master.global.model.ReligionMaster;
import com.param.global.model.AreaMaster;
import com.param.rabbitmq.dto.base.Payload;
import com.param.rabbitmq.dto.base.Request;
import com.param.rabbitmq.service.EmrService.IEmrService;
import com.param.rabbitmq.util.constants.MasterKeyConstants;
import com.param.global.service.VerifiedIdentityService;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ReligionMasterServiceImpl implements IReligionMasterService, ICommonConstants {

	@Autowired
	private IReligionMasterDao iReligionDao;
	
	@Autowired
	IEmrService EmrService;   

	@Autowired
	private VerifiedIdentityService verifiedIdentityService;


	@Override
	public Response addReligionMaster(ReligionMasterDto religionMasterDto) throws ApplicationException {
		try {

			religionMasterDto.setOrganizationId(verifiedIdentityService.getVerifiedOrganizationId());
			Response deptResponse = iReligionDao.getReligionByName(religionMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {

				return new Response(ERROR, null, ALREADY_EXIST, null, null);

			} else {
				/*religionMasterDto
						.setCreatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););
				religionMasterDto
						.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););*/
				Response res = iReligionDao.addReligionMaster(religionMasterDto);
				
				if(res.getObject() != null) {
					ReligionMaster dto = new ReligionMaster();
					dto = (ReligionMaster) res.getObject();
					religionMasterDto.setId(dto.getId());
					Request request = new Request();
					Payload payload = new Payload();
					payload.setActionType("create");
					payload.setData(religionMasterDto);
					request.setKey(MasterKeyConstants.ReligionMaster.toString());
					request.setPayload(payload);
					//EmrService.publish(request);
				}
				// propagate DAO status — do not mask a failed save as SUCCESS
				if (res.getStatus() != null && res.getStatus().equals(SUCCESS)) {
					return new Response(SUCCESS, null, COMMON_SUCCESS, null, null);
				}
				return new Response(ERROR, null, null, null, null);
			}
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getReligionMasterList(ReligionMasterDto religionMasterDto) throws ApplicationException {
		try {
			religionMasterDto.setOrganizationId(verifiedIdentityService.getVerifiedOrganizationId());
			return iReligionDao.getReligionMasterList(religionMasterDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateReligionMaster(ReligionMasterDto religionMasterDto) throws ApplicationException {
		try {
			religionMasterDto.setOrganizationId(verifiedIdentityService.getVerifiedOrganizationId());
			Response deptResponse = iReligionDao.getReligionByNameNotId(religionMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {

				return new Response(ERROR, null, ALREADY_EXIST, null, null);

			} else {
				/*religionMasterDto
						.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););*/
				Response updateRes = iReligionDao.updateReligionMaster(religionMasterDto);
				if (updateRes.getStatus() == null || !updateRes.getStatus().equals(SUCCESS)) {
					// propagate DAO status — do not mask a rejected/failed update as SUCCESS
					return updateRes;
				}
				
				religionMasterDto.setCreatedBy(religionMasterDto.getUpdatedBy());
				religionMasterDto.setCreatedDate(religionMasterDto.getUpdatedDate());
				Request request = new Request();
				Payload payload = new Payload();
				payload.setActionType("update");
				payload.setData(religionMasterDto);
				request.setKey(MasterKeyConstants.ReligionMaster.toString());
				request.setPayload(payload);
				//EmrService.publish(request);
				
				return new Response(SUCCESS, null, COMMON_UPDATE, null, null);
			}
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getReligionById(ReligionMasterDto religionMasterDto) throws ApplicationException {
		try {
			religionMasterDto.setOrganizationId(verifiedIdentityService.getVerifiedOrganizationId());
			return iReligionDao.getReligionById(religionMasterDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateStatusForReligion(ReligionMasterDto religionMasterDto) throws ApplicationException {
		try {
			religionMasterDto.setOrganizationId(verifiedIdentityService.getVerifiedOrganizationId());
			Response deptResponse = iReligionDao.getReligionById(religionMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {
				/*religionMasterDto
						.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););*/
				Response res = iReligionDao.updateReligionStatus(religionMasterDto);
				if (res.getStatus() == null || !res.getStatus().equals(SUCCESS)) {
					// propagate DAO status — do not mask a rejected/failed toggle as SUCCESS
					return res;
				}
				return new Response(SUCCESS, null, COMMON_UPDATE, null, null);

			} else {
				return new Response(ERROR, null, ALREADY_EXIST, null, null);

			}
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getActiveReligionList() throws ApplicationException {
		try {
			// active-list reads bind the JWT-verified org, never all tenants
			return iReligionDao.getActiveReligionList(verifiedIdentityService.getVerifiedOrganizationId());
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getReligionCount(ReligionMasterDto religionMasterDto) throws ApplicationException {
		try {
			religionMasterDto.setOrganizationId(verifiedIdentityService.getVerifiedOrganizationId());
			return iReligionDao.getCount(religionMasterDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

}
