package com.param.inventory.store.service.materialInwardApproval;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.constants.Constants;
import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.BatchAccessoriesMapper;
import com.param.entity.model.inventory.BatchUomMapper;
import com.param.entity.model.inventory.InwardsUomMapper;
import com.param.entity.model.inventory.MaterialInward;
import com.param.entity.model.inventory.MaterialInwardAccessories;
import com.param.entity.model.inventory.MaterialInwardDetail;
import com.param.entity.model.inventory.ReturnableGatepass;
import com.param.entity.model.inventory.StockTransaction;
import com.param.entity.model.inventory.StoreStock;
import com.param.entity.model.master.AssetType;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.RejectReason;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.Tax;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.UomUnit;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dto.items.AsseccoriesResponse;
import com.param.global.dto.items.UomResponse;
import com.param.service.global.SessionService;
import com.param.global.utility.Util;
import com.param.inventory.stock.dto.openingBalance.ItemRequest;
import com.param.inventory.store.dao.MaterialInwardApproval.IMaterialInwardApprovalDao;
import com.param.inventory.store.dao.materialInward.IMaterialInwardDao;
import com.param.inventory.store.dto.materialInward.MaterialInwardAccessoriesRequest;
import com.param.inventory.store.dto.materialInward.MaterialInwardDropdownResponse;
import com.param.inventory.store.dto.materialInward.MaterialInwardRequest;
import com.param.inventory.store.dto.materialInward.MaterialInwardResponse;
import com.param.inventory.store.dto.materialInward.MaterialItemResponse;

@Service
public class MaterialInwardApprovalService implements IMaterialInwardApprovalService {

	private Integer screenId = Screen.MaterialInwardApproval.getScreen();

	@Autowired
	IGlobalDao GlobalDao;

	@Autowired
	IMaterialInwardDao MaterialInwardDao;

	@Autowired
	IMaterialInwardApprovalDao MaterialInwardApprovalDao;

@Autowired
private SessionService sessionService;

	@Override
	@Transactional
	public MaterialInwardDropdownResponse getDropdowns(Integer unitId, Integer orgId, Integer userId) {
		MaterialInwardDropdownResponse response = new MaterialInwardDropdownResponse();

		for (Store store : GlobalDao.getUserStoreList(unitId,orgId,userId)) {
			response.addStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		}

		for (ScreenStatus screenStatus : GlobalDao.getScreenStatus(screenId)) {
			response.addStatus(new com.param.global.dto.global.Status(screenStatus.getStatus().getId(),
					screenStatus.getStatus().getStatus()));
		}

		for (AssetType assetType : GlobalDao.getAssetTypeList(0,0)) {
			response.addAssetType(new com.param.global.dto.global.AssetType(assetType.getId(), assetType.getType()));
		}
		return response;
	}

