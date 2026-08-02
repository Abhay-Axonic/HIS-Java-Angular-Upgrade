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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.AllergyTypeMasterDto;
import com.param.global.service.VerifiedIdentityService;
import com.param.global.model.AllergyTypeMaster;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class AllergyTypeMasterDaoImpl extends GenericDao<AllergyTypeMaster, Integer>
		implements IAllergyTypeMasterDao, ICommonConstants {

	private static final Logger logger = Logger.getLogger(AllergyTypeMasterDaoImpl.class);

	@Autowired
	private VerifiedIdentityService verifiedIdentityService;

	public AllergyTypeMasterDaoImpl() {
		super(AllergyTypeMaster.class);
	}

	@Override
	public Response getListOfAllergyTypeMaster(AllergyTypeMasterDto allergyTypeMasterDto) throws ApplicationException {
		try {
			StatelessSession statlessSession = sessionFactory.openStatelessSession();
			List<AllergyTypeMasterDto> listAllergyPatientMapperDto = statlessSession
					.getNamedQuery("GET_LIST_ALLERGY_TYPE_MAPPER")
					.setParameter("organizationId", allergyTypeMasterDto.getOrganizationId())
//					.setParameter("unitId", allergyTypeMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(AllergyTypeMasterDto.class)).list();
			statlessSession.close();
			return new Response(SUCCESS, SUCCESS_CODE, null, listAllergyPatientMapperDto, null);
		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response saveAllergyTypeMaster(AllergyTypeMasterDto allergyTypeMasterDto) throws ApplicationException {
		try {
			AllergyTypeMaster allergyTypeMaster = new AllergyTypeMaster();
			allergyTypeMaster.setAllergyTypeDesc(allergyTypeMasterDto.getAllergyTypeDesc());
			allergyTypeMaster.setAllergyTypeName(allergyTypeMasterDto.getAllergyTypeName());
			allergyTypeMaster.setIsGeneric(allergyTypeMasterDto.getIsGeneric());
			allergyTypeMaster.setStatus(allergyTypeMasterDto.getStatus());
//			allergyTypeMaster.setUnitId(allergyTypeMasterDto.getUnitId());
			allergyTypeMaster.setOrganizationId(allergyTypeMasterDto.getOrganizationId());
			allergyTypeMaster.setCreatedBy(allergyTypeMasterDto.getCreatedBy());
			allergyTypeMaster.setCreatedDate(
					GlobalCommonDateUtils.getDate(allergyTypeMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			allergyTypeMaster.setUpdatedBy(allergyTypeMasterDto.getUpdatedBy());
			allergyTypeMaster.setUpdatedDate(
					GlobalCommonDateUtils.getDate(allergyTypeMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			save(allergyTypeMaster);
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateAllergyTypeMaster(AllergyTypeMasterDto allergyTypeMasterDto) throws ApplicationException {
		try {
			AllergyTypeMaster allergyTypeMaster = findById(allergyTypeMasterDto.getAllergyTypeId());

			// BR-147: cross-tenant IDOR fix -- the target row's organization must
			// match the JWT-verified identity, never a client-supplied value.
			if (allergyTypeMaster == null || allergyTypeMaster.getOrganizationId() == null
					|| !allergyTypeMaster.getOrganizationId()
							.equals(verifiedIdentityService.getVerifiedOrganizationId())) {
				return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}

			allergyTypeMaster.setAllergyTypeDesc(allergyTypeMasterDto.getAllergyTypeDesc());
			allergyTypeMaster.setAllergyTypeName(allergyTypeMasterDto.getAllergyTypeName());
			allergyTypeMaster.setIsGeneric(allergyTypeMasterDto.getIsGeneric());
			allergyTypeMaster.setUpdatedBy(allergyTypeMasterDto.getUpdatedBy());
			allergyTypeMaster.setUpdatedDate(
					GlobalCommonDateUtils.getDate(allergyTypeMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(allergyTypeMaster);
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateAllergyTypeMasterStatus(AllergyTypeMasterDto allergyTypeMasterDto)
			throws ApplicationException {
		try {
			AllergyTypeMaster allergyTypeMaster = findById(allergyTypeMasterDto.getAllergyTypeId());

			// BR-147: cross-tenant IDOR fix -- same ownership check as updateAllergyTypeMaster.
			if (allergyTypeMaster == null || allergyTypeMaster.getOrganizationId() == null
					|| !allergyTypeMaster.getOrganizationId()
							.equals(verifiedIdentityService.getVerifiedOrganizationId())) {
				return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}

			allergyTypeMaster.setStatus(allergyTypeMasterDto.getStatus() == ACTIVE ? ACTIVE : INACTIVE);
			allergyTypeMaster.setUpdatedBy(allergyTypeMasterDto.getUpdatedBy());
			allergyTypeMaster.setUpdatedDate(
					GlobalCommonDateUtils.getDate(allergyTypeMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(allergyTypeMaster);
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getAllergyTypeMasterList(AllergyTypeMasterDto allergyTypeMasterDto) throws ApplicationException {
		try {
			List<AllergyTypeMasterDto> allergyTypeMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ALLERGY_TYPE_MASTER_LIST")
					.setParameter("orgId", allergyTypeMasterDto.getOrganizationId())
//					.setParameter("unitId", allergyTypeMasterDto.getUnitId())
					.setFirstResult(allergyTypeMasterDto.getOffset() != null ? allergyTypeMasterDto.getOffset() : 0)
					.setMaxResults(allergyTypeMasterDto.getNoOfRecordsPerPage() != null ? allergyTypeMasterDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(AllergyTypeMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, allergyTypeMasterDtoList, null);
		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getAllergyTypeMasterListCount(AllergyTypeMasterDto allergyTypeMasterDto)
			throws ApplicationException {
		try {
			Session session=sessionFactory.getCurrentSession();
			Criteria crit=session.createCriteria(AllergyTypeMaster.class);
			crit.add(Restrictions.eq("organizationId", allergyTypeMasterDto.getOrganizationId()));
//			crit.add(Restrictions.eq("unitId", allergyTypeMasterDto.getUnitId()));
			crit.setProjection(Projections.rowCount());
			Long count = (Long)crit.uniqueResult();
			return new Response(SUCCESS, null, null, null, count);
		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getAllergyTypeMasterListById(Integer allergyTypeId, int orgId, int unitId)
			throws ApplicationException {
		try {
			List<AllergyTypeMasterDto> allergyTypeMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ALLERGY_TYPE_MASTER_LIST_BY_ID")
					.setParameter("orgId", orgId)
//					.setParameter("unitId", unitId)
					.setParameter("allergyTypeId", allergyTypeId)
					.setResultTransformer(Transformers.aliasToBean(AllergyTypeMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, allergyTypeMasterDtoList, null);
		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getAllergyTypeMasterListByName(AllergyTypeMasterDto allergyTypeMasterDto)
			throws ApplicationException {
		try {
			List<AllergyTypeMasterDto> allergyTypeMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ALLERGY_TYPE_MASTER_LIST_BY_NAME")
					.setParameter("orgId", allergyTypeMasterDto.getOrganizationId())
//					.setParameter("unitId", allergyTypeMasterDto.getUnitId())
					.setParameter("name", allergyTypeMasterDto.getAllergyTypeDesc().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(AllergyTypeMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, allergyTypeMasterDtoList, null);
		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getAllergyTypeMasterListByNameNotById(AllergyTypeMasterDto allergyTypeMasterDto)
			throws ApplicationException {
		try {
			List<AllergyTypeMasterDto> allergyTypeMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ALLERGY_TYPE_MASTER_LIST_BY_NAME_NOT_BY_ID")
					.setParameter("orgId", allergyTypeMasterDto.getOrganizationId())
//					.setParameter("unitId", allergyTypeMasterDto.getUnitId())
					.setParameter("name", allergyTypeMasterDto.getAllergyTypeDesc().toLowerCase())
					.setParameter("allergyTypeId", allergyTypeMasterDto.getAllergyTypeId())
					.setResultTransformer(Transformers.aliasToBean(AllergyTypeMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, allergyTypeMasterDtoList, null);
		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getAllergyTypeMasterCheckIsDrugById(AllergyTypeMasterDto allergyTypeMasterDto)
			throws ApplicationException {

		try {
			List<AllergyTypeMasterDto> allergyTypeMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ALLERGY_TYPE_MASTER_LIST_CHECK_ISDRUG_BY_ID")
					.setParameter("orgId", allergyTypeMasterDto.getOrganizationId())
					.setParameter("allergyTypeId", allergyTypeMasterDto.getAllergyTypeId())
					.setResultTransformer(Transformers.aliasToBean(AllergyTypeMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, allergyTypeMasterDtoList, null);
		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);

	}

	@Override
	public Response getAllergyTypeMasterCheckIsDrug(AllergyTypeMasterDto allergyTypeMasterDto)
			throws ApplicationException {


		try {
			List<AllergyTypeMasterDto> allergyTypeMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ALLERGY_TYPE_MASTER_LIST_CHECK_ISDRUG")
					.setParameter("orgId", allergyTypeMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(AllergyTypeMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, allergyTypeMasterDtoList, null);
		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);


	}

}
