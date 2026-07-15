package com.param.actionmaster.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.billing.global.model.UserActionFavourites;
import com.param.data.dto.UserActionFavouritesDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.utility.TimeZoneComponent;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class UserActionFavouritesDaoImpl implements IUserActionFavouritesDao,ICommonConstants
{
	final static Logger logger = Logger.getLogger(UserActionFavouritesDaoImpl.class);


	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	public Response saveUserActionFavourites(UserActionFavouritesDto userActionFavouritesDto) {
		try {
			
			UserActionFavourites userActionFavourites = new UserActionFavourites();
				userActionFavourites.setActionId(userActionFavouritesDto.getActionId());
				userActionFavourites.setUserId(userActionFavouritesDto.getUserId());
				userActionFavourites.setOrganizationId(userActionFavouritesDto.getOrganizationId());
				userActionFavourites.setUnitId(userActionFavouritesDto.getUnitId());
				userActionFavourites.setStatus('A');
				userActionFavourites.setCreatedDate(TimeZoneComponent.getDateByzone(userActionFavouritesDto.getUnitId()));
				userActionFavourites.setUpdatedDate(TimeZoneComponent.getDateByzone(userActionFavouritesDto.getUnitId()));
			sessionFactory.getCurrentSession().save(userActionFavourites);
			
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		} catch (Exception e) {
		logger.error("saveUserActionFavourites failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response remove(Integer id) {
		try {
				sessionFactory.getCurrentSession().createQuery("UPDATE UserActionFavourites set status='I' where id = "+id).executeUpdate();
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (Exception e) {
		logger.error("remove failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@Override
	public Response get(Integer userId,Integer unitId) {
		try {

			String query = " select " 
					 + " 	id as \"id\", " 
					 + " 	fav.action_id as \"actionId\", " 
					 + " 	act.action_name as \"actionName\", " 
					 + " 	act.action_link as \"actionLink\" " 
					 + " from " 
					 + " 	t_user_action_favorites fav " 
					 + " inner join m_action_master act on " 
					 + " 	act.action_id = fav.action_id " 
					 + " 	and fav.status = 'A' " 
					 + " 	and fav.user_id =:userId " 
					 + " 	and fav.unit_id =:unitId ";
			
			List<UserActionFavouritesDto> userActionFavouritesDtosList = sessionFactory.getCurrentSession()
					.createSQLQuery(query)
					.setParameter("userId", userId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(UserActionFavouritesDto.class)).list();
			
			return new Response(SUCCESS, SUCCESS_CODE, null, userActionFavouritesDtosList, null);
		} catch (Exception e) {
		logger.error("get failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}
