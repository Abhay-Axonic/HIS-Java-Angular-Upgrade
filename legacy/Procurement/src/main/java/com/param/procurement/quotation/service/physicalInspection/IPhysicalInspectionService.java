package com.param.procurement.quotation.service.physicalInspection;

import java.util.List;

import com.param.procurement.quotation.dto.physicalInspection.ConsignmentStockConversionDetails;
import com.param.procurement.quotation.dto.physicalInspection.PhysicalInspectionDetails;
import com.param.procurement.quotation.dto.physicalInspection.PhysicalInspectionDropdown;
import com.param.procurement.quotation.dto.physicalInspection.PhysicalInspectionRequest;
import com.param.procurement.quotation.dto.physicalInspection.PhysicalInspectionResponse;
import com.param.procurement.quotation.dto.physicalInspection.PhysicalInspectionSave;

public interface IPhysicalInspectionService {

	List<PhysicalInspectionResponse> getPhysicalnspectionList(PhysicalInspectionRequest physicalInspectionRequest);
	
	PhysicalInspectionDetails getPhysicalnspectionDetails(PhysicalInspectionRequest physicalInspectionRequest);
	
	ConsignmentStockConversionDetails getConsignmentStockConversionDetails(PhysicalInspectionRequest physicalInspectionRequest);
	
		
	Object savePhysicalnspection(PhysicalInspectionSave physicalInspectionSave);

	PhysicalInspectionDropdown getDropdownList(Integer unitId, Integer orgId, Integer userId);
} 
