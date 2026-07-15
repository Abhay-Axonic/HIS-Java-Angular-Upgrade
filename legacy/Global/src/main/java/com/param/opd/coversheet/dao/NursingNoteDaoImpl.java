package com.param.opd.coversheet.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.opd.coversheet.dto.NursingNoteDto;
import com.param.opd.model.NursingNote;
import in.param.exception.ApplicationException;
import in.param.common.dao.GenericDao;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class NursingNoteDaoImpl extends GenericDao<NursingNote,  Integer> implements INursingNoteDao, ICommonConstants{
	final static Logger logger = Logger.getLogger(NursingNoteDaoImpl.class);

	public NursingNoteDaoImpl() {
		super(NursingNote.class);
	}

	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	public Response saveNursingNote(NursingNoteDto nursingNoteDto) throws ApplicationException {
		try {
			NursingNote nursingNote=new NursingNote();
			nursingNote.setNursingNoteDesc(nursingNoteDto.getNursingNoteDesc());
			nursingNote.setTemplate(nursingNoteDto.getTemplate());
			nursingNote.setPatientId(nursingNoteDto.getPatientId());
			 if(nursingNoteDto.getEncounterId()!=null){
				 nursingNote.setEncounterId(nursingNoteDto.getEncounterId());
			 }else{
				 nursingNote.setAdmissionId(nursingNoteDto.getAdmissionId());
			 }
			 nursingNote.setDoctorId(nursingNoteDto.getDoctorId());
			 nursingNote.setOrganizationId(nursingNoteDto.getOrganizationId());
			 nursingNote.setUnitId(nursingNoteDto.getUnitId());
			 nursingNote.setStatus(nursingNoteDto.getStatus());
			 nursingNote.setCreatedBy(nursingNoteDto.getCreatedBy());
			 nursingNote.setCreatedDate(GlobalCommonDateUtils.getDate(nursingNoteDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			 nursingNote.setUpdatedBy(nursingNoteDto.getUpdatedBy());
			 nursingNote.setUpdatedDate(GlobalCommonDateUtils.getDate(nursingNoteDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			 if(nursingNoteDto.getNursingNoteId()!=null) {
				 nursingNote.setNursingNoteId(nursingNoteDto.getNursingNoteId());
				 update(nursingNote);
			 }else {
				 save(nursingNote);
			 }
		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		
		} catch (HibernateException he) {
		logger.error("saveNursingNote failed", he);
		} catch (Exception e) {
		logger.error("saveNursingNote failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getNursingNoteList(NursingNoteDto dto) throws ApplicationException {
		try {
			StringBuilder stringQuery=new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_NURSING_NOTE_LIST").getQueryString());
			if(dto.getAdmissionId()!=null && dto.getAdmissionId()!=0){
				stringQuery.append(" AND nursingNote.admissionId= :admissionId");

			}else if(dto.getEncounterId()!=null && dto.getEncounterId()!=0){
				stringQuery.append(" AND nursingNote.encounterId= :encounterId");
			}else {
				stringQuery.append(" AND nurNote.patientId= :patientId ");
			}

			Query createQuery=sessionFactory.getCurrentSession().createQuery(stringQuery.toString())
					.setParameter("organizationId",  dto.getOrganizationId())
					.setParameter("unitId",  dto.getUnitId());

			if(dto.getAdmissionId()!=null && dto.getAdmissionId()!=0){
				createQuery.setParameter("admissionId",  dto.getAdmissionId());
			}else if(dto.getEncounterId()!=null && dto.getEncounterId()!=0){
				createQuery.setParameter("encounterId",  dto.getEncounterId());
			}else {
				createQuery	.setParameter("patientId",  dto.getPatientId());
			}

			List<NursingNoteDto> list=createQuery.setResultTransformer(Transformers.aliasToBean(NursingNoteDto.class)).list();
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getNursingNoteList failed", he);
		} catch (Exception e) {
		logger.error("getNursingNoteList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}
