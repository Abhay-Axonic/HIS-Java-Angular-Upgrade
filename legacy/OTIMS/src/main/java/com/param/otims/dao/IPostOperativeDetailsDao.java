package com.param.otims.dao;

import com.param.global.common.Response;
import com.param.global.otims.dto.PostOperativeDetailsDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IPostOperativeDetailsDao {

	Response savePostOperativeDetails(PostOperativeDetailsDto postOperativeDetailsDto)throws ApplicationException;

	Response getPostOperativeDetails(PostOperativeDetailsDto postOperativeDetailsDto)throws ApplicationException;

}
