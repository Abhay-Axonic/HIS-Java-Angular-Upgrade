package com.param.pharmacy.ip.service.rejectedRejectionConsumable;

import java.util.List;

import com.param.global.dto.global.CommonDropdown;
import com.param.pharmacy.ip.dto.pendingIssues.acceptance.PendingIssuesAcceptanceSave;
import com.param.pharmacy.ip.dto.rejectedRejection.RejectedRejectionDetails;
import com.param.pharmacy.ip.dto.rejectedRejectionConsumable.RejectedRejectionConsumableSearch;

public interface IRejectedRejectionConsumableService {
	
	CommonDropdown getDropdownList(Integer unitId);
	
	List<RejectedRejectionDetails> rejectedRejectionConsumableSearch(RejectedRejectionConsumableSearch rejectedRejectionSearch);

	RejectedRejectionDetails rejectedRejectionMedicationDetails(Integer id, Integer unitId);
	
	Object rejectedRejectionMedicationSave(PendingIssuesAcceptanceSave rejectedRejectionSave);
}