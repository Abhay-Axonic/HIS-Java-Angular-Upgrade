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

import com.param.bloodBank.dto.BloodBagTypeMasterDto;
import com.param.bloodBank.model.BloodBagTypeMaster;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class BloodBagTypeMasterDaoImpl extends GenericDao<BloodBagTypeMaster, Integer> implements IBloodBagTypeMasterDao, ICommonConstants{

	final static Logger logger = Logger.getLogger(BloodBagTypeMasterDaoImpl.class);


	public BloodBagTypeMasterDaoImpl() {
		super(BloodBagTypeMaster.class);
	}
	
		@Autowired
		SessionFactory sessionFactory;
		
	@Override
	public Response saveBloodBagTypeMaster(BloodBagTypeMasterDto bloodBagTypeMasterDto)
			throws ApplicationException {
		try {
			BloodBagTypeMaster bloodBagTypeMaster=new BloodBagTypeMaster();
			bloodBagTypeMaster.setBloodBagTypeCode(bloodBagTypeMasterDto.getBloodBagTypeCode());
			bloodBagTypeMaster.setBloodBagTypeDesc(bloodBagTypeMasterDto.getBloodBagTypeDesc());
			bloodBagTypeMaster.setItemName(bloodBagTypeMasterDto.getItemName());
			bloodBagTypeMaster.setCapacityMax(bloodBagTypeMasterDto.getCapacityMax());
			bloodBagTypeMaster.setCapacityMin(bloodBagTypeMasterDto.getCapacityMin());
			bloodBagTypeMaster.setOrganizationId(bloodBagTypeMasterDto.getOrganizationId());
			bloodBagTypeMaster.setUnitId(bloodBagTypeMasterDto.getUnitId());
			bloodBagTypeMaster.setStatus(bloodBagTypeMasterDto.getStatus());
			bloodBagTypeMaster.setCreatedBy(bloodBagTypeMasterDto.getCreatedBy());
			bloodBagTypeMaster.setCreatedDate(GlobalCommonDateUtils.getDate(bloodBagTypeMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			bloodBagTypeMaster.setUpdatedBy(bloodBagTypeMasterDto.getUpdatedBy());
			bloodBagTypeMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(bloodBagTypeMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			
			save(bloodBagTypeMaster);
		
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
			
			} catch (HibernateException he) {
				logger.error("saveBloodBagTypeMaster failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("saveBloodBagTypeMaster failed", e);
				throw new ServiceException(e.getMessage());
			}
	}

	@Override
	public Response updateBloodBagTypeMaster(BloodBagTypeMasterDto bloodBagTypeMasterDto)
			throws ApplicationException {
		try {
			BloodBagTypeMaster bloodBagTypeMaster=findById(bloodBagTypeMasterDto.getBloodBagTypeId());
			
			bloodBagTypeMaster.setBloodBagTypeCode(bloodBagTypeMasterDto.getBloodBagTypeCode());
			bloodBagTypeMaster.setBloodBagTypeDesc(bloodBagTypeMasterDto.getBloodBagTypeDesc());
			bloodBagTypeMaster.setItemName(bloodBagTypeMasterDto.getItemName());
			bloodBagTypeMaster.setCapacityMax(bloodBagTypeMasterDto.getCapacityMax());
			bloodBagTypeMaster.setCapacityMin(bloodBagTypeMasterDto.getCapacityMin());
			bloodBagTypeMaster.setOrganizationId(bloodBagTypeMasterDto.getOrganizationId());
			bloodBagTypeMaster.setUnitId(bloodBagTypeMasterDto.getUnitId());
			bloodBagTypeMaster.setUpdatedBy(bloodBagTypeMasterDto.getUpdatedBy());
			bloodBagTypeMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(bloodBagTypeMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));


			update(bloodBagTypeMaster);
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
				
			} catch (HibernateException he) {
				logger.error("updateBloodBagTypeMaster failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("updateBloodBagTypeMaster failed", e);
				throw new ServiceException(e.getMessage());
			}
	}

	@Override
	public Response updateBloodBagTypeMasterStatus(BloodBagTypeMasterDto bloodBagTypeMasterDto)
			throws ApplicationException {
		try {
			BloodBagTypeMaster bloodBagTypeMaster=findById(bloodBagTypeMasterDto.getBloodBagTypeId());
			bloodBagTypeMaster.setStatus(bloodBagTypeMasterDto.getStatus());
			bloodBagTypeMaster.setUpdatedBy(bloodBagTypeMasterDto.getUpdatedBy());
			bloodBagTypeMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(bloodBagTypeMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));

			update(bloodBagTypeMaster);
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
				
			} catch (HibernateException he) {
				logger.error("updateBloodBagTypeMasterStatus failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("updateBloodBagTypeMasterStatus failed", e);
				throw new ServiceException(e.getMessage());
			}
	}
	@Override
	public Response getBloodBagTypeMasterList(BloodBagTypeMasterDto bloodBagTypeMasterDto)
			throws ApplicationException {
		try {
			List<BloodBagTypeMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BLOOD_BAG_TYPE_MASTER_LIST")
					.setParameter("organizationId", bloodBagTypeMasterDto.getOrganizationId())
					.setParameter("unitId", bloodBagTypeMasterDto.getUnitId())
					.setFirstResult(bloodBagTypeMasterDto.getOffset() != null ? bloodBagTypeMasterDto.getOffset() : 0)
					.setMaxResults(bloodBagTypeMasterDto.getNoOfRecordsPerPage() != null ? bloodBagTypeMasterDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(BloodBagTypeMasterDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
				
			} catch (HibernateException he) {
				logger.error("getBloodBagTypeMasterList failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getBloodBagTypeMasterList failed", e);
				throw new ServiceException(e.getMessage());
			}
		}
	@Override
	public Response getBloodBagTypeMasterById(Integer bloodBagTypeId, Integer organizationId, Integer unitId)
			throws ApplicationException {
		try {
			List<BloodBagTypeMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BLOOD_BAG_TYPE_MASTER_BY_ID")
					.setParameter("bloodBagTypeId", bloodBagTypeId)
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(BloodBagTypeMasterDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
				
			} catch (HibernateException he) {
				logger.error("getBloodBagTypeMasterById failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getBloodBagTypeMasterById failed", e);
				throw new ServiceException(e.getMessage());
			}
		}


	@Override
	public Response getActiveBloodBagTypeMaster(Integer organizationId, Integer unitId) throws ApplicationException {
		try {
			List<BloodBagTypeMasterDto> list=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_ACTIVE_BLOOD_BAG_TYPE_MASTER")
				.setParameter("organizationId", organizationId)
				.setParameter("unitId", unitId)
				.setResultTransformer(Transformers.aliasToBean(BloodBagTypeMasterDto.class)).list();

				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
				
			} catch (HibernateException he) {
				logger.error("getActiveBloodBagTypeMaster failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getActiveBloodBagTypeMaster failed", e);
				throw new ServiceException(e.getMessage());
			}
		}
	@Override
	public Response getBloodBagTypeMasterCount(BloodBagTypeMasterDto bloodBagTypeMasterDto)
			throws ApplicationException {
		try {
			Session session = sessionFactory.getCurrentSession();
			Criteria crit = session.createCriteria(BloodBagTypeMaster.class);
			crit.add(Restrictions.eq("organizationId", bloodBagTypeMasterDto.getOrganizationId()));
			crit.add(Restrictions.eq("unitId", bloodBagTypeMasterDto.getUnitId()));
			crit.setProjection(Projections.rowCount());
			Long count = (Long) crit.uniqueResult();
				
				return new Response(SUCCESS, SUCCESS_CODE, null, null, count);
				
			} catch (HibernateException he) {
				logger.error("getBloodBagTypeMasterCount failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getBloodBagTypeMasterCount failed", e);
				throw new ServiceException(e.getMessage());
			}
		}
	@Override
	public Response getBloodBagTypeMasterByName(BloodBagTypeMasterDto bloodBagTypeMasterDto)
			throws ApplicationException {
		try {
			List<BloodBagTypeMasterDto> list=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_BLOOD_BAG_TYPE_MASTER_BY_NAME")
				.setParameter("bloodBagTypeDesc", bloodBagTypeMasterDto.getBloodBagTypeDesc().toLowerCase())
				.setResultTransformer(Transformers.aliasToBean(BloodBagTypeMasterDto.class)).list();

				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
				
			} catch (HibernateException he) {
				logger.error("getBloodBagTypeMasterByName failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getBloodBagTypeMasterByName failed", e);
				throw new ServiceException(e.getMessage());
			}
		}

	@Override
	public Response getBloodBagTypeMasterByNameNotId(BloodBagTypeMasterDto bloodBagTypeMasterDto)
			throws ApplicationException {
		try {
			List<BloodBagTypeMasterDto> list=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_BLOOD_BAG_TYPE_MASTER_BY_NAME_NOT_ID")
				.setParameter("bloodBagTypeDesc", bloodBagTypeMasterDto.getBloodBagTypeDesc().toLowerCase())
				.setParameter("bloodBagTypeId", bloodBagTypeMasterDto.getBloodBagTypeId())
				.setResultTransformer(Transformers.aliasToBean(BloodBagTypeMasterDto.class)).list();

				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
				
			} catch (HibernateException he) {
				logger.error("getBloodBagTypeMasterByNameNotId failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getBloodBagTypeMasterByNameNotId failed", e);
				throw new ServiceException(e.getMessage());
			}
		}

}
