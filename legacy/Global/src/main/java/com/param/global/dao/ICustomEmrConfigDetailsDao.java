package com.param.global.dao;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.dto.CustomEMRConfigurationDetailsDto;
@Transactional
public interface ICustomEmrConfigDetailsDao {
	public Response getCustomEMRCategorySubcategoryList(CustomEMRConfigurationDetailsDto customEMRConfigurationDetailsDto);

	public Response updateCustomEMRCategorySubcategory(CustomEMRConfigurationDetailsDto customEMRConfigurationDetailsDto);

	public Response getAllListoCustomEMRByPatientId(CustomEMRConfigurationDetailsDto customEMRConfigurationDetailsDto);

	public Response checkAvailableCustomData(CustomEMRConfigurationDetailsDto customEMRConfigurationDetailsDto);

}
