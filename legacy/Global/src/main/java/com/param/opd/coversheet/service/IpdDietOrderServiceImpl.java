package com.param.opd.coversheet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.opd.coversheet.dao.IIpdDietOrderDao;
import com.param.opd.coversheet.dto.IpdDietOrderDto;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({"rawtypes"})	
public class IpdDietOrderServiceImpl implements IIpdDietOrderService, ICommonConstants {
	final static Logger logger = Logger.getLogger(IpdDietOrderServiceImpl.class);

	@Autowired
	IIpdDietOrderDao iIpdDietOrderDao;


	@Override
	public Response saveIpdDietOrder(IpdDietOrderDto ipdDietOrderDto)
			throws ApplicationException {
		try {
//			Response response=iIpdDietOrderDao.getIpdDietOrderByName(IpdDietOrderDto);
//			
//			if(response.getListObject()!=null && response.getListObject().size()>0){
//				
//				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
//			}else{
				
				return iIpdDietOrderDao.saveIpdDietOrder(ipdDietOrderDto.getIpdDietOrderDtoList());
//			}
		} catch (Exception e) {
		logger.error("saveIpdDietOrder failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getIpdDietOrderList(IpdDietOrderDto ipdDietOrderDto)
			throws ApplicationException {
		try {
				
				return iIpdDietOrderDao.getIpdDietOrderList(ipdDietOrderDto);
		} catch (Exception e) {
		logger.error("getIpdDietOrderList failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

	@Override
	public Response updateIpdDietOrder(IpdDietOrderDto ipdDietOrderDto) throws ApplicationException {
		try {
				return iIpdDietOrderDao.updateIpdDietOrder(ipdDietOrderDto.getIpdDietOrderDtoList());
		} catch (Exception e) {
		logger.error("updateIpdDietOrder failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response cancelDietOrder(IpdDietOrderDto ipdDietOrderDto) throws ApplicationException {
		try {
			return iIpdDietOrderDao.cancelDietOrder(ipdDietOrderDto);
	} catch (Exception e) {
	logger.error("cancelDietOrder failed", e);
	}
	return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

}