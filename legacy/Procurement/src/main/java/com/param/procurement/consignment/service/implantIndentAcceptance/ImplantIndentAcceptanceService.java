package com.param.procurement.consignment.service.implantIndentAcceptance;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.adt.master.global.model.ReasonMaster;
import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.adt.Admission;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.IndentType;
import com.param.entity.model.inventory.StockTransaction;
import com.param.entity.model.master.RejectReason;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.VisitType;
import com.param.entity.model.patient.PatientRegistration;
import com.param.entity.model.pharmacy.Issue;
import com.param.entity.model.pharmacy.IssueDetail;
import com.param.entity.model.pharmacy.IssueDetailBatchMapper;
import com.param.entity.model.pharmacy.PatientIssueReturn;
import com.param.entity.model.pharmacy.PatientIssueReturnDetail;
import com.param.entity.model.procurement.ImplantIndent;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.service.global.IGlobalService;
import com.param.global.utility.Util;
import com.param.procurement.consignment.dao.implantIndentAcceptance.IImplantIndentAcceptanceDao;
import com.param.procurement.consignment.dto.implantIndent.ImplantIndentDropdown;
import com.param.procurement.consignment.dto.implantIndent.ImplantIndentItemResponse;
import com.param.procurement.consignment.dto.implantIndent.ImplantIndentRequest;
import com.param.procurement.consignment.dto.implantIndent.ImplantIndentRequestItems;
import com.param.procurement.consignment.dto.implantIndent.ImplantIndentResponse;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service
public class ImplantIndentAcceptanceService implements IImplantIndentAcceptanceService {

	private Integer screenId = Screen.ImplantIndentAcceptance.getScreen();

	private Integer returnScreenId = Screen.ReturnAcceptance.getScreen();

	private com.param.entity.model.master.Screen screen;

	@Autowired
	IImplantIndentAcceptanceDao ImplantIndentAcceptanceDao;

	@Autowired
	IGlobalDao GlobalDao;

	@Autowired
	IGlobalService GlobalService;

	@Autowired
	private SessionService sessionService;

