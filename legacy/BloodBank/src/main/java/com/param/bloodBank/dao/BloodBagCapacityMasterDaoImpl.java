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

import com.param.bloodBank.dto.BloodBagCapacityMasterDto;
import com.param.bloodBank.model.BloodBagCapacityMaster;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class BloodBagCapacityMasterDaoImpl extends GenericDao<BloodBagCapacityMaster, Integer> implements IBloodBagCapacityMasterDao, ICommonConstants{

	final static Logger logger = Logger.getLogger(BloodBagCapacityMasterDaoImpl.class);

	public BloodBagCapacityMasterDaoImpl() {
		super(BloodBagCapacityMaster.class);
	}
	
		@Autowired
		SessionFactory sessionFactory;
		
	@Override
	public Response saveBloodBagCapacityMaster(BloodBagCapacityMasterDto bloodBagCapacityMasterDto)
			throws ApplicationException {
		try {
			BloodBagCapacityMaster bloodBagCapacityMaster=new BloodBagCapacityMaster();
			bloodBagCapacityMaster.setBloodBagCapacityCode(bloodBagCapacityMasterDto.getBloodBagCapacityCode());
			bloodBagCapacityMaster.setBloodBagCapacityDesc(bloodBagCapacityMasterDto.getBloodBagCapacityDesc());
			bloodBagCapacityMaster.setOrganizationId(bloodBagCapacityMasterDto.getOrganizationId());
			bloodBagCapacityMaster.setUnitId(bloodBagCapacityMasterDto.getUnitId());
			bloodBagCapacityMaster.setStatus(bloodBagCapacityMasterDto.getStatus());
			bloodBagCapacityMaster.setCreatedBy(bloodBagCapacityMasterDto.getCreatedBy());
			bloodBagCapacityMaster.setCreatedDate(GlobalCommonDateUtils.getDate(bloodBagCapacityMasterDto.getCreatedDate(), "dd-M-yyyy HH:ss:mm"));
			bloodBagCapacityMaster.setUpdatedBy(bloodBagCapacityMasterDto.getUpdatedBy());
			bloodBagCapacityMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(bloodBagCapacityMasterDto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));
			
			save(bloodBagCapacityMaster);
		
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
			
			} catch (HibernateException he) {
				logger.error("saveBloodBagCapacityMaster failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("saveBloodBagCapacityMaster failed", e);
				throw new ServiceException(e.getMessage());
			}
	}

	@Override
	public Response updateBloodBagCapacityMaster(BloodBagCapacityMasterDto bloodBagCapacityMasterDto)
			throws ApplicationException {
		try {
			BloodBagCapacityMaster bloodBagCapacityMaster=findById(bloodBagCapacityMasterDto.getBloodBagCapacityId());
			
			bloodBagCapacityMaster.setBloodBagCapacityCode(bloodBagCapacityMasterDto.getBloodBagCapacityCode());
			bloodBagCapacityMaster.setBloodBagCapacityDesc(bloodBagCapacityMasterDto.getBloodBagCapacityDesc());
			bloodBagCapacityMaster.setOrganizationId(bloodBagCapacityMasterDto.getOrganizationId());
			bloodBagCapacityMaster.setUnitId(bloodBagCapacityMasterDto.getUnitId());
			bloodBagCapacityMaster.setUpdatedBy(bloodBagCapacityMasterDto.getUpdatedBy());
			bloodBagCapacityMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(bloodBagCapacityMasterDto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));


			update(bloodBagCapacityMaster);
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
				
			} catch (HibernateException he) {
				logger.error("updateBloodBagCapacityMaster failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("updateBloodBagCapacityMaster failed", e);
				throw new ServiceException(e.getMessage());
			}
	}

	@Override
	public Response updateBloodBagCapacityMasterStatus(BloodBagCapacityMasterDto bloodBagCapacityMasterDto)
			throws ApplicationException {
		try {
			BloodBagCapacityMaster bloodBagCapacityMaster=findById(bloodBagCapacityMasterDto.getBloodBagCapacityId());
			bloodBagCapacityMaster.setStatus(bloodBagCapacityMasterDto.getStatus());
			bloodBagCapacityMaster.setUpdatedBy(bloodBagCapacityMasterDto.getUpdatedBy());
			bloodBagCapacityMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(bloodBagCapacityMasterDto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));

			update(bloodBagCapacityMaster);
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
				
			} catch (HibernateException he) {
				logger.error("updateBloodBagCapacityMasterStatus failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("updateBloodBagCapacityMasterStatus failed", e);
				throw new ServiceException(e.getMessage());
			}
	}
	@Override
	public Response getBloodBagCapacityMasterList(BloodBagCapacityMasterDto bloodBagCapacityMasterDto)
			throws ApplicationException {
		try {
			List<BloodBagCapacityMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BLOOD_BAG_CAPACITY_MASTER_LIST")
					.setParameter("organizationId", bloodBagCapacityMasterDto.getOrganizationId())
					.setParameter("unitId", bloodBagCapacityMasterDto.getUnitId())
					.setFirstResult(bloodBagCapacityMasterDto.getOffset() != null ? bloodBagCapacityMasterDto.getOffset() : 0)
					.setMaxResults(bloodBagCapacityMasterDto.getNoOfRecordsPerPage() != null ? bloodBagCapacityMasterDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(BloodBagCapacityMasterDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
				
			} catch (HibernateException he) {
				logger.error("getBloodBagCapacityMasterList failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getBloodBagCapacityMasterList failed", e);
				throw new ServiceException(e.getMessage());
			}
		}
	@Override
	public Response getBloodBagCapacityMasterById(Integer bloodBagCapacityId, Integer organizationId, Integer unitId)
			throws ApplicationException {
		try {
			List<BloodBagCapacityMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BLOOD_BAG_CAPACITY_MASTER_BY_ID")
					.setParameter("bloodBagCapacityId", bloodBagCapacityId)
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(BloodBagCapacityMasterDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
				
			} catch (HibernateException he) {
				logger.error("getBloodBagCapacityMasterById failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getBloodBagCapacityMasterById failed", e);
				throw new ServiceException(e.getMessage());
			}
		}


	@Override
	public Response getActiveBloodBagCapacityMaster(Integer organizationId, Integer unitId) throws ApplicationException {
		try {
			List<BloodBagCapacityMasterDto> list=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_ACTIVE_BLOOD_BAG_CAPACITY_MASTER")
				.setParameter("organizationId", organizationId)
				.setParameter("unitId", unitId)
				.setResultTransformer(Transformers.aliasToBean(BloodBagCapacityMasterDto.class)).list();

				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
				
			} catch (HibernateException he) {
				logger.error("getActiveBloodBagCapacityMaster failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getActiveBloodBagCapacityMaster failed", e);
				throw new ServiceException(e.getMessage());
			}
		}
	@Override
	public Response getBloodBagCapacityMasterCount(BloodBagCapacityMasterDto bloodBagCapacityMasterDto)
			throws ApplicationException {
		try {
			Session session = sessionFactory.getCurrentSession();
			Criteria crit = session.createCriteria(BloodBagCapacityMaster.class);
			crit.add(Restrictions.eq("organizationId", bloodBagCapacityMasterDto.getOrganizationId()));
			crit.add(Restrictions.eq("unitId", bloodBagCapacityMasterDto.getUnitId()));
			crit.setProjection(Projections.rowCount());
			Long count = (Long) crit.uniqueResult();
				
				return new Response(SUCCESS, SUCCESS_CODE, null, null, count);
				
			} catch (HibernateException he) {
				logger.error("getBloodBagCapacityMasterCount failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getBloodBagCapacityMasterCount failed", e);
				throw new ServiceException(e.getMessage());
			}
		}
	@Override
	public Response getBloodBagCapacityMasterByName(BloodBagCapacityMasterDto bloodBagCapacityMasterDto)
			throws ApplicationException {
		try {
			List<BloodBagCapacityMasterDto> list=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_BLOOD_BAG_CAPACITY_MASTER_BY_NAME")
				.setParameter("bloodBagCapacityCode", bloodBagCapacityMasterDto.getBloodBagCapacityCode().toLowerCase())
				.setResultTransformer(Transformers.aliasToBean(BloodBagCapacityMasterDto.class)).list();

				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
				
			} catch (HibernateException he) {
				logger.error("getBloodBagCapacityMasterByName failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getBloodBagCapacityMasterByName failed", e);
				throw new ServiceException(e.getMessage());
			}
		}

	@Override
	public Response getBloodBagCapacityMasterByNameNotId(BloodBagCapacityMasterDto bloodBagCapacityMasterDto)
			throws ApplicationException {
		try {
			List<BloodBagCapacityMasterDto> list=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_BLOOD_BAG_CAPACITY_MASTER_BY_NAME_NOT_ID")
				.setParameter("bloodBagCapacityCode", bloodBagCapacityMasterDto.getBloodBagCapacityDesc().toLowerCase())
				.setParameter("bloodBagCapacityId", bloodBagCapacityMasterDto.getBloodBagCapacityId())
				.setResultTransformer(Transformers.aliasToBean(BloodBagCapacityMasterDto.class)).list();

				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
				
			} catch (HibernateException he) {
				logger.error("getBloodBagCapacityMasterByNameNotId failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getBloodBagCapacityMasterByNameNotId failed", e);
				throw new ServiceException(e.getMessage());
			}
		}

}

