package com.param.global.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.AllergyMasterDto;
import com.param.global.dto.FormulationTypeToDosageMapperDto;
import com.param.global.model.AllergyMaster;
import com.param.global.model.FormulationTypeToDosageMapper;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class FormulationTypeToDosageMapperDaoImpl extends GenericDao<FormulationTypeToDosageMapper, Integer>
implements IFormulationTypeToDosageMapperDao, ICommonConstants {

private static final Logger logger = Logger.getLogger(FormulationTypeToDosageMapperDaoImpl.class);

public FormulationTypeToDosageMapperDaoImpl() {
super(FormulationTypeToDosageMapper.class);
}

@Override
public Response getFormulationTypeToDosageMapperByName(
		FormulationTypeToDosageMapperDto formulationTypeToDosageMapperDto) throws ApplicationException {
	try {
		List<FormulationTypeToDosageMapperDto> list = sessionFactory.getCurrentSession()
				.createSQLQuery("select fdm.formulation_dosage_mapp_id as \"formulationDosageMappId\" from public.m_fomulation_dosage_mapper fdm"
						+ " where fdm.organization_id=:orgId "
						+ "and lower(fdm.dosage_form)=:dosageForm"
						+ " and fdm.formulation_type_id=:typeId ")
				.setParameter("orgId", formulationTypeToDosageMapperDto.getOrganizationId())
				.setParameter("typeId", formulationTypeToDosageMapperDto.getFormulationTypeId())
				.setParameter("dosageForm", formulationTypeToDosageMapperDto.getDosageForm().toLowerCase())
				.setResultTransformer(Transformers.aliasToBean(FormulationTypeToDosageMapperDto.class))
				.list();
		return new Response<>(SUCCESS, null, null, list, null);
	} catch (HibernateException he) {
		logger.error("getFormulationTypeToDosageMapperByName failed", he);
	} catch (Exception e) {
		logger.error("getFormulationTypeToDosageMapperByName failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

@Override
public Response saveFormulationTypeToDosageMapper(FormulationTypeToDosageMapperDto formulationTypeToDosageMapperDto)
		throws ApplicationException {
	try {
		FormulationTypeToDosageMapper mapper = new FormulationTypeToDosageMapper();
		mapper.setDosageForm(formulationTypeToDosageMapperDto.getDosageForm());
		mapper.setFactorId(formulationTypeToDosageMapperDto.getFactorId());
		mapper.setFormulationTypeId(formulationTypeToDosageMapperDto.getFormulationTypeId());
	    if(formulationTypeToDosageMapperDto.getFactorId()==1) {
			mapper.setFromConversionFactor(formulationTypeToDosageMapperDto.getFromConversionFactor());
			mapper.setFromConversionFactorUnit(formulationTypeToDosageMapperDto.getFromConversionFactorUnit());
			mapper.setToConversionFactor(formulationTypeToDosageMapperDto.getToConversionFactor());
			mapper.setToConversionFactorUnit(formulationTypeToDosageMapperDto.getToConversionFactorUnit());
	    }
		mapper.setStatus(formulationTypeToDosageMapperDto.getStatus());
		mapper.setUnitId(formulationTypeToDosageMapperDto.getUnitId());
		mapper.setOrganizationId(formulationTypeToDosageMapperDto.getOrganizationId());
		mapper.setCreatedBy(formulationTypeToDosageMapperDto.getCreatedBy());
		mapper.setCreatedDate(
				GlobalCommonDateUtils.getDate(formulationTypeToDosageMapperDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
		mapper.setUpdatedBy(formulationTypeToDosageMapperDto.getUpdatedBy());
		mapper.setUpdatedDate(
				GlobalCommonDateUtils.getDate(formulationTypeToDosageMapperDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
		save(mapper);
		return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
	} catch (HibernateException he) {
		logger.error("saveFormulationTypeToDosageMapper failed", he);
	} catch (Exception e) {
		logger.error("saveFormulationTypeToDosageMapper failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

@Override
public Response getFormulationTypeToDosageMapperList(FormulationTypeToDosageMapperDto formulationTypeToDosageMapperDto)
		throws ApplicationException {
	try {
		
		StringBuilder query=new StringBuilder("select fdm.formulation_dosage_mapp_id as \"formulationDosageMappId\",fdm.formulation_type_id as \"formulationTypeId\",\r\n" 
				+"fdm.dosage_form as \"dosageForm\",fdm.from_conversion_factor as \"fromConversionFactor\",fdm.to_conversion_factor as \"toConversionFactor\",\r\n"  
				+"fdm.factor_id as \"factorId\",fdm.from_conversion_factor_unit_id as \"fromConversionFactorUnit\",\r\n" 
				+"fdm.to_conversion_factor_unit_id as \"toConversionFactorUnit\",suf.unit as \"fromConversionUnit\",sut.unit as \"toConversionUnit\",\r\n"  
				+"fdm.status as \"status\",ft.type as \"formulationType\"\r\n" 
				+"from public.m_fomulation_dosage_mapper fdm\r\n" 
				+"inner join public.m_formulation_type ft on ft.id=fdm.formulation_type_id\r\n" 
				+"left join public.m_strength_unit suf on suf.id=fdm.from_conversion_factor_unit_id\r\n"  
				+"left join public.m_strength_unit sut on sut.id=fdm.to_conversion_factor_unit_id\r\n"  
				+"where  fdm.organization_id=:orgId"
//				+ "and fdm.unit_id=:unitId "
				);
		
		if(formulationTypeToDosageMapperDto.getSearchKeyword()!=null) {
			query.append(" and (lower(ft.type) like :searchKeyword "
					+ " or lower(fdm.dosage_form)  like :searchKeyword ) ");
		}
		Query query2 = sessionFactory.getCurrentSession().createSQLQuery(query.toString());
		if(formulationTypeToDosageMapperDto.getSearchKeyword()!=null) {
			query2.setParameter("searchKeyword",'%'+formulationTypeToDosageMapperDto.getSearchKeyword().toLowerCase()+'%');
		}
		List<FormulationTypeToDosageMapperDto> list = query2
				.setParameter("orgId", formulationTypeToDosageMapperDto.getOrganizationId())
				.setFirstResult(formulationTypeToDosageMapperDto.getOffset() != null ? formulationTypeToDosageMapperDto.getOffset() : 0)
				.setMaxResults(formulationTypeToDosageMapperDto.getNoOfRecordsPerPage() != null ? formulationTypeToDosageMapperDto.getNoOfRecordsPerPage() : 10)
				.setResultTransformer(Transformers.aliasToBean(FormulationTypeToDosageMapperDto.class)).list();
		return new Response<>(SUCCESS, null, null, list, null);
	} catch (HibernateException he) {
		logger.error("getFormulationTypeToDosageMapperList failed", he);
	} catch (Exception e) {
		logger.error("getFormulationTypeToDosageMapperList failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

@Override
public Response updateFormulationTypeToDosageMapper(FormulationTypeToDosageMapperDto formulationTypeToDosageMapperDto)
		throws ApplicationException {
	try {
		FormulationTypeToDosageMapper mapper = findById(formulationTypeToDosageMapperDto.getFormulationDosageMappId());
		mapper.setDosageForm(formulationTypeToDosageMapperDto.getDosageForm());
		mapper.setFactorId(formulationTypeToDosageMapperDto.getFactorId());
		mapper.setFormulationTypeId(formulationTypeToDosageMapperDto.getFormulationTypeId());
		mapper.setFromConversionFactor(formulationTypeToDosageMapperDto.getFromConversionFactor());
		mapper.setFromConversionFactorUnit(formulationTypeToDosageMapperDto.getFromConversionFactorUnit());
		mapper.setToConversionFactor(formulationTypeToDosageMapperDto.getToConversionFactor());
		mapper.setToConversionFactorUnit(formulationTypeToDosageMapperDto.getToConversionFactorUnit());
		mapper.setStatus(formulationTypeToDosageMapperDto.getStatus());
		mapper.setUnitId(formulationTypeToDosageMapperDto.getUnitId());
		mapper.setOrganizationId(formulationTypeToDosageMapperDto.getOrganizationId());
		mapper.setUpdatedBy(formulationTypeToDosageMapperDto.getUpdatedBy());
		mapper.setUpdatedDate(
				GlobalCommonDateUtils.getDate(formulationTypeToDosageMapperDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
		update(mapper);
		return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
	} catch (HibernateException he) {
		logger.error("updateFormulationTypeToDosageMapper failed", he);
	} catch (Exception e) {
		logger.error("updateFormulationTypeToDosageMapper failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

@Override
public Response getFormulationTypeToDosageMapperListByNameNotById(
		FormulationTypeToDosageMapperDto formulationTypeToDosageMapperDto) throws ApplicationException {
	try {
		List<FormulationTypeToDosageMapper> list = sessionFactory.getCurrentSession()
				.createQuery("From FormulationTypeToDosageMapper mapper where mapper.organizationId=:orgId"
						+ " and lower(mapper.dosageForm)=:dosageForm "
						+ " and mapper.formulationTypeId=:formTypeId and mapper.formulationDosageMappId!=:id ")
				.setParameter("orgId", formulationTypeToDosageMapperDto.getOrganizationId())
				.setParameter("dosageForm", formulationTypeToDosageMapperDto.getDosageForm().toLowerCase())
				.setParameter("formTypeId", formulationTypeToDosageMapperDto.getFormulationTypeId())
				.setParameter("id", formulationTypeToDosageMapperDto.getFormulationDosageMappId())
				.list();
		return new Response<>(SUCCESS, null, null, list, null);
	} catch (HibernateException he) {
		logger.error("getFormulationTypeToDosageMapperListByNameNotById failed", he);
	} catch (Exception e) {
		logger.error("getFormulationTypeToDosageMapperListByNameNotById failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

@Override
public Response updateFormulationTypeToDosageMapperStatus(
		FormulationTypeToDosageMapperDto formulationTypeToDosageMapperDto) throws ApplicationException {
	try {
		FormulationTypeToDosageMapper mapper = findById(formulationTypeToDosageMapperDto.getFormulationDosageMappId());
		mapper.setStatus(formulationTypeToDosageMapperDto.getStatus());
		mapper.setUpdatedBy(formulationTypeToDosageMapperDto.getUpdatedBy());
		mapper.setUpdatedDate(
				GlobalCommonDateUtils.getDate(formulationTypeToDosageMapperDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
		update(mapper);
		return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
	} catch (HibernateException he) {
		logger.error("updateFormulationTypeToDosageMapperStatus failed", he);
	} catch (Exception e) {
		logger.error("updateFormulationTypeToDosageMapperStatus failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

@Override
public Response getFormTypeToDosMappListByFormTypeId(
		FormulationTypeToDosageMapperDto formulationTypeToDosageMapperDto) {
try {
		
		List<FormulationTypeToDosageMapperDto> list = sessionFactory.getCurrentSession().createSQLQuery("select fdm.formulation_dosage_mapp_id as \"formulationDosageMappId\",fdm.formulation_type_id as \"formulationTypeId\",\r\n" 
				+"fdm.dosage_form as \"dosageForm\",fdm.from_conversion_factor as \"fromConversionFactor\",fdm.to_conversion_factor as \"toConversionFactor\",\r\n"  
				+"fdm.factor_id as \"factorId\",fdm.from_conversion_factor_unit_id as \"fromConversionFactorUnit\",\r\n" 
				+"fdm.to_conversion_factor_unit_id as \"toConversionFactorUnit\",suf.unit as \"fromConversionUnit\",sut.unit as \"toConversionUnit\",\r\n"  
				+"fdm.status as \"status\",ft.type as \"formulationType\"\r\n" 
				+"from public.m_fomulation_dosage_mapper fdm\r\n" 
				+"inner join public.m_formulation_type ft on ft.id=fdm.formulation_type_id\r\n" 
				+"left join public.m_strength_unit suf on suf.id=fdm.from_conversion_factor_unit_id\r\n"  
				+"left join public.m_strength_unit sut on sut.id=fdm.to_conversion_factor_unit_id\r\n"  
				+"where fdm.organization_id=:orgId and fdm.formulation_type_id=:formTypeId and fdm.status='A'"
				+ "ORDER BY fdm.dosage_form ASC")
				.setParameter("orgId", formulationTypeToDosageMapperDto.getOrganizationId())
				.setParameter("formTypeId", formulationTypeToDosageMapperDto.getFormulationTypeId())
				.setResultTransformer(Transformers.aliasToBean(FormulationTypeToDosageMapperDto.class)).list();

		return new Response<>(SUCCESS, null, null, list, null);
	} catch (HibernateException he) {
		logger.error("getFormTypeToDosMappListByFormTypeId failed", he);
	} catch (Exception e) {
		logger.error("getFormTypeToDosMappListByFormTypeId failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

}
