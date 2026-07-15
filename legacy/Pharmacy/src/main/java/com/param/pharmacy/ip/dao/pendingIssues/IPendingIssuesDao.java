package com.param.pharmacy.ip.dao.pendingIssues;

import java.util.List;

import com.param.entity.model.pharmacy.Issue;
import com.param.entity.model.pharmacy.IssueDetail;
import com.param.entity.model.pharmacy.Prescription;
import com.param.entity.model.pharmacy.Sale;
import com.param.global.common.Response;
import com.param.global.pharmacy.dto.ItemRequest;
import com.param.global.pharmacy.dto.PendingIssuesSave;
import com.param.pharmacy.ip.dto.pharmacology.DrugItems;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyItem;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyValidationSearch;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyValidationSearchResult;
import com.param.pharmacy.ip.dto.pharmacology.ReturnDetails;

public interface IPendingIssuesDao {
	
	List<Issue> pendingIssuesSearch(PharmacologyValidationSearch pharmacologyValidationSearch, Integer screenId);
	
	Issue pendingIssuesById(Integer id);
	
	Long getItemWiseIssueQuantity(Integer issueDetailId);
	
	List<IssueDetail> pendingIssuesDetails(Integer id);
	
	Issue pendingIssuesSave(Issue issue);
	
	Issue pendingIssuesUpdate(Issue issue);

	Issue getIssue(Integer id);
	
	Sale saveBill(Sale sale);
	
	List<DrugItems> getDrugItems(PharmacologyValidationSearch request);
	
	List<ReturnDetails> getReturnDetails(List<Long> saleId);

	List<PharmacologyValidationSearchResult> pendingIssuesSearchBP(
			PharmacologyValidationSearch pharmacologyValidationSearch, Integer screenId);

	List<PharmacologyItem> getIssuedQuantity(List<Long> prescriptionDetailId);

	List<PharmacologyItem> getQuantityDetails(List<Long> prescriptionDetailId);

	List<Issue> getIssuesOfPicker(Integer id);

	@SuppressWarnings("rawtypes")
	Response issueDetailsIssueQuentity(Integer id);

	Response getOtherIssueDetails(Long long1, Integer id);

	Response getOrederQty(Long id);

	Response updateIssueDetailForSaledetailId(Integer prescriptionDtlsId, Integer batchId);

	List<Prescription> getPreviousIssues(PharmacologyValidationSearch pharmacologyValidationSearch);

	Integer getIssueStatus(Integer id);

	Integer getIssueDetialsStatus(Integer id, Integer id2);
}