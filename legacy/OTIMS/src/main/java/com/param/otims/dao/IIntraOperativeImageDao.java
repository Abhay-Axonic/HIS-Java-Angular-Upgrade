package com.param.otims.dao;

import com.param.global.common.Response;
import com.param.global.otims.dto.IntraOperativeImageDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IIntraOperativeImageDao {

	Response saveIntraImage(IntraOperativeImageDto dto)throws ApplicationException;

}
