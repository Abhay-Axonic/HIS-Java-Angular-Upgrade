package com.param.opd.unit.dao;

import java.sql.BatchUpdateException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.IAppointmentAndSlotStatusMasterConstants;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.doctor.dao.IDoctorScheduleDao;
import com.param.global.dto.BlockSlotReqDto;
import com.param.global.dto.DoctorMasterDto;
import com.param.global.dto.EquipmentMasterDto;
import com.param.global.dto.ScheduleMasterDto;
import com.param.global.dto.SlotBlockUnblockRequestDto;
import com.param.global.dto.SlotMasterDto;
import com.param.global.org.dto.OrganizationMasterDto;
import com.param.opd.coversheet.dto.WeeklySlotsDto;
import com.param.opd.unit.model.SlotMaster;
import com.param.utility.TimeZoneComponent;

import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked","deprecation","unused" })
public class SlotMasterDaoImpl extends GenericDao<SlotMaster, Integer>implements ISlotMasterDao,ICommonConstants,IAppointmentAndSlotStatusMasterConstants{

	private static final Logger logger = Logger.getLogger(SlotMasterDaoImpl.class);

	public SlotMasterDaoImpl() {
		super(SlotMaster.class);
		// TODO Auto-generated constructor stub
	}

	@Autowired
	HikariDataSource driverManager;
	
	@Autowired
	IDoctorScheduleDao iDoctorScheduleDao;
	
