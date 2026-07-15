package com.param.global.billing.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.billing.dao.IDoctorGradeDao;
import com.param.global.billing.dto.DoctorGradeDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({"rawtypes"})
public class DoctorGradeServiceImpl implements IDoctorGradeService, ICommonConstants{

	final static Logger logger = Logger.getLogger(DoctorGradeServiceImpl.class);

	@Autowired
	IDoctorGradeDao iDoctorGradeDao;
	
	@Override
	public Response saveDoctorGrade(DoctorGradeDto doctorGradeDto) throws ApplicationException {
		try {
			Response response=iDoctorGradeDao.getDoctorGradeByName(doctorGradeDto);
			
			if(response.getListObject().size()>0 && response.getListObject()!=null){
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				return iDoctorGradeDao.saveDoctorGrade(doctorGradeDto);
			}

		} catch (Exception e) {
			logger.error("saveDoctorGrade failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateDoctorGrade(DoctorGradeDto doctorGradeDto) throws ApplicationException {
		try {
			Response response=iDoctorGradeDao.getDoctorGradeByNameNotId(doctorGradeDto);
			
			if(response.getListObject().size()>0 && response.getListObject()!=null){
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				return iDoctorGradeDao.updateDoctorGrade(doctorGradeDto);
			}

		} catch (Exception e) {
			logger.error("updateDoctorGrade failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response updateDoctorGradeStatus(DoctorGradeDto doctorGradeDto) throws ApplicationException {
		try {	
				return iDoctorGradeDao.updateDoctorGradeStatus(doctorGradeDto);

			} catch (Exception e) {
				logger.error("updateDoctorGradeStatus failed", e);
				throw new ServiceException(e.getMessage());
		}
	}
	
	

	@Override
	public Response getDoctorGradeList(DoctorGradeDto doctorGradeDto) throws ApplicationException {
		try {	
			return iDoctorGradeDao.getDoctorGradeList(doctorGradeDto);

		} catch (Exception e) {
			logger.error("getDoctorGradeList failed", e);
			throw new ServiceException(e.getMessage());
	}
	}
	

	@Override
	public Response getDoctorGradeById(Integer doctorGradeId, Integer orgId, Integer orgUnitId)
			throws ApplicationException {
		try {	
			return iDoctorGradeDao.getDoctorGradeById(doctorGradeId, orgId, orgUnitId);

		} catch (Exception e) {
			logger.error("getDoctorGradeById failed", e);
			throw new ServiceException(e.getMessage());
	}
	}
	
	

	@Override
	public Response getDoctorGradeCount(DoctorGradeDto doctorGradeDto) throws ApplicationException {
		try {	
			return iDoctorGradeDao.getDoctorGradeCount(doctorGradeDto);

		} catch (Exception e) {
			logger.error("getDoctorGradeCount failed", e);
			throw new ServiceException(e.getMessage());
	}
	}
	
	
	@Override
	public Response getActiveDoctorGradeList(DoctorGradeDto doctorGradeDto) throws ApplicationException {
		try {	
			return iDoctorGradeDao.getActiveDoctorGradeList(doctorGradeDto);

		} catch (Exception e) {
			logger.error("getActiveDoctorGradeList failed", e);
			throw new ServiceException(e.getMessage());
	}
	}
	
	@Override
	public Response getDoctorGradeByName(DoctorGradeDto doctorGradeDto) throws ApplicationException {
		try {	
			return iDoctorGradeDao.getDoctorGradeByName(doctorGradeDto);

		} catch (Exception e) {
			logger.error("getDoctorGradeByName failed", e);
			throw new ServiceException(e.getMessage());
	}
	}

	@Override
	public Response getDoctorGradeByNameNotId(DoctorGradeDto doctorGradeDto) throws ApplicationException {
		try {	
			return iDoctorGradeDao.getDoctorGradeByNameNotId(doctorGradeDto);

		} catch (Exception e) {
			logger.error("getDoctorGradeByNameNotId failed", e);
			throw new ServiceException(e.getMessage());
	}
	}
}
