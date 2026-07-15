package com.param.pharmacy.master.unit.dao.storeItemMapper;

import java.math.BigInteger;
import java.util.List;

import com.param.common.dto.CommonResponse;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.StoreItemMapper;
import com.param.pharmacy.master.unit.dto.MultipleStoreRequest;
import com.param.pharmacy.master.unit.dto.StoreStockRequestDate;
import com.param.pharmacy.master.unit.dto.StoreStockResponse;
import com.param.pharmacy.master.unit.dto.storeItemMapper.StoreItemMapperListResponse;

public interface IStoreItemMapperDao {

	List<StoreItemMapper> getAll(Integer storeId);

	StoreItemMapper getById(BigInteger id);

	BigInteger create(StoreItemMapper storeItemMapper);
	
//	BigInteger update(StoreItemMapper storeItemMapper);

	Store getStore(Integer storeId);

	Item getItem(Integer itemId);

	List<StoreItemMapper> update();

	BigInteger update(StoreItemMapper storeItemMapper);

	List<StoreItemMapperListResponse> getListBetterPerformance(Integer storeId);

	CommonResponse GetMultipleStore(StoreItemMapperListResponse request);

	List<StoreStockResponse> checkStock(StoreStockRequestDate req);

	List<StoreItemMapperListResponse> getStoreItemMappedList(Integer itemId);

	List<StoreItemMapperListResponse> getStoreItemMappedListForItemMasterNew(Integer id, Integer unitId);

	List<StoreItemMapperListResponse> GetTransactionCount(List<MultipleStoreRequest> request, BigInteger itemId);

}
