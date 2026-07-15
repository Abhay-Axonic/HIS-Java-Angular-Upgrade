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

import com.param.bloodBank.dto.BloodTestResultMasterDto;
import com.param.bloodBank.model.BloodTestResultMaster;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class BloodTestResultMasterDaoImpl extends GenericDao<BloodTestResultMaster, Integer> implements IBloodTestResultMasterDao, ICommonConstants {

	final static Logger logger = Logger.getLogger(BloodTestResultMasterDaoImpl.class);

	public BloodTestResultMasterDaoImpl() {
		super(BloodTestResultMaster.class);
	}

	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	public Response saveBloodTestResultMaster(BloodTestResultMasterDto bloodTestResultMasterDto)
			throws ApplicationException {
		try {
			BloodTestResultMaster bloodTestResultMaster=new BloodTestResultMaster();
			bloodTestResultMaster.setBloodTestResultCode(bloodTestResultMasterDto.getBloodTestResultCode());
			bloodTestResultMaster.setBloodTestResultDesc(bloodTestResultMasterDto.getBloodTestResultDesc());
			bloodTestResultMaster.setOrganizationId(bloodTestResultMasterDto.getOrganizationId());
			bloodTestResultMaster.setUnitId(bloodTestResultMasterDto.getUnitId());
			bloodTestResultMaster.setStatus(bloodTestResultMasterDto.getStatus());
			bloodTestResultMaster.setCreatedBy(bloodTestResultMasterDto.getCreatedBy());
			bloodTestResultMaster.setCreatedDate(GlobalCommonDateUtils.getDate(bloodTestResultMasterDto.getCreatedDate(), "dd-M-yyyy HH:ss:mm"));
			bloodTestResultMaster.setUpdatedBy(bloodTestResultMasterDto.getUpdatedBy());
			bloodTestResultMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(bloodTestResultMasterDto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));
			
			save(bloodTestResultMaster);
		
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
			
			} catch (HibernateException he) {
				logger.error("saveBloodTestResultMaster failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("saveBloodTestResultMaster failed", e);
				throw new ServiceException(e.getMessage());
			}
	}

	@Override
	public Response updateBloodTestResultMaster(BloodTestResultMasterDto bloodTestResultMasterDto)
			throws ApplicationException {
		try {
			BloodTestResultMaster bloodTestResultMaster=findById(bloodTestResultMasterDto.getBloodTestResultId());
			
			bloodTestResultMaster.setBloodTestResultCode(bloodTestResultMasterDto.getBloodTestResultCode());
			bloodTestResultMaster.setBloodTestResultDesc(bloodTestResultMasterDto.getBloodTestResultDesc());
			bloodTestResultMaster.setOrganizationId(bloodTestResultMasterDto.getOrganizationId());
			bloodTestResultMaster.setUnitId(bloodTestResultMasterDto.getUnitId());
			bloodTestResultMaster.setUpdatedBy(bloodTestResultMasterDto.getUpdatedBy());
			bloodTestResultMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(bloodTestResultMasterDto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));


			update(bloodTestResultMaster);
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
				
			} catch (HibernateException he) {
				logger.error("updateBloodTestResultMaster failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("updateBloodTestResultMaster failed", e);
				throw new ServiceException(e.getMessage());
			}
	}

	@Override
	public Response updateBloodTestResultMasterStatus(BloodTestResultMasterDto bloodTestResultMasterDto)
			throws ApplicationException {
		try {
			BloodTestResultMaster bloodTestResultMaster=findById(bloodTestResultMasterDto.getBloodTestResultId());
			bloodTestResultMaster.setStatus(bloodTestResultMasterDto.getStatus());
			bloodTestResultMaster.setUpdatedBy(bloodTestResultMasterDto.getUpdatedBy());
			bloodTestResultMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(bloodTestResultMasterDto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));

			update(bloodTestResultMaster);
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
				
			} catch (HibernateException he) {
				logger.error("updateBloodTestResultMasterStatus failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("updateBloodTestResultMasterStatus failed", e);
				throw new ServiceException(e.getMessage());
			}
	}
	@Override
	public Response getBloodTestResultMasterList(BloodTestResultMasterDto bloodTestResultMasterDto)
			throws ApplicationException {
		try {
			List<BloodTestResultMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BLOOD_TEST_RESULT_MASTER_LIST")
					.setParameter("organizationId", bloodTestResultMasterDto.getOrganizationId())
					.setParameter("unitId", bloodTestResultMasterDto.getUnitId())
					.setFirstResult(bloodTestResultMasterDto.getOffset() != null ? bloodTestResultMasterDto.getOffset() : 0)
					.setMaxResults(bloodTestResultMasterDto.getNoOfRecordsPerPage() != null ? bloodTestResultMasterDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(BloodTestResultMasterDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
				
			} catch (HibernateException he) {
				logger.error("getBloodTestResultMasterList failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getBloodTestResultMasterList failed", e);
				throw new ServiceException(e.getMessage());
			}
		}
	@Override
	public Response getBloodTestResultMasterById(Integer bloodTestResultId, Integer organizationId, Integer unitId)
			throws ApplicationException {
		try {
			List<BloodTestResultMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BLOOD_TEST_RESULT_MASTER_BY_ID")
					.setParameter("bloodTestResultId", bloodTestResultId)
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(BloodTestResultMasterDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
				
			} catch (HibernateException he) {
				logger.error("getBloodTestResultMasterById failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getBloodTestResultMasterById failed", e);
				throw new ServiceException(e.getMessage());
			}
		}


	@Override
	public Response getActiveBloodTestResultMaster(Integer organizationId, Integer unitId) throws ApplicationException {
		try {
			List<BloodTestResultMasterDto> list=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_ACTIVE_BLOOD_TEST_RESULT_MASTER")
				.setResultTransformer(Transformers.aliasToBean(BloodTestResultMasterDto.class)).list();

				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
				
			} catch (HibernateException he) {
				logger.error("getActiveBloodTestResultMaster failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getActiveBloodTestResultMaster failed", e);
				throw new ServiceException(e.getMessage());
			}
		}
	@Override
	public Response getBloodTestResultMasterCount(BloodTestResultMasterDto bloodTestResultMasterDto)
			throws ApplicationException {
		try {
			Session session = sessionFactory.getCurrentSession();
			Criteria crit = session.createCriteria(BloodTestResultMaster.class);
			crit.add(Restrictions.eq("organizationId", bloodTestResultMasterDto.getOrganizationId()));
			crit.add(Restrictions.eq("unitId", bloodTestResultMasterDto.getUnitId()));
			crit.setProjection(Projections.rowCount());
			Long count = (Long) crit.uniqueResult();
				
				return new Response(SUCCESS, SUCCESS_CODE, null, null, count);
				
			} catch (HibernateException he) {
				logger.error("getBloodTestResultMasterCount failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getBloodTestResultMasterCount failed", e);
				throw new ServiceException(e.getMessage());
			}
		}
	@Override
	public Response getBloodTestResultMasterByName(BloodTestResultMasterDto bloodTestResultMasterDto)
			throws ApplicationException {
		try {
			List<BloodTestResultMasterDto> list=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_BLOOD_TEST_RESULT_MASTER_BY_NAME")
				.setParameter("bloodTestResultDesc", bloodTestResultMasterDto.getBloodTestResultDesc().toLowerCase())
				.setResultTransformer(Transformers.aliasToBean(BloodTestResultMasterDto.class)).list();

				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
				
			} catch (HibernateException he) {
				logger.error("getBloodTestResultMasterByName failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getBloodTestResultMasterByName failed", e);
				throw new ServiceException(e.getMessage());
			}
		}

	@Override
	public Response getBloodTestResultMasterByNameNotId(BloodTestResultMasterDto bloodTestResultMasterDto)
			throws ApplicationException {
		try {
			List<BloodTestResultMasterDto> list=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_BLOOD_TEST_RESULT_MASTER_BY_NAME_NOT_ID")
				.setParameter("bloodTestResultDesc", bloodTestResultMasterDto.getBloodTestResultDesc().toLowerCase())
				.setParameter("bloodTestResultId", bloodTestResultMasterDto.getBloodTestResultId())
				.setResultTransformer(Transformers.aliasToBean(BloodTestResultMasterDto.class)).list();

				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
				
			} catch (HibernateException he) {
				logger.error("getBloodTestResultMasterByNameNotId failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getBloodTestResultMasterByNameNotId failed", e);
				throw new ServiceException(e.getMessage());
			}
		}

}


