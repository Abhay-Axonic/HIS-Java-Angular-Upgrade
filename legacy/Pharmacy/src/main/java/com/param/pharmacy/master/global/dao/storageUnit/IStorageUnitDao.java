package com.param.pharmacy.master.global.dao.storageUnit;

import java.util.List;

import com.param.entity.model.master.StorageUnit;

public interface IStorageUnitDao {

	List<StorageUnit> getAll(Integer orgId);

	StorageUnit getById(Integer id);

	Integer create(StorageUnit storageUnit);

	Integer update(StorageUnit storageUnit);

	Long getStorageUnitCountFromItem(Integer id);

}
