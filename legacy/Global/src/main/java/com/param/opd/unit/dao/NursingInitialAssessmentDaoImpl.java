package com.param.opd.unit.dao;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.entity.model.opd.NursingInitialAssessmentModel;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.NursingInitialAssessmentDto;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class NursingInitialAssessmentDaoImpl extends GenericDao<NursingInitialAssessmentModel, Integer> implements INursingInitialAssessmentDao, ICommonConstants {

	private static final Logger logger = Logger.getLogger(NursingInitialAssessmentDaoImpl.class);

	public NursingInitialAssessmentDaoImpl() {
		super(NursingInitialAssessmentModel.class);
	}


	@Override
	public Response saveAppointmentVitalMapper(NursingInitialAssessmentDto nursingInitialAssessmentDto)
			throws ApplicationException {
		try {
			NursingInitialAssessmentModel nursingInitialAssessmentModel=new NursingInitialAssessmentModel();

			LocalTime assessmentStartTime1 = LocalTime.parse( nursingInitialAssessmentDto.getAssessmentStartTime());
			LocalDate today = LocalDate.now();
			Instant assessmentStartTime = assessmentStartTime1.atDate(today).atZone(ZoneId.of("UTC")).toInstant();

			LocalTime assessmentEndTime1 = LocalTime.parse(nursingInitialAssessmentDto.getAssessmentEndTime());
			Instant assessmentEndTime = assessmentEndTime1.atDate(today).atZone(ZoneId.of("UTC")).toInstant();
			
			nursingInitialAssessmentModel.setCommunicationAndCognitionDesc(nursingInitialAssessmentDto.getCommunicationAndCognitionDesc());
			nursingInitialAssessmentModel.setInvasiveLinesCatheterTubesDrainsDesc(nursingInitialAssessmentDto.getInvasiveLinesCatheterTubesDrainsDesc());
			nursingInitialAssessmentModel.setPsychosocialDesc(nursingInitialAssessmentDto.getPsychosocialDesc());
			nursingInitialAssessmentModel.setRestAndSleepDesc(nursingInitialAssessmentDto.getRestAndSleepDesc());
			nursingInitialAssessmentModel.setPainAssessment(nursingInitialAssessmentDto.getPainAssessment());
			nursingInitialAssessmentModel.setRestAndSleepDesc(nursingInitialAssessmentDto.getRestAndSleepDesc());
			nursingInitialAssessmentModel.setNutritionAssessmentDesc(nursingInitialAssessmentDto.getNutritionAssessmentDesc());
			nursingInitialAssessmentModel.setFallRiskAssessmentDesc(nursingInitialAssessmentDto.getFallRiskAssessmentDesc());
			nursingInitialAssessmentModel.setExaminationDesc(nursingInitialAssessmentDto.getExaminationDesc());
			nursingInitialAssessmentModel.setAssessmentStartTime(assessmentStartTime);
			nursingInitialAssessmentModel.setAssessmentEndTime(assessmentEndTime);
			nursingInitialAssessmentModel.setPatientId(nursingInitialAssessmentDto.getPatientId());
			nursingInitialAssessmentModel.setIsSubmit(nursingInitialAssessmentDto.getIsSubmit());
			nursingInitialAssessmentModel.setEncounterId(nursingInitialAssessmentDto.getEncounterId());
			nursingInitialAssessmentModel.setAdmissionId(nursingInitialAssessmentDto.getAdmissionId());
			nursingInitialAssessmentModel.setArrivedBy(nursingInitialAssessmentDto.getArrivedBy());
			nursingInitialAssessmentModel.setOrganizationId(nursingInitialAssessmentDto.getOrganizationId());
			nursingInitialAssessmentModel.setUnitId(nursingInitialAssessmentDto.getUnitId());
			nursingInitialAssessmentModel.setStatus(nursingInitialAssessmentDto.getStatus());
			nursingInitialAssessmentModel.setCreatedBy(nursingInitialAssessmentDto.getCreatedBy());
			nursingInitialAssessmentModel.setCreatedDate(GlobalCommonDateUtils.getDate(nursingInitialAssessmentDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			nursingInitialAssessmentModel.setUpdatedBy(nursingInitialAssessmentDto.getUpdatedBy());
			nursingInitialAssessmentModel.setUpdatedDate(GlobalCommonDateUtils.getDate(nursingInitialAssessmentDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));

		
			save(nursingInitialAssessmentModel);
		
		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		
		} catch (HibernateException he) {
			logger.error("saveAppointmentVitalMapper failed", he);
		} catch (Exception e) {
			logger.error("saveAppointmentVitalMapper failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getNursingInitialAssessmentList(NursingInitialAssessmentDto nursingInitialAssessmentDto)
			throws ApplicationException {
		try {
			
			Integer id = null;
			if(nursingInitialAssessmentDto.getAdmissionId() != null) {
				id = nursingInitialAssessmentDto.getAdmissionId();
				NursingInitialAssessmentDto dto= (NursingInitialAssessmentDto)sessionFactory.getCurrentSession()
						.getNamedQuery("GET_NURSING_INITIAL_ASSESSMENT_LIST")
						.setParameter("patientId", nursingInitialAssessmentDto.getPatientId())
						.setParameter("organizationId", nursingInitialAssessmentDto.getOrganizationId())
						.setParameter("unitId", nursingInitialAssessmentDto.getUnitId())
						.setParameter("id", id)
						.setMaxResults(1)
						.setResultTransformer(Transformers.aliasToBean(NursingInitialAssessmentDto.class)).uniqueResult();
				return new Response(SUCCESS, SUCCESS_CODE, null, null, dto);
			}else {
				id = nursingInitialAssessmentDto.getEncounterId();
				NursingInitialAssessmentDto dto= (NursingInitialAssessmentDto)sessionFactory.getCurrentSession()
						.getNamedQuery("GET_NURSING_INITIAL_ASSESSMENT_LIST_BY_ENCOUNTER")
						.setParameter("patientId", nursingInitialAssessmentDto.getPatientId())
						.setParameter("organizationId", nursingInitialAssessmentDto.getOrganizationId())
						.setParameter("unitId", nursingInitialAssessmentDto.getUnitId())
						.setParameter("id", id)
						.setMaxResults(1)
						.setResultTransformer(Transformers.aliasToBean(NursingInitialAssessmentDto.class)).uniqueResult();
				return new Response(SUCCESS, SUCCESS_CODE, null, null, dto);
			}
		
		} catch (HibernateException he) {
			logger.error("getNursingInitialAssessmentList failed", he);
		} catch (Exception e) {
			logger.error("getNursingInitialAssessmentList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}
