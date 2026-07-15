package com.param.global.dao;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.ExaminationMasterDto;
import com.param.global.model.ExaminationMaster;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ExaminationMasterDaoImpl extends GenericDao<ExaminationMaster, Integer>
		implements IExaminationMasterDao, ICommonConstants {

	private static final Logger logger = Logger.getLogger(ExaminationMasterDaoImpl.class);

	public ExaminationMasterDaoImpl() {
		super(ExaminationMaster.class);
	}

	@Override
	public Response saveExaminationMaster(ExaminationMasterDto examinationMasterDto) throws ApplicationException {
		try {
			ExaminationMaster examinationMaster = new ExaminationMaster();
			examinationMaster.setExaminationDesc(examinationMasterDto.getExaminationDesc());
			examinationMaster.setExaminationCode(examinationMasterDto.getExaminationCode());
			examinationMaster.setStatus(examinationMasterDto.getStatus());
			examinationMaster.setOrganizationId(examinationMasterDto.getOrganizationId());
			examinationMaster.setUnitId(examinationMasterDto.getUnitId());
			examinationMaster.setCreatedBy(examinationMasterDto.getCreatedBy());
			examinationMaster.setUpdatedBy(examinationMasterDto.getUpdatedBy());
			examinationMaster.setCreatedDate(new Date());
			examinationMaster.setUpdatedDate(new Date());
			
			save(examinationMaster);

			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		} catch (HibernateException he) {
			logger.error("Error while saving examination master", he);
		} catch (Exception e) {
			logger.error("Error while saving examination master", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response updateExaminationMaster(ExaminationMasterDto examinationMasterDto) throws ApplicationException {
		try {
			ExaminationMaster examinationMaster = findById(examinationMasterDto.getExaminationMasterId());
			examinationMaster.setExaminationDesc(examinationMasterDto.getExaminationDesc());
			examinationMaster.setExaminationCode(examinationMasterDto.getExaminationCode());
			examinationMaster.setStatus(examinationMasterDto.getStatus());
			examinationMaster.setOrganizationId(examinationMasterDto.getOrganizationId());
			examinationMaster.setUnitId(examinationMasterDto.getUnitId());
			examinationMaster.setUpdatedBy(examinationMasterDto.getUpdatedBy());
			examinationMaster.setUpdatedDate(new Date());


			update(examinationMaster);

			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (HibernateException he) {
			logger.error("Error while updating examination master", he);
		} catch (Exception e) {
			logger.error("Error while updating examination master", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response updateExaminationMasterStatus(ExaminationMasterDto examinationMasterDto)
			throws ApplicationException {
		try {
			ExaminationMaster examinationMaster = findById(examinationMasterDto.getExaminationMasterId());
			examinationMaster.setStatus(examinationMasterDto.getStatus());
			examinationMaster.setUpdatedBy(examinationMasterDto.getUpdatedBy());
			examinationMaster.setUpdatedDate(new Date());

			update(examinationMaster);

			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (HibernateException he) {
			logger.error("Error while updating examination master status", he);
		} catch (Exception e) {
			logger.error("Error while updating examination master status", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getExaminationMasterById(ExaminationMasterDto examinationMasterDto) throws ApplicationException {
		try {
			List<ExaminationMasterDto> list= sessionFactory.getCurrentSession()
					.getNamedQuery("GET_EXAMINATION_MASTER_BY_ID")
					 .setParameter("id", examinationMasterDto.getExaminationMasterId())
					.setParameter("organizationId", examinationMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(ExaminationMasterDto.class)).list();
			return new Response(SUCCESS, null, null, list, null);

		} catch (HibernateException he) {
			logger.error("Error while fetching examination master by id", he);
		} catch (Exception e) {
			logger.error("Error while fetching examination master by id", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getExaminationMasterList(ExaminationMasterDto examinationMasterDto) throws ApplicationException {
		try {
			List<ExaminationMasterDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_EXAMINATION_MASTER_LIST")
					.setParameter("organizationId", examinationMasterDto.getOrganizationId())
					.setFirstResult(examinationMasterDto.getOffset() != null ? examinationMasterDto.getOffset()  : 0)
					.setMaxResults(examinationMasterDto.getNoOfRecordsPerPage() != null ? examinationMasterDto.getNoOfRecordsPerPage()  : 10)
					.setResultTransformer(Transformers.aliasToBean(ExaminationMasterDto.class)).list();
			return new Response(SUCCESS, null, null, list, null);

		} catch (HibernateException he) {
			logger.error("Error while fetching examination master list", he);
		} catch (Exception e) {
			logger.error("Error while fetching examination master list", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getExaminationMasterActiveList(ExaminationMasterDto examinationMasterDto)
			throws ApplicationException {
		try {
			List<ExaminationMasterDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_EXAMINATION_MASTER_ACTIVE_LIST")
					.setParameter("organizationId", examinationMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(ExaminationMasterDto.class)).list();
			return new Response(SUCCESS, null, null, list, null);

		} catch (HibernateException he) {
			logger.error("Error while fetching examination master active list", he);
		} catch (Exception e) {
			logger.error("Error while fetching examination master active list", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getExaminationMasterCount(ExaminationMasterDto examinationMasterDto)
			throws ApplicationException {
		try {
			
			Session session = sessionFactory.getCurrentSession();
			Criteria crit = session.createCriteria(ExaminationMaster.class);
			crit.add(Restrictions.eq("organizationId", examinationMasterDto.getOrganizationId()));
			crit.setProjection(Projections.rowCount());
			Long count = (Long) crit.uniqueResult();
			
			return new Response(SUCCESS, SUCCESS_CODE, null, null, count);

		} catch (HibernateException he) {
			logger.error("Error while fetching examination master count", he);
		} catch (Exception e) {
			logger.error("Error while fetching examination master count", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response searchExaminationMasterActiveList(ExaminationMasterDto examinationMasterDto)
			throws ApplicationException {
		try {
			List<ExaminationMasterDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("SEARCH_EXAMINATION_MASTER_ACTIVE_LIST")
					.setParameter("searchKeyword", "%"+ examinationMasterDto.getSearchKeyword().toLowerCase() + "%")
					.setParameter("organizationId", examinationMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(ExaminationMasterDto.class)).list();
			return new Response(SUCCESS, null, null, list, null);

		} catch (HibernateException he) {
			logger.error("Error while searching examination master active list", he);
		} catch (Exception e) {
			logger.error("Error while searching examination master active list", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
}
