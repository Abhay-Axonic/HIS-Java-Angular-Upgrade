package com.param.assetManagement.indent.rejectedAssetAcceptance.service;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.assetManagement.indent.assetIndent.dto.AssetIndentDropdown;
import com.param.assetManagement.indent.assetIssue.dto.AssetIssueRequest;
import com.param.assetManagement.indent.assetIssueAcceptance.dto.AccessoriesRequest;
import com.param.assetManagement.indent.assetIssueAcceptance.dto.ItemRequest;
import com.param.assetManagement.indent.assetIssueList.dto.AssetIssueItemResponse;
import com.param.assetManagement.indent.rejectedAssetAcceptance.dao.IRejectedAssetAcceptanceDao;
import com.param.assetManagement.indent.rejectedAssetAcceptance.dto.RejectAssetResponse;
import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.asset.AssetIssue;
import com.param.entity.model.asset.AssetIssueAccessory;
import com.param.entity.model.asset.AssetIssueBatchMapper;
import com.param.entity.model.asset.AssetReturn;
import com.param.entity.model.asset.AssetReturnAccessory;
import com.param.entity.model.asset.AssetReturnDetail;
import com.param.entity.model.asset.RejectAssetAccessory;
import com.param.entity.model.asset.RejectedAssetAcceptance;
import com.param.entity.model.asset.RejectedAssetAcceptanceDetail;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.StockTransaction;
import com.param.entity.model.master.RejectReason;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.User;
import com.param.global.dao.global.IGlobalDao;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service
public class RejectedAssetAcceptanceService implements IRejectedAssetAcceptanceService {
	
	@Autowired
	private SessionService sessionService;

	private Integer screenId = Screen.AssetRejectAcceptance.getScreen();

	private com.param.entity.model.master.Screen screen;

	@Autowired
	IGlobalDao GlobalDao;

	@Autowired
	IRejectedAssetAcceptanceDao RejectedAssetAcceptanceDao;

