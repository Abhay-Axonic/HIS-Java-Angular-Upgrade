package com.param.inventory.store.service.batchChangeApproval;

import java.util.List;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.inventory.BatchExp;
import com.param.entity.model.inventory.BatchExpDetail;
import com.param.entity.model.inventory.BatchUomMapper;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.RejectReason;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.User;
import com.param.global.dao.global.GlobalDao;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dto.items.UomResponse;
import com.param.global.utility.Util;
import com.param.inventory.store.dao.batchChange.IBatchChangeDao;
import com.param.inventory.store.dto.batchChange.BatchChangeRequest;
import com.param.inventory.store.dto.batchChange.BatchChangeResponse;
import com.param.inventory.store.dto.batchChange.BatchRequest;
import com.param.inventory.store.dto.batchChange.BatchResponse;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service("BatchChangeApprovalService")
public class BatchChangeApprovalService implements IBatchChangeApprovalService {

	@Autowired
	IBatchChangeDao BatchChangeDao;

	@Autowired
	IGlobalDao GlobalDao;
	
	@Autowired
	SessionService sessionService;

	private Integer screenId = Screen.BatchExpiryApproval.getScreen();

	@Override
	@Transactional
	public Object getBatchChangeList(BatchChangeRequest request) {
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());
		return BatchChangeDao.getBatchChangeList(request, screenId);
	}

	@Override
	@Transactional
	public Object getBatchChangeDetails(Integer id) {
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
	public Object approveBatchChange(BatchChangeRequest request) {
		for (Integer id : request.getAdjustmentId()) {
			BatchExp batchExp = BatchChangeDao.getBathExp(id);

			for (BatchExpDetail detail : batchExp.getBatchExpDetails()) {
				if (detail.getIsActive() && !detail.getIsDeleted() && detail.getStatus().getId()
						.equals(com.param.common.constants.Status.PendingApproval.getStatus())) {

					/*
					 * Double mrpLeaseQuantity = Util.GetMrpLeaseQuantity(detail.getNewMrp(),
					 * detail.getBatch().getBatchUomList(),
					 * detail.getBatch().getBatchUomList().get(0).getUomType()); //Batch
					 * b=detail.getBatch(); Integer
					 * batchId=BatchChangeDao.getBatchId(detail.getId());
					 * BatchChangeDao.getbatch(batchId , detail , mrpLeaseQuantity);
					 */
					Integer batchId = BatchChangeDao.getBatchId(detail.getId());
					List<BatchUomMapper> list = BatchChangeDao.getBatchUomMapper(batchId);
					Double mrpLeaseQuantity = Util.GetMrpLeaseQuantity(detail.getNewMrp(), list,
							detail.getMrpUomType());
					Double lcopLeaseQuantity = 0d;
					Boolean flag = false;
					if (detail.getLcop() != null && detail.getLcop() > 0) {
						lcopLeaseQuantity = Util.GetMrpLeaseQuantity(detail.getLcop(), list, detail.getMrpUomType());
						flag = true;
					}
					Double copLeaseQuantity = Util.GetMrpLeaseQuantity(detail.getCop(), list, detail.getMrpUomType());

					if (flag.equals(true)) {
						BatchChangeDao.getbatch(batchId, detail, mrpLeaseQuantity, lcopLeaseQuantity, copLeaseQuantity);
					} else {
						BatchChangeDao.getbatchWithOutLcop(batchId, detail, mrpLeaseQuantity, copLeaseQuantity);
					}

					/*
					 * Batch b =BatchChangeDao.getbatch(detail.getId());
					 * b.setBatchNo(detail.getNewBatchNo());
					 * b.setExpiryDate(detail.getNewBatchExpiry());
					 * b.setSellingPrice(mrpLeaseQuantity); b.setRate(mrpLeaseQuantity);
					 * b.setUpdatedByUser(request.getUserId()); b.setUpdatedDate(TimeZoneComponent.getDateByzone(unitId));
					 * BatchChangeDao.updateBatch(b);
					 */
					/*
					 * detail.getBatch().updateBatchChange(detail.getNewBatchNo(),
					 * detail.getNewBatchExpiry(), mrpLeaseQuantity,request.getUserId());
					 */
					detail.approveBatchChangeDetail(new Status(com.param.common.constants.Status.Approved.getStatus()),
							null, null, request.getUserId());
				}
			}

			batchExp.setStatus(new Status(request.getStatusId()));
			batchExp.setApprovalStatus(new Status(request.getApprovalStatusId()));

			batchExp.setApprovedOn(TimeZoneComponent.getDateByzone(batchExp.getUnitId()));
			// batchExp.setApprovedBy(batchExp.getUser());
			batchExp.setApprovedBy(new User(request.getUserId()));

			batchExp.setUpdatedDate(TimeZoneComponent.getDateByzone(batchExp.getUnitId()));
			// batchExp.setUpdatedBy(batchExp.getUser());
			batchExp.setUpdatedByUser(request.getUserId());

			BatchChangeDao.updateBatchChange(batchExp);
		}

		CommonResponse response = new CommonResponse();
		response.setMessage("Batch change request details approved successfully");

		return response;
	}

	@Override
	@Transactional
	public Object approveBatchChangeDetails(BatchChangeRequest request) {
		BatchExp batchExp = BatchChangeDao.getBathExp(request.getId());

		for (BatchExpDetail detail : BatchChangeDao.getBatchChangeDetailsById(batchExp.getId())) {
			for (BatchRequest batchRequest : request.getBatch()) {
				if (batchRequest.getId() != null && batchRequest.getId().equals(detail.getId()) && detail.getStatus()
						.getId().equals(com.param.common.constants.Status.PendingApproval.getStatus())) {

					if (batchRequest.getStatusId().equals(com.param.common.constants.Status.Approved.getStatus())) {
						// Batch b=detail.getBatch();
						Integer batchId = BatchChangeDao.getBatchId(detail.getId());
						List<BatchUomMapper> list = BatchChangeDao.getBatchUomMapper(batchId);
						Double mrpLeaseQuantity = Util.GetMrpLeaseQuantity(detail.getNewMrp(), list,
								detail.getMrpUomType());
						Double lcopLeaseQuantity = 0d;
						Boolean flag = false;
						if (detail.getLcop() != null && detail.getLcop() > 0) {
							lcopLeaseQuantity = Util.GetMrpLeaseQuantity(detail.getLcop(), list,
									detail.getMrpUomType());
							flag = true;
						}

						Double copLeaseQuantity = Util.GetMrpLeaseQuantity(detail.getCop(), list,
								detail.getMrpUomType());
						if (flag.equals(true)) {
							BatchChangeDao.getbatch(batchId, detail, mrpLeaseQuantity, lcopLeaseQuantity,
									copLeaseQuantity);
						} else {
							BatchChangeDao.getbatchWithOutLcop(batchId, detail, mrpLeaseQuantity, copLeaseQuantity);
						}

						/*
						 * Batch b1=new Batch(); b1=b; b1.setId(Long.valueOf(batchId));
						 * b1.setBatchNo(detail.getNewBatchNo());
						 * b1.setExpiryDate(detail.getNewBatchExpiry());
						 * b1.setSellingPrice(mrpLeaseQuantity); b1.setRate(mrpLeaseQuantity);
						 * b1.setUpdatedByUser(request.getUserId()); b1.setUpdatedDate(TimeZoneComponent.getDateByzone(unitId));
						 */
						// BatchChangeDao.updateBatch(b1);
						/*
						 * detail.getBatch().updateBatchChange(detail.getNewBatchNo(),
						 * detail.getNewBatchExpiry(), mrpLeaseQuantity,request.getUserId());
						 */
					}

					detail.approveBatchChangeDetail(new Status(batchRequest.getStatusId()),
							batchRequest.getRejectNote(),
							batchRequest.getRejectReasonId() != null
									? new RejectReason(batchRequest.getRejectReasonId())
									: null,
							request.getUserId());
					break;
				}
			}

		}

		batchExp.setStatus(new Status(request.getStatusId()));
		batchExp.setApprovalStatus(new Status(request.getApprovalStatusId()));

		batchExp.setApprovedOn(TimeZoneComponent.getDateByzone(batchExp.getUnitId()));
		// batchExp.setApprovedBy(batchExp.getUser());
		batchExp.setApprovedBy(new User(request.getUserId()));

		batchExp.setUpdatedDate(TimeZoneComponent.getDateByzone(batchExp.getUnitId()));
		// batchExp.setUpdatedBy(batchExp.getUser());
		batchExp.setUpdatedByUser(request.getUserId());

		BatchChangeDao.updateBatchChange(batchExp);

		CommonResponse response = new CommonResponse();
		response.setMessage("Batch change request details approved successfully");

		return response;
	}

	@Override
	@Transactional
	public Object cancelBatchChange(BatchChangeRequest request) {

		BatchExp batchExp = BatchChangeDao.getBathExp(request.getId());

		for (BatchExpDetail detail : batchExp.getBatchExpDetails()) {
			if (detail.getIsActive() && !detail.getIsDeleted() && detail.getStatus().getId()
					.equals(com.param.common.constants.Status.PendingApproval.getStatus())) {
				detail.setStatus(new Status(com.param.common.constants.Status.Cancelled.getStatus()));
				detail.setUpdatedDate(TimeZoneComponent.getDateByzone(batchExp.getUnitId()));
				// detail.setUpdatedBy(detail.getUser());
				detail.setUpdatedByUser(request.getUserId() != null ? request.getUserId() : sessionService.getUserId());
			}
		}

		batchExp.setCancelReason(new CancelReason(request.getCancelReasonId()));
		batchExp.setCancelNote(request.getCancelNote());

		batchExp.setStatus(new Status(request.getStatusId()));

		batchExp.setUpdatedDate(TimeZoneComponent.getDateByzone(batchExp.getUnitId()));
		// batchExp.setUpdatedBy(batchExp.getUser());
		batchExp.setUpdatedByUser(request.getUserId() != null ? request.getUserId() : sessionService.getUserId());

		BatchChangeDao.updateBatchChange(batchExp);

		CommonResponse response = new CommonResponse();
		response.setMessage("Batch change request cancelled successfully");

		return response;
	}
}
