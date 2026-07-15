package com.param.procurement.consignment.service.ConsignmentPOCreation;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.Tax;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.UomUnit;
import com.param.entity.model.master.User;
import com.param.entity.model.master.Vendor;
import com.param.entity.model.patient.PatientRegistration;
import com.param.entity.model.pharmacy.Issue;
import com.param.entity.model.procurement.ConsignmentPurchaseOrder;
import com.param.entity.model.procurement.ConsignmentPurchaseOrderDetail;
import com.param.entity.model.procurement.ConsignmentPurchaseOrderPatient;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.service.global.IGlobalService;
import com.param.procurement.consignment.dao.ConsignmentPOCreation.IConsignmentPOCreationDao;
import com.param.procurement.consignment.dto.ConsignmentPOCreation.ConsignmentPOCreationDetailRequest;
import com.param.procurement.consignment.dto.ConsignmentPOCreation.ConsignmentPOCreationDetailResponse;
import com.param.procurement.consignment.dto.ConsignmentPOCreation.ConsignmentPOCreationPatientListRequest;
import com.param.procurement.consignment.dto.ConsignmentPOCreation.ConsignmentPOCreationRequest;
import com.param.procurement.consignment.dto.ConsignmentPOCreation.ConsignmentPOCreationResponse;
import com.param.procurement.consignment.dto.ConsignmentPOCreation.PatientWisePOIssueList;
import com.param.procurement.purchase.purchaseOrder.dto.PurchaseOrderDropDownDto;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service
public class ConsignmentPOCreationService implements IConsignmentPOCreationService{
	
	
	private Integer screenId = Screen.ConsignmentPurchaseOrder.getScreen();
	
	@Autowired
	IConsignmentPOCreationDao consignmentPOCreationDao;
	
	@Autowired
	private SessionService sessionService;

	
	@Autowired
	private IGlobalDao GlobalDao;

