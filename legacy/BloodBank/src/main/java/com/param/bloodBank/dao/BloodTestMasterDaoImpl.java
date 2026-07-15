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

import com.param.bloodBank.dto.BloodTestMasterDto;
import com.param.bloodBank.model.BloodTestMaster;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class BloodTestMasterDaoImpl extends GenericDao<BloodTestMaster, Integer> implements IBloodTestMasterDao, ICommonConstants{

	final static Logger logger = Logger.getLogger(BloodTestMasterDaoImpl.class);

	public BloodTestMasterDaoImpl() {
		super(BloodTestMaster.class);
	}

	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	public Response saveBloodTestMaster(BloodTestMasterDto bloodTestMasterDto)
			throws ApplicationException {
		try {
			BloodTestMaster bloodTestMaster=new BloodTestMaster();
			bloodTestMaster.setBloodTestCode(bloodTestMasterDto.getBloodTestCode());
			bloodTestMaster.setBloodTestDesc(bloodTestMasterDto.getBloodTestDesc());
			bloodTestMaster.setOrganizationId(bloodTestMasterDto.getOrganizationId());
			bloodTestMaster.setUnitId(bloodTestMasterDto.getUnitId());
			bloodTestMaster.setStatus(bloodTestMasterDto.getStatus());
			bloodTestMaster.setCreatedBy(bloodTestMasterDto.getCreatedBy());
			bloodTestMaster.setCreatedDate(GlobalCommonDateUtils.getDate(bloodTestMasterDto.getCreatedDate(), "dd-M-yyyy HH:ss:mm"));
			bloodTestMaster.setUpdatedBy(bloodTestMasterDto.getUpdatedBy());
			bloodTestMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(bloodTestMasterDto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));
			
			save(bloodTestMaster);
		
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
			
			} catch (HibernateException he) {
				logger.error("saveBloodTestMaster failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("saveBloodTestMaster failed", e);
				throw new ServiceException(e.getMessage());
			}
	}

	@Override
	public Response updateBloodTestMaster(BloodTestMasterDto bloodTestMasterDto)
			throws ApplicationException {
		try {
			BloodTestMaster bloodTestMaster=findById(bloodTestMasterDto.getBloodTestId());
			
			bloodTestMaster.setBloodTestCode(bloodTestMasterDto.getBloodTestCode());
			bloodTestMaster.setBloodTestDesc(bloodTestMasterDto.getBloodTestDesc());
			bloodTestMaster.setOrganizationId(bloodTestMasterDto.getOrganizationId());
			bloodTestMaster.setUnitId(bloodTestMasterDto.getUnitId());
			bloodTestMaster.setUpdatedBy(bloodTestMasterDto.getUpdatedBy());
			bloodTestMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(bloodTestMasterDto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));


			update(bloodTestMaster);
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
				
			} catch (HibernateException he) {
				logger.error("updateBloodTestMaster failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("updateBloodTestMaster failed", e);
				throw new ServiceException(e.getMessage());
			}
	}

	@Override
	public Response updateBloodTestMasterStatus(BloodTestMasterDto bloodTestMasterDto)
			throws ApplicationException {
		try {
			BloodTestMaster bloodTestMaster=findById(bloodTestMasterDto.getBloodTestId());
			bloodTestMaster.setStatus(bloodTestMasterDto.getStatus());
			bloodTestMaster.setUpdatedBy(bloodTestMasterDto.getUpdatedBy());
			bloodTestMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(bloodTestMasterDto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));

			update(bloodTestMaster);
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
				
			} catch (HibernateException he) {
				logger.error("updateBloodTestMasterStatus failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("updateBloodTestMasterStatus failed", e);
				throw new ServiceException(e.getMessage());
			}
	}
	@Override
	public Response getBloodTestMasterList(BloodTestMasterDto bloodTestMasterDto)
			throws ApplicationException {
		try {
			List<BloodTestMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BLOOD_TEST_MASTER_LIST")
					.setParameter("organizationId", bloodTestMasterDto.getOrganizationId())
					.setParameter("unitId", bloodTestMasterDto.getUnitId())
					.setFirstResult(bloodTestMasterDto.getOffset() != null ? bloodTestMasterDto.getOffset() : 0)
					.setMaxResults(bloodTestMasterDto.getNoOfRecordsPerPage() != null ? bloodTestMasterDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(BloodTestMasterDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
				
			} catch (HibernateException he) {
				logger.error("getBloodTestMasterList failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getBloodTestMasterList failed", e);
				throw new ServiceException(e.getMessage());
			}
		}
	@Override
	public Response getBloodTestMasterById(Integer bloodTestId, Integer organizationId, Integer unitId)
			throws ApplicationException {
		try {
			List<BloodTestMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BLOOD_TEST_MASTER_BY_ID")
					.setParameter("bloodTestId", bloodTestId)
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(BloodTestMasterDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
				
			} catch (HibernateException he) {
				logger.error("getBloodTestMasterById failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getBloodTestMasterById failed", e);
				throw new ServiceException(e.getMessage());
			}
		}


	@Override
	public Response getActiveBloodTestMaster(Integer organizationId, Integer unitId) throws ApplicationException {
		try {
			List<BloodTestMasterDto> list=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_ACTIVE_BLOOD_TEST_MASTER")
				.setParameter("organizationId", organizationId)
				.setParameter("unitId", unitId)
				.setResultTransformer(Transformers.aliasToBean(BloodTestMasterDto.class)).list();

				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
				
			} catch (HibernateException he) {
				logger.error("getActiveBloodTestMaster failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getActiveBloodTestMaster failed", e);
				throw new ServiceException(e.getMessage());
			}
		}
	@Override
	public Response getBloodTestMasterCount(BloodTestMasterDto bloodTestMasterDto)
			throws ApplicationException {
		try {
			Session session = sessionFactory.getCurrentSession();
			Criteria crit = session.createCriteria(BloodTestMaster.class);
			crit.add(Restrictions.eq("organizationId", bloodTestMasterDto.getOrganizationId()));
			crit.add(Restrictions.eq("unitId", bloodTestMasterDto.getUnitId()));
			crit.setProjection(Projections.rowCount());
			Long count = (Long) crit.uniqueResult();
				
				return new Response(SUCCESS, SUCCESS_CODE, null, null, count);
				
			} catch (HibernateException he) {
				logger.error("getBloodTestMasterCount failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getBloodTestMasterCount failed", e);
				throw new ServiceException(e.getMessage());
			}
		}
	@Override
	public Response getBloodTestMasterByName(BloodTestMasterDto bloodTestMasterDto)
			throws ApplicationException {
		try {
			List<BloodTestMasterDto> list=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_BLOOD_TEST_MASTER_BY_NAME")
				.setParameter("bloodTestDesc", bloodTestMasterDto.getBloodTestDesc().toLowerCase())
				.setResultTransformer(Transformers.aliasToBean(BloodTestMasterDto.class)).list();

				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
				
			} catch (HibernateException he) {
				logger.error("getBloodTestMasterByName failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getBloodTestMasterByName failed", e);
				throw new ServiceException(e.getMessage());
			}
		}

	@Override
	public Response getBloodTestMasterByNameNotId(BloodTestMasterDto bloodTestMasterDto)
			throws ApplicationException {
		try {
			List<BloodTestMasterDto> list=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_BLOOD_TEST_MASTER_BY_NAME_NOT_ID")
				.setParameter("bloodTestDesc", bloodTestMasterDto.getBloodTestDesc().toLowerCase())
				.setParameter("bloodTestId", bloodTestMasterDto.getBloodTestId())
				.setResultTransformer(Transformers.aliasToBean(BloodTestMasterDto.class)).list();

				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
				
			} catch (HibernateException he) {
				logger.error("getBloodTestMasterByNameNotId failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getBloodTestMasterByNameNotId failed", e);
				throw new ServiceException(e.getMessage());
			}
		}

}




