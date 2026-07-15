package com.param.assetManagement.indent.assetReturn.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.adt.master.global.model.ReasonMaster;
import com.param.assetManagement.indent.assetIndent.dto.AssetIndentDropdown;
import com.param.assetManagement.indent.assetIndent.dto.AssetIndentRequest;
import com.param.assetManagement.indent.assetIndent.dto.AssetIndentRequestAccessories;
import com.param.assetManagement.indent.assetIndent.dto.AssetIndentRequestItem;
import com.param.assetManagement.indent.assetIssue.dto.AssetIssueResponse;
import com.param.assetManagement.indent.assetIssueList.dto.AssetIssueItemResponse;
import com.param.assetManagement.indent.assetReturn.dao.IAssetReturnDao;
import com.param.assetManagement.indent.assetReturn.dto.AssetReturnRequest;
import com.param.assetManagement.indent.assetReturn.dto.AssetReturnResponse;
import com.param.assetManagement.indent.assetReturn.dto.ItemResponse;
import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.asset.AssetIssue;
import com.param.entity.model.asset.AssetIssueAccessory;
import com.param.entity.model.asset.AssetIssueBatchMapper;
import com.param.entity.model.asset.AssetIssueDetail;
import com.param.entity.model.asset.AssetReturn;
import com.param.entity.model.asset.AssetReturnAccessory;
import com.param.entity.model.asset.AssetReturnDetail;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.StockTransaction;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.ReturnReason;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.User;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dto.items.AsseccoriesResponse;
import com.param.global.service.global.IGlobalService;
import com.param.global.utility.Util;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service
public class AssetReturnService implements IAssetReturnService {
	

@Autowired
private SessionService sessionService;


	private Integer screenId = Screen.AssetReturn.getScreen();

	private com.param.entity.model.master.Screen screen;

	@Autowired
	IGlobalDao GlobalDao;

