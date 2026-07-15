package com.param.global.otims.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.otims.dto.OperationTableTypeDto;
import com.param.otims.model.OperationTable;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
@SuppressWarnings({ "unchecked", "rawtypes" })

@Repository
public class OperationTableDaoImpl extends GenericDao<OperationTable, Integer>
		implements IOperationTableDao, ICommonConstants {

	private static final Logger logger = Logger.getLogger(OperationTableDaoImpl.class);

	public OperationTableDaoImpl() {
		super(OperationTable.class);
	}

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response saveOperationTable(OperationTableTypeDto operationTableTypeDto) throws ApplicationException {
		try {
				OperationTable operationTable=new OperationTable();
				operationTable.setTableCode(operationTableTypeDto.getTableCode());
				operationTable.setTableName(operationTableTypeDto.getTableName());
				operationTable.setTableDesc(operationTableTypeDto.getTableDesc());
				operationTable.setTableTypeId(operationTableTypeDto.getTableTypeId());
				operationTable.setOperationTheatreId(operationTableTypeDto.getOperationTheatreId());
				operationTable.setCreatedBy(operationTableTypeDto.getCreatedBy());
				operationTable.setUpdatedBy(operationTableTypeDto.getUpdatedBy());
				operationTable.setUnitId(operationTableTypeDto.getUnitId());
				operationTable.setOrgId(operationTableTypeDto.getOrgId());
				operationTable.setCreatedDate(
						GlobalCommonDateUtils.getDate(operationTableTypeDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
				operationTable.setUpdatedDate(
						GlobalCommonDateUtils.getDate(operationTableTypeDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
				operationTable.setStatus(operationTableTypeDto.getStatus());
				operationTable =save(operationTable);
				return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, operationTable);
		} catch (HibernateException he) {
			logger.error("saveOperationTable failed", he);
		} catch (Exception e) {
			logger.error("saveOperationTable failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateOperationTable(OperationTableTypeDto operationTableTypeDto) throws ApplicationException {
		try {
			OperationTable operationTable=findById(operationTableTypeDto.getOperationTableId());
			operationTable.setTableCode(operationTableTypeDto.getTableCode());
			operationTable.setTableName(operationTableTypeDto.getTableName());
			operationTable.setTableDesc(operationTableTypeDto.getTableDesc());
			operationTable.setTableTypeId(operationTableTypeDto.getTableTypeId());
			operationTable.setOperationTheatreId(operationTableTypeDto.getOperationTheatreId());
			operationTable.setUpdatedBy(operationTableTypeDto.getUpdatedBy());
			operationTable.setUpdatedDate(
					GlobalCommonDateUtils.getDate(operationTableTypeDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(operationTable);
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (HibernateException he) {
			logger.error("updateOperationTable failed", he);
		} catch (Exception e) {
			logger.error("updateOperationTable failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateOperationTableStatus(OperationTableTypeDto operationTableTypeDto)
			throws ApplicationException {
		try {
			OperationTable operationTable=findById(operationTableTypeDto.getOperationTableId());
			operationTable.setStatus(operationTableTypeDto.getStatus()==ACTIVE?ACTIVE:INACTIVE);
			operationTable.setUpdatedBy(operationTableTypeDto.getUpdatedBy());
			operationTable.setUpdatedDate(
					GlobalCommonDateUtils.getDate(operationTableTypeDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(operationTable);
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (HibernateException he) {
			logger.error("updateOperationTableStatus failed", he);
		} catch (Exception e) {
			logger.error("updateOperationTableStatus failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getOperationTableList(OperationTableTypeDto operationTableTypeDto) throws ApplicationException {
		try {
			List<OperationTableTypeDto> operationTableTypeDtoList=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_OPERATION_TABLE_LIST")
					.setParameter("orgId", operationTableTypeDto.getOrgId())
					.setParameter("unitId", operationTableTypeDto.getUnitId())
					.setFirstResult(operationTableTypeDto.getOffset() != null ? operationTableTypeDto.getOffset() : 0)
					.setMaxResults(operationTableTypeDto.getNoOfRecordsPerPage() != null ? operationTableTypeDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(OperationTableTypeDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, operationTableTypeDtoList, null);
		} catch (HibernateException he) {
			logger.error("getOperationTableList failed", he);
		} catch (Exception e) {
			logger.error("getOperationTableList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getOperationTableListById(Integer operationTableId) throws ApplicationException {
		try {
			List<OperationTableTypeDto> operationTableTypeDtoList=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_OPERATION_TABLE_LIST_BY_ID")
					.setParameter("operationTableId",operationTableId)
					.setResultTransformer(Transformers.aliasToBean(OperationTableTypeDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, operationTableTypeDtoList, null);
		} catch (HibernateException he) {
			logger.error("getOperationTableListById failed", he);
		} catch (Exception e) {
			logger.error("getOperationTableListById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getOperationTableListCount(OperationTableTypeDto operationTableTypeDto)
			throws ApplicationException {
		try {
			Session session=sessionFactory.getCurrentSession();
			Criteria c=session.createCriteria(OperationTable.class); 
			c.add(Restrictions.eq("orgId", operationTableTypeDto.getOrgId()));
			c.add(Restrictions.eq("unitId", operationTableTypeDto.getUnitId()));
			c.setProjection(Projections.rowCount());  
			Long count = (Long)c.uniqueResult();
			return new Response(SUCCESS, null, null, null, count);
		} catch (HibernateException he) {
			logger.error("getOperationTableListCount failed", he);
		} catch (Exception e) {
			logger.error("getOperationTableListCount failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActiveOperationTableList(OperationTableTypeDto operationTableTypeDto)
			throws ApplicationException {
		try {
			List<OperationTableTypeDto> operationTableTypeDtoList=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_OPERATION_TABLE_LIST")
					.setParameter("orgId", operationTableTypeDto.getOrgId())
					.setParameter("unitId", operationTableTypeDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(OperationTableTypeDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, operationTableTypeDtoList, null);
		} catch (HibernateException he) {
			logger.error("getActiveOperationTableList failed", he);
		} catch (Exception e) {
			logger.error("getActiveOperationTableList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getOperationTableListByName(OperationTableTypeDto operationTableTypeDto)
			throws ApplicationException {
		try {
			List<OperationTableTypeDto> operationTableTypeDtoList=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_OPERATION_TABLE_LIST_BY_NAME")
					.setParameter("orgId", operationTableTypeDto.getOrgId())
					.setParameter("unitId", operationTableTypeDto.getUnitId())
					.setParameter("tableName", operationTableTypeDto.getTableName())
					.setResultTransformer(Transformers.aliasToBean(OperationTableTypeDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, operationTableTypeDtoList, null);
		} catch (HibernateException he) {
			logger.error("getOperationTableListByName failed", he);
		} catch (Exception e) {
			logger.error("getOperationTableListByName failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getOperationTableListByNameNotById(OperationTableTypeDto operationTableTypeDto)
			throws ApplicationException {
		try {
			List<OperationTableTypeDto> operationTableTypeDtoList=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_OPERATION_TABLE_LIST_BY_NAME_NOT_BY_ID")
					.setParameter("orgId", operationTableTypeDto.getOrgId())
					.setParameter("unitId", operationTableTypeDto.getUnitId())
					.setParameter("operationTableId", operationTableTypeDto.getOperationTableId())
					.setParameter("tableName", operationTableTypeDto.getTableName())
					.setResultTransformer(Transformers.aliasToBean(OperationTableTypeDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, operationTableTypeDtoList, null);
		} catch (HibernateException he) {
			logger.error("getOperationTableListByNameNotById failed", he);
		} catch (Exception e) {
			logger.error("getOperationTableListByNameNotById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActiveOperationTableListByTheatreId(OperationTableTypeDto operationTableTypeDto)
			throws ApplicationException {
		try {
			List<OperationTableTypeDto> operationTableTypeDtoList=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_OPERATION_TABLE_LIST_BY_THEATRE_ID")
					.setParameter("orgId", operationTableTypeDto.getOrgId())
					.setParameter("unitId", operationTableTypeDto.getUnitId())
					.setParameter("theatreId", operationTableTypeDto.getOperationTheatreId())
					.setResultTransformer(Transformers.aliasToBean(OperationTableTypeDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, operationTableTypeDtoList, null);
		} catch (HibernateException he) {
			logger.error("getActiveOperationTableListByTheatreId failed", he);
		} catch (Exception e) {
			logger.error("getActiveOperationTableListByTheatreId failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
}
