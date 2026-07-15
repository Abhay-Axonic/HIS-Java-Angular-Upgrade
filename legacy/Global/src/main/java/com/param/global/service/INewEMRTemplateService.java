package com.param.global.service;

import com.param.global.common.Response;
import com.param.global.dto.CoversheetTemplateMasterDto;
import com.param.global.dto.NewEMRTemplateDto;

public interface INewEMRTemplateService {

	public Response saveNewEMRTemplate(CoversheetTemplateMasterDto coversheetTemplateMasterDto);

}
