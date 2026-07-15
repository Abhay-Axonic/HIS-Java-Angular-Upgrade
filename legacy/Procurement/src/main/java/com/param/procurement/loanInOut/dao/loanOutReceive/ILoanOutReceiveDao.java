package com.param.procurement.loanInOut.dao.loanOutReceive;

import java.util.List;

import com.param.entity.model.procurement.LoanOut;
import com.param.entity.model.procurement.LoanOutDetail;
import com.param.entity.model.procurement.LoanOutReceive;
import com.param.procurement.loanInOut.dto.LoanInOutRequest;
import com.param.procurement.loanInOut.dto.LoanInOutResponse;

public interface ILoanOutReceiveDao {

	LoanOutReceive saveLoanOutReceive(LoanOutReceive loanOutReceive);

	List<LoanInOutResponse> searchLoanOutReceiveList(LoanInOutRequest request, Integer screenId);

	LoanOutReceive getLoanOutReceive(Integer id, Integer unitId);

	List<LoanOut> getLoanOutItems(LoanInOutRequest request, List<Integer> statusIds);

	List<LoanOutDetail> getLoanOutItemsDetails(LoanInOutRequest request);

	LoanOutReceive updateLoanOutReceive(LoanOutReceive loanOutReceive);

	List<LoanOutReceive> getLoanOutReceive1(Integer id, Integer unitId);

}
