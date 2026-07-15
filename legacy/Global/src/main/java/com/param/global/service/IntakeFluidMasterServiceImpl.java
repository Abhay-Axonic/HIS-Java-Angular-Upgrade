package com.param.global.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dao.IIntakeFluidMasterDao;
import com.param.global.dao.IIntakeFluidTypeMasterDao;
import com.param.global.dao.IOutputFluidTypeMasterDao;
import com.param.global.unit.dto.IntakeFluidMasterDto;
import com.param.global.unit.dto.IntakeFluidTypeMasterDto;
import com.param.global.unit.dto.OutputTypeMasterDto;

import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;

@Service
@SuppressWarnings({"rawtypes"})
public class IntakeFluidMasterServiceImpl implements IIntakeFluidMasterService, ICommonConstants{
	private static final Logger logger = Logger.getLogger(IntakeFluidMasterServiceImpl.class);
	@Autowired
	IIntakeFluidMasterDao iIntakeFluidMasterDao;
	
	@Autowired
	IIntakeFluidTypeMasterDao iIntakeFluidTypeMasterDao;
	
	@Autowired
	IOutputFluidTypeMasterDao iOutputFluidTypeMasterDao;


	@Override
	public Response getIntakeFluidMasterList(IntakeFluidMasterDto intakeFluidMasterDto) throws ApplicationException {
		try {
			
			return iIntakeFluidMasterDao.getIntakeFluidMasterList(intakeFluidMasterDto);

	} catch (Exception e) {
		logger.error("getIntakeFluidMasterList failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

	@Override
	public Response getIntakeFluidTypeMasterListById(IntakeFluidTypeMasterDto intakeFluidTypeMasterDto)
			throws ApplicationException {
		try {
			
			return iIntakeFluidTypeMasterDao.getIntakeFluidTypeMasterListById(intakeFluidTypeMasterDto);

	} catch (Exception e) {
		logger.error("getIntakeFluidTypeMasterListById failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

	@Override
	public Response getOutputFluidMasterList(OutputTypeMasterDto outputTypeMasterDto) throws ApplicationException {
		try {
			
			return iOutputFluidTypeMasterDao.getOutputFluidMasterList(outputTypeMasterDto);

	} catch (Exception e) {
		logger.error("getOutputFluidMasterList failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}
	
	
}
