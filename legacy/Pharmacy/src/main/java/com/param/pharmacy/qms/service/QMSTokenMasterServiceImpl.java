package com.param.pharmacy.qms.service;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.pharmacy.qms.dao.IQMSTokenMasterDao;
import com.param.pharmacy.qms.dto.QMSTokenMasterDto;
import com.param.qms.model.QMSTokenMaster;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class QMSTokenMasterServiceImpl implements IQMSTokenMasterService, ICommonConstants {

	private static final Logger logger = Logger.getLogger(QMSTokenMasterServiceImpl.class);

	@Autowired
	IQMSTokenMasterDao iQMSTokenMasterDao;
	
	
	@Override
	public Response saveToken(QMSTokenMasterDto dto) throws ApplicationException {
		try{
						
		   Response res= iQMSTokenMasterDao.saveToken(dto);
		   if(res.getStatus()==SUCCESS && res.getObject()!=null){
			   QMSTokenMaster master=(QMSTokenMaster)res.getObject();
			   dto.setQmsTokenMasterId(master.getQmsTokenMasterId());
			   dto.setCallLogStatus(0); //for pending
			   Response res1= iQMSTokenMasterDao.saveTokenlog(dto);
			   if(res1.getStatus()==SUCCESS)
			   return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, dto.getStartLetter()+"-"+master.getTokenNumber());
		   }
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		return new Response(ERROR, ERROR_CODE, null, null, null);
	}


	@Override
	public Response getTokenList(QMSTokenMasterDto dto) throws ApplicationException {
		try{
			   return iQMSTokenMasterDao.getTokenList(dto);
			}catch (Exception e) {
				logger.error(e.getMessage(), e);
				throw new ServiceException(e.getMessage());
			}
	}


	@Override
	public Response updateToken(QMSTokenMasterDto dto) throws ApplicationException {
		try{			
			 /*  Response res= iQMSTokenMasterDao.updateToken(dto);
			   if(res.getStatus()==SUCCESS){
				    return iQMSTokenMasterDao.saveTokenlog(dto);
			   }*/
			Response res=iQMSTokenMasterDao.updateTokenlog(dto);
			
			if(dto.getCallLogStatus() ==1 ) {
				iQMSTokenMasterDao.updateQMSTokenMaster(dto);
			}
						 
						 
			if(res.getStatus()==SUCCESS){
				return iQMSTokenMasterDao.saveTokenlog(dto);
			}

			}catch (Exception e) {
				logger.error(e.getMessage(), e);
				throw new ServiceException(e.getMessage());
			}
			return new Response(ERROR, ERROR_CODE, null, null, null);
	}


	@Override
	public Response transferToken(QMSTokenMasterDto dto) throws ApplicationException {
		try{			
			   Response res= iQMSTokenMasterDao.transferToken(dto);
			   if(res.getStatus()==SUCCESS){
				   dto.setCallLogStatus(0);
				   return iQMSTokenMasterDao.saveTokenlog(dto);
			   }
			}catch (Exception e) {
				logger.error(e.getMessage(), e);
				throw new ServiceException(e.getMessage());
			}
			return new Response(ERROR, ERROR_CODE, null, null, null);
	}


	@Override
	public Response resetTokenSequence(QMSTokenMasterDto dto) throws ApplicationException {
		try{
			 return iQMSTokenMasterDao.resetToken(dto);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response getTokenListForDisplay(QMSTokenMasterDto dto) throws ApplicationException {
		try{
			 return iQMSTokenMasterDao.getTokenListForDisplay(dto);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response getTokenDisplayMessage(QMSTokenMasterDto dto) throws ApplicationException {
		try{
			 return iQMSTokenMasterDao.getTokenDisplayMessage(dto);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response checkingIsCalledOrNot(QMSTokenMasterDto dto) throws ApplicationException {
		try{
			 return iQMSTokenMasterDao.checkingIsCalledOrNot(dto);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response releaseTokenNumber(QMSTokenMasterDto dto) throws ApplicationException {
		try{
			 return iQMSTokenMasterDao.releaseTokenNumber(dto);
		}catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

}
