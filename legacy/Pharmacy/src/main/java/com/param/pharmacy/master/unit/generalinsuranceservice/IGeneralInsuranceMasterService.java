package com.param.pharmacy.master.unit.generalinsuranceservice;

import java.util.List;
import com.param.common.dto.CommonResponse;
import com.param.pharmacy.master.global.CustomeException;
import com.param.pharmacy.master.unit.genralinsurancedto.GeneralInsuranceDetailsResponse;
import com.param.pharmacy.master.unit.genralinsurancedto.GeneralInsuranceRequest;
import com.param.pharmacy.master.unit.genralinsurancedto.GenralInsuranceListResponse;

public interface IGeneralInsuranceMasterService {

	
	List<GenralInsuranceListResponse> getAll();

	CommonResponse save(GeneralInsuranceRequest generalInsuranceRequest);

	GeneralInsuranceDetailsResponse getById(Integer id);
	
	CommonResponse update(GeneralInsuranceRequest generalInsuranceRequest) throws CustomeException;

}
