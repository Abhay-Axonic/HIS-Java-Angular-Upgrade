package com.param.global.billing.service;

import java.util.List;

import com.param.billing.global.ConvertServicesFromOptoIpDto;
import com.param.billing.global.CostEstimateDto;
import com.param.global.common.Response;
import com.param.global.dto.AdmissionsForBatchProcessDto;
import com.param.global.dto.BillingMasterDto;
import com.param.global.dto.CancelOrderDto;
import com.param.global.dto.OrderDetailsPackageMapperDto;
import com.param.global.dto.OrderDetailsPayeeMapperDto;
import com.param.global.model.OrderDetailsMaster;
@SuppressWarnings("rawtypes")
public interface IBillingService {

	Response postServiceToEncounterBill(OrderDetailsMaster orderDetailsMasterDto,AdmissionsForBatchProcessDto vistDetailsObj, List<OrderDetailsPayeeMapperDto> orderDetailsPayeeMapperDtosList,List<OrderDetailsPackageMapperDto> orderDetailsPackageMapperDtoList);

	Response postServicesIntoExistingBill(List<Integer> orderDetailsIdsList);

	Response cancelPendingService(CancelOrderDto cancelOrderDto);
	
	AdmissionsForBatchProcessDto getBillByEncounterId(Integer encounterId);

	Response saveBillEstimate(CostEstimateDto costEstimationDto);

	Response convertServicesFromOPToIP(ConvertServicesFromOptoIpDto costEstimationDto);

	Response updateLabSampleStatus(Integer labSampleDetailsId);

	AdmissionsForBatchProcessDto getBillDataByAdmissionId(Integer admissionId);

	Response getPackageConsumeAmountItemwise(Long id, Integer orderDetailsId);

}
