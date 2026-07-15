package com.param.linenLaundry.linenReturnAcceptance.service;

import java.sql.Timestamp;
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
import com.param.entity.model.linenAndLaundry.LinenReturn;
import com.param.entity.model.linenAndLaundry.LinenReturnDetail;
import com.param.entity.model.linenAndLaundry.RejectedAcceptance;
import com.param.entity.model.linenAndLaundry.RejectedAcceptanceDetail;
import com.param.entity.model.master.RejectReason;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.User;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dto.global.BatchProperty;
import com.param.global.utility.Util;
import com.param.linenLaundry.linenIndent.dto.ItemRequest;
import com.param.linenLaundry.linenIndent.dto.ItemResponse;
import com.param.linenLaundry.linenIndent.dto.LinenLaundryDropdown;
import com.param.linenLaundry.linenIndent.dto.LinenRequest;
import com.param.linenLaundry.linenIndent.dto.LinenResponse;
import com.param.linenLaundry.linenReturnAcceptance.dao.ILinenReturnAcceptanceDao;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service("LinenReturnAcceptanceService")
@Transactional
public class LinenReturnAcceptanceService implements ILinenReturnAcceptanceService {
	
	@Autowired
	private SessionService sessionService;

	private Integer screenId = Screen.LinenReturnAcceptance.getScreen();
	
	private com.param.entity.model.master.Screen screen;
	
	@Autowired
	IGlobalDao GlobalDao;

	@Autowired
	ILinenReturnAcceptanceDao linenReturnAcceptanceDao;

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
	public Object getLinenReturnAcceptanceList(LinenRequest request) {

		return linenReturnAcceptanceDao.searchLinenReturnAcceptance(request, screenId);
	}

	@Override
	@Transactional
	public Object saveLinenReturnAcceptance(LinenRequest request) {
	
		screen = GlobalDao.getScreen(screenId);
		LinenReturn linenReturn = linenReturnAcceptanceDao.getLinenReturnAcceptanceId(request.getId());
		for(LinenReturnDetail linenReturnDetail : linenReturn.getLinenReturnDetails())
		{
			for(ItemRequest item : request.getItems())
			{
				if(item.getAcceptQuantity() > 0)
				{
					if(item.getId() != null && item.getId() == linenReturnDetail.getId())
					{
						BatchMapperStoreStock(linenReturn.getToStore().getStoreId(), linenReturnDetail.getBatch(),
								item.getAcceptQuantity(),request);
						
						linenReturnDetail.updateLinenReturnDetail(item.getAcceptQuantity(), new Batch(item.getBatchId()),
								(item.getRejectNote() != null) ? item.getRejectNote() : null, (item.getRejectQuantity() != null) ? item.getRejectQuantity() : null,
								(item.getRejectReasonId() != null) ? new RejectReason(item.getRejectReasonId()) : null,
								new Status(item.getStatusId()));
						
					}
				}
			}
		}
		linenReturn.setRemark(request.getRemark());
		linenReturn.setStatus(new Status(request.getStatusId()));
		linenReturn.setUpdatedByUser(request.getUserId()!=null ?request.getUserId() :sessionService.getUserId());
		linenReturn.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
		
		linenReturnAcceptanceDao.saveLinenReturnAcceptance(linenReturn);
		saveRejectDetails(request);
		CommonResponse response = new CommonResponse();
		response.setId(linenReturn.getId().longValue());
		response.setMessage("Linen Return Acceptance saved successfully");
		
		return response;
		
	}

	@Override
	@Transactional
	public Object getLinenReturnAcceptance(Integer id) {
		
		LinenResponse linenResponse = linenReturnAcceptanceDao.getLinenReturnAcceptance(id);
		
		for(LinenReturnDetail detail : linenReturnAcceptanceDao.getLinenReturnAcceptanceDetail(linenResponse.getId()))
		{
			Long storeStock = GlobalDao.getStoreStockByItem(linenResponse.getFromStoreId(),
					detail.getBatch().getItem().getId(),0);
			
			BatchProperty batchProperty = new BatchProperty();
            List<Batch> batchList = GlobalDao.getBatchDetails(detail.getBatch().getItem().getId(),0, linenResponse.getFromStoreId(),
					batchProperty);
				
				ItemResponse item = new ItemResponse(detail.getId(), detail.getBatch().getItem().getId(), detail.getBatch().getItem().getCode(), 
					detail.getBatch().getItem().getName(), storeStock, detail.getStatus().getId(), detail.getStatus().getStatus(),
					detail.getBatch().getId(), detail.getBatch().getBatchNo(), detail.getReturnQuantity(), (detail.getReturnReason() != null) ? detail.getReturnReason().getId() : null,
					(detail.getReturnReason() != null) ? detail.getReturnReason().getReason() : null);

				for (Batch batch : batchList) {
					item.addBatches(Util.PopulateBatchWithStock(batch));
				}
				
				linenResponse.addItems(item);
		}
		
		return linenResponse;
	}
	
