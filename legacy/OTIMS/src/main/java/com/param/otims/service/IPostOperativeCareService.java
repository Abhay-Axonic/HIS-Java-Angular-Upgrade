package com.param.otims.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.otims.dto.PostOperativeCareDto;

import in.param.exception.ApplicationException;
@Transactional
@SuppressWarnings("rawtypes")
public interface IPostOperativeCareService {

	Response savePostOperativeCare(PostOperativeCareDto postOperativeCareDto)throws ApplicationException;

	Response getNotesListByProConfigId(Integer orgId, Integer unitId, Integer proReqId)throws ApplicationException;

}
