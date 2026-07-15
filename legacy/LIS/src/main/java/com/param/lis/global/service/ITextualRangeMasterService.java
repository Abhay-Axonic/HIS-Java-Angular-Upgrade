package com.param.lis.global.service;

import javax.transaction.Transactional;
import com.param.lis.global.common.Response;
import com.param.lis.global.dto.TextualRangeMasterDto;

import in.param.exception.ApplicationException;

@Transactional
@SuppressWarnings({"rawtypes"})

public interface ITextualRangeMasterService {
	
	public Response saveTextualRangeMaster(TextualRangeMasterDto textualRangeMasterDto) throws ApplicationException;
	
	public Response updateTextualRangeMaster(TextualRangeMasterDto textualRangeMasterDto) throws ApplicationException;
	
	public Response updateTextualRangeMasterStatus(TextualRangeMasterDto textualRangeMasterDto) throws ApplicationException;
	
	public Response getTextualRangeMasterById(Integer textualRangeId, Integer orgId, Integer orgUnitId) throws ApplicationException;
	
	public Response getTextualRangeMasterList(TextualRangeMasterDto textualRangeMasterDto) throws ApplicationException;
	
	public Response getActiveTextualRangeMasterList(TextualRangeMasterDto textualRangeMasterDto) throws ApplicationException;
	
	public Response getTextualRangeMasterCount(TextualRangeMasterDto textualRangeMasterDto) throws ApplicationException;
	
	public Response getTextualRangeMasterByName(TextualRangeMasterDto textualRangeMasterDto) throws ApplicationException;
	
	public Response getTextualRangeMasterByNameNotId(TextualRangeMasterDto textualRangeMasterDto) throws ApplicationException;
}
