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
import com.param.global.otims.dto.EquipmentSchedulingDto;
import com.param.global.otims.dto.OTReservationDto;
import com.param.global.otims.dto.ProcedureEquipmentScheduleDto;
import com.param.global.otims.dto.ProcedureSchedulingDto;
import com.param.otims.model.ProcedureEquipmentSchedule;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class ProcedureEquipmentScheduleDaoImpl extends GenericDao< ProcedureEquipmentSchedule, Integer>implements IProcedureEquipmentScheduleDao, ICommonConstants{
	final static Logger logger = Logger.getLogger(ProcedureEquipmentScheduleDaoImpl.class);


	public ProcedureEquipmentScheduleDaoImpl() {
		super(ProcedureEquipmentSchedule.class);
	}

	@Override
	public Response saveProcedureEquipmentSchedule(ProcedureEquipmentScheduleDto procedureEquipmentScheduleDto) throws ApplicationException {
		try{

			LocalTime fromTime1 = LocalTime.parse(procedureEquipmentScheduleDto.getFromTime());
			LocalDate today = LocalDate.now();
			Instant fromTime = fromTime1.atDate(today).atZone(ZoneId.of("UTC")).toInstant();

			LocalTime toTime1 = LocalTime.parse(procedureEquipmentScheduleDto.getToTime());
			Instant toTime = toTime1.atDate(today).atZone(ZoneId.of("UTC")).toInstant();

					if(procedureEquipmentScheduleDto.getProcedureEquipmentScheduleId()!=null) {
						ProcedureEquipmentSchedule mstr = findById(procedureEquipmentScheduleDto.getProcedureEquipmentScheduleId());
						 mstr.setFromTime(fromTime);
						 mstr.setStatus(procedureEquipmentScheduleDto.getStatus());
						 mstr.setColor(procedureEquipmentScheduleDto.getColor());
						 mstr.setProcedureScheduleId(procedureEquipmentScheduleDto.getProcedureScheduleId());
						 mstr.setProdeureDate(GlobalCommonDateUtils.getDate(procedureEquipmentScheduleDto.getProdeureDate(), "dd-M-yyyy HH:mm:ss"));
						 mstr.setRemark(procedureEquipmentScheduleDto.getRemark());
						 mstr.setToTime(toTime);
						sessionFactory.getCurrentSession().update(mstr);
					}else {
						
						@SuppressWarnings("unused")
						Integer result = sessionFactory.getCurrentSession().createSQLQuery("update otims.t_procedure_equipment_schedule set status = 'I' "  
									 		+ "where pro_req_id ="+procedureEquipmentScheduleDto.getProReqId())
								.executeUpdate();
						
						ProcedureEquipmentSchedule mstr = new ProcedureEquipmentSchedule();
						
						 mstr.setProReqId(procedureEquipmentScheduleDto.getProReqId());
						 mstr.setCreatedBy(procedureEquipmentScheduleDto.getCreatedBy());
						 mstr.setCreatedDate(procedureEquipmentScheduleDto.getCreatedDate() != null && !procedureEquipmentScheduleDto.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(procedureEquipmentScheduleDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(procedureEquipmentScheduleDto.getUnitId()));
						 mstr.setOrganizationId(procedureEquipmentScheduleDto.getOrganizationId());
						 mstr.setUnitId(procedureEquipmentScheduleDto.getUnitId());
						 mstr.setUpdatedBy(procedureEquipmentScheduleDto.getUpdatedBy());
						 mstr.setUpdatedDate(procedureEquipmentScheduleDto.getUpdatedDate() != null && !procedureEquipmentScheduleDto.getUpdatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(procedureEquipmentScheduleDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(procedureEquipmentScheduleDto.getUnitId()));
						 mstr.setFromTime(fromTime);
						 mstr.setProdeureDate(GlobalCommonDateUtils.getDate(procedureEquipmentScheduleDto.getProdeureDate(), "dd-M-yyyy HH:mm:ss"));
						 mstr.setItemId(procedureEquipmentScheduleDto.getItemId());
						 mstr.setBatchId(procedureEquipmentScheduleDto.getBatchId());
						 mstr.setProcedureId(procedureEquipmentScheduleDto.getProcedureId());
						 mstr.setAssetCatId(procedureEquipmentScheduleDto.getAssetCatId());
						 mstr.setToTime(toTime);
						 mstr.setColor(procedureEquipmentScheduleDto.getColor());
						 mstr.setProcedureScheduleId(procedureEquipmentScheduleDto.getProcedureScheduleId());
						mstr.setStatus('A');
						mstr.setRemark(procedureEquipmentScheduleDto.getRemark());
						sessionFactory.getCurrentSession().save(mstr);
					}
				return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
			}catch(Exception e){
				
				throw e;
			}
			
	}

	@Override
	public Response checkEquipmentUsedSlot(EquipmentSchedulingDto dto) throws ApplicationException {
		try {
			
			if(dto.getProReqId() == null || dto.getProReqId() == 0) {
				
				List<ProcedureEquipmentScheduleDto> list = sessionFactory.getCurrentSession()
						.getNamedQuery("TO_CHECK_EQUIPMENT_USED")
						.setParameter("organizationId", dto.getOrgId())
						.setParameter("unitId", dto.getUnitId())
						.setParameter("itemId", dto.getItemId())
						.setParameter("batchId", dto.getBatchId())
						.setParameter("equDate", dto.getEquipmentSchedulingDate())
						.setParameter("fromTime", dto.getFromTime())
						.setParameter("toTime", dto.getToTime())
						.setResultTransformer(Transformers.aliasToBean(ProcedureEquipmentScheduleDto.class)).list();
					return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
				
			}else {
				
				List<ProcedureEquipmentScheduleDto> list = sessionFactory.getCurrentSession()
						.getNamedQuery("TO_CHECK_EQUIPMENT_USED_WITH_PRO_REQ_ID")
						.setParameter("organizationId", dto.getOrgId())
						.setParameter("unitId", dto.getUnitId())
						.setParameter("itemId", dto.getItemId())
						.setParameter("proReqId", dto.getProReqId())
						.setParameter("batchId", dto.getBatchId())
						.setParameter("equDate", dto.getEquipmentSchedulingDate())
						.setParameter("fromTime", dto.getFromTime())
						.setParameter("toTime", dto.getToTime())
						.setResultTransformer(Transformers.aliasToBean(ProcedureEquipmentScheduleDto.class)).list();
					return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
				
			}
			
			
		} catch (HibernateException he) {
		logger.error("checkEquipmentUsedSlot failed", he);
		} catch (Exception e) {
		logger.error("checkEquipmentUsedSlot failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response checkEquipmentMaintenamceStatus(EquipmentSchedulingDto dto)
			throws ApplicationException {
		try {
			List<EquipmentSchedulingDto> list = sessionFactory.getCurrentSession()
				.getNamedQuery("TO_CHECK_EQUIPMENT_MAINTENANCE_STATUS")
				.setParameter("organizationId", dto.getOrgId())
				.setParameter("unitId", dto.getUnitId())
				.setParameter("itemId", dto.getItemId())
				.setParameter("batchId", dto.getBatchId())
//				.setParameter("equDate", dto.getEquipmentSchedulingDate())
				.setResultTransformer(Transformers.aliasToBean(EquipmentSchedulingDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
			
		} catch (HibernateException he) {
		logger.error("checkEquipmentMaintenamceStatus failed", he);
		} catch (Exception e) {
		logger.error("checkEquipmentMaintenamceStatus failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getProcedureEquipmentScheduleById(ProcedureSchedulingDto procedureSchedulingDto)
			throws ApplicationException {
		try{
			List<ProcedureEquipmentScheduleDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PROCEDURE_EQUIPMENT_SCHEDULE_BY_PRO_SCHEDULE_ID")
					.setParameter("organizationId", procedureSchedulingDto.getOrganizationId())
					.setParameter("unitId", procedureSchedulingDto.getUnitId())
					.setParameter("procedureScheduleId", procedureSchedulingDto.getProcedureScheduleId())
					.setResultTransformer(Transformers.aliasToBean(ProcedureEquipmentScheduleDto.class)).list();
				return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
			}catch(Exception e){
				
				throw e;
			}
	}

	@Override
	public Response saveProcedureEquipmentSchedule(OTReservationDto oTReservationdto) throws ApplicationException {
		try{	
			Integer count = sessionFactory.getCurrentSession().createSQLQuery(" update otims.t_procedure_equipment_schedule set status = 'I' "
					+ " where pro_req_id =:proReqId  ")
					.setParameter("proReqId",oTReservationdto.getProReqId())
					.executeUpdate();
			if(count>0) {
				
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
			}else {
				return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}
		
			
		}catch (HibernateException he) {
		logger.error("saveProcedureEquipmentSchedule failed", he);
		} catch (Exception e) {
		logger.error("saveProcedureEquipmentSchedule failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
}
