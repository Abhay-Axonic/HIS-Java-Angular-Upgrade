package com.param.pharmacy.op.service.billList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.param.billing.global.model.DepositMaster;
import com.param.common.constants.Status;
import com.param.entity.model.pharmacy.Sale;
import com.param.entity.model.pharmacy.SaleType;
import com.param.global.billing.dao.IDepositMasterDao;
import com.param.global.billing.dao.IPOSMachineResponseMapperDao;
import com.param.global.billing.dao.IPaymentDepositDetailsDao;
import com.param.global.billing.dto.DepositMasterDto;
import com.param.global.billing.dto.PaymentDepositDetailsDto;
import com.param.global.billing.service.IDepositMasterService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IGlobalUnitMasterDao;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.pharmacy.dao.IWalkInSalesDao;
import com.param.pharmacy.op.dao.billList.IBillListDao;
import com.param.pharmacy.op.dto.billList.BillSaveRequest;
import com.param.pharmacy.op.dto.billList.PaymentReceiptDetailsDtosList;
import com.param.service.global.SessionService;

/**
 * Characterization tests (current behavior, not verified-correct behavior) for
 * {@code BillListService.saveBillDetails} -- one of Pharmacy's 5 originally-flagged
 * live financial-calculation calls (this method is the caller of {@code saveDeposit}
 * and {@code savePaymentDepositDetails}), scoped to the method's outstanding-amount
 * computation and overpayment-creates-a-deposit branches (lines ~320-452 of the
 * production source). NOT covered in this batch, deferred to a follow-up batch:
 * the existing-deposit CONSUMPTION loop (~lines 454-574) and the full payment-mode
 * dispatch logic in the private {@code buildSalePaymentModes} helper (mpesa, POS
 * card, loyalty card, cheque, e-transfer, razorpay/ccavenue branches) -- every test
 * here deliberately uses payment mode 1 (plain cash-shaped) and a null/empty
 * deposit-details list on the request so those branches are never entered, isolating
 * the arithmetic under test. No bug in this scope is fixed here, per standing
 * instruction; anything that looks like an actual defect is noted in the test's own
 * comment and logged in the manifest, not corrected.
 */
@ExtendWith(MockitoExtension.class)
@SuppressWarnings({ "rawtypes", "unchecked" })
@DisplayName("BillListService.saveBillDetails -- outstanding-amount + overpayment-deposit-creation characterization")
class BillListServiceSaveBillDetailsCharacterizationTest {

    @Mock private IBillListDao billListDao;
    @Mock private IDepositMasterDao iDepositMasterDao;
    @Mock private IPaymentDepositDetailsDao iPaymentDepositDetailsDao;
    @Mock private IGlobalUnitMasterDao iGlobalUnitMasterDao;
    @Mock private IDepositMasterService iDepositMasterService;
    @Mock private IPOSMachineResponseMapperDao iPOSMachineResponseMapperDao;
    @Mock private IGlobalDao globalDao;
    @Mock private IWalkInSalesDao walkInSalesDao;
    @Mock private SessionService sessionService;

    private BillListService service;

    @BeforeEach
    void setUp() {
        service = new BillListService();
        // Package-private fields on BillListService: direct assignment works from this
        // same-package test class.
        service.BillListDao = billListDao;
        service.iDepositMasterService = iDepositMasterService;
        service.iPOSMachineResponseMapperDao = iPOSMachineResponseMapperDao;
        service.GlobalDao = globalDao;
        service.WalkInSalesDao = walkInSalesDao;
        // `private` fields on BillListService: need reflection, matching production
        // visibility exactly (mirrors this project's existing characterization-test
        // convention rather than loosening production access modifiers for testability).
        setPrivate(service, "iDepositMasterDao", iDepositMasterDao);
        setPrivate(service, "iPaymentDepositDetailsDao", iPaymentDepositDetailsDao);
        setPrivate(service, "iGlobalUnitMasterDao", iGlobalUnitMasterDao);
        setPrivate(service, "sessionService", sessionService);

        // Common stubs every test needs to reach the end of the method without an NPE.
        // lenient(): not every test's code path reaches all of these.
        lenient().when(billListDao.saveBill(any(Sale.class))).thenAnswer(inv -> inv.getArgument(0));
        lenient().when(globalDao.updateDueAmountByBillBilPayeeId(anyInt(), any(), any())).thenReturn(null);
        lenient().when(sessionService.getSelfPayeeId()).thenReturn(10);
    }

