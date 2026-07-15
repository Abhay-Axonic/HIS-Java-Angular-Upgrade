package com.param.opd.coversheet.dao;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.persistence.Column;

import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.CoversheetTemplateMasterDto;
import com.param.opd.common.CommonDateUtils;
import com.param.opd.coversheet.dto.ConsultDoctorDto;
import com.param.opd.coversheet.dto.PatientTravelHistoryDetailsDto;
import com.param.opd.coversheet.model.PatientTravelHistoryDetails;
import com.param.opd.model.ConsultDoctor;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({ "unused", "unchecked","rawtypes" })
public class ConsultDoctorDaoImpl extends GenericDao<ConsultDoctor, Integer> implements IConsultDoctorDao,ICommonConstants{
	final static Logger logger = Logger.getLogger(ConsultDoctorDaoImpl.class);


	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	DozerBeanMapper mapper;
	
	public ConsultDoctorDaoImpl() {
		super(ConsultDoctor.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Response saveConsultDoctor(ConsultDoctorDto consultDoctorDto) throws ApplicationException {
		try {
			ConsultDoctor consultDoctor = mapper.map(consultDoctorDto, ConsultDoctor.class,
					"ConsultDoctorDto_to_ConsultDoctor");
			consultDoctor.setCreatedDate(TimeZoneComponent.getDateByzone(consultDoctorDto.getUnitId()));
			consultDoctor.setUpdatedDate(TimeZoneComponent.getDateByzone(consultDoctorDto.getUnitId()));
			consultDoctor = save(consultDoctor);
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		} catch (HibernateException he) {
		logger.error("saveConsultDoctor failed", he);
		} catch (Exception e) {
		logger.error("saveConsultDoctor failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response consultDoctorListByEncounter(Integer encounterId) throws ApplicationException {
		try {
			List<ConsultDoctorDto> consultDoctorDtosList = sessionFactory.getCurrentSession().getNamedQuery("CONSULT_DOCTOR_LIST_BY_ENCOUNTER_ID")
					.setParameter("encounterId", encounterId)
					.setResultTransformer(Transformers.aliasToBean(ConsultDoctorDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, consultDoctorDtosList, null);
		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("consultDoctorListByEncounter failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response inactiveConsultDoctor(ConsultDoctorDto consultDoctorDto) throws ApplicationException {
		try {
			/*sessionFactory.getCurrentSession().createQuery("UPDATE ConsultDoctor set "
					+ "status='I',"
					+ "updatedBy=:updatedBy,"
					+ "updatedDate=:updatedDate "
					+ "WHERE consultDoctorId=:consultDoctorId ")
					.setParameter("consultDoctorId", consultDoctorDto.getConsultDoctorId())
					.setParameter("updatedBy",consultDoctorDto.getUpdatedBy())
					.setDate("updatedDate", CommonDateUtils.getDate(consultDoctorDto.getUpdatedDate(),"dd-M-yyyy HH:mm:ss"))
					.setResultTransformer(Transformers.aliasToBean(ConsultDoctorDto.class)).list();*/
			sessionFactory.getCurrentSession().createSQLQuery("UPDATE opd.t_consult_doctor set status='I' WHERE consult_doctor_id =:id ")
			.setParameter("id", consultDoctorDto.getConsultDoctorId())
			.executeUpdate();
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("inactiveConsultDoctor failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getConsultationPatientListForDocDesk(ConsultDoctorDto ConsultDoctorDto)
			throws ApplicationException {
		try {
			
			
			Integer count=0;
			List<ConsultDoctorDto> consultDoctorDtosList  = null;
			List<ConsultDoctorDto> consultDoctorDtosList1 = null;
			
			if (ConsultDoctorDto.getIsRMO()==null) {
				
				consultDoctorDtosList = sessionFactory.getCurrentSession().getNamedQuery("PATIENT_LIST_BY_DOC_ORG_UNIT_ID_FOR_DOC_DESK")
						.setParameter("orgId", ConsultDoctorDto.getOrganizationId())
						.setParameter("docId", ConsultDoctorDto.getRefToDoctorId())
						.setFirstResult(ConsultDoctorDto.getOffset() != null ? ConsultDoctorDto.getOffset()  : 0)
						.setMaxResults(ConsultDoctorDto.getNoOfRecordsPerPage() != null ? ConsultDoctorDto.getNoOfRecordsPerPage()  : 10)
						.setResultTransformer(Transformers.aliasToBean(ConsultDoctorDto.class)).list();
				
				consultDoctorDtosList1 = sessionFactory.getCurrentSession().getNamedQuery("PATIENT_LIST_BY_DOC_ORG_UNIT_ID_FOR_DOC_DESK")
						.setParameter("orgId", ConsultDoctorDto.getOrganizationId())
						.setParameter("docId", ConsultDoctorDto.getRefToDoctorId())
						.setResultTransformer(Transformers.aliasToBean(ConsultDoctorDto.class)).list();
				
			}else{
				if(ConsultDoctorDto.getDoctorList()!=null &&  ConsultDoctorDto.getDoctorList().size()>0) {
				consultDoctorDtosList = sessionFactory.getCurrentSession().getNamedQuery("PATIENT_LIST_BY_DOC_ORG_UNIT_ID_FOR_DOC_DESK_BY_RMO_MAPPED_DOCTOR_LIST")
						.setParameter("orgId", ConsultDoctorDto.getOrganizationId())
						.setParameterList("doctorIdList", ConsultDoctorDto.getDoctorList())
						.setFirstResult(ConsultDoctorDto.getOffset() != null ? ConsultDoctorDto.getOffset()  : 0)
						.setMaxResults(ConsultDoctorDto.getNoOfRecordsPerPage() != null ? ConsultDoctorDto.getNoOfRecordsPerPage()  : 10)
						.setResultTransformer(Transformers.aliasToBean(ConsultDoctorDto.class)).list();
				
				consultDoctorDtosList1 = sessionFactory.getCurrentSession().getNamedQuery("PATIENT_LIST_BY_DOC_ORG_UNIT_ID_FOR_DOC_DESK_BY_RMO_MAPPED_DOCTOR_LIST")
						.setParameter("orgId", ConsultDoctorDto.getOrganizationId())
						.setParameterList("doctorIdList", ConsultDoctorDto.getDoctorList())
						.setResultTransformer(Transformers.aliasToBean(ConsultDoctorDto.class)).list();
				
				}
			}
			
			 if(consultDoctorDtosList1 != null && consultDoctorDtosList1.size()>0) {
		    	  count = consultDoctorDtosList1.size();
		      }
			 
			return new Response(SUCCESS, null, null, consultDoctorDtosList, count);
	
		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("getConsultationPatientListForDocDesk failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response consultDoctorListByAdmission(ConsultDoctorDto consultDoctorDto) throws ApplicationException {
		try {
			List<ConsultDoctorDto> consultDoctorDtosList = sessionFactory.getCurrentSession().getNamedQuery("GET_CONSULT_DOCTOR_LIST_BY_ADMISSION_ID")
//					.setParameter("orgId", ConsultDoctorDto.getOrganizationId())
//					.setParameter("unitId", ConsultDoctorDto.getUnitId())
					.setParameter("admissionId", consultDoctorDto.getAdmissionId())
					.setResultTransformer(Transformers.aliasToBean(ConsultDoctorDto.class)).list();

			if (consultDoctorDtosList != null && !consultDoctorDtosList.isEmpty() && consultDoctorDto.getPatientId() != null
			    && consultDoctorDto.getUnitId()!=null && consultDoctorDto.getOrganizationId()!=null) {
				List<Integer> recordIds = consultDoctorDtosList.stream()
						.map(dto -> dto.getConsultDoctorId())
						.collect(Collectors.toList());
				List<ConsultDoctorDto> consultDoctorDtosListForWorkBench =
						sessionFactory.getCurrentSession()
								.getNamedQuery("GET_WORKBENCH_DETAILS_FOR_CONSULT")
								.setParameterList("recordIds", recordIds)
								.setParameter("patientId", consultDoctorDto.getPatientId())
								.setParameter("unitId", consultDoctorDto.getUnitId())
								.setParameter("organizationId", consultDoctorDto.getOrganizationId())
								.setResultTransformer(Transformers.aliasToBean(ConsultDoctorDto.class))
								.list();
				if (consultDoctorDtosListForWorkBench != null && !consultDoctorDtosListForWorkBench.isEmpty()) {
					Map<Integer, ConsultDoctorDto> workbenchMap =
							consultDoctorDtosListForWorkBench.stream()
									.filter(w -> w.getConsultDoctorId() != null)
									.collect(Collectors.toMap(
											ConsultDoctorDto::getConsultDoctorId,
											Function.identity(),
											(a, b) -> a   // in case of duplicate keys
									));
					consultDoctorDtosList.forEach(dto -> {
						ConsultDoctorDto wbDto = workbenchMap.get(dto.getConsultDoctorId());

						if (wbDto != null) {
							dto.setPatientWorkbenchId(wbDto.getPatientWorkbenchId());
							dto.setResultId(wbDto.getResultId());
							dto.setWorkbenchAddedDate(wbDto.getWorkbenchAddedDate());
							dto.setWorkbenchRemark(wbDto.getWorkbenchRemark());
						}
					});
				}
			}
			return new Response(SUCCESS, SUCCESS_CODE, null, consultDoctorDtosList, null);
		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("consultDoctorListByAdmission failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response updateActionMasterStatus(ConsultDoctorDto consultDoctorDto) throws ApplicationException {
		try {
			ConsultDoctor consultDoctor=findById(consultDoctorDto.getConsultDoctorId());
			consultDoctor.setReferredStatus(consultDoctorDto.getReferredStatus());
			consultDoctor.setUpdatedBy(consultDoctorDto.getUpdatedBy());
			consultDoctor.setUpdatedDate(GlobalCommonDateUtils.getDate(consultDoctorDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			
			update(consultDoctor);			
		return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		
	} catch (HibernateException he) {
	logger.error("updateActionMasterStatus failed", he);
	} catch (Exception e) {
	logger.error("updateActionMasterStatus failed", e);
	}
return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

	@Override
	public Response consultDoctorListForBookView(ConsultDoctorDto consultDoctorDto) throws ApplicationException {
		try {
			List<ConsultDoctorDto> consultDoctorDtosList = sessionFactory.getCurrentSession().getNamedQuery("CONSULT_DOCTOR_LIST_FOR_BOOK_VIEW")
					.setParameter("fromDate", consultDoctorDto.getFromDate())
					.setParameter("toDate", consultDoctorDto.getToDate())
					.setParameter("patientId", consultDoctorDto.getPatientId())
					.setResultTransformer(Transformers.aliasToBean(ConsultDoctorDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, consultDoctorDtosList, null);
		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("consultDoctorListForBookView failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}


	@Override
	public Response updateConsultDoctorNotes(ConsultDoctorDto consultDoctorDto) throws ApplicationException {
		try {
			
			Session session=sessionFactory.getCurrentSession();
			session.createSQLQuery(" update opd.t_consult_doctor set notes= '"+consultDoctorDto.getNotes()+"' "
			+ " where consult_doctor_id="+consultDoctorDto.getConsultDoctorId())
			.executeUpdate();
			
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
			
			} catch (HibernateException he) {
			logger.error("updateConsultDoctorNotes failed", he);
			} catch (Exception e) {
			logger.error("updateConsultDoctorNotes failed", e);
			}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response savePatientConsultDoctor(List<ConsultDoctorDto> list, CoversheetTemplateMasterDto resDto)
			throws ApplicationException {
	try{
			
			for (ConsultDoctorDto dto : list){
				if(dto.getCovTemplateId() == null){
					ConsultDoctor consultDoctor=new ConsultDoctor();
					consultDoctor.setRefByDoctorId(dto.getRefByDoctorId());
					consultDoctor.setRefBySpecialityId(dto.getRefBySpecialityId());
					consultDoctor.setNotes(dto.getNotes());
					consultDoctor.setReferredStatus(dto.getReferredStatus());
					consultDoctor.setOutsideHosDocId(dto.getOutsideHosDocId());
					consultDoctor.setOutsideHospitalId(dto.getOutsideHospitalId());
					consultDoctor.setReferredUnitId(dto.getReferredUnitId());
					consultDoctor.setRefToDoctorId(dto.getRefToDoctorId());
					consultDoctor.setRefToSpecialityId(dto.getRefToSpecialityId());
					consultDoctor.setPriority(dto.getPriority());
					consultDoctor.setVisitTypeId(dto.getVisitTypeId());
//					if(dto.getEncounterId() != null){
//						consultDoctor.setEncounterId(resDto.getEncounterId());
//					}else {
//						consultDoctor.setAdmissionId(resDto.getAdmissionId());
//					}
					consultDoctor.setCreatedBy(dto.getCreatedBy());
					consultDoctor.setUpdatedBy(dto.getUpdatedBy());
					consultDoctor.setUpdatedDate(TimeZoneComponent.getDateByzone(dto.getUnitId()));
					consultDoctor.setStatus(dto.getStatus());
					consultDoctor.setOrganizationId(dto.getOrganizationId());
					consultDoctor.setUnitId(dto.getUnitId());
					consultDoctor.setCovTemplateId(resDto.getCovTemplateId());
//					consultDoctor.setPatientId(dto.getPatientId());
					save(consultDoctor);
				}/*else {
					ConsultDoctor consultDoctor= findById(resDto.getCovTemplateId());
					consultDoctor.setRefByDoctorId(dto.getRefByDoctorId());
					consultDoctor.setRefBySpecialityId(dto.getRefBySpecialityId());
					consultDoctor.setNotes(dto.getNotes());
					consultDoctor.setReferredStatus(dto.getReferredStatus());
					consultDoctor.setOutsideHosDocId(dto.getOutsideHosDocId());
					consultDoctor.setOutsideHospitalId(dto.getOutsideHospitalId());
					consultDoctor.setReferredUnitId(dto.getReferredUnitId());
					consultDoctor.setRefToDoctorId(dto.getRefToDoctorId());
					consultDoctor.setRefToSpecialityId(dto.getRefToSpecialityId());
					consultDoctor.setPriority(dto.getPriority());
					consultDoctor.setVisitTypeId(dto.getVisitTypeId());
//					if(resDto.getEncounterId() != null){
//						consultDoctor.setEncounterId(resDto.getEncounterId());
//					}else {
//						consultDoctor.setAdmissionId(resDto.getAdmissionId());
//					}
					consultDoctor.setCreatedBy(resDto.getCreatedBy());
					consultDoctor.setUpdatedBy(resDto.getUpdatedBy());
					consultDoctor.setUpdatedDate(TimeZoneComponent.getDateByzone(resDto.getUnitId()));
					consultDoctor.setStatus(resDto.getStatus());
					consultDoctor.setOrganizationId(resDto.getOrganizationId());
					consultDoctor.setUnitId(resDto.getUnitId());
					consultDoctor.setPatientId(resDto.getPatientId());
					update(consultDoctor);
				}*/
				}
				
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response updateCovTempPatientConsultDoctor(List<ConsultDoctorDto> list,
			CoversheetTemplateMasterDto coversheetTemplateMasterDto) {
try{
			
			for (ConsultDoctorDto dto : list){
				if(dto.getCovTemplateId() == null){
					
					ConsultDoctor consultDoctor= findById(coversheetTemplateMasterDto.getCovTemplateId());
					consultDoctor.setRefByDoctorId(dto.getRefByDoctorId());
					consultDoctor.setRefBySpecialityId(dto.getRefBySpecialityId());
					consultDoctor.setNotes(dto.getNotes());
					consultDoctor.setReferredStatus(dto.getReferredStatus());
					consultDoctor.setOutsideHosDocId(dto.getOutsideHosDocId());
					consultDoctor.setOutsideHospitalId(dto.getOutsideHospitalId());
					consultDoctor.setReferredUnitId(dto.getReferredUnitId());
					consultDoctor.setRefToDoctorId(dto.getRefToDoctorId());
					consultDoctor.setRefToSpecialityId(dto.getRefToSpecialityId());
					consultDoctor.setPriority(dto.getPriority());
					consultDoctor.setVisitTypeId(dto.getVisitTypeId());
//					if(resDto.getEncounterId() != null){
//						consultDoctor.setEncounterId(resDto.getEncounterId());
//					}else {
//						consultDoctor.setAdmissionId(resDto.getAdmissionId());
//					}
					consultDoctor.setCreatedBy(coversheetTemplateMasterDto.getCreatedBy());
					consultDoctor.setUpdatedBy(coversheetTemplateMasterDto.getUpdatedBy());
					consultDoctor.setUpdatedDate(TimeZoneComponent.getDateByzone(coversheetTemplateMasterDto.getUnitId()));
					consultDoctor.setStatus(coversheetTemplateMasterDto.getStatus());
					consultDoctor.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
					consultDoctor.setUnitId(coversheetTemplateMasterDto.getUnitId());
					consultDoctor.setPatientId(coversheetTemplateMasterDto.getPatientId());
					update(consultDoctor);
				}
				}
				
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		
		}catch(Exception e){
			
			throw e;
		}
	}


	
	
	

}
