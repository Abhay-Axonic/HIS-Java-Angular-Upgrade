package com.param.pharmacy.master.unit.dao.rack;

import java.util.List;

import com.param.entity.model.master.Rack;
import com.param.entity.model.master.Shelf;
import com.param.entity.model.master.Store;

public interface IRackDao {

	List<Rack> getAll(Integer unitId, Integer orgId);

	Rack getById(Integer id);

	Integer create(Rack rack);
	
	Integer update(Rack rack);

	Store getStore(Integer storeId);

	void create(Shelf shelf);

	Rack getRack(Integer id);

	List<Shelf> getShelf(Integer id);

	Rack updateRack(Rack rack);

	List<Rack> getRackByStoreId(Integer storeId);
}
