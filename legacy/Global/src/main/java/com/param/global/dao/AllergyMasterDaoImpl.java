package com.param.global.dao;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.AllergyMasterDto;
import com.param.global.model.AllergyMaster;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class AllergyMasterDaoImpl extends GenericDao<AllergyMaster, Integer>
		implements IAllergyMasterDao, ICommonConstants {

	final static Logger logger = Logger.getLogger(AllergyMasterDaoImpl.class);

	public AllergyMasterDaoImpl() {
		super(AllergyMaster.class);
	}

	@Override
	public Response getListOfAllergyMaster(AllergyMasterDto allergyMasterDto) throws ApplicationException {
		try {
			StatelessSession statlessSession = sessionFactory.openStatelessSession();
			List<AllergyMasterDto> listAllergyMasterDto = statlessSession
					.getNamedQuery("GET_ALLERGY_MASTER_LIST_BY_ALLERGY_TYPE_ID")
					.setParameter("organizationId", allergyMasterDto.getOrganizationId())
//					.setParameter("unitId", allergyMasterDto.getUnitId())
					.setParameter("allergyTypeId", allergyMasterDto.getAllergyTypeId())
					.setResultTransformer(Transformers.aliasToBean(AllergyMasterDto.class)).list();
			statlessSession.close();
			return new Response(SUCCESS, SUCCESS_CODE, null, listAllergyMasterDto, null);
		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
			logger.error("getListOfAllergyMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response saveAllergyMaster(AllergyMasterDto allergyMasterDto) throws ApplicationException {
		try {
			AllergyMaster allergyMaster = new AllergyMaster();
			allergyMaster.setAllergyDesc(allergyMasterDto.getAllergyDesc());
			allergyMaster.setAllergyName(allergyMasterDto.getAllergyName());
			allergyMaster.setAllergyTypeId(allergyMasterDto.getAllergyTypeId());
			allergyMaster.setStatus(allergyMasterDto.getStatus());
			allergyMaster.setCimsCode(allergyMasterDto.getCimsCode());
			allergyMaster.setCimsType(allergyMasterDto.getCimsType());
//			allergyMaster.setUnitId(allergyMasterDto.getUnitId());
			allergyMaster.setOrganizationId(allergyMasterDto.getOrganizationId());
			allergyMaster.setCreatedBy(allergyMasterDto.getCreatedBy());
			allergyMaster.setCreatedDate(
					GlobalCommonDateUtils.getDate(allergyMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			allergyMaster.setUpdatedBy(allergyMasterDto.getUpdatedBy());
			allergyMaster.setUpdatedDate(
					GlobalCommonDateUtils.getDate(allergyMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			save(allergyMaster);
			allergyMasterDto.setAllergyId(allergyMaster.getAllergyId());
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, allergyMasterDto);

		} catch (HibernateException he) {
			logger.error("saveAllergyMaster failed", he);
		} catch (Exception e) {
			logger.error("saveAllergyMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateAllergyMaster(AllergyMasterDto allergyMasterDto) throws ApplicationException {
		try {
			AllergyMaster allergyMaster = findById(allergyMasterDto.getAllergyId());
			allergyMaster.setAllergyDesc(allergyMasterDto.getAllergyDesc());
			allergyMaster.setAllergyName(allergyMasterDto.getAllergyName());
			allergyMaster.setAllergyTypeId(allergyMasterDto.getAllergyTypeId());
			allergyMaster.setCimsCode(allergyMasterDto.getCimsCode());
			allergyMaster.setCimsType(allergyMasterDto.getCimsType());
			allergyMaster.setUpdatedBy(allergyMasterDto.getUpdatedBy());
			allergyMaster.setUpdatedDate(
					GlobalCommonDateUtils.getDate(allergyMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(allergyMaster);
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (HibernateException he) {
			logger.error("updateAllergyMaster failed", he);
		} catch (Exception e) {
			logger.error("updateAllergyMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateAllergyMasterStatus(AllergyMasterDto allergyMasterDto) throws ApplicationException {
		try {
			AllergyMaster allergyMaster = findById(allergyMasterDto.getAllergyId());
			allergyMaster.setStatus(allergyMasterDto.getStatus() == ACTIVE ? ACTIVE : INACTIVE);
			allergyMaster.setUpdatedBy(allergyMasterDto.getUpdatedBy());
			allergyMaster.setUpdatedDate(
					GlobalCommonDateUtils.getDate(allergyMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(allergyMaster);
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (HibernateException he) {
			logger.error("updateAllergyMasterStatus failed", he);
		} catch (Exception e) {
			logger.error("updateAllergyMasterStatus failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getAllergyMasterList(AllergyMasterDto allergyMasterDto) throws ApplicationException {
		try {
			List<AllergyMasterDto> allergyMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ALLERGY_MASTER_LIST")
					.setParameter("orgId", allergyMasterDto.getOrganizationId())
//					.setParameter("unitId", allergyMasterDto.getUnitId())
					.setFirstResult(allergyMasterDto.getOffset() != null ? allergyMasterDto.getOffset() : 0)
					.setMaxResults(allergyMasterDto.getNoOfRecordsPerPage() != null ? allergyMasterDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(AllergyMasterDto.class)).list();
			return new Response<>(SUCCESS, null, null, allergyMasterDtoList, null);
		} catch (HibernateException he) {
			logger.error("getAllergyMasterList failed", he);
		} catch (Exception e) {
			logger.error("getAllergyMasterList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getAllergyMasterListCount(AllergyMasterDto allergyMasterDto) throws ApplicationException {
		try {
			Session session = sessionFactory.getCurrentSession();
			Criteria crit = session.createCriteria(AllergyMaster.class);
			crit.add(Restrictions.eq("organizationId", allergyMasterDto.getOrganizationId()));
//			crit.add(Restrictions.eq("unitId", allergyMasterDto.getUnitId()));
			crit.setProjection(Projections.rowCount());
			Long count = (Long) crit.uniqueResult();
			return new Response(SUCCESS, null, null, null, count);
		} catch (HibernateException he) {
			logger.error("getAllergyMasterListCount failed", he);
		} catch (Exception e) {
			logger.error("getAllergyMasterListCount failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getAllergyMasterListById(Integer allergyId, int orgId, int unitId) throws ApplicationException {
		try {
			List<AllergyMasterDto> allergyMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ALLERGY_MASTER_LIST_BY_ID")
					.setParameter("orgId", orgId)
//					.setParameter("unitId", unitId)
					.setParameter("allergyId", allergyId)
					.setResultTransformer(Transformers.aliasToBean(AllergyMasterDto.class)).list();
			return new Response<>(SUCCESS, null, null, allergyMasterDtoList, null);
		} catch (HibernateException he) {
			logger.error("getAllergyMasterListById failed", he);
		} catch (Exception e) {
			logger.error("getAllergyMasterListById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getAllergyMasterListByName(AllergyMasterDto allergyMasterDto) throws ApplicationException {
		try {
			List<AllergyMasterDto> allergyMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ALLERGY_MASTER_LIST_BY_NAME")
					.setParameter("orgId", allergyMasterDto.getOrganizationId())
//					.setParameter("unitId", allergyMasterDto.getUnitId())
					.setParameter("name", allergyMasterDto.getAllergyDesc().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(AllergyMasterDto.class)).list();
			return new Response<>(SUCCESS, null, null, allergyMasterDtoList, null);
		} catch (HibernateException he) {
			logger.error("getAllergyMasterListByName failed", he);
		} catch (Exception e) {
			logger.error("getAllergyMasterListByName failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getAllergyMasterListByNameNotById(AllergyMasterDto allergyMasterDto) throws ApplicationException {
		try {
			List<AllergyMasterDto> allergyMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ALLERGY_MASTER_LIST_BY_NAME_NOT_BY_ID")
					.setParameter("orgId", allergyMasterDto.getOrganizationId())
//					.setParameter("unitId", allergyMasterDto.getUnitId())
					.setParameter("allergyId", allergyMasterDto.getAllergyId())
					.setParameter("allergyTypeId", allergyMasterDto.getAllergyTypeId())
					.setParameter("name", allergyMasterDto.getAllergyDesc().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(AllergyMasterDto.class)).list();
			return new Response<>(SUCCESS, null, null, allergyMasterDtoList, null);
		} catch (HibernateException he) {
			logger.error("getAllergyMasterListByNameNotById failed", he);
		} catch (Exception e) {
			logger.error("getAllergyMasterListByNameNotById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}
