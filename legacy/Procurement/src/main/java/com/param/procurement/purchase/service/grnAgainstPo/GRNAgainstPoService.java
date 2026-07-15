package com.param.procurement.purchase.service.grnAgainstPo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.master.AssetType;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.procurement.GoodReceiptNote;
import com.param.entity.model.procurement.GoodReceiptNoteDetail;
import com.param.entity.model.procurement.PurchaseOrder;
import com.param.entity.model.procurement.PurchaseOrderDetail;
import com.param.global.dao.global.IGlobalDao;
import com.param.procurement.purchase.dao.grnAgainstPo.IGRNAgainstPoDao;
import com.param.procurement.purchase.dao.purchaseOrder.IPurchaseOrderDao;
import com.param.procurement.purchase.dto.goodReceiptNote.GRNDropdownDto;
import com.param.procurement.purchase.dto.goodReceiptNote.GRNResponse;
import com.param.procurement.purchase.purchaseOrder.dto.PurchaseOrderMasterDto;
import com.param.procurement.purchase.purchaseOrder.dto.PurchaseOrderSearchDto;
import com.param.utility.TimeZoneComponent;

@Service
public class GRNAgainstPoService implements IGRNAgainstPoService{
	
	private Integer screenId = Screen.GRNAgainstPo.getScreen();
	
	@Autowired
	IGRNAgainstPoDao grnAgainstDao;
	
	@Autowired
	private IPurchaseOrderDao purchaseOrderDao;
	
	@Autowired
	IGlobalDao GlobalDao;
	
	@Override
	@Transactional
	public GRNDropdownDto getDropdowns(Integer unitId, Integer orgId, Integer userId) {
		GRNDropdownDto grnDropDownDto = new GRNDropdownDto();

		for (Store store : GlobalDao.getUserStoreList(unitId,orgId,userId)) {
			grnDropDownDto.addRequestingStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		}

		for (ScreenStatus screenStatus : GlobalDao.getScreenStatus(screenId)) {
			grnDropDownDto.addStatus(new com.param.global.dto.global.Status(screenStatus.getStatus().getId(),
					screenStatus.getStatus().getStatus()));
		}

		for (AssetType assetType : GlobalDao.getAssetTypeList(unitId,orgId)) {
			grnDropDownDto
					.addAssetType(new com.param.global.dto.global.AssetType(assetType.getId(), assetType.getType()));
		}
		return grnDropDownDto;
	}
	
	@Override
	@Transactional
	public Object getPuchaseOrderList(PurchaseOrderMasterDto PurchaseOrdDto) {
		PurchaseOrdDto.setToDate((new DateTime(PurchaseOrdDto.getToDate())).plusDays(1).toDate());

		List<PurchaseOrderSearchDto> purchaseOrderSearchDtoList = new ArrayList<PurchaseOrderSearchDto>();
		
		List<Integer> statusId = new ArrayList<Integer>(Arrays.asList(com.param.common.constants.Status.Completed.getStatus(),
				com.param.common.constants.Status.PartiallyCompleted.getStatus()));		

		for (PurchaseOrder purchaseOrder : grnAgainstDao.searchPurchaseOrderList(PurchaseOrdDto, screenId,statusId)) {			

			PurchaseOrderSearchDto purchaseReqSearchDto = new PurchaseOrderSearchDto(purchaseOrder.getId(),
					purchaseOrder.getAddedDate(), purchaseOrder.getPoNumber(), purchaseOrder.getStore().getStoreName(),
					purchaseOrder.getApprovalStatus().getStatus(), purchaseOrder.getStatus().getStatus(),
					purchaseOrder.getAddedBy().getUserName(), 
					purchaseOrder.getVendor().getName(),purchaseOrder.getTotalAmount()!=null ? purchaseOrder.getTotalAmount() : 0.0,
					grnAgainstDao.getGoodReceiptNoteAmount(purchaseOrder.getId())!=null ? grnAgainstDao.getGoodReceiptNoteAmount(purchaseOrder.getId()) : 0.0 ,purchaseOrder.getRequestingStore().getStoreName() );

			purchaseOrderSearchDtoList.add(purchaseReqSearchDto);

		}
		return purchaseOrderSearchDtoList;
	}
	
	@Override
	@Transactional
	public Object getGoodReceiptNoteList(Integer id) {
		List<GoodReceiptNote> goodReceiptNoteList = grnAgainstDao.getGoodReceiptNote(id);
		List<GRNResponse> GRNResponseList = new ArrayList<GRNResponse>();
		for(GoodReceiptNote goodReceiptNote : goodReceiptNoteList) {			
			GRNResponse grnResponse = new GRNResponse(goodReceiptNote.getGrnNumber(),goodReceiptNote.getAddedDate(),
					goodReceiptNote.getInvoiceDate(),goodReceiptNote.getInvoiceNumber(),
					goodReceiptNote.getTotalAmount());
			GRNResponseList.add(grnResponse);
		}
		return GRNResponseList;
		
	}
	
	@Override
	@Transactional
	public Object cancelPurchaseOrder(PurchaseOrderMasterDto purchaseOrderDto) {

		PurchaseOrder purchaseOrder = purchaseOrderDao.getPurchaseOrder(purchaseOrderDto.getId());
		List<GoodReceiptNote> goodReceiptNoteList = grnAgainstDao.getGoodReceiptNote(purchaseOrderDto.getId());
		
		for (PurchaseOrderDetail detail : purchaseOrder.getPurchaseOrderDetailList()) {
			
			for(GoodReceiptNote goodReceiptNote : goodReceiptNoteList) {
				
				for(GoodReceiptNoteDetail goodReceiptNoteDetail : goodReceiptNote.getGoodReceiptNoteDetailsList()) {
					if(goodReceiptNoteDetail.getPurchaseOrderDetail().getId().equals(detail.getId()))	{			
			
						detail.setStatus(new Status(com.param.common.constants.Status.Closed.getStatus()));			
						
					}
					else {
						detail.setStatus(new Status(com.param.common.constants.Status.Cancelled.getStatus()));
					}
				}
			}
			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(purchaseOrder.getUnitId()));
			detail.setUpdatedByUser(purchaseOrderDto.getUserId());
		}

		purchaseOrder.setCancelReason(new CancelReason(purchaseOrderDto.getCancelReasonId()));
		purchaseOrder.setCancelNote(purchaseOrderDto.getCancelNote());

		purchaseOrder.setStatus(new Status(purchaseOrderDto.getStatusId()));
		purchaseOrder.setApprovalStatus(new Status(purchaseOrderDto.getApprovalStatusId()));

		purchaseOrder.setUpdatedDate(TimeZoneComponent.getDateByzone(purchaseOrder.getUnitId()));
		purchaseOrder.setUpdatedByUser(purchaseOrderDto.getUserId());

		purchaseOrderDao.updatePurchaseOrder(purchaseOrder);

		CommonResponse response = new CommonResponse();
		response.setMessage("Purchase Order cancelled successfully");

		return response;
	}

}
