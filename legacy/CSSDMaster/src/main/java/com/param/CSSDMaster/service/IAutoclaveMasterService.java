package com.param.CSSDMaster.service;

import com.param.CSSDMaster.dto.AutoclaveMasterDto;
import com.param.global.common.Response;

@SuppressWarnings("rawtypes")
public interface IAutoclaveMasterService {

	Response getAllMasterList(Integer organizationId,Integer unitId,Integer offset,Integer noOfRecordsPerPage);
	
	Response getActiveMasterList(Integer organizationId,Integer unitId);

	Response updateMaster(AutoclaveMasterDto masterDto);
	
	Response updateAutoclaveStatus(AutoclaveMasterDto masterDto);

	Response createMaster(AutoclaveMasterDto masterDto);

	Response getAutoclavesListcount(AutoclaveMasterDto autoclaveMasterDto);

	Response getActiveSterilizationColorList(AutoclaveMasterDto autoclaveMasterDto);

}
