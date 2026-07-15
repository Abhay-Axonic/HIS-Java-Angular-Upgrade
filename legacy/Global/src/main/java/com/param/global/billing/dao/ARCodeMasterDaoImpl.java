package com.param.global.billing.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import in.param.exception.ApplicationException;
import org.springframework.stereotype.Repository;


import com.param.global.billing.dto.ARCodeMasterDto;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;

import com.param.global.model.ARCodeMaster;

import in.param.common.dao.GenericDao;
@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class ARCodeMasterDaoImpl extends GenericDao<ARCodeMaster, Integer> implements IARCodeMasterDao, ICommonConstants {

	final static Logger logger = Logger.getLogger(ARCodeMasterDaoImpl.class);

	public ARCodeMasterDaoImpl() {
		super(ARCodeMaster.class);

	}

	@Override
	public Response saveARCodeMaster(ARCodeMasterDto aRCodeMasterDto) throws ApplicationException {
		try {
			ARCodeMaster arCodeMaster=new ARCodeMaster();
			arCodeMaster.setaRCode(aRCodeMasterDto.getaRCode());
			arCodeMaster.setaRCodeDesc(aRCodeMasterDto.getaRCodeDesc());
			arCodeMaster.setStatus(aRCodeMasterDto.getStatus());
			arCodeMaster.setOrgId(aRCodeMasterDto.getOrgId());
			arCodeMaster.setUnitId(aRCodeMasterDto.getUnitId());
			arCodeMaster.setCreatedBy(aRCodeMasterDto.getCreatedBy());
			arCodeMaster.setCreatedDate(GlobalCommonDateUtils.getDate(aRCodeMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			arCodeMaster.setUpdatedBy(aRCodeMasterDto.getUpdatedBy());
			arCodeMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(aRCodeMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			
			
			save(arCodeMaster);

				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);

			} catch (HibernateException he) {
				logger.error("saveARCodeMaster failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("saveARCodeMaster failed", e);
				throw new ServiceException(e.getMessage());
			}
	}
	
	@Override
	public Response updateARCodeMaster(ARCodeMasterDto aRCodeMasterDto) throws ApplicationException {
		try {
			ARCodeMaster arCodeMaster=findById(aRCodeMasterDto.getaRCodeId());
			arCodeMaster.setaRCode(aRCodeMasterDto.getaRCode());
			arCodeMaster.setaRCodeDesc(aRCodeMasterDto.getaRCodeDesc());
			arCodeMaster.setOrgId(aRCodeMasterDto.getOrgId());
			arCodeMaster.setUnitId(aRCodeMasterDto.getUnitId());
			arCodeMaster.setUpdatedBy(aRCodeMasterDto.getUpdatedBy());
			arCodeMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(aRCodeMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));

			update(arCodeMaster);
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);

			} catch (HibernateException he) {
				logger.error("updateARCodeMaster failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("updateARCodeMaster failed", e);
				throw new ServiceException(e.getMessage());
			}
	}

	@Override
	public Response updateARCodeMasterStatus(ARCodeMasterDto aRCodeMasterDto) throws ApplicationException {
		try {
			ARCodeMaster arCodeMaster=findById(aRCodeMasterDto.getaRCodeId());
			arCodeMaster.setStatus(aRCodeMasterDto.getStatus());
			arCodeMaster.setOrgId(aRCodeMasterDto.getOrgId());
			arCodeMaster.setUnitId(aRCodeMasterDto.getUnitId());
			arCodeMaster.setUpdatedBy(aRCodeMasterDto.getUpdatedBy());
			arCodeMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(aRCodeMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));

			update(arCodeMaster);
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);

			} catch (HibernateException he) {
				logger.error("updateARCodeMasterStatus failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("updateARCodeMasterStatus failed", e);
				throw new ServiceException(e.getMessage());
			}
		}


	@Override
	public Response getARCodeMasterList(Integer orgId, Integer unitId,  Integer offset, Integer noOfRecordsPerPage) throws ApplicationException {
		try {
			List<ARCodeMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_AR_CODE_MASTER_LIST")
					.setParameter("orgId", orgId)
					.setParameter("unitId", unitId)
					.setFirstResult(offset != null ? offset : 0)
					.setMaxResults(noOfRecordsPerPage != null ? noOfRecordsPerPage : 10)
					.setResultTransformer(Transformers.aliasToBean(ARCodeMasterDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

			} catch (HibernateException he) {
				logger.error("getARCodeMasterList failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getARCodeMasterList failed", e);
				throw new ServiceException(e.getMessage());
			}
		}

	@Override
	public Response getARCodeMasterById(Integer aRCodeId,Integer orgId, Integer unitId) throws ApplicationException {
		try {
			List<ARCodeMasterDto> list=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_AR_CODE_MASTER_BY_ID")
				.setParameter("aRCodeId", aRCodeId)
				.setParameter("orgId", orgId)
				.setParameter("unitId", unitId)
				.setResultTransformer(Transformers.aliasToBean(ARCodeMasterDto.class)).list();

				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

			} catch (HibernateException he) {
				logger.error("getARCodeMasterById failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getARCodeMasterById failed", e);
				throw new ServiceException(e.getMessage());
			}
		}

	@Override
	public Response getARCodeMasterCount(Integer orgId, Integer unitId) throws ApplicationException {
		try {
			Session session = sessionFactory.getCurrentSession();
			Criteria crit = session.createCriteria(ARCodeMaster.class);
			crit.add(Restrictions.eq("orgId", orgId));
			crit.add(Restrictions.eq("unitId", unitId));
			crit.setProjection(Projections.rowCount());
			Long count = (Long) crit.uniqueResult();
				
				return new Response(SUCCESS, SUCCESS_CODE, null, null, count);

			} catch (HibernateException he) {
				logger.error("getARCodeMasterCount failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getARCodeMasterCount failed", e);
				throw new ServiceException(e.getMessage());
			}
		}

	@Override
	public Response getActiveARCodeMaster(Integer orgId, Integer unitId) throws ApplicationException {
		try {
			List<ARCodeMasterDto> list=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_ACTIVE_AR_CODE_MASTER")
				.setParameter("orgId", orgId)
				.setParameter("unitId", unitId)
				.setResultTransformer(Transformers.aliasToBean(ARCodeMasterDto.class)).list();

				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

			} catch (HibernateException he) {
				logger.error("getActiveARCodeMaster failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getActiveARCodeMaster failed", e);
				throw new ServiceException(e.getMessage());
			}
		}


	@Override
	public Response getARCodeMasterByName(ARCodeMasterDto aRCodeMasterDto) throws ApplicationException {
		try {
			List<ARCodeMasterDto> list=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_AR_CODE_MASTER_BY_NAME")
				.setParameter("aRCodeDesc", aRCodeMasterDto.getaRCodeDesc().toLowerCase())
				.setParameter("orgId",aRCodeMasterDto.getOrgId())
				.setParameter("unitId", aRCodeMasterDto.getUnitId())
				.setResultTransformer(Transformers.aliasToBean(ARCodeMasterDto.class)).list();

				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

			} catch (HibernateException he) {
				logger.error("getARCodeMasterByName failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getARCodeMasterByName failed", e);
				throw new ServiceException(e.getMessage());
			}
		}

	@Override
	public Response getARCodeMasterByNameNotId(ARCodeMasterDto aRCodeMasterDto) throws ApplicationException {
		try {
			List<ARCodeMasterDto> list=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_AR_CODE_MASTER_BY_NAME_NOT_ID")
				.setParameter("aRCodeId", aRCodeMasterDto.getaRCodeId())
				.setParameter("aRCodeDesc", aRCodeMasterDto.getaRCodeDesc().toLowerCase())
				.setParameter("orgId",aRCodeMasterDto.getOrgId())
				.setParameter("unitId", aRCodeMasterDto.getUnitId())
				.setResultTransformer(Transformers.aliasToBean(ARCodeMasterDto.class)).list();

				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

			} catch (HibernateException he) {
				logger.error("getARCodeMasterByNameNotId failed", he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error("getARCodeMasterByNameNotId failed", e);
				throw new ServiceException(e.getMessage());
			}
		}


}
