package com.param.billing.global.service;

import javax.transaction.Transactional;

import com.param.billing.common.Response;
import com.param.billing.global.dto.CardTypeMasterDto;
import com.param.global.dto.CountryMasterDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface ICardTypeMasterService {
	public Response gerActiveCardTypeMaster(CardTypeMasterDto cardTypeMasterDto)throws ApplicationException;

	@Transactional
	Response addCardTypeMaster(CardTypeMasterDto cardTypeMasterDto) throws ApplicationException;

	@Transactional
	Response getCardTypeMasterList(CardTypeMasterDto cardTypeMasterDto) throws ApplicationException;

	@Transactional
	Response updateStatusForCardType(CardTypeMasterDto cardTypeMasterDto) throws ApplicationException;

	@Transactional
	Response updateCardTypeMaster(CardTypeMasterDto cardTypeMasterDto) throws ApplicationException;

	
}
