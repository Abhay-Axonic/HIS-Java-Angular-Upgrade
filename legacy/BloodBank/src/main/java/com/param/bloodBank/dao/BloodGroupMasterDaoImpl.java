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

import com.param.bloodBank.dto.BloodGroupMasterDto;
import com.param.bloodBank.model.BloodGroupMaster;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class BloodGroupMasterDaoImpl extends GenericDao<BloodGroupMaster, Integer> implements IBloodGroupMasterDao, ICommonConstants{

	final static Logger logger = Logger.getLogger(BloodGroupMasterDaoImpl.class);

	public BloodGroupMasterDaoImpl() {
		super(BloodGroupMaster.class);
	}

	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	public Response saveBloodGroupMaster(BloodGroupMasterDto bloodGroupMasterDto)
			throws ApplicationException {
		try {
			BloodGroupMaster bloodGroupMaster=new BloodGroupMaster();
			bloodGroupMaster.setBloodGroupCode(bloodGroupMasterDto.getBloodGroupCode());
			bloodGroupMaster.setBloodGroupDesc(bloodGroupMasterDto.getBloodGroupDesc());
			bloodGroupMaster.setOrganizationId(bloodGroupMasterDto.getOrganizationId());
			bloodGroupMaster.setUnitId(bloodGroupMasterDto.getUnitId());
			bloodGroupMaster.setStatus(bloodGroupMasterDto.getStatus());
			bloodGroupMaster.setCreatedBy(bloodGroupMasterDto.getCreatedBy());
			bloodGroupMaster.setCreatedDate(GlobalCommonDateUtils.getDate(bloodGroupMasterDto.getCreatedDate(), "dd-M-yyyy HH:ss:mm"));
			bloodGroupMaster.setUpdatedBy(bloodGroupMasterDto.getUpdatedBy());
			bloodGroupMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(bloodGroupMasterDto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));
			
			save(bloodGroupMaster);
		
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
			
			} catch (HibernateException he) {
				logger.error("saveBloodGroupMaster failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("saveBloodGroupMaster failed", e);
				throw new ServiceException(e.getMessage());
			}
	}

	@Override
	public Response updateBloodGroupMaster(BloodGroupMasterDto bloodGroupMasterDto)
			throws ApplicationException {
		try {
			BloodGroupMaster bloodGroupMaster=findById(bloodGroupMasterDto.getBloodGroupId());
			
			bloodGroupMaster.setBloodGroupCode(bloodGroupMasterDto.getBloodGroupCode());
			bloodGroupMaster.setBloodGroupDesc(bloodGroupMasterDto.getBloodGroupDesc());
			bloodGroupMaster.setOrganizationId(bloodGroupMasterDto.getOrganizationId());
			bloodGroupMaster.setUnitId(bloodGroupMasterDto.getUnitId());
			bloodGroupMaster.setUpdatedBy(bloodGroupMasterDto.getUpdatedBy());
			bloodGroupMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(bloodGroupMasterDto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));


			update(bloodGroupMaster);
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
				
			} catch (HibernateException he) {
				logger.error("updateBloodGroupMaster failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("updateBloodGroupMaster failed", e);
				throw new ServiceException(e.getMessage());
			}
	}

	@Override
	public Response updateBloodGroupMasterStatus(BloodGroupMasterDto bloodGroupMasterDto)
			throws ApplicationException {
		try {
			BloodGroupMaster bloodGroupMaster=findById(bloodGroupMasterDto.getBloodGroupId());
			bloodGroupMaster.setStatus(bloodGroupMasterDto.getStatus());
			bloodGroupMaster.setUpdatedBy(bloodGroupMasterDto.getUpdatedBy());
			bloodGroupMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(bloodGroupMasterDto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));

			update(bloodGroupMaster);
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
				
			} catch (HibernateException he) {
				logger.error("updateBloodGroupMasterStatus failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("updateBloodGroupMasterStatus failed", e);
				throw new ServiceException(e.getMessage());
			}
	}
	@Override
	public Response getBloodGroupMasterList(BloodGroupMasterDto bloodGroupMasterDto)
			throws ApplicationException {
		try {
			List<BloodGroupMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BLOOD_GROUP_MASTER_LIST")
					.setParameter("organizationId", bloodGroupMasterDto.getOrganizationId())
					.setParameter("unitId", bloodGroupMasterDto.getUnitId())
					.setFirstResult(bloodGroupMasterDto.getOffset() != null ? bloodGroupMasterDto.getOffset() : 0)
					.setMaxResults(bloodGroupMasterDto.getNoOfRecordsPerPage() != null ? bloodGroupMasterDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(BloodGroupMasterDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
				
			} catch (HibernateException he) {
				logger.error("getBloodGroupMasterList failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getBloodGroupMasterList failed", e);
				throw new ServiceException(e.getMessage());
			}
		}
	@Override
	public Response getBloodGroupMasterById(Integer bloodGroupId, Integer organizationId, Integer unitId)
			throws ApplicationException {
		try {
			List<BloodGroupMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BLOOD_GROUP_MASTER_BY_ID")
					.setParameter("bloodGroupId", bloodGroupId)
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(BloodGroupMasterDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
				
			} catch (HibernateException he) {
				logger.error("getBloodGroupMasterById failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getBloodGroupMasterById failed", e);
				throw new ServiceException(e.getMessage());
			}
		}


	@Override
	public Response getActiveBloodGroupMaster(Integer organizationId, Integer unitId) throws ApplicationException {
		try {
			List<BloodGroupMasterDto> list=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_ACTIVE_BLOOD_GROUP_MASTER")
				.setParameter("organizationId", organizationId)
				.setParameter("unitId", unitId)
				.setResultTransformer(Transformers.aliasToBean(BloodGroupMasterDto.class)).list();

				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
				
			} catch (HibernateException he) {
				logger.error("getActiveBloodGroupMaster failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getActiveBloodGroupMaster failed", e);
				throw new ServiceException(e.getMessage());
			}
		}
	@Override
	public Response getBloodGroupMasterCount(BloodGroupMasterDto bloodGroupMasterDto)
			throws ApplicationException {
		try {
			Session session = sessionFactory.getCurrentSession();
			Criteria crit = session.createCriteria(BloodGroupMaster.class);
			crit.add(Restrictions.eq("organizationId", bloodGroupMasterDto.getOrganizationId()));
			crit.add(Restrictions.eq("unitId", bloodGroupMasterDto.getUnitId()));
			crit.setProjection(Projections.rowCount());
			Long count = (Long) crit.uniqueResult();
				
				return new Response(SUCCESS, SUCCESS_CODE, null, null, count);
				
			} catch (HibernateException he) {
				logger.error("getBloodGroupMasterCount failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getBloodGroupMasterCount failed", e);
				throw new ServiceException(e.getMessage());
			}
		}
	@Override
	public Response getBloodGroupMasterByName(BloodGroupMasterDto bloodGroupMasterDto)
			throws ApplicationException {
		try {
			List<BloodGroupMasterDto> list=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_BLOOD_GROUP_MASTER_BY_NAME")
				.setParameter("bloodGroupDesc", bloodGroupMasterDto.getBloodGroupDesc().toLowerCase())
				.setResultTransformer(Transformers.aliasToBean(BloodGroupMasterDto.class)).list();

				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
				
			} catch (HibernateException he) {
				logger.error("getBloodGroupMasterByName failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getBloodGroupMasterByName failed", e);
				throw new ServiceException(e.getMessage());
			}
		}

	@Override
	public Response getBloodGroupMasterByNameNotId(BloodGroupMasterDto bloodGroupMasterDto)
			throws ApplicationException {
		try {
			List<BloodGroupMasterDto> list=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_BLOOD_GROUP_MASTER_BY_NAME_NOT_ID")
				.setParameter("bloodGroupDesc", bloodGroupMasterDto.getBloodGroupDesc().toLowerCase())
				.setParameter("bloodGroupId", bloodGroupMasterDto.getBloodGroupId())
				.setResultTransformer(Transformers.aliasToBean(BloodGroupMasterDto.class)).list();

				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
				
			} catch (HibernateException he) {
				logger.error("getBloodGroupMasterByNameNotId failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getBloodGroupMasterByNameNotId failed", e);
				throw new ServiceException(e.getMessage());
			}
		}

}