	@Autowired
	IGlobalService GlobalService;
	
	
	@Override
	@Transactional
	public PurchaseOrderDropDownDto getDropdowns(Integer unitId, Integer orgId, Integer userId) {
		PurchaseOrderDropDownDto purchaseOrdDropDownDto = new PurchaseOrderDropDownDto();

		for (Store store : GlobalDao.getUserStoreList(unitId,orgId,userId)) {
			purchaseOrdDropDownDto
					.addRequestingStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		}

		for (ScreenStatus screenStatus : GlobalDao.getScreenStatus(screenId)) {
			purchaseOrdDropDownDto.addStatus(new com.param.global.dto.global.Status(screenStatus.getStatus().getId(),
					screenStatus.getStatus().getStatus()));
		}

		
		return purchaseOrdDropDownDto;
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
	public Object getConsignmentgetissueListForSavePO(ConsignmentPOCreationRequest consignmentPOCreationRequest) {
		consignmentPOCreationRequest.setToDate((new DateTime(consignmentPOCreationRequest.getToDate())).plusDays(1).toDate());

		Double totalAmount= 0.0;
		Double taxAmount=0.0;
		Double netAmount= 0.0;
		
		List<ConsignmentPOCreationDetailResponse> purchaseOrderSearchDtoList = new ArrayList<ConsignmentPOCreationDetailResponse>();

		for (ConsignmentPOCreationDetailResponse issue : consignmentPOCreationDao.getConsignmentgetissueListFor(consignmentPOCreationRequest, screenId)) {

			
			Double detailTotalAmount= issue.getMrp() * issue.getQuantity();
			Double detailTaxAmount=totalAmount * (issue.getTaxPercentage() / 100);
			Double detailNetAmount= totalAmount + taxAmount;
						
			List<PatientWisePOIssueList> issueList=new ArrayList<PatientWisePOIssueList>();
			for (PatientWisePOIssueList consignmentPOCreationResponse : consignmentPOCreationDao.getConsignmentgetissueListForSavePO(
					consignmentPOCreationRequest,
					issue.getItemId(),
					consignmentPOCreationRequest.getVendorId(),
					issue.getBatchId())) {
				
				Double issueTotalAmount= consignmentPOCreationResponse.getIssueMrp() * consignmentPOCreationResponse.getIssueQty();
				Double issueTaxAmount=totalAmount * (consignmentPOCreationResponse.getIssueTaxPercentage() / 100);
				Double issueNetAmount= totalAmount + taxAmount;
				
				PatientWisePOIssueList patientWisePOIssueList=new PatientWisePOIssueList(
						consignmentPOCreationResponse.getIssueNo(),consignmentPOCreationResponse.getIssueDate(),
						consignmentPOCreationResponse.getVisitTypeId(),consignmentPOCreationResponse.getVisitType(),consignmentPOCreationResponse.getVisitNo(),
						consignmentPOCreationResponse.getPatientDetails(),consignmentPOCreationResponse.getIssueItemCode(),
						consignmentPOCreationResponse.getIssueItemName(),consignmentPOCreationResponse.getBatchId(),consignmentPOCreationResponse.getBatchNo(),
						consignmentPOCreationResponse.getIssueExpiry(),consignmentPOCreationResponse.getIssueQty(),consignmentPOCreationResponse.getIssueCop(),
						consignmentPOCreationResponse.getIssueMrp(),consignmentPOCreationResponse.getIssueTaxCode(),consignmentPOCreationResponse.getIssueTaxPercentage(),
						issueNetAmount,consignmentPOCreationResponse.getPatientId(),consignmentPOCreationResponse.getIssueId());
				issueList.add(patientWisePOIssueList);
				
			}
			ConsignmentPOCreationDetailResponse purchaseReqSearchDto = new ConsignmentPOCreationDetailResponse(issue.getStoreId(),
					issue.getStore(),issue.getItemId(),issue.getItemCode(),	issue.getItemName(),issue.getExpiry(),
					issue.getQuantity(),issue.getBatchId(),issue.getBatchNo(),issue.getCop(),issue.getTaxCode(),
					issue.getTaxName(),issue.getTaxPercentage(),issue.getMrp(),
					detailTotalAmount,detailTaxAmount,detailNetAmount,issueList,issue.getTaxId(),
					issue.getUomTypeId(),issue.getUomUnitId());
			
			
			purchaseOrderSearchDtoList.add(purchaseReqSearchDto);

		}
		
		ConsignmentPOCreationResponse consignmentPOCreationResponse=new ConsignmentPOCreationResponse(totalAmount,taxAmount,netAmount,purchaseOrderSearchDtoList,
				consignmentPOCreationRequest.getStoreId(),consignmentPOCreationRequest.getVendorId());
		return consignmentPOCreationResponse;
	}
	
	@Override
	@Transactional
	public Object saveConsignmentPOCreation(ConsignmentPOCreationRequest consignmentPOCreationRequest)
  	{	
		
		/*Store storeNew = GlobalDao.getStore(consignmentPOCreationRequest.getStoreId());
		
		Unit unit = GlobalDao.getUnit(sessionService.getUnitId());*/

		String consignmentPONumber = GlobalService.getBillNoByGeneratedCode(consignmentPOCreationRequest.getStoreId(), consignmentPOCreationRequest.getUnitId(), screenId);
		
		
		  ConsignmentPurchaseOrder consignmentPurchaseOrder=new ConsignmentPurchaseOrder( consignmentPOCreationRequest.getVendorId() != null
				  	? new Vendor( consignmentPOCreationRequest.getVendorId()) : null,new Store(consignmentPOCreationRequest.getStoreId()),consignmentPONumber,
				  	0,0.0,consignmentPOCreationRequest.getTotalAmount(),consignmentPOCreationRequest.getTotalTaxAmount(),
				  	consignmentPOCreationRequest.getNetAmount(),0.0,true,new Status(consignmentPOCreationRequest.getStatus()), true,
				  	new Status(consignmentPOCreationRequest.getApprovalStatusId()));
		  
		  
		  for (ConsignmentPOCreationDetailRequest consignmentPOCreationDetailRequest : consignmentPOCreationRequest.getItems())
			{
			  
			
		  ConsignmentPurchaseOrderDetail consignmentPurchaseOrderDetail= new  ConsignmentPurchaseOrderDetail( new Item(consignmentPOCreationDetailRequest.getItemId()),
				  consignmentPOCreationDetailRequest.getPoQuantity(), consignmentPOCreationDetailRequest.getCop(),
				  consignmentPOCreationDetailRequest.getMrp() , new Tax(consignmentPOCreationDetailRequest.getTaxId()),
				  consignmentPOCreationDetailRequest.getTaxPercentage(), consignmentPOCreationDetailRequest.getTotalAmount(),0.0,
				  consignmentPOCreationDetailRequest.getTaxAmount(),consignmentPOCreationDetailRequest.getNetAmount(),
				  consignmentPOCreationDetailRequest.getUomTypeId() != null ? new UomType(consignmentPOCreationDetailRequest.getUomTypeId()) : null ,
						  consignmentPOCreationDetailRequest.getUomUnitId() != null ? new
				  UomUnit(consignmentPOCreationDetailRequest.getUomUnitId()) : null ,new Batch(consignmentPOCreationDetailRequest.getBatchId()));
		  
//		  sessionService.setEntityDefaults(consignmentPurchaseOrderDetail);
		  consignmentPurchaseOrderDetail.setCreateDefaults(consignmentPOCreationRequest.getOrganizationId(),consignmentPOCreationRequest.getUnitId(),
				  consignmentPOCreationRequest.getUserId());
		  consignmentPurchaseOrder.addConsignmentPurchaseOrderDetail(consignmentPurchaseOrderDetail);
		  for (ConsignmentPOCreationPatientListRequest consignmentPOCreationPatientListRequest : consignmentPOCreationDetailRequest.getIssueList())
			{
			  ConsignmentPurchaseOrderPatient consignmentPurchaseOrderPatient=new ConsignmentPurchaseOrderPatient(
					  new Issue(consignmentPOCreationPatientListRequest.getIssueId()),new PatientRegistration(consignmentPOCreationPatientListRequest.getPatientId())
					  );
		  
			 
				
//			  sessionService.setEntityDefaults(consignmentPurchaseOrderPatient);
			  consignmentPurchaseOrderPatient.setCreateDefaults(consignmentPOCreationRequest.getOrganizationId(),consignmentPOCreationRequest.getUnitId(),
					  consignmentPOCreationRequest.getUserId());
			  consignmentPurchaseOrderDetail.addConsignmentPurchaseOrderPatient(consignmentPurchaseOrderPatient);
		 
			}
		 
			}
		 
//		sessionService.setEntityDefaults(consignmentPurchaseOrder);
		  consignmentPurchaseOrder.setCreateDefaults(consignmentPOCreationRequest.getOrganizationId(),consignmentPOCreationRequest.getUnitId(),
				  consignmentPOCreationRequest.getUserId());
		consignmentPOCreationDao.saveConsignmentOutward(consignmentPurchaseOrder);
		CommonResponse response = new CommonResponse();
		response.setId(consignmentPurchaseOrder.getId().longValue());
		response.setMessage("Consignment PO Created successfully");
		return response;
	
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
					consignmentPOCreationDetailResponse.getConsignmentPurchaseOrderPatient().get(0).getIssue().getIssueDetail().get(0).getIssueDetailBatchMapper().get(0).getUomType() != null ?
					consignmentPOCreationDetailResponse.getConsignmentPurchaseOrderPatient().get(0).getIssue().getIssueDetail().get(0).getIssueDetailBatchMapper().get(0).getUomType().getId() : null,
					consignmentPOCreationDetailResponse.getConsignmentPurchaseOrderPatient().get(0).getIssue().getIssueDetail().get(0).getIssueDetailBatchMapper().get(0).getUnit() != null ?
					consignmentPOCreationDetailResponse.getConsignmentPurchaseOrderPatient().get(0).getIssue().getIssueDetail().get(0).getIssueDetailBatchMapper().get(0).getUnit().getUnitId() : null,
					consignmentPOCreationDetailResponse.getId(),consignmentPOCreationDetailResponse.getRemark());
			
			purchaseOrderSearchDtoList.add(purchaseReqSearchDto);
			
			
			

		}
		
		
		