    private static void setPrivate(Object target, String fieldName, Object value) {
        try {
            java.lang.reflect.Field f = target.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            f.set(target, value);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    private Sale sale(double outstandingAmount, double totalSelfPayable, double totalCreditPayable) {
        Sale sale = new Sale(500L);
        sale.setSaleType(new SaleType(2)); // not 1, so it's left unchanged (line 323-326 branch not exercised here)
        sale.setOutstandingAmount(outstandingAmount);
        sale.setPaidAmount(0.0);
        sale.setTotalSelfPayable(totalSelfPayable);
        sale.setTotalCreditPayable(totalCreditPayable);
        sale.setUnitId(0); // 0 -> TimeZoneComponent's unitId<=0 branch, avoids the empty static timezone map in a bare unit test
        return sale;
    }

    private BillSaveRequest request(Double balanceAmount, double paidAmount) {
        BillSaveRequest request = new BillSaveRequest();
        request.setBillingMasterId(500L);
        request.setSelfPayeeId(10);
        request.setUnitId(0);
        request.setOrgnizationId(1);
        request.setCreatedBy(99);
        request.setUpdatedBy(99);
        request.setPatientId(777);
        request.setBillPayeeDetailsId(55L);
        request.setBalanceAmount(balanceAmount);
        request.setAmountData(paidAmount);
        request.setPaymentDepositDetailsList(null); // deliberately out of scope this batch, see class javadoc

        PaymentReceiptDetailsDtosList paymentDetail = new PaymentReceiptDetailsDtosList();
        paymentDetail.setPaymentModeId(1); // plain cash-shaped mode: no mpesa/POS/loyalty/cheque/e-transfer branch fires
        paymentDetail.setAmount(paidAmount);
        paymentDetail.setReceivedFrom("Test Patient");
        List<PaymentReceiptDetailsDtosList> list = new ArrayList<>();
        list.add(paymentDetail);
        request.setPaymentReceiptDetailsDtosList(list);

        return request;
    }

    private void stubNoDeposit(Sale sale, String isInvoiceGeneratied) throws Exception {
        when(billListDao.getBillDetails(500L)).thenReturn(sale);
        when(billListDao.getIsInvoiceGeneratied(500L, 10)).thenReturn(isInvoiceGeneratied);
    }

    // ---- outstanding-amount branch: invoice-generated == "Y" ----

    @Test
    @DisplayName("invoiceGenerated=Y, paid > balance -> outstandingAmount hardcoded to 0.00 (uses neither paid nor balance)")
    void invoiceGeneratedY_paidExceedsBalance_outstandingAmountZeroed() throws Exception {
        Sale sale = sale(/*outstanding*/ 800.0, /*totalSelfPayable*/ 1000.0, /*totalCreditPayable*/ 0.0);
        stubNoDeposit(sale, "Y");
        when(iGlobalUnitMasterDao.getGeneratedNo(0, 0, 123)).thenReturn("DEP-001");
        DepositMaster savedDeposit = new DepositMaster();
        savedDeposit.setDepositMasterId(9001);
        when(iDepositMasterDao.saveDeposit(any(DepositMasterDto.class)))
                .thenReturn(new Response<>(ICommonConstants.SUCCESS, ICommonConstants.SUCCESS_CODE, null, null, savedDeposit));

        BillSaveRequest request = request(500.0, 700.0);
        service.saveBillDetails(request);

        assertThat(sale.getOutstandingAmount()).isEqualTo(0.00);
        assertThat(sale.getStatus().getId()).isEqualTo(Status.Completed.getStatus());
    }

    @Test
    @DisplayName("invoiceGenerated=Y, paid > balance -> a genuine overpayment deposit is created for the (paid-balance) remainder")
    void invoiceGeneratedY_paidExceedsBalance_createsDepositForRemainder() throws Exception {
        Sale sale = sale(800.0, 1000.0, 0.0);
        stubNoDeposit(sale, "Y");
        when(iGlobalUnitMasterDao.getGeneratedNo(0, 0, 123)).thenReturn("DEP-001");
        DepositMaster savedDeposit = new DepositMaster();
        savedDeposit.setDepositMasterId(9001);
        when(iDepositMasterDao.saveDeposit(any(DepositMasterDto.class)))
                .thenReturn(new Response<>(ICommonConstants.SUCCESS, ICommonConstants.SUCCESS_CODE, null, null, savedDeposit));

        BillSaveRequest request = request(500.0, 700.0);
        service.saveBillDetails(request);

        ArgumentCaptor<DepositMasterDto> depositCaptor = ArgumentCaptor.forClass(DepositMasterDto.class);
        verify(iDepositMasterDao).saveDeposit(depositCaptor.capture());
        DepositMasterDto deposit = depositCaptor.getValue();
        assertThat(deposit.getAvailableDeposit()).isEqualTo(200.0); // paid(700) - balance(500)
        assertThat(deposit.getDepositAmount()).isEqualTo(200.0);
        assertThat(deposit.getConsumedDeposit()).isEqualTo(0.00);
        assertThat(deposit.getPatientId()).isEqualTo(777);
        assertThat(deposit.getDepositTypeId()).isEqualTo(1);
        assertThat(deposit.getDepositNo()).isEqualTo("DEP-001");

        ArgumentCaptor<PaymentDepositDetailsDto> detailsCaptor = ArgumentCaptor.forClass(PaymentDepositDetailsDto.class);
        verify(iPaymentDepositDetailsDao).savePaymentDepositDetails(detailsCaptor.capture());
        PaymentDepositDetailsDto details = detailsCaptor.getValue();
        assertThat(details.getDepositId()).isEqualTo(9001);
        assertThat(details.getPaymentModeId()).isEqualTo(7);
        assertThat(details.getAmount()).isEqualTo(200.0);
        assertThat(details.getBalanceAmount()).isEqualTo(200.0);
        assertThat(details.getRefundAmount()).isEqualTo(0.00);
    }

    @Test
    @DisplayName("invoiceGenerated=Y, paid < balance -> outstandingAmount = outstanding - paid")
    void invoiceGeneratedY_paidBelowBalance_outstandingAmountReducedByPaid() throws Exception {
        Sale sale = sale(800.0, 1000.0, 0.0);
        stubNoDeposit(sale, "Y");

        BillSaveRequest request = request(900.0, 400.0);
        service.saveBillDetails(request);

        assertThat(sale.getOutstandingAmount()).isEqualTo(400.0); // 800 - 400
        verify(iDepositMasterDao, never()).saveDeposit(any());
        assertThat(sale.getStatus().getId()).isEqualTo(Status.PartiallyCompleted.getStatus());
    }

    // ---- outstanding-amount branch: invoice-generated == "N" (or null, which is treated as "N") ----

    @Test
    @DisplayName("invoiceGenerated=N, paid > balance -> outstandingAmount = outstanding - balance (NOT the same formula as the Y branch)")
    void invoiceGeneratedN_paidExceedsBalance_outstandingAmountReducedByBalanceNotPaid() throws Exception {
        Sale sale = sale(800.0, 1000.0, /*totalCreditPayable*/ 50.0);
        stubNoDeposit(sale, "N");
        when(iGlobalUnitMasterDao.getGeneratedNo(0, 0, 123)).thenReturn("DEP-002");
        DepositMaster savedDeposit = new DepositMaster();
        savedDeposit.setDepositMasterId(9002);
        when(iDepositMasterDao.saveDeposit(any(DepositMasterDto.class)))
                .thenReturn(new Response<>(ICommonConstants.SUCCESS, ICommonConstants.SUCCESS_CODE, null, null, savedDeposit));

        BillSaveRequest request = request(500.0, 700.0);
        service.saveBillDetails(request);

        // Y-branch (same paid/balance shape, invoiceGenerated=Y) hardcodes outstandingAmount to 0.00 (see
        // invoiceGeneratedY_paidExceedsBalance_outstandingAmountZeroed above). This N-branch instead does
        // outstanding - balance = 800 - 500 = 300, a materially different result for what is otherwise the
        // identical paid/balance/outstanding shape -- pinned here, not judged as correct or incorrect.
        assertThat(sale.getOutstandingAmount()).isEqualTo(300.0);
        // invoiceGenerated=N AND totalCreditPayable>0 -> PartiallyCompleted, not Completed
        assertThat(sale.getStatus().getId()).isEqualTo(Status.PartiallyCompleted.getStatus());
    }

    @Test
    @DisplayName("invoiceGenerated=null (DAO returns no row) -> treated as N; paid < balance -> outstandingAmount = outstanding - paid")
    void invoiceGeneratedNull_treatedAsN_paidBelowBalance() throws Exception {
        Sale sale = sale(800.0, 1000.0, 0.0);
        stubNoDeposit(sale, null);

        BillSaveRequest request = request(900.0, 400.0);
        service.saveBillDetails(request);

        // Same numeric result as the Y-branch's paid<balance case (both subtract paidAmount) -- the Y/N
        // distinction only actually changes behavior in the paid>balance case, not here.
        assertThat(sale.getOutstandingAmount()).isEqualTo(400.0);
        verify(iDepositMasterDao, never()).saveDeposit(any());
    }

    // ---- paid == balance exactly (falls through the Y/N branches into the final "else") ----

    @Test
    @DisplayName("balanceAmount omitted (defaults to totalSelfPayable) and paid == that default exactly -> outstandingAmount can go NEGATIVE")
    void balanceAmountDefaulted_paidEqualsDefault_outstandingAmountGoesNegative() throws Exception {
        // request.getBalanceAmount()==null defaults it to sale.getTotalSelfPayable() (checkBalance=1 path,
        // lines ~348-352 of the production source) before the Y/N branch logic runs.
        Sale sale = sale(/*outstanding*/ 800.0, /*totalSelfPayable*/ 1000.0, 0.0);
        stubNoDeposit(sale, "Y");

        BillSaveRequest request = request(null, 1000.0); // balanceAmount omitted; paid == totalSelfPayable
        service.saveBillDetails(request);

        // NOTE (characterization only, not a fix): paidAmount(1000) equals the defaulted balanceAmount(1000)
        // exactly, so the paid>balance overpayment-deposit-creation block never fires (it requires a strict
        // ">"), yet the outstanding-amount arithmetic still subtracts the full paidAmount from the ORIGINAL
        // outstanding balance (800), producing -200.00. No deposit or credit record is created for this
        // negative outstanding balance on this path -- whether that's an intended "patient has a credit"
        // representation or an unaccounted-for overpayment is a business-intent question, not something this
        // test resolves. Logged in the manifest as a characterized observation, not flagged as an urgent
        // active money-loss finding (no confirmed evidence of harm, unlike the session's fast-tracked fixes).
        assertThat(sale.getOutstandingAmount()).isEqualTo(-200.0);
        verify(iDepositMasterDao, never()).saveDeposit(any());
    }

    // ---- deposit-number validation gate ----

    @Test
    @DisplayName("deposit-number generator returns null -> the method stops immediately with a config-error Response, before touching the sale")
    void depositNumberGeneratorReturnsNull_stopsBeforeSavingAnything() throws Exception {
        Sale sale = sale(800.0, 1000.0, 0.0);
        stubNoDeposit(sale, "N");
        when(iGlobalUnitMasterDao.getGeneratedNo(0, 0, 123)).thenReturn(null);

        BillSaveRequest request = request(500.0, 700.0); // paid > balance, so the overpayment branch is reached
        Object result = service.saveBillDetails(request);

        assertThat(result).isInstanceOf(Response.class);
        Response<?, ?> response = (Response<?, ?>) result;
        assertThat(response.getStatus()).isEqualTo(ICommonConstants.ERROR);
        assertThat(response.getCode()).isEqualTo(ICommonConstants.ERROR_CODE);
        assertThat(response.getMessage()).isEqualTo("Please Configure Deposit Number.");

        verify(iDepositMasterDao, never()).saveDeposit(any());
        verify(billListDao, never()).saveBill(any());
        verify(globalDao, never()).updateDueAmountByBillBilPayeeId(anyInt(), any(), any());
    }

    // ---- deposit-master save failing (Response object null / non-success) ----

    @Test
    @DisplayName("deposit save fails (non-success Response) -> the method does NOT stop: it silently skips creating the compensating payment-deposit-details record and proceeds to save the bill anyway")
    void depositSaveFails_silentlySkipsPaymentDepositDetails_butStillCompletesTheBill() throws Exception {
        Sale sale = sale(800.0, 1000.0, 0.0);
        stubNoDeposit(sale, "N");
        when(iGlobalUnitMasterDao.getGeneratedNo(0, 0, 123)).thenReturn("DEP-003");
        // getObject()==null is one of the two ways the "success" check at production line ~422 can fail
        // (`depositRes.getCode().equalsIgnoreCase(SUCCESS_CODE) && depositRes.getObject() != null`).
        when(iDepositMasterDao.saveDeposit(any(DepositMasterDto.class)))
                .thenReturn(new Response<>(ICommonConstants.SUCCESS, ICommonConstants.SUCCESS_CODE, null, null, null));

        BillSaveRequest request = request(500.0, 700.0);
        Object result = service.saveBillDetails(request);

        // NOTE (characterization only): no exception is thrown and the method does not stop -- it simply
        // never calls savePaymentDepositDetails, so the $200 overpayment recorded as "available" on the
        // (unusable, since we don't have its real id) deposit is never reflected in a matching
        // PaymentDepositDetails row. The bill itself still saves successfully. This is a real gap in the
        // failure path, not the happy path -- logged in the manifest as a moderate, non-urgent finding
        // (it only matters if saveDeposit itself fails, which is presumably rare), not fixed here.
        verify(iPaymentDepositDetailsDao, never()).savePaymentDepositDetails(any());
        verify(billListDao).saveBill(any(Sale.class));
        verify(walkInSalesDao, never()).updateSalePaymentModeIdInDepositMaster(any(), any());
        assertThat(result).isNotNull();
    }
}
