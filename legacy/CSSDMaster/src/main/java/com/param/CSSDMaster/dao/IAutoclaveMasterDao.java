package com.param.CSSDMaster.dao;

import com.param.CSSDMaster.dto.AutoclaveMasterDto;
import com.param.global.common.Response;

@SuppressWarnings("rawtypes")
public interface IAutoclaveMasterDao {

	Response updateAutoclave(AutoclaveMasterDto masterDto);
	
	Response updateAutoclaveStatus(AutoclaveMasterDto masterDto);

	Response getAllAutoclaveList(Integer organizationId,Integer unitId,Integer offset,Integer noOfRecordsPerPage);
	
	Response getActiveAutoclaveList(Integer organizationId,Integer unitId);

	Response createAutoclave(AutoclaveMasterDto masterDto);

	Response getAutoclavesListcount(AutoclaveMasterDto autoclaveMasterDto);

	Response getActiveSterilizationColorList(AutoclaveMasterDto autoclaveMasterDto);

}
