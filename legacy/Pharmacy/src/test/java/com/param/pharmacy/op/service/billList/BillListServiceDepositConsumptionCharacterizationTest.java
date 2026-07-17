package com.param.pharmacy.op.service.billList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
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

import com.param.entity.model.pharmacy.Sale;
import com.param.entity.model.pharmacy.SaleType;
import com.param.global.billing.dao.IDepositMasterDao;
import com.param.global.billing.dao.IPOSMachineResponseMapperDao;
import com.param.global.billing.dto.PaymentDepositDetailsDto;
import com.param.global.billing.dto.PaymentDepositDto;
import com.param.global.billing.dto.PaymentDepositDetailsList;
import com.param.global.billing.service.IDepositMasterService;
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
 * {@code BillListService.saveBillDetails}'s existing-deposit CONSUMPTION loop
 * (production source lines ~453-574) -- deliberately deferred out of batch 1's scope,
 * now completed. Deliberately still NOT covering {@code buildSalePaymentModes} (a
 * separate remaining-scope item, characterized in its own test file).
 */
@ExtendWith(MockitoExtension.class)
@SuppressWarnings({ "rawtypes", "unchecked" })
@DisplayName("BillListService.saveBillDetails -- deposit-consumption loop characterization")
class BillListServiceDepositConsumptionCharacterizationTest {

    @Mock private IBillListDao billListDao;
    @Mock private IDepositMasterDao iDepositMasterDao;
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
        service.BillListDao = billListDao;
        service.iDepositMasterService = iDepositMasterService;
        service.iPOSMachineResponseMapperDao = iPOSMachineResponseMapperDao;
        service.GlobalDao = globalDao;
        service.WalkInSalesDao = walkInSalesDao;
        setPrivate(service, "iDepositMasterDao", iDepositMasterDao);
        setPrivate(service, "iGlobalUnitMasterDao", iGlobalUnitMasterDao);
        setPrivate(service, "sessionService", sessionService);

        lenient().when(billListDao.saveBill(any(Sale.class))).thenAnswer(inv -> inv.getArgument(0));
        lenient().when(sessionService.getSelfPayeeId()).thenReturn(10);
        lenient().when(globalDao.updateDueAmountByBillBilPayeeId(anyInt(), any(), any())).thenReturn(null);
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

    private Sale sale(double outstandingAmount, double totalSelfPayable) {
        Sale sale = new Sale(500L);
        sale.setSaleType(new SaleType(2));
        sale.setOutstandingAmount(outstandingAmount);
        sale.setPaidAmount(0.0);
        sale.setTotalSelfPayable(totalSelfPayable);
        sale.setTotalCreditPayable(0.0);
        sale.setUnitId(0);
        return sale;
    }

    private PaymentDepositDetailsList depositBeingConsumed(Integer billId, double consumedAmount) {
        PaymentDepositDetailsList d = new PaymentDepositDetailsList();
        d.setDepositMasterId(9001);
        d.setBillId(billId);
        d.setBillPayeeDetailsId(null);
        d.setConsumedAmount(consumedAmount);
        d.setAvailableDeposit(500.0);
        d.setOrganizationId(1);
        d.setUnitId(0);
        d.setRemark("test");
        return d;
    }

    private PaymentDepositDetailsDto detailRow(double balanceAmount) {
        PaymentDepositDetailsDto row = new PaymentDepositDetailsDto();
        row.setBalanceAmount(balanceAmount);
        return row;
    }

    private BillSaveRequest request(double balanceAmount, double paidAmount, PaymentDepositDetailsList depositEntry) {
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

        List<PaymentDepositDetailsList> depositList = new ArrayList<>();
        depositList.add(depositEntry);
        request.setPaymentDepositDetailsList(depositList);

        PaymentReceiptDetailsDtosList paymentDetail = new PaymentReceiptDetailsDtosList();
        paymentDetail.setPaymentModeId(1);
        paymentDetail.setAmount(paidAmount);
        paymentDetail.setReceivedFrom("Test Patient");
        List<PaymentReceiptDetailsDtosList> list = new ArrayList<>();
        list.add(paymentDetail);
        request.setPaymentReceiptDetailsDtosList(list);

        return request;
    }

