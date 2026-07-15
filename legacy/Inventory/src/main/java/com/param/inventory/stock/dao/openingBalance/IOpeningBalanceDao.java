package com.param.inventory.stock.dao.openingBalance;

import java.math.BigInteger;
import java.util.List;

import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.OpbUomMapper;
import com.param.entity.model.inventory.OpeningBalShelfItem;
import com.param.entity.model.inventory.OpeningBalance;
import com.param.entity.model.inventory.OpeningBalanceDetail;
import com.param.entity.model.inventory.StockTransaction;
import com.param.entity.model.inventory.StoreStock;
import com.param.entity.model.procurement.ShelfItem;
import com.param.global.dto.items.UomResponse;
import com.param.inventory.stock.dto.openingBalance.ItemResponse;
import com.param.inventory.stock.dto.openingBalance.OpeningBalanceDetailsResponse;
import com.param.inventory.stock.dto.openingBalance.OpeningBalanceRequest;
import com.param.inventory.stock.dto.openingBalance.OpeningBalanceResponse;
import com.param.inventory.stock.dto.openingBalance.StorageDetails;
import com.param.inventory.stock.dto.openingBalance.StorageLocationDetails;

public interface IOpeningBalanceDao {

	List<OpeningBalance> searchOpeningBalance(OpeningBalanceRequest request);

	OpeningBalance saveOpeningBalance(OpeningBalance openingBalance);

	OpeningBalance getOpeningBalace(Integer id);

	List<OpeningBalanceDetail> getOpeningbalaceDetail(Integer openingBalaceId);

	List<OpbUomMapper> getOpbUomMapperList(Integer id);

	OpeningBalance updateOpeningBalance(OpeningBalance openingBalance);

	Batch saveBatch(Batch batch);

	StoreStock saveStoreStock(StoreStock storeStock);

	StockTransaction saveStockTransaction(StockTransaction stockTransaction);
	
	public List<OpeningBalanceResponse> searchOpeningBalanceBetterPerformance(OpeningBalanceRequest request);
	
	ShelfItem saveShelfItem(ShelfItem shelfItem);

	OpeningBalShelfItem saveOpeningBalShelfItem(OpeningBalShelfItem shelfItem);

	List<StorageLocationDetails> getStogareLocation(Integer id);

	List<OpeningBalShelfItem> getStorageLocationById(Integer id);

	OpeningBalanceDetailsResponse getOpeningBalacePT(Integer id);

	List<ItemResponse> getOpeningbalaceDetailPT(Integer id);

	List<StorageDetails> getStogareLocationPT(List<Integer> collect);

	List<UomResponse> getOpbUomMapperListPT(List<Integer> opbDtlIds);

	Integer getBatchId(String batchNo,  Integer unitId);

}
