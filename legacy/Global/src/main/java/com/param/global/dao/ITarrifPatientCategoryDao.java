package com.param.global.dao;

import com.param.billing.global.transaction.model.ServiceTarrifPatientCategoryManager;
import com.param.global.common.Response;
import com.param.global.dto.TarrifPatientCategoryMpprDto;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;

public interface ITarrifPatientCategoryDao extends IGenericDao<ServiceTarrifPatientCategoryManager, Integer>{

	Response savePatientCategoryMultiplicationFactor(TarrifPatientCategoryMpprDto patientCategoryMpprDto) throws ApplicationException;
	
	Response getPatientCategoryMultiplicationFactorByTarrifId(Integer tarrifMasterId) throws ApplicationException;
	
}
 