    @Test
    @DisplayName("waterfall consumption across multiple deposit-detail rows: fully consumes row 1, partially consumes row 2, exactly exhausting the deposit's consumedAmount")
    void waterfallConsumption_fullyThenPartiallyConsumesRows() throws Exception {
        Sale sale = sale(800.0, 1000.0);
        when(billListDao.getBillDetails(500L)).thenReturn(sale);
        when(billListDao.getIsInvoiceGeneratied(500L, 10)).thenReturn("Y");
        when(iGlobalUnitMasterDao.getGeneratedNo(0, 0, 144)).thenReturn("DEPCON-001");
        when(billListDao.saveDepositeConsumption(any(BillSaveRequest.class)))
                .thenReturn(new Response<>("success", null, null, null, 7001));

        PaymentDepositDetailsDto row1 = detailRow(100.0);
        PaymentDepositDetailsDto row2 = detailRow(100.0);
        List<PaymentDepositDetailsDto> rows = new ArrayList<>(List.of(row1, row2));
        when(iDepositMasterService.getDepositDetailsByDepositId(9001))
                .thenReturn(new Response<>("success", null, null, rows, null));

        // deposit's own consumedAmount=150: row1 (balance 100) is fully consumed, row2
        // (balance 100) gets the remaining 50, exactly exhausting consumedAmount.
        PaymentDepositDetailsList depositEntry = depositBeingConsumed(null, 150.0);
        BillSaveRequest req = request(1000.0, 1000.0, depositEntry);

        service.saveBillDetails(req);

        assertThat(row1.getConsumedAmount()).isEqualTo(100.0);
        assertThat(row1.getBalanceAmount()).isEqualTo(0.0);
        assertThat(row2.getConsumedAmount()).isEqualTo(50.0);
        assertThat(row2.getBalanceAmount()).isEqualTo(50.0);
        verify(iDepositMasterService, times(2)).updateDepositDetails(any());
    }

    @Test
    @DisplayName("NOTED, NOT FIXED: once the deposit's consumedAmount is fully exhausted, every REMAINING detail row still gets a live no-op updateDepositDetails() write -- the loop guard only checks consumedAmount>0 once, before the loop, not per iteration (same defect class already logged for Billing's consumeDeposit, BR-PENDING-consumedeposit-findings)")
    void consumedAmountExhausted_remainingRowsStillGetNoOpWrite() throws Exception {
        Sale sale = sale(800.0, 1000.0);
        when(billListDao.getBillDetails(500L)).thenReturn(sale);
        when(billListDao.getIsInvoiceGeneratied(500L, 10)).thenReturn("Y");
        when(iGlobalUnitMasterDao.getGeneratedNo(0, 0, 144)).thenReturn("DEPCON-002");
        when(billListDao.saveDepositeConsumption(any(BillSaveRequest.class)))
                .thenReturn(new Response<>("success", null, null, null, 7002));

        PaymentDepositDetailsDto row1 = detailRow(100.0);
        PaymentDepositDetailsDto row2 = detailRow(100.0);
        PaymentDepositDetailsDto row3 = detailRow(100.0);
        List<PaymentDepositDetailsDto> rows = new ArrayList<>(List.of(row1, row2, row3));
        when(iDepositMasterService.getDepositDetailsByDepositId(9001))
                .thenReturn(new Response<>("success", null, null, rows, null));

        // consumedAmount=100 fully exhausts on row1 alone; rows 2 and 3 have nothing left
        // to consume, yet the production loop is only gated ONCE (if(consumedAmount>0)
        // before the loop starts), not per-iteration -- so it still visits and writes rows
        // 2 and 3.
        PaymentDepositDetailsList depositEntry = depositBeingConsumed(null, 100.0);
        BillSaveRequest req = request(1000.0, 1000.0, depositEntry);

        service.saveBillDetails(req);

        assertThat(row1.getConsumedAmount()).isEqualTo(100.0);
        assertThat(row1.getBalanceAmount()).isEqualTo(0.0);
        // rows 2 and 3: zero actually consumed, balance genuinely unchanged...
        assertThat(row2.getConsumedAmount()).isEqualTo(0.0);
        assertThat(row2.getBalanceAmount()).isEqualTo(100.0);
        assertThat(row3.getConsumedAmount()).isEqualTo(0.0);
        assertThat(row3.getBalanceAmount()).isEqualTo(100.0);
        // ...but updateDepositDetails() was still called 3 times, not 1 -- a real, wasted
        // no-op database write per already-exhausted row, matching the recurring defect
        // shape already logged for Billing's consumeDeposit.
        verify(iDepositMasterService, times(3)).updateDepositDetails(any());
    }

