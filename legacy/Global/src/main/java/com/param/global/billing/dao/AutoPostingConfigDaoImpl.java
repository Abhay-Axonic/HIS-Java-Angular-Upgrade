package com.param.global.billing.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.billing.global.transaction.config.model.AutoPostingServicesCofiguration;
import com.param.global.billing.dto.AutoPostingServicesConfigDto;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({"unchecked","rawtypes"})
public class AutoPostingConfigDaoImpl extends GenericDao<AutoPostingServicesCofiguration, Integer> implements IAutoPostingConfigDao,ICommonConstants{

	public AutoPostingConfigDaoImpl() {
		super(AutoPostingServicesCofiguration.class);
	}
	
	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	public Response saveAutoPostingConfig(AutoPostingServicesConfigDto postingServicesConfigDto1) {
		try{
			for(AutoPostingServicesConfigDto postingServicesConfigDto:postingServicesConfigDto1.getPostingServicesConfigDtosList())
			{
			AutoPostingServicesCofiguration autoPostingServicesCofiguration = new AutoPostingServicesCofiguration();
			    if(postingServicesConfigDto.getId()!=null) 
			    {
			    	autoPostingServicesCofiguration.setId(postingServicesConfigDto.getId());
			    }
				autoPostingServicesCofiguration.setOrgId(postingServicesConfigDto.getOrgId());
				autoPostingServicesCofiguration.setUnitId(postingServicesConfigDto.getUnitId());
				autoPostingServicesCofiguration.setServiceId(postingServicesConfigDto.getServiceId());
				autoPostingServicesCofiguration.setPostingTime(postingServicesConfigDto.getPostingTime());
				autoPostingServicesCofiguration.setIsHalfDay(postingServicesConfigDto.getIsHalfDay());
				autoPostingServicesCofiguration.setBufferTimeApplicable(postingServicesConfigDto.getBufferTimeApplicable());
				
				if(postingServicesConfigDto.getHalfDayHrs()!=null)
					autoPostingServicesCofiguration.setHalfDayHrs(postingServicesConfigDto.getHalfDayHrs());
				
				if(postingServicesConfigDto.getBufferTimeForAdmission()!=null)
					autoPostingServicesCofiguration.setBufferTimeForAdmission(postingServicesConfigDto.getBufferTimeForAdmission());
				
				if(postingServicesConfigDto.getBufferTimeForDischarge()!=null)
					autoPostingServicesCofiguration.setBufferTimeForDischarge(postingServicesConfigDto.getBufferTimeForDischarge());
				
				autoPostingServicesCofiguration.setStatus(postingServicesConfigDto.getStatus());
				autoPostingServicesCofiguration.setCreatedBy(postingServicesConfigDto.getCreatedBy());
				autoPostingServicesCofiguration.setUpdatedBy(postingServicesConfigDto.getCreatedBy());
				autoPostingServicesCofiguration.setCreatedDate(TimeZoneComponent.getDateByzone(postingServicesConfigDto.getUnitId()));
				autoPostingServicesCofiguration.setUpdatedDate(TimeZoneComponent.getDateByzone(postingServicesConfigDto.getUnitId()));
				autoPostingServicesCofiguration.setIsPeriodic(postingServicesConfigDto.getIsPeriodic());
				autoPostingServicesCofiguration.setIsBedChargesService(postingServicesConfigDto.getIsBedChargesService());
			sessionFactory.getCurrentSession().saveOrUpdate(autoPostingServicesCofiguration);	
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		}catch(Exception e){
			throw e;
		}
	}

	@Override
	public Response getAutoPostingConfigList(Integer orgId, Integer unitId) throws ApplicationException {
		try{
			
			String query ="select " 
					 + " 	autopostin0_.id as \"id\", " 
					 + " 	autopostin0_.org_id as \"orgId\", " 
					 + " 	autopostin0_.unit_id as \"unitId\", " 
					 + " 	autopostin0_.service_id as \"serviceId\", " 
					 + " 	servicemas1_.speciality_id as \"specialityId\", " 
					 + " 	autopostin0_.posting_time as \"postingTime\", " 
					 + " 	autopostin0_.is_half_day as \"isHalfDay\", " 
					 + " 	autopostin0_.half_day_hrs as \"halfDayHrs\", " 
					 + " 	autopostin0_.is_buffer_time_applicable as \"bufferTimeApplicable\", " 
					 + " 	autopostin0_.buffer_time_for_admission as \"bufferTimeForAdmission\", " 
					 + " 	autopostin0_.buffer_time_for_discharge as \"bufferTimeForDischarge\", " 
					 + " 	autopostin0_.status as \"status\", "
					 + " 	usmd.is_doc_req as \"isDocReq\", "
					 + "	servicemas1_.service_standard_name as \"serviceStandardName\", "
					 + "	autopostin0_.is_bed_charges_service as \"isBedChargesService\" "
					 + " from " 
					 + " 	public.t_auto_posting_services_cofiguration autopostin0_ " 
					 + " inner join service.m_service_master servicemas1_ on " 
					 + " 	autopostin0_.service_id = servicemas1_.service_master_id "
					 + " inner join service.t_unit_service_master_details usmd on "
					 + " 	usmd.service_master_id  = servicemas1_.service_master_id " 
					 + " where "
					 + "	autopostin0_.unit_id = usmd.unit_id " 
					 + " 	and autopostin0_.org_id =:orgId "
					 + " 	and autopostin0_.unit_id =:unitId "
					 + " 	and autopostin0_.status = 'A' " 
					 + " 	and autopostin0_.is_periodic = 'N' " ;
					// + " 	and '"+GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId),"yyyy-MM-dd")+"' between to_char(servicemas1_.valid_from,'YYYY-MM-DD') and to_char(servicemas1_.valid_till,'YYYY-MM-DD') ";
			
			
			Query createQuery = sessionFactory.getCurrentSession()
					.createSQLQuery(query);
			createQuery.setParameter("orgId",orgId);
			createQuery.setParameter("unitId",unitId);

			List<AutoPostingServicesConfigDto> list=createQuery.setResultTransformer(Transformers.aliasToBean(AutoPostingServicesConfigDto.class)).list();
			for(AutoPostingServicesConfigDto l:list) 
			{
				l.setIsSelected((l.getId()!=null&&l.getId()>0)?'Y':'N');
			}
			
			
			String query2 ="select " 
					 + " 	autopostin0_.id as \"id\", " 
					 + " 	autopostin0_.org_id as \"orgId\", " 
					 + " 	autopostin0_.unit_id as \"unitId\", " 
					 + " 	autopostin0_.service_id as \"serviceId\", " 
					 + " 	servicemas1_.speciality_id as \"specialityId\", " 
					 + " 	autopostin0_.posting_time as \"postingTime\", " 
					 + " 	autopostin0_.is_half_day as \"isHalfDay\", " 
					 + " 	autopostin0_.half_day_hrs as \"halfDayHrs\", " 
					 + " 	autopostin0_.is_buffer_time_applicable as \"bufferTimeApplicable\", " 
					 + " 	autopostin0_.buffer_time_for_admission as \"bufferTimeForAdmission\", " 
					 + " 	autopostin0_.buffer_time_for_discharge as \"bufferTimeForDischarge\", " 
					 + " 	autopostin0_.status as \"status\", "
					 + "	servicemas1_.service_standard_name as \"serviceStandardName\", "
					 + "	autopostin0_.is_bed_charges_service as \"isBedChargesService\" "
					 + " from " 
					 + " 	public.t_auto_posting_services_cofiguration autopostin0_ " 
					 + " inner join service.m_service_master servicemas1_ on " 
					 + " 	autopostin0_.service_id = servicemas1_.service_master_id " 
					 + " where " 
					 + " 	autopostin0_.org_id =:orgId "
					 + " 	and autopostin0_.unit_id =:unitId "
					 + " 	and autopostin0_.status = 'A' " 
					 + " 	and autopostin0_.is_periodic = 'Y' " ;
					 //+ " 	and '"+GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId),"yyyy-MM-dd")+"' between to_char(servicemas1_.valid_from,'YYYY-MM-DD') and to_char(servicemas1_.valid_till,'YYYY-MM-DD') ";
			


			Query createQueryS = sessionFactory.getCurrentSession()
					.createSQLQuery(query2);
			createQueryS.setParameter("orgId",orgId);
			createQueryS.setParameter("unitId",unitId);

			List<AutoPostingServicesConfigDto> periodicList=createQueryS.setResultTransformer(Transformers.aliasToBean(AutoPostingServicesConfigDto.class)).list();
			
			
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, list, periodicList);
		}catch(Exception e){
			throw e;
		}
	}

