package com.param.procurement.quotation.service.physicalInspection;

import java.util.List;

import org.springframework.util.MultiValueMap;

import com.param.common.dto.CommonResponse;
import com.param.global.dto.AllDataResponse;
import com.param.global.pharmacy.dto.BillableConsumptionSave;
import com.param.procurement.quotation.dto.physicalInspection.ConsignmentStockConversationBatch;
import com.param.procurement.quotation.dto.physicalInspection.ConsignmentStockConversationSave;
import com.param.procurement.quotation.dto.physicalInspection.ConsignmentStockConversionDetails;
import com.param.procurement.quotation.dto.physicalInspection.ConsignmentStockConversionItem;
import com.param.procurement.quotation.dto.physicalInspection.DetailsConsignmentStockConversionSaveRequest;
import com.param.procurement.quotation.dto.physicalInspection.PhysicalInspectionRequest;
import com.param.procurement.quotation.dto.physicalInspection.PhysicalInspectionResponse;

public interface IConsignmentStockConversionService {

	List<PhysicalInspectionResponse> getPhysicalnspectionList(PhysicalInspectionRequest physicalInspectionRequest);
	
	ConsignmentStockConversionDetails getConsignmentStockConversionDetails(PhysicalInspectionRequest physicalInspectionRequest);
	
	//CommonResponse saveConsignmentStockConversion(ConsignmentStockConversionItem consignmentStockConversionItem) throws Exception;
	
	//CommonResponse saveConsignmentStockConversion(ConsignmentStockConversionDetails consignmentStockConversionDetails) throws Exception;
	
	CommonResponse saveConsignmentStockConversion(DetailsConsignmentStockConversionSaveRequest detailsConsignmentStockConversionSaveRequest) throws Exception;

	List<AllDataResponse> allDataList(Integer storeId , Integer batchId, String batchNo, String lotNo);
	
} 
