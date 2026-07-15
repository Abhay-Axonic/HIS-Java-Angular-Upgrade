package com.param.global.unit.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.IBillingCommonConstants;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.model.ReportedIncidentRoleMapper;
import com.param.global.model.ReportedIncidentUserMapper;
import com.param.global.nabhprocess.dao.NabhProcessMasterDaoImpl;
import com.param.global.nabhprocess.dto.ReportedNabhMapperDto;
import com.param.global.unit.dto.IncidentReportMasterDto;
import com.param.global.unit.dto.IncidentReportRoleMapperDto;
import com.param.global.unit.dto.IncidentReportTemplateMapperDto;
import com.param.global.unit.dto.IncidentReportingMapperDto;
import com.param.global.unit.dto.NabhReportingMapperDto;
import com.param.global.unit.dto.ReportedIncidentMapperDto;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({ "rawtypes", "unchecked" })
@Repository
public class ReportedIncidentUserMapperDaoImpl extends GenericDao<ReportedIncidentUserMapper, Integer>
		implements IReportedIncidentUserMapperDao, ICommonConstants, IBillingCommonConstants {
	final static Logger logger = Logger.getLogger(ReportedIncidentUserMapperDaoImpl.class);


	public ReportedIncidentUserMapperDaoImpl() {
		super(ReportedIncidentUserMapper.class);
	}

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response saveReportedIncidentUserMapper(ReportedIncidentMapperDto dtoUser) throws ApplicationException {
		try {

				ReportedIncidentUserMapper reportedIncidentMapper = new ReportedIncidentUserMapper();

				reportedIncidentMapper.setReportedIncMpprId(dtoUser.getReportedIncMpprId());				
				reportedIncidentMapper.setReportedTemplateId(dtoUser.getReportedTemplateId());
				reportedIncidentMapper.setReportedTemplateObj(dtoUser.getReportedTemplateObj());
				reportedIncidentMapper.setParentRepIncUserId(dtoUser.getParentRepIncUserId());
				reportedIncidentMapper.setIsSubmit(dtoUser.getIsSubmit());
				reportedIncidentMapper.setRoleId(dtoUser.getRoleId());
				reportedIncidentMapper.setUserId(dtoUser.getUserId());
				reportedIncidentMapper.setOrganizationId(dtoUser.getOrganizationId());
				reportedIncidentMapper.setUnitId(dtoUser.getUnitId());
				reportedIncidentMapper.setStatus(dtoUser.getStatus());
				reportedIncidentMapper.setCreatedBy(dtoUser.getCreatedBy());
				reportedIncidentMapper.setResolutionDetailsTypeId(dtoUser.getResolutionDetailsTypeId());
				reportedIncidentMapper
						.setCreatedDate(GlobalCommonDateUtils.getDate(dtoUser.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
				reportedIncidentMapper.setUpdatedBy(dtoUser.getUpdatedBy());
				reportedIncidentMapper
						.setUpdatedDate(GlobalCommonDateUtils.getDate(dtoUser.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
				reportedIncidentMapper.setConversationId(dtoUser.getConversationId());
				save(reportedIncidentMapper);

				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (Exception e) {
		logger.error("saveReportedIncidentUserMapper failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getReportedIncidentMapperList(ReportedIncidentMapperDto dtoUser)
			throws ApplicationException {
	try {
		List<ReportedIncidentMapperDto> list=sessionFactory.getCurrentSession().getNamedQuery("GET_REPORTED_INCIDENT_MAPPER_LIST")
				.setParameter("organizationId", dtoUser.getOrganizationId())
				.setParameter("unitId", dtoUser.getUnitId())
				.setParameter("fromDate",dtoUser.getFromDate())
				.setParameter("toDate", dtoUser.getToDate())
				.setParameter("specialityId", dtoUser.getSpecialityId())
				.setParameter("incidentTypeId", dtoUser.getIncidentTypeId())
				.setParameter("userId", dtoUser.getUserId())
				.setResultTransformer(Transformers.aliasToBean(IncidentReportingMapperDto.class)).list();
		
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

	} catch (HibernateException he) {
	logger.error("getReportedIncidentMapperList failed", he);
	}
	catch (Exception e) {
	logger.error("getReportedIncidentMapperList failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@Override
	public Response getReportedNabhMapperList(ReportedNabhMapperDto dtoUser)
			throws ApplicationException {
	try {
		List<ReportedNabhMapperDto> list=sessionFactory.getCurrentSession().getNamedQuery("GET_REPORTED_NABH_MAPPER_LIST")
				.setParameter("organizationId", dtoUser.getOrganizationId())
				.setParameter("unitId", dtoUser.getUnitId())
				.setParameter("fromDate",dtoUser.getFromDate())
				.setParameter("toDate", dtoUser.getToDate())
				.setParameter("specialityId", dtoUser.getSpecialityId())
				.setParameter("nabhTypeId", dtoUser.getNabhTypeId())
				.setParameter("userId", dtoUser.getUserId())
				.setResultTransformer(Transformers.aliasToBean(ReportedNabhMapperDto.class)).list();
		
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

	} catch (HibernateException he) {
	logger.error("getReportedNabhMapperList failed", he);
		String errorMsg = ReportedIncidentUserMapperDaoImpl.class.getName()+" :"+he.getMessage();
		return new Response<>(ERROR, ERROR_CODE, errorMsg, null, null);
	}
	}

	@Override
	public Response getReportedIncidentMapperById(ReportedIncidentMapperDto dtoUser)
			throws ApplicationException {
		try {
			/*List<IncidentReportingMapperDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_REPORTED_INCIDENT_MAPPER_BY_ID")
					.setParameter("organizationId", dtoUser.getOrganizationId())
					.setParameter("unitId", dtoUser.getUnitId())
					.setParameter("reportedIncMpprId", dtoUser.getReportedIncMpprId())
					.setResultTransformer(Transformers.aliasToBean(IncidentReportingMapperDto.class)).list();

			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);*/
			

			List<ReportedIncidentMapperDto> list2=new ArrayList<>();
			
			List<ReportedIncidentMapperDto> list1 = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_REPORTED_INCIDENT_MAPPER_BY_ID")
					.setParameter("organizationId", dtoUser.getOrganizationId())
					.setParameter("unitId", dtoUser.getUnitId())
					.setParameter("reportedIncMpprId", dtoUser.getReportedIncMpprId())
//					.setParameter("patientId", dtoUser.getPatientId())
					.setResultTransformer(Transformers.aliasToBean(ReportedIncidentMapperDto.class)).list();
			
				if(list1.size() > 0 && list1 !=null) {
				for(ReportedIncidentMapperDto dto : list1)
				{
						list2=sessionFactory.getCurrentSession()
							.getNamedQuery("GET_REPORTED_INCIDENT_MAPPER_FOR_ROLE_BY_ID")
							.setParameter("organizationId", dtoUser.getOrganizationId())
							.setParameter("unitId", dtoUser.getUnitId())
							.setParameter("reportedIncMpprId", dtoUser.getReportedIncMpprId())
							.setResultTransformer(Transformers.aliasToBean(ReportedIncidentMapperDto.class)).list();
						
						dto.setReportedIncidentRoleMapperList(list2);
				
				if(list2.size() > 0 && list2 != null) {
				for(ReportedIncidentMapperDto dto2 : list2)
				{
						List<ReportedIncidentMapperDto> list3=sessionFactory.getCurrentSession()
								.getNamedQuery("GET_REPORTED_INCIDENT_MAPPER_FOR_USER_BY_ID")
								.setParameter("organizationId", dtoUser.getOrganizationId())
								.setParameter("unitId", dtoUser.getUnitId())
								.setParameter("reportedIncMpprId", dto2.getReportedIncMpprId())
								.setParameter("roleId", dto2.getRoleId())
								.setResultTransformer(Transformers.aliasToBean(ReportedIncidentMapperDto.class)).list();
				
				         dto.setReportedIncidentUserMapperList(list3);
				}
			}
		}
     }
	return new Response(SUCCESS, SUCCESS_CODE, null, list1, null);
	
	

		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			throw e;
		}
		//return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getTemplateByReportedIncidentId(ReportedIncidentMapperDto reportedIncidentMapperDto)
			throws ApplicationException {
		try {
			List<ReportedIncidentMapperDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_TEMPLATE_BY_REPORTED_INCIDENT_ID")
					.setParameter("organizationId", reportedIncidentMapperDto.getOrganizationId())
					.setParameter("unitId", reportedIncidentMapperDto.getUnitId())
					.setParameter("incidentReportingMapperId", reportedIncidentMapperDto.getIncidentReportingMapperId())
					.setResultTransformer(Transformers.aliasToBean(IncidentReportingMapperDto.class)).list();
			
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

		} catch (HibernateException he) {
		logger.error("getTemplateByReportedIncidentId failed", he);
		}
		catch (Exception e) {
		logger.error("getTemplateByReportedIncidentId failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}

	public Response submitReportedIncidentUserMapper(ReportedIncidentMapperDto dtoUser) throws ApplicationException {
		try {

			Session session = sessionFactory.getCurrentSession();

			String sql = "UPDATE public.t_reported_incident_user_mapper " +
					"SET is_submit = :isSubmit, " +
					"resolution_details_type_id = :resolutionDetailsTypeId " +
					"WHERE rep_inc_user_mppr_id = :repIncUserMpprId";

			Query query = session.createSQLQuery(sql);
			query.setParameter("isSubmit", dtoUser.getIsSubmit());
			query.setParameter("resolutionDetailsTypeId", dtoUser.getResolutionDetailsTypeId());
			query.setParameter("repIncUserMpprId", dtoUser.getRepIncUserMpprId());

			int count = query.executeUpdate();


			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, dtoUser);
		} catch (HibernateException he) {
		logger.error("submitReportedIncidentUserMapper failed", he);
		} catch (Exception e) {
		logger.error("submitReportedIncidentUserMapper failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getReportedNabhMapperList(ReportedIncidentMapperDto reportedIncidentMapperDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response saveReportedNabhUserMapper(ReportedIncidentMapperDto dtoUser) throws ApplicationException {
		try {
				ReportedIncidentUserMapper reportedIncidentMapper = new ReportedIncidentUserMapper();

				reportedIncidentMapper.setReportedNabhMpprId(dtoUser.getReportedNabhMpprId());				
				reportedIncidentMapper.setReportedTemplateId(dtoUser.getReportedTemplateId());
				reportedIncidentMapper.setReportedTemplateObj(dtoUser.getReportedTemplateObj());
				reportedIncidentMapper.setParentRepIncUserId(dtoUser.getParentRepIncUserId());
				reportedIncidentMapper.setIsSubmit(dtoUser.getIsSubmit());
				reportedIncidentMapper.setRoleId(dtoUser.getRoleId());
				reportedIncidentMapper.setUserId(dtoUser.getUserId());
				reportedIncidentMapper.setOrganizationId(dtoUser.getOrganizationId());
				reportedIncidentMapper.setUnitId(dtoUser.getUnitId());
				reportedIncidentMapper.setStatus(dtoUser.getStatus());
				reportedIncidentMapper.setCreatedBy(dtoUser.getCreatedBy());
				reportedIncidentMapper.setResolutionDetailsTypeId(dtoUser.getResolutionDetailsTypeId());
				reportedIncidentMapper
						.setCreatedDate(GlobalCommonDateUtils.getDate(dtoUser.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
				reportedIncidentMapper.setUpdatedBy(dtoUser.getUpdatedBy());
				reportedIncidentMapper
						.setUpdatedDate(GlobalCommonDateUtils.getDate(dtoUser.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
				reportedIncidentMapper.setConversationId(dtoUser.getConversationId());
				save(reportedIncidentMapper);

				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);

		} catch (HibernateException he) {
		logger.error("saveReportedNabhUserMapper failed", he);
		} catch (Exception e) {
		logger.error("saveReportedNabhUserMapper failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response submitReportedNabhUserMapper(ReportedIncidentMapperDto dtoUser) throws ApplicationException {
		try {
			Session session = sessionFactory.getCurrentSession();
			String sql = "UPDATE public.t_reported_incident_user_mapper " +
					"SET is_submit = :isSubmit, " +
					"resolution_details_type_id = :resolutionDetailsTypeId " +
					"WHERE rep_inc_user_mppr_id = :repIncUserMpprId";
			Query query = session.createSQLQuery(sql);
			query.setParameter("isSubmit", dtoUser.getIsSubmit());
			query.setParameter("resolutionDetailsTypeId", dtoUser.getResolutionDetailsTypeId());
			query.setParameter("repIncUserMpprId", dtoUser.getRepIncUserMpprId());
			int count = query.executeUpdate();

			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, dtoUser);
		} catch (HibernateException he) {
		logger.error("submitReportedNabhUserMapper failed", he);
			String errorMsg = ReportedIncidentUserMapperDaoImpl.class.getName()+" :"+he.getMessage();
			return new Response<>(ERROR, ERROR_CODE, errorMsg, null, null);
		}

	}

	@Override
	public Response getTemplateByReportedNabhId(ReportedNabhMapperDto reportedNabhMapperDto)
			throws ApplicationException {
		try {
			List<ReportedNabhMapperDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_TEMPLATE_BY_REPORTED_NABH_ID")
					.setParameter("organizationId", reportedNabhMapperDto.getOrganizationId())
					.setParameter("unitId", reportedNabhMapperDto.getUnitId())
					.setParameter("nabhReportingMapperId", reportedNabhMapperDto.getReportedNabhMpprId())
					.setResultTransformer(Transformers.aliasToBean(ReportedNabhMapperDto.class)).list();
			
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

		} catch (HibernateException he) {
			throw he;
		}
		catch (Exception e) {
			throw e;
		}
		//return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}

	@Override
	public Response getReportedNabhMapperById(ReportedNabhMapperDto reportedNabhMapperDto)
			throws ApplicationException {
		try {
			List<ReportedIncidentMapperDto> list2=new ArrayList<>();
			
			List<ReportedNabhMapperDto> list1=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_TEMPLATE_BY_REPORTED_NABH_PROCESS_ID")
					.setParameter("organizationId", reportedNabhMapperDto.getOrganizationId())
					.setParameter("unitId", reportedNabhMapperDto.getUnitId())
					.setParameter("reportedNabhMpprId", reportedNabhMapperDto.getReportedNabhMpprId())
					.setResultTransformer(Transformers.aliasToBean(ReportedNabhMapperDto.class)).list();
			
				if(list1.size() > 0 && list1 !=null) {
				for(ReportedNabhMapperDto dto : list1)
				{
						list2=sessionFactory.getCurrentSession()
							.getNamedQuery("GET_REPORTED_NABH_MAPPER_FOR_ROLE_BY_ID")
							.setParameter("organizationId", reportedNabhMapperDto.getOrganizationId())
							.setParameter("unitId", reportedNabhMapperDto.getUnitId())
							.setParameter("reportedNabhMpprId", dto.getReportedNabhMpprId())
							.setResultTransformer(Transformers.aliasToBean(ReportedIncidentMapperDto.class)).list();
						
						dto.setReportedIncidentRoleMapperList(list2);
				
				if(list2.size() > 0 && list2 != null) {
				for(ReportedIncidentMapperDto dto2 : list2)
				{
						List<ReportedIncidentMapperDto> list3=sessionFactory.getCurrentSession()
								.getNamedQuery("GET_REPORTED_NABH_MAPPER_FOR_USER_BY_ID")
								.setParameter("organizationId", reportedNabhMapperDto.getOrganizationId())
								.setParameter("unitId", reportedNabhMapperDto.getUnitId())
								.setParameter("reportedNabhMpprId", dto2.getReportedNabhMpprId())
								.setParameter("roleId", dto2.getRoleId())
								.setResultTransformer(Transformers.aliasToBean(ReportedIncidentMapperDto.class)).list();
				
				         dto.setReportedIncidentUserMapperList(list3);
				}
			}
		}
     }
	return new Response(SUCCESS, SUCCESS_CODE, null, list1, null);
	
	

		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			throw e;
		}
	}
    @Override
    public void updateReportedNabhUserMapper(ReportedIncidentMapperDto dtoUser) {
        Integer result = sessionFactory.getCurrentSession().getNamedQuery("UPDATE_REPORTED_INCIDENT_USER_MAPPER_NABH")
                .setParameter("status", 'I').setParameter("organizationId", dtoUser.getOrganizationId())
                .setParameter("unitId", dtoUser.getUnitId())
                .setParameter("repIncUserMpprId", dtoUser.getReportedNabhMpprId()).executeUpdate();
    }


	@Override
	public Integer updateReportedIncidentUserMapper(Integer reportedIncMpprId, Integer unitId, Integer organizationId ) throws ApplicationException {
		try {
			Integer result = sessionFactory.getCurrentSession().getNamedQuery("UPDATE_REPORTED_INCIDENT_USER_MAPPER")
					.setParameter("status", 'I')
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setParameter("reportedIncMpprId", reportedIncMpprId)
					.executeUpdate();

			return result;
		}
		catch (Exception e){
		logger.error("updateReportedIncidentUserMapper failed", e);
		}
		return 0;
	}
	}

	


