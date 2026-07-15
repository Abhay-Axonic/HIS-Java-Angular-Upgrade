package com.param.global.nabhprocess.dao;

import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.model.NabhProcessMaster;
import com.param.global.nabhprocess.dto.NabhProcessMasterDto;
import com.param.global.unit.dto.IncidentReportRoleMapperDto;
import com.param.global.unit.dto.IncidentReportTemplateMapperDto;
import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({"rawtypes", "unchecked"})
@Repository
public class NabhProcessMasterDaoImpl extends GenericDao<NabhProcessMaster, Integer> implements INabhProcessMasterDao,ICommonConstants{
	final static Logger logger = Logger.getLogger(NabhProcessMasterDaoImpl.class);



	public NabhProcessMasterDaoImpl() {
		super(NabhProcessMaster.class);
	}

	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	DozerBeanMapper mapper;
	
	@Override
	public Response saveNabhProcessMaster(NabhProcessMasterDto nabhProcessMasterDto)
			throws ApplicationException {
		try {
			NabhProcessMaster nabhProcessMaster=new NabhProcessMaster();
			nabhProcessMaster.setNabhProcessCode(nabhProcessMasterDto.getNabhProcessCode());
			nabhProcessMaster.setNabhProcessDesc(nabhProcessMasterDto.getNabhProcessDesc());
			nabhProcessMaster.setOrganizationId(nabhProcessMasterDto.getOrganizationId());
			nabhProcessMaster.setUnitId(nabhProcessMasterDto.getUnitId());
			nabhProcessMaster.setIsCapaOff(nabhProcessMasterDto.getIsCapaOff());
			nabhProcessMaster.setStatus(nabhProcessMasterDto.getStatus());
			nabhProcessMaster.setCreatedBy(nabhProcessMasterDto.getCreatedBy());
			nabhProcessMaster.setCreatedDate(GlobalCommonDateUtils.getDate(nabhProcessMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			nabhProcessMaster.setUpdatedBy(nabhProcessMasterDto.getUpdatedBy());
			nabhProcessMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(nabhProcessMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			 
			NabhProcessMaster savedNabhProcessMaster = save(nabhProcessMaster);
			nabhProcessMasterDto.setNabhProcessId(savedNabhProcessMaster.getNabhProcessId());
		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, nabhProcessMasterDto);
		
		} catch (HibernateException e) {
		logger.error("saveNabhProcessMaster failed", e);
			String errorMsg = NabhProcessMasterDaoImpl.class.getName()+" :"+e.getMessage();
			return new Response<>(ERROR, ERROR_CODE, errorMsg, null, null);
		}
	}

	@Override
	public Response updateNabhProcessMaster(NabhProcessMasterDto nabhProcessMasterDto)
			throws ApplicationException {
		try {
			NabhProcessMaster nabhProcessMaster=findById(nabhProcessMasterDto.getNabhProcessId());
			nabhProcessMaster.setNabhProcessCode(nabhProcessMasterDto.getNabhProcessCode());
			nabhProcessMaster.setNabhProcessDesc(nabhProcessMasterDto.getNabhProcessDesc());
			nabhProcessMaster.setOrganizationId(nabhProcessMasterDto.getOrganizationId());
			nabhProcessMaster.setIsCapaOff(nabhProcessMasterDto.getIsCapaOff());
			nabhProcessMaster.setUnitId(nabhProcessMasterDto.getUnitId());
			nabhProcessMaster.setStatus(nabhProcessMasterDto.getStatus());
//			nabhProcessMaster.setCreatedBy(nabhProcessMasterDto.getCreatedBy());
//			nabhProcessMaster.setCreatedDate(GlobalCommonDateUtils.getDate(nabhProcessMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			nabhProcessMaster.setUpdatedBy(nabhProcessMasterDto.getUpdatedBy());
			nabhProcessMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(nabhProcessMasterDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			 
			update(nabhProcessMaster);
			nabhProcessMasterDto.setNabhProcessId(nabhProcessMaster.getNabhProcessId());

			if (nabhProcessMasterDto.getNabhProcessId() != null
					&& nabhProcessMasterDto.getOrganizationId() != null
					&& nabhProcessMasterDto.getUnitId() != null) {

				int rowsAffected = sessionFactory.getCurrentSession()
						.createQuery("UPDATE IncidentReportRoleMapper "
								+ "SET status = :status "
								+ "WHERE nabhProcessId = :nabhProcessId "
								+ "AND organizationId = :organizationId "
								+ "AND unitId = :unitId")
						.setParameter("status", 'I')
						.setParameter("nabhProcessId", nabhProcessMasterDto.getNabhProcessId())
						.setParameter("organizationId", nabhProcessMasterDto.getOrganizationId())
						.setParameter("unitId", nabhProcessMasterDto.getUnitId())
						.executeUpdate();
			}


			if (nabhProcessMasterDto.getNabhProcessId() != null
					&& nabhProcessMasterDto.getOrganizationId() != null
					&& nabhProcessMasterDto.getUnitId() != null) {

				int rowsAffected = sessionFactory.getCurrentSession()
						.createQuery("UPDATE IncidentReportTemplateMapper incidentReportTemplateMapper "
								+ "SET incidentReportTemplateMapper.status = :status "
								+ "WHERE nabhProcessId = :nabhProcessId "
								+ "AND organizationId = :organizationId "
								+ "AND unitId = :unitId")
						.setParameter("status", 'I')
						.setParameter("nabhProcessId", nabhProcessMasterDto.getNabhProcessId())
						.setParameter("organizationId", nabhProcessMasterDto.getOrganizationId())
						.setParameter("unitId", nabhProcessMasterDto.getUnitId())
						.executeUpdate();
			}

			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, nabhProcessMasterDto);
		
		} catch (HibernateException he) {
		logger.error("updateNabhProcessMaster failed", he);
			String errorMsg = NabhProcessMasterDaoImpl.class.getName()+" :"+he.getMessage();
			return new Response<>(ERROR, ERROR_CODE, errorMsg, null, null);
		}
	}




	@Override
	public Response getNabhProcessMasterList(NabhProcessMasterDto nabhProcessMasterDto)
			throws ApplicationException {
		try {
			List<NabhProcessMasterDto> list1=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_NABH_PROCESS_MASTER_LIST")
					.setParameter("organizationId",  nabhProcessMasterDto.getOrganizationId())
					.setParameter("unitId",  nabhProcessMasterDto.getUnitId())
					.setFirstResult(nabhProcessMasterDto.getOffset() != null ? nabhProcessMasterDto.getOffset()  : 0)
					.setMaxResults(nabhProcessMasterDto.getNoOfRecordsPerPage() != null ? nabhProcessMasterDto.getNoOfRecordsPerPage()  : 10)
					.setResultTransformer(Transformers.aliasToBean(NabhProcessMasterDto.class)).list();
		       List<NabhProcessMasterDto> count=sessionFactory.getCurrentSession()
		                .getNamedQuery("GET_NABH_PROCESS_MASTER_LIST_COUNT")
		                .setParameter("organizationId",  nabhProcessMasterDto.getOrganizationId())
		                .setParameter("unitId",  nabhProcessMasterDto.getUnitId())
		                .setResultTransformer(Transformers.aliasToBean(NabhProcessMasterDto.class)).list();
			if(list1.size() > 0 && list1 !=null) {
				for(NabhProcessMasterDto dto : list1) 
				{			
					List<IncidentReportRoleMapperDto> list2=sessionFactory.getCurrentSession()
							.getNamedQuery("GET_NABH_PROCESS_MASTER_LIST_FOR_ROLE")
							.setParameter("organizationId",  nabhProcessMasterDto.getOrganizationId())
							.setParameter("unitId",  nabhProcessMasterDto.getUnitId())
							.setParameter("nabhProcessId",  dto.getNabhProcessId())
							.setResultTransformer(Transformers.aliasToBean(IncidentReportRoleMapperDto.class)).list();
					
					dto.setIncidentReportRoleMapperList(list2); 
					if(list2.size() > 0 && list2 != null) {
						for(IncidentReportRoleMapperDto dto2 : list2) 
						{			
							List<IncidentReportTemplateMapperDto> list3=sessionFactory.getCurrentSession()
									.getNamedQuery("GET_NABH_PROCESS_MASTER_LIST_FOR_TEMPLATE")
									.setParameter("organizationId",  nabhProcessMasterDto.getOrganizationId())
									.setParameter("unitId",  nabhProcessMasterDto.getUnitId())
									.setParameter("nabhProcessId",  dto2.getNabhProcessId())
									.setParameter("roleId",  dto2.getRoleId())
									.setResultTransformer(Transformers.aliasToBean(IncidentReportTemplateMapperDto.class)).list();
							
							dto.setIncidentReportTemplateMapperList(list3);
							
						}
					}
					
					
				}
			}
			
			return new Response(SUCCESS, SUCCESS_CODE, null, list1, count.size());
			
		
		} catch (HibernateException e) {
		logger.error("getNabhProcessMasterList failed", e);
			String errorMsg = NabhProcessMasterDaoImpl.class.getName()+" :"+e.getMessage();
			return new Response<>(ERROR, ERROR_CODE, errorMsg, null, null);
		}
	}

	@Override
	public Response updateNabhProcessMasterStatus(NabhProcessMasterDto nabhProcessMasterDto)
			throws ApplicationException {
				try {
					NabhProcessMaster nabhProcessMaster=findById(nabhProcessMasterDto.getNabhProcessId());
					
					nabhProcessMaster.setStatus(nabhProcessMasterDto.getStatus());
					nabhProcessMaster.setUpdatedBy(nabhProcessMasterDto.getUpdatedBy());
					nabhProcessMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(nabhProcessMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
					 
					update(nabhProcessMaster);
			
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, nabhProcessMasterDto);
			
			} catch (HibernateException he) {
			logger.error("updateNabhProcessMasterStatus failed", he);
			} catch (Exception e) {
			logger.error("updateNabhProcessMasterStatus failed", e);
			}
			return new Response<>(ERROR, ERROR_CODE, COMMON_UPDATE, null, null);
		}

	@Override
	public Response updateNabhProcessRoleMasterStatus(NabhProcessMasterDto nabhProcessMasterDto)
			throws ApplicationException {
		try {
			NabhProcessMaster nabhProcessMaster=findById(nabhProcessMasterDto.getNabhProcessId());
			
			nabhProcessMaster.setStatus(nabhProcessMasterDto.getStatus());
			nabhProcessMaster.setUpdatedBy(nabhProcessMasterDto.getUpdatedBy());
			nabhProcessMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(nabhProcessMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			 
			update(nabhProcessMaster);
	
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, nabhProcessMasterDto);
			
			} catch (HibernateException he) {
			logger.error("updateNabhProcessRoleMasterStatus failed", he);
			} catch (Exception e) {
			logger.error("updateNabhProcessRoleMasterStatus failed", e);
			}
			return new Response<>(ERROR, ERROR_CODE, COMMON_UPDATE, null, null);
		}

	@Override
	public Response updateNabhProcessTemplateMasterStatus(NabhProcessMasterDto nabhProcessMasterDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Response getActiveNabhProcessMasterList(NabhProcessMasterDto nabhProcessMasterDto) {
	try {
		List<IncidentReportRoleMapperDto> list2=new ArrayList<>();
	
		List<NabhProcessMasterDto> list1=sessionFactory.getCurrentSession()
		.getNamedQuery("GET_ACTIVE_NABH_PROCESS_MASTER_LIST")
		.setParameter("organizationId", nabhProcessMasterDto.getOrganizationId())
		.setParameter("unitId", nabhProcessMasterDto.getUnitId())
		.setResultTransformer(Transformers.aliasToBean(NabhProcessMasterDto.class)).list();
		
		if(list1.size() > 0 && list1 !=null) {
			for(NabhProcessMasterDto dto : list1) 
			{			
				list2=sessionFactory.getCurrentSession()
						.getNamedQuery("GET_NABH_PROCESS_MASTER_LIST_FOR_ROLE")
						.setParameter("organizationId",  nabhProcessMasterDto.getOrganizationId())
						.setParameter("unitId",  nabhProcessMasterDto.getUnitId())
						.setParameter("nabhProcessId",  dto.getNabhProcessId())
						.setResultTransformer(Transformers.aliasToBean(IncidentReportRoleMapperDto.class)).list();
				
				dto.setIncidentReportRoleMapperList(list2);
				if(list2.size() > 0 && list2 != null) {
					for(IncidentReportRoleMapperDto dto2 : list2) 
					{			
						List<IncidentReportTemplateMapperDto> list3=sessionFactory.getCurrentSession()
								.getNamedQuery("GET_NABH_PROCESS_MASTER_LIST_FOR_TEMPLATE")
								.setParameter("organizationId",  nabhProcessMasterDto.getOrganizationId())
								.setParameter("unitId",  nabhProcessMasterDto.getUnitId())
								.setParameter("nabhProcessId",  dto2.getNabhProcessId())
								.setParameter("roleId",  dto2.getRoleId())
								.setResultTransformer(Transformers.aliasToBean(IncidentReportTemplateMapperDto.class)).list();
						
						dto.setIncidentReportTemplateMapperList(list3);
					}
				}
				
				
			}
		}
	
		return new Response(SUCCESS, SUCCESS_CODE, null, list1, null);
	
	
			} catch (HibernateException he) {
			logger.error("getActiveNabhProcessMasterList failed", he);
				String errorMsg = NabhProcessMasterDaoImpl.class.getName()+" :"+he.getMessage();
				return new Response<>(ERROR, ERROR_CODE, errorMsg, null, null);
			}

	}

}
