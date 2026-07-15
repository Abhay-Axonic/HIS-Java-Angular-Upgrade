package com.param.procurement.consignment.dao.implantIndentAcceptance;

import java.util.List;

import com.param.entity.model.inventory.StoreStock;
import com.param.entity.model.pharmacy.Issue;
import com.param.entity.model.pharmacy.PatientIssueReturn;
import com.param.entity.model.pharmacy.Sale;
import com.param.procurement.consignment.dto.implantIndent.ImplantIndentRequest;
import com.param.procurement.consignment.dto.implantIndent.ImplantIndentResponse;

public interface IImplantIndentAcceptanceDao {

	List<ImplantIndentResponse> searchMaterialIssueAcceptanceList(ImplantIndentRequest request, Integer screenId);

	Issue getIssue(Integer id);

	PatientIssueReturn savePatientIssueReturn(PatientIssueReturn issueReturn);

	StoreStock saveStoreStock(StoreStock storeStockNew);
	
	Sale saveSale(Sale sale);
}
