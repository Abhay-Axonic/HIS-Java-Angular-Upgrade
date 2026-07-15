package com.param.global.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.param.common.dto.CommonResponse;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.ISubSpecialityMasterDao;
import com.param.global.dto.SubSpecialityMasterDto;
import com.param.rabbitmq.dto.base.Payload;
import com.param.rabbitmq.dto.base.Request;
import com.param.rabbitmq.service.EmrService.IEmrService;
import com.param.rabbitmq.service.OtimsService.IOtimsService;
import com.param.rabbitmq.util.constants.MasterKeyConstants;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({ "rawtypes", "unchecked" })
@Service
public class SubSpecialityServiceImpl implements ICommonConstants, ISubSpecialityService {
	final static Logger logger = Logger.getLogger(SubSpecialityServiceImpl.class);


	@Autowired
	private ISubSpecialityMasterDao iSubSpecialityMasterDao;
	
	@Autowired
	private IOtimsService iOtimsService;
	
	@Autowired
	IEmrService EmrService;


	@Transactional
	@Override
	public Response addSubSpecialityMaster(SubSpecialityMasterDto subspecialityMasterDto) throws ApplicationException {
		try {
			Response deptResponse = iSubSpecialityMasterDao.getSubSpecialityByName(subspecialityMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {

				return new Response(ERROR, null, ALREADY_EXIST, null, null);

			} else {

				/*subspecialityMasterDto
						.setCreatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););
				subspecialityMasterDto
						.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););*/

				iSubSpecialityMasterDao.addSubSpecialityMaster(subspecialityMasterDto);
				return new Response(SUCCESS, null, COMMON_SUCCESS, null, null);
			}
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Transactional
	@Override
	public Response getSubSpecialityMasterList(SubSpecialityMasterDto subSpecialityMasterDto) throws ApplicationException {
		// TODO Auto-generated method stub
		try {
			return iSubSpecialityMasterDao.getSubSpecialityMasterList(subSpecialityMasterDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Transactional
	@Override
	public Response getSubSpecialityById(SubSpecialityMasterDto subSpecialityMasterDto) throws ApplicationException {
		try {
			return iSubSpecialityMasterDao.getSubSpecialityById(subSpecialityMasterDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Transactional
	@Override
	public Response updateStatusForSubSpeciality(SubSpecialityMasterDto subSpecialityMasterDto)
			throws ApplicationException {
		try {
			Response deptResponse = iSubSpecialityMasterDao.getSubSpecialityById(subSpecialityMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {
				/*subSpecialityMasterDto
						.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););*/
				iSubSpecialityMasterDao.updateSubSpecialityStatus(subSpecialityMasterDto);
				return new Response(SUCCESS, null, COMMON_UPDATE, null, null);

			} else {
				return new Response(ERROR, null, ALREADY_EXIST, null, null);

			}
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		

	}

	@Transactional
	@Override
	public Response updateSubSpecialityMaster(SubSpecialityMasterDto subspecialityMasterDto)
			throws ApplicationException {
		try {
			Response deptResponse = iSubSpecialityMasterDao.getSubSpecialityByNameNotId(subspecialityMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {

				return new Response(ERROR, null, ALREADY_EXIST, null, null);

			} else {
				/*subspecialityMasterDto
						.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););*/
				iSubSpecialityMasterDao.updateSubSpecialityMaster(subspecialityMasterDto);
				
				subspecialityMasterDto.setSubSpecialityId(subspecialityMasterDto.getSubSpecialityMasterId());
				subspecialityMasterDto.setSubSpecialityName(subspecialityMasterDto.getSubSpecialityMasterName());
				CommonResponse response = new CommonResponse();
				response.setMessage("subSpeciality Updated Successfully");
				Request request = new Request();
				Payload payload = new Payload();
				payload.setActionType("update");
				payload.setData(subspecialityMasterDto);
				request.setKey(MasterKeyConstants.SubSpeciality.toString());
				request.setPayload(payload);
				//iOtimsService.publish(request);
				//EmrService.publish(request);
				
				return new Response(SUCCESS, null, COMMON_UPDATE, null, null);
			}
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		

	}
	
	@Transactional
	@Override
	public Response getSubSpecialityCount(SubSpecialityMasterDto subSpecialityMasterDto) throws ApplicationException {
		try {
			return iSubSpecialityMasterDao.getCount(subSpecialityMasterDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Transactional
	@Override
	public Response getSubSpecialityBySpecialityId(SubSpecialityMasterDto subspecialityMasterDto)
			throws ApplicationException {
		try {
			return iSubSpecialityMasterDao.getSubSpecialityBySpecialityId(subspecialityMasterDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Transactional
	@Override
	public Response getSubspecialityNotInUnit(int orgId,int unitId,int specialityId) throws ApplicationException {
		try {
			return iSubSpecialityMasterDao.getSubspecialityNotInUnit(orgId,unitId,specialityId);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}
	@Transactional
	@Override
	public Response getUnitSubSpecialityBySpecialityId(SubSpecialityMasterDto masterDto) throws ApplicationException {
		try {
			return iSubSpecialityMasterDao.getUnitSubSpecialityBySpecialityId(masterDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getSubSpecialityList(int orgId, int unitId) throws ApplicationException {
		try {
			return iSubSpecialityMasterDao.getSubSpecialityList(orgId,unitId);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response subSpecialityMasterUniversalSearch(SubSpecialityMasterDto subSpecialityMasterDto)
			throws ApplicationException {
		try {
			return iSubSpecialityMasterDao.subSpecialityMasterUniversalSearch(subSpecialityMasterDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getSubSpecialityBySpecialityList(SubSpecialityMasterDto subSpecialityMasterDto) {
		try {
			return iSubSpecialityMasterDao.getSubSpecialityBySpecialityList(subSpecialityMasterDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getSubSpecialityMRIandCT(SubSpecialityMasterDto subSpecialityMasterDto)
			throws ApplicationException {
		try {
			return iSubSpecialityMasterDao.getSubSpecialityMRIandCT(subSpecialityMasterDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}


}
