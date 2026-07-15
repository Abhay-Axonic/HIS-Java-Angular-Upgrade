package com.param.procurement.consignment.dao.ConsignmentPOApproval;

import java.util.List;

import com.param.entity.model.procurement.ConsignmentPurchaseOrder;
import com.param.procurement.consignment.dto.ConsignmentPOCreation.ConsignmentPOCreationRequest;

public interface IConsignmentPOApprovalDao {
	
	 List<ConsignmentPurchaseOrder> searchConsignmentPurchaseOrderList(ConsignmentPOCreationRequest consignmentPOCreationRequest,Integer screenId);
	 
	 ConsignmentPurchaseOrder getConsignmentPOCreationDetails(Integer id, Integer unitId);
	 
	 ConsignmentPurchaseOrder updateConsignmentPO(ConsignmentPurchaseOrder consignmentPurchaseOrder);


}
