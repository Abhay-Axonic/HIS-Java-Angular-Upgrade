package com.param.procurement.loanInOut.service.loanIn;

import java.util.List;

import com.param.procurement.loanInOut.dto.LoanInOutDropdown;
import com.param.procurement.loanInOut.dto.LoanInOutRequest;
import com.param.procurement.loanInOut.dto.LoanInOutResponse;

public interface ILoanInService {

	List<LoanInOutResponse> getLoanInList(LoanInOutRequest request);
	
	LoanInOutResponse getLoanInDetails(Integer id, Integer unitId);
	
	Object saveLoanIn(LoanInOutRequest request);

	LoanInOutDropdown getDropdownList(Integer unitId, Integer orgId, Integer userId);
	
	Object cancelLoanIn(LoanInOutRequest request);
	
	Object approveLoanIn(LoanInOutRequest request);
	
	Object updateLoanIn(LoanInOutRequest request);
} 
