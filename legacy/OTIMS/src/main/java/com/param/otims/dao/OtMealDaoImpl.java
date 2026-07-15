package com.param.otims.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.otims.dto.OtMealDto;
import com.param.otims.model.OtMeal;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class OtMealDaoImpl extends GenericDao<OtMeal, Integer> implements IOtMealDao,ICommonConstants{

	private static final Logger logger = Logger.getLogger(OtMealDaoImpl.class);

	public OtMealDaoImpl() {
		super(OtMeal.class);
	}

	@Override
	public Response saveOtMeal(OtMealDto dto) throws ApplicationException {
		try{
			OtMeal master = new OtMeal();
			master.setPostOperativeCareId(dto.getPostOperativeCareId());
			master.setMealId(dto.getMealId());
			master.setOrganizationId(dto.getOrganizationId());
			master.setUnitId(dto.getUnitId());
			master.setCreatedBy(dto.getCreatedBy());
			master.setCreatedDate(dto.getCreatedDate() != null && !dto.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
			master.setUpdatedDate(dto.getUpdatedDate() != null && !dto.getUpdatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
			master.setUpdatedBy(dto.getUpdatedBy());
			master.setStatus(dto.getStatus());
			if(dto.getOtMealId() != null) {
				master.setOtMealId(dto.getOtMealId());
				master = update(master);
			}else {
				master = save(master);
			}
			
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, master);
		 } catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage());
        }
	}

	@Override
	public Response getOtMeals(Integer postOperativeCareId) throws ApplicationException {
		try{
			List<OtMealDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_OT_MEAL_DATA")
					.setParameter("postOperativeCareId", postOperativeCareId)
					.setResultTransformer(Transformers.aliasToBean(OtMealDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		}catch(Exception e){
			
			throw e;
		}
	}

}
