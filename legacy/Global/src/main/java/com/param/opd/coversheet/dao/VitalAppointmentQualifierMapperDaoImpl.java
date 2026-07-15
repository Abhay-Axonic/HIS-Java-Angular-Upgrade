package com.param.opd.coversheet.dao;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.opd.coversheet.dto.AppointmentVitalMapperDto;
import com.param.opd.coversheet.model.AppointmentVitalMapper;
import com.param.opd.coversheet.model.VitalAppointmentQualifierMapper;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class VitalAppointmentQualifierMapperDaoImpl extends GenericDao<VitalAppointmentQualifierMapper, Integer> implements IVitalAppointmentQualifierMapperDao, ICommonConstants {
	final static Logger logger = Logger.getLogger(VitalAppointmentQualifierMapperDaoImpl.class);


	public VitalAppointmentQualifierMapperDaoImpl() {
		super(VitalAppointmentQualifierMapper.class);
		// TODO Auto-generated constructor stub
	}
	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	public Response saveVitalAppointmentQualifierMapper(AppointmentVitalMapperDto appointmentVitalMapperDto) throws ApplicationException {
		try
		{
					VitalAppointmentQualifierMapper mppr = new VitalAppointmentQualifierMapper();
					mppr.setVitalId(appointmentVitalMapperDto.getVitalId());
					mppr.setAppointmentVitalMapperId(appointmentVitalMapperDto.getAppointmentVitalMapperId());
					mppr.setVitalQualifireId(appointmentVitalMapperDto.getVitalQualifireId());
					mppr.setPatientId(appointmentVitalMapperDto.getPatientId());
					mppr.setAdmissionId(appointmentVitalMapperDto.getAdmissionId());
					mppr.setCreatedBy(appointmentVitalMapperDto.getCreatedBy());
					mppr.setCreatedDate(GlobalCommonDateUtils.getDate(appointmentVitalMapperDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
					mppr.setEncounterId(appointmentVitalMapperDto.getEncounterId());
					mppr.setOrganizationId(appointmentVitalMapperDto.getOrganizationId());
					mppr.setUnitId(appointmentVitalMapperDto.getUnitId());
					mppr.setUpdatedBy(appointmentVitalMapperDto.getUpdatedBy());
					mppr.setUpdatedDate(GlobalCommonDateUtils.getDate(appointmentVitalMapperDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
					mppr.setStatus(appointmentVitalMapperDto.getStatus());
					
					save(mppr);
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		}catch (HibernateException he) {
		logger.error("saveVitalAppointmentQualifierMapper failed", he);
		} catch (Exception e) {
		logger.error("saveVitalAppointmentQualifierMapper failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}
}
