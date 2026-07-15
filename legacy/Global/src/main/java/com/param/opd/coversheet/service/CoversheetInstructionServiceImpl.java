package com.param.opd.coversheet.service;

import java.util.ListIterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.opd.coversheet.dao.ICoversheetInstructionDao;
import com.param.opd.coversheet.dto.CoversheetInstructionDto;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class CoversheetInstructionServiceImpl implements ICoversheetInstructionService,ICommonConstants{
	final static Logger logger = Logger.getLogger(CoversheetInstructionServiceImpl.class);


	@Autowired
	ICoversheetInstructionDao iCoversheetInstructionDao;
	
	
	@Override
	public Response saveCoversheetInstruction(CoversheetInstructionDto coversheetInstructionDto)
			throws ApplicationException {
		try{
			if(coversheetInstructionDto.getCoversheetInstructionDtoslist().size() > 0) {
			ListIterator<CoversheetInstructionDto> iterator = coversheetInstructionDto.getCoversheetInstructionDtoslist().listIterator();
			while(iterator.hasNext())
			{
				CoversheetInstructionDto instructionDto = iterator.next();
				instructionDto.setIsOtims(false);
				iCoversheetInstructionDao.saveCoversheetInstruction(instructionDto);
			}
			}else {
			iCoversheetInstructionDao.saveCoversheetInstruction(coversheetInstructionDto);
			}
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		}catch(Exception e){
		logger.error("saveCoversheetInstruction failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	
	@Override
	public Response coversheetInstructionList(Integer visitId,int visitTypeId) throws ApplicationException {
		try{
			return iCoversheetInstructionDao.coversheetInstructionList(visitId,visitTypeId);
		}catch(Exception e){
		logger.error("coversheetInstructionList failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response updateCoversheetInstructionStatus(CoversheetInstructionDto coversheetInstructionDto)
			throws ApplicationException {
		try{
			return iCoversheetInstructionDao.updateCoversheetInstructionStatus(coversheetInstructionDto);
		}catch(Exception e){
		logger.error("updateCoversheetInstructionStatus failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response coversheetInstructionListForBookView(CoversheetInstructionDto coversheetInstructionDto)
			throws ApplicationException {
		try{
			return iCoversheetInstructionDao.coversheetInstructionListForBookView(coversheetInstructionDto);
		}catch(Exception e){
		logger.error("coversheetInstructionListForBookView failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}