	@Override
	@Transactional
	public ImplantIndentDropdown getDropdownList(Integer unitId,Integer orgId)
	{
		ImplantIndentDropdown response = new ImplantIndentDropdown();

		for (ScreenStatus status : GlobalDao.getScreenStatus(screenId)) {
			response.addStatus(new com.param.global.dto.global.Status(status.getStatus().getId(), status.getStatus().getStatus()));
		}

		
		for(Store store : GlobalDao.getStoreList(unitId))
		{
			response.addStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		}
		for (IndentType indentType : GlobalDao.getIndentTypeList(orgId,unitId)) {
			response.addIndentType(
					new com.param.global.dto.global.IndentType(indentType.getId(), indentType.getType()));
		}
		
		
		
		return response;
	}
	@Override
	@Transactional
	public Object getImplantIndentAcceptanceList(ImplantIndentRequest request) {
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());
		return ImplantIndentAcceptanceDao.searchMaterialIssueAcceptanceList(request, screenId);
	}

	@Override
	@Transactional
	public Object getImplantIndentAcceptanceDetails(Integer id,Integer unitId) {

		Issue issue = ImplantIndentAcceptanceDao.getIssue(id);

		ImplantIndentResponse response = null;
		if (issue.getImplantIndent() != null) {
			 response = new ImplantIndentResponse(issue.getId(),
					issue.getImplantIndent().getImplantIndentNo(), issue.getImplantIndent().getAddedDate(),
					issue.getImplantIndent().getIndentType().getType(),
					issue.getImplantIndent().getIndentType().getId(), issue.getImplantIndent().getStatus().getStatus(),
					issue.getImplantIndent().getStatus().getId(),
					issue.getImplantIndent().getApprovalStatus().getStatus(),
					issue.getImplantIndent().getApprovalStatus().getId(),
					issue.getImplantIndent().getPriority().getPriorityId(),
					issue.getImplantIndent().getPriority().getPriorityDesc(), issue.getImplantIndent().getIsImplant(),
					issue.getImplantIndent().getIsConsignment(), issue.getImplantIndent().getIsKit(),
					issue.getImplantIndent().getRemark(),
					issue.getAddedDate(),issue.getIssueNo(),issue.getStore().getStoreId(),
					issue.getStore().getStoreName());

			for (IssueDetail issueDetail : issue.getIssueDetail()) {

				Long storeStock = GlobalDao.getStoreStockByItem(issue.getImplantIndent().getStore().getStoreId(),
						issueDetail.getImplantIndentDetail().getItem().getId(),unitId);
				Long availableStock = Util.ConvertQuantity(storeStock,
						issueDetail.getIssueDetailBatchMapper().get(0).getBatch().getBatchUomList(),
						issueDetail.getIssueDetailBatchMapper().get(0).getUomType());

				Long stockInTransit = GlobalDao.getStoreStockInTransitByItem(
						issue.getImplantIndent().getStore().getStoreId(),
						issueDetail.getImplantIndentDetail().getItem().getId());
				// Long tQuantity = Util.ConvertQuantity(stockInTransit,
				// materialIssueDetail.getMaterialIssueBatchMappers().get(0).getBatch().getBatchUomList(),
				// materialIssueDetail.getMaterialIssueBatchMappers().get(0).getUomType());
				Integer maxStoreQuantity = GlobalDao.getStoreMaxQuantityByItem(
						issue.getImplantIndent().getStore().getStoreId(), issueDetail.getImplantIndentDetail().getId(),unitId);
				Long maxOrderQuantity = maxStoreQuantity - (storeStock + stockInTransit);
				Long maxStoreQty = Util.ConvertQuantity(maxOrderQuantity,
						issueDetail.getIssueDetailBatchMapper().get(0).getBatch().getBatchUomList(),
						issueDetail.getIssueDetailBatchMapper().get(0).getUomType());
				Integer pendingQunatity = issueDetail.getImplantIndentDetail().getIndentQuantity()
						- issueDetail.getIssueDetailBatchMapper().get(0).getIssueQuantity();

				for (IssueDetailBatchMapper issueDetailBatchMapper : issueDetail.getIssueDetailBatchMapper()) {

					Batch batchObj = issueDetailBatchMapper.getBatch();

					ImplantIndentItemResponse itemResponse = new ImplantIndentItemResponse(issueDetail.getId(),
							issueDetail.getImplantIndentDetail().getItem().getId(),
							issueDetail.getImplantIndentDetail().getItem().getCode(),
							issueDetail.getImplantIndentDetail().getItem().getName(),
							issueDetail.getImplantIndentDetail().getItem().getGeneric() != null
									? issueDetail.getImplantIndentDetail().getItem().getGeneric().getName()
									: null,
							issueDetail.getImplantIndentDetail().getUomType() != null  ? issueDetail.getImplantIndentDetail().getUomType().getType() : null,
							issueDetail.getImplantIndentDetail().getUomUnit() != null ? issueDetail.getImplantIndentDetail().getUomUnit().getUnits() : null,
							pendingQunatity,
							issueDetail.getImplantIndentDetail().getUomType() != null ? issueDetail.getImplantIndentDetail().getUomType().getId() :null,
							issueDetail.getImplantIndentDetail().getUomUnit() != null ? issueDetail.getImplantIndentDetail().getUomUnit().getId() : null,
							availableStock, batchObj.getId(),
							batchObj.getBatchNo(), batchObj.getExpiryDate(),
							issueDetail.getImplantIndentDetail().getIndentQuantity(),
							issueDetail.getIssueDetailBatchMapper().get(0).getIssueQuantity(), maxOrderQuantity,
							issueDetailBatchMapper.getId(), issueDetailBatchMapper.getAcceptedQuantity(),
							issueDetailBatchMapper.getRejectedQuantity());

					response.addItems(itemResponse);
				}
			}
		}
		return response;
	}

	@Override
	@Transactional
	public Object updateImplantIndentAcceptance(ImplantIndentRequest request) {

		screen = GlobalDao.getScreen(screenId);

		Issue issue = ImplantIndentAcceptanceDao.getIssue(request.getId());

		for (IssueDetail detail : issue.getIssueDetail()) {

			for (IssueDetailBatchMapper batchMapper : detail.getIssueDetailBatchMapper()) {

				for (ImplantIndentRequestItems batches : request.getItems()) {

					if (batches.getBatchMapperId() != null && batches.getBatchMapperId().equals(batchMapper.getId())) {

						batchMapper.updateIssueDetailBatchMapper(batches.getBatchMapperId(),
								batches.getAcceptedQuantity(), batches.getRejectNote(),
								(batches.getRejectQuantity() != null ? batches.getRejectQuantity() : 0),
								new RejectReason(batches.getRejectReasonId()));

						updateStoreStock(issue, batchMapper.getBatch(), batches.getAcceptedQuantity(),
								batchMapper.getUomType(), request, detail);

						ImplantIndent implantIndent = issue.getImplantIndent();
						implantIndent.setStatus(new Status(request.getStatusId()));

					}
				}
			}
		}
		issue.setStatus(new Status(8));
		issue.setUpdatedByUser(request.getUserId());
		issue.setUpdatedDate(TimeZoneComponent.getDateByzone(issue.getUnitId()));

		/*Integer storeId = request.getStoreId();
	
		Store storeNew = GlobalDao.getStore(storeId);

		Unit unit = GlobalDao.getUnit(sessionService.getUnitId());*/

		String returnNumber = GlobalService.getBillNoByGeneratedCode(request.getStoreId(), request.getUnitId(), returnScreenId);

		Boolean flag = false;

		PatientIssueReturn issueReturn = new PatientIssueReturn(returnNumber,new PatientRegistration(issue.getAdmission().getPatient().getPatientId()),
				new VisitType(issue.getVisitType().getVisitTypeId()),new Admission(issue.getAdmission().getAdmissionId()),
				new Store(issue.getStore().getStoreId()),new Status(2));
//		sessionService.setEntityDefaults(issueReturn);
		issueReturn.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
				request.getUserId());
		
		for (ImplantIndentRequestItems item : request.getItems()) {
			
				PatientIssueReturnDetail patientIssueReturnDetail=	new PatientIssueReturnDetail(new IssueDetailBatchMapper(item.getBatchMapperId()),
						item.getRejectQuantity(), new ReasonMaster((item.getRejectReasonId())),new Status(com.param.common.constants.Status.Pending.getStatus()));
				issueReturn.addPatientIssueReturnDetail(patientIssueReturnDetail);
//				sessionService.setEntityDefaults(patientIssueReturnDetail);
				patientIssueReturnDetail.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
						request.getUserId());
		}
		
			ImplantIndentAcceptanceDao.savePatientIssueReturn(issueReturn);
		

		CommonResponse response = new CommonResponse();
		response.setId(issue.getId().longValue());
		response.setMessage("Implant Indent Acceptance Updated successfully");
		return response;
	}

	private void updateStoreStock(Issue issue, Batch batch, Integer acceptedQuantity, UomType uomType, ImplantIndentRequest request, IssueDetail d) {

		Integer avaialableStoreStock = GlobalDao.getStoreStockByBatch(issue.getStore().getStoreId(), batch.getId(),0);//0 itemId

		Integer leaseQuantity = Util.GetLeaseQuantity(acceptedQuantity, batch.getBatchUomList(), uomType);

		Integer balanceQuantity = avaialableStoreStock - leaseQuantity;

		batch.getStoreStockList().get(0).setAvailableQty(balanceQuantity);

		// Add stock transaction
		StockTransaction stockTransaction=	new StockTransaction(balanceQuantity, screen, screen.getCode(), leaseQuantity,
				batch.getStoreStockList().get(0).getStore(), d.getId());
//		sessionService.setEntityDefaults(stockTransaction);	
		stockTransaction.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
				request.getUserId());
		batch.addStockTransaction(stockTransaction);

	}

}
