package com.param.billing.global.transaction.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.StatelessSession;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.billing.dto.InvestigationOrderDto;
import com.param.entity.model.master.FreeTextOrderedServices;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.FreeTextOrderedServicesDto;
import com.param.opd.coversheet.dto.MedicationListDto;
import com.param.opd.model.InvestigationModel;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
@Repository
@SuppressWarnings({ "unused", "unchecked","rawtypes" })
public class FreeTextInvestigationsDAOImpl extends GenericDao<FreeTextOrderedServices, Integer>  implements IFreeTextInvestigationsDAO,ICommonConstants{
	public FreeTextInvestigationsDAOImpl() {
		super(FreeTextOrderedServices.class);
	}

	@Override
	public Response saveFreeTextInvestigations(FreeTextOrderedServicesDto freeTextOrderedServicesDto)
			throws ApplicationException {
		try{
			FreeTextOrderedServices freeTextOrderedServices = new FreeTextOrderedServices();
				freeTextOrderedServices.setServiceText(freeTextOrderedServicesDto.getServiceText() != null ? freeTextOrderedServicesDto.getServiceText() : "");
				freeTextOrderedServices.setIsConsumed(false);
				freeTextOrderedServices.setDoctorId(freeTextOrderedServicesDto.getDoctorId());
				freeTextOrderedServices.setAdmissionId(freeTextOrderedServicesDto.getAdmissionId() != null ? freeTextOrderedServicesDto.getAdmissionId() : null);
				freeTextOrderedServices.setCreatedBy(freeTextOrderedServicesDto.getCreatedBy());
				freeTextOrderedServices.setPatientId(freeTextOrderedServicesDto.getPatientId());
				freeTextOrderedServices.setEncounterId(freeTextOrderedServicesDto.getEncounterId() != null ? freeTextOrderedServicesDto.getEncounterId() : null);
				freeTextOrderedServices.setOrderDetailsId(freeTextOrderedServicesDto.getOrderDetailsId());
				freeTextOrderedServices.setOrderId(freeTextOrderedServicesDto.getOrderId());
				freeTextOrderedServices.setUnitId(freeTextOrderedServicesDto.getUnitId());
				freeTextOrderedServices.setOrganizationId(freeTextOrderedServicesDto.getOrganizationId());
				freeTextOrderedServices.setStatus('A');
				freeTextOrderedServices.setCreatedDate(TimeZoneComponent.getDateByzone(freeTextOrderedServicesDto.getUnitId()));
				freeTextOrderedServices.setUpdatedBy(freeTextOrderedServicesDto.getUpdatedBy());
				freeTextOrderedServices.setUpdatedDate(TimeZoneComponent.getDateByzone(freeTextOrderedServicesDto.getUnitId()));
				save(freeTextOrderedServices);
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		}catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getFreeTextInvestigations(FreeTextOrderedServicesDto freeTextOrderedServicesDto)
			throws ApplicationException {
		try {
		String query="select " + 
				"	ftos.free_text_ordered_services_id as \"freeTextOrderedServicesId\", " + 
				"	ftos.service_text as \"serviceText\", " + 
				"	ftos.patient_id as \"patientId\", " + 
				"	ftos.encounter_id as \"encounterId\", " + 
				"	ftos.admission_id as \"admissionId\", " + 
				"	ftos.doctor_id as \"doctorId\", " + 
				"	ftos.order_id as \"orderId\", " + 
				"	ftos.order_details_id as \"orderDetailsId\", " + 
				"	ftos.is_consumed as \"isConsumed\", " + 
				"	ftos.status as \"status\", " + 
				"	ftos.unit_id as \"unitId\", " + 
				"	ftos.organization_id as \"organizationId\" " + 
				"from " + 
				"	public.t_free_text_ordered_services ftos " + 
				"where " + 
				"	ftos.organization_id =:orgId " + 
				"	and ftos.unit_id =:unitId " + 
				"	and ftos.status = 'A' " + 
				"	and ftos.encounter_id =:encounterId";
		
		List<MedicationListDto> list = sessionFactory.getCurrentSession().createSQLQuery(query)
				.setParameter("unitId",freeTextOrderedServicesDto.getUnitId())
				 .setParameter("encounterId", freeTextOrderedServicesDto.getEncounterId())
				.setParameter("orgId",freeTextOrderedServicesDto.getOrganizationId())
				.setResultTransformer(Transformers.aliasToBean(FreeTextOrderedServicesDto.class)).list();
		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS, list, null);	
		}catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);

	}

	@Override
	public Response searchFreeTextInvestigations(FreeTextOrderedServicesDto freeTextOrderedServicesDto)
			throws ApplicationException {
		try {
			String query="select " + 
			//"	ftos.free_text_ordered_services_id as \"freeTextOrderedServicesId\", " + 
					"	distinct ftos.service_text as \"serviceText\" " + 
					"from " + 
					"	public.t_free_text_ordered_services ftos " + 
					"where " + 
					"	ftos.organization_id =:orgId " + 
					"	and ftos.unit_id =:unitId " + 
			//"	and ftos.status = 'A' " + 
					"	and ((lower(ftos.service_text) like :searchKeyWord) or (ftos.service_text like :searchKeyWord ))" ;
			
			List<MedicationListDto> list = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("unitId",freeTextOrderedServicesDto.getUnitId())
					.setParameter("orgId",freeTextOrderedServicesDto.getOrganizationId())
					.setParameter("searchKeyWord",freeTextOrderedServicesDto.getSearchKeyword().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(FreeTextOrderedServicesDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS, list, null);	
			}catch (HibernateException he) {
				throw he;
			} catch (Exception e) {
				
				throw e;
			}
			//return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);

		}

	@Override
	public Response cancelFreeTextInvestigations(FreeTextOrderedServicesDto freeTextOrderedServicesDto)
			throws ApplicationException {
		try{
			FreeTextOrderedServices freeTextOrderedServices = findById(freeTextOrderedServicesDto.getFreeTextOrderedServicesId());
				freeTextOrderedServices.setUnitId(freeTextOrderedServicesDto.getUnitId());
				freeTextOrderedServices.setOrganizationId(freeTextOrderedServicesDto.getOrganizationId());
				freeTextOrderedServices.setStatus('I');
				freeTextOrderedServices.setUpdatedBy(freeTextOrderedServicesDto.getUpdatedBy());
				freeTextOrderedServices.setUpdatedDate(TimeZoneComponent.getDateByzone(freeTextOrderedServicesDto.getUnitId()));
				update(freeTextOrderedServices);
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		}catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}
}
