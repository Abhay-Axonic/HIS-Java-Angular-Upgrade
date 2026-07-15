package com.param.linenLaundry.linenIssueAcceptance.service;

import java.sql.Timestamp;
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
import com.param.entity.model.linenAndLaundry.RejectedAcceptance;
import com.param.entity.model.linenAndLaundry.RejectedAcceptanceDetail;
import com.param.entity.model.master.RejectReason;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.User;
import com.param.global.dao.global.IGlobalDao;
import com.param.linenLaundry.linenIndent.dto.ItemRequest;
import com.param.linenLaundry.linenIndent.dto.LinenLaundryDropdown;
import com.param.linenLaundry.linenIndent.dto.LinenRequest;
import com.param.linenLaundry.linenIndent.dto.LinenResponse;
import com.param.linenLaundry.linenIssueAcceptance.dao.ILinenIssueAcceptanceDao;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service("LinenIssueAcceptanceService")
@Transactional
public class LinenIssueAcceptanceService implements ILinenIssueAcceptanceService {
	
	@Autowired
	private SessionService sessionService;

	private Integer screenId = Screen.LinenIssueAcceptance.getScreen();
	
	private com.param.entity.model.master.Screen screen;

	@Autowired
	IGlobalDao GlobalDao;

	@Autowired
	ILinenIssueAcceptanceDao linenIssueAcceptanceDao;

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
	public Object getLinenIssueAcceptance(LinenRequest request) {
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());

		List<LinenResponse> response = new ArrayList<LinenResponse>();

		for (LinenIssue linenIssue : linenIssueAcceptanceDao.searchLinenIssueAcceptance(request, screenId)) {
			LinenResponse result = new LinenResponse(linenIssue.getId(), linenIssue.getAddedDate(), linenIssue.getIssueNo(),
					linenIssue.getFromStore().getStoreId(), linenIssue.getFromStore().getStoreName(), linenIssue.getToStore().getStoreId(),
					linenIssue.getPriority().getPriorityId(), linenIssue.getPriority().getPriorityDesc(), linenIssue.getToStore().getStoreName(),
					linenIssue.getRemark(), linenIssue.getStatus().getId(), linenIssue.getStatus().getStatus());
			response.add(result);
		}

