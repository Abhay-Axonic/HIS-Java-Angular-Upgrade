package com.param.pharmacy.ip.service.rejectedRejection;

import java.util.List;

import com.param.global.billing.controller.BillingException;
import com.param.global.dto.global.CommonDropdown;
import com.param.pharmacy.ip.dto.pendingIssues.acceptance.PendingIssuesAcceptanceSave;
import com.param.pharmacy.ip.dto.rejectedRejection.RejectedRejectionDetails;
import com.param.pharmacy.ip.dto.rejectedRejection.RejectedRejectionSearch;

public interface IRejectedRejectionMedicationService {
	
	CommonDropdown getDropdownList(Integer unitId);
	
	List<RejectedRejectionDetails> rejectedRejectionMedicationSearch(RejectedRejectionSearch rejectedRejectionSearch);

	RejectedRejectionDetails rejectedRejectionMedicationDetails(Integer id, Integer unitId);
	
	Object rejectedRejectionMedicationSave(PendingIssuesAcceptanceSave rejectedRejectionSave) throws BillingException, Exception;
}