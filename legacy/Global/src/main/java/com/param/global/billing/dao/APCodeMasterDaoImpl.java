package com.param.global.billing.dao;

import java.util.List;


import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import in.param.exception.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.billing.dto.APCodeMasterDto;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.model.APCodeMaster;

import in.param.common.dao.GenericDao;
@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class APCodeMasterDaoImpl extends GenericDao<APCodeMaster, Integer> implements IAPCodeMasterDao, ICommonConstants{

	final static Logger logger = Logger.getLogger(APCodeMasterDaoImpl.class);

	@Autowired
	SessionFactory sessionFactory;

	public APCodeMasterDaoImpl() {
		super(APCodeMaster.class);
	}

	@Override
	public Response saveAPCodeMaster(APCodeMasterDto aPCodeMasterDto) throws ApplicationException {
		try {
			APCodeMaster apCodeMaster=new APCodeMaster();
			apCodeMaster.setaPCode(aPCodeMasterDto.getaPCode());
			apCodeMaster.setaPCodeDesc(aPCodeMasterDto.getaPCodeDesc());
			apCodeMaster.setStatus(aPCodeMasterDto.getStatus());
			apCodeMaster.setOrgId(aPCodeMasterDto.getOrgId());
			apCodeMaster.setUnitId(aPCodeMasterDto.getUnitId());
			apCodeMaster.setCreatedBy(aPCodeMasterDto.getCreatedBy());
			apCodeMaster.setCreatedDate(GlobalCommonDateUtils.getDate(aPCodeMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			apCodeMaster.setUpdatedBy(aPCodeMasterDto.getUpdatedBy());
			apCodeMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(aPCodeMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			
			
			save(apCodeMaster);
		
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);

			} catch (HibernateException he) {
				logger.error("saveAPCodeMaster failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("saveAPCodeMaster failed", e);
				throw new ServiceException(e.getMessage());
			}
	}

	@Override
	public Response updateAPCodeMaster(APCodeMasterDto aPCodeMasterDto) throws ApplicationException {
		try {
			APCodeMaster apCodeMaster=findById(aPCodeMasterDto.getaPCodeId());
			apCodeMaster.setaPCode(aPCodeMasterDto.getaPCode());
			apCodeMaster.setaPCodeDesc(aPCodeMasterDto.getaPCodeDesc());
			apCodeMaster.setOrgId(aPCodeMasterDto.getOrgId());
			apCodeMaster.setUnitId(aPCodeMasterDto.getUnitId());
			apCodeMaster.setUpdatedBy(aPCodeMasterDto.getUpdatedBy());
			apCodeMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(aPCodeMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));

			update(apCodeMaster);
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);

			} catch (HibernateException he) {
				logger.error("updateAPCodeMaster failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("updateAPCodeMaster failed", e);
				throw new ServiceException(e.getMessage());
			}
	}
	@Override
	public Response updateAPCodeMasterStatus(APCodeMasterDto aPCodeMasterDto) throws ApplicationException {
		try {
			APCodeMaster apCodeMaster=findById(aPCodeMasterDto.getaPCodeId());
			apCodeMaster.setStatus(aPCodeMasterDto.getStatus());
			apCodeMaster.setOrgId(aPCodeMasterDto.getOrgId());
			apCodeMaster.setUnitId(aPCodeMasterDto.getUnitId());
			apCodeMaster.setUpdatedBy(aPCodeMasterDto.getUpdatedBy());
			apCodeMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(aPCodeMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));

			update(apCodeMaster);
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);

			} catch (HibernateException he) {
				logger.error("updateAPCodeMasterStatus failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("updateAPCodeMasterStatus failed", e);
				throw new ServiceException(e.getMessage());
			}
		}

	@Override
	public Response getAPCodeMasterList(Integer orgId, Integer unitId, Integer offset, Integer noOfRecordsPerPage) throws ApplicationException {
		try {
			List<APCodeMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_AP_CODE_MASTER_LIST")
					.setParameter("orgId",orgId)
					.setParameter("unitId", unitId)
					.setFirstResult(offset != null ? offset : 0)
					.setMaxResults(noOfRecordsPerPage != null ? noOfRecordsPerPage : 10)
					.setResultTransformer(Transformers.aliasToBean(APCodeMasterDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

			} catch (HibernateException he) {
				logger.error("getAPCodeMasterList failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getAPCodeMasterList failed", e);
				throw new ServiceException(e.getMessage());
			}
		}

	@Override
	public Response getAPCodeMasterById(Integer aPCodeId,Integer orgId, Integer unitId) throws ApplicationException {
		try {
			List<APCodeMasterDto> list=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_AP_CODE_MASTER_BY_ID")
				.setParameter("aPCodeId", aPCodeId)
				.setParameter("orgId",orgId)
				.setParameter("unitId", unitId)
				.setResultTransformer(Transformers.aliasToBean(APCodeMasterDto.class)).list();

				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

			} catch (HibernateException he) {
				logger.error("getAPCodeMasterById failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getAPCodeMasterById failed", e);
				throw new ServiceException(e.getMessage());
			}
		}


	@Override
	public Response getAPCodeMasterCount(Integer orgId, Integer unitId) throws ApplicationException {
		try {
			Session session = sessionFactory.getCurrentSession();
			Criteria crit = session.createCriteria(APCodeMaster.class);
			crit.add(Restrictions.eq("orgId", orgId));
			crit.add(Restrictions.eq("unitId", unitId));
			crit.setProjection(Projections.rowCount());
			Long count = (Long) crit.uniqueResult();
				
				return new Response(SUCCESS, SUCCESS_CODE, null, null, count);

			} catch (HibernateException he) {
				logger.error("getAPCodeMasterCount failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getAPCodeMasterCount failed", e);
				throw new ServiceException(e.getMessage());
			}
		}

	@Override
	public Response getActiveAPCodeMaster(Integer orgId, Integer unitId) throws ApplicationException {
		try {
			List<APCodeMasterDto> list=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_ACTIVE_AP_CODE_MASTER")
				.setParameter("orgId",orgId)
				.setParameter("unitId", unitId)
				.setResultTransformer(Transformers.aliasToBean(APCodeMasterDto.class)).list();

				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

			} catch (HibernateException he) {
				logger.error("getActiveAPCodeMaster failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getActiveAPCodeMaster failed", e);
				throw new ServiceException(e.getMessage());
			}
		}

	@Override
	public Response getAPCodeMasterByName(APCodeMasterDto aPCodeMasterDto) throws ApplicationException {
		try {
			List<APCodeMasterDto> list=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_AP_CODE_MASTER_BY_NAME")
				.setParameter("aPCodeDesc", aPCodeMasterDto.getaPCodeDesc().toLowerCase())
				.setParameter("orgId",aPCodeMasterDto.getOrgId())
				.setParameter("unitId", aPCodeMasterDto.getUnitId())
				.setResultTransformer(Transformers.aliasToBean(APCodeMasterDto.class)).list();

				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

			} catch (HibernateException he) {
				logger.error("getAPCodeMasterByName failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getAPCodeMasterByName failed", e);
				throw new ServiceException(e.getMessage());
			}
		}

	@Override
	public Response getAPCodeMasterByNameNotId(APCodeMasterDto aPCodeMasterDto) throws ApplicationException {
		try {
			List<APCodeMasterDto> list=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_AP_CODE_MASTER_BY_NAME_NOT_ID")
				.setParameter("aPCodeId", aPCodeMasterDto.getaPCodeId())
				.setParameter("aPCodeDesc", aPCodeMasterDto.getaPCodeDesc().toLowerCase())
				.setParameter("orgId",aPCodeMasterDto.getOrgId())
				.setParameter("unitId", aPCodeMasterDto.getUnitId())
				.setResultTransformer(Transformers.aliasToBean(APCodeMasterDto.class)).list();

				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

			} catch (HibernateException he) {
				logger.error("getAPCodeMasterByNameNotId failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getAPCodeMasterByNameNotId failed", e);
				throw new ServiceException(e.getMessage());
			}
		}

}
