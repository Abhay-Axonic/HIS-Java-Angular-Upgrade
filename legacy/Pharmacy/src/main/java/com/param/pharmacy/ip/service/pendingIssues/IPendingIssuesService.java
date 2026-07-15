package com.param.pharmacy.ip.service.pendingIssues;

import java.util.List;

import org.springframework.util.MultiValueMap;

import com.param.common.dto.CommonResponse;
import com.param.entity.model.pharmacy.SaleDetail;
import com.param.entity.model.procurement.ItemRequest;
import com.param.global.common.Response;
import com.param.global.dto.global.CommonDropdown;
import com.param.global.pharmacy.dto.PendingIssuesSave;
import com.param.global.service.items.CustomException;
import com.param.pharmacy.ip.dto.assignPicker.CancelRequest;
import com.param.pharmacy.ip.dto.pharmacology.DrugItems;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyValidationDetails;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyValidationSearch;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyValidationSearchResult;

public interface IPendingIssuesService {
	
	CommonDropdown getDropdownList();
	
	List<PharmacologyValidationSearchResult> pendingIssuesSearch(PharmacologyValidationSearch pharmacologyValidationSearch);

	PharmacologyValidationDetails pendingIssuesDetails(Integer id, Integer unitId, Integer storeId, Integer orgId);
	
	CommonResponse pendingIssuesSave(PendingIssuesSave pendingIssuesSave) throws Exception;
	
	List<DrugItems> getDrugItems(PharmacologyValidationSearch request);

	List<PharmacologyValidationSearchResult> pendingIssuesSearchBP(
			PharmacologyValidationSearch pharmacologyValidationSearch);

	void cancelPendingIssues(CancelRequest cancelReq) throws CustomException;

	Response cancelPendingIssuesRequest(CancelRequest cancelReq) throws CustomException;

	Response updateIssueDetailForSaledetailId(Integer prescriptionDtlsId,Integer batchIdId) throws CustomException;

	Object savePatientIssueAcceptance(PendingIssuesSave issueData);

	List<PharmacologyValidationDetails> pendingPreviousIssues(PharmacologyValidationSearch pharmacologyValidationSearch);

	Response pendingIssueStatus(PendingIssuesSave cancelReq, Integer id);

	void updateInsertIntoStoreStock(List<SaleDetail> details, Long id, PendingIssuesSave pendingIssuesSave);
}