	@Override
	@Transactional
	public AssetIndentDropdown getDropdowns(Integer unitId, Integer orgId, Integer userId) {
		AssetIndentDropdown response = new AssetIndentDropdown();

		for (Store store : GlobalDao.getUserStoreList(unitId,orgId,userId)) {
			response.addFromStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		}

		for (Store store : GlobalDao.getStoreList(unitId)) {
			response.addToStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName(),
					store.getStoreType().getCode().equals("MS")));
		}

		for (ScreenStatus screenStatus : GlobalDao.getScreenStatus(screenId)) {
			response.addStatus(new com.param.global.dto.global.Status(screenStatus.getStatus().getId(),
					screenStatus.getStatus().getStatus()));
		}
		return response;
	}

	@Override
	@Transactional
	public Object getRejectAssetAcceptanceList(AssetIssueRequest request) {
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());

		return RejectedAssetAcceptanceDao.searchRejectAssetAcceptanceList(request, screenId);
	}

	@Override
	@Transactional
	public Object getRejectAssetAcceptanceDetails(Integer id) {

		RejectedAssetAcceptance rejectedAssetAcceptance = RejectedAssetAcceptanceDao.getRejectAssetAcceptance(id);

		RejectAssetResponse response = new RejectAssetResponse(rejectedAssetAcceptance.getId(),
				rejectedAssetAcceptance.getRejectedOn(), rejectedAssetAcceptance.getReturnNo(),
				rejectedAssetAcceptance.getFromStore().getStoreId(),
				rejectedAssetAcceptance.getFromStore().getStoreName(),
				rejectedAssetAcceptance.getToStore().getStoreId(), rejectedAssetAcceptance.getToStore().getStoreName(),
				rejectedAssetAcceptance.getRejectedBy().getUserName(),null,
				rejectedAssetAcceptance.getStatus().getStatus());
                  //rejectedAssetAcceptance.getAssetIssue().getAssetIndent().getRemark()
		for (RejectedAssetAcceptanceDetail detail : RejectedAssetAcceptanceDao.getRejectAssetAcceptanceDetail(id)) {

			AssetIssueBatchMapper assetIssueBatchMapper = detail.getAssetIssueBatchMapper();

			Batch batchObj = detail.getAssetIssueBatchMapper().getBatch();

			AssetIssueItemResponse itemResponse = new AssetIssueItemResponse(detail.getId(),
					assetIssueBatchMapper.getAssetIssueDetail().getItem().getId(),
					assetIssueBatchMapper.getAssetIssueDetail().getItem().getCode(),
					assetIssueBatchMapper.getAssetIssueDetail().getItem().getName(),null, batchObj.getId(),
					batchObj.getBatchNo(), batchObj.getExpiryDate(), detail.getRejectedQuantity(),
					detail.getRejectedNote());

			response.addItems(itemResponse);

		}
		return response;
	}

	@Override
	@Transactional
	public Object saveRejectAssetAcceptance(AssetIssueRequest request) {

		screen = GlobalDao.getScreen(screenId);

		RejectedAssetAcceptance rejectedAssetAcceptance = RejectedAssetAcceptanceDao
				.getRejectAssetAcceptance(request.getId());

		Integer storeId = rejectedAssetAcceptance.getToStore().getStoreId();

		for (RejectedAssetAcceptanceDetail detail : RejectedAssetAcceptanceDao
				.getRejectAssetAcceptanceDetail(request.getId())) {

			for (RejectAssetAccessory accessories : detail.getRejectAssetAccessories()) {
				//System.out.println("Size =" + detail.getRejectAssetAccessories().size());

				for (ItemRequest item : request.getItemRequest()) {

					if (item.getId().equals(detail.getId())) {
						detail.setAcceptedQuantity(item.getAcceptedQuantity());
						detail.setRejectedRejectQuantity(item.getRejectQuantity());
						detail.setAcceptedBy(new User(request.getUserId()!=null ? request.getUserId() : sessionService.getUserId()));
						detail.setAcceptedOn(TimeZoneComponent.getDateByzone(rejectedAssetAcceptance.getUnitId()));
						detail.setUpdatedDate(TimeZoneComponent.getDateByzone(rejectedAssetAcceptance.getUnitId()));
						detail.setUpdatedByUser(request.getUserId()!=null?request.getUserId():sessionService.getUserId());
						rejectedAssetAcceptance.addRejectedAssetAcceptanceDetail(detail);

						// RejectAssetAccessory rejectAssetAccessory = null;
						Integer accessoryId = null;
						for (AccessoriesRequest accessory : item.getAccessories()) {

							if (accessory.getId() != null && accessory.getId().equals(accessories.getId())) {
								accessoryId = accessory.getId();

								if (accessory.getIssueAccessoriesId() != null) {
									accessories.updateRejectAssetAccessory(accessory.getAcceptedQuantity(),
											new AssetIssueAccessory(accessory.getIssueAccessoriesId()),
											accessory.getRejectQuantity(),
											new RejectReason(accessory.getRejectReasonId()));
									// detail.addRejectAssetAccessory(accessories);
								}

								if (accessory.getReturnAccessoriesId() != null) {
									accessories.updateRejectAssetAccessory(accessory.getAcceptedQuantity(),
											new AssetReturnAccessory(accessory.getReturnAccessoriesId()),
											accessory.getRejectQuantity(),
											new RejectReason(accessory.getRejectReasonId()));
									// detail.addRejectAssetAccessory(accessories);
								}
							}

							if (rejectedAssetAcceptance.getAssetIssue() != null) {
								updateStoreStock(storeId, detail.getAssetIssueBatchMapper().getBatch().getId(),
										item.getAcceptedQuantity(),rejectedAssetAcceptance.getUnit().getUnitId(), request);
							}

							if (rejectedAssetAcceptance.getAssetReturn() != null) {
								updateStoreStock(storeId, detail.getAssetReturnDetail().getBatch().getId(),
										item.getAcceptedQuantity(),rejectedAssetAcceptance.getUnit().getUnitId(), request);
							}
						}
					}
				}
				saveIntoAssetReject(detail, rejectedAssetAcceptance, request);
			}
		}
		rejectedAssetAcceptance.setStatus(new Status(request.getStatusId()));
		rejectedAssetAcceptance.setUpdatedDate(TimeZoneComponent.getDateByzone(rejectedAssetAcceptance.getUnitId()));
		rejectedAssetAcceptance.setUpdatedByUser(request.getUserId()!=null?request.getUserId():sessionService.getUserId());
		RejectedAssetAcceptanceDao.updateRejectdAssetAcceptance(rejectedAssetAcceptance);
		CommonResponse response = new CommonResponse();
		response.setId(rejectedAssetAcceptance.getId().longValue());
		response.setMessage("Reject Asset Acceptance Updated successfully");
		return response;
	}

	private void saveIntoAssetReject(RejectedAssetAcceptanceDetail detail,
			RejectedAssetAcceptance rejectedAssetAcceptance, AssetIssueRequest request) {

		for (ItemRequest item : request.getItemRequest()) {

			for (AccessoriesRequest accessory : item.getAccessories()) {
				if (item.getRejectQuantity() > 0) {

					if (rejectedAssetAcceptance.getAssetIssue() != null) {
						// flag = true;
						RejectedAssetAcceptance rejectedAsset = new RejectedAssetAcceptance(
								new AssetIssue(request.getAssetIssueId()), new User(sessionService.getUserId()), TimeZoneComponent.getDateByzone(request.getUnitId()),
								"RAc-001", new Status(2),
								new Store(rejectedAssetAcceptance.getFromStore().getStoreId()),
								new Store(rejectedAssetAcceptance.getToStore().getStoreId()));
//						sessionService.setEntityDefaults(rejectedAsset);	
						rejectedAsset.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
								request.getUserId());
						RejectedAssetAcceptanceDetail acceptanceDetail = null;
//						sessionService.setEntityDefaults(acceptanceDetail);	
						acceptanceDetail.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
								request.getUserId());
					RejectedAssetAcceptanceDetail rejectedAssetAcceptanceDetail2=new RejectedAssetAcceptanceDetail(item.getRejectNote(), item.getRejectQuantity(),
								new RejectReason(item.getRejectReasonId()),
								new AssetIssueBatchMapper(item.getIssueBatchMapperId()));
						
//						sessionService.setEntityDefaults(rejectedAssetAcceptanceDetail2);
					rejectedAssetAcceptanceDetail2.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
							request.getUserId());
						acceptanceDetail = rejectedAsset.addRejectedAssetAcceptanceDetail(rejectedAssetAcceptanceDetail2);

						RejectAssetAccessory rejectAssetAccessory=new RejectAssetAccessory(
								new AssetIssueAccessory(accessory.getIssueAccessoriesId()), accessory.getRejectNote(),
								accessory.getRejectQuantity(), new RejectReason(accessory.getRejectReasonId()));
						
						acceptanceDetail.addRejectedAssetAcceptanceDetail(rejectAssetAccessory);
//						sessionService.setEntityDefaults(rejectAssetAccessory);	
						rejectAssetAccessory.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
								request.getUserId());
						RejectedAssetAcceptanceDao.save(rejectedAsset);
					}

					if (rejectedAssetAcceptance.getAssetReturn() != null) {

						RejectedAssetAcceptance rejectedAsset = new RejectedAssetAcceptance(
								new AssetReturn(request.getAssetReturnId()), new User(sessionService.getUserId()), TimeZoneComponent.getDateByzone(request.getUnitId()),
								"RAc-001", new Status(2),
								new Store(rejectedAssetAcceptance.getFromStore().getStoreId()),
								new Store(rejectedAssetAcceptance.getToStore().getStoreId()));
//						sessionService.setEntityDefaults(rejectedAsset);	
						rejectedAsset.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
								request.getUserId());
						RejectedAssetAcceptanceDetail acceptanceDetail = null;
						
						RejectedAssetAcceptanceDetail rejectedAssetAcceptanceDetail2=new RejectedAssetAcceptanceDetail(item.getRejectNote(), item.getRejectQuantity(),
								new RejectReason(item.getRejectReasonId()),
								new AssetReturnDetail(item.getIssueReturnDetailId()));
