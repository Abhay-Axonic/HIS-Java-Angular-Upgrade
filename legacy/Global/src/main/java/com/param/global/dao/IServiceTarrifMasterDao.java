package com.param.global.dao;

import com.param.billing.global.transaction.model.ServiceTarrifMaster;
import com.param.global.common.Response;
import com.param.global.dto.VariablePricingDto;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;

public interface IServiceTarrifMasterDao extends IGenericDao<ServiceTarrifMaster, Integer>{

	Response saveServiceTarrifMaster(VariablePricingDto variablePricingDto) throws ApplicationException;
	
	Response updateServiceTarrifMaster(VariablePricingDto variablePricingDto) throws ApplicationException;
	
	Response getServiceTarrifMaster(VariablePricingDto variablePricingDto) throws ApplicationException;
	
	Response getVariableFactorDetailsByVisitType(VariablePricingDto variablePricingDto) throws ApplicationException;
}
