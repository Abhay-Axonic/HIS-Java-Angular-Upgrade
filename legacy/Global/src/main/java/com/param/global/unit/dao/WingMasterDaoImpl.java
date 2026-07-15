package com.param.global.unit.dao;

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

import com.param.adt.master.unit.model.WingMaster;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.unit.dto.WingMasterDto;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class WingMasterDaoImpl extends GenericDao<WingMaster, Integer> implements IWingMasterDao, ICommonConstants {
	final static Logger logger = Logger.getLogger(WingMasterDaoImpl.class);


	public WingMasterDaoImpl() {
		super(WingMaster.class);
	}

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response getWingMasterListByOrgAndUnit(WingMasterDto wingMasterDto) throws ApplicationException {
		try {
			List<WingMasterDto> listWingMasterDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_WING_MASTER_LIST")
					.setParameter("organizationId", wingMasterDto.getOrganizationId())
					.setParameter("unitId", wingMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(WingMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listWingMasterDto, null);
		} catch (HibernateException he) {
		logger.error("getWingMasterListByOrgAndUnit failed", he);
		} catch (Exception e) {
		logger.error("getWingMasterListByOrgAndUnit failed", e);
		}
		return new Response(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response saveWingMaster(WingMasterDto wingMasterDto) throws ApplicationException {
		try {
			WingMaster wingMaster = new WingMaster();
			wingMaster.setWingCode(wingMasterDto.getWingCode());
			wingMaster.setWingDesc(wingMasterDto.getWingDesc());
			wingMaster.setCreatedBy(wingMasterDto.getCreatedBy());
			wingMaster.setUpdatedBy(wingMasterDto.getUpdatedBy());
			wingMaster.setCreatedDate(GlobalCommonDateUtils.getDate(wingMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			wingMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(wingMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			wingMaster.setStatus(wingMasterDto.getStatus());
			wingMaster.setOrganizationId(wingMasterDto.getOrganizationId());
			wingMaster.setUnitId(wingMasterDto.getUnitId());
			save(wingMaster);
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (HibernateException he) {
		logger.error("saveWingMaster failed", he);
		} catch (Exception e) {
		logger.error("saveWingMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateWingMaster(WingMasterDto wingMasterDto) throws ApplicationException {
		try {
			WingMaster wingMaster = findById(wingMasterDto.getWingId());
			wingMaster.setWingCode(wingMasterDto.getWingCode());
			wingMaster.setWingDesc(wingMasterDto.getWingDesc());
			wingMaster.setUpdatedBy(wingMasterDto.getUpdatedBy());
			wingMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(wingMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(wingMaster);
			return new Response<>(SUCCESS, null, COMMON_UPDATE, null, null);
		} catch (HibernateException he) {
		logger.error("updateWingMaster failed", he);
		} catch (Exception e) {
		logger.error("updateWingMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateWingMasterStatus(WingMasterDto wingMasterDto) throws ApplicationException {
		try {
			WingMaster wingMaster = findById(wingMasterDto.getWingId());
			wingMaster.setStatus(wingMasterDto.getStatus() == ACTIVE ? ACTIVE : INACTIVE);
			wingMaster.setUpdatedBy(wingMasterDto.getUpdatedBy());
			wingMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(wingMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(wingMaster);
			return new Response<>(SUCCESS, null, COMMON_UPDATE, null, null);
		} catch (HibernateException he) {
		logger.error("updateWingMasterStatus failed", he);
		} catch (Exception e) {
		logger.error("updateWingMasterStatus failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getWingMasterList(WingMasterDto wingMasterDto) throws ApplicationException {
		try {
			List<WingMasterDto> listWingMasterDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_WING_MASTER_LIST")
					.setParameter("orgId", wingMasterDto.getOrganizationId())
					.setParameter("unitId", wingMasterDto.getUnitId())
					.setFirstResult(wingMasterDto.getOffset() != null ? wingMasterDto.getOffset() : 0)
					.setMaxResults(wingMasterDto.getNoOfRecordsPerPage() != null ? wingMasterDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(WingMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listWingMasterDto, null);
		} catch (HibernateException he) {
		logger.error("getWingMasterList failed", he);
		} catch (Exception e) {
		logger.error("getWingMasterList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getWingMasterListCount(WingMasterDto wingMasterDto) throws ApplicationException {
		try {
			Session session=sessionFactory.getCurrentSession();
			Criteria c=session.createCriteria(WingMaster.class);
			c.add(Restrictions.eq("organizationId", wingMasterDto.getOrganizationId()));
			c.add(Restrictions.eq("unitId", wingMasterDto.getUnitId()));
			c.setProjection(Projections.rowCount());
			Long l=(Long) c.uniqueResult();
			return new Response<>(SUCCESS, null, null, null, l);
		} catch (HibernateException he) {
		logger.error("getWingMasterListCount failed", he);
		} catch (Exception e) {
		logger.error("getWingMasterListCount failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getWingMasterListById(Integer wingId) throws ApplicationException {
		try {
			List<WingMasterDto> listWingMasterDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_WING_MASTER_LIST_BY_ID")
					.setParameter("wingId", wingId)
					.setResultTransformer(Transformers.aliasToBean(WingMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listWingMasterDto, null);
		} catch (HibernateException he) {
		logger.error("getWingMasterListById failed", he);
		} catch (Exception e) {
		logger.error("getWingMasterListById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getWingMasterListByName(WingMasterDto wingMasterDto) throws ApplicationException {
		try {
			List<WingMasterDto> listWingMasterDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_WING_MASTER_LIST_BY_NAME")
					.setParameter("name", wingMasterDto.getWingDesc().toLowerCase())
					.setParameter("orgId", wingMasterDto.getOrganizationId())
					.setParameter("unitId", wingMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(WingMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listWingMasterDto, null);
		} catch (HibernateException he) {
		logger.error("getWingMasterListByName failed", he);
		} catch (Exception e) {
		logger.error("getWingMasterListByName failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getWingMasterListByNameNotById(WingMasterDto wingMasterDto) throws ApplicationException {
		try {
			List<WingMasterDto> listWingMasterDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_WING_MASTER_LIST_BY_NAME_NOT_BY_ID")
					.setParameter("name", wingMasterDto.getWingDesc().toLowerCase())
					.setParameter("wingId", wingMasterDto.getWingId())
					.setParameter("orgId", wingMasterDto.getOrganizationId())
					.setParameter("unitId", wingMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(WingMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listWingMasterDto, null);
		} catch (HibernateException he) {
		logger.error("getWingMasterListByNameNotById failed", he);
		} catch (Exception e) {
		logger.error("getWingMasterListByNameNotById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}
