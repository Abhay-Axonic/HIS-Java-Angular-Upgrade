package com.param.global.otims.dao;

import com.param.global.common.Response;
import com.param.global.otims.dto.SubSequentDetailsDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface ISubSequentDetailsDao {

	Response saveSubSequentDetails(SubSequentDetailsDto dto)throws ApplicationException;

}
