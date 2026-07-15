package com.param.procurement.purchase.dao.goodReceiptNote;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import com.param.entity.model.inventory.Batch;
import com.param.entity.model.master.ApplicationConfiguration;
import com.param.entity.model.master.DiscountType;
import com.param.entity.model.procurement.GoodReceiptNote;
import com.param.entity.model.procurement.GoodReceiptNoteDetail;
import com.param.entity.model.procurement.GoodReceiptNoteDetailLog;
import com.param.entity.model.procurement.GrnAccessory;
import com.param.entity.model.procurement.GrnBonusUomMapper;
import com.param.entity.model.procurement.GrnOtherChargesMapper;
import com.param.entity.model.procurement.GrnSettlement;
import com.param.entity.model.procurement.GrnUomMapper;
import com.param.entity.model.procurement.PhysicalInspection;
import com.param.entity.model.procurement.PurchaseOrderDetail;
import com.param.global.dto.TaxComponentDetailsDto;
import com.param.procurement.purchase.dto.goodReceiptNote.CreditNoteAmountList;
import com.param.procurement.purchase.dto.goodReceiptNote.CreditNoteVendorWise;
import com.param.procurement.purchase.dto.goodReceiptNote.GRNDetailResponse;
import com.param.procurement.purchase.dto.goodReceiptNote.GRNOtherChargesMapper;
import com.param.procurement.purchase.dto.goodReceiptNote.GRNRequest;
import com.param.procurement.purchase.dto.goodReceiptNote.GRNResponse;
import com.param.procurement.purchase.dto.goodReceiptNote.GRNSearchResponse;
import com.param.procurement.purchase.dto.goodReceiptNote.GRNSearchResponse1;
import com.param.procurement.purchase.dto.goodReceiptNote.GRNSettledDetailsResponse;
import com.param.procurement.purchase.dto.goodReceiptNote.GRNSettledSearchResponse;
import com.param.procurement.purchase.dto.goodReceiptNote.GRNStagedDto;
import com.param.procurement.purchase.dto.goodReceiptNote.GRNStagedList;
import com.param.procurement.purchase.dto.goodReceiptNote.GRNUomMapper;
import com.param.procurement.purchase.dto.goodReceiptNote.GrnAccessoriesMapper;
import com.param.procurement.purchase.dto.goodReceiptNote.PreviousGRNSearchResponse;
import com.param.procurement.purchase.dto.goodReceiptNote.PurchasedOrderResponse;
import com.param.procurement.purchase.dto.goodReceiptNote.SettledDetails;
import com.param.procurement.purchase.service.goodReceiptNote.GrnNetAmount;
import com.param.procurement.quotation.dto.physicalInspection.DetailsConsignmentStockConversionSaveRequest;

public interface IGoodReceiptNoteDao {

	List<GoodReceiptNote> searchGRNList(GRNRequest grnRequest, Integer screenId);

	GoodReceiptNote getGoodReceiptNote(Integer id);

	DiscountType getDiscountTypeById(Integer discountTypeId);

	List<GoodReceiptNoteDetail> getGoodReceiptNoteDetail(Integer id);

	List<GrnBonusUomMapper> getGRNBonusUomMapper(Integer id);

	List<GrnUomMapper> getGRNUomMapper(Integer id);

	List<GrnOtherChargesMapper> getGRNOtherChargesMapper(Integer id);

	List<GRNStagedDto> getGRNStagedQuantityForApproval(Integer id);

	GoodReceiptNote saveGoodReceiptNote(GoodReceiptNote goodReceiptNote);

	GRNSearchResponse1 getNetAndPaidAmt(List<Integer> grnIds);

	PurchaseOrderDetail getPurchaseOrderDetails(Integer purchaseOrderDetailIds);

	GoodReceiptNote updateGoodReceiptNote(GoodReceiptNote goodReceiptNote);
	
	PhysicalInspection savePhysicalInspection(PhysicalInspection physicalInspection);

	Batch saveBatch(Batch batch);

	List<GRNStagedDto> getGRNStagedQuantity(Integer id);

	List<PurchasedOrderResponse> getPurchaseOrderId(Integer goodReceiptNoteDetailId);

	List<GrnAccessory> getGrnAccessory(Integer id);

	List<GRNStagedList> getGRNQuantityByDcDate(Integer id);

	GRNResponse getGoodReceiptNoteBP(Integer id);

	List<GRNDetailResponse> getGoodReceiptNoteDetailsBP(Integer id);

	List<PurchasedOrderResponse> getPurchaseOrderIdBP(List<Integer> allDetailIds);

