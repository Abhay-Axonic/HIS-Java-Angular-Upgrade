package com.param.pharmacy.op.dao.billList;

import java.util.List;

import com.param.billing.global.model.BankMaster;
import com.param.billing.global.model.CardTypeMaster;
import com.param.entity.model.master.PaymentMode;
import com.param.entity.model.pharmacy.PharamcyBillPayeeDetails;
import com.param.entity.model.pharmacy.Sale;
import com.param.global.billing.dto.BillBadDebitListResponse;
import com.param.global.billing.dto.BillListResponse;
import com.param.global.billing.dto.ItemDetailsResponse;
import com.param.global.billing.dto.PaymentDepositDetailsDto;
import com.param.global.billing.dto.SalePayeeDetailsDto;
import com.param.global.common.Response;
import com.param.pharmacy.inPatient.dto.patientConsumableIssueList.PatientDetailsRequestForDrugRecall;
import com.param.pharmacy.inPatient.dto.patientConsumableIssueList.PatientDetailsResponseForDrugRecall;
import com.param.pharmacy.op.dto.billList.BillListRequest;
import com.param.pharmacy.op.dto.billList.BillSaveRequest;

public interface IBillListDao {

	List<Sale> searchBills(BillListRequest billListRequest, Integer screenId);

	Sale getBillDetails(Long id);
	
	Sale saveBill(Sale sale);

	PaymentMode getPaymentModeById(Integer paymentModeId);

	BankMaster getBankMasterById(Integer bankId);

	CardTypeMaster getCardTypeyId(Integer cardTypeId);

	List<BillListResponse> searchBillsBP(BillListRequest billListRequest, Integer screenId);

	List<PatientDetailsResponseForDrugRecall> searchBillsForDrugRecall(PatientDetailsRequestForDrugRecall request);

	List<ItemDetailsResponse> getSaleDetailsBP(Long id);

	BillListResponse getBillDetailsBP(Long id, Integer selfPayeeId, Integer unitId);

	Response saveDepositeConsumption(BillSaveRequest request);

	Response addDepositConsumptionDetails(PaymentDepositDetailsDto paymentDepositDetailsDtoConsumption);



	List<BillBadDebitListResponse> searchBillsBadDebitlist(BillListRequest billListRequest, Integer screenId);

	BillListResponse getbadDebtBillDetialsBP(Long id);

	List<SalePayeeDetailsDto> badDebtBillPayeeDetials(Long id);

	BillListResponse getCreditDuaAmount(Long id, Integer selfPayeeId);

	String getIsInvoiceGeneratied(Long id, Integer selfPayeeId);

	List<BillListResponse> getIpdOrOpdBillGeneratedStatus(List<Long> saleIds);

	List<BillListResponse> pharmacyReceiptCancellationFlag(BillListRequest billListRequest);

	List<BillListResponse>  isPharmacyReturn(List<Long> saleIds);

	List<BillListResponse>  isIssueReturn(List<Long> saleIds);
	
    Integer getOrgId(Long id);

    PharamcyBillPayeeDetails checkSelfDueAmount(BillSaveRequest request);

    List<BillListResponse> isInvoiceGenerated(List<Long> saleIds, Integer unitId);
	
	
}
