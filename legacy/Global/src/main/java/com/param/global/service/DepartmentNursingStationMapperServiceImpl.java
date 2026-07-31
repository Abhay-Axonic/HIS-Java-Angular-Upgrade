package com.param.global.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.IError;
import com.param.global.common.Response;
import com.param.global.dao.IDepartmentNursingStationMapperDao;
import com.param.global.dto.DepartmentNursingStationMapperDto;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({"rawtypes", "unchecked"})
public class DepartmentNursingStationMapperServiceImpl implements IDepartmentNursingStationMapperService, ICommonConstants, IError{
	final static Logger logger = Logger.getLogger(DepartmentNursingStationMapperServiceImpl.class);


	@Autowired
	IDepartmentNursingStationMapperDao iDepartmentNursingStationMapperDao;

	@Autowired
	private VerifiedIdentityService verifiedIdentityService;

	@Override
	public Response saveDepartmentNursingStationMapper(DepartmentNursingStationMapperDto departmentNursingStationMapperDto) throws ApplicationException {
		try
		{
			// BR-121: organisationId/unitId previously came straight from the
			// client (no SessionService or any identity check anywhere in this
			// screen's call chain) -- always use the JWT-verified values instead.
			Integer verifiedOrgId = verifiedIdentityService.getVerifiedOrganizationId();
			Integer verifiedUnitId = verifiedIdentityService.getVerifiedUnitId();
			departmentNursingStationMapperDto.setOrganisationId(verifiedOrgId);
			departmentNursingStationMapperDto.setUnitId(verifiedUnitId);

			Response res = iDepartmentNursingStationMapperDao.checkDepartmentNursingStationMapperAvailable(departmentNursingStationMapperDto);
			if(res.getListObject() != null && res.getListObject().size() > 0){

				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);

			}else{
			Iterator ite=departmentNursingStationMapperDto.getSelectdepartmentNursingStationMapperList().iterator();
			while (ite.hasNext()) {
				DepartmentNursingStationMapperDto dto = new DepartmentNursingStationMapperDto();
				Integer nursingStationId = (Integer) ite.next();
				dto.setCreatedDate(departmentNursingStationMapperDto.getCreatedDate());
				dto.setSpecialityId(departmentNursingStationMapperDto.getSpecialityId());
				dto.setNursingStationId(nursingStationId);
				dto.setStatus(departmentNursingStationMapperDto.getStatus());
				dto.setOrganisationId(verifiedOrgId);
				dto.setUnitId(verifiedUnitId);
				dto.setCreatedBy(departmentNursingStationMapperDto.getCreatedBy());
				dto.setUpdatedBy(departmentNursingStationMapperDto.getUpdatedBy());
				dto.setUpdatedDate(departmentNursingStationMapperDto.getUpdatedDate());
				dto.setIsDeleted(departmentNursingStationMapperDto.getIsDeleted());

				iDepartmentNursingStationMapperDao.saveDepartmentNursingStationMapper(dto);
			}
			}

			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (Exception e)
		{
		logger.error("saveDepartmentNursingStationMapper failed", e);
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}

	@Override
	public Response updateDepartmentNursingStationMapper(DepartmentNursingStationMapperDto departmentNursingStationMapperDto) throws ApplicationException {
		try
		{
			// BR-121: same override as save -- always use verified identity.
			Integer verifiedOrgId = verifiedIdentityService.getVerifiedOrganizationId();
			Integer verifiedUnitId = verifiedIdentityService.getVerifiedUnitId();

			List<DepartmentNursingStationMapperDto> listDepartmentNursingStationMapperDto = new ArrayList();
			Iterator ite=departmentNursingStationMapperDto.getSelectdepartmentNursingStationMapperList().iterator();
			while (ite.hasNext()) {
				DepartmentNursingStationMapperDto dto=new DepartmentNursingStationMapperDto();

				Integer nursingStationId = (Integer) ite.next();
				dto.setUpdatedDate(departmentNursingStationMapperDto.getUpdatedDate());
				dto.setSpecialityId(departmentNursingStationMapperDto.getSpecialityId());
				dto.setCreatedBy(departmentNursingStationMapperDto.getCreatedBy());
				dto.setOrganisationId(verifiedOrgId);
				dto.setUnitId(verifiedUnitId);
				dto.setCreatedDate(departmentNursingStationMapperDto.getCreatedDate());
				dto.setStatus(departmentNursingStationMapperDto.getStatus());
				dto.setIsDeleted(departmentNursingStationMapperDto.getIsDeleted());
				dto.setNursingStationId(nursingStationId);
				dto.setUpdatedBy(departmentNursingStationMapperDto.getUpdatedBy());
				listDepartmentNursingStationMapperDto.add(dto);
			}
			if(!listDepartmentNursingStationMapperDto.isEmpty())
			{
				return iDepartmentNursingStationMapperDao.updateDepartmentNursingStationMapper(listDepartmentNursingStationMapperDto);
			}
			else {
				return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
			}
			} catch (Exception e)
		{
		logger.error("updateDepartmentNursingStationMapper failed", e);
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}
	@Override
	public Response updateDepartmentNursingStationMapperStatus(Integer specialityId, Integer organisationId, Integer unitId,
			Character status) throws ApplicationException {
		try
		{
			// BR-121: always use verified identity, ignore client-supplied org/unit.
			return iDepartmentNursingStationMapperDao.updateDepartmentNursingStationMapperStatus(specialityId,
					verifiedIdentityService.getVerifiedOrganizationId(), verifiedIdentityService.getVerifiedUnitId(), status);
		} catch (Exception e)
		{
		logger.error("updateDepartmentNursingStationMapperStatus failed", e);
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}


	@Override
	public Response getDepartmentNursingStationMapperlist(DepartmentNursingStationMapperDto departmentNursingStationMapperDto) throws ApplicationException {
		try
		{
			// BR-121: always use verified identity, ignore client-supplied org/unit.
			departmentNursingStationMapperDto.setOrganisationId(verifiedIdentityService.getVerifiedOrganizationId());
			departmentNursingStationMapperDto.setUnitId(verifiedIdentityService.getVerifiedUnitId());
			return iDepartmentNursingStationMapperDao.getDepartmentNursingStationMapperlist(departmentNursingStationMapperDto);
		} catch (Exception e)
		{
		logger.error("getDepartmentNursingStationMapperlist failed", e);
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}


	@Override
	public Response getDepartmentNursingStationMapperCount(Integer organisationId, Integer unitId) throws ApplicationException {
		try
		{
			// BR-121: always use verified identity, ignore client-supplied org/unit.
			return iDepartmentNursingStationMapperDao.getDepartmentNursingStationMapperCount(
					verifiedIdentityService.getVerifiedOrganizationId(), verifiedIdentityService.getVerifiedUnitId());
		} catch (Exception e)
		{
		logger.error("getDepartmentNursingStationMapperCount failed", e);
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}


	@Override
	public Response getActiveDepartmentNursingStationMapper(Integer organisationId, Integer unitId) throws ApplicationException {
		try
		{
			// BR-121: always use verified identity, ignore client-supplied org/unit.
			return iDepartmentNursingStationMapperDao.getActiveDepartmentNursingStationMapper(
					verifiedIdentityService.getVerifiedOrganizationId(), verifiedIdentityService.getVerifiedUnitId());
		} catch (Exception e)
		{
		logger.error("getActiveDepartmentNursingStationMapper failed", e);
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}


	@Override
	public Response getDepartmentNursingStationMapperById(Integer specialityId, Integer organisationId, Integer unitId)
			throws ApplicationException {
		try
		{
			// BR-121: always use verified identity, ignore client-supplied org/unit.
			return iDepartmentNursingStationMapperDao.getDepartmentNursingStationMapperById(specialityId,
					verifiedIdentityService.getVerifiedOrganizationId(), verifiedIdentityService.getVerifiedUnitId());
		} catch (Exception e)
		{
		logger.error("getDepartmentNursingStationMapperById failed", e);
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}


	@Override
	public Response checkDepartmentNursingStationMapperAvailable(DepartmentNursingStationMapperDto departmentNursingStationMapperDto)
			throws ApplicationException {
		try
		{
			return iDepartmentNursingStationMapperDao.checkDepartmentNursingStationMapperAvailable(departmentNursingStationMapperDto);
		} catch (Exception e)
		{
		logger.error("checkDepartmentNursingStationMapperAvailable failed", e);
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}

	@Override
	public Response getDetailedListNursingStation(Integer specialityId, Integer organisationId, Integer unitId)
			throws ApplicationException {
		try
		{
			// BR-121: always use verified identity, ignore client-supplied org/unit.
			return iDepartmentNursingStationMapperDao.getDetailedListNursingStation(specialityId,
					verifiedIdentityService.getVerifiedOrganizationId(), verifiedIdentityService.getVerifiedUnitId());
		} catch (Exception e)
		{
		logger.error("getDetailedListNursingStation failed", e);
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}

	@Override
	public Response getDeptListByNursingStationId(Integer nursingStationId, Integer organisationId, Integer unitId)
			throws ApplicationException {
		try
		{
			// BR-121: always use verified identity, ignore client-supplied org/unit.
			return iDepartmentNursingStationMapperDao.getDeptListByNursingStationId(nursingStationId,
					verifiedIdentityService.getVerifiedOrganizationId(), verifiedIdentityService.getVerifiedUnitId());
		} catch (Exception e)
		{
		logger.error("getDeptListByNursingStationId failed", e);
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}

	@Override
	public Response getWardListForER(Integer organisationId, Integer unitId) throws ApplicationException {
		try
		{
			// BR-121: always use verified identity, ignore client-supplied org/unit.
			return iDepartmentNursingStationMapperDao.getWardListForER(
					verifiedIdentityService.getVerifiedOrganizationId(), verifiedIdentityService.getVerifiedUnitId());
		} catch (Exception e)
		{
		logger.error("getWardListForER failed", e);
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}
}