	@Override
	public Response checkExsistance(AutoPostingServicesConfigDto postingServicesConfigDto) throws ApplicationException {
		try{
			List<AutoPostingServicesConfigDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("CHECK_EXISTANCE")
					.setParameter("orgId", postingServicesConfigDto.getOrgId())
					.setParameter("unitId", postingServicesConfigDto.getUnitId())
					.setParameter("serviceId", postingServicesConfigDto.getServiceId())
					.setResultTransformer(Transformers.aliasToBean(AutoPostingServicesConfigDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, list);
		}catch(Exception e){
			throw e;
		}
	}

	@Override
	public Response updateServiceAutoPostingStatus(AutoPostingServicesConfigDto postingServicesConfigDto)
			throws ApplicationException {
		try{
			AutoPostingServicesCofiguration autoPostingServicesCofiguration=findById(postingServicesConfigDto.getId());
				autoPostingServicesCofiguration.setStatus(postingServicesConfigDto.getStatus());
				autoPostingServicesCofiguration.setUpdatedBy(postingServicesConfigDto.getUpdatedBy());
				autoPostingServicesCofiguration.setUpdatedDate(GlobalCommonDateUtils.getDate(postingServicesConfigDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			sessionFactory.getCurrentSession().update(autoPostingServicesCofiguration);	
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		}catch(Exception e){
			throw e;
		}
	}

	@Override
	public Response getAutoPostingConfigList(Integer organizationId, Integer unitId, Integer postingId)
			throws ApplicationException {
		try{
			List<AutoPostingServicesConfigDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_AUTO_POSTING_CONFIG_LIST_FOR_ROOM_RENT_POSTING")
					.setParameter("orgId", organizationId)
					.setParameter("unitId", unitId)
					.setParameter("postingId", postingId)
					.setResultTransformer(Transformers.aliasToBean(AutoPostingServicesConfigDto.class)).list();
			for(AutoPostingServicesConfigDto l:list) {
				l.setIsSelected('N');
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, list, null);
		}catch(Exception e){
			throw e;
		}
	}
	
	@Override
	public Response getAutoPostingConfigListForRoomRent()throws ApplicationException {
		try{
			
			String query ="select " 
					 + " 	autopostin0_.id as \"id\", " 
					 + " 	autopostin0_.org_id as \"orgId\", " 
					 + " 	autopostin0_.unit_id as \"unitId\", " 
					 + " 	autopostin0_.service_id as \"serviceId\", " 
					 + " 	servicemas1_.speciality_id as \"specialityId\", " 
					 + " 	autopostin0_.posting_time as \"postingTime\", " 
					 + " 	autopostin0_.is_half_day as \"isHalfDay\", " 
					 + " 	autopostin0_.half_day_hrs as \"halfDayHrs\", " 
					 + " 	autopostin0_.is_buffer_time_applicable as \"bufferTimeApplicable\", " 
					 + " 	autopostin0_.buffer_time_for_admission as \"bufferTimeForAdmission\", " 
					 + " 	autopostin0_.buffer_time_for_discharge as \"bufferTimeForDischarge\", " 
					 + " 	autopostin0_.status as \"status\", "
					 + " 	usmd.is_doc_req as \"isDocReq\", "
					 + "	servicemas1_.service_standard_name as \"serviceStandardName\", "
					 + "	autopostin0_.is_bed_charges_service as \"isBedChargesService\" "
					 + " from " 
					 + " 	public.t_auto_posting_services_cofiguration autopostin0_ " 
					 + " inner join service.m_service_master servicemas1_ on " 
					 + " 	autopostin0_.service_id = servicemas1_.service_master_id "
					 + " inner join service.t_unit_service_master_details usmd on "
					 + " 	usmd.service_master_id  = servicemas1_.service_master_id " 
					 + " where "
					 + "	autopostin0_.unit_id = usmd.unit_id " 
					 + " 	and autopostin0_.status = 'A' " 
					 + " 	and autopostin0_.is_periodic = 'N' " ;
			
			List<AutoPostingServicesConfigDto> list=sessionFactory.getCurrentSession()
					.createSQLQuery(query)
					//.setParameterList("unitIds", unitIds)
					.setResultTransformer(Transformers.aliasToBean(AutoPostingServicesConfigDto.class)).list();
			
			
			
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, list, null);
		}catch(Exception e){
			throw e;
		}
	}
	
	
	
}
