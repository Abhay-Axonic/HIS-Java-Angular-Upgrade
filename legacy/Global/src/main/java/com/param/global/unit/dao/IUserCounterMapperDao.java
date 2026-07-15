package com.param.global.unit.dao;

import com.param.global.common.Response;
import com.param.global.dto.UserCounterMapperDto;
import com.param.global.model.UserCounterMapper;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings({ "rawtypes" })
public interface IUserCounterMapperDao extends IGenericDao<UserCounterMapper, Integer> {
	
	public Response addUserCounterMapper(UserCounterMapperDto userCounterMapperDto)throws ApplicationException;
	public Response updateUserCounterMapper(UserCounterMapperDto userCounterMapperDto) throws ApplicationException;
	public Response deleteUserCounerMapper(UserCounterMapperDto userCounterMapperDto) throws ApplicationException;
	public Response checkUserIsMappedOrNotAdd(UserCounterMapperDto userCounterMapperDto) throws ApplicationException;
	public Response listUserCounterMapper(Integer orgId,Integer unitId, Integer offset, Integer recordPerPage) throws ApplicationException;
	public Response getTotalUserCounterMapperCount(Integer orgId,Integer unitId) throws ApplicationException;
	public Response activateInactivateUserCounterMapper(Integer userId, char status) throws ApplicationException;
	public Response getUserCounterMapperViewById(Integer orgId,Integer unitId, Integer userId) throws ApplicationException;
	public Response getUserCounterMapperById(Integer orgId,Integer unitId, Integer userId) throws ApplicationException;

}
