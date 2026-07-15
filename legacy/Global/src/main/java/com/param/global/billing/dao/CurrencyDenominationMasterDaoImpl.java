package com.param.global.billing.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.billing.global.model.CurrencyDenominationMaster;
import com.param.global.billing.dto.CurrencyDenominationMasterDto;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class CurrencyDenominationMasterDaoImpl extends GenericDao<CurrencyDenominationMaster, Integer>
		implements ICurrencyDenominationMasterDao, ICommonConstants {

	final static Logger logger = Logger.getLogger(CurrencyDenominationMasterDaoImpl.class);

	@Autowired
	SessionFactory sessionFactory;

	public CurrencyDenominationMasterDaoImpl() {
		super(CurrencyDenominationMaster.class);
	}

	@Override
	public Response saveCurrencyDenominationMaster(CurrencyDenominationMasterDto currencyDenominationMasterDto)
			throws ApplicationException {
		try {
			CurrencyDenominationMaster currencyDenominationMaster = new CurrencyDenominationMaster();
			currencyDenominationMaster.setCreatedBy(currencyDenominationMasterDto.getCreatedBy());
			currencyDenominationMaster.setCreatedDate(GlobalCommonDateUtils
					.getDate(currencyDenominationMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			currencyDenominationMaster.setCurrencyId(currencyDenominationMasterDto.getCurrencyId());
			currencyDenominationMaster.setDenominationCode(currencyDenominationMasterDto.getDenominationCode());
			currencyDenominationMaster.setDenominationDesc(currencyDenominationMasterDto.getDenominationDesc());
			currencyDenominationMaster.setDenominatonUnit(currencyDenominationMasterDto.getDenominatonUnit());
			currencyDenominationMaster.setLowestDenomination(currencyDenominationMasterDto.getLowestDenomination());
			currencyDenominationMaster.setOrgnisationId(currencyDenominationMasterDto.getOrgnisationId());
			currencyDenominationMaster.setUpdatedBy(currencyDenominationMasterDto.getUpdatedBy());
			currencyDenominationMaster.setUnitId(currencyDenominationMasterDto.getUnitId());
			currencyDenominationMaster.setUpdatedDate(GlobalCommonDateUtils
					.getDate(currencyDenominationMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			currencyDenominationMaster.setStatus(currencyDenominationMasterDto.getStatus());
			save(currencyDenominationMaster);
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);

		} catch (HibernateException he) {
			logger.error("saveCurrencyDenominationMaster failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("saveCurrencyDenominationMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getCurrencyDenominationMasterById(int currencyId, int orgId) throws ApplicationException {
		try {

			List<CurrencyDenominationMasterDto> currencyDenominationMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CURRENCY_DENOMINATIONMASTER_BY_ID")
					.setParameter("orgId", orgId)
					.setParameter("currencyId", currencyId)
					.setResultTransformer(Transformers.aliasToBean(CurrencyDenominationMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, currencyDenominationMasterDtoList, null);
		} catch (HibernateException he) {
			logger.error("getCurrencyDenominationMasterById failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getCurrencyDenominationMasterById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getCurrencyDenominationMasterList(CurrencyDenominationMasterDto currencyDenominationMasterDto)
			throws ApplicationException {
		try {
			List<CurrencyDenominationMasterDto> currencyDenominationMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CURRENCY_DENOMINATIONMASTER_LIST")
					.setParameter("orgId", currencyDenominationMasterDto.getOrgnisationId())
					.setFirstResult(currencyDenominationMasterDto.getOffset() != null ? currencyDenominationMasterDto.getOffset() : 0)
					.setMaxResults(currencyDenominationMasterDto.getNoOfRecordsPerPage() != null ? currencyDenominationMasterDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(CurrencyDenominationMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, currencyDenominationMasterDtoList, null);
		} catch (HibernateException he) {
			logger.error("getCurrencyDenominationMasterList failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getCurrencyDenominationMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateCurrencyDenominationMaster(CurrencyDenominationMasterDto currencyDenominationMasterDto)
			throws ApplicationException {
		try {

			CurrencyDenominationMaster currencyDenominationMaster = findById(
					currencyDenominationMasterDto.getCurrencyDenominationId());
			currencyDenominationMaster.setCurrencyId(currencyDenominationMasterDto.getCurrencyId());
			currencyDenominationMaster.setDenominationCode(currencyDenominationMasterDto.getDenominationCode());
			currencyDenominationMaster.setDenominationDesc(currencyDenominationMasterDto.getDenominationDesc());
			currencyDenominationMaster.setDenominatonUnit(currencyDenominationMasterDto.getDenominatonUnit());
			currencyDenominationMaster.setLowestDenomination(currencyDenominationMasterDto.getLowestDenomination());
			currencyDenominationMaster.setUpdatedBy(currencyDenominationMasterDto.getUpdatedBy());
			currencyDenominationMaster.setUpdatedDate(GlobalCommonDateUtils
					.getDate(currencyDenominationMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(currencyDenominationMaster);
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (HibernateException he) {
			logger.error("updateCurrencyDenominationMaster failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("updateCurrencyDenominationMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateCurrencyDenominationMasterStatus(CurrencyDenominationMasterDto currencyDenominationMasterDto)
			throws ApplicationException {
		try {
			CurrencyDenominationMaster currencyDenominationMaster = findById(
					currencyDenominationMasterDto.getCurrencyDenominationId());

			currencyDenominationMaster
					.setStatus(currencyDenominationMasterDto.getStatus() == ACTIVE ? ACTIVE : INACTIVE);
			currencyDenominationMaster.setUpdatedBy(currencyDenominationMasterDto.getUpdatedBy());
			currencyDenominationMaster.setUpdatedDate(GlobalCommonDateUtils
					.getDate(currencyDenominationMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));

			update(currencyDenominationMaster);
			return new Response<>(SUCCESS, COMMON_UPDATE, COMMON_UPDATE, null, null);
		} catch (HibernateException he) {
			logger.error("updateCurrencyDenominationMasterStatus failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("updateCurrencyDenominationMasterStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getCurrencyDenominationCount(CurrencyDenominationMasterDto currencyDenominationMasterDto)
			throws ApplicationException {
		try {
			Session session=sessionFactory.getCurrentSession();
			Criteria c=session.createCriteria(CurrencyDenominationMaster.class); 
			c.add(Restrictions.eq("orgnisationId", currencyDenominationMasterDto.getOrgnisationId()));
			c.setProjection(Projections.rowCount());  
			Long count = (Long)c.uniqueResult();
			return new Response(SUCCESS, null, null, null, count);

		} catch (HibernateException he) {
			logger.error("getCurrencyDenominationCount failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getCurrencyDenominationCount failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getCurrencyDenominationMasterByName(CurrencyDenominationMasterDto currencyDenominationMasterDto)
			throws ApplicationException {
		try {
			List<CurrencyDenominationMasterDto> currencyDenominationMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CURRENCY_DENOMINATIONMASTER_BY_NAME")
					.setParameter("orgId", currencyDenominationMasterDto.getOrgnisationId())
					.setParameter("denominationName", currencyDenominationMasterDto.getDenominationDesc().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(CurrencyDenominationMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, currencyDenominationMasterDtoList, null);
		} catch (HibernateException he) {
			logger.error("getCurrencyDenominationMasterByName failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getCurrencyDenominationMasterByName failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getCurrencyDenominationMasterByNameNotById(
			CurrencyDenominationMasterDto currencyDenominationMasterDto) throws ApplicationException {
		try {
			List<CurrencyDenominationMasterDto> currencyDenominationMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CURRENCY_DENOMINATIONMASTER_BY_NAME_NOT_BY_ID")
					.setParameter("currencyId", currencyDenominationMasterDto.getCurrencyDenominationId())
					.setParameter("orgId", currencyDenominationMasterDto.getOrgnisationId()) //dilip
					.setParameter("denominationName", currencyDenominationMasterDto.getDenominationDesc().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(CurrencyDenominationMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, currencyDenominationMasterDtoList, null);
		} catch (HibernateException he) {
			logger.error("getCurrencyDenominationMasterByNameNotById failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getCurrencyDenominationMasterByNameNotById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

}