	@Override
	@Transactional
	public Object getMaterialInwardApprovalList(MaterialInwardRequest request) {
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());
		return MaterialInwardApprovalDao.searchMaterialInwardApprovalList(request, screenId);
	}

	@Override
	@Transactional
	public Object getMaterialInwardApproval(Integer id) {
		MaterialInward materialInward = MaterialInwardApprovalDao.getMaterialInwardApproval(id);

		MaterialInwardResponse response = new MaterialInwardResponse(materialInward.getId(),
				materialInward.getInwordNo(), materialInward.getAddedDate(), materialInward.getStore().getStoreId(),
				materialInward.getStore().getStoreName(), materialInward.getVendor().getId(),
				materialInward.getVendor().getName(), materialInward.getAssetType().getId(),
				materialInward.getAssetType().getType(), materialInward.getRequestType().getId(),
				materialInward.getRequestType().getType(), materialInward.getReturnableGatepass().getId(),
				materialInward.getReturnableGatepass().getRetGpNo(),
				materialInward.getDocumentNo(), materialInward.getDocumentDate(), materialInward.getTotalAmount(),
				materialInward.getTotalTaxAmount(), materialInward.getNetAmount(), materialInward.getStatus().getId(),
				materialInward.getStatus().getStatus());
		response.setAddedBY(materialInward.getAddedBy().getUserName());
		response.setRemarks(materialInward.getRemark());
		response.setReturnAmount(materialInward.getReturnAmount());
		response.setVendorPayable(materialInward.getVendorPayable());
		response.setCreditNote(materialInward.getCreditNote());


		Double totalAmount = 0.00;
		Double totalTaxAmount = 0.00;
		Double totalNetAmount = 0.00;
		for (MaterialInwardDetail detail : materialInward.getMaterialInwardsDetailList()) {
			if (materialInward.getRequestType().getId() == Constants.replacementId) {
				
				if(response.getAssetTypeId() == Constants.consumableId)
				{
					MaterialItemResponse m=new MaterialItemResponse(detail.getId(),
							//detail.getReturnableGatepassId().getId(),
							detail.getMaterialInward().getReturnableGatepass().getId(), /* change code for resolved HIS-5496 Material Inward - Replacement - Not working */
							detail.getItem().getId(),
							detail.getItem().getCode(), detail.getItem().getName(), detail.getUomType().getId(),
							detail.getUomType().getType(), detail.getUomUnit().getId(), detail.getUomUnit().getCode(),
							detail.getBatchNo(), detail.getExpiryDate(), detail.getCop(), detail.getTax().getId(),
							detail.getTax().getName(), detail.getTaxPercentage(), detail.getTaxAmount(),
							detail.getTotalAmount(), detail.getNetAmount(), detail.getReceivedQty(),
							detail.getStatus() != null ? detail.getStatus().getId() : null,
							detail.getStatus() != null ? detail.getStatus().getStatus() : null, detail.getMarkup(),
							//detail.getReturnableGatepassId().getReturnableGatepassDetails().get(0).getApprovedQty(),
							detail.getMaterialInward().getReturnableGatepass().getReturnableGatepassDetails().get(0).getApprovedQty(), /* change code for resolved HIS-5496 Material Inward - Replacement - Not working */
							detail.getMrp(), detail.getSpecification(),
							//detail.getReturnableGatepassId().getTotalAmt(),
							detail.getMaterialInward().getReturnableGatepass().getReturnableGatepassDetails().get(0).getTotalAmt(), /* change code for resolved HIS-5496 Material Inward - Replacement - Not working */
							detail.getUomType().getId(),
							GlobalDao.getUnitOfMeasurementListByItem(detail.getItem().getId(), detail.getOrganizationId()), detail.getApprovedQuantity()!=null ? detail.getApprovedQuantity() :0,detail.getRejectQuantity()!=null ? detail.getRejectQuantity() : 0,
						    detail.getItem().getIsThreeUOMApplicable());
				m.setApprovedQuantity(detail.getApprovedQuantity()!=null ? detail.getApprovedQuantity() :0);
				m.setRejectedQuantity(detail.getRejectQuantity()!=null ? detail.getRejectQuantity() : 0);
				m.setIsReject(detail.getIsReject());
					response.addItems(m);
					totalAmount = totalAmount + detail.getTotalAmount();
					totalTaxAmount = totalTaxAmount + detail.getTaxAmount();
					totalNetAmount = totalNetAmount + detail.getNetAmount();

				}
				else if(response.getAssetTypeId() == Constants.fixedAssetId)
				{
					MaterialItemResponse m=	new MaterialItemResponse(detail.getId(),
							//detail.getReturnableGatepassId().getId(),
                            detail.getMaterialInward().getReturnableGatepass().getId(),/* change code for resolved HIS-5496 Material Inward - Replacement - Not working */
							detail.getItem().getId(),
							detail.getItem().getCode(), detail.getItem().getName(),
							detail.getBatchNo(), detail.getExpiryDate(), detail.getCop(), detail.getTax().getId(),
							detail.getTax().getName(), detail.getTaxPercentage(), detail.getTaxAmount(),
							detail.getTotalAmount(), detail.getNetAmount(), detail.getReceivedQty(),
							detail.getStatus() != null ? detail.getStatus().getId() : null,
							detail.getStatus() != null ? detail.getStatus().getStatus() : null, detail.getMarkup(),
							//detail.getReturnableGatepassId().getReturnableGatepassDetails().get(0).getApprovedQty(),
							detail.getMaterialInward().getReturnableGatepass().getReturnableGatepassDetails().get(0).getApprovedQty(), /* change code for resolved HIS-5496 Material Inward - Replacement - Not working */
							detail.getMrp(), detail.getSpecification(),
							//detail.getReturnableGatepassId().getTotalAmt(),
							detail.getMaterialInward().getReturnableGatepass().getReturnableGatepassDetails().get(0).getTotalAmt(), /* change code for resolved HIS-5496 Material Inward - Replacement - Not working */
							PopulateAccessories(MaterialInwardDao.getInwardAccessoriesList(detail.getId())), detail.getItem().getIsThreeUOMApplicable());
				
					m.setApprovedQuantity(detail.getApprovedQuantity()!=null ? detail.getApprovedQuantity() :0);
					m.setRejectedQuantity(detail.getRejectQuantity()!=null ? detail.getRejectQuantity() : 0);
					m.setIsReject(detail.getIsReject());
					response.addItems(m);
					totalAmount = totalAmount + detail.getTotalAmount();
					totalTaxAmount = totalTaxAmount + detail.getTaxAmount();
					totalNetAmount = totalNetAmount + detail.getNetAmount();
				}
			}
			if (materialInward.getRequestType().getId() == Constants.creditNoteId) {
				ReturnableGatepass rg=MaterialInwardDao.getReturnableGatepass(detail.getReturnableGatepassDetail().getReturnableGatepass());
				MaterialItemResponse m=	new MaterialItemResponse(detail.getId(),
						rg.getAddedDate(),
						detail.getTotalAmount(),
						detail.getNetAmount(),
						detail.getTaxAmount(),
						detail.getStatus().getId(), detail.getStatus().getStatus(),
						detail.getCreditNoteAmount(), detail.getCreditNetAmount(), detail.getCreditTaxAmount());
				m.setApprovedQuantity(detail.getApprovedQuantity()!=null ? detail.getApprovedQuantity() :0);
				m.setRejectQuantity(detail.getRejectQuantity()!=null ? detail.getRejectQuantity() : 0);
				m.setReturnableGatePassNo(rg.getRetGpNo());
				response.addItems(m);
			}
		}
		return response;
	}
	
	private List<UomResponse> PopulateUom(List<InwardsUomMapper> uomMapper) {
		List<UomResponse> response = new ArrayList<UomResponse>();

		for (InwardsUomMapper uom : uomMapper) {
			response.add(new UomResponse(uom.getId(), uom.getUomType().getType(), uom.getUomType().getId(),
					uom.getUomUnit().getId(), uom.getUomUnit().getUnits(), uom.getConversion(), uom.getIpUom(),
					uom.getOpUom(), uom.getStoreUom()));
		}
		return response;
	}

	private List<AsseccoriesResponse> PopulateAccessories(List<MaterialInwardAccessories> InwardAccessories) {
		List<AsseccoriesResponse> response = new ArrayList<AsseccoriesResponse>();

		for (MaterialInwardAccessories accessories : InwardAccessories) {
			response.add(new AsseccoriesResponse(accessories.getId(), accessories.getQuantity(),accessories.getSpecification(),accessories.getApprovedQty()));
		}
		return response;
	}

	
	@Override
	@Transactional
	public Object approveMaterialInwardApproval(MaterialInwardRequest request) {

		MaterialInward materialInward = MaterialInwardDao.getMaterialInward(request.getId());
		materialInward.approveMaterialInward(new Status(request.getStatusId()),
				new Status(request.getApprovalStatusId()));
		
		Integer storeId = materialInward.getStore().getStoreId();

		for (MaterialInwardDetail detail : materialInward.getMaterialInwardsDetailList()) {

					for (ItemRequest item : request.getItem()) {
						if (item.getId() != null && item.getId().equals(detail.getId())) {
							
							detail.approveMaterialInwardDetail(item.getApprovedQuantity() != null ? item.getApprovedQuantity() : null,
									item.getRejectQuantity() != null ? item.getRejectQuantity() : null,
									item.getRejectNote() != null ? item.getRejectNote() : null, 
									item.getRejectReasonId() != null ? new RejectReason(item.getRejectReasonId()) : null,
									item.getStatusId() != null ? new Status(item.getStatusId()) : null);
							detail.setIsReject(item.getIsReject()!=null ? item.getIsReject() : false);
							detail.approveMaterialInwardDetail(item.getStatusId() != null ? new Status(item.getStatusId()) : null,
									item.getCreditNoteAmount() != null ? item.getCreditNoteAmount() : 0.00,
									item.getCreditTaxAmount() != null ? item.getCreditTaxAmount() : 0.00,item.getCreditNetAmount() != null ? item.getCreditNetAmount() : 0.00);
							
						   if(item.getApprovedQuantity() != null) {
							   if(request.getAssetTypeId() == Constants.consumableId)	
								{
								   	MaterialInwardDao.saveBatch(InsertApprovedItems(detail, storeId,request));
								}
								 else if(request.getAssetTypeId() == Constants.fixedAssetId)
								{
									 Batch batch = MaterialInwardDao.saveBatch(InsertFixedApprovedItems(detail, storeId,item.getApprovedQuantity(),request));
									 
									 if(item.getMaterialInwardAccessoriesRequest()!=null)
										{
									 for (MaterialInwardAccessoriesRequest accessories : item.getMaterialInwardAccessoriesRequest()) {
										 
											 if (accessories.getId() != null && accessories.getId().equals(detail.getMaterialInwardAccessories().get(0).getId())) {
													
											 detail.getMaterialInwardAccessories().get(0).setApprovedQty(accessories.getApprovedQuantity());
											 detail.getMaterialInwardAccessories().get(0).setUpdatedDate(new Date());
											 detail.getMaterialInwardAccessories().get(0).setUpdatedByUser(request.getUserId());
											 
											
										 
											 Batch batchAccessory = MaterialInwardDao.saveBatch(InsertFixedApprovedAccessories(detail, storeId,accessories.getApprovedQuantity(),request));
											 BatchAccessoriesMapper batchAccessoriesMapper=	 new BatchAccessoriesMapper(
														new Batch(batch.getId()), new Batch(batchAccessory.getId()));
										//	 sessionService.setEntityDefaults(batchAccessoriesMapper);	
											 batchAccessoriesMapper.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
											 MaterialInwardDao.saveBatchAccessoriesMapper(batchAccessoriesMapper);
											 
											 }
									 }
								}
									 break;
								}
		
							detail.setUpdatedDate(new Date());
						//	detail.setUpdatedBy(detail.getUser());
							detail.setUpdatedByUser(request.getUserId());
						}
					}
				}
		}
		materialInward.getReturnableGatepass().setStatus(new Status(com.param.common.constants.Status.Closed.getStatus()));
		materialInward.setUpdatedDate(new Date());
	//	materialInward.setUpdatedBy(materialInward.getUser());
		materialInward.setUpdatedByUser(request.getUserId());

		MaterialInwardDao.updateMaterialInward(materialInward);

		CommonResponse response = new CommonResponse();
		response.setMessage("Material Inward approval saved successfully");

		return response;
	}

	private Batch InsertApprovedItems(MaterialInwardDetail detail, Integer storeId, MaterialInwardRequest request) {

		Integer leaseQuantity = Util.GetLeaseApprovedQuantity(detail.getApprovedQuantity(), detail.getInwardsUomMapperList(), detail.getUomType());
		
		Double mrpLeaseQuantity = Util.GetMrpLeaseQuantityForMatInw(detail.getMrp(), detail.getInwardsUomMapperList(), detail.getUomType());
		
		Batch batch = new Batch(detail.getMrp(), detail.getBatchNo(), detail.getCop(), leaseQuantity,
				detail.getExpiryDate(), new Item(detail.getItem().getId()), detail.getMrp(), null,
				detail.getMarkup(), leaseQuantity, mrpLeaseQuantity, detail.getMrp(), detail.getTaxAmount(),
				new Tax(detail.getTax().getId()), detail.getTaxPercentage(), new UomUnit(detail.getUomUnit().getId()),
				true);
		batch.setIsQuarantine(false);
	//	sessionService.setEntityDefaults(batch);
		batch.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
		for (InwardsUomMapper inwardsUomMapper : detail.getInwardsUomMapperList())
		{
			BatchUomMapper batchUomMapper=new BatchUomMapper(inwardsUomMapper.getConversion(), inwardsUomMapper.getIpUom(), inwardsUomMapper.getOpUom(),
					inwardsUomMapper.getStoreUom(), new UomType(inwardsUomMapper.getUomType().getId()),
					new UomUnit(inwardsUomMapper.getUomUnit().getId()));
		//	sessionService.setEntityDefaults(batchUomMapper);
			batchUomMapper.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
			batch.addBatchUom(batchUomMapper);
		}
		StoreStock storeStock=	new StoreStock(leaseQuantity, new Store(detail.getMaterialInward().getStore().getStoreId()),
				leaseQuantity, new Batch(batch.getId()));
		//sessionService.setEntityDefaults(storeStock);
		storeStock.setTotalQty(leaseQuantity);
		storeStock.settQty(0);
		storeStock.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
		batch.addStoreStock(storeStock);

		com.param.entity.model.master.Screen screen = GlobalDao.getScreen(screenId);
		StockTransaction stockTransaction=new StockTransaction(detail.getCop(), leaseQuantity,
				new com.param.entity.model.master.Screen(screen.getId()), screen.getCode(), detail.getMrp(),
				leaseQuantity, detail.getMrp(), new Store(storeId), new Batch(batch.getId()),detail.getId());
		stockTransaction.setInQty(leaseQuantity);
		stockTransaction.setTransactionId(detail.getMaterialInward().getId());
	//	sessionService.setEntityDefaults(stockTransaction);
		stockTransaction.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
		batch.addStockTransaction(stockTransaction);

		return batch;
	}
	@SuppressWarnings("unlikely-arg-type")
	private Batch InsertFixedApprovedItems(MaterialInwardDetail detail, Integer storeId,Integer detailApprovedquantity,MaterialInwardRequest request) {

		
		Batch batch = new Batch(detail.getMrp(), detail.getBatchNo(), detail.getCop(), detailApprovedquantity,
				detail.getExpiryDate(), new Item(detail.getItem().getId()), detail.getMrp(), null,
				detail.getMarkup(), detailApprovedquantity, detail.getMrp(), detail.getMrp(), detail.getTaxAmount(),
				new Tax(detail.getTax().getId()), detail.getTaxPercentage(), true);
		//sessionService.setEntityDefaults(batch);
		batch.setIsQuarantine(false);
		StoreStock storeStock=new StoreStock(detailApprovedquantity, new Store(detail.getMaterialInward().getStore().getStoreId()),
				0, new Batch(batch.getId()));
		storeStock.setTotalQty(detailApprovedquantity);
		storeStock.settQty(0);
	//	sessionService.setEntityDefaults(storeStock);
		storeStock.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
		batch.addStoreStock(storeStock);
		batch.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
		com.param.entity.model.master.Screen screen = GlobalDao.getScreen(screenId);
	// Add stock transaction
		StockTransaction stockTransaction=new StockTransaction(detail.getCop(), detailApprovedquantity,
				new com.param.entity.model.master.Screen(screen.getId()), screen.getCode(), detail.getMrp(),
				detailApprovedquantity, detail.getMrp(), new Store(storeId), new Batch(batch.getId()), detail.getId());
		stockTransaction.setInQty(detailApprovedquantity);
		stockTransaction.setTransactionId(detail.getMaterialInward().getId());
		//sessionService.setEntityDefaults(stockTransaction);
		stockTransaction.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
			batch.addStockTransaction(stockTransaction);
			return batch;
	}
