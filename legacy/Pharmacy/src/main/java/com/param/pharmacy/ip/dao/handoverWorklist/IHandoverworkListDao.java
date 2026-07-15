package com.param.pharmacy.ip.dao.handoverWorklist;

import java.util.List;

import com.param.entity.model.pharmacy.Sale;
import com.param.entity.model.pharmacy.SaleDetail;
import com.param.pharmacy.ip.dto.handoverWorklist.HandoverDetails;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyValidationSearch;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyValidationSearchResult;

public interface IHandoverworkListDao {
	
	List<Sale> handoverworkListSearch(PharmacologyValidationSearch pharmacologyValidationSearch, Integer screenId);
	
	Sale handoverworkListDetails(Long id, Integer unitId);
	
	List<SaleDetail> handoverworkListDetailList(Long id, Integer unitId);
	
	Sale handoverworkListUpdate(Sale sale);

	List<PharmacologyValidationSearchResult> handoverworkListSearchBP(
			PharmacologyValidationSearch pharmacologyValidationSearch, Integer screenId);

	HandoverDetails handoverworkListDetailsBP(Long id, Integer unitId);
	
	Long getIssuedQuantity(Long id);

	List<PharmacologyValidationSearchResult> getDoctorDetail(Long id, Integer unitId);
}