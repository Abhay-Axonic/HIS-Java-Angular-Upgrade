package com.param.procurement.consignment.service.ConsignmentPOApproval;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.common.constants.Constants;
import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.master.AssetType;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.RequestType;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.Tax;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.UomUnit;
import com.param.entity.model.master.User;
import com.param.entity.model.master.Vendor;
import com.param.entity.model.procurement.ConsignmentPurchaseOrder;
import com.param.entity.model.procurement.ConsignmentPurchaseOrderDetail;
import com.param.entity.model.procurement.ConsignmentPurchaseOrderPatient;
import com.param.entity.model.procurement.GoodReceiptNote;
import com.param.entity.model.procurement.GoodReceiptNoteDetail;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.service.global.IGlobalService;
import com.param.procurement.consignment.dao.ConsignmentPOApproval.IConsignmentPOApprovalDao;
import com.param.procurement.consignment.dto.ConsignmentPOApproval.ConsignmentPOApprovalDropdown;
import com.param.procurement.consignment.dto.ConsignmentPOCreation.ConsignmentPOCreationDetailResponse;
import com.param.procurement.consignment.dto.ConsignmentPOCreation.ConsignmentPOCreationRequest;
import com.param.procurement.consignment.dto.ConsignmentPOCreation.ConsignmentPOCreationResponse;
import com.param.procurement.consignment.dto.ConsignmentPOCreation.PatientWisePOIssueList;
import com.param.procurement.purchase.dao.goodReceiptNote.IGoodReceiptNoteDao;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service
public class ConsignmentPOApprovalService implements IConsignmentPOApprovalService{
	
	private Integer screenId = Screen.ConsignmentPurchaseOrderApproval.getScreen();
	
	private Integer grnScreenId = Screen.GoodReceiptNote.getScreen();
	
	@Autowired
	IConsignmentPOApprovalDao consignmentPOCreationDao;
	
	@Autowired
	IGlobalDao GlobalDao;
	
	@Autowired
	IGlobalService GlobalService;
	
	@Autowired
	private SessionService sessionService;
	
	@Autowired
	IGoodReceiptNoteDao grnDao;
	
