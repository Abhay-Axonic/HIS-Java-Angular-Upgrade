package com.param.pharmacy.narcotics.issueWorklist.service;

import java.util.List;

import com.param.common.dto.CommonResponse;
import com.param.global.pharmacy.dto.PendingIssuesSave;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyValidationDetails;
import com.param.pharmacy.narcotics.issueWorklist.dto.NarcoticsDropdown;
import com.param.pharmacy.narcotics.issueWorklist.dto.NarcoticsRequest;
import com.param.pharmacy.narcotics.issueWorklist.dto.NarcoticsResponse;

public interface INarcoticsIssueWorklistService {
	
	NarcoticsDropdown getDropdownList(Integer unitId);
	
	List<NarcoticsResponse> narcoticsIssueWorklistSearch(NarcoticsRequest narcoticsRequest);
	
	PharmacologyValidationDetails narcoticsDetails(Integer id, Integer unitId, Integer storeId);
	
	//CommonResponse narcoticsSave(PendingIssuesSave request) throws Exception;

	List<NarcoticsResponse> narcoticsIssueWorklistSearchPT(NarcoticsRequest narcoticsRequest);

	//PharmacologyValidationDetails narcoticsDetailsPT(Integer id);

}