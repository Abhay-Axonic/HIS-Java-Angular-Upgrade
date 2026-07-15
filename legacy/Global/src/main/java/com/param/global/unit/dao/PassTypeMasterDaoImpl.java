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

import com.param.adt.admission.model.VisitorPassTypeMaster;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.unit.dto.PassTypeMasterDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class PassTypeMasterDaoImpl extends GenericDao<VisitorPassTypeMaster, Integer>
		implements IPassTypeMasterDao, ICommonConstants {
	final static Logger logger = Logger.getLogger(PassTypeMasterDaoImpl.class);


	@Autowired 
	SessionFactory sessionFactory;
	public PassTypeMasterDaoImpl() {
		super(VisitorPassTypeMaster.class);
	}

	@Override
	public Response savePassTypeMaster(PassTypeMasterDto dto) throws ApplicationException {
		try {
			VisitorPassTypeMaster master=new VisitorPassTypeMaster();
			master.setCreatedBy(dto.getCreatedBy());
			master.setUpdatedBy(dto.getUpdatedBy());
			master.setCreatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
			master.setUpdatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
			master.setVisitorPassTypeCode(dto.getVisitorPassTypeCode());
			master.setVisitorPassTypeDesc(dto.getVisitorPassTypeDesc());
			master.setUnitId(dto.getUnitId());
			master.setOrganizationId(dto.getOrganizationId());
			master.setStatus('A');
			sessionFactory.getCurrentSession().save(master);
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (HibernateException he) {
		logger.error("savePassTypeMaster failed", he);
		} catch (Exception e) {
		logger.error("savePassTypeMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response updateDiscountCategory(PassTypeMasterDto dto) throws ApplicationException {
		try {
			VisitorPassTypeMaster visitorPassTypeMaster= findById(dto.getVisitorPassTypeId());
			visitorPassTypeMaster.setUpdatedBy(dto.getUpdatedBy());
			visitorPassTypeMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
			visitorPassTypeMaster.setVisitorPassTypeCode(dto.getVisitorPassTypeCode());
			visitorPassTypeMaster.setVisitorPassTypeDesc(dto.getVisitorPassTypeDesc());
			sessionFactory.getCurrentSession().update(visitorPassTypeMaster);
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (HibernateException he) {
		logger.error("updateDiscountCategory failed", he);
		} catch (Exception e) {
		logger.error("updateDiscountCategory failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response updatePassTypeMasterStatus(PassTypeMasterDto dto) throws ApplicationException {
		try {
			VisitorPassTypeMaster master=findById(dto.getVisitorPassTypeId());
			master.setUpdatedBy(dto.getUpdatedBy());
			master.setUpdatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
			master.setStatus(dto.getStatus()=='A' ? dto.getStatus() : 'I');
			sessionFactory.getCurrentSession().update(master);
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (HibernateException he) {
		logger.error("updatePassTypeMasterStatus failed", he);
		} catch (Exception e) {
		logger.error("updatePassTypeMasterStatus failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	
	@Override
	public Response getPassTypeMasterList(PassTypeMasterDto dto) throws ApplicationException {
		try {
			List<PassTypeMasterDto> list=sessionFactory.getCurrentSession().getNamedQuery("GET_VISITOR_TYPE_PASS_LIST")
					.setParameter("orgId", dto.getOrganizationId())
					.setParameter("unitId", dto.getUnitId())
					.setFirstResult(dto.getOffset() != null ? dto.getOffset() : 0)
					.setMaxResults(dto.getNoOfRecordsPerPage() != null ? dto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(PassTypeMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, list, null);
		} catch (HibernateException he) {
		logger.error("getPassTypeMasterList failed", he);
		} catch (Exception e) {
		logger.error("getPassTypeMasterList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response getPassTypeMasterCount(PassTypeMasterDto dto) throws ApplicationException {
		try {
			Criteria  c=sessionFactory.getCurrentSession().createCriteria(VisitorPassTypeMaster.class);
			c.add(Restrictions.eq("organizationId", dto.getOrganizationId()));
			c.add(Restrictions.eq("unitId",dto.getUnitId()));
			c.setProjection(Projections.rowCount());  
			Long count = (Long)c.uniqueResult();
			return new Response<>(SUCCESS, null, null, null, count);
		} catch (HibernateException he) {
		logger.error("getPassTypeMasterCount failed", he);
		} catch (Exception e) {
		logger.error("getPassTypeMasterCount failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response getPassTypeMasterById(PassTypeMasterDto dto) throws ApplicationException {
		try {
			List<PassTypeMasterDto> list=sessionFactory.getCurrentSession().getNamedQuery("GET_PASS_TYPE_MASTER_BY_ID")
//					.setParameter("orgId", dto.getOrganizationId())
//					.setParameter("unitId", dto.getUnitId())
					.setParameter("id", dto.getVisitorPassTypeId())					
					.setResultTransformer(Transformers.aliasToBean(PassTypeMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, list, null);
		} catch (HibernateException he) {
		logger.error("getPassTypeMasterById failed", he);
		} catch (Exception e) {
		logger.error("getPassTypeMasterById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response checkPassTypeMasterAlreadyExistOrNot(PassTypeMasterDto dto) throws ApplicationException {
		try {
			List<PassTypeMasterDto> list=sessionFactory.getCurrentSession().getNamedQuery("CHECK_PASS_TYPE_MASTER_ALREADY_EXIST_OR_NOT")
					.setParameter("orgId", dto.getOrganizationId())
					.setParameter("unitId", dto.getUnitId())
					.setParameter("code", dto.getVisitorPassTypeCode().toLowerCase())
					.setParameter("desc", dto.getVisitorPassTypeDesc().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(PassTypeMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, list, null);
		} catch (HibernateException he) {
		logger.error("checkPassTypeMasterAlreadyExistOrNot failed", he);
		} catch (Exception e) {
		logger.error("checkPassTypeMasterAlreadyExistOrNot failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response checkPassTypeNotById(PassTypeMasterDto dto) throws ApplicationException {
		try {
			List<PassTypeMasterDto> list=sessionFactory.getCurrentSession().getNamedQuery("CHECK_PASS_TYPE_NOT_BY_ID")
					.setParameter("orgId", dto.getOrganizationId())
					.setParameter("unitId", dto.getUnitId())
					.setParameter("code", dto.getVisitorPassTypeCode().toLowerCase())
					.setParameter("desc", dto.getVisitorPassTypeDesc().toLowerCase())
					.setParameter("id", dto.getVisitorPassTypeId())
					.setResultTransformer(Transformers.aliasToBean(PassTypeMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, list, null);
		} catch (HibernateException he) {
		logger.error("checkPassTypeNotById failed", he);
		} catch (Exception e) {
		logger.error("checkPassTypeNotById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response getActivePassTypeMasterList(PassTypeMasterDto dto) throws ApplicationException {
		try{
			List<PassTypeMasterDto> list=sessionFactory.getCurrentSession().getNamedQuery("GET_ACTIVE_VISITOR_TYPE_LIST")
					.setParameter("orgId", dto.getOrganizationId())
					.setParameter("unitId", dto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(PassTypeMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, list, null);
		}catch(HibernateException he){
		logger.error("getActivePassTypeMasterList failed", he);
		}
		catch(Exception e){
		logger.error("getActivePassTypeMasterList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

}
