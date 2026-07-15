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

import com.param.bloodBank.dto.BloodBankMasterDto;
import com.param.bloodBank.model.BloodBankMaster;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class BloodBankMasterDaoImpl extends GenericDao<BloodBankMaster, Integer> implements IBloodBankMasterDao, ICommonConstants{

	final static Logger logger = Logger.getLogger(BloodBankMasterDaoImpl.class);

	public BloodBankMasterDaoImpl() {
		super(BloodBankMaster.class);
	}
	
		@Autowired
		SessionFactory sessionFactory;
		
	@Override
	public Response saveBloodBankMaster(BloodBankMasterDto bloodBankMasterDto)
			throws ApplicationException {
		try {
			BloodBankMaster bloodBankMaster=new BloodBankMaster();
			bloodBankMaster.setBloodBankCode(bloodBankMasterDto.getBloodBankCode());
			bloodBankMaster.setBloodBankDesc(bloodBankMasterDto.getBloodBankDesc());
			bloodBankMaster.setContactNo(bloodBankMasterDto.getContactNo());
			bloodBankMaster.setAddress(bloodBankMasterDto.getAddress());
			bloodBankMaster.setOrganizationId(bloodBankMasterDto.getOrganizationId());
			bloodBankMaster.setUnitId(bloodBankMasterDto.getUnitId());
			bloodBankMaster.setStatus(bloodBankMasterDto.getStatus());
			bloodBankMaster.setCreatedBy(bloodBankMasterDto.getCreatedBy());
			bloodBankMaster.setCreatedDate(GlobalCommonDateUtils.getDate(bloodBankMasterDto.getCreatedDate(), "dd-M-yyyy HH:ss:mm"));
			bloodBankMaster.setUpdatedBy(bloodBankMasterDto.getUpdatedBy());
			bloodBankMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(bloodBankMasterDto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));
			
			save(bloodBankMaster);
		
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
			
			} catch (HibernateException he) {
				logger.error("saveBloodBankMaster failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("saveBloodBankMaster failed", e);
				throw new ServiceException(e.getMessage());
			}
	}

	@Override
	public Response updateBloodBankMaster(BloodBankMasterDto bloodBankMasterDto)
			throws ApplicationException {
		try {
			BloodBankMaster bloodBankMaster=findById(bloodBankMasterDto.getBloodBankId());
			
			bloodBankMaster.setBloodBankCode(bloodBankMasterDto.getBloodBankCode());
			bloodBankMaster.setBloodBankDesc(bloodBankMasterDto.getBloodBankDesc());
			bloodBankMaster.setContactNo(bloodBankMasterDto.getContactNo());
			bloodBankMaster.setAddress(bloodBankMasterDto.getAddress());
			bloodBankMaster.setOrganizationId(bloodBankMasterDto.getOrganizationId());
			bloodBankMaster.setUnitId(bloodBankMasterDto.getUnitId());
			bloodBankMaster.setUpdatedBy(bloodBankMasterDto.getUpdatedBy());
			bloodBankMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(bloodBankMasterDto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));


			update(bloodBankMaster);
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
				
			} catch (HibernateException he) {
				logger.error("updateBloodBankMaster failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("updateBloodBankMaster failed", e);
				throw new ServiceException(e.getMessage());
			}
	}

	@Override
	public Response updateBloodBankMasterStatus(BloodBankMasterDto bloodBankMasterDto)
			throws ApplicationException {
		try {
			BloodBankMaster bloodBankMaster=findById(bloodBankMasterDto.getBloodBankId());
			bloodBankMaster.setStatus(bloodBankMasterDto.getStatus());
			bloodBankMaster.setUpdatedBy(bloodBankMasterDto.getUpdatedBy());
			bloodBankMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(bloodBankMasterDto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));

			update(bloodBankMaster);
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
				
			} catch (HibernateException he) {
				logger.error("updateBloodBankMasterStatus failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("updateBloodBankMasterStatus failed", e);
				throw new ServiceException(e.getMessage());
			}
	}
	@Override
	public Response getBloodBankMasterList(BloodBankMasterDto bloodBankMasterDto)
			throws ApplicationException {
		try {
			List<BloodBankMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BLOOD_BANK_MASTER_LIST")
					.setParameter("organizationId", bloodBankMasterDto.getOrganizationId())
					.setParameter("unitId", bloodBankMasterDto.getUnitId())
					.setFirstResult(bloodBankMasterDto.getOffset() != null ? bloodBankMasterDto.getOffset() : 0)
					.setMaxResults(bloodBankMasterDto.getNoOfRecordsPerPage() != null ? bloodBankMasterDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(BloodBankMasterDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
				
			} catch (HibernateException he) {
				logger.error("getBloodBankMasterList failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getBloodBankMasterList failed", e);
				throw new ServiceException(e.getMessage());
			}
		}
	@Override
	public Response getBloodBankMasterById(Integer bloodBankId, Integer organizationId, Integer unitId)
			throws ApplicationException {
		try {
			List<BloodBankMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BLOOD_BANK_MASTER_BY_ID")
					.setParameter("bloodBankId", bloodBankId)
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(BloodBankMasterDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
				
			} catch (HibernateException he) {
				logger.error("getBloodBankMasterById failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getBloodBankMasterById failed", e);
				throw new ServiceException(e.getMessage());
			}
		}


	@Override
	public Response getActiveBloodBankMaster(Integer organizationId, Integer unitId) throws ApplicationException {
		try {
			List<BloodBankMasterDto> list=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_ACTIVE_BLOOD_BANK_MASTER")
				.setParameter("organizationId", organizationId)
				.setParameter("unitId", unitId)
				.setResultTransformer(Transformers.aliasToBean(BloodBankMasterDto.class)).list();

				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
				
			} catch (HibernateException he) {
				logger.error("getActiveBloodBankMaster failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getActiveBloodBankMaster failed", e);
				throw new ServiceException(e.getMessage());
			}
		}
	@Override
	public Response getBloodBankMasterCount(BloodBankMasterDto bloodBankMasterDto)
			throws ApplicationException {
		try {
			Session session = sessionFactory.getCurrentSession();
			Criteria crit = session.createCriteria(BloodBankMaster.class);
			crit.add(Restrictions.eq("organizationId", bloodBankMasterDto.getOrganizationId()));
			crit.add(Restrictions.eq("unitId", bloodBankMasterDto.getUnitId()));
			crit.setProjection(Projections.rowCount());
			Long count = (Long) crit.uniqueResult();
				
				return new Response(SUCCESS, SUCCESS_CODE, null, null, count);
				
			} catch (HibernateException he) {
				logger.error("getBloodBankMasterCount failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getBloodBankMasterCount failed", e);
				throw new ServiceException(e.getMessage());
			}
		}
	@Override
	public Response getBloodBankMasterByName(BloodBankMasterDto bloodBankMasterDto)
			throws ApplicationException {
		try {
			List<BloodBankMasterDto> list=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_BLOOD_BANK_MASTER_BY_NAME")
				.setParameter("bloodBankDesc", bloodBankMasterDto.getBloodBankDesc().toLowerCase())
				.setResultTransformer(Transformers.aliasToBean(BloodBankMasterDto.class)).list();

				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
				
			} catch (HibernateException he) {
				logger.error("getBloodBankMasterByName failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getBloodBankMasterByName failed", e);
				throw new ServiceException(e.getMessage());
			}
		}

	@Override
	public Response getBloodBankMasterByNameNotId(BloodBankMasterDto bloodBankMasterDto)
			throws ApplicationException {
		try {
			List<BloodBankMasterDto> list=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_BLOOD_BANK_MASTER_BY_NAME_NOT_ID")
				.setParameter("bloodBankDesc", bloodBankMasterDto.getBloodBankDesc().toLowerCase())
				.setParameter("bloodBankId", bloodBankMasterDto.getBloodBankId())
				.setResultTransformer(Transformers.aliasToBean(BloodBankMasterDto.class)).list();

				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
				
			} catch (HibernateException he) {
				logger.error("getBloodBankMasterByNameNotId failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getBloodBankMasterByNameNotId failed", e);
				throw new ServiceException(e.getMessage());
			}
		}

}

