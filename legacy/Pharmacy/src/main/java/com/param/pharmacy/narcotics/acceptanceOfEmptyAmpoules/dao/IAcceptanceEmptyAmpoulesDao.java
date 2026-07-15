package com.param.pharmacy.narcotics.acceptanceOfEmptyAmpoules.dao;

import java.util.List;

import com.param.entity.model.pharmacy.ReturnOfEmptyAmpoules;
import com.param.entity.model.pharmacy.ReturnOfEmptyAmpoulesDetail;
import com.param.global.dto.global.CommonSearchRequest;

public interface IAcceptanceEmptyAmpoulesDao {
	
	List<ReturnOfEmptyAmpoules> acceptanceEmptyAmpoulesSearch(CommonSearchRequest request, Integer screenId);
	
	ReturnOfEmptyAmpoules acceptanceEmptyAmpoulesById(Integer id);
	
	List<ReturnOfEmptyAmpoulesDetail> acceptanceEmptyAmpoulesDetails(Integer issueId);
	
	ReturnOfEmptyAmpoules acceptanceEmptyAmpoulesSave(ReturnOfEmptyAmpoules returnOfEmptyAmpoules);
	
	ReturnOfEmptyAmpoulesDetail acceptanceEmptyAmpoulesDetailsById(Integer id);
	
}