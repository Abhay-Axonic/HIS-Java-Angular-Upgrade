package com.param.opd.coversheet.service;

import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.IError;
import com.param.global.common.Response;
import com.param.opd.coversheet.dao.IObservationDao;
import com.param.opd.coversheet.dto.ObservationDto;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class OservationServiceImpl implements IObservationService, ICommonConstants, IError {
	final static Logger logger = Logger.getLogger(OservationServiceImpl.class);

 @Autowired
 IObservationDao iObservationDao;
	@Override
	public Response saveObservation(ObservationDto observationDto) throws ApplicationException {
		try{
			return iObservationDao.saveObservation(observationDto);
		}catch(Exception e){
		logger.error("saveObservation failed", e);
		}
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Response updateObservation(ObservationDto observationDto) throws ApplicationException {
		try{
			return iObservationDao.updateObservation(observationDto);
		}catch(Exception e){
		logger.error("updateObservation failed", e);
		}
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}


	@Override
	public Response updateObservationStatus(ObservationDto observationDto) throws ApplicationException {
		try{
			return iObservationDao.updateObservationStatus(observationDto);
		}catch(Exception e){
		logger.error("updateObservationStatus failed", e);
		}
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}


	@Override
	public Response getObservationList(ObservationDto observationDto) throws ApplicationException {
		try{
			return iObservationDao.getObservationList(observationDto);
		}catch(Exception e){
		logger.error("getObservationList failed", e);
		}
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Response getObservationById(Integer observationId, Integer organizationId, Integer unitId)
			throws ApplicationException {
		try{
			return iObservationDao.getObservationById(observationId, organizationId, unitId);
		}catch(Exception e){
		logger.error("getObservationById failed", e);
		}
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Response getObservationCount(ObservationDto observationDto) throws ApplicationException {
		try{
			return iObservationDao.getObservationCount(observationDto);
		}catch(Exception e){
		logger.error("getObservationCount failed", e);
		}
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}


}
