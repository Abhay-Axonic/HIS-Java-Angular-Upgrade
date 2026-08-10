package com.param.global.otims.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.otims.dto.OperationTheatreDto;
import com.param.otims.model.OperationTheatre;

import in.param.common.dao.GenericDao;
@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class OperationTheatreDaoImpl extends GenericDao<OperationTheatre, Integer> implements IOperationTheatreDao,ICommonConstants {

	final static Logger logger = Logger.getLogger(OperationTheatreDaoImpl.class);

	public OperationTheatreDaoImpl() {
		super(OperationTheatre.class);
		
	}
	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response saveOperationTheatre(OperationTheatreDto operationTheatreDto)
			throws ApplicationException {
		try{
			OperationTheatre operationTheatre=new OperationTheatre();
			operationTheatre.setTheatreCode(operationTheatreDto.getTheatreCode());
			operationTheatre.setTheatreDesc(operationTheatreDto.getTheatreDesc());
			operationTheatre.setTheatreName(operationTheatreDto.getTheatreName());
			operationTheatre.setTheatreTypeId(operationTheatreDto.getTheatreTypeId());
			operationTheatre.setStoreId(operationTheatreDto.getStoreId());
			operationTheatre.setOrgId(operationTheatreDto.getOrgId());
			operationTheatre.setUnitId(operationTheatreDto.getUnitId());
			operationTheatre.setCreatedBy(operationTheatreDto.getCreatedBy());
			operationTheatre.setUpdatedBy(operationTheatreDto.getUpdatedBy());
			operationTheatre.setStatus(operationTheatreDto.getStatus());
			operationTheatre.setCreatedDate(GlobalCommonDateUtils.getDate(operationTheatreDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			operationTheatre.setUpdatedDate(GlobalCommonDateUtils.getDate(operationTheatreDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			
			operationTheatre = save(operationTheatre);
			
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, operationTheatre);

		} catch (HibernateException he) {
			logger.error("saveOperationTheatre failed", he);
			throw new ApplicationException(he.getMessage(), he);
		} catch (Exception e) {
			logger.error("saveOperationTheatre failed", e);
			throw new ApplicationException(e.getMessage(), e);
		}
	}

	@Override
	public Response updateOperationTheatre(OperationTheatreDto operationTheatreDto)
			throws ApplicationException {
		try{
			OperationTheatre operationTheatre=findById(operationTheatreDto.getOperationTheatreId());
			operationTheatre.setTheatreCode(operationTheatreDto.getTheatreCode());
			operationTheatre.setTheatreDesc(operationTheatreDto.getTheatreDesc());
			operationTheatre.setTheatreName(operationTheatreDto.getTheatreName());
			operationTheatre.setTheatreTypeId(operationTheatreDto.getTheatreTypeId());
			operationTheatre.setStoreId(operationTheatreDto.getStoreId());
			operationTheatre.setOrgId(operationTheatreDto.getOrgId());
			operationTheatre.setUnitId(operationTheatreDto.getUnitId());
			operationTheatre.setUpdatedBy(operationTheatreDto.getUpdatedBy());
			operationTheatre.setUpdatedDate(GlobalCommonDateUtils.getDate(operationTheatreDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
	
			update(operationTheatre);
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);

		} catch (HibernateException he) {
			logger.error("updateOperationTheatre failed", he);
		} catch (Exception e) {
			logger.error("updateOperationTheatre failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateOperationTheatreStatus(OperationTheatreDto operationTheatreDto)
			throws ApplicationException {
		try{
			OperationTheatre operationTheatre=findById(operationTheatreDto.getOperationTheatreId());
			operationTheatre.setUpdatedBy(operationTheatreDto.getUpdatedBy());
			operationTheatre.setUpdatedDate(GlobalCommonDateUtils.getDate(operationTheatreDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			operationTheatre.setStatus(operationTheatreDto.getStatus());
			
			update(operationTheatre);
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);

		} catch (HibernateException he) {
			logger.error("updateOperationTheatreStatus failed", he);
		} catch (Exception e) {
			logger.error("updateOperationTheatreStatus failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getOperationTheatreById(Integer operationTheatreId, Integer orgId, Integer unitId) throws ApplicationException {
		try{
			List<OperationTheatreDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_OPERATION_THEATRE_BY_ID")
					.setParameter("operationTheatreId", operationTheatreId)
					.setParameter("orgId", orgId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(OperationTheatreDto.class)).list();
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);

		} catch (HibernateException he) {
			logger.error("getOperationTheatreById failed", he);
		} catch (Exception e) {
			logger.error("getOperationTheatreById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getOperationTheatreList(OperationTheatreDto operationTheatreDto) throws ApplicationException {
		try{
			List<OperationTheatreDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_OPERATION_THEATRE_LIST")
					.setParameter("orgId", operationTheatreDto.getOrgId())
					.setParameter("unitId", operationTheatreDto.getUnitId())
					.setFirstResult(operationTheatreDto.getOffset() != null ? operationTheatreDto.getOffset() : 0)
					.setMaxResults(operationTheatreDto.getNoOfRecordsPerPage() != null ? operationTheatreDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(OperationTheatreDto.class)).list();
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);

		} catch (HibernateException he) {
			logger.error("getOperationTheatreList failed", he);
		} catch (Exception e) {
			logger.error("getOperationTheatreList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getActiveOperationTheatreList(OperationTheatreDto operationTheatreDto) throws ApplicationException {
		try{
			List<OperationTheatreDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_OPERATION_THEATRE_LIST")
					.setParameter("orgId", operationTheatreDto.getOrgId())
					.setParameter("unitId", operationTheatreDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(OperationTheatreDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);

		} catch (HibernateException he) {
			logger.error("getActiveOperationTheatreList failed", he);
		} catch (Exception e) {
			logger.error("getActiveOperationTheatreList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getOperationTheatreCount(OperationTheatreDto operationTheatreDto) throws ApplicationException {
		try{
			Session session = sessionFactory.getCurrentSession();
			Criteria c = session.createCriteria(OperationTheatre.class);
			c.add(Restrictions.eq("orgId", operationTheatreDto.getOrgId()));
			c.add(Restrictions.eq("unitId", operationTheatreDto.getUnitId()));
			c.setProjection(Projections.rowCount());
			Long count = (Long) c.uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, count);

		} catch (HibernateException he) {
			logger.error("getOperationTheatreCount failed", he);
		} catch (Exception e) {
			logger.error("getOperationTheatreCount failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getOperationTheatreByName(OperationTheatreDto operationTheatreDto) throws ApplicationException {
		try{
			List<OperationTheatreDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_OPERATION_THEATRE_BY_NAME")
					.setParameter("orgId", operationTheatreDto.getOrgId())
					.setParameter("unitId", operationTheatreDto.getUnitId())
					.setParameter("theatreName", operationTheatreDto.getTheatreName().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(OperationTheatreDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);

		} catch (HibernateException he) {
			logger.error("getOperationTheatreByName failed", he);
		} catch (Exception e) {
			logger.error("getOperationTheatreByName failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getOperationTheatreByNameNotId(OperationTheatreDto operationTheatreDto) throws ApplicationException {
		try{
			List<OperationTheatreDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_OPERATION_THEATRE_BY_NAME_NOT_ID")
					.setParameter("operationTheatreId", operationTheatreDto.getOperationTheatreId())
					.setParameter("orgId", operationTheatreDto.getOrgId())
					.setParameter("unitId", operationTheatreDto.getUnitId())
					.setParameter("theatreName", operationTheatreDto.getTheatreName().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(OperationTheatreDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);

		} catch (HibernateException he) {
			logger.error("getOperationTheatreByNameNotId failed", he);
		} catch (Exception e) {
			logger.error("getOperationTheatreByNameNotId failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getOperationTheatreByTypeId(Integer operationTheatreTypeId, Integer orgId, Integer unitId)
			throws ApplicationException {
		try{
			List<OperationTheatreDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_OPERATION_THEATRE_BY_OT_TYPE_ID_NEW")
					.setParameter("operationTheatreTypeId", operationTheatreTypeId)
					.setParameter("orgId", orgId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(OperationTheatreDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);

		} catch (HibernateException he) {
			logger.error("getOperationTheatreByTypeId failed", he);
		} catch (Exception e) {
			logger.error("getOperationTheatreByTypeId failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
}
