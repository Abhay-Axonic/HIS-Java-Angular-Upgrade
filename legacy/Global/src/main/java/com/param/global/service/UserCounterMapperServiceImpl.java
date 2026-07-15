package com.param.global.service;

import java.math.BigInteger;
import java.util.Date;
import java.util.Iterator;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.UserCounterMapperDto;
import com.param.global.unit.dao.IUserCounterMapperDao;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings("rawtypes")
@Service
public class UserCounterMapperServiceImpl implements IUserCounterMapperService, ICommonConstants {
	final static Logger logger = Logger.getLogger(UserCounterMapperServiceImpl.class);


	@Autowired
	IUserCounterMapperDao iUserCounterMapperDao;

	@Override
	@Transactional
	public Response addUserCounterMapper(UserCounterMapperDto userCounterMapperDto) throws ApplicationException {
		try {

			Date date = TimeZoneComponent.getDateByzone(userCounterMapperDto.getUnitId());
			String dateTime = GlobalCommonDateUtils.getStringDate(date, "dd-M-yyyy HH:mm:ss");
			
			BigInteger count = (BigInteger) this.checkUserIsMappedOrNotAdd(userCounterMapperDto).getObject();
			if(count.intValue()>0){
				return new Response<>(ERROR, ERROR_CODE, ALREADY_EXIST, null, null);
			}else{
			Iterator ite=userCounterMapperDto.getSelectedCounterList().iterator(); 
			while (ite.hasNext()) {
				UserCounterMapperDto dto = new UserCounterMapperDto();
				Integer counterId = (Integer) ite.next();
				dto.setCreatedDate(dateTime);
				dto.setCreatedBy(userCounterMapperDto.getCreatedBy());
				dto.setUpdatedBy(userCounterMapperDto.getUpdatedBy());
				dto.setUpdatedDate(dateTime);
				dto.setUnitId(userCounterMapperDto.getUnitId());
				dto.setOrganisationId(userCounterMapperDto.getOrganisationId());
				dto.setIsDeleted('N');
				dto.setStatus('A');
				dto.setUserId(userCounterMapperDto.getUserId());
				dto.setCounterId(counterId);
				iUserCounterMapperDao.addUserCounterMapper(dto);
			}
			}
			return new Response<>(SUCCESS, SUCCESS_CODE,SUCCESS_MESSAGE, null, null);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response updateUserCounterMapper(UserCounterMapperDto userCounterMapperDto) throws ApplicationException {
		try {

			Date date = TimeZoneComponent.getDateByzone(userCounterMapperDto.getUnitId());
			String dateTime = GlobalCommonDateUtils.getStringDate(date, "dd-M-yyyy HH:mm:ss");
			
			Iterator ite=userCounterMapperDto.getSelectedCounterList().iterator(); 
			while (ite.hasNext()) {
				UserCounterMapperDto dto = new UserCounterMapperDto();
				Integer counterId = (Integer) ite.next();
				dto.setCreatedDate(dateTime);
				dto.setCreatedBy(userCounterMapperDto.getCreatedBy());
				dto.setUpdatedBy(userCounterMapperDto.getUpdatedBy());
				dto.setUpdatedDate(dateTime);
				dto.setUnitId(userCounterMapperDto.getUnitId());
				dto.setOrganisationId(userCounterMapperDto.getOrganisationId());
				dto.setIsDeleted('N');
				dto.setStatus('A');
				dto.setUserId(userCounterMapperDto.getUserId());
				dto.setCounterId(counterId);
				iUserCounterMapperDao.updateUserCounterMapper(dto);
			}
			this.deleteUserCounerMapper(userCounterMapperDto);
			return new Response<>(SUCCESS, SUCCESS_CODE,COMMON_UPDATE, null, null);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response checkUserIsMappedOrNotAdd(UserCounterMapperDto userCounterMapperDto) throws ApplicationException {
		try {
			return iUserCounterMapperDao.checkUserIsMappedOrNotAdd(userCounterMapperDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}


	@Override
	@Transactional
	public Response listUserCounterMapper(Integer orgId, Integer unitId, Integer offset, Integer recordPerPage)
			throws ApplicationException {
		try {
			return iUserCounterMapperDao.listUserCounterMapper(orgId, unitId, offset, recordPerPage);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getTotalUserCounterMapperCount(Integer orgId, Integer unitId) throws ApplicationException {
		try {
			return iUserCounterMapperDao.getTotalUserCounterMapperCount(orgId, unitId);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response activateInactivateUserCounterMapper(Integer userId, char status) throws ApplicationException {
		try {
			return iUserCounterMapperDao.activateInactivateUserCounterMapper(userId, status);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getUserCounterMapperViewById(Integer orgId, Integer unitId, Integer userId)
			throws ApplicationException {
		try {
			return iUserCounterMapperDao.getUserCounterMapperViewById(orgId, unitId, userId);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getUserCounterMapperById(Integer orgId, Integer unitId, Integer userId)
			throws ApplicationException 
	{
		try {	
			return iUserCounterMapperDao.getUserCounterMapperById(orgId, unitId, userId);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response deleteUserCounerMapper(UserCounterMapperDto userCounterMapperDto) throws ApplicationException {
		try {	
			return iUserCounterMapperDao.deleteUserCounerMapper(userCounterMapperDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

}
