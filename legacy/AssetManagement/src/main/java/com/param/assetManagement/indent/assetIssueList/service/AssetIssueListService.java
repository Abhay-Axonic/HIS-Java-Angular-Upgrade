package com.param.assetManagement.indent.assetIssueList.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.assetManagement.indent.assetIndent.dto.AssetIndentDropdown;
import com.param.assetManagement.indent.assetIssue.dao.IAssetIssueDao;
import com.param.assetManagement.indent.assetIssue.dto.AssetIssueDetailRequest;
import com.param.assetManagement.indent.assetIssue.dto.AssetIssueRequest;
import com.param.assetManagement.indent.assetIssue.dto.AssetIssueResponse;
import com.param.assetManagement.indent.assetIssueList.dao.IAssetIssueListDao;
import com.param.assetManagement.indent.assetIssueList.dto.AssetIssueItemResponse;
import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.adt.Priority;
import com.param.entity.model.asset.AssetIssue;
import com.param.entity.model.asset.AssetIssueAccessory;
import com.param.entity.model.asset.AssetIssueBatchMapper;
import com.param.entity.model.asset.AssetIssueDetail;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.IndentType;
import com.param.entity.model.inventory.StockTransaction;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.User;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dao.items.IItemsDao;
import com.param.global.dto.global.BatchProperty;
import com.param.global.dto.global.PickerResponse;
import com.param.global.dto.items.AsseccoriesResponse;
import com.param.global.dto.items.BatchResponse;
import com.param.global.dto.items.ItemsDetailsListResponse;
import com.param.global.dto.items.ItemsRequest;
import com.param.global.service.global.IGlobalService;
import com.param.global.utility.Util;
import com.param.service.global.SessionService;

@Service
public class AssetIssueListService implements IAssetIssueListService {

	@Autowired
	private SessionService sessionService;

	private Integer screenId = Screen.AssetIssue.getScreen();
	@Autowired
	IItemsDao ItemsDao;
	@Autowired
	IGlobalDao GlobalDao;

	@Autowired
	IAssetIssueListDao assetIssueListDao;

	@Autowired
	IAssetIssueDao AssetIssueDao;
	@Autowired
	IGlobalService GlobalService;
	private com.param.entity.model.master.Screen screen;

