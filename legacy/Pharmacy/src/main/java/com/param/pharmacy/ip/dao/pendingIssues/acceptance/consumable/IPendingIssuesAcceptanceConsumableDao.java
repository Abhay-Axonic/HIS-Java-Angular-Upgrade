package com.param.pharmacy.ip.dao.pendingIssues.acceptance.consumable;

import java.util.List;

import com.param.entity.model.pharmacy.Issue;
import com.param.pharmacy.ip.dto.pendingIssues.acceptance.PendingIssuesAcceptanceConsumableSearch;

public interface IPendingIssuesAcceptanceConsumableDao {
	
	List<Issue> pendingIssuesAcceptanceConsumableSearch(PendingIssuesAcceptanceConsumableSearch pendingIssuesAcceptanceSearch, Integer screenId);
	
}