package com.param.inventory.store.service.batchChange;

import java.util.List;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.BatchExp;
import com.param.entity.model.inventory.BatchExpDetail;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.User;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dto.items.UomResponse;
import com.param.global.service.global.IGlobalService;
import com.param.inventory.store.dao.batchChange.IBatchChangeDao;
import com.param.inventory.store.dto.batchChange.BatchChangeRequest;
import com.param.inventory.store.dto.batchChange.BatchChangeResponse;
import com.param.inventory.store.dto.batchChange.BatchRequest;
import com.param.inventory.store.dto.batchChange.BatchResponse;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service("BatchChangeService")
public class BatchChangeService implements IBatchChangeService {

	@Autowired
	private SessionService sessionService;

	@Autowired
	IBatchChangeDao BatchChangeDao;
	
	@Autowired
	IGlobalDao GlobalDao;
	
	@Autowired
	IGlobalService GlobalService;

	private Integer screenId = Screen.BatchExpiry.getScreen();

	@Override
	@Transactional
	public Object saveBatchChange(BatchChangeRequest request) {

		/*Integer storeId = request.getStoreId();

		Store storeNew = GlobalDao.getStore(storeId);

		Unit unit = GlobalDao.getUnit(sessionService.getUnitId());*/

		String batchNumber = GlobalService.getBillNoByGeneratedCode(request.getStoreId(), request.getUnitId(), screenId);
		
		BatchExp batchExp = new BatchExp(batchNumber, new Status(request.getStatusId()),
				new Status(request.getApprovalStatusId()), new Store(request.getStoreId()));
	//	sessionService.setEntityDefaults(batchExp);	
		batchExp.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
		for (BatchRequest batchRequest : request.getBatch())
		{
			
			BatchExpDetail batchExpDetail=new BatchExpDetail(batchRequest.getNewBatchExpiry(),
					batchRequest.getNewBatchNo(), batchRequest.getNewMrp(), batchRequest.getOldBatchExpiry(),
					batchRequest.getOldBatchNo(), batchRequest.getOldMrp(), new Status(batchRequest.getStatusId()),
					new Batch(batchRequest.getBatchId()), new UomType(batchRequest.getMrpUomTypeId()), batchRequest.getLcop());
		//	sessionService.setEntityDefaults(batchExpDetail);	
			batchExpDetail.setCop(batchRequest.getCop());
			batchExpDetail.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
			batchExp.addBatchExpDetail(batchExpDetail);
		}

		BatchChangeDao.saveBatchChange(batchExp);

		CommonResponse response = new CommonResponse();
		response.setId(batchExp.getId().longValue());
		response.setMessage("Batch expiry change request saved successfully");

		return response;
	}

