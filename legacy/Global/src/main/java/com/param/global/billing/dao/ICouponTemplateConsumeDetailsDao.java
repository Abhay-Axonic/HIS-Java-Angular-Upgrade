package com.param.global.billing.dao;

import java.util.List;

import com.param.global.billing.dto.CouponTemplateConsumeDetailsDto;
import com.param.global.common.Response;
import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface ICouponTemplateConsumeDetailsDao {

	Response saveCouponTemplateConsumeDetails(List<CouponTemplateConsumeDetailsDto> dto, Integer billingMasterId)throws ApplicationException;
}