		return response;
	}

	@Override
	@Transactional
	public Object saveLinenIssueAcceptance(LinenRequest request) {
		
		screen = GlobalDao.getScreen(screenId);
		
		LinenIssue linenIssue = linenIssueAcceptanceDao.getLinenIssueAcceptance(request.getId());
				
		for(LinenIssueDetail issueDetail : linenIssueAcceptanceDao.getLinenIssueAcceptanceDetails(linenIssue.getId()))
		{
			for(LinenIssueBatchMapper batchMapper : issueDetail.getLinenIssueBatchMapper())
			{
				for(ItemRequest issueDetailBatchMapper : request.getItems())
				{
					if(issueDetailBatchMapper.getAcceptQuantity() > 0)
					{
						if(issueDetailBatchMapper.getBatchMapperId() != null && batchMapper.getId() == issueDetailBatchMapper.getBatchMapperId())
						{
							BatchMapperStoreStock(linenIssue.getToStore().getStoreId(), batchMapper.getBatch(),
									batchMapper.getIssueQuantity(), request);
	
							batchMapper.updateLinenIssueBatchMapper(issueDetailBatchMapper.getBatchMapperId(), (issueDetailBatchMapper.getAcceptQuantity() != null) ? issueDetailBatchMapper.getAcceptQuantity() : null, 
									(issueDetailBatchMapper.getRejectNote() != null) ? issueDetailBatchMapper.getRejectNote() : null, issueDetailBatchMapper.getRejectQuantity(),
									(issueDetailBatchMapper.getRejectReasonId() != null) ? new RejectReason(issueDetailBatchMapper.getRejectReasonId()) : null);
						}
					}
				}
			}
			
		}
		//linenIssue.setUpdatedBy(linenIssue.getUser());
		linenIssue.setUpdatedByUser(request.getUserId());
		linenIssue.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId() != null ? request.getUnitId() : 0));
		linenIssueAcceptanceDao.updateLinenIssueAcceptance(linenIssue);
		saveRejectDetails(request);
		
		CommonResponse response = new CommonResponse();
		response.setId(linenIssue.getId().longValue());
		response.setMessage("Linen Issue saved successfully");

		return response;
		
	}

	@Override
	@Transactional
	public Object getLinenIssueAcceptance(Integer id) {
		
		LinenIssue linenIssue = linenIssueAcceptanceDao.getLinenIssueAcceptance(id);
		
		LinenResponse result = new LinenResponse(linenIssue.getId(), linenIssue.getAddedDate(), linenIssue.getIssueNo(),
				linenIssue.getFromStore().getStoreId(), linenIssue.getFromStore().getStoreName(), linenIssue.getToStore().getStoreId(),
				linenIssue.getPriority().getPriorityId(), linenIssue.getPriority().getPriorityDesc(), linenIssue.getToStore().getStoreName(),
				linenIssue.getRemark(), linenIssue.getStatus().getId(), linenIssue.getStatus().getStatus());
		result.setIndentNo(linenIssue.getLinenIndent().getIndentNo());
		
		result.setItems(linenIssueAcceptanceDao.getLinenIssueAcceptanceDetail(linenIssue.getId()));
		
		return result;
	}
	
	private void BatchMapperStoreStock(Integer storeId, Batch batch, Integer issueQuantity, LinenRequest request) {

		boolean hasStock = false;

		for (StoreStock storeStock : batch.getStoreStockList()) {
			if (storeStock.getStore().getStoreId().equals(storeId)) {

				hasStock = true;
				Integer avaialableStoreStock = GlobalDao.getStoreStockByBatch(storeId, batch.getId());

				Integer balanceQuantity = avaialableStoreStock + issueQuantity;

				storeStock.setAvailableQty(balanceQuantity);
				StockTransaction stockTransaction=new StockTransaction(balanceQuantity, screen, screen.getCode(), issueQuantity,
						batch.getStoreStockList().get(0).getStore(),0);
//				sessionService.setEntityDefaults(stockTransaction);

				stockTransaction.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
									request.getUserId());
				batch.addStockTransaction(stockTransaction);
			}
		}
		if (!hasStock) {

			StoreStock storeStock=new StoreStock(issueQuantity, new Store(storeId), new Batch(batch.getId()));
//			sessionService.setEntityDefaults(storeStock);
			storeStock.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
					request.getUserId());
			Integer avaialableStoreStock = GlobalDao.getStoreStockByBatch(storeId, batch.getId());
			StockTransaction stockTransaction=new StockTransaction(avaialableStoreStock, screen, screen.getCode(),
					issueQuantity, batch.getStoreStockList().get(0).getStore(),0);
//			sessionService.setEntityDefaults(stockTransaction);	
			stockTransaction.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
					request.getUserId());
			batch.addStockTransaction(stockTransaction);
		}
	}

	private void saveRejectDetails(LinenRequest request) {
		Timestamp now = new Timestamp((new DateTime()).getMillis());
		boolean flag = false;
	
		LinenIssue linenIssue = linenIssueAcceptanceDao.getLinenIssueAcceptance(request.getId());
		
		RejectedAcceptance rejectedAcceptance = new RejectedAcceptance("RET-001", new Store(linenIssue.getToStore().getStoreId()),
				new User(sessionService.getUserId()), now, new Status(com.param.common.constants.Status.Pending.getStatus()),
				new Store(linenIssue.getFromStore().getStoreId()), new LinenIssue(linenIssue.getId()));
//		sessionService.setEntityDefaults(rejectedAcceptance);
		rejectedAcceptance.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
				request.getUserId());
		for(ItemRequest issueDetailItem : request.getItems())
		{
			if(issueDetailItem.getRejectQuantity() > 0)
			{
				flag = true;
				RejectedAcceptanceDetail rejectedAcceptanceDetail=	new RejectedAcceptanceDetail(new Batch(issueDetailItem.getBatchId()), (issueDetailItem.getRejectNote() != null) ? issueDetailItem.getRejectNote() : null,
						(issueDetailItem.getRejectQuantity() != null) ? issueDetailItem.getRejectQuantity() : null, (issueDetailItem.getRejectReasonId() != null) ? new RejectReason(issueDetailItem.getRejectReasonId()) : null,
						(issueDetailItem.getBatchMapperId() != null) ? new LinenIssueBatchMapper(issueDetailItem.getBatchMapperId()) : null);
//				sessionService.setEntityDefaults(rejectedAcceptanceDetail);
				rejectedAcceptanceDetail.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
						request.getUserId());
				rejectedAcceptance.addRejectedAcceptanceDetail(rejectedAcceptanceDetail);
			}
		}
		if(flag == true)
		{
			linenIssueAcceptanceDao.saveRejectedAcceptance(rejectedAcceptance);
		}
	}
}