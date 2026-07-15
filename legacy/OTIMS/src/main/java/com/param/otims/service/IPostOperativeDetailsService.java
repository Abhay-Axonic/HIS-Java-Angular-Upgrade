package com.param.otims.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.otims.dto.PostOperativeDetailsDto;

import in.param.exception.ApplicationException;

@Transactional
@SuppressWarnings("rawtypes")
public interface IPostOperativeDetailsService {

	Response savePostOperativeDetails(PostOperativeDetailsDto postOperativeDetailsDto)throws ApplicationException;

	Response getPostOperativeDetails(PostOperativeDetailsDto postOperativeDetailsDto)throws ApplicationException;

}
