package com.param.global.dao;

import java.util.List;

import com.param.entity.model.pharmacy.Issue;
import com.param.entity.model.pharmacy.OperationsCompletionInventory;
import com.param.entity.model.pharmacy.Sale;
import com.param.global.common.Response;
import com.param.global.dto.items.ItemsSearchResponse;
import com.param.global.otims.dto.OtcCompletionInvImplantsDto;
import com.param.global.otims.dto.OtcCompletionInvMedicationDto;

public interface IOTCompletionDao {

	List<ItemsSearchResponse> itemsSearch(boolean isImplant, String search, Integer storeId, Integer unitId);

	OperationsCompletionInventory saveOTCompletions(OperationsCompletionInventory completionInventory);

	Sale saveSale(Sale sale);

	Issue saveIssue(Issue issue);

	Response getIssueAcceptanceData(OtcCompletionInvMedicationDto otcCompletionInvMedicationDto);

	Response getImplantIssueAcceptanceData(OtcCompletionInvImplantsDto otcCompletionInvImplantsDto);

	Response updateIssue(Sale sale, Integer issueId);

	Response getBatchListByItemId(int storeId, Integer itemId, Integer orgId, Integer unitId) throws ArithmeticException;

	Response getUOMListByBatchId(Integer batchId, Integer orgId, Integer unitId)throws ArithmeticException;

}
