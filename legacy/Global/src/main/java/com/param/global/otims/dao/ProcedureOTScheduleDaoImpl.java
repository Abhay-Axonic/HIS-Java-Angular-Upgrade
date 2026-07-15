package com.param.global.otims.dao;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.otims.dto.ChecklistMasterDto;
import com.param.global.otims.dto.OTReservationDto;
import com.param.global.otims.dto.OtSchedulingDto;
import com.param.global.otims.dto.ProcedureOTScheduleDto;
import com.param.global.otims.dto.ProcedureOrderMasterDto;
import com.param.global.otims.dto.ProcedureSchedulingDto;
import com.param.otims.model.ProcedureOTSchedule;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class ProcedureOTScheduleDaoImpl extends GenericDao< ProcedureOTSchedule, Integer>implements IProcedureOTScheduleDao, ICommonConstants{
	final static Logger logger = Logger.getLogger(ProcedureOTScheduleDaoImpl.class);


	public ProcedureOTScheduleDaoImpl() {
		super(ProcedureOTSchedule.class);
	}

	
	@Override
	public Response saveProcedureOTSchedule(ProcedureOTScheduleDto procedureOTScheduleDto) throws ApplicationException {
		try{
			LocalTime fromTime1 = LocalTime.parse(procedureOTScheduleDto.getFromTime());
			LocalDate today = LocalDate.now();
			Instant fromTime = fromTime1.atDate(today).atZone(ZoneId.of("UTC")).toInstant();

			LocalTime toTime1 = LocalTime.parse(procedureOTScheduleDto.getToTime());
			Instant toTime = toTime1.atDate(today).atZone(ZoneId.of("UTC")).toInstant();

					if(procedureOTScheduleDto.getProcedureOtScheduleId()!=null) {
						ProcedureOTSchedule mstr = findById(procedureOTScheduleDto.getProcedureOtScheduleId());
						 mstr.setFromTime(fromTime);
						 mstr.setProdeureDate(GlobalCommonDateUtils.getDate(procedureOTScheduleDto.getProdeureDate(), "dd-M-yyyy HH:mm:ss"));
						 mstr.setToTime(toTime);
						 mstr.setColor(procedureOTScheduleDto.getColor());
						 mstr.setProcedureScheduleId(procedureOTScheduleDto.getProcedureScheduleId());
						 mstr.setRemark(procedureOTScheduleDto.getRemark());
						 mstr.setStatus(procedureOTScheduleDto.getStatus());
						sessionFactory.getCurrentSession().update(mstr);
					}else {

						@SuppressWarnings("unused")
						Integer result = sessionFactory.getCurrentSession().createSQLQuery("update otims.t_procedure_ot_schedule set status = 'I' "  
									 		+ "where pro_req_id ="+procedureOTScheduleDto.getProReqId())
								.executeUpdate();
						
						ProcedureOTSchedule mstr = new ProcedureOTSchedule();
						
						 mstr.setProReqId(procedureOTScheduleDto.getProReqId());
						 mstr.setCreatedBy(procedureOTScheduleDto.getCreatedBy());
						 mstr.setCreatedDate(TimeZoneComponent.getDateByzone(procedureOTScheduleDto.getUnitId()));
						 mstr.setOrganizationId(procedureOTScheduleDto.getOrganizationId());
						 mstr.setUnitId(procedureOTScheduleDto.getUnitId());
						 mstr.setUpdatedBy(procedureOTScheduleDto.getUpdatedBy());
						 mstr.setUpdatedDate(TimeZoneComponent.getDateByzone(procedureOTScheduleDto.getUnitId()));
						 mstr.setFromTime(fromTime);
						 mstr.setProdeureDate(GlobalCommonDateUtils.getDate(procedureOTScheduleDto.getProdeureDate(), "dd-M-yyyy HH:mm:ss"));
						 mstr.setTableId(procedureOTScheduleDto.getTableId());
						 mstr.setTheatreId(procedureOTScheduleDto.getTheatreId());
						 mstr.setColor(procedureOTScheduleDto.getColor());
						 mstr.setProcedureScheduleId(procedureOTScheduleDto.getProcedureScheduleId());
						 mstr.setToTime(toTime);
						mstr.setStatus('A');
						mstr.setRemark(procedureOTScheduleDto.getRemark());
						sessionFactory.getCurrentSession().save(mstr);
					}
				return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
			}catch(Exception e){
				
				throw e;
			}
	}


	@Override
	public Response checkUsedSlot(OtSchedulingDto otSchedulingDto) throws ApplicationException {
		try {
			if(otSchedulingDto.getProReqId() == null || otSchedulingDto.getProReqId() == 0) {
				List<ProcedureOTScheduleDto> obj = sessionFactory.getCurrentSession()
						.getNamedQuery("TO_CHECK_OT_USED")
						.setParameter("organizationId", otSchedulingDto.getOrgId())
						.setParameter("unitId", otSchedulingDto.getUnitId())
						.setParameter("opThreatre", otSchedulingDto.getTheatreId())
						.setParameter("opTable", otSchedulingDto.getTableId())
						.setParameter("otDate", otSchedulingDto.getOtDate())
						.setParameter("fromTime", otSchedulingDto.getFromTime())
						.setParameter("toTime", otSchedulingDto.getToTime())
						.setResultTransformer(Transformers.aliasToBean(ProcedureOTScheduleDto.class)).list();
				
				return new Response(SUCCESS, SUCCESS_CODE, null, obj, null);
			}else {
				
				List<ProcedureOTScheduleDto> obj = sessionFactory.getCurrentSession()
						.getNamedQuery("TO_CHECK_OT_USED_WITH_PRO_REQ_ID")
						.setParameter("organizationId", otSchedulingDto.getOrgId())
						.setParameter("unitId", otSchedulingDto.getUnitId())
						.setParameter("opThreatre", otSchedulingDto.getTheatreId())
						.setParameter("opTable", otSchedulingDto.getTableId())
						.setParameter("proReqId", otSchedulingDto.getProReqId())
						.setParameter("otDate", otSchedulingDto.getOtDate())
						.setParameter("fromTime", otSchedulingDto.getFromTime())
						.setParameter("toTime", otSchedulingDto.getToTime())
						.setResultTransformer(Transformers.aliasToBean(ProcedureOTScheduleDto.class)).list();
				
				return new Response(SUCCESS, SUCCESS_CODE, null, obj, null);
			}
			
			
			
		} catch (HibernateException he) {
		logger.error("checkUsedSlot failed", he);
		} catch (Exception e) {
		logger.error("checkUsedSlot failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response updateProcedureReqStatus(Integer proReqId, int i , String changeOfDate, String changeOfTime) throws ApplicationException {
		try{

			String timeString = changeOfTime; // Assuming this is a string in HH:mm:ss format
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

			Time preferedTime = null;
			try {
				preferedTime = new Time(sdf.parse(timeString).getTime());
			} catch (ParseException e) {
			logger.error("updateProcedureReqStatus failed", e);
			}
			@SuppressWarnings("unused")
			String sql = "UPDATE otims.t_procedure_req_mst "
					+ "SET procedure_status_id = :statusId, "
					+ "prefered_date_time = :preferedDateTime, "
					+ "prefered_time = :preferedTime "
					+ "WHERE pro_req_id = :proReqId";

			Integer result = sessionFactory.getCurrentSession().createSQLQuery(sql)
					.setParameter("statusId", i)
					.setParameter("preferedDateTime", GlobalCommonDateUtils.getDate(changeOfDate, "dd-M-yyyy HH:mm:ss"))
					.setParameter("preferedTime", preferedTime)
					.setParameter("proReqId", proReqId)
					.executeUpdate();

			String sqlq = "UPDATE otims.t_procedure_order_master "
					+ "SET prefered_date_time = :preferedDateTime, "
					+ "prefered_time = :preferedTime "
					+ "WHERE pro_req_id = :proReqId";

			Integer result1 = sessionFactory.getCurrentSession().createSQLQuery(sqlq)
					.setParameter("preferedDateTime", GlobalCommonDateUtils.getDate(changeOfDate, "dd-M-yyyy HH:mm:ss"))
					.setParameter("preferedTime", preferedTime)
					.setParameter("proReqId", proReqId)
					.executeUpdate();

			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, result1);
		}catch(Exception e){
			
			throw e;
		}
	}
	
	@Override
	public Response getProcedureOTScheduleById(ProcedureSchedulingDto procedureSchedulingDto) throws ApplicationException {
		try{
			List<ProcedureOTScheduleDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PROCEDURE_OT_SCHEDULE_BY_PRO_SCHEDULE_ID")
					.setParameter("organizationId", procedureSchedulingDto.getOrganizationId())
					.setParameter("unitId", procedureSchedulingDto.getUnitId())
					.setParameter("procedureScheduleId", procedureSchedulingDto.getProcedureScheduleId())
					.setResultTransformer(Transformers.aliasToBean(ProcedureOTScheduleDto.class)).list();
				return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
			}catch(Exception e){
				
				throw e;
			}
	}


	@Override
	public Response saveProcedureOTSchedule(OTReservationDto oTReservationdto) throws ApplicationException {
		try{	
			Integer count = sessionFactory.getCurrentSession().createSQLQuery(" update otims.t_procedure_ot_schedule set status = 'I' "
					+ " where pro_req_id =:proReqId  ")
					.setParameter("proReqId",oTReservationdto.getProReqId())
					.executeUpdate();
			if(count>0) {
				
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
			}else {
				return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}
		
			
		}catch (HibernateException he) {
		logger.error("saveProcedureOTSchedule failed", he);
		} catch (Exception e) {
		logger.error("saveProcedureOTSchedule failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getOtCheckList(ProcedureSchedulingDto dto) throws ApplicationException {
		try{
			StringBuilder query = new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("SEARCH_OT_CHECK_LIST").getQueryString());
			if(dto.getVisitTypeId()!=0) {
				query.append(" and prm.visit_type_id =:visitTypeId ");
			}
			Query createQuery   = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
					.setParameter("unitId", dto.getUnitId())
					.setParameter("organizationId", dto.getOrganizationId())
					.setParameter("fromDate", dto.getFromDate())
					.setParameter("toDate", dto.getToDate());
			if(dto.getVisitTypeId()!=0) {
				createQuery.setParameter("visitTypeId",dto.getVisitTypeId());
			}
			List<ProcedureSchedulingDto> list = createQuery.setResultTransformer(Transformers.aliasToBean(ProcedureSchedulingDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		}catch(Exception e){
			
			throw e;
		}
	}


	@Override
	public Response getPreOparetiveDeta(ProcedureSchedulingDto dto) throws ApplicationException {
		try{
			List<ProcedureOrderMasterDto> list = sessionFactory.getCurrentSession().getNamedQuery("GET_PROCEDURE_ORDER_MASTER_BY_PRO_REQ_ID").setParameter("orderId", dto.getProReqId()).setResultTransformer(Transformers.aliasToBean(ProcedureOrderMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		}catch(Exception e){
			
			throw e;
		}
	}


	@Override
	public Response getPreOparetiveCheckListDeta(Integer proReqId) throws ApplicationException {
		try{
			List<ChecklistMasterDto> list = sessionFactory.getCurrentSession().getNamedQuery("GET_PRE_CHECKLIST_BY_PRO_REQ_ID").setParameter("proReqId", proReqId).setResultTransformer(Transformers.aliasToBean(ChecklistMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		}catch(Exception e){
			
			throw e;
		}
	}


	@Override
	public Response getWheelInDataByProReqId(Integer proReqId) throws ApplicationException {
		try{
			ProcedureOrderMasterDto obj = (ProcedureOrderMasterDto) sessionFactory.getCurrentSession().getNamedQuery("GET_WHEEL_IN_DATA_BY_PRO_REQ_ID").setParameter("proReqId", proReqId).setResultTransformer(Transformers.aliasToBean(ProcedureOrderMasterDto.class)).uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, obj);
		}catch(Exception e){
			
			throw e;
		}
	}
}
