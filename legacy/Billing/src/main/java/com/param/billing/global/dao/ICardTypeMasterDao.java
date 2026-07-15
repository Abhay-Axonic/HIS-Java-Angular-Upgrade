package com.param.billing.global.dao;

import com.param.billing.common.Response;
import com.param.billing.global.dto.CardTypeMasterDto;
import com.param.billing.global.model.CardTypeMaster;
import com.param.global.dto.CountryMasterDto;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface ICardTypeMasterDao extends IGenericDao<CardTypeMaster, Integer>{
	public Response gerActiveCardTypeMaster(CardTypeMasterDto cardTypeMasterDto)throws ApplicationException;

	Response getCardTypeByName(CardTypeMasterDto cardTypeMasterDto)throws ApplicationException;

	Response addCardTypeMaster(CardTypeMasterDto cardTypeMasterDto)throws ApplicationException;

	Response getCardTypeMasterList(CardTypeMasterDto cardTypeMasterDto)throws ApplicationException;

	Response updateCardTypeStatus(CardTypeMasterDto cardTypeMasterDto)throws ApplicationException;

	Response getCardTypeByNameNotId(CardTypeMasterDto cardTypeMasterDto)throws ApplicationException;

	Response updateCardTypeMaster(CardTypeMasterDto cardTypeMasterDto)throws ApplicationException;
	
}
