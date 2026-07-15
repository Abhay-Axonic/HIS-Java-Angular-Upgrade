package com.param.opd.coversheet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.IError;
import com.param.global.common.Response;
import com.param.opd.coversheet.dao.INewCovNursingNotesDao;
import com.param.opd.coversheet.dto.NewCovNursingNotesDto;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class NewCovNursingNotesServiceImpl implements INewCovNursingNotesService, ICommonConstants, IError {
	final static Logger logger = Logger.getLogger(NewCovNursingNotesServiceImpl.class);

	 @Autowired
	 INewCovNursingNotesDao iNewCovNursingNotesDao;
		@Override
		public Response saveNewCovNursingNotes(NewCovNursingNotesDto newCovNursingNotesDto) throws ApplicationException {
			try{
				return iNewCovNursingNotesDao.saveNewCovNursingNotes(newCovNursingNotesDto);
			}catch(Exception e){
			logger.error("saveNewCovNursingNotes failed", e);
			}
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}

		@Override
		public Response updateNewCovNursingNotes(NewCovNursingNotesDto newCovNursingNotesDto) throws ApplicationException {
			try{
				return iNewCovNursingNotesDao.updateNewCovNursingNotes(newCovNursingNotesDto);
			}catch(Exception e){
			logger.error("updateNewCovNursingNotes failed", e);
			}
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}


		@Override
		public Response updateNewCovNursingNotesStatus(NewCovNursingNotesDto newCovNursingNotesDto) throws ApplicationException {
			try{
				return iNewCovNursingNotesDao.updateNewCovNursingNotesStatus(newCovNursingNotesDto);
			}catch(Exception e){
			logger.error("updateNewCovNursingNotesStatus failed", e);
			}
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}


		@Override
		public Response getNewCovNursingNotesList(NewCovNursingNotesDto newCovNursingNotesDto) throws ApplicationException {
			try{
				return iNewCovNursingNotesDao.getNewCovNursingNotesList(newCovNursingNotesDto);
			}catch(Exception e){
			logger.error("getNewCovNursingNotesList failed", e);
			}
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}

		@Override
		public Response getNewCovNursingNotesById(Integer newCovNursingNotesId, Integer organizationId, Integer unitId)
				throws ApplicationException {
			try{
				return iNewCovNursingNotesDao.getNewCovNursingNotesById(newCovNursingNotesId, organizationId, unitId);
			}catch(Exception e){
			logger.error("getNewCovNursingNotesById failed", e);
			}
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}

		@Override
		public Response getNewCovNursingNotesCount(NewCovNursingNotesDto newCovNursingNotesDto) throws ApplicationException {
			try{
				return iNewCovNursingNotesDao.getNewCovNursingNotesCount(newCovNursingNotesDto);
			}catch(Exception e){
			logger.error("getNewCovNursingNotesCount failed", e);
			}
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}


	}
