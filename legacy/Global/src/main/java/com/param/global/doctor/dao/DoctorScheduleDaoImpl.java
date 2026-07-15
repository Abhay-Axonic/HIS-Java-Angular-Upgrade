package com.param.global.doctor.dao;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.MyLifeConnectionLinkDto;
import com.param.global.dto.ScheduleLogsDto;
import com.param.global.dto.ScheduleMasterDto;
import com.param.global.dto.sync.DoctorChargesSyncDto;
import com.param.global.dto.sync.DoctorScheduleSyncDto;
import com.param.global.model.DoctorSchedule;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class DoctorScheduleDaoImpl extends GenericDao<DoctorSchedule, Integer> implements IDoctorScheduleDao, ICommonConstants{

	private static final Logger logger = Logger.getLogger(DoctorScheduleDaoImpl.class);

	public DoctorScheduleDaoImpl() {
		super(DoctorSchedule.class);
	}
	
	@Autowired
	HikariDataSource driverManager;

	@Override
	public Response getDoctorScheduleForSync(int id) throws ApplicationException {
		try{
			DoctorScheduleSyncDto dto = (DoctorScheduleSyncDto)sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SCHEDULE_TO_SYNC_BY_ID")
					.setParameter("id", id)
					.setParameter("currentDate", GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(0), "yyyy-MM-dd"))
					.setResultTransformer(Transformers.aliasToBean(DoctorScheduleSyncDto.class))
					.uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, dto);
		}catch(Exception e){
			
			throw e;
		}
	}

	@SuppressWarnings({ "deprecation"})
	@Override
	public Response saveDoctorSchedule(List<ScheduleMasterDto> scheduleMasterDtosList,String action) throws ApplicationException {
		Connection connection =null;
		PreparedStatement statement = null;
		try{
			connection = driverManager.getConnection();
			connection.setAutoCommit(false);
			
			Boolean isSyncScheduling=(Boolean)sessionFactory.getCurrentSession()
					.createSQLQuery("  select om.is_sync_scheduling as \"isSyncScheduling\" from public.m_organization_master om where  om.organization_id=:organizationId and om.status='A' ")
					.setParameter("organizationId", scheduleMasterDtosList.get(0).getOrganisationId())
					.uniqueResult();
			
			if(isSyncScheduling == true) {
				if(action.equals("INSERT"))
				{
					 statement = connection.prepareStatement("INSERT INTO doctor.m_doctor_schedule "
							+ " (doctor_id, speciality_id, schedule_date, from_time, to_time,created_by,created_date, unit_id, org_id,time_per_slot,add_remove) "
							+ " VALUES(?, ?, ?, ? , ? , ? , ? ,? ,? ,?, 0 );"); 
					int i = 0;
					for(ScheduleMasterDto dto : scheduleMasterDtosList)
					{
						LocalTime fromTime = Instant.parse(dto.getFromTime())
								.atZone(ZoneId.of("UTC"))
								.toLocalTime();

						LocalTime toTime = Instant.parse(dto.getToTime())
								.atZone(ZoneId.of("UTC"))
								.toLocalTime();

			        	statement.setInt(1, dto.getDoctorId());
			        	statement.setInt(2, dto.getSpecialityId());
			        	statement.setDate(3, new java.sql.Date( GlobalCommonDateUtils.getDate(dto.getScheduleDate(),"yyyy-M-dd").getTime()));
//			        	statement.setTime(4, new Time(LocalTime.parse(dto.getFromTime()).getHour(),LocalTime.parse(dto.getFromTime()).getMinute(),LocalTime.parse(dto.getFromTime()).getSecond()));
//						statement.setTime(5, new Time(LocalTime.parse(dto.getToTime()).getHour(),LocalTime.parse(dto.getToTime()).getMinute(),LocalTime.parse(dto.getToTime()).getSecond()));
						statement.setTime(4, Time.valueOf(fromTime));
						statement.setTime(5, Time.valueOf(toTime));
						statement.setInt(6,dto.getCreatedBy());
						statement.setDate(7,new java.sql.Date(TimeZoneComponent.getDateByzone(dto.getUnitId()).getTime()));
						statement.setInt(8,dto.getUnitId());
			        	statement.setInt(9,dto.getOrganisationId());
			        	statement.setInt(10,dto.getTimePerSlot());

			            statement.addBatch();
			            i++;

			            if (i % 500 == 0 || i == scheduleMasterDtosList.size()) {
			              
			            	try{
			            		statement.executeBatch(); // Execute every 500 items.
			            	}catch(BatchUpdateException e){
			            		throw e.getNextException();
			            	}
			            	connection.commit();  
			            }
					}
				}else{
					for(ScheduleMasterDto dto : scheduleMasterDtosList)
					{
						LocalTime fromTime1 = null;
						LocalTime toTime1 = null;
						LocalDate today = null;

						if(dto.getFromTime() != null) {
							fromTime1 = LocalTime.parse(dto.getFromTime());
							today = LocalDate.now();
						}

						if(dto.getFromTime() != null) {
							toTime1 = LocalTime.parse(dto.getToTime());
							today = LocalDate.now();
						}


						DoctorSchedule schedule = new DoctorSchedule();
							schedule.setCreatedDate(TimeZoneComponent.getDateByzone(dto.getUnitId()));
							schedule.setDoctorId(dto.getDoctorId());
							schedule.setSpecialityId(dto.getSpecialityId());
							schedule.setStatus(ACTIVE);
							schedule.setOrgId(dto.getOrganisationId());
							schedule.setUnitId(dto.getUnitId());
							schedule.setFromTime(dto.getFromTime() != null && ! dto.getFromTime().isEmpty()? fromTime1.atDate(today).atZone(ZoneId.of("UTC")).toInstant() : null);
							schedule.setToTime(dto.getToTime() != null && !dto.getToTime().isEmpty() ? toTime1.atDate(today).atZone(ZoneId.of("UTC")).toInstant() : null);
							if(dto.getScheduleDate()!= null && !dto.getScheduleDate().isEmpty()){
								schedule.setScheduleDate(dto.getScheduleDate() != null && !dto.getScheduleDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getScheduleDate(), "dd-MM-yyyy") : null);
							}
							else{
								schedule.setScheduleDate(dto.getFromDate() != null && !dto.getFromDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getFromDate(), "yyyy-MM-dd") : null);
							}

							schedule.setFromDate(dto.getFromDate() != null && !dto.getFromDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getFromDate(), "yyyy-MM-dd") : null);
							schedule.setToDate(dto.getToDate() != null && !dto.getToDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getToDate(), "yyyy-MM-dd") : null);
							schedule.setAddRemove(dto.getAddRemove());
							schedule.setDeleteAllSlots(dto.getDeleteAllSlots()!=null && dto.getDeleteAllSlots()=='Y' ? true : false);
						schedule = save(schedule);
					}
				}
				return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
			}else {
				return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
			}
		}catch(Exception e){
			logger.error("saveDoctorSchedule failed", e);
		}finally{
			try {
				if (statement != null) {
					statement.close();
				}
				connection.setAutoCommit(true);
				connection.close();
			} catch (SQLException e) {
				logger.error("saveDoctorSchedule failed while closing connection", e);
				return new Response(ERROR, null, null, null,e);
			}
		}
		return new Response(ERROR, null, null, null,null);
	}

	@Override
	public Response getDoctorChargesForSyncByDoctorId(Integer consultionServiceMapperId) throws ApplicationException {
		try{
			List<DoctorChargesSyncDto> doctorChargesSyncDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DOCTOR_CHARGES_TO_SYNC_BY_DOCTOR_ID")
					.setParameter("id", consultionServiceMapperId)
					.setResultTransformer(Transformers.aliasToBean(DoctorChargesSyncDto.class))
					.list();
			List<DoctorChargesSyncDto> finalDoctorChargesSyncDtoList = new ArrayList<DoctorChargesSyncDto>();
		
			
			for(DoctorChargesSyncDto dto : doctorChargesSyncDtoList)
			{
				if(dto.getNewVisitRate()!=null)
				{
					DoctorChargesSyncDto newDto = new DoctorChargesSyncDto();
						newDto.setLocalDoctorId(dto.getLocalDoctorId());
						newDto.setDoctorId(dto.getDoctorId());
						newDto.setHospitalId(dto.getHospitalId());
						newDto.setUnitId(dto.getUnitId());
						newDto.setGlobalOrgId(dto.getGlobalOrgId());
						newDto.setGlobalUnitId(dto.getGlobalUnitId());
						newDto.setApplicationId(2);
						newDto.setVisitTypeId(OP);
						newDto.setCharges(dto.getNewVisitRate());
						newDto.setCurrency("INR");
						newDto.setClassificationId(dto.getClassificationId());
						newDto.setClassificationDesc(dto.getClassificationDesc());
					
					DoctorChargesSyncDto newDto2 = new DoctorChargesSyncDto();
						newDto2.setLocalDoctorId(dto.getLocalDoctorId());
						newDto2.setDoctorId(dto.getDoctorId());
						newDto2.setHospitalId(dto.getHospitalId());
						newDto2.setUnitId(dto.getUnitId());
						newDto2.setGlobalOrgId(dto.getGlobalOrgId());
						newDto2.setGlobalUnitId(dto.getGlobalUnitId());
						newDto2.setApplicationId(2);
						newDto2.setVisitTypeId(ER);
						newDto2.setCharges(dto.getNewVisitRate());
						newDto2.setCurrency("INR");
						newDto2.setClassificationId(dto.getClassificationId());
						newDto2.setClassificationDesc(dto.getClassificationDesc());	
						
					finalDoctorChargesSyncDtoList.add(newDto);
					finalDoctorChargesSyncDtoList.add(newDto2);
				}
				if(dto.getFollowUpVisitRate()!=null)
				{
					DoctorChargesSyncDto newDto = new DoctorChargesSyncDto();
						newDto.setLocalDoctorId(dto.getLocalDoctorId());
						newDto.setDoctorId(dto.getDoctorId());
						newDto.setHospitalId(dto.getHospitalId());
						newDto.setUnitId(dto.getUnitId());
						newDto.setGlobalOrgId(dto.getGlobalOrgId());
						newDto.setGlobalUnitId(dto.getGlobalUnitId());
						newDto.setApplicationId(2);
						newDto.setVisitTypeId(IP);
						newDto.setCharges(dto.getFollowUpVisitRate());
						newDto.setCurrency("INR");
						newDto.setClassificationId(dto.getClassificationId());
						newDto.setClassificationDesc(dto.getClassificationDesc());
						
					DoctorChargesSyncDto newDto2 = new DoctorChargesSyncDto();
						newDto2.setLocalDoctorId(dto.getLocalDoctorId());
						newDto2.setDoctorId(dto.getDoctorId());
						newDto2.setHospitalId(dto.getHospitalId());
						newDto2.setUnitId(dto.getUnitId());
						newDto2.setGlobalOrgId(dto.getGlobalOrgId());
						newDto2.setGlobalUnitId(dto.getGlobalUnitId());
						newDto2.setApplicationId(2);
						newDto2.setVisitTypeId(5);
						newDto2.setCharges(dto.getFollowUpVisitRate());
						newDto2.setCurrency("INR");
						newDto2.setClassificationId(dto.getClassificationId());
						newDto2.setClassificationDesc(dto.getClassificationDesc());	
					finalDoctorChargesSyncDtoList.add(newDto);
					finalDoctorChargesSyncDtoList.add(newDto2);
				}
			}
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, finalDoctorChargesSyncDtoList, null);
		}catch(Exception e){
			
			throw e;
		}
	}

}