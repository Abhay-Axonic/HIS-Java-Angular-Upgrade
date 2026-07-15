package com.param.assetManagement.indent.assetReturnAcceptance.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.assetManagement.indent.assetIndent.dto.AssetIndentDropdown;
import com.param.assetManagement.indent.assetIndent.dto.AssetIndentRequest;
import com.param.assetManagement.indent.assetIndent.dto.AssetIndentRequestAccessories;
import com.param.assetManagement.indent.assetIndent.dto.AssetIndentRequestItem;
import com.param.assetManagement.indent.assetIssue.dao.IAssetIssueDao;
import com.param.assetManagement.indent.assetReturn.dto.AssetReturnRequest;
import com.param.assetManagement.indent.assetReturn.dto.AssetReturnResponse;
import com.param.assetManagement.indent.assetReturn.dto.ItemResponse;
import com.param.assetManagement.indent.assetReturnAcceptance.dao.IAssetReturnAcceptanceDao;
import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.asset.AssetIssue;
import com.param.entity.model.asset.AssetIssueAccessory;
import com.param.entity.model.asset.AssetReturn;
import com.param.entity.model.asset.AssetReturnAccessory;
import com.param.entity.model.asset.AssetReturnDetail;
import com.param.entity.model.asset.RejectAssetAccessory;
import com.param.entity.model.asset.RejectedAssetAcceptance;
import com.param.entity.model.asset.RejectedAssetAcceptanceDetail;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.IndentType;
import com.param.entity.model.inventory.StockTransaction;
import com.param.entity.model.inventory.StoreStock;
import com.param.entity.model.master.RejectReason;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.User;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dto.items.AsseccoriesResponse;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service
public class AssetReturnAcceptanceService implements IAssetReturnAcceptanceService {

	@Autowired
	private SessionService sessionService;

	private Integer screenId = Screen.AssetReturnAcceptance.getScreen();

	private com.param.entity.model.master.Screen screen;

	@Autowired
	IAssetReturnAcceptanceDao AssetReturnAcceptanceDao;

	@Autowired
	IGlobalDao GlobalDao;
	@Autowired
	IAssetIssueDao AssetIssueDao;

	@Override
	@Transactional
	public AssetIndentDropdown getDropdowns(Integer unitId, Integer orgId, Integer userId) {
		AssetIndentDropdown response = new AssetIndentDropdown();

		for (Store store : GlobalDao.getUserStoreList(unitId, orgId, userId)) {
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
		for (IndentType indentType : GlobalDao.getIndentTypeList(orgId,unitId)) {
			response.addIndentType(
					new com.param.global.dto.global.IndentType(indentType.getId(), indentType.getType()));
		}

		return response;
	}

	@Override
	@Transactional
	public Object getAssetReturnAcceptanceList(AssetIndentRequest request) {
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());
		return AssetReturnAcceptanceDao.searchAssetReturnAcceptanceList(request, screenId);
	}

