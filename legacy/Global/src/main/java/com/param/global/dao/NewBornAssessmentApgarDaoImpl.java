package com.param.global.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.NewBornApgarDto;
import com.param.global.model.NewBornAssessmentApgar;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class NewBornAssessmentApgarDaoImpl extends GenericDao<NewBornAssessmentApgar, Integer>
		implements INewBornApgarDao, ICommonConstants {
	final static Logger logger = Logger.getLogger(NewBornAssessmentApgarDaoImpl.class);


	@Autowired
	private SessionFactory sessionFactory;

	public NewBornAssessmentApgarDaoImpl() {
		super(NewBornAssessmentApgar.class);
	}
	

	@Override
	public Response getNewBornApgarList(NewBornApgarDto dto) throws ApplicationException {
		try {
			List<NewBornApgarDto> apgarList =  sessionFactory.getCurrentSession()
					.getNamedQuery("GET_NEW_BORN_APGAR_DATA")
					.setParameter("assessmentId", dto.getAssessmentId())
					.setParameter("organizationId", dto.getOrganizationId())
					.setParameter("unitId", dto.getUnitId())
					.setParameter("status", 'A')
					.setResultTransformer(Transformers.aliasToBean(NewBornAssessmentApgar.class)).list();
			return new Response(SUCCESS, null, null, apgarList, null);
		}catch (HibernateException he) {
		logger.error("getNewBornApgarList failed", he);
		} catch (Exception e) {
		logger.error("getNewBornApgarList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateNewBornApgar(NewBornApgarDto dto) throws ApplicationException {
		try {
			NewBornAssessmentApgar apgar;
			if(dto.getApgarId()!=0) {
				apgar =findById(dto.getApgarId());
			}else {
				apgar = new NewBornAssessmentApgar();
				apgar.setAssessmentId(dto.getAssessmentId());
				apgar.setTime(dto.getTime());
				apgar.setOrganizationId(dto.getOrganizationId());
				apgar.setUnitId(dto.getUnitId());
			}
			apgar.setColorScore(dto.getColorScore());
			apgar.setHeartRateScore(dto.getHeartRateScore());
			apgar.setReflexScore(dto.getReflexScore());
			apgar.setMuscleScore(dto.getMuscleScore());
			apgar.setRespirationScore(dto.getRespirationScore());
			apgar.setTotalScore(dto.getTotalScore());
			apgar.setApgarRemark(dto.getApgarRemark());
			apgar.setUpdatedBy(dto.getUpdatedBy());
			apgar.setUpdatedDate(dto.getUpdatedDate());
			apgar.setStatus('A');
			
			sessionFactory.getCurrentSession().saveOrUpdate(apgar);
			return new Response<NewBornApgarDto, Integer>(SUCCESS, null, COMMON_SUCCESS, null, null);

		}catch (HibernateException he) {
		logger.error("updateNewBornApgar failed", he);
		} catch (Exception e) {
		logger.error("updateNewBornApgar failed", e);
		}
		return new Response(ERROR, null, null, null, null);
		}
	
	
	@Override
	public Response saveSingleApgarScore(NewBornApgarDto dto) throws ApplicationException {
		try {
			NewBornAssessmentApgar apgar = new NewBornAssessmentApgar();
			apgar.setAssessmentId(dto.getAssessmentId());
			apgar.setTime(dto.getTime());
			apgar.setColorScore(dto.getColorScore());
			apgar.setHeartRateScore(dto.getHeartRateScore());
			apgar.setReflexScore(dto.getReflexScore());
			apgar.setMuscleScore(dto.getMuscleScore());
			apgar.setRespirationScore(dto.getRespirationScore());
			apgar.setTotalScore(dto.getTotalScore());
			apgar.setApgarRemark(dto.getApgarRemark());
			apgar.setCreatedBy(dto.getCreatedBy());
			apgar.setCreatedDate(dto.getCreatedDate());
			apgar.setUnitId(dto.getUnitId());
			apgar.setStatus('A');
			apgar.setOrganizationId(dto.getOrganizationId());
			Integer id = (Integer) sessionFactory.getCurrentSession().save(apgar);
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, id);
		}catch (HibernateException he) {
		logger.error("saveSingleApgarScore failed", he);
		} catch (Exception e) {
		logger.error("saveSingleApgarScore failed", e);
		}
		return new Response(ERROR, null, null, null, null);
		}

	
	
}
