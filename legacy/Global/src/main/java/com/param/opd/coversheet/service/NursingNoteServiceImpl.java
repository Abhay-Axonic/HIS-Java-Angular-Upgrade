package com.param.opd.coversheet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.IError;
import com.param.global.common.Response;
import com.param.opd.coversheet.dao.INursingNoteDao;
import com.param.opd.coversheet.dto.NursingNoteDto;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class NursingNoteServiceImpl implements INursingNoteService, ICommonConstants, IError{
	final static Logger logger = Logger.getLogger(NursingNoteServiceImpl.class);

	@Autowired
	INursingNoteDao iNursingNoteDao;
		@Override
		public Response saveNursingNote(NursingNoteDto dto) throws ApplicationException {
			try{
				return iNursingNoteDao.saveNursingNote(dto);
			}catch(Exception e){
			logger.error("saveNursingNote failed", e);
			}
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
		@Override
		public Response getNursingNoteList(NursingNoteDto dto) throws ApplicationException {
			try{
				return iNursingNoteDao.getNursingNoteList(dto);
			}catch(Exception e){
			logger.error("getNursingNoteList failed", e);
			}
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}

}