private Batch InsertFixedApprovedAccessories(MaterialInwardDetail detail, Integer storeId,Integer accessoriesApprovedquantity,MaterialInwardRequest request) {

		
		Batch batch = new Batch(detail.getMrp(), detail.getBatchNo(), detail.getCop(), accessoriesApprovedquantity,
				detail.getExpiryDate(), new Item(detail.getItem().getId()), detail.getMrp(), null,
				null, accessoriesApprovedquantity, detail.getMrp(), detail.getMrp(), detail.getTaxAmount(),
				new Tax(detail.getTax().getId()), detail.getTaxPercentage(), true);
	//	sessionService.setEntityDefaults(batch);
		batch.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
		StoreStock storeStock=	new StoreStock(accessoriesApprovedquantity, new Store(detail.getMaterialInward().getStore().getStoreId()),
				accessoriesApprovedquantity, new Batch(batch.getId()));
	//	sessionService.setEntityDefaults(storeStock);
		storeStock.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
				batch.addStoreStock(storeStock);

		com.param.entity.model.master.Screen screen = GlobalDao.getScreen(screenId);

			// Add stock transaction
		StockTransaction stockTransaction=new StockTransaction(detail.getCop(), accessoriesApprovedquantity,
				new com.param.entity.model.master.Screen(screen.getId()), screen.getCode(), detail.getMrp(),
				accessoriesApprovedquantity, detail.getMrp(), new Store(storeId), new Batch(batch.getId()),detail.getId());
		stockTransaction.setInQty(accessoriesApprovedquantity);
		stockTransaction.setTransactionId(detail.getMaterialInward().getId());
	//	sessionService.setEntityDefaults(stockTransaction);
		stockTransaction.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
			batch.addStockTransaction(stockTransaction);
	return batch;
	}
	
}
