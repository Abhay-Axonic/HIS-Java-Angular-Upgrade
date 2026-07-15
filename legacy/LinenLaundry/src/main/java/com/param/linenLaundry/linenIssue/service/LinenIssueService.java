package com.param.linenLaundry.linenIssue.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.StockTransaction;
import com.param.entity.model.inventory.StoreStock;
import com.param.entity.model.linenAndLaundry.LinenIssue;
import com.param.entity.model.linenAndLaundry.LinenIssueBatchMapper;
import com.param.entity.model.linenAndLaundry.LinenIssueDetail;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.User;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dto.global.BatchProperty;
import com.param.global.dto.items.BatchResponse;
import com.param.global.utility.Util;
import com.param.linenLaundry.linenIndent.dto.ItemRequest;
import com.param.linenLaundry.linenIndent.dto.ItemResponse;
import com.param.linenLaundry.linenIndent.dto.LinenLaundryDropdown;
import com.param.linenLaundry.linenIndent.dto.LinenRequest;
import com.param.linenLaundry.linenIndent.dto.LinenResponse;
import com.param.linenLaundry.linenIssue.dao.ILinenIssueDao;
import com.param.linenLaundry.linenIssue.dto.Carrier;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service("LinenIssueService")
@Transactional
public class LinenIssueService implements ILinenIssueService {

	private Integer screenId = Screen.LinenIssue.getScreen();
	
	private com.param.entity.model.master.Screen screen;
	

@Autowired
private SessionService sessionService;


	@Autowired
	IGlobalDao GlobalDao;

	@Autowired
	ILinenIssueDao linenIssueDao;

