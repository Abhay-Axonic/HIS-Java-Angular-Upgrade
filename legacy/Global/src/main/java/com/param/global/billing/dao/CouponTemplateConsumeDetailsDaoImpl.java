package com.param.global.billing.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.billing.global.model.CouponTemplateConsumeDetails;
import com.param.global.billing.controller.BillingException;
import com.param.global.billing.dto.CouponTemplateConsumeDetailsDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
@SuppressWarnings({"rawtypes","unused"})
@Repository
public class CouponTemplateConsumeDetailsDaoImpl extends GenericDao<CouponTemplateConsumeDetails, Integer> implements ICouponTemplateConsumeDetailsDao, ICommonConstants{

	public CouponTemplateConsumeDetailsDaoImpl() {
		super(CouponTemplateConsumeDetails.class);
	}
	
	@Autowired
	private SessionFactory SessionFactory;

	@Override
	public Response saveCouponTemplateConsumeDetails(List<CouponTemplateConsumeDetailsDto> list, Integer billingMasterId)
			throws ApplicationException {
		try{
			
			for (CouponTemplateConsumeDetailsDto dto : list) {
				
				CouponTemplateConsumeDetails masterD = new CouponTemplateConsumeDetails();
				masterD.setBillId(billingMasterId);
				masterD.setConsumeAmount(dto.getConsumeAmount());
				masterD.setPatientCouponTemplateMapperId(dto.getPatientCouponTemplateMapperId());
				masterD.setOrderDetailsId(dto.getOrderDetailsId());
				masterD.setServiceId(dto.getServiceId() != null ? dto.getServiceId() : null);
				masterD.setCreatedBy(dto.getCreatedBy());
				masterD.setCreatedDate(TimeZoneComponent.getDateByzone(dto.getUnitId()));
				masterD.setUnitId(dto.getUnitId());
				masterD.setOrganizationId(dto.getOrganizationId());
				masterD.setUpdatedBy(dto.getCreatedBy());
				masterD.setUpdatedDate(TimeZoneComponent.getDateByzone(dto.getUnitId()));
				masterD.setStatus(ACTIVE);
				masterD = save(masterD);
			}
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		}catch(HibernateException he){
			throw new BillingException(he.getMessage());
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}

}
