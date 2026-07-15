package com.param.pharmacy.op.service.billList;

import java.util.List;

import com.param.entity.model.pharmacy.PharamcyBillPayeeDetails;
import com.param.global.billing.dto.BillBadDebitListResponse;
import com.param.global.billing.dto.BillListResponse;
import com.param.global.billing.dto.SalePayeeDetailsDto;
import com.param.pharmacy.op.dto.billList.BillListDropdownResponse;
import com.param.pharmacy.op.dto.billList.BillListRequest;
import com.param.pharmacy.op.dto.billList.BillSaveRequest;

public interface IBillListService {
	BillListDropdownResponse getDropdowns();

	List<BillListResponse> getBillList(BillListRequest billListRequest);
	
	BillListResponse getBillDetails(Long id, Integer selfPayeeId, Integer unitId);
	
	Object saveBillDetails(BillSaveRequest request) throws Exception;

	List<BillBadDebitListResponse> badDebtBillsearch(BillListRequest billListRequest);

	BillListResponse badDebtBillDetials(Long id);

	
	List<SalePayeeDetailsDto> badDebtBillPayeeDetials(Long id);

	List<BillListResponse> pharmacyReceiptCancellationFlag(BillListRequest billListRequest);

	PharamcyBillPayeeDetails checkSelfDueAmount(BillSaveRequest request);
	
}
