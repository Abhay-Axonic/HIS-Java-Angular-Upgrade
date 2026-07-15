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
import com.param.pharmacy.qms.dto.QMSCounterDto;
import com.param.pharmacy.qms.dto.QMSCounterMasterDto;
import com.param.qms.model.QMSCounter;
import com.param.qms.model.QMSCounterMaster;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "unchecked", "rawtypes" })
public class QMSCounterMasterDaoImpl extends GenericDao<QMSCounterMaster, Integer>
		implements IQMSCounterMasterDao, ICommonConstants {

	private static final Logger logger = Logger.getLogger(QMSCounterMasterDaoImpl.class);

	@Autowired
	SessionFactory sessionFactory;

	public QMSCounterMasterDaoImpl() {
		super(QMSCounterMaster.class);
	}

	@Override
	public Response saveQMSCounterMaster(QMSCounterMasterDto dto) throws ApplicationException {
		try {
			QMSCounterMaster master =new QMSCounterMaster();
			master.setQmsCounterCode(dto.getQmsCounterCode());
			master.setQmsCounterDesc(dto.getQmsCounterDesc());
			master.setQmsDeptMasterId(dto.getQmsDeptMasterId());
			master.setQmsKioskMasterId(dto.getQmsKioskMasterId());
			master.setCreatedBy(dto.getCreatedBy());
			master.setCreatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
			master.setUpdatedBy(dto.getUpdatedBy());
			master.setUpdatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
			master.setOrganisationId(dto.getOrganisationId());
			master.setUnitId(dto.getUnitId());
			master.setQmsCounterId(dto.getQmsCounterId());
			master.setStatus(ACTIVE);
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
	public Response updateQMSCounterMaster(QMSCounterMasterDto dto) throws ApplicationException {
		try {
			QMSCounterMaster master =findById(dto.getQmsCounterMasterId());
			master.setQmsCounterCode(dto.getQmsCounterCode());
			master.setQmsCounterDesc(dto.getQmsCounterDesc());
			master.setQmsDeptMasterId(dto.getQmsDeptMasterId());
			master.setQmsKioskMasterId(dto.getQmsKioskMasterId());
			master.setUpdatedBy(dto.getUpdatedBy());
			master.setUpdatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(master.getUnitId()), "dd-M-yyyy HH:mm:ss"));
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
	public Response updateQMSCounterMasterStatus(QMSCounterMasterDto dto) throws ApplicationException {
		try {
			
		
			Integer count=sessionFactory.getCurrentSession().createSQLQuery(" update pharmacy.m_qms_counter_master set status=:status  where qms_counter_id=:qmsCounterId ")
					.setParameter("status", dto.getStatus())
					.setParameter("qmsCounterId", dto.getQmsCounterId())
					.executeUpdate();
			
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, count);
		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response getQMSCounterMasterList(QMSCounterMasterDto dto) throws ApplicationException {
		try {
			List<QMSCounterDto> dtoList=sessionFactory.getCurrentSession().getNamedQuery("GET_QMS_COUNTER_MASTER_LIST")
					.setParameter("orgId", dto.getOrganisationId())
					.setParameter("unitId", dto.getUnitId())
					.setFirstResult(dto.getOffset()!=null ? dto.getOffset() : 0)
					.setMaxResults(dto.getNoOfRecordsPerPage()!=null ? dto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(QMSCounterDto.class)).list();
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
	public Response getQMSCounterMasterCount(QMSCounterMasterDto dto) throws ApplicationException {
		try {
			Criteria c = sessionFactory.getCurrentSession().createCriteria(QMSCounter.class);
			c.add(Restrictions.eq("organizationId", dto.getOrganisationId()));
			c.add(Restrictions.eq("unitId", dto.getUnitId()));
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
	public Response getQMSCounterMasterById(QMSCounterMasterDto dto) throws ApplicationException {
		try {
			
			
			QMSCounterMasterDto counterDto=(QMSCounterMasterDto) sessionFactory.getCurrentSession().getNamedQuery("GET_QMS_COUNTER_BY_ID")
					.setParameter("orgId", dto.getOrganisationId())
					.setParameter("unitId", dto.getUnitId())
					.setParameter("id", dto.getQmsCounterId())
					.setResultTransformer(Transformers.aliasToBean(QMSCounterMasterDto.class)).uniqueResult();
			
			if(counterDto != null) {
				
				List<QMSCounterMasterDto> dtoList = sessionFactory.getCurrentSession().getNamedQuery("GET_QMS_COUNTER_MASTER_BY_ID")
						.setParameter("orgId", dto.getOrganisationId())
						.setParameter("unitId", dto.getUnitId())
						.setParameter("id", dto.getQmsCounterId())
						.setResultTransformer(Transformers.aliasToBean(QMSCounterMasterDto.class)).list();
				
				if(dtoList != null && dtoList.size()>0) {
					
					counterDto.setQmsKioskMasterId(dtoList.get(0).getQmsKioskMasterId());
					counterDto.setQmsDesc(dtoList.get(0).getQmsDesc());
					counterDto.setDepartmentList(dtoList);
					
					return new Response<>(SUCCESS, SUCCESS_CODE, null, null, counterDto);
					
				}
			}


		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response getQMSCounterMasterListByName(QMSCounterMasterDto dto) throws ApplicationException {
		try {
			List<QMSCounterMasterDto> dtoList=sessionFactory.getCurrentSession().getNamedQuery("GET_QMS_COUNTER_MASTER_LIST_BY_NAME")
					.setParameter("orgId", dto.getOrganisationId())
					.setParameter("unitId", dto.getUnitId())
					.setParameter("code", dto.getQmsCounterCode().toLowerCase())
					.setParameter("desc", dto.getQmsCounterDesc().toLowerCase())
					//.setParameter("qmsDeptId", dto.getQmsDeptMasterId())
					.setParameter("kioskId", dto.getQmsKioskMasterId())
					.setResultTransformer(Transformers.aliasToBean(QMSCounterMasterDto.class)).list();
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
	public Response getQMSCounterMasterListByNameNotById(QMSCounterMasterDto dto) throws ApplicationException {
		try {
			List<QMSCounterMasterDto> dtoList=sessionFactory.getCurrentSession().getNamedQuery("GET_QMS_COUNTER_MASTER_LIST_BY_NAME_NOT_BY_ID")
					.setParameter("orgId", dto.getOrganisationId())
					.setParameter("unitId", dto.getUnitId())
					.setParameter("code", dto.getQmsCounterCode().toLowerCase())
					.setParameter("desc", dto.getQmsCounterDesc().toLowerCase())
					//.setParameter("qmsDeptId", dto.getQmsDeptMasterId())
					.setParameter("kioskId", dto.getQmsKioskMasterId())
					.setParameter("id", dto.getQmsCounterMasterId())
					.setResultTransformer(Transformers.aliasToBean(QMSCounterMasterDto.class)).list();
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
	public Response getQMSCounterListByDeptId(QMSCounterMasterDto dto) throws ApplicationException {
		try {
			List<QMSCounterMasterDto> dtoList=sessionFactory.getCurrentSession().getNamedQuery("GET_QMS_COUNTER_LIST_BY_DEPT_ID")
					.setParameter("orgId", dto.getOrganisationId())
					.setParameter("unitId", dto.getUnitId())
					.setParameter("qmsDeptId", dto.getQmsDeptMasterId())
					.setResultTransformer(Transformers.aliasToBean(QMSCounterMasterDto.class)).list();
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