	@Override
	@Transactional
	public Object updateAssetReturnAcceptance(AssetReturnRequest request) {

		screen = GlobalDao.getScreen(screenId);

		AssetReturn assetReturn = AssetReturnAcceptanceDao.getAssetReturn(request.getId());

		Integer storeId = assetReturn.getFromStore().getStoreId();

		for (AssetReturnDetail detail : AssetReturnAcceptanceDao.getAssetReturnDetail(request.getId())) {

			for (AssetIndentRequestItem item : request.getItems()) {
				if (detail.getAssetIssueDetail() != null) {
					AssetIssue issue = detail.getAssetIssueDetail().getAssetIssue();
					issue.setIsReturn(true);
					AssetReturnAcceptanceDao.issueUpdate(issue);
				}
				if (item.getId().equals(detail.getId()) && detail.getIsActive() && !detail.getIsDeleted()) {
					/*
					 * detail.setAcceptedQuantity(item.getAcceptedQuantity());
					 * detail.setRejectQuantity(item.getRejectQuantity() != null ?
					 * item.getRejectQuantity() : 0); detail.setRejectReason(new
					 * RejectReason(item.getRejectReasonId()));
					 * detail.setRejectNote(item.getRejectNote()); detail.setUpdatedDate(new
					 * Date()); detail.setUpdatedBy(assetReturn.getUser()); detail.setStatus(new
					 * Status(item.getStatusId())); assetReturn.addAssetReturnDetail(detail);
					 */

					detail.updateAssetReturnDetail(item.getAcceptedQuantity(), item.getRejectNote(),
							item.getRejectQuantity() != null ? item.getRejectQuantity() : 0,
							item.getRejectReasonId() != null ? new RejectReason(item.getRejectReasonId()) : null,
							new Status(item.getStatusId()));

					updateStoreStock( assetReturn.getToStore().getStoreId(),assetReturn.getFromStore().getStoreId(),
							detail.getBatch(), item.getAcceptedQuantity(), assetReturn.getUnit().getUnitId(),
							request, detail);

					for (AssetReturnAccessory accessories : detail.getAssetReturnAccessories()) {

						for (AssetIndentRequestAccessories accessory : item.getAccessories()) {
							if (accessory.getId().equals(accessories.getId()) && accessories.getIsActive()
									&& !accessories.getIsDeleted()) {
								accessories.updateAssetReturnAccessories(accessory.getApprovedQuantity(),
										new RejectReason(accessory.getRejectReasonId()), accessory.getRejectNote(),
										accessory.getRejectQuantity());
								accessories.setUpdatedDate(TimeZoneComponent.getDateByzone(assetReturn.getUnitId()));
								accessories.setUpdatedByUser(
										request.getUserId() != null ? request.getUserId() : sessionService.getUserId());

							}
						}

					}
				}

			}
			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(assetReturn.getUnitId()));
			detail.setUpdatedByUser(request.getUserId() != null ? request.getUserId() : sessionService.getUserId());
			saveAssetReturn(detail, assetReturn, request);

		}
		// assetReturn.setApprovalStatus(new Status(request.getApprovalStatusId()));
		assetReturn.setUpdatedDate(TimeZoneComponent.getDateByzone(assetReturn.getUnitId()));
		assetReturn.setUpdatedByUser(request.getUserId() != null ? request.getUserId() : sessionService.getUserId());
		assetReturn.setStatus(new Status(4));
		assetReturn.setApprovalStatus(new Status(5));
		AssetReturnAcceptanceDao.updateAssetReturn(assetReturn);

