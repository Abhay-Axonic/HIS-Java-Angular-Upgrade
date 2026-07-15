package com.param.global.billing.dao;

import java.util.List;

import com.param.billing.global.model.CouponTemplateMaster;
import com.param.global.billing.dto.CouponTemplateDetailsDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface ICouponTemplateDetailsDao {
	
	Response saveCouponTemplateMasterDetails(List<CouponTemplateDetailsDto> couponTemplateDetailsDtoList,
			CouponTemplateMaster master)throws ApplicationException;

	Response getCouponTemplateDetailsById(Integer couponTemplateMasterId)throws ApplicationException;

}
