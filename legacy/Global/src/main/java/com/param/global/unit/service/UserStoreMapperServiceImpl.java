package com.param.global.unit.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.IError;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.unit.dao.IUserStoreMapperDao;
import com.param.global.unit.dto.UserStoreMapperDto;

import in.param.exception.ApplicationException;
@Service
@SuppressWarnings({"rawtypes", "unchecked"})
public class UserStoreMapperServiceImpl implements IUserStoreMapperService , ICommonConstants, IError{

	private static final Logger logger = Logger.getLogger(UserStoreMapperServiceImpl.class);

	@Autowired
	IUserStoreMapperDao iUserStoreMapperDao;

	@Override
	public Response saveUserStoreMapper(UserStoreMapperDto userStoreMapperDto) throws ApplicationException {
		try
		{
			Response res = iUserStoreMapperDao.checkUserStoreMapperAvailable(userStoreMapperDto);
			if(res.getListObject() != null && res.getListObject().size() > 0){

				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);

			}else{
			Iterator itr=userStoreMapperDto.getSelectUserStoreMapperList().iterator();
			while (itr.hasNext()) {
				UserStoreMapperDto dto = new UserStoreMapperDto();
				Integer storeId = (Integer) itr.next();
				dto.setAddedDate(userStoreMapperDto.getAddedDate());
				dto.setUserId(userStoreMapperDto.getUserId());
				dto.setStoreId(storeId);
				dto.setIsActive(userStoreMapperDto.getIsActive());
				dto.setOrganizationId(userStoreMapperDto.getOrganizationId());
				dto.setUnitId(userStoreMapperDto.getUnitId());
				dto.setAddedBy(userStoreMapperDto.getAddedBy());
				dto.setUpdatedBy(userStoreMapperDto.getUpdatedBy());
				dto.setUpdatedDate(userStoreMapperDto.getUpdatedDate());
				dto.setIsDeleted(userStoreMapperDto.getIsDeleted());

				iUserStoreMapperDao.saveUserStoreMapper(dto);
			}
			}

			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (Exception e)
		{
			logger.error("saveUserStoreMapper failed", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}
	@Override
	public Response updateUserStoreMapper(UserStoreMapperDto userStoreMapperDto) throws ApplicationException {
		try
		{
			List<UserStoreMapperDto> listuserStoreMapperDto = new ArrayList();
			Iterator itr=userStoreMapperDto.getSelectUserStoreMapperList().iterator();
			while (itr.hasNext()) {
				UserStoreMapperDto dto=new UserStoreMapperDto();

				Integer storeId = (Integer) itr.next();
				dto.setUpdatedDate(userStoreMapperDto.getUpdatedDate());
				dto.setUserId(userStoreMapperDto.getUserId());
				dto.setAddedBy(userStoreMapperDto.getAddedBy());
				dto.setOrganizationId(userStoreMapperDto.getOrganizationId());
				dto.setUnitId(userStoreMapperDto.getUnitId());
				dto.setIsActive(userStoreMapperDto.getIsActive());
				dto.setAddedDate(userStoreMapperDto.getAddedDate());
				dto.setIsDeleted(userStoreMapperDto.getIsDeleted());
				dto.setStoreId(storeId);
				dto.setOrganizationId(userStoreMapperDto.getOrganizationId());
				dto.setUpdatedBy(userStoreMapperDto.getUpdatedBy());
				listuserStoreMapperDto.add(dto);
			}
			if(!listuserStoreMapperDto.isEmpty())
			{
				return iUserStoreMapperDao.updateUserStoreMapper(listuserStoreMapperDto);
			}
			else {
				return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
			}
			} catch (Exception e)
		{
			logger.error("updateUserStoreMapper failed", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}
	@Override
	public Response updateUserStoreMapperStatus(Integer userId, Integer organisationId, Integer unitId,
			Boolean isActive) throws ApplicationException {
		try
		{
			return iUserStoreMapperDao.updateUserStoreMapperStatus(userId, organisationId, unitId, isActive);
		} catch (Exception e)
		{
			logger.error("updateUserStoreMapperStatus failed", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}


	@Override
	public Response getUserStoreMapperlist(UserStoreMapperDto userStoreMapperDto) throws ApplicationException {
		try
		{
			return iUserStoreMapperDao.getUserStoreMapperlist(userStoreMapperDto);
		} catch (Exception e)
		{
			logger.error("getUserStoreMapperlist failed", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}

	@Override
	public Response getUserStoreMapperlistUserNameWise(UserStoreMapperDto userStoreMapperDto) throws ApplicationException {
		try
		{
			return iUserStoreMapperDao.getUserStoreMapperlistUserNameWise(userStoreMapperDto);
		} catch (Exception e)
		{
			logger.error("getUserStoreMapperlistUserNameWise failed", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}


	@Override
	public Response getUserStoreMapperCount(Integer organisationId, Integer unitId) throws ApplicationException {
		try
		{
			return iUserStoreMapperDao.getUserStoreMapperCount(organisationId,unitId);
		} catch (Exception e)
		{
			logger.error("getUserStoreMapperCount failed", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}


	@Override
	public Response getActiveUserStoreMapper(Integer organisationId, Integer unitId) throws ApplicationException {
		try
		{
			return iUserStoreMapperDao.getActiveUserStoreMapper(organisationId, unitId);
		} catch (Exception e)
		{
			logger.error("getActiveUserStoreMapper failed", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}


	@Override
	public Response getUserStoreMapperById(Integer userId, Integer organisationId, Integer unitId)
			throws ApplicationException {
		try
		{
			return iUserStoreMapperDao.getUserStoreMapperById(userId, organisationId, unitId);
		} catch (Exception e)
		{
			logger.error("getUserStoreMapperById failed", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}


	@Override
	public Response checkUserStoreMapperAvailable(UserStoreMapperDto userStoreMapperDto)
			throws ApplicationException {
		try
		{
			return iUserStoreMapperDao.checkUserStoreMapperAvailable(userStoreMapperDto);
		} catch (Exception e)
		{
			logger.error("checkUserStoreMapperAvailable failed", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}

//	@Override
//	public Response getUserList(Integer organisationId, Integer unitId) throws ApplicationException {
//		try
//		{
//			return iUserStoreMapperDao.getUserList(organisationId, unitId);
//		} catch (Exception e)
//		{
//
//			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
//		}
//	}

	@Override
	public Response getDetailedStoreList(Integer userId, Integer organisationId, Integer unitId)
			throws ApplicationException {
		try
		{
			return iUserStoreMapperDao.getDetailedStoreList(userId, organisationId, unitId);
		} catch (Exception e)
		{
			logger.error("getDetailedStoreList failed", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}

	@Override
	public Response getStoreList(Integer organisationId, Integer unitId) throws ApplicationException {
		try
		{
			return iUserStoreMapperDao.getStoreList(organisationId, unitId);
		} catch (Exception e)
		{
			logger.error("getStoreList failed", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}

}
