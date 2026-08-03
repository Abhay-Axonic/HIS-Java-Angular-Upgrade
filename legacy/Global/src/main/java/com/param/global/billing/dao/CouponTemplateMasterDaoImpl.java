package com.param.global.billing.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.billing.global.model.CouponTemplateMaster;
import com.param.global.billing.controller.BillingException;
import com.param.global.billing.dto.CouponTemplateMasterDto;
import com.param.global.billing.dto.PatientCouponTemplateMapperDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.service.VerifiedIdentityService;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class CouponTemplateMasterDaoImpl extends GenericDao<CouponTemplateMaster, Integer> implements ICouponTemplateMasterDao, ICommonConstants{

	public CouponTemplateMasterDaoImpl() {
		super(CouponTemplateMaster.class);
	}
	
	@Autowired
	private SessionFactory SessionFactory;

	@Autowired
	private VerifiedIdentityService verifiedIdentityService;

	@Override
	public Response saveCouponTemplateMaster(CouponTemplateMasterDto dto) throws ApplicationException {
		try{
			// BR-157: cross-tenant IDOR fix -- tenant scoping comes from the JWT-verified
			// identity only, never from client-supplied org/unit (the old code wrote
			// dto.getOrganizationId()/dto.getUnitId() directly, which live-confirmed let an
			// org-1 session create rows in org 999). Created/updated-by are also re-bound to
			// the verified user identity.
			Integer verifiedOrgId = verifiedIdentityService.getVerifiedOrganizationId();
			Integer verifiedUnitId = verifiedIdentityService.getVerifiedUnitId();
			CouponTemplateMaster master = new CouponTemplateMaster();
				master.setCouponTemplateName(dto.getCouponTemplateName());
				master.setCouponTemplateDescription(dto.getCouponTemplateDescription());
				master.setExpireDays(dto.getExpireDays());
				master.setCreatedBy(dto.getCreatedBy());
				master.setCreatedDate(TimeZoneComponent.getDateByzone(verifiedUnitId));
				master.setStatus(ACTIVE);
				master.setUnitId(verifiedUnitId);
				master.setOrganizationId(verifiedOrgId);
				master.setUpdatedBy(dto.getUpdatedBy());
				master.setUpdatedDate(TimeZoneComponent.getDateByzone(verifiedUnitId));
				master.setServiceId(dto.getServiceId()!= null ? dto.getServiceId() : null);
				if(dto.getCouponTemplateMasterId() != null) {
					CouponTemplateMaster existing = findById(dto.getCouponTemplateMasterId());
					// BR-157: reject update of a row not owned by the verified tenant
					if (existing == null || existing.getOrganizationId() == null
							|| !existing.getOrganizationId().equals(verifiedOrgId)
							|| existing.getUnitId() == null
							|| !existing.getUnitId().equals(verifiedUnitId)) {
						return new Response<>(ERROR, ERROR_CODE, "Cross-tenant update rejected", null, null);
					}
					master.setCouponTemplateMasterId(dto.getCouponTemplateMasterId());
					master = update(master);
				}else {
					master = save(master);
				}
				
				return new Response<>(SUCCESS, SUCCESS_CODE, null, null, master);
		}catch(HibernateException he){
			throw new BillingException(he.getMessage());
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	public Response getCouponTemplateMaster(CouponTemplateMasterDto dto) throws ApplicationException {
 	try
 {
 		// BR-161: cross-tenant read fix -- scope the list by the JWT-verified
 		// org/unit, never the client-supplied values.
 		Integer verifiedOrgId = verifiedIdentityService.getVerifiedOrganizationId();
 		Integer verifiedUnitId = verifiedIdentityService.getVerifiedUnitId();
 		List<CouponTemplateMasterDto> list = sessionFactory.getCurrentSession().createSQLQuery(
 			" select coupon_template_master_id as \"couponTemplateMasterId\" ,coupon_template_name as \"couponTemplateName\",coupon_template_description as \"couponTemplateDescription\" ,expire_days as \"expireDays\" ," + 
 			"status as \"status\" " + 
 			"from billing.m_coupon_template_master where organization_id =:organizationId and unit_id =:unitId ")
 				.setParameter("organizationId", verifiedOrgId)
 				.setParameter("unitId", verifiedUnitId)
 			.setResultTransformer(Transformers.aliasToBean(CouponTemplateMasterDto.class)).list();
			
				return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		
        } catch (Exception e)
        {
            
            throw e;
        }
	}

	@Override
	public Response updateStatus(CouponTemplateMasterDto dto) throws ApplicationException {
		try {
			// BR-157: cross-tenant IDOR fix (unscoped status toggle) -- the old UPDATE had
			// no org/unit in the WHERE clause, so any authenticated session could flip any
			// tenant's coupon status by ID (live-confirmed: org-1 session toggled an
			// org-999 victim row A->I). Scope the UPDATE to the JWT-verified org/unit and
			// return an error when no row is owned by the verified tenant.
			Integer verifiedOrgId = verifiedIdentityService.getVerifiedOrganizationId();
			Integer verifiedUnitId = verifiedIdentityService.getVerifiedUnitId();
			int updated = SessionFactory.getCurrentSession().createSQLQuery(
					"UPDATE billing.m_coupon_template_master SET status=:status "
					+ "WHERE coupon_template_master_id =:id AND organization_id =:organizationId AND unit_id =:unitId")
					.setInteger("id", dto.getCouponTemplateMasterId())
					.setParameter("status", dto.getStatus())
					.setParameter("organizationId", verifiedOrgId)
					.setParameter("unitId", verifiedUnitId)
					.executeUpdate();
			if (updated == 0) {
				return new Response<>(ERROR, ERROR_CODE, "Coupon template not found for the verified tenant", null, null);
			}
			return new Response(SUCCESS, SUCCESS_CODE, null, null, null);

		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response getCouponTemplateMasterById(CouponTemplateMasterDto dto) throws ApplicationException {
		try
        {
			// BR-161: cross-tenant read fix -- the old query keyed ONLY by
			// coupon_template_master_id, so any authenticated session could read any
			// tenant's coupon master by ID (live-confirmed: org-1 session read an
			// org-999 victim master + its details). Scope to the JWT-verified org/unit.
			Integer verifiedOrgId = verifiedIdentityService.getVerifiedOrganizationId();
			Integer verifiedUnitId = verifiedIdentityService.getVerifiedUnitId();
			CouponTemplateMasterDto couponTemplateMasterDto = (CouponTemplateMasterDto)sessionFactory.getCurrentSession().createSQLQuery(
				" select coupon_template_master_id as \"couponTemplateMasterId\" ,coupon_template_name as \"couponTemplateName\",coupon_template_description as \"couponTemplateDescription\" ,expire_days as \"expireDays\" ," + 
				" mctm.status as \"status\" ,service_id as \"serviceId\", msm.service_standard_name as \"serviceName\" " + 
				"from billing.m_coupon_template_master mctm "
				+ " left join service.m_service_master msm on msm.service_master_id = service_id  "
				+ " where coupon_template_master_id =:couponTemplateMasterId "
				+ " AND mctm.organization_id =:organizationId AND mctm.unit_id =:unitId " )
					.setParameter("couponTemplateMasterId", dto.getCouponTemplateMasterId())
					.setParameter("organizationId", verifiedOrgId)
					.setParameter("unitId", verifiedUnitId)
				.setResultTransformer(Transformers.aliasToBean(CouponTemplateMasterDto.class)).uniqueResult();
			
				return new Response<>(SUCCESS, SUCCESS_CODE, null, null, couponTemplateMasterDto);
		
        } catch (Exception e)
        {
            
            throw e;
        }
	}

	@Override
	public Response getCheckServiceBilledOrNot(CouponTemplateMasterDto dto, Integer serviceId)
			throws ApplicationException {
		try
        {
			List<PatientCouponTemplateMapperDto> couponTemplateMasterDto = sessionFactory.getCurrentSession().createSQLQuery(
				"  select order_details_id as \"orderDetailsId\"  from billing.t_billing_details tbd where order_details_id in (select order_details_id  from t_order_details tod   " + 
				" inner join t_order_master tom on tom.order_id  = tod.order_id   " + 
				" where tom.patient_id =:patientId   and tom.visit_type_id  = 1  and tod.service_id =:serviceId  " +
				" and order_details_id  not in (select distinct order_details_id  from billing.t_patient_coupon_template_mapper tpctm where patient_id =:patientId  )) " )
					.setParameter("patientId", dto.getPatientId())
					.setParameter("serviceId",serviceId)
					.setResultTransformer(Transformers.aliasToBean(PatientCouponTemplateMapperDto.class)).list();
			
				return new Response<>(SUCCESS, SUCCESS_CODE, null, couponTemplateMasterDto, null);
		
        } catch (Exception e)
        {
            
            throw e;
        }
	}
}
