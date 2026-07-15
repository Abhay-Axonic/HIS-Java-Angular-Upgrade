package com.param.opd.coversheet.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.param.exception.ApplicationException;

import com.param.global.common.Response;
import com.param.opd.coversheet.dao.IComplaintAppointmentMapperDao;
import com.param.opd.coversheet.dto.ComplaintAppointmentMapperDto;
import com.param.opd.coversheet.model.ComplaintAppointmentMapper;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings("rawtypes")
public class ComplaintAppointmentMapperServiceImpl implements IComplaintAppointmentMapperService {
	final static Logger logger = Logger.getLogger(ComplaintAppointmentMapperServiceImpl.class);


	private static final String COMMON_ERROR_MESSAGE = null;
	private static final String ERROR_CODE = null;
	private static final String ERROR = null;
	@Autowired
	private IComplaintAppointmentMapperDao iComplaintAppointmentMapperDao;
	
	@Override
	@Transactional
	public Response saveComplaintAppointmentMapper(ComplaintAppointmentMapperDto complaintAppointmentMapperDto)throws ApplicationException {
		// TODO Auto-generated method stub
		return iComplaintAppointmentMapperDao.saveComplaintAppointmentMapper(complaintAppointmentMapperDto);
	}

	@Override
	@Transactional
	public Response getListOfComplaintAppointmentMapper(ComplaintAppointmentMapperDto complaintAppointmentMapperDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iComplaintAppointmentMapperDao.getListOfComplaintAppointmentMapper(complaintAppointmentMapperDto);
	}

	@Override
	@Transactional
	public Response editListOfComplaintAppointmentMapper(ComplaintAppointmentMapperDto complaintAppointmentMapperDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iComplaintAppointmentMapperDao.editListOfComplaintAppointmentMapper(complaintAppointmentMapperDto);
	}

	@Override
	@Transactional
	public Response updateComplaintAppointmentMapper(
			ComplaintAppointmentMapperDto complaintAppointmentMapperDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iComplaintAppointmentMapperDao.updateComplaintAppointmentMapper(complaintAppointmentMapperDto);
	}

	@Override
	@Transactional
	public Response updateSaveNurseReviewedFlag(
			ComplaintAppointmentMapper complaintAppointmentMapper)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iComplaintAppointmentMapperDao.updateSaveNurseReviewedFlag(complaintAppointmentMapper);
	}

	@Override
	@Transactional
	public Response updateComplaintAppointmentStatusToRemoveComplaint(
			ComplaintAppointmentMapperDto complaintAppointmentMapperDto) throws ApplicationException {
		try {
			
			return iComplaintAppointmentMapperDao.updateComplaintAppointmentStatusToRemoveComplaint(complaintAppointmentMapperDto);
	
	} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	
	}

	@Transactional
	@Override
	public Response getComplaintAppointmentByPatientIdForBookView(
			ComplaintAppointmentMapperDto complaintAppointmentMapperDto) throws ApplicationException {
		
		return iComplaintAppointmentMapperDao.getComplaintAppointmentByPatientIdForBookView(complaintAppointmentMapperDto);
		}

	@Transactional
	@Override
	public Response getComplaintAppointmentByPatientId(ComplaintAppointmentMapperDto complaintAppointmentMapperDto)
			throws ApplicationException {
		try {
			
			return iComplaintAppointmentMapperDao.getComplaintAppointmentByPatientId(complaintAppointmentMapperDto);
	
	} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	
	}

	@Transactional
	@Override
	public Response getPastComplaintAppointmentByPatientId(ComplaintAppointmentMapperDto complaintAppointmentMapperDto)
			throws ApplicationException {
    try {
			
			return iComplaintAppointmentMapperDao.getPastComplaintAppointmentByPatientId(complaintAppointmentMapperDto);
	
	} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	
	}

	@Transactional
	@Override
	public Response saveResolvedComplaintAppointment(ComplaintAppointmentMapperDto complaintAppointmentMapperDto)
			throws ApplicationException {
    try {
			
			return iComplaintAppointmentMapperDao.saveResolvedComplaintAppointment(complaintAppointmentMapperDto);
	
	} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	
	}

	@Override
	public Response getComplaintsByPatientIdForBookView(ComplaintAppointmentMapperDto complaintAppointmentMapperDto)
			throws ApplicationException {
		try {
			
			return iComplaintAppointmentMapperDao.getComplaintsByPatientIdForBookView(complaintAppointmentMapperDto);
	
	} catch (Exception e) {
	logger.error("getComplaintsByPatientIdForBookView failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getPastComplaintAppointmentForBookView(ComplaintAppointmentMapperDto complaintAppointmentMapperDto)
			throws ApplicationException {
		try {
			
			return iComplaintAppointmentMapperDao.getPastComplaintAppointmentForBookView(complaintAppointmentMapperDto);
	
	} catch (Exception e) {
	logger.error("getPastComplaintAppointmentForBookView failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getMostSavedComplaint(ComplaintAppointmentMapperDto complaintAppointmentMapperDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iComplaintAppointmentMapperDao.getMostSavedComplaint(complaintAppointmentMapperDto);
	}

	@Override
	public Response searchFreeTextComplaint(ComplaintAppointmentMapperDto complaintAppointmentMapperDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iComplaintAppointmentMapperDao.searchFreeTextComplaint(complaintAppointmentMapperDto);
	}


}
