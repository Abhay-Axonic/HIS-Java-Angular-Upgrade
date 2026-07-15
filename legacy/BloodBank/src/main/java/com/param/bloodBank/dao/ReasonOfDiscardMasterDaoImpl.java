package com.param.bloodBank.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.bloodBank.dto.ReasonOfDiscardMasterDto;
import com.param.bloodBank.model.ReasonOfDiscardMaster;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class ReasonOfDiscardMasterDaoImpl extends GenericDao<ReasonOfDiscardMaster, Integer> implements IReasonOfDiscardMasterDao, ICommonConstants{

	final static Logger logger = Logger.getLogger(ReasonOfDiscardMasterDaoImpl.class);


	public ReasonOfDiscardMasterDaoImpl() {
		super(ReasonOfDiscardMaster.class);
	}
	
		@Autowired
		SessionFactory sessionFactory;
		
	@Override
	public Response saveReasonOfDiscardMaster(ReasonOfDiscardMasterDto reasonOfDiscardMasterDto)
			throws ApplicationException {
		try {
			ReasonOfDiscardMaster reasonOfDiscardMaster=new ReasonOfDiscardMaster();
			reasonOfDiscardMaster.setReasonOfDiscardCode(reasonOfDiscardMasterDto.getReasonOfDiscardCode());
			reasonOfDiscardMaster.setReasonOfDiscardDesc(reasonOfDiscardMasterDto.getReasonOfDiscardDesc());
			reasonOfDiscardMaster.setOrganizationId(reasonOfDiscardMasterDto.getOrganizationId());
			reasonOfDiscardMaster.setUnitId(reasonOfDiscardMasterDto.getUnitId());
			reasonOfDiscardMaster.setStatus(reasonOfDiscardMasterDto.getStatus());
			reasonOfDiscardMaster.setCreatedBy(reasonOfDiscardMasterDto.getCreatedBy());
			reasonOfDiscardMaster.setCreatedDate(GlobalCommonDateUtils.getDate(reasonOfDiscardMasterDto.getCreatedDate(), "dd-M-yyyy HH:ss:mm"));
			reasonOfDiscardMaster.setUpdatedBy(reasonOfDiscardMasterDto.getUpdatedBy());
			reasonOfDiscardMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(reasonOfDiscardMasterDto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));
			
			save(reasonOfDiscardMaster);
		
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
			
			} catch (HibernateException he) {
				logger.error("saveReasonOfDiscardMaster failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("saveReasonOfDiscardMaster failed", e);
				throw new ServiceException(e.getMessage());
			}
	}

	@Override
	public Response updateReasonOfDiscardMaster(ReasonOfDiscardMasterDto reasonOfDiscardMasterDto)
			throws ApplicationException {
		try {
			ReasonOfDiscardMaster reasonOfDiscardMaster=findById(reasonOfDiscardMasterDto.getReasonOfDiscardId());
			
			reasonOfDiscardMaster.setReasonOfDiscardCode(reasonOfDiscardMasterDto.getReasonOfDiscardCode());
			reasonOfDiscardMaster.setReasonOfDiscardDesc(reasonOfDiscardMasterDto.getReasonOfDiscardDesc());
			reasonOfDiscardMaster.setOrganizationId(reasonOfDiscardMasterDto.getOrganizationId());
			reasonOfDiscardMaster.setUnitId(reasonOfDiscardMasterDto.getUnitId());
			reasonOfDiscardMaster.setUpdatedBy(reasonOfDiscardMasterDto.getUpdatedBy());
			reasonOfDiscardMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(reasonOfDiscardMasterDto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));


			update(reasonOfDiscardMaster);
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
				
			} catch (HibernateException he) {
				logger.error("updateReasonOfDiscardMaster failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("updateReasonOfDiscardMaster failed", e);
				throw new ServiceException(e.getMessage());
			}
	}

	@Override
	public Response updateReasonOfDiscardMasterStatus(ReasonOfDiscardMasterDto reasonOfDiscardMasterDto)
			throws ApplicationException {
		try {
			ReasonOfDiscardMaster reasonOfDiscardMaster=findById(reasonOfDiscardMasterDto.getReasonOfDiscardId());
			reasonOfDiscardMaster.setStatus(reasonOfDiscardMasterDto.getStatus());
			reasonOfDiscardMaster.setUpdatedBy(reasonOfDiscardMasterDto.getUpdatedBy());
			reasonOfDiscardMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(reasonOfDiscardMasterDto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));

			update(reasonOfDiscardMaster);
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
				
			} catch (HibernateException he) {
				logger.error("updateReasonOfDiscardMasterStatus failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("updateReasonOfDiscardMasterStatus failed", e);
				throw new ServiceException(e.getMessage());
			}
	}
	@Override
	public Response getReasonOfDiscardMasterList(ReasonOfDiscardMasterDto reasonOfDiscardMasterDto)
			throws ApplicationException {
		try {
			List<ReasonOfDiscardMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_REASON_OF_DISCARD_MASTER_LIST")
					.setParameter("organizationId", reasonOfDiscardMasterDto.getOrganizationId())
					.setParameter("unitId", reasonOfDiscardMasterDto.getUnitId())
					.setFirstResult(reasonOfDiscardMasterDto.getOffset() != null ? reasonOfDiscardMasterDto.getOffset() : 0)
					.setMaxResults(reasonOfDiscardMasterDto.getNoOfRecordsPerPage() != null ? reasonOfDiscardMasterDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(ReasonOfDiscardMasterDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
				
			} catch (HibernateException he) {
				logger.error("getReasonOfDiscardMasterList failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getReasonOfDiscardMasterList failed", e);
				throw new ServiceException(e.getMessage());
			}
		}
	@Override
	public Response getReasonOfDiscardMasterById(Integer reasonOfDiscardId, Integer organizationId, Integer unitId)
			throws ApplicationException {
		try {
			List<ReasonOfDiscardMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_REASON_OF_DISCARD_MASTER_BY_ID")
					.setParameter("reasonOfDiscardId", reasonOfDiscardId)
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(ReasonOfDiscardMasterDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
				
			} catch (HibernateException he) {
				logger.error("getReasonOfDiscardMasterById failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getReasonOfDiscardMasterById failed", e);
				throw new ServiceException(e.getMessage());
			}
		}


	@Override
	public Response getActiveReasonOfDiscardMaster(Integer organizationId, Integer unitId) throws ApplicationException {
		try {
			List<ReasonOfDiscardMasterDto> list=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_ACTIVE_REASON_OF_DISCARD_MASTER")
				.setParameter("organizationId", organizationId)
				.setParameter("unitId", unitId)
				.setResultTransformer(Transformers.aliasToBean(ReasonOfDiscardMasterDto.class)).list();

				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
				
			} catch (HibernateException he) {
				logger.error("getActiveReasonOfDiscardMaster failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getActiveReasonOfDiscardMaster failed", e);
				throw new ServiceException(e.getMessage());
			}
		}
	@Override
	public Response getReasonOfDiscardMasterCount(ReasonOfDiscardMasterDto reasonOfDiscardMasterDto)
			throws ApplicationException {
		try {
			Session session = sessionFactory.getCurrentSession();
			Criteria crit = session.createCriteria(ReasonOfDiscardMaster.class);
			crit.add(Restrictions.eq("organizationId", reasonOfDiscardMasterDto.getOrganizationId()));
			crit.add(Restrictions.eq("unitId", reasonOfDiscardMasterDto.getUnitId()));
			crit.setProjection(Projections.rowCount());
			Long count = (Long) crit.uniqueResult();
				
				return new Response(SUCCESS, SUCCESS_CODE, null, null, count);
				
			} catch (HibernateException he) {
				logger.error("getReasonOfDiscardMasterCount failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getReasonOfDiscardMasterCount failed", e);
				throw new ServiceException(e.getMessage());
			}
		}
	@Override
	public Response getReasonOfDiscardMasterByName(ReasonOfDiscardMasterDto reasonOfDiscardMasterDto)
			throws ApplicationException {
		try {
			List<ReasonOfDiscardMasterDto> list=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_REASON_OF_DISCARD_MASTER_BY_NAME")
				.setParameter("reasonOfDiscardDesc", reasonOfDiscardMasterDto.getReasonOfDiscardDesc().toLowerCase())
				.setResultTransformer(Transformers.aliasToBean(ReasonOfDiscardMasterDto.class)).list();

				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
				
			} catch (HibernateException he) {
				logger.error("getReasonOfDiscardMasterByName failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getReasonOfDiscardMasterByName failed", e);
				throw new ServiceException(e.getMessage());
			}
		}

	@Override
	public Response getReasonOfDiscardMasterByNameNotId(ReasonOfDiscardMasterDto reasonOfDiscardMasterDto)
			throws ApplicationException {
		try {
			List<ReasonOfDiscardMasterDto> list=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_REASON_OF_DISCARD_MASTER_BY_NAME_NOT_ID")
				.setParameter("reasonOfDiscardDesc", reasonOfDiscardMasterDto.getReasonOfDiscardDesc().toLowerCase())
				.setParameter("reasonOfDiscardId", reasonOfDiscardMasterDto.getReasonOfDiscardId())
				.setResultTransformer(Transformers.aliasToBean(ReasonOfDiscardMasterDto.class)).list();

				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
				
			} catch (HibernateException he) {
				logger.error("getReasonOfDiscardMasterByNameNotId failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getReasonOfDiscardMasterByNameNotId failed", e);
				throw new ServiceException(e.getMessage());
			}
		}

	@Override
	public Response getAllReasonOfDiscardMasterList(ReasonOfDiscardMasterDto reasonOfDiscardMasterDto)
			throws ApplicationException {
		try {
			List<ReasonOfDiscardMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_REASON_OF_DISCARD_MASTER_LIST")
					.setParameter("organizationId", reasonOfDiscardMasterDto.getOrganizationId())
					.setParameter("unitId", reasonOfDiscardMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(ReasonOfDiscardMasterDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
				
			} catch (HibernateException he) {
				logger.error("getAllReasonOfDiscardMasterList failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getAllReasonOfDiscardMasterList failed", e);
				throw new ServiceException(e.getMessage());
			}
	}

}
