package com.param.global.service;

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
import com.param.global.dao.IUserNursingStationMapperDao;
import com.param.global.dto.UserNursingStationMapperDto;

import in.param.exception.ApplicationException;
@Service
@SuppressWarnings({"rawtypes", "unchecked"})
public class UserNursingStationMapperServiceImpl implements IUserNursingStationMapperService, ICommonConstants, IError{

	private static final Logger logger = Logger.getLogger(UserNursingStationMapperServiceImpl.class);

	@Autowired
	IUserNursingStationMapperDao iUserNursingStationMapperDao;

	@Autowired
	private VerifiedIdentityService verifiedIdentityService;

	@Override
	public Response saveUserNursingStationMapper(UserNursingStationMapperDto userNursingStationMapperDto) throws ApplicationException {
		try
		{
			// BR-121: organisationId/unitId previously came straight from the
			// client -- always use the JWT-verified values instead.
			Integer verifiedOrgId = verifiedIdentityService.getVerifiedOrganizationId();
			Integer verifiedUnitId = verifiedIdentityService.getVerifiedUnitId();
			userNursingStationMapperDto.setOrganisationId(verifiedOrgId);
			userNursingStationMapperDto.setUnitId(verifiedUnitId);

			Response res = iUserNursingStationMapperDao.checkUserNursingStationMapperAvailable(userNursingStationMapperDto);
			if(res.getListObject() != null && res.getListObject().size() > 0){

				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);

			}else{
			Iterator ite=userNursingStationMapperDto.getSelectUserNursingStationMapperList().iterator();
			while (ite.hasNext()) {
				UserNursingStationMapperDto dto = new UserNursingStationMapperDto();
				Integer nursingStationId = (Integer) ite.next();
				dto.setCreatedDate(userNursingStationMapperDto.getCreatedDate());
				dto.setUserId(userNursingStationMapperDto.getUserId());
				dto.setNursingStationId(nursingStationId);
				dto.setStatus(userNursingStationMapperDto.getStatus());
				dto.setOrganisationId(verifiedOrgId);
				dto.setUnitId(verifiedUnitId);
				dto.setCreatedBy(userNursingStationMapperDto.getCreatedBy());
				dto.setUpdatedBy(userNursingStationMapperDto.getUpdatedBy());
				dto.setUpdatedDate(userNursingStationMapperDto.getUpdatedDate());
				dto.setIsDeleted(userNursingStationMapperDto.getIsDeleted());

				iUserNursingStationMapperDao.saveUserNursingStationMapper(dto);
			}
			}
			
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (Exception e)
		{
			logger.error("saveUserNursingStationMapper failed", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}

	@Override
	public Response updateUserNursingStationMapper(UserNursingStationMapperDto userNursingStationMapperDto) throws ApplicationException {
		try
		{
			// BR-121: same override as save -- always use verified identity.
			Integer verifiedOrgId = verifiedIdentityService.getVerifiedOrganizationId();
			Integer verifiedUnitId = verifiedIdentityService.getVerifiedUnitId();
			userNursingStationMapperDto.setOrganisationId(verifiedOrgId);
			userNursingStationMapperDto.setUnitId(verifiedUnitId);

			List<UserNursingStationMapperDto> listUserNursingStationMapperDto = new ArrayList();
			Iterator ite=userNursingStationMapperDto.getSelectUserNursingStationMapperList().iterator();
			while (ite.hasNext()) {
				UserNursingStationMapperDto dto=new UserNursingStationMapperDto();

				Integer nursingStationId = (Integer) ite.next();
				dto.setUpdatedDate(userNursingStationMapperDto.getUpdatedDate());
				dto.setUserId(userNursingStationMapperDto.getUserId());
				dto.setCreatedBy(userNursingStationMapperDto.getCreatedBy());
				dto.setOrganisationId(verifiedOrgId);
				dto.setUnitId(verifiedUnitId);
				dto.setCreatedDate(userNursingStationMapperDto.getCreatedDate());
				dto.setStatus(userNursingStationMapperDto.getStatus());
				dto.setIsDeleted(userNursingStationMapperDto.getIsDeleted());
				dto.setNursingStationId(nursingStationId);
				dto.setUpdatedBy(userNursingStationMapperDto.getUpdatedBy());
				listUserNursingStationMapperDto.add(dto);
			}
//			if(!listUserNursingStationMapperDto.isEmpty())
//			{
				return iUserNursingStationMapperDao.updateUserNursingStationMapper(listUserNursingStationMapperDto,userNursingStationMapperDto);
//			}
//			else {
//				return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
//			}
			} catch (Exception e)
		{
		logger.error("updateUserNursingStationMapper failed", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}
	@Override
	public Response updateUserNursingStationMapperStatus(Integer userId, Integer organisationId, Integer unitId,
			Character status) throws ApplicationException {
		try
		{
			// BR-121: always use verified identity, ignore client-supplied org/unit.
			return iUserNursingStationMapperDao.updateUserNursingStationMapperStatus(userId,
					verifiedIdentityService.getVerifiedOrganizationId(), verifiedIdentityService.getVerifiedUnitId(), status);
		} catch (Exception e)
		{
		logger.error("updateUserNursingStationMapperStatus failed", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}


	@Override
	public Response getUserNursingStationMapperlist(UserNursingStationMapperDto userNursingStationMapperDto) throws ApplicationException {
		try
		{
			// BR-121: always use verified identity, ignore client-supplied org/unit.
			userNursingStationMapperDto.setOrganisationId(verifiedIdentityService.getVerifiedOrganizationId());
			userNursingStationMapperDto.setUnitId(verifiedIdentityService.getVerifiedUnitId());
			return iUserNursingStationMapperDao.getUserNursingStationMapperlist(userNursingStationMapperDto);
		} catch (Exception e)
		{
		logger.error("getUserNursingStationMapperlist failed", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}


	@Override
	public Response getUserNursingStationMapperCount(Integer organisationId, Integer unitId) throws ApplicationException {
		try
		{
			// BR-121: always use verified identity, ignore client-supplied org/unit.
			return iUserNursingStationMapperDao.getUserNursingStationMapperCount(
					verifiedIdentityService.getVerifiedOrganizationId(), verifiedIdentityService.getVerifiedUnitId());
		} catch (Exception e)
		{
		logger.error("getUserNursingStationMapperCount failed", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}


	@Override
	public Response getActiveUserNursingStationMapper(Integer organisationId, Integer unitId) throws ApplicationException {
		try
		{
			// BR-121: always use verified identity, ignore client-supplied org/unit.
			return iUserNursingStationMapperDao.getActiveUserNursingStationMapper(
					verifiedIdentityService.getVerifiedOrganizationId(), verifiedIdentityService.getVerifiedUnitId());
		} catch (Exception e)
		{
		logger.error("getActiveUserNursingStationMapper failed", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}


	@Override
	public Response getUserNursingStationMapperById(Integer userId, Integer organisationId, Integer unitId)
			throws ApplicationException {
		try
		{
			// BR-121: always use verified identity, ignore client-supplied org/unit.
			return iUserNursingStationMapperDao.getUserNursingStationMapperById(userId,
					verifiedIdentityService.getVerifiedOrganizationId(), verifiedIdentityService.getVerifiedUnitId());
		} catch (Exception e)
		{
		logger.error("getUserNursingStationMapperById failed", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}


	@Override
	public Response checkUserNursingStationMapperAvailable(UserNursingStationMapperDto userNursingStationMapperDto)
			throws ApplicationException {
		try
		{
			return iUserNursingStationMapperDao.checkUserNursingStationMapperAvailable(userNursingStationMapperDto);
		} catch (Exception e)
		{
		logger.error("checkUserNursingStationMapperAvailable failed", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}

	@Override
	public Response getUserList(Integer organisationId, Integer unitId) throws ApplicationException {
		try
		{
			// BR-121: always use verified identity, ignore client-supplied org/unit.
			return iUserNursingStationMapperDao.getUserList(
					verifiedIdentityService.getVerifiedOrganizationId(), verifiedIdentityService.getVerifiedUnitId());
		} catch (Exception e)
		{
		logger.error("getUserList failed", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}

	@Override
	public Response getDetailedNursingStationList(Integer userId, Integer organisationId, Integer unitId)
			throws ApplicationException {
		try
		{
			// BR-121: always use verified identity, ignore client-supplied org/unit.
			return iUserNursingStationMapperDao.getDetailedNursingStationList(userId,
					verifiedIdentityService.getVerifiedOrganizationId(), verifiedIdentityService.getVerifiedUnitId());
		} catch (Exception e)
		{
		logger.error("getDetailedNursingStationList failed", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}

	@Override
	public Response getNursingStationList(Integer organisationId, Integer unitId) throws ApplicationException {
		try
		{
			// BR-121: always use verified identity, ignore client-supplied org/unit.
			return iUserNursingStationMapperDao.getNursingStationList(
					verifiedIdentityService.getVerifiedOrganizationId(), verifiedIdentityService.getVerifiedUnitId());
		} catch (Exception e)
		{
		logger.error("getNursingStationList failed", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}

	@Override
	public Response getUserListForStore(Integer organisationId, Integer unitId) throws ApplicationException {
		try
		{
			// BR-121: always use verified identity, ignore client-supplied org/unit.
			return iUserNursingStationMapperDao.getUserListForStore(
					verifiedIdentityService.getVerifiedOrganizationId(), verifiedIdentityService.getVerifiedUnitId());
		} catch (Exception e)
		{
		logger.error("getUserListForStore failed", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}

}

