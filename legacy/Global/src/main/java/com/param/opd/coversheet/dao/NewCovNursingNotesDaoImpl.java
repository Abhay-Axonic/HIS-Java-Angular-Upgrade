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
import com.param.opd.coversheet.dto.NewCovNursingNotesDto;
import com.param.opd.model.NewCovNursingNotes;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class NewCovNursingNotesDaoImpl extends GenericDao<NewCovNursingNotes,  Integer> implements INewCovNursingNotesDao, ICommonConstants{
	final static Logger logger = Logger.getLogger(NewCovNursingNotesDaoImpl.class);


	public NewCovNursingNotesDaoImpl() {
		super(NewCovNursingNotes.class);
	}

	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	public Response saveNewCovNursingNotes(NewCovNursingNotesDto newCovNursingNotesDto) throws ApplicationException {
		try {
			 NewCovNursingNotes newCovNursingNotes=new NewCovNursingNotes();
			 newCovNursingNotes.setNursingNotes(newCovNursingNotesDto.getNursingNotes());
			 newCovNursingNotes.setPatientId(newCovNursingNotesDto.getPatientId());
			 if(newCovNursingNotesDto.getEncounterId()!=null){
			 newCovNursingNotes.setEncounterId(newCovNursingNotesDto.getEncounterId());
			 }else{
				 newCovNursingNotes.setAdmissionId(newCovNursingNotesDto.getAdmissionId());
			 }
			 newCovNursingNotes.setDoctorId(newCovNursingNotesDto.getDoctorId());
			 newCovNursingNotes.setOrganizationId(newCovNursingNotesDto.getOrganizationId());
			 newCovNursingNotes.setUnitId(newCovNursingNotesDto.getUnitId());
			 newCovNursingNotes.setStatus(newCovNursingNotesDto.getStatus());
			 newCovNursingNotes.setCreatedBy(newCovNursingNotesDto.getCreatedBy());
			 newCovNursingNotes.setCreatedDate(GlobalCommonDateUtils.getDate(newCovNursingNotesDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			 newCovNursingNotes.setUpdatedBy(newCovNursingNotesDto.getUpdatedBy());
			 newCovNursingNotes.setUpdatedDate(GlobalCommonDateUtils.getDate(newCovNursingNotesDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			 save(newCovNursingNotes);
		
		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		
		} catch (HibernateException he) {
		logger.error("saveNewCovNursingNotes failed", he);
		} catch (Exception e) {
		logger.error("saveNewCovNursingNotes failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateNewCovNursingNotes(NewCovNursingNotesDto newCovNursingNotesDto) throws ApplicationException {
		try {
			 NewCovNursingNotes newCovNursingNotes=findById(newCovNursingNotesDto.getNewCovNursingNotesId());
			 newCovNursingNotes.setNursingNotes(newCovNursingNotesDto.getNursingNotes());
			 newCovNursingNotes.setPatientId(newCovNursingNotesDto.getPatientId());
			 if(newCovNursingNotesDto.getEncounterId()!=null){
				 newCovNursingNotes.setEncounterId(newCovNursingNotesDto.getEncounterId());
				 }else{
					 newCovNursingNotes.setAdmissionId(newCovNursingNotesDto.getAdmissionId());
				 }			 newCovNursingNotes.setDoctorId(newCovNursingNotesDto.getDoctorId());
			 newCovNursingNotes.setOrganizationId(newCovNursingNotesDto.getOrganizationId());
			 newCovNursingNotes.setUnitId(newCovNursingNotesDto.getUnitId());
			 newCovNursingNotes.setUpdatedBy(newCovNursingNotesDto.getUpdatedBy());
			 newCovNursingNotes.setUpdatedDate(GlobalCommonDateUtils.getDate(newCovNursingNotesDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			 update(newCovNursingNotes);
		
		return new Response(SUCCESS,  SUCCESS_MESSAGE, COMMON_UPDATE, null, null);
		
		} catch (HibernateException he) {
		logger.error("updateNewCovNursingNotes failed", he);
		} catch (Exception e) {
		logger.error("updateNewCovNursingNotes failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@Override
	public Response updateNewCovNursingNotesStatus(NewCovNursingNotesDto newCovNursingNotesDto) throws ApplicationException {
		try {
			 NewCovNursingNotes newCovNursingNotes=findById(newCovNursingNotesDto.getNewCovNursingNotesId());
			 newCovNursingNotes.setStatus(newCovNursingNotesDto.getStatus());
			 newCovNursingNotes.setUpdatedBy(newCovNursingNotesDto.getUpdatedBy());
			 newCovNursingNotes.setUpdatedDate(GlobalCommonDateUtils.getDate(newCovNursingNotesDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			 update(newCovNursingNotes);
		
		return new Response(SUCCESS, COMMON_UPDATE, SUCCESS_MESSAGE, null, null);
		
		} catch (HibernateException he) {
		logger.error("updateNewCovNursingNotesStatus failed", he);
		} catch (Exception e) {
		logger.error("updateNewCovNursingNotesStatus failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@Override
	public Response getNewCovNursingNotesList(NewCovNursingNotesDto newCovNursingNotesDto) throws ApplicationException {
		try {
			String query = "select " 
					+ "	nurNote.new_cov_nursing_notes_id as \"newCovNursingNotesId\","
					+ "	nurNote.nursing_notes as \"doctorNotes\", "
					+ "	nurNote.encounter_id as \"encounterId\", "
					+ "	nurNote.admission_id as \"admissionId\", " 
					+ "	nurNote.patient_id as \"patientId\", "
					+ "	nurNote.doctor_id as \"doctorId\", " 
					+ "	nurNote.unit_id as \"unitId\", "
					+ "	nurNote.organization_id as \"organizationId\", "
					+ "	to_char(nurNote.created_date, 'dd/mm/yyyy HH24:MI') as \"createdDate\" ,nurNote.created_by as \"createdBy\", "
					+ "	nurNote.status as \"status\" " 
					+ " from "
					+ "	patient.t_new_cov_nursing_notes nurNote "
					+ " where " 
					+ "	nurNote.unit_id =:unitId "
					+ "	and nurNote.organization_id =:organizationId " 
					+ "	and nurNote.status ='A' ";
			StringBuilder stringQuery=new StringBuilder(sessionFactory.getCurrentSession().createSQLQuery(query).getQueryString());
			if(newCovNursingNotesDto.getAdmissionId()!=null && newCovNursingNotesDto.getAdmissionId()!=0){
				stringQuery.append(" AND nurNote.admission_id= :admissionId");

			}else if(newCovNursingNotesDto.getEncounterId()!=null && newCovNursingNotesDto.getEncounterId()!=0){
				stringQuery.append(" AND nurNote.encounter_id= :encounterId");
			}else {
				stringQuery.append(" AND nurNote.patient_id= :patientId ");
			}
			stringQuery.append(" order by nurNote.new_cov_nursing_notes_id desc ");
			Query createQuery=sessionFactory.getCurrentSession().createSQLQuery(stringQuery.toString());
			createQuery.setParameter("organizationId",  newCovNursingNotesDto.getOrganizationId());
			createQuery.setParameter("unitId",  newCovNursingNotesDto.getUnitId());

			if(newCovNursingNotesDto.getAdmissionId()!=null && newCovNursingNotesDto.getAdmissionId()!=0){
				createQuery.setParameter("admissionId",  newCovNursingNotesDto.getAdmissionId());

			}else if(newCovNursingNotesDto.getEncounterId()!=null && newCovNursingNotesDto.getEncounterId()!=0){
				createQuery  .setParameter("encounterId",  newCovNursingNotesDto.getEncounterId());
			}else {
				createQuery	.setParameter("patientId",  newCovNursingNotesDto.getPatientId());
			}

			List<NewCovNursingNotesDto> list=createQuery.setResultTransformer(Transformers.aliasToBean(NewCovNursingNotesDto.class)).list();
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getNewCovNursingNotesList failed", he);
		} catch (Exception e) {
		logger.error("getNewCovNursingNotesList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@Override
	public Response getNewCovNursingNotesById(Integer newCovNursingNotesId, Integer organizationId, Integer unitId)
			throws ApplicationException {
		try {
			String query="select " 
					+ "	nurNote.new_cov_nursing_notes_id as \"newCovDoctorNotesId\","
					+ "	nurNote.nursing_notes as \"nursingNotes\", "
					+ "	nurNote.encounter_id as \"encounterId\", "
					+ "	nurNote.admission_id as \"admissionId\", " 
					+ "	nurNote.patient_id as \"patientId\", "
					+ "	nurNote.doctor_id as \"doctorId\", " 
					+ "	nurNote.unit_id as \"unitId\", "
					+ "	nurNote.organization_id as \"organizationId\", "
					+ "	to_char(nurNote.created_date, 'dd/mm/yyyy') as \"createdDate\" nurNote.created_by as \"createdBy\", "
					+ "	nurNote.status as \"status\" " 
					+ " from "
					+ "	patient.t_new_cov_nursing_notes nurNote "
					+ " where " 
					+ "	nurNote.unit_id =:unitId "
					+ "	and nurNote.organization_id =:organizationId " 
					+ "	and nurNote.new_cov_nursing_notes_id =:newCovNursingNotesId";
			List<NewCovNursingNotesDto> list=sessionFactory.getCurrentSession()
					.createSQLQuery(query)
					.setParameter("newCovNursingNotesId",  newCovNursingNotesId)
					.setParameter("organizationId",  organizationId)
					.setParameter("unitId",  unitId)
					.setResultTransformer(Transformers.aliasToBean(NewCovNursingNotesDto.class)).list();

		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getNewCovNursingNotesById failed", he);
		} catch (Exception e) {
		logger.error("getNewCovNursingNotesById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@Override
	public Response getNewCovNursingNotesCount(NewCovNursingNotesDto newCovNursingNotesDto) throws ApplicationException {
try {
			
			Session session = sessionFactory.getCurrentSession();
			Criteria crit = session.createCriteria(NewCovNursingNotes.class);
			crit.add(Restrictions.eq("organizationId", newCovNursingNotesDto.getOrganizationId()));
			crit.add(Restrictions.eq("unitId", newCovNursingNotesDto.getUnitId()));
			crit.setProjection(Projections.rowCount());
			Long count = (Long) crit.uniqueResult();
			
			return new Response(SUCCESS, SUCCESS_CODE, null, null, count);
				
		} catch (HibernateException he) {
		logger.error("getNewCovNursingNotesCount failed", he);
		} catch (Exception e) {
		logger.error("getNewCovNursingNotesCount failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


}
