package com.param.global.dao;

import com.param.global.common.Response;

public interface IGetStoreListUsingsessionDao {

	Response getStoreListUsingSessionByUserId(Integer userId, Integer unitId);

}
