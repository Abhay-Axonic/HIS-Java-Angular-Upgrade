package com.param.global.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.VitalQualifireMapperDto;
import com.param.global.dto.VitalQualifireMasterDto;
import com.param.opd.coversheet.model.VitalQualifireMapper;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({"rawtypes", "unchecked"})
@Repository
public class VitalQualifireMapperDaoImpl extends GenericDao<VitalQualifireMapper, Integer> implements IVitalQualifireMapperDao,ICommonConstants
{
	final static Logger logger = Logger.getLogger(VitalQualifireMapperDaoImpl.class);


	public VitalQualifireMapperDaoImpl() {
		super(VitalQualifireMapper.class);
	}

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response saveVitalQualifireMapper(VitalQualifireMapperDto vitalQualifireMapperDto) throws ApplicationException{
		try {
			for(int i=0;i<vitalQualifireMapperDto.getListOfVitalQualifireMapperDtoS().size();i++)
			{
			VitalQualifireMapper vitalQualifireMapper=new VitalQualifireMapper();
			vitalQualifireMapper.setVitalId(vitalQualifireMapperDto.getVitalId());
			vitalQualifireMapper.setVitalQualifireId(vitalQualifireMapperDto.getListOfVitalQualifireMapperDtoS().get(i).getVitalQualifireId());
			vitalQualifireMapper.setVitalCategoryId(vitalQualifireMapperDto.getListOfVitalQualifireMapperDtoS().get(i).getVitalCategoryId());
			vitalQualifireMapper.setOrganizationId(vitalQualifireMapperDto.getOrganizationId());
			vitalQualifireMapper.setUnitId(vitalQualifireMapperDto.getUnitId());
			vitalQualifireMapper.setIsDeleted('N');
			vitalQualifireMapper.setStatus(vitalQualifireMapperDto.getStatus());
			vitalQualifireMapper.setCreatedBy(vitalQualifireMapperDto.getCreatedBy());
			vitalQualifireMapper.setCreatedDate(TimeZoneComponent.getDateByzone(vitalQualifireMapperDto.getUnitId()));
			vitalQualifireMapper.setUpdatedBy(vitalQualifireMapperDto.getUpdatedBy());
			vitalQualifireMapper.setUpdatedDate(TimeZoneComponent.getDateByzone(vitalQualifireMapperDto.getUnitId()));
			save(vitalQualifireMapper);
		}
		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		
		} catch (HibernateException he) {
		logger.error("saveVitalQualifireMapper failed", he);
		} catch (Exception e) {
		logger.error("saveVitalQualifireMapper failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@Override
	public Response updateVitalQualifireMapper(VitalQualifireMapperDto vitalQualifireMapperDto) throws ApplicationException{
		try {
			Integer result = sessionFactory.getCurrentSession()
			.getNamedQuery("UPDATE_VITAL_QUALIFIRE_MAPPER")
			.setParameter("isDeleted", 'Y')
			.setParameter("organizationId", vitalQualifireMapperDto.getOrganizationId())
			.setParameter("vitalId", vitalQualifireMapperDto.getVitalId())
			.executeUpdate();
			if (result > 0) {
				for(int i=0;i<vitalQualifireMapperDto.getListOfVitalQualifireMapperDtoS().size();i++)
				{
					VitalQualifireMapper vitalQualifireMapper=new VitalQualifireMapper();
					vitalQualifireMapper.setVitalId(vitalQualifireMapperDto.getVitalId());
					vitalQualifireMapper.setIsDeleted('N');
					vitalQualifireMapper.setVitalQualifireId(vitalQualifireMapperDto.getListOfVitalQualifireMapperDtoS().get(i).getVitalQualifireId());
					vitalQualifireMapper.setVitalCategoryId(vitalQualifireMapperDto.getListOfVitalQualifireMapperDtoS().get(i).getVitalCategoryId());
					vitalQualifireMapper.setOrganizationId(vitalQualifireMapperDto.getOrganizationId());
					vitalQualifireMapper.setUnitId(vitalQualifireMapperDto.getUnitId());
					vitalQualifireMapper.setStatus(vitalQualifireMapperDto.getStatus());
					vitalQualifireMapper.setCreatedBy(vitalQualifireMapperDto.getCreatedBy());
					vitalQualifireMapper.setCreatedDate(TimeZoneComponent.getDateByzone(vitalQualifireMapperDto.getUnitId()));
					vitalQualifireMapper.setUpdatedBy(vitalQualifireMapperDto.getUpdatedBy());
					vitalQualifireMapper.setUpdatedDate(TimeZoneComponent.getDateByzone(vitalQualifireMapperDto.getUnitId()));
					save(vitalQualifireMapper);
			}
			} 

		return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		
		} catch (HibernateException he) {
		logger.error("updateVitalQualifireMapper failed", he);
		} catch (Exception e) {
		logger.error("updateVitalQualifireMapper failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateVitalQualifireMapperStatus(VitalQualifireMapperDto vitalQualifireMapperDto) throws ApplicationException{
		try {
				Session session=sessionFactory.getCurrentSession();
				session.createSQLQuery(" update emr.t_vital_qualifire_mapper  set status = '"+vitalQualifireMapperDto.getStatus()+"'"
				+ " where vital_id=:vitalId ")
						.setParameter("vitalId",vitalQualifireMapperDto.getVitalId())
				.executeUpdate();
				 
		
		return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		
		} catch (HibernateException he) {
		logger.error("updateVitalQualifireMapperStatus failed", he);
		} catch (Exception e) {
		logger.error("updateVitalQualifireMapperStatus failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@Override
	public Response getVitalQualifireMapperList(VitalQualifireMapperDto vitalQualifireMapperDto) throws ApplicationException{
		try {
			List<VitalQualifireMapperDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_VITAL_QUALIFIRE_MAPPER_LIST")
					.setParameter("organizationId", vitalQualifireMapperDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(VitalQualifireMapperDto.class)).list();
			
			for(VitalQualifireMapperDto dto : list) {
				List<VitalQualifireMapperDto> list1=sessionFactory.getCurrentSession()
						.getNamedQuery("GET_VITAL_QUALIFIRE_MAPPER_LIST_FOR_CATEGORY_IN_MAPPER")
						.setParameter("organizationId", vitalQualifireMapperDto.getOrganizationId())
						.setParameter("vitalId", dto.getVitalId())
						.setResultTransformer(Transformers.aliasToBean(VitalQualifireMapperDto.class)).list();
				
					for(VitalQualifireMapperDto dto1 : list1) {
						List<VitalQualifireMapperDto> list2=sessionFactory.getCurrentSession()
								.getNamedQuery("GET_VITAL_QUALIFIRE_MAPPER_LIST_FOR_QUALIFIRES_IN_MAPPER")
								.setParameter("organizationId", vitalQualifireMapperDto.getOrganizationId())
								.setParameter("vitalCategoryId", dto1.getVitalCategoryId())
								.setParameter("vitalId", dto.getVitalId())
								.setResultTransformer(Transformers.aliasToBean(VitalQualifireMapperDto.class)).list();
						dto1.setListOfVitalQualifiresMapperForQualifires(list2);
					}
					dto.setListOfVitalQualifiresForCategory(list1);
			}
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getVitalQualifireMapperList failed", he);
		} catch (Exception e) {
		logger.error("getVitalQualifireMapperList failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getVitalQualifireMapperCount(VitalQualifireMapperDto vitalQualifireMapperDto) throws ApplicationException{
		try {
			
			Session session = sessionFactory.getCurrentSession();
			Criteria crit = session.createCriteria(VitalQualifireMapper.class);
			crit.add(Restrictions.eq("organizationId", vitalQualifireMapperDto.getOrganizationId()));
			crit.add(Restrictions.eq("unitId", vitalQualifireMapperDto.getUnitId()));
			crit.setProjection(Projections.rowCount());
			Long count = (Long) crit.uniqueResult();
			
			return new Response(SUCCESS, SUCCESS_CODE, null, null, count);
				
		} catch (HibernateException he) {
		logger.error("getVitalQualifireMapperCount failed", he);
		} catch (Exception e) {
		logger.error("getVitalQualifireMapperCount failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActiveVitalQualifireMapper(Integer organizationId) throws ApplicationException{
		try {
			List<VitalQualifireMapperDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_VITAL_QUALIFIRE_MAPPER")
					.setParameter("organizationId", organizationId)
					.setResultTransformer(Transformers.aliasToBean(VitalQualifireMapperDto.class)).list();	
	
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getActiveVitalQualifireMapper failed", he);
		} catch (Exception e) {
		logger.error("getActiveVitalQualifireMapper failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getVitalQualifireMapperByNameNotId(VitalQualifireMapperDto vitalQualifireMapperDto) throws ApplicationException{
		try {
			List<VitalQualifireMapperDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_VITAL_QUALIFIRE_MAPPER_BY_NAME_NOT_ID")
					.setParameter("vitalId", vitalQualifireMapperDto.getVitalId())
					.setParameter("orgId", vitalQualifireMapperDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(VitalQualifireMapperDto.class)).list();
			 
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getVitalQualifireMapperByNameNotId failed", he);
		} catch (Exception e) {
		logger.error("getVitalQualifireMapperByNameNotId failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getVitalQualifireListByCategoryId(VitalQualifireMapperDto vitalQualifireMapperDto)
			throws ApplicationException {
		try {
			List<VitalQualifireMasterDto> list2=new ArrayList<>();
			List<VitalQualifireMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_LIST_OF_CATEGORY_WISE_QUALIFIERS")
					.setParameter("organizationId", vitalQualifireMapperDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(VitalQualifireMasterDto.class)).list();
			
	for(VitalQualifireMasterDto dto : list) {
		List<VitalQualifireMasterDto> list1=sessionFactory.getCurrentSession()
				.getNamedQuery("GET_VITAL_QUALIFIRE_LIST_BY_CATEGORY_ID")
				.setParameter("organizationId", vitalQualifireMapperDto.getOrganizationId())
				.setParameter("vitalCategoryId", dto.getVitalCategoryId())
				.setResultTransformer(Transformers.aliasToBean(VitalQualifireMasterDto.class)).list();
		dto.setListOfVitalQualifires(list1);
		list2.add(dto);
	}
		
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getVitalQualifireListByCategoryId failed", he);
		} catch (Exception e) {
		logger.error("getVitalQualifireListByCategoryId failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getVitalQualifireListByVitalId(VitalQualifireMapperDto vitalQualifireMapperDto)
			throws ApplicationException {
		try {
			List<VitalQualifireMapperDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_VITAL_QUALIFIRE_MAPPER_LIST_FOR_CATEGORY")
					.setParameter("organizationId", vitalQualifireMapperDto.getOrganizationId())
					.setParameter("vitalId", vitalQualifireMapperDto.getVitalId())

					.setResultTransformer(Transformers.aliasToBean(VitalQualifireMapperDto.class)).list();
			for(VitalQualifireMapperDto dto : list) {
						List<VitalQualifireMapperDto> list2=sessionFactory.getCurrentSession()
						.getNamedQuery("GET_VITAL_QUALIFIRE_MAPPER_LIST_FOR_QUALIFIRES")
						.setParameter("organizationId", vitalQualifireMapperDto.getOrganizationId())
						.setParameter("vitalCategoryId", dto.getVitalCategoryId())
						.setParameter("vitalId", vitalQualifireMapperDto.getVitalId())
						.setResultTransformer(Transformers.aliasToBean(VitalQualifireMapperDto.class)).list();
				dto.setListOfVitalQualifiresMapperForQualifires(list2);	
				
			}
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getVitalQualifireListByVitalId failed", he);
		} catch (Exception e) {
		logger.error("getVitalQualifireListByVitalId failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getVitalQualifireMapperListById(VitalQualifireMapperDto vitalQualifireMapperDto)
			throws ApplicationException {
		try {
			List<VitalQualifireMapperDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_VITAL_QUALIFIRE_MAPPER_LIST_BY_ID")
					.setParameter("organizationId", vitalQualifireMapperDto.getOrganizationId())
					.setParameter("vitalId", vitalQualifireMapperDto.getVitalId())
					.setResultTransformer(Transformers.aliasToBean(VitalQualifireMapperDto.class)).list();
			
			for(VitalQualifireMapperDto dto : list) {
				List<VitalQualifireMapperDto> list1=sessionFactory.getCurrentSession()
						.getNamedQuery("GET_VITAL_QUALIFIRE_MAPPER_LIST_FOR_CATEGORY_FOR_UPDATE")
						.setParameter("organizationId", vitalQualifireMapperDto.getOrganizationId())
						.setParameter("vitalId", dto.getVitalId())
						.setResultTransformer(Transformers.aliasToBean(VitalQualifireMapperDto.class)).list();
				
					for(VitalQualifireMapperDto dto1 : list1) {
						List<VitalQualifireMapperDto> list2=sessionFactory.getCurrentSession()
								.getNamedQuery("GET_VITAL_QUALIFIRE_MAPPER_LIST_FOR_QUALIFIRES_FOR_UPDATE")
								.setParameter("organizationId", vitalQualifireMapperDto.getOrganizationId())
								.setParameter("vitalCategoryId", dto1.getVitalCategoryId())
								.setParameter("vitalId", dto.getVitalId())
								.setResultTransformer(Transformers.aliasToBean(VitalQualifireMapperDto.class)).list();
						dto1.setListOfVitalQualifiresMapperForQualifires(list2);
					}
					dto.setListOfVitalQualifiresForCategory(list1);
			}
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getVitalQualifireMapperListById failed", he);
		} catch (Exception e) {
		logger.error("getVitalQualifireMapperListById failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


}
