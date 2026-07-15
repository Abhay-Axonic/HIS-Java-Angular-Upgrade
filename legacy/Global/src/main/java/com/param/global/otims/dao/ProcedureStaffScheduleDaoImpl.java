package com.param.global.otims.dao;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.otims.dto.OTReservationDto;
import com.param.global.otims.dto.ProcedureSchedulingDto;
import com.param.global.otims.dto.ProcedureStaffScheduleDto;
import com.param.global.otims.dto.StaffSchedulingDto;
import com.param.otims.model.ProcedureStaffSchedule;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class ProcedureStaffScheduleDaoImpl extends GenericDao<ProcedureStaffSchedule, Integer>implements IProcedureStaffScheduleDao, ICommonConstants{
	final static Logger logger = Logger.getLogger(ProcedureStaffScheduleDaoImpl.class);


	public ProcedureStaffScheduleDaoImpl() {
		super(ProcedureStaffSchedule.class);
	}

	@Override
	public Response saveProcedureStaffSchedule(ProcedureStaffScheduleDto procedureStaffScheduleDto) throws ApplicationException {
		try{

			LocalTime fromTime1 = LocalTime.parse(procedureStaffScheduleDto.getFromTime());
			LocalDate today = LocalDate.now();
			Instant fromTime = fromTime1.atDate(today).atZone(ZoneId.of("UTC")).toInstant();

			LocalTime toTime1 = LocalTime.parse(procedureStaffScheduleDto.getToTime());
			Instant toTime = toTime1.atDate(today).atZone(ZoneId.of("UTC")).toInstant();

					if(procedureStaffScheduleDto.getProcedureStaffScheduleId()!=null) {
						
						ProcedureStaffSchedule mstr = findById(procedureStaffScheduleDto.getProcedureStaffScheduleId());
						 mstr.setFromTime(fromTime);
						 mstr.setStatus(procedureStaffScheduleDto.getStatus());
						 mstr.setColor(procedureStaffScheduleDto.getColor());
						 mstr.setProcedureScheduleId(procedureStaffScheduleDto.getProcedureScheduleId());
						 mstr.setRemark(procedureStaffScheduleDto.getRemark());
						 mstr.setProdeureDate(GlobalCommonDateUtils.getDate(procedureStaffScheduleDto.getProdeureDate(), "dd-M-yyyy HH:mm:ss"));
						 mstr.setToTime(toTime);
						sessionFactory.getCurrentSession().update(mstr);
						
					}else {
						
						@SuppressWarnings("unused")
						Integer result = sessionFactory.getCurrentSession().createSQLQuery("update otims.t_procedure_staff_schedule set status = 'I' "  
									 		+ "where pro_req_id ="+procedureStaffScheduleDto.getProReqId())
								.executeUpdate();
						
						ProcedureStaffSchedule mstr = new ProcedureStaffSchedule();
					
						 mstr.setProReqId(procedureStaffScheduleDto.getProReqId());
						 mstr.setCreatedBy(procedureStaffScheduleDto.getCreatedBy());
						 mstr.setCreatedDate(TimeZoneComponent.getDateByzone(procedureStaffScheduleDto.getUnitId()));
						 mstr.setOrganizationId(procedureStaffScheduleDto.getOrganizationId());
						 mstr.setUnitId(procedureStaffScheduleDto.getUnitId());
						 mstr.setUpdatedBy(procedureStaffScheduleDto.getUpdatedBy());
						 mstr.setUpdatedDate(TimeZoneComponent.getDateByzone(procedureStaffScheduleDto.getUnitId()));
						 mstr.setFromTime(fromTime);
						 mstr.setProdeureDate(GlobalCommonDateUtils.getDate(procedureStaffScheduleDto.getProdeureDate(), "dd-M-yyyy HH:mm:ss"));
						 mstr.setRoleId(procedureStaffScheduleDto.getRoleId());
						 mstr.setUserId(procedureStaffScheduleDto.getUserId());
						 mstr.setProcedureId(procedureStaffScheduleDto.getProcedureId());
						 mstr.setToTime(toTime);
						 mstr.setColor(procedureStaffScheduleDto.getColor());
						 mstr.setProcedureScheduleId(procedureStaffScheduleDto.getProcedureScheduleId());
						mstr.setStatus('A');
						mstr.setRemark(procedureStaffScheduleDto.getRemark());
						sessionFactory.getCurrentSession().save(mstr);
					}
				return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
			}catch(Exception e){
				
				throw e;
			}
			
	}

	@Override
	public Response checkStaffUsedSlot(StaffSchedulingDto dto) throws ApplicationException {
		try {
			
			if(dto.getProReqId() == null ||  dto.getProReqId().equals(0)) {
				List<ProcedureStaffScheduleDto> obj = sessionFactory.getCurrentSession()
						.getNamedQuery("TO_CHECK_USED_STAFF_SLOT")
						.setParameter("organizationId", dto.getOrgId())
						.setParameter("unitId", dto.getUnitId())
						.setParameter("userId", dto.getUserId())
						.setParameter("roleId", dto.getRoleId())
						.setParameter("staffScheDate", dto.getStaffSchedulingDate())
						.setParameter("fromTime", dto.getFromTime())
						.setParameter("toTime", dto.getToTime())
						.setResultTransformer(Transformers.aliasToBean(ProcedureStaffScheduleDto.class)).list();
					return new Response(SUCCESS, SUCCESS_CODE, null, obj,null );
			}else {
				List<ProcedureStaffScheduleDto> obj = sessionFactory.getCurrentSession()
						.getNamedQuery("TO_CHECK_USED_STAFF_SLOT_WITH_PRO_REQ_ID")
						.setParameter("organizationId", dto.getOrgId())
						.setParameter("unitId", dto.getUnitId())
						.setParameter("userId", dto.getUserId())
						.setParameter("proReqId", dto.getProReqId())
						.setParameter("roleId", dto.getRoleId())
						.setParameter("staffScheDate", dto.getStaffSchedulingDate())
						.setParameter("fromTime", dto.getFromTime())
						.setParameter("toTime", dto.getToTime())
						.setResultTransformer(Transformers.aliasToBean(ProcedureStaffScheduleDto.class)).list();
					return new Response(SUCCESS, SUCCESS_CODE, null, obj,null );
				
			
			}
			
			
		} catch (HibernateException he) {
		logger.error("checkStaffUsedSlot failed", he);
		} catch (Exception e) {
		logger.error("checkStaffUsedSlot failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getProcedureStaffScheduleById(ProcedureSchedulingDto procedureSchedulingDto)
			throws ApplicationException {
		try{
			List<ProcedureStaffScheduleDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PROCEDURE_STAFF_SCHEDULE_BY_PRO_SCHEDULE_ID")
					.setParameter("organizationId", procedureSchedulingDto.getOrganizationId())
					.setParameter("unitId", procedureSchedulingDto.getUnitId())
					.setParameter("procedureScheduleId", procedureSchedulingDto.getProcedureScheduleId())
					.setResultTransformer(Transformers.aliasToBean(ProcedureStaffScheduleDto.class)).list();
				return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
			}catch(Exception e){
				
				throw e;
			}
	}

	@Override
	public Response saveProcedureStaffSchedule(OTReservationDto oTReservationdto) throws ApplicationException {
		try{	
			Integer count = sessionFactory.getCurrentSession().createSQLQuery(" update otims.t_procedure_staff_schedule set status = 'I' "
					+ " where pro_req_id =:proReqId  ")
					.setParameter("proReqId",oTReservationdto.getProReqId())
					.executeUpdate();
			if(count>0) {
				
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
			}else {
				return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}
		
			
		}catch (HibernateException he) {
		logger.error("saveProcedureStaffSchedule failed", he);
		} catch (Exception e) {
		logger.error("saveProcedureStaffSchedule failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getotimsRoleByProReqId(OTReservationDto oTReservationdto) throws ApplicationException {
		try
        {
			List<ProcedureStaffScheduleDto> assetList = sessionFactory.getCurrentSession().createSQLQuery(
					 " select emp.email as \"emailId\",emp.first_name as \"staffName\", proSff.role_id as \"roleId\" ,"
					 + " to_char(proSff.prodeure_date,'YYYY-MM-DD') as \"prodeureDate\", "
					 + " to_char(proSff.from_time,'hh24:mi') as \"fromTime\", "
					 + " to_char(proSff.to_time,'hh24:mi') as \"toTime\", "
					 + "proSff.user_id as \"userId\",usmstr.role_type_id as \"roleTypeId\" " + 
					 " from otims.t_procedure_staff_schedule  proSff " + 
					 " inner  join public.m_employee_master emp on  emp.user_id = proSff.user_id " + 
					 " left  join public.t_user_role_mapper usmppr  on usmppr.user_id =  proSff.user_id " + 
					 " left join  public.m_role_master usmstr on usmppr.role_id = usmstr.role_id   " + 
					 " where usmstr.role_type_id in(8,9,10,11,12) and usmppr.status = 'A' and proSff.pro_req_id ="+oTReservationdto.getProReqId() + 
					 " union all " + 
					 " select doc.email as \"emailId\",doc.first_name as \"staffName\" , proSff.role_id as \"roleId\","+
					 " to_char(proSff.prodeure_date,'YYYY-MM-DD') as \"prodeureDate\", "+
					 " to_char(proSff.from_time,'hh24:mi') as \"fromTime\", "+
					 " to_char(proSff.to_time,'hh24:mi') as \"toTime\", "+
					 " proSff.user_id as \"userId\","+
					 " usmstr.role_type_id as \"roleTypeId\" " + 
					 " from otims.t_procedure_staff_schedule  proSff " + 
					 " inner  join doctor.m_doctor_master doc on  doc.user_id = proSff.user_id " + 
					 " left  join public.t_user_role_mapper usmppr  on usmppr.user_id =  proSff.user_id " + 
					 " left join  public.m_role_master usmstr on usmppr.role_id = usmstr.role_id    " + 
					 " where usmstr.role_type_id in(8,9,10,11,12) and usmppr.status = 'A' and proSff.pro_req_id ="+oTReservationdto.getProReqId() )
				.setResultTransformer(Transformers.aliasToBean(ProcedureStaffScheduleDto.class)).list();
			
				return new Response<>(SUCCESS, SUCCESS_CODE, null, assetList, null);
		
        } catch (Exception e)
        {
        logger.error("getotimsRoleByProReqId failed", e);
        }
        return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getRoleTypeByRoleId(ProcedureStaffScheduleDto procedureStaffScheduleDto)
			throws ApplicationException {
		try
        {
			ProcedureStaffScheduleDto assetList = (ProcedureStaffScheduleDto)sessionFactory.getCurrentSession().createSQLQuery(
					 "select role_type_id as \"roleTypeId\" from public.m_role_master where role_id =:roleId  limit 1")
					.setParameter("roleId",procedureStaffScheduleDto.getRoleId())
				.setResultTransformer(Transformers.aliasToBean(ProcedureStaffScheduleDto.class)).uniqueResult();
			
				return new Response<>(SUCCESS, SUCCESS_CODE, null, null, assetList);
		
        } catch (Exception e)
        {
        logger.error("getRoleTypeByRoleId failed", e);
        }
        return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}
}
