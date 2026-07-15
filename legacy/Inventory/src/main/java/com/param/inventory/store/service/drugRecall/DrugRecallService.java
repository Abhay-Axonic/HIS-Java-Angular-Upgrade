package com.param.inventory.store.service.drugRecall;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.adt.master.global.model.ReasonMaster;
import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.DrugRecall;
import com.param.entity.model.inventory.DrugRecallDetail;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.RequestType;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.User;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.service.global.IGlobalService;
import com.param.inventory.store.dao.drugRecall.IDrugRecallDao;
import com.param.inventory.store.dto.drugRecall.DrugRecallDropdownResponse;
import com.param.inventory.store.dto.drugRecall.DrugRecallItemRequest;
import com.param.inventory.store.dto.drugRecall.DrugRecallItemResponse;
import com.param.inventory.store.dto.drugRecall.DrugRecallRequest;
import com.param.inventory.store.dto.drugRecall.DrugRecallResponse;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service("DrugRecallService")
public class DrugRecallService implements IDrugRecallService {

	private Integer screenId = Screen.DrugRecall.getScreen();

	@Autowired
	private SessionService sessionService;

	@Autowired
	IDrugRecallDao DrugRecallDao;

	@Autowired
	IGlobalDao GlobalDao;
	
	@Autowired
	IGlobalService GlobalService;

	@Override
	@Transactional
	public DrugRecallDropdownResponse getDropdowns() {

		DrugRecallDropdownResponse response = new DrugRecallDropdownResponse();

		for (ReasonMaster reason : GlobalDao.getReasonList()) {
			response.addReason(new ReasonMaster(reason.getReasonMasterId(), reason.getReasonDesc()));
		}
		return response;
	}

