package com.param.inventory.nursing.service.consumableAcceptance;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.NonBillableConsumption;
import com.param.entity.model.inventory.NonBillableConsumptionDetail;
import com.param.entity.model.inventory.StockTransaction;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.RejectReason;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.UomType;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.utility.Util;
import com.param.inventory.nursing.dao.nonBillableConsumption.IConsumptionDao;
import com.param.inventory.nursing.dto.nonBillableConsumption.BatchRequest;
import com.param.inventory.nursing.dto.nonBillableConsumption.ConsumptionDropdownResponse;
import com.param.inventory.nursing.dto.nonBillableConsumption.ConsumptionRequest;
import com.param.inventory.nursing.dto.nonBillableConsumption.ConsumptionResponse;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service("ConsumableAcceptanceService")
public class ConsumableAcceptanceService implements IConsumableAcceptanceService {

	@Autowired
	IConsumptionDao ConsumptionDao;

	@Autowired
	IGlobalDao GlobalDao;

	private Integer screenId = Screen.ConsumableAcceptance.getScreen();

	private com.param.entity.model.master.Screen screen;
	
	@Autowired
	private SessionService sessionService;
	
	@Override
	@Transactional
	public Object getDropdowns(Integer unitId, Integer orgId, Integer userId) {
		ConsumptionDropdownResponse response = new ConsumptionDropdownResponse();

		for (Store store : GlobalDao.getUserStoreList(unitId,orgId,userId)) {
			response.addStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		}

		for (ScreenStatus screenStatus : GlobalDao.getScreenStatus(screenId)) {
			response.addStatus(new com.param.global.dto.global.Status(screenStatus.getStatus().getId(),
					screenStatus.getStatus().getStatus()));
		}

		return response;
	}

