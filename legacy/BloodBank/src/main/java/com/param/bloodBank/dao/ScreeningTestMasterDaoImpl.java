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

import com.param.bloodBank.dto.ScreeningTestMasterDto;
import com.param.bloodBank.model.ScreeningTestMaster;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class ScreeningTestMasterDaoImpl extends GenericDao<ScreeningTestMaster, Integer> implements IScreeningTestMasterDao, ICommonConstants{

	final static Logger logger = Logger.getLogger(ScreeningTestMasterDaoImpl.class);

	public ScreeningTestMasterDaoImpl() {
		super(ScreeningTestMaster.class);
	}

	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	public Response saveScreeningTestMaster(ScreeningTestMasterDto screeningTestMasterDto)
			throws ApplicationException {
		try {
			ScreeningTestMaster screeningTestMaster=new ScreeningTestMaster();
			screeningTestMaster.setScreeningTestCode(screeningTestMasterDto.getScreeningTestCode());
			screeningTestMaster.setScreeningTestDesc(screeningTestMasterDto.getScreeningTestDesc());
			screeningTestMaster.setTestId(screeningTestMasterDto.getTestId());
			screeningTestMaster.setOrganizationId(screeningTestMasterDto.getOrganizationId());
			screeningTestMaster.setUnitId(screeningTestMasterDto.getUnitId());
			screeningTestMaster.setStatus(screeningTestMasterDto.getStatus());
			screeningTestMaster.setCreatedBy(screeningTestMasterDto.getCreatedBy());
			screeningTestMaster.setCreatedDate(GlobalCommonDateUtils.getDate(screeningTestMasterDto.getCreatedDate(), "dd-M-yyyy HH:ss:mm"));
			screeningTestMaster.setUpdatedBy(screeningTestMasterDto.getUpdatedBy());
			screeningTestMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(screeningTestMasterDto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));
			
			save(screeningTestMaster);
		
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
			
			} catch (HibernateException he) {
				logger.error("saveScreeningTestMaster failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("saveScreeningTestMaster failed", e);
				throw new ServiceException(e.getMessage());
			}
	}

	@Override
	public Response updateScreeningTestMaster(ScreeningTestMasterDto screeningTestMasterDto)
			throws ApplicationException {
		try {
			ScreeningTestMaster screeningTestMaster=findById(screeningTestMasterDto.getScreeningTestId());
			
			screeningTestMaster.setScreeningTestCode(screeningTestMasterDto.getScreeningTestCode());
			screeningTestMaster.setScreeningTestDesc(screeningTestMasterDto.getScreeningTestDesc());
			screeningTestMaster.setOrganizationId(screeningTestMasterDto.getOrganizationId());
			screeningTestMaster.setTestId(screeningTestMasterDto.getTestId());
			screeningTestMaster.setUnitId(screeningTestMasterDto.getUnitId());
			screeningTestMaster.setUpdatedBy(screeningTestMasterDto.getUpdatedBy());
			screeningTestMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(screeningTestMasterDto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));


			update(screeningTestMaster);
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
				
			} catch (HibernateException he) {
				logger.error("updateScreeningTestMaster failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("updateScreeningTestMaster failed", e);
				throw new ServiceException(e.getMessage());
			}
	}

	@Override
	public Response updateScreeningTestMasterStatus(ScreeningTestMasterDto screeningTestMasterDto)
			throws ApplicationException {
		try {
			ScreeningTestMaster screeningTestMaster=findById(screeningTestMasterDto.getScreeningTestId());
			screeningTestMaster.setStatus(screeningTestMasterDto.getStatus());
			screeningTestMaster.setUpdatedBy(screeningTestMasterDto.getUpdatedBy());
			screeningTestMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(screeningTestMasterDto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));

			update(screeningTestMaster);
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
				
			} catch (HibernateException he) {
				logger.error("updateScreeningTestMasterStatus failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("updateScreeningTestMasterStatus failed", e);
				throw new ServiceException(e.getMessage());
			}
	}
	@Override
	public Response getScreeningTestMasterList(ScreeningTestMasterDto screeningTestMasterDto)
			throws ApplicationException {
		try {
			List<ScreeningTestMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SCREENING_TEST_MASTER_LIST")
					.setParameter("organizationId", screeningTestMasterDto.getOrganizationId())
					.setParameter("unitId", screeningTestMasterDto.getUnitId())
					.setFirstResult(screeningTestMasterDto.getOffset() != null ? screeningTestMasterDto.getOffset() : 0)
					.setMaxResults(screeningTestMasterDto.getNoOfRecordsPerPage() != null ? screeningTestMasterDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(ScreeningTestMasterDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
				
			} catch (HibernateException he) {
				logger.error("getScreeningTestMasterList failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getScreeningTestMasterList failed", e);
				throw new ServiceException(e.getMessage());
			}
		}
	@Override
	public Response getScreeningTestMasterById(Integer screeningTestId, Integer organizationId, Integer unitId)
			throws ApplicationException {
		try {
			List<ScreeningTestMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SCREENING_TEST_MASTER_BY_ID")
					.setParameter("screeningTestId", screeningTestId)
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(ScreeningTestMasterDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
				
			} catch (HibernateException he) {
				logger.error("getScreeningTestMasterById failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getScreeningTestMasterById failed", e);
				throw new ServiceException(e.getMessage());
			}
		}


	@Override
	public Response getActiveScreeningTestMaster(Integer organizationId, Integer unitId) throws ApplicationException {
		try {
			List<ScreeningTestMasterDto> list=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_ACTIVE_SCREENING_TEST_MASTER")
				.setParameter("organizationId", organizationId)
				.setParameter("unitId", unitId)
				.setResultTransformer(Transformers.aliasToBean(ScreeningTestMasterDto.class)).list();

				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
				
			} catch (HibernateException he) {
				logger.error("getActiveScreeningTestMaster failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getActiveScreeningTestMaster failed", e);
				throw new ServiceException(e.getMessage());
			}
		}
	@Override
	public Response getScreeningTestMasterCount(ScreeningTestMasterDto screeningTestMasterDto)
			throws ApplicationException {
		try {
			Session session = sessionFactory.getCurrentSession();
			Criteria crit = session.createCriteria(ScreeningTestMaster.class);
			crit.add(Restrictions.eq("organizationId", screeningTestMasterDto.getOrganizationId()));
			crit.add(Restrictions.eq("unitId", screeningTestMasterDto.getUnitId()));
			crit.setProjection(Projections.rowCount());
			Long count = (Long) crit.uniqueResult();
				
				return new Response(SUCCESS, SUCCESS_CODE, null, null, count);
				
			} catch (HibernateException he) {
				logger.error("getScreeningTestMasterCount failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getScreeningTestMasterCount failed", e);
				throw new ServiceException(e.getMessage());
			}
		}
	@Override
	public Response getScreeningTestMasterByName(ScreeningTestMasterDto screeningTestMasterDto)
			throws ApplicationException {
		try {
			List<ScreeningTestMasterDto> list=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_SCREENING_TEST_MASTER_BY_NAME")
				.setParameter("screeningTestDesc", screeningTestMasterDto.getScreeningTestDesc().toLowerCase())
				.setResultTransformer(Transformers.aliasToBean(ScreeningTestMasterDto.class)).list();

				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
				
			} catch (HibernateException he) {
				logger.error("getScreeningTestMasterByName failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getScreeningTestMasterByName failed", e);
				throw new ServiceException(e.getMessage());
			}
		}

	@Override
	public Response getScreeningTestMasterByNameNotId(ScreeningTestMasterDto screeningTestMasterDto)
			throws ApplicationException {
		try {
			List<ScreeningTestMasterDto> list=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_SCREENING_TEST_MASTER_BY_NAME_NOT_ID")
				.setParameter("screeningTestDesc", screeningTestMasterDto.getScreeningTestDesc().toLowerCase())
				.setParameter("screeningTestId", screeningTestMasterDto.getScreeningTestId())
				.setResultTransformer(Transformers.aliasToBean(ScreeningTestMasterDto.class)).list();

				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
				
			} catch (HibernateException he) {
				logger.error("getScreeningTestMasterByNameNotId failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getScreeningTestMasterByNameNotId failed", e);
				throw new ServiceException(e.getMessage());
			}
		}

}




