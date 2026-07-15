package com.param.global.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.IBillingCommonConstants;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.BabyStillAssmntDto;
import com.param.global.dto.NewBornBabyDto;
import com.param.global.model.BabyStillAssessment;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class NewBornBabyDisplayDaoImpl extends GenericDao<BabyStillAssessment, Integer>
		implements INewBornBabyDisplayDao, ICommonConstants, IBillingCommonConstants {
	final static Logger logger = Logger.getLogger(NewBornBabyDisplayDaoImpl.class);


	@Autowired
	private SessionFactory sessionFactory;
	
//	@Autowired
//	private IGlobalUnitMasterDao iGlobalUnitMasterDao;

	public NewBornBabyDisplayDaoImpl() {
		super(BabyStillAssessment.class);
	}

	@Override
	public Response saveStillBirthAssessment(BabyStillAssmntDto babyStillAssmntDto) throws ApplicationException {
		try {
			int count = ((Long)getSession().createQuery("SELECT count(*) FROM BabyStillAssessment as still "
					+ "WHERE still.status='A' AND still.uhidNumberBaby=:UHID "
					+ "AND still.unitId=:unitId AND still.organizationId=:organizationId")
					.setParameter( "UHID", babyStillAssmntDto.getUhidNumberBaby() )
					.setParameter("unitId", babyStillAssmntDto.getUnitId())
					.setParameter("organizationId", babyStillAssmntDto.getOrganizationId())
					.uniqueResult()).intValue();
			if(count == 0) {
				BabyStillAssessment newBornStillBaby=new BabyStillAssessment();
					newBornStillBaby.setPatientId(babyStillAssmntDto.getPatientId());
					newBornStillBaby.setUhidNumberBaby(babyStillAssmntDto.getUhidNumberBaby());
					newBornStillBaby.setStillType(babyStillAssmntDto.getStillType());
					newBornStillBaby.setWeight(babyStillAssmntDto.getWeight());
					newBornStillBaby.setStatus(babyStillAssmntDto.getStatus());
					newBornStillBaby.setUnitId(babyStillAssmntDto.getUnitId());
					newBornStillBaby.setOrganizationId(babyStillAssmntDto.getOrganizationId());
					newBornStillBaby.setCreatedBy(babyStillAssmntDto.getCreatedBy());
					newBornStillBaby.setCreatedDate(babyStillAssmntDto.getCreatedDate());
					newBornStillBaby.setUpdatedBy(babyStillAssmntDto.getUpdatedBy());
					newBornStillBaby.setUpdatedDate(babyStillAssmntDto.getUpdatedDate());
				sessionFactory.getCurrentSession().save(newBornStillBaby);
			
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
			}else {
				return new Response(ERROR, null, "Record Already Exist !!! ", null, null);
			}
		} catch (HibernateException he) {
		logger.error("saveStillBirthAssessment failed", he);
		} catch (Exception e) {
		logger.error("saveStillBirthAssessment failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getStillBabyDetails(BabyStillAssmntDto babyStillAssmntDto) throws ApplicationException {
		try {
			List<BabyStillAssmntDto> list =  sessionFactory.getCurrentSession()
					.getNamedQuery("GET_STILL_BABY_UHID")
					.setParameter("uhidNumberBaby", babyStillAssmntDto.getUhidNumberBaby())
					.setParameter("organizationId", babyStillAssmntDto.getOrganizationId())
					.setParameter("unitId", babyStillAssmntDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(BabyStillAssessment.class)).list();
			return new Response(SUCCESS, null, null, list, null);
		} catch (HibernateException he) {
		logger.error("getStillBabyDetails failed", he);
		} catch (Exception e) {
		logger.error("getStillBabyDetails failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getSingleStillBaby(BabyStillAssmntDto babyStillAssmntDto) throws ApplicationException {
		try {
			BabyStillAssessment object = (BabyStillAssessment) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SINGLE_STILL_BABY_REC")
					.setParameter("newBornBabyStillId", babyStillAssmntDto.getNewBornBabyStillId())
					.setParameter("organizationId", babyStillAssmntDto.getOrganizationId())
					.setParameter("unitId", babyStillAssmntDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(BabyStillAssessment.class)).uniqueResult();
			return new Response(SUCCESS, null, null, null, object);
		} catch (HibernateException he) {
		logger.error("getSingleStillBaby failed", he);
		} catch (Exception e) {
		logger.error("getSingleStillBaby failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateStillBirthAssessment(BabyStillAssmntDto babyStillAssmntDto) throws ApplicationException {
		try {
			BabyStillAssessment newBornStillBaby = findById(babyStillAssmntDto.getNewBornBabyStillId());
				newBornStillBaby.setStillType(babyStillAssmntDto.getStillType());
				newBornStillBaby.setWeight(babyStillAssmntDto.getWeight());
				newBornStillBaby.setUpdatedBy(babyStillAssmntDto.getUpdatedBy());
				newBornStillBaby.setUpdatedDate(babyStillAssmntDto.getUpdatedDate());
			sessionFactory.getCurrentSession().update(newBornStillBaby);
			
			return new Response<NewBornBabyDto, Integer>(SUCCESS, null, COMMON_SUCCESS, null, null);
		} catch (HibernateException he) {
		logger.error("updateStillBirthAssessment failed", he);
		} catch (Exception e) {
		logger.error("updateStillBirthAssessment failed", e);
		}
		return new Response<NewBornBabyDto, Integer>(ERROR, null, null, null, null);
	}
	

}
