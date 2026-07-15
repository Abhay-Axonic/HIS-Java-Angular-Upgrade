package com.param.procurement.consignment.dao.ConsignmentPOCreation;

import java.util.List;

import com.param.entity.model.pharmacy.Issue;
import com.param.entity.model.procurement.ConsignmentPurchaseOrder;
import com.param.procurement.consignment.dto.ConsignmentPOCreation.ConsignmentPOCreationDetailResponse;
import com.param.procurement.consignment.dto.ConsignmentPOCreation.ConsignmentPOCreationRequest;
import com.param.procurement.consignment.dto.ConsignmentPOCreation.PatientWisePOIssueList;
public interface IConsignmentPOCreationDao {
	
	 List<ConsignmentPurchaseOrder> searchConsignmentPurchaseOrderList(ConsignmentPOCreationRequest consignmentPOCreationRequest,Integer screenId);
	
	 List<ConsignmentPOCreationDetailResponse> getConsignmentgetissueListFor(ConsignmentPOCreationRequest consignmentPOCreationRequest,Integer screenId);
	
	List<PatientWisePOIssueList> getConsignmentgetissueListForSavePO(ConsignmentPOCreationRequest PurchaseOrdDto,Integer itemid,Integer vendorId,Long batchId);
	
	ConsignmentPurchaseOrder saveConsignmentOutward(ConsignmentPurchaseOrder consignmentPurchaseOrder);

	 ConsignmentPurchaseOrder getConsignmentPOCreationDetails(Integer id, Integer unitId);
	
	
	
	 ConsignmentPurchaseOrder updateConsignmentPO(ConsignmentPurchaseOrder consignmentPurchaseOrder);


}
