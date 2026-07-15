package com.param.assetManagement.indent.assetIssueAcceptance.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.assetManagement.indent.assetIndent.dto.AssetIndentDropdown;
import com.param.assetManagement.indent.assetIssue.dao.IAssetIssueDao;
import com.param.assetManagement.indent.assetIssue.dto.AssetIssueRequest;
import com.param.assetManagement.indent.assetIssue.dto.AssetIssueResponse;
import com.param.assetManagement.indent.assetIssueAcceptance.dao.IAssetIssueAcceptanceDao;
import com.param.assetManagement.indent.assetIssueAcceptance.dto.AccessoriesRequest;
import com.param.assetManagement.indent.assetIssueAcceptance.dto.ItemRequest;
import com.param.assetManagement.indent.assetIssueList.dto.AssetIssueItemResponse;
import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.adt.Priority;
import com.param.entity.model.asset.AssetIssue;
import com.param.entity.model.asset.AssetIssueAccessory;
import com.param.entity.model.asset.AssetIssueBatchMapper;
import com.param.entity.model.asset.AssetIssueDetail;
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
public class AssetIssueAcceptanceService implements IAssetIssueAcceptanceService {
	
	@Autowired
	private SessionService sessionService;

	private Integer screenId = Screen.AssetIssueAcceptance.getScreen();

	private com.param.entity.model.master.Screen screen;

	@Autowired
	IGlobalDao GlobalDao;

	@Autowired
	IAssetIssueAcceptanceDao AssetIssueAcceptanceDao;

	@Autowired
	IAssetIssueDao AssetIssueDao;

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
	public Object getAssetIssueAcceptanceList(AssetIssueRequest request) {
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());
		List<AssetIssueResponse> list=AssetIssueAcceptanceDao.searchAssetIssueAcceptanceList(request, screenId);
		for(AssetIssueResponse ai : list)
		{
			if(ai.getType()==null)
			{
				ai.setType("Direct");
			}
		}
		return list;
	}

	@Override
	@Transactional
	public Object saveAssetIssueAcceptance(AssetIssueRequest request)
	{
		screen = GlobalDao.getScreen(screenId);
		AssetIssue assetIssue = AssetIssueDao.getAssetIssue(request.getId());
		for (AssetIssueDetail detail : AssetIssueDao.getAssetIssueDetail(request.getId())) 
		{

			for (AssetIssueBatchMapper batchMapper : AssetIssueDao.getAssetIssueBatchList(detail.getId())) {

			

					for (ItemRequest batches : request.getItemRequest()) {

						if (batches.getBatchMapperId() != null && batches.getBatchMapperId().equals(batchMapper.getId())) {

							batchMapper.updateAssetIssueBatchMapper(batches.getBatchMapperId(),
									batches.getAcceptedQuantity(), batches.getRejectNote(),
									(batches.getRejectQuantity() != null ? batches.getRejectQuantity() : 0),
									new RejectReason(batches.getRejectReasonId()));
							
							updateStoreStock(assetIssue, batchMapper.getBatch(),
									batches.getAcceptedQuantity(), request, batchMapper);
						}

						if( batches.getAccessories() != null)
							for (AssetIssueAccessory accessory : AssetIssueDao.getAssetIssueAccessoryList(batchMapper.getId())) {
							{
								for (AccessoriesRequest accessories : batches.getAccessories()) {
		
									if (accessories.getAccessoryId() != null && accessories.getAccessoryId().equals(accessory.getId())) {
		
										accessory.updateAssetIssueAccessory(accessories.getAcceptedQuantity(),
												accessories.getRejectNote(),
												(accessories.getRejectQuantity() != null ? accessories.getRejectQuantity() : 0),
												new RejectReason(accessories.getRejectReasonId()));
									}
								}
							}
					}
						
				}
			}
		}
		//HIS-14230
		assetIssue.setStatus(new Status(8));
		if(assetIssue.getAssetIndent()!=null)
		{
			assetIssue.getAssetIndent().setStatus(new Status(8));
			assetIssue.getAssetIndent().setApprovalStatus(new Status(8));	
		}
		
//		assetIssue.setUpdatedBy(assetIssue.getUser());
		assetIssue.setUpdatedByUser(request.getUserId());
		assetIssue.setUpdatedDate(TimeZoneComponent.getDateByzone(assetIssue.getUnitId()));
		assetIssue.setAcceptedBy(new User(request.getUserId()));
		assetIssue.setAcceptedDate(TimeZoneComponent.getDateByzone(assetIssue.getUnitId()));
		Boolean flag = false;
		RejectedAssetAcceptance rejectedAssetAcceptance = new RejectedAssetAcceptance(
				new AssetIssue(assetIssue.getId()), new User(sessionService.getUserId()), TimeZoneComponent.getDateByzone(assetIssue.getUnitId()), "RIA-101", new Status(2),
				new Store(assetIssue.getFromStore().getStoreId()), new Store(assetIssue.getToStore().getStoreId()));
//		sessionService.setEntityDefaults(rejectedAssetAcceptance);	
		rejectedAssetAcceptance.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
				request.getUserId());
		RejectedAssetAcceptanceDetail acceptanceDetail = new RejectedAssetAcceptanceDetail();
