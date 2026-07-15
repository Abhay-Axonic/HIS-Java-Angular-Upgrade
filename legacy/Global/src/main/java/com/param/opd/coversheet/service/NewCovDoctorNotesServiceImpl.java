package com.param.opd.coversheet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.IError;
import com.param.global.common.Response;
import com.param.opd.coversheet.dao.INewCovDoctorNotesDao;
import com.param.opd.coversheet.dto.NewCovDoctorNotesDto;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class NewCovDoctorNotesServiceImpl implements INewCovDoctorNotesService, ICommonConstants, IError {
	final static Logger logger = Logger.getLogger(NewCovDoctorNotesServiceImpl.class);

	 @Autowired
	 INewCovDoctorNotesDao iNewCovDoctorNotesDao;
		@Override
		public Response saveNewCovDoctorNotes(NewCovDoctorNotesDto newCovDoctorNotesDto) throws ApplicationException {
			try{
				return iNewCovDoctorNotesDao.saveNewCovDoctorNotes(newCovDoctorNotesDto);
			}catch(Exception e){
			logger.error("saveNewCovDoctorNotes failed", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
			}
		}

		@Override
		public Response updateNewCovDoctorNotes(NewCovDoctorNotesDto newCovDoctorNotesDto) throws ApplicationException {
			try{
				return iNewCovDoctorNotesDao.updateNewCovDoctorNotes(newCovDoctorNotesDto);
			}catch(Exception e){
			logger.error("updateNewCovDoctorNotes failed", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
			}
		}


		@Override
		public Response updateNewCovDoctorNotesStatus(NewCovDoctorNotesDto newCovDoctorNotesDto) throws ApplicationException {
			try{
				return iNewCovDoctorNotesDao.updateNewCovDoctorNotesStatus(newCovDoctorNotesDto);
			}catch(Exception e){
			logger.error("updateNewCovDoctorNotesStatus failed", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
			}
		}


		@Override
		public Response getNewCovDoctorNotesList(NewCovDoctorNotesDto newCovDoctorNotesDto) throws ApplicationException {
			try{
				return iNewCovDoctorNotesDao.getNewCovDoctorNotesList(newCovDoctorNotesDto);
			}catch(Exception e){
			logger.error("getNewCovDoctorNotesList failed", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
			}
		}

		@Override
		public Response getNewCovDoctorNotesById(Integer newCovDoctorNotesId, Integer organizationId, Integer unitId)
				throws ApplicationException {
			try{
				return iNewCovDoctorNotesDao.getNewCovDoctorNotesById(newCovDoctorNotesId, organizationId, unitId);
			}catch(Exception e){
			logger.error("getNewCovDoctorNotesById failed", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
			}
		}

		@Override
		public Response getNewCovDoctorNotesCount(NewCovDoctorNotesDto newCovDoctorNotesDto) throws ApplicationException {
			try{
				return iNewCovDoctorNotesDao.getNewCovDoctorNotesCount(newCovDoctorNotesDto);
			}catch(Exception e){
			logger.error("getNewCovDoctorNotesCount failed", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
			}
		}


	}
