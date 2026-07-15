package com.param.actionmaster.dao;

import com.param.data.dto.UserActionFavouritesDto;
import com.param.global.common.Response;
@SuppressWarnings("rawtypes")
public interface IUserActionFavouritesDao {

	Response saveUserActionFavourites(UserActionFavouritesDto userActionFavouritesDto);

	Response remove(Integer id);

	Response get(Integer userId, Integer unitId);

}