	List<GRNStagedDto> getGRNStagedQuantityBP(List<Integer> allDetailIds);

	List<GRNStagedList> getGRNQuantityByDcDateBP(List<Integer> poDetailId, Integer grnId);

	List<GRNOtherChargesMapper> getGRNOtherChargesMapperBP(Integer allDetailIds);

	List<GRNUomMapper> getGRNUomMapperBP(List<Integer> consumableDetailIds);

	List<GRNUomMapper> getGRNBonusUomMapperBP(List<Integer> consumabelItemIds);

	List<GrnAccessoriesMapper> getGrnAccessoryBP(List<Integer> fixedDetaildId);

	List<GRNSearchResponse> searchGRNListBP(GRNRequest grnRequest, Integer screenId);

	List<GRNStagedDto> getGRNStagedQuantityForApprovalBP(List<Integer> allDetailIds);

	List<GoodReceiptNote> getByInvoiceNumberAndVendor(Integer vendorId, String invoiceNumber);

	List<GoodReceiptNote> getByInvoiceNumberAndDCNumber(Integer vendorId, String invoiceNumber);

	List<PreviousGRNSearchResponse> previousGrnDetails(Integer vendorId, Integer itemid , Integer grnId);

	List<GRNSearchResponse1> searchGRNListSettlement(GRNRequest grnRequest, Integer screenId);

	GrnNetAmount getGrnNetAmt(Integer grnId, Integer vendorId, Integer orgnizationId, Integer unitId);

	Integer saveGrnSettlement(GrnSettlement grnsettle);

	void updateGrnNetAmt(Integer grnId, Integer vendorId, Integer orgnizationId, Integer unitId, Double amount);

	void updatePartiallyGrnNetAmt(Integer grnId, Integer vendorId, Integer orgnizationId, Integer unitId,
			Double amount);

	List<GRNSettledSearchResponse> getGoodReceiptNoteSettledList(GRNRequest grnRequest);

	GRNSettledDetailsResponse getGoodReceiptNoteSettledDetails(Integer grnid,Integer unitId);

	List<SettledDetails> settledDetails(Integer grnid);

	List<CreditNoteAmountList> getCreditNoteAmt(Integer id);

	void updateCreditNoteAmt(Integer mateinwarddetailsid, Double consumeAmt);

	GRNResponse getGoodReceiptNoteSettledBP(Integer id);

	List<PreviousGRNSearchResponse> previousNewGrnDetails(Integer vendorId, Integer itemid);

	List<CreditNoteVendorWise> getReturnableGatepassDetails(Integer mateinwardid);

	GoodReceiptNoteDetail getGoodReceiptNoteDetailById(Integer id);

	GrnOtherChargesMapper getGRNOtherChargesMapperById(Integer id);

	void updateOtherCharges(GrnOtherChargesMapper otherChargesmapper);

	BigInteger getLastId();

	ApplicationConfiguration getApplicationConfiguration(Integer userId);

	Batch saveOrUpdateBatch(Batch batchToSave);

	List<TaxComponentDetailsDto> getTaxDetails(Integer id);

	Object getInvoiceNumber(GRNRequest grnRequest);
	Double getNetAmountSumByGrnIdFromGrnDetails(Integer id, Integer unitId, Integer organizationId);
	DetailsConsignmentStockConversionSaveRequest getTransactionDetails(Long id);

	Boolean getIsSkipPhysicalInspection(Integer unitId, Integer orgId);

	List<GRNOtherChargesMapper> getGRNOtherChargesMapperBP1(Integer id);

	Double geRoundOffAmountSumByGrnIdFromGrnDetails(Integer id, Integer unitId, Integer organizationId);

	boolean updateBatchId(Long id, Integer grnBonusBatchExpiryId);

	boolean updateBatchIdForBonusGrn(Integer key, Long value);

	Date getexpiry(Integer grnBonusBatchExpiryId);

	Double getCreditNoteAmount(Integer grnId, Integer unitId);

	void updateOtherCharges(Integer id);

	Double getPaidAmount(Integer grnId, Integer orgnizationId, Integer unitId);

	void updateSettlementReceiptDetailsStatusByGrnId(Integer grnId);

	BigInteger alreadyGoodReceiptNoteExits(List<Integer> grnIds);

	void saveDetails(GoodReceiptNoteDetailLog grnLogDetail);

	List<GoodReceiptNote> getGoodReceiptNoteAll(List<Integer> grnIds);

	BigInteger checkAlreadyExists(GRNRequest grnRequest);

 
	
   
}