		CommonResponse response = new CommonResponse();
		response.setId(assetReturn.getId().longValue());
		response.setMessage("Asset Return Acceptance Updated successfully");
		return response;
	}

	private void saveAssetReturn(AssetReturnDetail detail, AssetReturn assetReturn, AssetReturnRequest request) {

		Boolean flag = false;
		RejectedAssetAcceptance rejectedAssetAcceptance = new RejectedAssetAcceptance(
				new User(sessionService.getUserId()), TimeZoneComponent.getDateByzone(assetReturn.getUnitId()), "RAA-001", new Status(2),
				new Store(assetReturn.getFromStore().getStoreId()), new Store(assetReturn.getToStore().getStoreId()));
		rejectedAssetAcceptance.setAssetReturn(assetReturn);
//		sessionService.setEntityDefaults(rejectedAssetAcceptance);	
		rejectedAssetAcceptance.setCreateDefaults(request.getOrganizationId(), request.getUnitId(),
				request.getUserId());
		RejectedAssetAcceptanceDetail acceptanceDetail = null;

		for (AssetIndentRequestItem item : request.getItems()) {
			if (item.getRejectQuantity() != null && item.getRejectQuantity() > 0) {
				flag = true;
				RejectedAssetAcceptanceDetail rejectedAssetAcceptanceDetail = new RejectedAssetAcceptanceDetail(
						item.getRejectNote(), item.getRejectQuantity(),
						item.getRejectReasonId() != null ? new RejectReason(item.getRejectReasonId()) : null,
						new AssetReturnDetail(detail.getId()));
				rejectedAssetAcceptanceDetail.setAssetIssueBatchMapper(detail.getAssetIssueBatchMapper());
				acceptanceDetail = rejectedAssetAcceptance
						.addRejectedAssetAcceptanceDetail(rejectedAssetAcceptanceDetail);
				
//				sessionService.setEntityDefaults(rejectedAssetAcceptanceDetail);	
				rejectedAssetAcceptanceDetail.setCreateDefaults(request.getOrganizationId(), request.getUnitId(),
						request.getUserId());
				for (AssetIndentRequestAccessories accessories : item.getAccessories()) {
					RejectAssetAccessory rejectAssetAccessory = new RejectAssetAccessory(
							new AssetIssueAccessory(accessories.getAccessoryId()), item.getRejectNote(),
							item.getRejectQuantity(), new RejectReason(item.getRejectReasonId()));
//					  sessionService.setEntityDefaults(rejectAssetAccessory);	
					rejectAssetAccessory.setCreateDefaults(request.getOrganizationId(), request.getUnitId(),
							request.getUserId());
					acceptanceDetail.addRejectAssetAccessory(rejectAssetAccessory);
				}

				acceptanceDetail.setCreateDefaults(request.getOrganizationId(), request.getUnitId(),
						request.getUserId());
			}

		}
//		sessionService.setEntityDefaults(acceptanceDetail);	

		if (rejectedAssetAcceptance.getRejectedAssetAcceptanceDetails() != null) {
			AssetReturnAcceptanceDao.save(rejectedAssetAcceptance);
		}
	}

	@Override
	@Transactional
	public Object getAssetReturnAcceptanceDetails(Integer id, Integer unitId) {

		AssetReturn assetReturn = AssetReturnAcceptanceDao.getAssetReturn(id);

		AssetReturnResponse response = new AssetReturnResponse(assetReturn.getId(), assetReturn.getAddedDate(),
				assetReturn.getReturnNo(), assetReturn.getFromStore().getStoreId(),
				assetReturn.getFromStore().getStoreName(), assetReturn.getToStore().getStoreId(),
				assetReturn.getToStore().getStoreName(), assetReturn.getAddedBy().getUserName(),
				assetReturn.getRemark(), assetReturn.getStatus().getId(), assetReturn.getStatus().getStatus(),
				assetReturn.getApprovalStatus().getStatus(), assetReturn.getApprovalStatus().getId());

		for (AssetReturnDetail detail : AssetReturnAcceptanceDao.getAssetReturnDetail(id)) {

			Long storeStock = GlobalDao.getStoreStockByItem(assetReturn.getFromStore().getStoreId(),
					detail.getBatch().getItem().getId(), unitId);

			Long availableQuantity = storeStock;

			List<AsseccoriesResponse> asseccoriesResponsesList = new ArrayList<AsseccoriesResponse>();
			for (AssetReturnAccessory issueAccessory : detail.getAssetReturnAccessories()) {

				asseccoriesResponsesList.add(PopulateAccessories(issueAccessory));
			}
			ItemResponse itmResponse = new ItemResponse(detail.getBatch().getItem().getId(),
					detail.getBatch().getItem().getCode(), detail.getBatch().getItem().getName(),
					detail.getBatch().getId(), detail.getBatch().getExpiryDate(), availableQuantity, detail.getId(),
					detail.getReturnReason().getReasonMasterId(), detail.getReturnQuantity(),
					detail.getReturnReason().getReasonDesc(), detail.getBatch().getBatchNo(),
					detail.getBatch().getItem().getGeneric() != null
							? detail.getBatch().getItem().getGeneric().getName()
							: "",
					asseccoriesResponsesList);
			itmResponse.setAcceptedQuantity(detail.getAcceptedQuantity() != null ? detail.getAcceptedQuantity() : null);
			itmResponse.setRejectQuantity(detail.getRejectQuantity() != null ? detail.getRejectQuantity() : null);
			response.addItems(itmResponse);
		}
		return response;
	}

	private static AsseccoriesResponse PopulateAccessories(AssetReturnAccessory issueAccessory) {

		return new AsseccoriesResponse(issueAccessory.getBatch().getId(), issueAccessory.getBatch().getBatchNo(),
				issueAccessory.getBatch().getItem().getId(), issueAccessory.getBatch().getItem().getCode(),
				issueAccessory.getBatch().getItem().getName(),
				issueAccessory.getBatch().getStoreStockList().getFirst().getAvailableQty());
	}

	private Batch updateStoreStock(Integer storeId , Integer acceptingStore,	Batch batch, Integer quantity,
			Integer unitId, AssetReturnRequest request , AssetReturnDetail d) {
		{
			// get batch needs to be issued
		//	Batch batch = GlobalDao.getBatch(storeId, batchId, unitId);

			// Get existing batch store stock
			Integer avaialableStoreStock = GlobalDao.getStoreStockByBatch(storeId, batch.getId(), 0);// 0 itemId

			Integer balanceQuantity = avaialableStoreStock - quantity;

			// Deduct issued quantity from the batch store stock
			// batch.getStoreStockList().get(0).settQty(quantity);

			//batch.getStoreStockList().get(0).setAvailableQty(balanceQuantity);

			//batch.getStoreStockList().get(0).setCreateDefaults(request.getOrganizationId(), request.getUnitId(),request.getUserId());
			List<StoreStock> fromStoreStock = batch.getStoreStockList().stream()
					.filter(i -> i.getStore().getStoreId().equals(storeId)).collect(Collectors.toList());
			// Add stock transaction
			Integer inTransitQuantity = GlobalDao.getTransitQuantityByBatch(storeId, batch.getId());
			Integer tQty = inTransitQuantity - quantity;
			if(!fromStoreStock.isEmpty()) {
				//fromStoreStock.get(0).setAvailableQty(balanceQuantity);	
				fromStoreStock.getFirst().settQty(tQty);
			}
			StockTransaction stockTransaction = new StockTransaction(balanceQuantity, screen, screen.getCode(),
					quantity, batch.getStoreStockList().getFirst().getStore(), d.getId());
			stockTransaction.setCreateDefaults(request.getOrganizationId(), request.getUnitId(), request.getUserId());
			batch.addStockTransaction(stockTransaction);

			batch.setCreateDefaults(request.getOrganizationId(), request.getUnitId(), request.getUserId());

			Integer avaialableStoreStock1 = GlobalDao.getStoreStockByBatch(acceptingStore, batch.getId(), 0);// 0 itemId
			Integer balanceQuantity1 = avaialableStoreStock1 + quantity;
			//Batch batch1 = GlobalDao.getBatch(acceptingStore, batchId, unitId);
			//Batch batch1 =	GlobalDao.getBatch(batchId);
			List<StoreStock> toStoreStock = batch.getStoreStockList().stream()
					.filter(i -> i.getStore().getStoreId().equals(acceptingStore)).collect(Collectors.toList());
			if (toStoreStock != null && toStoreStock.size() > 0) {
				toStoreStock.getFirst().setAvailableQty(balanceQuantity1);
				//toStoreStock.get(0).setTotalQty(toStoreStock.get(0).getTotalQty()+quantity);
				StockTransaction stockTransaction1 = new StockTransaction(avaialableStoreStock1, screen,
						screen.getCode(), quantity, batch.getStoreStockList().getFirst().getStore(), d.getId());
//				sessionService.setEntityDefaults(stockTransaction);
				stockTransaction1.setCreateDefaults(request.getOrganizationId(), request.getUnitId(),
						request.getUserId());
				batch.addStockTransaction(stockTransaction1);
			} else {
				StoreStock storeStockNew = new StoreStock(quantity, new Store(acceptingStore),
						new Batch(batch.getId()));
				storeStockNew.setTotalQty(quantity);
				storeStockNew.setCreateDefaults(request.getOrganizationId(), request.getUnitId(), request.getUserId());
				AssetIssueDao.saveStoreStock(storeStockNew);
				StockTransaction stockTransaction1 = new StockTransaction(avaialableStoreStock, screen,
						screen.getCode(), quantity, batch.getStoreStockList().getFirst().getStore(), d.getId());
//				sessionService.setEntityDefaults(stockTransaction1);
				stockTransaction1.setCreateDefaults(request.getOrganizationId(), request.getUnitId(),
						request.getUserId());
				batch.addStockTransaction(stockTransaction1);
			}

			return batch;
		}
	}
}
