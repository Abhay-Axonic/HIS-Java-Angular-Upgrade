package com.param.pharmacy.nursing.narcoticsStore.dao;

import java.util.List;

import com.param.entity.model.pharmacy.IssueDetail;
import com.param.entity.model.pharmacy.ReturnOfEmptyAmpoules;
import com.param.entity.model.pharmacy.ReturnOfEmptyAmpoulesDetail;
import com.param.global.dto.global.CommonSearchRequest;

public interface IReturnEmptyAmpoulesDao {

	// List<Issue> returnEmptyAmpoulesSearch(CommonSearchRequest request, Integer screenId);

	List<ReturnOfEmptyAmpoules> returnEmptyAmpoulesSearch(CommonSearchRequest request, Integer screenId);

    //  Issue returnEmptyAmpoulesById(Integer id);
	
	ReturnOfEmptyAmpoules returnEmptyAmpoulesById(Integer id);

    //  List<IssueDetail> returnEmptyAmpoulesDetails(Integer issueId);
	
	List<ReturnOfEmptyAmpoulesDetail> returnEmptyAmpoulesDetails(Integer issueId);

	ReturnOfEmptyAmpoules returnEmptyAmpoulesSave(ReturnOfEmptyAmpoules returnOfEmptyAmpoules);

	IssueDetail returnEmptyAmpoulesDetailById(Integer issueDetailId);

	ReturnOfEmptyAmpoules updateReturnOfEmptyAmpoules(ReturnOfEmptyAmpoules returnOfEmptyAmpoules);

}