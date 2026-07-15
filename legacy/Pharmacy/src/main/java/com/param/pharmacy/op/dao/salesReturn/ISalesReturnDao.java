package com.param.pharmacy.op.dao.salesReturn;

import java.util.List;

import com.param.entity.model.pharmacy.Return;
import com.param.entity.model.pharmacy.ReturnDetail;
import com.param.entity.model.pharmacy.Sale;
import com.param.entity.model.pharmacy.SaleDetail;
import com.param.global.common.Response;
import com.param.pharmacy.op.dto.SalesWorkQty;
import com.param.pharmacy.op.dto.salesReturn.ItemDetails;
import com.param.pharmacy.op.dto.salesReturn.RequestSaleReturnDto;
import com.param.pharmacy.op.dto.salesReturn.ResponseAddBill;
import com.param.pharmacy.op.dto.salesReturn.ResponseReturnListSearch;
import com.param.pharmacy.op.dto.salesReturn.ResponseSalesReturnBillList;
import com.param.pharmacy.op.dto.salesReturn.ReturnDetails;
import com.param.pharmacy.op.dto.salesReturn.ReturnItem;
import com.param.pharmacy.op.dto.salesReturn.SalesReturnBillListSerach;
import com.param.pharmacy.op.dto.salesReturn.SalesReturnListSearch;
import com.param.pharmacy.op.dto.salesReturn.SalesReturnQtyDetail;

public interface ISalesReturnDao {
	
	List<Return> salesReturnListSearch(SalesReturnListSearch salesReturnList, Integer screenId);
	
	Return salesReturnById(Integer id, Integer unitId);
	
	List<ReturnDetail> salesReturnDetailsById(Integer returnId);
	
	List<Sale> searchSalesReturnBills(SalesReturnBillListSerach salesReturnBillListSerach, Integer screenId);

	Sale salesReturnGetBillById(Long id);
	
	List<ReturnDetail> populateReturnDetail(Long saleDetailId);
	
	Long getReturnedQuantity(Long id);
	
	Return salesReturnDetailsSave(Return returnObject);

	SaleDetail getSaleDetailById(Long saleDetailId, Integer unitId);

	List<SalesWorkQty> getReturnedQuantityBP(List<Long> detailIds, Integer unitId);

	List<ResponseReturnListSearch> salesReturnListSearchBP(SalesReturnListSearch salesReturnList, Integer screenId);

	ReturnDetails salesReturnByIdBP(Integer id, Integer unitId);

	List<ReturnItem> salesReturnDetailsByIdBP(Integer id, Integer unitId);

	List<ResponseSalesReturnBillList> searchSalesReturnBillsBP(SalesReturnBillListSerach salesReturnBillListSerach,
			Integer screenId, List<Integer> statusesList);

	List<ResponseAddBill> salesReturnGetBillByIdBP(List<Long> id);

//	ResponseAddBill salesReturnGetBillByIdBP(Long id);

	List<ItemDetails> saleDetailsBySaleId(Long id);

	List<SalesReturnQtyDetail> populateReturnDetailBP(List<Long> saleDetailsIds);

	List<SaleDetail> getSaleDetailByIdBP(List<Long> saleDetailIds);
	
	String getMuiltipleBillNoByReturnId(Integer id);
	
	Return salesReturnUpdate(Return returObj);

	Response getPatientNameSearchForSalesReturn(RequestSaleReturnDto requestSaleReturnDto);
	
	List<Long> getSaleIdForNotReturned(List<Long> saleIds);

	Integer getTaxId(Long id);

	List<ItemDetails> patientIssueReturnList(List<Long> saleDetailsIds);

	Double getSalesAmount(List<Long> saleIds, Integer selfPayeeId);

	Double getReturnedAmount(List<Long> saleIds);

	String getPaymentRefundVoucherNoByReturnId(Integer id);
	
}