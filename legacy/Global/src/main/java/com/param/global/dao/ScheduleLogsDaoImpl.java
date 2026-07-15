package com.param.global.dao;

import java.util.List;

import javax.persistence.Query;
import javax.transaction.Transactional;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.param.entity.model.master.Organization;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.BillingMasterDto;
import com.param.global.dto.MyLifeConnectionLinkDto;
import com.param.global.dto.ScheduleLogsDto;
import com.param.global.dto.ScheduleLogsResDto;
import com.param.global.dto.sync.DoctorScheduleVisitSyncDto;
import com.param.global.dto.sync.ServiceappoServiceMpprSyncDto;
import com.param.global.model.ScheduleLogs;
import com.param.global.org.dto.OrganizationMasterDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ScheduleLogsDaoImpl extends GenericDao<ScheduleLogs, Integer>
		implements IScheduleLogsDao, ICommonConstants {
	final static Logger logger = Logger.getLogger(ScheduleLogsDaoImpl.class);


	public ScheduleLogsDaoImpl() {
		super(ScheduleLogs.class);
	}

	@Override
	@Transactional
	public Response saveScheduleLogs(ScheduleLogsDto scheduleLogsDto) throws ApplicationException {
		try {
			
			
			Boolean isSyncScheduling=(Boolean)sessionFactory.getCurrentSession()
					.createSQLQuery("  select om.is_sync_scheduling as \"isSyncScheduling\" from public.m_organization_master om where  om.organization_id=:organizationId and om.status='A' ")
					.setParameter("organizationId", scheduleLogsDto.getOrgId())
					.uniqueResult();
			
			if(isSyncScheduling == true) {
				ScheduleLogs scheduleLogs = new ScheduleLogs();
				scheduleLogs.setAction(scheduleLogsDto.getAction());
				scheduleLogs.setAddedBy(scheduleLogsDto.getAddedBy());
				scheduleLogs.setDateTime(GlobalCommonDateUtils.getDate(scheduleLogsDto.getDateTime(),"dd-M-yyyy HH:mm:ss"));
				scheduleLogs.setAddedDate(scheduleLogsDto.getAddedDate() != null ?GlobalCommonDateUtils.getDate(scheduleLogsDto.getAddedDate(), "dd-M-yyyy hh:mm:ss"): GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(0), "dd-M-yyyy hh:mm:ss"));
				scheduleLogs.setApplication("HIS");
				scheduleLogs.setErrorCount(0);
				scheduleLogs.setPriority("NORMAL");
				scheduleLogs.setRecordId(scheduleLogsDto.getRecordId());
				scheduleLogs.setStatus("PENDING");
				scheduleLogs.setTableName(scheduleLogsDto.getTableName());
				scheduleLogs.setAppointmentStatusId(scheduleLogsDto.getAppointmentStatusId() != null ? scheduleLogsDto.getAppointmentStatusId() : null);
				scheduleLogs.setDateTime(scheduleLogsDto.getDateTime() != null ?GlobalCommonDateUtils.getDate(scheduleLogsDto.getDateTime(), "dd-M-yyyy hh:mm:ss") : GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(0), "dd-M-yyyy hh:mm:ss"));
				scheduleLogs.setOrgId(scheduleLogsDto.getOrgId());
				
				ObjectMapper objectMapper = new ObjectMapper();
		        String json = objectMapper.writeValueAsString(scheduleLogs);
		        System.out.println("ORG.......... JSON is:"+json);
				
			scheduleLogs = save(scheduleLogs);
			
			return new Response(SUCCESS, SUCCESS_CODE, null, null, null);	
			}else {
				return new Response(SUCCESS, SUCCESS_CODE, null, null, null);
			}
		} catch (HibernateException he) {
		throw new ApplicationException(he.getMessage(), he);
	} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	public Response getPendingScheduleLogs() throws ApplicationException {
		try {
			List<ScheduleLogsResDto> pendingLogsList = sessionFactory.getCurrentSession()
			.getNamedQuery("GET_PENDING_SCHEDULED_LOGS")
			.setResultTransformer(Transformers.aliasToBean(ScheduleLogsResDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, pendingLogsList, null);
		} catch (Exception e) {
		logger.error("getPendingScheduleLogs failed", e);
		}
		return new Response(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response markScheduleLogsCompleted(Integer scheduleLogsId) throws ApplicationException {
		try {
			Session session=sessionFactory.openSession();
			session.beginTransaction();
			session.getNamedQuery("MARK_SCHEDULED_LOGS_COMPLETED")
			.setParameter("scheduleLogsId", scheduleLogsId).executeUpdate();
			session.getTransaction().commit();
			session.close();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		} catch (Exception e) {
		logger.error("markScheduleLogsCompleted failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response markScheduleLogsError(Integer scheduleLogsId) throws ApplicationException {
		try {
			sessionFactory.getCurrentSession()
			.getNamedQuery("MARK_SCHEDULED_LOGS_ERROR")
			.setParameter("scheduleLogsId", scheduleLogsId).executeUpdate();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		} catch (Exception e) {
		logger.error("markScheduleLogsError failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response getPendingScheduleLogsForAppointmenDetails() {
		try {
			List<ScheduleLogsResDto> pendingLogsList = sessionFactory.getCurrentSession()
			.getNamedQuery("GET_PENDING_SCHEDULED_LOGS_FOR_APPOINTMENTDETAIL")
			.setResultTransformer(Transformers.aliasToBean(ScheduleLogsResDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, pendingLogsList, null);
		} catch (Exception e) {
		logger.error("getPendingScheduleLogsForAppointmenDetails failed", e);
		}
		return new Response(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response updatePathalogyLabReportStatus(List<ServiceappoServiceMpprSyncDto> serviceAppoMapperList) {
		try {
			/*for(ServiceappoServiceMpprSyncDto list:serviceAppoMapperList) {
				if(list.getLabResultId()!=null) {*/
			Session session=sessionFactory.openSession();
			session.beginTransaction();
					Integer res=session.createSQLQuery("update lab.t_lab_test_result set  my_life_sync_flag = 'Y' where my_life_sync_flag = 'N' and sample_status_id = 8 ")
							    .executeUpdate();
					session.getTransaction().commit();
					session.close();
				//}
			//}
			
			return new Response(SUCCESS, SUCCESS_CODE, null, null, null);
		} catch (Exception e) {
		logger.error("updatePathalogyLabReportStatus failed", e);
		}
		return new Response(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	@Transactional
	public Response getConnectionStringForMyLife() throws ApplicationException {
		try {
			MyLifeConnectionLinkDto myLifeConnectionLinkDto=(MyLifeConnectionLinkDto)sessionFactory.getCurrentSession()
					.createSQLQuery(" select my_life_url as \"myLifeLink\",global_scheduling_url as \"globalSchedulingLink\","
							+ " status as \"status\", pacs_host as \"pacsHost\", pacs_port as \"pacsPort\"  "
							+ " from m_my_life_connection_link where status='A' limit 1 ")
					  .setResultTransformer(Transformers.aliasToBean(MyLifeConnectionLinkDto.class)).uniqueResult();
			if(myLifeConnectionLinkDto!=null)
			return new Response(SUCCESS, SUCCESS_CODE, null, null, myLifeConnectionLinkDto);
			else
			return new Response(ERROR, ERROR_CODE, null, null, null);	
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	public Response isRecordIdAndTableNameExist(ScheduleLogsDto scheduleLogsDto) throws ApplicationException {
		try {
			String split[]=scheduleLogsDto.getDateTime().split(" ");
			String time=split[1];
			MyLifeConnectionLinkDto dto=(MyLifeConnectionLinkDto)sessionFactory.getCurrentSession()
					.createSQLQuery(" select schedule_logs_id as \"scheduleLogsId\" from t_schedule_logs "
							+ " where table_name='"+scheduleLogsDto.getTableName()+"' and record_id="+scheduleLogsDto.getRecordId()+" and to_char(date_time,'HH24:MI:SS')='"+time+"' ")
					.setResultTransformer(Transformers.aliasToBean(MyLifeConnectionLinkDto.class)).uniqueResult();
			if(dto!=null)
			return new Response(SUCCESS, SUCCESS_CODE, null, null, dto);
			else
			return new Response(ERROR, ERROR_CODE, null, null, null);	
		} catch (Exception e) {
		logger.error("isRecordIdAndTableNameExist failed", e);
		}
		return new Response(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response getScheduleDetailByIdForGlobalScheduling(Integer recordId) throws ApplicationException {
		try {
			List<DoctorScheduleVisitSyncDto> scheduleDetailList=sessionFactory.getCurrentSession()
					.createSQLQuery("SELECT		docsch.doctor_schedule_id as \"scheduleId\", docsch.doctor_schedule_visit_detail_id as \"doctorScheduleVisitDetailId\"," 
							+" 			docsch.visit_type_id as \"visitTypeId\", docsch.status as \"visitTypeStatus\", " 
							+"			cast(docsch.visit_duration as varchar) as \"visitDuration\", docsch.created_by as \"createdBy\", " 
							+"			to_char(docsch.created_date,'DD-MM-YYYY') as \"createdDate\", docsch.visit_color as \"visitColor\",  2 as \"applicationId\" " 
							+" FROM		doctor.m_doctor_schedule_visit_details docsch "  
							+" WHERE		docsch.doctor_schedule_id =:scheduleId ")
					.setParameter("scheduleId", recordId)
					.setResultTransformer(Transformers.aliasToBean(DoctorScheduleVisitSyncDto.class)).list();
			if(scheduleDetailList!=null && scheduleDetailList.size()>0)
			return new Response(SUCCESS, SUCCESS_CODE, null, scheduleDetailList, null);
			else
			return new Response(ERROR, ERROR_CODE, null, null, null);	
		} catch (Exception e) {
		logger.error("getScheduleDetailByIdForGlobalScheduling failed", e);
		}
		return new Response(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response getMyLifeDetail(Integer id) throws ApplicationException {
		try {
			OrganizationMasterDto obj=(OrganizationMasterDto)sessionFactory.getCurrentSession()
					.createSQLQuery("select " + 
							"	coalesce(mom.is_sync_scheduling, false) as \"isSyncScheduling\", " + 
							"	mmlcl.my_life_url as \"myLifeLink\", " + 
							"	mmlcl.global_scheduling_url as \"globalSchedulingLink\", " + 
							"	mom.organization_id as \"organizationId\" " + 
							"from " + 
							"	m_organization_master mom " + 
							"cross join m_my_life_connection_link mmlcl " + 
							"where " + 
							"	mom.organization_id ="+id)
					  .setResultTransformer(Transformers.aliasToBean(OrganizationMasterDto.class)).uniqueResult();
			if(obj!=null)
			return new Response(SUCCESS, SUCCESS_CODE, null, null, obj);
			else
			return new Response(ERROR, ERROR_CODE, null, null, null);	
		} catch (Exception e) {
		logger.error("getMyLifeDetail failed", e);
		}
		return new Response(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	@Transactional
	public Integer getchannelIdForSync(Integer organizationId) throws ApplicationException {
		try{
			Integer channelId = (Integer)sessionFactory.getCurrentSession()
				.createSQLQuery("select channel_id from public.m_organization_master where organization_id=:organizationId ")
				.setParameter("organizationId", organizationId)
				.uniqueResult();
		
		return channelId;
	} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	
}

	
	@Override
	public Response markScheduleLogsCompletedUsingPatientId(Integer patientId) throws ApplicationException {
		try {
			Session session=sessionFactory.openSession();
			session.beginTransaction();
			session.getNamedQuery("MARK_SCHEDULED_LOGS_COMPLETED_WITH_PATIENT_ID")
			.setParameter("recordId", patientId).executeUpdate();
			session.getTransaction().commit();
			session.close();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		} catch (Exception e) {
		logger.error("markScheduleLogsCompletedUsingPatientId failed", e);
			String errorMsg = ScheduleLogsDaoImpl.class.getName() + " : "+e;
			return new Response<>(ERROR, ERROR_CODE, errorMsg, null, null);
		}

	}
}
