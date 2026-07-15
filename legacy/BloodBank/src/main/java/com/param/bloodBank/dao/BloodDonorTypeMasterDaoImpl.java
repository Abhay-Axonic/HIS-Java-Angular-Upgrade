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

import com.param.bloodBank.dto.BloodDonorTypeMasterDto;
import com.param.bloodBank.model.BloodDonorTypeMaster;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class BloodDonorTypeMasterDaoImpl extends GenericDao<BloodDonorTypeMaster, Integer> implements IBloodDonorTypeMasterDao, ICommonConstants{

	final static Logger logger = Logger.getLogger(BloodDonorTypeMasterDaoImpl.class);


	public BloodDonorTypeMasterDaoImpl() {
		super(BloodDonorTypeMaster.class);
	}
	
		@Autowired
		SessionFactory sessionFactory;
		
	@Override
	public Response saveBloodDonorTypeMaster(BloodDonorTypeMasterDto bloodDonorTypeMasterDto)
			throws ApplicationException {
		try {
			BloodDonorTypeMaster bloodDonorTypeMaster=new BloodDonorTypeMaster();
			bloodDonorTypeMaster.setBloodDonorTypeCode(bloodDonorTypeMasterDto.getBloodDonorTypeCode());
			bloodDonorTypeMaster.setBloodDonorTypeDesc(bloodDonorTypeMasterDto.getBloodDonorTypeDesc());
			bloodDonorTypeMaster.setOrganizationId(bloodDonorTypeMasterDto.getOrganizationId());
			bloodDonorTypeMaster.setUnitId(bloodDonorTypeMasterDto.getUnitId());
			bloodDonorTypeMaster.setStatus(bloodDonorTypeMasterDto.getStatus());
			bloodDonorTypeMaster.setCreatedBy(bloodDonorTypeMasterDto.getCreatedBy());
			bloodDonorTypeMaster.setCreatedDate(GlobalCommonDateUtils.getDate(bloodDonorTypeMasterDto.getCreatedDate(), "dd-M-yyyy HH:ss:mm"));
			bloodDonorTypeMaster.setUpdatedBy(bloodDonorTypeMasterDto.getUpdatedBy());
			bloodDonorTypeMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(bloodDonorTypeMasterDto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));
			
			save(bloodDonorTypeMaster);
		
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
			
			} catch (HibernateException he) {
				logger.error("saveBloodDonorTypeMaster failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("saveBloodDonorTypeMaster failed", e);
				throw new ServiceException(e.getMessage());
			}
	}

	@Override
	public Response updateBloodDonorTypeMaster(BloodDonorTypeMasterDto bloodDonorTypeMasterDto)
			throws ApplicationException {
		try {
			BloodDonorTypeMaster bloodDonorTypeMaster=findById(bloodDonorTypeMasterDto.getBloodDonorTypeId());
			
			bloodDonorTypeMaster.setBloodDonorTypeCode(bloodDonorTypeMasterDto.getBloodDonorTypeCode());
			bloodDonorTypeMaster.setBloodDonorTypeDesc(bloodDonorTypeMasterDto.getBloodDonorTypeDesc());
			bloodDonorTypeMaster.setOrganizationId(bloodDonorTypeMasterDto.getOrganizationId());
			bloodDonorTypeMaster.setUnitId(bloodDonorTypeMasterDto.getUnitId());
			bloodDonorTypeMaster.setUpdatedBy(bloodDonorTypeMasterDto.getUpdatedBy());
			bloodDonorTypeMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(bloodDonorTypeMasterDto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));


			update(bloodDonorTypeMaster);
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
				
			} catch (HibernateException he) {
				logger.error("updateBloodDonorTypeMaster failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("updateBloodDonorTypeMaster failed", e);
				throw new ServiceException(e.getMessage());
			}
	}

	@Override
	public Response updateBloodDonorTypeMasterStatus(BloodDonorTypeMasterDto bloodDonorTypeMasterDto)
			throws ApplicationException {
		try {
			BloodDonorTypeMaster bloodDonorTypeMaster=findById(bloodDonorTypeMasterDto.getBloodDonorTypeId());
			bloodDonorTypeMaster.setStatus(bloodDonorTypeMasterDto.getStatus());
			bloodDonorTypeMaster.setUpdatedBy(bloodDonorTypeMasterDto.getUpdatedBy());
			bloodDonorTypeMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(bloodDonorTypeMasterDto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));

			update(bloodDonorTypeMaster);
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
				
			} catch (HibernateException he) {
				logger.error("updateBloodDonorTypeMasterStatus failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("updateBloodDonorTypeMasterStatus failed", e);
				throw new ServiceException(e.getMessage());
			}
	}
	@Override
	public Response getBloodDonorTypeMasterList(BloodDonorTypeMasterDto bloodDonorTypeMasterDto)
			throws ApplicationException {
		try {
			List<BloodDonorTypeMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BLOOD_DONOR_TYPE_MASTER_LIST")
					.setParameter("organizationId", bloodDonorTypeMasterDto.getOrganizationId())
					.setParameter("unitId", bloodDonorTypeMasterDto.getUnitId())
					.setFirstResult(bloodDonorTypeMasterDto.getOffset() != null ? bloodDonorTypeMasterDto.getOffset() : 0)
					.setMaxResults(bloodDonorTypeMasterDto.getNoOfRecordsPerPage() != null ? bloodDonorTypeMasterDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(BloodDonorTypeMasterDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
				
			} catch (HibernateException he) {
				logger.error("getBloodDonorTypeMasterList failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getBloodDonorTypeMasterList failed", e);
				throw new ServiceException(e.getMessage());
			}
		}
	@Override
	public Response getBloodDonorTypeMasterById(Integer bloodDonorTypeId, Integer organizationId, Integer unitId)
			throws ApplicationException {
		try {
			List<BloodDonorTypeMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BLOOD_DONOR_TYPE_MASTER_BY_ID")
					.setParameter("bloodDonorTypeId", bloodDonorTypeId)
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(BloodDonorTypeMasterDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
				
			} catch (HibernateException he) {
				logger.error("getBloodDonorTypeMasterById failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getBloodDonorTypeMasterById failed", e);
				throw new ServiceException(e.getMessage());
			}
		}


	@Override
	public Response getActiveBloodDonorTypeMaster(Integer organizationId, Integer unitId) throws ApplicationException {
		try {
			List<BloodDonorTypeMasterDto> list=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_ACTIVE_BLOOD_DONOR_TYPE_MASTER")
				.setParameter("organizationId", organizationId)
				.setParameter("unitId", unitId)
				.setResultTransformer(Transformers.aliasToBean(BloodDonorTypeMasterDto.class)).list();

				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
				
			} catch (HibernateException he) {
				logger.error("getActiveBloodDonorTypeMaster failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getActiveBloodDonorTypeMaster failed", e);
				throw new ServiceException(e.getMessage());
			}
		}
	@Override
	public Response getBloodDonorTypeMasterCount(BloodDonorTypeMasterDto bloodDonorTypeMasterDto)
			throws ApplicationException {
		try {
			Session session = sessionFactory.getCurrentSession();
			Criteria crit = session.createCriteria(BloodDonorTypeMaster.class);
			crit.add(Restrictions.eq("organizationId", bloodDonorTypeMasterDto.getOrganizationId()));
			crit.add(Restrictions.eq("unitId", bloodDonorTypeMasterDto.getUnitId()));
			crit.setProjection(Projections.rowCount());
			Long count = (Long) crit.uniqueResult();
				
				return new Response(SUCCESS, SUCCESS_CODE, null, null, count);
				
			} catch (HibernateException he) {
				logger.error("getBloodDonorTypeMasterCount failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getBloodDonorTypeMasterCount failed", e);
				throw new ServiceException(e.getMessage());
			}
		}
	@Override
	public Response getBloodDonorTypeMasterByName(BloodDonorTypeMasterDto bloodDonorTypeMasterDto)
			throws ApplicationException {
		try {
			List<BloodDonorTypeMasterDto> list=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_BLOOD_DONOR_TYPE_MASTER_BY_NAME")
				.setParameter("bloodDonorTypeDesc", bloodDonorTypeMasterDto.getBloodDonorTypeDesc().toLowerCase())
				.setResultTransformer(Transformers.aliasToBean(BloodDonorTypeMasterDto.class)).list();

				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
				
			} catch (HibernateException he) {
				logger.error("getBloodDonorTypeMasterByName failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getBloodDonorTypeMasterByName failed", e);
				throw new ServiceException(e.getMessage());
			}
		}

	@Override
	public Response getBloodDonorTypeMasterByNameNotId(BloodDonorTypeMasterDto bloodDonorTypeMasterDto)
			throws ApplicationException {
		try {
			List<BloodDonorTypeMasterDto> list=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_BLOOD_DONOR_TYPE_MASTER_BY_NAME_NOT_ID")
				.setParameter("bloodDonorTypeDesc", bloodDonorTypeMasterDto.getBloodDonorTypeDesc().toLowerCase())
				.setParameter("bloodDonorTypeId", bloodDonorTypeMasterDto.getBloodDonorTypeId())
				.setResultTransformer(Transformers.aliasToBean(BloodDonorTypeMasterDto.class)).list();

				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
				
			} catch (HibernateException he) {
				logger.error("getBloodDonorTypeMasterByNameNotId failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getBloodDonorTypeMasterByNameNotId failed", e);
				throw new ServiceException(e.getMessage());
			}
		}

}
