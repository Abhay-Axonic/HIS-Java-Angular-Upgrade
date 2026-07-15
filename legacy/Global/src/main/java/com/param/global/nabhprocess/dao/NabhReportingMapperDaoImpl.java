package com.param.global.nabhprocess.dao;

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
import com.param.global.model.IncidentReportingMapper;
import com.param.global.model.NabhReportingMapper;
import com.param.global.unit.dto.IncidentReportingMapperDto;
import com.param.global.unit.dto.NabhReportingMapperDto;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({ "rawtypes", "unchecked" })
@Repository
public class NabhReportingMapperDaoImpl extends GenericDao<NabhReportingMapper, Integer> implements INabhReportingMapperDao, ICommonConstants, IBillingCommonConstants{
	final static Logger logger = Logger.getLogger(NabhReportingMapperDaoImpl.class);

	public NabhReportingMapperDaoImpl() {
		super(NabhReportingMapper.class);
	}

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	IGlobalUnitMasterDao iGlobalUnitMasterDao;

	@Override
	public Response saveNabhReportingMapper(NabhReportingMapperDto nabhReportingMapperDto)
			throws ApplicationException {
		try {
			if (nabhReportingMapperDto.getNabhReportingMapperId() == null) {

				LocalTime time1 = LocalTime.parse(nabhReportingMapperDto.getNabhReportingTime());
				LocalDate today = LocalDate.now();
				Instant nabhReportingTime = time1.atDate(today).atZone(ZoneId.of("UTC")).toInstant();



				String nabhNo = iGlobalUnitMasterDao.getGeneratedNo(0, nabhReportingMapperDto.getUnitId(),
						NABH_NUMBER);

                NabhReportingMapper nabhReportingMapper = new NabhReportingMapper();
                nabhReportingMapper.setIncidentCategoryId(nabhReportingMapperDto.getIncidentCategoryId());
                nabhReportingMapper.setNabhProcessId(nabhReportingMapperDto.getNabhProcessId());
                nabhReportingMapper.setNabhRemark(nabhReportingMapperDto.getNabhRemark());
                nabhReportingMapper.setNabhTypeId(nabhReportingMapperDto.getNabhTypeId());
                nabhReportingMapper.setNabhReportingDate(GlobalCommonDateUtils
                        .getDate(nabhReportingMapperDto.getNabhReportingDate(), "dd-M-yyyy HH:mm:ss"));
                nabhReportingMapper.setReportingTemplateId(nabhReportingMapperDto.getReportingTemplateId());
                nabhReportingMapper.setReportingTemplateObj(nabhReportingMapperDto.getReportingTemplateObj());
                nabhReportingMapper.setRoleId(nabhReportingMapperDto.getRoleId());
                nabhReportingMapper.setUserId(nabhReportingMapperDto.getUserId());
                nabhReportingMapper.setNabhReportingTime(nabhReportingTime);
                nabhReportingMapper.setPatientId(nabhReportingMapperDto.getPatientId());
                nabhReportingMapper.setSpecialityId(nabhReportingMapperDto.getSpecialityId());
                nabhReportingMapper.setNabhReportingNumber(nabhNo);
                nabhReportingMapper.setOrganizationId(nabhReportingMapperDto.getOrganizationId());
                nabhReportingMapper.setUnitId(nabhReportingMapperDto.getUnitId());
                nabhReportingMapper.setStatus(nabhReportingMapperDto.getStatus());
                nabhReportingMapper.setCreatedBy(nabhReportingMapperDto.getCreatedBy());
                nabhReportingMapper.setCreatedDate(GlobalCommonDateUtils
                        .getDate(nabhReportingMapperDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
                nabhReportingMapper.setUpdatedBy(nabhReportingMapperDto.getUpdatedBy());
                nabhReportingMapper.setUpdatedDate(GlobalCommonDateUtils
                        .getDate(nabhReportingMapperDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
                nabhReportingMapper.setConversationId(nabhReportingMapperDto.getConversationId());
                nabhReportingMapper.setEncounterId(nabhReportingMapperDto.getEncounterId());
                nabhReportingMapper.setAdmissionId(nabhReportingMapperDto.getAdmissionId());
                save(nabhReportingMapper);
                Integer nabhReportingMapperId = nabhReportingMapper.getNabhReportingMapperId();
                return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, nabhReportingMapperId);
			} 
			else {
				String timeString = nabhReportingMapperDto.getNabhReportingTime();
				LocalTime time1 = LocalTime.parse(timeString);
				LocalDate today = LocalDate.now();
				Instant nabhReportingTime = time1.atDate(today).atZone(ZoneId.of("UTC")).toInstant();
//				String nabhNo = iGlobalUnitMasterDao.getGeneratedNo(0, nabhReportingMapperDto.getUnitId(),
//						NABH_NUMBER);


				NabhReportingMapper nabhReportingMapper = findById(
						nabhReportingMapperDto.getNabhReportingMapperId());
				nabhReportingMapper.setIncidentCategoryId(nabhReportingMapperDto.getIncidentCategoryId());
				nabhReportingMapper.setNabhProcessId(nabhReportingMapperDto.getNabhProcessId());
				nabhReportingMapper.setNabhRemark(nabhReportingMapperDto.getNabhRemark());
				nabhReportingMapper.setNabhTypeId(nabhReportingMapperDto.getNabhTypeId());
				nabhReportingMapper.setNabhReportingDate(GlobalCommonDateUtils
						.getDate(nabhReportingMapperDto.getNabhReportingDate(), "dd-M-yyyy HH:mm:ss"));
				nabhReportingMapper.setReportingTemplateId(nabhReportingMapperDto.getReportingTemplateId());
				nabhReportingMapper.setReportingTemplateObj(nabhReportingMapperDto.getReportingTemplateObj());
				nabhReportingMapper.setRoleId(nabhReportingMapperDto.getRoleId());
				nabhReportingMapper.setUserId(nabhReportingMapperDto.getUserId());
				nabhReportingMapper.setNabhReportingTime(nabhReportingTime);
				nabhReportingMapper.setPatientId(nabhReportingMapperDto.getPatientId());
				nabhReportingMapper.setSpecialityId(nabhReportingMapperDto.getSpecialityId());
				//nabhReportingMapper.setNabhReportingNumber(nabhNo);
				nabhReportingMapper.setOrganizationId(nabhReportingMapperDto.getOrganizationId());
				nabhReportingMapper.setUnitId(nabhReportingMapperDto.getUnitId());
				nabhReportingMapper.setStatus(nabhReportingMapperDto.getStatus());
				nabhReportingMapper.setCreatedBy(nabhReportingMapperDto.getCreatedBy());
				nabhReportingMapper.setCreatedDate(GlobalCommonDateUtils.getDate(nabhReportingMapperDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
				nabhReportingMapper.setUpdatedBy(nabhReportingMapperDto.getUpdatedBy());
				nabhReportingMapper.setUpdatedDate(GlobalCommonDateUtils
						.getDate(nabhReportingMapperDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
				nabhReportingMapper.setConversationId(nabhReportingMapperDto.getConversationId());
				nabhReportingMapper.setEncounterId(nabhReportingMapperDto.getEncounterId());
				nabhReportingMapper.setAdmissionId(nabhReportingMapperDto.getAdmissionId());
				update(nabhReportingMapper);
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, nabhReportingMapperDto.getNabhReportingMapperId());
			}

		} 
		catch (Exception e) {
		logger.error("saveNabhReportingMapper failed", e);
			String errorMessage = NabhReportingMapperDaoImpl.class.getName()+": "+e.getMessage();
			return new Response<>(ERROR, ERROR_CODE, errorMessage, null, null);
		}

	}
	
	@Override
	public Response getNabhReportingMapperList(NabhReportingMapperDto nabhReportingMapperDto)
			throws ApplicationException {
		try {
			List<NabhReportingMapperDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_NABH_REPORTING_MAPPER_LIST")
					.setParameter("organizationId", nabhReportingMapperDto.getOrganizationId())
					.setParameter("unitId", nabhReportingMapperDto.getUnitId())
					.setParameter("fromDate",nabhReportingMapperDto.getFromDate())
					.setParameter("toDate", nabhReportingMapperDto.getToDate())
					.setParameter("specialityId", nabhReportingMapperDto.getSpecialityId())
					.setParameter("nabhTypeId", nabhReportingMapperDto.getNabhTypeId())
					.setParameter("userId", nabhReportingMapperDto.getUserId())

					.setResultTransformer(Transformers.aliasToBean(NabhReportingMapperDto.class)).list();

			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

		} catch (HibernateException he) {
		logger.error("getNabhReportingMapperList failed", he);
			String errorMessage = NabhReportingMapperDaoImpl.class.getName()+": "+he.getMessage();
			return new Response<>(ERROR, ERROR_CODE, errorMessage, null, null);
		}
	}
	@Override
	public Response getNabhReportingMapperById(NabhReportingMapperDto nabhReportingMapperDto)
			throws ApplicationException {
		try {
			if(nabhReportingMapperDto.getIsDepartmentflag() == false) 
			{
			
			List<NabhReportingMapperDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_NABH_REPORTING_MAPPER_BY_ID")                                    
					.setParameter("organizationId", nabhReportingMapperDto.getOrganizationId())
					.setParameter("unitId", nabhReportingMapperDto.getUnitId())
					.setParameter("nabhReportingMapperId", nabhReportingMapperDto.getNabhReportingMapperId())
					.setResultTransformer(Transformers.aliasToBean(NabhReportingMapperDto.class)).list();

			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
			
			}
			else
			{
				List<NabhReportingMapperDto> list = sessionFactory.getCurrentSession()
						.getNamedQuery("GET_NABH_REPORTING_MAPPER_BY_ID_FOR_DEPRTMENT")                                    
						.setParameter("organizationId", nabhReportingMapperDto.getOrganizationId())
						.setParameter("unitId", nabhReportingMapperDto.getUnitId())
						.setParameter("nabhReportingMapperId", nabhReportingMapperDto.getNabhReportingMapperId())
						.setResultTransformer(Transformers.aliasToBean(NabhReportingMapperDto.class)).list();

				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
			}

		} catch (HibernateException he) {
		logger.error("getNabhReportingMapperById failed", he);
			String errorMessage = NabhReportingMapperDaoImpl.class.getName()+": "+he.getMessage();
			return new Response<>(ERROR, ERROR_CODE, errorMessage, null, null);
		}
	}
}
