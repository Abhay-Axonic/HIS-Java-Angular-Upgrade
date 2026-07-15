package com.param.assetManagement.indent.assetIssue.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.assetManagement.indent.assetIndent.dto.AssetIndentDropdown;
import com.param.assetManagement.indent.assetIndent.dto.AssetIndentRequest;
import com.param.assetManagement.indent.assetIssue.dao.IAssetIssueDao;
import com.param.assetManagement.indent.assetIssue.dto.AssetIssueDetailRequest;
import com.param.assetManagement.indent.assetIssue.dto.AssetIssueDetailResponse;
import com.param.assetManagement.indent.assetIssue.dto.AssetIssueRequest;
import com.param.assetManagement.indent.assetIssue.dto.AssetIssueResponse;
import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.adt.Priority;
import com.param.entity.model.asset.AssetIndentDetail;
import com.param.entity.model.asset.AssetIssue;
import com.param.entity.model.asset.AssetIssueAccessory;
import com.param.entity.model.asset.AssetIssueBatchMapper;
import com.param.entity.model.asset.AssetIssueDetail;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.BatchAccessoriesMapper;
import com.param.entity.model.inventory.IndentType;
import com.param.entity.model.inventory.StockTransaction;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.User;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dto.global.BatchProperty;
import com.param.global.dto.global.PickerResponse;
import com.param.global.dto.items.AsseccoriesResponse;
import com.param.global.dto.items.BatchResponse;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service
public class AssetIssueService implements IAssetIssueService {

	@Autowired
	private SessionService sessionService;

	private Integer screenId = Screen.AssetIssue.getScreen();

	@Autowired
	IGlobalDao GlobalDao;

	@Autowired
	IAssetIssueDao AssetIssueDao;

	private com.param.entity.model.master.Screen screen;

