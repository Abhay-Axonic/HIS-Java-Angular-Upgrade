package com.param.global.service;

import com.param.global.common.Response;
import com.param.global.dto.CustomEMRConfigurationDetailsDto;

public interface ICustomEmrConfigDetailsService {

	public Response getCustomEMRCategorySubcategoryList(CustomEMRConfigurationDetailsDto customEMRConfigurationDetailsDto);

	public Response updateCustomEMRCategorySubcategory(CustomEMRConfigurationDetailsDto customEMRConfigurationDetailsDto);

	public Response getAllListoCustomEMRByPatientId(CustomEMRConfigurationDetailsDto customEMRConfigurationDetailsDto);

}
