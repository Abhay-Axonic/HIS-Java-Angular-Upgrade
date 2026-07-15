package com.param.procurement.purchase.dao.purchaseOrder;

import java.math.BigInteger;
import java.util.List;

import com.param.entity.model.procurement.BonusUomMapper;
import com.param.entity.model.procurement.PoAccessoriesMapper;
import com.param.entity.model.procurement.PurchaseOrder;
import com.param.entity.model.procurement.PurchaseOrderDetail;
import com.param.entity.model.procurement.PurchaseOrderOtherChargesMapper;
import com.param.entity.model.procurement.PurchaseOrderStaged;
import com.param.entity.model.procurement.PurchaseOrderUomMapper;
import com.param.entity.model.procurement.PurchaseRequestDetail;
import com.param.procurement.purchase.consignmentstockpo.dto.ConsignmentStockPOHistoryDto;
import com.param.procurement.purchase.dto.purchaseOrder.PoOtherChargesMapperDto;
import com.param.procurement.purchase.dto.purchaseOrder.PoPurchaseRequestArray;
import com.param.procurement.purchase.dto.purchaseOrder.PurchaseOrderStagedDto;
import com.param.procurement.purchase.dto.purchaseOrder.PurchaseOrderUomMapperDto;
import com.param.procurement.purchase.purchaseOrder.dto.LastPurchaseOrderDto;
import com.param.procurement.purchase.purchaseOrder.dto.PurchaseOrderDetailsRespDto;
import com.param.procurement.purchase.purchaseOrder.dto.PurchaseOrderMasterDetailsDto;
import com.param.procurement.purchase.purchaseOrder.dto.PurchaseOrderMasterDto;
import com.param.procurement.purchase.purchaseOrder.dto.QrDateDto;

public interface IPurchaseOrderDao {
	
	public List<PurchaseOrder> searchPurchaseOrderList(PurchaseOrderMasterDto PurchaseOrdDto,Integer screenId);
	
	public PurchaseOrder getPurchaseOrder(Integer id);
	
	public List<PurchaseOrderDetail> getPurchaseOrderDetail(Integer id);
	
	public List<PurchaseOrderUomMapper> getPurchaseOrderUomMapper(Integer id);
	
	public List<BonusUomMapper> getBonusUomMapper(Integer id);
	
	public List<PurchaseOrderOtherChargesMapper> getPoOtherChargesMapper(Integer id);
	
	public PurchaseOrder savePurchaseOrder(PurchaseOrder purchaseOrder);
	
	public List<PoPurchaseRequestArray> getPurchaseRequestTypeId(Integer purchaseOrderDetailId);
	
	public PurchaseOrder updatePurchaseOrder(PurchaseOrder purchaseOrder);

	public List<PurchaseOrderStaged> getPoStaged(Integer id);

	List<PurchaseRequestDetail> getPurchaseRequestDetails(List<Integer> purchaseRequestDetailIds);

	List<PoAccessoriesMapper> getPoAccessoriesMapper(Integer id);

	public PurchaseOrderMasterDetailsDto getPurchaseOrderBP(Integer id);

	public List<PurchaseOrderDetailsRespDto> getPoDetailsConsumablesByPoIdBP(Integer id);
	
	//added by minesh	
	List<PurchaseOrder> searchConsignmentStockPurchaseOrderList(PurchaseOrderMasterDto PurchaseOrdDto, Integer screenId);
	public List<PurchaseOrder> searchNonConsignmentPurchaseOrderList(PurchaseOrderMasterDto PurchaseOrdDto,Integer screenId);
	public List<PurchaseOrderDetailsRespDto> getConsignmentStockPoDetailsConsumablesByPoIdBP(Integer id);
	public BigInteger getTotalPOApprovedQuantityAgainGRNDetail(Integer id ,  Integer unitId);
	public List<ConsignmentStockPOHistoryDto> getConsignmentStockPOHistory(Integer grnDetailId , Integer unitId);
	//end

	public List<PurchaseOrderDetailsRespDto> getPoDetailsByAssetsPoIdBP(Integer id);

	public List<PoPurchaseRequestArray> getPurchaseRequestTypeIdBP(List<Integer> detailsIds);

	public List<PurchaseOrderUomMapperDto> getPurchaseOrderUomMapperBP(List<Integer> detailsIds);

	public List<PurchaseOrderUomMapperDto> getBonusUomMapperBP(List<Integer> detailsIds);

	public List<PoOtherChargesMapperDto> getPoOtherChargesMapperBP(Integer poId);

	public List<PurchaseOrderStagedDto> getPoStagedBP(List<Integer> detailsIds);

	public List<com.param.procurement.purchase.dto.purchaseOrder.PoAccessoriesMapper> getPoAccessoriesMapperBP(
			List<Integer> consumabelItemIds);

	public List<PurchaseOrder> getPurchaseOrders(List<Integer> poIds);

	public List<PurchaseOrderDetail> getPurchaseOrderDetails(List<Integer> poIds);

	public List<QrDateDto> getqrDetails(Integer qrdid);

	public List<LastPurchaseOrderDto> lastPurchaseOrderList(Integer itemid, Integer vendorid,Integer poType);

	public PurchaseOrderDetail getPurchaseOrderDetailById(int i);

	public PurchaseOrderOtherChargesMapper getPoOtherChargesMapperById(Integer id);

	public void updateOtherCharges(PurchaseOrderOtherChargesMapper purchaseOrderOtherChargesMapper);

	public void updateTaxId(Integer id, Integer taxId,Integer purchaseOrderId);

	public void updateOtherCharges(Integer id);

	public boolean getFinalPOApprovalFlag(Integer unitId);

	public List<PoOtherChargesMapperDto> getPoOtherChargesMapperBPPoDetails(Integer id);
	
	public List<PurchaseOrderStagedDto> getInitialPurchaseOrderStagedDto(List<Integer> detailsIds);

	

}
