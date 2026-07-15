package com.param.pharmacy.master.unit.dao.fixedAssetType;

import java.util.List;

import com.param.entity.model.master.FixedAssetType;

public interface IFixedAssetTypeDao {

	List<FixedAssetType> getAll();

	FixedAssetType getById(Integer id);

	Integer create(FixedAssetType fixedAssetType);

	Integer update(FixedAssetType fixedAssetType);

}