//						sessionService.setEntityDefaults(rejectedAssetAcceptanceDetail2);	
						rejectedAssetAcceptanceDetail2.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
								request.getUserId());
						acceptanceDetail = rejectedAsset.addRejectedAssetAcceptanceDetail(rejectedAssetAcceptanceDetail2);

						
						RejectAssetAccessory rejectAssetAccessory=new RejectAssetAccessory(
								new AssetReturnAccessory(accessory.getReturnAccessoriesId()), accessory.getRejectNote(),
								accessory.getRejectQuantity(), new RejectReason(accessory.getRejectReasonId()));
//						sessionService.setEntityDefaults(rejectAssetAccessory);
						rejectAssetAccessory.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
								request.getUserId());
						acceptanceDetail.addRejectedAssetAcceptanceDetail(rejectAssetAccessory);

						RejectedAssetAcceptanceDao.save(rejectedAsset);
					}
				}
			}
		}
	}

	private Batch updateStoreStock(Integer storeId, Long batchId, Integer quantity,Integer unitId, AssetIssueRequest request) {
		{
			// get batch needs to be issued
			Batch batch = GlobalDao.getBatch(storeId, batchId,unitId);

			// Get existing batch store stock
			Integer avaialableStoreStock = GlobalDao.getStoreStockByBatch(storeId, batchId,0);//0 itemId

			// Get lease quantity
			Integer leaseQuantity = quantity;

			Integer balanceQuantity = avaialableStoreStock + leaseQuantity;

			// Deduct issued quantity from the batch store stock
			batch.getStoreStockList().get(0).setAvailableQty(balanceQuantity);

			// Add stock transaction

			StockTransaction stockTransaction=new StockTransaction(balanceQuantity, screen, screen.getCode(), leaseQuantity,
					batch.getStoreStockList().get(0).getStore(),0);
//			sessionService.setEntityDefaults(stockTransaction);
			stockTransaction.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
					request.getUserId());
			batch.addStockTransaction(stockTransaction);

			return batch;
		}
	}
}
