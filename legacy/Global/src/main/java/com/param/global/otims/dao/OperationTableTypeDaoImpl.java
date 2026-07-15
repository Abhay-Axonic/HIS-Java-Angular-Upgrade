package com.param.global.otims.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.otims.dto.OperationTableTypeDto;
import com.param.otims.model.OperationTableType;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "unchecked", "rawtypes" })
public class OperationTableTypeDaoImpl extends GenericDao<OperationTableType, Integer>
		implements IOperationTableTypeDao, ICommonConstants {

	final static Logger logger = Logger.getLogger(OperationTableTypeDaoImpl.class);

	public OperationTableTypeDaoImpl() {
		super(OperationTableType.class);
	}

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response saveOperationTableType(OperationTableTypeDto operationTableTypeDto) throws ApplicationException {
		try {
			OperationTableType operationTableType = new OperationTableType();
			operationTableType.setTableCode(operationTableTypeDto.getTableCode());
			operationTableType.setTableName(operationTableTypeDto.getTableName());
			operationTableType.setTableDesc(operationTableTypeDto.getTableDesc());
			operationTableType.setCreatedBy(operationTableTypeDto.getCreatedBy());
			operationTableType.setUpdatedBy(operationTableTypeDto.getUpdatedBy());
			operationTableType.setUnitId(operationTableTypeDto.getUnitId());
			operationTableType.setOrgId(operationTableTypeDto.getOrgId());
			operationTableType.setCreatedDate(
					GlobalCommonDateUtils.getDate(operationTableTypeDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			operationTableType.setUpdatedDate(
					GlobalCommonDateUtils.getDate(operationTableTypeDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			operationTableType.setStatus(operationTableTypeDto.getStatus());
			operationTableType=save(operationTableType);
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, operationTableType);
		} catch (HibernateException he) {
			logger.error("saveOperationTableType failed", he);
		} catch (Exception e) {
			logger.error("saveOperationTableType failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateOperationTableType(OperationTableTypeDto operationTableTypeDto) throws ApplicationException {
		try {
			OperationTableType operationTableType = findById(operationTableTypeDto.getOperationTableTypeId());
			operationTableType.setTableCode(operationTableTypeDto.getTableCode());
			operationTableType.setTableName(operationTableTypeDto.getTableName());
			operationTableType.setTableDesc(operationTableTypeDto.getTableDesc());
			operationTableType.setUpdatedBy(operationTableTypeDto.getUpdatedBy());
			operationTableType.setUpdatedDate(
					GlobalCommonDateUtils.getDate(operationTableTypeDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(operationTableType);
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (HibernateException he) {
			logger.error("updateOperationTableType failed", he);
		} catch (Exception e) {
			logger.error("updateOperationTableType failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateOperationTableTypeStatus(OperationTableTypeDto operationTableTypeDto)
			throws ApplicationException {
		try {
			OperationTableType operationTableType = findById(operationTableTypeDto.getOperationTableTypeId());
			operationTableType.setStatus(operationTableTypeDto.getStatus() == ACTIVE ? ACTIVE : INACTIVE);
			operationTableType.setUpdatedBy(operationTableTypeDto.getUpdatedBy());
			operationTableType.setUpdatedDate(
					GlobalCommonDateUtils.getDate(operationTableTypeDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(operationTableType);
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (HibernateException he) {
			logger.error("updateOperationTableTypeStatus failed", he);
		} catch (Exception e) {
			logger.error("updateOperationTableTypeStatus failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getOperationTableTypeList(OperationTableTypeDto operationTableTypeDto) throws ApplicationException {
		try {
			List<OperationTableTypeDto> operationTableTypeDtoList=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_OPERATION_TABLE_TYPE_LIST")
					.setParameter("orgId", operationTableTypeDto.getOrgId())
					.setParameter("unitId", operationTableTypeDto.getUnitId())
					.setFirstResult(operationTableTypeDto.getOffset() != null ? operationTableTypeDto.getOffset() : 0)
					.setMaxResults(operationTableTypeDto.getNoOfRecordsPerPage() != null ? operationTableTypeDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(OperationTableTypeDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, operationTableTypeDtoList, null);
		} catch (HibernateException he) {
			logger.error("getOperationTableTypeList failed", he);
		} catch (Exception e) {
			logger.error("getOperationTableTypeList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getOperationTableTypeListById(Integer operationTableTypeId) throws ApplicationException {
		try {
			List<OperationTableTypeDto> operationTableTypeDtoList=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_OPERATION_TABLE_TYPE_LIST_BY_ID")
					.setParameter("operationTableTypeId", operationTableTypeId)
					.setResultTransformer(Transformers.aliasToBean(OperationTableTypeDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, operationTableTypeDtoList, null);
		} catch (HibernateException he) {
			logger.error("getOperationTableTypeListById failed", he);
		} catch (Exception e) {
			logger.error("getOperationTableTypeListById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getOperationTableTypeListCount(OperationTableTypeDto operationTableTypeDto)
			throws ApplicationException {
		try {
			Session session=sessionFactory.getCurrentSession();
			Criteria c=session.createCriteria(OperationTableType.class); 
			c.add(Restrictions.eq("orgId", operationTableTypeDto.getOrgId()));
			c.add(Restrictions.eq("unitId", operationTableTypeDto.getUnitId()));
			c.setProjection(Projections.rowCount());  
			Long count = (Long)c.uniqueResult();
			return new Response(SUCCESS, null, null, null, count);
		} catch (HibernateException he) {
			logger.error("getOperationTableTypeListCount failed", he);
		} catch (Exception e) {
			logger.error("getOperationTableTypeListCount failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActiveOperationTableTypeList(OperationTableTypeDto operationTableTypeDto)
			throws ApplicationException {
		try {
			List<OperationTableTypeDto> operationTableTypeDtoList=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_OPERATION_TABLE_TYPE_LIST")
					.setParameter("orgId", operationTableTypeDto.getOrgId())
					.setParameter("unitId", operationTableTypeDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(OperationTableTypeDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, operationTableTypeDtoList, null);
		} catch (HibernateException he) {
			logger.error("getActiveOperationTableTypeList failed", he);
		} catch (Exception e) {
			logger.error("getActiveOperationTableTypeList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getOperationTableTypeListByName(OperationTableTypeDto operationTableTypeDto)
			throws ApplicationException {
		try {
			List<OperationTableTypeDto> operationTableTypeDtoList=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_OPERATION_TABLE_TYPE_LIST_BY_NAME")
					.setParameter("orgId", operationTableTypeDto.getOrgId())
					.setParameter("unitId", operationTableTypeDto.getUnitId())
					.setParameter("tableName", operationTableTypeDto.getTableName())
					.setResultTransformer(Transformers.aliasToBean(OperationTableTypeDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, operationTableTypeDtoList, null);
		} catch (HibernateException he) {
			logger.error("getOperationTableTypeListByName failed", he);
		} catch (Exception e) {
			logger.error("getOperationTableTypeListByName failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getOperationTableTypeListByNameNotById(OperationTableTypeDto operationTableTypeDto)
			throws ApplicationException {
		try {
			List<OperationTableTypeDto> operationTableTypeDtoList=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_OPERATION_TABLE_TYPE_LIST_BY_NAME_NOT_BY_ID")
					.setParameter("orgId", operationTableTypeDto.getOrgId())
					.setParameter("unitId", operationTableTypeDto.getUnitId())
					.setParameter("operationTableId", operationTableTypeDto.getOperationTableTypeId())
					.setParameter("tableName", operationTableTypeDto.getTableName())
					.setResultTransformer(Transformers.aliasToBean(OperationTableTypeDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, operationTableTypeDtoList, null);
		} catch (HibernateException he) {
			logger.error("getOperationTableTypeListByNameNotById failed", he);
		} catch (Exception e) {
			logger.error("getOperationTableTypeListByNameNotById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}
