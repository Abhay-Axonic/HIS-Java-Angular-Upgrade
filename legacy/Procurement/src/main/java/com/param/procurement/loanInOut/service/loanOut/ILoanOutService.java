package com.param.procurement.loanInOut.service.loanOut;

import com.param.procurement.loanInOut.dto.LoanInOutDropdown;
import com.param.procurement.loanInOut.dto.LoanInOutRequest;
import com.param.procurement.loanInOut.dto.LoanInOutResponse;

public interface ILoanOutService {

	LoanInOutDropdown getDropdownList(Integer unitId, Integer orgId, Integer userId);

	Object saveLoanOut(LoanInOutRequest request);

	Object getLoanOutList(LoanInOutRequest request);

	LoanInOutResponse getLoanOutDetails(Integer id, Integer unitId);

	Object cancelLoanOut(LoanInOutRequest request);

	Object approveLoanOut(LoanInOutRequest request);

	Object updateLoanOut(LoanInOutRequest request);

}
