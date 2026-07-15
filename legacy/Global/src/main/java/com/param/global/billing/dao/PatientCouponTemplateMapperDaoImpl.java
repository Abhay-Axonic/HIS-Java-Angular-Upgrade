package com.param.global.billing.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.billing.global.model.PatientCouponTemplateMapper;
import com.param.global.billing.controller.BillingException;
import com.param.global.billing.dto.CouponTemplateConsumeDetailsDto;
import com.param.global.billing.dto.PatientCouponTemplateMapperDto;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class PatientCouponTemplateMapperDaoImpl extends GenericDao<PatientCouponTemplateMapper, Integer> implements IPatientCouponTemplateMapperDao, ICommonConstants{

	public PatientCouponTemplateMapperDaoImpl() {
		super(PatientCouponTemplateMapper.class);
	}
	
	@Autowired
	private SessionFactory SessionFactory;

	@Override
	public Response savePatientCouponTemplateMapper(List<PatientCouponTemplateMapperDto> list) {
		try{
			
			List<PatientCouponTemplateMapper> saveList=new ArrayList<PatientCouponTemplateMapper>(); 
			
			for (PatientCouponTemplateMapperDto dto : list) {
				
				PatientCouponTemplateMapper masterD = new PatientCouponTemplateMapper();
				masterD.setCouponTemplateMasterId(dto.getCouponTemplateMasterId() != null ? dto.getCouponTemplateMasterId() : null);
				masterD.setConsume(false);
				masterD.setCouponTemplateDetailsId(dto.getCouponTemplateDetailsId() != null ? dto.getCouponTemplateDetailsId(): null );
				masterD.setPatientId(dto.getPatientId());
				masterD.setPercentage(dto.getPercentage());
				masterD.setAmount(dto.getAmount());
				masterD.setServiceId(dto.getServiceId() != null ? dto.getServiceId() : null);
				masterD.setSpecialityId(dto.getSpecialityId()!= null ? dto.getSpecialityId() : null);
				masterD.setSubSpecialityId(dto.getSubSpecialityId()!= null ? dto.getSubSpecialityId() : null);
				masterD.setCouponTemplateDescription(dto.getCouponTemplateDescription());
				masterD.setCreatedBy(dto.getCreatedBy());
				masterD.setCreatedDate(TimeZoneComponent.getDateByzone(dto.getUnitId()));
				masterD.setUnitId(dto.getUnitId());
				masterD.setOrganizationId(dto.getOrganizationId());
				masterD.setUpdatedBy(dto.getUpdatedBy());
				masterD.setExpireDays(dto.getExpireDays());
				masterD.setUpdatedDate(TimeZoneComponent.getDateByzone(dto.getUnitId()));
				masterD.setConsumption(dto.getConsumption()!= null ? dto.getConsumption() : 'N');
				masterD.setStatus(ACTIVE);
				masterD.setOrderDetailsId(dto.getOrderDetailsId() != null ? dto.getOrderDetailsId() : null);
				masterD = save(masterD);
				saveList.add(masterD);
			}
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, saveList, null);
		}catch(HibernateException he){
			throw new BillingException(he.getMessage());
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	public Response forceStopPatientCouponTemplate(PatientCouponTemplateMapperDto dto) throws ApplicationException {
		try {
			
			SessionFactory.getCurrentSession().createSQLQuery("UPDATE billing.t_patient_coupon_template_mapper set status='"+dto.getStatus()+"' WHERE patient_coupon_template_mapper_id =:id  ")
			.setParameter("id", dto.getPatientCouponTemplateMapperId())
			.executeUpdate();
			return new Response(SUCCESS, SUCCESS_CODE, null, null, null);

		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response updateCouponCode(List<PatientCouponTemplateMapper> saveList) throws ApplicationException {
	try {
			
		
		for (PatientCouponTemplateMapper patientCouponTemplateMapper : saveList) {
			SessionFactory.getCurrentSession().createSQLQuery("UPDATE billing.t_patient_coupon_template_mapper set coupon_code='"+patientCouponTemplateMapper.getCouponCode()+"' WHERE patient_coupon_template_mapper_id =:id  ")
			.setParameter("id", patientCouponTemplateMapper.getPatientCouponTemplateMapperId())
			.executeUpdate();
		}
		
		
			return new Response(SUCCESS, SUCCESS_CODE, null, null, null);

		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response getMappedPatientCouponTemplate(PatientCouponTemplateMapperDto dto) throws ApplicationException {
		try
        {
			List<PatientCouponTemplateMapperDto> list = sessionFactory.getCurrentSession().createSQLQuery(
				"  select " + 
				"	distinct mctd.patient_coupon_template_mapper_id as \"patientCouponTemplateMapperId\", " + 
				"	mctd.patient_id as \"patientId\", " + 
				"	mctd.coupon_code as \"couponCode\", " + 
				"	mctd.percentage as \"percentage\", " + 
				"	mctd.amount as \"amount\", " + 
				"	mctd.expiry_days as \"expireDays\", " + 
				"	mctd.consume as \"consume\", " + 
				"	mctd.coupon_template_description as \"couponTemplateDescription\", " + 
				"	msm.speciality_name  as \"specialityName\", " + 
				"	mssm.sub_speciality_name  as \"subSpecialityName\", " + 
				"	ser.service_standard_name  as \"serviceName\", " + 
				"	mctd.status  as \"status\","
				+ " mbm.bill_number  as \"billNumber\","
				+ " mctm.coupon_template_name as \"couponTemplateName\" " + 
				"	from billing.t_patient_coupon_template_mapper mctd  " + 
				"	inner join doctor.m_speciality_master msm  on msm.speciality_id = mctd.speciality_id  "
				+" inner join  billing.m_coupon_template_master mctm on mctm.coupon_template_master_id = mctd.coupon_template_master_id " + 
				"	left join doctor.m_sub_speciality_master mssm  on mssm.sub_speciality_id  = mctd.sub_speciality_id  " + 
				"	left join service.m_service_master ser on ser.service_master_id  = mctd.service_id "
				+ " left join billing.t_coupon_template_consume_details tctcd on mctd.patient_coupon_template_mapper_id  = tctcd.patient_coupon_template_mapper_id " + 
				"  left join billing.m_billing_master mbm on tctcd.bill_id = mbm.billing_master_id  " + 
				"	where mctd.patient_id =:patientId  and mctd.unit_id =:unitId  and mctd.organization_id =:organizationId ")
					.setParameter("patientId", dto.getPatientId())
					.setParameter("unitId", dto.getUnitId())
					.setParameter("organizationId", dto.getOrganizationId())
				.setResultTransformer(Transformers.aliasToBean(PatientCouponTemplateMapperDto.class)).list();
			
				return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		
        } catch (Exception e)
        {
            
            throw e;
        }
	}

	@Override
	public Response counsumCouponTemplate(PatientCouponTemplateMapperDto dto) throws ApplicationException {
		try
        {
			PatientCouponTemplateMapperDto obj = (PatientCouponTemplateMapperDto) sessionFactory.getCurrentSession().createSQLQuery(
				" select " + 
				"	mctd.patient_coupon_template_mapper_id as \"patientCouponTemplateMapperId\", " + 
				"	mctd.percentage as \"percentage\", " + 
				"	mctd.amount as \"amount\", " + 
				"	mctd.expiry_days as \"expireDays\", " + 
				"	mctd.consume as \"consume\", " + 
				"	mctd.status  as \"status\","
				+ " mctd.speciality_id  as \"specialityId\"," + 
				"	mctd.sub_speciality_id  as \"subSpecialityId\","
				+ " to_char(mctd.created_date ,'YYYY-MM-DD')  as \"createdDate\", " + 
				"	mctd.service_id  as \"serviceId\","
				+ " mctd.patient_id as \"patientId\" ,"
				+ " mctd.consumption as \"consumption\"  " + 
				"	from billing.t_patient_coupon_template_mapper mctd  " + 
				"	inner join doctor.m_speciality_master msm  on msm.speciality_id = mctd.speciality_id  "+
				"	left join doctor.m_sub_speciality_master mssm  on mssm.sub_speciality_id  = mctd.sub_speciality_id  " + 
				"	left join service.m_service_master ser on ser.service_master_id  = mctd.service_id  " + 
				"	where mctd.coupon_code =:couponCode order by 1  desc limit 1 ")
					.setParameter("couponCode", dto.getCouponCode())
				.setResultTransformer(Transformers.aliasToBean(PatientCouponTemplateMapperDto.class)).uniqueResult();
			
				return new Response<>(SUCCESS, SUCCESS_CODE, null, null, obj);
		
        } catch (Exception e)
        {
            
            throw e;
        }
	}

	@Override
	public Response getServiceDetailsById(PatientCouponTemplateMapperDto dto) throws ApplicationException {
		try
        {
			PatientCouponTemplateMapperDto obj = (PatientCouponTemplateMapperDto) sessionFactory.getCurrentSession().createSQLQuery(
				" select "  + 
				"	mctd.speciality_id as \"specialityId\", "  + 
				"	mctd.sub_speciality_id  as \"subSpecialityId\", "  + 
				"	mctd.service_master_id as \"serviceId\" "  + 
				"	from service.m_service_master  mctd  "  + 
				"	inner join doctor.m_speciality_master msm  on msm.speciality_id = mctd.speciality_id  "  + 
				"	left join doctor.m_sub_speciality_master mssm  on mssm.sub_speciality_id  = mctd.sub_speciality_id  "  + 
				"	where mctd.service_master_id  =:serviceId  "  + "	order by 1  desc limit 1  ").setParameter("serviceId",dto.getServiceId())
				.setResultTransformer(Transformers.aliasToBean(PatientCouponTemplateMapperDto.class)).uniqueResult();
			
				return new Response<>(SUCCESS, SUCCESS_CODE, null, null, obj);
		
        } catch (Exception e)
        {
            
            throw e;
        }
	}

	@Override
	public Response updateToCousumeCoupon(CouponTemplateConsumeDetailsDto couponTemplateConsumeDetailsDto)
			throws ApplicationException {
		try {
			
			SessionFactory.getCurrentSession().createSQLQuery("UPDATE billing.t_patient_coupon_template_mapper set consume= true WHERE patient_coupon_template_mapper_id =:id  ")
			.setParameter("id", couponTemplateConsumeDetailsDto.getPatientCouponTemplateMapperId())
			.executeUpdate();
			return new Response(SUCCESS, SUCCESS_CODE, null, null, null);

		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response getpatientCouponMapperByOrderId(List<Integer> orderIdList) throws ApplicationException {
		 try{
				List<CouponTemplateConsumeDetailsDto> obj =  sessionFactory.getCurrentSession().createSQLQuery(
					" select patient_coupon_template_mapper_id as \"patientCouponTemplateMapperId\", " + 
					"order_details_id  as \"orderDetailsId\", " + 
					"ord_discount as \"ordDiscount\" , " + 
					"service_id as \"serviceId\" from t_order_details  where patient_coupon_template_mapper_id is not null and order_id in("+orderIdList.toString().replaceAll("\\[","").replaceAll("\\]","")+")")
					.setResultTransformer(Transformers.aliasToBean(CouponTemplateConsumeDetailsDto.class)).list();
				
					return new Response<>(SUCCESS, SUCCESS_CODE, null, obj, null);
			
	        } catch (Exception e)
	        {
	            
	            throw e;
	        }
	}

	@Override
	public Response getOrderDetailsAndPatientCoupon(Integer billingMasterId) throws ApplicationException {
		 try{
				List<CouponTemplateConsumeDetailsDto> obj =  sessionFactory.getCurrentSession().createSQLQuery(
					"  select patient_coupon_template_mapper_id as \"patientCouponTemplateMapperId\", " + 
					"order_details_id  as \"orderDetailsId\", " + 
					"consume_amount as \"consumeAmount\" from billing.t_coupon_template_consume_details  where  bill_id =:billingMasterId ")
						.setParameter("billingMasterId", billingMasterId)
					.setResultTransformer(Transformers.aliasToBean(CouponTemplateConsumeDetailsDto.class)).list();
				
					return new Response<>(SUCCESS, SUCCESS_CODE, null, obj, null);
			
	        } catch (Exception e)
	        {
	            
	            throw e;
	        }
	}

	@Override
	public Response updateOrderDetailsAndPatientCoupon(CouponTemplateConsumeDetailsDto couponTemplateConsumeDetailsDto)
			throws ApplicationException {
		try {
			
			
			SessionFactory.getCurrentSession().createSQLQuery("	update t_order_details set ord_discount = ord_discount - "+couponTemplateConsumeDetailsDto.getConsumeAmount()+" , "
					+ "self_payable = self_payable + "+couponTemplateConsumeDetailsDto.getConsumeAmount()+","
					+ "hospital_share =hospital_share + "+couponTemplateConsumeDetailsDto.getConsumeAmount()+", "
					+ "net_amount  = net_amount + "+couponTemplateConsumeDetailsDto.getConsumeAmount()
					+ " WHERE order_details_id =:id  ")
			.setParameter("id", couponTemplateConsumeDetailsDto.getOrderDetailsId())
			.executeUpdate();
			
			SessionFactory.getCurrentSession().createSQLQuery("UPDATE billing.t_patient_coupon_template_mapper set status='I' WHERE patient_coupon_template_mapper_id =:id  ")
			.setParameter("id", couponTemplateConsumeDetailsDto.getPatientCouponTemplateMapperId())
			.executeUpdate();
			return new Response(SUCCESS, SUCCESS_CODE, null, null, null);

		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response checkToCousumeCoupon(Integer encounterId, Integer patientCouponTemplateMapperId)
			throws ApplicationException {
		 try{
				List<CouponTemplateConsumeDetailsDto> obj =  sessionFactory.getCurrentSession().createSQLQuery(
					" select\r\n" + 
					"	patient_coupon_template_mapper_id as \"patientCouponTemplateMapperId\"," + 
					"	order_details_id as \"orderDetailsId\"," + 
					"	ord_discount as \"ordDiscount\" ," + 
					"	service_id as \"serviceId\" " + 
					" from " + 
					"	t_order_details tod " + 
					"	inner join t_order_master tom on tod.order_id = tom.order_id " + 
					" where " + 
					"	tod.patient_coupon_template_mapper_id =:patientCouponTemplateMapperId " +
					"	and tom.encounter_id  <>:encounterId ")
						.setParameter("encounterId",encounterId)
						.setParameter("patientCouponTemplateMapperId", patientCouponTemplateMapperId)
					.setResultTransformer(Transformers.aliasToBean(CouponTemplateConsumeDetailsDto.class)).list();
				
					return new Response<>(SUCCESS, SUCCESS_CODE, null, obj, null);
			
	        } catch (Exception e)
	        {
	            
	            throw e;
	        }
	}

	@Override
	public Response checkToCousumeCouponPerDay(Integer patientId, Integer unitId, Integer serviceId) throws ApplicationException {
		 try{
			
			 Date date = TimeZoneComponent.getDateByzone(unitId);
				List<CouponTemplateConsumeDetailsDto> obj =  sessionFactory.getCurrentSession().createSQLQuery(
					" select service_id as \"serviceId\" from billing.t_coupon_template_consume_details"
					+ " where to_char(created_date,'YYYY-MM-DD') = '"+GlobalCommonDateUtils.getStringDate(date,"yyyy-MM-dd")+"' "
					+ " and patient_coupon_template_mapper_id in( select patient_coupon_template_mapper_id from billing.t_patient_coupon_template_mapper "
					+ " where patient_id =:patientId  and  service_id =:serviceId ) ")
						.setParameter("patientId", patientId)
						.setParameter("serviceId", serviceId)
					.setResultTransformer(Transformers.aliasToBean(CouponTemplateConsumeDetailsDto.class)).list();
				
					return new Response<>(SUCCESS, SUCCESS_CODE, null, obj, null);
			
	        } catch (Exception e)
	        {
	            
	            throw e;
	        }
	
	}
}
