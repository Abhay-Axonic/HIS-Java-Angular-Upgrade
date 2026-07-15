package com.param.global.billing.dao;

import java.util.List;

import com.param.billing.global.model.PatientCouponTemplateMapper;
import com.param.global.billing.dto.CouponTemplateConsumeDetailsDto;
import com.param.global.billing.dto.PatientCouponTemplateMapperDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IPatientCouponTemplateMapperDao {

	Response savePatientCouponTemplateMapper(List<PatientCouponTemplateMapperDto> dto)throws ApplicationException;

	Response forceStopPatientCouponTemplate(PatientCouponTemplateMapperDto dto)throws ApplicationException;

	Response updateCouponCode(List<PatientCouponTemplateMapper> saveList)throws ApplicationException;

	Response getMappedPatientCouponTemplate(PatientCouponTemplateMapperDto dto)throws ApplicationException;

	Response counsumCouponTemplate(PatientCouponTemplateMapperDto dto)throws ApplicationException;

	Response getServiceDetailsById(PatientCouponTemplateMapperDto dto)throws ApplicationException;

	Response updateToCousumeCoupon(CouponTemplateConsumeDetailsDto couponTemplateConsumeDetailsDto)throws ApplicationException;

	Response getpatientCouponMapperByOrderId(List<Integer> orderIdList)throws ApplicationException;

	Response getOrderDetailsAndPatientCoupon(Integer billingMasterId)throws ApplicationException;

	Response updateOrderDetailsAndPatientCoupon(CouponTemplateConsumeDetailsDto couponTemplateConsumeDetailsDto)throws ApplicationException;

	Response checkToCousumeCoupon(Integer encounterId, Integer patientCouponTemplateMapperId)throws ApplicationException;

	Response checkToCousumeCouponPerDay(Integer patientId, Integer unitId, Integer serviceId)throws ApplicationException;

}
