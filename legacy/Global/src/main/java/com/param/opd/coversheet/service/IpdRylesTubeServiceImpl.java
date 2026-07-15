package com.param.opd.coversheet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.opd.coversheet.dao.IIpdRylesTubeDao;
import com.param.opd.coversheet.dto.IpdRylesTubeDto;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({"rawtypes"})	
public class IpdRylesTubeServiceImpl implements IIpdRylesTubesService, ICommonConstants {
	final static Logger logger = Logger.getLogger(IpdRylesTubeServiceImpl.class);

	
	@Autowired
	IIpdRylesTubeDao iIpdRylesTubeDao;

	@Override
	public Response saveIpdRylesTubeOrder(IpdRylesTubeDto ipdRylesTubeDto) throws ApplicationException {
		try {
//			Response response=iIpdDietOrderDao.getIpdDietOrderByName(IpdDietOrderDto);
//			
//			if(response.getListObject()!=null && response.getListObject().size()>0){
//				
//				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
//			}else{
				
				return iIpdRylesTubeDao.saveIpdRylesTubeOrder(ipdRylesTubeDto.getIpdRylesTubeOrderDtoList());
//			}
		} catch (Exception e) {
		logger.error("saveIpdRylesTubeOrder failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@Override
	public Response getIpdRylesTubeOrderList(IpdRylesTubeDto ipdRylesTubeDto) throws ApplicationException {
		try {
			
			return iIpdRylesTubeDao.getIpdRylesTubeOrderList(ipdRylesTubeDto);
	} catch (Exception e) {
	logger.error("getIpdRylesTubeOrderList failed", e);
	}
	return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}
	@Override
	public Response updateIpdRylesTubeOrder(IpdRylesTubeDto ipdRylesTubeDto) throws ApplicationException {
		try {
				
				return iIpdRylesTubeDao.updateIpdRylesTubeOrder(ipdRylesTubeDto.getIpdRylesTubeOrderDtoList());
		} catch (Exception e) {
		logger.error("updateIpdRylesTubeOrder failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@Override
	public Response cancelRylesTubeOrder(IpdRylesTubeDto ipdRylesTubeDto) throws ApplicationException {
		try {
			
			return iIpdRylesTubeDao.cancelRylesTubeOrder(ipdRylesTubeDto);
	} catch (Exception e) {
	logger.error("cancelRylesTubeOrder failed", e);
	}
	return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}


}
