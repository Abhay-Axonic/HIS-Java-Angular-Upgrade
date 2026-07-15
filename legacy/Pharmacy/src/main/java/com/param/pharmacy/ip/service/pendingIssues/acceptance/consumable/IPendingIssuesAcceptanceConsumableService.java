package com.param.pharmacy.ip.service.pendingIssues.acceptance.consumable;

import java.util.List;

import com.param.pharmacy.ip.dto.pendingIssues.acceptance.IssueAcceptanceDropdown;
import com.param.pharmacy.ip.dto.pendingIssues.acceptance.PendingIssuesAcceptanceConsumableSearch;
import com.param.pharmacy.ip.dto.pendingIssues.acceptance.PendingIssuesAcceptanceDetails;
import com.param.pharmacy.ip.dto.pendingIssues.acceptance.PendingIssuesAcceptanceSave;
import com.param.pharmacy.ip.dto.pendingIssues.acceptance.PendingIssuesAcceptanceSearchResult;

public interface IPendingIssuesAcceptanceConsumableService {
	
	IssueAcceptanceDropdown getDropdownList(Integer unitId);
	
	List<PendingIssuesAcceptanceSearchResult> pendingIssuesAcceptanceConsumableSearch(PendingIssuesAcceptanceConsumableSearch pendingIssuesAcceptanceSearch);

	PendingIssuesAcceptanceDetails pendingIssuesAcceptanceConsumableDetails(Integer id);
	
	Object pendingIssuesAcceptanceConsumableSave(PendingIssuesAcceptanceSave pendingIssuesAcceptanceSave);
}