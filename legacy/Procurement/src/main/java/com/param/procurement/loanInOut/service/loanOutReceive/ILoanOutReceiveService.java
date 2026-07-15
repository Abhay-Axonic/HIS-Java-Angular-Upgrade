package com.param.procurement.loanInOut.service.loanOutReceive;

import java.util.List;

import com.param.procurement.common.exception.CustomException;
import com.param.procurement.loanInOut.dto.LoanInOutDropdown;
import com.param.procurement.loanInOut.dto.LoanInOutRequest;
import com.param.procurement.loanInOut.dto.LoanInOutResponse;

public interface ILoanOutReceiveService {

	LoanInOutDropdown getDropdownList(Integer unitId, Integer orgId, Integer userId);

	Object saveLoanOutReceive(LoanInOutRequest request) throws CustomException;

	Object getLoanOutReceiveList(LoanInOutRequest request);

	LoanInOutResponse getLoanOutReceiveDetails(Integer id, Integer unitId);

	List<LoanInOutResponse> getLoanOutReceiveItemList(LoanInOutRequest request);

	LoanInOutResponse getLoanOutReceiveAddItemList(LoanInOutRequest request);

	Object cancelLoanOutReceive(LoanInOutRequest request);

	Object approveLoanOutReceive(LoanInOutRequest request);

	Object updateLoanOutReceive(LoanInOutRequest request);

}
