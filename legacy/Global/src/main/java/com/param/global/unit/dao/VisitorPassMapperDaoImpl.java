package com.param.global.unit.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.adt.admission.model.VisitorPassMapper;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.unit.dto.PassTypeMasterDto;
import com.param.global.unit.dto.VisitorPassMapperDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class VisitorPassMapperDaoImpl extends GenericDao<VisitorPassMapper, Integer>
		implements IVisitorPassMapperDao, ICommonConstants {
	final static Logger logger = Logger.getLogger(VisitorPassMapperDaoImpl.class);


	@Autowired
	SessionFactory sessionFactory;
	public VisitorPassMapperDaoImpl() {
		super(VisitorPassMapper.class);
	}

	
	@Override
	public Response saveVisitorPassMapper(VisitorPassMapperDto dto) throws ApplicationException {
		try {
			VisitorPassMapper mppr =new VisitorPassMapper();
			mppr.setBedCategoryId(dto.getBedCategoryId());
			mppr.setNoOfPass(dto.getNoOfPass());
			mppr.setPassTypeId(dto.getPassTypeId());
			mppr.setVisitorTypePassId(dto.getVisitorTypePassId());
			mppr.setCreatedBy(dto.getCreatedBy());
			mppr.setUpdatedBy(dto.getUpdatedBy());
			mppr.setUnitId(dto.getUnitId());
			mppr.setOrganisationId(dto.getOrganisationId());
			mppr.setCreatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
			mppr.setUpdatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
			mppr.setStatus('A');
			sessionFactory.getCurrentSession().save(mppr);
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (HibernateException he) {
		logger.error("saveVisitorPassMapper failed", he);
		} catch (Exception e) {
		logger.error("saveVisitorPassMapper failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response updateVisitorPassMapper(VisitorPassMapperDto dto) throws ApplicationException {
		try {
			VisitorPassMapper mppr =findById(dto.getVisitorPassMapperId());
			mppr.setBedCategoryId(dto.getBedCategoryId());
			mppr.setNoOfPass(dto.getNoOfPass());
			mppr.setPassTypeId(dto.getPassTypeId());
			mppr.setUpdatedBy(dto.getUpdatedBy());
			mppr.setVisitorTypePassId(dto.getVisitorTypePassId());
			mppr.setUpdatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
			sessionFactory.getCurrentSession().update(mppr);
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (HibernateException he) {
		logger.error("updateVisitorPassMapper failed", he);
		} catch (Exception e) {
		logger.error("updateVisitorPassMapper failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response updateVisitorPassMapperStatus(VisitorPassMapperDto dto) throws ApplicationException {
		try {
			VisitorPassMapper mppr =findById(dto.getVisitorPassMapperId());
			mppr.setUpdatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
			mppr.setUpdatedBy(dto.getUpdatedBy());
			mppr.setStatus(dto.getStatus() =='A' ? 'A' :'I');
			sessionFactory.getCurrentSession().update(mppr);
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (HibernateException he) {
		logger.error("updateVisitorPassMapperStatus failed", he);
		} catch (Exception e) {
		logger.error("updateVisitorPassMapperStatus failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response getVisitorPassMapperList(VisitorPassMapperDto dto) throws ApplicationException {
		try {
			List<VisitorPassMapperDto> dtoList=sessionFactory.getCurrentSession().getNamedQuery("GET_VISITOR_PASS_MAPPER_LIST")
					.setParameter("orgId", dto.getOrganisationId())
					.setParameter("unitId", dto.getUnitId())
					.setFirstResult(dto.getOffset() != null ? dto.getOffset() : 0)
					.setMaxResults(dto.getNoOfRecordsPerPage() != null ? dto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(VisitorPassMapperDto.class)).list();
				return new Response<>(SUCCESS, SUCCESS_CODE, null, dtoList, null);
		} catch (HibernateException he) {
		logger.error("getVisitorPassMapperList failed", he);
		} catch (Exception e) {
		logger.error("getVisitorPassMapperList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response getVisitorPassMapperCount(VisitorPassMapperDto dto) throws ApplicationException {
		try {
			Criteria  c=sessionFactory.getCurrentSession().createCriteria(VisitorPassMapper.class);
			c.add(Restrictions.eq("organisationId", dto.getOrganisationId()));
			c.add(Restrictions.eq("unitId",dto.getUnitId()));
			c.setProjection(Projections.rowCount());  
			Long count = (Long)c.uniqueResult();
			return new Response<>(SUCCESS, null, null, null, count);
		} catch (HibernateException he) {
		logger.error("getVisitorPassMapperCount failed", he);
		} catch (Exception e) {
		logger.error("getVisitorPassMapperCount failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response getVisitorPassMapperById(VisitorPassMapperDto dto) throws ApplicationException {
		try {
			List<VisitorPassMapperDto> dtoList=sessionFactory.getCurrentSession().getNamedQuery("GET_VISITOR_PASS_MAPPER_BY_ID")
					.setParameter("id", dto.getVisitorPassMapperId())
					.setResultTransformer(Transformers.aliasToBean(VisitorPassMapperDto.class)).list();
				return new Response<>(SUCCESS, SUCCESS_CODE, null, dtoList, null);
		} catch (HibernateException he) {
		logger.error("getVisitorPassMapperById failed", he);
		} catch (Exception e) {
		logger.error("getVisitorPassMapperById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}


	@Override
	public Response checkVisitorPassMapperAlreadyExistOrNot(VisitorPassMapperDto dto) throws ApplicationException {
		try{
			List<VisitorPassMapperDto> dtoList=sessionFactory.getCurrentSession().getNamedQuery("CHECK_VISITOR_PASS_MAPPER_ALREADY_EXIST_OR_NOT")
					.setParameter("orgId", dto.getOrganisationId())
					.setParameter("unitId", dto.getUnitId())
					.setParameter("bedCategoryId", dto.getBedCategoryId())
					.setParameter("passTypeId", dto.getPassTypeId())
					.setParameter("visitorPassTypeId", dto.getVisitorTypePassId())
					.setResultTransformer(Transformers.aliasToBean(VisitorPassMapperDto.class)).list();
				return new Response<>(SUCCESS, SUCCESS_CODE, null, dtoList, null);
		}catch(HibernateException he){
		logger.error("checkVisitorPassMapperAlreadyExistOrNot failed", he);
		}
		catch(Exception e){
		logger.error("checkVisitorPassMapperAlreadyExistOrNot failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}


	@Override
	public Response checkVisitorPassMapperAlreadyExistOrNotById(VisitorPassMapperDto dto) throws ApplicationException {
		try{
			List<VisitorPassMapperDto> dtoList=sessionFactory.getCurrentSession().getNamedQuery("CHECK_VISITOR_PASS_MAPPER_ALREADY_EXIST_OR_NOT_BY_ID")
					.setParameter("orgId", dto.getOrganisationId())
					.setParameter("unitId", dto.getUnitId())
					.setParameter("bedCategoryId", dto.getBedCategoryId())
					.setParameter("passTypeId", dto.getPassTypeId())
					.setParameter("visitorPassTypeId", dto.getVisitorTypePassId())
					.setParameter("id", dto.getVisitorPassMapperId())
					.setResultTransformer(Transformers.aliasToBean(VisitorPassMapperDto.class)).list();
				return new Response<>(SUCCESS, SUCCESS_CODE, null, dtoList, null);
		}catch(HibernateException he){
		logger.error("checkVisitorPassMapperAlreadyExistOrNotById failed", he);
		}
		catch(Exception e){
		logger.error("checkVisitorPassMapperAlreadyExistOrNotById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}


	@Override
	public Response getVisitorPassCountByBedCategoryByVisitorPassIdByPassId(VisitorPassMapperDto dto)
			throws ApplicationException {
		try{
			Long cnt=(Long)sessionFactory.getCurrentSession().getNamedQuery("GET_VISITORPASSCOUNT_BY_BEDCATEGORY_BY_VISITORPASSID_BY_PASSID")
					.setParameter("orgId", dto.getOrganisationId())
					.setParameter("unitId", dto.getUnitId())
					.setParameter("bedCategoryId", dto.getBedCategoryId())
					.setParameter("passTypeId", dto.getPassTypeId())
					.setParameter("visitorPassTypeId", dto.getVisitorTypePassId()).uniqueResult();
				return new Response<>(SUCCESS, SUCCESS_CODE, null, null, cnt);
		}catch(HibernateException he){
		logger.error("getVisitorPassCountByBedCategoryByVisitorPassIdByPassId failed", he);
		}
		catch(Exception e){
		logger.error("getVisitorPassCountByBedCategoryByVisitorPassIdByPassId failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}


	@Override
	public Response getVisitorPassMapperListByBedCategoryNVisitType(VisitorPassMapperDto dto)
			throws ApplicationException {
		try {
			List<PassTypeMasterDto> dtoList=sessionFactory.getCurrentSession().getNamedQuery("GET_VISITOR_PASS_LIST_BY_BEDCAT_VISITTYPE")
					.setParameter("orgId", dto.getOrganisationId())
					.setParameter("unitId", dto.getUnitId())
					.setParameter("bedCategoryId", dto.getBedCategoryId())
					.setParameter("visitId", dto.getVisitTypeId())
					.setResultTransformer(Transformers.aliasToBean(PassTypeMasterDto.class)).list();
				return new Response<>(SUCCESS, SUCCESS_CODE, null, dtoList, null);
		} catch (HibernateException he) {
		logger.error("getVisitorPassMapperListByBedCategoryNVisitType failed", he);
		} catch (Exception e) {
		logger.error("getVisitorPassMapperListByBedCategoryNVisitType failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

}
