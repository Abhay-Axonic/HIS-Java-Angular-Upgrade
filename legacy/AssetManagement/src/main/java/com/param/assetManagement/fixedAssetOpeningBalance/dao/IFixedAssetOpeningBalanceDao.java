package com.param.assetManagement.fixedAssetOpeningBalance.dao;

import java.util.List;

import com.param.assetManagement.fixedAssetOpeningBalance.dto.FixedAssetOpeningBalanceRequest;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.BatchAccessoriesMapper;
import com.param.entity.model.inventory.OpeningBalance;
import com.param.entity.model.inventory.OpeningBalanceAccessories;
import com.param.entity.model.inventory.OpeningBalanceDetail;
import com.param.entity.model.inventory.StockTransaction;
import com.param.entity.model.inventory.StoreStock;

public interface IFixedAssetOpeningBalanceDao {

	List<OpeningBalance> searchOpeningBalance(FixedAssetOpeningBalanceRequest request, Integer screenId);
	
	OpeningBalance getOpeningBalace(Integer id);

	List<OpeningBalanceDetail> getOpeningbalaceDetail(Integer openingBalaceId);

	OpeningBalance saveOpeningBalance(OpeningBalance openingBalance);
	
	OpeningBalance updateOpeningBalance(OpeningBalance openingBalance);
	
	Batch saveBatch(Batch batch);
	
	BatchAccessoriesMapper saveBatchAccessoriesMapper(BatchAccessoriesMapper batchAccessoriesMapper);
	
	StoreStock saveStoreStock(StoreStock storeStock);

	StockTransaction saveStockTransaction(StockTransaction stockTransaction);

	List<OpeningBalanceAccessories> getAccessoriesData(Integer id);

	Integer updateBatchVendorId(Batch batch, Integer id);
}