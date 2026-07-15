package com.param.procurement.loanInOut.dao.loanInOut;

import java.util.List;

import com.param.entity.model.inventory.Batch;
import com.param.entity.model.procurement.LoanIn;
import com.param.entity.model.procurement.LoanInDetail;
import com.param.entity.model.procurement.LoanInOut;
import com.param.entity.model.procurement.LoanInOutDetail;
import com.param.entity.model.procurement.ShelfItem;
import com.param.procurement.loanInOut.dto.LoanInOutRequest;

public interface ILoanInOutDao {

	List<LoanInOut> getloanInOutList(LoanInOutRequest request, Integer screenId);
	
	LoanInOut getloanInOutDetails(Integer id, Integer unitId);
	
	LoanInOut saveloanInOut(LoanInOut loanIn);

	LoanInOutDetail getloanInOutDaoDetailById(Integer id);
	
	ShelfItem saveShelfItem(ShelfItem shelfItem);
	
	Batch updateBatch(Batch batch);
	
	LoanInOut updateloanInOut(LoanInOut loanIn);
	
	Batch saveBatch(Batch batch);
	
	List<LoanIn> getLoanInItems(LoanInOutRequest request);
	
	List<LoanInDetail> getLoanInItemDetails(LoanInOutRequest request);
	
	List<LoanInDetail> getLoanInItemDetails(Integer loanInId, Integer unitId);

	List<LoanInOutDetail> getLoanInOutDtls(Integer id, Integer screenId, Integer unitId);
}