	@Override
	@Transactional
	public ConsignmentPOApprovalDropdown getDropdowns(Integer unitId, Integer orgId, Integer userId) {
		ConsignmentPOApprovalDropdown consignmentPOApprovalDropdown = new ConsignmentPOApprovalDropdown();

		for (Store store : GlobalDao.getUserStoreList(unitId,orgId,userId)) {
			consignmentPOApprovalDropdown
					.addStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		}

		for (ScreenStatus screenStatus : GlobalDao.getScreenStatus(screenId)) {
			consignmentPOApprovalDropdown.addStatus(new com.param.global.dto.global.Status(screenStatus.getStatus().getId(),
					screenStatus.getStatus().getStatus()));
		}

		
		return consignmentPOApprovalDropdown;
	}
	
	
	@Override
	@Transactional
	public Object getConsignmentPuchaseOrderList(ConsignmentPOCreationRequest consignmentPOCreationRequest) {
		consignmentPOCreationRequest.setToDate((new DateTime(consignmentPOCreationRequest.getToDate())).plusDays(1).toDate());

		List<ConsignmentPOCreationResponse> purchaseOrderSearchDtoList = new ArrayList<ConsignmentPOCreationResponse>();

		for (ConsignmentPurchaseOrder purchaseOrder : consignmentPOCreationDao.searchConsignmentPurchaseOrderList(consignmentPOCreationRequest, screenId)) {

			ConsignmentPOCreationResponse purchaseReqSearchDto = new ConsignmentPOCreationResponse(purchaseOrder.getId(),
					purchaseOrder.getAddedDate(), purchaseOrder.getPoNumber(), purchaseOrder.getStore().getStoreName(),purchaseOrder.getStore().getStoreId(),
					
					purchaseOrder.getApproveStatusId().getStatus(), purchaseOrder.getStatus().getStatus(),
					purchaseOrder.getAddedBy().getUserName(), purchaseOrder.getRemark(),
					purchaseOrder.getVendor().getName(), purchaseOrder.getAddedDate());

			purchaseOrderSearchDtoList.add(purchaseReqSearchDto);

		}
		return purchaseOrderSearchDtoList;
	}
	
	
	@Override
	@Transactional
	public ConsignmentPOCreationResponse getConsignmentPOCreationDetails(Integer id,Integer unitId) {

		ConsignmentPurchaseOrder consignmentPurchaseOrder = consignmentPOCreationDao.getConsignmentPOCreationDetails(id,unitId);
		
		
		List<ConsignmentPOCreationDetailResponse> purchaseOrderSearchDtoList = new ArrayList<ConsignmentPOCreationDetailResponse>();
		

		for (ConsignmentPurchaseOrderDetail consignmentPOCreationDetailResponse : consignmentPurchaseOrder.getConsignmentPurchaseOrderDetail()) {
			
			
			
			List<PatientWisePOIssueList> issueList=new ArrayList<PatientWisePOIssueList>();
			for (ConsignmentPurchaseOrderPatient consignmentPOCreationResponse : consignmentPOCreationDetailResponse.getConsignmentPurchaseOrderPatient() )
			{
				PatientWisePOIssueList patientWisePOIssueList=new PatientWisePOIssueList(
						consignmentPOCreationResponse.getIssue().getIssueNo(),consignmentPOCreationResponse.getIssue().getAddedDate(),
						consignmentPOCreationResponse.getIssue().getVisitType().getVisitTypeId(),consignmentPOCreationResponse.getIssue().getVisitType().getVisitTypeName(),consignmentPOCreationResponse.getIssue().getVisitType().getVisitTypeCode(),
						consignmentPOCreationResponse.getPatient().getPrefix().getPrefixCode() + " "+consignmentPOCreationResponse.getPatient().getFirstName() + " "+
								consignmentPOCreationResponse.getPatient().getMiddleName() + " "+ consignmentPOCreationResponse.getPatient().getLastName() ,
						
								consignmentPOCreationDetailResponse.getItem().getCode(),
								consignmentPOCreationDetailResponse.getItem().getName(),consignmentPOCreationDetailResponse.getBatch().getId(),consignmentPOCreationDetailResponse.getBatch().getBatchNo(),
								consignmentPOCreationDetailResponse.getBatch().getExpiryDate(),consignmentPOCreationDetailResponse.getPoQuantity(),consignmentPOCreationDetailResponse.getCop(),
								consignmentPOCreationDetailResponse.getMrp(),consignmentPOCreationDetailResponse.getTax().getCode(),consignmentPOCreationDetailResponse.getTaxPercentage(),
								consignmentPOCreationDetailResponse.getNetAmount(),consignmentPOCreationResponse.getPatient().getPatientId(),consignmentPOCreationResponse.getIssue().getId());
				issueList.add(patientWisePOIssueList);
			}
			ConsignmentPOCreationDetailResponse purchaseReqSearchDto = new ConsignmentPOCreationDetailResponse(consignmentPurchaseOrder.getStore().getStoreId(),
					consignmentPurchaseOrder.getStore().getStoreName(),consignmentPOCreationDetailResponse.getItem().getId(),
					consignmentPOCreationDetailResponse.getItem().getCode(),	consignmentPOCreationDetailResponse.getItem().getName(),
					consignmentPOCreationDetailResponse.getBatch().getExpiryDate(),
					consignmentPOCreationDetailResponse.getPoQuantity(),consignmentPOCreationDetailResponse.getBatch().getId(),
					consignmentPOCreationDetailResponse.getBatch().getBatchNo(),consignmentPOCreationDetailResponse.getCop(),
					consignmentPOCreationDetailResponse.getTax().getCode(),consignmentPOCreationDetailResponse.getTax().getName(),
					consignmentPOCreationDetailResponse.getTaxPercentage(),consignmentPOCreationDetailResponse.getMrp(),
					consignmentPOCreationDetailResponse.getTotalAmount(),consignmentPOCreationDetailResponse.getTaxAmount(),
					consignmentPOCreationDetailResponse.getNetAmount(),issueList,consignmentPOCreationDetailResponse.getTax().getId(),
					consignmentPOCreationDetailResponse.getConsignmentPurchaseOrderPatient().getFirst().getIssue().getIssueDetail().getFirst().getIssueDetailBatchMapper().getFirst().getUomType() != null ?
					consignmentPOCreationDetailResponse.getConsignmentPurchaseOrderPatient().getFirst().getIssue().getIssueDetail().getFirst().getIssueDetailBatchMapper().getFirst().getUomType().getId() : null,
					consignmentPOCreationDetailResponse.getConsignmentPurchaseOrderPatient().getFirst().getIssue().getIssueDetail().getFirst().getIssueDetailBatchMapper().getFirst().getUnit() != null ?
					consignmentPOCreationDetailResponse.getConsignmentPurchaseOrderPatient().getFirst().getIssue().getIssueDetail().getFirst().getIssueDetailBatchMapper().getFirst().getUnit().getUnitId() : null,
					consignmentPOCreationDetailResponse.getId(),consignmentPOCreationDetailResponse.getRemark());
			
			purchaseOrderSearchDtoList.add(purchaseReqSearchDto);
			
			
		}

			ConsignmentPOCreationResponse consignmentPOCreationResponse=new ConsignmentPOCreationResponse(consignmentPurchaseOrder.getId(),consignmentPurchaseOrder.getTotalAmount(),consignmentPurchaseOrder.getTotalTaxAmount(),
					consignmentPurchaseOrder.getNetAmount(),purchaseOrderSearchDtoList,
					consignmentPurchaseOrder.getStore().getStoreId(),consignmentPurchaseOrder.getConsignmentPurchaseOrderDetail().getFirst().getBatch().getVendor().getId(),
					consignmentPurchaseOrder.getConsignmentPurchaseOrderDetail().getFirst().getBatch().getVendor().getName(),consignmentPurchaseOrder.getRemark(),
					consignmentPurchaseOrder.getAddedDate(),consignmentPurchaseOrder.getPoNumber());

			
			return consignmentPOCreationResponse;
			
	
	}
	
