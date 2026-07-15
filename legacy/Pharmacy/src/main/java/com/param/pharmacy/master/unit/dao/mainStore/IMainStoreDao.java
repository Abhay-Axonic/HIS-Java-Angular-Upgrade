package com.param.pharmacy.master.unit.dao.mainStore;

import java.util.List;

import com.param.entity.model.master.Store;
import com.param.entity.model.master.StoreType;
import com.param.entity.model.master.VisitType;

public interface IMainStoreDao {

	List<Store> getAll(Integer unitId, Integer orgId);

	Store getById(Integer id);

	Integer create(Store store);

	Integer update(Store store);

	StoreType getStoretype(Integer storeTypeId);

	VisitType getVisitType(Integer visitTypeId);
}
