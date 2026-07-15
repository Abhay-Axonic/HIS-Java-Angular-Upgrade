package com.param.global.service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dao.IICPCMapperDao;
import com.param.global.dto.ICPCMapperDto;

import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
@SuppressWarnings({ "rawtypes"})
@Service
public class ICPCMapperServiceImpl implements IICPCMapperService, ICommonConstants {
	private static final Logger logger = Logger.getLogger(ICPCMapperServiceImpl.class);
	@Autowired
	IICPCMapperDao iICPCMapperDao;
	
	@Override
	public Response saveICPCMapper(ICPCMapperDto iCPCMapperDto) throws ApplicationException {
		// TODO Auto-generated method stub
//		return iICPCMapperDao.saveICPCMapper(iCPCMapperDto);
		Response res=iICPCMapperDao.checkICPCMapperAvailable(iCPCMapperDto);
		try {
			if(res.getListObject()!=null && res.getListObject().size()>0){
				
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				return iICPCMapperDao.saveICPCMapper(iCPCMapperDto);
			}

		} catch (Exception e) {
			logger.error("saveICPCMapper failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateICPCMapper(ICPCMapperDto iCPCMapperDto) throws ApplicationException {
		// TODO Auto-generated method stub
		return iICPCMapperDao.updateICPCMapper(iCPCMapperDto);
	}

	@Override
	public Response updateICPCMapperStatus(ICPCMapperDto iCPCMapperDto) throws ApplicationException {
		// TODO Auto-generated method stub
		return iICPCMapperDao.updateICPCMapperStatus(iCPCMapperDto);
	}

	@Override
	public Response getICPCMapperById(ICPCMapperDto iCPCMapperDto) throws ApplicationException {
		// TODO Auto-generated method stub
		return iICPCMapperDao.getICPCMapperById(iCPCMapperDto);
	}

	@Override
	public Response getICPCMapperList(ICPCMapperDto iCPCMapperDto) throws ApplicationException {
		// TODO Auto-generated method stub
		return iICPCMapperDao.getICPCMapperList(iCPCMapperDto);
	}

	@Override
	public Response getICPCMapperCount(ICPCMapperDto iCPCMapperDto) throws ApplicationException {
		// TODO Auto-generated method stub
		return iICPCMapperDao.getICPCMapperCount(iCPCMapperDto);
	}

	@Override
	public Response getActiveICPCMapperList(ICPCMapperDto iCPCMapperDto) throws ApplicationException {
		// TODO Auto-generated method stub
		return iICPCMapperDao.getActiveICPCMapperList(iCPCMapperDto);
	}

	@Override
	public Response getGenderListByICPCSystemId(ICPCMapperDto iCPCMapperDto) throws ApplicationException {
		// TODO Auto-generated method stub
		return iICPCMapperDao.getGenderListByICPCSystemId(iCPCMapperDto);
	}

	@Override
	public Response checkICPCMapperAvailable(ICPCMapperDto iCPCMapperDto) throws ApplicationException {
		// TODO Auto-generated method stub
		return iICPCMapperDao.checkICPCMapperAvailable(iCPCMapperDto);
	}

	@Override
	public Response getSystemListForComplaintsOnCoversheet(ICPCMapperDto iCPCMapperDto) throws ApplicationException {
		// TODO Auto-generated method stub
		return iICPCMapperDao.getSystemListForComplaintsOnCoversheet(iCPCMapperDto);	}

//	@Override
//	public Response getActiveGenderListForICPCMapper(ICPCMapperDto iCPCMapperDto) throws ApplicationException {
//		// TODO Auto-generated method stub
//		return iICPCMapperDao.getActiveGenderListForICPCMapper(iCPCMapperDto);
//	}

	@Override
	public Response getActiveGenderListForICPCMapper(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return iICPCMapperDao.getActiveGenderListForICPCMapper(request);
	}

	@Override
	public Response getICPCByName(String icpcName) {
		// TODO Auto-generated method stub
		return iICPCMapperDao.getICPCByName(icpcName);
	}

	@Override
		public Response getICPCByCode(String icpcName) {
		// TODO Auto-generated method stub
		return iICPCMapperDao.getICPCByCode(icpcName);
	}

}
