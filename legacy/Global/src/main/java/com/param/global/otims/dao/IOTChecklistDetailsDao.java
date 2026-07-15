package com.param.global.otims.dao;

import com.param.global.otims.dto.OTChecklistDetailsDto;
import com.param.global.common.Response;
import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IOTChecklistDetailsDao {

	Response saveOTChecklistDetails(OTChecklistDetailsDto otChecklistDetailsDto2)throws ApplicationException;

	Response getIntraOperativeChecklistByProReqId(OTChecklistDetailsDto oTChecklistDetailsDto)throws ApplicationException;

}
