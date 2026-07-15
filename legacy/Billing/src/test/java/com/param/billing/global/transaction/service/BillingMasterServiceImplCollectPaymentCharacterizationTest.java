package com.param.billing.global.transaction.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.param.billing.global.dto.BillPaymentCollectionReqDto;
import com.param.billing.global.transaction.dao.IBillPayeeDetailsDao;
import com.param.billing.global.transaction.dao.IBillStatusMapperDao;
import com.param.billing.global.transaction.dao.IBillingMasterDao;
import com.param.global.billing.dao.IBillingDao;
import com.param.global.billing.dto.BillPayeeDetailsResDto;
import com.param.global.billing.dto.PaymentRecieptMasterDto;
import com.param.global.common.IBillingStatusMasterConstant;
import com.param.global.dao.IUnitApplicationBillingConfigurationDao;
import com.param.global.common.Response;
import com.param.global.dto.BillingMasterDto;
import com.param.global.dto.UnitApplicationConfigurationDto;

/**
 * WAVE-1 CHARACTERIZATION TEST -- DRAFT, NOT YET REVIEWED OR APPROVED.
 *
 * Targets {@link BillingMasterServiceImpl#collectPayment}, entry point #3 in
 * docs/DECISION_NEEDED_ShadowTestHarnessApproach.md Section 1 -- the literal money-collection entry
 * point, and where RISK-1's dueAmount/totalSelfDueAmount/totalCreditDueAmount double accumulation
 * (docs/AUDIT_BillingRisk.md lines 1879-1886, 1947-1949, 1977, 2027-2029) lives.
 *
 * SCOPE, decided and disclosed rather than silently assumed: this method is ~480 lines and, beyond
 * collecting a payment, also orchestrates SMS notifications (3 separate trigger branches), an
 * external MyLife/Global-Scheduling sync integration (raw HTTP calls via RestTemplate), patient
 * loyalty-card consumption, appointment/encounter status updates, and per-payment-mode integration
 * side effects (Mpesa, Pesapal, RazorPay, CCAvenue, POS machine). This test suite covers the core
 * money arithmetic and the validation gates that guard it -- the part RISK-1 specifically flagged and
 * the part this entry point exists for. It deliberately does NOT cover the SMS/sync/loyalty/
 * appointment orchestration -- those are real, un-pinned code paths, not silently assumed safe.
 * Fixture values (isInterimOrFinalBill=0, appointmentId=null, patientCouponId=null, empty payment-
 * receipt-details list, patientId=null) are deliberately chosen to route execution around those
 * branches so this suite can stay focused, not to hide them.
 *
 * DIVERGENCE FOUND WHILE WRITING THIS TEST -- FIXED 2026-07-15
 * (docs/DECISION_NEEDED_TriagePendingFindingsFixes.md, finding 3, owner-approved). Previously: two of
 * the three SMS-trigger branches null-guard `isInterimOrFinalBill` before comparing it to a literal
 * (`reqDto.getIsInterimOrFinalBill() != null && ... == 2`), but the third did not
 * (`reqDto.getIsInterimOrFinalBill() == 1`, no null check) -- a request with isInterimOrFinalBill left
 * null threw a real NullPointerException (auto-unboxing null) on that one branch, unlike its two
 * siblings; because the whole method is wrapped in a catch-all that rethrows as BillingException, the
 * caller saw a generic billing exception, not a distinguishable NPE. Now: the third branch has the
 * same `!= null &&` guard as the other two, copied verbatim -- see
 * UnguardedNullCheck#noLongerThrowsWhenIsInterimOrFinalBillIsNull below, which now pins the fixed
 * (graceful-skip) behavior instead of the crash.
 *
 * Per Decision 4 (owner-approved): assertions use exact equality, not RULES.md's $0.01 tolerance.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("BillingMasterServiceImpl.collectPayment -- core arithmetic + validation-gate characterization (current behavior, not verified-correct behavior)")
class BillingMasterServiceImplCollectPaymentCharacterizationTest {

    private static final Integer SELF_PAYEE_ID = 100;
    private static final Integer CREDIT_PAYEE_ID = 200;

    @Mock private IBillingMasterDao billingMasterDao;
    @Mock private IBillingDao iBillingDao;
    @Mock private IUnitApplicationBillingConfigurationDao iUnitApplicationBillingConfigurationDao;
    @Mock private IPaymentRecieptMasterService iPaymentRecieptMasterService;
    @Mock private IBillStatusMapperDao iBillStatusMapperDao;
    @Mock private IBillPayeeDetailsDao iBillPayeeDetailsDao;
    @Mock private IPaymentDepositConsumptionService iPaymentDepositConsumptionService;
    @Mock private SessionFactory sessionFactory;

    @InjectMocks
    private BillingMasterServiceImpl service;

    @BeforeEach
    void setUp() {
        // sessionFactory.getCurrentSession().createSQLQuery(...).setParameter(...).uniqueResult() is
        // called UNCONDITIONALLY (the isSyncScheduling lookup, no if-guard around the call itself) --
        // stubbed to return false so the MyLife/Global-Scheduling sync block (its own large,
        // out-of-scope side flow) never activates. lenient() because not every test reaches this line
        // (the validation-gate tests return before it).
        Session session = mock(Session.class);
        SQLQuery query = mock(SQLQuery.class, RETURNS_DEEP_STUBS);
        lenient().when(sessionFactory.getCurrentSession()).thenReturn(session);
        lenient().when(session.createSQLQuery(anyString())).thenReturn(query);
        lenient().when(query.setParameter(anyString(), any())).thenReturn(query);
        lenient().when(query.uniqueResult()).thenReturn(Boolean.FALSE);
    }

    private BillPaymentCollectionReqDto request(Integer payeeId, Double amount, Double payeeDueAmount) {
        BillPaymentCollectionReqDto req = new BillPaymentCollectionReqDto();
        req.setBillingMasterId(1);
        req.setOrgnizationId(1);
        req.setUnitId(1);
        req.setPayeeId(payeeId);
        req.setAmount(amount);
        req.setPayeeDueAmount(payeeDueAmount);
        req.setTotalSelfPayable(amount);
        req.setTotalCreditPayable(0.0);
        req.setIsInterimOrFinalBill(0); // 0 skips all 3 SMS branches without hitting the unguarded-null NPE
        req.setVisitTypeId(1);
        req.setAppointmentId(null); // skips the appointment/encounter status-update block entirely
        req.setEncounterId(null);
        req.setAdmissionId(null); // skips the financial-clearance-flag update
        req.setPatientId(null); // skips the uuid lookup query
        req.setPatientCouponId(null); // skips the coupon slot-status update
        req.setDisallowedAmount(null); // skips the disallowed-amount update
        req.setSignetureImage(null);
        req.setPaymentReceiptDetailsDtosList(new ArrayList<>()); // empty, not null -- skips the Mpesa/Pesapal/RazorPay/CCAvenue/POS loop
        req.setPaymentDepositDetailsList(null);
        req.setReceivedFrom("front-desk");
        req.setCreatedBy(1);
        req.setUpdatedBy(1);
        return req;
    }

    @SuppressWarnings("unchecked")
    private Response<Object, java.io.Serializable> mockResponse() {
        return mock(Response.class);
    }

    private UnitApplicationConfigurationDto config(Integer selfPayeeId, Character preOrPostBilling,
            Character nursingAssessment, Character kioskRequired) {
        UnitApplicationConfigurationDto dto = new UnitApplicationConfigurationDto();
        dto.setSelfPayeeId(selfPayeeId);
        dto.setIsPreOrPostBilling(preOrPostBilling);
        dto.setIsNursingAssessment(nursingAssessment);
        dto.setIsKioskRequired(kioskRequired);
        return dto;
    }

    @SuppressWarnings("unchecked")
    private void stubAppConfig(UnitApplicationConfigurationDto configDto) {
        Response<Object, java.io.Serializable> configRes = mockResponse();
        when(configRes.getObject()).thenReturn(configDto);
        when(iUnitApplicationBillingConfigurationDao.getUnitApplicationBillingConfigByOrgUnit(any()))
                .thenReturn(configRes);
    }

    @SuppressWarnings("unchecked")
    private void stubPayeeDetails(List<BillPayeeDetailsResDto> payees) {
        Response<Object, java.io.Serializable> payeeRes = mockResponse();
        when(payeeRes.getListObject()).thenReturn((List) payees);
        when(iBillingDao.getBillPayeeDetailsByBillId(any())).thenReturn(payeeRes);
    }

    private BillingMasterDto billingMaster(Character billCancelled, Double outstanding) {
        BillingMasterDto dto = new BillingMasterDto();
        dto.setBillCancelled(billCancelled);
        dto.setOutstanding(outstanding);
        dto.setTotalBillAmount(1000.0);
        dto.setNetAmount(1000.0);
        dto.setConcessionAmount(0.0);
        dto.setCreatedBy(1);
        dto.setOrganisationId(1);
        dto.setUnitId(1);
        return dto;
    }

    @SuppressWarnings("unchecked")
    private void stubBillMaster(BillingMasterDto billingMasterDto) {
        Response<Object, java.io.Serializable> billRes = mockResponse();
        when(billRes.getStatus()).thenReturn("success");
        when(billRes.getObject()).thenReturn(billingMasterDto);
        when(billingMasterDao.getBillMasterById(any())).thenReturn(billRes);
    }

    @SuppressWarnings("unchecked")
    private void stubSavePaymentReciept(Double resultingDueAmount, Integer receiptId, String receiptNo) {
        Response<Object, java.io.Serializable> saveRes = mockResponse();
        // lenient(): shared helper stubs the full Response surface, but the UnguardedNullCheck test
        // deliberately crashes mid-method (before getCode()/getListObject() are ever read) -- that's
        // the real behavior being pinned, not a reason to trim this helper down for one test.
        lenient().when(saveRes.getObject()).thenReturn(resultingDueAmount);
        lenient().when(saveRes.getMessage()).thenReturn(String.valueOf(receiptId));
        lenient().when(saveRes.getCode()).thenReturn(receiptNo);
        lenient().when(saveRes.getListObject()).thenReturn(new ArrayList<>()); // empty -- skips the loyalty-consumption loop, iMailingService never invoked
        lenient().when(iPaymentRecieptMasterService.savePaymentReciept(any(PaymentRecieptMasterDto.class), any(Double.class), anyDouble()))
                .thenReturn(saveRes);
    }

    @Nested
    @DisplayName("validation gates, checked in the order the method actually checks them")
    class ValidationGates {

        @Test
        @DisplayName("returns an error, does not proceed, when application configuration is entirely missing")
        void returnsError_whenApplicationConfigMissing() {
            Response<Object, java.io.Serializable> emptyConfigRes = mockResponse();
            when(emptyConfigRes.getObject()).thenReturn(null);
            when(iUnitApplicationBillingConfigurationDao.getUnitApplicationBillingConfigByOrgUnit(any()))
                    .thenReturn(emptyConfigRes);

            Response<?, ?> result = service.collectPayment(request(SELF_PAYEE_ID, 100.0, 100.0));

            assertThat(result.getMessage()).isEqualTo("Application Configuration is yet to set !!");
        }

        @Test
        @DisplayName("returns an error when selfPayeeId is not configured")
        void returnsError_whenSelfPayeeNotDefined() {
            stubAppConfig(config(null, 'Y', 'Y', 'N'));

            Response<?, ?> result = service.collectPayment(request(SELF_PAYEE_ID, 100.0, 100.0));

            assertThat(result.getMessage()).isEqualTo("Self Payee is not defined !!");
        }

        @Test
        @DisplayName("returns an error when isPreOrPostBilling is not configured, even with selfPayeeId defined")
        void returnsError_whenPreOrPostBillingNotDefined() {
            stubAppConfig(config(SELF_PAYEE_ID, null, 'Y', 'N'));

            Response<?, ?> result = service.collectPayment(request(SELF_PAYEE_ID, 100.0, 100.0));

            assertThat(result.getMessage()).isEqualTo("Pre billing post billing is not defined !!");
        }

        @Test
        @DisplayName("returns an error when isNursingAssessment is not configured")
        void returnsError_whenNursingAssessmentNotDefined() {
            stubAppConfig(config(SELF_PAYEE_ID, 'Y', null, 'N'));

            Response<?, ?> result = service.collectPayment(request(SELF_PAYEE_ID, 100.0, 100.0));

            assertThat(result.getMessage()).isEqualTo("Is Nursing Assessment Required is not defined !!");
        }

        @Test
        @DisplayName("returns an error when isKioskRequired is not configured")
        void returnsError_whenKioskRequiredNotDefined() {
            stubAppConfig(config(SELF_PAYEE_ID, 'Y', 'Y', null));

            Response<?, ?> result = service.collectPayment(request(SELF_PAYEE_ID, 100.0, 100.0));

            assertThat(result.getMessage()).isEqualTo("Is Kiosk Required is not defined !!");
        }

        @Test
        @DisplayName("returns an error when the bill is already cancelled")
        void returnsError_whenBillAlreadyCancelled() {
            stubAppConfig(config(SELF_PAYEE_ID, 'Y', 'Y', 'N'));
            stubPayeeDetails(Arrays.asList());
            stubBillMaster(billingMaster('Y', 500.0));

            Response<?, ?> result = service.collectPayment(request(SELF_PAYEE_ID, 100.0, 100.0));

            assertThat(result.getMessage()).isEqualTo("Bill is already Cancelled!!");
        }

        @Test
        @DisplayName("returns an error when the client-supplied outstanding no longer matches the server's stored value")
        void returnsError_whenOutstandingMismatch() {
            stubAppConfig(config(SELF_PAYEE_ID, 'Y', 'Y', 'N'));
            stubPayeeDetails(Arrays.asList());
            stubBillMaster(billingMaster('N', 500.0));

            BillPaymentCollectionReqDto req = request(SELF_PAYEE_ID, 100.0, 100.0);
            req.setOutstanding(499.0); // client's stale/mismatched value vs. server's 500.0

            Response<?, ?> result = service.collectPayment(req);

            assertThat(result.getMessage()).isEqualTo("Selected bill is already in use...!!");
        }
    }

    @Nested
    @DisplayName("core arithmetic -- the self/credit due-amount split and combination RISK-1 flags")
    class CoreArithmetic {

        @Test
        @DisplayName("self-payee payment: the service's returned due amount is combined with the OTHER payees' (credit) outstanding due")
        void selfPayeePayment_combinesWithCreditDue() {
            stubAppConfig(config(SELF_PAYEE_ID, 'Y', 'Y', 'N'));
            BillPayeeDetailsResDto selfPayee = new BillPayeeDetailsResDto();
            selfPayee.setPayeeId(SELF_PAYEE_ID);
            selfPayee.setDueAmount(100.0);
            BillPayeeDetailsResDto creditPayee = new BillPayeeDetailsResDto();
            creditPayee.setPayeeId(CREDIT_PAYEE_ID);
            creditPayee.setDueAmount(300.0);
            stubPayeeDetails(Arrays.asList(selfPayee, creditPayee));
            stubBillMaster(billingMaster('N', 400.0));
            stubSavePaymentReciept(0.0, 555, "REC-001"); // self side fully paid off by this payment

            BillPaymentCollectionReqDto req = request(SELF_PAYEE_ID, 100.0, 100.0);

            ArgumentCaptor<BillingMasterDto> updateCaptor = ArgumentCaptor.forClass(BillingMasterDto.class);

            service.collectPayment(req);

            verify(billingMasterDao).updateBillSummaryAmount(updateCaptor.capture());
            // 0.0 (self side settled by this payment) + 300.0 (the credit payee's own, untouched due) = 300.0
            assertThat(updateCaptor.getValue().getOutstanding()).isEqualTo(300.0);
        }

        @Test
        @DisplayName("credit-payee payment: the service's returned due amount is combined with the OTHER payees' (self) outstanding due")
        void creditPayeePayment_combinesWithSelfDue() {
            stubAppConfig(config(SELF_PAYEE_ID, 'Y', 'Y', 'N'));
            BillPayeeDetailsResDto selfPayee = new BillPayeeDetailsResDto();
            selfPayee.setPayeeId(SELF_PAYEE_ID);
            selfPayee.setDueAmount(150.0);
            BillPayeeDetailsResDto creditPayee = new BillPayeeDetailsResDto();
            creditPayee.setPayeeId(CREDIT_PAYEE_ID);
            creditPayee.setDueAmount(300.0);
            stubPayeeDetails(Arrays.asList(selfPayee, creditPayee));
            stubBillMaster(billingMaster('N', 450.0));
            stubSavePaymentReciept(0.0, 556, "REC-002");

            BillPaymentCollectionReqDto req = request(CREDIT_PAYEE_ID, 300.0, 300.0);

            ArgumentCaptor<BillingMasterDto> updateCaptor = ArgumentCaptor.forClass(BillingMasterDto.class);

            service.collectPayment(req);

            verify(billingMasterDao).updateBillSummaryAmount(updateCaptor.capture());
            // 0.0 (credit side settled) + 150.0 (the self payee's own, untouched due) = 150.0
            assertThat(updateCaptor.getValue().getOutstanding()).isEqualTo(150.0);
        }

        @Test
        @DisplayName("bill status is ONHOLD when the combined outstanding is still positive, SETTLED (and the sale-table status also updated) when it reaches zero")
        void billStatus_dependsOnWhetherOutstandingIsFullySettled() {
            stubAppConfig(config(SELF_PAYEE_ID, 'Y', 'Y', 'N'));
            BillPayeeDetailsResDto selfPayee = new BillPayeeDetailsResDto();
            selfPayee.setPayeeId(SELF_PAYEE_ID);
            selfPayee.setDueAmount(100.0);
            stubPayeeDetails(Arrays.asList(selfPayee));
            stubBillMaster(billingMaster('N', 100.0));
            stubSavePaymentReciept(0.0, 557, "REC-003"); // fully settled, no other payees

            service.collectPayment(request(SELF_PAYEE_ID, 100.0, 100.0));

            ArgumentCaptor<BillingMasterDto> updateCaptor = ArgumentCaptor.forClass(BillingMasterDto.class);
            verify(billingMasterDao).updateBillSummaryAmount(updateCaptor.capture());
            assertThat(updateCaptor.getValue().getBillStatusId()).isEqualTo(IBillingStatusMasterConstant.SETTLED);
            verify(billingMasterDao).updateSaleTableBillstatus(any());
        }

        @Test
        @DisplayName("bill stays ONHOLD, and updateSaleTableBillstatus is never called, when a positive balance remains")
        void billStaysOnHold_whenBalanceRemains() {
            stubAppConfig(config(SELF_PAYEE_ID, 'Y', 'Y', 'N'));
            BillPayeeDetailsResDto selfPayee = new BillPayeeDetailsResDto();
            selfPayee.setPayeeId(SELF_PAYEE_ID);
            selfPayee.setDueAmount(400.0);
            stubPayeeDetails(Arrays.asList(selfPayee));
            stubBillMaster(billingMaster('N', 400.0));
            stubSavePaymentReciept(200.0, 558, "REC-004"); // partial payment, 200.0 still owed

            service.collectPayment(request(SELF_PAYEE_ID, 200.0, 400.0));

            ArgumentCaptor<BillingMasterDto> updateCaptor = ArgumentCaptor.forClass(BillingMasterDto.class);
            verify(billingMasterDao).updateBillSummaryAmount(updateCaptor.capture());
            assertThat(updateCaptor.getValue().getBillStatusId()).isEqualTo(IBillingStatusMasterConstant.ONHOLD);
            verify(billingMasterDao, never()).updateSaleTableBillstatus(any());
        }

        @Test
        @DisplayName("deposit consumption reduces the due amount BEFORE it's passed into savePaymentReciept, rounded to 2 decimal places via Math.round(x*100.0)/100.0")
        void depositConsumption_reducesDueAmountBeforeSaving() {
            stubAppConfig(config(SELF_PAYEE_ID, 'Y', 'Y', 'N'));
            BillPayeeDetailsResDto selfPayee = new BillPayeeDetailsResDto();
            selfPayee.setPayeeId(SELF_PAYEE_ID);
            selfPayee.setDueAmount(1000.0);
            stubPayeeDetails(Arrays.asList(selfPayee));
            stubBillMaster(billingMaster('N', 1000.0));
            stubSavePaymentReciept(0.0, 559, "REC-005");

            Response<Object, java.io.Serializable> consumeRes = mockResponse();
            when(consumeRes.getStatus()).thenReturn("success");
            when(consumeRes.getMessage()).thenReturn("77"); // depositConsumptionId
            when(iPaymentDepositConsumptionService.consumeDeposit(any())).thenReturn(consumeRes);

            BillPaymentCollectionReqDto req = request(SELF_PAYEE_ID, 0.0, 1000.0);
            req.setPaymentDepositDetailsList(Arrays.asList(new com.param.global.billing.dto.PaymentDepositDto()));
            req.setTotalDepositConsumption(333.333); // deliberately not a "nice" decimal

            ArgumentCaptor<PaymentRecieptMasterDto> saveCaptor = ArgumentCaptor.forClass(PaymentRecieptMasterDto.class);
            ArgumentCaptor<Double> dueAmountCaptor = ArgumentCaptor.forClass(Double.class);

            service.collectPayment(req);

            verify(iPaymentRecieptMasterService).savePaymentReciept(saveCaptor.capture(), dueAmountCaptor.capture(), anyDouble());
            verify(iPaymentDepositConsumptionService).consumeDeposit(any());
            // 1000.0 (payeeDueAmount) - 333.333 (totalDepositConsumption) = 666.667, then
            // Math.round(666.667 * 100.0) / 100.0 -- pins the exact current rounding rule (half-up on
            // this positive value, in double), per docs/AUDIT_BillingRisk.md RISK-2.
            double expected = Math.round((1000.0 - 333.333) * 100.0) / 100.0;
            assertThat(dueAmountCaptor.getValue()).isEqualTo(expected);
            assertThat(saveCaptor.getValue().getDepositeConsumptionId()).isEqualTo(77);
        }
    }

    @Nested
    @DisplayName("UnguardedNullCheck -- confirms this branch now mirrors its two siblings' null guard")
    class UnguardedNullCheck {

        @Test
        @DisplayName("FIXED 2026-07-15 (docs/DECISION_NEEDED_TriagePendingFindingsFixes.md, finding 3, owner-approved): no longer throws when isInterimOrFinalBill is null and visitTypeId is not OP -- the branch is simply skipped, and the rest of collectPayment still completes")
        void noLongerThrowsWhenIsInterimOrFinalBillIsNull() {
            stubAppConfig(config(SELF_PAYEE_ID, 'Y', 'Y', 'N'));
            BillPayeeDetailsResDto selfPayee = new BillPayeeDetailsResDto();
            selfPayee.setPayeeId(SELF_PAYEE_ID);
            selfPayee.setDueAmount(100.0);
            stubPayeeDetails(Arrays.asList(selfPayee));
            stubBillMaster(billingMaster('N', 100.0));
            stubSavePaymentReciept(0.0, 560, "REC-006");

            BillPaymentCollectionReqDto req = request(SELF_PAYEE_ID, 100.0, 100.0);
            req.setIsInterimOrFinalBill(null);
            req.setVisitTypeId(2); // anything other than OP (1) -- the branch that used to crash

            // Previously threw a NullPointerException (auto-unboxing a null Integer in `== 1`),
            // wrapped by the method's catch-all into a BillingException. Now the added `!= null &&`
            // guard makes the condition simply false, the branch body (the SMS call) never runs, and
            // collectPayment completes normally -- same as its two already-guarded sibling branches.
            assertThatNoException().isThrownBy(() -> service.collectPayment(req));

            // The rest of the payment-collection flow still completes: the bill summary is still
            // updated, same as any other successful payment.
            verify(billingMasterDao).updateBillSummaryAmount(any(BillingMasterDto.class));
        }
    }
}
