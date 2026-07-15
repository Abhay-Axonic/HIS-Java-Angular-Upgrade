package com.param.global.service;

import java.util.List;

import com.param.global.common.Response;
import com.param.global.dto.items.ItemsSearchResponse;
import com.param.global.model.OrderDetailsMaster;
import com.param.global.otims.dto.OtcCompletionInvMedicationDto;
import com.param.global.procedure.dto.OTCompletionRequest;

public interface IOTCompletionService {

	List<ItemsSearchResponse> itemsSearch(boolean isImplant, String search, Integer storeId, Integer unitId);

	Object saveOTCompletion(OTCompletionRequest request);

	Response getIssueAcceptanceData(OtcCompletionInvMedicationDto otcCompletionInvMedicationDto) ;

	Response chageOfPayeeOt(List<OrderDetailsMaster> dto,Integer admissionId) throws Exception;
}
