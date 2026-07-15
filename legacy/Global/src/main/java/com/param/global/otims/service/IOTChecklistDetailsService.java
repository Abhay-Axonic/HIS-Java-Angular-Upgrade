package com.param.global.otims.service;

import java.util.List;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.otims.dto.OTChecklistDetailsDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
@Transactional
public interface IOTChecklistDetailsService {

	Response saveOTChecklistDetails(List<OTChecklistDetailsDto> oTChecklistDetailsDto)throws ApplicationException;

	Response getIntraOperativeChecklistByProReqId(OTChecklistDetailsDto oTChecklistDetailsDto)throws ApplicationException;

}
