package com.param.procurement.quotation.dao.physicalInspection;

import java.math.BigInteger;
import java.util.List;

import com.param.entity.model.inventory.Batch;
import com.param.entity.model.procurement.PhysicalInspection;
import com.param.entity.model.procurement.PhysicalInspectionDetail;
import com.param.entity.model.procurement.ShelfItem;
import com.param.global.dto.AllDataResponse;
import com.param.global.dto.items.BatchResponse;
import com.param.procurement.quotation.dto.physicalInspection.PhysicalInspectionRequest;
import com.param.procurement.quotation.dto.physicalInspection.PhysicalInspectionSave;

public interface IConsignmentStockConversionDao {

	List<PhysicalInspection> getPhysicalnspectionList(PhysicalInspectionRequest physicalInspectionRequest, Integer screenId);
	
	PhysicalInspection getPhysicalnspectionDetailsForSearch(PhysicalInspectionRequest physicalInspectionRequest);
	
	PhysicalInspection getPhysicalnspectionDetailsForSearch(PhysicalInspectionSave physicalInspectionSave);
	
	PhysicalInspection getPhysicalnspectionDetails(Integer id);
	
	PhysicalInspection savePhysicalnspection(PhysicalInspection physicalInspection);

	PhysicalInspectionDetail getPhysicalInspectionDetailById(Integer id);
	
	List<PhysicalInspectionDetail> getPhysicalInspectionDetailByInspectionId(Integer id , Integer unitId);
	
	/*added by minesh*/
	List<Batch> getBatchListFromItem(Integer itemNum,String batchNum,String lotNum , Integer unitId);
	
	ShelfItem saveShelfItem(ShelfItem shelfItem);
	
	Batch updateBatchQuantityAndFlag(Batch batch);

	Batch getBatch(Long long1);

	BigInteger getBatchReturnQty(Long batchId, Integer unitId);

	Integer getAvailableConsignmentStock(Long batchId);

	List<AllDataResponse> allDataList(Integer storeId, Integer batchId , String batchNo, String lotNo);

	BigInteger getStoreStockTransfered(Long batchId, Integer grnStoreId);

	BigInteger getConsumedQty(String batchNo);
	//added by minesh
	BigInteger getConsumedQtyWithReturnedQty(String batchNo);
	BigInteger getConsumedQtyWithReturnedQty(String batchNo, String lotNo);

	BigInteger getconvertedConsignmentStock(String batchNo);
	BigInteger getconvertedConsignmentStock(String batchNo,String lotNo);

	Integer getTotalConsignmentStock(Integer id);

	Integer getTotalConsignmentStockFromBatch(String batchNo, String lotNo);

	Integer getTotalStock(String batchno);

	BigInteger getAvailableStock(String batchno, String lotno, Boolean isconsignment,int storeId, int itemId, Integer unitId);

	Integer getConsignmentReturnQty(String batchNo, String lotNumber, Integer id);
	BigInteger getTotalConsignmentReturnQtyAgainGRNDetailId(Integer grnDetailId,  Integer unitId);

	BigInteger getAvailableWithoutLotStock(String batchno, Boolean isconsignment, int storeId, int itemId, Integer unitId);
	BigInteger getAvailableStockWithoutLotStock(Integer  batchId, Boolean isconsignment, int storeId, int itemId, Integer unitId);

	BigInteger getAvailableWithoutBatchStock(String lotno, boolean isConsignmentFlag, int storeId, int itemId, Integer unitId);
	
	BigInteger getTotalGrnApprovedQty(String batchno,String lotno, boolean isConsignmentFlag, int storeId, int itemId);
	BigInteger getTotalStoreTotalQty(String batchno,String lotno, boolean isConsignmentFlag, int storeId, int itemId, Integer unitId);
	
	BigInteger getAvailableBatchStock(String batchno,String lotno, int storeId, int itemId, Integer unitId);

	BigInteger getAvailableWithBatchIdStock(Long long1, boolean isConsignmentFlag, Integer storeId, Integer id, Integer unitId);

	BigInteger getStoreStockTransferedByLotNo(String lotNo, Integer grnStoreId);
}
