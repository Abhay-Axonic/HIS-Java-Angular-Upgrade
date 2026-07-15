package com.param.global.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.adt.templet.model.AppointmentTemplateMaster;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.model.AppointmentTemplateMasterDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class AppointmentTemplateMasterDaoImpl  extends GenericDao<AppointmentTemplateMaster, Integer> implements IAppointmentTemplateMasterDao,ICommonConstants
{
	final static Logger logger = Logger.getLogger(AppointmentTemplateMasterDaoImpl.class);


	public AppointmentTemplateMasterDaoImpl() {
		super(AppointmentTemplateMaster.class);
	}

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response saveAppointmentTemplate(AppointmentTemplateMasterDto dto) throws ApplicationException {

		try
		{	
			AppointmentTemplateMaster master = new AppointmentTemplateMaster();
			master.setTemplateName(dto.getTemplateName());	
			master.setRemark(dto.getRemark()!= null ? dto.getRemark():null);	
			master.setMorningMond(dto.getMorningMond()); 	
			master.setMorningFrid(dto.getMorningFrid());	
			master.setMorningSatur(dto.getMorningSatur());	
			master.setMorningSund(dto.getMorningSund());	
			master.setMorningThus(dto.getMorningThus());	
			master.setMorningTues(dto.getMorningTues());	
			master.setMorningWedn(dto.getMorningWedn()); 	
			master.setEveningFrid(dto.getEveningFrid());	
			master.setEveningMond(dto.getEveningMond());	
			master.setEveningSatur(dto.getEveningSatur());	
			master.setEveningSund(dto.getEveningSund());	
			master.setEveningThus(dto.getEveningThus());  	
			master.setEveningTues(dto.getEveningTues());	
			master.setEveningWedn(dto.getEveningWedn());	
			master.setUnitId(dto.getUnitId());
			master.setUpdatedBy(dto.getCreatedBy());
			master.setCreatedBy(dto.getCreatedBy());
			master.setOrganizationId(dto.getOrganizationId());
			master.setStatus('A');
			master.setCreatedDate(TimeZoneComponent.getDateByzone(dto.getUnitId()));
			master.setUpdatedDate( TimeZoneComponent.getDateByzone(dto.getUnitId()));
			if(dto.getAppointmentTemplateMasterId()!= null) {
				master.setAppointmentTemplateMasterId(dto.getAppointmentTemplateMasterId());
				master = update(master);
			}else {
				master = save(master);
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, master);

		} catch (Exception e)
		{
		logger.error("saveAppointmentTemplate failed", e);
		}
		return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);

	}

	@Override
	public Response getAppointmentTemplate(AppointmentTemplateMasterDto dto) throws ApplicationException {
		try {
			List<AppointmentTemplateMasterDto> list=sessionFactory.getCurrentSession().createSQLQuery(""
					+ " select  " + 
					"   appointment_template_master_id as \"appointmentTemplateMasterId\", " + 
					"	template_name as \"templateName\", " + 
					"	morning_mond  as \"morningMond\", " + 
					"	morning_tues  as \"morningTues\", " + 
					"	morning_wedn as \"morningWedn\", " + 
					"	morning_thus as \"morningThus\", " + 
					"	morning_frid  as \"morningFrid\", " + 
					"	morning_satur  as \"morningSatur\", " + 
					"	morning_sund as \"morningSund\", " + 
					"	evening_mond  as \"eveningMond\", " + 
					"	evening_tues  as \"eveningTues\", " + 
					"	evening_wedn as \"eveningWedn\", " + 
					"	evening_thus  as \"eveningThus\", " + 
					"	evening_frid as \"eveningFrid\", " + 
					"	evening_satur as \"eveningSatur\", " + 
					"	evening_sund as \"eveningSund\", " + 
					"	remark  as \"remark\", " + 
					"	status  as \"status\","
					+ " organization_id  as \"organizationId\", " + 
					"	unit_id as \"unitId\", " + 
					"	created_by as \"createdBy\" " + 
					"   from m_appointment_template_master "
					+"	where organization_id=:orgId and unit_id=:unitId")
					.setInteger("orgId", dto.getOrganizationId())
					.setInteger("unitId", dto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(AppointmentTemplateMasterDto.class))
					.list();
		 return new Response(SUCCESS, SUCCESS_CODE, null, list, null);	
	} catch (HibernateException he) {
	logger.error("getAppointmentTemplate failed", he);
	} catch (Exception e) {
	logger.error("getAppointmentTemplate failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateStatus(AppointmentTemplateMasterDto dto) throws ApplicationException {
		try{	
			Session session=sessionFactory.getCurrentSession();
			String query = "UPDATE m_appointment_template_master " +
					"SET status = :status " +
					"WHERE appointment_template_master_id = :appointmentTemplateMasterId";
			Integer count = (Integer) session.createSQLQuery(query)
					.setParameter("status", dto.getStatus())
					.setParameter("appointmentTemplateMasterId", dto.getAppointmentTemplateMasterId())
					.executeUpdate();
			return new Response(SUCCESS, SUCCESS_CODE, null, null, count);
			
		}catch (HibernateException he) {
		logger.error("updateStatus failed", he);
		} catch (Exception e) {
		logger.error("updateStatus failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	
}
