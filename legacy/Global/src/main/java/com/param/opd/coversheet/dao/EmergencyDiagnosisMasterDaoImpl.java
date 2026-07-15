package com.param.opd.coversheet.dao;

import java.util.List;

import org.hibernate.*;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.opd.coversheet.dto.D2NDiagnisis;
import com.param.opd.coversheet.dto.DiagnosisMapperDto;
import com.param.opd.coversheet.dto.EmergencyDiagnosisMasterDto;
import com.param.opd.coversheet.model.EmergencyDiagnosisMaster;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({ "unchecked", "rawtypes" })
public class EmergencyDiagnosisMasterDaoImpl extends GenericDao<EmergencyDiagnosisMaster, Integer>
		implements IEmergencyDiagnosisMasterDao, ICommonConstants {
	final static Logger logger = Logger.getLogger(EmergencyDiagnosisMasterDaoImpl.class);


	@Autowired
	SessionFactory sessionFactory;

	public EmergencyDiagnosisMasterDaoImpl() {
		super(EmergencyDiagnosisMaster.class);
	}

	@Override
	public Response saveEmergencyDiagnosisMaster(EmergencyDiagnosisMasterDto emergencyDiagnosisMasterDto)
			throws ApplicationException {
		try {
			EmergencyDiagnosisMaster mapper = new EmergencyDiagnosisMaster();
			mapper.setDiagnosisId(emergencyDiagnosisMasterDto.getDiagnosisId());
			mapper.setStatus(emergencyDiagnosisMasterDto.getStatus());
			mapper.setOrganizationId(emergencyDiagnosisMasterDto.getOrganizationId());
			mapper.setUnitId(emergencyDiagnosisMasterDto.getUnitId());
			mapper.setCreatedBy(emergencyDiagnosisMasterDto.getCreatedBy());
			mapper.setCreatedDate(TimeZoneComponent.getDateByzone(emergencyDiagnosisMasterDto.getUnitId()));
			mapper.setUpdatedBy(emergencyDiagnosisMasterDto.getUpdatedBy());
			mapper.setUpdatedDate(TimeZoneComponent.getDateByzone(emergencyDiagnosisMasterDto.getUnitId()));
			sessionFactory.getCurrentSession().save(mapper);

			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (HibernateException he) {
		logger.error("saveEmergencyDiagnosisMaster failed", he);
		} catch (Exception e) {
		logger.error("saveEmergencyDiagnosisMaster failed", e);
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getListOfEmergencyDiagnosisMaster(EmergencyDiagnosisMasterDto emergencyDiagnosisMasterDto)
			throws ApplicationException {
		try {
			List<EmergencyDiagnosisMasterDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_LIST_OF_EMERGENCY_DIAGNOSIS_MASTER")
					.setParameter("organizationId", emergencyDiagnosisMasterDto.getOrganizationId())
					.setFirstResult(emergencyDiagnosisMasterDto.getOffset() != null
							? emergencyDiagnosisMasterDto.getOffset() : 0)
					.setMaxResults(emergencyDiagnosisMasterDto.getNoOfRecordsPerPage() != null
							? emergencyDiagnosisMasterDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(EmergencyDiagnosisMasterDto.class)).list();
			return new Response(SUCCESS, null, null, list, null);
		} catch (HibernateException he) {
		logger.error("getListOfEmergencyDiagnosisMaster failed", he);
		} catch (Exception e) {
		logger.error("getListOfEmergencyDiagnosisMaster failed", e);
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateEmergencyDiagnosisMasterStatus(EmergencyDiagnosisMasterDto emergencyDiagnosisMasterDto)
			throws ApplicationException {

		try {
			EmergencyDiagnosisMaster mapper = findById(emergencyDiagnosisMasterDto.getEmergencyDiagnosisMapperId());

			mapper.setStatus(emergencyDiagnosisMasterDto.getStatus());
			mapper.setUpdatedBy(emergencyDiagnosisMasterDto.getUpdatedBy());
			mapper.setUpdatedDate(TimeZoneComponent.getDateByzone(emergencyDiagnosisMasterDto.getUnitId()));
			update(mapper);

			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);

		} catch (HibernateException he) {
		logger.error("updateEmergencyDiagnosisMasterStatus failed", he);
		} catch (Exception e) {
		logger.error("updateEmergencyDiagnosisMasterStatus failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_UPDATE, null, null);
	}

	@Override
	public Response getEmergencyDiagnosisMasterById(EmergencyDiagnosisMasterDto emergencyDiagnosisMasterDto)
			throws ApplicationException {
		try {
			List<DiagnosisMapperDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_EMERGENCY_DIAGNOSIS_MASTER_LIST_BY_ID")
					.setParameter("emergencyDiagnosisMapperId",
							emergencyDiagnosisMasterDto.getEmergencyDiagnosisMapperId())
					.setParameter("organizationId", emergencyDiagnosisMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(EmergencyDiagnosisMasterDto.class)).list();
			return new Response(SUCCESS, null, null, list, null);
		} catch (HibernateException he) {
		logger.error("getEmergencyDiagnosisMasterById failed", he);
		} catch (Exception e) {
		logger.error("getEmergencyDiagnosisMasterById failed", e);
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateEmergencyDiagnosisMaster(EmergencyDiagnosisMasterDto emergencyDiagnosisMasterDto)
			throws ApplicationException {
		try {
			try {
				EmergencyDiagnosisMaster mapper = findById(emergencyDiagnosisMasterDto.getEmergencyDiagnosisMapperId());
				mapper.setDiagnosisId(emergencyDiagnosisMasterDto.getDiagnosisId());
				mapper.setStatus(emergencyDiagnosisMasterDto.getStatus());
				mapper.setOrganizationId(emergencyDiagnosisMasterDto.getOrganizationId());
				mapper.setUnitId(emergencyDiagnosisMasterDto.getUnitId());
				mapper.setUpdatedBy(emergencyDiagnosisMasterDto.getUpdatedBy());
				mapper.setUpdatedDate(TimeZoneComponent.getDateByzone(emergencyDiagnosisMasterDto.getUnitId()));
				update(mapper);

				return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);

			} catch (HibernateException he) {
			logger.error("updateEmergencyDiagnosisMaster failed", he);
			} catch (Exception e) {
			logger.error("updateEmergencyDiagnosisMaster failed", e);
			}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		} catch (HibernateException he) {
		logger.error("updateEmergencyDiagnosisMaster failed", he);
		} catch (Exception e) {
		logger.error("updateEmergencyDiagnosisMaster failed", e);
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getEmergencyDiagnosisMasterCount(EmergencyDiagnosisMasterDto emergencyDiagnosisMasterDto)
			throws ApplicationException {
		try {

			Session session = sessionFactory.getCurrentSession();
			Criteria crit = session.createCriteria(EmergencyDiagnosisMaster.class);
			crit.add(Restrictions.eq("organizationId", emergencyDiagnosisMasterDto.getOrganizationId()));
			crit.setProjection(Projections.rowCount());
			Long count = (Long) crit.uniqueResult();

			return new Response(SUCCESS, SUCCESS_CODE, null, null, count);

		} catch (HibernateException he) {
		logger.error("getEmergencyDiagnosisMasterCount failed", he);
		} catch (Exception e) {
		logger.error("getEmergencyDiagnosisMasterCount failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response checkEmergencyDiagnosisMasterAvailable(EmergencyDiagnosisMasterDto emergencyDiagnosisMasterDto)
			throws ApplicationException {
		try {
			List<EmergencyDiagnosisMasterDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_EMERGENCY_DIAGNOSIS_MASTER_BY_ID")
					.setParameter("diagnosisId", emergencyDiagnosisMasterDto.getDiagnosisId())
					.setParameter("organizationId", emergencyDiagnosisMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(EmergencyDiagnosisMasterDto.class)).list();

			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

		} catch (HibernateException he) {
		logger.error("checkEmergencyDiagnosisMasterAvailable failed", he);
		} catch (Exception e) {
		logger.error("checkEmergencyDiagnosisMasterAvailable failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActiveEmergencyDiagnosis(EmergencyDiagnosisMasterDto emergencyDiagnosisMasterDto)
			throws ApplicationException {
		try {
			List<DiagnosisMapperDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_EMERGENCY_DIAGNOSIS_MASTER_LIST")
					.setParameter("diagnosisName",
							"%" + emergencyDiagnosisMasterDto.getDiagnosisName().toLowerCase() + "%")
					.setParameter("organizationId", emergencyDiagnosisMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(EmergencyDiagnosisMasterDto.class)).list();
			return new Response(SUCCESS, null, null, list, null);
		} catch (HibernateException he) {
		logger.error("getActiveEmergencyDiagnosis failed", he);
		} catch (Exception e) {
		logger.error("getActiveEmergencyDiagnosis failed", e);
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getDataForDoorToNiddle(EmergencyDiagnosisMasterDto emergencyDiagnosisMasterDto) {
		try {
			/*+ " 	and pdtls.order_set_id is not null " */
			
			StringBuilder query = new StringBuilder(" select "
					+ " mdm.diagnosis_id as \"diagnosisId\", "
					+ " mdm.diagnosis_desc as \"diagnosisName\","
					+ " ta.admission_id as \"admissionId\", "
					+ " tpr.patient_id as \"patientId\", "
					+ " concat(coalesce(mpm.prefix_code, ''), ' ', coalesce(tpr.first_name) , ' ', coalesce(tpr.middle_name) , ' ', coalesce(tpr.last_name)) as \"patientName\", "
					+ " mi.id as \"itemId\", "
					+ " mi.\"name\" as \"itemDesc\", "
					+ " to_char(cast((select concat(to_char(dad.added_date,'YYYY-MM-DD'),' ',to_char(dad.administration_time,'HH24:MI:SS')) from pharmacy.t_drug_administration_detail dad where prescription_detail_id = pdtls.id order by 1 limit 1) as timestamp), 'YYYY-MM-DD HH24:MI:SS') as \"administrationTime\", "
					+ " to_char(ta.created_date, 'YYYY-MM-dd HH24:MI:SS') as \"admissionTime\" "
					+ " from "
					+ " patient.t_patient_diagnosis_appo_mapper mpr "
					+ " inner join adt.t_admission ta on "
					+ " ta.admission_id = mpr.admission_id "
					+ " inner join patient.t_patient_registration tpr on "
					+ " tpr.patient_id = ta.patient_id "
					+ " inner join m_prefix_master mpm on "
					+ " mpm.prefix_id = tpr.prefix_id "
					+ " inner join emr.m_diagnosis_master mdm on "
					+ " mdm.diagnosis_id = mpr.diagnosis_id "
					+ " inner join pharmacy.t_prescription tp on "
					+ " tp.admission_id = ta.admission_id "
					+ " inner join pharmacy.t_prescription_detail pdtls on "
					+ " tp.id = pdtls.prescription_id "
					+ " and pdtls.order_set_id is not null "
					+ " inner join m_item mi on "
					+ " mi.id = pdtls.item_id "
					+ " where "
					+ " ta.status = 'A' "
					+ " and mpr.is_while_admission = 'Y' "
				    + "	and ta.unit_id=:unitId ");
			if(emergencyDiagnosisMasterDto.getFromDate()!=null && emergencyDiagnosisMasterDto.getToDate()!=null)
			{
				query.append(" and to_char(mpr.created_date, 'YYYY-MM-DD') between :fromDate and :toDate"); // Use parameters for date range
			}
			if(emergencyDiagnosisMasterDto.getPatientId()!=null)
			{
				query.append(" and tpr.patient_id = :patientId");
			}
			if(emergencyDiagnosisMasterDto.getDiagnisisId()!=null)
			{
				query.append(" and mdm.diagnosis_id = :diagnosisId");
			}
			query.append(" order by mdm.diagnosis_id ");
			
			Query hQuery = sessionFactory.getCurrentSession()
					.createSQLQuery(query.toString())
					.setParameter("unitId", emergencyDiagnosisMasterDto.getUnitId());

			if (emergencyDiagnosisMasterDto.getFromDate() != null && emergencyDiagnosisMasterDto.getToDate() != null) {
				hQuery.setParameter("fromDate", emergencyDiagnosisMasterDto.getFromDate()); // Set fromDate parameter
				hQuery.setParameter("toDate", emergencyDiagnosisMasterDto.getToDate()); // Set toDate parameter
			}
			if (emergencyDiagnosisMasterDto.getPatientId() != null) {
				hQuery.setParameter("patientId", emergencyDiagnosisMasterDto.getPatientId()); // Set patientId parameter
			}
			if (emergencyDiagnosisMasterDto.getDiagnisisId() != null) {
				hQuery.setParameter("diagnosisId", emergencyDiagnosisMasterDto.getDiagnisisId()); // Set diagnosisId parameter
			}

			List<D2NDiagnisis> list=hQuery.setResultTransformer(Transformers.aliasToBean(D2NDiagnisis.class)).list();
			return new Response(SUCCESS, null, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getDataForDoorToNiddle failed", he);
		} catch (Exception e) {
		logger.error("getDataForDoorToNiddle failed", e);
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

}
