package com.param.global.dao;


import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.NewBornAssessmentDto;
import com.param.global.model.NewBornAssessment;
import com.param.global.model.NewBornAssessmentApgar;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class NewBornAssessmentDaoImpl extends GenericDao<NewBornAssessment, Integer>
		implements INewBornAssessmentDao, ICommonConstants {
	final static Logger logger = Logger.getLogger(NewBornAssessmentDaoImpl.class);


	@Autowired
	private SessionFactory sessionFactory;

	public NewBornAssessmentDaoImpl() {
		super(NewBornAssessment.class);
	}
	

	@Override
	public Response saveNewBornAssessment(NewBornAssessmentDto assessmentDto) throws ApplicationException {
		try {
			NewBornAssessment assess =new NewBornAssessment();
				assess.setUhidNumberBaby(assessmentDto.getUhidNumberBaby());
				assess.setBreastFeeding(assessmentDto.getBreastFeeding());
				assess.setTransferOfBabyTo(assessmentDto.getTransferOfBabyTo());
				assess.setCordClamping(assessmentDto.getCordClamping());
				assess.setRemarks(assessmentDto.getRemarks());
				assess.setBloodPressureDiastolic(assessmentDto.getBloodPressureDiastolic());
				assess.setBloodPressureSystolic(assessmentDto.getBloodPressureSystolic());
				assess.setHeadCircumference(assessmentDto.getHeadCircumference());
				assess.setHeight(assessmentDto.getHeight());
				assess.setPulseRate(assessmentDto.getPulseRate());
				assess.setRespiration(assessmentDto.getRespiration());
				assess.setTemperature(assessmentDto.getTemperature());
				assess.setWeight(assessmentDto.getWeight());
				assess.setStatus(assessmentDto.getStatus());
				assess.setOrganizationId(assessmentDto.getOrganizationId());
				assess.setUnitId(assessmentDto.getUnitId());
				assess.setCreatedBy(assessmentDto.getCreatedBy());
				assess.setCreatedDate(assessmentDto.getCreatedDate());
			Integer id= (Integer)sessionFactory.getCurrentSession().save(assess);
			
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, id);
		}catch (HibernateException he) {
		logger.error("saveNewBornAssessment failed", he);
		} catch (Exception e) {
		logger.error("saveNewBornAssessment failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response saveNewBornAssessmentApgar(NewBornAssessmentApgar apagr) throws ApplicationException {
		try {
				sessionFactory.getCurrentSession().save(apagr);
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		}catch (HibernateException he) {
		logger.error("saveNewBornAssessmentApgar failed", he);
		} catch (Exception e) {
		logger.error("saveNewBornAssessmentApgar failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getNewBornAssessmentDetails(NewBornAssessmentDto dto) throws ApplicationException {
		try {
			
			NewBornAssessmentDto assessObj =  (NewBornAssessmentDto)sessionFactory.getCurrentSession()
					.getNamedQuery("GET_NEW_BORN_ASSESSMENT_DATA")
					.setParameter("uhidNumberBaby", dto.getUhidNumberBaby())
					.setParameter("organizationId", dto.getOrganizationId())
					.setParameter("unitId", dto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(NewBornAssessmentDto.class)).uniqueResult();
			return new Response(SUCCESS, null, null, null, assessObj);
		}catch (HibernateException he) {
		logger.error("getNewBornAssessmentDetails failed", he);
		} catch (Exception e) {
		logger.error("getNewBornAssessmentDetails failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@Override
	public Response updateNewBornAssessment(NewBornAssessmentDto assessmentDto) throws ApplicationException {
		try {
			
			NewBornAssessment assess =findById(assessmentDto.getAssessmentId());
				assess.setBreastFeeding(assessmentDto.getBreastFeeding());
				assess.setTransferOfBabyTo(assessmentDto.getTransferOfBabyTo());
				assess.setCordClamping(assessmentDto.getCordClamping());
				assess.setRemarks(assessmentDto.getRemarks());
				assess.setBloodPressureDiastolic(assessmentDto.getBloodPressureDiastolic());
				assess.setBloodPressureSystolic(assessmentDto.getBloodPressureSystolic());
				assess.setHeadCircumference(assessmentDto.getHeadCircumference());
				assess.setHeight(assessmentDto.getHeight());
				assess.setPulseRate(assessmentDto.getPulseRate());
				assess.setRespiration(assessmentDto.getRespiration());
				assess.setTemperature(assessmentDto.getTemperature());
				assess.setWeight(assessmentDto.getWeight());
				assess.setStatus(assessmentDto.getStatus());
				assess.setUpdatedBy(assessmentDto.getUpdatedBy());
				assess.setUpdatedDate(assessmentDto.getUpdatedDate().toString());
			sessionFactory.getCurrentSession().update(assess);
			
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		}catch (HibernateException he) {
		logger.error("updateNewBornAssessment failed", he);
		} catch (Exception e) {
		logger.error("updateNewBornAssessment failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	
}
