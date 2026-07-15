package com.param.global.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.adt.templet.model.AppointmentTemplateService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.model.AppointmentTemplateMasterMapperDto;
import com.param.global.model.AppointmentTemplateServiceDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class AppointmentTemplateServiceDaoImpl extends GenericDao<AppointmentTemplateService, Integer> implements IAppointmentTemplateServiceDao,ICommonConstants
{

	final static Logger logger = Logger.getLogger(AppointmentTemplateServiceDaoImpl.class);

	public AppointmentTemplateServiceDaoImpl() {
		super(AppointmentTemplateService.class);
	}

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response saveAppointmentTemplateService(AppointmentTemplateServiceDto dto)
			throws ApplicationException {

		try
		{	
			AppointmentTemplateService master = new AppointmentTemplateService();
			master.setAppointmentTemplateMasterId(dto.getAppointmentTemplateMasterId());
			master.setAppointmentTemplateMasterMapperId(dto.getAppointmentTemplateMasterMapperId());
			master.setUnitId(dto.getUnitId());
			master.setServiceId(dto.getServiceId());
			master.setUpdatedBy(dto.getCreatedBy());
			master.setCreatedBy(dto.getCreatedBy());
			master.setOrganizationId(dto.getOrganizationId());
			master.setCreatedDate(TimeZoneComponent.getDateByzone(dto.getUnitId()));
			master.setUpdatedDate( TimeZoneComponent.getDateByzone(dto.getUnitId()));
			master.setConsultantSpecialityId(dto.getConsultantSpecialityId());
			if(dto.getAppointmentTemplateServiceId()!= null) {
				master.setAppointmentTemplateServiceId(dto.getAppointmentTemplateServiceId());
				master.setStatus(dto.getStatus());
				master = update(master);
			}else {
				master.setStatus('A');
				master = save(master);
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, master);

		} catch (Exception e)
		{
			logger.error("saveAppointmentTemplateService failed", e);
		}
		return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);

	}

	@Override
	public Response getServiceList(AppointmentTemplateMasterMapperDto dto)
			throws ApplicationException {
		try {
			List<AppointmentTemplateServiceDto> list=sessionFactory.getCurrentSession().createSQLQuery(""
					+ " select " + 
					" ats.appointment_template_master_mapper_id as \"appointmentTemplateMasterMapperId\"," + 
					" ats.appointment_template_master_id as \"appointmentTemplateMasterId\"," + 
					" ats.appointment_template_service_id as \"appointmentTemplateServiceId\"," + 
					" ats.service_id as \"serviceId\"," + 
					" msm.service_standard_name as \"serviceName\"," + 
					" ats.status  as \"status\" ," 
					+ " ats.organization_id  as \"organizationId\", " + 
					"	ats.unit_id as \"unitId\", " + 
					"	ats.created_by as \"createdBy\","
					+ "	msm.speciality_id as \"specialityId\", " + 
					"	ats.consultant_speciality_id as \"consultantSpecialityId\" ,"
					+ "msm.sub_speciality_id as \"subSpecialityId\" , "
					+ "msmaster.speciality_name  as \"specialityName\" " + 
					" from public.m_appointment_template_service ats " + 
					" inner join service.m_service_master msm on msm.service_master_id = ats.service_id "
					+"left join doctor.m_speciality_master msmaster on msmaster.speciality_id  = ats.consultant_speciality_id " + 
					" where ats.appointment_template_master_mapper_id =:templateMasterId and ats.status  = 'A'  ")
					.setParameter("templateMasterId", dto.getAppointmentTemplateMasterMapperId())
					.setResultTransformer(Transformers.aliasToBean(AppointmentTemplateServiceDto.class))
					.list();
		 return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
	} catch (HibernateException he) {
		logger.error("getServiceList failed", he);
	} catch (Exception e) {
		logger.error("getServiceList failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);

	}

}
