package com.param.lis.global.dao;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.entity.lis.global.TemplateServiceMapper;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.lis.global.dto.TemplateServiceMapperDto;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings({"rawtypes", "unchecked"})
@Repository
public class TemplateServiceMapperDaoImpl extends GenericDao<TemplateServiceMapper, Integer> implements ITemplateServiceMapperDao, ICommonConstants{

	public TemplateServiceMapperDaoImpl() {
		super(TemplateServiceMapper.class);
	}
	
	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	public Response saveTemplateServiceMapper(TemplateServiceMapperDto templateServiceMapperDto) throws ApplicationException {
		try{
			TemplateServiceMapper templateServiceMapperr=new TemplateServiceMapper();
			templateServiceMapperr.setTemplateId(templateServiceMapperDto.getTemplateId());
			templateServiceMapperr.setServiceId(templateServiceMapperDto.getServiceId());
			templateServiceMapperr.setSubSpecialityId(templateServiceMapperDto.getSubSpecialityId());
			templateServiceMapperr.setSpecialityId(templateServiceMapperDto.getSpecialityId());
			templateServiceMapperr.setOrganizationId(templateServiceMapperDto.getOrganizationId());
			templateServiceMapperr.setUnitId(templateServiceMapperDto.getUnitId());
			templateServiceMapperr.setCreatedBy(templateServiceMapperDto.getCreatedBy());
			templateServiceMapperr.setStatus(templateServiceMapperDto.getStatus());
			templateServiceMapperr.setIsDeleted('N');
			templateServiceMapperr.setCreatedDate(GlobalCommonDateUtils.getDate(templateServiceMapperDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			templateServiceMapperr.setUpdatedBy(templateServiceMapperDto.getUpdatedBy());
			templateServiceMapperr.setUpdatedDate(GlobalCommonDateUtils.getDate(templateServiceMapperDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			
			save(templateServiceMapperr);
			
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			throw e;
		}
	}


	@Override
	public Response updateTemplateServiceMapper(List<TemplateServiceMapperDto> listTemplateServiceMapperDto) throws ApplicationException {
		try{
			
			
			Integer result = sessionFactory.getCurrentSession()
					.getNamedQuery("UPDATE_TEMPLATE_SERVICE_MAPPER")
					.setParameter("isDeleted", 'Y')
					.setParameter("organizationId", listTemplateServiceMapperDto.get(0).getOrganizationId())
					.setParameter("unitId", listTemplateServiceMapperDto.get(0).getUnitId())
					.setParameter("serviceId", listTemplateServiceMapperDto.get(0).getServiceId())
					.executeUpdate();
			if (result > 0) {
				for (Iterator iterator = listTemplateServiceMapperDto.iterator(); iterator.hasNext();) {
					TemplateServiceMapperDto listTemplateServiceMapper = (TemplateServiceMapperDto) iterator
							.next();
					TemplateServiceMapper templateServiceMapperr=new TemplateServiceMapper();
					templateServiceMapperr.setServiceId(listTemplateServiceMapper.getServiceId());
					templateServiceMapperr.setTemplateId(listTemplateServiceMapper.getTemplateId());
					templateServiceMapperr.setSubSpecialityId(listTemplateServiceMapper.getSubSpecialityId());
					templateServiceMapperr.setSpecialityId(listTemplateServiceMapper.getSpecialityId());
					templateServiceMapperr.setOrganizationId(listTemplateServiceMapper.getOrganizationId());
					templateServiceMapperr.setUnitId(listTemplateServiceMapper.getUnitId());
					templateServiceMapperr.setIsDeleted('N');
					templateServiceMapperr.setCreatedBy(listTemplateServiceMapper.getCreatedBy());
					templateServiceMapperr.setStatus(listTemplateServiceMapper.getStatus());
					templateServiceMapperr.setUpdatedBy(listTemplateServiceMapper.getUpdatedBy());
					templateServiceMapperr.setCreatedDate(GlobalCommonDateUtils.getDate(listTemplateServiceMapper.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
					templateServiceMapperr.setUpdatedDate(GlobalCommonDateUtils.getDate(listTemplateServiceMapper.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));

					
					save(templateServiceMapperr);
				}
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
				
			} 
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			throw e;
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateTemplateServiceMapperStatus(Integer serviceId, Integer organizationId, Integer unitId,
			Character status) throws ApplicationException {
		try{
			if (serviceId != 0) {

				Integer result = sessionFactory.getCurrentSession()
						.getNamedQuery("UPDATE_TEMPLATE_SERVICE_MAPPER_STATUS")
						.setParameter("organizationId", organizationId)
						.setParameter("unitId", unitId)
						.setParameter("status", status)
						.setParameter("serviceId", serviceId)
						.executeUpdate();
				if (result > 0) {
					if (status.equals(ACTIVE)) {
						return new Response(SUCCESS,SUCCESS_CODE, COMMON_UPDATE, null, null);
					} else if (status.equals(INACTIVE)) {
						return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
					}
				} else {
					return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
				}

			} else {
				return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}
			
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			throw e;
		}
	}


	@Override
	public Response getTemplateServiceMapperlist(TemplateServiceMapperDto templateServiceMapperDto) throws ApplicationException {
		try{
			List<TemplateServiceMapperDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_TEMPLATE_SERVICE_MAPPER_LIST")
					.setParameter("organizationId", templateServiceMapperDto.getOrganizationId())
					.setParameter("unitId", templateServiceMapperDto.getUnitId())
					.setFirstResult(templateServiceMapperDto.getOffset() != null ? templateServiceMapperDto.getOffset() : 0)
					.setMaxResults(templateServiceMapperDto.getNoOfRecordsPerPage() != null ? templateServiceMapperDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(TemplateServiceMapperDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			throw e;
		}
	}


	@Override
	public Response getTemplateServiceMapperCount(Integer organizationId, Integer unitId) throws ApplicationException {
		try{
			BigInteger templateServiceMapperrCount = (BigInteger) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_TEMPLATE_SERVICE_MAPPER_COUNT")
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.uniqueResult();
				if (templateServiceMapperrCount.compareTo(BigInteger.ZERO) == 1) {
					return new Response(SUCCESS, SUCCESS_CODE, null, null, templateServiceMapperrCount);
				} else {
					return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, BigInteger.ZERO);
				}
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			throw e;
		}
	}
	@Override
	public Response getActiveTemplateServiceMapper(Integer organizationId, Integer unitId) throws ApplicationException {
		try{
			
		/*	List<TemplateServiceMapperDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_TEMPLATE_SERVICE_MAPPER_LIST")
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(TemplateServiceMapperDto.class)).list();*/
				return new Response(SUCCESS, SUCCESS_CODE, null, null, null);
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			throw e;
		}
	}

	
	@Override
	public Response getTemplateServiceMapperById(Integer serviceId, Integer organizationId, Integer unitId)
			throws ApplicationException {
		try{
			List<TemplateServiceMapperDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_TEMPLATE_SERVICE_MAPPER_BY_ID")
					.setParameter("serviceId", serviceId)
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(TemplateServiceMapperDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			throw e;
		}
	}
	@Override
	public Response checkTemplateServiceMapperAvailable(TemplateServiceMapperDto templateServiceMapperDto)
			throws ApplicationException {
		try{
			List<TemplateServiceMapperDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("CHECK_TEMPLATE_SERVICE_MAPPER_AVAILABLE")
					.setParameter("serviceId", templateServiceMapperDto.getServiceId())
					.setParameter("templateId", templateServiceMapperDto.getTemplateId())
					.setParameter("organizationId", templateServiceMapperDto.getOrganizationId())
					.setParameter("unitId", templateServiceMapperDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(TemplateServiceMapperDto.class)).list();
			
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			throw e;
		}
	}




	@Override
	public Response getDetailedServiceList(Integer serviceId, Integer organizationId, Integer unitId)
			throws ApplicationException {
		try{
			List<TemplateServiceMapperDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DETAILED_SERVICE_LIST")
					.setParameter("serviceId", serviceId)
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(TemplateServiceMapperDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			throw e;
		}
	}


}
