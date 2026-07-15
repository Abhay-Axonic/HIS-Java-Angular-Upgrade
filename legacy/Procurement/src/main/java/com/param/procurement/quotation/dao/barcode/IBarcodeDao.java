package com.param.procurement.quotation.dao.barcode;

import java.util.Date;
import java.util.List;

import com.param.global.dto.items.ItemsDetail;
import com.param.global.dto.items.ItemsRequest;
import com.param.procurement.quotation.dto.barcode.BarcodeBatchListResponse;

public interface IBarcodeDao {

	List<BarcodeBatchListResponse> GetBatchList(ItemsRequest itemRequest);

	List<BarcodeBatchListResponse> GetBatcByFixedAssetTypeList(ItemsRequest itemRequest);

	List<ItemsDetail> savePrePrintedBarcode(ItemsRequest itemRequest);

	boolean isManualBarcodeRequired(Integer unitId);

	List<BarcodeBatchListResponse> searchPrePrintedBarcode(ItemsRequest itemRequest);

	List<BarcodeBatchListResponse> getPrePrintedBarcode(ItemsRequest itemRequest);

	boolean isPRNRequired(Integer unitId);

	String pharmacyPRNFormat(Integer unitId);

	List<BarcodeBatchListResponse> getGrnInfo(Integer itemId, String batchNo, Date expiry, String lotNo);

	List<BarcodeBatchListResponse> getGrnInfoAll(List<Integer> itemIds, List<String> batchNos, List<Date> allExpiries,
			List<String> allLotNos);

    Integer updatePrintedCount(List <ItemsRequest> itemRequestList);

    List<BarcodeBatchListResponse> getUpdatedPrintedCount(List <Long> batchIds);

}
