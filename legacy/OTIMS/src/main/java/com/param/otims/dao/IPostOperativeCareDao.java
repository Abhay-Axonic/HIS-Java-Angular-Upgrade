package com.param.otims.dao;

import com.param.global.common.Response;
import com.param.global.otims.dto.PostOperativeCareDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IPostOperativeCareDao {
	
	Response savePostOperativeCare(PostOperativeCareDto postOperativeCareDto)throws ApplicationException;

	Response getNotesListByProConfigId(Integer orgId, Integer unitId, Integer proReqId)throws ApplicationException;

	Response getPostOperativeCare(Integer proReqId)throws ApplicationException;

}
