package com.param.global.billing.dao;

import com.param.global.billing.dto.CouponTemplateMasterDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface ICouponTemplateMasterDao {

	Response saveCouponTemplateMaster(CouponTemplateMasterDto dto)throws ApplicationException;

	Response getCouponTemplateMaster(CouponTemplateMasterDto dto)throws ApplicationException;

	Response updateStatus(CouponTemplateMasterDto dto)throws ApplicationException;

	Response getCouponTemplateMasterById(CouponTemplateMasterDto dto)throws ApplicationException;

	Response getCheckServiceBilledOrNot(CouponTemplateMasterDto dto, Integer serviceId)throws ApplicationException;

}
