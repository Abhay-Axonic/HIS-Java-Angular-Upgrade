package com.param.procurement.loanInOut.dao.loanOut;

import java.util.List;

import com.param.entity.model.procurement.LoanOut;
import com.param.entity.model.procurement.LoanOutDetail;
import com.param.procurement.loanInOut.dto.LoanInOutRequest;
import com.param.procurement.loanInOut.dto.LoanInOutResponse;

public interface ILoanOutDao {

	LoanOut saveLoanIn(LoanOut loanOut);

	List<LoanInOutResponse> searchLoanOutList(LoanInOutRequest request, Integer screenId);

	LoanOut getLoanOut(Integer id, Integer unitId);

	LoanOut updateLoanOut(LoanOut loanOut);

	List<LoanOutDetail> getLoanOutDetails(Integer id);

}
