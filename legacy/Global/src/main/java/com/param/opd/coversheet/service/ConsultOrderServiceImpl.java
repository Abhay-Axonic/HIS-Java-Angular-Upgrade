package com.param.opd.coversheet.service;

import java.util.List;
import java.util.ListIterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.opd.coversheet.dao.IConsultDoctorDao;
import com.param.opd.coversheet.dto.ConsultDoctorDto;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
/*
 * 
 * Created by Tushar
 */


@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class ConsultOrderServiceImpl implements IConsultDoctorService,ICommonConstants{
	final static Logger logger = Logger.getLogger(ConsultOrderServiceImpl.class);

	
	@Autowired
	private IConsultDoctorDao iConsultDoctorDao;

	@Override
	public Response saveConsultDoctor(List<ConsultDoctorDto> consultDoctorDtoList) throws ApplicationException {
		try{
			ListIterator<ConsultDoctorDto> iterator = consultDoctorDtoList.listIterator();
			while(iterator.hasNext())
			{
				ConsultDoctorDto consultDoctorDto = iterator.next();
				iConsultDoctorDao.saveConsultDoctor(consultDoctorDto);
			}
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		}catch(Exception e){
		logger.error("saveConsultDoctor failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response consultDoctorListByEncounter(Integer encounterId) throws ApplicationException {
		try {
			return iConsultDoctorDao.consultDoctorListByEncounter(encounterId);

		} catch (Exception e) {
		logger.error("consultDoctorListByEncounter failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response inactiveConsultDoctor(ConsultDoctorDto consultDoctorDto) throws ApplicationException {
		try {
			return iConsultDoctorDao.inactiveConsultDoctor(consultDoctorDto);

		} catch (Exception e) {
		logger.error("inactiveConsultDoctor failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getConsultationPatientListForDocDesk(ConsultDoctorDto consultDoctorDto) throws ApplicationException {
		try {
			return iConsultDoctorDao.getConsultationPatientListForDocDesk(consultDoctorDto);
		} catch (Exception e) {
		logger.error("getConsultationPatientListForDocDesk failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response consultDoctorListByAdmission(ConsultDoctorDto consultDoctorDto) throws ApplicationException {
		try {
			return iConsultDoctorDao.consultDoctorListByAdmission(consultDoctorDto);
		} catch (Exception e) {
		logger.error("consultDoctorListByAdmission failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateReferredStatus(ConsultDoctorDto consultDoctorDto) throws ApplicationException {
		try {
			
			return iConsultDoctorDao.updateActionMasterStatus(consultDoctorDto);
			
		} catch (Exception e) {
		logger.error("updateReferredStatus failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response consultDoctorListForBookView(ConsultDoctorDto consultDoctorDto) throws ApplicationException {
		try {
			return iConsultDoctorDao.consultDoctorListForBookView(consultDoctorDto);
		} catch (Exception e) {
		logger.error("consultDoctorListForBookView failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response updateConsultDoctorNotes(ConsultDoctorDto consultDoctorDto) throws ApplicationException {
		try {
			return iConsultDoctorDao.updateConsultDoctorNotes(consultDoctorDto);
		} catch (Exception e) {
		logger.error("updateConsultDoctorNotes failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	
}
