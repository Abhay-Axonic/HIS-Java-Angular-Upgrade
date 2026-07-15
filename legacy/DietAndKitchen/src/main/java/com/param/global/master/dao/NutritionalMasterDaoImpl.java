package com.param.global.master.dao;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
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
import com.param.global.common.ServiceException;
import com.param.global.master.dto.NutritionalMasterDto;
import com.param.global.master.model.NutritionalMaster;
import com.param.global.model.CountryMaster;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class NutritionalMasterDaoImpl extends GenericDao<NutritionalMaster, Integer>
		implements INutritionalMasterDao, ICommonConstants {

	private static final Logger logger = Logger.getLogger(NutritionalMasterDaoImpl.class);

	public NutritionalMasterDaoImpl() {
		super(NutritionalMaster.class);
	}

	@Autowired
	private DozerBeanMapper mapper;

	@Autowired
	SessionFactory sessionFactory;

	public Response getNutritionByName(NutritionalMasterDto nutritionalMasterDto) throws ApplicationException {
		try {
			List<NutritionalMasterDto> nutritionalMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_NUTRITION_MASTER_LIST_BY_NAME")
					.setParameter("nutritionName", nutritionalMasterDto.getNutritionName().toLowerCase())
					.setParameter("nutritionCode", nutritionalMasterDto.getNutritionCode().toLowerCase())
					.setParameter("orgId", nutritionalMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(NutritionalMasterDto.class)).list();
			return new Response(SUCCESS, null, null, nutritionalMasterDtoList, null);

		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	public Response saveNutritionalMaster(NutritionalMasterDto nutritionalMasterDto) throws ApplicationException {
		try {
			NutritionalMaster nutritionalMaster = mapper.map(nutritionalMasterDto, NutritionalMaster.class,
					"NutritionalMasterDto_to_NutritionalMaster");
			nutritionalMaster.setCreatedDate(TimeZoneComponent.getDateByzone(nutritionalMasterDto.getUnitId()!=null ? nutritionalMasterDto.getUnitId() :0));
			nutritionalMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(nutritionalMasterDto.getUnitId()!=null ? nutritionalMasterDto.getUnitId() :0));
			save(nutritionalMaster);
			return new Response(SUCCESS, null, COMMON_SUCCESS, null, null);
		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getNutritionalMasterList(NutritionalMasterDto nutritionalMasterDto) throws ApplicationException {
		try {
			List<NutritionalMasterDto> nutritionalMasterDtoListById = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_NUTRITION_MASTER_LIST")
					.setParameter("orgId", nutritionalMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(NutritionalMasterDto.class)).list();
			return new Response(SUCCESS, null, null, nutritionalMasterDtoListById, null);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getAllNutritionalMasterList(Integer organizationId,Integer unitId,Integer offset, Integer noOfRecordsPerPage) throws ApplicationException {
		try {
			List<NutritionalMasterDto> nutritionalMasterDtoListById = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ALL_NUTRITION_MASTER_LIST")
					.setParameter("organizationId", organizationId)
					//.setParameter("unitId", unitId) //dilip
					.setFirstResult(offset != null ? offset : 0)
					.setMaxResults(noOfRecordsPerPage != null ? noOfRecordsPerPage : 10)
					.setResultTransformer(Transformers.aliasToBean(NutritionalMasterDto.class)).list();
			return new Response(SUCCESS, null, null, nutritionalMasterDtoListById, null);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getActiveNutritionalMasterList(Integer organizationId, Integer unitId) throws ApplicationException {
		try {
			List<NutritionalMasterDto> nutritionalMasterDtoListById = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_NUTRITION_MASTER_LIST")
					.setParameter("organizationId", organizationId)
					//.setParameter("unitId", unitId) //dilip
					.setResultTransformer(Transformers.aliasToBean(NutritionalMasterDto.class)).list();
			return new Response(SUCCESS, null, null, nutritionalMasterDtoListById, null);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateNutritionalMaster(NutritionalMasterDto nutritionalMasterDto) throws ApplicationException {
		try {

			Session session = sessionFactory.getCurrentSession();
			NutritionalMaster nutritionalMaster = (NutritionalMaster) session.load(NutritionalMaster.class,
					nutritionalMasterDto.getNutritionId());

			nutritionalMaster.setNutritionCode(nutritionalMasterDto.getNutritionCode());
			nutritionalMaster.setNutritionName(nutritionalMasterDto.getNutritionName());
			nutritionalMaster.setUpdatedBy(nutritionalMasterDto.getUpdatedBy());
			nutritionalMaster.setOrganizationId(nutritionalMasterDto.getOrganizationId());
			nutritionalMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(0));
			session.update(nutritionalMaster);
			return new Response<NutritionalMasterDto, Integer>(SUCCESS, null, COMMON_UPDATE, null, null);

		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getNutritionalMasterById(NutritionalMasterDto nutritionalMasterDto) throws ApplicationException {
		try {
			List<NutritionalMasterDto> nutritonMasterList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_NUTRITION_MASTER_LIST_BY_ID")
					.setParameter("nutritionId", nutritionalMasterDto.getNutritionId())
					.setParameter("orgId", nutritionalMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(NutritionalMasterDto.class)).list();
			return new Response(SUCCESS, null, null, nutritonMasterList, null);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateNutritionMasterStatus(NutritionalMasterDto nutritionalMasterDto) throws ApplicationException {
		try {
			Session session = sessionFactory.getCurrentSession();
			NutritionalMaster nutritionalMaster = (NutritionalMaster) session.load(NutritionalMaster.class,
					nutritionalMasterDto.getNutritionId());
			nutritionalMaster.setStatus(nutritionalMasterDto.getStatus());
			nutritionalMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(0));
			session.update(nutritionalMaster);
			if (nutritionalMaster.getStatus() == 'A') {
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
			} else {
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_DELETE, null, null);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response checkNutritionByIdNotByName(NutritionalMasterDto nutritionalMasterDto) throws ApplicationException {
		try {
			List<NutritionalMasterDto> nutritonMasterList = sessionFactory.getCurrentSession()
					.getNamedQuery("CHECK_NUTRITION_BY_ID_NOT_BY_NAME")
					.setParameter("nutritionId", nutritionalMasterDto.getNutritionId())
					.setParameter("orgId", nutritionalMasterDto.getOrganizationId())
					.setParameter("nutritionName", nutritionalMasterDto.getNutritionName().toLowerCase())
					.setParameter("nutritionCode", nutritionalMasterDto.getNutritionCode().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(NutritionalMasterDto.class)).list();
			return new Response(SUCCESS, null, null, nutritonMasterList, null);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getNutritionMasterCount(NutritionalMasterDto nutritionalMasterDto) throws ApplicationException {
		try{
			Session session=sessionFactory.getCurrentSession();
			Criteria c=session.createCriteria(NutritionalMaster.class); 
			c.add(Restrictions.eq("organizationId", nutritionalMasterDto.getOrganizationId()));
			c.setProjection(Projections.rowCount());  
			Long count = (Long)c.uniqueResult();
			session.close();
			return new Response(SUCCESS, null, null, null, count);
		}catch(Exception e)
		{
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

}