	@Override
	@Transactional
	public Object getConsumableAcceptanceList(ConsumptionRequest request) {
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());
		return ConsumptionDao.getConsumptionList(request, screenId);
	}

	@Override
	@Transactional
	public Object getConsumableAcceptanceDetail(Integer id) {
		ConsumptionResponse response = ConsumptionDao.getConsumption(id);
		response.setBatch(ConsumptionDao.getConsumptionDetail(id));
		return response;
	}

	@Override
	@Transactional
	public Object approveConsumableAcceptance(ConsumptionRequest request) {
		screen = GlobalDao.getScreen(screenId);
		for (Integer id : request.getConsumptionId()) {
			NonBillableConsumption consumption = ConsumptionDao.getNonBillableConsumption(id);

			for (NonBillableConsumptionDetail detail : consumption.getNonBillableConsumptionDetails()) {
				if (detail.getIsActive() && !detail.getIsDeleted() && detail.getStatus().getId()
						.equals(com.param.common.constants.Status.PendingApproval.getStatus())) {

					detail.setBatch(BatchStoreTransaction(detail.getBatch().getId(),
							consumption.getStore().getStoreId(), detail.getConsumedQty(), detail.getUomType(),request.getUnitId(),detail));

					detail.approveConsumption(detail.getConsumedQty(), detail.getRejectQty(), detail.getRejectNote(),
							new Status(com.param.common.constants.Status.Approved.getStatus()), null,request.getUserId());
				}
			}

			consumption.setStatus(new Status(request.getStatusId()));
			consumption.setApprovalStatus(new Status(request.getApprovalStatusId()));

			consumption.setApprovedOn(TimeZoneComponent.getDateByzone(consumption.getUnitId()));
			consumption.setApprovedByUser(request.getUserId());
			/*consumption.setApprovedBy(consumption.getUser());*/
			consumption.setUpdatedDate(TimeZoneComponent.getDateByzone(consumption.getUnitId()));
			//consumption.setUpdatedBy(consumption.getUser());
			consumption.setUpdatedByUser(request.getUserId());
			ConsumptionDao.updateConsumption(consumption);
		}

		CommonResponse response = new CommonResponse();
		response.setMessage("Non billable consumption approved successfully");

		return response;
	}

	@Override
	@Transactional
	public Object approveConsumableAcceptanceDetails(ConsumptionRequest request) {
		screen = GlobalDao.getScreen(screenId);
		NonBillableConsumption consumption = ConsumptionDao.getNonBillableConsumption(request.getId());

		for (NonBillableConsumptionDetail detail : consumption.getNonBillableConsumptionDetails()) {
			for (BatchRequest batchRequest : request.getBatch()) {
				if (batchRequest.getId() != null && batchRequest.getId().equals(detail.getId()) && detail.getStatus()
						.getId().equals(com.param.common.constants.Status.PendingApproval.getStatus())) {
					if (detail.getBatch()!=null && consumption.getStore()!=null && batchRequest.getAcceptedQuantity()!=null && detail.getUomType()!=null)
					{
						detail.setBatch(
								BatchStoreTransaction(detail.getBatch().getId(), consumption.getStore().getStoreId(),
										batchRequest.getAcceptedQuantity(), detail.getUomType(),consumption.getUnit().getUnitId(),detail));

					}
					detail.approveConsumption(batchRequest.getAcceptedQuantity(), batchRequest.getRejectedQuantity(),
							batchRequest.getRejectNote(), new Status(batchRequest.getStatusId()),
							batchRequest.getRejectReasonId() != null
									? new RejectReason(batchRequest.getRejectReasonId())
									: null,request.getUserId());
					break;
				}
			}
		}

		consumption.setStatus(new Status(request.getStatusId()));
		consumption.setApprovalStatus(new Status(request.getApprovalStatusId()));

		consumption.setApprovedOn(TimeZoneComponent.getDateByzone(consumption.getUnitId()));
		//consumption.setApprovedBy(consumption.getUser());
		consumption.setApprovedByUser(request.getUserId());
		consumption.setUpdatedDate(TimeZoneComponent.getDateByzone(consumption.getUnitId()));
		consumption.setUpdatedByUser(request.getUserId());
		//consumption.setUpdatedBy(consumption.getUser());

		ConsumptionDao.updateConsumption(consumption);

		CommonResponse response = new CommonResponse();
		response.setMessage("Consumption details approved successfully");

		return response;
	}

	@Override
	@Transactional
	public Object cancelConsumableAcceptance(ConsumptionRequest request) {

		NonBillableConsumption consumption = ConsumptionDao.getNonBillableConsumption(request.getId());

		for (NonBillableConsumptionDetail detail : consumption.getNonBillableConsumptionDetails()) {
			if (detail.getIsActive() && !detail.getIsDeleted() && detail.getStatus().getId()
					.equals(com.param.common.constants.Status.PendingApproval.getStatus())) {
				detail.setStatus(new Status(com.param.common.constants.Status.Cancelled.getStatus()));
				detail.setUpdatedDate(TimeZoneComponent.getDateByzone(consumption.getUnitId()));
				//detail.setUpdatedBy(sessionService.getUserId());
				detail.setUpdatedByUser(sessionService.getUserId());
			}
		}

		consumption.setCancelReason(new CancelReason(request.getCancelReasonId()));
		consumption.setCancelNote(request.getCancelNote());

		consumption.setStatus(new Status(request.getStatusId()));

		consumption.setUpdatedDate(TimeZoneComponent.getDateByzone(consumption.getUnitId()));
		consumption.setUpdatedByUser(sessionService.getUserId());
		//consumption.setUpdatedBy(consumption.getUser());

		ConsumptionDao.updateConsumption(consumption);

		CommonResponse response = new CommonResponse();
		response.setMessage("Consumption cancelled successfully");

		return response;
	}

	private Batch BatchStoreTransaction(Long batchId, Integer storeId, Integer quantity, UomType uomType,Integer unitId , NonBillableConsumptionDetail nonBillableConsumptionDetail) {

		// get batch needs to be issued
		Batch batch = GlobalDao.getBatch(storeId, batchId,unitId);

		// Get existing batch store stock
		Integer avaialableStoreStock = GlobalDao.getStoreStockByBatch(storeId, batch.getId(),0);//0 itemId

		// Get lease quantity
		Integer leaseQuantity = 0;
		if (batch.getBatchUomList()!=null)
		{
			leaseQuantity = Util.GetLeaseQuantity(quantity, batch.getBatchUomList(), uomType);
		}
		else
		{
			leaseQuantity=quantity;
		}

		// Calculate store stock balance quantity
		Integer balanceQuantity = avaialableStoreStock - leaseQuantity;

		// Deduct issued quantity from the batch store stock
		batch.getStoreStockList().getFirst().setAvailableQty(balanceQuantity);

		// Calculate current batch quantity

		// Set current batch quantity
		batch.setCurrentQuantity(batch.getCurrentQuantity() - leaseQuantity);

		// Add stock transaction
		StockTransaction stockTransaction=new StockTransaction(balanceQuantity, screen, screen.getCode(), leaseQuantity,
				batch.getStoreStockList().getFirst().getStore(), nonBillableConsumptionDetail.getId());
		stockTransaction.setTransactionId(nonBillableConsumptionDetail.getNonBillableConsumption().getId());
		stockTransaction.setBatch(batch);
		stockTransaction.setItem(batch.getItem());
		stockTransaction.setOutQty(leaseQuantity);
		stockTransaction.setIsTransit(false);
		//sessionService.setEntityDefaults(stockTransaction);
		//stockTransaction.setNonBillableConsumptionDetail(nonBillableConsumptionDetail);
		
		stockTransaction.setCreateDefaults(sessionService.getOrganizationId(),unitId,sessionService.getUserId());
		batch.addStockTransaction(stockTransaction);

		return batch;
	}
}
