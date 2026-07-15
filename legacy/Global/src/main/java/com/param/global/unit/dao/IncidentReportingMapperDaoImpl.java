package com.param.global.unit.dao;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
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

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.IBillingCommonConstants;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IGlobalUnitMasterDao;
import com.param.global.model.CabinMaster;
import com.param.global.model.IncidentReportingMapper;
import com.param.global.model.IncidentReportingMapper;
import com.param.global.unit.dto.CabinMasterDto;
import com.param.global.unit.dto.IncidentReportingMapperDto;
import com.param.global.utility.Util;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({ "rawtypes", "unchecked" })
@Repository
public class IncidentReportingMapperDaoImpl extends GenericDao<IncidentReportingMapper, Integer>
		implements IIncidentReportingMapperDao, ICommonConstants, IBillingCommonConstants {
	final static Logger logger = Logger.getLogger(IncidentReportingMapperDaoImpl.class);


	public IncidentReportingMapperDaoImpl() {
		super(IncidentReportingMapper.class);
	}

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	private IGlobalUnitMasterDao iGlobalUnitMasterDao;

	@Override
	public Response saveIncidentReportingMapper(IncidentReportingMapperDto incidentReportingMapperDto)
			throws ApplicationException {
		try {
			if (incidentReportingMapperDto.getIncidentReportingMapperId() == null) {

				LocalTime time1 = LocalTime.parse(incidentReportingMapperDto.getIncidentReportingTime());
				LocalDate today = LocalDate.now();
				Instant incidentReportingTime = time1.atDate(today).atZone(ZoneId.of("UTC")).toInstant();


				String incidentNo = iGlobalUnitMasterDao.getGeneratedNo(0, incidentReportingMapperDto.getUnitId(),
						INCIDENT_NO);

				IncidentReportingMapper incidentReportingMapper = new IncidentReportingMapper();
				incidentReportingMapper.setIncidentCategoryId(incidentReportingMapperDto.getIncidentCategoryId());
				incidentReportingMapper.setIncidentReportId(incidentReportingMapperDto.getIncidentReportId());
				incidentReportingMapper.setIncidentRemark(incidentReportingMapperDto.getIncidentRemark());
				incidentReportingMapper.setIncidentTypeId(incidentReportingMapperDto.getIncidentTypeId());
				incidentReportingMapper.setIncidentReportingDate(GlobalCommonDateUtils
						.getDate(incidentReportingMapperDto.getIncidentReportingDate(), "dd-M-yyyy HH:mm:ss"));
				incidentReportingMapper.setReportingTemplateId(incidentReportingMapperDto.getReportingTemplateId());
				incidentReportingMapper.setReportingTemplateObj(incidentReportingMapperDto.getReportingTemplateObj());
				incidentReportingMapper.setRoleId(incidentReportingMapperDto.getRoleId());
				incidentReportingMapper.setUserId(incidentReportingMapperDto.getUserId());
				incidentReportingMapper.setIncidentReportingTime(incidentReportingTime);
				incidentReportingMapper.setPatientId(incidentReportingMapperDto.getPatientId());
				incidentReportingMapper.setSpecialityId(incidentReportingMapperDto.getSpecialityId());
				incidentReportingMapper.setIncidentReportingNumber(incidentNo);
				incidentReportingMapper.setOrganizationId(incidentReportingMapperDto.getOrganizationId());
				incidentReportingMapper.setUnitId(incidentReportingMapperDto.getUnitId());
				incidentReportingMapper.setStatus(incidentReportingMapperDto.getStatus());
				incidentReportingMapper.setCreatedBy(incidentReportingMapperDto.getCreatedBy());
				incidentReportingMapper.setCreatedDate(GlobalCommonDateUtils
						.getDate(incidentReportingMapperDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
				incidentReportingMapper.setUpdatedBy(incidentReportingMapperDto.getUpdatedBy());
				incidentReportingMapper.setUpdatedDate(GlobalCommonDateUtils
						.getDate(incidentReportingMapperDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
				incidentReportingMapper.setConversationId(incidentReportingMapperDto.getConversationId());
				incidentReportingMapper.setEncounterId(incidentReportingMapperDto.getEncounterId());
				incidentReportingMapper.setAdmissionId(incidentReportingMapperDto.getAdmissionId());
				save(incidentReportingMapper);
				Integer incidentReportingMapperId = incidentReportingMapper.getIncidentReportingMapperId();
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, incidentReportingMapperId);
			} else {
				String timeString = incidentReportingMapperDto.getIncidentReportingTime();

				LocalTime time1 = LocalTime.parse(timeString);
				LocalDate today = LocalDate.now();
				Instant incidentReportingTime = time1.atDate(today).atZone(ZoneId.of("UTC")).toInstant();

//				String incidentNo = iGlobalUnitMasterDao.getGeneratedNo(0, incidentReportingMapperDto.getUnitId(),
//						INCIDENT_NO);

				IncidentReportingMapper incidentReportingMapper = findById(
						incidentReportingMapperDto.getIncidentReportingMapperId());
				incidentReportingMapper.setIncidentCategoryId(incidentReportingMapperDto.getIncidentCategoryId());
				incidentReportingMapper.setIncidentReportId(incidentReportingMapperDto.getIncidentReportId());
				incidentReportingMapper.setIncidentRemark(incidentReportingMapperDto.getIncidentRemark());
				incidentReportingMapper.setIncidentTypeId(incidentReportingMapperDto.getIncidentTypeId());
				incidentReportingMapper.setIncidentReportingDate(GlobalCommonDateUtils
						.getDate(incidentReportingMapperDto.getIncidentReportingDate(), "dd-M-yyyy HH:mm:ss"));
				incidentReportingMapper.setReportingTemplateId(incidentReportingMapperDto.getReportingTemplateId());
				incidentReportingMapper.setReportingTemplateObj(incidentReportingMapperDto.getReportingTemplateObj());
				incidentReportingMapper.setRoleId(incidentReportingMapperDto.getRoleId());
				incidentReportingMapper.setUserId(incidentReportingMapperDto.getUserId());
				incidentReportingMapper.setIncidentReportingTime(incidentReportingTime);
				incidentReportingMapper.setPatientId(incidentReportingMapperDto.getPatientId());
				incidentReportingMapper.setSpecialityId(incidentReportingMapperDto.getSpecialityId());
//				incidentReportingMapper.setIncidentReportingNumber(incidentNo);
				incidentReportingMapper.setOrganizationId(incidentReportingMapperDto.getOrganizationId());
				incidentReportingMapper.setUnitId(incidentReportingMapperDto.getUnitId());
				incidentReportingMapper.setStatus(incidentReportingMapperDto.getStatus());
				incidentReportingMapper.setCreatedBy(incidentReportingMapperDto.getCreatedBy());
				incidentReportingMapper.setCreatedDate(GlobalCommonDateUtils.getDate(incidentReportingMapperDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
				incidentReportingMapper.setUpdatedBy(incidentReportingMapperDto.getUpdatedBy());
				incidentReportingMapper.setUpdatedDate(GlobalCommonDateUtils
						.getDate(incidentReportingMapperDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
				incidentReportingMapper.setConversationId(incidentReportingMapperDto.getConversationId());
				incidentReportingMapper.setEncounterId(incidentReportingMapperDto.getEncounterId());
				incidentReportingMapper.setAdmissionId(incidentReportingMapperDto.getAdmissionId());
				update(incidentReportingMapper);
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, incidentReportingMapperDto.getIncidentReportingMapperId());
			}

		} catch (Exception e) {
		logger.error("saveIncidentReportingMapper failed", e);
			String errorMessage = IncidentReportingMapperDaoImpl.class.getName()+": "+e.getMessage();
			return new Response<>(ERROR, ERROR_CODE, errorMessage, null, null);
		}
	}

	@Override
	public Response updateIncidentReportingMapper(IncidentReportingMapperDto incidentReportingMapperDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response getIncidentReportingMapperById(IncidentReportingMapperDto incidentReportingMapperDto)
			throws ApplicationException {
		try {
			List<IncidentReportingMapperDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_INCIDENT_REPORTING_MAPPER_BY_ID")
					.setParameter("organizationId", incidentReportingMapperDto.getOrganizationId())
					.setParameter("unitId", incidentReportingMapperDto.getUnitId())
					.setParameter("incidentReportingMapperId", incidentReportingMapperDto.getIncidentReportingMapperId())
					.setResultTransformer(Transformers.aliasToBean(IncidentReportingMapperDto.class)).list();

			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

		} catch (HibernateException he) {
		logger.error("getIncidentReportingMapperById failed", he);
		} catch (Exception e) {
		logger.error("getIncidentReportingMapperById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getIncidentReportingMapperList(IncidentReportingMapperDto incidentReportingMapperDto)
			throws ApplicationException {
		try {
			List<IncidentReportingMapperDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_INCIDENT_REPORTING_MAPPER_LIST")
					.setParameter("organizationId", incidentReportingMapperDto.getOrganizationId())
					.setParameter("unitId", incidentReportingMapperDto.getUnitId())
					.setParameter("fromDate",incidentReportingMapperDto.getFromDate())
					.setParameter("toDate", incidentReportingMapperDto.getToDate())
					.setParameter("specialityId", incidentReportingMapperDto.getSpecialityId())
					.setParameter("incidentTypeId", incidentReportingMapperDto.getIncidentTypeId())
					.setParameter("userId", incidentReportingMapperDto.getUserId())
					.setFirstResult(
							incidentReportingMapperDto.getOffset() != null ? incidentReportingMapperDto.getOffset() : 0)
					.setMaxResults(incidentReportingMapperDto.getNoOfRecordsPerPage() != null
							? incidentReportingMapperDto.getNoOfRecordsPerPage()
							: 10)
					.setResultTransformer(Transformers.aliasToBean(IncidentReportingMapperDto.class)).list();

			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

		} catch (HibernateException he) {
		logger.error("getIncidentReportingMapperList failed", he);
		} catch (Exception e) {
		logger.error("getIncidentReportingMapperList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateIncidentReportingMapperStatus(IncidentReportingMapperDto incidentReportingMapperDto)
			throws ApplicationException {
		try {
			IncidentReportingMapper incidentReportingMapper = findById(
					incidentReportingMapperDto.getIncidentReportingMapperId());

			incidentReportingMapper.setStatus(incidentReportingMapperDto.getStatus());
			incidentReportingMapper.setUpdatedBy(incidentReportingMapperDto.getUpdatedBy());
			incidentReportingMapper.setUpdatedDate(
					GlobalCommonDateUtils.getDate(incidentReportingMapperDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));

			update(incidentReportingMapper);

			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);

		} catch (HibernateException he) {
		logger.error("updateIncidentReportingMapperStatus failed", he);
		} catch (Exception e) {
		logger.error("updateIncidentReportingMapperStatus failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_UPDATE, null, null);
	}

	@Override
	public Response getActiveIncidentReportingMapperList(IncidentReportingMapperDto incidentReportingMapperDto)
			throws ApplicationException {
		try {
			List<IncidentReportingMapperDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_INCIDENT_REPORTING_MAPPER_LIST")
					.setParameter("organizationId", incidentReportingMapperDto.getOrganizationId())
					.setParameter("unitId", incidentReportingMapperDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(IncidentReportingMapperDto.class)).list();

			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

		} catch (HibernateException he) {
		logger.error("getActiveIncidentReportingMapperList failed", he);
		} catch (Exception e) {
		logger.error("getActiveIncidentReportingMapperList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getIncidentReportingMapperCount(IncidentReportingMapperDto incidentReportingMapperDto)
			throws ApplicationException {
		try {

			Session session = sessionFactory.getCurrentSession();
			Criteria crit = session.createCriteria(IncidentReportingMapper.class);
			crit.add(Restrictions.eq("organizationId", incidentReportingMapperDto.getOrganizationId()));
			crit.add(Restrictions.eq("unitId", incidentReportingMapperDto.getUnitId()));
			crit.setProjection(Projections.rowCount());
			Long count = (Long) crit.uniqueResult();

			return new Response(SUCCESS, SUCCESS_CODE, null, null, count);

		} catch (HibernateException he) {
		logger.error("getIncidentReportingMapperCount failed", he);
		} catch (Exception e) {
		logger.error("getIncidentReportingMapperCount failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getRoleWiseUserList(IncidentReportingMapperDto incidentReportingMapperDto)
			throws ApplicationException {
		try {
			List<IncidentReportingMapperDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ROLE_WISE_USER_LIST")
					.setParameter("organizationId", incidentReportingMapperDto.getOrganizationId())
					.setParameter("unitId", incidentReportingMapperDto.getUnitId())
					.setParameterList("roleIdList", incidentReportingMapperDto.getRoleIdList())
					.setResultTransformer(Transformers.aliasToBean(IncidentReportingMapperDto.class)).list();

			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

		} catch (HibernateException he) {
		logger.error("getRoleWiseUserList failed", he);
		} catch (Exception e) {
		logger.error("getRoleWiseUserList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


}
