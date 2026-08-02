package com.param.global.org.common.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.global.InstructionMasterDto;
import com.param.global.org.common.dto.DoseMasterDto;
import com.param.global.service.VerifiedIdentityService;
import com.param.global.org.common.model.DoseMaster;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
@SuppressWarnings({ "rawtypes", "unchecked" })
@Repository
public class DoseMasterDaoImpl extends GenericDao<DoseMaster, Integer> implements IDoseMasterDao, ICommonConstants {

	private static final Logger logger = Logger.getLogger(DoseMasterDaoImpl.class);

	@Autowired
	private VerifiedIdentityService verifiedIdentityService;

	public DoseMasterDaoImpl() {
		super(DoseMaster.class);
	}

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response saveDoseMaster(DoseMasterDto doseMasterDto) throws ApplicationException {
		try {
			DoseMaster doseMaster = new DoseMaster();
			doseMaster.setDoseCode(doseMasterDto.getDoseCode());
			doseMaster.setDoseDesc(doseMasterDto.getDoseDesc());
			doseMaster.setStatus(doseMasterDto.getStatus());
			doseMaster.setCreatedBy(doseMasterDto.getCreatedBy());
			doseMaster.setUpdatedBy(doseMasterDto.getUpdatedBy());
			doseMaster.setOrganizationId(doseMasterDto.getOrganizationId());
			doseMaster.setCreatedDate(
					GlobalCommonDateUtils.getDate(doseMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			doseMaster.setUpdatedDate(
					GlobalCommonDateUtils.getDate(doseMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			doseMaster = save(doseMaster);
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, doseMaster);
		} catch (HibernateException he) {
			logger.error("saveDoseMaster failed", he);
		} catch (Exception e) {
			logger.error("saveDoseMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getDoseMasterListById(int doseId, int orgId) throws ApplicationException {
		try {
			List<DoseMasterDto> doseMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DOSE_MASTER_LIST_BY_ID")
					.setInteger("doseId", doseId)
					.setInteger("orgId", orgId)
					.setResultTransformer(Transformers.aliasToBean(DoseMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, doseMasterDtoList, null);
		} catch (HibernateException he) {
			logger.error("getDoseMasterListById failed", he);
		} catch (Exception e) {
			logger.error("getDoseMasterListById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getDoseMasterList(DoseMasterDto doseMasterDto) throws ApplicationException {
		try {
			List<DoseMasterDto> doseMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DOSE_MASTER_LIST")
					.setInteger("orgId", doseMasterDto.getOrganizationId())
					.setFirstResult(doseMasterDto.getOffset() != null ? doseMasterDto.getOffset() : 0)
					.setMaxResults(doseMasterDto.getNoOfRecordsPerPage() != null ? doseMasterDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(DoseMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, doseMasterDtoList, null);
		} catch (HibernateException he) {
			logger.error("getDoseMasterList failed", he);
		} catch (Exception e) {
			logger.error("getDoseMasterList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateDoseMaster(DoseMasterDto doseMasterDto) throws ApplicationException {
		try {
			DoseMaster doseMaster = findById(doseMasterDto.getDoseId());

			// BR-148: cross-tenant IDOR fix -- the target row's organization must
			// match the JWT-verified identity, never a client-supplied value.
			if (doseMaster == null || doseMaster.getOrganizationId() == null
					|| !doseMaster.getOrganizationId()
							.equals(verifiedIdentityService.getVerifiedOrganizationId())) {
				return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}

			doseMaster.setDoseCode(doseMasterDto.getDoseCode());
			doseMaster.setDoseDesc(doseMasterDto.getDoseDesc());
			doseMaster.setUpdatedBy(doseMasterDto.getUpdatedBy());
			doseMaster.setUpdatedDate(
					GlobalCommonDateUtils.getDate(doseMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			doseMaster = update(doseMaster);
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, doseMaster);
		} catch (HibernateException he) {
			logger.error("updateDoseMaster failed", he);
		} catch (Exception e) {
			logger.error("updateDoseMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateDoseMasterStatus(DoseMasterDto doseMasterDto) throws ApplicationException {
		try {
			DoseMaster doseMaster = findById(doseMasterDto.getDoseId());

			// BR-148: cross-tenant IDOR fix -- same ownership check as updateDoseMaster.
			if (doseMaster == null || doseMaster.getOrganizationId() == null
					|| !doseMaster.getOrganizationId()
							.equals(verifiedIdentityService.getVerifiedOrganizationId())) {
				return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}

			doseMaster.setStatus(doseMasterDto.getStatus() == ACTIVE ? ACTIVE : INACTIVE);
			doseMaster.setUpdatedBy(doseMasterDto.getUpdatedBy());
			doseMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(doseMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(doseMaster);
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (HibernateException he) {
			logger.error("updateDoseMasterStatus failed", he);
		} catch (Exception e) {
			logger.error("updateDoseMasterStatus failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getDoseMasterCount(DoseMasterDto doseMasterDto) throws ApplicationException {
		try {
			Session session=sessionFactory.getCurrentSession();
			Criteria c=session.createCriteria(DoseMaster.class); 
			c.add(Restrictions.eq("organizationId", doseMasterDto.getOrganizationId()));
			c.setProjection(Projections.rowCount());  
			Long count = (Long)c.uniqueResult();
			return new Response(SUCCESS, null, null, null, count);
		} catch (HibernateException he) {
			logger.error("getDoseMasterCount failed", he);
		} catch (Exception e) {
			logger.error("getDoseMasterCount failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActiveDoseMasterList(DoseMasterDto doseMasterDto) throws ApplicationException {
		try {
			List<DoseMasterDto> doseMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_DOSE_MASTER_LIST")
					.setInteger("orgId", doseMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(DoseMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, doseMasterDtoList, null);
		} catch (HibernateException he) {
			logger.error("getActiveDoseMasterList failed", he);
		} catch (Exception e) {
			logger.error("getActiveDoseMasterList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getDoseMasterListByName(DoseMasterDto doseMasterDto) throws ApplicationException {
		try {
			List<DoseMasterDto> doseMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DOSE_MASTER_LIST_BY_NAME")
					.setInteger("orgId", doseMasterDto.getOrganizationId())
					.setString("name", doseMasterDto.getDoseDesc().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(DoseMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, doseMasterDtoList, null);
		} catch (HibernateException he) {
			logger.error("getDoseMasterListByName failed", he);
		} catch (Exception e) {
			logger.error("getDoseMasterListByName failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getDoseMasterListByNameNotById(DoseMasterDto doseMasterDto) throws ApplicationException {
		try {
			List<DoseMasterDto> doseMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DOSEMASTER_LIST_BY_NAME_NOT_BY_ID")
					.setInteger("doseId", doseMasterDto.getDoseId())
					.setInteger("orgId", doseMasterDto.getOrganizationId())
					.setString("name", doseMasterDto.getDoseDesc().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(DoseMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, doseMasterDtoList, null);
		} catch (HibernateException he) {
			logger.error("getDoseMasterListByNameNotById failed", he);
		} catch (Exception e) {
			logger.error("getDoseMasterListByNameNotById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
}
