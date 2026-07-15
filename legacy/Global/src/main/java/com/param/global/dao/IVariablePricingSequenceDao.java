package com.param.global.dao;

import com.param.billing.global.transaction.config.model.VariablePricingSequenceMaster;
import com.param.global.common.Response;
import com.param.global.dto.VariablePricingSequenceDto;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;

public interface IVariablePricingSequenceDao  extends IGenericDao<VariablePricingSequenceMaster, Integer>{

	//Method to save variable pricing sequence
	Response saveVariablePricingSequenceMaster(VariablePricingSequenceDto variablePricingSequenceDto) throws ApplicationException;
	
	//Method to get variable pricing sequence by organisation id
	Response getVariablePricingSequenceByOrganisationId(VariablePricingSequenceDto variablePricingSequenceDto) throws ApplicationException;
}
