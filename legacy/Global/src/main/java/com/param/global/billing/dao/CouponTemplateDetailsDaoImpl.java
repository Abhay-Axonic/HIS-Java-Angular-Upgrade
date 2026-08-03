package com.param.global.billing.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.billing.global.model.CouponTemplateDetails;
import com.param.billing.global.model.CouponTemplateMaster;
import com.param.global.billing.controller.BillingException;
import com.param.global.billing.dto.CouponTemplateDetailsDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.service.VerifiedIdentityService;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({"rawtypes"})
public class CouponTemplateDetailsDaoImpl extends GenericDao<CouponTemplateDetails, Integer> implements ICouponTemplateDetailsDao, ICommonConstants{

	public CouponTemplateDetailsDaoImpl() {
		super(CouponTemplateDetails.class);
	}

	@Autowired
	private VerifiedIdentityService verifiedIdentityService;

	@Override
	public Response saveCouponTemplateMasterDetails(List<CouponTemplateDetailsDto> couponTemplateDetailsDtoList,
			CouponTemplateMaster master) throws ApplicationException {
		try{
			
			for (CouponTemplateDetailsDto dto : couponTemplateDetailsDtoList) {
				
				CouponTemplateDetails masterD = new CouponTemplateDetails();
				masterD.setCouponTemplateMasterId(master.getCouponTemplateMasterId());
				masterD.setPercentage(dto.getPercentage());
				masterD.setAmount(dto.getAmount());
				masterD.setServiceId(dto.getServiceId() != null ? dto.getServiceId() : null);
				masterD.setSpecialityId(dto.getSpecialityId()!= null ? dto.getSpecialityId() : null);
				masterD.setSubSpecialityId(dto.getSubSpecialityId()!= null ? dto.getSubSpecialityId() : null);
				masterD.setCreatedBy(dto.getCreatedBy());
				masterD.setCreatedDate(TimeZoneComponent.getDateByzone(dto.getUnitId()));
				masterD.setUnitId(dto.getUnitId());
				masterD.setUpdatedBy(dto.getUpdatedBy());
				masterD.setUpdatedDate(TimeZoneComponent.getDateByzone(dto.getUnitId()));
				
				if(dto.getCouponTemplateDetailsId() != null) {
					masterD.setCouponTemplateDetailsId(dto.getCouponTemplateDetailsId());
					masterD.setStatus(dto.getStatus());
					masterD = update(masterD);
				}else {
					masterD.setStatus(ACTIVE);
					masterD = save(masterD);
				}
				
			}
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, master);
		}catch(HibernateException he){
			throw new BillingException(he.getMessage());
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response getCouponTemplateDetailsById(Integer couponTemplateMasterId) throws ApplicationException {
		try
        {
			List<CouponTemplateDetailsDto> list = sessionFactory.getCurrentSession().createSQLQuery(
				" select "  + 
				"	mctd.coupon_template_details_id as \"couponTemplateDetailsId\", "  + 
				"	mctd.coupon_template_master_id as \"couponTemplateMasterId\", "  + 
				"	mctd.speciality_id  as \"specialityId\", "  + 
				"	mctd.sub_speciality_id  as \"subSpecialityId\", "  + 
				"	mctd.service_id  as \"serviceId\", "  + 
				"	mctd.percentage  as \"percentage\", "  + 
				"	mctd.amount  as \"amount\", "  + 
				"	msm.speciality_name  as \"specialityName\", "  + 
				"	mssm.sub_speciality_name  as \"subSpecialityName\", "  + 
				"	ser.service_standard_name  as \"serviceName\", "  + 
				"	mctd.status  as \"status\" "  + 
				"	from billing.m_coupon_template_details mctd  "  + 
				"	inner join doctor.m_speciality_master msm  on msm.speciality_id = mctd.speciality_id  "  + 
				"	left join doctor.m_sub_speciality_master mssm  on mssm.sub_speciality_id  = mctd.sub_speciality_id  "  + 
				"	left join service.m_service_master ser on ser.service_master_id  = mctd.service_id  "  + 
				"	where mctd.status  = 'A' and mctd.coupon_template_master_id =:couponTemplateMasterId "
				+ " AND mctd.organization_id =:organizationId AND mctd.unit_id =:unitId ")
					.setParameter("couponTemplateMasterId", couponTemplateMasterId)
					.setParameter("organizationId", verifiedIdentityService.getVerifiedOrganizationId())
					.setParameter("unitId", verifiedIdentityService.getVerifiedUnitId())
				.setResultTransformer(Transformers.aliasToBean(CouponTemplateDetailsDto.class)).list();
			
				return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		
        } catch (Exception e)
        {
            
            throw e;
        }
	}
}
