package com.param.global.master.dao;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.master.dto.KeyIngredientsMasterDto;
import com.param.global.master.dto.NutritionalMasterDto;
import com.param.global.master.dto.UnitOfMeasurmentMasterDto;
import com.param.global.master.model.KeyIngredientsMaster;
import com.param.global.master.model.KeyIngredientsNutritionMapper;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "unchecked", "rawtypes" })
public class KeyIngredientsMasterDaoImpl extends GenericDao<KeyIngredientsMaster, Integer>
		implements IKeyIngredientsMasterDao, ICommonConstants {

	private static final Logger logger = Logger.getLogger(KeyIngredientsMasterDaoImpl.class);

	public KeyIngredientsMasterDaoImpl() {
		super(KeyIngredientsMaster.class);
	}

	@Autowired
	private DozerBeanMapper mapper;

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response getKeyIngredientsByName(KeyIngredientsMasterDto keyIngredientsMasterDto)
			throws ApplicationException {
		try {

			List<KeyIngredientsMasterDto> keyIngredientsMasterList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_KEY_INGREDIENTS_LIST_BY_NAME")
					.setParameter("ingredientsName", keyIngredientsMasterDto.getIngredientCode())
					.setParameter("orgId", keyIngredientsMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(KeyIngredientsMasterDto.class)).list();
			return new Response(SUCCESS, null, null, keyIngredientsMasterList, null);

		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response saveKeyIngredientsMaster(KeyIngredientsMasterDto keyIngredientsMasterDto)
			throws ApplicationException {
		try {
			KeyIngredientsMaster keyIngredientsMaster = mapper.map(keyIngredientsMasterDto, KeyIngredientsMaster.class,
					"KeyIngredientsMasterDto_to_KeyIngredientsMaster");
			keyIngredientsMaster.setCreatedDate(TimeZoneComponent.getDateByzone(keyIngredientsMasterDto.getUnitId()!=null ? keyIngredientsMasterDto.getUnitId() :0));
			keyIngredientsMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(keyIngredientsMasterDto.getUnitId()!=null ? keyIngredientsMasterDto.getUnitId() :0));
			keyIngredientsMaster = save(keyIngredientsMaster);
			for (Iterator iterator = keyIngredientsMasterDto.getNutriciansList().iterator(); iterator.hasNext();) {
				NutritionalMasterDto nutritionalMasterDto = (NutritionalMasterDto) iterator.next();
				KeyIngredientsNutritionMapper keyIngredientsNutritionMapper = new KeyIngredientsNutritionMapper();
				keyIngredientsNutritionMapper.setNutritionMasterId(nutritionalMasterDto.getNutritionId());
				keyIngredientsNutritionMapper.setKeyIngredientsMasterId(keyIngredientsMaster.getKeyIngredientId());
				keyIngredientsNutritionMapper.setNutritionValue(nutritionalMasterDto.getValue());
				keyIngredientsNutritionMapper.setUomId(nutritionalMasterDto.getUomId());
				keyIngredientsNutritionMapper.setOrganizationId(keyIngredientsMaster.getOrganizationId());
				keyIngredientsNutritionMapper.setCreatedBy(keyIngredientsMaster.getCreatedBy());
				keyIngredientsNutritionMapper.setCreatedDate(keyIngredientsMaster.getCreatedDate());
				keyIngredientsNutritionMapper.setUpdatedBy(keyIngredientsMaster.getUpdatedBy());
				keyIngredientsNutritionMapper.setUpdatedDate(keyIngredientsMaster.getUpdatedDate());
				keyIngredientsNutritionMapper.setStatus(keyIngredientsMaster.getStatus());
				sessionFactory.getCurrentSession().save(keyIngredientsNutritionMapper);
			}

			return new Response(SUCCESS, null, null, null, null);
		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getUnitMasterDetails(Integer organizationId,Integer unitId) throws ApplicationException {
		try {

			List<UnitOfMeasurmentMasterDto> unitOfMeasurmentMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_UNIT_MASTER_DETAILS_BY_NAME")
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(UnitOfMeasurmentMasterDto.class)).list();
			return new Response(SUCCESS, null, null, unitOfMeasurmentMasterDtosList, null);

		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getKeyIngredientsMasterById(KeyIngredientsMasterDto keyIngredientsMasterDto)
			throws ApplicationException {
		try {
			KeyIngredientsMasterDto keyIngredientsMasterList = (KeyIngredientsMasterDto) sessionFactory
					.getCurrentSession().getNamedQuery("GET_KEY_INGREDIENTS_MASTER_BY_ID")
					.setParameter("keyIngredientsId", keyIngredientsMasterDto.getKeyIngredientId())
					.setParameter("orgId", keyIngredientsMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(KeyIngredientsMasterDto.class)).uniqueResult();

			List<NutritionalMasterDto> nutritionalMasterList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_NUTRITION_LIST_BY_KEYINGREDIENTSMASTER_ID")
					.setParameter("keyIngredientId", keyIngredientsMasterDto.getKeyIngredientId())
					.setResultTransformer(Transformers.aliasToBean(NutritionalMasterDto.class)).list();

			keyIngredientsMasterList.setNutriciansList(nutritionalMasterList);
			return new Response(SUCCESS, null, null, null, keyIngredientsMasterList);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getKeyIngredientsDetailsById(KeyIngredientsMasterDto keyIngredientsMasterDto)
			throws ApplicationException {
		try {
			List<KeyIngredientsMasterDto> nutritionalMasterList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_NUTRITION_LIST_BY_KEYINGREDIENTSMASTER_ID")
					.setParameter("keyIngredientId", keyIngredientsMasterDto.getKeyIngredientId())
					.setResultTransformer(Transformers.aliasToBean(NutritionalMasterDto.class)).list();
			return new Response(SUCCESS, null, null, nutritionalMasterList, null);

		} catch (Exception e) {
			throw e;
		}
//		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getAllKeyIngredientsMaster(Integer organizationId,Integer unitId) throws ApplicationException {
		try {
			// BR-121: unitId was accepted by this method's signature but never
			// actually applied to the query (commented out, tagged "dilip") --
			// any unit within the same org could see every other unit's
			// ingredients. Restored now that the service layer passes only
			// verified values here, closing this alongside the org-level fix.
			List<KeyIngredientsMasterDto> getAllKeyIngredientsMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ALL_KEY_INGREDIENTS_MASTER")
					.setParameter("organizationId", organizationId)
					.setParameter("unitId",unitId)
					.setResultTransformer(Transformers.aliasToBean(KeyIngredientsMasterDto.class)).list();
			return new Response(SUCCESS, null, null, getAllKeyIngredientsMasterDtosList, null);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getAllKeyIngredientsMasterWithoutOrgId() throws ApplicationException {
		try {
			List<KeyIngredientsMasterDto> getAllKeyIngredientsMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ALL_KEY_INGREDIENTS_MASTER_WITHOUT_ORG_ID")
					.setResultTransformer(Transformers.aliasToBean(KeyIngredientsMasterDto.class)).list();
			return new Response(SUCCESS, null, null, getAllKeyIngredientsMasterDtosList, null);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getActiveKeyIngredientsMaster(Integer organizationId,Integer unitId) throws ApplicationException {
		try {
			// BR-121: unitId was accepted by this method's signature but never
			// actually applied to the query (commented out, tagged "dilip") --
			// same gap as getAllKeyIngredientsMaster above.
			List<KeyIngredientsMasterDto> getAllKeyIngredientsMasterDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_KEY_INGREDIENTS_MASTER")
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(KeyIngredientsMasterDto.class)).list();
			return new Response(SUCCESS, null, null, getAllKeyIngredientsMasterDtosList, null);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateKeyIngredientsMasterStatus(KeyIngredientsMasterDto keyIngredientsMasterDto)
			throws ApplicationException {
		try {
			Session session = sessionFactory.getCurrentSession();
			KeyIngredientsMaster keyIngredientsMaster = (KeyIngredientsMaster) session.get(KeyIngredientsMaster.class,
					keyIngredientsMasterDto.getKeyIngredientId());

			// BR-121: session.get(id) alone has no ownership check -- this
			// endpoint's legacy frontend doesn't even send organizationId/unitId,
			// so the DTO's values here are the service-layer-verified ones.
			// Refuse rather than silently mutating another tenant's row.
			if (keyIngredientsMaster == null
					|| !keyIngredientsMaster.getOrganizationId().equals(keyIngredientsMasterDto.getOrganizationId())
					|| !keyIngredientsMaster.getUnitId().equals(keyIngredientsMasterDto.getUnitId())) {
				logger.error("updateKeyIngredientsMasterStatus: refused update of keyIngredientId="
						+ keyIngredientsMasterDto.getKeyIngredientId() + " -- not owned by the verified organization/unit");
				return new Response<>(ERROR, ERROR_CODE, "Not found.", null, null);
			}

			keyIngredientsMaster.setStatus(keyIngredientsMasterDto.getStatus());
			keyIngredientsMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(keyIngredientsMasterDto.getUnitId()!= null ? keyIngredientsMasterDto.getUnitId() :0));
			keyIngredientsMaster.setUpdatedBy(keyIngredientsMasterDto.getUpdatedBy());
			session.update(keyIngredientsMaster);
			if (keyIngredientsMasterDto.getStatus() == 'A') {
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
	public Response updateKeyIngredientsMaster(KeyIngredientsMasterDto keyIngredientsMasterDto)
			throws ApplicationException {
		try {

			KeyIngredientsMaster keyIngredientsMaster = findById(keyIngredientsMasterDto.getKeyIngredientId());

			// BR-121: findById(id) alone has no ownership check -- verify the
			// row being updated really belongs to the (service-layer-verified)
			// tenant on the DTO before mutating it. Checked against the same
			// already-loaded managed entity (not a second findById() call),
			// since loading a second entity with the same identifier in this
			// session throws Hibernate's NonUniqueObjectException once this
			// method's own update() call runs -- confirmed live on the
			// checklistEquipmentMaster stopgap fix, avoided here from the start.
			if (keyIngredientsMaster == null
					|| !keyIngredientsMaster.getOrganizationId().equals(keyIngredientsMasterDto.getOrganizationId())
					|| !keyIngredientsMaster.getUnitId().equals(keyIngredientsMasterDto.getUnitId())) {
				logger.error("updateKeyIngredientsMaster: refused update of keyIngredientId="
						+ keyIngredientsMasterDto.getKeyIngredientId() + " -- not owned by the verified organization/unit");
				return new Response<>(ERROR, ERROR_CODE, "Not found.", null, null);
			}

			keyIngredientsMaster.setIngredientCode(keyIngredientsMasterDto.getIngredientCode());
			keyIngredientsMaster.setIngredientDescription(keyIngredientsMasterDto.getIngredientDescription());
			keyIngredientsMaster.setIngredientValue(keyIngredientsMasterDto.getIngredientValue());
			keyIngredientsMaster.setUomId(keyIngredientsMasterDto.getUomId());
			keyIngredientsMaster.setUpdatedBy(keyIngredientsMasterDto.getUpdatedBy());
			keyIngredientsMaster.setStatus(keyIngredientsMasterDto.getStatus());
			keyIngredientsMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(keyIngredientsMasterDto.getUnitId()!= null ? keyIngredientsMasterDto.getUnitId() :0));

			update(keyIngredientsMaster);

			String deleteQuery = "DELETE FROM KeyIngredientsNutritionMapper WHERE key_ingredients_master_id=:key_ingredients_master_id";
			sessionFactory.getCurrentSession().createQuery(deleteQuery)
					.setParameter("key_ingredients_master_id", keyIngredientsMasterDto.getKeyIngredientId())
					.executeUpdate();

			for (Iterator iterator = keyIngredientsMasterDto.getNutriciansList().iterator(); iterator.hasNext();) {
				NutritionalMasterDto nutritionalMasterDto = (NutritionalMasterDto) iterator.next();
				KeyIngredientsNutritionMapper keyIngredientsNutritionMapper = new KeyIngredientsNutritionMapper();
				keyIngredientsNutritionMapper.setNutritionMasterId(nutritionalMasterDto.getNutritionId());
				keyIngredientsNutritionMapper.setKeyIngredientsMasterId(keyIngredientsMaster.getKeyIngredientId());
				keyIngredientsNutritionMapper.setNutritionValue(nutritionalMasterDto.getValue());
				keyIngredientsNutritionMapper.setUomId(nutritionalMasterDto.getUomId());
				keyIngredientsNutritionMapper.setOrganizationId(keyIngredientsMaster.getOrganizationId());
				keyIngredientsNutritionMapper.setCreatedBy(keyIngredientsMaster.getCreatedBy());
				keyIngredientsNutritionMapper.setCreatedDate(keyIngredientsMaster.getCreatedDate());
				keyIngredientsNutritionMapper.setUpdatedBy(keyIngredientsMaster.getUpdatedBy());
				keyIngredientsNutritionMapper.setUpdatedDate(keyIngredientsMaster.getUpdatedDate());
				keyIngredientsNutritionMapper.setStatus(keyIngredientsMaster.getStatus());
				sessionFactory.getCurrentSession().save(keyIngredientsNutritionMapper);
			}
			// for (Iterator iterator =
			// keyIngredientsMasterDto.getNutriciansList().iterator();
			// iterator.hasNext();) {
			// NutritionalMasterDto nutritionalMasterDto =
			// (NutritionalMasterDto) iterator.next();
			// if(nutritionalMasterDto.getIngredientNutritionId()!=null){
			// String updateQuery =
			// "UPDATE "
			// + " KeyIngredientsNutritionMapper "
			// + " SET nutritionValue=:nutritionValue ,"
			// + " nutritionMasterId=:nutritionMasterId , "
			// + " uomId=:uomId ,"
			// + " updatedBy=:updatedBy ,"
			// + " updatedDate=:updatedDate "
			// + " WHERE ingredientNutritionId=:id";
			// sessionFactory.getCurrentSession().createQuery(updateQuery)
			// .setParameter("nutritionValue", nutritionalMasterDto.getValue())
			// .setParameter("nutritionMasterId",
			// nutritionalMasterDto.getNutritionId())
			// .setParameter("uomId", nutritionalMasterDto.getUomId())
			// .setParameter("updatedBy",
			// keyIngredientsMasterDto.getUpdatedBy())
			// .setParameter("updatedDate",TimeZoneComponent.getDateByzone(unitId))
			// .setParameter("id",
			// nutritionalMasterDto.getIngredientNutritionId())
			// .executeUpdate();
			// }else{
			// //System.out.println("you have added new nutrition in existing
			// nutrition list");
			// }
			// }

			return new Response<KeyIngredientsMasterDto, Integer>(SUCCESS, null, COMMON_UPDATE, null, null);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public boolean isKeyIngredientOwnedByOrgAndUnit(Integer keyIngredientId, Integer organizationId, Integer unitId)
			throws ApplicationException {
		try {
			KeyIngredientsMaster keyIngredientsMaster = findById(keyIngredientId);
			return keyIngredientsMaster != null
					&& keyIngredientsMaster.getOrganizationId().equals(organizationId)
					&& keyIngredientsMaster.getUnitId().equals(unitId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

}
