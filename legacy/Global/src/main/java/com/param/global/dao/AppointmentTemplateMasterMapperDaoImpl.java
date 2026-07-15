package com.param.global.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.adt.templet.model.AppointmentTemplateMasterMapper;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.model.AppointmentTemplateMasterDto;
import com.param.global.model.AppointmentTemplateMasterMapperDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class AppointmentTemplateMasterMapperDaoImpl  extends GenericDao<AppointmentTemplateMasterMapper, Integer> implements IAppointmentTemplateMasterMapperDao,ICommonConstants
{
	final static Logger logger = Logger.getLogger(AppointmentTemplateMasterMapperDaoImpl.class);


	public AppointmentTemplateMasterMapperDaoImpl() {
		super(AppointmentTemplateMasterMapper.class);
	}

	@Autowired
	SessionFactory sessionFactory;

	
	@Override
	public Response saveAppointmentTemplateMapper(AppointmentTemplateMasterMapperDto dto)
			throws ApplicationException {
		try
		{	
			AppointmentTemplateMasterMapper master = new AppointmentTemplateMasterMapper();
			master.setAppointmentTemplateMasterId(dto.getAppointmentTemplateMasterId());
			master.setSrNo(dto.getSrNo());
			master.setDays(dto.getDays());
			master.setUnitId(dto.getUnitId());
			master.setUpdatedBy(dto.getCreatedBy());
			master.setCreatedBy(dto.getCreatedBy());
			master.setOrganizationId(dto.getOrganizationId());
			master.setCreatedDate(TimeZoneComponent.getDateByzone(dto.getUnitId()));
			master.setUpdatedDate( TimeZoneComponent.getDateByzone(dto.getUnitId()));
			if(dto.getAppointmentTemplateMasterMapperId()!= null) {
				master.setAppointmentTemplateMasterMapperId(dto.getAppointmentTemplateMasterMapperId());
				master.setStatus(dto.getStatus());
				master = update(master);
			}else {
				master.setStatus('A');
				master = save(master);
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, master);

		} catch (Exception e)
		{
		logger.error("saveAppointmentTemplateMapper failed", e);
		}
		return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);


	}


	@Override
	public Response getAppointmentTemplateMapperData(AppointmentTemplateMasterDto dto) throws ApplicationException {

		try {
			List<AppointmentTemplateMasterMapperDto> list=sessionFactory.getCurrentSession().createSQLQuery(""
					+ " select  "  + 
					" appointment_template_master_mapper_id as \"appointmentTemplateMasterMapperId\", "  + 
					" appointment_template_master_id as \"appointmentTemplateMasterId\", "  + 
					" days as \"days\", "  + 
					" sr_no  as \"srNo\", "  + 
					" status  as \"status\" ,"  
					+ " organization_id  as \"organizationId\", " + 
					"	unit_id as \"unitId\", " + 
					"	created_by as \"createdBy\" " + 
					" from public.m_appointment_template_master_mapper where appointment_template_master_id=:templateMasterId and status  = 'A' order by 1 asc")
					.setParameter("templateMasterId", dto.getAppointmentTemplateMasterId())
					.setResultTransformer(Transformers.aliasToBean(AppointmentTemplateMasterMapperDto.class))
					.list();
		 return new Response(SUCCESS, SUCCESS_CODE, null, list, null);	
	} catch (HibernateException he) {
	logger.error("getAppointmentTemplateMapperData failed", he);
	} catch (Exception e) {
	logger.error("getAppointmentTemplateMapperData failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);

	}
}
