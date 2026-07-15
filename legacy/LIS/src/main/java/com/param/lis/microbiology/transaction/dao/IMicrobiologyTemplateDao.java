package com.param.lis.microbiology.transaction.dao;

import com.param.lis.global.common.Response;
import com.param.lis.global.common.SearchDto;
import com.param.lis.transaction.dto.MicrobiologyTemplateDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IMicrobiologyTemplateDao {

	Response saveTemplate(MicrobiologyTemplateDto microbiologyTemplateDto)throws ApplicationException;

	Response getTemplate(MicrobiologyTemplateDto microbiologyTemplateDto)throws ApplicationException;

	public Response updatetMcrobiologyTemplate(SearchDto searchDto)throws ApplicationException;
}
