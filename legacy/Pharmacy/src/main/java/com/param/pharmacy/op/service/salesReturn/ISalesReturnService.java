package com.param.pharmacy.op.service.salesReturn;

import java.util.List;

import com.param.common.dto.CommonResponse;
import com.param.global.common.Response;
import com.param.pharmacy.op.dto.salesReturn.RequestSaleReturnDto;
import com.param.pharmacy.op.dto.salesReturn.RequestSaleReturnSave;
import com.param.pharmacy.op.dto.salesReturn.ResponseAddBill;
import com.param.pharmacy.op.dto.salesReturn.ResponseReturnListSearch;
import com.param.pharmacy.op.dto.salesReturn.ResponseSalesReturnBillList;
import com.param.pharmacy.op.dto.salesReturn.ReturnDetails;
import com.param.pharmacy.op.dto.salesReturn.SalesReturnBillListSerach;
import com.param.pharmacy.op.dto.salesReturn.SalesReturnListSearch;
@SuppressWarnings("rawtypes")
public interface ISalesReturnService {
	
	List<ResponseReturnListSearch> salesReturnListSearch(SalesReturnListSearch salesReturnList);

	ReturnDetails salesReturnDetailsById(Integer id);
	
	List<ResponseSalesReturnBillList> salesReturnBillListSerach(SalesReturnBillListSerach salesReturnBillListSerach);

	ResponseAddBill salesReturnGetBillById(Long id);
	
	CommonResponse saveSalesReturn(RequestSaleReturnSave requestSaleReturnSave);
	
	List<ResponseReturnListSearch> salesReturnListSearchBP(SalesReturnListSearch salesReturnList);
	
	ReturnDetails salesReturnDetailsByIdBP(Integer id, Integer unitId);

	List<ResponseSalesReturnBillList> salesReturnBillListSerachBP(SalesReturnBillListSerach salesReturnBillListSerach);

	List<ResponseAddBill> salesReturnGetBillByIdBP(List<Long> id);
	
	//ReturnDetails salesReturnDetailsByIdBP(Integer id);
	
	Object cancelsalesReturnById(Integer id,Integer storeId, Integer unitId);
	
	
	CommonResponse updateSalesReturnById(RequestSaleReturnSave requestSaleReturnSave,Integer id, Integer unitId) throws Exception;

	Response getPatientNameSearchForSalesReturn(RequestSaleReturnDto requestSaleReturnDto);

	Double getsalesAmount(List<Long> saleIds, Integer selfPayeeId);

	Double getReturnedAmount(List<Long> saleIds);

}
