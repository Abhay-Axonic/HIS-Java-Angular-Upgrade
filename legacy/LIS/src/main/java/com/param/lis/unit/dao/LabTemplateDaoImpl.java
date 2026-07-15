package com.param.lis.unit.dao;

import java.math.BigInteger;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.entity.lis.unit.LabTemplateMaster;
import com.param.entity.lis.unit.SpecialtyTemplateMapper;
import com.param.global.common.ServiceException;
import com.param.global.dto.TemplateMasterDto;
import com.param.lis.common.dto.TemplateDto;
import com.param.lis.global.common.AbstractDao;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.Response;
import com.param.lis.unit.dto.LabTemplateMasterDto;
import com.param.lis.unit.dto.SpecialtyTemplateMapperDto;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;



@Repository("labTemplateDao")

@SuppressWarnings({"rawtypes", "unchecked"})
public class LabTemplateDaoImpl extends AbstractDao<Integer, LabTemplateMasterDto>
    implements ILabTemplateDao,IError,ICommonConstants {

	final static Logger logger = Logger.getLogger(LabTemplateDaoImpl.class);
	
	
	@Autowired
	private DozerBeanMapper mapper;
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public List<LabTemplateMasterDto> retrieveAllLabTemplates(Integer orgId,Integer orgUnitId,Integer offset, Integer recordPerPage) 
	{
		@SuppressWarnings("unchecked")
		List<LabTemplateMasterDto> templateList =  getSessionFactory().getCurrentSession().getNamedQuery("GET_LAB_TEMPLATE_LIST")
				.setParameter("orgId", orgId)
				.setParameter("orgUnitId", orgUnitId)
				.setFirstResult(offset==null?0:offset)
				.setMaxResults(recordPerPage==null?10:recordPerPage)
				.setResultTransformer(Transformers.aliasToBean(LabTemplateMasterDto.class)).list();
				//System.out.println("list size ::: "+templateList.size()); 
		return templateList;
	}

	@Override
	public boolean createLabTemplate(LabTemplateMasterDto labTemplateMasterDto) {
		Date date = TimeZoneComponent.getDateByzone(labTemplateMasterDto.getOrgUnitId());
		labTemplateMasterDto.setCreatedDate(date.getTime());
		labTemplateMasterDto.setStatus('A');
		LabTemplateMaster labTemplateMaster = mapper.map(labTemplateMasterDto, LabTemplateMaster.class,
				"LabTemplateMasterToLabTemplateMaster");
		
		Integer labTemplateMst= (Integer ) sessionFactory.getCurrentSession().save(labTemplateMaster);
		try
		{
		
		  if(!labTemplateMasterDto.getListSpecialtyTemplateMapperDto().isEmpty())
			  {		
			  
				  for (Iterator iterator = labTemplateMasterDto.getListSpecialtyTemplateMapperDto().iterator(); iterator.hasNext();) 
				  {
					  SpecialtyTemplateMapperDto specialtyTemplateMapperDto = (SpecialtyTemplateMapperDto) iterator.next();
					  SpecialtyTemplateMapper master= new SpecialtyTemplateMapper();
					  master.setLabTemplateId(labTemplateMst);
					  master.setDoctorId(specialtyTemplateMapperDto.getDoctorId());
					  master.setOrgId(specialtyTemplateMapperDto.getOrgId());
					  master.setOrgUnitId(specialtyTemplateMapperDto.getOrgUnitId());
					  master.setStatus(specialtyTemplateMapperDto.getStatus());
					  master.setTemplateTypeId(specialtyTemplateMapperDto.getTemplateTypeId());
					  master.setSpecialityId(specialtyTemplateMapperDto.getSpecialityId());
					  master.setUpdatedBy(specialtyTemplateMapperDto.getUpdatedBy());
					  master.setUpdatedDate(specialtyTemplateMapperDto.getUpdatedDate());
					  master.setCreatedBy(specialtyTemplateMapperDto.getCreatedBy());
					  master.setCreatedDate(date.getTime());
					  master.setIsDeleted('N');
	  				  sessionFactory.getCurrentSession().save(master);
				  }
				  return true;
			  }
		  else 
		  {
			  return false;
		  }
			
		} catch (Exception e)
		{
			logger.error("Exection", e);
			return false;
		}
	}

	

	@Override
	public boolean deleteLabTemplate(Integer labTemplateId) 
	{
		LabTemplateMaster labTemplateMaster = (LabTemplateMaster) getSessionFactory().getCurrentSession().get(LabTemplateMaster.class, labTemplateId);
		if(labTemplateMaster!=null)
		{
			labTemplateMaster.setIsDeleted('Y');
			getSessionFactory().getCurrentSession().update(labTemplateMaster);
			return true;
		}
		{
			return false;
		}
	}

	@SuppressWarnings("unused")
	@Override
	public boolean updateLabTemplate(LabTemplateMasterDto labTemplateMasterDto) {
		Date date = TimeZoneComponent.getDateByzone(labTemplateMasterDto.getOrgUnitId());
		
		try {
			if(labTemplateMasterDto!=null)
			{
				labTemplateMasterDto.setUpdatedDate(date.getTime());
				labTemplateMasterDto.setIsDeleted('N');
				LabTemplateMaster labTemplateMaster = mapper.map(labTemplateMasterDto, LabTemplateMaster.class,
						"LabTemplateMasterToLabTemplateMaster");
				getSessionFactory().getCurrentSession().update(labTemplateMaster) ;
				
				
				Integer labTemplateMstUpdate = (Integer) sessionFactory.getCurrentSession()
						.getNamedQuery("UPDATE_BY_LAB_TEMPLATE_ID")
						.setParameter("labTemplateId", labTemplateMasterDto.getLabTemplateId())
						.setParameter("orgId", labTemplateMasterDto.getOrgId())
						.setParameter("specialityId", labTemplateMasterDto.getListSpecialtyTemplateMapperDto().getFirst().getSpecialityId())
						.executeUpdate();
				
				if(labTemplateMstUpdate >0) {
					
				try
				{
				
				  if(!labTemplateMasterDto.getListSpecialtyTemplateMapperDto().isEmpty())
					  {		
					  
						  for (Iterator iterator = labTemplateMasterDto.getListSpecialtyTemplateMapperDto().iterator(); iterator.hasNext();) 
						  {
							  SpecialtyTemplateMapperDto specialtyTemplateMapperDto = (SpecialtyTemplateMapperDto) iterator.next();
							  SpecialtyTemplateMapper master= new SpecialtyTemplateMapper();
							  master.setLabTemplateId(labTemplateMasterDto.getLabTemplateId());
							  master.setDoctorId(specialtyTemplateMapperDto.getDoctorId());
							  master.setOrgId(specialtyTemplateMapperDto.getOrgId());
							  master.setOrgUnitId(specialtyTemplateMapperDto.getOrgUnitId());
							  master.setStatus(specialtyTemplateMapperDto.getStatus());
							  master.setTemplateTypeId(specialtyTemplateMapperDto.getTemplateTypeId());
							  master.setSpecialityId(specialtyTemplateMapperDto.getSpecialityId());
							  master.setUpdatedBy(specialtyTemplateMapperDto.getUpdatedBy());
							  master.setUpdatedDate(specialtyTemplateMapperDto.getUpdatedDate());
							  master.setCreatedBy(specialtyTemplateMapperDto.getCreatedBy());
							  master.setIsDeleted('N');
							  master.setCreatedDate(date.getTime());
			  				  sessionFactory.getCurrentSession().save(master);
						  }
						  return true;
					  }
				  else 
				  {
					  return false;
				  }
					
				} catch (Exception e)
				{
					logger.error("Exection", e);
					return false;
				}
			}  
			}else {

				 return false;
			}
		} catch (Exception e) {
			logger.error("Exection", e);
			throw new ServiceException(e.getMessage());
		}
		 return true;
	}

	@Override
	public LabTemplateMasterDto findLabTemplateById(Integer labTemplateId) 
	{
		LabTemplateMaster labTemplateMaster = (LabTemplateMaster) getSessionFactory().getCurrentSession().get(LabTemplateMaster.class, labTemplateId);
		if(labTemplateId!=null)
		{
			LabTemplateMasterDto labTemplateMasterDto =mapper.map(labTemplateMaster, LabTemplateMasterDto.class,
					"LabTemplateMasterToLabTemplateMaster");
			
			
			  List<SpecialtyTemplateMapperDto> listSampleMasterDto =
			          sessionFactory.getCurrentSession().getNamedQuery("GET_SPECIALTY_TEMPLATE_LIST")
			              .setParameter("labTemplateId", labTemplateId)
			              .setResultTransformer(Transformers.aliasToBean(SpecialtyTemplateMapperDto.class)).list();

			  labTemplateMasterDto.setListSpecialtyTemplateMapperDto(listSampleMasterDto);
			
			return labTemplateMasterDto;
		}
		     
		else {
			return null;
		}
		
	} 
	
	@Override
	public Response activateInactivateLabTemplateMaster(Integer orgId, Integer labTemplateId, Character sampleStatus)
			throws ApplicationException
	{
		try
		{
			LabTemplateMaster labTemplateMaster = (LabTemplateMaster) sessionFactory.getCurrentSession().get(LabTemplateMaster.class, labTemplateId);
			if (labTemplateMaster.getLabTemplateId() != 0)
			{
			
				 labTemplateMaster.setStatus(sampleStatus);
				 sessionFactory.getCurrentSession().update(labTemplateMaster);
				 
					Integer labTemplateMstUpdate = (Integer) sessionFactory.getCurrentSession()
							.getNamedQuery("UPDATE_STATUS_SPECL_TEMPLATE")
							.setParameter("labTemplateId", labTemplateId)
							.setParameter("status", sampleStatus)
							.executeUpdate();
					
				 
				 if(labTemplateMstUpdate>0) {
					 if (sampleStatus == ACTIVE)
						{
							return new Response(SUCCESS, SUCCESS_200, LAB_TEMP_ACTIVATE_SUCC, null, null);
						} else if (sampleStatus == INACTIVE)
						{
							return new Response(SUCCESS, SUCCESS_200, LAB_TEMP_INACTIVATE_SUCC, null, null);
						} else
						{
							if (sampleStatus == ACTIVE)
							{
								return new Response(SUCCESS, SUCCESS_200, LAB_TEMP_INACTIVATE_SUCC, null, null);
							} else
							{
								return new Response(SUCCESS, SUCCESS_200, LAB_TEMP_INACTIVATE_SUCC, null, null);
							}
						}
					} else
					{
						return new Response(SUCCESS, SUCCESS_200, LAB_TEMP_INACTIVATE_FAIL, null, null);
					}
				 }else {
					 return new Response(SUCCESS, SUCCESS_200, LAB_TEMP_INACTIVATE_FAIL, null, null);
				 }
				

		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}
	
	
	

	
	

	@Override
	public Response getToTalLabTemplateMasterRecord(Integer orgId,Integer orgUnitId) throws ApplicationException {
		try
		{
			BigInteger templateMasterRecordCount = (BigInteger) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_TOTAL_LAB_TEMPLATE_RECORD").setParameter("orgId", orgId).uniqueResult();
			if (templateMasterRecordCount.compareTo(BigInteger.ZERO) == 1)
			{
				return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, templateMasterRecordCount);
			} else
			{
				return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, BigInteger.ZERO);
			}

		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	}
  @Override
  public Response getLabTemplates(Integer orgId, Integer orgUnitId,Integer labSampleDtlsId) throws ApplicationException {
    try 
    {
      List<TemplateMasterDto> listSampleMasterDto =
          sessionFactory.getCurrentSession().getNamedQuery("GET_LAB_TEMPLATES")
              .setParameter("orgId", orgId)
              .setParameter("unitId", orgUnitId)
              .setParameter("labSampleDtlsId", labSampleDtlsId)
              .setResultTransformer(Transformers.aliasToBean(TemplateMasterDto.class)).list();

      if (listSampleMasterDto.isEmpty()) 
      {
        return new Response(ERROR, ERR_500, SAMPLE_RECORDS_NOT_FOUND, null, null);
      } else {
        return new Response(SUCCESS, SUCCESS_200, null, listSampleMasterDto, null);
      }

    } catch (Exception e) {
      logger.error("Exection", e);
      throw e;
    }
  }


  @Override
  public Response getLabTemplatesByDoctor(Integer orgId,Integer orgUnitId, Integer doctorId, Integer templateTypeId)
      throws ApplicationException {
    try 
    { 
      
      List<TemplateDto> listTemplateDto = null;
      String GET_TEMPLATES_BY_TEMPLATE_TYPE = sessionFactory.getCurrentSession().getNamedQuery("GET_TEMPLATES_BY_TEMPLATE_TYPE").getQueryString();

       if(doctorId.intValue()==0)
       {
         
         listTemplateDto =   sessionFactory.getCurrentSession().createQuery(GET_TEMPLATES_BY_TEMPLATE_TYPE)
                 .setParameter("orgId", orgId)
                 .setParameter("orgUnitId", orgUnitId)
                 .setParameter("templateTypeId", templateTypeId)
                 .setResultTransformer(Transformers.aliasToBean(TemplateDto.class)).list();
       }
       else 
       {
         StringBuilder GET_TEMPLATES_BY_TEMPLATE_BY_DOCT  = new StringBuilder(GET_TEMPLATES_BY_TEMPLATE_TYPE).append(" AND lstSpecialtyTemplateMapper.doctorId = :doctorId");
       listTemplateDto =
             sessionFactory.getCurrentSession().createQuery(GET_TEMPLATES_BY_TEMPLATE_BY_DOCT.toString())
                 .setParameter("orgId", orgId)
                 .setParameter("orgUnitId", orgUnitId)
                 .setParameter("doctorId", doctorId)
                 .setParameter("templateTypeId", templateTypeId)
                 .setResultTransformer(Transformers.aliasToBean(TemplateDto.class)).list();
       }
   
      if (listTemplateDto.isEmpty()) 
      {
        return new Response(ERROR, ERR_500, SAMPLE_RECORDS_NOT_FOUND, null, null);
      } else {
        return new Response(SUCCESS, SUCCESS_200, null, listTemplateDto, null);
      }
    } catch (Exception e) {
      logger.error("Exection", e);
      throw e;
    }
  }

@Override
public Response isTemplateExistOrNot(Integer orgId, Integer orgUnitId, Integer templateTypeId)
		throws ApplicationException {
	try
	{
		Long templateTypeExistCount = (Long) sessionFactory.getCurrentSession()
				.getNamedQuery("GET_TEMPLATE_TYPE_EXIST_OR_NOT")
				.setParameter("orgId", orgId)
				.setParameter("orgUnitId", orgUnitId)
				.setParameter("templateTypeId", templateTypeId)
				.uniqueResult();
		if (templateTypeExistCount >0)
		{
			return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, null, templateTypeExistCount);
		} else
		{
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, BigInteger.ZERO);
		}

	} catch (Exception e)
	{
		//logger.error("Exection", e);
		throw e;
	}
}

