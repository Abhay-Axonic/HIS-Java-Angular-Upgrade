package com.param.global.otims.dao;

import com.param.global.common.Response;
import com.param.global.otims.dto.SubSequentDetailsDto;
import com.param.global.otims.dto.SubSequentDetailsInfoDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface ISubSequentDetailsInfoDao {

	Response saveSubSequentDetailsInfo(SubSequentDetailsInfoDto info)throws ApplicationException;

	Response getSubSequentDetailsInfo(SubSequentDetailsDto subSequentDetailsDto)throws ApplicationException;

}