	@Override
	@Transactional
	public AssetIndentDropdown getDropdowns(Integer unitId, Integer orgId, Integer userId) {
		AssetIndentDropdown response = new AssetIndentDropdown();

		for (Store store : GlobalDao.getStoreList(unitId)) {
			response.addFromStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		}

		for (Store store : GlobalDao.getUserStoreList(unitId,orgId,userId)) {
			response.addToStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName(),
					store.getStoreType().getCode().equals("MS")));
		}

		for (ScreenStatus screenStatus : GlobalDao.getScreenStatus(screenId)) {
			if(screenStatus.getStatus().getId().equals(4) || screenStatus.getStatus().getId().equals(8))
			{
				response.addStatus(new com.param.global.dto.global.Status(screenStatus.getStatus().getId(),
						screenStatus.getStatus().getStatus()));	
			}
			
		}

		for (IndentType indentType : GlobalDao.getIndentTypeList(orgId,unitId)) {
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
	public Object getAssetIssueList(AssetIssueRequest request) {
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());
		List<AssetIssueResponse> response = new ArrayList<AssetIssueResponse>();

		for (AssetIssue assetIssue : assetIssueListDao.searchAssetIssueList(request, screenId)) {

			if (assetIssue.getAssetIndent() == null) {

				AssetIssueResponse assetIssueResponse = new AssetIssueResponse(assetIssue.getId(),
						assetIssue.getFromStore().getStoreId(), assetIssue.getFromStore().getStoreName(),
						assetIssue.getToStore().getStoreId(), assetIssue.getToStore().getStoreName(),
						assetIssue.getRemark(), assetIssue.getStatus().getStatus(), assetIssue.getStatus().getId(),
						assetIssue.getAddedDate(), assetIssue.getIssueNo());
				if(assetIssue.getIndentType()!=null)
				{
					assetIssueResponse.setType(assetIssue.getIndentType().getType());	
				}
				else
				{
					assetIssueResponse.setType("Direct");		
				}
				

				response.add(assetIssueResponse);
			} else {
				AssetIssueResponse assetIssueResponse = new AssetIssueResponse(assetIssue.getId(),
						assetIssue.getAssetIndent().getIndentNo(), assetIssue.getFromStore().getStoreId(),
						assetIssue.getFromStore().getStoreName(), assetIssue.getToStore().getStoreId(),
						assetIssue.getToStore().getStoreName(),
						assetIssue.getAssetIndent().getPriority().getPriorityDesc(),
						assetIssue.getAssetIndent().getIndentType().getType(), assetIssue.getRemark(),
						assetIssue.getStatus().getStatus(), assetIssue.getStatus().getId(), assetIssue.getAddedDate(),
						assetIssue.getIssueNo());

				response.add(assetIssueResponse);
			}

		}
		return response;
	}

	@Override
	@Transactional
	public Object saveAssetIssueList(AssetIssueRequest request) {
		String issueNumber = GlobalService.getBillNoByGeneratedCode(request.getFromStoreId(), request.getUnitId(), screenId);
		AssetIssue assetIssue = new AssetIssue(new User(request.getCarrierId()), new Store(request.getFromStoreId()),
				new Store(request.getToStoreId()), issueNumber!=null ? issueNumber : "AI-001", request.getRemark(), new Status(request.getStatusId()));

		for (AssetIssueDetailRequest item : request.getItem()) 
		{

			AssetIssueDetail detail = new AssetIssueDetail(item.getIssueQuantity(), new Item(item.getItemId()));
//			sessionService.setEntityDefaults(detail);
			detail.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
					request.getUserId());
			for (BatchResponse batch : item.getBatches())
			{

				if (batch != null) 
				{
					Batch issueBatch = BatchStoreTransaction(request.getToStoreId(), batch.getBatchId(),
							batch.getIssueQuantity(),request.getOrganizationId(), request.getUnitId(), request.getUserId(),detail);
//					sessionService.setEntityDefaults(issueBatch);
					issueBatch.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
							request.getUserId());
					// detail.addBatch(new AssetIssueBatchMapper(issueBatch,
					// batch.getIssueQuantity()));

					AssetIssueBatchMapper issueBatchMapper = new AssetIssueBatchMapper(issueBatch,batch.getIssueQuantity());
//					sessionService.setEntityDefaults(issueBatchMapper);
					issueBatchMapper.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
							request.getUserId());
					for (com.param.global.dto.items.AsseccoriesResponse asseccories : batch.getAsseccories())
					{
						AssetIssueAccessory assetIssueAccessory=new AssetIssueAccessory(new Batch(asseccories.getBatchId()),
								asseccories.getIssueQuantity());
//						sessionService.setEntityDefaults(assetIssueAccessory);
						assetIssueAccessory.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
								request.getUserId());
						issueBatchMapper.addAccessories(assetIssueAccessory);
					}
					detail.addBatch(issueBatchMapper);
				}
			}

			assetIssue.addAssetIssueDetail(detail);
		}
