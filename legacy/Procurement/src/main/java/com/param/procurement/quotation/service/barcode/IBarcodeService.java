package com.param.procurement.quotation.service.barcode;

import java.util.List;

import org.springframework.util.MultiValueMap;
import com.param.global.common.Response;

import com.param.global.dto.items.BatchListResponse;
import com.param.global.dto.items.ItemsDetail;
import com.param.global.dto.items.ItemsRequest;
import com.param.procurement.quotation.dto.barcode.BarcodeBatchListResponse;

public interface IBarcodeService {

	List<BarcodeBatchListResponse> getBatchList(ItemsRequest itemRequest);

	List<ItemsDetail> savePrePrintedBarcode(ItemsRequest itemRequest);

	List<BarcodeBatchListResponse> searchPrePrintedBarcode(ItemsRequest itemRequest);

	List<BarcodeBatchListResponse> getPrePrintedBarcode(ItemsRequest itemRequest);

    Response updatePrintedCount(List <ItemsRequest> itemRequestList);
}
