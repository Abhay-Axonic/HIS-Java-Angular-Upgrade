package com.param.pharmacy.master.global.service.manufacturer;

import java.util.List;

import com.param.common.dto.CommonResponse;
import com.param.pharmacy.master.global.CustomeException;
import com.param.pharmacy.master.global.dto.manufacturer.ManufacturerDetailsResponse;
import com.param.pharmacy.master.global.dto.manufacturer.ManufacturerListResponse;
import com.param.pharmacy.master.global.dto.manufacturer.ManufacturerRequset;

public interface IManufacturerService {

	List<ManufacturerListResponse> getAll();
	
	ManufacturerDetailsResponse getById(Integer id, Integer orgId);
	
	CommonResponse create(ManufacturerRequset manufacturerRequset);
	
	CommonResponse update(ManufacturerRequset manufacturerRequset) throws CustomeException;

	Object GetPrefixById(Integer orgId, Integer screenId);
}