	@Override
	@Transactional
	public AssetIndentDropdown getDropdowns(Integer unitId, Integer orgId, Integer userId) {
		AssetIndentDropdown response = new AssetIndentDropdown();

		for (Store store : GlobalDao.getStoreList(unitId)) {
			response.addFromStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		}

		for (Store store : GlobalDao.getUserStoreList(unitId, orgId, userId)) {
			response.addToStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName(),
					store.getStoreType().getCode().equals("MS")));
		}

		for (ScreenStatus screenStatus : GlobalDao.getScreenStatus(screenId)) {
			response.addStatus(new com.param.global.dto.global.Status(screenStatus.getStatus().getId(),
					screenStatus.getStatus().getStatus()));
		}

		for (IndentType indentType : GlobalDao.getIndentTypeList(orgId, unitId)) {
			response.addIndentType(
					new com.param.global.dto.global.IndentType(indentType.getId(), indentType.getType()));
		}

		for (Priority priority : GlobalDao.getPriorityList()) {
			response.addPriority(
					new com.param.global.dto.global.Priority(priority.getPriorityId(), priority.getPriorityDesc()));
		}

		return response;
	}

	@Override
	@Transactional
	public Object getAssetIssuePendingList(AssetIndentRequest request) {
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());
		return AssetIssueDao.searchAssetIssuePendingList(request, screenId);
	}

	@Override
	@Transactional
	public Object saveAssetIssuePending(AssetIssueRequest request) {

		screen = GlobalDao.getScreen(screenId);
		AssetIssue assetIssue = AssetIssueDao.getAssetIssue(request.getId());

		assetIssue.updateAssetIssue(new User(request.getCarrierId()), request.getRemark(),
				new Status(request.getStatusId()));

		Integer storeId = assetIssue.getAssetIndent().getToStore().getStoreId();

		for (AssetIssueDetail detail : AssetIssueDao.getAssetIssueDetail(request.getId())) {

			for (AssetIssueDetailRequest item : request.getItem()) {
				if (item.getId() != null && item.getId().equals(detail.getId())) {
					detail.setIssueQuantity(item.getIssueQuantity());
					detail.setUpdatedDate(TimeZoneComponent.getDateByzone(assetIssue.getUnitId()));
//					detail.setUpdatedBy(detail.getUser());
					detail.setUpdatedByUser(request.getUserId());
					AssetIndentDetail assetIndentDetail = detail.getAssetIndentDetail();
					assetIndentDetail
							.setPendingQuantity(assetIndentDetail.getPendingQuantity() - item.getIssueQuantity());
					// sessionService.setEntityDefaults(assetIndentDetail);
					for (AssetIssueBatchMapper batchMapper : AssetIssueDao.getAssetIssueBatchList(item.getId())) {
						Integer issueBatchId = null;

						for (BatchResponse batch : item.getBatches()) {
							if (batch.getId() != null && batch.getId().equals(batchMapper.getId())) {
								issueBatchId = batch.getId();

								Batch issueBatch = BatchStoreTransaction(storeId, batch.getBatchId(),
										batchMapper.getIssueQuantity(), assetIssue.getUnit().getUnitId(), request, assetIndentDetail.getItem(), assetIssue.getIssueNo(),detail);

								batchMapper.updateBatchMapper(issueBatch, batch.getIssueQuantity());

								break;
							}
						}

						if (issueBatchId == null) {
							batchMapper.setIsDeleted(true);
						}

						batchMapper.setUpdatedDate(TimeZoneComponent.getDateByzone(assetIssue.getUnitId()));
						batchMapper.setUpdatedByUser(request.getUserId());
					}
					for (BatchResponse batch : item.getBatches()) {
						if (batch.getId() == null) {

							Batch issueBatch = BatchStoreTransaction(storeId, batch.getBatchId(),
									batch.getIssueQuantity(), assetIssue.getUnit().getUnitId(), request, assetIndentDetail.getItem(), assetIssue.getIssueNo(),detail);
							// sessionService.setEntityDefaults(issueBatch);
							AssetIssueBatchMapper issueBatchMapper = new AssetIssueBatchMapper(issueBatch,
									batch.getIssueQuantity());
//							sessionService.setEntityDefaults(issueBatchMapper);
							issueBatchMapper.setCreateDefaults(request.getOrganizationId(), request.getUnitId(),
									request.getUserId());
							if (batch.getAsseccories() != null) {
								for (com.param.global.dto.items.AsseccoriesResponse asseccories : batch
										.getAsseccories()) {

									AssetIssueAccessory sssetIssueAccessory1 = new AssetIssueAccessory(
											new Batch(asseccories.getBatchId()), asseccories.getIssueQuantity());
									sssetIssueAccessory1.setCreateDefaults(request.getOrganizationId(),
											request.getUnitId(), request.getUserId());
									issueBatchMapper.addAccessories(sssetIssueAccessory1);
								}
							}
							detail.addBatch(issueBatchMapper);
						}
					}
					break;
				}
			}
		}
		AssetIssueDao.updateMaterialIssue(assetIssue);

		CommonResponse response = new CommonResponse();
		response.setId(assetIssue.getId().longValue());
		response.setMessage("Asset Issue Pending saved successfully");

		return response;
	}

	private Batch BatchStoreTransaction(Integer storeId, Long batchId, Integer quantity, Integer unitId,
			AssetIssueRequest request, Item item, String transitNo, AssetIssueDetail detail) {

		// get batch needs to be issued
		Batch batch = GlobalDao.getBatch(storeId, batchId, unitId);

		// Get existing batch store stock
		Integer avaialableStoreStock = GlobalDao.getStoreStockByBatch(storeId, batchId, 0);// 0 itemId

		Integer leaseQuantity = quantity;

		Integer balanceQuantity = avaialableStoreStock - leaseQuantity;

		// Deduct issued quantity from the batch store stock
		batch.getStoreStockList().get(0).setAvailableQty(balanceQuantity);
		Integer inTransitQuantity = GlobalDao.getTransitQuantityByBatch(storeId, batch.getId());
		Integer tQty = inTransitQuantity + leaseQuantity;
		batch.getStoreStockList().get(0).settQty(tQty);
		// Add stock transaction
		StockTransaction st = new StockTransaction(balanceQuantity, screen, screen.getCode(), leaseQuantity,
				batch.getStoreStockList().get(0).getStore(),detail.getId());
		st.setIsTransit(true);
		st.setItem(item);
		st.setTransitNo(transitNo);
		st.setCreateDefaults(request.getOrganizationId(), request.getUnitId(), request.getUserId());
		batch.addStockTransaction(st);

		return batch;
	}

	@Override
	@Transactional
	public Object getAssetIssuePendingDetails(Integer id, Integer unitId, Integer orgId) {

		AssetIssueResponse response = AssetIssueDao.getAssetIssuePending(id);
		if (response.getStatus().equalsIgnoreCase("Pending")) {
			for (AssetIssueDetailResponse issueDetails : AssetIssueDao.getAssetIssuePendingDetail(id)) {
				issueDetails.setStockAvailable(
						GlobalDao.getStoreStockByItem(response.getToStoreId(), issueDetails.getItemId(), unitId));

				BatchProperty batchProperty = new BatchProperty();
				batchProperty.setIsIndent(false);
				List<Batch> batchList = GlobalDao.getBatchDetailsForAsset(issueDetails.getItemId(),
						response.getToStoreId(), batchProperty, unitId);

				for (Batch batch : batchList) {
					issueDetails.addBatches(PopulateBatchWithStock(batch));
				}
				response.addItems(issueDetails);
			}
			response.setCarriers(PopulateCarriers(orgId, unitId));
			return response;
		} else {

			for (AssetIssueDetailResponse issueDetails : AssetIssueDao.getAssetIssuePendingDetail(id)) {
				 List<BatchResponse> batches=new ArrayList<>();
				for (AssetIssueBatchMapper assetIssueBatchMapper : AssetIssueDao.getAsssetIssueBatchList(issueDetails.getId())) {
					BatchResponse b=PopulateBatchWithStock1(assetIssueBatchMapper.getBatch(), assetIssueBatchMapper.getIssueQuantity());
					batches.add(b);
					//issueDetails.setBatches(PopulateBatchWithStock1(assetIssueBatchMapper.getBatch(), assetIssueBatchMapper.getIssueQuantity()));

				}
				issueDetails.setBatches(batches);
				issueDetails.setStockAvailable(
						GlobalDao.getStoreStockByItem(response.getToStoreId(), issueDetails.getItemId(), unitId));
				BatchProperty batchProperty = new BatchProperty();
				batchProperty.setIsIndent(false);
				List<Batch> batchList = GlobalDao.getBatchDetailsForAsset(issueDetails.getItemId(),
						response.getToStoreId(), batchProperty, unitId);

				for (Batch batch : batchList) {
					//issueDetails.addBatch(PopulateBatchWithStock(batch));
					issueDetails.addBatches(PopulateBatchWithStock(batch));
				}
				response.addItems(issueDetails);
			}
			response.setCarriers(PopulateCarriers(orgId, unitId));
			return response;
		}

	}

	public static BatchResponse PopulateBatchWithStock(Batch batch) {
		BatchResponse batchResponse = new BatchResponse();
		List<AsseccoriesResponse> asseccoriesResponsesList = new ArrayList<AsseccoriesResponse>();
		for (BatchAccessoriesMapper accessories : batch.getBatchAccessoriesMapperList()) {
			asseccoriesResponsesList.add(PopulateAccessories(accessories));
		}
		batchResponse = new BatchResponse(batch.getId(), batch.getBatchNo(), batch.getExpiryDate(),
				batch.getStoreStockList().get(0).getAvailableQty(), asseccoriesResponsesList, batch.getSellingPrice());
		return batchResponse;
	}
	public static BatchResponse PopulateBatchWithStock1(Batch batch , Integer issueQty) {
		BatchResponse batchResponse = new BatchResponse();
		List<AsseccoriesResponse> asseccoriesResponsesList = new ArrayList<AsseccoriesResponse>();
		for (BatchAccessoriesMapper accessories : batch.getBatchAccessoriesMapperList()) {
			asseccoriesResponsesList.add(PopulateAccessories(accessories));
		}
		batchResponse = new BatchResponse(batch.getId(), batch.getBatchNo(), batch.getExpiryDate(),
				batch.getStoreStockList().get(0).getAvailableQty(), asseccoriesResponsesList, batch.getSellingPrice());
		batchResponse.setIssueQuantity(issueQty);
		return batchResponse;
	}
	private static AsseccoriesResponse PopulateAccessories(BatchAccessoriesMapper accessories) {

		return new AsseccoriesResponse(accessories.getBatchAccessory().getId(),
				accessories.getBatchAccessory().getBatchNo(), accessories.getBatchAccessory().getItem().getId(),
				accessories.getBatchAccessory().getItem().getCode(),
				accessories.getBatchAccessory().getItem().getName(),
				accessories.getBatchAccessory().getStoreStockList().get(0).getAvailableQty());
	}

	private List<PickerResponse> PopulateCarriers(Integer orgId, Integer unitId) {
		List<PickerResponse> pickerResponseList = new ArrayList<PickerResponse>();

		for (User user : GlobalDao.getPickerUsers(unitId)) {
			PickerResponse pickerResponse = new PickerResponse(user.getUserId(), user.getUserName());
			pickerResponseList.add(pickerResponse);
		}
		/*
		for (User user : GlobalDao.getUser(orgId, unitId)) {
			PickerResponse pickerResponse = new PickerResponse(user.getUserId(), user.getUserName());
			pickerResponseList.add(pickerResponse);
		}*/
		return pickerResponseList;
	}
}
