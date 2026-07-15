package com.param.procurement.quotation.dao.physicalInspection;

import java.util.List;

import com.param.entity.model.inventory.Batch;
import com.param.entity.model.procurement.PhysicalInspection;
import com.param.entity.model.procurement.PhysicalInspectionDetail;
import com.param.entity.model.procurement.PurchaseOrderDetail;
import com.param.entity.model.procurement.ShelfItem;
import com.param.global.dto.items.BatchResponse;
import com.param.procurement.quotation.dto.physicalInspection.PhysicalInspectionRequest;
import com.param.procurement.quotation.dto.physicalInspection.PhysicalInspectionSave;

public interface IPhysicalInspectionDao {

	List<PhysicalInspection> getPhysicalnspectionList(PhysicalInspectionRequest physicalInspectionRequest, Integer screenId);
	
	PhysicalInspection getPhysicalnspectionDetailsForSearch(PhysicalInspectionRequest physicalInspectionRequest);
	
	PhysicalInspection getPhysicalnspectionDetailsForSearch(PhysicalInspectionSave physicalInspectionSave);
	
	PhysicalInspection getPhysicalnspectionDetails(Integer id);
	
	PhysicalInspection savePhysicalnspection(PhysicalInspection physicalInspection);

	PhysicalInspectionDetail getPhysicalInspectionDetailById(Integer id,Integer unitId);
	
	List<PhysicalInspectionDetail> getPhysicalInspectionDetailByInspectionId(Integer id , Integer unitId);
	
	/*added by minesh*/
	List<Batch> getBatchListFromItem(Integer itemNum,String batchNum,String lotNum , Integer unitId);
	
	ShelfItem saveShelfItem(ShelfItem shelfItem);
	
	Batch updateBatchQuantityAndFlag(Batch batch);

	PurchaseOrderDetail getPoDetails(Integer id);

	PhysicalInspection getPhysicalnspectionDetailsForAuto(Integer id, Integer unitId, Integer storeId);

	Integer getGrnSize(Integer poId, List<Integer> poDetailsId);

	Long getBatchId(Integer id);

	void updateStockTransaction(Integer id,Integer integer, Integer j, Integer screenId, String screenCode, Boolean intrasit, Integer rejectedQuantity,boolean phyFlag);

	Integer getGrnApproveedQty(Integer id);

	List<Batch> getBatchIds(Integer id);
	
	void updateStockTransactionForBonusBatch(Integer id,Integer integer, Integer j, Integer screenId, String screenCode, Boolean intrasit, Integer rejectedQuantity,Integer batchId);
}