	@Override
	@Transactional
	public LinenLaundryDropdown getDropdowns(Integer unitId, Integer userId) {
		LinenLaundryDropdown response = new LinenLaundryDropdown();

		for (Store store : GlobalDao.getLinenUserStoreList(unitId,userId)) {
			response.addFromStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName(),
					store.getStoreType().getCode().equals("MS")));
		}

		for (Store store : GlobalDao.getLinenStoreList(unitId)) {
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
	public Object getLinenIssueList(LinenRequest request) {
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());

		List<LinenResponse> response = new ArrayList<LinenResponse>();

		for (LinenIssue linenIssue : linenIssueDao.searchLinenIssue(request, screenId)) {
			LinenResponse result = new LinenResponse(linenIssue.getId(), linenIssue.getAddedDate(), linenIssue.getLinenIndent().getIndentNo(),
					linenIssue.getFromStore().getStoreId(), linenIssue.getFromStore().getStoreName(), linenIssue.getToStore().getStoreId(),
					linenIssue.getPriority().getPriorityId(), linenIssue.getPriority().getPriorityDesc(), linenIssue.getToStore().getStoreName(),
					linenIssue.getAddedBy().getUserName(), linenIssue.getAddedBy().getUserId(), linenIssue.getRemark(), linenIssue.getStatus().getId(),
					linenIssue.getStatus().getStatus());
			response.add(result);
		}

		return response;
	}

	@Override
	@Transactional
	public Object saveLinenIssue(LinenRequest request) {
		
		screen = GlobalDao.getScreen(screenId);
		
		LinenIssue linenIssue = linenIssueDao.getLinenIssue(request.getId());

		linenIssue.updateLinenIssue(new Status(request.getStatusId()), 
				new User(request.getCarrierId()), request.getRemark());
		
		Integer storeId = linenIssue.getFromStore().getStoreId();
		
		for(LinenIssueDetail  detail : linenIssue.getLinenIssueDetail())
		{
			for(ItemRequest item : request.getItems())
			{
				if (item.getId() != null && item.getId().equals(detail.getId())) {	
					detail.setIssueQuantity(item.getIssueQuantity());
					detail.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId() != null ? request.getUnitId() : 0));
					//detail.setUpdatedBy(detail.getUser());
					detail.setUpdatedByUser(request.getUserId());
						
					for (BatchResponse batch : item.getBatches()) 
					{
						StoreStock storeStock = GlobalDao.getStoreStockByBatchAndStore(batch.getBatchId(), storeId,request.getUnitId());
						if(storeStock != null)
						{
							storeStock.settQty(storeStock.gettQty() + batch.getIssueQuantity());
						}
						
						Batch issueBatch = BatchStoreTransaction(storeId, batch.getBatchId(),
								batch.getIssueQuantity(),linenIssue.getUnit().getUnitId(), request);
//						sessionService.setEntityDefaults(issueBatch);	
						issueBatch.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
								request.getUserId());
						if(issueBatch != null)
						{
							LinenIssueBatchMapper batchMapper = new LinenIssueBatchMapper(new Batch(issueBatch.getId()),
									batch.getIssueQuantity());
//							sessionService.setEntityDefaults(batchMapper);
							batchMapper.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
									request.getUserId());
							detail.addBatch(batchMapper);
						}
					}
				}
			}
		}
		
		CommonResponse response = new CommonResponse();
		response.setId(linenIssue.getId().longValue());
		response.setMessage("Linen Issue saved successfully");

		return response;
		
	}

	@Override
	@Transactional
	public Object getLinenIssue(Integer id,Integer unitId, Integer orgId) {
		
		LinenIssue linenIssue = linenIssueDao.getLinenIssue(id);
		
		LinenResponse result = new LinenResponse(linenIssue.getId(), linenIssue.getAddedDate(), linenIssue.getLinenIndent().getIndentNo(),
				linenIssue.getFromStore().getStoreId(), linenIssue.getFromStore().getStoreName(), linenIssue.getToStore().getStoreId(),
				linenIssue.getPriority().getPriorityId(), linenIssue.getPriority().getPriorityDesc(), linenIssue.getToStore().getStoreName(),
				linenIssue.getAddedBy().getUserName(), linenIssue.getAddedBy().getUserId(), linenIssue.getRemark(), linenIssue.getStatus().getId(),
				linenIssue.getStatus().getStatus());
		
		for(LinenIssueDetail detail : linenIssue.getLinenIssueDetail())
		{
			if(detail.getIsActive() == true && detail.getIsDeleted() == false)
			{
				Long storeStock = GlobalDao.getStoreStockByItem(linenIssue.getFromStore().getStoreId(),
						detail.getItem().getId(),0);
				
				BatchProperty batchProperty = new BatchProperty();
                List<Batch> batchList = GlobalDao.getBatchDetails(detail.getItem().getId(), 0,result.getFromStoreId(),
						batchProperty);
					
					ItemResponse item = new ItemResponse(detail.getId(), detail.getItem().getId(), detail.getItem().getCode(), 
						detail.getItem().getName(), detail.getLinenIndentDetail().getIndentQuantity(), storeStock, 
						detail.getLinenIndentDetail().getPendingQuantity(), detail.getIssueQuantity());
	
					for (Batch batch : batchList) {
						item.addBatches(Util.PopulateBatchWithStock(batch));
					}
					
					result.addItems(item);
			}
		}
		List<Carrier> carrierList = new ArrayList<Carrier>();
		for(User user : GlobalDao.getUser(orgId,unitId))
		{
			Carrier carrier = new Carrier(user.getUserId(), user.getUserName());
			carrierList.add(carrier);
		}
		result.setCarrier(carrierList);
		return result;
	}
	
	private Batch BatchStoreTransaction(Integer storeId, Long batchId, Integer quantity,Integer unitId, LinenRequest request) {

		// get batch needs to be issued
		Batch batch = GlobalDao.getBatch(storeId, batchId,unitId);

		// Get existing batch store stock
		Integer avaialableStoreStock = GlobalDao.getStoreStockByBatch(storeId, batchId);

		Integer balanceQuantity = avaialableStoreStock - quantity;

		// Deduct issued quantity from the batch store stock
		if(batch != null)
		{
			if(batch.getStoreStockList() != null)
			{
				batch.getStoreStockList().getFirst().setAvailableQty(balanceQuantity);
		
				// Add stock transaction
				StockTransaction stockTransaction=	new StockTransaction(balanceQuantity, screen, screen.getCode(), quantity,
						batch.getStoreStockList().getFirst().getStore(),0);
//				sessionService.setEntityDefaults(stockTransaction);	
				stockTransaction.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
						request.getUserId());
				batch.addStockTransaction(stockTransaction);
			}	
		}

		return batch;
	}

}