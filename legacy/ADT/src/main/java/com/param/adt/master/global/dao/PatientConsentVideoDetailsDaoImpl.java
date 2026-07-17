package com.param.adt.master.global.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.adt.common.ICommonConstants;
import com.param.adt.common.Response;
import com.param.adt.master.global.model.PatientConsentVideoDetails;
import com.param.global.billing.controller.BillingException;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.dto.PatientConsentMapperDto;
import com.param.global.dto.PatientConsentVideoDetailsDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class PatientConsentVideoDetailsDaoImpl extends GenericDao<PatientConsentVideoDetails, Integer> implements IPatientConsentVideoDetailsDao,ICommonConstants{



	@Autowired
	private SessionFactory sessionFactory;

	public PatientConsentVideoDetailsDaoImpl() {
		super(PatientConsentVideoDetails.class);
		// TODO Auto-generated constructor stub
	}
	@Override
	public Response uploadpatientConsentVideo(PatientConsentVideoDetailsDto dto) throws ApplicationException {
		try{
			Integer documentDetailsId = 0 ;
		if(dto.getVideoDetailsId()!=null &&dto.getVideoDetailsId()>0)
		{
			PatientConsentVideoDetails master = new PatientConsentVideoDetails();
			master.setVideoDetailsId(dto.getVideoDetailsId());
			master.setConsentTransactionId(dto.getConsentTransactionId());
			master.setOrganizationId(dto.getOrganizationId());
			master.setUnitId(dto.getUnitId());
			master.setStatus(ACTIVE);
			master.setUnitId(dto.getUnitId());
			master.setCreatedBy(dto.getCreatedBy());
			master.setCreatedDate(dto.getCreatedDate() != null && !dto.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId() != null ? dto.getUnitId() : 0));
			master.setUpdatedBy(dto.getUpdatedBy());
			master.setUpdatedDate(dto.getUpdatedDate() != null && !dto.getUpdatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId() != null ? dto.getUnitId() : 0));
			master.setVideoName(dto.getVideoName()!= null ? dto.getVideoName() : null);
			master.setVideoPath(dto.getVideoPath()!= null ? dto.getVideoPath()  : null);
			sessionFactory.getCurrentSession().update(master);
		}
		else {
			PatientConsentVideoDetails master = new PatientConsentVideoDetails();
			master.setConsentTransactionId(dto.getConsentTransactionId());
			master.setOrganizationId(dto.getOrganizationId());
			master.setUnitId(dto.getUnitId());
			master.setStatus(ACTIVE);
			master.setUnitId(dto.getUnitId());
			master.setCreatedBy(dto.getCreatedBy());
			master.setCreatedDate(dto.getCreatedDate() != null && !dto.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId() != null ? dto.getUnitId() : 0));
			master.setUpdatedBy(dto.getUpdatedBy());
			master.setUpdatedDate(dto.getUpdatedDate() != null && !dto.getUpdatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId() != null ? dto.getUnitId() : 0));
			master.setVideoName(dto.getVideoName()!= null ? dto.getVideoName() : null);
			master.setVideoPath(dto.getVideoPath()!= null ? dto.getVideoPath()  : null);
			documentDetailsId = (Integer) sessionFactory.getCurrentSession().save(master);
			master.setVideoDetailsId(documentDetailsId);
		}
		
		return new Response<>(SUCCESS, SUCCESS_CODE, null, null, documentDetailsId);
	}catch(HibernateException he){
		throw new BillingException(he.getMessage());
	}catch(Exception e){
		
		throw new BillingException(e.getMessage());
	}
}

	@Override
	public Response getConsentDocsListByMapperId(PatientConsentMapperDto dto)
			throws ApplicationException {
		try {
			List<PatientConsentVideoDetailsDto> consentMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CONSENT_VIDEO_LIST_BY_MAPPER_ID")
					.setParameter("consentTransactionId", dto.getConsentTransactionId())
					.setParameter("orgId", dto.getOrganizationId())
					.setParameter("unitId", dto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(PatientConsentVideoDetailsDto.class)).list();
			return new Response(SUCCESS, null, null, consentMasterDtosList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} finally {
		}
		return new Response(ERROR, null, null, null, null);
	}
	@Override
	public Response deactiveConsentVideo(PatientConsentVideoDetails dto) throws ApplicationException {
		try{	
			Session session=sessionFactory.getCurrentSession();
			Integer count=session.createSQLQuery(" update public.t_patient_consent_video_details set status='I' where video_details_id =:videoDetailsId ")
					.setParameter("videoDetailsId", dto.getVideoDetailsId())
					.executeUpdate();
			
			   return new Response(SUCCESS, SUCCESS_CODE, null, null, count);
			
		}catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response(ERROR, "400", COMMON_ERROR_MESSAGE, null, null);
	}

}
