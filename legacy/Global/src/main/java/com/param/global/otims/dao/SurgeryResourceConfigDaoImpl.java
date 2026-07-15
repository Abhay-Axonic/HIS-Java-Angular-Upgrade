package com.param.global.otims.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import org.dozer.DozerBeanMapper;
import org.hibernate.*;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.model.UnitServiceMapper;
import com.param.global.model.UnitServiceMasterDetails;
import com.param.global.otims.dto.ResourceBillOfServicesMapperDto;
import com.param.global.otims.dto.SurgeryResourceConfigByCritertiaReqDto;
import com.param.global.otims.dto.SurgeryResourceConfigDto;
import com.param.otims.model.ResourceBillOfServicesMapper;
import com.param.otims.model.SurgeryResourceConfig;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
import scala.Array;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class SurgeryResourceConfigDaoImpl extends GenericDao<SurgeryResourceConfig, Integer>
		implements ISurgeryResourceConfigDao, ICommonConstants {
	final static Logger logger = Logger.getLogger(SurgeryResourceConfigDaoImpl.class);


	public SurgeryResourceConfigDaoImpl() {
		super(SurgeryResourceConfig.class);
	}

	@Autowired
	SessionFactory sessionFactory;
	@Autowired
	private DozerBeanMapper mapper;

	@Autowired
	private ServletContext servletContext;


	@Override
	public Response saveSurgeryResourceConfig(SurgeryResourceConfigDto surgeryResourceConfigDto)
			throws ApplicationException {
		try {			
			SurgeryResourceConfig surgeryResourceConfig = mapper.map(surgeryResourceConfigDto, SurgeryResourceConfig.class,
					"SurgeryResourceConfigDto_to_SurgeryResourceConfig");
			surgeryResourceConfig.setStatus('A');
			surgeryResourceConfig = save(surgeryResourceConfig);
			surgeryResourceConfigDto.setSurgeryResourceConfigId(surgeryResourceConfig.getSurgeryResourceConfigId());
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, surgeryResourceConfigDto);
		} catch (HibernateException he) {
		logger.error("saveSurgeryResourceConfig failed", he);
		} catch (Exception e) {
		logger.error("saveSurgeryResourceConfig failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateSurgeryResourceConfig(SurgeryResourceConfigDto surgeryResourceConfigDto)
			throws ApplicationException {
		try {
			SurgeryResourceConfig surgeryResourceConfig=findById(surgeryResourceConfigDto.getSurgeryResourceConfigId());
			surgeryResourceConfig.setAnesthesiologistPercentage(surgeryResourceConfigDto.getAnesthesiologistPercentage());
			surgeryResourceConfig.setAsstSurgeonPercentage(surgeryResourceConfigDto.getAsstSurgeonPercentage());
			surgeryResourceConfig.setBedCategoryId(surgeryResourceConfigDto.getBedCategoryId());
			surgeryResourceConfig.setDoctorGradeId(surgeryResourceConfigDto.getDoctorGradeId());
			surgeryResourceConfig.setOrgId(surgeryResourceConfigDto.getOrgId());
			surgeryResourceConfig.setOtPercentage(surgeryResourceConfigDto.getOtPercentage());
			surgeryResourceConfig.setPatientCategoryId(surgeryResourceConfigDto.getPatientCategoryId());
			surgeryResourceConfig.setPaymentEntitlementId(surgeryResourceConfigDto.getPaymentEntitlementId());
			surgeryResourceConfig.setSpecialityId(surgeryResourceConfigDto.getSpecialityId());
			surgeryResourceConfig.setSurgeonPercentage(surgeryResourceConfigDto.getSurgeonPercentage());
			surgeryResourceConfig.setSurgeryGradeId(surgeryResourceConfigDto.getSurgeryGradeId());
			surgeryResourceConfig.setSurgeonDependentOn(surgeryResourceConfigDto.getSurgeonDependentOn());
			surgeryResourceConfig.setAsstSurgeonDependentOn(surgeryResourceConfigDto.getAsstSurgeonDependentOn());
			surgeryResourceConfig.setAnesthesiologistDependentOn(surgeryResourceConfigDto.getAnesthesiologistDependentOn());
			surgeryResourceConfig.setOtDependentOn(surgeryResourceConfigDto.getOtDependentOn());
			surgeryResourceConfig.setUnitId(surgeryResourceConfigDto.getUnitId());
			surgeryResourceConfig.setUpdatedBy(surgeryResourceConfigDto.getUpdatedBy());
			surgeryResourceConfig.setTariffId(surgeryResourceConfigDto.getTariffId());
			surgeryResourceConfig.setVisitTypeId(surgeryResourceConfigDto.getVisitTypeId());
			surgeryResourceConfig.setProcedureName(surgeryResourceConfigDto.getProcedureName());
			surgeryResourceConfig.setServiceId(surgeryResourceConfigDto.getServiceId());
			surgeryResourceConfig.setUpdatedDate(GlobalCommonDateUtils.getDate(surgeryResourceConfigDto.getUpdatedDate(), "dd-MM-yyyy HH:mm:ss"));
			update(surgeryResourceConfig);
			
			String query = " UPDATE otims.m_resource_bill_of_services_mapper SET status='I'"
			+" WHERE surgery_resource_config_id =:surgeryResourceConfigId "  ;
	        Query sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query);
			sqlQuery.setParameter("surgeryResourceConfigId",surgeryResourceConfigDto.getSurgeryResourceConfigId());
	        sqlQuery.executeUpdate();
			
			for (ResourceBillOfServicesMapperDto dto : surgeryResourceConfigDto.getListSelectedResourceBillOfServicesMapperDto()) {
				dto.setSurgeryResourceConfigId(surgeryResourceConfigDto.getSurgeryResourceConfigId());
				dto.setStatus('A');
				dto.setCreatedBy(surgeryResourceConfigDto.getCreatedBy());
				dto.setUpdatedBy(surgeryResourceConfigDto.getUpdatedBy());
				dto.setOrganizationId(surgeryResourceConfigDto.getOrgId());
				dto.setUnitId(surgeryResourceConfigDto.getUnitId());
				ResourceBillOfServicesMapper resourceBillOfServicesMapper = mapper.map(dto , ResourceBillOfServicesMapper.class,
						"ResourceBillOfServicesMapperDto_to_ResourceBillOfServicesMapper");
				sessionFactory.getCurrentSession().save(resourceBillOfServicesMapper);
			}
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (HibernateException he) {
		logger.error("updateSurgeryResourceConfig failed", he);
		} catch (Exception e) {
		logger.error("updateSurgeryResourceConfig failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getSurgeryResourceConfigList(SurgeryResourceConfigDto surgeryResourceConfigDto)
			throws ApplicationException {
		try {
			
			List<SurgeryResourceConfigDto> surgeryResourceConfigList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SURGERY_RESOURCE_CONFIG_LIST")
					.setParameter("orgId", surgeryResourceConfigDto.getOrgId())
					.setParameter("unitId", surgeryResourceConfigDto.getUnitId())
					.setFirstResult(surgeryResourceConfigDto.getOffset() != null ? surgeryResourceConfigDto.getOffset() : 0)
					.setMaxResults(surgeryResourceConfigDto.getNoOfRecordsPerPage() != null ? surgeryResourceConfigDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(SurgeryResourceConfigDto.class)).list();
			for (SurgeryResourceConfigDto surgeryResourceConfigDto2 : surgeryResourceConfigList) {
				List<ResourceBillOfServicesMapperDto> surgeryResourceConfigList1 = sessionFactory.getCurrentSession()
						.getNamedQuery("GET_RESOURCE_BILL_OF_SERVICE_MAPPER")
						.setParameter("surgeryResourceConfigId", surgeryResourceConfigDto2.getSurgeryResourceConfigId())
						.setResultTransformer(Transformers.aliasToBean(ResourceBillOfServicesMapperDto.class)).list();
				surgeryResourceConfigDto2.setListSelectedResourceBillOfServicesMapperDto(surgeryResourceConfigList1);
			}
			
			
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, surgeryResourceConfigList, null);
		} catch (HibernateException he) {
		logger.error("getSurgeryResourceConfigList failed", he);
		} catch (Exception e) {
		logger.error("getSurgeryResourceConfigList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getSurgeryResourceConfigListById(Integer surgeryResourceConfigId) throws ApplicationException {
		try {
			List<SurgeryResourceConfigDto> surgeryResourceConfigList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SURGERY_RESOURCE_CONFIG_LIST_BY_ID")
					.setParameter("surgeryResourceConfigId", surgeryResourceConfigId)
					.setResultTransformer(Transformers.aliasToBean(SurgeryResourceConfigDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, surgeryResourceConfigList, null);
		} catch (HibernateException he) {
		logger.error("getSurgeryResourceConfigListById failed", he);
		} catch (Exception e) {
		logger.error("getSurgeryResourceConfigListById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getSurgeryResourceConfigListCount(SurgeryResourceConfigDto surgeryResourceConfigDto)
			throws ApplicationException {
		try {
			Session session=sessionFactory.getCurrentSession();
			Criteria c=session.createCriteria(SurgeryResourceConfig.class);
			c.add(Restrictions.eq("unitId",surgeryResourceConfigDto.getUnitId()));
			c.add(Restrictions.eq("orgId",surgeryResourceConfigDto.getOrgId()));
			c.setProjection(Projections.rowCount());  
			Long count = (Long)c.uniqueResult();
			return new Response(SUCCESS, null, null, null, count);
		} catch (HibernateException he) {
		logger.error("getSurgeryResourceConfigListCount failed", he);
		} catch (Exception e) {
		logger.error("getSurgeryResourceConfigListCount failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateSurgeryResourceConfigStatus(SurgeryResourceConfigDto surgeryResourceConfigDto)
			throws ApplicationException {
		try {
			SurgeryResourceConfig surgeryResourceConfig=findById(surgeryResourceConfigDto.getSurgeryResourceConfigId());
			
			surgeryResourceConfig.setStatus(surgeryResourceConfigDto.getStatus()==ACTIVE ? ACTIVE:INACTIVE);	
			surgeryResourceConfig.setUpdatedBy(surgeryResourceConfigDto.getUpdatedBy());
			surgeryResourceConfig.setUpdatedDate(GlobalCommonDateUtils.getDate(surgeryResourceConfigDto.getUpdatedDate(), "dd-MM-yyyy HH:mm:ss"));
			update(surgeryResourceConfig);
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (HibernateException he) {
		logger.error("updateSurgeryResourceConfigStatus failed", he);
		} catch (Exception e) {
		logger.error("updateSurgeryResourceConfigStatus failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response checkAlreadyExistOrNot(SurgeryResourceConfigDto surgeryResourceConfigDto)
			throws ApplicationException {
		try {
			List<SurgeryResourceConfigDto> surgeryResourceConfigList = sessionFactory.getCurrentSession()
					.getNamedQuery("CHECK_ALREADY_EXIST_OR_NOT")
					.setParameter("serviceId", surgeryResourceConfigDto.getServiceId())
				  	.setParameter("tariffId", surgeryResourceConfigDto.getTariffId())
					.setParameter("visitTypeId", surgeryResourceConfigDto.getVisitTypeId())
					.setParameter("paymentEntitleId", surgeryResourceConfigDto.getPaymentEntitlementId())
					.setParameter("bedCatId", surgeryResourceConfigDto.getBedCategoryId())
					.setParameter("patientCategoryId", surgeryResourceConfigDto.getPatientCategoryId())
					.setParameter("orgId", surgeryResourceConfigDto.getOrgId())
					.setParameter("unitId", surgeryResourceConfigDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(SurgeryResourceConfigDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, surgeryResourceConfigList, null);
		} catch (HibernateException he) {
		logger.error("checkAlreadyExistOrNot failed", he);
		} catch (Exception e) {
		logger.error("checkAlreadyExistOrNot failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getConfigForProcPostingByCriteria(SurgeryResourceConfigByCritertiaReqDto reqDto)throws ApplicationException {
		try {
			SurgeryResourceConfigDto dto = (SurgeryResourceConfigDto)sessionFactory.getCurrentSession().getNamedQuery("GET_SURGERY_RES_CONFIG_FOR_PROCEDURE_POSTING_BY_CRITERIA")
						.setParameter("specialityId", reqDto.getSpecialityId()).setParameter("surgeryGradeId", reqDto.getSurgeryGradeId())
						.setParameter("doctorGradeId", reqDto.getDoctorGradeId()).setParameter("patientCategoryId", reqDto.getPatientCategoryId())
						.setParameter("paymentEntitlementId", reqDto.getPaymentEntitlementId()).setParameter("bedCategoryId", reqDto.getBedCategoryId())
						.setParameter("orgId", reqDto.getOrgId()).setParameter("unitId", reqDto.getUnitId())
						.setResultTransformer(Transformers.aliasToBean(SurgeryResourceConfigDto.class)).uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, dto);
			
		} catch (Exception e) {
			
			throw e;
		}
	}


	

	@Override
	public Response mapperResorceSave(SurgeryResourceConfigDto surgeryResourceConfigDto) throws ApplicationException {
		try {			
			
			for (ResourceBillOfServicesMapperDto dto : surgeryResourceConfigDto.getListSelectedResourceBillOfServicesMapperDto()) {
				dto.setSurgeryResourceConfigId(surgeryResourceConfigDto.getSurgeryResourceConfigId());
				dto.setStatus('A');
				dto.setCreatedBy(surgeryResourceConfigDto.getCreatedBy());
				dto.setUpdatedBy(surgeryResourceConfigDto.getUpdatedBy());
				dto.setOrganizationId(surgeryResourceConfigDto.getOrgId());
				dto.setUnitId(surgeryResourceConfigDto.getUnitId());
				ResourceBillOfServicesMapper resourceBillOfServicesMapper = mapper.map(dto , ResourceBillOfServicesMapper.class,
						"ResourceBillOfServicesMapperDto_to_ResourceBillOfServicesMapper");
				sessionFactory.getCurrentSession().save(resourceBillOfServicesMapper);
			}
		
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (HibernateException he) {
		logger.error("mapperResorceSave failed", he);
		} catch (Exception e) {
		logger.error("mapperResorceSave failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getSurgeryResourceConfigListByProcedureId(SurgeryResourceConfigDto surgeryResourceConfigDto)
			throws ApplicationException {
		try {
			
			List<SurgeryResourceConfigDto> surgeryResourceConfigList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SURGERY_RESOURCE_CONFIG_LIST_BY_PROCEDURE_LIST")
					.setParameter("bedCatId", surgeryResourceConfigDto.getBedCategoryId())
					.setParameter("procedureId", surgeryResourceConfigDto.getServiceId())
					.setParameter("tariffId", surgeryResourceConfigDto.getTariffId())
					.setParameter("paymentEntitlementId", surgeryResourceConfigDto.getPaymentEntitlementId())
					.setParameter("visitTypeId", surgeryResourceConfigDto.getVisitTypeId())
					.setParameter("patientCategoryId", surgeryResourceConfigDto.getPatientCategoryId())
					.setParameter("orgId", surgeryResourceConfigDto.getOrgId())
					.setParameter("unitId", surgeryResourceConfigDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(SurgeryResourceConfigDto.class)).list();
			for (SurgeryResourceConfigDto surgeryResourceConfigDto2 : surgeryResourceConfigList) {
				List<ResourceBillOfServicesMapperDto> surgeryResourceConfigList1 = sessionFactory.getCurrentSession()
						.getNamedQuery("GET_RESOURCE_BILL_OF_SERVICE_MAPPER")
						.setParameter("surgeryResourceConfigId", surgeryResourceConfigDto2.getSurgeryResourceConfigId())
						.setResultTransformer(Transformers.aliasToBean(ResourceBillOfServicesMapperDto.class)).list();
				surgeryResourceConfigDto2.setListSelectedResourceBillOfServicesMapperDto(surgeryResourceConfigList1);
			}
			
			
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, surgeryResourceConfigList, null);
		} catch (HibernateException he) {
		logger.error("getSurgeryResourceConfigListByProcedureId failed", he);
		} catch (Exception e) {
		logger.error("getSurgeryResourceConfigListByProcedureId failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}
