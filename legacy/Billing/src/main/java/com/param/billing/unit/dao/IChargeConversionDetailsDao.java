package com.param.billing.unit.dao;

import com.param.billing.global.transaction.config.dto.ChargeConversionDetailsDto;
import com.param.billing.global.transaction.model.ChargeConversionDetails;
import com.param.global.common.Response;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IChargeConversionDetailsDao extends IGenericDao<ChargeConversionDetails,Integer>{

	public  Response saveChargeConversionDetailsList(ChargeConversionDetails chargeConversionMaster)throws ApplicationException;
	
	public  Response getOrderDetailsListByOrderId(ChargeConversionDetailsDto chargeConversionMasterDto)throws ApplicationException;
	

}
