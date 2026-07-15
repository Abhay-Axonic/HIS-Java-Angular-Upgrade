package com.param.opd.coversheet.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.PatientRegistrationDto;
import com.param.opd.coversheet.dto.NewCovDoctorNotesDto;
import com.param.opd.model.NewCovDoctorNotes;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class NewCovDoctorNotesDaoImpl extends GenericDao<NewCovDoctorNotes,  Integer> implements INewCovDoctorNotesDao, ICommonConstants{
	final static Logger logger = Logger.getLogger(NewCovDoctorNotesDaoImpl.class);


	public NewCovDoctorNotesDaoImpl() {
		super(NewCovDoctorNotes.class);
	}

	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	public Response saveNewCovDoctorNotes(NewCovDoctorNotesDto newCovDoctorNotesDto) throws ApplicationException {
		try {
			 NewCovDoctorNotes newCovDoctorNotes=new NewCovDoctorNotes();
			 newCovDoctorNotes.setDoctorNotes(newCovDoctorNotesDto.getDoctorNotes());
			 newCovDoctorNotes.setPatientId(newCovDoctorNotesDto.getPatientId());
			 if(newCovDoctorNotesDto.getEncounterId()!=null){
			 newCovDoctorNotes.setEncounterId(newCovDoctorNotesDto.getEncounterId());
			 }else{
				 newCovDoctorNotes.setAdmissionId(newCovDoctorNotesDto.getAdmissionId());
			 }
			 newCovDoctorNotes.setDoctorId(newCovDoctorNotesDto.getDoctorId());
			 newCovDoctorNotes.setOrganizationId(newCovDoctorNotesDto.getOrganizationId());
			 newCovDoctorNotes.setUnitId(newCovDoctorNotesDto.getUnitId());
			 newCovDoctorNotes.setStatus(newCovDoctorNotesDto.getStatus());
			 newCovDoctorNotes.setCreatedBy(newCovDoctorNotesDto.getCreatedBy());
			 newCovDoctorNotes.setCreatedDate(GlobalCommonDateUtils.getDate(newCovDoctorNotesDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			 newCovDoctorNotes.setUpdatedBy(newCovDoctorNotesDto.getUpdatedBy());
			 newCovDoctorNotes.setUpdatedDate(GlobalCommonDateUtils.getDate(newCovDoctorNotesDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			 save(newCovDoctorNotes);
		
		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		
		} catch (HibernateException he) {
		logger.error("saveNewCovDoctorNotes failed", he);
		} catch (Exception e) {
		logger.error("saveNewCovDoctorNotes failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateNewCovDoctorNotes(NewCovDoctorNotesDto newCovDoctorNotesDto) throws ApplicationException {
		try {
			 NewCovDoctorNotes newCovDoctorNotes=findById(newCovDoctorNotesDto.getNewCovDoctorNotesId());
			 newCovDoctorNotes.setDoctorNotes(newCovDoctorNotesDto.getDoctorNotes());
			 newCovDoctorNotes.setPatientId(newCovDoctorNotesDto.getPatientId());
			 if(newCovDoctorNotesDto.getEncounterId()!=null){
				 newCovDoctorNotes.setEncounterId(newCovDoctorNotesDto.getEncounterId());
				 }else{
					 newCovDoctorNotes.setAdmissionId(newCovDoctorNotesDto.getAdmissionId());
				 }			 newCovDoctorNotes.setDoctorId(newCovDoctorNotesDto.getDoctorId());
			 newCovDoctorNotes.setOrganizationId(newCovDoctorNotesDto.getOrganizationId());
			 newCovDoctorNotes.setUnitId(newCovDoctorNotesDto.getUnitId());
			 newCovDoctorNotes.setUpdatedBy(newCovDoctorNotesDto.getUpdatedBy());
			 newCovDoctorNotes.setUpdatedDate(GlobalCommonDateUtils.getDate(newCovDoctorNotesDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			 update(newCovDoctorNotes);
		
		return new Response(SUCCESS, SUCCESS_MESSAGE, COMMON_UPDATE, null, null);
		
		} catch (HibernateException he) {
		logger.error("updateNewCovDoctorNotes failed", he);
		} catch (Exception e) {
		logger.error("updateNewCovDoctorNotes failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@Override
	public Response updateNewCovDoctorNotesStatus(NewCovDoctorNotesDto newCovDoctorNotesDto) throws ApplicationException {
		try {
			 NewCovDoctorNotes newCovDoctorNotes=findById(newCovDoctorNotesDto.getNewCovDoctorNotesId());
			 newCovDoctorNotes.setStatus(newCovDoctorNotesDto.getStatus());
			 newCovDoctorNotes.setUpdatedBy(newCovDoctorNotesDto.getUpdatedBy());
			 newCovDoctorNotes.setUpdatedDate(GlobalCommonDateUtils.getDate(newCovDoctorNotesDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			 update(newCovDoctorNotes);
		
		return new Response(SUCCESS, COMMON_UPDATE, SUCCESS_MESSAGE, null, null);
		
		} catch (HibernateException he) {
		logger.error("updateNewCovDoctorNotesStatus failed", he);
		} catch (Exception e) {
		logger.error("updateNewCovDoctorNotesStatus failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@Override
	public Response getNewCovDoctorNotesList(NewCovDoctorNotesDto newCovDoctorNotesDto) throws ApplicationException {
		try {
			
			String query = "select " 
					+ "	docNote.new_cov_doctor_notes_id as \"newCovDoctorNotesId\","
					+ "	docNote.doctor_notes as \"doctorNotes\", "
					+ "	docNote.encounter_id as \"encounterId\", "
					+ "	docNote.admission_id as \"admissionId\", " 
					+ "	docNote.patient_id as \"patientId\", "
					+ "	docNote.doctor_id as \"doctorId\", " 
					+ "	docNote.unit_id as \"unitId\", "
					+ "	docNote.organization_id as \"organizationId\", "
					+ "	to_char(docNote.created_date, 'dd/mm/yyyy HH24:MI') as \"createdDate\" ,docNote.created_by as \"createdBy\", "
					+ "	docNote.status as \"status\" " 
					+ " from "
					+ "	patient.t_new_cov_doctor_notes docNote "
					+ " where " 
					+ "	docNote.status ='A'"
					+ "	and docNote.unit_id =:unitId "
					+ "	and docNote.organization_id =:organizationId ";
			
			StringBuilder stringQuery=new StringBuilder(sessionFactory.getCurrentSession().createSQLQuery(query).getQueryString());
			if(newCovDoctorNotesDto.getAdmissionId()!=null && newCovDoctorNotesDto.getAdmissionId()!=0){
				stringQuery.append(" AND docNote.admission_id = :admissionId");
			}else if(newCovDoctorNotesDto.getEncounterId()!=null && newCovDoctorNotesDto.getEncounterId()!=0){
				stringQuery.append(" AND docNote.encounter_id = :encounterId");
			}else {
				stringQuery.append(" AND docNote.patient_id = :patientId");
			}
			stringQuery.append(" order by docNote.new_cov_doctor_notes_id desc");
			Query createQuery=sessionFactory.getCurrentSession().createSQLQuery(stringQuery.toString())
					.setParameter("organizationId",  newCovDoctorNotesDto.getOrganizationId())
					.setParameter("unitId",  newCovDoctorNotesDto.getUnitId());

			if (newCovDoctorNotesDto.getAdmissionId() != null && newCovDoctorNotesDto.getAdmissionId() != 0) {
				createQuery.setParameter("admissionId", newCovDoctorNotesDto.getAdmissionId());
			} else if (newCovDoctorNotesDto.getEncounterId() != null && newCovDoctorNotesDto.getEncounterId() != 0) {
				createQuery.setParameter("encounterId", newCovDoctorNotesDto.getEncounterId());
			} else {
				createQuery.setParameter("patientId", newCovDoctorNotesDto.getPatientId());
			}

			List<NewCovDoctorNotesDto> list=createQuery.setResultTransformer(Transformers.aliasToBean(NewCovDoctorNotesDto.class)).list();
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getNewCovDoctorNotesList failed", he);
		} catch (Exception e) {
		logger.error("getNewCovDoctorNotesList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@Override
	public Response getNewCovDoctorNotesById(Integer newCovDoctorNotesId, Integer organizationId, Integer unitId)
			throws ApplicationException {
		try {
			String query="select " 
					+ "	docNote.new_cov_doctor_notes_id as \"newCovDoctorNotesId\","
					+ "	docNote.doctor_notes as \"doctorNotes\", "
					+ "	docNote.encounter_id as \"encounterId\", "
					+ "	docNote.admission_id as \"admissionId\", " 
					+ "	docNote.patient_id as \"patientId\", "
					+ "	docNote.doctor_id as \"doctorId\", " 
					+ "	docNote.unit_id as \"unitId\", "
					+ "	docNote.organization_id as \"organizationId\", "
					+ "	to_char(docNote.created_date, 'dd/mm/yyyy') as \"createdDate\" docNote.created_by as \"createdBy\", "
					+ "	docNote.status as \"status\" " 
					+ " from "
					+ "	patient.t_new_cov_doctor_notes docNote "
					+ " where " 
					+ "	docNote.unit_id =:unitId "
					+ "	and docNote.organization_id =:organizationId " 
					+ "	and docNote.new_cov_doctor_notes_id =:newCovDoctorNotesId";
			List<NewCovDoctorNotesDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery(query)
					.setParameter("newCovDoctorNotesId",  newCovDoctorNotesId)
					.setParameter("organizationId",  organizationId)
					.setParameter("unitId",  unitId)
					.setResultTransformer(Transformers.aliasToBean(NewCovDoctorNotesDto.class)).list();

		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getNewCovDoctorNotesById failed", he);
		} catch (Exception e) {
		logger.error("getNewCovDoctorNotesById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@Override
	public Response getNewCovDoctorNotesCount(NewCovDoctorNotesDto newCovDoctorNotesDto) throws ApplicationException {
try {
			
			Session session = sessionFactory.getCurrentSession();
			Criteria crit = session.createCriteria(NewCovDoctorNotes.class);
			crit.add(Restrictions.eq("organizationId", newCovDoctorNotesDto.getOrganizationId()));
			crit.add(Restrictions.eq("unitId", newCovDoctorNotesDto.getUnitId()));
			crit.setProjection(Projections.rowCount());
			Long count = (Long) crit.uniqueResult();
			
			return new Response(SUCCESS, SUCCESS_CODE, null, null, count);
				
		} catch (HibernateException he) {
		logger.error("getNewCovDoctorNotesCount failed", he);
		} catch (Exception e) {
		logger.error("getNewCovDoctorNotesCount failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


}
