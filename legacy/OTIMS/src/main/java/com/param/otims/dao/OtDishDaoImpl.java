package com.param.otims.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.otims.dto.OtDishDto;
import com.param.otims.model.OtDish;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class OtDishDaoImpl  extends GenericDao<OtDish, Integer> implements IOtDishDao,ICommonConstants{

	private static final Logger logger = Logger.getLogger(OtDishDaoImpl.class);

	public OtDishDaoImpl() {
		super(OtDish.class);
	}

	@Override
	public Response saveOtDish(OtDishDto dto) throws ApplicationException {
		try{
			OtDish master = new OtDish();
			master.setOtMealId(dto.getOtMealId());
			master.setDishId(dto.getDishId());
			master.setOrganizationId(dto.getOrganizationId());
			master.setUnitId(dto.getUnitId());
			master.setCreatedBy(dto.getCreatedBy());
			master.setCreatedDate(dto.getCreatedDate() != null && !dto.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
			master.setUpdatedDate(dto.getUpdatedDate() != null && !dto.getUpdatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
			master.setUpdatedBy(dto.getUpdatedBy());
			master.setStatus('A');
			if(dto.getOtDishId() != null) {
				master.setOtDishId(dto.getOtDishId());
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
	public Response getOtDishData(Integer otMealId) throws ApplicationException {
		try{
			List<OtDishDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_OT_DISH_DATA")
					.setParameter("otMealId", otMealId)
					.setResultTransformer(Transformers.aliasToBean(OtDishDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		}catch(Exception e){
			
			throw e;
		}
	}

}
