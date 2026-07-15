package com.param.global.dao;

import com.param.billing.global.transaction.model.ServiceTarrifPaymentEntitlementManager;
import com.param.global.common.Response;
import com.param.global.dto.TarrifPaymentEntitlementMpprDto;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;

public interface ITarrifPaymentCategoryDao extends IGenericDao<ServiceTarrifPaymentEntitlementManager, Integer>{

	Response savePaymentEntitlementFactor(TarrifPaymentEntitlementMpprDto paymentEntitlementMpprDto) throws ApplicationException;
	
	/**
	 * Method to get multiplication factor for Payment Category by given tarrif master Id
	 * @param tarrifMasterId
	 * @return
	 * @throws ApplicationException
	 */
	Response getPaymentCategoryMultiplicationFactorByTarrifId(Integer tarrifMasterId) throws ApplicationException;
}
