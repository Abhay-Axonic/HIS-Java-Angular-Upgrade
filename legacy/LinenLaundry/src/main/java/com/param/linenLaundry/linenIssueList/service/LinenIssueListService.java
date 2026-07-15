package com.param.linenLaundry.linenIssueList.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.adt.Priority;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.StockTransaction;
import com.param.entity.model.inventory.StoreStock;
import com.param.entity.model.linenAndLaundry.LinenIssue;
import com.param.entity.model.linenAndLaundry.LinenIssueBatchMapper;
import com.param.entity.model.linenAndLaundry.LinenIssueDetail;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.User;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dto.items.BatchResponse;
import com.param.service.global.SessionService;
import com.param.linenLaundry.linenIndent.dto.ItemRequest;
import com.param.linenLaundry.linenIndent.dto.ItemResponse;
import com.param.linenLaundry.linenIndent.dto.LinenLaundryDropdown;
import com.param.linenLaundry.linenIndent.dto.LinenRequest;
import com.param.linenLaundry.linenIndent.dto.LinenResponse;
import com.param.linenLaundry.linenIssueList.dao.ILinenIssueListDao;

@Service("LinenIssueListService")
@Transactional
public class LinenIssueListService implements ILinenIssueListService {

	private Integer screenId = Screen.LinenIssue.getScreen();
	
	private com.param.entity.model.master.Screen screen;

	@Autowired
	IGlobalDao GlobalDao;

	@Autowired
	ILinenIssueListDao linenIssueListDao;
	

@Autowired
private SessionService sessionService;


	@Override
	@Transactional
	public LinenLaundryDropdown getDropdowns(Integer unitId, Integer userId) {
		LinenLaundryDropdown response = new LinenLaundryDropdown();

		for (Store store : GlobalDao.getLinenUserStoreList(unitId,unitId)) {
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

		for (Priority priority : GlobalDao.getPriorityList()) {
			response.addPriority(
					new com.param.global.dto.global.Priority(priority.getPriorityId(), priority.getPriorityDesc()));
		}

		return response;
	}

	@Override
	@Transactional
	public Object getLinenIssueList(LinenRequest request) {
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());

		List<LinenResponse> response = new ArrayList<LinenResponse>();

		for (LinenIssue linenIssue : linenIssueListDao.searchLinenIssueList(request, screenId)) {
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
	public Object saveLinenIssueList(LinenRequest request) {
		
		screen = GlobalDao.getScreen(screenId);

		LinenIssue linenIssue = new LinenIssue("ISS-001", new Store(request.getFromStoreId()),
				new Store(request.getToStoreId()), new Status(request.getStatusId()), new User(request.getCarrierId()),
				request.getRemark(), new Priority(request.getPriorityId()));
//		sessionService.setEntityDefaults(linenIssue);

		linenIssue.setCreateDefaults(sessionService.getOrganizationId(),sessionService.getUnitId(),
				sessionService.getUserId());
		
		Integer storeId = request.getFromStoreId();
		
		for (ItemRequest item : request.getItems()) {
				
			LinenIssueDetail detail = linenIssue.addLinenIssueDetail(new LinenIssueDetail(new Item(item.getId())));
//			sessionService.setEntityDefaults(detail);
			detail.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
					request.getUserId());
			for (BatchResponse batch : item.getBatches()) {
				
				StoreStock storeStock = GlobalDao.getStoreStockByBatchAndStore(batch.getBatchId(), storeId,request.getUnitId());
				if(storeStock != null)
				{
					storeStock.settQty(storeStock.gettQty() + batch.getIssueQuantity());
				}
				
				Batch issueBatch = BatchStoreTransaction(storeId, batch.getBatchId(),batch.getIssueQuantity(),request.getUnitId(), request);
//				sessionService.setEntityDefaults(issueBatch);
				issueBatch.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
						request.getUserId());
				if(issueBatch != null)
				{
					LinenIssueBatchMapper batchMapper = new LinenIssueBatchMapper(new Batch(issueBatch.getId()),
							batch.getIssueQuantity());
//					sessionService.setEntityDefaults(batchMapper);
					batchMapper.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
							request.getUserId());
					detail.addBatch(batchMapper);
				}
			}
		}
		
		linenIssueListDao.saveLinenIssueList(linenIssue);
		
		CommonResponse response = new CommonResponse();
		response.setId(linenIssue.getId().longValue());
		response.setMessage("Linen Issue saved successfully");

		return response;
		
	}

	@Override
	@Transactional
	public Object getLinenIssueList(Integer id) {
		
		LinenIssue linenIssue = linenIssueListDao.getLinenIssueList(id);
		
		LinenResponse result = new LinenResponse(linenIssue.getId(), linenIssue.getAddedDate(), linenIssue.getLinenIndent().getIndentNo(),
				linenIssue.getFromStore().getStoreId(), linenIssue.getFromStore().getStoreName(), linenIssue.getToStore().getStoreId(),
				linenIssue.getPriority().getPriorityId(), linenIssue.getPriority().getPriorityDesc(), linenIssue.getToStore().getStoreName(),
				linenIssue.getAddedBy().getUserName(), linenIssue.getAddedBy().getUserId(), linenIssue.getRemark(), linenIssue.getStatus().getId(),
				linenIssue.getStatus().getStatus());
		
		result.setCarrierId(linenIssue.getCarrier().getUserId());
		result.setCarrierName(linenIssue.getCarrier().getUserName());
		
		for(ItemResponse detail : linenIssueListDao.getLinenIssueListDetail(linenIssue.getId()))
		{
			Long storeStock = GlobalDao.getStoreStockByItem(linenIssue.getFromStore().getStoreId(),
					detail.getItemId(),0);
			
			detail.setStockAvailable(storeStock);
			result.addItems(detail);
		}
		
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
				batch.getStoreStockList().get(0).setAvailableQty(balanceQuantity);
		
				// Add stock transaction
				StockTransaction stockTransaction=new StockTransaction(balanceQuantity, screen, screen.getCode(), quantity,
						batch.getStoreStockList().get(0).getStore(),0);
//				sessionService.setEntityDefaults(stockTransaction);

				stockTransaction.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
									request.getUserId());
				batch.addStockTransaction(stockTransaction);
			}	
		}

		return batch;
	}

}