package com.param.procurement.consignment.dao.ImplantReturnAcceptance;

import java.util.List;

import com.param.entity.model.pharmacy.PatientIssueReturn;
import com.param.entity.model.pharmacy.Reject;
import com.param.entity.model.procurement.ConsignmentPurchaseOrder;
import com.param.procurement.consignment.dto.implantReturnAcceptance.ImplantReturnAcceptanceRequest;


public interface IImplantReturnAcceptanceDao
{
	List<PatientIssueReturn> consignmentReturnAcceptanceSearch(ImplantReturnAcceptanceRequest returnAcceptanceRequest, Integer screenId);
	
	List<PatientIssueReturn> implantReturnAcceptanceSearch(ImplantReturnAcceptanceRequest returnAcceptanceRequest, Integer screenId);

	PatientIssueReturn updateImplantReturnAcceptance(PatientIssueReturn patientIssueReturn);
	
	Reject saveRejectDetails(Reject reject);
	
	 ConsignmentPurchaseOrder saveConsignmentPOCreation(ConsignmentPurchaseOrder consignmentPurchaseOrder);
}
