package com.param.pharmacy.op.service.billList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.param.entity.model.pharmacy.Sale;
import com.param.entity.model.pharmacy.SalePaymentMode;
import com.param.global.billing.dao.IPOSMachineResponseMapperDao;
import com.param.global.common.Response;
import com.param.global.mail.IMailingService;
import com.param.global.mpesa.Dao.IMpesaMasterDao;
import com.param.global.org.common.dao.IPatientLoyaltyCardMapperDao;
import com.param.global.org.common.dto.PatientLoyaltyCardMapperDto;
import com.param.pharmacy.op.dto.billList.BillSaveRequest;
import com.param.pharmacy.op.dto.billList.PaymentReceiptDetailsDtosList;
import com.param.service.global.SessionService;

/**
 * Characterization tests (current behavior, not verified-correct behavior) for
 * {@code BillListService.buildSalePaymentModes} -- the private payment-mode dispatch
 * helper deferred out of batches 1-3. Invoked directly via reflection (it's private and
 * self-contained given its own two parameters plus the service's autowired
 * dependencies, so isolating it avoids building an unrelated {@code saveBillDetails}
 * fixture).
 */
@ExtendWith(MockitoExtension.class)
@SuppressWarnings({ "rawtypes", "unchecked" })
@DisplayName("BillListService.buildSalePaymentModes -- payment-mode dispatch characterization")
class BillListServiceBuildSalePaymentModesCharacterizationTest {

    @Mock private IMpesaMasterDao iMpesaMasterDao;
    @Mock private IPatientLoyaltyCardMapperDao iPatientLoyaltyCardMapperDao;
    @Mock private IMailingService iMailingService;
    @Mock private IPOSMachineResponseMapperDao iPOSMachineResponseMapperDao;
    @Mock private SessionService sessionService;

    private BillListService service;

