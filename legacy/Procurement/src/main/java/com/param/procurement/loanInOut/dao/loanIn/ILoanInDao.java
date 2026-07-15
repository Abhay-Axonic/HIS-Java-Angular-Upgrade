package com.param.procurement.loanInOut.dao.loanIn;

import java.util.List;

import com.param.entity.model.inventory.Batch;
import com.param.entity.model.procurement.LoanIn;
import com.param.entity.model.procurement.LoanInDetail;
import com.param.entity.model.procurement.ShelfItem;
import com.param.procurement.loanInOut.dto.LoanInOutRequest;

public interface ILoanInDao {

	List<LoanIn> getLoanInList(LoanInOutRequest request, Integer screenId);
	
	LoanIn getLoanInDetails(Integer id, Integer unitId);
	
	List<LoanInDetail> getLoanInDetailList(Integer loanInId);
	
	LoanIn saveLoanIn(LoanIn loanIn);

	LoanInDetail getLoanInDetailById(Integer id);
	
	ShelfItem saveShelfItem(ShelfItem shelfItem);
	
	Batch updateBatchQuantityAndFlag(Batch batch);
	
	LoanIn updateLoanIn(LoanIn loanIn);
	
	Batch saveBatch(Batch batch);
}
