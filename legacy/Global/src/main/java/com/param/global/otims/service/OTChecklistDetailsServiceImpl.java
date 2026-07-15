package com.param.global.otims.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.otims.dao.IOTChecklistDetailsDao;
import com.param.global.otims.dto.OTChecklistDetailsDto;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({"rawtypes"})
@Service
public class OTChecklistDetailsServiceImpl  implements IOTChecklistDetailsService, ICommonConstants {
	final static Logger logger = Logger.getLogger(OTChecklistDetailsServiceImpl.class);


	
	@Autowired
	IOTChecklistDetailsDao iOTChecklistDetailsDao;

	@Override
	public Response saveOTChecklistDetails(List<OTChecklistDetailsDto> oTChecklistDetailsDto)throws ApplicationException {
		try{
			
			for (OTChecklistDetailsDto otChecklistDetailsDto2 : oTChecklistDetailsDto) {
				 iOTChecklistDetailsDao.saveOTChecklistDetails(otChecklistDetailsDto2);
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		}catch(Exception e){
		logger.error("saveOTChecklistDetails failed", e);
		}
		return new Response<>(ERROR, null, null, null, null);
	}

	@Override
	public Response getIntraOperativeChecklistByProReqId(OTChecklistDetailsDto oTChecklistDetailsDto)
			throws ApplicationException {
		try{
				return iOTChecklistDetailsDao.getIntraOperativeChecklistByProReqId(oTChecklistDetailsDto);
		}catch(Exception e){
		logger.error("getIntraOperativeChecklistByProReqId failed", e);
		}
		return new Response<>(ERROR, null, null, null, null);
	}
}
