package com.param.global.billing.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.billing.global.model.PatientCouponTemplateMapper;
import com.param.global.billing.dao.ICouponTemplateConsumeDetailsDao;
import com.param.global.billing.dao.IPatientCouponTemplateMapperDao;
import com.param.global.billing.dto.CouponTemplateConsumeDetailsDto;
import com.param.global.billing.dto.PatientCouponTemplateMapperDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class PatientCouponTemplateMapperServiceImpl implements IPatientCouponTemplateMapperService, ICommonConstants{

	
	@Autowired
	IPatientCouponTemplateMapperDao iPatientCouponTemplateMapperDao;
	
	@Autowired
	ICouponTemplateConsumeDetailsDao iCouponTemplateConsumeDetailsDao;
	 
	
	@Override
	public Response savePatientCouponTemplateMapper(List<PatientCouponTemplateMapperDto> dto) throws ApplicationException {
		try {
			return iPatientCouponTemplateMapperDao.savePatientCouponTemplateMapper(dto);
			
		} catch (Exception e) {
			
			throw e;
		}
	}


	@Override
	public Response forceStopPatientCouponTemplate(PatientCouponTemplateMapperDto dto) throws ApplicationException {
		try {
			return iPatientCouponTemplateMapperDao.forceStopPatientCouponTemplate(dto);
			
		} catch (Exception e) {
			
			throw e;
		}
	}


	@Override
	public Response updateCouponCode(List<PatientCouponTemplateMapper> saveList) throws ApplicationException {
		try {
			return iPatientCouponTemplateMapperDao.updateCouponCode(saveList);
			
		} catch (Exception e) {
			
			throw e;
		}
	}


	@Override
	public Response getMappedPatientCouponTemplate(PatientCouponTemplateMapperDto dto) throws ApplicationException {
		try {
			return iPatientCouponTemplateMapperDao.getMappedPatientCouponTemplate(dto);
			
		} catch (Exception e) {
			
			throw e;
		}
	}


	@Override
	public Response counsumCouponTemplate(PatientCouponTemplateMapperDto dto) throws Exception {
		try {
			Response res = iPatientCouponTemplateMapperDao.counsumCouponTemplate(dto);
			
			if(res.getObject() != null) {
				
				PatientCouponTemplateMapperDto obj = (PatientCouponTemplateMapperDto) res.getObject();
				
				if(obj.getStatus() == 'A') {
					
					if(obj.getConsume() == false) {
						
						Character consumption = 'Y';
						if(obj.getConsumption().equals(consumption)) {
							
							Response rescheckPerDay = iPatientCouponTemplateMapperDao.checkToCousumeCouponPerDay(obj.getPatientId(),dto.getUnitId(),obj.getServiceId());
							
							if(rescheckPerDay.getListObject()!=null && rescheckPerDay.getListObject().size()>0)
							{
								return new Response(ERROR, ERROR,"Already you have consume same service coupon. you have configure for this service (Consumption Once Per Day) ", null, null);
							}
						}
						
						String pattern = "yyyy-MM-dd";
						SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
							
							Date targetTime =  TimeZoneComponent.getDateByzone(dto.getUnitId());
							String date1 = simpleDateFormat.format(targetTime);
							Date getAddedDate = simpleDateFormat.parse(date1);   
							
							Date getDate = simpleDateFormat.parse(obj.getCreatedDate());
							getDate = DateUtils.addDays(getDate, obj.getExpireDays()); 
						
							if (getDate.after(getAddedDate) || getDate.equals(getAddedDate)) {
								
								if(dto.getEncounterId() != null){
								
									Response rescheck = iPatientCouponTemplateMapperDao.checkToCousumeCoupon(dto.getEncounterId(),obj.getPatientCouponTemplateMapperId());
									
									if(rescheck.getListObject()!=null && rescheck.getListObject().size()>0)
									{
										return new Response(ERROR, ERROR,"This coupon is used in other visit", null, null);
									}else {
										 return new Response(SUCCESS, SUCCESS_CODE, null, null, obj);
									}	
								
								}else{
									 return new Response(SUCCESS, SUCCESS_CODE, null, null, obj);
								}
								
							}else {
									
									return new Response(ERROR, ERROR,"This coupon is expired", null, null);
							}
						
					}else {
						return new Response<>(ERROR, ERROR, "This coupon is already consumed ", null, null);
					}
					
				}else {
					return new Response<>(ERROR, ERROR, "This coupon is inactive ", null, null);
				}
				
			}else {
				return new Response<>(ERROR, ERROR, "Please enter valid coupon code ", null, null);
			}
			
			
		} catch (Exception e) {
			
			throw e;
		}
	}


	@Override
	public Response savePatientConsumptionData(List<CouponTemplateConsumeDetailsDto> patientCouponConsumptionList,
			Integer billingMasterId) throws ApplicationException, Exception {
	
		try {
			Response res =  iCouponTemplateConsumeDetailsDao.saveCouponTemplateConsumeDetails(patientCouponConsumptionList,billingMasterId);
			
			if(res.getStatus() == SUCCESS) {
				
				for (CouponTemplateConsumeDetailsDto couponTemplateConsumeDetailsDto : patientCouponConsumptionList) {
					 iPatientCouponTemplateMapperDao.updateToCousumeCoupon(couponTemplateConsumeDetailsDto);
				}
				
			}
			
			return res;
		} catch (Exception e) {
			
			throw e;
		}
	}


	@Override
	public Response getpatientCouponMapperByOrderId(List<Integer> orderIdList) throws ApplicationException, Exception {
		try {
			return iPatientCouponTemplateMapperDao.getpatientCouponMapperByOrderId(orderIdList);
			
		} catch (Exception e) {
			
			throw e;
		}
	}


	@Override
	public Response updateOrderDetailsAndPatientCoupon(Integer billingMasterId) throws ApplicationException, Exception {
		try {
			Response res = iPatientCouponTemplateMapperDao.getOrderDetailsAndPatientCoupon(billingMasterId);
			
			if(res.getListObject()!= null && res.getListObject().size()>0) {
				
				List<CouponTemplateConsumeDetailsDto> obj =res.getListObject();
				
				for (CouponTemplateConsumeDetailsDto couponTemplateConsumeDetailsDto : obj) {
					 iPatientCouponTemplateMapperDao.updateOrderDetailsAndPatientCoupon(couponTemplateConsumeDetailsDto);
				}
				
			}
			
			return res;
		} catch (Exception e) {
			
			throw e;
		}
	}

}
