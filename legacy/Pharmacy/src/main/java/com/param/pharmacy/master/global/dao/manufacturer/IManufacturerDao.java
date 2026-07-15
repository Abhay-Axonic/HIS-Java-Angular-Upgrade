package com.param.pharmacy.master.global.dao.manufacturer;

import java.util.List;

import com.param.entity.model.master.Manufacturer;
import com.param.pharmacy.master.global.dto.manufacturer.ManufacturerListResponse;

public interface IManufacturerDao {
	
	List<ManufacturerListResponse> getAll();
	
	Manufacturer getById(Integer id, Integer orgId);
	
	Integer create(Manufacturer manufacturer);
	
	Integer update(Manufacturer manufacturer);

	Long getManufacturerCountFromItem(Integer id);

	Long getManufacturerCountFromVendor(Integer id);


}