		ConsignmentPOCreationResponse consignmentPOCreationResponse=new ConsignmentPOCreationResponse(consignmentPurchaseOrder.getId(),consignmentPurchaseOrder.getTotalAmount(),consignmentPurchaseOrder.getTotalTaxAmount(),
				consignmentPurchaseOrder.getNetAmount(),purchaseOrderSearchDtoList,
				consignmentPurchaseOrder.getStore().getStoreId(),consignmentPurchaseOrder.getConsignmentPurchaseOrderDetail().get(0).getBatch().getVendor().getId(),
				consignmentPurchaseOrder.getConsignmentPurchaseOrderDetail().get(0).getBatch().getVendor().getName(),consignmentPurchaseOrder.getRemark(),
				consignmentPurchaseOrder.getAddedDate(),consignmentPurchaseOrder.getPoNumber());

		
		return consignmentPOCreationResponse;
	}
	
	@Override
	@Transactional
	public Object updateConsignmentPOCreationDetails(ConsignmentPOCreationRequest consignmentPOCreationRequest)
  	{	
		
		ConsignmentPurchaseOrder consignmentPurchaseOrder = consignmentPOCreationDao.getConsignmentPOCreationDetails(consignmentPOCreationRequest.getId(),consignmentPOCreationRequest.getUnitId());
		
		if(consignmentPOCreationRequest.getTotalAmount() != null)
		{
		consignmentPurchaseOrder.setTotalAmount(consignmentPOCreationRequest.getTotalAmount());
		}
		if(consignmentPOCreationRequest.getTotalTaxAmount() != null)
		{
		consignmentPurchaseOrder.setTotalTaxAmount(consignmentPOCreationRequest.getTotalTaxAmount());
		}
		if(consignmentPOCreationRequest.getNetAmount() != null)
		{
		consignmentPurchaseOrder.setNetAmount(consignmentPOCreationRequest.getNetAmount());
		}
		if(consignmentPOCreationRequest.getRemark() != null)
		{
		consignmentPurchaseOrder.setRemark(consignmentPOCreationRequest.getRemark());
		}
		

		for (ConsignmentPurchaseOrderDetail detail : consignmentPurchaseOrder.getConsignmentPurchaseOrderDetail()) {
			Integer id = null;

			
				 for (ConsignmentPOCreationDetailRequest items : consignmentPOCreationRequest.getItems())
					{
					 if (items.getId() != null && items.getId().equals(detail.getId()))
						{
						 	if((items.getItemId().equals(detail.getItem().getId())) 
						 			&& (items.getBatchId().equals(detail.getBatch().getId())))
						 	{
						 		if(items.getCop() != null)
								{
						 			detail.setCop(items.getCop());
								}
						 		if(items.getMrp() != null)
								{
						 			detail.setMrp(items.getMrp());
								}
						 		if(items.getTotalAmount() != null)
								{
						 			detail.setTotalAmount(items.getTotalAmount());
								}
						 		if(items.getTaxAmount() != null)
								{
						 			detail.setTaxAmount(items.getTaxAmount());
								}
						 		if(items.getNetAmount() != null)
								{
						 			detail.setNetAmount(items.getNetAmount());
								}
						 		if(items.getRemark() != null)
								{
						 			detail.setRemark(items.getRemark());
								}
						 		consignmentPurchaseOrder.getConsignmentPurchaseOrderDetail().get(0).setUpdatedDate(TimeZoneComponent.getDateByzone(consignmentPurchaseOrder.getUnitId()));
						 		consignmentPurchaseOrder.getConsignmentPurchaseOrderDetail().get(0).setUpdatedByUser(consignmentPOCreationRequest.getUserId());
						 		
						 	}
						 
						}
					}
			}
		 
		
		 	consignmentPurchaseOrder.setUpdatedDate(TimeZoneComponent.getDateByzone(consignmentPurchaseOrder.getUnitId()));
		 	consignmentPurchaseOrder.setUpdatedByUser(consignmentPOCreationRequest.getUserId());
		 	ConsignmentPurchaseOrder issueResponse = consignmentPOCreationDao.updateConsignmentPO(consignmentPurchaseOrder);
			CommonResponse response = new CommonResponse();
			response.setId(issueResponse.getId().longValue());
			response.setMessage("Consignment PO Update successfully");
		return response;
	
  	}
	
	@Override
	@Transactional
	public Object approveConsignmentPurchaseOrder(ConsignmentPOCreationRequest consignmentPOCreationRequest) {

		for (Integer id : consignmentPOCreationRequest.getPoIdList()) {
			ConsignmentPurchaseOrder consignmentPurchaseOrder = consignmentPOCreationDao.getConsignmentPOCreationDetails(id,consignmentPOCreationRequest.getUnitId());

			consignmentPurchaseOrder.setStatus(new Status(consignmentPOCreationRequest.getStatusId()));
			consignmentPurchaseOrder.setApproveStatusId(new Status(consignmentPOCreationRequest.getApprovalStatusId()));

			for (ConsignmentPurchaseOrderDetail detail : consignmentPurchaseOrder.getConsignmentPurchaseOrderDetail()) {
				detail.setStatus(new Status(consignmentPOCreationRequest.getStatusId()));

				detail.setUpdatedDate(TimeZoneComponent.getDateByzone(consignmentPurchaseOrder.getUnitId()));
				detail.setUpdatedByUser(consignmentPOCreationRequest.getUserId());
			}

			consignmentPurchaseOrder.setApprovedDate(TimeZoneComponent.getDateByzone(consignmentPurchaseOrder.getUnitId()));
			consignmentPurchaseOrder.setApprovedBy(new User(consignmentPOCreationRequest.getUserId()));

			consignmentPurchaseOrder.setUpdatedDate(TimeZoneComponent.getDateByzone(consignmentPurchaseOrder.getUnitId()));
			consignmentPurchaseOrder.setUpdatedByUser(consignmentPOCreationRequest.getUserId());

			consignmentPOCreationDao.updateConsignmentPO(consignmentPurchaseOrder);
		}

		CommonResponse response = new CommonResponse();
		response.setMessage("Consignment Purchase Order approved successfully");

		return response;
	}
	
	@Override
	@Transactional
	public Object cancelConsignemntPurchaseOrder(ConsignmentPOCreationRequest consignmentPOCreationRequest) {

		ConsignmentPurchaseOrder consignmentPurchaseOrder = consignmentPOCreationDao.getConsignmentPOCreationDetails(consignmentPOCreationRequest.getId(),consignmentPOCreationRequest.getUnitId());

		for (ConsignmentPurchaseOrderDetail detail : consignmentPurchaseOrder.getConsignmentPurchaseOrderDetail()) {
			detail.setStatus(new Status(consignmentPOCreationRequest.getStatusId()));

			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(consignmentPurchaseOrder.getUnitId()));
			detail.setUpdatedByUser(consignmentPOCreationRequest.getUserId());
		}

		if(consignmentPOCreationRequest.getCancelReasonId() != null)
		{
			consignmentPurchaseOrder.setCancelReason(new CancelReason(consignmentPOCreationRequest.getCancelReasonId()));	
		}
		if(consignmentPOCreationRequest.getCancelNote() != null)
		{
			consignmentPurchaseOrder.setCancelNote(consignmentPOCreationRequest.getCancelNote());
		}
		

		consignmentPurchaseOrder.setStatus(new Status(consignmentPOCreationRequest.getStatusId()));
		consignmentPurchaseOrder.setApproveStatusId(new Status(consignmentPOCreationRequest.getApprovalStatusId()));

		consignmentPurchaseOrder.setUpdatedDate(TimeZoneComponent.getDateByzone(consignmentPurchaseOrder.getUnitId()));
		consignmentPurchaseOrder.setUpdatedByUser(consignmentPOCreationRequest.getUserId());

		consignmentPOCreationDao.updateConsignmentPO(consignmentPurchaseOrder);

		CommonResponse response = new CommonResponse();
		response.setMessage("Consignment Purchase Order cancelled successfully");

		return response;
	}

}