@Override
public Response<LabTemplateMaster, Integer> labTemplatesbydoctorForDischargeScreen(Integer orgId, Integer orgUnitId,
		Integer doctorId, Integer templateTypeId) throws ApplicationException {
    try 
    { 
      
      List<TemplateDto> listTemplateDto = null;
      String GET_TEMPLATES_BY_TEMPLATE_TYPE_FOR_DISCHARGE_SCREEN = sessionFactory.getCurrentSession().getNamedQuery("GET_TEMPLATES_BY_TEMPLATE_TYPE_FOR_DISCHARGE_SCREEN").getQueryString();

       if(doctorId.intValue()==0)
       {
         
         listTemplateDto =   sessionFactory.getCurrentSession().createSQLQuery(GET_TEMPLATES_BY_TEMPLATE_TYPE_FOR_DISCHARGE_SCREEN)
                 .setParameter("orgId", orgId)
                 .setParameter("orgUnitId", orgUnitId)
                 .setParameter("templateTypeId", templateTypeId)
                 .setResultTransformer(Transformers.aliasToBean(TemplateDto.class)).list();
       }
       else 
       {
         StringBuilder GET_TEMPLATES_BY_TEMPLATE_TYPE_FOR_DISCHARGE_SCREEN_DOCT  = new StringBuilder(GET_TEMPLATES_BY_TEMPLATE_TYPE_FOR_DISCHARGE_SCREEN).append(" AND lstSpecialtyTemplateMapper.doctorId = :doctorId");
       listTemplateDto =
             sessionFactory.getCurrentSession().createSQLQuery(GET_TEMPLATES_BY_TEMPLATE_TYPE_FOR_DISCHARGE_SCREEN_DOCT.toString())
                 .setParameter("orgId", orgId)
                 .setParameter("orgUnitId", orgUnitId)
                 .setParameter("doctorId", doctorId)
                 .setParameter("templateTypeId", templateTypeId)
                 .setResultTransformer(Transformers.aliasToBean(TemplateDto.class)).list();
       }
   
      if (listTemplateDto.isEmpty()) 
      {
        return new Response(ERROR, ERR_500, SAMPLE_RECORDS_NOT_FOUND, null, null);
      } else {
        return new Response(SUCCESS, SUCCESS_200, null, listTemplateDto, null);
      }
    } catch (Exception e) {
      logger.error("Exection", e);
      throw e;
    }
  }

}
