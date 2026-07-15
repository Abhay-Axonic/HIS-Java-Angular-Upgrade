package com.param.lis.microbiology.transaction.service;

import javax.transaction.Transactional;

import com.param.lis.global.common.Response;
import com.param.lis.transaction.dto.MicrobiologyTemplateDto;

import in.param.exception.ApplicationException;

@Transactional
@SuppressWarnings("rawtypes")
public interface IMicrobiologyTemplateService {

	Response saveTemplate(MicrobiologyTemplateDto microbiologyTemplateDto)throws ApplicationException;

	Response  getTemplate(MicrobiologyTemplateDto microbiologyTemplateDto)throws ApplicationException;

}
