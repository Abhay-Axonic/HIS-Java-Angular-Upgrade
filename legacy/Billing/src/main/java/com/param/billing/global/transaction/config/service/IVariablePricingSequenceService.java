package com.param.billing.global.transaction.config.service;

import com.param.global.common.Response;
import com.param.global.dto.VariablePricingSequenceDto;

import in.param.exception.ApplicationException;

public interface IVariablePricingSequenceService {

	//Method to save variable pricing sequence
	Response saveVariablePricingSequenceMaster(VariablePricingSequenceDto variablePricingSequenceDto) throws ApplicationException;
	
	//Method to get variable pricing sequence by organisation id
	Response getVariablePricingSequenceByOrganisationId(VariablePricingSequenceDto variablePricingSequenceDto) throws ApplicationException;
}