	@Override
	@Transactional
	public Object approveConsignmentPurchaseOrder(ConsignmentPOCreationRequest consignmentPOCreationRequest) {

	
		
		
			ConsignmentPurchaseOrder consignmentPurchaseOrder = consignmentPOCreationDao.getConsignmentPOCreationDetails(consignmentPOCreationRequest.getId(),consignmentPOCreationRequest.getUnitId());

			Random rand = new Random();
			int num = rand.nextInt(1000);

			/*  Integer storeId = consignmentPurchaseOrder.getStore().getStoreId();
			  Store storeNew = GlobalDao.getStore(storeId);
			 Unit unit = GlobalDao.getUnit(sessionService.getUnitId());*/

			String grnNo = GlobalService.getBillNoByGeneratedCode(consignmentPurchaseOrder.getStore().getStoreId(), consignmentPOCreationRequest.getUnitId(), grnScreenId);
			
			GoodReceiptNote grn = new GoodReceiptNote(new Status(consignmentPOCreationRequest.getStatusId()),new AssetType(Constants.getConsumableId()),grnNo,
					consignmentPurchaseOrder.getIsConsignment(), consignmentPurchaseOrder.getNetAmount(),
					consignmentPurchaseOrder.getRemark() != null ? consignmentPurchaseOrder.getRemark() : null,
					new RequestType(com.param.common.constants.RequestType.AgainstPurchaseOrder.getRequestType()),
					new Status(consignmentPOCreationRequest.getStatusId()), new Store(consignmentPurchaseOrder.getStore().getStoreId()), 
					consignmentPurchaseOrder.getTotalAmount(),consignmentPurchaseOrder.getTotalTaxAmount(),
					new Vendor(consignmentPurchaseOrder.getConsignmentPurchaseOrderDetail().getFirst().getBatch().getVendor().getId() != null ? 
					consignmentPurchaseOrder.getConsignmentPurchaseOrderDetail().getFirst().getBatch().getVendor().getId() : null));
			grn.setConsignmentPurchaseOrder(new ConsignmentPurchaseOrder(consignmentPurchaseOrder.getId()));
			
			GoodReceiptNoteDetail grnDetail = null;
			for (ConsignmentPurchaseOrderDetail detail : consignmentPurchaseOrder.getConsignmentPurchaseOrderDetail()) {
				
				GoodReceiptNoteDetail goodReceiptNoteDetail=new GoodReceiptNoteDetail(detail.getBatch().getBatchNo(),
						detail.getPoQuantity(),detail.getBatch().getExpiryDate() != null ? detail.getBatch().getExpiryDate() : null, 
						detail.getUomType() != null ? new UomType(detail.getUomType().getId()) : null,
						detail.getUomUnit() != null ? new UomUnit(detail.getUomUnit().getId()) : null,
								detail.getItem() != null ? new Item(detail.getItem().getId()) : null,detail.getMrp(), detail.getNetAmount(),
						detail.getRemark() != null ? detail.getRemark() : null,
						new Status(consignmentPOCreationRequest.getStatusId()), detail.getTaxAmount() != null ? detail.getTaxAmount() : null,
						detail.getTax() != null ? new Tax(detail.getTax().getId()) : null,
						detail.getTaxPercentage()!=null ? detail.getTaxPercentage():null, detail.getTotalAmount(),detail.getCop());
//				sessionService.setEntityDefaults(goodReceiptNoteDetail);
				goodReceiptNoteDetail.setCreateDefaults(consignmentPOCreationRequest.getOrganizationId(),consignmentPOCreationRequest.getUnitId(),
						consignmentPOCreationRequest.getUserId());
				grnDetail = grn.addGoodReceiptNoteDetailList(goodReceiptNoteDetail);
				//grnDetail.setConsignmentPurchaseOrderDetail(new ConsignmentPurchaseOrderDetail(detail.getId()));
				
			}
			
//			sessionService.setEntityDefaults(grn);

			grn.setCreateDefaults(consignmentPOCreationRequest.getOrganizationId(),consignmentPOCreationRequest.getUnitId(),
					consignmentPOCreationRequest.getUserId());
			grnDao.saveGoodReceiptNote(grn);
			
			
			consignmentPurchaseOrder.setStatus(new Status(consignmentPOCreationRequest.getStatusId()));
			consignmentPurchaseOrder.setApproveStatusId(new Status(consignmentPOCreationRequest.getApprovalStatusId()));
			
			consignmentPurchaseOrder.setGoodReceiptNote(new GoodReceiptNote(grn.getId()));
			for (ConsignmentPurchaseOrderDetail detail : consignmentPurchaseOrder.getConsignmentPurchaseOrderDetail()) {
				{
					detail.setStatus(new Status(consignmentPOCreationRequest.getStatusId()));
					detail.setUpdatedDate(TimeZoneComponent.getDateByzone(consignmentPOCreationRequest.getUnitId()));
					detail.setUpdatedByUser(consignmentPOCreationRequest.getUserId());
					for (GoodReceiptNoteDetail goodReceiptNoteDetail : grn.getGoodReceiptNoteDetailsList()) {
						
						if(goodReceiptNoteDetail.getConsignmentPurchaseOrderDetail().getId() != null && (goodReceiptNoteDetail.getConsignmentPurchaseOrderDetail().getId().equals(detail.getId())))
						{
							detail.setGoodReceiptNoteDetail(new GoodReceiptNoteDetail(goodReceiptNoteDetail.getId()));
						}
					}
				
				}

			
			}
	
			consignmentPurchaseOrder.setApprovedDate(TimeZoneComponent.getDateByzone(consignmentPOCreationRequest.getUnitId()));
			consignmentPurchaseOrder.setApprovedBy(new User(consignmentPOCreationRequest.getUserId()));

			consignmentPurchaseOrder.setUpdatedDate(TimeZoneComponent.getDateByzone(consignmentPOCreationRequest.getUnitId()));
			consignmentPurchaseOrder.setUpdatedByUser(consignmentPOCreationRequest.getUserId());

			consignmentPOCreationDao.updateConsignmentPO(consignmentPurchaseOrder);
		

		CommonResponse response = new CommonResponse();
		response.setMessage("Consignment Purchase Order approved successfully");

		return response;
	}
	
	
}
