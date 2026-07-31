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

import com.param.bloodBank.dto.BloodComponentMasterDto;
import com.param.bloodBank.model.BloodComponentMaster;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class BloodComponentMasterDaoImpl extends GenericDao<BloodComponentMaster, Integer> implements IBloodComponentMasterDao, ICommonConstants{

	final static Logger logger = Logger.getLogger(BloodComponentMasterDaoImpl.class);


	public BloodComponentMasterDaoImpl() {
		super(BloodComponentMaster.class);
	}
	
		@Autowired
		SessionFactory sessionFactory;
		
	@Override
	public Response saveBloodComponentMaster(BloodComponentMasterDto bloodComponentMasterDto)
			throws ApplicationException {
		try {
			BloodComponentMaster bloodComponentMaster=new BloodComponentMaster();
			bloodComponentMaster.setBloodComponentCode(bloodComponentMasterDto.getBloodComponentCode());
			bloodComponentMaster.setBloodComponentDesc(bloodComponentMasterDto.getBloodComponentDesc());
			bloodComponentMaster.setBloodComponentShortDesc(bloodComponentMasterDto.getBloodComponentShortDesc());
			bloodComponentMaster.setExpiry(bloodComponentMasterDto.getExpiry());
			bloodComponentMaster.setQuantity(bloodComponentMasterDto.getQuantity());
			bloodComponentMaster.setByCrossChecking(bloodComponentMasterDto.getByCrossChecking());
			bloodComponentMaster.setOrganizationId(bloodComponentMasterDto.getOrganizationId());
			bloodComponentMaster.setUnitId(bloodComponentMasterDto.getUnitId());
			bloodComponentMaster.setStatus(bloodComponentMasterDto.getStatus());
			bloodComponentMaster.setCreatedBy(bloodComponentMasterDto.getCreatedBy());
			bloodComponentMaster.setCreatedDate(GlobalCommonDateUtils.getDate(bloodComponentMasterDto.getCreatedDate(), "dd-M-yyyy HH:ss:mm"));
			bloodComponentMaster.setUpdatedBy(bloodComponentMasterDto.getUpdatedBy());
			bloodComponentMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(bloodComponentMasterDto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));
			
			save(bloodComponentMaster);
		
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
			
			} catch (HibernateException he) {
				logger.error("saveBloodComponentMaster failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("saveBloodComponentMaster failed", e);
				throw new ServiceException(e.getMessage());
			}
	}

	@Override
	public Response updateBloodComponentMaster(BloodComponentMasterDto bloodComponentMasterDto)
			throws ApplicationException {
		try {
			BloodComponentMaster bloodComponentMaster=findById(bloodComponentMasterDto.getBloodComponentId());

			if (bloodComponentMaster == null || bloodComponentMaster.getOrganizationId() == null
					|| !bloodComponentMaster.getOrganizationId().equals(bloodComponentMasterDto.getOrganizationId())
					|| bloodComponentMaster.getUnitId() == null
					|| !bloodComponentMaster.getUnitId().equals(bloodComponentMasterDto.getUnitId())) {
				return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}

			bloodComponentMaster.setBloodComponentCode(bloodComponentMasterDto.getBloodComponentCode());
			bloodComponentMaster.setBloodComponentDesc(bloodComponentMasterDto.getBloodComponentDesc());
			bloodComponentMaster.setBloodComponentShortDesc(bloodComponentMasterDto.getBloodComponentShortDesc());
			bloodComponentMaster.setExpiry(bloodComponentMasterDto.getExpiry());
			bloodComponentMaster.setQuantity(bloodComponentMasterDto.getQuantity());
			bloodComponentMaster.setByCrossChecking(bloodComponentMasterDto.getByCrossChecking());
			bloodComponentMaster.setOrganizationId(bloodComponentMasterDto.getOrganizationId());
			bloodComponentMaster.setUnitId(bloodComponentMasterDto.getUnitId());
			bloodComponentMaster.setUpdatedBy(bloodComponentMasterDto.getUpdatedBy());
			bloodComponentMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(bloodComponentMasterDto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));


			update(bloodComponentMaster);
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
				
			} catch (HibernateException he) {
				logger.error("updateBloodComponentMaster failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("updateBloodComponentMaster failed", e);
				throw new ServiceException(e.getMessage());
			}
	}

	@Override
	public Response updateBloodComponentMasterStatus(BloodComponentMasterDto bloodComponentMasterDto)
			throws ApplicationException {
		try {
			BloodComponentMaster bloodComponentMaster=findById(bloodComponentMasterDto.getBloodComponentId());

			if (bloodComponentMaster == null || bloodComponentMaster.getOrganizationId() == null
					|| !bloodComponentMaster.getOrganizationId().equals(bloodComponentMasterDto.getOrganizationId())
					|| bloodComponentMaster.getUnitId() == null
					|| !bloodComponentMaster.getUnitId().equals(bloodComponentMasterDto.getUnitId())) {
				return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}

			bloodComponentMaster.setStatus(bloodComponentMasterDto.getStatus());
			bloodComponentMaster.setUpdatedBy(bloodComponentMasterDto.getUpdatedBy());
			bloodComponentMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(bloodComponentMasterDto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));

			update(bloodComponentMaster);
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
				
			} catch (HibernateException he) {
				logger.error("updateBloodComponentMasterStatus failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("updateBloodComponentMasterStatus failed", e);
				throw new ServiceException(e.getMessage());
			}
	}
	@Override
	public Response getBloodComponentMasterList(BloodComponentMasterDto bloodComponentMasterDto)
			throws ApplicationException {
		try {
			List<BloodComponentMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BLOOD_COMPONENT_MASTER_LIST")
					.setParameter("organizationId", bloodComponentMasterDto.getOrganizationId())
					.setParameter("unitId", bloodComponentMasterDto.getUnitId())
					.setFirstResult(bloodComponentMasterDto.getOffset() != null ? bloodComponentMasterDto.getOffset() : 0)
					.setMaxResults(bloodComponentMasterDto.getNoOfRecordsPerPage() != null ? bloodComponentMasterDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(BloodComponentMasterDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
				
			} catch (HibernateException he) {
				logger.error("getBloodComponentMasterList failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getBloodComponentMasterList failed", e);
				throw new ServiceException(e.getMessage());
			}
		}
	@Override
	public Response getBloodComponentMasterById(Integer bloodComponentId, Integer organizationId, Integer unitId)
			throws ApplicationException {
		try {
			List<BloodComponentMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BLOOD_COMPONENT_MASTER_BY_ID")
					.setParameter("bloodComponentId", bloodComponentId)
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(BloodComponentMasterDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
				
			} catch (HibernateException he) {
				logger.error("getBloodComponentMasterById failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getBloodComponentMasterById failed", e);
				throw new ServiceException(e.getMessage());
			}
		}


	@Override
	public Response getActiveBloodComponentMaster(Integer organizationId, Integer unitId) throws ApplicationException {
		try {
			List<BloodComponentMasterDto> list=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_ACTIVE_BLOOD_COMPONENT_MASTER")
				.setParameter("organizationId", organizationId)
				.setParameter("unitId", unitId)
				.setResultTransformer(Transformers.aliasToBean(BloodComponentMasterDto.class)).list();

				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
				
			} catch (HibernateException he) {
				logger.error("getActiveBloodComponentMaster failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getActiveBloodComponentMaster failed", e);
				throw new ServiceException(e.getMessage());
			}
		}
	@Override
	public Response getBloodComponentMasterCount(BloodComponentMasterDto bloodComponentMasterDto)
			throws ApplicationException {
		try {
			Session session = sessionFactory.getCurrentSession();
			Criteria crit = session.createCriteria(BloodComponentMaster.class);
			crit.add(Restrictions.eq("organizationId", bloodComponentMasterDto.getOrganizationId()));
			crit.add(Restrictions.eq("unitId", bloodComponentMasterDto.getUnitId()));
			crit.setProjection(Projections.rowCount());
			Long count = (Long) crit.uniqueResult();
				
				return new Response(SUCCESS, SUCCESS_CODE, null, null, count);
				
			} catch (HibernateException he) {
				logger.error("getBloodComponentMasterCount failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getBloodComponentMasterCount failed", e);
				throw new ServiceException(e.getMessage());
			}
		}
	@Override
	public Response getBloodComponentMasterByName(BloodComponentMasterDto bloodComponentMasterDto)
			throws ApplicationException {
		try {
			List<BloodComponentMasterDto> list=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_BLOOD_COMPONENT_MASTER_BY_NAME")
				.setParameter("bloodComponentDesc", bloodComponentMasterDto.getBloodComponentDesc().toLowerCase())
				.setResultTransformer(Transformers.aliasToBean(BloodComponentMasterDto.class)).list();

				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
				
			} catch (HibernateException he) {
				logger.error("getBloodComponentMasterByName failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getBloodComponentMasterByName failed", e);
				throw new ServiceException(e.getMessage());
			}
		}

	@Override
	public Response getBloodComponentMasterByNameNotId(BloodComponentMasterDto bloodComponentMasterDto)
			throws ApplicationException {
		try {
			List<BloodComponentMasterDto> list=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_BLOOD_COMPONENT_MASTER_BY_NAME_NOT_ID")
				.setParameter("bloodComponentDesc", bloodComponentMasterDto.getBloodComponentDesc().toLowerCase())
				.setParameter("bloodComponentId", bloodComponentMasterDto.getBloodComponentId())
				.setResultTransformer(Transformers.aliasToBean(BloodComponentMasterDto.class)).list();

				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
				
			} catch (HibernateException he) {
				logger.error("getBloodComponentMasterByNameNotId failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getBloodComponentMasterByNameNotId failed", e);
				throw new ServiceException(e.getMessage());
			}
		}

}
