package com.param.global.dao;

import com.param.global.common.Response;
import com.param.global.dto.CoversheetTemplateMasterDto;
import com.param.global.dto.NewEMRTemplateDto;

public interface INewEMRTemplateDao {


	public Response checkAvailableCoversheetAsTemaplate(NewEMRTemplateDto newEMRTemplateDto);

	public Response saveNewEMRTemplate(CoversheetTemplateMasterDto coversheetTemplateMasterDto);

}