    @Test
    @DisplayName("NOTED, NOT FIXED: the billId/billPayeeDetailsId null-check only overwrites when the field is ALREADY non-null -- backwards from 'fill in if missing', same defect class already logged for Billing's consumeDeposit")
    void billIdNullCheck_onlyOverwritesWhenAlreadyNonNull() throws Exception {
        Sale sale = sale(800.0, 1000.0);
        when(billListDao.getBillDetails(500L)).thenReturn(sale);
        when(billListDao.getIsInvoiceGeneratied(500L, 10)).thenReturn("Y");
        when(iGlobalUnitMasterDao.getGeneratedNo(0, 0, 144)).thenReturn("DEPCON-003");
        when(billListDao.saveDepositeConsumption(any(BillSaveRequest.class)))
                .thenReturn(new Response<>("success", null, null, null, 7003));
        when(iDepositMasterService.getDepositDetailsByDepositId(9001))
                .thenReturn(new Response<>("success", null, null, new ArrayList<>(), null));

        // billId starts null -- per the production check (`if (depositDto.getBillId() !=
        // null) depositDto.setBillId(...)`), it should NEVER get filled in.
        PaymentDepositDetailsList depositEntry = depositBeingConsumed(null, 0.0);
        BillSaveRequest req = request(1000.0, 1000.0, depositEntry);

        service.saveBillDetails(req);

        ArgumentCaptor<PaymentDepositDto> captor = ArgumentCaptor.forClass(PaymentDepositDto.class);
        verify(iDepositMasterService).updateDepositMaster(captor.capture());
        assertThat(captor.getValue().getBillId()).isNull();
    }

    @Test
    @DisplayName("billId already non-null: the null-check IS true, so it DOES get overwritten with request.getBillingMasterId() -- confirms the asymmetry precisely (opposite of the null-starting case above)")
    void billIdAlreadyNonNull_getsOverwrittenWithBillingMasterId() throws Exception {
        Sale sale = sale(800.0, 1000.0);
        when(billListDao.getBillDetails(500L)).thenReturn(sale);
        when(billListDao.getIsInvoiceGeneratied(500L, 10)).thenReturn("Y");
        when(iGlobalUnitMasterDao.getGeneratedNo(0, 0, 144)).thenReturn("DEPCON-004");
        when(billListDao.saveDepositeConsumption(any(BillSaveRequest.class)))
                .thenReturn(new Response<>("success", null, null, null, 7004));
        when(iDepositMasterService.getDepositDetailsByDepositId(9001))
                .thenReturn(new Response<>("success", null, null, new ArrayList<>(), null));

        PaymentDepositDetailsList depositEntry = depositBeingConsumed(42, 0.0); // already non-null
        BillSaveRequest req = request(1000.0, 1000.0, depositEntry);
        req.setBillingMasterId(500L);

        service.saveBillDetails(req);

        ArgumentCaptor<PaymentDepositDto> captor = ArgumentCaptor.forClass(PaymentDepositDto.class);
        verify(iDepositMasterService).updateDepositMaster(captor.capture());
        assertThat(captor.getValue().getBillId()).isEqualTo(500);
    }
}