	@Autowired
	IAssetReturnDao AssetReturnDao;
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	IGlobalService GlobalService;

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
	public Object getAssetReturnList(AssetIndentRequest request) {
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());
		return AssetReturnDao.searchAssetReturnList(request, screenId);
	}

	@Override
	@Transactional
	public Object saveAssetReturn(AssetReturnRequest request) {
		String returnNumber = GlobalService.getBillNoByGeneratedCode(request.getFromStoreId(), request.getUnitId(), screenId);
		AssetReturn assetReturn = new AssetReturn(new Status(request.getApprovalStatusId()),
				new User(request.getCarrierId()), new Store(request.getFromStoreId()),
				new Store(request.getToStoreId()), returnNumber!=null ? returnNumber :"RET-001", new Status(request.getStatusId()));
		assetReturn.setRemark(request.getRemark()!=null ? request.getRemark() :null);
		for (AssetIndentRequestItem items : request.getItems()) 
		{
			AssetReturnDetail assetReturnDetail =  new AssetReturnDetail(new Batch(items.getBatchId()), items.getReturnQuantity(),
							new ReasonMaster(items.getReturnReasonId()),
							new Status(items.getStatusId()));
			assetReturnDetail.setAssetIssueDetail(items.getIssueDetailId()!=null ? new AssetIssueDetail(items.getIssueDetailId()):null);
			assetReturnDetail.setAssetIssueBatchMapper(items.getIssueBatchMapperId()!=null ? new AssetIssueBatchMapper(items.getIssueBatchMapperId()):null);
//			sessionService.setEntityDefaults(assetReturnDetail);
			assetReturnDetail.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
			if(items.getAccessories()!=null && items.getAccessories().size()>0) {
			for (AssetIndentRequestAccessories accessories : items.getAccessories())
			{
				AssetReturnAccessory assetReturnAccessory=	new AssetReturnAccessory(new Batch(accessories.getBatchId()),
						new ReturnReason(accessories.getReturnReasonId()), accessories.getReturnQuantity());
//				sessionService.setEntityDefaults(assetReturnAccessory);
				assetReturnAccessory.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
				assetReturnDetail.addAssetReturnAccessories(assetReturnAccessory);
			}
			}
			assetReturn.addAssetReturnDetail(assetReturnDetail);

		}
		assetReturn.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
		AssetReturnDao.saveASsetReturn(assetReturn);

		CommonResponse response = new CommonResponse();
		response.setId(assetReturn.getId().longValue());
		response.setMessage("Asset Return saved successfully");

		return response;
	}

	@Override
	@Transactional
	public Object updateAssetReturn(AssetIndentRequest request) {

		AssetReturn assetReturn = AssetReturnDao.getAssetReturn(request.getId());

		assetReturn.updateAssetreturn(new User(request.getCarrierId()), request.getRemark());

		for (AssetReturnDetail detail : assetReturn.getAssetReturnDetail()) {
			Integer id = null;

			for (AssetIndentRequestItem item : request.getItems()) {
				if (item.getId() != null && item.getId().equals(detail.getId())) {
					id = item.getId();
					detail.updateAssetReturn(new ReasonMaster(item.getReturnReasonId()),
							item.getReturnQuantity());

					if(detail.getAssetReturnAccessories()!=null && detail.getAssetReturnAccessories().size()>0) {
					for (AssetReturnAccessory accessories : detail.getAssetReturnAccessories()) {
						Integer accessoryId = null;
						for (AssetIndentRequestAccessories accessory : item.getAccessories()) {
							if (accessory.getId() != null && accessory.getId().equals(accessories.getId())) {
								accessoryId = accessory.getId();
								accessories.updateAssetReturnAccessories(new Batch(accessory.getBatchId()),
										new ReturnReason(accessory.getReturnReasonId()), accessory.getReturnQuantity());
							}
						}

						if (accessoryId == null) {
							accessories.setIsDeleted(true);
						}

						accessories.setUpdatedDate(TimeZoneComponent.getDateByzone(assetReturn.getUnitId()));
						accessories.setUpdatedByUser(request.getUserId());
					}
					}
					break;
				}
			}

			if (id == null) {
				detail.setIsDeleted(true);
			}

			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(assetReturn.getUnitId()));
			detail.setUpdatedByUser(request.getUserId());
		}

		for (AssetIndentRequestItem item : request.getItems()) {
			if (item.getId() == null) {
				/*
				 * assetReturn.addAssetReturnDetail( new AssetReturnDetail(new
				 * Batch(item.getBatchId()), item.getReturnQuantity(), new
				 * com.param.entity.model.master.ReturnReason(item.getReturnReasonId()), new
				 * Status(item.getStatusId())));
				 */
				AssetReturnDetail assetReturnDetail = new AssetReturnDetail(new Batch(item.getBatchId()),
						item.getReturnQuantity(),new ReasonMaster(item.getReturnReasonId()),
						new Status(item.getStatusId()));
//				sessionService.setEntityDefaults(assetReturnDetail);
				assetReturnDetail.setAssetIssueBatchMapper(item.getIssueBatchMapperId()!=null ? new AssetIssueBatchMapper(item.getIssueBatchMapperId()):null);
				assetReturnDetail.setCreateDefaults(sessionService.getOrganizationId(),sessionService.getUnitId(),
						sessionService.getUserId());
				if(item.getAccessories()!=null && item.getAccessories().size()>0) {
				for (AssetIndentRequestAccessories accessories : item.getAccessories())
				{
					AssetReturnAccessory assetReturnAccessory=	new AssetReturnAccessory(
							new Batch(accessories.getBatchId()), new ReturnReason(accessories.getReturnReasonId()),
							accessories.getReturnQuantity());
//					sessionService.setEntityDefaults(assetReturnAccessory);
					assetReturnAccessory.setCreateDefaults(sessionService.getOrganizationId(),sessionService.getUnitId(),
							sessionService.getUserId());
					assetReturnDetail.addAssetReturnAccessories(assetReturnAccessory);
				}
				}
				assetReturn.addAssetReturnDetail(assetReturnDetail);
			}
		}

		assetReturn.setUpdatedDate(TimeZoneComponent.getDateByzone(assetReturn.getUnitId()));
		assetReturn.setUpdatedByUser(request.getUserId());

		AssetReturnDao.updateAssetReturn(assetReturn);

		CommonResponse response = new CommonResponse();
		response.setMessage("Material Return updated successfully");

		return response;
	}

	@Override
	@Transactional
	public Object getAssetReturnDetails(Integer id, Integer unitId, Integer orgId) {

		AssetReturn assetReturn = AssetReturnDao.getAssetReturn(id);

		AssetReturnResponse response = new AssetReturnResponse(assetReturn.getId(), assetReturn.getAddedDate(),
				assetReturn.getReturnNo(), assetReturn.getFromStore().getStoreId(),
				assetReturn.getFromStore().getStoreName(), assetReturn.getToStore().getStoreId(),
				assetReturn.getToStore().getStoreName(), assetReturn.getAddedBy().getUserName(),
				assetReturn.getRemark(), assetReturn.getStatus().getId(), assetReturn.getStatus().getStatus(),
				assetReturn.getCarrier().getUserId(), assetReturn.getCarrier().getUserName());

		for (AssetReturnDetail detail : AssetReturnDao.getAssetReturnDetail(id)) {

			Long availableQuantity = GlobalDao.getStoreStockByItem(assetReturn.getFromStore().getStoreId(),
					detail.getBatch().getItem().getId(),unitId);

			List<AsseccoriesResponse> asseccoriesResponsesList = new ArrayList<AsseccoriesResponse>();
			for (AssetReturnAccessory issueAccessory : detail.getAssetReturnAccessories()) {

				asseccoriesResponsesList.add(PopulateAccessories(issueAccessory));
			}
				response.addItems(new ItemResponse(detail.getBatch().getItem().getId(),
						detail.getBatch().getItem().getCode(), detail.getBatch().getItem().getName(),
						detail.getBatch().getId(), detail.getBatch().getExpiryDate(),detail.getAssetIssueBatchMapper()!=null ? detail.getAssetIssueBatchMapper().getAcceptedQuantity(): availableQuantity, detail.getId(),
						detail.getReturnReason().getReasonMasterId(), detail.getReturnQuantity(),
						detail.getReturnReason().getReasonDesc(), detail.getBatch().getBatchNo(),
						detail.getBatch().getItem().getGeneric()!=null ? detail.getBatch().getItem().getGeneric().getName(): "", asseccoriesResponsesList));
			}
		return response;
	}

	private static AsseccoriesResponse PopulateAccessories(AssetReturnAccessory issueAccessory) {

		return new AsseccoriesResponse(issueAccessory.getBatch().getId(), issueAccessory.getBatch().getBatchNo(),
				issueAccessory.getBatch().getItem().getId(), issueAccessory.getBatch().getItem().getCode(),
				issueAccessory.getBatch().getItem().getName(),
				issueAccessory.getBatch().getStoreStockList().getFirst().getAvailableQty());
	}

	@Override
	@Transactional
	public Object approveAssetReturn(AssetReturnRequest request) {
		screen = GlobalDao.getScreen(screenId);

		for (Integer id : request.getReturnId()) {

			AssetReturn assetReturn = AssetReturnDao.getAssetReturn(id);

			Status status = (Status) sessionFactory.getCurrentSession().createSQLQuery(" SELECT id, code, status " + 
					"FROM public.m_status  where id = "+request.getApprovalStatusId() )
					.setResultTransformer(Transformers.aliasToBean(Status.class)).uniqueResult();
			
			
			status.setId(request.getApprovalStatusId());
			status.setUpdatedByUser(request.getUserId());
			status.setAddedByUser(request.getUserId());
			
			assetReturn.setStatus(new Status(request.getStatusId()));
			assetReturn.setApprovalStatus(status);

			for (AssetReturnDetail detail : assetReturn.getAssetReturnDetail()) {

			 	updateStoreStock(assetReturn.getToStore().getStoreId(), detail.getBatch().getId(),
				 		detail.getReturnQuantity(),assetReturn.getUnit().getUnitId(),request);

				
				detail.setStatus(new Status(request.getApprovalStatusId()));
				detail.setUpdatedDate(TimeZoneComponent.getDateByzone(assetReturn.getUnitId()));
				detail.setUpdatedByUser(request.getUserId());
			}

			assetReturn.setApprovedDate(TimeZoneComponent.getDateByzone(assetReturn.getUnitId()));
			assetReturn.setApprovedBy(new User(sessionService.getUserId()));

			assetReturn.setUpdatedDate(TimeZoneComponent.getDateByzone(assetReturn.getUnitId()));
			assetReturn.setUpdatedByUser(request.getUserId());

			AssetReturnDao.updateAssetReturn(assetReturn);
		}

		CommonResponse response = new CommonResponse();
		response.setMessage("Asset Return approved successfully");

		return response;
	}

	private Batch updateStoreStock(Integer storeId, Long batchId, Integer quantity,Integer unitId,AssetReturnRequest request) {
		{/*
			// get batch needs to be issued
			Batch batch = GlobalDao.getBatch(storeId, batchId,unitId);

			// Get existing batch store stock
			Integer avaialableStoreStock = GlobalDao.getStoreStockByBatch(storeId, batchId,0);//0 itemId

			Integer balanceQuantity = avaialableStoreStock - quantity;

			// Deduct issued quantity from the batch store stock
			batch.getStoreStockList().get(0).settQty(quantity);

			batch.getStoreStockList().get(0).setAvailableQty(balanceQuantity);
			
			batch.getStoreStockList().get(0).setCreateDefaults(request.getOrganizationId(), request.getUnitId(), request.getUserId());

			// Add stock transaction
			StockTransaction stockTransaction=new StockTransaction(balanceQuantity, screen, screen.getCode(), quantity,
					batch.getStoreStockList().get(0).getStore(),0);
			stockTransaction.setCreateDefaults(request.getOrganizationId(), request.getUnitId(), request.getUserId());
			batch.addStockTransaction(stockTransaction);
			
			batch.setCreateDefaults(request.getOrganizationId(), request.getUnitId(), request.getUserId());

			return batch;
		*/
			
		
		

			// get batch needs to be issued
			Batch batch = GlobalDao.getBatch(storeId, batchId,unitId);

			// Get existing batch store stock
			Integer avaialableStoreStock = GlobalDao.getStoreStockByBatch(storeId, batchId);

			// Get lease quantity
			Integer leaseQuantity = quantity;

			Integer balanceQuantity = avaialableStoreStock - leaseQuantity;
			batch.getStoreStockList().getFirst().setAvailableQty(balanceQuantity <0 ? 0 : balanceQuantity);

			// Deduct issued quantity from the batch store stock
			Integer inTransitQuantity = GlobalDao.getTransitQuantityByBatch(storeId, batch.getId());
			Integer tQty = inTransitQuantity + leaseQuantity;
			batch.getStoreStockList().getFirst().settQty(tQty);

			// Add stock transaction
			StockTransaction stockTransaction=new StockTransaction(balanceQuantity, screen, screen.getCode(), quantity,
					batch.getStoreStockList().getFirst().getStore(),0);
			stockTransaction.setCreateDefaults(request.getOrganizationId(), request.getUnitId(), request.getUserId());
			batch.addStockTransaction(stockTransaction);
			
			batch.setCreateDefaults(request.getOrganizationId(), request.getUnitId(), request.getUserId());


			return batch;
		
		
		
		
		
		}
	}

	@Override
	@Transactional
	public Object cancelAssetReturn(AssetReturnRequest request) {
		AssetReturn assetReturn = AssetReturnDao.getAssetReturn(request.getId());

		for (AssetReturnDetail detail : assetReturn.getAssetReturnDetail()) {
			detail.setStatus(new Status(request.getStatusId()));

			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(assetReturn.getUnitId()));
			detail.setUpdatedByUser(request.getUserId());
		}
		
		assetReturn.setCancelReason(new CancelReason(request.getCancelReasonId()));
		assetReturn.setCancelNote(request.getCancelNote());
		 
		assetReturn.setStatus(new Status(request.getStatusId()));

		assetReturn.setUpdatedDate(TimeZoneComponent.getDateByzone(assetReturn.getUnitId()));
		assetReturn.setUpdatedByUser(request.getUserId());

		AssetReturnDao.updateAssetReturn(assetReturn);

		CommonResponse response = new CommonResponse();
		response.setMessage("Asset Return cancelled successfully");

		return response;
	}

	@Override
	@Transactional
	public Object getAssetReturnAgainstIssue(AssetIndentRequest request) {
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());
		List<AssetIssueResponse> response1 =new ArrayList<>();
		for(AssetIssue assetIssue : AssetReturnDao.getAssetReturnIssueList(request))
		{

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

			for (AssetIssueDetail assetIssueDetail :assetIssue.getAssetIssueDetail()) {
				Long storeStock = GlobalDao.getStoreStockByItem(assetIssue.getFromStore().getStoreId(),
						assetIssueDetail.getItem().getId(),assetIssue.getUnitId());
				Long stockInTransit = GlobalDao.getStoreStockInTransitByItem(assetIssue.getFromStore().getStoreId(),
						assetIssueDetail.getItem().getId());
				Integer maxStoreQuantity = GlobalDao.getStoreMaxQuantityByItem(assetIssue.getFromStore().getStoreId(),
						assetIssueDetail.getItem().getId(),assetIssue.getOrganizationId());
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
							pendingQunatity != null ? pendingQunatity : null, assetIssueDetail.getIssueQuantity(),
							maxOrderQuantity, asseccoriesResponsesList);
					assetIssueItemResponse.setIssueQuantity(assetIssueBatchMapper.getIssueQuantity());
					assetIssueItemResponse.setAcceptedQuantity(assetIssueBatchMapper.getAcceptedQuantity());
					assetIssueItemResponse.setAvailableQuantity(storeStock);
					assetIssueItemResponse.setBatchMapperId(assetIssueBatchMapper.getId());
					response.addItems(assetIssueItemResponse);
				
					
				}
			
			}	
			response1.add(response);
		}
		
		

		return response1;
	}
	private static AsseccoriesResponse PopulateAccessories(AssetIssueAccessory issueAccessory) {

		return new AsseccoriesResponse(issueAccessory.getBatch().getId(), issueAccessory.getBatch().getBatchNo(),
				issueAccessory.getBatch().getItem().getId(), issueAccessory.getBatch().getItem().getCode(),
				issueAccessory.getBatch().getItem().getName(),
				issueAccessory.getBatch().getStoreStockList().getFirst().getAvailableQty());
	}

}
