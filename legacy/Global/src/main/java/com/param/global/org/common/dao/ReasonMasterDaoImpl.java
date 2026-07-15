package com.param.global.org.common.dao;

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

import com.param.adt.master.global.model.ReasonMaster;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.org.common.dto.ReasonMasterDto;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ReasonMasterDaoImpl extends GenericDao<ReasonMaster, Integer>
		implements IReasonMasterDao, ICommonConstants {
	final static Logger logger = Logger.getLogger(ReasonMasterDaoImpl.class);


	public ReasonMasterDaoImpl() {
		super(ReasonMaster.class);
	}

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response getReasonMasterById(int reasonId, int orgId) throws ApplicationException {
		try {
			List<ReasonMasterDto> reasonMasterDtoList=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_REASON_MASTER_BY_ID")
					.setParameter("orgId", orgId)
					.setParameter("reasonId", reasonId)
					.setResultTransformer(Transformers.aliasToBean(ReasonMasterDto.class)).list();		
					return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, reasonMasterDtoList, null);
		} catch (HibernateException he) {
		logger.error("getReasonMasterById failed", he);
		} catch (Exception e) {
		logger.error("getReasonMasterById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response saveReasonMaster(ReasonMasterDto reasonMasterDto) throws ApplicationException {
		try {
			ReasonMaster reasonMaster=new ReasonMaster();
			reasonMaster.setReasonDesc(reasonMasterDto.getReasonDesc());
			reasonMaster.setReasonTypeId(reasonMasterDto.getReasonTypeId());
			reasonMaster.setUpdatedBy(reasonMasterDto.getUpdatedBy());
			reasonMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(reasonMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			reasonMaster.setCreatedBy(reasonMasterDto.getCreatedBy());
			reasonMaster.setCreatedDate(GlobalCommonDateUtils.getDate(reasonMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			reasonMaster.setStatus(reasonMasterDto.getStatus());
			reasonMaster.setReasonCode(reasonMasterDto.getReasonCode());
			reasonMaster.setOrganizationId(reasonMasterDto.getOrganizationId());
			reasonMaster=save(reasonMaster);
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, reasonMaster);
		} catch (HibernateException he) {
		logger.error("saveReasonMaster failed", he);
		} catch (Exception e) {
		logger.error("saveReasonMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getReasonMasterList(ReasonMasterDto reasonMasterDto) throws ApplicationException {
		try {
			List<ReasonMasterDto> reasonMasterDtoList=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_REASONMASTER_LIST")
					.setParameter("orgId", reasonMasterDto.getOrganizationId())
					.setFirstResult(reasonMasterDto.getOffset() != null ? reasonMasterDto.getOffset() : 0)
					.setMaxResults(reasonMasterDto.getNoOfRecordsPerPage() != null ? reasonMasterDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(ReasonMasterDto.class)).list();		
					return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, reasonMasterDtoList, null);
		} catch (HibernateException he) {
		logger.error("getReasonMasterList failed", he);
		} catch (Exception e) {
		logger.error("getReasonMasterList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateReasonMaster(ReasonMasterDto reasonMasterDto) throws ApplicationException {
		try {
			ReasonMaster reasonMaster=findById(reasonMasterDto.getReasonMasterId());
			reasonMaster.setReasonDesc(reasonMasterDto.getReasonDesc());
			reasonMaster.setReasonCode(reasonMasterDto.getReasonCode());
			reasonMaster.setReasonTypeId(reasonMasterDto.getReasonTypeId());
			reasonMaster.setUpdatedBy(reasonMasterDto.getUpdatedBy());
			reasonMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(reasonMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(reasonMaster);
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, reasonMaster);
		} catch (HibernateException he) {
		logger.error("updateReasonMaster failed", he);
		} catch (Exception e) {
		logger.error("updateReasonMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateReasonMasterStatus(ReasonMasterDto reasonMasterDto) throws ApplicationException {
		try {
			ReasonMaster reasonMaster=findById(reasonMasterDto.getReasonMasterId());
			reasonMaster.setStatus(reasonMasterDto.getStatus() == ACTIVE ? ACTIVE : INACTIVE);
			reasonMaster.setUpdatedBy(reasonMasterDto.getUpdatedBy());
			reasonMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(reasonMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(reasonMaster);
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, reasonMaster.getOrganizationId());
			

		} catch (HibernateException he) {
		logger.error("updateReasonMasterStatus failed", he);
		} catch (Exception e) {
		logger.error("updateReasonMasterStatus failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getReasonMasterCount(ReasonMasterDto reasonMasterDto) throws ApplicationException {
		try {
			Session session = sessionFactory.getCurrentSession();
			Criteria c = session.createCriteria(ReasonMaster.class);
			c.add(Restrictions.eq("organizationId", reasonMasterDto.getOrganizationId()));
			c.setProjection(Projections.rowCount());
			Long count = (Long) c.uniqueResult();
			return new Response(SUCCESS, null, null, null, count);
		} catch (HibernateException he) {
		logger.error("getReasonMasterCount failed", he);
		} catch (Exception e) {
		logger.error("getReasonMasterCount failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getReasonMasterByName(ReasonMasterDto reasonMasterDto) throws ApplicationException {
		try {
			List<ReasonMasterDto> reasonMasterDtoList=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_REASON_MASTER_BY_NAME")
					.setParameter("reasonDesc", reasonMasterDto.getReasonDesc().toLowerCase())
					.setParameter("reasonCode", reasonMasterDto.getReasonCode().toLowerCase())
					.setParameter("reasonTypeId", reasonMasterDto.getReasonTypeId())
					.setParameter("orgId", reasonMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(ReasonMasterDto.class)).list();		
					return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, reasonMasterDtoList, null);

		} catch (HibernateException he) {
		logger.error("getReasonMasterByName failed", he);
		} catch (Exception e) {
		logger.error("getReasonMasterByName failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	
	@Override
	public Response getReasonMasterByNameNotById(ReasonMasterDto reasonMasterDto) throws ApplicationException {
		try {
			List<ReasonMasterDto> reasonMasterDtoList=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_REASON_MASTER_BY_NAME_NOT_BY_ID")
					.setParameter("reasonDesc", reasonMasterDto.getReasonDesc().toLowerCase())
					.setParameter("reasonCode", reasonMasterDto.getReasonCode().toLowerCase())
					.setParameter("reasonTypeId", reasonMasterDto.getReasonTypeId())
					.setParameter("reasonId", reasonMasterDto.getReasonMasterId())
					.setParameter("orgId", reasonMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(ReasonMasterDto.class)).list();		
					return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, reasonMasterDtoList, null);
		} catch (HibernateException he) {
		logger.error("getReasonMasterByNameNotById failed", he);
		} catch (Exception e) {
		logger.error("getReasonMasterByNameNotById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActiveReasonMasterList(ReasonMasterDto reasonMasterDto) throws ApplicationException {
		try {
			List<ReasonMasterDto> reasonMasterDtoList=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_REASON_MASTER_LIST")
					.setParameter("orgId", reasonMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(ReasonMasterDto.class)).list();		
					return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, reasonMasterDtoList, null);
		} catch (HibernateException he) {
		logger.error("getActiveReasonMasterList failed", he);
		} catch (Exception e) {
		logger.error("getActiveReasonMasterList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getReasonMasterListByReasonTypeId(ReasonMasterDto reasonMasterDto) throws ApplicationException {
		try {
			List<ReasonMasterDto> reasonMasterDtoList=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_REASON_MASTER_LIST_BY_REASON_TYPE_ID")
					.setParameter("organizationId", reasonMasterDto.getOrganizationId())
					.setParameter("reasonTypeId", reasonMasterDto.getReasonTypeId())
					.setResultTransformer(Transformers.aliasToBean(ReasonMasterDto.class)).list();		
					return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, reasonMasterDtoList, null);
		} catch (HibernateException he) {
		logger.error("getReasonMasterListByReasonTypeId failed", he);
		} catch (Exception e) {
		logger.error("getReasonMasterListByReasonTypeId failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}