//		sessionService.setEntityDefaults(assetIssue);	
		assetIssue.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
				request.getUserId());
		assetIssueListDao.saveAssetIssueList(assetIssue);

		CommonResponse response = new CommonResponse();
		response.setId(assetIssue.getId().longValue());
		response.setMessage("Material Issue saved successfully");

		return response;
	}

	private Batch BatchStoreTransaction(Integer storeId, Long batchId, Integer quantity,Integer organizationId, Integer unitId, Integer userId , AssetIssueDetail d) {

		screen = GlobalDao.getScreen(screenId);

		// get batch needs to be issued
		Batch batch = GlobalDao.getBatch(storeId, batchId,unitId);

		// Get existing batch store stock
		Integer avaialableStoreStock = GlobalDao.getStoreStockByBatch(storeId, batchId,0);//0 itemId

		// Get lease quantity
		Integer leaseQuantity = quantity;

		Integer balanceQuantity = avaialableStoreStock - leaseQuantity;

		// Deduct issued quantity from the batch store stock
		batch.getStoreStockList().getFirst().setAvailableQty(balanceQuantity);
		Integer inTransitQuantity = GlobalDao.getTransitQuantityByBatch(storeId, batch.getId());
		Integer tQty = inTransitQuantity + leaseQuantity;
		batch.getStoreStockList().getFirst().settQty(tQty);
		// Add stock transaction
		StockTransaction stockTransaction=new StockTransaction(balanceQuantity, screen, screen.getCode(), leaseQuantity,
				batch.getStoreStockList().getFirst().getStore(), d.getId());
//		sessionService.setEntityDefaults(stockTransaction);	
		stockTransaction.setCreateDefaults(organizationId,unitId,
				userId);
		batch.addStockTransaction(stockTransaction);

		return batch;
	}

	@Override
	@Transactional
	public Object getAssetIssueListDetails(Integer id, Integer unitId, Integer orgId) {

		AssetIssue assetIssue = AssetIssueDao.getAssetIssue(id);

		AssetIssueResponse response = new AssetIssueResponse(assetIssue.getId(),
				assetIssue.getAssetIndent() != null ? assetIssue.getAssetIndent().getIndentNo() : null,
				assetIssue.getFromStore().getStoreId(), assetIssue.getFromStore().getStoreName(),
				assetIssue.getToStore().getStoreId(), assetIssue.getToStore().getStoreName(),
				assetIssue.getAssetIndent() != null ? assetIssue.getAssetIndent().getPriority().getPriorityDesc()
						: null,
				assetIssue.getAssetIndent() != null ? assetIssue.getAssetIndent().getIndentType().getType() : null,
				assetIssue.getRemark() != null ? assetIssue.getRemark() : null, assetIssue.getAddedDate(),
				assetIssue.getIssueNo(), assetIssue.getCarrier() != null ? assetIssue.getCarrier().getUserId() : 0,
				assetIssue.getCarrier() != null ? assetIssue.getCarrier().getUserName() : null);

		for (AssetIssueDetail assetIssueDetail : AssetIssueDao.getAssetIssueDetail(id)) {

			Long storeStock = GlobalDao.getStoreStockByItem(assetIssue.getFromStore().getStoreId(),
					assetIssueDetail.getItem().getId(),unitId);
			Long stockInTransit = GlobalDao.getStoreStockInTransitByItem(assetIssue.getFromStore().getStoreId(),
					assetIssueDetail.getItem().getId());
			Integer maxStoreQuantity = GlobalDao.getStoreMaxQuantityByItem(assetIssue.getFromStore().getStoreId(),
					assetIssueDetail.getItem().getId(),orgId);
			Long maxOrderQuantity = maxStoreQuantity - (storeStock + stockInTransit);
			Integer pendingQunatity = 0;
			if (assetIssueDetail.getAssetIndentDetail() != null) {
				pendingQunatity = assetIssueDetail.getAssetIndentDetail().getIndentQuantity()
						- assetIssueDetail.getIssueQuantity();
			}

			for (AssetIssueBatchMapper assetIssueBatchMapper : assetIssueDetail.getAssetIssueBatchMappers()) {

				List<AsseccoriesResponse> asseccoriesResponsesList = new ArrayList<AsseccoriesResponse>();
				for (AssetIssueAccessory issueAccessory : assetIssueBatchMapper.getAssetIssueAccessories()) {

					// Batch batchObj = assetIssueBatchMapper.getBatch();
					asseccoriesResponsesList.add(PopulateAccessories(issueAccessory));
				}
				AssetIssueItemResponse assetIssueItemResponse = new AssetIssueItemResponse(assetIssueDetail.getId(),
						assetIssueDetail.getItem().getId(), assetIssueDetail.getItem().getCode(),
						assetIssueDetail.getItem().getName(), null,
						assetIssueBatchMapper.getBatch().getId(), assetIssueBatchMapper.getBatch().getBatchNo(),
						assetIssueBatchMapper.getBatch().getExpiryDate(),
						assetIssueDetail.getAssetIndentDetail() != null
								? assetIssueDetail.getAssetIndentDetail().getIndentQuantity()
								: null,
						pendingQunatity != null ? pendingQunatity : null, assetIssueBatchMapper.getIssueQuantity(),
						maxOrderQuantity, asseccoriesResponsesList);

				response.addItems(assetIssueItemResponse);
			}
		}
		return response;
	}

	private static AsseccoriesResponse PopulateAccessories(AssetIssueAccessory issueAccessory) {

		return new AsseccoriesResponse(issueAccessory.getBatch().getId(), issueAccessory.getBatch().getBatchNo(),
				issueAccessory.getBatch().getItem().getId(), issueAccessory.getBatch().getItem().getCode(),
				issueAccessory.getBatch().getItem().getName(),
				issueAccessory.getBatch().getStoreStockList().getFirst().getAvailableQty());
	}

	@Override
	@Transactional
	public List<ItemsDetailsListResponse> batchDetails(ItemsRequest itemsRequest) {

		List<ItemsDetailsListResponse> response = ItemsDao.fixedItemsDetails(itemsRequest.getItemIds(),
				itemsRequest.getStoreId(),itemsRequest.getUnitId());

		for (ItemsDetailsListResponse item : response) 
		{

			Long storeStock = GlobalDao.getStoreStockByItem(itemsRequest.getStoreId(), item.getItemId(),itemsRequest.getUnitId());
			Long stockInTransit = GlobalDao.getstockInTransitByItem(itemsRequest.getStoreId(), item.getItemId(),itemsRequest.getUnitId());
			Integer maxStoreQuantity = GlobalDao.getStoreMaxQuantityByItem(itemsRequest.getStoreId(), item.getItemId(),itemsRequest.getOrganizationId());
			
			//Integer maxStoreQuantity = GlobalDao.getStoreMaxQuantityByItem(itemsRequest.getStoreId(), item.getItemId(),itemsRequest.getOrganizationId());
			//Long maxOrderQuantity = maxStoreQuantity - (storeStock + stockInTransit);
			
			Long approvedPRAndPOQuantity = ItemsDao.getApprovedPRandPOQuantity(itemsRequest.getStoreId(), item.getItemId(),itemsRequest.getUnitId());
            //System.out.println("approvedPRAndPOQuantity:-" + approvedPRAndPOQuantity);

			Long approvedPOQuantity = ItemsDao.getApprovedPurchaseOrderQuantity(itemsRequest.getStoreId(), item.getItemId(),itemsRequest.getUnitId());
            //System.out.println("approvedPOQuantity:-" + approvedPOQuantity);

			//System.out.println("maxStoreQuantity:-" + maxStoreQuantity + "\nstoreStock : " + storeStock+"\nstockInTransit : "+stockInTransit+"\napprovedPRAndPOQuantity : "+approvedPRAndPOQuantity+"\n approvedPOQuantity : "+approvedPOQuantity);

			Long maxOrderQuantity = GlobalDao.getMaxOrderQuantity(maxStoreQuantity,storeStock,stockInTransit , approvedPRAndPOQuantity , approvedPOQuantity);
			

			if (maxOrderQuantity < 0) {
				maxOrderQuantity = 0L;
			}
			//item.setUom(GlobalDao.getUnitOfMeasurementListByItem(item.getItemId(),itemsRequest.getOrganizationId()));
			item.setStockAvailable(GlobalDao.getStoreStockByItem(itemsRequest.getToStoreId(), item.getItemId(),itemsRequest.getUnitId()));
			item.setMaxOrderQuantity(maxOrderQuantity);
		//	BatchProperty batchProperty = new BatchProperty();
		//	batchProperty.setIsIndent(false);
			
			BatchProperty batchProperty = new BatchProperty();
			batchProperty.setIsIndent(false);
			List<Batch> batchList = GlobalDao.getBatchDetailsForAsset(item.getItemId(), itemsRequest.getToStoreId(),
					batchProperty,itemsRequest.getUnitId());

			for (Batch batch : GlobalDao.getBatchDetailsForAsset(item.getItemId(), itemsRequest.getToStoreId(),
					batchProperty,itemsRequest.getUnitId())) 
			{
				item.addBatch(Util.PopulateBatchWithStock(batch));
			}
			item.setCarriers(PopulateCarriers(itemsRequest.getOrganizationId(),itemsRequest.getUnitId()));
		}
		
		return response;
	}
	private List<PickerResponse> PopulateCarriers(Integer orgId, Integer unitId) {
		List<PickerResponse> pickerResponseList = new ArrayList<PickerResponse>();

		for (User user : GlobalDao.getUser(orgId,unitId)) {
			PickerResponse pickerResponse = new PickerResponse(user.getUserId(), user.getUserName());
			pickerResponseList.add(pickerResponse);
		}
		return pickerResponseList;
	}

	@Override
	@Transactional
	public Object carriersList(Integer orgId, Integer unitId) {
		return PopulateCarriers(orgId,unitId);
	}
}
