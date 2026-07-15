package com.param.global.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.AdvisedDateAppointmentDto;
import com.param.global.model.AdvisedDateAppointmentMapper;
import com.param.global.model.CabinMaster;
import com.param.opd.coversheet.dao.IMedicationDAO;
import com.param.opd.coversheet.dto.PrescriptionDto;
import com.param.service.global.SessionService;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({ "unused", "unchecked","rawtypes" })
public class AdvisedDateAppointmentDaoImpl extends GenericDao<AdvisedDateAppointmentMapper, Integer>
implements IAdvisedDateAppointmentDao, ICommonConstants {
	final static Logger logger = Logger.getLogger(AdvisedDateAppointmentDaoImpl.class);


public AdvisedDateAppointmentDaoImpl() {
super(AdvisedDateAppointmentMapper.class);
}

@Override
public Response saveAdvisedDateAppointment(AdvisedDateAppointmentDto advisedDateAppointmentDto)
		throws ApplicationException {
	try {
		AdvisedDateAppointmentMapper advisedDateAppointmentMapper=new AdvisedDateAppointmentMapper();
		advisedDateAppointmentMapper.setPatientId(advisedDateAppointmentDto.getPatientId());
		advisedDateAppointmentMapper.setEncounterId(advisedDateAppointmentDto.getEncounterId());
		advisedDateAppointmentMapper.setDoctorId(advisedDateAppointmentDto.getDoctorId());
		advisedDateAppointmentMapper.setAdvisedDate(GlobalCommonDateUtils.getDate(advisedDateAppointmentDto.getAdvisedDate(), "dd-M-yyyy HH:mm:ss"));
		advisedDateAppointmentMapper.setOrganizationId(advisedDateAppointmentDto.getOrganizationId());
		advisedDateAppointmentMapper.setUnitId(advisedDateAppointmentDto.getUnitId());
		advisedDateAppointmentMapper.setStatus(advisedDateAppointmentDto.getStatus());
		advisedDateAppointmentMapper.setCreatedBy(advisedDateAppointmentDto.getCreatedBy());
		advisedDateAppointmentMapper.setCreatedDate(GlobalCommonDateUtils.getDate(advisedDateAppointmentDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
		advisedDateAppointmentMapper.setUpdatedBy(advisedDateAppointmentDto.getUpdatedBy());
		advisedDateAppointmentMapper.setUpdatedDate(GlobalCommonDateUtils.getDate(advisedDateAppointmentDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
		 
		save(advisedDateAppointmentMapper);
	
	return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
	
	} catch (HibernateException he) {
	logger.error("saveAdvisedDateAppointment failed", he);
	} catch (Exception e) {
	logger.error("saveAdvisedDateAppointment failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

@Override
public Response getAdvisedDateAppointmentList(AdvisedDateAppointmentDto advisedDateAppointmentDto)
		throws ApplicationException {
	try {
	
			String query = "select  " + "	advmppr.advised_date_appo_id as \"advisedDateAppoId\",  "
					+ "	advmppr.patient_id as \"patientId\" ,  " 
					+ "	advmppr.encounter_id as \"encounterId\" ,  "
					+ "	advmppr.status as \"status\" ,  "
					+ "	advmppr.doctor_id as \"doctorId\",  "
					+ "	to_char(advmppr.advised_date, 'dd/mm/yyyy') as \"advisedDate\",  "
					+ "	concat(tpr.first_name, ' ', tpr.middle_name, ' ', tpr.last_name) as \"patientName\",  "
					+ "	concat('Dr. ', mdm.first_name, ' ', mdm.middle_name, ' ', mdm.last_name) as \"doctorName\",  "
					+ "	to_char(ted.encounter_date, 'dd/mm/yyyy') as \"encounterDate\","
					+ " tpr.mobile_number as \"mobileNumber\"  " 
					+ " from  "
					+ "	public.t_advised_date_appointment_mapper advmppr  "
					+ " inner join patient.t_patient_registration tpr on  " 
					+ "	tpr.patient_id = advmppr.patient_id  "
					+ " inner join doctor.m_doctor_master mdm on  " 
					+ "	mdm.doctor_id = advmppr.doctor_id  "
					+ " inner join opd.t_encounter_master ted on  " 
					+ "	ted.encounter_id = advmppr.encounter_id  "
					+ " where  " 
					+ "	advmppr.organization_id =:organizationId  " 
					+ "	and advmppr.status ='A'"
					+ "	and advmppr.unit_id =:unitId"
					+ "	and (advmppr.doctor_id =:doctorId or :doctorId = 0 )"
					+ "	and to_char(advmppr.advised_date, 'yyyy-mm-dd') between :fromDate and :todate "
					;
			
		List<AdvisedDateAppointmentDto> list=sessionFactory.getCurrentSession()
				.createSQLQuery(query)
				.setParameter("organizationId",  advisedDateAppointmentDto.getOrganizationId())
				.setParameter("unitId",  advisedDateAppointmentDto.getUnitId())
				.setParameter("doctorId",  advisedDateAppointmentDto.getDoctorId()!=null ? advisedDateAppointmentDto.getDoctorId() : 0)
				.setParameter("fromDate",  advisedDateAppointmentDto.getFromDate())
				.setParameter("todate",  advisedDateAppointmentDto.getToDate())
				.setFirstResult(advisedDateAppointmentDto.getOffset() != null ? advisedDateAppointmentDto.getOffset()  : 0)
				.setMaxResults(advisedDateAppointmentDto.getNoOfRecordsPerPage() != null ? advisedDateAppointmentDto.getNoOfRecordsPerPage()  : 10)
				.setResultTransformer(Transformers.aliasToBean(AdvisedDateAppointmentDto.class)).list();

	return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
	
	} catch (HibernateException he) {
	logger.error("getAdvisedDateAppointmentList failed", he);
	} catch (Exception e) {
	logger.error("getAdvisedDateAppointmentList failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

@Override
public Response getAdvisedDateAppointmentListCount(AdvisedDateAppointmentDto advisedDateAppointmentDto)
		throws ApplicationException {
	try {
		Session session = sessionFactory.getCurrentSession();
		Criteria crit = session.createCriteria(AdvisedDateAppointmentMapper.class);
		crit.add(Restrictions.eq("organizationId", advisedDateAppointmentDto.getOrganizationId()));
		crit.add(Restrictions.eq("unitId", advisedDateAppointmentDto.getUnitId()));
		crit.setProjection(Projections.rowCount());
		Long count = (Long) crit.uniqueResult();

		return new Response(SUCCESS, SUCCESS_CODE, null, null, count);

	}catch(HibernateException he) 
		{
		logger.error("getAdvisedDateAppointmentListCount failed", he);
		}
		catch (Exception e)
		{
		logger.error("getAdvisedDateAppointmentListCount failed", e);
		}
	return new Response<>(ERROR,ERROR_CODE,COMMON_ERROR_MESSAGE,null,null);
}

@Override
public Response checkAppointmentAvailable(AdvisedDateAppointmentDto advisedDateAppointmentDto)
		throws ApplicationException {
		try {

			String query = "select "
					+ "	advmppr.advised_date_appo_id as \"advisedDateAppoId\" " 
					+ " from "
					+ "	public.t_advised_date_appointment_mapper advmppr "
					+ " where "
					+ "	advmppr.organization_id = :organizationId " 
					+ "	and advmppr.unit_id = :unitId "
					+ "	and advmppr.encounter_id = :encounterId";

			List<AdvisedDateAppointmentDto> list = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("organizationId", advisedDateAppointmentDto.getOrganizationId())
					.setParameter("unitId", advisedDateAppointmentDto.getUnitId())
					.setParameter("encounterId", advisedDateAppointmentDto.getEncounterId())
					.setResultTransformer(Transformers.aliasToBean(AdvisedDateAppointmentDto.class)).list();

			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

		} catch (HibernateException he) {
		logger.error("checkAppointmentAvailable failed", he);
		} catch (Exception e) {
		logger.error("checkAppointmentAvailable failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

@Override
public Response updateAdvisedDateAppointment(AdvisedDateAppointmentDto advisedDateAppointmentDto)
		throws ApplicationException {
	try {

		String query = "UPDATE public.t_advised_date_appointment_mapper " +
				"SET advised_date = :advisedDate " +
				"WHERE encounter_id = :encounterId " +
				"AND organization_id = :organizationId " +
				"AND unit_id = :unitId";

		int rowsUpdated = sessionFactory.getCurrentSession()
				.createSQLQuery(query)
				.setParameter("advisedDate", GlobalCommonDateUtils.getDate(advisedDateAppointmentDto.getAdvisedDate(), "dd-M-yyyy HH:mm:ss"))
				.setParameter("encounterId", advisedDateAppointmentDto.getEncounterId())
				.setParameter("organizationId", advisedDateAppointmentDto.getOrganizationId())
				.setParameter("unitId", advisedDateAppointmentDto.getUnitId())
				.executeUpdate();



		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
	
	} catch (HibernateException he) {
	logger.error("updateAdvisedDateAppointment failed", he);
	} catch (Exception e) {
	logger.error("updateAdvisedDateAppointment failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

@Override
public Response updateCancelStatusInAdvisedAppointment(PrescriptionDto medicationDTOs) throws ApplicationException {
try {
	String query = "UPDATE public.t_advised_date_appointment_mapper " +
			"SET status = 'I' " +
			"WHERE encounter_id = :encounterId " +
			"AND organization_id = :organizationId " +
			"AND unit_id = :unitId";

	sessionFactory.getCurrentSession().createSQLQuery(query)
			.setParameter("encounterId", medicationDTOs.getEncounterId())
			.setParameter("organizationId", medicationDTOs.getOrganizationId())
			.setParameter("unitId", medicationDTOs.getUnitId())
			.executeUpdate();
	return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
	
	} catch (HibernateException he) {
	logger.error("updateCancelStatusInAdvisedAppointment failed", he);
	} catch (Exception e) {
	logger.error("updateCancelStatusInAdvisedAppointment failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}
}
