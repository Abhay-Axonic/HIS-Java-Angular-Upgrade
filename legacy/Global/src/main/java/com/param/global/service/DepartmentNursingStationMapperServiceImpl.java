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
	@Override
	public Response saveDepartmentNursingStationMapper(DepartmentNursingStationMapperDto departmentNursingStationMapperDto) throws ApplicationException {
		try
		{
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
				dto.setOrganisationId(departmentNursingStationMapperDto.getOrganisationId());
				dto.setUnitId(departmentNursingStationMapperDto.getUnitId());
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
			List<DepartmentNursingStationMapperDto> listDepartmentNursingStationMapperDto = new ArrayList();
			Iterator ite=departmentNursingStationMapperDto.getSelectdepartmentNursingStationMapperList().iterator(); 
			while (ite.hasNext()) {
				DepartmentNursingStationMapperDto dto=new DepartmentNursingStationMapperDto();

				Integer nursingStationId = (Integer) ite.next();
				dto.setUpdatedDate(departmentNursingStationMapperDto.getUpdatedDate());
				dto.setSpecialityId(departmentNursingStationMapperDto.getSpecialityId());
				dto.setCreatedBy(departmentNursingStationMapperDto.getCreatedBy());
				dto.setOrganisationId(departmentNursingStationMapperDto.getOrganisationId());
				dto.setUnitId(departmentNursingStationMapperDto.getUnitId());
				dto.setCreatedDate(departmentNursingStationMapperDto.getCreatedDate());
				dto.setStatus(departmentNursingStationMapperDto.getStatus());
				dto.setIsDeleted(departmentNursingStationMapperDto.getIsDeleted());
				dto.setNursingStationId(nursingStationId);
				dto.setOrganisationId(departmentNursingStationMapperDto.getOrganisationId());
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
			return iDepartmentNursingStationMapperDao.updateDepartmentNursingStationMapperStatus(specialityId, organisationId, unitId, status);
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
			return iDepartmentNursingStationMapperDao.getDepartmentNursingStationMapperCount(organisationId,unitId);
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
			return iDepartmentNursingStationMapperDao.getActiveDepartmentNursingStationMapper(organisationId, unitId);
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
			return iDepartmentNursingStationMapperDao.getDepartmentNursingStationMapperById(specialityId, organisationId, unitId);
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
			return iDepartmentNursingStationMapperDao.getDetailedListNursingStation(specialityId, organisationId, unitId);
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
			return iDepartmentNursingStationMapperDao.getDeptListByNursingStationId(nursingStationId, organisationId, unitId);
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
			return iDepartmentNursingStationMapperDao.getWardListForER(organisationId, unitId);
		} catch (Exception e)
		{
		logger.error("getWardListForER failed", e);
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}
}