//		sessionService.setEntityDefaults(acceptanceDetail);
		acceptanceDetail.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
				request.getUserId());
		for (ItemRequest item : request.getItemRequest()) 
		{
			if (item.getRejectQuantity() > 0) 
			{
				flag = true;
				RejectedAssetAcceptanceDetail rejectedAssetAcceptanceDetail=	new RejectedAssetAcceptanceDetail(
						item.getRejectNote(), item.getRejectQuantity(), new RejectReason(item.getRejectReasonId()),
						new AssetIssueBatchMapper(item.getBatchMapperId()));
//				sessionService.setEntityDefaults(rejectedAssetAcceptanceDetail);
				rejectedAssetAcceptanceDetail.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
						request.getUserId());
				acceptanceDetail = rejectedAssetAcceptance.addRejectedAssetAcceptanceDetail(rejectedAssetAcceptanceDetail);
				
				if(item.getAccessories() != null)
				{
					for (AccessoriesRequest accessories : item.getAccessories()) {
						acceptanceDetail.addRejectAssetAccessory(new RejectAssetAccessory(new AssetIssueAccessory(accessories.getAccessoryId()),
								item.getRejectNote(), item.getRejectQuantity(), new RejectReason(item.getRejectReasonId())));
					}
				for (AccessoriesRequest accessories : item.getAccessories())
				{
					RejectAssetAccessory rejectAssetAccessory=	new RejectAssetAccessory(new AssetIssueAccessory(accessories.getAccessoryId()),
							item.getRejectNote(), item.getRejectQuantity(), new RejectReason(item.getRejectReasonId()));
//					sessionService.setEntityDefaults(rejectAssetAccessory);	
					rejectAssetAccessory.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
							request.getUserId());
					acceptanceDetail.addRejectAssetAccessory(rejectAssetAccessory);
				}
				}
			}
		}
		if (flag == true) {
			AssetIssueAcceptanceDao.saveRejectedAssetAcceptance(rejectedAssetAcceptance);
		}

		CommonResponse response = new CommonResponse();
		response.setId(assetIssue.getId().longValue());
		response.setMessage("Asset Issue Acceptance Updated successfully");
		return response;
	}

	@Override
	@Transactional
	public Object getAssetIssueAcceptanceDetails(Integer id,Integer unitId, Integer orgId) {

		AssetIssue assetIssue = AssetIssueDao.getAssetIssue(id);

		AssetIssueResponse response = new AssetIssueResponse(assetIssue.getId(), assetIssue.getFromStore().getStoreId(),
				assetIssue.getFromStore().getStoreName(), assetIssue.getToStore().getStoreId(),
				assetIssue.getToStore().getStoreName(),
				assetIssue.getAssetIndent() != null ? assetIssue.getAssetIndent().getPriority().getPriorityDesc()
						: null,
				assetIssue.getAssetIndent() != null ? assetIssue.getAssetIndent().getIndentType().getType() : null,
				assetIssue.getRemark() != null ? assetIssue.getRemark() : null, assetIssue.getAddedDate(),
				assetIssue.getIssueNo());
		response.setStatusId(assetIssue.getStatus().getId());
		response.setStatus(assetIssue.getStatus().getStatus());
		for (AssetIssueDetail assetIssueDetail : AssetIssueDao.getAssetIssueDetail(id)) {

			Long storeStock = GlobalDao.getStoreStockByItem(assetIssue.getFromStore().getStoreId(),
					assetIssueDetail.getItem().getId(),unitId);
			Long stockInTransit = GlobalDao.getStoreStockInTransitByItem(assetIssue.getFromStore().getStoreId(),
					assetIssueDetail.getItem().getId());
			Integer maxStoreQuantity = GlobalDao.getStoreMaxQuantityByItemFromAssetAcc(assetIssue.getFromStore().getStoreId(),
					assetIssueDetail.getItem().getId(),orgId);
			Long maxOrderQuantity = maxStoreQuantity - (storeStock + stockInTransit);
			if(maxOrderQuantity < 0){ maxOrderQuantity = 0L; }
			Integer pendingQunatity = 0;
			if (assetIssueDetail.getAssetIndentDetail() != null) {
				pendingQunatity = assetIssueDetail.getAssetIndentDetail().getIndentQuantity()
						- assetIssueDetail.getIssueQuantity();
			}

			for (AssetIssueBatchMapper assetIssueBatchMapper : assetIssueDetail.getAssetIssueBatchMappers()) {

				List<AsseccoriesResponse> asseccoriesResponsesList = new ArrayList<AsseccoriesResponse>();
				for (AssetIssueAccessory issueAccessory : assetIssueBatchMapper.getAssetIssueAccessories()) {

					asseccoriesResponsesList.add(PopulateAccessories(issueAccessory));
				}
				AssetIssueItemResponse itemResponse = new AssetIssueItemResponse(assetIssueDetail.getId(),
						assetIssueDetail.getItem() !=  null ? assetIssueDetail.getItem().getId() : null,
						assetIssueDetail.getItem() != null ? assetIssueDetail.getItem().getCode() : null,
						assetIssueDetail.getItem() != null ? assetIssueDetail.getItem().getName() : null,
						assetIssueDetail.getItem().getGeneric() != null ? assetIssueDetail.getItem().getGeneric().getName() : null,
						assetIssueBatchMapper != null ? assetIssueBatchMapper.getBatch().getId() : null, 
						assetIssueBatchMapper != null ? assetIssueBatchMapper.getBatch().getBatchNo() : null,
						assetIssueBatchMapper != null ? assetIssueBatchMapper.getBatch().getExpiryDate() : null,
						assetIssueDetail.getAssetIndentDetail() != null
								? assetIssueDetail.getAssetIndentDetail().getIndentQuantity()
								: null,
						pendingQunatity != null ? pendingQunatity : null, assetIssueBatchMapper.getIssueQuantity(),
						maxOrderQuantity,assetIssueBatchMapper.getId(),
						assetIssueDetail.getAssetIndentDetail() != null ? assetIssueDetail.getAssetIndentDetail().getSpecification() : null,
						asseccoriesResponsesList);
				itemResponse.setAcceptedQuantity(assetIssueBatchMapper.getAcceptedQuantity()!=null ?assetIssueBatchMapper.getAcceptedQuantity() : 0);
				itemResponse.setRejectQuantity(assetIssueBatchMapper.getRejectedQuantity()!=null ? assetIssueBatchMapper.getRejectedQuantity() :0);
				itemResponse.setApprovedQuantity(assetIssueBatchMapper.getAcceptedQuantity()!=null ?assetIssueBatchMapper.getAcceptedQuantity() : 0);
				response.addItems(itemResponse);

			}
		}
		return response;
	}

	private static AsseccoriesResponse PopulateAccessories(AssetIssueAccessory issueAccessory) {

		return new AsseccoriesResponse(issueAccessory.getBatch().getId(), issueAccessory.getBatch().getBatchNo(),
				issueAccessory.getBatch().getItem().getId(), issueAccessory.getBatch().getItem().getCode(),
				issueAccessory.getBatch().getItem().getName(),
				issueAccessory.getBatch().getStoreStockList().getFirst().getAvailableQty(), issueAccessory.getIssueQuantity() );
	}
	
	
	private void updateStoreStock(AssetIssue issue, Batch batch, Integer acceptedQuantity, AssetIssueRequest request , AssetIssueBatchMapper bm) {

		boolean hasStock = false;
		
		Integer avaialableStoreStock = GlobalDao.getStoreStockByBatch(issue.getFromStore().getStoreId(), batch.getId(),0);//0 itemId
		
//		Integer leaseQuantity = Util.GetLeaseQuantity(acceptedQuantity, batch.getBatchUomList(), uomType);
		
		Integer inTransitQuantity = GlobalDao.getTransitQuantityByBatch(issue.getToStore().getStoreId(), batch.getId());
		
		Integer balanceQuantity = avaialableStoreStock + acceptedQuantity;
		
		Integer tQty = inTransitQuantity - acceptedQuantity;
		
		List<StoreStock> fromStoreStock = batch.getStoreStockList().stream().filter(i -> i.getStore().getStoreId() == issue.getToStore().getStoreId()).collect(Collectors.toList());
		if(fromStoreStock!=null &&  fromStoreStock.size()>0) {
			fromStoreStock.getFirst().settQty(tQty);
		}
		
		List<StoreStock> toStoreStock = batch.getStoreStockList().stream().filter(i -> i.getStore().getStoreId() == issue.getFromStore().getStoreId()).collect(Collectors.toList());
		if(toStoreStock!=null &&  toStoreStock.size()>0)
		{
			toStoreStock.getFirst().setAvailableQty(balanceQuantity);
			StockTransaction stockTransaction=	new StockTransaction(avaialableStoreStock, screen, screen.getCode(),
	    			acceptedQuantity, batch.getStoreStockList().getFirst().getStore(), bm.getId());
//			sessionService.setEntityDefaults(stockTransaction);
			stockTransaction.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
					request.getUserId());
	    	batch.addStockTransaction(stockTransaction);
		}
		else {
			StoreStock storeStockNew = new StoreStock(acceptedQuantity, new Store(issue.getFromStore().getStoreId()), new Batch(batch.getId()));
			storeStockNew.setTotalQty(acceptedQuantity);
			storeStockNew.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
					request.getUserId());
			AssetIssueDao.saveStoreStock(storeStockNew);
			StockTransaction stockTransaction1=	new StockTransaction(avaialableStoreStock, screen, screen.getCode(),
					acceptedQuantity, batch.getStoreStockList().getFirst().getStore(), bm.getId());
//			sessionService.setEntityDefaults(stockTransaction1);
			stockTransaction1.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
					request.getUserId());
			batch.addStockTransaction(stockTransaction1);
		}
	}
	
}
