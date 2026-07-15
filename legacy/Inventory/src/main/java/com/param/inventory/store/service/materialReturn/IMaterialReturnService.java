package com.param.inventory.store.service.materialReturn;

import java.util.List;

import org.springframework.util.MultiValueMap;

import com.param.global.common.Response;
import com.param.inventory.store.dto.materialIssueList.MaterialIssueListDropdownResponse;
import com.param.inventory.store.dto.materialReturn.MaterialReturnIssue;
import com.param.inventory.store.dto.materialReturn.MaterialReurnRequest;
import com.param.inventory.store.dto.materialReturn.ReturnReasonDropdownResponse;
import com.param.inventory.store.dto.storeIndent.StoreIndentDropdownResponse;
import com.param.pharmacy.nursing.workstation.dto.patientIssueReturn.PatientIssueReturnRequest;

public interface IMaterialReturnService {

	StoreIndentDropdownResponse getDropdowns(Integer unitId, Integer orgId, Integer userId);

	Object saveMaterialReturn(MaterialReurnRequest request);

	Object getMaterialReturnList(MaterialReurnRequest request);

	Object getMaterialReturnDetails(Integer id, Integer unitId);

	Object approveMaterialReturn(MaterialReurnRequest request);

	ReturnReasonDropdownResponse getDropdownForReturnReason(Integer unitId);

	MaterialIssueListDropdownResponse dropdownMaterialReturnCarrierList(Integer unitId, Integer orgId, Integer userId);

	Object updateMaterialReturn(MaterialReurnRequest request);

	Object cancelMaterialReturn(MaterialReurnRequest request);

	List<MaterialReturnIssue> patientAgainstIssueSearch(PatientIssueReturnRequest patientIssueReturnRequest);

	List<MaterialReturnIssue> againstExpireItemSearch(PatientIssueReturnRequest patientIssueReturnRequest);
	
    Response getMaterialReturnDetails1(List<Integer> ids, Integer fromStoreId, Integer toStoreId) throws Exception;

	Response materialReturnStatus(Integer id);

}
