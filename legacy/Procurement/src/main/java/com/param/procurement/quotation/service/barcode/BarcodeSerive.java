package com.param.procurement.quotation.service.barcode;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.procurement.quotation.dao.barcode.BarcodeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.constants.Constants;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dao.items.IItemsDao;
import com.param.global.dto.items.ItemsDetail;
import com.param.global.dto.items.ItemsRequest;
import com.param.global.dto.items.UomResponse;
import com.param.global.utility.Util;
import com.param.procurement.quotation.dao.barcode.IBarcodeDao;
import com.param.procurement.quotation.dto.barcode.BarcodeBatchListResponse;

@Service
public class BarcodeSerive implements IBarcodeService {

	@Autowired
	IItemsDao ItemsDao;

	@Autowired
	IGlobalDao GlobalDao;

	@Autowired
	IBarcodeDao BarcodeDao;

	@Override
	@Transactional
	public List<BarcodeBatchListResponse> getBatchList(ItemsRequest itemRequest) {
		List<BarcodeBatchListResponse> response = null;
		List<BarcodeBatchListResponse> grnRes = null;
		boolean isManualBarcodeRequired=BarcodeDao.isManualBarcodeRequired(itemRequest.getUnitId());
		boolean isPRNRequired=BarcodeDao.isPRNRequired(itemRequest.getUnitId());
		String pharmacyPRNFormat=BarcodeDao.pharmacyPRNFormat(itemRequest.getUnitId());
		if(itemRequest.getAssetTypeId().equals(Constants.getConsumableId())) {
		 response = BarcodeDao.GetBatchList(itemRequest);
		    List<Integer> itemIds = response.stream().map(BarcodeBatchListResponse::getItemId).collect(Collectors.toList());
		    List<String> batchNos = response.stream().map(BarcodeBatchListResponse::getBatchNo).collect(Collectors.toList());
		    List<Date> allExpiries = response.stream().map(BarcodeBatchListResponse::getExpiry).collect(Collectors.toList());
		    List<String> allLotNos = response.stream().map(BarcodeBatchListResponse::getLotNo).collect(Collectors.toList());
		    batchNos.replaceAll(String::toUpperCase);
		    List<BarcodeBatchListResponse> grnResAll=BarcodeDao.getGrnInfoAll(itemIds,batchNos,allExpiries,allLotNos);
			for (BarcodeBatchListResponse batchRes : response) {
				
	            Stream<BarcodeBatchListResponse> allLotNosStream = grnResAll.stream();
	            grnRes=allLotNosStream
	            .filter(str -> str.getItemId().equals(batchRes.getItemId()) && str.getBatchNo().equals(batchRes.getBatchNo())
	            		&& str.getExpiry().equals(batchRes.getExpiry()))
	            .collect(Collectors.toList());
				if(grnRes.size()>0 && grnRes != null){
					batchRes.setGrnNo(grnRes.get(0).getGrnNo() != null ? grnRes.get(0).getGrnNo() :"-");
					batchRes.setVendorName(grnRes.get(0).getVendorName() != null ? grnRes.get(0).getVendorName() :"-");
					batchRes.setGrnDate(grnRes.get(0).getGrnDate() != null ? grnRes.get(0).getGrnDate() :"-");
				}
				else{
					batchRes.setGrnNo("-");
					batchRes.setVendorName("-");
					batchRes.setGrnDate("-");
				}
			}
	        List<Long> batchIds = response.stream().map(BarcodeBatchListResponse::getBatchId).collect(Collectors.toList());
	        List<UomResponse> uomListAll = GlobalDao.getBatchUomListAll(batchIds);
		for (BarcodeBatchListResponse batch : response) {
            Stream<UomResponse> uomListStream = uomListAll.stream();
            List<UomResponse> uomList=uomListStream
            .filter(str -> str.getBatchId().equals(batch.getBatchId()))
            .collect(Collectors.toList());
			if (uomList != null && !uomList.isEmpty()) {
				UomResponse sUom = Util.getStoreUom(uomList);
				Integer conversion = Util.getStoreUomConversion(uomList);
				if (sUom != null) {
				batch.setUom(sUom.getUomTypeId(), sUom.getUomType(), sUom.getUomUnitId(), sUom.getUomUnit());
//				batch.setUom(sUom.getUomTypeId(), sUom.getUomType(), sUom.getUomUnitId(), sUom.getUomUnit());
				batch.setMrp(conversion * batch.getMrp());
				batch.setAvailableQuantity(batch.getAvailableQuantity() / conversion);
				}
				batch.setPrePrintedBarcode(batch.getPrePrintedBarcode()!=null ? batch.getPrePrintedBarcode() :"");
				
			}
			batch.setIsManualBarcodeRequired(isManualBarcodeRequired);
			batch.setIsPRNRequired(isPRNRequired);
			batch.setPharmacyPRNFormat(pharmacyPRNFormat!=null ? pharmacyPRNFormat : "");
		  }
		}
		else if(itemRequest.getAssetTypeId().equals(Constants.getFixedAssetId())) {
			response = BarcodeDao.GetBatcByFixedAssetTypeList(itemRequest);
			for (BarcodeBatchListResponse batch : response) {
				batch.setIsManualBarcodeRequired(isManualBarcodeRequired);
				batch.setIsPRNRequired(isPRNRequired);
				batch.setPharmacyPRNFormat(pharmacyPRNFormat!=null ? pharmacyPRNFormat : "");
			}
		}

		return response;
	}

	@Override
	@Transactional
	public List<ItemsDetail> savePrePrintedBarcode(ItemsRequest itemRequest) {
        return BarcodeDao.savePrePrintedBarcode(itemRequest);
	}

	@Override
	@Transactional
	public List<BarcodeBatchListResponse> searchPrePrintedBarcode(ItemsRequest itemRequest) {
		List<BarcodeBatchListResponse> response = null;
		response = BarcodeDao.searchPrePrintedBarcode(itemRequest);
		for (BarcodeBatchListResponse batch : response) {
			batch.setPrePrintedBarcode(batch.getPrePrintedBarcode());
			batch.setBatchId(batch.getBatchId());
			batch.setItemId(batch.getItemId());
		}
		return response;
	}
	@Override
	@Transactional
	public List<BarcodeBatchListResponse> getPrePrintedBarcode(ItemsRequest itemRequest) {
		List<BarcodeBatchListResponse> response = null;
        boolean isManualBarcodeRequired=BarcodeDao.isManualBarcodeRequired(itemRequest.getUnitId());
		response = BarcodeDao.getPrePrintedBarcode(itemRequest);
		for (BarcodeBatchListResponse batch : response) {
			batch.setPrePrintedBarcode(batch.getPrePrintedBarcode());
			batch.setIsManualBarcodeRequired(isManualBarcodeRequired);
			batch.setBatchId(batch.getBatchId());
		}
		return response;
	}

	@Override
	@Transactional
	public Response updatePrintedCount(List <ItemsRequest> itemRequestList) {
		List<BarcodeBatchListResponse> response = null;

		List<Long> batchIds = itemRequestList.stream().map(ItemsRequest::getBatchId)
				.filter(Objects::nonNull).collect(Collectors.toList());

		Integer issueSaveStatus = BarcodeDao.updatePrintedCount(itemRequestList);

		response= BarcodeDao.getUpdatedPrintedCount(batchIds);

		return new Response<>("success", "200", null, response, issueSaveStatus);
	}

}