package com.param.pharmacy.qms.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.pharmacy.qms.dto.QMSKioskMasterDto;
import com.param.qms.model.QMSKioskMaster;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class QMSKioskMasterDaoImpl extends GenericDao<QMSKioskMaster, Integer> implements IQMSKioskMasterDao,ICommonConstants {

	@Autowired
	SessionFactory sessionFactory;

	private static final Logger logger = Logger.getLogger(QMSKioskMasterDaoImpl.class);

	public QMSKioskMasterDaoImpl() {
		super(QMSKioskMaster.class);
	}

	@Override
	public Response saveQMSMaster(QMSKioskMasterDto dto) throws ApplicationException {
		try {
			QMSKioskMaster master= new QMSKioskMaster();
			master.setQmsCode(dto.getQmsCode());
			master.setQmsDesc(dto.getQmsDesc());
			master.setStatus('A');
			master.setCreatedBy(dto.getCreatedBy());
			master.setUpdatedBy(dto.getUpdatedBy());
			master.setUnitId(dto.getUnitId());
			master.setOrganisationId(dto.getOrganisationId());
			master.setCreatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
			master.setUpdatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
			sessionFactory.getCurrentSession().save(master);
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateQMSMaster(QMSKioskMasterDto dto) throws ApplicationException {
		try {
			QMSKioskMaster master= findById(dto.getQmsKioskMasterId());
			master.setQmsCode(dto.getQmsCode());
			master.setQmsDesc(dto.getQmsDesc());
			master.setUpdatedBy(dto.getUpdatedBy());
			master.setUpdatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
			sessionFactory.getCurrentSession().update(master);
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateQMSMasterStatus(QMSKioskMasterDto dto) throws ApplicationException {
		try {
			QMSKioskMaster master= findById(dto.getQmsKioskMasterId());
			master.setStatus(dto.getStatus()==ACTIVE ? ACTIVE : INACTIVE);
			master.setUpdatedBy(dto.getUpdatedBy());
			master.setUpdatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
			sessionFactory.getCurrentSession().update(master);
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response getQMSMasterList(QMSKioskMasterDto dto) throws ApplicationException {
		try {
			List<QMSKioskMasterDto> dtoList=sessionFactory.getCurrentSession().getNamedQuery("GET_QMS_MASTER_LIST")
					.setParameter("orgId", dto.getOrganisationId())
					.setParameter("unitId", dto.getUnitId())
					.setFirstResult(dto.getOffset() != null ? dto.getOffset() : 0)
					.setMaxResults(dto.getNoOfRecordsPerPage() != null ? dto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(QMSKioskMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, dtoList, null);
		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getQMSMasterCount(QMSKioskMasterDto dto) throws ApplicationException {
		try {
			Criteria  c=sessionFactory.getCurrentSession().createCriteria(QMSKioskMaster.class);
			c.add(Restrictions.eq("organisationId", dto.getOrganisationId()));
			c.add(Restrictions.eq("unitId",dto.getUnitId()));
			c.setProjection(Projections.rowCount());  
			Long count = (Long)c.uniqueResult();
			return new Response<>(SUCCESS, null, null, null, count);
		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getQMSMasterById(QMSKioskMasterDto dto) throws ApplicationException {
		try {
			List<QMSKioskMasterDto> dtoList=sessionFactory.getCurrentSession().getNamedQuery("GET_QMS_MASTER_BY_ID")
					.setParameter("orgId", dto.getOrganisationId())
					.setParameter("unitId", dto.getUnitId())
					.setParameter("id", dto.getQmsKioskMasterId())
					.setResultTransformer(Transformers.aliasToBean(QMSKioskMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, dtoList, null);
		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getQMSMasterListByName(QMSKioskMasterDto dto) throws ApplicationException {
		try {
			List<QMSKioskMasterDto> dtoList=sessionFactory.getCurrentSession().getNamedQuery("GET_QMS_MASTER_LIST_BY_NAME")
					.setParameter("orgId", dto.getOrganisationId())
					.setParameter("unitId", dto.getUnitId())
					.setParameter("desc", dto.getQmsDesc().toLowerCase())
					.setParameter("code", dto.getQmsCode().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(QMSKioskMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, dtoList, null);
		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getQMSMasterListByNameNotById(QMSKioskMasterDto dto) throws ApplicationException {
		try {
			List<QMSKioskMasterDto> dtoList=sessionFactory.getCurrentSession().getNamedQuery("GET_QMS_MASTER_LIST_BY_NAME_NOT_BY_ID")
					.setParameter("orgId", dto.getOrganisationId())
					.setParameter("unitId", dto.getUnitId())
					.setParameter("desc", dto.getQmsDesc().toLowerCase())
					.setParameter("code", dto.getQmsCode().toLowerCase())
					.setParameter("id", dto.getQmsKioskMasterId())
					.setResultTransformer(Transformers.aliasToBean(QMSKioskMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, dtoList, null);
		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getActiveQMSMasterList(QMSKioskMasterDto dto) throws ApplicationException {
		try {
			List<QMSKioskMasterDto> dtoList=sessionFactory.getCurrentSession().getNamedQuery("GET_ACTIVE_QMS_MASTER_LIST")
					.setParameter("orgId", dto.getOrganisationId())
					.setParameter("unitId", dto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(QMSKioskMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, dtoList, null);
		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

}