	@Override
	@Transactional
	public Object getBatchChangeList(BatchChangeRequest request) {
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());
		return BatchChangeDao.getBatchChangeList(request, screenId);
	}

	@Override
	@Transactional
	public Object getBatchChangeDetail(Integer id) {
		BatchChangeResponse response = BatchChangeDao.getBatchChange(id);
		response.setBatch(BatchChangeDao.getBatchChangeDetails(id));
		for (BatchResponse d : response.getBatch()) {
			List<UomResponse> uomList = GlobalDao.getBatchUomList(d.getBatchId());
			d.setUomList(uomList);
		}
		return response;
	}

	@Override
	@Transactional
	public Object updateBatchChange(BatchChangeRequest request) {

		BatchExp batchExp = BatchChangeDao.getBathExp(request.getId());

		for (BatchExpDetail detail : batchExp.getBatchExpDetails()) {
			Integer id = null;

			for (BatchRequest batchRequest : request.getBatch()) {
				if (batchRequest.getId() != null && batchRequest.getId().equals(detail.getId())) {
					id = batchRequest.getId();
					detail.updateDetails(batchRequest.getNewBatchExpiry(), batchRequest.getNewBatchNo(),
							batchRequest.getNewMrp(), new UomType(batchRequest.getMrpUomTypeId()));
					detail.setCop(batchRequest.getCop());
					break;
				}
			}
			if (id == null) {
				detail.setIsDeleted(true);
			}
			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(batchExp.getUnitId()));
		//	detail.setUpdatedBy(detail.getUser());
			detail.setUpdatedByUser(request.getUserId());
		}
		for (BatchRequest batchRequest : request.getBatch()) {
			if (batchRequest.getId() == null) 
			{
				BatchExpDetail batchExpDetail=new BatchExpDetail(batchRequest.getNewBatchExpiry(),
						batchRequest.getNewBatchNo(), batchRequest.getNewMrp(), batchRequest.getOldBatchExpiry(),
						batchRequest.getOldBatchNo(), batchRequest.getOldMrp(), new Status(batchRequest.getStatusId()),
						new Batch(batchRequest.getBatchId()), new UomType(batchRequest.getMrpUomTypeId()));
		//		sessionService.setEntityDefaults(batchExpDetail);	
				batchExpDetail.setLcop(batchRequest.getLcop());
				batchExpDetail.setCop(batchRequest.getCop());
				batchExpDetail.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),request.getUserId());
				batchExp.addBatchExpDetail(batchExpDetail);
			}
		}
		BatchChangeDao.updateBatchChange(batchExp);

		CommonResponse response = new CommonResponse();
		response.setId(batchExp.getId().longValue());
		response.setMessage("Batch expiry change request updated successfully");

		return response;
	}

	@Override
	@Transactional
	public Object cancelBatchChange(BatchChangeRequest request) {

		BatchExp batchExp = BatchChangeDao.getBathExp(request.getId());

		for (BatchExpDetail detail : batchExp.getBatchExpDetails()) {
			detail.setStatus(new Status(com.param.common.constants.Status.Cancelled.getStatus()));

			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(batchExp.getUnitId()));
		//	detail.setUpdatedBy(detail.getUser());
			detail.setUpdatedByUser(request.getUserId());
		}

		batchExp.setCancelReason(new CancelReason(request.getCancelReasonId()));
		batchExp.setCancelNote(request.getCancelNote());

		batchExp.setStatus(new Status(request.getStatusId()));
		batchExp.setApprovalStatus(new Status(request.getApprovalStatusId()));

		batchExp.setUpdatedDate(TimeZoneComponent.getDateByzone(batchExp.getUnitId()));
	//	batchExp.setUpdatedBy(batchExp.getUser());
		batchExp.setUpdatedByUser(request.getUserId());

		BatchChangeDao.updateBatchChange(batchExp);

		CommonResponse response = new CommonResponse();
		response.setMessage("Batch change cancelled successfully");

		return response;
	}

	@Override
	@Transactional
	public Object submitBatchChange(BatchChangeRequest request) {
		for (Integer id : request.getAdjustmentId()) {
			BatchExp batchExp = BatchChangeDao.getBathExp(id);

			batchExp.setStatus(new Status(request.getStatusId()));
			batchExp.setApprovalStatus(new Status(request.getApprovalStatusId()));

			for (BatchExpDetail detail : batchExp.getBatchExpDetails()) {
				if (!detail.getIsDeleted()) {
					detail.setStatus(new Status(com.param.common.constants.Status.PendingApproval.getStatus()));
					detail.setUpdatedDate(TimeZoneComponent.getDateByzone(batchExp.getUnitId()));
				//	detail.setUpdatedBy(detail.getUser());
					detail.setUpdatedByUser(request.getUserId());
				}
			}

			batchExp.setApprovedOn(TimeZoneComponent.getDateByzone(batchExp.getUnitId()));
			batchExp.setApprovedBy(new User(request.getUserId()));

			batchExp.setUpdatedDate(TimeZoneComponent.getDateByzone(batchExp.getUnitId()));
		//	batchExp.setUpdatedBy(batchExp.getUser());
			batchExp.setUpdatedByUser(request.getUserId());

			BatchChangeDao.updateBatchChange(batchExp);
		}

		CommonResponse response = new CommonResponse();
		response.setMessage("Batch change submitted successfully");

		return response;
	}
}