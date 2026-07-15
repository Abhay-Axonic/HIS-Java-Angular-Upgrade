package com.param.procurement.quotation.dao.physicalInspection;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.Query;

import com.param.entity.model.inventory.Batch;
import com.param.entity.model.procurement.ConsignmentStockReturn;
import com.param.entity.model.procurement.ConsignmentStockReturnDetail;
import com.param.entity.model.procurement.PhysicalInspection;
import com.param.procurement.purchase.dto.goodReceiptNote.GRNDetailResponse;
import com.param.procurement.purchase.dto.goodReceiptNote.GRNResponse;
import com.param.procurement.quotation.dto.consignmentStockReturn.ConsignmentStockReturnDetailHistory;
import com.param.procurement.quotation.dto.consignmentStockReturn.ConsignmentStockReturnDetailResponse;
import com.param.procurement.quotation.dto.consignmentStockReturn.ConsignmentStockReturnDetailSaveRequest;
import com.param.procurement.quotation.dto.physicalInspection.ConsignmentStockReturnRequest;
import com.param.procurement.quotation.dto.physicalInspection.ConsignmentStockReturnResponse;

public interface IConsignmentStockReturnDao {

	List<ConsignmentStockReturnResponse> getConsignmentStockListResponse(ConsignmentStockReturnRequest consignmentStockReturnRequest, Integer screenId);
	
	List<ConsignmentStockReturnDetailSaveRequest> getAllGoodReceiptNoteCR(ConsignmentStockReturnRequest consignmentStockReturnRequest);
	ConsignmentStockReturnDetailSaveRequest getGoodReceiptNoteCR(ConsignmentStockReturnRequest consignmentStockReturnRequest);
	
	List<GRNResponse> getGoodReceiptNoteBPList(ConsignmentStockReturnRequest consignmentStockReturnRequest);
	
	List<GRNDetailResponse> getGoodReceiptNoteDetailsBP(ConsignmentStockReturnRequest consignmentStockReturnRequest);
	List<ConsignmentStockReturnDetailResponse> getGoodReceiptNoteDetailsCR(ConsignmentStockReturnRequest consignmentStockReturnRequest);
	
	ConsignmentStockReturn saveConsignmentStockReturn(ConsignmentStockReturn consignmentStockReturn);
		
	ConsignmentStockReturn getConsignmentStockReturn(Integer id, Integer unitId);
	
	ConsignmentStockReturnDetail getConsignmentStockReturnDetail(Integer id , Integer unitId);	
	List<ConsignmentStockReturnDetail> getConsignmentStockReturnDetails (Integer id , Integer unitId);
	
	/*added by minesh*/
	List<Batch> getBatchListFromItem(Integer itemNum, String batchNum, String lotNum , Integer unitId);
	List<Batch> getSameBatchList(Integer itemId, String batchNum, String lotNum, Integer unitId);
	ConsignmentStockReturnDetail updateconsignmentStockReturnDetail(ConsignmentStockReturnDetail consignmentStockReturnDetail);
	ConsignmentStockReturn updateconsignmentStockReturn(ConsignmentStockReturn consignmentStockReturn);

	void updateBatch(Long batchId, Integer returnQuantity);

	void updateBatchData(Long id, Integer returnBatchQuantity);

	List<Batch> getBatchList(String batchNo, String lotNo);

	void updateStoreStock(String batchNo, String lotNo, Integer returnQtyFromUi);
	//added by minesh
	void updateStoreStock(String batchNo, String lotNo, Integer returnQtyFromUi,Long batchId,Integer storeId);

	void updateConsignmentTrueBatchData(Long id, Integer returnQtyFromUi);

	void updateConsignmentFalseBatchData(Long id, Integer currentQuantity);

	void updateConsignmentFalseStoreStock(String batchNo, String lotNo, Integer returnQtyFromUi);
	//added by minesh
	void updateConsignmentFalseStoreStock(String batchNo, String lotNo, Integer returnQtyFromUi,Long batchId,Integer storeId);

	Batch getConsignmentBatch(Integer itemId,String batchNo, String lotNo);
	
	List<ConsignmentStockReturnDetailHistory> getConsignmentStockreturnDetailHistory(Integer grnDetailId,Integer crId , Integer unitId);
	
	BigInteger getTotalPOCreatedQuantity(Integer grndId);

	Integer getconsignmentReturnedQuantity(ConsignmentStockReturnDetailResponse grnDetailResponse);
}
