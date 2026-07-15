package com.param.billing.global.transaction.config.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.billing.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IVariablePricingSequenceDao;
import com.param.global.dto.VariablePricingSequenceDto;

import in.param.exception.ApplicationException;

@Service
@Transactional
public class VariablePricingSequenceServiceImpl implements IVariablePricingSequenceService, ICommonConstants{

	@Autowired
	IVariablePricingSequenceDao iVariablePricingSeqDao;
	
	@Override
	public Response saveVariablePricingSequenceMaster(
			VariablePricingSequenceDto variablePricingSequenceDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iVariablePricingSeqDao.saveVariablePricingSequenceMaster(variablePricingSequenceDto);
	}

	@Override
	public Response getVariablePricingSequenceByOrganisationId(VariablePricingSequenceDto variablePricingSequenceDto) throws ApplicationException {
		// TODO Auto-generated method stub
		return iVariablePricingSeqDao.getVariablePricingSequenceByOrganisationId(variablePricingSequenceDto);
	}

}
