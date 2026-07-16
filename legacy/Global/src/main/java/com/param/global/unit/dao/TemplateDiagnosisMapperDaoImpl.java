package com.param.global.unit.dao;

import java.math.BigInteger;
import java.util.Iterator;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import in.param.exception.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.model.TemplateDiagnosisMapper;
import com.param.global.unit.dto.TemplateDiagnosisMapperDto;
import com.param.service.global.SessionService;

import in.param.common.dao.GenericDao;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class TemplateDiagnosisMapperDaoImpl extends GenericDao<TemplateDiagnosisMapper, Integer> implements ITemplateDiagnosisMapperDao, ICommonConstants{
	final static Logger logger = Logger.getLogger(TemplateDiagnosisMapperDaoImpl.class);


	public TemplateDiagnosisMapperDaoImpl() {
		super(TemplateDiagnosisMapper.class);
	}

	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	SessionService sessionService;
	
	@Override
	public Response saveTemplateDiagnosisMapper(TemplateDiagnosisMapperDto templateDiagnosisMapperDto)
			throws ApplicationException {
		try{
			TemplateDiagnosisMapper templateDiagnosisMapper=new TemplateDiagnosisMapper();
			templateDiagnosisMapper.setDiagnosisId(templateDiagnosisMapperDto.getDiagnosisId());
			templateDiagnosisMapper.setTemplateId(templateDiagnosisMapperDto.getTemplateId());
			templateDiagnosisMapper.setOrganisationId(templateDiagnosisMapperDto.getOrganisationId());
			templateDiagnosisMapper.setUnitId(templateDiagnosisMapperDto.getUnitId());
			templateDiagnosisMapper.setCreatedBy(templateDiagnosisMapperDto.getCreatedBy());
			templateDiagnosisMapper.setCreatedDate(GlobalCommonDateUtils.getDate(templateDiagnosisMapperDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			templateDiagnosisMapper.setUpdatedBy(templateDiagnosisMapperDto.getUpdatedBy());
			templateDiagnosisMapper.setUpdatedDate(GlobalCommonDateUtils.getDate(templateDiagnosisMapperDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			templateDiagnosisMapper.setStatus(templateDiagnosisMapperDto.getStatus());
			templateDiagnosisMapper.setIsDeleted('N');
			
			save(templateDiagnosisMapper);
			
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (HibernateException he) {
		logger.error("saveTemplateDiagnosisMapper failed", he);
		} catch (Exception e) {
		logger.error("saveTemplateDiagnosisMapper failed", e);
		}
	return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateTemplateDiagnosisMapper(List<TemplateDiagnosisMapperDto> listTemplateDiagnosisMapperDto)
			throws ApplicationException {
	try{			
		Integer result = sessionFactory.getCurrentSession()
				.getNamedQuery("UPDATE_TEMPLATE_DIGNOSIS_MAPPER")
				.setParameter("isDeleted", 'Y')
				.setParameter("organisationId", listTemplateDiagnosisMapperDto.getFirst().getOrganisationId())
				.setParameter("unitId", listTemplateDiagnosisMapperDto.getFirst().getUnitId())
				.setParameter("templateId", listTemplateDiagnosisMapperDto.getFirst().getPrevTemplateId())
				.executeUpdate();
		if (result > 0) {
			for (Iterator iterator = listTemplateDiagnosisMapperDto.iterator(); iterator.hasNext();) {
				TemplateDiagnosisMapperDto listTemplateDiagnosisMapper = (TemplateDiagnosisMapperDto) iterator
						.next();
//				TemplateDiagnosisMapper templateDiagnosisMapper = mapper.map(listTemplateDiagnosisMapper,TemplateDiagnosisMapper.class, "TemplateDiagnosisMapperDtoTOTemplateDiagnosisMapper");
				TemplateDiagnosisMapper templateDiagnosisMapper=new TemplateDiagnosisMapper();
				templateDiagnosisMapper.setDiagnosisId(listTemplateDiagnosisMapper.getDiagnosisId());
				templateDiagnosisMapper.setTemplateId(listTemplateDiagnosisMapper.getTemplateId());
				templateDiagnosisMapper.setOrganisationId(listTemplateDiagnosisMapper.getOrganisationId());
				templateDiagnosisMapper.setUnitId(listTemplateDiagnosisMapper.getUnitId());
				templateDiagnosisMapper.setIsDeleted('N');
				templateDiagnosisMapper.setCreatedBy(listTemplateDiagnosisMapper.getCreatedBy());
				templateDiagnosisMapper.setCreatedDate(GlobalCommonDateUtils.getDate(listTemplateDiagnosisMapper.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
				templateDiagnosisMapper.setStatus(listTemplateDiagnosisMapper.getStatus());
				templateDiagnosisMapper.setUpdatedBy(listTemplateDiagnosisMapper.getUpdatedBy());
				templateDiagnosisMapper.setUpdatedDate(GlobalCommonDateUtils.getDate(listTemplateDiagnosisMapper.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
				save(templateDiagnosisMapper);
			}
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
				
			} 
			}catch (HibernateException he) {
			logger.error("updateTemplateDiagnosisMapper failed", he);
			} catch (Exception e) {
			logger.error("updateTemplateDiagnosisMapper failed", e);
			}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateTemplateDiagnosisMapperStatus(Integer templateId, Integer organisationId, Integer unitId, Character status)
			throws ApplicationException {
			try{
				if (templateId != 0) {

					Integer result = sessionFactory.getCurrentSession()
							.getNamedQuery("UPDATE_TEMPLATE_DIGNOSIS_MAPPER_STATUS")
							.setParameter("organisationId", organisationId)
							.setParameter("unitId", unitId)
							.setParameter("status", status)
							.setParameter("templateId", templateId)
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
		} catch (HibernateException he) {
		logger.error("updateTemplateDiagnosisMapperStatus failed", he);
		} catch (Exception e) {
		logger.error("updateTemplateDiagnosisMapperStatus failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	
	@Override
	public Response getTemplateDiagnosisMapperlist(Integer offset, Integer noOfRecordsPerPage)
			throws ApplicationException {
		try{
			List<TemplateDiagnosisMapperDto> list=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_TEMPLATE_DIGNOSIS_MAPPER_LIST")
				.setFirstResult(offset != null ? offset : 0)
				.setMaxResults(noOfRecordsPerPage != null ? noOfRecordsPerPage : 10)
				.setParameter("unitId", sessionService.getUnitId())
				.setParameter("organisationId", sessionService.getOrganizationId())
				.setResultTransformer(Transformers.aliasToBean(TemplateDiagnosisMapperDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
	} catch (HibernateException he) {
	logger.error("getTemplateDiagnosisMapperlist failed", he);
	} catch (Exception e) {
	logger.error("getTemplateDiagnosisMapperlist failed", e);
	}
return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

	@Override
	public Response getTemplateDiagnosisMapperById(Integer templateId, Integer organisationId, Integer unitId)
			throws ApplicationException {
		try{
			List<TemplateDiagnosisMapperDto> list=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_ACTIVE_TEMPLATE_DIGNOSIS_MAPPER_BY_ID")
				.setParameter("templateId", templateId)
				.setParameter("unitId", unitId)
				.setParameter("organisationId", organisationId)
				.setResultTransformer(Transformers.aliasToBean(TemplateDiagnosisMapperDto.class)).list();
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
	} catch (HibernateException he) {
	logger.error("getTemplateDiagnosisMapperById failed", he);
	} catch (Exception e) {
	logger.error("getTemplateDiagnosisMapperById failed", e);
	}
return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

	@Override
	public Response getActiveTemplateDiagnosisMapper(TemplateDiagnosisMapperDto templateDiagnosisMapperDto)
			throws ApplicationException {
		try{
			List<TemplateDiagnosisMapperDto> list=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_ACTIVE_TEMPLATE_DIGNOSIS_MAPPER")
				.setParameter("unitId", templateDiagnosisMapperDto.getUnitId())
				.setParameter("organisationId", templateDiagnosisMapperDto.getOrganisationId())
				.setResultTransformer(Transformers.aliasToBean(TemplateDiagnosisMapperDto.class)).list();
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
	} catch (HibernateException he) {
	logger.error("getActiveTemplateDiagnosisMapper failed", he);
	} catch (Exception e) {
	logger.error("getActiveTemplateDiagnosisMapper failed", e);
	}
return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

	@Override
	public Response checkActiveTemplateDiagnosisMapperAvaiable(TemplateDiagnosisMapperDto templateDiagnosisMapperDto)
			throws ApplicationException {
		try{
			List<TemplateDiagnosisMapperDto> list=sessionFactory.getCurrentSession()
				.getNamedQuery("CHECK_ACTIVE_TEMPLATE_DIGNOSIS_MAPPER_AVAILABLE")
				.setParameter("templateId", templateDiagnosisMapperDto.getTemplateId())
				.setParameter("organisationId", templateDiagnosisMapperDto.getOrganisationId())
				.setParameter("unitId", templateDiagnosisMapperDto.getUnitId())
				.setResultTransformer(Transformers.aliasToBean(TemplateDiagnosisMapperDto.class)).list();
			
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
	} catch (HibernateException he) {
	logger.error("checkActiveTemplateDiagnosisMapperAvaiable failed", he);
	} catch (Exception e) {
	logger.error("checkActiveTemplateDiagnosisMapperAvaiable failed", e);
	}
	return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getTemplateDiagnosisMapperDatailedList(Integer templateId, Integer organisationId, Integer unitId)
			throws ApplicationException {
		try{
			List<TemplateDiagnosisMapperDto> list=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_TEMPLATE_DIGNOSIS_MAPPER_DETAILED_LIST")
				.setParameter("templateId", templateId)
				.setParameter("organisationId", organisationId)
				.setParameter("unitId", unitId)
				.setResultTransformer(Transformers.aliasToBean(TemplateDiagnosisMapperDto.class)).list();
			
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
	} catch (HibernateException he) {
	logger.error("getTemplateDiagnosisMapperDatailedList failed", he);
	} catch (Exception e) {
	logger.error("getTemplateDiagnosisMapperDatailedList failed", e);
	}
	return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getTemplateDiagnosisMapperCount()
			throws ApplicationException {
		try{
			BigInteger templateDiagnosisMapperCount = (BigInteger) sessionFactory.getCurrentSession()
				.getNamedQuery("GET_TEMPLATE_DIGNOSIS_MAPPER_COUNT")
				.uniqueResult();
			if (templateDiagnosisMapperCount.compareTo(BigInteger.ZERO) == 1) {
				return new Response(SUCCESS, SUCCESS_CODE, null, null, templateDiagnosisMapperCount);
			} else {
				return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, BigInteger.ZERO);
			}
	} catch (HibernateException he) {
	logger.error("getTemplateDiagnosisMapperCount failed", he);
	} catch (Exception e) {
	logger.error("getTemplateDiagnosisMapperCount failed", e);
	}
	return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getTemplateList(Integer organisationId, Integer unitId)
			throws ApplicationException {
		try{
			List<TemplateDiagnosisMapperDto> list=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_LIST_OF_TEMPLATES")
				.setParameter("organisationId", organisationId)
				.setParameter("unitId", unitId)
				.setResultTransformer(Transformers.aliasToBean(TemplateDiagnosisMapperDto.class)).list();
			
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
	} catch (HibernateException he) {
	logger.error("getTemplateList failed", he);
	} catch (Exception e) {
	logger.error("getTemplateList failed", e);
	}
	return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getListOfDiagnosis(Integer organisationId, Integer unitId)
			throws ApplicationException {
		try{
			List<TemplateDiagnosisMapperDto> list=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_LIST_OF_DIAGNOSIS")
				.setParameter("organisationId", organisationId)
				.setParameter("unitId", unitId)
				.setResultTransformer(Transformers.aliasToBean(TemplateDiagnosisMapperDto.class)).list();
			
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
	} catch (HibernateException he) {
	logger.error("getListOfDiagnosis failed", he);
	} catch (Exception e) {
	logger.error("getListOfDiagnosis failed", e);
	}
	return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}
