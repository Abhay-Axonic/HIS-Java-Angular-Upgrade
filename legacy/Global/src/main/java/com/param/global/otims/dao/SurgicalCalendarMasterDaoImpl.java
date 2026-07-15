package com.param.global.otims.dao;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.stereotype.Repository;

import com.param.global.billing.controller.BillingException;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.otims.dto.SurgicalCalendarDetailsDto;
import com.param.global.otims.dto.SurgicalCalendarReqDto;
import com.param.global.otims.dto.SurgicalCalenderMapperDto;
import com.param.otims.model.SurgicalCalendarMaster;
import com.param.otims.model.SurgicalCalenderMapper;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class SurgicalCalendarMasterDaoImpl extends GenericDao<SurgicalCalendarMaster, Integer>
		implements ISurgicalCalendarMasterDao, ICommonConstants {
	final static Logger logger = Logger.getLogger(SurgicalCalendarMasterDaoImpl.class);


	public SurgicalCalendarMasterDaoImpl() {
		super(SurgicalCalendarMaster.class);
	}

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	HikariDataSource driverManager;

	@SuppressWarnings("deprecation")
	@Override
	public Response saveSurgicalCalendarMaster(List<SurgicalCalendarDetailsDto> list) throws ApplicationException {
		Connection connection = null;
		PreparedStatement statement = null;
		try {

			connection = driverManager.getConnection();
			connection.setAutoCommit(false);

			 statement = connection.prepareStatement("INSERT INTO public.m_surgical_calender "
					+ "(speciality_id, doctor_id, from_date, to_date, day_id, from_time, to_time, status, created_by, created_date, updated_by, updated_date, organisation_id, unit_id,procedure_date, calender_status_id) "
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?);");

			int i = 0;

			for (SurgicalCalendarDetailsDto entity : list) {
				statement.setInt(1, entity.getSpecialityId());
				statement.setInt(2, entity.getDoctorId());
				statement.setDate(3,
						new java.sql.Date(GlobalCommonDateUtils.getDate(entity.getFromDate(), "yyyy-M-dd").getTime()));
				statement.setDate(4,
						new java.sql.Date(GlobalCommonDateUtils.getDate(entity.getToDate(), "yyyy-M-dd").getTime()));
				statement.setInt(5, entity.getDayId());
//				statement.setTime(6, new Time(entity.getFromTimeToSave().getHour(),
//						entity.getFromTimeToSave().getMinute(), entity.getFromTimeToSave().getSecond()));
//				statement.setTime(7, new Time(entity.getToTimeToSave().getHour(), entity.getToTimeToSave().getMinute(),
//						entity.getToTimeToSave().getSecond()));
				if (entity.getFromTimeToSave() != null) {
					LocalDateTime fromDateTime = LocalDateTime.ofInstant(entity.getFromTimeToSave(), ZoneId.systemDefault());
					statement.setTime(6, Time.valueOf(fromDateTime.toLocalTime()));
				} else {
					statement.setNull(6, java.sql.Types.TIME);
				}

				if (entity.getToTimeToSave() != null) {
					LocalDateTime toDateTime = LocalDateTime.ofInstant(entity.getToTimeToSave(), ZoneId.systemDefault());
					statement.setTime(7, Time.valueOf(toDateTime.toLocalTime()));
				} else {
					statement.setNull(7, java.sql.Types.TIME);
				}

				statement.setString(8, "A");
				statement.setInt(9, entity.getCreatedBy());
				statement.setDate(10, new java.sql.Date(
						GlobalCommonDateUtils.getDate(entity.getCreatedDate(), "yyyy-M-dd").getTime()));
				statement.setInt(11, entity.getUpdatedBy());
				statement.setDate(12, new java.sql.Date(
						GlobalCommonDateUtils.getDate(entity.getUpdatedDate(), "yyyy-M-dd").getTime()));
				statement.setInt(13, entity.getOrganisationId());
				statement.setInt(14, entity.getUnitId());
				statement.setDate(15, new java.sql.Date(
						GlobalCommonDateUtils.getDate(entity.getProcedureDate(), "yyyy-M-dd").getTime()));
				statement.setInt(16, entity.getCalenderStatusId());

				statement.addBatch();
				i++;

				if (i % 20 == 0 || i == list.size()) {
					statement.executeBatch(); // Execute every 20 items.
					connection.commit();
				}
			}

			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (HibernateException he) {
		logger.error("saveSurgicalCalendarMaster failed", he);
		} catch (Exception e) {
		logger.error("saveSurgicalCalendarMaster failed", e);
		}finally {
			try {
				if (statement != null) {
					statement.close();
				}

				connection.setAutoCommit(true);
				connection.close();
			} catch (SQLException e) {
			logger.error("saveSurgicalCalendarMaster failed", e);
			}

		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response listSergicalCalender(SurgicalCalendarReqDto surgicalCalendarReqDto) throws ApplicationException {
		try{
			List<SurgicalCalendarDetailsDto> list = sessionFactory.getCurrentSession().getNamedQuery("GET_SURGICAL_CALENDER_LIST")
					.setParameter("orgId", surgicalCalendarReqDto.getOrgId())
					.setParameter("unitId", surgicalCalendarReqDto.getUnitId())
					.setFirstResult(surgicalCalendarReqDto.getOffset())
					.setMaxResults(surgicalCalendarReqDto.getRecordPerPage() != 0 ? surgicalCalendarReqDto.getRecordPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(SurgicalCalendarDetailsDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, list, null);
		}catch(Exception e){

			throw e;
		}
	}

	@Override
	public Response countSergicalCalender(SurgicalCalendarReqDto surgicalCalendarReqDto) throws ApplicationException {
		try{
			BigInteger count = (BigInteger) sessionFactory.getCurrentSession().getNamedQuery("GET_SURGICAL_CALENDER_COUNT")
					.setParameter("orgId", surgicalCalendarReqDto.getOrgId())
					.setParameter("unitId", surgicalCalendarReqDto.getUnitId())
					.uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, count);
		}catch(Exception e){

			throw e;
		}
	}

	@Override
	public Response updateStatusByCalenderId(Integer surgicalCalenderId, char status) throws ApplicationException {
		try{
			int result = sessionFactory.getCurrentSession().getNamedQuery("UPDATE_SURGICAL_CALENDER_BY_ID")
				.setParameter("surgicalCalenderId", surgicalCalenderId)
				.setParameter("status", status)
				/*.setParameter("updatedBy", depositMasterDto.getUpdatedBy())
				.setTimestamp("updatedDate", GlobalCommonDateUtils.getDate(depositMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"))*/
			.executeUpdate();
			if(result>0)
			{
				return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
			}
			return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
		}catch(Exception e){

			throw e;
		}
	}

	@Override
	public Response updateStatusByProcedureDate(SurgicalCalendarReqDto dto)
			throws ApplicationException {


			try{
				int result = sessionFactory.getCurrentSession().getNamedQuery("UPDATE_SURGICAL_CALENDER_BY_PROCEDURE_DATE")
					.setParameter("orgId", dto.getOrgId())
					.setParameter("unitId", dto.getUnitId())
					.setParameter("doctorId", dto.getDoctorId())
					.setParameter("specialityId", dto.getSpecialityId())
					.setParameter("status", dto.getStatus())
					.setParameter("procedureDate", GlobalCommonDateUtils.getDate(dto.getProcedureDate(), "yyyy/M/dd"))
				.executeUpdate();
				if(result>0)
				{
					return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
				}
				return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
			}catch(Exception e){

				throw e;
			}
	}

	@Override
	public Response surgicalCalenderDetails(SurgicalCalendarReqDto dto) throws ApplicationException {
		try{
			List<SurgicalCalendarDetailsDto> list = sessionFactory.getCurrentSession().getNamedQuery("GET_SURGICAL_CALENDER_DETAILS")
					.setParameter("orgId", dto.getOrgId())
					.setParameter("unitId", dto.getUnitId())
					.setParameter("doctorId", dto.getDoctorId())
					.setParameter("dayId", dto.getDayId())
					.setParameter("specialityId", dto.getSpecialityId())
					.setParameter("procedureDate", GlobalCommonDateUtils.getDate(dto.getProcedureDate(), "yyyy/M/dd"))
					.setResultTransformer(Transformers.aliasToBean(SurgicalCalendarDetailsDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, list, null);
		}catch(Exception e){

			throw e;
		}
	}

	@Override
	public Response getWeelkyCalenderSlots(SurgicalCalendarReqDto dto) throws ApplicationException {
		try{
			List<SurgicalCalendarDetailsDto> list = sessionFactory.getCurrentSession().getNamedQuery("GET_WEEKLY_CALENDER_SLOTS")
					.setParameter("orgId", dto.getOrgId())
					.setParameter("unitId", dto.getUnitId())
					.setParameter("doctorId", dto.getDoctorId())
					.setParameter("fromDate",GlobalCommonDateUtils.getDate(dto.getFromDate(), "yyyy-M-dd"))
					.setParameter("toDate", GlobalCommonDateUtils.getDate(dto.getToDate(), "yyyy-M-dd"))
					.setResultTransformer(Transformers.aliasToBean(SurgicalCalendarDetailsDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, list, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response saveCalenderSlotMpper(SurgicalCalenderMapperDto dto) throws ApplicationException {
		try{
			SurgicalCalenderMapper master = new SurgicalCalenderMapper();
			master.setPatientId(dto.getPatientId());
			master.setSpecialityId(dto.getSpecialityId());
			master.setProcedureOrderId(dto.getProcedureOrderId());
			master.setAdmissionId(dto.getAdmissionId());
			master.setEncounterId(dto.getEncounterId());
			master.setDoctorId(dto.getDoctorId());
			master.setSurgicalCalenderId(dto.getSurgicalCalenderId());
			master.setIsDeleted('N');
			master.setStatus('A');
			master.setOrganisationId(dto.getOrganisationId());
			master.setUnitId(dto.getUnitId());
			master.setCreatedBy(dto.getCreatedBy());
			master.setCreatedDate(dto.getCreatedDate() != null && !dto.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
			master.setUpdatedBy(dto.getUpdatedBy());
			master.setUpdatedDate(dto.getUpdatedDate() != null && !dto.getUpdatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
			sessionFactory.getCurrentSession().save(master);
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		}catch(HibernateException he){
			throw new BillingException(he.getMessage());
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}	
	}

	@Override
	public Response updateSlotStatusCalenderId(Integer surgicalCalenderId, Integer calenderStatusId)
			throws ApplicationException {
		try{
			int result = sessionFactory.getCurrentSession().getNamedQuery("UPDATE_SURGICAL_CALENDER_STATUS_ID")
				.setParameter("surgicalCalenderId", surgicalCalenderId)
				.setParameter("calenderStatusId", calenderStatusId)
			.executeUpdate();
			if(result>0)
			{
				return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
			}
			return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
		}catch(Exception e){
			
			throw e;
		}
	}

}
