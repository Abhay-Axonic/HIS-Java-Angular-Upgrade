package com.param.global.unit.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.dozer.DozerBeanMapper;
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
import com.param.global.dto.IntakeOutputChartMasterDto;
import com.param.global.model.CabinMaster;
import com.param.global.model.IncidentReportMaster;
import com.param.global.model.IncidentReportRoleMapper;
import com.param.global.model.IncidentReportTemplateMapper;
import com.param.global.unit.dto.CabinMasterDto;
import com.param.global.unit.dto.IncidentReportMasterDto;
import com.param.global.unit.dto.IncidentReportRoleMapperDto;
import com.param.global.unit.dto.IncidentReportTemplateMapperDto;
import com.param.opd.coversheet.model.PrescriptionFrequencyMaster;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({"rawtypes", "unchecked"})
@Repository
public class IncidentReportMasterDaoImpl extends GenericDao<IncidentReportMaster, Integer> implements IIncidentReportMasterDao,ICommonConstants{
	final static Logger logger = Logger.getLogger(IncidentReportMasterDaoImpl.class);


	public IncidentReportMasterDaoImpl() {
		super(IncidentReportMaster.class);
	}

	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	DozerBeanMapper mapper;
	
	@Override
	public Response saveIncidentReportMaster(IncidentReportMasterDto incidentReportMasterDto)
			throws ApplicationException {
		try {
			IncidentReportMaster incidentReportMaster=new IncidentReportMaster();
			incidentReportMaster.setIncidentReportCode(incidentReportMasterDto.getIncidentReportCode());
			incidentReportMaster.setIncidentReportDesc(incidentReportMasterDto.getIncidentReportDesc());
			incidentReportMaster.setOrganizationId(incidentReportMasterDto.getOrganizationId());
			incidentReportMaster.setUnitId(incidentReportMasterDto.getUnitId());
			incidentReportMaster.setStatus(incidentReportMasterDto.getStatus());
			incidentReportMaster.setCreatedBy(incidentReportMasterDto.getCreatedBy());
			incidentReportMaster.setCreatedDate(GlobalCommonDateUtils.getDate(incidentReportMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			incidentReportMaster.setUpdatedBy(incidentReportMasterDto.getUpdatedBy());
			incidentReportMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(incidentReportMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			 
			save(incidentReportMaster);
			incidentReportMasterDto.setIncidentReportId(incidentReportMaster.getIncidentReportId());
		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, incidentReportMasterDto);
		
		} catch (HibernateException he) {
		logger.error("saveIncidentReportMaster failed", he);
		} catch (Exception e) {
		logger.error("saveIncidentReportMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateIncidentReportMaster(IncidentReportMasterDto incidentReportMasterDto)
			throws ApplicationException {
		try {
			IncidentReportMaster incidentReportMaster=findById(incidentReportMasterDto.getIncidentReportId());
			incidentReportMaster.setIncidentReportCode(incidentReportMasterDto.getIncidentReportCode());
			incidentReportMaster.setIncidentReportDesc(incidentReportMasterDto.getIncidentReportDesc());
			incidentReportMaster.setOrganizationId(incidentReportMasterDto.getOrganizationId());
			incidentReportMaster.setUnitId(incidentReportMasterDto.getUnitId());
			incidentReportMaster.setStatus(incidentReportMasterDto.getStatus());
			incidentReportMaster.setUpdatedBy(incidentReportMasterDto.getUpdatedBy());
			incidentReportMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(incidentReportMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			 
			update(incidentReportMaster);
			incidentReportMasterDto.setIncidentReportId(incidentReportMaster.getIncidentReportId());
			
			sessionFactory.getCurrentSession().createQuery("UPDATE IncidentReportRoleMapper "
					+ " SET status='I' "
					+ " WHERE incidentReportId="+incidentReportMasterDto.getIncidentReportId() +" "
					+ " AND organizationId="+incidentReportMasterDto.getOrganizationId() +" "
					+ " AND unitId="+incidentReportMasterDto.getUnitId() +" "
					)
			.executeUpdate();
			
		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, incidentReportMasterDto);
		
		} catch (HibernateException he) {
		logger.error("updateIncidentReportMaster failed", he);
		} catch (Exception e) {
		logger.error("updateIncidentReportMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


@Override
public Response getIncidentReportMasterById(IncidentReportMasterDto incidentReportMasterDto)
throws ApplicationException {
try {
				List<IncidentReportRoleMapperDto> list2=new ArrayList<>();
				
				List<IncidentReportMasterDto> list1=sessionFactory.getCurrentSession()
							.getNamedQuery("GET_INCIDENT_REPORT_MASTER_BY_ID")
							.setParameter("incidentReportId", incidentReportMasterDto.getIncidentReportId())
							.setParameter("organizationId", incidentReportMasterDto.getOrganizationId())
							.setParameter("unitId", incidentReportMasterDto.getUnitId())
							.setFirstResult(incidentReportMasterDto.getOffset() != null ? incidentReportMasterDto.getOffset() : 0)
							.setMaxResults(incidentReportMasterDto.getNoOfRecordsPerPage() != null ? incidentReportMasterDto.getNoOfRecordsPerPage() : 10)
							.setResultTransformer(Transformers.aliasToBean(IncidentReportMasterDto.class)).list();
					if(list1.size() > 0 && list1 !=null) {
					for(IncidentReportMasterDto dto : list1)
					{
							list2=sessionFactory.getCurrentSession()
								.getNamedQuery("GET_INCIDENT_REPORT_MASTER_LIST_FOR_ROLE")
								.setParameter("organizationId", incidentReportMasterDto.getOrganizationId())
								.setParameter("unitId", incidentReportMasterDto.getUnitId())
								.setParameter("incidentReportId", dto.getIncidentReportId())
								.setResultTransformer(Transformers.aliasToBean(IncidentReportRoleMapperDto.class)).list();
							
							dto.setIncidentReportRoleMapperList(list2);
					
					if(list2.size() > 0 && list2 != null) {
					for(IncidentReportRoleMapperDto dto2 : list2)
					{
							List<IncidentReportTemplateMapperDto> list3=sessionFactory.getCurrentSession()
									.getNamedQuery("GET_INCIDENT_REPORT_MASTER_LIST_FOR_TEMPLATE")
									.setParameter("organizationId", incidentReportMasterDto.getOrganizationId())
									.setParameter("unitId", incidentReportMasterDto.getUnitId())
									.setParameter("incidentReportId", dto2.getIncidentReportId())
									.setParameter("roleId", dto2.getRoleId())
									.setResultTransformer(Transformers.aliasToBean(IncidentReportTemplateMapperDto.class)).list();
					
					         dto.setIncidentReportTemplateMapperList(list3);
					}
				}
			}
         }
		return new Response(SUCCESS, SUCCESS_CODE, null, list1, null);
		
		} catch (HibernateException he) {
		logger.error("getIncidentReportMasterById failed", he);
		} catch (Exception e) {
			logger.error("getIncidentReportMasterById failed", e);
			}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

	@Override
	public Response getIncidentReportMasterList(IncidentReportMasterDto incidentReportMasterDto)
			throws ApplicationException {
		try {
			List<IncidentReportRoleMapperDto> list2=new ArrayList<>();

			List<IncidentReportMasterDto> list1=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_INCIDENT_REPORT_MASTER_LIST")
					.setParameter("organizationId",  incidentReportMasterDto.getOrganizationId())
					.setParameter("unitId",  incidentReportMasterDto.getUnitId())
					.setFirstResult(incidentReportMasterDto.getOffset() != null ? incidentReportMasterDto.getOffset()  : 0)
					.setMaxResults(incidentReportMasterDto.getNoOfRecordsPerPage() != null ? incidentReportMasterDto.getNoOfRecordsPerPage()  : 10)
					.setResultTransformer(Transformers.aliasToBean(IncidentReportMasterDto.class)).list();
			if(list1.size() > 0 && list1 !=null) {
				for(IncidentReportMasterDto dto : list1) 
				{			
					list2=sessionFactory.getCurrentSession()
							.getNamedQuery("GET_INCIDENT_REPORT_MASTER_LIST_FOR_ROLE")
							.setParameter("organizationId",  incidentReportMasterDto.getOrganizationId())
							.setParameter("unitId",  incidentReportMasterDto.getUnitId())
							.setParameter("incidentReportId",  dto.getIncidentReportId())
							.setResultTransformer(Transformers.aliasToBean(IncidentReportRoleMapperDto.class)).list();
					
					dto.setIncidentReportRoleMapperList(list2);
//					list1.add(dto);
					if(list2.size() > 0 && list2 != null) {
						for(IncidentReportRoleMapperDto dto2 : list2) 
						{			
							List<IncidentReportTemplateMapperDto> list3=sessionFactory.getCurrentSession()
									.getNamedQuery("GET_INCIDENT_REPORT_MASTER_LIST_FOR_TEMPLATE")
									.setParameter("organizationId",  incidentReportMasterDto.getOrganizationId())
									.setParameter("unitId",  incidentReportMasterDto.getUnitId())
									.setParameter("incidentReportId",  dto2.getIncidentReportId())
									.setParameter("roleId",  dto2.getRoleId())
									.setResultTransformer(Transformers.aliasToBean(IncidentReportTemplateMapperDto.class)).list();
							
							dto.setIncidentReportTemplateMapperList(list3);
//							list2.add(dto2);
							
						}
					}
					
					
				}
			}
			
//			for (IncidentReportRoleMapperDto dtotest : list2) {
				
//			}
			
			
			return new Response(SUCCESS, SUCCESS_CODE, null, list1, null);
			
		
		} catch (HibernateException he) {
		logger.error("getIncidentReportMasterList failed", he);
		} catch (Exception e) {
		logger.error("getIncidentReportMasterList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getTemplateByIncidentReportId(IncidentReportMasterDto incidentReportMasterDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response getRoleByIncidentReportId(IncidentReportMasterDto incidentReportMasterDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response checkIncidentReportMasterAvailableByName(IncidentReportMasterDto incidentReportMasterDto)
	throws ApplicationException {
		try {
				List<IncidentReportRoleMapperDto> list2=new ArrayList<>();
			
				List<IncidentReportMasterDto> list1=sessionFactory.getCurrentSession()
				.getNamedQuery("CHECK_INCIDENT_REPORT_MASTER_AVAILABLE_BY_NAME")
				.setParameter("incidentReportDesc", incidentReportMasterDto.getIncidentReportDesc())
				.setParameter("organizationId", incidentReportMasterDto.getOrganizationId())
				.setParameter("unitId", incidentReportMasterDto.getUnitId())
				.setResultTransformer(Transformers.aliasToBean(IncidentReportMasterDto.class)).list();
			
				return new Response(SUCCESS, SUCCESS_CODE, null, list1, null);
			
			
				} catch (HibernateException he) {
				logger.error("checkIncidentReportMasterAvailableByName failed", he);
				} catch (Exception e) {
				logger.error("checkIncidentReportMasterAvailableByName failed", e);
				}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@Override
	public Response checkIncidentReportMasterAvailableByNameNotId(IncidentReportMasterDto incidentReportMasterDto)
			throws ApplicationException {
		try {
			List<IncidentReportRoleMapperDto> list2=new ArrayList<>();
			List<IncidentReportMasterDto> list1=sessionFactory.getCurrentSession()
					.getNamedQuery("CHECK_INCIDENT_REPORT_MASTER_AVAILABLE_BY_NAME")
					.setParameter("incidentReportDesc", incidentReportMasterDto.getIncidentReportDesc())
					.setInteger("organizationId", incidentReportMasterDto.getOrganizationId())
					.setInteger("unitId", incidentReportMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(IncidentReportMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, list1, null);

		} catch (HibernateException he) {
		logger.error("checkIncidentReportMasterAvailableByNameNotId failed", he);
		} catch (Exception e) {
		logger.error("checkIncidentReportMasterAvailableByNameNotId failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@Override
	public Response saveIncidentReportRoleMapper(IncidentReportRoleMapperDto dtoRoleList) throws ApplicationException {
		try {

			IncidentReportRoleMapper roleMapper = mapper.map(dtoRoleList, IncidentReportRoleMapper.class,
					"IncidentReportRoleMapperDto_to_IncidentReportRoleMapper");
			sessionFactory.getCurrentSession().save(roleMapper);
			dtoRoleList.setRoleId(roleMapper.getRoleId());
			roleMapper.setCreatedDate(GlobalCommonDateUtils.getDate(dtoRoleList.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			roleMapper.setUpdatedDate(GlobalCommonDateUtils.getDate(dtoRoleList.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, dtoRoleList);
		} catch (HibernateException he) {
		logger.error("saveIncidentReportRoleMapper failed", he);
		} catch (Exception e) {
		logger.error("saveIncidentReportRoleMapper failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response saveIncidentReportTemplateMapper(IncidentReportTemplateMapperDto dtoTemplateList)
			throws ApplicationException {
		try {

			IncidentReportTemplateMapper templateMapper = mapper.map(dtoTemplateList, IncidentReportTemplateMapper.class,
					"IncidentReportTemplateMapperDto_to_IncidentReportTemplateMapper");
			sessionFactory.getCurrentSession().save(templateMapper);
			templateMapper.setCreatedDate(GlobalCommonDateUtils.getDate(dtoTemplateList.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			templateMapper.setUpdatedDate(GlobalCommonDateUtils.getDate(dtoTemplateList.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		} catch (HibernateException he) {
		logger.error("saveIncidentReportTemplateMapper failed", he);
		} catch (Exception e) {
		logger.error("saveIncidentReportTemplateMapper failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response updateIncidentReportRoleMapper(IncidentReportRoleMapperDto dtoRoleList)
			throws ApplicationException {
		try {

			IncidentReportRoleMapper roleMapper = mapper.map(dtoRoleList, IncidentReportRoleMapper.class,
					"IncidentReportRoleMapperDto_to_IncidentReportRoleMapper");
			sessionFactory.getCurrentSession().save(roleMapper);
			dtoRoleList.setRoleId(roleMapper.getRoleId());
			roleMapper.setUpdatedDate(GlobalCommonDateUtils.getDate(dtoRoleList.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, dtoRoleList);
		} catch (HibernateException he) {
		logger.error("updateIncidentReportRoleMapper failed", he);
		} catch (Exception e) {
		logger.error("updateIncidentReportRoleMapper failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response updateIncidentReportTemplateMapper(IncidentReportTemplateMapperDto dtoTemplateList)
			throws ApplicationException {
		try {

			IncidentReportTemplateMapper templateMapper = mapper.map(dtoTemplateList, IncidentReportTemplateMapper.class,
					"IncidentReportTemplateMapperDto_to_IncidentReportTemplateMapper");
			sessionFactory.getCurrentSession().update(templateMapper);
			templateMapper.setUpdatedDate(GlobalCommonDateUtils.getDate(dtoTemplateList.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (HibernateException he) {
		logger.error("updateIncidentReportTemplateMapper failed", he);
		} catch (Exception e) {
		logger.error("updateIncidentReportTemplateMapper failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response updateIncidentReportMasterStatus(IncidentReportMasterDto incidentReportMasterDto)
			throws ApplicationException {
				try {
					IncidentReportMaster incidentReportMaster=findById(incidentReportMasterDto.getIncidentReportId());
					
					incidentReportMaster.setStatus(incidentReportMasterDto.getStatus());
					incidentReportMaster.setUpdatedBy(incidentReportMasterDto.getUpdatedBy());
					incidentReportMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(incidentReportMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
					 
					update(incidentReportMaster);
			
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, incidentReportMasterDto);
			
			} catch (HibernateException he) {
			logger.error("updateIncidentReportMasterStatus failed", he);
			} catch (Exception e) {
			logger.error("updateIncidentReportMasterStatus failed", e);
			}
			return new Response<>(ERROR, ERROR_CODE, COMMON_UPDATE, null, null);
		}

	@Override
	public Response updateIncidentReportRoleMasterStatus(IncidentReportMasterDto incidentReportMasterDto)
			throws ApplicationException {
		try {
			IncidentReportMaster incidentReportMaster=findById(incidentReportMasterDto.getIncidentReportId());
			
			incidentReportMaster.setStatus(incidentReportMasterDto.getStatus());
			incidentReportMaster.setUpdatedBy(incidentReportMasterDto.getUpdatedBy());
			incidentReportMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(incidentReportMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			 
			update(incidentReportMaster);
	
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, incidentReportMasterDto);
			
			} catch (HibernateException he) {
			logger.error("updateIncidentReportRoleMasterStatus failed", he);
			} catch (Exception e) {
			logger.error("updateIncidentReportRoleMasterStatus failed", e);
			}
			return new Response<>(ERROR, ERROR_CODE, COMMON_UPDATE, null, null);
		}

	@Override
	public Response updateIncidentReportTemplateMasterStatus(IncidentReportMasterDto incidentReportMasterDto)
			throws ApplicationException {
		try {
			IncidentReportMaster incidentReportMaster=findById(incidentReportMasterDto.getIncidentReportId());
			
			incidentReportMaster.setStatus(incidentReportMasterDto.getStatus());
			incidentReportMaster.setUpdatedBy(incidentReportMasterDto.getUpdatedBy());
			incidentReportMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(incidentReportMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			 
			update(incidentReportMaster);
	
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
			
			} catch (HibernateException he) {
			logger.error("updateIncidentReportTemplateMasterStatus failed", he);
			} catch (Exception e) {
			logger.error("updateIncidentReportTemplateMasterStatus failed", e);
			}
			return new Response<>(ERROR, ERROR_CODE, COMMON_UPDATE, null, null);
		}


	@Override
	public Response getActiveIncidentReportMasterList(IncidentReportMasterDto incidentReportMasterDto) {
	try {
		List<IncidentReportRoleMapperDto> list2=new ArrayList<>();
	
		List<IncidentReportMasterDto> list1=sessionFactory.getCurrentSession()
		.getNamedQuery("GET_ACTIVE_INCIDENT_REPORT_MASTER_LIST")
		.setParameter("organizationId", incidentReportMasterDto.getOrganizationId())
		.setParameter("unitId", incidentReportMasterDto.getUnitId())
		.setResultTransformer(Transformers.aliasToBean(IncidentReportMasterDto.class)).list();
		
		if(list1.size() > 0 && list1 !=null) {
			for(IncidentReportMasterDto dto : list1) 
			{			
				list2=sessionFactory.getCurrentSession()
						.getNamedQuery("GET_INCIDENT_REPORT_MASTER_LIST_FOR_ROLE")
						.setParameter("organizationId",  incidentReportMasterDto.getOrganizationId())
						.setParameter("unitId",  incidentReportMasterDto.getUnitId())
						.setParameter("incidentReportId",  dto.getIncidentReportId())
						.setResultTransformer(Transformers.aliasToBean(IncidentReportRoleMapperDto.class)).list();
				
				dto.setIncidentReportRoleMapperList(list2);
//				list1.add(dto);
				if(list2.size() > 0 && list2 != null) {
					for(IncidentReportRoleMapperDto dto2 : list2) 
					{			
						List<IncidentReportTemplateMapperDto> list3=sessionFactory.getCurrentSession()
								.getNamedQuery("GET_INCIDENT_REPORT_MASTER_LIST_FOR_TEMPLATE")
								.setParameter("organizationId",  incidentReportMasterDto.getOrganizationId())
								.setParameter("unitId",  incidentReportMasterDto.getUnitId())
								.setParameter("incidentReportId",  dto2.getIncidentReportId())
								.setParameter("roleId",  dto2.getRoleId())
								.setResultTransformer(Transformers.aliasToBean(IncidentReportTemplateMapperDto.class)).list();
						
						dto.setIncidentReportTemplateMapperList(list3);
//						list2.add(dto2);
						
					}
				}
				
				
			}
		}
	
		return new Response(SUCCESS, SUCCESS_CODE, null, list1, null);
	
	
			} catch (HibernateException he) {
				throw he ;
			} catch (Exception e) {
			   throw e;
			}
		//return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getIncidentReportMasterCount(IncidentReportMasterDto incidentReportMasterDto)
	throws ApplicationException {
	try {

		/*Session session = sessionFactory.getCurrentSession();
		Criteria crit = session.createCriteria(IncidentReportMaster.class);
		crit.add(Restrictions.eq("organizationId", incidentReportMasterDto.getOrganizationId()));
		crit.add(Restrictions.eq("unitId", incidentReportMasterDto.getUnitId()));
		crit.setProjection(Projections.rowCount());
		Long count = (Long) crit.uniqueResult();*/
		
		List<IncidentReportMasterDto> list1=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_INCIDENT_REPORT_MASTER_LIST")
				.setParameter("organizationId",  incidentReportMasterDto.getOrganizationId())
				.setParameter("unitId",  incidentReportMasterDto.getUnitId())
				.setResultTransformer(Transformers.aliasToBean(IncidentReportMasterDto.class)).list();
	
		return new Response(SUCCESS, SUCCESS_CODE, null, null, list1.size());
	
		} catch (HibernateException he) {
		logger.error("getIncidentReportMasterCount failed", he);
		} catch (Exception e) {
		logger.error("getIncidentReportMasterCount failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response checkIncidentReportRoleAvailableByRoleId(IncidentReportMasterDto incidentReportMasterDto)
	throws ApplicationException {
	try {
	List<IncidentReportRoleMapperDto> list2=new ArrayList<>();

	List<IncidentReportMasterDto> list1=sessionFactory.getCurrentSession()
	.getNamedQuery("CHECK_INCIDENT_REPORT_ROLE_AVAILABLE_BY_ROLE_ID")
	.setParameter("roleId", incidentReportMasterDto.getRoleId())
	.setParameter("incidentReportId", incidentReportMasterDto.getIncidentReportId())
	.setParameter("organizationId", incidentReportMasterDto.getOrganizationId())
	.setParameter("unitId", incidentReportMasterDto.getUnitId())
	.setResultTransformer(Transformers.aliasToBean(IncidentReportMasterDto.class)).list();

	return new Response(SUCCESS, SUCCESS_CODE, null, list1, null);


	} catch (HibernateException he) {
	logger.error("checkIncidentReportRoleAvailableByRoleId failed", he);
	} catch (Exception e) {
	logger.error("checkIncidentReportRoleAvailableByRoleId failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response checkIncidentReportTemplateAvailableByTemplateId(IncidentReportMasterDto incidentReportMasterDto)
	throws ApplicationException {
	try {
	List<IncidentReportRoleMapperDto> list2=new ArrayList<>();

	List<IncidentReportMasterDto> list1=sessionFactory.getCurrentSession()
	.getNamedQuery("CHECK_INCIDENT_REPORT_TEMPLATE_AVAILABLE_BY_TEMPLATE_ID")
	.setParameter("templateId", incidentReportMasterDto.getTemplateId())
	.setParameter("incidentReportId", incidentReportMasterDto.getIncidentReportId())
	.setParameter("organizationId", incidentReportMasterDto.getOrganizationId())
	.setParameter("unitId", incidentReportMasterDto.getUnitId())
	.setResultTransformer(Transformers.aliasToBean(IncidentReportMasterDto.class)).list();

	return new Response(SUCCESS, SUCCESS_CODE, null, list1, null);


	} catch (HibernateException he) {
	logger.error("checkIncidentReportTemplateAvailableByTemplateId failed", he);
	} catch (Exception e) {
	logger.error("checkIncidentReportTemplateAvailableByTemplateId failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	


}
