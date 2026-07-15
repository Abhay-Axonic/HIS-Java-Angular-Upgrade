package com.param.global.billing.service;

import java.util.List;

import javax.transaction.Transactional;

import com.param.billing.global.model.PatientCouponTemplateMapper;
import com.param.global.billing.dto.CouponTemplateConsumeDetailsDto;
import com.param.global.billing.dto.PatientCouponTemplateMapperDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@Transactional
@SuppressWarnings("rawtypes")
public interface IPatientCouponTemplateMapperService {

	Response savePatientCouponTemplateMapper(List<PatientCouponTemplateMapperDto> dto) throws ApplicationException;

	Response forceStopPatientCouponTemplate(PatientCouponTemplateMapperDto dto) throws ApplicationException;

	Response updateCouponCode(List<PatientCouponTemplateMapper> saveList)throws ApplicationException;

	Response getMappedPatientCouponTemplate(PatientCouponTemplateMapperDto dto)throws ApplicationException;

	Response counsumCouponTemplate(PatientCouponTemplateMapperDto dto)throws ApplicationException, Exception;

	Response savePatientConsumptionData(List<CouponTemplateConsumeDetailsDto> patientCouponConsumptionList,
			Integer billingMasterId)throws ApplicationException, Exception;

	Response getpatientCouponMapperByOrderId(List<Integer> orderIdList)throws ApplicationException, Exception;

	Response updateOrderDetailsAndPatientCoupon(Integer billingMasterId)throws ApplicationException, Exception;

}
