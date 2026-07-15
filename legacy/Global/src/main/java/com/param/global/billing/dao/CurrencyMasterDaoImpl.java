package com.param.global.billing.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.billing.global.model.CurrencyMaster;
import com.param.global.billing.dto.CurrencyMasterDto;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })

public class CurrencyMasterDaoImpl extends GenericDao<CurrencyMaster, Integer>
		implements ICurrencyMasterDao, ICommonConstants {

	final static Logger logger = Logger.getLogger(CurrencyMasterDaoImpl.class);

	public CurrencyMasterDaoImpl() {
		super(CurrencyMaster.class);
	}

	@Override
	public Response saveCurrencyMaster(CurrencyMasterDto currencyMasterDto) throws ApplicationException {
		try {

			CurrencyMaster currencyMaster = new CurrencyMaster();
			currencyMaster.setCurrencyName(currencyMasterDto.getCurrencyName());
			currencyMaster.setCurrencyCode(currencyMasterDto.getCurrencyCode());
			currencyMaster.setOrgnisationId(currencyMasterDto.getOrgnisationId());
			currencyMaster.setCountryId(currencyMasterDto.getCountryId());
			currencyMaster.setUnitId(currencyMasterDto.getUnitId());
			currencyMaster.setCreatedBy(currencyMasterDto.getCreatedBy());
			currencyMaster.setUpdatedBy(currencyMasterDto.getUpdatedBy());
			currencyMaster.setStatus(currencyMasterDto.getStatus());
			currencyMaster.setCreatedDate(
					GlobalCommonDateUtils.getDate(currencyMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			currencyMaster.setUpdatedDate(
					GlobalCommonDateUtils.getDate(currencyMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			save(currencyMaster);
			return new Response(SUCCESS, null, SUCCESS_MESSAGE, null, null);
		} catch (HibernateException he) {
			logger.error("saveCurrencyMaster failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("saveCurrencyMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getCurrencyMasterById(int currencyId, int orgId) throws ApplicationException {
		try {
			List<CurrencyMasterDto> currencyMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CURRENCY_MASTER_BY_ID").setParameter("currencyId", currencyId)
					.setParameter("orgId", orgId).setResultTransformer(Transformers.aliasToBean(CurrencyMasterDto.class))
					.list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, currencyMasterDtoList, null);

		} catch (HibernateException he) {
			logger.error("getCurrencyMasterById failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getCurrencyMasterById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getCurrencyMasterList(CurrencyMasterDto currencyMasterDto) throws ApplicationException {
		try {
			List<CurrencyMasterDto> currencyMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CURRENCY_MASTER_LIST")
					.setParameter("orgId", currencyMasterDto.getOrgnisationId())
					.setResultTransformer(Transformers.aliasToBean(CurrencyMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, currencyMasterDtoList, null);

		} catch (HibernateException he) {
			logger.error("getCurrencyMasterList failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getCurrencyMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override //Dilip
	public Response getAllCurrencyMasterList(CurrencyMasterDto currencyMasterDto) throws ApplicationException {
		try {
			List<CurrencyMasterDto> currencyMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ALL_CURRENCY_MASTER_LIST")
					.setParameter("orgId", currencyMasterDto.getOrgnisationId())
					.setFirstResult(currencyMasterDto.getOffset() != null ? currencyMasterDto.getOffset() : 0)
					.setMaxResults(currencyMasterDto.getNoOfRecordsPerPage() != null ? currencyMasterDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(CurrencyMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, currencyMasterDtoList, null);

		} catch (HibernateException he) {
			logger.error("getAllCurrencyMasterList failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getAllCurrencyMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateCurrencyMaster(CurrencyMasterDto currencyMasterDto) throws ApplicationException {
		try {

			CurrencyMaster currencyMaster = findById(currencyMasterDto.getCurrencyId());
			currencyMaster.setCurrencyName(currencyMasterDto.getCurrencyName());
			currencyMaster.setCurrencyCode(currencyMasterDto.getCurrencyCode());
			currencyMaster.setOrgnisationId(currencyMasterDto.getOrgnisationId());
			//currencyMaster.setUnitId(currencyMasterDto.getUnitId()); //dilip
			//currencyMaster.setCreatedBy(currencyMasterDto.getCreatedBy()); //dilip
			currencyMaster.setUpdatedBy(currencyMasterDto.getUpdatedBy());
			//currencyMaster.setCreatedDate(
				//	GlobalCommonDateUtils.getDate(currencyMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss")); //dilip
			currencyMaster.setUpdatedDate(
					GlobalCommonDateUtils.getDate(currencyMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(currencyMaster);
			//return new Response(SUCCESS, COMMON_UPDATE, SUCCESS_MESSAGE, null, null);
			return new Response<>(SUCCESS,SUCCESS_CODE , COMMON_UPDATE, null, null); //dilip
		} catch (HibernateException he) {
			logger.error("updateCurrencyMaster failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("updateCurrencyMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateCurrencyMasterStatus(CurrencyMasterDto currencyMasterDto) throws ApplicationException {
		try {

			CurrencyMaster currencyMaster = findById(currencyMasterDto.getCurrencyId());

			currencyMaster.setStatus(currencyMasterDto.getStatus() == ACTIVE ? ACTIVE : INACTIVE);
			currencyMaster.setUpdatedBy(currencyMasterDto.getUpdatedBy());
			currencyMaster.setUpdatedDate(
					GlobalCommonDateUtils.getDate(currencyMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));

			update(currencyMaster);
			return new Response<>(SUCCESS,SUCCESS_CODE , COMMON_UPDATE, null, null); //dilip
		} catch (HibernateException he) {
			logger.error("updateCurrencyMasterStatus failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("updateCurrencyMasterStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getCurrencyMasterByName(CurrencyMasterDto currencyMasterDto) throws ApplicationException {
		try {

			List<CurrencyMasterDto> currencyMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CURRENCY_MASTER_LIST_BY_NAME")
					.setParameter("orgId", currencyMasterDto.getOrgnisationId())
					.setParameter("currencyName", currencyMasterDto.getCurrencyName())
					.setFirstResult(currencyMasterDto.getOffset() != null ? currencyMasterDto.getOffset() : 0)
					.setMaxResults(currencyMasterDto.getNoOfRecordsPerPage() != null ? currencyMasterDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(CurrencyMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, currencyMasterDtoList, null);
		} catch (HibernateException he) {
			logger.error("getCurrencyMasterByName failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getCurrencyMasterByName failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getCurrencyMasterByNameNotId(CurrencyMasterDto currencyMasterDto) throws ApplicationException {
		try {
			List<CurrencyMasterDto> currencyMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CURRENCY_LIST_BY_NAME_NOT_ID")
					.setParameter("currencyId", currencyMasterDto.getCurrencyId())
					.setParameter("orgId", currencyMasterDto.getOrgnisationId())
					.setParameter("currencyName", currencyMasterDto.getCurrencyName().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(CurrencyMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, currencyMasterDtoList, null);

		} catch (HibernateException he) {
			logger.error("getCurrencyMasterByNameNotId failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getCurrencyMasterByNameNotId failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getCurrencyCount(CurrencyMasterDto currencyMasterDto) throws ApplicationException {
		try {
			Session session=sessionFactory.getCurrentSession();
			Criteria c=session.createCriteria(CurrencyMaster.class); 
			c.add(Restrictions.eq("orgnisationId", currencyMasterDto.getOrgnisationId()));
			c.setProjection(Projections.rowCount());  
			Long count = (Long)c.uniqueResult();
			return new Response(SUCCESS, null, null, null, count);
		} catch (HibernateException he) {
			logger.error("getCurrencyCount failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getCurrencyCount failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

}
