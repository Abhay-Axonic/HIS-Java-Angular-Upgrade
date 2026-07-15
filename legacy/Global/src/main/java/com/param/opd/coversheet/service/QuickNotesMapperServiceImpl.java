package com.param.opd.coversheet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.IError;
import com.param.global.common.Response;
import com.param.opd.coversheet.dao.IQuickNotesMapperDao;
import com.param.opd.coversheet.dto.QuickNotesMapperDto;

import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class QuickNotesMapperServiceImpl implements IQuickNotesMapperService, ICommonConstants, IError {
	final static Logger logger = Logger.getLogger(QuickNotesMapperServiceImpl.class);

	 @Autowired
	 IQuickNotesMapperDao iQuickNotesMapperDao;
		@Override
		public Response saveQuickNotesMapper(QuickNotesMapperDto quickNotesMapperDto) throws ApplicationException {
			try{
				return iQuickNotesMapperDao.saveQuickNotesMapper(quickNotesMapperDto);
			}catch(Exception e){
			logger.error("saveQuickNotesMapper failed", e);
			}
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}

		

		@Override
		public Response getQuickNotesMapperList(QuickNotesMapperDto quickNotesMapperDto) throws ApplicationException {
			try{
				return iQuickNotesMapperDao.getQuickNotesMapperList(quickNotesMapperDto);
			}catch(Exception e){
			logger.error("getQuickNotesMapperList failed", e);
			}
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}

		

}
