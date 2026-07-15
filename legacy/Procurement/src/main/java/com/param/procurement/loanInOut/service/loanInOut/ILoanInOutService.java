package com.param.procurement.loanInOut.service.loanInOut;

import java.util.List;

import com.param.procurement.loanInOut.dto.LoanInOutDropdown;
import com.param.procurement.loanInOut.dto.LoanInOutRequest;
import com.param.procurement.loanInOut.dto.LoanInOutResponse;

public interface ILoanInOutService {

	List<LoanInOutResponse> getLoanInOutList(LoanInOutRequest request);
	
	LoanInOutResponse getLoanInOutDetails(Integer id, Integer unitId);
	
	Object saveLoanInOut(LoanInOutRequest request);

	LoanInOutDropdown getDropdownList(Integer unitId, Integer orgId, Integer userId);
	
	Object cancelLoanInOut(LoanInOutRequest request);
	
	Object approveLoanInOut(LoanInOutRequest request);
	
	Object updateLoanInOut(LoanInOutRequest request);
	
	List<LoanInOutResponse> getLoanInOutItemList(LoanInOutRequest request);
	
	LoanInOutResponse getLoanInOutAddItemList(LoanInOutRequest request);
} 
