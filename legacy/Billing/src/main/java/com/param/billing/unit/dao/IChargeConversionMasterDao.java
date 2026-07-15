package com.param.billing.unit.dao;

import com.param.billing.global.transaction.config.dto.ChargeConversionMasterDto;
import com.param.billing.global.transaction.model.ChargeConversionMaster;
import com.param.global.common.Response;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IChargeConversionMasterDao extends IGenericDao<ChargeConversionMaster,Integer>{

	public  Response saveChargeConversionMasterList(ChargeConversionMasterDto chargeConversionMasterDto)throws ApplicationException;
	

}
