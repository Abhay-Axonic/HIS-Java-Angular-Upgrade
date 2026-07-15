package com.param.global.unit.dao;

import java.util.List;

import com.param.global.common.Response;
import com.param.global.unit.dto.UserStoreMapperDto;

import in.param.exception.ApplicationException;

@SuppressWarnings({"rawtypes"})
public interface IUserStoreMapperDao {
	public Response saveUserStoreMapper(UserStoreMapperDto userStoreMapperDto) throws ApplicationException;
	public Response updateUserStoreMapper(List<UserStoreMapperDto> listuserStoreMapperDto) throws ApplicationException;
	public Response updateUserStoreMapperStatus(Integer userId, Integer organizationId, Integer unitId, Boolean isActive) throws ApplicationException;
	public Response getUserStoreMapperlist(UserStoreMapperDto userStoreMapperDto) throws ApplicationException;
	public Response getUserStoreMapperlistUserNameWise(UserStoreMapperDto userStoreMapperDto) throws ApplicationException;
	public Response getUserStoreMapperCount(Integer organizationId, Integer unitId) throws ApplicationException;
	public Response getActiveUserStoreMapper(Integer organizationId, Integer unitId) throws ApplicationException;
	public Response getUserStoreMapperById(Integer userId, Integer organizationId, Integer unitId) throws ApplicationException;
	public Response checkUserStoreMapperAvailable(UserStoreMapperDto userStoreMapperDto) throws ApplicationException;
//	public Response getUserList(Integer organizationId, Integer unitId) throws ApplicationException;
	public Response getDetailedStoreList(Integer userId, Integer organizationId, Integer unitId) throws ApplicationException;
	public Response getStoreList(Integer organizationId, Integer unitId) throws ApplicationException;
}
