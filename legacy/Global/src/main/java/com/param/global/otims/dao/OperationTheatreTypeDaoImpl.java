package com.param.global.otims.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import in.param.exception.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.otims.dto.OperationTheatreTypeDto;
import com.param.otims.model.OperationTheatreType;

import in.param.common.dao.GenericDao;

@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class OperationTheatreTypeDaoImpl extends GenericDao<OperationTheatreType, Integer> implements IOperationTheatreTypeDao, ICommonConstants{

	private static final Logger logger = Logger.getLogger(OperationTheatreTypeDaoImpl.class);

	public OperationTheatreTypeDaoImpl() {
		super(OperationTheatreType.class);

	}
	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response saveOperationTheatreType(OperationTheatreTypeDto operationTheatreTypeDto)
			throws ApplicationException {
		try{
			OperationTheatreType operationTheatreType=new OperationTheatreType();
			operationTheatreType.setTheatreTypeCode(operationTheatreTypeDto.getTheatreTypeCode());
			operationTheatreType.setTheatreTypeDesc(operationTheatreTypeDto.getTheatreTypeDesc());
			operationTheatreType.setTheatreTypeName(operationTheatreTypeDto.getTheatreTypeName());
			operationTheatreType.setOrgId(operationTheatreTypeDto.getOrgId());
			operationTheatreType.setUnitId(operationTheatreTypeDto.getUnitId());
			operationTheatreType.setCreatedBy(operationTheatreTypeDto.getCreatedBy());
			operationTheatreType.setCreatedDate(GlobalCommonDateUtils.getDate(operationTheatreTypeDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			operationTheatreType.setUpdatedBy(operationTheatreTypeDto.getUpdatedBy());
			operationTheatreType.setUpdatedDate(GlobalCommonDateUtils.getDate(operationTheatreTypeDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			operationTheatreType.setStatus(operationTheatreTypeDto.getStatus());
			
			operationTheatreType=save(operationTheatreType);
			
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, operationTheatreType);

		} catch (HibernateException he) {
			logger.error("saveOperationTheatreType failed", he);
		} catch (Exception e) {
			logger.error("saveOperationTheatreType failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateOperationTheatreType(OperationTheatreTypeDto operationTheatreTypeDto)
			throws ApplicationException {
		try{
			OperationTheatreType operationTheatreType=findById(operationTheatreTypeDto.getOperationTheatreTypeId());
			operationTheatreType.setTheatreTypeCode(operationTheatreTypeDto.getTheatreTypeCode());
			operationTheatreType.setTheatreTypeDesc(operationTheatreTypeDto.getTheatreTypeDesc());
			operationTheatreType.setTheatreTypeName(operationTheatreTypeDto.getTheatreTypeName());
			operationTheatreType.setOrgId(operationTheatreTypeDto.getOrgId());
			operationTheatreType.setUnitId(operationTheatreTypeDto.getUnitId());
			operationTheatreType.setUpdatedBy(operationTheatreTypeDto.getUpdatedBy());
			operationTheatreType.setUpdatedDate(GlobalCommonDateUtils.getDate(operationTheatreTypeDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
	
			update(operationTheatreType);
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);

		} catch (HibernateException he) {
			logger.error("updateOperationTheatreType failed", he);
		} catch (Exception e) {
			logger.error("updateOperationTheatreType failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateOperationTheatreTypeStatus(OperationTheatreTypeDto operationTheatreTypeDto)
			throws ApplicationException {
		try{
			OperationTheatreType operationTheatreType=findById(operationTheatreTypeDto.getOperationTheatreTypeId());
			operationTheatreType.setUpdatedBy(operationTheatreTypeDto.getUpdatedBy());
			operationTheatreType.setUpdatedDate(GlobalCommonDateUtils.getDate(operationTheatreTypeDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			operationTheatreType.setStatus(operationTheatreTypeDto.getStatus());
			
			update(operationTheatreType);
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);

		} catch (HibernateException he) {
			logger.error("updateOperationTheatreTypeStatus failed", he);
		} catch (Exception e) {
			logger.error("updateOperationTheatreTypeStatus failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getOperationTheatreTypeById(Integer operationTheatreTypeId, Integer orgId, Integer unitId)
			throws ApplicationException {
		try{
			List<OperationTheatreTypeDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_OPERATION_THEATRE_TYPE_BY_ID")
					.setParameter("operationTheatreTypeId", operationTheatreTypeId)
					.setParameter("orgId", orgId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(OperationTheatreTypeDto.class)).list();
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
			
		} catch (HibernateException he) {
		logger.error("getOperationTheatreTypeById failed", he);
		} catch (Exception e) {
		logger.error("getOperationTheatreTypeById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	
	@Override
	public Response getOperationTheatreTypeList(OperationTheatreTypeDto operationTheatreTypeDto)
			throws ApplicationException {
		try{
			List<OperationTheatreTypeDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_OPERATION_THEATRE_TYPE_LIST")
					.setParameter("orgId", operationTheatreTypeDto.getOrgId())
					.setParameter("unitId", operationTheatreTypeDto.getUnitId())
					.setFirstResult(operationTheatreTypeDto.getOffset() != null ? operationTheatreTypeDto.getOffset() : 0)
					.setMaxResults(operationTheatreTypeDto.getNoOfRecordsPerPage() != null ? operationTheatreTypeDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(OperationTheatreTypeDto.class)).list();
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
			
		} catch (HibernateException he) {
		logger.error("getOperationTheatreTypeList failed", he);
		} catch (Exception e) {
		logger.error("getOperationTheatreTypeList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	
	@Override
	public Response getActiveOperationTheatreTypeList(OperationTheatreTypeDto operationTheatreTypeDto)
			throws ApplicationException {
		try{
			List<OperationTheatreTypeDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_OPERATION_THEATRE_TYPE_LIST")
					.setParameter("orgId", operationTheatreTypeDto.getOrgId())
					.setParameter("unitId", operationTheatreTypeDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(OperationTheatreTypeDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
			
		} catch (HibernateException he) {
		logger.error("getActiveOperationTheatreTypeList failed", he);
		} catch (Exception e) {
		logger.error("getActiveOperationTheatreTypeList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	
	@Override
	public Response getOperationTheatreTypeCount(OperationTheatreTypeDto operationTheatreTypeDto)
			throws ApplicationException {
		try{
			Session session = sessionFactory.getCurrentSession();
			Criteria c = session.createCriteria(OperationTheatreType.class);
			c.add(Restrictions.eq("orgId", operationTheatreTypeDto.getOrgId()));
			c.add(Restrictions.eq("unitId", operationTheatreTypeDto.getUnitId()));
			c.setProjection(Projections.rowCount());
			Long count = (Long) c.uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, count);
					
		} catch (HibernateException he) {
		logger.error("getOperationTheatreTypeCount failed", he);
		} catch (Exception e) {
		logger.error("getOperationTheatreTypeCount failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getOperationTheatreTypeByName(OperationTheatreTypeDto operationTheatreTypeDto)
			throws ApplicationException {
		try{
			List<OperationTheatreTypeDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_OPERATION_THEATRE_TYPE_BY_NAME")
					.setParameter("orgId", operationTheatreTypeDto.getOrgId())
					.setParameter("unitId", operationTheatreTypeDto.getUnitId())
					.setParameter("theatreTypeDesc", operationTheatreTypeDto.getTheatreTypeDesc().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(OperationTheatreTypeDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
			
		} catch (HibernateException he) {
		logger.error("getOperationTheatreTypeByName failed", he);
		} catch (Exception e) {
		logger.error("getOperationTheatreTypeByName failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getOperationTheatreTypeByNameNotId(OperationTheatreTypeDto operationTheatreTypeDto)
			throws ApplicationException {
		try{
			List<OperationTheatreTypeDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_OPERATION_THEATRE_TYPE_BY_NAME_NOT_ID")
					.setParameter("operationTheatreTypeId", operationTheatreTypeDto.getOperationTheatreTypeId())
					.setParameter("orgId", operationTheatreTypeDto.getOrgId())
					.setParameter("unitId", operationTheatreTypeDto.getUnitId())
					.setParameter("theatreTypeDesc", operationTheatreTypeDto.getTheatreTypeDesc().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(OperationTheatreTypeDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
			
		} catch (HibernateException he) {
		logger.error("getOperationTheatreTypeByNameNotId failed", he);
		} catch (Exception e) {
		logger.error("getOperationTheatreTypeByNameNotId failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}
