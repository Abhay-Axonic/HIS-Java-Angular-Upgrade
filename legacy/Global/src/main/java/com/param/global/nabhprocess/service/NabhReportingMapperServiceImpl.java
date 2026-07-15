package com.param.global.nabhprocess.service;


import com.param.global.common.ICommonConstants;
import com.param.global.common.IError;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.nabhprocess.dao.INabhReportingMapperDao;
import com.param.global.unit.dto.NabhReportingMapperDto;
import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
public class NabhReportingMapperServiceImpl implements INabhReportingMapperService, ICommonConstants, IError {

	private static final Logger logger = Logger.getLogger(NabhReportingMapperServiceImpl.class);

	@Autowired
	INabhReportingMapperDao iNabhReportingMapperDao;

	@Override
	public Response saveNabhReportingMapper(NabhReportingMapperDto nabhReportingMapperDto)
			throws ApplicationException {
		try {

			return iNabhReportingMapperDao.saveNabhReportingMapper(nabhReportingMapperDto);

		} catch (Exception e) {
			logger.error("saveNabhReportingMapper failed: " + e.getMessage(), e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@Override
	public Response getNabhReportingMapperList(NabhReportingMapperDto nabhReportingMapperDto)
			throws ApplicationException {
		try {

			return iNabhReportingMapperDao.getNabhReportingMapperList(nabhReportingMapperDto);

		} catch (Exception e) {
			logger.error("getNabhReportingMapperList failed: " + e.getMessage(), e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getNabhReportingMapperById(NabhReportingMapperDto nabhReportingMapperDto)
			throws ApplicationException {
		try {

			return iNabhReportingMapperDao.getNabhReportingMapperById(nabhReportingMapperDto);

		} catch (Exception e) {
			logger.error("getNabhReportingMapperById failed: " + e.getMessage(), e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}



}