	@Override
	@Transactional
	public Object getDrugRecallList(DrugRecallRequest request) {
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());
		return DrugRecallDao.searchDrugRecallList(request, screenId);
	}

	@Override
	@Transactional
	public Object saveDrugRecall(DrugRecallRequest request) {

		/*Unit unit = GlobalDao.getUnit(sessionService.getUnitId());*/

		String recallNumber = GlobalService.getBillNoByGeneratedCode(0, request.getUnitId(), screenId);

		DrugRecall drugRecall = new DrugRecall(recallNumber, new RequestType(request.getRequestTypeId()),
				new Status(request.getStatusId()));

		//sessionService.setEntityDefaults(drugRecall);
		drugRecall.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
		for (DrugRecallItemRequest items : request.getItems())
		{
			DrugRecallDetail drugRecallDetail=new DrugRecallDetail(
					(items.getItemId() != null) ? new Item(items.getItemId()) : null,
					(items.getBatchId() != null) ? new Batch(items.getBatchId()) : null, 
					items.getReason());
		//	sessionService.setEntityDefaults(drugRecallDetail);
			drugRecallDetail.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
		 drugRecall.addDrugRecallDetailList(drugRecallDetail);
		 
		}

		DrugRecallDao.saveDrugRecall(drugRecall);

		CommonResponse response = new CommonResponse();
		response.setId(drugRecall.getId().longValue());
		response.setMessage("Drug Recall saved successfully");

		return response;
	}
	
	@Override
	@Transactional
	public Object getDrugRecall(Integer id) {

		DrugRecall drugRecall = DrugRecallDao.getDrugRecall(id);

		DrugRecallResponse response = new DrugRecallResponse(drugRecall.getId(), drugRecall.getRecallNo(),
				drugRecall.getAddedDate(), drugRecall.getUpdatedBy().getUserName(), drugRecall.getRequestType().getId(),
				drugRecall.getRequestType().getType(), drugRecall.getStatus().getId(),
				drugRecall.getStatus().getStatus());

		for (DrugRecallDetail detail : drugRecall.getDrugRecallDetailList()) {
			//check if condation for resolved HIS-10204 Cancelled Item Getting Display in Drug Recall Details.
			if (!detail.getIsDeleted()) {
				response.addItems(new DrugRecallItemResponse(detail.getId(),
						detail.getItem() != null ? detail.getItem().getId() : detail.getBatch().getItem().getId(),
						detail.getItem() != null ? detail.getItem().getCode() : detail.getBatch().getItem().getCode(),
						detail.getItem() != null ? detail.getItem().getName() : detail.getBatch().getItem().getName(),
						detail.getBatch() != null ? detail.getBatch().getId() : null,
						detail.getBatch() != null ? detail.getBatch().getBatchNo() : null,
						detail.getBatch() != null ? detail.getBatch().getExpiryDate() : null, detail.getReason()));
			}
		}

		return response;
	}

	@Override
	@Transactional
	public Object cancelDrugRecall(DrugRecallRequest request) {
		DrugRecall drugRecall = DrugRecallDao.getDrugRecall(request.getId());

		drugRecall.setCancelReason(new CancelReason(request.getCancelReasonId()));
		drugRecall.setCancelNote(request.getCancelNote());

		drugRecall.setStatus(new Status(request.getStatusId()));

		drugRecall.setUpdatedDate(TimeZoneComponent.getDateByzone(drugRecall.getUnitId()));
		drugRecall.setUpdatedByUser(request.getUserId());

		DrugRecallDao.updateDrugRecall(drugRecall);

		CommonResponse response = new CommonResponse();
		response.setMessage("Drug Recall cancelled successfully");

		return response;
	}

	@Override
	@Transactional
	public Object updateDrugRecall(DrugRecallRequest request) {

		DrugRecall drugRecall = DrugRecallDao.getDrugRecall(request.getId());

		for (DrugRecallDetail detail : drugRecall.getDrugRecallDetailList()) {
			Integer id = null;
			
			for (DrugRecallItemRequest item : request.getItems()) {
				if (item.getId() != null && item.getId().equals(detail.getId())) {
					id = item.getId();
					detail.updateDrugRecallDetail((item.getItemId() != null) ? new Item(item.getItemId()) : null,
							(item.getBatchId() != null) ? new Batch(item.getBatchId()) : null, item.getReason());
					break;
				}
			}

			if (id == null) {
				detail.setIsDeleted(true);
			}

			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(drugRecall.getUnitId()));
		//	detail.setUpdatedBy(detail.getUser());
			detail.setUpdatedByUser(request.getUserId());
		}

		for (DrugRecallItemRequest item : request.getItems()) {
			if (item.getId() == null) {
				
				DrugRecallDetail recall = new DrugRecallDetail(new Item(item.getItemId()), item.getReason());
			//	sessionService.setEntityDefaults(recall);	
				recall.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
				drugRecall.addDrugRecallDetailList(recall);
			}
		}
	
		drugRecall.setUpdatedDate(TimeZoneComponent.getDateByzone(drugRecall.getUnitId()));
	//	drugRecall.setUpdatedBy(drugRecall.getUser());
		drugRecall.setUpdatedByUser(request.getUserId());

		DrugRecallDao.updateDrugRecall(drugRecall);

		CommonResponse response = new CommonResponse();
		response.setMessage("Drug Recall updated successfully");

		return response;
	}

	@Override
	@Transactional
	public Object approveDrugRecall(DrugRecallRequest request) {
		
		for (Integer id : request.getDrugRecallId()) {
			DrugRecall drugRecall = DrugRecallDao.getDrugRecall(id);

			drugRecall.setStatus(new Status(request.getStatusId()));

			drugRecall.setApprovedDate(TimeZoneComponent.getDateByzone(drugRecall.getUnitId()));
			drugRecall.setApprovedBy(new User(request.getUserId()));

			drugRecall.setUpdatedDate(TimeZoneComponent.getDateByzone(drugRecall.getUnitId()));
		//	drugRecall.setUpdatedBy(drugRecall.getUser());
			drugRecall.setUpdatedByUser(request.getUserId());

			for (DrugRecallDetail drugRecallDetail : drugRecall.getDrugRecallDetailList()) {

				List<Batch> BatchList = new ArrayList<>();
				drugRecallDetail.getItem().getBatchList().stream().filter(batch -> batch.isQuarantine().equals(true))
						.forEach(batch -> { batch.isQuarantine();
											BatchList.add(batch);
						});
				drugRecallDetail.getItem().getBatchList().removeAll(BatchList);

				for (Batch batchList : drugRecallDetail.getItem().getBatchList()) {
					
					updateBatch(drugRecallDetail.getBatch() != null ? drugRecallDetail.getBatch().getId() : batchList.getId() );
				}
				drugRecallDetail.setUpdatedDate(TimeZoneComponent.getDateByzone(drugRecall.getUnitId()));
		//		drugRecallDetail.setUpdatedBy(drugRecallDetail.getUser());
				drugRecallDetail.setUpdatedByUser(request.getUserId());
			}

			DrugRecallDao.updateDrugRecall(drugRecall);
		}

		CommonResponse response = new CommonResponse();
		response.setMessage("Drug Recall approved successfully");

		return response;
	}

	private void updateBatch(Long id) {
		Batch batch = GlobalDao.getBatch(id);
		batch.setIsPharmacy(true);
		GlobalDao.updateBatchLeaseQuantity(batch);
	}

}
