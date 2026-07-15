package com.param.global.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.IError;
import com.param.global.common.Response;
import com.param.global.dao.IDoctorIPConsultationServiceMapperDao;
import com.param.global.dto.DoctorIPConsultationServiceMapperDto;

import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({"rawtypes", "unchecked"})
@Service
public class DoctorIPConsultationServiceMapperServiceImpl implements IDoctorIPConsultationServiceMapperService, ICommonConstants, IError{
	final static Logger logger = Logger.getLogger(DoctorIPConsultationServiceMapperServiceImpl.class);

	@Autowired
	IDoctorIPConsultationServiceMapperDao iDoctorIPConsultationServiceMapperDao;
	
	@Override
	public Response saveDoctorIPConsultationServiceMapper(
			DoctorIPConsultationServiceMapperDto doctorIPConsultationServiceMapperDto) throws ApplicationException {
		try {
			Integer id = iDoctorIPConsultationServiceMapperDao
					.checkDoctorIPConsultationServiceMapperAvailable(doctorIPConsultationServiceMapperDto);
			if (id != null) {
				// if IP consultation for doctor is already exist then
				iDoctorIPConsultationServiceMapperDao.getinactivePreviousEntry(id);
				// adding new entry after in-activating previous
				iDoctorIPConsultationServiceMapperDao
						.saveDoctorIPConsultationServiceMapper(doctorIPConsultationServiceMapperDto);
				return new Response(SUCCESS, null, "Inactivated Previous And Added New...!!", null, null);
			} else {

				return iDoctorIPConsultationServiceMapperDao
						.saveDoctorIPConsultationServiceMapper(doctorIPConsultationServiceMapperDto);
			}
		} catch (Exception e) {
		logger.error("saveDoctorIPConsultationServiceMapper failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateDoctorIPConsultationServiceMapper(
			DoctorIPConsultationServiceMapperDto doctorIPConsultationServiceMapperDto) throws ApplicationException {
		try
		{
			return iDoctorIPConsultationServiceMapperDao.updateDoctorIPConsultationServiceMapper(doctorIPConsultationServiceMapperDto);
		} catch (Exception e)
		{
		logger.error("updateDoctorIPConsultationServiceMapper failed", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}
	
	@Override
	public Response getDoctorIPConsultationServiceMapperList(
			DoctorIPConsultationServiceMapperDto doctorIPConsultationServiceMapperDto) throws ApplicationException {
		try
		{
			return iDoctorIPConsultationServiceMapperDao.getDoctorIPConsultationServiceMapperList(doctorIPConsultationServiceMapperDto);
		} catch (Exception e)
		{
		logger.error("getDoctorIPConsultationServiceMapperList failed", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}

	@Override
	public Response getDoctorIPConsultationServiceMapperById(Integer doctorIPConsultationServiceMapperId,
			Integer organizationId, Integer unitId) throws ApplicationException {
		try
		{
			return iDoctorIPConsultationServiceMapperDao.getDoctorIPConsultationServiceMapperById(doctorIPConsultationServiceMapperId, organizationId, unitId);
		} catch (Exception e)
		{
		logger.error("getDoctorIPConsultationServiceMapperById failed", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}
	
	@Override
	public Response getDoctorIPConsultationByDoctorId(Integer doctorId,
			Integer organizationId, Integer unitId) throws ApplicationException {
		try
		{
			return iDoctorIPConsultationServiceMapperDao.getDoctorIPConsultationByDoctorId(doctorId, organizationId, unitId);
		} catch (Exception e)
		{
		logger.error("getDoctorIPConsultationByDoctorId failed", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}

	@Override
	public Response getDoctorIPConsultationServiceMapperListCount(
			DoctorIPConsultationServiceMapperDto doctorIPConsultationServiceMapperDto) throws ApplicationException {
		try
		{
			return iDoctorIPConsultationServiceMapperDao.getDoctorIPConsultationServiceMapperListCount(doctorIPConsultationServiceMapperDto);
		} catch (Exception e)
		{
		logger.error("getDoctorIPConsultationServiceMapperListCount failed", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}

		
	
}