	@Override
	public Response generateSlots(List<SlotMasterDto> slotMasterDtosList,Integer slotType) throws ApplicationException {
		
		int x[];
		int noOfSlots;
		Connection connection =null;
		PreparedStatement statement = null;
		PreparedStatement statement2 = null;
		PreparedStatement statement3 = null;
			try {
					connection = driverManager.getConnection();
					connection.setAutoCommit(false);
			        
					if(slotType==SlotTypeDoctor)
					{
						 statement = connection.prepareStatement("INSERT INTO opd.m_slot_master "
				        		+ "(organization_id, unit_id, speciality_id, doctor_id, appointment_date, day_id, from_time, to_time, slot_type_id, created_by, updated_by, slot_status_id)"
				        		+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);"); 
				     
				        int i = 0;

						// Convert Instant to ZonedDateTime using system's default time zone


				        for (SlotMasterDto entity : slotMasterDtosList) {

							ZoneId systemZone = ZoneId.systemDefault();

							LocalDateTime fromTimeLocal = entity.getFromTime().atZone(ZoneId.of("UTC")).toLocalDateTime();
							LocalDateTime toTimeLocal = entity.getToTime().atZone(ZoneId.of("UTC")).toLocalDateTime();

							ZonedDateTime fromTimeZoned = fromTimeLocal.atZone(systemZone);
							ZonedDateTime toTimeZoned = toTimeLocal.atZone(systemZone);

							Time fromTimeSql = Time.valueOf(fromTimeZoned.toLocalTime());
							Time toTimeSql = Time.valueOf(toTimeZoned.toLocalTime());

				        	statement.setInt(1, entity.getOrganizationId());
				        	statement.setInt(2, entity.getUnitId());
				        	statement.setDate(5, new java.sql.Date( GlobalCommonDateUtils.getDate(entity.getFromDate(),"yyyy-M-dd").getTime()));
				        	statement.setInt(6, entity.getDayId());
				        	statement.setInt(10,entity.getCreatedBy());
				        	statement.setInt(11,entity.getUpdatedBy());
				        	statement.setInt(12,Pending);
				        	statement.setInt(9,entity.getSlotTypeId());
							statement.setTime(7, fromTimeSql);
							statement.setTime(8, toTimeSql);
							statement.setInt(3, entity.getSpecialityId());
							statement.setInt(4, entity.getDoctorId()>0 ? entity.getDoctorId() : null);

				            statement.addBatch();
				            i++;

				            if (i % 500 == 0 || i == slotMasterDtosList.size()) {
				               x = statement.executeBatch(); // Execute every 500 items.
				                connection.commit();
				                //System.out.println(x);
				            }
				        }
					}

					if(slotType==4)
					{
					 statement = connection.prepareStatement("INSERT INTO opd.m_slot_master "
			        		+ "(organization_id, unit_id, speciality_id, appointment_date, day_id, from_time, to_time, slot_type_id, created_by, updated_by, slot_status_id)"
			        		+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");

			        int i = 0;

			        for (SlotMasterDto entity : slotMasterDtosList) {

						ZoneId systemZone = ZoneId.systemDefault();

						LocalDateTime fromTimeLocal = entity.getFromTime().atZone(ZoneId.of("UTC")).toLocalDateTime();
						LocalDateTime toTimeLocal = entity.getToTime().atZone(ZoneId.of("UTC")).toLocalDateTime();

						ZonedDateTime fromTimeZoned = fromTimeLocal.atZone(systemZone);
						ZonedDateTime toTimeZoned = toTimeLocal.atZone(systemZone);

						Time fromTimeSql = Time.valueOf(fromTimeZoned.toLocalTime());
						Time toTimeSql = Time.valueOf(toTimeZoned.toLocalTime());

			        	statement.setInt(1, entity.getOrganizationId());
			        	statement.setInt(2, entity.getUnitId());
			        	statement.setInt(3, entity.getSpecialityId());
			        	statement.setDate(4, new java.sql.Date( GlobalCommonDateUtils.getDate(entity.getFromDate(),"yyyy-M-dd").getTime()));
			        	statement.setInt(5, entity.getDayId());
						statement.setTime(6, fromTimeSql);
						statement.setTime(7, toTimeSql);
						statement.setInt(8,entity.getSlotTypeId());
						statement.setInt(9,entity.getCreatedBy());
			        	statement.setInt(10,entity.getUpdatedBy());
			        	statement.setInt(11,Pending);
			            statement.addBatch();
			            i++;

			            if (i % 500 == 0 || i == slotMasterDtosList.size()) {
			            	 x = statement.executeBatch(); // Execute every 500 items.
				                connection.commit();
				                //System.out.println(x);
			            }
			        }
					}

			        if(slotType==SlotTypeModality)
			        {
			        	 statement2 = connection.prepareStatement("INSERT INTO opd.m_slot_master "
				        		+ "(organization_id, unit_id, speciality_id, appointment_date, day_id, from_time, to_time,modality_id, slot_type_id, created_by, updated_by, slot_status_id,sub_speciality_id,visit_type_id)"
				        		+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?);");

				        int j = 0;

				        for (SlotMasterDto entity : slotMasterDtosList)
				        {
							ZoneId systemZone = ZoneId.systemDefault();

							LocalDateTime fromTimeLocal = entity.getFromTime().atZone(ZoneId.of("UTC")).toLocalDateTime();
							LocalDateTime toTimeLocal = entity.getToTime().atZone(ZoneId.of("UTC")).toLocalDateTime();

							ZonedDateTime fromTimeZoned = fromTimeLocal.atZone(systemZone);
							ZonedDateTime toTimeZoned = toTimeLocal.atZone(systemZone);

							Time fromTimeSql = Time.valueOf(fromTimeZoned.toLocalTime());
							Time toTimeSql = Time.valueOf(toTimeZoned.toLocalTime());

							statement2.setInt(1, entity.getOrganizationId());
				        	statement2.setInt(2, entity.getUnitId());
				        	statement2.setInt(3, entity.getSpecialityId());
				        	statement2.setDate(4, new java.sql.Date( GlobalCommonDateUtils.getDate(entity.getFromDate(),"yyyy-M-dd").getTime()));
				        	statement2.setInt(5, entity.getDayId());
							statement2.setTime(6, fromTimeSql);
							statement2.setTime(7, toTimeSql);
							statement2.setInt(8, entity.getModalityId()>0 ? entity.getModalityId() : null);
							statement2.setInt(9,entity.getSlotTypeId());
				        	statement2.setInt(10,entity.getCreatedBy());
				        	statement2.setInt(11,entity.getUpdatedBy());
				        	statement2.setInt(12,Pending);
				        	statement2.setInt(13,entity.getSubSpecialityId());
				        	statement2.setInt(14,entity.getVisitTypeId());
				            statement2.addBatch();
				            j++;

				            if (j % 500 == 0 || j == slotMasterDtosList.size()) {
				            	 x = statement2.executeBatch(); // Execute every 500 items.
					                connection.commit();
					                //System.out.println(x);
				            }
				        }

			        }
			        if(slotType==SlotTypeECH)
			        {
			        	 statement3 = connection.prepareStatement("INSERT INTO opd.m_slot_master "
				        		+ "(organization_id, unit_id, appointment_date, day_id, slot_type_id, created_by, updated_by, slot_status_id)"
				        		+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?);");

				        int k = 0;

				        for (SlotMasterDto entity : slotMasterDtosList)
				        {
				        	statement3.setInt(1, entity.getOrganizationId());
				        	statement3.setInt(2, entity.getUnitId());
				        	statement3.setDate(3, new java.sql.Date( GlobalCommonDateUtils.getDate(entity.getFromDate(),"yyyy-M-dd").getTime()));
				        	statement3.setInt(4, entity.getDayId());
							statement3.setInt(5,entity.getSlotTypeId());
				        	statement3.setInt(6,entity.getCreatedBy());
				        	statement3.setInt(7,entity.getUpdatedBy());
				        	statement3.setInt(8,Pending);

				        	statement3.addBatch();
				            k++;

				            if (k % 500 == 0 || k == slotMasterDtosList.size()) {
				            	 x = statement3.executeBatch(); // Execute every 500 items.
					                connection.commit();
					                //System.out.println(x);
				            }
				        }
			        }
			        if(slotType == SlotTypeHemodialysis)
		              {
		                //System.out.println("Hemodialysis slot generation.......");
		                 statement2 = connection.prepareStatement("INSERT INTO opd.m_slot_master "
		                    + "(organization_id, unit_id, speciality_id, appointment_date, day_id, slot_type_id, created_by, updated_by, slot_status_id,bed_id)"
		                    + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");

		                int j = 0;

		                for (SlotMasterDto entity : slotMasterDtosList)
		                {
		                  statement2.setInt(1, entity.getOrganizationId());
		                  statement2.setInt(2, entity.getUnitId());
		                  statement2.setInt(3, entity.getSpecialityId());
		                  statement2.setDate(4, new java.sql.Date( GlobalCommonDateUtils.getDate(entity.getFromDate(),"yyyy-M-dd").getTime()));
		                  statement2.setInt(5, entity.getDayId());
		                  statement2.setInt(6,entity.getSlotTypeId());
		                  statement2.setInt(7,entity.getCreatedBy());
		                  statement2.setInt(8,entity.getUpdatedBy());
		                  statement2.setInt(9,Pending);
		                  statement2.setInt(10,entity.getBedId());
		                   statement2.addBatch();
		                    j++;

		                    if (j % 500 == 0 || j == slotMasterDtosList.size()) {
		                    	//System.out.println("test");
		                       x = statement2.executeBatch(); // Execute every 500 items.
		                          connection.commit();
		                          //System.out.println(x);
		                    }
		                }

		              }
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		}catch (HibernateException he) {
		logger.error("generateSlots failed", he);
		} catch (BatchUpdateException e) {
		logger.error("generateSlots failed", e);
		}
			catch (Exception e) {
			logger.error("generateSlots failed", e);
			}finally{
			try {
				if (statement != null) {
					statement.close();
				}
				if (statement2 != null) {
					statement2.close();
				}
				if (statement3 != null) {
					statement3.close();
				}
				connection.setAutoCommit(true);
				connection.close();
			} catch (SQLException e) {
			logger.error("generateSlots failed", e);
			return new Response(ERROR, null, null, null,e);
			}
		}

		return new Response(ERROR, null, null, null,null);

		/*try {
			int count =1;
			ListIterator<SlotMasterDto> listIterator = slotMasterDtosList.listIterator();
			while(listIterator.hasNext())
			{
				SlotMasterDto slotMasterDtosObj = listIterator.next();

				SlotMaster slotMaster = new SlotMaster();

					slotMaster.setOrganizationId(slotMasterDtosObj.getOrganizationId());
					slotMaster.setUnitId(slotMasterDtosObj.getUnitId());
					slotMaster.setAppointmentDate(GlobalCommonDateUtils.getDate(slotMasterDtosObj.getFromDate(),"yyyy-M-dd"));
					slotMaster.setDayId(slotMasterDtosObj.getDayId());
					slotMaster.setCreatedDate(GlobalCommonDateUtils.getDate(slotMasterDtosObj.getCreatedDate(),"dd-M-yyyy HH:mm:ss"));
					slotMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(slotMasterDtosObj.getUpdatedDate(),"dd-M-yyyy HH:mm:ss"));
					slotMaster.setCreatedBy(slotMasterDtosObj.getCreatedBy());
					slotMaster.setUpdatedBy(slotMasterDtosObj.getUpdatedBy());
					slotMaster.setStatus(slotMasterDtosObj.getStatus());
					slotMaster.setSlotTypeId(slotMasterDtosObj.getSlotTypeId());
					slotMaster.setSlotStatusId(Vaccant);
					slotMaster.setIsBlocked(Unblocked);
					if(slotMasterDtosObj.getSlotTypeId()!=SlotTypeECH) // these details does not require for ECH
					{
						slotMaster.setFromTime(slotMasterDtosObj.getFromTime());
						slotMaster.setToTime(slotMasterDtosObj.getToTime());
						slotMaster.setSpecialityId(slotMasterDtosObj.getSpecialityId());
						slotMaster.setDoctorId(slotMasterDtosObj.getDoctorId());
						slotMaster.setModalityId(slotMasterDtosObj.getModalityId());
						slotMaster.setSubSpecialityId(slotMasterDtosObj.getSubSpecialityId());
					}
				save(slotMaster);
				count++;
				 if ( count % 20 == 0 ) { //20, same as the JDBC batch size
				        //flush a batch of inserts and release memory:
				        sessionFactory.getCurrentSession().flush();
				        sessionFactory.getCurrentSession().clear();
				    }

			}
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		}catch (HibernateException he) {
			h
		} catch (Exception e) {

		}
		return new Response(ERROR, null, null, null, null);*/
	}

	@Override
	public Response generateSlotsDoctorSheduling(List<SlotMasterDto> slotMasterDtosList,Integer slotType) throws ApplicationException {

		int x[];
		int noOfSlots;
		Connection connection =null;
		PreparedStatement statement = null;
		PreparedStatement statement2 = null;
		PreparedStatement statement3 = null;
			try {
					connection = driverManager.getConnection();
					connection.setAutoCommit(false);

					if(slotType==SlotTypeDoctor)
					{
						 statement = connection.prepareStatement("INSERT INTO opd.m_slot_master "
				        		+ "(organization_id, unit_id, speciality_id, doctor_id, appointment_date, day_id, from_time, to_time, slot_type_id, created_by, updated_by, slot_status_id)"
				        		+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");

				        int i = 0;

				        for (SlotMasterDto entity : slotMasterDtosList) {

							ZonedDateTime zonedDateTime = entity.getFromTime().atZone(ZoneId.systemDefault());

							// Extract hours and seconds
							int hours = zonedDateTime.getHour();
							int min = zonedDateTime.getMinute();
							int seconds = zonedDateTime.getSecond();

							ZonedDateTime zonedDateTimeTO = entity.getToTime().atZone(ZoneId.systemDefault());

							// Extract hours and seconds
							int hoursTo = zonedDateTime.getHour();
							int minTo = zonedDateTime.getMinute();
							int secondsTo = zonedDateTime.getSecond();

				        	statement.setInt(1, entity.getOrganizationId());
				        	statement.setInt(2, entity.getUnitId());
				        	statement.setDate(5, new java.sql.Date( GlobalCommonDateUtils.getDate(entity.getFromDate(),"yyyy-M-dd").getTime()));
				        	statement.setInt(6, entity.getDayId());
				        	statement.setInt(10,entity.getCreatedBy());
				        	statement.setInt(11,entity.getUpdatedBy());
				        	statement.setInt(12,Pending);
				        	statement.setInt(9,entity.getSlotTypeId());
							statement.setTime(7, new Time(hours,min,seconds));
							statement.setTime(8, new Time(hoursTo,minTo,secondsTo));
							statement.setInt(3, entity.getSpecialityId());
							statement.setInt(4, entity.getDoctorId()>0 ? entity.getDoctorId() : null);

				            statement.addBatch();
				            i++;

				            if (i % 500 == 0 || i == slotMasterDtosList.size()) {
				               x = statement.executeBatch(); // Execute every 500 items.
				                connection.commit();
				                //System.out.println(x);
				            }
				        }
					}

					if(slotType==4)
					{
					 statement = connection.prepareStatement("INSERT INTO opd.m_slot_master "
			        		+ "(organization_id, unit_id, speciality_id, appointment_date, day_id, from_time, to_time, slot_type_id, created_by, updated_by, slot_status_id)"
			        		+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");

			        int i = 0;

			        for (SlotMasterDto entity : slotMasterDtosList) {


						ZonedDateTime zonedDateTime = entity.getFromTime().atZone(ZoneId.systemDefault());

						// Extract hours and seconds
						int hours = zonedDateTime.getHour();
						int min = zonedDateTime.getMinute();
						int seconds = zonedDateTime.getSecond();

						ZonedDateTime zonedDateTimeTO = entity.getToTime().atZone(ZoneId.systemDefault());

						// Extract hours and seconds
						int hoursTo = zonedDateTime.getHour();
						int minTo = zonedDateTime.getMinute();
						int secondsTo = zonedDateTime.getSecond();

			        	statement.setInt(1, entity.getOrganizationId());
			        	statement.setInt(2, entity.getUnitId());
			        	statement.setInt(3, entity.getSpecialityId());
			        	statement.setDate(4, new java.sql.Date( GlobalCommonDateUtils.getDate(entity.getFromDate(),"yyyy-M-dd").getTime()));
			        	statement.setInt(5, entity.getDayId());
						statement.setTime(6, new Time(hours,min,seconds));
						statement.setTime(7, new Time(hoursTo,minTo,secondsTo));
						statement.setInt(8,entity.getSlotTypeId());
						statement.setInt(9,entity.getCreatedBy());
			        	statement.setInt(10,entity.getUpdatedBy());
			        	statement.setInt(11,Pending);
			            statement.addBatch();
			            i++;

			            if (i % 500 == 0 || i == slotMasterDtosList.size()) {
			            	 x = statement.executeBatch(); // Execute every 500 items.
				                connection.commit();
				                //System.out.println(x);
			            }
			        }
					}

			        if(slotType==SlotTypeModality)
			        {
			        	 statement2 = connection.prepareStatement("INSERT INTO opd.m_slot_master "
				        		+ "(organization_id, unit_id, speciality_id, appointment_date, day_id, from_time, to_time,modality_id, slot_type_id, created_by, updated_by, slot_status_id,sub_speciality_id,visit_type_id)"
				        		+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?);");

				        int j = 0;

				        for (SlotMasterDto entity : slotMasterDtosList)
				        {

							ZonedDateTime zonedDateTime = entity.getFromTime().atZone(ZoneId.systemDefault());

							// Extract hours and seconds
							int hours = zonedDateTime.getHour();
							int min = zonedDateTime.getMinute();
							int seconds = zonedDateTime.getSecond();

							ZonedDateTime zonedDateTimeTO = entity.getToTime().atZone(ZoneId.systemDefault());

							// Extract hours and seconds
							int hoursTo = zonedDateTime.getHour();
							int minTo = zonedDateTime.getMinute();
							int secondsTo = zonedDateTime.getSecond();

				        	statement2.setInt(1, entity.getOrganizationId());
				        	statement2.setInt(2, entity.getUnitId());
				        	statement2.setInt(3, entity.getSpecialityId());
				        	statement2.setDate(4, new java.sql.Date( GlobalCommonDateUtils.getDate(entity.getFromDate(),"yyyy-M-dd").getTime()));
				        	statement2.setInt(5, entity.getDayId());
							statement2.setTime(7, new Time(hours,min,seconds));
							statement2.setTime(8, new Time(hoursTo,minTo,secondsTo));
							statement2.setInt(8, entity.getModalityId()>0 ? entity.getModalityId() : null);
							statement2.setInt(9,entity.getSlotTypeId());
				        	statement2.setInt(10,entity.getCreatedBy());
				        	statement2.setInt(11,entity.getUpdatedBy());
				        	statement2.setInt(12,Pending);
				        	statement2.setInt(13,entity.getSubSpecialityId());
				        	statement2.setInt(14,entity.getVisitTypeId());
				            statement2.addBatch();
				            j++;

				            if (j % 500 == 0 || j == slotMasterDtosList.size()) {
				            	 x = statement2.executeBatch(); // Execute every 500 items.
					                connection.commit();
					                //System.out.println(x);
				            }
				        }

			        }
			        if(slotType==SlotTypeECH)
			        {
			        	 statement3 = connection.prepareStatement("INSERT INTO opd.m_slot_master "
				        		+ "(organization_id, unit_id, appointment_date, day_id, slot_type_id, created_by, updated_by, slot_status_id)"
				        		+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?);");

				        int k = 0;

				        for (SlotMasterDto entity : slotMasterDtosList)
				        {
				        	statement3.setInt(1, entity.getOrganizationId());
				        	statement3.setInt(2, entity.getUnitId());
				        	statement3.setDate(3, new java.sql.Date( GlobalCommonDateUtils.getDate(entity.getFromDate(),"yyyy-M-dd").getTime()));
				        	statement3.setInt(4, entity.getDayId());
							statement3.setInt(5,entity.getSlotTypeId());
				        	statement3.setInt(6,entity.getCreatedBy());
				        	statement3.setInt(7,entity.getUpdatedBy());
				        	statement3.setInt(8,Pending);

				        	statement3.addBatch();
				            k++;

				            if (k % 500 == 0 || k == slotMasterDtosList.size()) {
				            	 x = statement3.executeBatch(); // Execute every 500 items.
					                connection.commit();
					                //System.out.println(x);
				            }
				        }
			        }
			        if(slotType == SlotTypeHemodialysis)
		              {
		                //System.out.println("Hemodialysis slot generation.......");
		                 statement2 = connection.prepareStatement("INSERT INTO opd.m_slot_master "
		                    + "(organization_id, unit_id, speciality_id, appointment_date, day_id, slot_type_id, created_by, updated_by, slot_status_id,bed_id)"
		                    + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");

		                int j = 0;

		                for (SlotMasterDto entity : slotMasterDtosList)
		                {
		                  statement2.setInt(1, entity.getOrganizationId());
		                  statement2.setInt(2, entity.getUnitId());
		                  statement2.setInt(3, entity.getSpecialityId());
		                  statement2.setDate(4, new java.sql.Date( GlobalCommonDateUtils.getDate(entity.getFromDate(),"yyyy-M-dd").getTime()));
		                  statement2.setInt(5, entity.getDayId());
		                  statement2.setInt(6,entity.getSlotTypeId());
		                  statement2.setInt(7,entity.getCreatedBy());
		                  statement2.setInt(8,entity.getUpdatedBy());
		                  statement2.setInt(9,Pending);
		                  statement2.setInt(10,entity.getBedId());
		                   statement2.addBatch();
		                    j++;

		                    if (j % 500 == 0 || j == slotMasterDtosList.size()) {
		                    	//System.out.println("test");
		                       x = statement2.executeBatch(); // Execute every 500 items.
		                          connection.commit();
		                          //System.out.println(x);
		                    }
		                }

		              }
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		}catch (HibernateException he) {
		logger.error("generateSlotsDoctorSheduling failed", he);
		} catch (BatchUpdateException e) {
		logger.error("generateSlotsDoctorSheduling failed", e);
		}
			catch (Exception e) {
			logger.error("generateSlotsDoctorSheduling failed", e);
			}finally{
			try {
				if (statement != null) {
					statement.close();
				}
				if (statement2 != null) {
					statement2.close();
				}
				if (statement3 != null) {
					statement3.close();
				}
				connection.setAutoCommit(true);
				connection.close();
			} catch (SQLException e) {
			logger.error("generateSlotsDoctorSheduling failed", e);
			return new Response(ERROR, null, null, null,e);
			}
		}

		return new Response(ERROR, null, null, null,null);


		/*try {
			int count =1;
			ListIterator<SlotMasterDto> listIterator = slotMasterDtosList.listIterator();
			while(listIterator.hasNext())
			{
				SlotMasterDto slotMasterDtosObj = listIterator.next();

				SlotMaster slotMaster = new SlotMaster();

					slotMaster.setOrganizationId(slotMasterDtosObj.getOrganizationId());
					slotMaster.setUnitId(slotMasterDtosObj.getUnitId());
					slotMaster.setAppointmentDate(GlobalCommonDateUtils.getDate(slotMasterDtosObj.getFromDate(),"yyyy-M-dd"));
					slotMaster.setDayId(slotMasterDtosObj.getDayId());
					slotMaster.setCreatedDate(GlobalCommonDateUtils.getDate(slotMasterDtosObj.getCreatedDate(),"dd-M-yyyy HH:mm:ss"));
					slotMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(slotMasterDtosObj.getUpdatedDate(),"dd-M-yyyy HH:mm:ss"));
					slotMaster.setCreatedBy(slotMasterDtosObj.getCreatedBy());
					slotMaster.setUpdatedBy(slotMasterDtosObj.getUpdatedBy());
					slotMaster.setStatus(slotMasterDtosObj.getStatus());
					slotMaster.setSlotTypeId(slotMasterDtosObj.getSlotTypeId());
					slotMaster.setSlotStatusId(Vaccant);
					slotMaster.setIsBlocked(Unblocked);
					if(slotMasterDtosObj.getSlotTypeId()!=SlotTypeECH) // these details does not require for ECH
					{
						slotMaster.setFromTime(slotMasterDtosObj.getFromTime());
						slotMaster.setToTime(slotMasterDtosObj.getToTime());
						slotMaster.setSpecialityId(slotMasterDtosObj.getSpecialityId());
						slotMaster.setDoctorId(slotMasterDtosObj.getDoctorId());
						slotMaster.setModalityId(slotMasterDtosObj.getModalityId());
						slotMaster.setSubSpecialityId(slotMasterDtosObj.getSubSpecialityId());
					}
				save(slotMaster);
				count++;
				 if ( count % 20 == 0 ) { //20, same as the JDBC batch size
				        //flush a batch of inserts and release memory:
				        sessionFactory.getCurrentSession().flush();
				        sessionFactory.getCurrentSession().clear();
				    }

			}
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		}catch (HibernateException he) {
			h
		} catch (Exception e) {

		}
		return new Response(ERROR, null, null, null, null);*/
	}

	@Override
	public Response getSlots(SlotMasterDto slotMasterDto) throws ApplicationException {
		try {

			String query = new String();

			if(slotMasterDto.getSlotTypeId()==4 && slotMasterDto.getDeptWiseAppointment()=='Y')
			{
				query = "select "
						+ " 	distinct(sm.speciality_id) as \"specialityId\",  "
						+ " 	speMstr.speciality_name as \"specialityName\"  "
						+ " from  "
						+ " 	opd.m_slot_master sm  "
						+ " 	inner join doctor.m_speciality_master speMstr on  "
						+ " 		sm.speciality_id = speMstr.speciality_id  "
						+ " 	inner join public.t_unit_speciality_mapper usm on  "
						+ " 		 usm.speciality_id = sm.speciality_id  "
						+ " where  "
						+ " 	sm.unit_id =:unitId  "
						+ " 	and usm.unit_id =:unitId "
						+ " 	and usm.dept_wise_appointment = 'Y' "
						+ "		and sm.doctor_id is null  "
						+ "		and sm.status='A' "
						+ " order by  "
						+ " 	sm.speciality_id  ";

				List<SlotMasterDto> slotMasterDtosList = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
						.setFirstResult(slotMasterDto.getOffset() != null ? slotMasterDto.getOffset() : 0)
						.setMaxResults(slotMasterDto.getNoOfRecordsPerPage() != null ? slotMasterDto.getNoOfRecordsPerPage() : 10)
						//.setParameter("orgId", slotMasterDto.getOrganizationId())
						.setParameter("unitId", slotMasterDto.getUnitId())
						//.setParameter("slotTypeId", slotMasterDto.getSlotTypeId())
						.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
				return new Response(SUCCESS, null, null, slotMasterDtosList, null);
			}
			if(slotMasterDto.getSlotTypeId()==1)
			{

				query =  "select "
						+ "		distinct(sm.doctor_id) as \"doctorId\", "
						+ "		concat('Dr. ',doc.first_name,' ',doc.middle_name,' ',doc.last_name) as \"doctorName\", "
						+ "		sm.speciality_id as \"specialityId\", "
						+ "     um.unit_name as \"unitName\", "
						+ "     sm.unit_id  as \"unitId\", "
						+ "		speMstr.speciality_name as \"specialityName\" "
						+ "from "
						+ "		opd.m_slot_master sm "
						+ "		inner join doctor.m_speciality_master speMstr on "
						+ "			sm.speciality_id = speMstr.speciality_id "
						+ "		inner join doctor.m_doctor_master doc on "
						+ "			sm.doctor_id = doc.doctor_id "
						+ "     inner join public.m_unit_master um on "
						+ "         sm.unit_id = um.unit_id "
						+ "	where "
						+ " 	sm.organization_id =:orgId  "
						//+ "		sm.unit_id =:unitId "
						+ " 	and speMstr.dept_wise_appointment != 'Y' "
						+ "	 	and sm.doctor_id is not null "
						+ " 	and sm.status='A'  "
						+ " order by sm.doctor_id";


				List<SlotMasterDto> slotMasterDtosList = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
						.setFirstResult(slotMasterDto.getOffset() != null ? slotMasterDto.getOffset() : 0)
						.setMaxResults(slotMasterDto.getNoOfRecordsPerPage() != null ? slotMasterDto.getNoOfRecordsPerPage() : 10)
						.setParameter("orgId", slotMasterDto.getOrganizationId())
						//.setParameter("unitId", slotMasterDto.getUnitId())
						//.setParameter("slotTypeId", slotMasterDto.getSlotTypeId())
						.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
				return new Response(SUCCESS, null, null, slotMasterDtosList, null);
			}
			if(slotMasterDto.getSlotTypeId()==2)
			{

				query =  "select "
					 + " 	distinct(sm.modality_id) as \"modalityId\", "
					 + " 	modality.modality_desc as \"modalityDesc\", "
					 + " 	sm.speciality_id as \"specialityId\", "
					 + " 	speMstr.speciality_name as \"specialityName\", "
					 + " 	sm.sub_speciality_id as \"subSpecialityId\", "
					 + " 	subSpeMastr.sub_speciality_name as \"subSpecialityName\" "
					 + " from "
					 + " 	opd.m_slot_master sm "
					 + " inner join doctor.m_speciality_master speMstr on "
					 + " 	sm.speciality_id = speMstr.speciality_id "
					 + " inner join doctor.m_sub_speciality_master subSpeMastr on "
					 + " 	sm.sub_speciality_id = subSpeMastr.sub_speciality_id "
					 + " inner join m_modality_master modality on "
					 + " 	sm.modality_id = modality.modality_id	 "
					 + " where "
					 + " 	sm.unit_id =:unitId "
					 + " 	and sm.status = 'A' "
					 + " order by "
					 + " 	sm.modality_id ";


				List<SlotMasterDto> slotMasterDtosList = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
						.setFirstResult(slotMasterDto.getOffset() != null ? slotMasterDto.getOffset() : 0)
						.setMaxResults(slotMasterDto.getNoOfRecordsPerPage() != null ? slotMasterDto.getNoOfRecordsPerPage() : 10)
						//.setParameter("orgId", slotMasterDto.getOrganizationId())
						.setParameter("unitId", slotMasterDto.getUnitId())
						//.setParameter("slotTypeId", slotMasterDto.getSlotTypeId())
						.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
				return new Response(SUCCESS, null, null, slotMasterDtosList, null);
			}
			if(slotMasterDto.getSlotTypeId()==3)
			{

				List<SlotMasterDto> slotMasterDtosList = sessionFactory.getCurrentSession().getNamedQuery("GET_ACTIVE_APPOINTMENTS")
						.setFirstResult(slotMasterDto.getOffset() != null ? slotMasterDto.getOffset() : 0)
						.setMaxResults(slotMasterDto.getNoOfRecordsPerPage() != null ? slotMasterDto.getNoOfRecordsPerPage() : 10)
						.setParameter("orgId", slotMasterDto.getOrganizationId())
						.setParameter("unitId", slotMasterDto.getUnitId())
						.setParameter("slotTypeId", slotMasterDto.getSlotTypeId())
						.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
				return new Response(SUCCESS, null, null, slotMasterDtosList, null);
			}

		} catch (HibernateException he) {
		logger.error("getSlots failed", he);
		} catch (Exception e) {
		logger.error("getSlots failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	public Response getSlotCount(SlotMasterDto slotMasterDto) throws ApplicationException {
		try {
			StringBuilder query = new StringBuilder();
			List<SlotMasterDto> slotMasterDtosList = null;
			if(slotMasterDto.getSlotTypeId()==4 && slotMasterDto.getDeptWiseAppointment()=='Y')
			{
				/*query = "select "
						+ " 	distinct(sm.speciality_id) as \"specialityId\",  "
						+ " 	speMstr.speciality_name as \"specialityName\"  "
						+ " from  "
						+ " 	opd.m_slot_master sm  "
						+ " 	left outer join doctor.m_speciality_master speMstr on  "
						+ " 		sm.speciality_id = speMstr.speciality_id  "
						+ " where  "
						+ " 	sm.organization_id =:orgId  "
						+ " 	and sm.unit_id =:unitId  "
						+ " 	and speMstr.dept_wise_appointment = 'Y' "
						+ "		and sm.doctor_id is null  "
						+ " order by  "
						+ " 	sm.speciality_id  ";*/

				query.append("select "
						+ " 	distinct(sm.speciality_id) as \"specialityId\",  "
						+ " 	speMstr.speciality_name as \"specialityName\"  "
						+ " from  "
						//+ " 	opd.m_slot_master sm  "
						+ " 	t_unit_speciality_mapper sm  "
						+ " 	inner join doctor.m_speciality_master speMstr on  "
						+ " 		sm.speciality_id = speMstr.speciality_id  "
						+ " 	inner join public.t_unit_speciality_mapper usm on  "
						+ " 		 usm.speciality_id = sm.speciality_id  "
						+ " where  "
						+ " 	sm.unit_id =:unitId  "
						//+ " 	and usm.unit_id =:unitId "
						+ " 	and usm.dept_wise_appointment = 'Y' "
						//+ "		and sm.doctor_id is null  "
						+ "		and sm.status='A' "
						+ " order by  "
						+ " 	sm.speciality_id  ");

				slotMasterDtosList = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
						/*.setFirstResult(slotMasterDto.getOffset() != null ? slotMasterDto.getOffset() : 0)
						.setMaxResults(slotMasterDto.getNoOfRecordsPerPage() != null ? slotMasterDto.getNoOfRecordsPerPage() : 10)*/
						//.setParameter("orgId", slotMasterDto.getOrganizationId())
						.setParameter("unitId", slotMasterDto.getUnitId())
						//.setParameter("slotTypeId", slotMasterDto.getSlotTypeId())
						.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
			}
			if(slotMasterDto.getSlotTypeId()==1){

//				query =  "select "
//						+ "distinct(sm.doctor_id) as \"doctorId\", "
//						+ "concat('Dr. ',doc.first_name,' ',doc.middle_name,' ',doc.last_name) as \"doctorName\", "
//						+ "sm.speciality_id as \"specialityId\", "
//						+ "speMstr.speciality_name as \"specialityName\" "
//						+ "from "
//						+ "opd.m_slot_master sm "
//						+ "left outer join doctor.m_speciality_master speMstr on "
//						+ "sm.speciality_id = speMstr.speciality_id "
//						+ "left outer join doctor.m_doctor_master doc on "
//						+ "sm.doctor_id = doc.doctor_id "
//						+ "where "
//						+ "	sm.organization_id =:orgId "
//					//	+ "	and sm.unit_id =:unitId "
//						+ " and speMstr.dept_wise_appointment != 'Y' and sm.doctor_id is not null "
//						+ " order by sm.doctor_id";

				query.append("select "
				        + "	distinct(mdm.doctor_id) as \"doctorId\", "
						+ "	concat('Dr. ',mdm.first_name,' ',mdm.middle_name,' ',mdm.last_name) as \"doctorName\", "
						+ " msm.speciality_id as \"specialityId\", " + "	um.unit_name as \"unitName\",  "
						+ "  mdm.unit_id  as \"unitId\" , msm.speciality_name as \"specialityName\" from doctor.m_doctor_master mdm	"
						+ " inner join public.t_user_unit_mapper uum on "
						+ " mdm.user_id = uum.user_id "
						+ "inner join doctor.t_doctor_speciality_mapper speMstr on "
						+ "speMstr.doctor_id = mdm.doctor_id " + "inner join public.m_unit_master um on  "
						+ "  mdm.unit_id = um.unit_id "
						// + "inner join doctor.t_doctor_speciality_mapper dm on "
						// + "dm.doctor_id = mdm.doctor_id "
						+ "inner join doctor.m_speciality_master msm on "
						+ "	speMstr.speciality_id = msm.speciality_id " + "where " + "msm.speciality_id in ( "
						+ "select " + "msm.speciality_id " + "from " + "t_unit_speciality_mapper tusm " + " where "
						+ "	tusm.dept_wise_appointment != 'Y'  "
						// + " and unit_id =:unitId "
						+ "	and organization_id =:orgId " + "	and status = 'A' ) " + "and mdm.doctor_id is not null "
						+ "and mdm.organization_id =:orgId "
						//+ "and mdm.unit_id =:unitId "
						);

				if(slotMasterDto.getSearchKeyword()!=null && !slotMasterDto.getSearchKeyword().equals("") && !slotMasterDto.getSearchKeyword().isEmpty()) {
					/*query.append(" AND ( "
							+ " lower(concat('Dr. ',mdm.first_name,' ',mdm.middle_name,' ',mdm.last_name)) like '%" + slotMasterDto.getSearchKeyword().toLowerCase() + "%'"
							+ " OR lower(msm.speciality_name) like '%" + slotMasterDto.getSearchKeyword().toLowerCase() + "%'"
							+ " OR lower(um.unit_name) like '%" + slotMasterDto.getSearchKeyword().toLowerCase() + "%'"
							+ " ) ");*/

					query.append(" AND ( ")
							.append(" lower(concat('Dr. ', mdm.first_name, ' ', mdm.middle_name, ' ', mdm.last_name)) LIKE :searchKeyword ")
							.append(" OR lower(msm.speciality_name) LIKE :searchKeyword ")
							.append(" OR lower(um.unit_name) LIKE :searchKeyword ")
							.append(" ) ");
				}

				Query createQuery = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
						/*.setFirstResult(slotMasterDto.getOffset() != null ? slotMasterDto.getOffset() : 0)
						.setMaxResults(slotMasterDto.getNoOfRecordsPerPage() != null ? slotMasterDto.getNoOfRecordsPerPage() : 10)*/
						.setParameter("orgId", slotMasterDto.getOrganizationId());
					//	.setParameter("unitId", slotMasterDto.getUnitId())
						//.setParameter("slotTypeId", slotMasterDto.getSlotTypeId())

				if (slotMasterDto.getSearchKeyword() != null && !slotMasterDto.getSearchKeyword().trim().isEmpty()) {
					createQuery.setParameter("searchKeyword", "%" + slotMasterDto.getSearchKeyword().toLowerCase() + "%");
				}

				slotMasterDtosList=createQuery.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
			}
			if(slotMasterDto.getSlotTypeId()==2)
			{

//				query =  "select "
//					 + " 	distinct(sm.modality_id) as \"modalityId\", "
//					 + " 	modality.modality_desc as \"modalityDesc\", "
//					 + " 	sm.speciality_id as \"specialityId\", "
//					 + " 	speMstr.speciality_name as \"specialityName\", "
//					 + " 	sm.sub_speciality_id as \"subSpecialityId\", "
//					 + " 	subSpeMastr.sub_speciality_name as \"subSpecialityName\" "
//					 + " from "
//					 + " 	opd.m_slot_master sm "
//					 + " inner join doctor.m_speciality_master speMstr on "
//					 + " 	sm.speciality_id = speMstr.speciality_id "
//					 + " inner join doctor.m_sub_speciality_master subSpeMastr on "
//					 + " 	sm.sub_speciality_id = subSpeMastr.sub_speciality_id "
//					 + " inner join m_modality_master modality on "
//					 + " 	sm.modality_id = modality.modality_id	 "
//					 + " where "
//					 + " 	sm.unit_id =:unitId "
//					 + " 	and sm.status = 'A' "
//					 + " order by "
//					 + " 	sm.modality_id ";

				query.append("select "
						 + " 	distinct(modality.modality_id) as \"modalityId\", "
						 + " 	modality.modality_desc as \"modalityDesc\", "
						 + " 	modality.speciality_id as \"specialityId\", "
						 + " 	speMstr.speciality_name as \"specialityName\", "
						 + " 	modality.sub_speciality_id as \"subSpecialityId\", "
						 + " 	subSpeMastr.sub_speciality_name as \"subSpecialityName\" "
						 + " from "
						// + " 	opd.m_slot_master sm "
						 + "  	 m_modality_master modality "
						 + " inner join doctor.m_speciality_master speMstr on "
						 + " 	modality.speciality_id = speMstr.speciality_id "
						 + " inner join doctor.m_sub_speciality_master subSpeMastr on "
						 + " 	modality.sub_speciality_id = subSpeMastr.sub_speciality_id "
						 //+ " inner join m_modality_master modality on "
						 //+ " 	modality.modality_id = modality.modality_id	 "
						 + " where "
						 + " 	modality.unit_id =:unitId "
						 + " 	and modality.status = 'A' "
						 + " order by "
						 + " 	modality.modality_id ");


				slotMasterDtosList = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
						/*.setFirstResult(slotMasterDto.getOffset() != null ? slotMasterDto.getOffset() : 0)
						.setMaxResults(slotMasterDto.getNoOfRecordsPerPage() != null ? slotMasterDto.getNoOfRecordsPerPage() : 10)*/
						//.setParameter("orgId", slotMasterDto.getOrganizationId())
						.setParameter("unitId", slotMasterDto.getUnitId())
						//.setParameter("slotTypeId", slotMasterDto.getSlotTypeId())
						.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
				//return new Response(SUCCESS, null, null, slotMasterDtosList, null);
			}
			if(slotMasterDto.getSlotTypeId()==3)
			{

				slotMasterDtosList = sessionFactory.getCurrentSession().getNamedQuery("GET_ACTIVE_APPOINTMENTS")
						//.setFirstResult(slotMasterDto.getOffset() != null ? slotMasterDto.getOffset() : 0)
						//.setMaxResults(slotMasterDto.getNoOfRecordsPerPage() != null ? slotMasterDto.getNoOfRecordsPerPage() : 10)
						.setParameter("orgId", slotMasterDto.getOrganizationId())
						.setParameter("unitId", slotMasterDto.getUnitId())
						.setParameter("slotTypeId", slotMasterDto.getSlotTypeId())
						.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
				//return new Response(SUCCESS, null, null, slotMasterDtosList, null);
			}

			if(slotMasterDto.getSlotTypeId()==8)
			{

				slotMasterDtosList = sessionFactory.getCurrentSession().getNamedQuery("GET_ACTIVE_APPOINTMENTS")
						.setFirstResult(slotMasterDto.getOffset() != null ? slotMasterDto.getOffset() : 0)
						.setMaxResults(slotMasterDto.getNoOfRecordsPerPage() != null ? slotMasterDto.getNoOfRecordsPerPage() : 10)
						.setParameter("orgId", slotMasterDto.getOrganizationId())
						.setParameter("unitId", slotMasterDto.getUnitId())
						.setParameter("slotTypeId", slotMasterDto.getSlotTypeId())
						.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
				//return new Response(SUCCESS, null, null, slotMasterDtosList, null);
			}

			Integer count=slotMasterDtosList.size();

			return new Response(SUCCESS, null, null, null, count);

		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			throw e;
		}
//		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getSlotDetails(SlotMasterDto slotMasterDto) throws ApplicationException {
		try {
			//For doctor
			if(slotMasterDto.getSlotTypeId()==SlotTypeDoctor){

				StringBuilder stringQuery = new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_APPOINTMENTS_SLOT_DETAILS").getQueryString().toString());

				stringQuery.append(" AND sm.specialityId=:specialityId"
						+ " AND sm.doctorId=:docId "
						+ " ORDER BY sm.fromTime ");

				Query query = sessionFactory.getCurrentSession().createQuery(stringQuery.toString());

				List<SlotMasterDto> slotMasterDtosList = query
						.setParameter("orgId", slotMasterDto.getOrganizationId())
						.setParameter("unitId", slotMasterDto.getUnitId())
						.setParameter("slotTypeId", slotMasterDto.getSlotTypeId())
						.setParameter("specialityId", slotMasterDto.getSpecialityId())
						.setParameter("docId", slotMasterDto.getDoctorId())
						.setParameter("appointmentDate", GlobalCommonDateUtils.getDate(slotMasterDto.getAppointmentDate(),"yyyy-MM-dd"))
						//.setParameter("dayId", slotMasterDto.getDayId())
						.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
				return new Response(SUCCESS, null, null, slotMasterDtosList, null);
			}
			//For modality
			if(slotMasterDto.getSlotTypeId()==SlotTypeModality){

				StringBuilder stringQuery = new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_APPOINTMENTS_SLOT_DETAILS").getQueryString().toString());
				stringQuery.append(" AND sm.modalityId=:modalityId "
						+ " AND sm.subSpecialityId=:subSpecialityId "
						+ " AND sm.specialityId=:specialityId "
						+ " ORDER BY sm.fromTime ");
				Query query = sessionFactory.getCurrentSession().createQuery(stringQuery.toString());
				if (slotMasterDto != null) {
				List<SlotMasterDto> slotMasterDtosList = query
						.setParameter("orgId", slotMasterDto.getOrganizationId())
						.setParameter("unitId", slotMasterDto.getUnitId())
						.setParameter("slotTypeId", slotMasterDto.getSlotTypeId())
						.setParameter("modalityId", slotMasterDto.getModalityId())

						.setParameter("specialityId",slotMasterDto.getSpecialityId())
						.setParameter("subSpecialityId", slotMasterDto.getSubSpecialityId())
						.setParameter("appointmentDate", GlobalCommonDateUtils.getDate(slotMasterDto.getAppointmentDate(),"yyyy-MM-dd"))
						//.setParameter("dayId", slotMasterDto.getDayId()!=null ? slotMasterDto.getDayId() : 0)
						.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
				return new Response(SUCCESS, null, null, slotMasterDtosList, null);
				}
			}
			//For ECH
			if(slotMasterDto.getSlotTypeId()==SlotTypeECH){
				StringBuilder stringQuery = new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_APPOINTMENTS_SLOT_DETAILS").getQueryString().toString());
				stringQuery.append(" ORDER BY sm.slotId ");
				Query query = sessionFactory.getCurrentSession().createQuery(stringQuery.toString());

				List<SlotMasterDto> slotMasterDtosList = query
						.setParameter("orgId", slotMasterDto.getOrganizationId())
						.setParameter("unitId", slotMasterDto.getUnitId())
						.setParameter("slotTypeId", slotMasterDto.getSlotTypeId())
						.setParameter("appointmentDate", GlobalCommonDateUtils.getDate(slotMasterDto.getAppointmentDate(),"yyyy-MM-dd"))
						//.setParameter("dayId", slotMasterDto.getDayId())
						.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
				return new Response(SUCCESS, null, null, slotMasterDtosList, null);
			}
			//For dept wise
			if(slotMasterDto.getSlotTypeId()==4){

				StringBuilder stringQuery = new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_APPOINTMENTS_SLOT_DETAILS").getQueryString().toString());

				stringQuery.append(" AND sm.specialityId=:specialityId"
						+ " AND sm.doctorId is null "
						+ " ORDER BY sm.fromTime ");

				Query query = sessionFactory.getCurrentSession().createQuery(stringQuery.toString());

				List<SlotMasterDto> slotMasterDtosList = query
						.setParameter("orgId", slotMasterDto.getOrganizationId())
						.setParameter("unitId", slotMasterDto.getUnitId())
						.setParameter("slotTypeId", slotMasterDto.getSlotTypeId())
						.setParameter("specialityId", slotMasterDto.getSpecialityId())
						.setParameter("appointmentDate", GlobalCommonDateUtils.getDate(slotMasterDto.getAppointmentDate(),"yyyy-MM-dd"))
					//	.setParameter("dayId", slotMasterDto.getDayId())
						.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
				return new Response(SUCCESS, null, null, slotMasterDtosList, null);
			}
			if(slotMasterDto.getSlotTypeId()==SlotTypeHemodialysis){

				StringBuilder stringQuery = new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_APPOINTMENTS_SLOT_DETAILS").getQueryString().toString());
				stringQuery.append(" AND sm.bedId=:bedId "
						+ " AND sm.dayId=:dayId "
						+ " ORDER BY sm.slotId");
				Query query = sessionFactory.getCurrentSession().createQuery(stringQuery.toString());

				List<SlotMasterDto> slotMasterDtosList = query
						.setParameter("orgId", slotMasterDto.getOrganizationId())
						.setParameter("unitId", slotMasterDto.getUnitId())
						.setParameter("slotTypeId", slotMasterDto.getSlotTypeId())
						.setParameter("bedId", slotMasterDto.getBedId())
						//.setParameter("specialityId",slotMasterDto.getSpecialityId())
						.setParameter("appointmentDate", GlobalCommonDateUtils.getDate(slotMasterDto.getAppointmentDate(),"yyyy-MM-dd"))
						.setParameter("dayId", slotMasterDto.getDayId())
						.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
				return new Response(SUCCESS, null, null, slotMasterDtosList, null);
			}
		} catch (HibernateException he) {
		logger.error("getSlotDetails failed", he);
		} catch (Exception e) {
		logger.error("getSlotDetails failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateSlotStatus(SlotMasterDto slotMasterDto) throws ApplicationException {
		try {
			List<SlotMasterDto> slotMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SLOT_STATUS")
					.setParameter("orgId", slotMasterDto.getOrganizationId())
					.setParameter("unitId", slotMasterDto.getUnitId())
					.setParameter("slotId", slotMasterDto.getSlotId())
					.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
			Character status = slotMasterDto.getStatus()!=null ? slotMasterDto.getStatus() : 'I';

			if(slotMasterDtosList.isEmpty())
			{

				SlotMaster master = findById(slotMasterDto.getSlotId());

				if(master.getSlotTypeId()==1)
				{
					if(status=='I')
					{
						List<ScheduleMasterDto> scheduleMasterDtosList = new ArrayList();

							ScheduleMasterDto dto = new ScheduleMasterDto();
								dto.setDoctorId(master.getDoctorId());
								dto.setSpecialityId(master.getSpecialityId());
								dto.setOrganisationId(master.getOrganizationId());
								dto.setUnitId(master.getUnitId());
								dto.setDeleteAllSlots('N');
								dto.setScheduleDate(GlobalCommonDateUtils.getStringDate(master.getAppointmentDate(),"dd-MM-yyyy"));
								dto.setFromTime(master.getFromTime().toString());
								dto.setToTime(master.getToTime().toString());
								dto.setAddRemove(1);
							scheduleMasterDtosList.add(dto);

						iDoctorScheduleDao.saveDoctorSchedule(scheduleMasterDtosList,"DELETE");
					}else{
						List<ScheduleMasterDto> scheduleMasterDtosList = new ArrayList();

							ScheduleMasterDto dto = new ScheduleMasterDto();
								dto.setDoctorId(master.getDoctorId());
								dto.setSpecialityId(master.getSpecialityId());
								dto.setOrganisationId(master.getOrganizationId());
								dto.setUnitId(master.getUnitId());
								dto.setDeleteAllSlots('N');
								dto.setScheduleDate(GlobalCommonDateUtils.getStringDate(master.getAppointmentDate(),"dd-MM-yyyy"));
								dto.setFromTime(master.getFromTime().toString());
								dto.setToTime(master.getToTime().toString());
								dto.setAddRemove(2);
							scheduleMasterDtosList.add(dto);

						iDoctorScheduleDao.saveDoctorSchedule(scheduleMasterDtosList,"DELETE");
					}

				}



				Query query=sessionFactory.getCurrentSession().createQuery("update SlotMaster sm set sm.status='"+status+"' "
						+ "WHERE sm.slotId=:slotId "
						+ "AND sm.organizationId=:orgId "
						+ "AND sm.unitId=:unitId ");
				query.setParameter("orgId", slotMasterDto.getOrganizationId());
				query.setParameter("unitId", slotMasterDto.getUnitId());
				query.setParameter("slotId", slotMasterDto.getSlotId());

				query.executeUpdate();

				return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
			}else
				return new Response(ERROR, COMMON_ERROR_CODE, "appointment is booked on this slot, can not deactive !!", null, null);

		} catch (HibernateException he) {
		logger.error("updateSlotStatus failed", he);
		} catch (Exception e) {
		logger.error("updateSlotStatus failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getYearlySlots(SlotMasterDto slotMasterDto) throws ApplicationException {
		try {

			if(slotMasterDto.getSlotTypeId()==SlotTypeDoctor){
				List<SlotMasterDto> yearlySlotsForDoctorsList = sessionFactory.getCurrentSession()
						.createQuery("SELECT DISTINCT(to_char(sm.appointmentDate, 'Mon yyyy')) as appointmentDate, "
								+ "to_char(sm.appointmentDate, 'MM-yyyy') as fromDate, 	"
								+ "to_char(sm.appointmentDate, 'yyyy') as yearSlots "
								+ "FROM SlotMaster sm "
								+ "WHERE sm.organizationId=:orgId "
								+ "AND sm.unitId=:unitId "
								+ "AND sm.slotTypeId=:slotTypeId "
								+ "AND sm.doctorId=:doctorId "
								+ "AND sm.specialityId=:specialityId "
									+ "AND sm.status='A' "
								+ "AND sm.appointmentDate >= '"+slotMasterDto.getAppointmentDate().substring(0,10).concat(" 00:00:00")+"' "
								+ "ORDER BY yearSlots,fromDate ")
						.setParameter("orgId", slotMasterDto.getOrganizationId())
						.setParameter("unitId", slotMasterDto.getUnitId())
						.setParameter("slotTypeId", slotMasterDto.getSlotTypeId())
						.setParameter("doctorId", slotMasterDto.getDoctorId())
						.setParameter("specialityId", slotMasterDto.getSpecialityId())
						//.setParameter("appointmentDate", slotMasterDto.getAppointmentDate())
						.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
				return new Response(SUCCESS, null, null, yearlySlotsForDoctorsList, null);
			}
			if(slotMasterDto.getSlotTypeId()==SlotTypeModality){
				List<SlotMasterDto> yearlySlotsForModalitysList = sessionFactory.getCurrentSession()
						.createQuery("SELECT DISTINCT(to_char(sm.appointmentDate, 'Mon yyyy')) as appointmentDate, "
								+ "to_char(sm.appointmentDate, 'MM-yyyy') as fromDate, 	"
								+ "to_char(sm.appointmentDate, 'yyyy') as yearSlots "
								+ "FROM SlotMaster sm "
								+ "WHERE sm.organizationId=:orgId "
								+ "AND sm.unitId=:unitId "
								+ "AND sm.slotTypeId=:slotTypeId "
								+ "AND sm.modalityId=:modalityId "
								+ "AND sm.subSpecialityId=:subSpecialityId "
								+ "AND sm.specialityId=:specialityId "
								+ "AND sm.status='A' "
								+ "AND sm.appointmentDate >= '"+slotMasterDto.getAppointmentDate()+"' "
								+ "ORDER BY yearSlots,fromDate ")
						.setParameter("orgId", slotMasterDto.getOrganizationId())
						.setParameter("unitId", slotMasterDto.getUnitId())
						.setParameter("slotTypeId", slotMasterDto.getSlotTypeId())
						.setParameter("modalityId", slotMasterDto.getModalityId())
						.setParameter("specialityId",slotMasterDto.getSpecialityId())
						.setParameter("subSpecialityId", slotMasterDto.getSubSpecialityId())
						//.setParameter("appointmentDate", slotMasterDto.getAppointmentDate())
						.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
				return new Response(SUCCESS, null, null, yearlySlotsForModalitysList, null);
			}
			if(slotMasterDto.getSlotTypeId()==SlotTypeECH){
				List<SlotMasterDto> yearlySlotsForECHsList = sessionFactory.getCurrentSession()
						.createQuery("SELECT DISTINCT(to_char(sm.appointmentDate, 'Mon yyyy')) as appointmentDate, "
								+ "to_char(sm.appointmentDate, 'MM-yyyy') as fromDate, 	"
								+ "to_char(sm.appointmentDate, 'yyyy') as yearSlots "
								+ "FROM SlotMaster sm "
								+ "WHERE sm.organizationId=:orgId "
								+ "AND sm.unitId=:unitId "
								+ "AND sm.slotTypeId=:slotTypeId "
								+ "AND sm.status='A' "
								+ "AND sm.appointmentDate >= '"+slotMasterDto.getAppointmentDate().substring(0, 10).concat(" 00:00:00")+"' "
								+ "ORDER BY yearSlots,fromDate ")
						.setParameter("orgId", slotMasterDto.getOrganizationId())
						.setParameter("unitId", slotMasterDto.getUnitId())
						.setParameter("slotTypeId", slotMasterDto.getSlotTypeId())
						//.setParameter("appointmentDate", slotMasterDto.getAppointmentDate())
						.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
				return new Response(SUCCESS, null, null, yearlySlotsForECHsList, null);

			}
			if(slotMasterDto.getSlotTypeId()==4){
				List<SlotMasterDto> yearlySlotsForDoctorsList = sessionFactory.getCurrentSession()
						.createQuery("SELECT DISTINCT(to_char(sm.appointmentDate, 'Mon yyyy')) as appointmentDate, "
								+ "to_char(sm.appointmentDate, 'MM-yyyy') as fromDate, 	"
								+ "to_char(sm.appointmentDate, 'yyyy') as yearSlots "
								+ "FROM SlotMaster sm "
								+ "WHERE sm.organizationId=:orgId "
								+ "AND sm.unitId=:unitId "
								+ "AND sm.slotTypeId=:slotTypeId "
								+ "AND sm.doctorId is null "
								+ "AND sm.specialityId=:specialityId "
								+ "AND sm.status='A' "
								+ "AND sm.appointmentDate >= '"+slotMasterDto.getAppointmentDate().substring(0,10).concat(" 00:00:00")+"' "
								+ "ORDER BY yearSlots,fromDate ")
						.setParameter("orgId", slotMasterDto.getOrganizationId())
						.setParameter("unitId", slotMasterDto.getUnitId())
						.setParameter("slotTypeId", slotMasterDto.getSlotTypeId())
						.setParameter("specialityId", slotMasterDto.getSpecialityId())
						.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
				return new Response(SUCCESS, null, null, yearlySlotsForDoctorsList, null);
			}
			if(slotMasterDto.getSlotTypeId()== 8){
				List<SlotMasterDto> yearlySlotsForModalitysList = sessionFactory.getCurrentSession()
						.createQuery("SELECT DISTINCT(to_char(sm.appointmentDate, 'Mon yyyy')) as appointmentDate, "
								+ "to_char(sm.appointmentDate, 'MM-yyyy') as fromDate, 	"
								+ "to_char(sm.appointmentDate, 'yyyy') as yearSlots "
								+ "FROM SlotMaster sm "
								+ "WHERE sm.organizationId=:orgId "
								+ "AND sm.unitId=:unitId "
								+ "AND sm.slotTypeId=:slotTypeId "
								+ "AND sm.bedId=:bedId "
								+ "AND sm.specialityId=:specialityId "
								+ "AND sm.status='A' "
								+ "AND sm.appointmentDate >= '"+slotMasterDto.getAppointmentDate()+"' "
								+ "ORDER BY yearSlots,fromDate ")
						.setParameter("orgId", slotMasterDto.getOrganizationId())
						.setParameter("unitId", slotMasterDto.getUnitId())
						.setParameter("slotTypeId", slotMasterDto.getSlotTypeId())
						.setParameter("bedId", slotMasterDto.getBedId())
						.setParameter("specialityId",slotMasterDto.getSpecialityId())
						//.setParameter("appointmentDate", slotMasterDto.getAppointmentDate())
						.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
				return new Response(SUCCESS, null, null, yearlySlotsForModalitysList, null);
			}

		} catch (HibernateException he) {
		logger.error("getYearlySlots failed", he);
		} catch (Exception e) {
		logger.error("getYearlySlots failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public List<SlotMasterDto> getAppointmentsForMonth(SlotMasterDto slotMasterDto) throws ApplicationException {
		try {

		if(slotMasterDto.getSlotTypeId()==SlotTypeDoctor){
				List<SlotMasterDto> slotMasterDtosList = sessionFactory.getCurrentSession()
						.createQuery("SELECT DISTINCT to_char(sm.appointmentDate,'yyyy-mm-dd') as appointmentDate "
								+ "FROM SlotMaster sm "
								+ "WHERE sm.organizationId=:orgId "
								+ "AND sm.unitId=:unitId "
								+ "AND sm.slotTypeId=:slotTypeId "
								+ "AND sm.doctorId=:doctorId "
								+ "AND sm.specialityId=:specialityId "
								+ "AND sm.status='A' "
								+ "AND sm.appointmentDate BETWEEN :fromDate AND :toDate ")
						.setParameter("orgId", slotMasterDto.getOrganizationId())
						.setParameter("unitId", slotMasterDto.getUnitId())
						.setParameter("slotTypeId", slotMasterDto.getSlotTypeId())
						.setParameter("fromDate", GlobalCommonDateUtils.getDate(slotMasterDto.getFromDate(),"yyyy-M-dd"))
						.setParameter("toDate", GlobalCommonDateUtils.getDate(slotMasterDto.getToDate(),"yyyy-M-dd"))
						.setParameter("doctorId", slotMasterDto.getDoctorId())
						.setParameter("specialityId", slotMasterDto.getSpecialityId())
						.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();

				return slotMasterDtosList;
			}
			if(slotMasterDto.getSlotTypeId()==SlotTypeModality){
				List<SlotMasterDto> slotMasterDtosList = sessionFactory.getCurrentSession()
						.createQuery("SELECT DISTINCT to_char(sm.appointmentDate,'yyyy-mm-dd') as appointmentDate "
								+ "FROM SlotMaster sm "
								+ "WHERE sm.organizationId=:orgId "
								+ "AND sm.unitId=:unitId "
								+ "AND sm.slotTypeId=:slotTypeId "
								+ "AND sm.modalityId=:modalityId "
								+ "AND sm.subSpecialityId=:subSpecialityId "
								+ "AND sm.specialityId=:specialityId "
								+ "AND sm.status='A' "
								+ "AND sm.appointmentDate BETWEEN :fromDate AND :toDate ")
						.setParameter("orgId", slotMasterDto.getOrganizationId())
						.setParameter("unitId", slotMasterDto.getUnitId())
						.setParameter("slotTypeId", slotMasterDto.getSlotTypeId())
						.setParameter("modalityId", slotMasterDto.getModalityId())
						.setParameter("specialityId",slotMasterDto.getSpecialityId())
						.setParameter("subSpecialityId", slotMasterDto.getSubSpecialityId())
						.setParameter("fromDate", GlobalCommonDateUtils.getDate(slotMasterDto.getFromDate(),"yyyy-M-dd"))
						.setParameter("toDate", GlobalCommonDateUtils.getDate(slotMasterDto.getToDate(),"yyyy-M-dd"))
						.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
				//System.out.println("F632Rom"+GlobalCommonDateUtils.getDate(slotMasterDto.getFromDate(),"yyyy-M-dd"));
				//System.out.println("TO"+GlobalCommonDateUtils.getDate(slotMasterDto.getToDate(),"yyyy-M-dd"));

				return slotMasterDtosList;
			}
			if(slotMasterDto.getSlotTypeId()==SlotTypeECH){
				List<SlotMasterDto> slotMasterDtosList = sessionFactory.getCurrentSession()
						.createQuery("SELECT DISTINCT to_char(sm.appointmentDate,'yyyy-mm-dd') as appointmentDate "
								+ "FROM SlotMaster sm "
								+ "WHERE sm.organizationId=:orgId "
								+ "AND sm.unitId=:unitId "
								+ "AND sm.slotTypeId=:slotTypeId "
								+ "AND sm.status='A' "
								+ "AND sm.appointmentDate BETWEEN :fromDate AND :toDate ")
						.setParameter("orgId", slotMasterDto.getOrganizationId())
						.setParameter("unitId", slotMasterDto.getUnitId())
						.setParameter("slotTypeId", slotMasterDto.getSlotTypeId())
						.setParameter("fromDate", GlobalCommonDateUtils.getDate(slotMasterDto.getFromDate(),"yyyy-M-dd"))
						.setParameter("toDate", GlobalCommonDateUtils.getDate(slotMasterDto.getToDate(),"yyyy-M-dd"))
						.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();

				return slotMasterDtosList;
			}

			if(slotMasterDto.getSlotTypeId()== 4){
				List<SlotMasterDto> slotMasterDtosList = sessionFactory.getCurrentSession()
						.createQuery("SELECT DISTINCT to_char(sm.appointmentDate,'yyyy-mm-dd') as appointmentDate "
								+ "FROM SlotMaster sm "
								+ "WHERE sm.organizationId=:orgId "
								+ "AND sm.unitId=:unitId "
								+ "AND sm.slotTypeId=:slotTypeId "
								+ "AND sm.doctorId is null "
								+ "AND sm.specialityId=:specialityId "
								+ "AND sm.status='A' "
								+ "AND sm.appointmentDate BETWEEN :fromDate AND :toDate ")
						.setParameter("orgId", slotMasterDto.getOrganizationId())
						.setParameter("unitId", slotMasterDto.getUnitId())
						.setParameter("slotTypeId", slotMasterDto.getSlotTypeId())
						.setParameter("fromDate", GlobalCommonDateUtils.getDate(slotMasterDto.getFromDate(),"yyyy-M-dd"))
						.setParameter("toDate", GlobalCommonDateUtils.getDate(slotMasterDto.getToDate(),"yyyy-M-dd"))
						.setParameter("specialityId", slotMasterDto.getSpecialityId())
						.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();

				return slotMasterDtosList;
			}


			if(slotMasterDto.getSlotTypeId()== 8){
				//System.out.println("**********");
				//System.out.println("FRom"+GlobalCommonDateUtils.getDate(slotMasterDto.getFromDate(),"yyyy-M-dd"));
				//System.out.println("TO"+GlobalCommonDateUtils.getDate(slotMasterDto.getToDate(),"yyyy-M-dd"));
				List<SlotMasterDto> slotMasterDtosList = sessionFactory.getCurrentSession()
						.createQuery("SELECT DISTINCT to_char(sm.appointmentDate,'yyyy-mm-dd') as appointmentDate "
								+ "FROM SlotMaster sm "
								+ "WHERE sm.organizationId=:orgId "
								+ "AND sm.unitId=:unitId "
								+ "AND sm.slotTypeId=:slotTypeId "
								+ "AND sm.bedId=:bedId "
								+ "AND sm.specialityId=:specialityId "
								+ "AND sm.status='A' "
								+ "AND sm.appointmentDate BETWEEN :fromDate AND :toDate ")
						.setParameter("orgId", slotMasterDto.getOrganizationId())
						.setParameter("unitId", slotMasterDto.getUnitId())
						.setParameter("slotTypeId", slotMasterDto.getSlotTypeId())
						.setParameter("bedId", slotMasterDto.getBedId())
						.setParameter("specialityId",slotMasterDto.getSpecialityId())
						.setParameter("fromDate", GlobalCommonDateUtils.getDate(slotMasterDto.getFromDate(),"yyyy-M-dd"))
						.setParameter("toDate", GlobalCommonDateUtils.getDate(slotMasterDto.getToDate(),"yyyy-M-dd"))
						.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();

				return slotMasterDtosList;
			}

		} catch (HibernateException he) {
		logger.error("getAppointmentsForMonth failed", he);
		} catch (Exception e) {
		logger.error("getAppointmentsForMonth failed", e);
		}
		return null;
	}


	@Override
	public Response getDailySlots(SlotMasterDto slotMasterDto) throws ApplicationException {
		try {

			// For doctor
			if (slotMasterDto.getSlotTypeId() == SlotTypeDoctor) {
				StringBuilder stringQuery = new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_APPOINTMENTS_SLOTS_BY_DATE").getQueryString().toString());
				stringQuery.append(" AND sm.speciality_id=:specialityId "
						+" AND sm.doctor_id=:docId "
						+" AND sm.appointment_date=:appointmentDate "
						+" ORDER BY sm.from_time ");
				SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(stringQuery.toString());

				List<SlotMasterDto> slotMasterDtosList = query
						.setParameter("orgId", slotMasterDto.getOrganizationId())
						.setParameter("unitId", slotMasterDto.getUnitId())
						.setParameter("slotTypeId", slotMasterDto.getSlotTypeId())
						.setParameter("specialityId", slotMasterDto.getSpecialityId())
						.setParameter("docId", slotMasterDto.getDoctorId())
						.setParameter("appointmentDate",GlobalCommonDateUtils.getDate(slotMasterDto.getAppointmentDate(), "yyyy-M-dd"))
						.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
				return new Response(SUCCESS, null, null, slotMasterDtosList, null);
			}
			// For modality
			if (slotMasterDto.getSlotTypeId() == SlotTypeModality) {
				StringBuilder stringQuery = new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_APPOINTMENTS_SLOTS_BY_DATE").getQueryString().toString());
				stringQuery.append(" AND sm.modality_id=:modalityId "
						+ "AND sm.sub_speciality_id=:subSpecialityId "
						+ "AND sm.speciality_id=:specialityId "
						+ " AND sm.appointment_date=:appointmentDate "
						+ " AND sm.visit_type_id =:visitTypeId "
						+ " ORDER BY sm.from_time ");
				SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(stringQuery.toString());

				List<SlotMasterDto> slotMasterDtosList = query.setParameter("orgId", slotMasterDto.getOrganizationId())
						.setParameter("visitTypeId", slotMasterDto.getVisitTypeId())
						.setParameter("unitId", slotMasterDto.getUnitId())
						.setParameter("slotTypeId", slotMasterDto.getSlotTypeId())
						.setParameter("modalityId", slotMasterDto.getModalityId())
						.setParameter("specialityId",slotMasterDto.getSpecialityId())
						.setParameter("subSpecialityId", slotMasterDto.getSubSpecialityId())
						.setParameter("appointmentDate",	GlobalCommonDateUtils.getDate(slotMasterDto.getAppointmentDate(), "yyyy-M-dd"))
						.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
				return new Response(SUCCESS, null, null, slotMasterDtosList, null);
			}
			// For ECH
			if (slotMasterDto.getSlotTypeId() == SlotTypeECH) {

				StringBuilder stringQuery = new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_APPOINTMENTS_SLOTS_BY_DATE").getQueryString().toString());
				stringQuery.append(" AND sm.appointment_date=:appointmentDate "
						+ " ORDER BY sm.slot_id");
				SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(stringQuery.toString());

				List<SlotMasterDto> slotMasterDtosList = query
						.setParameter("orgId", slotMasterDto.getOrganizationId())
						.setParameter("unitId", slotMasterDto.getUnitId())
						.setParameter("slotTypeId", slotMasterDto.getSlotTypeId())
						.setParameter("appointmentDate",	GlobalCommonDateUtils.getDate(slotMasterDto.getAppointmentDate(), "yyyy-M-dd"))
						.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
				return new Response(SUCCESS, null, null, slotMasterDtosList, null);
			}
			// For dept scheduling
			if (slotMasterDto.getSlotTypeId() == 4) {
				StringBuilder stringQuery = new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_APPOINTMENTS_SLOTS_BY_DATE").getQueryString().toString());
				stringQuery.append(" AND sm.speciality_id=:specialityId "
						+" AND sm.appointment_date=:appointmentDate "
						+" AND sm.doctor_id is null "
						+" ORDER BY sm.from_time ");
				SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(stringQuery.toString());

				List<SlotMasterDto> slotMasterDtosList = query
						.setParameter("orgId", slotMasterDto.getOrganizationId())
						.setParameter("unitId", slotMasterDto.getUnitId())
						.setParameter("slotTypeId", slotMasterDto.getSlotTypeId())
						.setParameter("specialityId", slotMasterDto.getSpecialityId())
						.setParameter("appointmentDate",GlobalCommonDateUtils.getDate(slotMasterDto.getAppointmentDate(), "yyyy-M-dd"))
						.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
				return new Response(SUCCESS, null, null, slotMasterDtosList, null);
			}
			if (slotMasterDto.getSlotTypeId() == 8) {
				StringBuilder stringQuery = new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_APPOINTMENTS_SLOTS_BY_DATE").getQueryString().toString());

				if(slotMasterDto.getBedId()!=null){
				stringQuery.append(" AND "
						+ " sm.speciality_id=:specialityId "
						+ " AND sm.appointment_date=:appointmentDate "
						+ " AND sm.bed_id=:bedId "
						+ " ORDER BY sm.slot_id");
				}else{
					stringQuery.append(" AND "
							+ " sm.speciality_id=:specialityId "
							+ " AND sm.appointment_date=:appointmentDate "
							+ " ORDER BY sm.slot_id");
				}

				SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(stringQuery.toString());

				List<SlotMasterDto> slotMasterDtosList;
				if(slotMasterDto.getBedId()!=null){
				slotMasterDtosList = query.setParameter("orgId", slotMasterDto.getOrganizationId())
						.setParameter("unitId", slotMasterDto.getUnitId())
						.setParameter("slotTypeId", slotMasterDto.getSlotTypeId())
						.setParameter("specialityId",slotMasterDto.getSpecialityId())
						.setParameter("bedId",slotMasterDto.getBedId())
						.setParameter("appointmentDate",	GlobalCommonDateUtils.getDate(slotMasterDto.getAppointmentDate(), "yyyy-M-dd"))
						.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
				}else{
					slotMasterDtosList = query.setParameter("orgId", slotMasterDto.getOrganizationId())
							.setParameter("unitId", slotMasterDto.getUnitId())
							.setParameter("slotTypeId", slotMasterDto.getSlotTypeId())
							.setParameter("specialityId",slotMasterDto.getSpecialityId())
							.setParameter("appointmentDate",	GlobalCommonDateUtils.getDate(slotMasterDto.getAppointmentDate(), "yyyy-M-dd"))
							.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
				}
				return new Response(SUCCESS, null, null, slotMasterDtosList, null);
			}
			if (slotMasterDto.getSlotTypeId() == SlotForCyclic) {

				StringBuilder stringQuery = new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_APPOINTMENTS_SLOTS_BY_DATE").getQueryString().toString());
				stringQuery.append(" AND sm.speciality_id=:specialityId "
						//+" AND sm.doctor_id=:docId "
						+" AND sm.appointment_date=:appointmentDate "
						//+" AND sm.from_time BETWEEN '"+slotMasterDto.getFromTime()+"' AND '" +slotMasterDto.getToTime()+"' "
						+" AND sm.from_time BETWEEN :fromTime AND :toTime "
						);

				if(slotMasterDto.getDeptWiseAppointment().equals('N')) {
					if(slotMasterDto.getDoctorId()>0 && slotMasterDto.getDoctorId()!=null) {
						stringQuery.append(" AND sm.doctor_id = :doctorId ");
						slotMasterDto.setSlotTypeId(1);
					}
			    }else {
					slotMasterDto.setSlotTypeId(4);
				}

				stringQuery.append(" ORDER BY sm.slot_id ");
				SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(stringQuery.toString());

				 query.setParameter("orgId", slotMasterDto.getOrganizationId())
						.setParameter("unitId", slotMasterDto.getUnitId())
						.setParameter("slotTypeId", slotMasterDto.getSlotTypeId())
						.setParameter("appointmentDate", GlobalCommonDateUtils.getDate(slotMasterDto.getAppointmentDate(), "yyyy-M-dd"))
						.setParameter("specialityId", slotMasterDto.getSpecialityId())
				        .setParameter("fromTime", slotMasterDto.getFromTime())
						.setParameter("toTime", slotMasterDto.getToTime());
						//.setParameter("docId", slotMasterDto.getDoctorId())

				if(slotMasterDto.getDeptWiseAppointment().equals('N')) {
					if(slotMasterDto.getDoctorId()>0 && slotMasterDto.getDoctorId()!=null) {
					query.setParameter("doctorId", slotMasterDto.getDoctorId());
				}}

				List<SlotMasterDto> slotMasterDtosList =query.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
				return new Response(SUCCESS, null, null, slotMasterDtosList, null);
			}
		} catch (HibernateException he) {
		logger.error("getDailySlots failed", he);
		} catch (Exception e) {
		logger.error("getDailySlots failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getWeeklySlots(SlotMasterDto slotMasterDto) throws ApplicationException {
		try{
			// For doctor
			if (slotMasterDto.getSlotTypeId() == SlotTypeDoctor) {

				StringBuilder stringQuery = new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_APPOINTMENTS_SLOTS_BY_DATE").getQueryString().toString());
				stringQuery.append(" AND sm.speciality_id=:specialityId "
						+" AND sm.doctor_id=:docId "
						+" AND sm.appointment_date BETWEEN :fromDate AND :toDate "
						+" ORDER BY sm.from_time ");
				SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(stringQuery.toString());

				List<SlotMasterDto> slotMasterDtosList = query
						.setParameter("orgId", slotMasterDto.getOrganizationId())
						.setParameter("unitId", slotMasterDto.getUnitId())
						.setParameter("slotTypeId", slotMasterDto.getSlotTypeId())
						.setParameter("specialityId", slotMasterDto.getSpecialityId())
						.setParameter("docId", slotMasterDto.getDoctorId())
						.setParameter("fromDate",GlobalCommonDateUtils.getDate(slotMasterDto.getFromDate(), "yyyy-M-dd"))
						.setParameter("toDate",GlobalCommonDateUtils.getDate(slotMasterDto.getToDate(), "yyyy-M-dd"))
						.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
				return new Response(SUCCESS, null, null, slotMasterDtosList, null);
			}
			// For dept scheduling
			if (slotMasterDto.getSlotTypeId() == 4) {

				StringBuilder stringQuery = new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_APPOINTMENTS_SLOTS_BY_DATE").getQueryString().toString());
				stringQuery.append(" AND sm.speciality_id=:specialityId "
						+" AND sm.doctor_id is null "
						+" AND sm.appointment_date BETWEEN :fromDate AND :toDate "
						+" ORDER BY sm.from_time ");
				SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(stringQuery.toString());

				List<SlotMasterDto> slotMasterDtosList = query
						.setParameter("orgId", slotMasterDto.getOrganizationId())
						.setParameter("unitId", slotMasterDto.getUnitId())
						.setParameter("slotTypeId", slotMasterDto.getSlotTypeId())
						.setParameter("specialityId", slotMasterDto.getSpecialityId())
						.setParameter("fromDate",GlobalCommonDateUtils.getDate(slotMasterDto.getFromDate(), "yyyy-M-dd"))
						.setParameter("toDate",GlobalCommonDateUtils.getDate(slotMasterDto.getToDate(), "yyyy-M-dd"))
						.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
				return new Response(SUCCESS, null, null, slotMasterDtosList, null);
			}
			// For modality
			if (slotMasterDto.getSlotTypeId() == SlotTypeModality) {

				StringBuilder stringQuery = new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_APPOINTMENTS_SLOTS_BY_DATE").getQueryString().toString());
				stringQuery.append(" AND sm.modality_id=:modalityId "
						+" AND sm.speciality_id=:specialityId "
						+" AND sm.sub_speciality_id=:subSpecialityId "
						+" AND sm.appointment_date BETWEEN :fromDate AND :toDate "
						+" ORDER BY sm.from_time ");
				SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(stringQuery.toString());

				List<SlotMasterDto> slotMasterDtosList = query
						.setParameter("orgId", slotMasterDto.getOrganizationId())
						.setParameter("unitId", slotMasterDto.getUnitId())
						.setParameter("slotTypeId", slotMasterDto.getSlotTypeId())
						.setParameter("modalityId", slotMasterDto.getModalityId())
						.setParameter("specialityId", slotMasterDto.getSpecialityId())
						.setParameter("subSpecialityId", slotMasterDto.getSubSpecialityId())
						.setParameter("fromDate",GlobalCommonDateUtils.getDate(slotMasterDto.getFromDate(), "yyyy-M-dd"))
						.setParameter("toDate",GlobalCommonDateUtils.getDate(slotMasterDto.getToDate(), "yyyy-M-dd"))
						.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
				return new Response(SUCCESS, null, null, slotMasterDtosList, null);
			}
			// For EHC
			if (slotMasterDto.getSlotTypeId() == SlotTypeECH) {

				StringBuilder stringQuery = new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_APPOINTMENTS_SLOTS_BY_DATE").getQueryString().toString());
				stringQuery.append(" AND sm.appointment_date BETWEEN :fromDate AND :toDate "
						+ "ORDER BY sm.slot_id");
				SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(stringQuery.toString());

				List<SlotMasterDto> slotMasterDtosList = query.setParameter("orgId", slotMasterDto.getOrganizationId())
						.setParameter("unitId", slotMasterDto.getUnitId())
						.setParameter("slotTypeId", slotMasterDto.getSlotTypeId())
						.setParameter("fromDate",GlobalCommonDateUtils.getDate(slotMasterDto.getFromDate(), "yyyy-M-dd"))
						.setParameter("toDate",GlobalCommonDateUtils.getDate(slotMasterDto.getToDate(), "yyyy-M-dd"))
						.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
				return new Response(SUCCESS, null, null, slotMasterDtosList, null);
			}
			if (slotMasterDto.getSlotTypeId() == 8) {

				StringBuilder stringQuery = new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_APPOINTMENTS_SLOTS_BY_DATE").getQueryString().toString());
				stringQuery.append(" AND sm.bed_id=:bedId "
						+" AND sm.speciality_id=:specialityId "
						+" AND sm.appointment_date BETWEEN :fromDate AND :toDate "
						+" ORDER BY sm.slot_id");
				SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(stringQuery.toString());

				List<SlotMasterDto> slotMasterDtosList = query
						.setParameter("orgId", slotMasterDto.getOrganizationId())
						.setParameter("unitId", slotMasterDto.getUnitId())
						.setParameter("slotTypeId", slotMasterDto.getSlotTypeId())
						.setParameter("bedId", slotMasterDto.getBedId())
						.setParameter("specialityId", slotMasterDto.getSpecialityId())
						.setParameter("fromDate",GlobalCommonDateUtils.getDate(slotMasterDto.getFromDate(), "yyyy-M-dd"))
						.setParameter("toDate",GlobalCommonDateUtils.getDate(slotMasterDto.getToDate(), "yyyy-M-dd"))
						.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
				return new Response(SUCCESS, null, null, slotMasterDtosList, null);
			}
			//For Cyclic Appointments
			if (slotMasterDto.getSlotTypeId() == SlotForCyclic) {

				StringBuilder stringQuery = new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_APPOINTMENTS_SLOTS_BY_DATE").getQueryString().toString());
				stringQuery.append(" AND sm.speciality_id=:specialityId "
						+" AND sm.appointment_date BETWEEN :fromDate AND :toDate "
						+"AND sm.from_time BETWEEN CAST(:fromTime AS time) AND CAST(:toTime AS time) "
					);

				if(slotMasterDto.getDeptWiseAppointment().equals('N')) {
					if(slotMasterDto.getDoctorId()>0) {
						stringQuery.append(" AND sm.doctor_id = :doctorId ");
						slotMasterDto.setSlotTypeId(1);
					}
			    }else {
					slotMasterDto.setSlotTypeId(4);
				}
				stringQuery.append(" ORDER BY sm.appointment_date,sm.from_time ");

				SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(stringQuery.toString());
				DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

				String fromTimeStr = slotMasterDto.getFromTime().atZone(ZoneOffset.UTC).toLocalTime().format(timeFormatter);
				String toTimeStr = slotMasterDto.getToTime().atZone(ZoneOffset.UTC).toLocalTime().format(timeFormatter);
				query
						.setParameter("orgId", slotMasterDto.getOrganizationId())
						.setParameter("unitId", slotMasterDto.getUnitId())
						.setParameter("slotTypeId",slotMasterDto.getSlotTypeId())
						.setParameter("specialityId", slotMasterDto.getSpecialityId())
						.setParameter("fromDate",GlobalCommonDateUtils.getDate(slotMasterDto.getFromDate(), "yyyy-M-dd"))
						.setParameter("toDate",GlobalCommonDateUtils.getDate(slotMasterDto.getToDate(), "yyyy-M-dd"))
						.setParameter("fromTime", fromTimeStr)
						.setParameter("toTime", toTimeStr);

				if(slotMasterDto.getDeptWiseAppointment().equals('N')) {
					if(slotMasterDto.getDoctorId()>0) {
					query.setParameter("doctorId", slotMasterDto.getDoctorId());
				}}

				//.setParameter("docId", slotMasterDto.getDoctorId())
				List<SlotMasterDto> slotMasterDtosList =query.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();

				StringBuilder appointmentDateListQuery = new StringBuilder(" select to_char(sm.appointment_date,'DD-MM-YYYY') from opd.m_slot_master sm "
						+ " where sm.speciality_id=:specialityId AND sm.slot_type_id=:slotTypeId AND sm.organization_id=:orgId AND sm.unit_id=:unitId "
						+" AND sm.appointment_date BETWEEN :fromDate AND :toDate "
						+"AND sm.from_time BETWEEN CAST(:fromTime AS time) AND CAST(:toTime AS time) ");

				if(slotMasterDto.getDeptWiseAppointment().equals('N')) {
					if(slotMasterDto.getDoctorId()>0) {
						appointmentDateListQuery.append(" AND sm.doctor_id = :doctorId ");
						slotMasterDto.setSlotTypeId(1);
					}
			    }
				appointmentDateListQuery.append(" group by sm.appointment_date  ");
				SQLQuery query1 = sessionFactory.getCurrentSession().createSQLQuery(appointmentDateListQuery.toString());

				//List<String> appointmentDateList=sessionFactory.getCurrentSession().createSQLQuery(appointmentDateListQuery.toString())
				query1.setParameter("slotTypeId", slotMasterDto.getSlotTypeId());
				query1.setParameter("specialityId", slotMasterDto.getSpecialityId());
				query1.setParameter("orgId", slotMasterDto.getOrganizationId());
				query1.setParameter("unitId", slotMasterDto.getUnitId());
				query1.setParameter("fromDate",GlobalCommonDateUtils.getDate(slotMasterDto.getFromDate(), "yyyy-M-dd"));
				query1.setParameter("toDate",GlobalCommonDateUtils.getDate(slotMasterDto.getToDate(), "yyyy-M-dd"));
				query1.setParameter("fromTime", fromTimeStr);
				query1.setParameter("toTime", toTimeStr);

				if(slotMasterDto.getDeptWiseAppointment().equals('N')) {
					if(slotMasterDto.getDoctorId()>0) {
						query1.setParameter("doctorId", slotMasterDto.getDoctorId());
					}}
				List<String> appointmentDateList = query1.list();
				SlotMasterDto slotMasterObj=new SlotMasterDto();
				slotMasterObj.setAppointmentDateList(appointmentDateList);

				return new Response(SUCCESS, null, null, slotMasterDtosList, slotMasterObj);
			}
		} catch (HibernateException he) {
throw he;
		} catch (Exception e) {
			throw e;
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response blockUnblockSlots(BlockSlotReqDto blockSlotReqDto)
			throws ApplicationException {

		try{
			int updateCount = sessionFactory.getCurrentSession()
					.getNamedQuery("BLOCK_UNBLOCK_SLOT")
					.setParameter("isBlocked", blockSlotReqDto.getIsBlockUnblock())
					.setParameterList("listSlotId", blockSlotReqDto.getListSlotId())
					.executeUpdate();
			String msg = null;
			if(blockSlotReqDto.getIsBlockUnblock()==1)
			{
				msg ="Slot blocked Successfully...!!";
			}
			if(blockSlotReqDto.getIsBlockUnblock()==2)
			{
				msg ="Slot un-blocked Successfully...!!";
			}
			return new Response<>(SUCCESS, COMMON_SUCCESS, msg, null, updateCount);
		}catch(HibernateException he){
			throw he;
		}catch(ApplicationException ae){
			throw ae;
		}
	}

	@Override
	public Response getSlotsForBlockUnblock(
			SlotBlockUnblockRequestDto slotReqDto) throws ApplicationException {
		try {

			StringBuilder stringQuery = new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_SLOTS_FOR_BLOCK_UNBLOCK").getQueryString().toString());

			if(slotReqDto.getSlotTypeId()==1)
			{
				stringQuery.append(" AND sm.doctor_id IN (:listDoctorIds) ");

				if((slotReqDto.getFromTime() !=  null && !slotReqDto.getFromTime().isEmpty()) && (slotReqDto.getToTime()!=  null && !slotReqDto.getToTime().isEmpty()))
					stringQuery.append(" AND sm.from_time >= :fromTime AND sm.to_time <= :toTime ");

				stringQuery.append(" ORDER BY sm.from_time ");

			SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(stringQuery.toString());

		List<SlotMasterDto> slotMasterDtosList = query
					.setParameterList("listDoctorIds", slotReqDto.getListDoctorId())
					.setParameterList("listSpecialityIds", slotReqDto.getListSpecialityId())
					.setParameter("fromDate", (slotReqDto.getFromDate()!=null?GlobalCommonDateUtils.getDate(slotReqDto.getFromDate(), "yyyy-MM-dd"):TimeZoneComponent.getDateByzone(slotReqDto.getUnitId())))
					.setParameter("toDate", (slotReqDto.getToDate()!=null?GlobalCommonDateUtils.getDate(slotReqDto.getToDate(), "yyyy-MM-dd"):TimeZoneComponent.getDateByzone(slotReqDto.getUnitId())))
					.setParameter("orgId", slotReqDto.getOrganisationId())
					.setParameter("unitId", slotReqDto.getUnitId())
					.setParameter("slotTypeId", slotReqDto.getSlotTypeId())
					.setParameter("isBlocked", slotReqDto.getIsBlocked())
					.setParameter("fromTime", slotReqDto.getFromTime())
					.setParameter("toTime", slotReqDto.getToTime())
					.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
			return new Response(SUCCESS, null, null, slotMasterDtosList, null);
			}else if(slotReqDto.getSlotTypeId()==4){

				// BR-local-doctorwise-getslots-time-filter-crash: the original unconditional
				// .setParameter("fromTime"/"toTime", ...) below threw
				// org.hibernate.QueryParameterException whenever these were blank (the SQL
				// clause referencing :fromTime/:toTime is only appended when both are
				// non-empty, so the named parameter never existed in the compiled query), and
				// threw a PostgreSQL "operator does not exist: time without time zone >=
				// character varying" type-mismatch error whenever they WERE supplied (Hibernate
				// binds a Java String as varchar with no implicit cast to the `time` column).
				// Net effect on real, unmodified legacy: this endpoint's slotTypeId==4 branch
				// (used by doctorWise) returned status:"error" on every single search, in every
				// input combination, with the failure silently swallowed by the surrounding
				// try/catch and no error surfaced to the user (an empty results table is
				// indistinguishable from "no slots found"). Fixed narrowly for this branch only
				// (the sibling slotTypeId==1 and modality/sub-speciality branches below have the
				// identical unconditional-setParameter pattern and are NOT touched here -- out of
				// scope for this screen's rebuild; flagged separately as a cross-cutting finding).
				boolean hasTimeFilter = (slotReqDto.getFromTime() !=  null && !slotReqDto.getFromTime().isEmpty()) && (slotReqDto.getToTime()!=  null && !slotReqDto.getToTime().isEmpty());
				if(hasTimeFilter)
					stringQuery.append(" AND sm.from_time >= CAST(:fromTime AS time) AND sm.to_time <= CAST(:toTime AS time) ");

				stringQuery.append(" ORDER BY sm.from_time ");

			SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(stringQuery.toString());

			query.setParameterList("listSpecialityIds", slotReqDto.getListSpecialityId())
					.setParameter("fromDate", (slotReqDto.getFromDate()!=null?GlobalCommonDateUtils.getDate(slotReqDto.getFromDate(), "yyyy-MM-dd"):TimeZoneComponent.getDateByzone(slotReqDto.getUnitId())))
					.setParameter("toDate", (slotReqDto.getToDate()!=null?GlobalCommonDateUtils.getDate(slotReqDto.getToDate(), "yyyy-MM-dd"):TimeZoneComponent.getDateByzone(slotReqDto.getUnitId())))
					.setParameter("orgId", slotReqDto.getOrganisationId())
					.setParameter("unitId", slotReqDto.getUnitId())
					.setParameter("slotTypeId", slotReqDto.getSlotTypeId())
					.setParameter("isBlocked", slotReqDto.getIsBlocked());
			if(hasTimeFilter){
				query.setParameter("fromTime", slotReqDto.getFromTime())
						.setParameter("toTime", slotReqDto.getToTime());
			}
		List<SlotMasterDto> slotMasterDtosList = query
					.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
			return new Response(SUCCESS, null, null, slotMasterDtosList, null);

			}else{
				stringQuery.append(" AND sm.sub_speciality_id IN (:listSubSpecialityIds) "
						+ " AND sm.modality_id IN (:listModalityIds) ");

			if((slotReqDto.getFromTime() !=  null && !slotReqDto.getFromTime().isEmpty()) && (slotReqDto.getToTime()!=  null && !slotReqDto.getToTime().isEmpty()))
				stringQuery.append(" AND sm.from_time BETWEEN :fromTime AND :toTime ");

			SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(stringQuery.toString());

		List<SlotMasterDto> slotMasterDtosList = query
					.setParameterList("listModalityIds", slotReqDto.getListModalityId())
					.setParameterList("listSubSpecialityIds", slotReqDto.getListSubSpecialityId())
					.setParameterList("listSpecialityIds", slotReqDto.getListSpecialityId())
					.setParameter("fromDate", (slotReqDto.getFromDate()!=null?GlobalCommonDateUtils.getDate(slotReqDto.getFromDate(), "yyyy-MM-dd"):TimeZoneComponent.getDateByzone(slotReqDto.getUnitId())))
					.setParameter("toDate", (slotReqDto.getToDate()!=null?GlobalCommonDateUtils.getDate(slotReqDto.getToDate(), "yyyy-MM-dd"):TimeZoneComponent.getDateByzone(slotReqDto.getUnitId())))
					.setParameter("fromTime", slotReqDto.getFromTime())
					.setParameter("toTime", slotReqDto.getToTime())
					.setParameter("orgId", slotReqDto.getOrganisationId())
					.setParameter("unitId", slotReqDto.getUnitId())
					.setParameter("slotTypeId", slotReqDto.getSlotTypeId())
					.setParameter("isBlocked", slotReqDto.getIsBlocked())
					.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
			return new Response(SUCCESS, null, null, slotMasterDtosList, null);
			}

		} catch (HibernateException he) {
		logger.error("getSlotsForBlockUnblock failed", he);
		} catch (Exception e) {
		logger.error("getSlotsForBlockUnblock failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getBlockedUnblockedSlotDetails(SlotMasterDto slotMasterDto) throws ApplicationException {
		try {

		//For doctor
		if(slotMasterDto.getSlotTypeId()==SlotTypeDoctor){

			StringBuilder stringQuery = new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_APPOINTMENTS_SLOT_DETAILS").getQueryString().toString());

			if((slotMasterDto.getFromTime() !=  null ) && (slotMasterDto.getToTime()!=  null))
				stringQuery.append(" AND sm.fromTime BETWEEN :fromTime AND :toTime ");

			stringQuery.append(" AND sm.specialityId=:specialityId"
							 + " AND sm.doctorId=:docId "
							 + " AND sm.isBlocked=:isBlocked "
							 + " ORDER BY sm.slotId");

			Query query = sessionFactory.getCurrentSession().createQuery(stringQuery.toString());

			List<SlotMasterDto> slotMasterDtosList = query
					.setParameter("orgId", slotMasterDto.getOrganizationId())
					.setParameter("unitId", slotMasterDto.getUnitId())
					.setParameter("slotTypeId", slotMasterDto.getSlotTypeId())
					.setParameter("specialityId", slotMasterDto.getSpecialityId())
					.setParameter("isBlocked", slotMasterDto.getIsBlockedUnblocked())
					.setParameter("docId", slotMasterDto.getDoctorId())
					.setParameter("appointmentDate", GlobalCommonDateUtils.getDate(slotMasterDto.getAppointmentDate(),"yyyy-MM-dd"))
					.setParameter("fromTime", slotMasterDto.getFromTime())
					.setParameter("toTime", slotMasterDto.getToTime())
					//.setParameter("dayId", slotMasterDto.getDayId())
					.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
			return new Response(SUCCESS, null, null, slotMasterDtosList, null);
		}
		//For modality
		if(slotMasterDto.getSlotTypeId()==SlotTypeModality){

			StringBuilder stringQuery = new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_APPOINTMENTS_SLOT_DETAILS").getQueryString().toString());

			if((slotMasterDto.getFromTime() !=  null ) && (slotMasterDto.getToTime()!=  null))
				stringQuery.append(" AND sm.fromTime BETWEEN :fromTime AND :toTime ");

			stringQuery.append(" AND sm.modalityId=:modalityId "
							 + " AND sm.subSpecialityId=:subSpecialityId "
							 + " AND sm.specialityId=:specialityId "
							 + " AND sm.isBlocked=:isBlocked "
							 + " ORDER BY sm.slotId");
			Query query = sessionFactory.getCurrentSession().createQuery(stringQuery.toString());

			List<SlotMasterDto> slotMasterDtosList = query
					.setParameter("orgId", slotMasterDto.getOrganizationId())
					.setParameter("unitId", slotMasterDto.getUnitId())
					.setParameter("slotTypeId", slotMasterDto.getSlotTypeId())
					.setParameter("modalityId", slotMasterDto.getModalityId())
					.setParameter("specialityId",slotMasterDto.getSpecialityId())
					.setParameter("isBlocked", slotMasterDto.getIsBlockedUnblocked())
					.setParameter("subSpecialityId", slotMasterDto.getSubSpecialityId())
					.setParameter("appointmentDate", GlobalCommonDateUtils.getDate(slotMasterDto.getAppointmentDate(),"yyyy-MM-dd"))
					//.setParameter("dayId", slotMasterDto.getDayId())
					.setParameter("fromTime", slotMasterDto.getFromTime())
					.setParameter("toTime", slotMasterDto.getToTime())
					.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
			return new Response(SUCCESS, null, null, slotMasterDtosList, null);
		}
		//For ECH
		if(slotMasterDto.getSlotTypeId()==SlotTypeECH){
			StringBuilder stringQuery = new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_APPOINTMENTS_SLOT_DETAILS").getQueryString().toString());

			if((slotMasterDto.getFromTime() !=  null ) && (slotMasterDto.getToTime()!=  null))
				stringQuery.append(" AND sm.fromTime BETWEEN :fromTime AND :toTime ");

			stringQuery.append(" AND sm.isBlocked=:isBlocked "
							+  " ORDER BY sm.slotId");
			Query query = sessionFactory.getCurrentSession().createQuery(stringQuery.toString());

			List<SlotMasterDto> slotMasterDtosList = query
					.setParameter("orgId", slotMasterDto.getOrganizationId())
					.setParameter("unitId", slotMasterDto.getUnitId())
					.setParameter("isBlocked", slotMasterDto.getIsBlockedUnblocked())
					.setParameter("slotTypeId", slotMasterDto.getSlotTypeId())
					.setParameter("appointmentDate", GlobalCommonDateUtils.getDate(slotMasterDto.getAppointmentDate(),"yyyy-MM-dd"))
					//.setParameter("dayId", slotMasterDto.getDayId())
					.setParameter("fromTime", slotMasterDto.getFromTime())
					.setParameter("toTime", slotMasterDto.getToTime())
					.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
			return new Response(SUCCESS, null, null, slotMasterDtosList, null);
		}

	} catch (HibernateException he) {
	logger.error("getBlockedUnblockedSlotDetails failed", he);
	} catch (Exception e) {
	logger.error("getBlockedUnblockedSlotDetails failed", e);
	}
	return new Response(ERROR, null, null, null, null);

	}

	@Override
	public Response updateStatusOfSlot(SlotMasterDto slotMasterDto) throws ApplicationException {
	try{
		sessionFactory.getCurrentSession().createQuery("update SlotMaster sm set "
				+ "sm.slotStatusId=:slotStatusId, " //slot booked
				+ "sm.updatedBy=:updatedBy , "
				+ "sm.updatedDate=:updatedDate "
				+ "where sm.slotId=:slotId ")
			.setParameter("updatedBy", slotMasterDto.getUpdatedBy())
			.setParameter("updatedDate",GlobalCommonDateUtils.getDate(slotMasterDto.getUpdatedDate(),"dd-M-yyyy HH:mm:ss"))
			.setParameter("slotId",slotMasterDto.getSlotId())
			.setParameter("slotStatusId", slotMasterDto.getSlotStatusId())
		.executeUpdate();
		return new Response(SUCCESS, null, null, null, null);
	}catch(Exception e)
	{
	logger.error("updateStatusOfSlot failed", e);
	}
	return new Response(ERROR, null, null, null, null);
	}

	@Override
	public boolean isOverBookingAllowedOrNot(SlotMasterDto slotMasterDto) throws ApplicationException {
		try {

			WeeklySlotsDto dto = (WeeklySlotsDto)sessionFactory.getCurrentSession().getNamedQuery("IS_OVERBOOKING_ALLOWED_OR_NOT")
					.setParameter("slotId", slotMasterDto.getSlotId())
					.setResultTransformer(Transformers.aliasToBean(WeeklySlotsDto.class)).uniqueResult();

			return dto.isResult();

		} catch (HibernateException he) {
		logger.error("isOverBookingAllowedOrNot failed", he);
		} catch (Exception e) {
		logger.error("isOverBookingAllowedOrNot failed", e);
		}
		return false;
	}

	@Override
	public Response deactiveSlotsDateAndDoctorWise(SlotMasterDto slotMasterDto) throws ApplicationException {
		try{
			if(slotMasterDto.getSlotTypeId()==SlotTypeDoctor){
			sessionFactory.getCurrentSession().createQuery("delete from SlotMaster sm where "
					+ " sm.doctorId=:doctorId "
					+ "AND sm.specialityId=:specialityId "
					+ "AND sm.organizationId=:orgId "
					+ "AND sm.unitId=:unitId "
					+ "AND sm.slotTypeId=:slotTypeId ")
				.setParameter("doctorId",slotMasterDto.getDoctorId())
				.setParameter("specialityId", slotMasterDto.getSpecialityId())
				.setParameter("orgId", slotMasterDto.getOrganizationId())
				.setParameter("unitId", slotMasterDto.getUnitId())
				.setParameter("slotTypeId", slotMasterDto.getSlotTypeId())
			.executeUpdate();


			List<ScheduleMasterDto> scheduleMasterDtosList = new ArrayList();

				ScheduleMasterDto dto = new ScheduleMasterDto();
					dto.setDoctorId(slotMasterDto.getDoctorId());
					dto.setSpecialityId(slotMasterDto.getSpecialityId());
					dto.setOrganisationId(slotMasterDto.getOrganizationId());
					dto.setUnitId(slotMasterDto.getUnitId());
					dto.setDeleteAllSlots('Y');
					dto.setAddRemove(1);
					//dto.setFromDate(fromDate);
					//dto.setToDate(toDate);
				scheduleMasterDtosList.add(dto);

			iDoctorScheduleDao.saveDoctorSchedule(scheduleMasterDtosList,"DELETE");

			}
			if(slotMasterDto.getSlotTypeId()==4){
				sessionFactory.getCurrentSession().createQuery("delete from SlotMaster sm where "
						+ "sm.specialityId=:specialityId "
						//+ "AND sm.appointmentDate=:appointmentDate "
						+ "AND sm.organizationId=:orgId "
						+ "AND sm.unitId=:unitId "
						+ "AND sm.slotTypeId=:slotTypeId "
						+ "and sm.slotId not in (select slotId from AppointmentMaster where slotId is not null and unitId =:unitId) ")
					.setParameter("specialityId", slotMasterDto.getSpecialityId())
					.setParameter("orgId", slotMasterDto.getOrganizationId())
					.setParameter("unitId", slotMasterDto.getUnitId())
					.setParameter("slotTypeId", slotMasterDto.getSlotTypeId())
				.executeUpdate();
				}

			if(slotMasterDto.getSlotTypeId()==SlotTypeModality){
				sessionFactory.getCurrentSession().createQuery("delete from SlotMaster sm where "
						+ "sm.subSpecialityId=:subSpecialityId "
						+ "AND sm.specialityId=:specialityId "
						+ "AND sm.organizationId=:orgId "
						+ "AND sm.unitId=:unitId "
						+ "AND sm.modalityId=:modalityId "
						+ "AND sm.slotTypeId=:slotTypeId "
						+ "and sm.slotId not in (select slotId from AppointmentMaster where slotId is not null and unitId =:unitId) ")
					.setParameter("modalityId",slotMasterDto.getModalityId())
					.setParameter("specialityId", slotMasterDto.getSpecialityId())
					.setParameter("subSpecialityId", slotMasterDto.getSubSpecialityId())
					.setParameter("orgId", slotMasterDto.getOrganizationId())
					.setParameter("unitId", slotMasterDto.getUnitId())
					.setParameter("slotTypeId", slotMasterDto.getSlotTypeId())
				.executeUpdate();
				}
			if(slotMasterDto.getSlotTypeId()==SlotTypeECH){
				sessionFactory.getCurrentSession().createQuery("delete from SlotMaster sm where "
						+ "sm.appointmentDate=:appointmentDate "
						+ "AND sm.organizationId=:orgId "
						+ "AND sm.unitId=:unitId "
						+ "AND sm.slotTypeId=:slotTypeId "
						+ "and sm.slotId not in (select slotId from AppointmentMaster where slotId is not null and unitId =:unitId) " )
					.setParameter("orgId", slotMasterDto.getOrganizationId())
					.setParameter("unitId", slotMasterDto.getUnitId())
					.setParameter("slotTypeId", slotMasterDto.getSlotTypeId())
					.setParameter("appointmentDate", GlobalCommonDateUtils.getDate(slotMasterDto.getAppointmentDate(),"yyyy-MM-dd"))
				.executeUpdate();
				}

			if(slotMasterDto.getSlotTypeId()==8){
				//System.out.println("deactive 8");
				sessionFactory.getCurrentSession().createQuery("delete from SlotMaster sm where "
						+ "sm.specialityId=:specialityId "
						+ "AND sm.appointmentDate=:appointmentDate "
						+ "AND sm.organizationId=:orgId "
						+ "AND sm.unitId=:unitId "
						+ "AND sm.bedId=:bedId "
						+ "AND sm.slotTypeId=:slotTypeId "
						+ "and sm.slotId not in (select slotId from AppointmentMaster where slotId is not null and unitId =:unitId) ")
					.setParameter("bedId",slotMasterDto.getBedId())
					.setParameter("specialityId", slotMasterDto.getSpecialityId())
					.setParameter("orgId", slotMasterDto.getOrganizationId())
					.setParameter("unitId", slotMasterDto.getUnitId())
					.setParameter("slotTypeId", slotMasterDto.getSlotTypeId())
					.setParameter("appointmentDate", GlobalCommonDateUtils.getDate(slotMasterDto.getAppointmentDate(),"yyyy-MM-dd"))
				.executeUpdate();
				}
			return new Response(SUCCESS, null, "Slots deactivated successfully !!", null, null);
		}catch(Exception e)
		{
		logger.error("deactiveSlotsDateAndDoctorWise failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response isSlotAlreadyExist(SlotMasterDto slotMasterDtoNew) throws ApplicationException {
		try{
			List<SlotMasterDto> slotMasterDtosList = null;
			if(slotMasterDtoNew.getSlotTypeId()==SlotTypeDoctor){

				slotMasterDtosList = sessionFactory.getCurrentSession()
				.createQuery("SELECT sm.slotId as slotId "
						+ "FROM SlotMaster sm "
						+ "WHERE sm.organizationId=:orgId "
						+ "AND sm.unitId=:unitId "
						+ "AND sm.slotTypeId=:slotTypeId "
						+ "AND sm.doctorId=:doctorId "
						+ "AND sm.specialityId=:specialityId "
						+ "AND sm.status='A' "
						+ "AND sm.appointmentDate=:appointmentDate "
						+ "AND sm.fromTime='"+slotMasterDtoNew.getFromTime()+"' "
						+ "AND sm.toTime='"+slotMasterDtoNew.getToTime()+"' "
						+ "AND sm.dayId=:dayId ")

				.setParameter("orgId", slotMasterDtoNew.getOrganizationId())
				.setParameter("unitId", slotMasterDtoNew.getUnitId())
				.setParameter("slotTypeId", slotMasterDtoNew.getSlotTypeId())
				.setParameter("doctorId", slotMasterDtoNew.getDoctorId())
				.setParameter("specialityId", slotMasterDtoNew.getSpecialityId())
				.setParameter("appointmentDate", GlobalCommonDateUtils.getDate(slotMasterDtoNew.getFromDate(),"yyyy-MM-dd"))
				.setParameter("dayId", slotMasterDtoNew.getDayId())
				.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
			}

			if(slotMasterDtoNew.getSlotTypeId()==SlotTypeModality){

				slotMasterDtosList = sessionFactory.getCurrentSession()
				.createQuery("SELECT sm.slotId as slotId "
						+ "FROM SlotMaster sm "
						+ "WHERE sm.organizationId=:orgId "
						+ "AND sm.unitId=:unitId "
						+ "AND sm.slotTypeId=:slotTypeId "
						+ "AND sm.modalityId=:modalityId "
						+ "AND sm.subSpecialityId=:subSpecialityId "
						+ "AND sm.specialityId=:specialityId "
						+ "AND sm.status='A' "
						+ "AND sm.appointmentDate=:appointmentDate "
						+ "AND sm.fromTime='"+slotMasterDtoNew.getFromTime()+"' "
						+ "AND sm.toTime='"+slotMasterDtoNew.getToTime()+"' "
						+ "AND sm.dayId=:dayId ")

				.setParameter("orgId", slotMasterDtoNew.getOrganizationId())
				.setParameter("unitId", slotMasterDtoNew.getUnitId())
				.setParameter("slotTypeId", slotMasterDtoNew.getSlotTypeId())
				.setParameter("subSpecialityId", slotMasterDtoNew.getSubSpecialityId())
				.setParameter("modalityId", slotMasterDtoNew.getModalityId())
				.setParameter("specialityId", slotMasterDtoNew.getSpecialityId())
				.setParameter("appointmentDate", GlobalCommonDateUtils.getDate(slotMasterDtoNew.getFromDate(),"yyyy-MM-dd"))
				.setParameter("dayId", slotMasterDtoNew.getDayId())
				.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
			}

			if(slotMasterDtoNew.getSlotTypeId()==SlotTypeECH){

				slotMasterDtosList = sessionFactory.getCurrentSession()
				.createQuery("SELECT sm.slotId as slotId "
						+ "FROM SlotMaster sm "
						+ "WHERE sm.organizationId=:orgId "
						+ "AND sm.unitId=:unitId "
						+ "AND sm.slotTypeId=:slotTypeId "
						+ "AND sm.status='A' "
						+ "AND sm.appointmentDate=:appointmentDate "
						+ "AND sm.dayId=:dayId ")

				.setParameter("orgId", slotMasterDtoNew.getOrganizationId())
				.setParameter("unitId", slotMasterDtoNew.getUnitId())
				.setParameter("slotTypeId", slotMasterDtoNew.getSlotTypeId())
				.setParameter("subSpecialityId", slotMasterDtoNew.getSubSpecialityId())
				.setParameter("modalityId", slotMasterDtoNew.getModalityId())
				.setParameter("specialityId", slotMasterDtoNew.getSpecialityId())
				.setParameter("appointmentDate", GlobalCommonDateUtils.getDate(slotMasterDtoNew.getFromDate(),"yyyy-MM-dd"))
				.setParameter("dayId", slotMasterDtoNew.getDayId())
				.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
			}
			if(slotMasterDtoNew.getSlotTypeId()==4){

				slotMasterDtosList = sessionFactory.getCurrentSession()
				.createQuery("SELECT sm.slotId as slotId "
						+ "FROM SlotMaster sm "
						+ "WHERE sm.organizationId=:orgId "
						+ "AND sm.unitId=:unitId "
						+ "AND sm.slotTypeId=:slotTypeId "
						+ "AND sm.doctorId is null "
						+ "AND sm.specialityId=:specialityId "
						+ "AND sm.status='A' "
						+ "AND sm.appointmentDate=:appointmentDate "
						+ "AND sm.fromTime='"+slotMasterDtoNew.getFromTime()+"' "
						+ "AND sm.toTime='"+slotMasterDtoNew.getToTime()+"' "
						+ "AND sm.dayId=:dayId ")

				.setParameter("orgId", slotMasterDtoNew.getOrganizationId())
				.setParameter("unitId", slotMasterDtoNew.getUnitId())
				.setParameter("slotTypeId", slotMasterDtoNew.getSlotTypeId())
				.setParameter("specialityId", slotMasterDtoNew.getSpecialityId())
				.setParameter("appointmentDate", GlobalCommonDateUtils.getDate(slotMasterDtoNew.getFromDate(),"yyyy-MM-dd"))
				.setParameter("dayId", slotMasterDtoNew.getDayId())
				.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
			}

		Character yesNo = null;
			if(slotMasterDtosList.size()>0)
				yesNo='Y';
			else
				yesNo='N';

		return new Response(SUCCESS, null, null,null,yesNo);
		}catch(Exception e)
		{
		logger.error("isSlotAlreadyExist failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response isEHCSlotAvailable(SlotMasterDto slotMasterDto) throws ApplicationException {
		try{

			StringBuilder stringQuery = new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_APPOINTMENTS_SLOTS_BY_DATE").getQueryString().toString());
			stringQuery.append(" AND sm.appointment_date=:appointmentDate "
					//+ " AND sm.slot_status_id=1 "
					+ " ORDER BY sm.slot_id");
			SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(stringQuery.toString());

			List<SlotMasterDto> slotMasterDtosList = query
					.setParameter("orgId", slotMasterDto.getOrganizationId())
					.setParameter("unitId", slotMasterDto.getUnitId())
					.setParameter("slotTypeId", slotMasterDto.getSlotTypeId())
					.setParameter("appointmentDate",	GlobalCommonDateUtils.getDate(slotMasterDto.getAppointmentDate(), "yyyy-MM-dd"))
					.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
			return new Response(SUCCESS, null, null, slotMasterDtosList, null);

		}catch(Exception e){
		logger.error("isEHCSlotAvailable failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response blockSlot(Integer slotId) throws ApplicationException {
		try{
			sessionFactory.getCurrentSession().createQuery("update SlotMaster sm set "
					+ "sm.slotStatusId=5, " //slot blocked
					+ "sm.updatedDate=:updatedDate "
					+ "where sm.slotId=:slotId ")
				.setParameter("updatedDate",TimeZoneComponent.getDateByzone(0))
				.setParameter("slotId",slotId)
			.executeUpdate();
			return new Response(SUCCESS, null, null, null, null);
		}catch(Exception e)
		{
		logger.error("blockSlot failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response checkSlotAlreadyBookedOrNot(BlockSlotReqDto blockSlotReqDto) throws ApplicationException {
		try{	
			List<SlotMasterDto> dtolist=sessionFactory.getCurrentSession().createSQLQuery(" select mstr.slot_id as \"slotId\",  "
					 +" 	concat(to_char(mstr.from_time,'HH:MI')) as \"fromTimeString\",  "
					 +"     concat(to_char(mstr.to_time,'HH:MI')) as \"toTimeString\" , "
					 +"     to_char(mstr.appointment_date,'dd/MM/yyyy') as \"appointmentDate\" "
					 +" 	from  opd.m_slot_master mstr   "
					 +" 	where mstr.slot_id IN (:slotIdList) "
					 +" 	and mstr.slot_status_id=4 and mstr.status='A'")
					.setParameterList("slotIdList", blockSlotReqDto.getListSlotId())
					.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
			
			return new Response(SUCCESS, null, null, dtolist, 'Y');
		}catch(HibernateException he){
		logger.error("checkSlotAlreadyBookedOrNot failed", he);
		}catch(Exception e){
		logger.error("checkSlotAlreadyBookedOrNot failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	 @Override
	    public Response getAsset(Integer orgId) throws ApplicationException {
	        try{    
	            List<EquipmentMasterDto> dtolist=sessionFactory.getCurrentSession().createSQLQuery(" select item.product_category_id as\"productCategoryId\", "
	                     + "  prodcat.category as\"category\" ,item.name as\"assetype\" ,item.id as\"itemId\"  "
	                     +"  from public.m_item item   "
	                     +"  inner join m_product_category prodcat on prodcat.id=item.product_category_id "
	                     +"  inner join m_asset_category asscat  on asscat.id=prodcat.asset_category_id  "
	                     +"  inner join m_asset_type asstype on asstype.id=asscat.asset_type_id  "
	                     +"  where asstype.id=2 and item.is_deleted=false and prodcat.is_deleted=false "
	                     +"  and asscat.is_deleted=false and asstype.is_deleted=false and item.organization_id="+orgId)
	                    .setResultTransformer(Transformers.aliasToBean(EquipmentMasterDto.class)).list();
	            
	            return new Response(SUCCESS, null, null, dtolist, null);
	        }catch(HibernateException he){
	        logger.error("getAsset failed", he);
	        }catch(Exception e){
	        logger.error("getAsset failed", e);
	        }
	        return new Response(ERROR, null, null, null, null);
	    }

	@Override
	public Response getHemoSlots(SlotMasterDto slotMasterDto) throws ApplicationException {
		try {
			List<SlotMasterDto> slotMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_HEMODIALYSIS_SLOTS")
					.setFirstResult(slotMasterDto.getOffset() != null ? slotMasterDto.getOffset() : 0)
					.setMaxResults(slotMasterDto.getNoOfRecordsPerPage() != null ? slotMasterDto.getNoOfRecordsPerPage() : 10)
					.setParameter("orgId", slotMasterDto.getOrganizationId())
					.setParameter("unitId", slotMasterDto.getUnitId())
					.setParameter("slotTypeId", slotMasterDto.getSlotTypeId())
					.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
			return new Response(SUCCESS, null, null, slotMasterDtosList, null);

		} catch (HibernateException he) {
		logger.error("getHemoSlots failed", he);
		} catch (Exception e) {
		logger.error("getHemoSlots failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getSlotHemoCount(SlotMasterDto slotMasterDto) throws ApplicationException {
		try {
			List<SlotMasterDto> slotMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_HEMODIALYSIS_SLOTS")
					.setParameter("orgId", slotMasterDto.getOrganizationId())
					.setParameter("unitId", slotMasterDto.getUnitId())
					.setParameter("slotTypeId", slotMasterDto.getSlotTypeId())
					.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
			Integer count=slotMasterDtosList.size();
			return new Response(SUCCESS, null, null, null, count);

		} catch (HibernateException he) {
		logger.error("getSlotHemoCount failed", he);
		} catch (Exception e) {
		logger.error("getSlotHemoCount failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response deactiveSlotsWithInDateRange(SlotMasterDto slotMasterDto) {
		
		try{
			if(slotMasterDto.getSlotTypeId()==SlotTypeDoctor)
			{
				String fromDate = null;
				String toDate = null;
				
				if(slotMasterDto.getMonth()!=null)
				{
					String month=slotMasterDto.getMonth().toString().length()>1 ? slotMasterDto.getMonth().toString() : "0"+slotMasterDto.getMonth().toString();
					String year=slotMasterDto.getYear().toString();
					sessionFactory.getCurrentSession().createQuery("DELETE from SlotMaster sm  "
							+ "WHERE sm.doctorId=:doctorId "
							+ "AND sm.specialityId=:specialityId "
							+ "AND to_char(sm.appointmentDate,'YYYY-MM')='"+year+"-"+month+"' "
							+ "AND sm.organizationId=:orgId "
							+ "AND sm.unitId=:unitId "
							+ "AND sm.slotTypeId=:slotTypeId "
							+ "and sm.slotId not in (select slotId from AppointmentMaster where slotId is not null and unitId =:unitId)")
						.setParameter("doctorId",slotMasterDto.getDoctorId())
						.setParameter("specialityId", slotMasterDto.getSpecialityId())
						.setParameter("orgId", slotMasterDto.getOrganizationId())
						.setParameter("unitId", slotMasterDto.getUnitId())
						.setParameter("slotTypeId", slotMasterDto.getSlotTypeId())
					.executeUpdate();
					
					YearMonth yearMonth = YearMonth.of(Integer.parseInt(year),Integer.parseInt(month));  // January of 2015.
					LocalDate firstDayOfMonth = yearMonth.atDay(1);
					LocalDate lastDayOfMonth = yearMonth.atEndOfMonth();
					fromDate = firstDayOfMonth.toString();
					toDate = lastDayOfMonth.toString();
					
				}else if(slotMasterDto.getFromDate()!=null && slotMasterDto.getToDate()!=null){
					sessionFactory.getCurrentSession().createQuery("DELETE from SlotMaster sm  "
							+ "WHERE sm.doctorId=:doctorId "
							+ "AND sm.specialityId=:specialityId "
							+ "AND to_char(sm.appointmentDate,'YYYY-MM-DD') >= '"+slotMasterDto.getFromDate()+"' "
							+ "AND to_char(sm.appointmentDate,'YYYY-MM-DD') <= '"+slotMasterDto.getToDate()+"' "
							+ "AND sm.organizationId=:orgId "
							+ "AND sm.unitId=:unitId "
							+ "AND sm.slotTypeId=:slotTypeId "
							+ "and sm.slotId not in (select slotId from AppointmentMaster where slotId is not null and unitId =:unitId)")
						.setParameter("doctorId",slotMasterDto.getDoctorId())
						.setParameter("specialityId", slotMasterDto.getSpecialityId())
						.setParameter("orgId", slotMasterDto.getOrganizationId())
						.setParameter("unitId", slotMasterDto.getUnitId())
						.setParameter("slotTypeId", slotMasterDto.getSlotTypeId())
					.executeUpdate();
					
					fromDate = slotMasterDto.getFromDate();
					toDate = slotMasterDto.getToDate();
				}
				
				List<ScheduleMasterDto> scheduleMasterDtosList = new ArrayList();
				
					ScheduleMasterDto dto = new ScheduleMasterDto();	
						dto.setDoctorId(slotMasterDto.getDoctorId());				    	
						dto.setSpecialityId(slotMasterDto.getSpecialityId());				    	
						dto.setOrganisationId(slotMasterDto.getOrganizationId());				 
						dto.setUnitId(slotMasterDto.getUnitId());
						dto.setFromDate(fromDate);
						dto.setToDate(toDate);
						dto.setDeleteAllSlots('N');
						dto.setAddRemove(1);
					scheduleMasterDtosList.add(dto);
				
				iDoctorScheduleDao.saveDoctorSchedule(scheduleMasterDtosList,"DELETE");
				
			}
			if(slotMasterDto.getSlotTypeId()==4)
			{
				if(slotMasterDto.getMonth()!=null)
				{
					String month=slotMasterDto.getMonth().toString().length()>1 ? slotMasterDto.getMonth().toString() : "0"+slotMasterDto.getMonth().toString();	
					String year=slotMasterDto.getYear().toString();
					sessionFactory.getCurrentSession().createQuery("DELETE from SlotMaster sm "
							+ "WHERE sm.specialityId=:specialityId "
							+ "AND to_char(sm.appointmentDate,'YYYY-MM')='"+year+"-"+month+"' "
							+ "AND sm.organizationId=:orgId "
							+ "AND sm.unitId=:unitId "
							+ "AND sm.slotTypeId=:slotTypeId "
							+ "and sm.slotId not in (select slotId from AppointmentMaster where slotId is not null and unitId =:unitId)")
						.setParameter("specialityId", slotMasterDto.getSpecialityId())
						.setParameter("orgId", slotMasterDto.getOrganizationId())
						.setParameter("unitId", slotMasterDto.getUnitId())
						.setParameter("slotTypeId", slotMasterDto.getSlotTypeId())
					.executeUpdate();
				}else if(slotMasterDto.getFromDate()!=null && slotMasterDto.getToDate()!=null){
//					sessionFactory.getCurrentSession().createQuery("DELETE from SlotMaster "
//							+ " WHERE sm.specialityId=:specialityId "
//							+ "AND to_char(sm.appointmentDate,'YYYY-MM-DD') >= '"+slotMasterDto.getFromDate()+"' "
//							+ "AND to_char(sm.appointmentDate,'YYYY-MM-DD') <= '"+slotMasterDto.getToDate()+"' "
//							+ "AND sm.organizationId=:orgId "
//							+ "AND sm.unitId=:unitId "
//							+ "AND sm.slotTypeId=:slotTypeId "
//							+ "and sm.slotId not in (select slotId from AppointmentMaster where slotId is not null and unitId =:unitId)")
//						.setParameter("specialityId", slotMasterDto.getSpecialityId())
//						.setParameter("orgId", slotMasterDto.getOrganizationId())
//						.setParameter("unitId", slotMasterDto.getUnitId())
//						.setParameter("slotTypeId", slotMasterDto.getSlotTypeId())
//					.executeUpdate();
					
					
					sessionFactory.getCurrentSession().createQuery("DELETE from SlotMaster sm  "
							+ "WHERE "
							+ " sm.specialityId=:specialityId "
							+ "AND to_char(sm.appointmentDate,'YYYY-MM-DD') >= '"+slotMasterDto.getFromDate()+"' "
							+ "AND to_char(sm.appointmentDate,'YYYY-MM-DD') <= '"+slotMasterDto.getToDate()+"' "
							+ "AND sm.organizationId=:orgId "
							+ "AND sm.unitId=:unitId "
							+ "AND sm.slotTypeId=:slotTypeId "
							+ "and sm.slotId not in (select slotId from AppointmentMaster where slotId is not null and unitId =:unitId)")
						.setParameter("specialityId", slotMasterDto.getSpecialityId())
						.setParameter("orgId", slotMasterDto.getOrganizationId())
						.setParameter("unitId", slotMasterDto.getUnitId())
						.setParameter("slotTypeId", slotMasterDto.getSlotTypeId())
					.executeUpdate();
					
//					Query query = sessionFactory.getCurrentSession().createQuery("DELETE from SlotMaster "
//							+ " WHERE sm.specialityId=:specialityId "
//							+ "AND to_char(sm.appointmentDate,'YYYY-MM-DD') >= '"+slotMasterDto.getFromDate()+"' "
//							+ "AND to_char(sm.appointmentDate,'YYYY-MM-DD') <= '"+slotMasterDto.getToDate()+"' "
//							+ "AND sm.organizationId=:orgId "
//							+ "AND sm.unitId=:unitId "
//							+ "AND sm.slotTypeId=:slotTypeId "
//							+ "and sm.slotId not in (select slotId from AppointmentMaster where slotId is not null and unitId =:unitId)");
//					sessionFactory.getCurrentSession().createSQLQuery(query)
//						.setParameter("specialityId", slotMasterDto.getSpecialityId())
//						.setParameter("orgId", slotMasterDto.getOrganizationId())
//						.setParameter("fromDate", slotMasterDto.getFromDate().substring(0,10))
//						.setParameter("toDate", slotMasterDto.getToDate().substring(0,10))
//						.setParameter("unitId", slotMasterDto.getUnitId())
//						.setParameter("slotTypeId", slotMasterDto.getSlotTypeId());
					
				}
			}
			
			if(slotMasterDto.getSlotTypeId()==SlotTypeModality)
			{
				if(slotMasterDto.getMonth()!=null)
				{
					String month=slotMasterDto.getMonth().toString().length()>1 ? slotMasterDto.getMonth().toString() : "0"+slotMasterDto.getMonth().toString();	
					String year=slotMasterDto.getYear().toString();
					sessionFactory.getCurrentSession().createQuery("DELETE from SlotMaster sm "
							+ "WHERE sm.specialityId=:specialityId "
							+ "AND sm.modalityId=:modalityId "
							+ "AND sm.subSpecialityId=:subSpecialityId "
							+ "AND to_char(sm.appointmentDate,'YYYY-MM')='"+year+"-"+month+"' "
							+ "AND sm.organizationId=:orgId "
							+ "AND sm.unitId=:unitId "
							+ "AND sm.slotTypeId=:slotTypeId "
							+ "and sm.slotId not in (select slotId from AppointmentMaster where slotId is not null and unitId =:unitId)")
						.setParameter("specialityId", slotMasterDto.getSpecialityId())
						.setParameter("subSpecialityId", slotMasterDto.getSubSpecialityId())
						.setParameter("modalityId", slotMasterDto.getModalityId())
						.setParameter("orgId", slotMasterDto.getOrganizationId())
						.setParameter("unitId", slotMasterDto.getUnitId())
						.setParameter("slotTypeId", slotMasterDto.getSlotTypeId())
					.executeUpdate();
				}else if(slotMasterDto.getFromDate()!=null && slotMasterDto.getToDate()!=null){
					sessionFactory.getCurrentSession().createQuery("DELETE from SlotMaster sm  "
							+ "WHERE sm.specialityId=:specialityId "
							+ "AND sm.modalityId=:modalityId "
							+ "AND sm.subSpecialityId=:subSpecialityId "
							+ "AND to_char(sm.appointmentDate,'YYYY-MM-DD') >= '"+slotMasterDto.getFromDate()+"' "
							+ "AND to_char(sm.appointmentDate,'YYYY-MM-DD') <= '"+slotMasterDto.getToDate()+"' "
							+ "AND sm.organizationId=:orgId "
							+ "AND sm.unitId=:unitId "
							+ "AND sm.slotTypeId=:slotTypeId "
							+ "and sm.slotId not in (select slotId from AppointmentMaster where slotId is not null and unitId =:unitId)")
						.setParameter("specialityId", slotMasterDto.getSpecialityId())
						.setParameter("subSpecialityId", slotMasterDto.getSubSpecialityId())
						.setParameter("modalityId", slotMasterDto.getModalityId())
						.setParameter("orgId", slotMasterDto.getOrganizationId())
						.setParameter("unitId", slotMasterDto.getUnitId())
						.setParameter("slotTypeId", slotMasterDto.getSlotTypeId())
					.executeUpdate();
				}
				
			}
			return new Response(SUCCESS, null, "Slots deactivated successfully !!", null, null);
		}catch(Exception e){
		logger.error("deactiveSlotsWithInDateRange failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@Override
	public Response getOldSlotDetails(SlotMasterDto slotMasterDtosObj, List<DoctorMasterDto> doctorArrayList,Integer unitId, Integer slotTypeId,char override) {
		try {
			
			Set<Integer> docIds = new HashSet<>();
			Set<Integer> deptIds = new HashSet<>();
			Set<Integer> subSplIds = new HashSet<>();
			Set<Integer> modalityIds = new HashSet<>();
			for(DoctorMasterDto docDto : doctorArrayList)
			{
				if(docDto.getDoctorId()!=null)
					docIds.add(docDto.getDoctorId());
				
				if(docDto.getSpecialityId()!=null)
					deptIds.add(docDto.getSpecialityId());
				
				if(docDto.getSubSpecialityId()!=null)
					subSplIds.add(docDto.getSubSpecialityId());
				
				if(docDto.getModalityId()!=null)
					modalityIds.add(docDto.getModalityId());
			}
			
			if(override=='Y')
			{
				
				if(slotTypeId==1)
				{
					LocalTime toTime = slotMasterDtosObj.getToTime().atZone(ZoneOffset.UTC).toLocalTime();
					LocalTime fromTime = slotMasterDtosObj.getFromTime().atZone(ZoneOffset.UTC).toLocalTime();

					String query = " delete from opd.m_slot_master "
							 + " where " 
							 + " 	doctor_id in (:docIds) " 
							 + " 	and speciality_id in (:deptIds) " 
							 + " 	and to_char(appointment_date, 'YYYY-MM-DD') >= :fromDate " 
							 + " 	and to_char(appointment_date, 'YYYY-MM-DD') <= :toDate " 
							 + " 	and to_char(from_time, 'HH24:MI:SS') >= :fromTime "
							 + " 	and to_char(to_time, 'HH24:MI:SS') <= :toTime "
							 + " 	and slot_type_id =:slotTypeId " 
							 + " 	and day_id in (:dayIds) " 
							 + " 	and unit_id =:unitId "
							 + " 	and status ='A' "
							 + " 	and slot_id not in (select slot_id from opd.m_appointment_master where slot_id is not null and unit_id =:unitId) ";
					
					sessionFactory.getCurrentSession().createSQLQuery(query)
						.setParameterList("deptIds", deptIds)
						.setParameterList("docIds", docIds)
						.setParameter("unitId", unitId)
						.setParameter("slotTypeId", slotTypeId)
						.setParameter("fromDate", slotMasterDtosObj.getFromDate().substring(0,10))
						.setParameter("toDate", slotMasterDtosObj.getToDate().substring(0,10))
						.setParameter("fromTime", fromTime.toString()+":00")
						.setParameter("toTime", toTime.toString()+":00")
						.setParameterList("dayIds", slotMasterDtosObj.getDayArrayList())
						.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).executeUpdate();
					System.out.println("fromTime : " + toTime.toString()+":00");
				}
				if(slotTypeId==2){

					LocalTime toTime = slotMasterDtosObj.getToTime().atZone(ZoneOffset.UTC).toLocalTime();
					LocalTime fromTime = slotMasterDtosObj.getFromTime().atZone(ZoneOffset.UTC).toLocalTime();

					String query = "  delete from opd.m_slot_master "
							 + " where " 
							 + "	modality_id in (:modalityIds) "
							 + "	AND sub_speciality_id in (:subSplIds) "
							 + " 	and visit_type_id =:visitTypeId " 
							 + " 	and to_char(appointment_date, 'YYYY-MM-DD') >= :fromDate " 
							 + " 	and to_char(appointment_date, 'YYYY-MM-DD') <= :toDate " 
							 + " 	and to_char(from_time, 'HH24:MI:SS') >= :fromTime "
							 + " 	and to_char(to_time, 'HH24:MI:SS') <= :toTime "
							 + " 	and slot_type_id =:slotTypeId " 
							 + " 	and day_id in (:dayIds) " 
							 + " 	and unit_id =:unitId " 
							 + " 	and status ='A' "
							 + " 	and slot_id not in (select slot_id from opd.m_appointment_master where slot_id is not null and unit_id =:unitId) ";
					
					sessionFactory.getCurrentSession().createSQLQuery(query)
						.setParameter("visitTypeId", slotMasterDtosObj.getVisitTypeId())
						.setParameterList("modalityIds", modalityIds)
						.setParameterList("subSplIds", subSplIds)
						.setParameter("unitId", unitId)
						.setParameter("slotTypeId", slotTypeId)
						.setParameter("fromDate", slotMasterDtosObj.getFromDate().substring(0,10))
						.setParameter("toDate", slotMasterDtosObj.getToDate().substring(0,10))
						.setParameter("fromTime", fromTime.toString()+":00")
						.setParameter("toTime", toTime.toString()+":00")
						.setParameterList("dayIds", slotMasterDtosObj.getDayArrayList())
						.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).executeUpdate();
				}if(slotTypeId==4)
				{
					LocalTime toTime = slotMasterDtosObj.getToTime().atZone(ZoneOffset.UTC).toLocalTime();
					LocalTime fromTime = slotMasterDtosObj.getFromTime().atZone(ZoneOffset.UTC).toLocalTime();

					String query = " delete from opd.m_slot_master "
							 + " where " 
							 + " 	speciality_id in (:deptIds) " 
							 + " 	and to_char(appointment_date, 'YYYY-MM-DD') >= :fromDate " 
							 + " 	and to_char(appointment_date, 'YYYY-MM-DD') <= :toDate " 
							 + " 	and to_char(from_time, 'HH24:MI:SS') >= :fromTime "
							 + " 	and to_char(to_time, 'HH24:MI:SS') <= :toTime "
							 + " 	and slot_type_id =:slotTypeId " 
							 + " 	and day_id in (:dayIds) " 
							 + " 	and unit_id =:unitId " 
							 + " 	and status ='A' "
							 + " 	and slot_id not in (select slot_id from opd.m_appointment_master where slot_id is not null and unit_id =:unitId) ";
					
					sessionFactory.getCurrentSession().createSQLQuery(query)
						.setParameterList("deptIds", deptIds)
						.setParameter("unitId", unitId)
						.setParameter("slotTypeId", slotTypeId)
						.setParameter("fromDate", slotMasterDtosObj.getFromDate().substring(0,10))
						.setParameter("toDate", slotMasterDtosObj.getToDate().substring(0,10))
						.setParameter("fromTime", fromTime.toString()+":00")
						.setParameter("toTime", toTime.toString()+":00")
						.setParameterList("dayIds", slotMasterDtosObj.getDayArrayList())
						.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).executeUpdate();
				}
				
				return new Response(SUCCESS, null, null, null, null);
			}else{
				
				if(slotTypeId==1)
				{
					LocalTime toTime = slotMasterDtosObj.getToTime().atZone(ZoneOffset.UTC).toLocalTime();
					LocalTime fromTime = slotMasterDtosObj.getFromTime().atZone(ZoneOffset.UTC).toLocalTime();

					String query = " select "
							+  " um.unit_name as\"unitName\", " 
							 + " 	concat('Dr. ',doc.first_name,' ',doc.last_name,' (',spl.speciality_name,')') as \"doctorName\" " 
							 + " from " 
							 + " 	opd.m_slot_master msm " 
							 + " 	inner join doctor.m_doctor_master doc "
							 + " 		on doc.doctor_id =msm.doctor_id "
							+ " 	inner join doctor.m_speciality_master spl "
							+ " 		on spl.speciality_id =msm.speciality_id "
							+ " 	inner join public.m_unit_master um "
							+ " 		on um.unit_id =msm.unit_id "
							 + " where " 
							 + " 	msm.doctor_id in (:docIds) " 
							 + " 	and msm.speciality_id in (:deptIds) " 
							 + " 	and to_char(msm.appointment_date, 'YYYY-MM-DD') >= :fromDate " 
							 + " 	and to_char(msm.appointment_date, 'YYYY-MM-DD') <= :toDate " 
							 + " 	and to_char(msm.from_time, 'HH24:MI:SS') >= :fromTime " 
							 + " 	and to_char(msm.to_time, 'HH24:MI:SS') <= :toTime "
							 + " 	and msm.slot_type_id =:slotTypeId " 
							 + " 	and msm.day_id in (:dayIds) " 
							 //+ " 	and msm.unit_id =:unitId " 
							 + " 	and msm.status ='A' "
							 +"		group by (msm.speciality_id, msm.doctor_id),um.unit_name,doc.first_name,doc.last_name,spl.speciality_name ";
					
					List<SlotMasterDto> slotMasterDtolist = sessionFactory.getCurrentSession().createSQLQuery(query)
						.setParameterList("deptIds", deptIds)
						.setParameterList("docIds", docIds)
						//.setParameter("unitId", unitId)
						.setParameter("slotTypeId", slotTypeId)
						.setParameter("fromDate", slotMasterDtosObj.getFromDate().substring(0,10))
						.setParameter("toDate", slotMasterDtosObj.getToDate().substring(0,10))
						.setParameter("fromTime", fromTime.toString()+":00")
						.setParameter("toTime", toTime.toString()+":00")
						.setParameterList("dayIds", slotMasterDtosObj.getDayArrayList())
						.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
					String message=null;
					if(slotMasterDtolist!=null && slotMasterDtolist.size()>0)
					{
						
						StringBuilder docs = new StringBuilder();
						for(SlotMasterDto dto : slotMasterDtolist)
						{
							if(docs.length()>0)
							{
								docs.append(", "+dto.getDoctorName());
							}else{
								docs.append(dto.getDoctorName());
							}
						}
						String unit = null;
						for(SlotMasterDto dto : slotMasterDtolist)
						{
							unit = dto.getUnitName() ;
						}
						message = "There are some overlapping schedules for "+docs+ ", unable to generate new schedules...!!";
						
						return new Response(ERROR, ALREADY_EXIST_CODE, message, null, null);
					}
				}if(slotTypeId==2)
				{
					LocalTime toTime = slotMasterDtosObj.getToTime().atZone(ZoneOffset.UTC).toLocalTime();
					LocalTime fromTime = slotMasterDtosObj.getFromTime().atZone(ZoneOffset.UTC).toLocalTime();

					String query = " select " 
							 + " 	moda.modality_desc as \"doctorName\" " 
							 + " from " 
							 + " 	opd.m_slot_master msm " 
							 + " 	inner join m_modality_master moda on "
							 + " 	moda.modality_id  = msm.modality_id "
							 + " where " 
							 + "	msm.visit_type_id=:visitTypeId "
							 + "	and msm.sub_speciality_id in (:subSplIds) "
							 + " 	and msm.modality_id in (:modalityIds) " 
							 + " 	and to_char(msm.appointment_date, 'YYYY-MM-DD') >= :fromDate " 
							 + " 	and to_char(msm.appointment_date, 'YYYY-MM-DD') <= :toDate " 
							 + " 	and to_char(msm.from_time, 'HH24:MI:SS') >= :fromTime " 
							 + " 	and to_char(msm.to_time, 'HH24:MI:SS') <= :toTime "
							 + " 	and msm.slot_type_id =:slotTypeId " 
							 + " 	and msm.day_id in (:dayIds) " 
							 + " 	and msm.unit_id =:unitId " 
							 + " 	and msm.status ='A' "
							 +"		group by moda.modality_desc ";
					
					List<SlotMasterDto> slotMasterDtolist = sessionFactory.getCurrentSession().createSQLQuery(query)
							.setParameter("visitTypeId", slotMasterDtosObj.getVisitTypeId())
						.setParameterList("modalityIds", modalityIds)
						.setParameterList("subSplIds", subSplIds)
						.setParameter("unitId", unitId)
						.setParameter("slotTypeId", slotTypeId)
						.setParameter("fromDate", slotMasterDtosObj.getFromDate().substring(0,10))
						.setParameter("toDate", slotMasterDtosObj.getToDate().substring(0,10))
						.setParameter("fromTime", fromTime.toString()+":00")
						.setParameter("toTime", toTime.toString()+":00")
						.setParameterList("dayIds", slotMasterDtosObj.getDayArrayList())
						.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
					String message=null;
					if(slotMasterDtolist!=null && slotMasterDtolist.size()>0)
					{
						
						StringBuilder docs = new StringBuilder();
						for(SlotMasterDto dto : slotMasterDtolist)
						{
							if(docs.length()>0)
							{
								docs.append(", "+dto.getDoctorName());
							}else{
								docs.append(dto.getDoctorName());
							}
						}
						message = "There are some overlapping schedules for "+docs+", unable to generate new schedules...!!";
						
						return new Response(ERROR, ALREADY_EXIST_CODE, message, null, null);
					}
				}if(slotTypeId==4)
				{
					LocalTime toTime = slotMasterDtosObj.getToTime().atZone(ZoneOffset.UTC).toLocalTime();
					LocalTime fromTime = slotMasterDtosObj.getFromTime().atZone(ZoneOffset.UTC).toLocalTime();

					String query = " select " 
							 + " 	spl.speciality_name as \"specialityName\" " 
							 + " from " 
							 + " 	opd.m_slot_master msm " 
							+ " 	inner join doctor.m_speciality_master spl "
							+ " 		on spl.speciality_id = msm.speciality_id "
							 + " where " 
							 + " 	msm.speciality_id in (:deptIds) " 
							 + " 	and to_char(msm.appointment_date, 'YYYY-MM-DD') >= :fromDate " 
							 + " 	and to_char(msm.appointment_date, 'YYYY-MM-DD') <= :toDate " 
							 + " 	and to_char(msm.from_time, 'HH24:MI:SS') >= :fromTime " 
							 + " 	and to_char(msm.to_time, 'HH24:MI:SS') <= :toTime "
							 + " 	and msm.slot_type_id =:slotTypeId " 
							 + " 	and msm.unit_id =:unitId " 
							 + " 	and msm.status ='A' "
							 + " 	and msm.day_id in (:dayIds) " 
							 +"		group by msm.speciality_id,spl.speciality_name ";
					
					List<SlotMasterDto> slotMasterDtolist = sessionFactory.getCurrentSession().createSQLQuery(query)
						.setParameterList("deptIds", deptIds)
						.setParameter("unitId", unitId)
						.setParameter("slotTypeId", slotTypeId)
						.setParameter("fromDate", slotMasterDtosObj.getFromDate().substring(0,10))
						.setParameter("toDate", slotMasterDtosObj.getToDate().substring(0,10))
						.setParameter("fromTime", fromTime.toString()+":00")
						.setParameter("toTime", toTime.toString()+":00")
						.setParameterList("dayIds", slotMasterDtosObj.getDayArrayList())
						.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
					String message=null;
					if(slotMasterDtolist!=null && slotMasterDtolist.size()>0)
					{
						
						StringBuilder docs = new StringBuilder();
						for(SlotMasterDto dto : slotMasterDtolist)
						{
							if(docs.length()>0)
							{
								docs.append(", "+dto.getSpecialityName());
							}else{
								docs.append(dto.getSpecialityName());
							}
						}
						message = "There are some overlapping schedules for "+docs+", unable to generate new schedules...!!";
						
						return new Response(ERROR, ALREADY_EXIST_CODE, message, null, null);
					}
				}
				
			}
			
			
			return new Response(SUCCESS, null, null, null, null);

		} catch (HibernateException he) {
		logger.error("getOldSlotDetails failed", he);
		} catch (Exception e) {
		logger.error("getOldSlotDetails failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}


	@Override
	public Response getOldSlotDetailsDoctor(SlotMasterDto slotMasterDtosObj, List<DoctorMasterDto> doctorArrayList,Integer unitId, Integer slotTypeId,char override) {
		try {
			
			Set<Integer> docIds = new HashSet<>();
			Set<Integer> deptIds = new HashSet<>();
			Set<Integer> subSplIds = new HashSet<>();
			Set<Integer> modalityIds = new HashSet<>();
			for(DoctorMasterDto docDto : doctorArrayList)
			{
				if(docDto.getDoctorId()!=null)
					docIds.add(docDto.getDoctorId());
				
				if(docDto.getSpecialityId()!=null)
					deptIds.add(docDto.getSpecialityId());
				
				if(docDto.getSubSpecialityId()!=null)
					subSplIds.add(docDto.getSubSpecialityId());
				
				if(docDto.getModalityId()!=null)
					modalityIds.add(docDto.getModalityId());
			}
			
			if(override=='Y')
			{
				
				if(slotTypeId==1)
				{
					String query = " delete from opd.m_slot_master "
							 + " where " 
							 + " 	doctor_id in (:docIds) " 
							 + " 	and speciality_id in (:deptIds) " 
							 + " 	and to_char(appointment_date, 'YYYY-MM-DD') >= :fromDate " 
							 + " 	and to_char(appointment_date, 'YYYY-MM-DD') <= :toDate " 
							// + " 	and to_char(from_time, 'HH24:MI:SS') >= :fromTime " 
							// + " 	and to_char(to_time, 'HH24:MI:SS') <= :toTime "
							 + " 	and slot_type_id =:slotTypeId " 
							 + " 	and day_id in (:dayIds) " 
							 + " 	and unit_id =:unitId " 
							 + " 	and status ='A' "
							 + " 	and slot_id not in (select slot_id from opd.m_appointment_master where slot_id is not null and unit_id =:unitId) ";
					
					sessionFactory.getCurrentSession().createSQLQuery(query)
						.setParameterList("deptIds", deptIds)
						.setParameterList("docIds", docIds)
						.setParameter("unitId", unitId)
						.setParameter("slotTypeId", slotTypeId)
						.setParameter("fromDate", slotMasterDtosObj.getFromDate().substring(0,10))
						.setParameter("toDate", slotMasterDtosObj.getToDate().substring(0,10))
						//.setParameter("fromTime", slotMasterDtosObj.getFromTime().toString())
						//.setParameter("toTime", slotMasterDtosObj.getToTime().toString())
						.setParameterList("dayIds", slotMasterDtosObj.getDayArrayList())
						.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).executeUpdate();
				}
				if(slotTypeId==2){
					String query = "  delete from opd.m_slot_master "
							 + " where " 
							 + "	modality_id in (:modalityIds) "
							 + "	AND sub_speciality_id in (:subSplIds) "
							 + " 	and visit_type_id =:visitTypeId " 
							 + " 	and to_char(appointment_date, 'YYYY-MM-DD') >= :fromDate " 
							 + " 	and to_char(appointment_date, 'YYYY-MM-DD') <= :toDate " 
							 + " 	and to_char(from_time, 'HH24:MI:SS') >= :fromTime " 
							 + " 	and to_char(to_time, 'HH24:MI:SS') <= :toTime "
							 + " 	and slot_type_id =:slotTypeId " 
							 + " 	and day_id in (:dayIds) " 
							 + " 	and unit_id =:unitId " 
							 + " 	and status ='A' "
							 + " 	and slot_id not in (select slot_id from opd.m_appointment_master where slot_id is not null and unit_id =:unitId) ";
					
					sessionFactory.getCurrentSession().createSQLQuery(query)
						.setParameter("visitTypeId", slotMasterDtosObj.getVisitTypeId())
						.setParameterList("modalityIds", modalityIds)
						.setParameterList("subSplIds", subSplIds)
						.setParameter("unitId", unitId)
						.setParameter("slotTypeId", slotTypeId)
						.setParameter("fromDate", slotMasterDtosObj.getFromDate().substring(0,10))
						.setParameter("toDate", slotMasterDtosObj.getToDate().substring(0,10))
						.setParameter("fromTime", slotMasterDtosObj.getFromTime().toString())
						.setParameter("toTime", slotMasterDtosObj.getToTime().toString())
						.setParameterList("dayIds", slotMasterDtosObj.getDayArrayList())
						.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).executeUpdate();
				}if(slotTypeId==4)
				{
					String query = " delete from opd.m_slot_master "
							 + " where " 
							 + " 	speciality_id in (:deptIds) " 
							 + " 	and to_char(appointment_date, 'YYYY-MM-DD') >= :fromDate " 
							 + " 	and to_char(appointment_date, 'YYYY-MM-DD') <= :toDate " 
							 + " 	and to_char(from_time, 'HH24:MI:SS') >= :fromTime " 
							 + " 	and to_char(to_time, 'HH24:MI:SS') <= :toTime "
							 + " 	and slot_type_id =:slotTypeId " 
							 + " 	and day_id in (:dayIds) " 
							 + " 	and unit_id =:unitId " 
							 + " 	and status ='A' "
							 + " 	and slot_id not in (select slot_id from opd.m_appointment_master where slot_id is not null and unit_id =:unitId) ";
					
					sessionFactory.getCurrentSession().createSQLQuery(query)
						.setParameterList("deptIds", deptIds)
						.setParameter("unitId", unitId)
						.setParameter("slotTypeId", slotTypeId)
						.setParameter("fromDate", slotMasterDtosObj.getFromDate().substring(0,10))
						.setParameter("toDate", slotMasterDtosObj.getToDate().substring(0,10))
						.setParameter("fromTime", slotMasterDtosObj.getFromTime().toString())
						.setParameter("toTime", slotMasterDtosObj.getToTime().toString())
						.setParameterList("dayIds", slotMasterDtosObj.getDayArrayList())
						.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).executeUpdate();
				}
				
				return new Response(SUCCESS, null, null, null, null);
			}else{
				
				if(slotTypeId==1)
				{
					String query = " select "
							 + " msm.unit_id as \"unitId\", "
							 +  " um.unit_name as\"unitName\", " 
							 + " 	concat('Dr. ',doc.first_name,' ',doc.last_name,' (',spl.speciality_name,')') as \"doctorName\" " 
							 + " from " 
							 + " 	opd.m_slot_master msm " 
							 + " 	inner join doctor.m_doctor_master doc "
							 + " 		on doc.doctor_id =msm.doctor_id "
							+ " 	inner join doctor.m_speciality_master spl "
							+ " 		on spl.speciality_id =msm.speciality_id "
							+ " 	inner join public.m_unit_master um "
							+ " 		on um.unit_id =msm.unit_id "
							 + " where " 
							 + " 	msm.doctor_id in (:docIds) " 
							 + " 	and msm.speciality_id in (:deptIds) " 
							 + " 	and to_char(msm.appointment_date, 'YYYY-MM-DD') >= :fromDate " 
							 + " 	and to_char(msm.appointment_date, 'YYYY-MM-DD') <= :toDate " 
							 + " 	and to_char(msm.from_time, 'HH24:MI:SS') >= :fromTime " 
							 + " 	and to_char(msm.to_time, 'HH24:MI:SS') <= :toTime "
							 + " 	and msm.slot_type_id =:slotTypeId " 
							 + " 	and msm.day_id in (:dayIds) " 
							 + " 	and msm.unit_id =:unitId " 
							 + " 	and msm.status ='A' "
							 +"		group by (msm.speciality_id, msm.doctor_id),msm.unit_id,um.unit_name,doc.first_name,doc.last_name,spl.speciality_name ";
					
					List<SlotMasterDto> slotMasterDtolist = sessionFactory.getCurrentSession().createSQLQuery(query)
						.setParameterList("deptIds", deptIds)
						.setParameterList("docIds", docIds)
						.setParameter("unitId", unitId)
						.setParameter("slotTypeId", slotTypeId)
						.setParameter("fromDate", slotMasterDtosObj.getFromDate().substring(0,10))
						.setParameter("toDate", slotMasterDtosObj.getToDate().substring(0,10))
						.setParameter("fromTime", slotMasterDtosObj.getFromTime().toString())
						.setParameter("toTime", slotMasterDtosObj.getToTime().toString())
						.setParameterList("dayIds", slotMasterDtosObj.getDayArrayList())
						.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
					String message=null;
					if(slotMasterDtolist!=null && slotMasterDtolist.size()>0)
					{
						
						StringBuilder docs = new StringBuilder();
						for(SlotMasterDto dto : slotMasterDtolist)
						{
							if(docs.length()>0)
							{
								docs.append(", "+dto.getDoctorName());
							}else{
								docs.append(dto.getDoctorName());
							}
						}
						String unit = null;
						for(SlotMasterDto dto : slotMasterDtolist)
						{
							unit = dto.getUnitName() ;
						}
						message = "There are some overlapping schedules for "+docs+" in unit "+unit+ ", Do you want to deactivate existing schedule and create new schedule?";
						
						return new Response(ERROR, ALREADY_EXIST_CODE, message, slotMasterDtolist, null);
					}
				}if(slotTypeId==2)
				{
					String query = " select " 
							 + " 	moda.modality_desc as \"doctorName\" " 
							 + " from " 
							 + " 	opd.m_slot_master msm " 
							 + " 	inner join m_modality_master moda on "
							 + " 	moda.modality_id  = msm.modality_id "
							 + " where " 
							 + "	msm.visit_type_id=:visitTypeId "
							 + "	and msm.sub_speciality_id in (:subSplIds) "
							 + " 	and msm.modality_id in (:modalityIds) " 
							 + " 	and to_char(msm.appointment_date, 'YYYY-MM-DD') >= :fromDate " 
							 + " 	and to_char(msm.appointment_date, 'YYYY-MM-DD') <= :toDate " 
							 + " 	and to_char(msm.from_time, 'HH24:MI:SS') >= :fromTime " 
							 + " 	and to_char(msm.to_time, 'HH24:MI:SS') <= :toTime "
							 + " 	and msm.slot_type_id =:slotTypeId " 
							 + " 	and msm.day_id in (:dayIds) " 
							 + " 	and msm.unit_id =:unitId " 
							 + " 	and msm.status ='A' "
							 +"		group by moda.modality_desc ";
					
					List<SlotMasterDto> slotMasterDtolist = sessionFactory.getCurrentSession().createSQLQuery(query)
							.setParameter("visitTypeId", slotMasterDtosObj.getVisitTypeId())
						.setParameterList("modalityIds", modalityIds)
						.setParameterList("subSplIds", subSplIds)
						.setParameter("unitId", unitId)
						.setParameter("slotTypeId", slotTypeId)
						.setParameter("fromDate", slotMasterDtosObj.getFromDate().substring(0,10))
						.setParameter("toDate", slotMasterDtosObj.getToDate().substring(0,10))
						.setParameter("fromTime", slotMasterDtosObj.getFromTime().toString())
						.setParameter("toTime", slotMasterDtosObj.getToTime().toString())
						.setParameterList("dayIds", slotMasterDtosObj.getDayArrayList())
						.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
					String message=null;
					if(slotMasterDtolist!=null && slotMasterDtolist.size()>0)
					{
						
						StringBuilder docs = new StringBuilder();
						for(SlotMasterDto dto : slotMasterDtolist)
						{
							if(docs.length()>0)
							{
								docs.append(", "+dto.getDoctorName());
							}else{
								docs.append(dto.getDoctorName());
							}
						}
						message = "There are some overlapping schedules for "+docs+", unable to generate new schedules...!!";
						
						return new Response(ERROR, ALREADY_EXIST_CODE, message, null, null);
					}
				}if(slotTypeId==4)
				{
					String query = " select " 
							 + " 	spl.speciality_name as \"specialityName\" " 
							 + " from " 
							 + " 	opd.m_slot_master msm " 
							+ " 	inner join doctor.m_speciality_master spl "
							+ " 		on spl.speciality_id = msm.speciality_id "
							 + " where " 
							 + " 	msm.speciality_id in (:deptIds) " 
							 + " 	and to_char(msm.appointment_date, 'YYYY-MM-DD') >= :fromDate " 
							 + " 	and to_char(msm.appointment_date, 'YYYY-MM-DD') <= :toDate " 
							 + " 	and to_char(msm.from_time, 'HH24:MI:SS') >= :fromTime " 
							 + " 	and to_char(msm.to_time, 'HH24:MI:SS') <= :toTime "
							 + " 	and msm.slot_type_id =:slotTypeId " 
							 + " 	and msm.unit_id =:unitId " 
							 + " 	and msm.status ='A' "
							 + " 	and msm.day_id in (:dayIds) " 
							 +"		group by msm.speciality_id,spl.speciality_name ";
					
					List<SlotMasterDto> slotMasterDtolist = sessionFactory.getCurrentSession().createSQLQuery(query)
						.setParameterList("deptIds", deptIds)
						.setParameter("unitId", unitId)
						.setParameter("slotTypeId", slotTypeId)
						.setParameter("fromDate", slotMasterDtosObj.getFromDate().substring(0,10))
						.setParameter("toDate", slotMasterDtosObj.getToDate().substring(0,10))
						.setParameter("fromTime", slotMasterDtosObj.getFromTime().toString())
						.setParameter("toTime", slotMasterDtosObj.getToTime().toString())
						.setParameterList("dayIds", slotMasterDtosObj.getDayArrayList())
						.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
					String message=null;
					if(slotMasterDtolist!=null && slotMasterDtolist.size()>0)
					{
						
						StringBuilder docs = new StringBuilder();
						for(SlotMasterDto dto : slotMasterDtolist)
						{
							if(docs.length()>0)
							{
								docs.append(", "+dto.getSpecialityName());
							}else{
								docs.append(dto.getSpecialityName());
							}
						}
						message = "There are some overlapping schedules for "+docs+", unable to generate new schedules...!!";
						
						return new Response(ERROR, ALREADY_EXIST_CODE, message, null, null);
					}
				}
				
			}
			
			
			return new Response(SUCCESS, null, null, null, null);

		} catch (HibernateException he) {
		logger.error("getOldSlotDetailsDoctor failed", he);
		} catch (Exception e) {
		logger.error("getOldSlotDetailsDoctor failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	

/*	@Override
	public Response getOldSlotDetails(SlotMasterDto slotMasterDtosObj, List<DoctorMasterDto> doctorArrayList,Integer unitId, Integer slotTypeId,char override) {
		try {
			
			Set<Integer> docIds = new HashSet<>();
			Set<Integer> deptIds = new HashSet<>();
			Set<Integer> subSplIds = new HashSet<>();
			Set<Integer> modalityIds = new HashSet<>();
			Set<Integer> unitIds = new HashSet<>();
			for(DoctorMasterDto docDto : doctorArrayList)
			{
				if(docDto.getDoctorId()!=null)
					docIds.add(docDto.getDoctorId());
				
				if(docDto.getSpecialityId()!=null)
					deptIds.add(docDto.getSpecialityId());
				
				if(docDto.getSubSpecialityId()!=null)
					subSplIds.add(docDto.getSubSpecialityId());
				
				if(docDto.getModalityId()!=null)
					modalityIds.add(docDto.getModalityId());
			}
			if(unitArrayList != null && unitArrayList.size()>0) {
				for(DoctorMasterDto docDto : unitArrayList)
				{
					if(docDto.getUnitId()!=null)
						unitIds.add(docDto.getUnitId());
				}
			}else {
				unitIds.add(slotMasterDtosObj.getUnitId());
			}
		
			
			if(override=='Y')
			{
				
				if(slotTypeId==1)
				{
					String query = " delete from opd.m_slot_master "
							 + " where " 
							 + " 	doctor_id in (:docIds) " 
							 + " 	and speciality_id in (:deptIds) " 
							 + " 	and to_char(appointment_date, 'YYYY-MM-DD') >= :fromDate " 
							 + " 	and to_char(appointment_date, 'YYYY-MM-DD') <= :toDate " 
							 + " 	and to_char(from_time, 'HH24:MI:SS') >= :fromTime " 
							 + " 	and to_char(to_time, 'HH24:MI:SS') <= :toTime "
							 + " 	and slot_type_id =:slotTypeId " 
							 + " 	and day_id in (:dayIds) " 
							// + " 	and unit_id =:unitId " 
							+ " 	and unit_id in (:unitIds) " 
							 + " 	and status ='A' "
							 + " 	and slot_id not in (select slot_id from opd.m_appointment_master where slot_id is not null and unit_id =:unitId) ";
					
					sessionFactory.getCurrentSession().createSQLQuery(query)
						.setParameterList("deptIds", deptIds)
						.setParameterList("docIds", docIds)
						//.setParameter("unitId", unitId)
						.setParameterList("unitIds", unitIds)
						.setParameter("slotTypeId", slotTypeId)
						.setParameter("fromDate", slotMasterDtosObj.getFromDate().substring(0,10))
						.setParameter("toDate", slotMasterDtosObj.getToDate().substring(0,10))
						.setParameter("fromTime", slotMasterDtosObj.getFromTime().toString())
						.setParameter("toTime", slotMasterDtosObj.getToTime().toString())
						.setParameterList("dayIds", slotMasterDtosObj.getDayArrayList())
						.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).executeUpdate();
				}
				if(slotTypeId==2){
					String query = "  delete from opd.m_slot_master "
							 + " where " 
							 + "	modality_id in (:modalityIds) "
							 + "	AND sub_speciality_id in (:subSplIds) "
							 + " 	and visit_type_id =:visitTypeId " 
							 + " 	and to_char(appointment_date, 'YYYY-MM-DD') >= :fromDate " 
							 + " 	and to_char(appointment_date, 'YYYY-MM-DD') <= :toDate " 
							 + " 	and to_char(from_time, 'HH24:MI:SS') >= :fromTime " 
							 + " 	and to_char(to_time, 'HH24:MI:SS') <= :toTime "
							 + " 	and slot_type_id =:slotTypeId " 
							 + " 	and day_id in (:dayIds) " 
							// + " 	and unit_id =:unitId " 
							+ " 	and unit_id in (:unitIds) " 
							 + " 	and status ='A' "
							 + " 	and slot_id not in (select slot_id from opd.m_appointment_master where slot_id is not null and unit_id =:unitId) ";
					
					sessionFactory.getCurrentSession().createSQLQuery(query)
						.setParameter("visitTypeId", slotMasterDtosObj.getVisitTypeId())
						.setParameterList("modalityIds", modalityIds)
						.setParameterList("subSplIds", subSplIds)
						//.setParameter("unitId", unitId)
						.setParameterList("unitIds", unitIds)
						.setParameter("slotTypeId", slotTypeId)
						.setParameter("fromDate", slotMasterDtosObj.getFromDate().substring(0,10))
						.setParameter("toDate", slotMasterDtosObj.getToDate().substring(0,10))
						.setParameter("fromTime", slotMasterDtosObj.getFromTime().toString())
						.setParameter("toTime", slotMasterDtosObj.getToTime().toString())
						.setParameterList("dayIds", slotMasterDtosObj.getDayArrayList())
						.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).executeUpdate();
				}if(slotTypeId==4)
				{
					String query = " delete from opd.m_slot_master "
							 + " where " 
							 + " 	speciality_id in (:deptIds) " 
							 + " 	and to_char(appointment_date, 'YYYY-MM-DD') >= :fromDate " 
							 + " 	and to_char(appointment_date, 'YYYY-MM-DD') <= :toDate " 
							 + " 	and to_char(from_time, 'HH24:MI:SS') >= :fromTime " 
							 + " 	and to_char(to_time, 'HH24:MI:SS') <= :toTime "
							 + " 	and slot_type_id =:slotTypeId " 
							 + " 	and day_id in (:dayIds) " 
							// + " 	and unit_id =:unitId " 
							+ " 	and unit_id in (:unitIds) " 
							 + " 	and status ='A' "
							 + " 	and slot_id not in (select slot_id from opd.m_appointment_master where slot_id is not null and unit_id =:unitId) ";
					
					sessionFactory.getCurrentSession().createSQLQuery(query)
						.setParameterList("deptIds", deptIds)
						//.setParameter("unitId", unitId)
						.setParameterList("unitIds", unitIds)
						.setParameter("slotTypeId", slotTypeId)
						.setParameter("fromDate", slotMasterDtosObj.getFromDate().substring(0,10))
						.setParameter("toDate", slotMasterDtosObj.getToDate().substring(0,10))
						.setParameter("fromTime", slotMasterDtosObj.getFromTime().toString())
						.setParameter("toTime", slotMasterDtosObj.getToTime().toString())
						.setParameterList("dayIds", slotMasterDtosObj.getDayArrayList())
						.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).executeUpdate();
				}
				
				return new Response(SUCCESS, null, null, null, null);
			}else{
				
				if(slotTypeId==1)
				{
					String query = " select " 
							 + " 	concat('Dr. ',doc.first_name,' ',doc.last_name,' (',spl.speciality_name,')') as \"doctorName\" " 
							 + " from " 
							 + " 	opd.m_slot_master msm " 
							 + " 	inner join doctor.m_doctor_master doc "
							 + " 		on doc.doctor_id =msm.doctor_id "
							+ " 	inner join doctor.m_speciality_master spl "
							+ " 		on spl.speciality_id =msm.speciality_id "
							 + " where " 
							 + " 	msm.doctor_id in (:docIds) " 
							 + " 	and msm.speciality_id in (:deptIds) " 
							 + " 	and to_char(msm.appointment_date, 'YYYY-MM-DD') >= :fromDate " 
							 + " 	and to_char(msm.appointment_date, 'YYYY-MM-DD') <= :toDate " 
							 + " 	and to_char(msm.from_time, 'HH24:MI:SS') >= :fromTime " 
							 + " 	and to_char(msm.to_time, 'HH24:MI:SS') <= :toTime "
							 + " 	and msm.slot_type_id =:slotTypeId " 
							 + " 	and msm.day_id in (:dayIds) " 
//							 + " 	and msm.unit_id =:unitId " 
							 + " 	and msm.unit_id in (:unitIds) " 
							 + " 	and msm.status ='A' "
							 +"		group by (msm.speciality_id, msm.doctor_id),doc.first_name,doc.last_name,spl.speciality_name ";
					
					List<SlotMasterDto> slotMasterDtolist = sessionFactory.getCurrentSession().createSQLQuery(query)
						.setParameterList("deptIds", deptIds)
						.setParameterList("docIds", docIds)
						//.setParameter("unitId", unitId)
						.setParameterList("unitIds", unitIds)
						.setParameter("slotTypeId", slotTypeId)
						.setParameter("fromDate", slotMasterDtosObj.getFromDate().substring(0,10))
						.setParameter("toDate", slotMasterDtosObj.getToDate().substring(0,10))
						.setParameter("fromTime", slotMasterDtosObj.getFromTime().toString())
						.setParameter("toTime", slotMasterDtosObj.getToTime().toString())
						.setParameterList("dayIds", slotMasterDtosObj.getDayArrayList())
						.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
					String message=null;
					if(slotMasterDtolist!=null && slotMasterDtolist.size()>0)
					{
						
						StringBuilder docs = new StringBuilder();
						for(SlotMasterDto dto : slotMasterDtolist)
						{
							if(docs.length()>0)
							{
								docs.append(", "+dto.getDoctorName());
							}else{
								docs.append(dto.getDoctorName());
							}
						}
						message = "There are some overlapping schedules for "+docs+", unable to generate new schedules...!!";
						
						return new Response(ERROR, ALREADY_EXIST_CODE, message, null, null);
					}
				}if(slotTypeId==2)
				{
					String query = " select " 
							 + " 	moda.modality_desc as \"doctorName\" " 
							 + " from " 
							 + " 	opd.m_slot_master msm " 
							 + " 	inner join m_modality_master moda on "
							 + " 	moda.modality_id  = msm.modality_id "
							 + " where " 
							 + "	msm.visit_type_id=:visitTypeId "
							 + "	and msm.sub_speciality_id in (:subSplIds) "
							 + " 	and msm.modality_id in (:modalityIds) " 
							 + " 	and to_char(msm.appointment_date, 'YYYY-MM-DD') >= :fromDate " 
							 + " 	and to_char(msm.appointment_date, 'YYYY-MM-DD') <= :toDate " 
							 + " 	and to_char(msm.from_time, 'HH24:MI:SS') >= :fromTime " 
							 + " 	and to_char(msm.to_time, 'HH24:MI:SS') <= :toTime "
							 + " 	and msm.slot_type_id =:slotTypeId " 
							 + " 	and msm.day_id in (:dayIds) " 
//							 + " 	and msm.unit_id =:unitId " 
                             + " 	and msm.unit_id in (:unitIds) "
							 + " 	and msm.status ='A' "
							 +"		group by moda.modality_desc ";
					
					List<SlotMasterDto> slotMasterDtolist = sessionFactory.getCurrentSession().createSQLQuery(query)
							.setParameter("visitTypeId", slotMasterDtosObj.getVisitTypeId())
						.setParameterList("modalityIds", modalityIds)
						.setParameterList("subSplIds", subSplIds)
						//.setParameter("unitId", unitId)
						.setParameterList("unitIds", unitIds)
						.setParameter("slotTypeId", slotTypeId)
						.setParameter("fromDate", slotMasterDtosObj.getFromDate().substring(0,10))
						.setParameter("toDate", slotMasterDtosObj.getToDate().substring(0,10))
						.setParameter("fromTime", slotMasterDtosObj.getFromTime().toString())
						.setParameter("toTime", slotMasterDtosObj.getToTime().toString())
						.setParameterList("dayIds", slotMasterDtosObj.getDayArrayList())
						.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
					String message=null;
					if(slotMasterDtolist!=null && slotMasterDtolist.size()>0)
					{
						
						StringBuilder docs = new StringBuilder();
						for(SlotMasterDto dto : slotMasterDtolist)
						{
							if(docs.length()>0)
							{
								docs.append(", "+dto.getDoctorName());
							}else{
								docs.append(dto.getDoctorName());
							}
						}
						message = "There are some overlapping schedules for "+docs+", unable to generate new schedules...!!";
						
						return new Response(ERROR, ALREADY_EXIST_CODE, message, null, null);
					}
				}if(slotTypeId==4)
				{
					String query = " select " 
							 + " 	spl.speciality_name as \"specialityName\" " 
							 + " from " 
							 + " 	opd.m_slot_master msm " 
							+ " 	inner join doctor.m_speciality_master spl "
							+ " 		on spl.speciality_id = msm.speciality_id "
							 + " where " 
							 + " 	msm.speciality_id in (:deptIds) " 
							 + " 	and to_char(msm.appointment_date, 'YYYY-MM-DD') >= :fromDate " 
							 + " 	and to_char(msm.appointment_date, 'YYYY-MM-DD') <= :toDate " 
							 + " 	and to_char(msm.from_time, 'HH24:MI:SS') >= :fromTime " 
							 + " 	and to_char(msm.to_time, 'HH24:MI:SS') <= :toTime "
							 + " 	and msm.slot_type_id =:slotTypeId " 
//							 + " 	and msm.unit_id =:unitId " 
                             + " 	and msm.unit_id in (:unitIds) "
							 + " 	and msm.status ='A' "
							 + " 	and msm.day_id in (:dayIds) " 
							 +"		group by msm.speciality_id,spl.speciality_name ";
					
					List<SlotMasterDto> slotMasterDtolist = sessionFactory.getCurrentSession().createSQLQuery(query)
						.setParameterList("deptIds", deptIds)
						//.setParameter("unitId", unitId)
						.setParameterList("unitIds", unitIds)
						.setParameter("slotTypeId", slotTypeId)
						.setParameter("fromDate", slotMasterDtosObj.getFromDate().substring(0,10))
						.setParameter("toDate", slotMasterDtosObj.getToDate().substring(0,10))
						.setParameter("fromTime", slotMasterDtosObj.getFromTime().toString())
						.setParameter("toTime", slotMasterDtosObj.getToTime().toString())
						.setParameterList("dayIds", slotMasterDtosObj.getDayArrayList())
						.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
					String message=null;
					if(slotMasterDtolist!=null && slotMasterDtolist.size()>0)
					{
						
						StringBuilder docs = new StringBuilder();
						for(SlotMasterDto dto : slotMasterDtolist)
						{
							if(docs.length()>0)
							{
								docs.append(", "+dto.getSpecialityName());
							}else{
								docs.append(dto.getSpecialityName());
							}
						}
						message = "There are some overlapping schedules for "+docs+", unable to generate new schedules...!!";
						
						return new Response(ERROR, ALREADY_EXIST_CODE, message, null, null);
					}
				}
				
			}
			
			
			return new Response(SUCCESS, null, null, null, null);

		} catch (HibernateException he) {
			h

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}*/

	@Override
	public Response deleteUnusedSlots(List<Integer> unitIds) {
	try {
			
			String query = " delete from opd.m_slot_master "
					 + " where " 
					 + " 	to_char(appointment_date, 'YYYY-MM-DD') < :currentDate " 
					 + " 	and slot_id not in (select slot_id from opd.m_appointment_master where slot_id is not null) "
					 + "    and unit_id in (:unitIds) ";
			
			sessionFactory.getCurrentSession().createSQLQuery(query)
				.setParameter("currentDate", GlobalCommonDateUtils.getStringDate(new Date(TimeZoneComponent.getDateByzone(0).getTime() - (1000 * 60 * 60 * 240)), "yyyy-MM-dd"))
				.setParameterList("unitIds", unitIds)
				.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).executeUpdate();
			
			return new Response(SUCCESS, null, null, null, null);

		} catch (HibernateException he) {
throw he;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public Response getSlotDetailsBySlotId(SlotMasterDto slotMasterDto) {

		try {

				String query = new String();
				query = " select "
						+ " concat(to_char(sm.from_time,'HH:MI')) as \"fromTimeString\", "
						+ " concat(to_char(sm.to_time,'HH:MI')) as \"toTimeString\", "
						+ " sm.slot_id as \"slotId\" "
						+ " from  "
						+ " opd.m_slot_master sm  "
                        + " where  "
                    	+ " sm.unit_id =:unitId  "
 	                    + " and sm.slot_id =:slotId "
 	                    + " and sm.status='A' ";
				
				List<SlotMasterDto> slotMasterDtosList = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
						.setParameter("unitId", slotMasterDto.getUnitId())
						.setParameter("slotId", slotMasterDto.getSlotId())
						.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
				return new Response(SUCCESS, null, null, slotMasterDtosList, null);
			
		} catch (HibernateException he) {
		logger.error("getSlotDetailsBySlotId failed", he);
		} catch (Exception e) {
		logger.error("getSlotDetailsBySlotId failed", e);
		}
		return new Response(ERROR, null, null, null, null);

	}

	@Override
	public List<SlotMasterDto> getSlotsNew(SlotMasterDto slotMasterDto) throws ApplicationException {
try {
			
			StringBuilder query = new StringBuilder();
			
			if(slotMasterDto.getSlotTypeId()==4 && slotMasterDto.getDeptWiseAppointment()=='Y')
			{
				query.append("select "
						+ " 	distinct(sm.speciality_id) as \"specialityId\",  "
						+ " 	speMstr.speciality_name as \"specialityName\"  "
						+ " from  "
						//+ " 	opd.m_slot_master sm  "
						+ " 	t_unit_speciality_mapper sm  "
						+ " 	inner join doctor.m_speciality_master speMstr on  "
						+ " 		sm.speciality_id = speMstr.speciality_id  "
						+ " 	inner join public.t_unit_speciality_mapper usm on  "
						+ " 		 usm.speciality_id = sm.speciality_id  "
						+ " where  "
						+ " 	sm.unit_id =:unitId  "
						//+ " 	and usm.unit_id =:unitId "
						+ " 	and usm.dept_wise_appointment = 'Y' "
						//+ "		and sm.doctor_id is null  "
						+ "		and sm.status='A' "
						+ " order by  "
						+ " 	sm.speciality_id  ");
				
				List<SlotMasterDto> slotMasterDtosList = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
						.setFirstResult(slotMasterDto.getOffset() != null ? slotMasterDto.getOffset() : 0)
						.setMaxResults(slotMasterDto.getNoOfRecordsPerPage() != null ? slotMasterDto.getNoOfRecordsPerPage() : 10)
						.setParameter("unitId", slotMasterDto.getUnitId())
						.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
				return slotMasterDtosList;
			}
			if(slotMasterDto.getSlotTypeId()==1)
			{
				
				query.append("select "
				        + "	distinct(mdm.doctor_id) as \"doctorId\", "
						+ "	concat('Dr. ',mdm.first_name,' ',mdm.middle_name,' ',mdm.last_name) as \"doctorName\", "
						+ " msm.speciality_id as \"specialityId\", " + "	um.unit_name as \"unitName\",  "
						+ "  uum.unit_id  as \"unitId\" , msm.speciality_name as \"specialityName\" from doctor.m_doctor_master mdm "
						+ " inner join public.t_user_unit_mapper uum on "
                        + " mdm.user_id = uum.user_id "
						+ "inner join doctor.t_doctor_speciality_mapper speMstr on "
						+ "speMstr.doctor_id = mdm.doctor_id " 
						+ "inner join public.m_unit_master um on  "
						+ " uum.unit_id = um.unit_id "
						+ "inner join doctor.m_speciality_master msm on "
						+ "	speMstr.speciality_id = msm.speciality_id " + "where " + "msm.speciality_id in ( "
						+ "select " + "msm.speciality_id " + "from " + "t_unit_speciality_mapper tusm " + " where "
						+ "	tusm.dept_wise_appointment != 'Y'  "
						+ "	and organization_id =:orgId " + "	and status = 'A' ) " + "and mdm.doctor_id is not null "
						+ "and mdm.organization_id =:orgId "
						+ "and mdm.status ='A' "
						);

				if(slotMasterDto.getSearchKeyword()!=null && !slotMasterDto.getSearchKeyword().equals("") && !slotMasterDto.getSearchKeyword().isEmpty()) {
					query.append(" AND ( "
							+ " lower(concat('Dr. ',mdm.first_name,' ',mdm.middle_name,' ',mdm.last_name)) like :searchKeyword "
							+ " OR lower(msm.speciality_name) like :searchKeyword "
							+ " OR lower(um.unit_name) like :searchKeyword "
							+ " ) ");
				}

				query.append(" order by mdm.doctor_id");

				Query createQuery = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
						.setParameter("orgId", slotMasterDto.getOrganizationId())
						.setFirstResult(slotMasterDto.getOffset() != null ? slotMasterDto.getOffset() : 0)
						.setMaxResults(slotMasterDto.getNoOfRecordsPerPage() != null ? slotMasterDto.getNoOfRecordsPerPage() : 200);
				if(slotMasterDto.getSearchKeyword()!=null && !slotMasterDto.getSearchKeyword().equals("") && !slotMasterDto.getSearchKeyword().isEmpty()) {
				createQuery.setParameter("searchKeyword", "%" + slotMasterDto.getSearchKeyword().toLowerCase() + "%");
				}

				List<SlotMasterDto> slotMasterDtosList = createQuery.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
				return slotMasterDtosList;
			}
			if(slotMasterDto.getSlotTypeId()==2)
			{
				
				query.append("select "
						 + " 	distinct(modality.modality_id) as \"modalityId\", " 
						 + " 	modality.modality_desc as \"modalityDesc\", " 
						 + " 	modality.speciality_id as \"specialityId\", " 
						 + " 	speMstr.speciality_name as \"specialityName\", " 
						 + " 	modality.sub_speciality_id as \"subSpecialityId\", " 
						 + " 	subSpeMastr.sub_speciality_name as \"subSpecialityName\" " 
						 + " from " 
						// + " 	opd.m_slot_master sm " 
						 + "  	 m_modality_master modality "
						 + " inner join doctor.m_speciality_master speMstr on " 
						 + " 	modality.speciality_id = speMstr.speciality_id " 
						 + " inner join doctor.m_sub_speciality_master subSpeMastr on " 
						 + " 	modality.sub_speciality_id = subSpeMastr.sub_speciality_id " 
						 //+ " inner join m_modality_master modality on " 
						 //+ " 	modality.modality_id = modality.modality_id	 " 
						 + " where " 
						 + " 	modality.unit_id =:unitId " 
						 + " 	and modality.status = 'A' " 
						 + " order by " 
						 + " 	modality.modality_id ");
				
				
				List<SlotMasterDto> slotMasterDtosList = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
						.setFirstResult(slotMasterDto.getOffset() != null ? slotMasterDto.getOffset() : 0)
						.setMaxResults(slotMasterDto.getNoOfRecordsPerPage() != null ? slotMasterDto.getNoOfRecordsPerPage() : 10)
						//.setParameter("orgId", slotMasterDto.getOrganizationId())
						.setParameter("unitId", slotMasterDto.getUnitId())
						//.setParameter("slotTypeId", slotMasterDto.getSlotTypeId())
						.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
				return slotMasterDtosList;
			}
			if(slotMasterDto.getSlotTypeId()==3)
			{
				
				List<SlotMasterDto> slotMasterDtosList = sessionFactory.getCurrentSession().getNamedQuery("GET_ACTIVE_APPOINTMENTS")
						.setFirstResult(slotMasterDto.getOffset() != null ? slotMasterDto.getOffset() : 0)
						.setMaxResults(slotMasterDto.getNoOfRecordsPerPage() != null ? slotMasterDto.getNoOfRecordsPerPage() : 10)
						.setParameter("orgId", slotMasterDto.getOrganizationId())
						.setParameter("unitId", slotMasterDto.getUnitId())
						.setParameter("slotTypeId", slotMasterDto.getSlotTypeId())
						.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
				return slotMasterDtosList;
			}

		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			throw e;
		} 
		return null;
	}

	@Override
	@Transactional
	public Response copyBackdatedSlots(SlotMasterDto dto) throws ApplicationException {
		try {
			LocalDate today1 = LocalDate.now();
			String appDate = today1.toString();
			System.out.println(today1);
			StringBuilder stringQuery = new StringBuilder(sessionFactory.getCurrentSession()
					.getNamedQuery("GET_BACK_DATED_SLOTS").getQueryString().toString());
			SQLQuery query = sessionFactory.getCurrentSession().createSQLQuery(stringQuery.toString());
			List<SlotMasterDto> slotMasterDtosList = query
					.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).setMaxResults(500).list();
			return new Response(SUCCESS, null, null, slotMasterDtosList, null);
		} catch (HibernateException he) {
		throw new ApplicationException(he.getMessage(), he);
	} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response saveBackDatedSlotsToSlotLogs(List<SlotMasterDto> slotMasterDtosList) throws ApplicationException {
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			int x[];
			LocalDate today1 = LocalDate.now();
			String appDate = today1.toString();
			System.out.println(today1);

			connection = driverManager.getConnection();
			connection.setAutoCommit(false);

			 statement = connection.prepareStatement("INSERT INTO opd.m_slot_master_log "
					+ " (slot_id, organization_id, unit_id, speciality_id, doctor_id, appointment_date, day_id, from_time, to_time, slot_type_id, visit_type_id, created_by, created_date, updated_by, updated_date, status, modality_id, slot_status_id, sub_speciality_id, is_blocked, bed_id, item_id)"
					+ " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");

			int i = 0;
			for (SlotMasterDto entity : slotMasterDtosList) {
				if (null != entity) {
					String timeString = entity.getFromTimeString();

					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
					LocalTime time = LocalTime.parse(timeString, formatter);
					System.out.println("Time: " + time + ":00");

					if (null != entity.getSlotId()) {
						statement.setInt(1, entity.getSlotId());
					} else {
						statement.setNull(1, Types.NULL);
					}

					if (null != entity.getOrganizationId()) {
						statement.setInt(2, entity.getOrganizationId());
					} else {
						statement.setNull(2, Types.NULL);
					}

					if (null != entity.getUnitId()) {
						statement.setInt(3, entity.getUnitId());
					} else {
						statement.setNull(3, Types.NULL);
					}

					if (null != entity.getSpecialityId()) {
						statement.setInt(4, entity.getSpecialityId());
					} else {
						statement.setNull(4, Types.NULL);
					}

					if (null != entity.getDoctorId() && entity.getDoctorId() > 0) {
						statement.setInt(5, entity.getDoctorId());
					} else {
						statement.setNull(5, Types.NULL);
					}

					if (null != entity.getAppointmentDate()) {
						// add logic and set the appointment date
						statement.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
					} else {
						statement.setNull(6, Types.NULL);
					}

					if (null != entity.getDayId()) {
						statement.setInt(7, entity.getDayId());
					} else {
						statement.setNull(7, Types.NULL);
					}

					if (null != entity.getFromTimeString()) {
						statement.setTime(8, new Time(time.getHour(), time.getMinute(), 00));
					} else {
						statement.setNull(8, Types.NULL);
					}

					if (null != entity.getToTimeString()) {
						statement.setTime(9, new Time(time.getHour(), time.getMinute(), 00));
					} else {
						statement.setNull(9, Types.NULL);
					}

					if (null != entity.getSlotTypeId()) {
						statement.setInt(10, entity.getSlotTypeId());
					} else {
						statement.setNull(10, Types.NULL);
					}

					if (null != entity.getVisitTypeId()) {
						statement.setInt(11, entity.getVisitTypeId());
					} else {
						statement.setNull(11, Types.NULL);
					}

					if (null != entity.getCreatedBy()) {
						statement.setInt(12, entity.getCreatedBy());
					} else {
						statement.setNull(12, Types.NULL);
					}

					if (null != entity.getCreatedDate()) {
						// add logic and set the CreatedDate
						statement.setTimestamp(13, new Timestamp(System.currentTimeMillis()));
					} else {
						statement.setNull(13, Types.NULL);
					}

					if (null != entity.getUpdatedBy()) {
						statement.setInt(14, entity.getUpdatedBy());
					} else {
						statement.setNull(14, Types.NULL);
					}

					if (null != entity.getUpdatedDate()) {
						// add logic and set the getUpdatedDate
						statement.setTimestamp(15, new Timestamp(System.currentTimeMillis()));
					} else {
						statement.setNull(15, Types.NULL);
					}

					if (null != entity.getStatus()) {
						statement.setString(16, String.valueOf(entity.getStatus()));
					} else {
						statement.setNull(16, Types.NULL);
					}

					if (null != entity.getModalityId()) {
						statement.setInt(17, entity.getModalityId());
					} else {
						statement.setNull(17, Types.NULL);
					}

					if (null != entity.getSlotStatusId()) {
						statement.setInt(18, entity.getSlotStatusId());
					} else {
						statement.setNull(18, Types.NULL);
					}

					if (null != entity.getSubSpecialityId()) {
						statement.setInt(19, entity.getSubSpecialityId());
					} else {
						statement.setNull(19, Types.NULL);
					}

					if (null != entity.getIsBlocked()) {
						statement.setInt(20, entity.getIsBlocked());
					} else {
						statement.setNull(20, Types.NULL);
					}

					if (null != entity.getBedId()) {
						statement.setInt(21, entity.getBedId());
					} else {
						statement.setNull(21, Types.NULL);
					}

					if (null != entity.getItemId()) {
						statement.setInt(22, entity.getItemId());
					} else {
						statement.setNull(22, Types.NULL);
					}

					statement.addBatch();
					i++;
					if (i % 100 == 0 || i == slotMasterDtosList.size()) {
						x = statement.executeBatch(); // Execute every 500 items.
						connection.commit();
					}
				}

				
			}

		} catch (HibernateException he) {
		throw new ApplicationException(he.getMessage(), he);
	} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}finally{
			try {
				if (statement != null) {
					statement.close();
				}

				connection.setAutoCommit(true);
				connection.close();
			} catch (SQLException e) {
			logger.error("saveBackDatedSlotsToSlotLogs failed", e);
			}
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	@Transactional
	public Response deleteBackdatedSlotsFromSlotMaster(List<Integer> slotIdList) throws ApplicationException {
		try {
			System.out.println("Dao deleteBackdatedSlotsFromSlotMaster");
			sessionFactory.getCurrentSession().createSQLQuery("DELETE FROM opd.m_slot_master where slot_id IN (:slotIdlist)")
			.setParameterList("slotIdlist", slotIdList)
			.executeUpdate();			
			return new Response(SUCCESS, null, null, null, null);
		} catch (HibernateException he) {
		throw new ApplicationException(he.getMessage(), he);
	} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getFlagCheckMyLife(SlotMasterDto slotMasterDto) throws ApplicationException {
		try {
			
			String query = "select is_sync_scheduling as  \"isSyncScheduling\" from m_organization_master mom where organization_id =" + slotMasterDto.getOrganizationId();

			OrganizationMasterDto configInfoDto = (OrganizationMasterDto) sessionFactory.getCurrentSession().createSQLQuery(query)
				.setResultTransformer(Transformers.aliasToBean(OrganizationMasterDto.class)).uniqueResult();
			return new Response(SUCCESS, null, null, null, configInfoDto);
		} catch (HibernateException he) {
		throw new ApplicationException(he.getMessage(), he);
	} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	public List<SlotMasterDto> getLastAppointmentDate(List<SlotMasterDto> slotMasterDtoList, Integer orgnizationId) {
		List <SlotMasterDto> list = new ArrayList<>();

		List<Integer> doctorIds = slotMasterDtoList.stream().map(SlotMasterDto::getDoctorId).collect(Collectors.toList());

		try {

			String query = "select msm.doctor_id as \"doctorId\", msm.speciality_id as \"specialityId\", "
				+	" max(msm.appointment_date) as \"lastActiveSlotDate\"  from opd.m_slot_master msm "
				+	" where doctor_id in ("+doctorIds.toString().replaceAll("\\[", "").replaceAll("\\]", "")+")  and organization_id  = "+ orgnizationId
				+	" group by msm.doctor_id, msm.speciality_id";

			 list = (List<SlotMasterDto>) sessionFactory.getCurrentSession().createSQLQuery(query)
					.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();

			return list;
		} catch (HibernateException he) {
		logger.error("getLastAppointmentDate failed", he);
		} catch (Exception e) {
		logger.error("getLastAppointmentDate failed", e);
		}
		return null;
	}

}