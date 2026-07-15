package com.param.pharmacy.op.service.salesWorkList;

import java.util.List;

import com.param.pharmacy.ip.dto.assignPicker.CancelRequest;
import com.param.pharmacy.op.dto.salesWorkList.PrescriptionDetailResponse;
import com.param.pharmacy.op.dto.salesWorkList.ResponseWorkListSearch;
import com.param.pharmacy.op.dto.salesWorkList.SalesWorkListDropdowns;
import com.param.pharmacy.op.dto.salesWorkList.WorkListSearch;
import com.param.pharmacy.op.service.walkInSales.CustomException;

public interface ISalesWorkListService {
	
	SalesWorkListDropdowns getDropdownList();

	List<ResponseWorkListSearch> worklistSearch(WorkListSearch WorkListSearch) throws CustomException;

	//PrescriptionDetailResponse prescriptionDetailsById(Long prescriptionId) throws Exception;
	
	PrescriptionDetailResponse getSubstitute(Integer id, Integer itemId) throws Exception;

	PrescriptionDetailResponse prescriptionDetailsByIdBP(Long id, Integer unitId, Integer storeId) throws Exception;

	PrescriptionDetailResponse getSubstitute(Integer id, Integer itemId, Integer encounterId);

	void cancelSalesWorklist(CancelRequest cancelRequest) throws CustomException;
	
}
