package com.param.otims.dao;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.otims.dto.OTReservationDto;
import com.param.global.otims.dto.ProcedureSchedulingDto;
import com.param.global.otims.dto.ProcedureStaffScheduleDto;
import com.param.otims.model.OTIMSDashboard;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
@Repository
@SuppressWarnings({"rawtypes","unused","unchecked"})
public class OTIMSDashboardDaoImpl extends GenericDao<OTIMSDashboard, Integer>implements IOTIMSDashboardDao, ICommonConstants{

	private static final Logger logger = Logger.getLogger(OTIMSDashboardDaoImpl.class);

	public OTIMSDashboardDaoImpl() {
		super(OTIMSDashboard.class);
	}

	@Override
	public Response saveProcedureSchedule(ProcedureSchedulingDto procedureSchedulingDto) throws ApplicationException {
		try{
			LocalTime preferTime1 = LocalTime.parse( procedureSchedulingDto.getPreferTime());
			LocalDate today = LocalDate.now();
			Instant preferTime = preferTime1.atDate(today).atZone(ZoneId.of("UTC")).toInstant();

					if(procedureSchedulingDto.getProcedureScheduleId()!=null) {
						OTIMSDashboard mstr = findById(procedureSchedulingDto.getProcedureScheduleId());
						 mstr.setScheduleDate(GlobalCommonDateUtils.getDate(procedureSchedulingDto.getScheduleDate(), "dd-M-yyyy HH:mm:ss"));
						 mstr.setProReqId(procedureSchedulingDto.getProReqId());
						 mstr.setPreferTime(preferTime);
						 mstr = update(mstr);
						 return new Response<>(SUCCESS, SUCCESS_CODE, null, null, mstr);
					}else {
						
						 OTIMSDashboard mstr = new OTIMSDashboard();
						 mstr.setProReqId(procedureSchedulingDto.getProReqId());
						 mstr.setCreatedBy(procedureSchedulingDto.getCreatedBy());
						 mstr.setCreatedDate(procedureSchedulingDto.getCreatedDate() != null && !procedureSchedulingDto.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(procedureSchedulingDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(procedureSchedulingDto.getUnitId()));
						 mstr.setOrganizationId(procedureSchedulingDto.getOrganizationId());
						 mstr.setUnitId(procedureSchedulingDto.getUnitId());
						 mstr.setUpdatedBy(procedureSchedulingDto.getUpdatedBy());
						 mstr.setUpdatedDate(procedureSchedulingDto.getUpdatedDate() != null && !procedureSchedulingDto.getUpdatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(procedureSchedulingDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(procedureSchedulingDto.getUnitId()));
						 mstr.setScheduleDate(GlobalCommonDateUtils.getDate(procedureSchedulingDto.getScheduleDate(), "dd-M-yyyy HH:mm:ss"));
						 mstr.setDepartment(procedureSchedulingDto.getDepartment());
						 mstr.setDepartmentId(procedureSchedulingDto.getDepartmentId());
						 mstr.setOtType(procedureSchedulingDto.getOtType());
						 mstr.setProceduerStatusId(procedureSchedulingDto.getProceduerStatusId());
						 mstr.setProcedureName(procedureSchedulingDto.getProcedureName());
						 mstr.setProcedureType(procedureSchedulingDto.getProcedureType());
						 mstr.setTotalDuration(procedureSchedulingDto.getTotalDuration());
						 mstr.setPreferTime(preferTime);
						mstr.setStatus('A');
						mstr.setOtTransferStatusId(null);
						mstr = save(mstr);
						return new Response<>(SUCCESS, SUCCESS_CODE, null, null, mstr);
					}
				
			}catch(Exception e){
				
				throw e;
			}
	}

	@Override
	public Response inActivePlannedProcedure(Integer proReqId) throws ApplicationException {
		try{
			
			
			Integer result = sessionFactory.getCurrentSession().createSQLQuery("update otims.t_procedure_schedule set status ='I' "  
						 		+ "where pro_req_id ="+proReqId).executeUpdate();
			
			Integer result1 = sessionFactory.getCurrentSession().createSQLQuery("update otims.t_procedure_ot_schedule  set status ='I' "  
			 		+ "where pro_req_id ="+proReqId).executeUpdate();
			
			Integer result2 = sessionFactory.getCurrentSession().createSQLQuery("update otims.t_procedure_staff_schedule  set status ='I' "  
			 		+ "where pro_req_id ="+proReqId).executeUpdate();
			
			Integer result3 = sessionFactory.getCurrentSession().createSQLQuery("update otims.t_procedure_equipment_schedule   set status ='I' "  
			 		+ "where pro_req_id ="+proReqId).executeUpdate();
	
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, result);
		}catch(Exception e){
			
			throw e;
		}
		
	}

	@Override
	public Response getSurgeriesOfTheDayList(ProcedureSchedulingDto procedureSchedulingDto)
			throws ApplicationException {
		try
		{
			String query=sessionFactory.getCurrentSession().getNamedQuery("GET_SURGERIES_OF_THE_DAY_LIST").getQueryString();
			List<ProcedureSchedulingDto> list = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("unitId", procedureSchedulingDto.getUnitId())
					.setParameter("organizationId", procedureSchedulingDto.getOrganizationId())
					.setParameter("createdDate", procedureSchedulingDto.getCreatedDate())
					.setResultTransformer(Transformers.aliasToBean(ProcedureSchedulingDto.class)).list();
		
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getProcedureSchedulingSurgeonList(ProcedureStaffScheduleDto dto) throws ApplicationException {
		try
		{
			String query=sessionFactory.getCurrentSession().getNamedQuery("GET_PROCEDURE_SCHEDULE_SURGEON_LIST").getQueryString();
			List<ProcedureStaffScheduleDto> list = sessionFactory.getCurrentSession().createQuery(query)
					//.setParameter("unitId", dto.getUnitId())
					//.setParameter("organizationId", dto.getOrganizationId())
					//.setParameter("roleId", dto.getRoleId())
					.setParameter("procedureScheduleId", dto.getProcedureScheduleId())
					.setResultTransformer(Transformers.aliasToBean(ProcedureStaffScheduleDto.class)).list();
		
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response cancelProcedureSchedule(OTReservationDto oTReservationdto) throws ApplicationException {
		try{	
			Integer count = sessionFactory.getCurrentSession().createSQLQuery(" update otims.t_procedure_schedule set status = 'I' "
					+ " where pro_req_id ="+oTReservationdto.getProReqId()+" ")
					.executeUpdate();
			if(count>0) {
				
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
			}else {
				return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}


		}catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getProcedureSchedule(ProcedureSchedulingDto procedureSchedulingDto) throws ApplicationException {
		try{
			OTIMSDashboard  mstr = findById(procedureSchedulingDto.getProcedureScheduleId());

				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, mstr);

		}catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getSchedulingByProReqId(ProcedureSchedulingDto dto) throws ApplicationException {
		try{
			ProcedureSchedulingDto dtoObj = (ProcedureSchedulingDto) sessionFactory.getCurrentSession().createSQLQuery(" select  to_char(proSche.schedule_date, 'dd-MM-yyyy HH:ss:mm')  as \"oldDate\","
					+ " to_char(proSche.prefer_time, 'HH:ss:mm') as \"oldTime\" from  otims.t_procedure_schedule proSche"
					+ " where proSche.pro_req_id ="+dto.getProReqId()+" and proSche.status= 'I' limit 1 ")
					.setResultTransformer(Transformers.aliasToBean(ProcedureSchedulingDto.class)).uniqueResult();

				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, dtoObj);

		}catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getOtDeatils(OTReservationDto oTReservationdto) throws ApplicationException {
		try
        {
			OTReservationDto assetList = (OTReservationDto)sessionFactory.getCurrentSession().createSQLQuery(
					 " select prosxch.procedure_name as \"procedureName\", " + 
					 " ther.theatre_name  as \"tableName\" ," + 
					 " concat(coalesce(pre.prefix_code,''),' ',patReg.first_name ,'  ',patReg.last_name) as \"patientName\" , "+
					 " patReg.patient_id as \"patientId\", " + 
					 " patReg.unit_id as \"unitId\", " + 
					 " proreq.org_id as \"organizationId\", " + 
					 " to_char(prosxch.schedule_date,'YYYY-MM-DD')  as \"scheduleDate\" " + 
					 " from otims.t_procedure_schedule prosxch " + 
					 " inner join otims.t_procedure_req_mst proreq on proreq.pro_req_id = prosxch.pro_req_id " + 
					 " inner join patient.t_patient_registration patReg on patReg.patient_id = proreq.patient_id " + 
					 " left join m_prefix_master pre on pre.prefix_id = patReg.prefix_id "+
					 " inner join otims.t_procedure_ot_schedule otsch on otsch.procedure_schedule_id = prosxch.procedure_schedule_id " + 
					 " inner join otims.m_operation_theatre ther on ther.operation_theatre_id = otsch.theatre_id " + 
					 " where prosxch.pro_req_id ="+oTReservationdto.getProReqId() )
				.setResultTransformer(Transformers.aliasToBean(OTReservationDto.class)).uniqueResult();
			
				return new Response<>(SUCCESS, SUCCESS_CODE, null, null, assetList);

        } catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage());
        }
	}

	@Override
	public Response getProcedureSchedulingSurgeonListForCalender(ProcedureStaffScheduleDto dto)
			throws ApplicationException {
		try
		{
			String query=sessionFactory.getCurrentSession().getNamedQuery("GET_PROCEDURE_SCHEDULE_SURGEON_LIST_FOR_CALENDER").getQueryString();
			List<ProcedureStaffScheduleDto> list = sessionFactory.getCurrentSession().createQuery(query)
					.setParameter("proReqId", dto.getProReqId())
					.setResultTransformer(Transformers.aliasToBean(ProcedureStaffScheduleDto.class)).list();
		
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

		} catch (Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}


}
