package com.param.pharmacy.master.global.dao.unitOfMeasurement;

import java.util.List;

import com.param.entity.model.master.Item;
import com.param.entity.model.master.UnitOfMeasurement;
import com.param.global.dto.items.UomResponse;
import com.param.pharmacy.master.global.dto.unitOfMeasurement.UnitOfMeasurementListResponse;

public interface IUnitOfMeasurementDao {

//	List<UnitOfMeasurement> getAll();
	
	//List<UnitOfMeasurementListResponse> getAll();

	UnitOfMeasurement getById(Integer id, Integer orgId);
	
	List<UnitOfMeasurementListResponse> getByItemId(Integer itemId, Integer orgId);
			
	UnitOfMeasurement getUOMByItem(Integer id,Integer UnitTypeId, Integer orgId);

	Integer create(UnitOfMeasurement unitOfMeasurement);

	Integer update(UnitOfMeasurement unitOfMeasurement);

	Item getItem(Integer itemId);

	List<UnitOfMeasurement> getAllByItemId(Integer itemId, Integer orgId);
	
	public List<UnitOfMeasurement> getAll();

	List<UnitOfMeasurementListResponse> getAllUoms(Integer orgId, Integer offset, Integer noOfRecordsPerPage, String itemName);

	List<UomResponse> getAllByItemIds(List<Integer> itemIds);

	Integer getAllUomsCount(Integer orgId);


//	List<UnitOfMeasurementListResponse> getByitemId(Integer itemId);

}
