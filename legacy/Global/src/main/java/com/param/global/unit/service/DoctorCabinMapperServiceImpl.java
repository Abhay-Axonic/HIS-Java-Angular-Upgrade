package com.param.global.unit.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.IError;
import com.param.global.common.Response;
import com.param.global.unit.dao.IDoctorCabinMapperDao;
import com.param.global.unit.dto.DoctorCabinMapperDto;

import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({"rawtypes", "unchecked"})
public class DoctorCabinMapperServiceImpl implements IDoctorCabinMapperService, ICommonConstants, IError{
	final static Logger logger = Logger.getLogger(DoctorCabinMapperServiceImpl.class);


	@Autowired
	IDoctorCabinMapperDao iDoctorCabinMapperDao;
	@Override
	public Response saveDoctorCabinMapper(DoctorCabinMapperDto doctorCabinMapperDto) throws ApplicationException {
		try
		{
			Response res = iDoctorCabinMapperDao.checkDoctorCabinMapperAvailable(doctorCabinMapperDto);
			if(res.getListObject() != null && res.getListObject().size() > 0){
				
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
				
			}else{
			Iterator ite=doctorCabinMapperDto.getSelectDoctorCabinMapperList().iterator(); 
			while (ite.hasNext()) {
				DoctorCabinMapperDto dto = new DoctorCabinMapperDto();
				Integer cabinId = (Integer) ite.next();
				dto.setCreatedDate(doctorCabinMapperDto.getCreatedDate());
				dto.setDoctorId(doctorCabinMapperDto.getDoctorId());
				dto.setCabinId(cabinId);
				dto.setStatus(doctorCabinMapperDto.getStatus());
				dto.setOrganisationId(doctorCabinMapperDto.getOrganisationId());
				dto.setUnitId(doctorCabinMapperDto.getUnitId());
				dto.setCreatedBy(doctorCabinMapperDto.getCreatedBy());
				dto.setUpdatedBy(doctorCabinMapperDto.getUpdatedBy());
				dto.setUpdatedDate(doctorCabinMapperDto.getUpdatedDate());
				dto.setIsDeleted(doctorCabinMapperDto.getIsDeleted());

				iDoctorCabinMapperDao.saveDoctorCabinMapper(dto);
			}
			}
			
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (Exception e)
		{
		logger.error("saveDoctorCabinMapper failed", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}

	@Override
	public Response updateDoctorCabinMapper(DoctorCabinMapperDto doctorCabinMapperDto) throws ApplicationException {
		try
		{
			List<DoctorCabinMapperDto> listDoctorCabinMapperDto = new ArrayList();
			Iterator ite=doctorCabinMapperDto.getSelectDoctorCabinMapperList().iterator(); 
			while (ite.hasNext()) {
				DoctorCabinMapperDto dto=new DoctorCabinMapperDto();

				Integer cabinId = (Integer) ite.next();
				dto.setUpdatedDate(doctorCabinMapperDto.getUpdatedDate());
				dto.setDoctorId(doctorCabinMapperDto.getDoctorId());
				dto.setCreatedBy(doctorCabinMapperDto.getCreatedBy());
				dto.setOrganisationId(doctorCabinMapperDto.getOrganisationId());
				dto.setUnitId(doctorCabinMapperDto.getUnitId());
				dto.setCreatedDate(doctorCabinMapperDto.getCreatedDate());
				dto.setStatus(doctorCabinMapperDto.getStatus());
				dto.setIsDeleted(doctorCabinMapperDto.getIsDeleted());
				dto.setCabinId(cabinId);
				dto.setOrganisationId(doctorCabinMapperDto.getOrganisationId());
				dto.setUpdatedBy(doctorCabinMapperDto.getUpdatedBy());
				listDoctorCabinMapperDto.add(dto);
			}
			if(!listDoctorCabinMapperDto.isEmpty())
			{
				return iDoctorCabinMapperDao.updateDoctorCabinMapper(listDoctorCabinMapperDto);
			}
			else {
				return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
			}
			} catch (Exception e)
		{
		logger.error("updateDoctorCabinMapper failed", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}
	@Override
	public Response updateDoctorCabinMapperStatus(Integer doctorId, Integer organisationId, Integer unitId,
			Character status) throws ApplicationException {
		try
		{
			return iDoctorCabinMapperDao.updateDoctorCabinMapperStatus(doctorId, organisationId, unitId, status);
		} catch (Exception e)
		{
		logger.error("updateDoctorCabinMapperStatus failed", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}


	@Override
	public Response getDoctorCabinMapperlist(DoctorCabinMapperDto doctorCabinMapperDto) throws ApplicationException {
		try
		{
			return iDoctorCabinMapperDao.getDoctorCabinMapperlist(doctorCabinMapperDto);
		} catch (Exception e)
		{
		logger.error("getDoctorCabinMapperlist failed", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}


	@Override
	public Response getDoctorCabinMapperCount(Integer organisationId, Integer unitId) throws ApplicationException {
		try
		{
			return iDoctorCabinMapperDao.getDoctorCabinMapperCount(organisationId,unitId);
		} catch (Exception e)
		{
		logger.error("getDoctorCabinMapperCount failed", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}


	@Override
	public Response getActiveDoctorCabinMapper(Integer organisationId, Integer unitId) throws ApplicationException {
		try
		{
			return iDoctorCabinMapperDao.getActiveDoctorCabinMapper(organisationId, unitId);
		} catch (Exception e)
		{
		logger.error("getActiveDoctorCabinMapper failed", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}


	@Override
	public Response getDoctorCabinMapperById(Integer doctorId, Integer organisationId, Integer unitId)
			throws ApplicationException {
		try
		{
			return iDoctorCabinMapperDao.getDoctorCabinMapperById(doctorId, organisationId, unitId);
		} catch (Exception e)
		{
		logger.error("getDoctorCabinMapperById failed", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}


	@Override
	public Response checkDoctorCabinMapperAvailable(DoctorCabinMapperDto doctorCabinMapperDto)
			throws ApplicationException {
		try
		{
			return iDoctorCabinMapperDao.checkDoctorCabinMapperAvailable(doctorCabinMapperDto);
		} catch (Exception e)
		{
		logger.error("checkDoctorCabinMapperAvailable failed", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}

	@Override
	public Response getDoctorList(Integer organisationId, Integer unitId) throws ApplicationException {
		try
		{
			return iDoctorCabinMapperDao.getDoctorList(organisationId, unitId);
		} catch (Exception e)
		{
		logger.error("getDoctorList failed", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}

	@Override
	public Response getDetailedCabinList(Integer doctorId, Integer organisationId, Integer unitId)
			throws ApplicationException {
		try
		{
			return iDoctorCabinMapperDao.getDetailedCabinList(doctorId, organisationId, unitId);
		} catch (Exception e)
		{
		logger.error("getDetailedCabinList failed", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}
}
