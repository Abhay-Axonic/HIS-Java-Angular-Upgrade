package com.param.pharmacy.op.dao;

import java.util.List;

import com.param.entity.model.pharmacy.Sale;
import com.param.entity.model.pharmacy.SaleDetail;
import com.param.pharmacy.ip.dto.handoverWorklist.HandoverDetails;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyValidationSearch;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyValidationSearchResult;

public interface IHandoverworkListDaoOP {
	
	List<Sale> handoverworkListSearch(PharmacologyValidationSearch pharmacologyValidationSearch, Integer screenId);
	
	Sale handoverworkListDetails(Long id);
	
	List<SaleDetail> handoverworkListDetailList(Long id);
	
	Sale handoverworkListUpdate(Sale sale);

	List<PharmacologyValidationSearchResult> handoverworkListSearchBP(
			PharmacologyValidationSearch pharmacologyValidationSearch, Integer screenId);

	HandoverDetails handoverworkListDetailsBP(Long id);
}