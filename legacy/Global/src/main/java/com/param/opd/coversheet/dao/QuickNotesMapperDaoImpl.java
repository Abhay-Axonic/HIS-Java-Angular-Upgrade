package com.param.opd.coversheet.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.opd.coversheet.dto.QuickNotesMapperDto;
import com.param.opd.model.QuickNotesMapper;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class QuickNotesMapperDaoImpl extends GenericDao<QuickNotesMapper,  Integer> implements IQuickNotesMapperDao, ICommonConstants{
	final static Logger logger = Logger.getLogger(QuickNotesMapperDaoImpl.class);


	public QuickNotesMapperDaoImpl() {
		super(QuickNotesMapper.class);
	}

	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	public Response saveQuickNotesMapper(QuickNotesMapperDto quickNotesMapperDto) throws ApplicationException {
		try {
			StringBuilder query = new StringBuilder("select qnm.quick_notes_mapper_id  as \"quickNotesMapperId\",\r\n" + 
					"qnm.quick_notes_desc as \"quickNotesDesc\"\r\n" + 
					"from opd.t_quick_notes_mapper qnm\r\n" + 
					"where qnm.patient_id  =:patientId\r\n" + 
					" and qnm.encounter_id  =:encounterId\r\n" + 
					" and qnm.organization_id =:orgId\r\n" + 
					" and qnm.unit_id =:unitId");

			List<QuickNotesMapperDto> listSaved = null;
			listSaved = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
					.setParameter("patientId", quickNotesMapperDto.getPatientId())
					.setParameter("unitId", quickNotesMapperDto.getUnitId())
					.setParameter("orgId", quickNotesMapperDto.getOrganizationId())
					.setParameter("encounterId", quickNotesMapperDto.getEncounterId())
					.setResultTransformer(Transformers.aliasToBean(QuickNotesMapperDto.class)).list();
			
			if(listSaved != null && listSaved.size() != 0) {
					List<QuickNotesMapperDto> listQuick = listSaved;
					for (QuickNotesMapperDto dtossss : listQuick) {
						Session session=sessionFactory.getCurrentSession();
						session.createSQLQuery("update  opd.t_quick_notes_mapper  set quick_notes_desc =:quickNotesDesc "
						+" where quick_notes_mapper_id=:quickNotesMapperId")
						.setParameter("quickNotesMapperId", dtossss.getQuickNotesMapperId())
						.setParameter("quickNotesDesc", quickNotesMapperDto.getQuickNotesDesc())
						.executeUpdate();
					}
			}else {
				QuickNotesMapper quickNotesMapper=new QuickNotesMapper();
				 quickNotesMapper.setQuickNotesDesc(quickNotesMapperDto.getQuickNotesDesc());
				 quickNotesMapper.setPatientId(quickNotesMapperDto.getPatientId());
				 if(quickNotesMapperDto.getEncounterId()!=null){
				 quickNotesMapper.setEncounterId(quickNotesMapperDto.getEncounterId());
				 }else{
					 quickNotesMapper.setAdmissionId(quickNotesMapperDto.getAdmissionId());
				 }
				 quickNotesMapper.setDoctorId(quickNotesMapperDto.getDoctorId());
				 quickNotesMapper.setOrganizationId(quickNotesMapperDto.getOrganizationId());
				 quickNotesMapper.setUnitId(quickNotesMapperDto.getUnitId());
				 quickNotesMapper.setStatus('A');
				 quickNotesMapper.setCreatedBy(quickNotesMapperDto.getCreatedBy());
				 quickNotesMapper.setCreatedDate(GlobalCommonDateUtils.getDate(quickNotesMapperDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
				 quickNotesMapper.setUpdatedBy(quickNotesMapperDto.getUpdatedBy());
				 quickNotesMapper.setUpdatedDate(GlobalCommonDateUtils.getDate(quickNotesMapperDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
				 save(quickNotesMapper);
			}
			 
		
		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		
		} catch (HibernateException he) {
		logger.error("saveQuickNotesMapper failed", he);
		} catch (Exception e) {
		logger.error("saveQuickNotesMapper failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	
	@Override
	public Response getQuickNotesMapperList(QuickNotesMapperDto quickNotesMapperDto) throws ApplicationException {
		try {
			String query="	select " + 
					"	mppr.quick_notes_mapper_id as \"quickNotesMapperId\", " + 
					"	mppr.quick_notes_desc as \"quickNotesDesc\", " + 
					"	mppr.admission_id as \"admissionId\", " + 
					"	mppr.patient_id as \"patientId\", " + 
					"	mppr.encounter_id as \"encounterId\", " + 
					"	mppr.doctor_id as \"doctorId\", " + 
					"	mppr.status as \"status\" " + 
					"from " + 
					"	opd.t_quick_notes_mapper mppr " + 
					"where " + 
					"	mppr.patient_id =:patientId " + 
					"	and mppr.encounter_id =:encounterId " + 
					"	and mppr.organization_id =:organizationId " + 
					"	and mppr.unit_id =:unitId "
					+ " order by mppr.quick_notes_mapper_id desc";
			List<QuickNotesMapperDto> list=sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("organizationId",  quickNotesMapperDto.getOrganizationId())
					.setParameter("patientId",  quickNotesMapperDto.getPatientId())
					.setParameter("encounterId",  quickNotesMapperDto.getEncounterId())
					.setParameter("unitId",  quickNotesMapperDto.getUnitId())					
					.setResultTransformer(Transformers.aliasToBean(QuickNotesMapperDto.class)).list();
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getQuickNotesMapperList failed", he);
		} catch (Exception e) {
		logger.error("getQuickNotesMapperList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}
