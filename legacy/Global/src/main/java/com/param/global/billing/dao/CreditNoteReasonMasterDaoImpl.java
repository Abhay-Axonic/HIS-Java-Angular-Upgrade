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
import in.param.exception.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.param.billing.global.model.CreditNoteReasonMaster;
import com.param.global.billing.dto.CreditNoteReasonMasterDto;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;

import in.param.common.dao.GenericDao;
@SuppressWarnings({"rawtypes", "unchecked"})
@Repository
public class CreditNoteReasonMasterDaoImpl extends GenericDao<CreditNoteReasonMaster, Integer> implements ICreditNoteReasonMasterDao, ICommonConstants{

	final static Logger logger = Logger.getLogger(CreditNoteReasonMasterDaoImpl.class);

	@Autowired
	SessionFactory sessionfactory;

	public CreditNoteReasonMasterDaoImpl() {
		super(CreditNoteReasonMaster.class);
	}

	@Override
	public Response saveCreditNoteReasonMaster(CreditNoteReasonMasterDto creditNoteReasonMasterDto) throws ApplicationException {
		try {
			CreditNoteReasonMaster creditNoteReasonMaster=new CreditNoteReasonMaster();
			creditNoteReasonMaster.setReasonDesc(creditNoteReasonMasterDto.getReasonDesc());
			creditNoteReasonMaster.setCode(creditNoteReasonMasterDto.getCode());
			creditNoteReasonMaster.setOrganisationId(creditNoteReasonMasterDto.getOrganisationId());
			creditNoteReasonMaster.setUnitId(creditNoteReasonMasterDto.getUnitId());
			creditNoteReasonMaster.setCreatedBy(creditNoteReasonMasterDto.getCreatedBy());
			creditNoteReasonMaster.setCreatedDate(GlobalCommonDateUtils.getDate(creditNoteReasonMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			creditNoteReasonMaster.setUpdatedBy(creditNoteReasonMasterDto.getUpdatedBy());
			creditNoteReasonMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(creditNoteReasonMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			creditNoteReasonMaster.setStatus(creditNoteReasonMasterDto.getStatus());
			
			save(creditNoteReasonMaster);
			
		return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (HibernateException he) {
			logger.error("saveCreditNoteReasonMaster failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("saveCreditNoteReasonMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response updateCreditNoteReasonMaster(CreditNoteReasonMasterDto creditNoteReasonMasterDto) throws ApplicationException {
		try {
			CreditNoteReasonMaster creditNoteReasonMaster=findById(creditNoteReasonMasterDto.getCreditNoteReasonId());
			creditNoteReasonMaster.setReasonDesc(creditNoteReasonMasterDto.getReasonDesc());
			creditNoteReasonMaster.setCode(creditNoteReasonMasterDto.getCode());
			creditNoteReasonMaster.setOrganisationId(creditNoteReasonMasterDto.getOrganisationId());
			creditNoteReasonMaster.setUnitId(creditNoteReasonMasterDto.getUnitId());
			creditNoteReasonMaster.setUpdatedBy(creditNoteReasonMasterDto.getUpdatedBy());
			creditNoteReasonMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(creditNoteReasonMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));

			
			update(creditNoteReasonMaster);
			
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
			} catch (HibernateException he) {
				logger.error("updateCreditNoteReasonMaster failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("updateCreditNoteReasonMaster failed", e);
				throw new ServiceException(e.getMessage());
			}
		}

	@Override
	public Response updateCreditNoteReasonMasterStatus(CreditNoteReasonMasterDto creditNoteReasonMasterDto) throws ApplicationException {
		try {
			CreditNoteReasonMaster creditNoteReasonMaster=findById(creditNoteReasonMasterDto.getCreditNoteReasonId());
			
			creditNoteReasonMaster.setUpdatedBy(creditNoteReasonMasterDto.getUpdatedBy());
			creditNoteReasonMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(creditNoteReasonMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			creditNoteReasonMaster.setStatus(creditNoteReasonMasterDto.getStatus());
			
			update(creditNoteReasonMaster);
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
			} catch (HibernateException he) {
				logger.error("updateCreditNoteReasonMasterStatus failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("updateCreditNoteReasonMasterStatus failed", e);
				throw new ServiceException(e.getMessage());
			}
		}


	@Override
	public Response getCreditNoteReasonMasterList(CreditNoteReasonMasterDto creditNoteReasonMasterDto) throws ApplicationException {
		try {
			List<CreditNoteReasonMasterDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CREDIT_NOTE_REASON_MASTER_LIST")
					.setParameter("organisationId", creditNoteReasonMasterDto.getOrganisationId())
					.setParameter("unitId", creditNoteReasonMasterDto.getOrganisationId())
					.setFirstResult(creditNoteReasonMasterDto.getOffset() != null ? creditNoteReasonMasterDto.getOffset() : 0)
					.setMaxResults(creditNoteReasonMasterDto.getNoOfRecordsPerPage() != null ? creditNoteReasonMasterDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(CreditNoteReasonMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
			} catch (HibernateException he) {
				logger.error("getCreditNoteReasonMasterList failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getCreditNoteReasonMasterList failed", e);
				throw new ServiceException(e.getMessage());
			}
		}

	@Override
	public Response getCreditNoteReasonMasterById(Integer creditNoteReasonId, Integer organisationId, Integer unitId) throws ApplicationException {
		try {
			List<CreditNoteReasonMasterDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CREDIT_NOTE_REASON_MASTER_BY_ID")
					.setParameter("creditNoteReasonId", creditNoteReasonId )
					.setParameter("organisationId", organisationId )
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(CreditNoteReasonMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
			} catch (HibernateException he) {
				logger.error("getCreditNoteReasonMasterById failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getCreditNoteReasonMasterById failed", e);
				throw new ServiceException(e.getMessage());
			}
		}


	@Override
	public Response getCreditNoteReasonMasterCount(CreditNoteReasonMasterDto creditNoteReasonMasterDto) throws ApplicationException {
		try {
			Session session = sessionFactory.getCurrentSession();
			Criteria c = session.createCriteria(CreditNoteReasonMaster.class);
			c.add(Restrictions.eq("organisationId", creditNoteReasonMasterDto.getOrganisationId()));
			c.add(Restrictions.eq("unitId", creditNoteReasonMasterDto.getUnitId()));
			c.setProjection(Projections.rowCount());
			Long count = (Long) c.uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, count);
			} catch (HibernateException he) {
				logger.error("getCreditNoteReasonMasterCount failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getCreditNoteReasonMasterCount failed", e);
				throw new ServiceException(e.getMessage());
			}
		}


	@Override
	public Response getActiveCreditNoteReasonMasterList(CreditNoteReasonMasterDto creditNoteReasonMasterDto) throws ApplicationException {
		try {
			List<CreditNoteReasonMasterDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_CREDIT_NOTE_REASON_MASTER_LIST")
					.setParameter("organisationId", creditNoteReasonMasterDto.getOrganisationId())
					.setParameter("unitId", creditNoteReasonMasterDto.getOrganisationId())
					.setResultTransformer(Transformers.aliasToBean(CreditNoteReasonMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
			} catch (HibernateException he) {
				logger.error("getActiveCreditNoteReasonMasterList failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getActiveCreditNoteReasonMasterList failed", e);
				throw new ServiceException(e.getMessage());
			}
		}

	@Override
	public Response getCreditNoteReasonMasterByName(CreditNoteReasonMasterDto creditNoteReasonMasterDto) throws ApplicationException {
		try {
			List<CreditNoteReasonMasterDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CREDIT_NOTE_REASON_MASTER_BY_NAME")
					.setParameter("reasonDesc", creditNoteReasonMasterDto.getReasonDesc().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(CreditNoteReasonMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
			} catch (HibernateException he) {
				logger.error("getCreditNoteReasonMasterByName failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getCreditNoteReasonMasterByName failed", e);
				throw new ServiceException(e.getMessage());
			}
		}


	@Override
	public Response getCreditNoteReasonMasterByNameNotId(CreditNoteReasonMasterDto creditNoteReasonMasterDto) throws ApplicationException {
		try {
			List<CreditNoteReasonMasterDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CREDIT_NOTE_REASON_MASTER_BY_NAME_NOT_ID")
					.setParameter("creditNoteReasonId", creditNoteReasonMasterDto.getCreditNoteReasonId())
					.setParameter("reasonDesc", creditNoteReasonMasterDto.getReasonDesc().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(CreditNoteReasonMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
			} catch (HibernateException he) {
				logger.error("getCreditNoteReasonMasterByNameNotId failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getCreditNoteReasonMasterByNameNotId failed", e);
				throw new ServiceException(e.getMessage());
			}
		}

}
