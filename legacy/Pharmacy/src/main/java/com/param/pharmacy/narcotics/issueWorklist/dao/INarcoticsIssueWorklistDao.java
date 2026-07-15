package com.param.pharmacy.narcotics.issueWorklist.dao;

import java.util.List;

import com.param.entity.model.pharmacy.Issue;
import com.param.entity.model.pharmacy.IssueDetail;
import com.param.pharmacy.narcotics.issueWorklist.dto.NarcoticsRequest;
import com.param.pharmacy.narcotics.issueWorklist.dto.NarcoticsResponse;

public interface INarcoticsIssueWorklistDao {
	
	List<Issue> narcoticsIssueWorklistSearch(NarcoticsRequest narcoticsRequest, Integer screenId);
	
	Issue narcoticsIssueWorklistById(Integer id);
	
	List<IssueDetail> narcoticsIssueWorklistDetails(Integer id);

	Long getIssuedQuantity(Long id);

	List<NarcoticsResponse> narcoticsIssueWorklistSearchPT(NarcoticsRequest narcoticsRequest, Integer screenId, Integer unitId);

	List<IssueDetail> addeIssueItems(Integer id);
}