    @BeforeEach
    void setUp() {
        service = new BillListService();
        service.iPatientLoyaltyCardMapperDao = iPatientLoyaltyCardMapperDao;
        service.iMailingService = iMailingService;
        service.iPOSMachineResponseMapperDao = iPOSMachineResponseMapperDao;
        setPrivate(service, "iMpesaMasterDao", iMpesaMasterDao);
        setPrivate(service, "sessionService", sessionService);
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

    @SuppressWarnings("unchecked")
    private List<SalePaymentMode> invoke(BillSaveRequest request, Sale sale) throws Exception {
        java.lang.reflect.Method m = BillListService.class.getDeclaredMethod(
                "buildSalePaymentModes", BillSaveRequest.class, Sale.class);
        m.setAccessible(true);
        return (List<SalePaymentMode>) m.invoke(service, request, sale);
    }

    private Sale sale() {
        Sale sale = new Sale(500L);
        sale.setUnitId(0);
        return sale;
    }

    private BillSaveRequest request(PaymentReceiptDetailsDtosList... payments) {
        BillSaveRequest request = new BillSaveRequest();
        request.setBillingMasterId(500L);
        request.setUnitId(0);
        request.setOrgnizationId(1);
        request.setCreatedBy(99);
        request.setPaymentReceiptDetailsDtosList(Arrays.asList(payments));
        return request;
    }

    private PaymentReceiptDetailsDtosList payment(int paymentModeId, double amount) {
        PaymentReceiptDetailsDtosList p = new PaymentReceiptDetailsDtosList();
        p.setPaymentModeId(paymentModeId);
        p.setAmount(amount);
        return p;
    }

    @Test
    @DisplayName("NOTED, NOT FIXED: mode 5 (mpesa) with skipMpesaValidation=true -- the nested 'mode==3' POS-flag block is structurally UNREACHABLE dead code, since paymentModeId cannot simultaneously equal 5 and 3")
    void mpesaMode_nestedPosBlockIsUnreachableDeadCode() throws Exception {
        // NOTE (characterization, not a fix): production nests
        // `if (paymentDetail.getPaymentModeId() == 3 && ...)` INSIDE the enclosing
        // `if (paymentDetail.getPaymentModeId() != null && paymentDetail.getPaymentModeId() == 5)`
        // block. A single payment's paymentModeId cannot be both 5 and 3 at once, so this
        // inner block can never execute from this location -- confirmed here by asserting
        // updatePOSMachineResponseMapperFlag is never called even when every other
        // condition of the inner block (skipMpesaValidation==true) is satisfied.
        PaymentReceiptDetailsDtosList p = payment(5, 100.0);
        p.setSkipMpesaValidation(true);
        p.setTransactionNo("TXN-1");
        when(iMpesaMasterDao.validateMpesaTrancastion(any())).thenReturn(new Response<>("success", null));

        List<SalePaymentMode> result = invoke(request(p), sale());

        assertThat(result).hasSize(1);
        verify(iMpesaMasterDao).validateMpesaTrancastion(any());
        verify(iPOSMachineResponseMapperDao, never()).updatePOSMachineResponseMapperFlag(any());
    }

    @Test
    @DisplayName("mode 12 (gateway), providerId=1: routes to updateRazorPay; providerId=2: routes to updateCCAvenue")
    void gatewayMode_routesByProviderId() throws Exception {
        PaymentReceiptDetailsDtosList razorpay = payment(12, 100.0);
        razorpay.setPaymentGatewayProviderId(1);
        razorpay.setTransactionNo("RZP-1");
        when(iMpesaMasterDao.updateRazorPay("RZP-1")).thenReturn(new Response<>("success", null));

        invoke(request(razorpay), sale());
        verify(iMpesaMasterDao).updateRazorPay("RZP-1");
        verify(iMpesaMasterDao, never()).updateCCAvenue(any(), any());
    }

    @Test
    @DisplayName("mode 12 (gateway), providerId=2: routes to updateCCAvenue with the billingMasterId")
    void gatewayMode_ccAvenue() throws Exception {
        PaymentReceiptDetailsDtosList ccavenue = payment(12, 100.0);
        ccavenue.setPaymentGatewayProviderId(2);
        ccavenue.setTransactionNo("CCA-1");
        when(iMpesaMasterDao.updateCCAvenue("CCA-1", 500)).thenReturn(new Response<>("success", null));

        invoke(request(ccavenue), sale());
        verify(iMpesaMasterDao).updateCCAvenue("CCA-1", 500);
    }

    @Test
    @DisplayName("MAJOR FINDING, NOTED NOT FIXED: mode 6 (loyalty card), sufficient card balance -- the card IS consumed and the payment mode IS added to the result, but the mailing/consumption notification (consumeLoyaltyAmount) is NEVER sent, no matter what -- it's structurally dead code")
    void loyaltyCard_sufficientBalance_addsToListButNeverNotifies() throws Exception {
        // MAJOR FINDING (characterization only, not a fix): production source (lines
        // ~823-830) has:
        //     }
        //     else
        //         //comment, not a real statement
        //
        //     for (PatientLoyaltyCardMapperDto patientLoyaltyCardMapperDto : mailList) {
        //         iMailingService.consumeLoyaltyAmount(patientLoyaltyCardMapperDto);
        //     }
        // Java requires a real statement after a brace-less `else`; since the only
        // candidate statement here is the `for` loop, the loop is INSIDE the else clause
        // -- it only runs when paymentReceiptDetailsDtosList is null/empty. But `mailList`
        // is only ever populated INSIDE the main if-branch's own for-loop (when a
        // sufficient-balance loyalty-card payment is processed) -- the one branch where the
        // mailList-consuming loop is reachable is exactly the one branch where mailList is
        // guaranteed empty. Net effect: iMailingService.consumeLoyaltyAmount(...) can NEVER
        // fire, for any input, ever -- confirmed empirically by this test (a first version
        // asserted it WAS called and failed with "zero interactions", which is what led to
        // finding this).
        PaymentReceiptDetailsDtosList p = payment(6, 100.0);
        p.setCardNumber("CARD-1");
        p.setCreatedBy(99);

        PatientLoyaltyCardMapperDto found = new PatientLoyaltyCardMapperDto();
        found.setCardAmount(200.0); // >= requested 100.0
        when(iPatientLoyaltyCardMapperDao.getPatientDetailsByLoyaltiCardNo(any()))
                .thenReturn(new Response<>("success", null, null, List.of(found), null));

        List<SalePaymentMode> result = invoke(request(p), sale());

        // The card consumption itself still happens...
        assertThat(result).hasSize(1);
        verify(iPatientLoyaltyCardMapperDao).updateLoyaltyCardAmountByCard(any());
        // ...but the notification never does, structurally, regardless of input.
        verify(iMailingService, never()).consumeLoyaltyAmount(any());
    }

    @Test
    @DisplayName("NOTED, NOT FIXED: mode 6 (loyalty card), INSUFFICIENT card balance -- the whole method returns null, silently discarding every OTHER payment mode in the same request too, not just this one; the caller (saveBillDetails) then reports the generic 'Invalid Transaction ID' message, masking the real cause")
    void loyaltyCard_insufficientBalance_returnsNullDiscardingWholeList() throws Exception {
        // NOTE (characterization, not a fix): `return null;` inside the per-payment loop
        // exits buildSalePaymentModes entirely -- any OTHER payment modes already
        // processed earlier in the same request's paymentReceiptDetailsDtosList are
        // discarded along with this one, and saveBillDetails's own caller-side check
        // (`if (salePaymentModes != null)`) then reports "Invalid Transaction ID" --
        // a real but different problem (insufficient loyalty balance) reported under a
        // misleading generic message, similar in shape to the already-logged
        // BR-PENDING-savebill-misleading-conflict-message finding.
        PaymentReceiptDetailsDtosList cashFirst = payment(1, 50.0); // would otherwise succeed on its own
        PaymentReceiptDetailsDtosList loyaltyCard = payment(6, 100.0);
        loyaltyCard.setCardNumber("CARD-1");
        loyaltyCard.setCreatedBy(99);

        PatientLoyaltyCardMapperDto found = new PatientLoyaltyCardMapperDto();
        found.setCardAmount(50.0); // < requested 100.0 -- insufficient
        when(iPatientLoyaltyCardMapperDao.getPatientDetailsByLoyaltiCardNo(any()))
                .thenReturn(new Response<>("success", null, null, List.of(found), null));

        List<SalePaymentMode> result = invoke(request(cashFirst, loyaltyCard), sale());

        assertThat(result).isNull();
        verify(iPatientLoyaltyCardMapperDao, never()).updateLoyaltyCardAmountByCard(any());
    }

    @Test
    @DisplayName("NOTED, NOT FIXED: mode 6 (loyalty card) DAO returns a Response with a null listObject -> NullPointerException, because the production code calls .size() BEFORE checking != null, not after")
    void loyaltyCard_nullListObject_throwsDueToBackwardsNullCheckOrder() throws Exception {
        // NOTE (characterization, not a fix): production line is
        // `if (loyalityCardResponse.getListObject().size() > 0 && loyalityCardResponse.getListObject() != null)`
        // -- .size() is called BEFORE the null-check, not after, so a null listObject
        // throws NPE before the null-check would ever have caught it.
        PaymentReceiptDetailsDtosList p = payment(6, 100.0);
        p.setCardNumber("CARD-1");
        p.setCreatedBy(99);
        when(iPatientLoyaltyCardMapperDao.getPatientDetailsByLoyaltiCardNo(any()))
                .thenReturn(new Response<>("success", null, null, null, null));

        assertThatThrownBy(() -> invoke(request(p), sale()))
                .hasCauseInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("mode 2 (cheque): bank/cheque-number/cheque-date fields set correctly")
    void chequeMode_setsFieldsCorrectly() throws Exception {
        PaymentReceiptDetailsDtosList p = payment(2, 100.0);
        p.setBankId(7);
        p.setChequeNumber("CHQ-001");
        p.setChequeDate("15-07-2026 10:00:00");

        List<SalePaymentMode> result = invoke(request(p), sale());

        assertThat(result).hasSize(1);
        SalePaymentMode mode = result.get(0);
        assertThat(mode.getBankMaster()).isNotNull();
        assertThat(mode.getCheckNumber()).isEqualTo("CHQ-001");
        assertThat(mode.getChequeDate()).isNotNull();
    }

    @Test
    @DisplayName("mode 8 (e-transfer): transactionNo/date/transactionTypeId/bank fields set correctly")
    void eTransferMode_setsFieldsCorrectly() throws Exception {
        PaymentReceiptDetailsDtosList p = payment(8, 100.0);
        p.setTransactionNo("ET-001");
        p.setTransTransactionDate("15-07-2026 10:00:00");
        p.setTransactionTypeId(3);
        p.setBankId(9);

        List<SalePaymentMode> result = invoke(request(p), sale());

        assertThat(result).hasSize(1);
        SalePaymentMode mode = result.get(0);
        assertThat(mode.getTransactionNo()).isEqualTo("ET-001");
        assertThat(mode.getTransTransactionDate()).isNotNull();
        assertThat(mode.getTransactionTypeId()).isEqualTo(3);
        assertThat(mode.getBankMaster()).isNotNull();
    }
}
