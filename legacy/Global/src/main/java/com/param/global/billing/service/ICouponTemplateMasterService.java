package com.param.global.billing.service;

import javax.transaction.Transactional;

import com.param.global.billing.dto.CouponTemplateMasterDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@Transactional
@SuppressWarnings("rawtypes")
public interface ICouponTemplateMasterService {

	Response saveCouponTemplateMaster(CouponTemplateMasterDto dto) throws ApplicationException;

	Response getCouponTemplateMaster(CouponTemplateMasterDto dto) throws ApplicationException;

	Response updateStatus(CouponTemplateMasterDto dto) throws ApplicationException;

	Response getCouponTemplateDetails(CouponTemplateMasterDto dto) throws ApplicationException;

}
