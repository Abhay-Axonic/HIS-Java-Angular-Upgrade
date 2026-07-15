package com.param.global.master.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.master.dto.DietTemplateDto;
import com.param.global.master.dto.DietTemplateMapperDto;
import com.param.global.master.model.DietTemplate;
import com.param.global.master.model.DietTemplateMapper;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DietTemplateMapperDaoImpl extends GenericDao<DietTemplateMapper, Integer>
		implements IDietTemplateMapperDao, ICommonConstants {

	private static final Logger logger = Logger.getLogger(DietTemplateMapperDaoImpl.class);

	public DietTemplateMapperDaoImpl() {
		super(DietTemplateMapper.class);
	}

	@Autowired
	SessionFactory sessionFactory;

	/*
	 * @Override public Response saveDietTemplateMapper(DietTemplateMapperDto
	 * dietTemplateMapperDto) throws ApplicationException { try {
	 * DietTemplateMapper dietTemplateMapper = mapper.map(dietTemplateMapperDto,
	 * DietTemplateMapper.class, "DietTemplateMapperDto_to_DietTemplateMapper");
	 * dietTemplateMapper.setCreatedDate(TimeZoneComponent.getDateByzone(unitId));
	 * dietTemplateMapper.setUpdatedDate(TimeZoneComponent.getDateByzone(unitId));
	 * sessionFactory.getCurrentSession().save(dietTemplateMapper);
	 *
	 * return new Response(SUCCESS, null, null, null, null); } catch
	 * (HibernateException he) { h } catch (Exception e) {
	 *  } return new Response(ERROR, null, null, null,
	 * null); }
	 */

	@Override
	public Response saveDietTemplateMapper(DietTemplateDto dietTemplateDto) throws ApplicationException {
		try {
			DietTemplate dietTemplate = new DietTemplate();

			dietTemplate.setDietTypeId(dietTemplateDto.getDietTypeId());

			dietTemplate.setOrganizationId(dietTemplateDto.getOrganizationId());

			dietTemplate.setUnitId(dietTemplateDto.getUnitId());

			dietTemplate.setUpdatedBy(dietTemplateDto.getUpdatedBy());

			dietTemplate.setCreatedBy(dietTemplateDto.getCreatedBy());

			dietTemplate.setUpdatedDate(TimeZoneComponent.getDateByzone(dietTemplateDto.getUnitId()));

			if (dietTemplateDto.getCreatedDate() != null) {
				dietTemplate.setCreatedDate(new SimpleDateFormat("yyyy-MM-dd").parse(dietTemplateDto.getCreatedDate()));
			} else {
				dietTemplate.setCreatedDate(TimeZoneComponent.getDateByzone(dietTemplateDto.getUnitId()));
			}

			Session session = sessionFactory.getCurrentSession();
			Integer dietTemplateId = (Integer) session.save(dietTemplate);

			return new Response(SUCCESS, null, null, null, dietTemplateId);
		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@SuppressWarnings("unused")
	@Override
	public Response updateDietTemplateMapper(DietTemplateDto dietTemplateDto) throws ApplicationException {
		try {
			Session session = sessionFactory.getCurrentSession();

			DietTemplate dietTemplate = (DietTemplate) session.get(DietTemplate.class,
					dietTemplateDto.getDietTemplateId());

			dietTemplateDto.setCreatedBy(dietTemplate.getCreatedBy());
			dietTemplateDto.setCreatedDate(dietTemplate.getCreatedDate().toString());

			Query query1 = session.createQuery("delete MealDetails md where md.dietTemplateId = :dietTemplateId");
			query1.setParameter("dietTemplateId", dietTemplateDto.getDietTemplateId());

			int result1 = query1.executeUpdate();

			Query query2 = session.createQuery(
					"delete DietTemplateDietPreferenceMapper dtm where dtm.dietTemplateId = :dietTemplateId");
			query2.setParameter("dietTemplateId", dietTemplateDto.getDietTemplateId());
			int result2 = query2.executeUpdate();

			Query query3 = session.createQuery("delete DietTemplate dt where dt.dietTemplateId = :dietTemplateId");
			query3.setParameter("dietTemplateId", dietTemplateDto.getDietTemplateId());
			int result3 = query3.executeUpdate();

			return new Response(SUCCESS, null, null, null, dietTemplateDto);
		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getDietTemplateMapperByDishAndFoodType(DietTemplateMapperDto dietTemplateMapperDto)
			throws ApplicationException {
		try {
			List<DietTemplateMapperDto> dietTemplateMapperList = sessionFactory.getCurrentSession()
					.getNamedQuery("DIET_TEMPLATE_MAPPER.GET_LIST_BY_DISH_FOOD_TYPE")
					.setParameter("dietTypeMasterId", dietTemplateMapperDto.getDietTypeMasterId())
					.setParameter("dietOption", dietTemplateMapperDto.getDietOption())
					.setResultTransformer(Transformers.aliasToBean(DietTemplateMapperDto.class)).list();

			return new Response(SUCCESS, null, null, null, dietTemplateMapperList);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getActiveDietTemplateMapper() throws ApplicationException {
		try {
			List<DietTemplateMapperDto> dietTemplateMapperList = sessionFactory.getCurrentSession()
					.getNamedQuery("DIET_TEMPLATE_MAPPER.GET_ACTIVE_LIST")
					.setResultTransformer(Transformers.aliasToBean(DietTemplateMapperDto.class)).list();

			return new Response(SUCCESS, null, null, null, dietTemplateMapperList);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getAllDietTemplateMapper() throws ApplicationException {
		try {
			List<DietTemplateMapperDto> dietTemplateMapperList = sessionFactory.getCurrentSession()
					.getNamedQuery("DIET_TEMPLATE_MAPPER.GET_ALL_LIST")
					.setResultTransformer(Transformers.aliasToBean(DietTemplateMapperDto.class)).list();

			return new Response(SUCCESS, null, null, null, dietTemplateMapperList);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateStatus(DietTemplateMapperDto dietTemplateMapperDto) {
		try {
			Session session = sessionFactory.getCurrentSession();
			DietTemplateMapper dietTemplateMapper = (DietTemplateMapper) session.get(DietTemplateMapper.class,
					dietTemplateMapperDto.getDietTemplateId());
			dietTemplateMapper.setUpdatedBy(dietTemplateMapperDto.getUpdatedBy());
			dietTemplateMapper.setUpdatedDate(TimeZoneComponent.getDateByzone(0));
			dietTemplateMapper.setStatus(dietTemplateMapperDto.getStatus());
			session.update(dietTemplateMapper);
			if (dietTemplateMapperDto.getStatus() == 'A') {
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
			} else {
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_DELETE, null, null);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

}
