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

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.org.common.dto.ReasonTypeMasterDto;
import com.param.global.org.common.model.ReasonTypeMaster;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({ "unchecked", "rawtypes" })
public class ReasonTypeMasterDaoImpl extends GenericDao<ReasonTypeMaster, Integer>
		implements IReasonTypeMasterDao, ICommonConstants {
	final static Logger logger = Logger.getLogger(ReasonTypeMasterDaoImpl.class);


	@Autowired
	SessionFactory sessionFactory;

	public ReasonTypeMasterDaoImpl() {
		super(ReasonTypeMaster.class);
	}

	@Override
	public Response getReasonTypeMasterById(int reasonTypeId, int orgId) throws ApplicationException {
		try {
			List<ReasonTypeMasterDto> reasonTypeMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_REASON_TYPE_MASTER_BY_ID").setInteger("reasonTypeId", reasonTypeId)
					.setInteger("orgId", orgId)
					.setResultTransformer(Transformers.aliasToBean(ReasonTypeMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, reasonTypeMasterDtoList, null);
		} catch (HibernateException he) {
		logger.error("getReasonTypeMasterById failed", he);
		} catch (Exception e) {
		logger.error("getReasonTypeMasterById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getReasonTypeMasterList(ReasonTypeMasterDto reasonTypeMasterDto) throws ApplicationException {
		try {
			List<ReasonTypeMasterDto> reasonTypeMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_REASON_TYPE_MASTER_LIST")
					//.setInteger("orgId", reasonTypeMasterDto.getOrganizationId())
				//	.setFirstResult(reasonTypeMasterDto.getOffset() != null ? reasonTypeMasterDto.getOffset() : 0)
				//	.setMaxResults(reasonTypeMasterDto.getNoOfRecordsPerPage() != null ? reasonTypeMasterDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(ReasonTypeMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, reasonTypeMasterDtoList, null);
		} catch (HibernateException he) {
		logger.error("getReasonTypeMasterList failed", he);
		} catch (Exception e) {
		logger.error("getReasonTypeMasterList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateReasonTypeMaster(ReasonTypeMasterDto reasonTypeMasterDto) throws ApplicationException {
		try {
			ReasonTypeMaster reasonTypeMaster = findById(reasonTypeMasterDto.getReasonTypeId());
			reasonTypeMaster.setReasonTypeCode(reasonTypeMasterDto.getReasonTypeCode());
			reasonTypeMaster.setReasonTypeDesc(reasonTypeMasterDto.getReasonTypeDesc());
			reasonTypeMaster.setUpdatedBy(reasonTypeMasterDto.getUpdatedBy());
			reasonTypeMaster.setUpdatedDate(
					GlobalCommonDateUtils.getDate(reasonTypeMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(reasonTypeMaster);
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);

		} catch (HibernateException he) {
		logger.error("updateReasonTypeMaster failed", he);
		} catch (Exception e) {
		logger.error("updateReasonTypeMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateReasonTypeMasterStatus(ReasonTypeMasterDto reasonTypeMasterDto) throws ApplicationException {
		try {
			ReasonTypeMaster reasonTypeMaster = findById(reasonTypeMasterDto.getReasonTypeId());
			reasonTypeMaster.setStatus(reasonTypeMasterDto.getStatus() == ACTIVE ? ACTIVE : INACTIVE);
			reasonTypeMaster.setUpdatedBy(reasonTypeMasterDto.getUpdatedBy());
			reasonTypeMaster.setUpdatedDate(
					GlobalCommonDateUtils.getDate(reasonTypeMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(reasonTypeMaster);
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (HibernateException he) {
		logger.error("updateReasonTypeMasterStatus failed", he);
		} catch (Exception e) {
		logger.error("updateReasonTypeMasterStatus failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getReasonTypeMasterCount(ReasonTypeMasterDto reasonTypeMasterDto) throws ApplicationException {
		try {
			Session session = sessionFactory.getCurrentSession();
			Criteria c = session.createCriteria(ReasonTypeMaster.class);
			c.add(Restrictions.eq("organizationId", reasonTypeMasterDto.getOrganizationId()));
			c.setProjection(Projections.rowCount());
			Long count = (Long) c.uniqueResult();
			return new Response(SUCCESS, null, null, null, count);
		} catch (HibernateException he) {
		logger.error("getReasonTypeMasterCount failed", he);
		} catch (Exception e) {
		logger.error("getReasonTypeMasterCount failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response saveReasonTypeMaster(ReasonTypeMasterDto reasonTypeMasterDto) throws ApplicationException {
		try {
			ReasonTypeMaster reasonTypeMaster = new ReasonTypeMaster();
			reasonTypeMaster.setCreatedBy(reasonTypeMasterDto.getCreatedBy());
			reasonTypeMaster.setCreatedDate(
					GlobalCommonDateUtils.getDate(reasonTypeMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			reasonTypeMaster.setOrganizationId(reasonTypeMasterDto.getOrganizationId());
			reasonTypeMaster.setReasonTypeCode(reasonTypeMasterDto.getReasonTypeCode());
			reasonTypeMaster.setReasonTypeDesc(reasonTypeMasterDto.getReasonTypeDesc());
			reasonTypeMaster.setStatus(reasonTypeMasterDto.getStatus());
			reasonTypeMaster.setUpdatedBy(reasonTypeMasterDto.getUpdatedBy());
			reasonTypeMaster.setUpdatedDate(
					GlobalCommonDateUtils.getDate(reasonTypeMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			save(reasonTypeMaster);
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (HibernateException he) {
		logger.error("saveReasonTypeMaster failed", he);
		} catch (Exception e) {
		logger.error("saveReasonTypeMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getReasonTypeMasterByName(ReasonTypeMasterDto reasonTypeMasterDto) throws ApplicationException {
		try {
			List<ReasonTypeMasterDto> reasonTypeMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_REASON_TYPEMASTER_BY_NAME")
					.setString("reasonTypeDesc", reasonTypeMasterDto.getReasonTypeDesc().toLowerCase())
					.setString("reasonTypeCode", reasonTypeMasterDto.getReasonTypeCode().toLowerCase())
					.setInteger("orgId", reasonTypeMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(ReasonTypeMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, reasonTypeMasterDtoList, null);

		} catch (HibernateException he) {
		logger.error("getReasonTypeMasterByName failed", he);
		} catch (Exception e) {
		logger.error("getReasonTypeMasterByName failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getReasonTypeMasterByNameNotById(ReasonTypeMasterDto reasonTypeMasterDto)
			throws ApplicationException {
		try {
			List<ReasonTypeMasterDto> reasonTypeMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_REASONTYPEMASTER_BY_NAME_NOT_BY_ID")
					.setString("reasonTypeDesc", reasonTypeMasterDto.getReasonTypeDesc().toLowerCase())
					.setString("reasonTypeCode", reasonTypeMasterDto.getReasonTypeCode().toLowerCase())
					.setInteger("reasonTypeId", reasonTypeMasterDto.getReasonTypeId())
					.setInteger("orgId", reasonTypeMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(ReasonTypeMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, reasonTypeMasterDtoList, null);

		} catch (HibernateException he) {
		logger.error("getReasonTypeMasterByNameNotById failed", he);
		} catch (Exception e) {
		logger.error("getReasonTypeMasterByNameNotById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}