	private void BatchMapperStoreStock(Integer storeId, Batch batch, Integer issueQuantity ,LinenRequest request ) {

		boolean hasStock = false;

		for (StoreStock storeStock : batch.getStoreStockList()) {
			if (storeStock.getStore().getStoreId().equals(storeId)) {

				hasStock = true;
				Integer avaialableStoreStock = GlobalDao.getStoreStockByBatch(storeId, batch.getId());

				Integer balanceQuantity = avaialableStoreStock + issueQuantity;

				storeStock.setAvailableQty(balanceQuantity);

				Integer currentQuantity = batch.getCurrentQuantity() + issueQuantity;
				batch.setCurrentQuantity(currentQuantity);
				StockTransaction stockTransaction=	new StockTransaction(balanceQuantity, screen, screen.getCode(), issueQuantity,
						batch.getStoreStockList().getFirst().getStore(),0);
			//	sessionService.setEntityDefaults(stockTransaction);
				stockTransaction.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
				batch.addStockTransaction(stockTransaction);
			}
		}
		if (!hasStock) {

			new StoreStock(issueQuantity, new Store(storeId), new Batch(batch.getId()));

			Integer avaialableStoreStock = GlobalDao.getStoreStockByBatch(storeId, batch.getId());

			Integer currentQuantity = batch.getCurrentQuantity() + issueQuantity;
			batch.setCurrentQuantity(currentQuantity);
			StockTransaction stockTransaction=new StockTransaction(avaialableStoreStock, screen, screen.getCode(),
					issueQuantity, batch.getStoreStockList().getFirst().getStore(),0);
			//sessionService.setEntityDefaults(stockTransaction);
			stockTransaction.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
			batch.addStockTransaction(stockTransaction);
		}
	}
	
	private void saveRejectDetails(LinenRequest request) {
		Timestamp now = new Timestamp((new DateTime()).getMillis());
		boolean flag = false;
		LinenReturn linenReturn = linenReturnAcceptanceDao.getLinenReturnAcceptanceId(request.getId());
		
		RejectedAcceptance rejectAcceptance = new RejectedAcceptance(new Store(linenReturn.getToStore().getStoreId()), new User(linenReturn.getAddedBy().getUserId()),
				now, "RET-001", new Status(com.param.common.constants.Status.Pending.getStatus()), new Store(linenReturn.getFromStore().getStoreId()),
				new LinenReturn(linenReturn.getId()));
	//	sessionService.setEntityDefaults(rejectAcceptance);	
		rejectAcceptance.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
		for(ItemRequest item : request.getItems())
		{
			if(item.getRejectQuantity() > 0)
			{
				flag = true;
		
				LinenReturnDetail linenReturnDetail =  linenReturnAcceptanceDao.getLinenReturnDetail(item.getId());
				
				RejectedAcceptanceDetail rejectedAcceptanceDetail=new RejectedAcceptanceDetail(new Batch(item.getBatchId()), (item.getRejectNote() != null) ? item.getRejectNote() : null, 
						(item.getRejectQuantity() != null) ? item.getRejectQuantity() : null,
						(item.getRejectReasonId() != null) ? new RejectReason(item.getRejectReasonId()) : null,
						new LinenReturnDetail(linenReturnDetail.getId()));
				//sessionService.setEntityDefaults(rejectedAcceptanceDetail);	
				rejectedAcceptanceDetail.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
				rejectAcceptance.addRejectedAcceptanceDetail(rejectedAcceptanceDetail);
			}
		}
		if(flag == true)
		{
			linenReturnAcceptanceDao.saveRejectAcceptance(rejectAcceptance);
		}
	}

}