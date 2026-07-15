package com.param.bloodBank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.bloodBank.dao.IBloodComponentSeparationDao;
import com.param.bloodBank.dao.IBloodComponentSeparationDetailsDao;
import com.param.bloodBank.dto.BloodComponentSeparationDetailsDto;
import com.param.bloodBank.dto.BloodComponentSeparationDto;
import com.param.bloodBank.model.BloodComponentSeparation;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class BloodComponentSeparationServiceImpl implements IBloodComponentSeparationService, ICommonConstants{

	final static Logger logger = Logger.getLogger(BloodComponentSeparationServiceImpl.class);


	
	@Autowired
	IBloodComponentSeparationDao iBloodComponentSeparationDao;
	
	@Autowired
	IBloodComponentSeparationDetailsDao iBloodComponentSeparationDetailsDao;
	
	@Override
	public Response saveBloodComponentSeparation(BloodComponentSeparationDto dto) throws ApplicationException {
		try {
			Response res =  iBloodComponentSeparationDao.saveBloodComponentSeparation(dto);
			if(res.getObject()!= null) {
				BloodComponentSeparation master= (BloodComponentSeparation) res.getObject();
				for (BloodComponentSeparationDetailsDto detailsDto : dto.getBloodComponentSeparationDetailsDtoList()) {
					detailsDto.setBloodComponentSeparationId(master.getBloodComponentSeparationId());
					iBloodComponentSeparationDetailsDao.saveBloodComponentSeparationDetails(detailsDto);
				}
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, master);
			}
			
		} catch (Exception e) {
			logger.error("saveBloodComponentSeparation failed", e);
			throw new ServiceException(e.getMessage());
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	@Transactional
	public Response getComponentSeparationListForStockUpdation(BloodComponentSeparationDetailsDto dto)
			throws ApplicationException {
		try {
			return iBloodComponentSeparationDao.getComponentSeparationListForStockUpdation(dto);

		} catch (Exception e) {
			logger.error("getComponentSeparationListForStockUpdation failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

}
