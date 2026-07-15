package com.param.pharmacy.nursing.narcoticsStore.service;

import java.util.List;

import com.param.common.dto.CommonResponse;
import com.param.global.dto.global.CommonDropdown;
import com.param.global.dto.global.CommonSearchRequest;
import com.param.pharmacy.ip.dto.pendingIssues.acceptance.PendingIssuesAcceptanceSearchResult;
import com.param.pharmacy.nursing.narcoticsStore.dto.EmptyAmpoulesResponse;
import com.param.pharmacy.nursing.narcoticsStore.dto.EmptyAmpoulesSave;

public interface IReturnEmptyAmpoulesService {
	
	CommonDropdown getDropdownList(Integer unitId);
	
	List<PendingIssuesAcceptanceSearchResult> returnEmptyAmpoulesSearch(CommonSearchRequest request);

	EmptyAmpoulesResponse returnEmptyAmpoulesDetails(Integer id);
	
	CommonResponse returnEmptyAmpoulesSave(EmptyAmpoulesSave request);
}