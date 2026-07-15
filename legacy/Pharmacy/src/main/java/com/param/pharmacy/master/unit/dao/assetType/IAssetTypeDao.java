package com.param.pharmacy.master.unit.dao.assetType;

import java.util.List;

import com.param.entity.model.master.AssetType;
import com.param.pharmacy.master.unit.dto.assetType.AssetTypeListResponse;

public interface IAssetTypeDao {

	List<AssetType> getAll();

	AssetType getById(Integer id);

	Integer create(AssetType assetType);

	Integer update(AssetType assetType);

	List<AssetTypeListResponse> getAllAssetTypesBetterPerformance();

}
