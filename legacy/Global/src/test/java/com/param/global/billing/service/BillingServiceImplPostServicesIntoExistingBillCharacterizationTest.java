package com.param.global.billing.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.param.billing.global.transaction.model.BillingMaster;
import com.param.global.billing.dao.IBillingDao;
import com.param.global.dao.IOrderDetailsMasterDao;
import com.param.global.dao.IPatientSponsorDetailsDao;
import com.param.global.dto.AdmissionsForBatchProcessDto;
import com.param.global.model.OrderDetailsMaster;
import com.param.global.pharmacy.dao.IWalkInSalesDao;
import com.param.global.service.IOrderDetailsMasterService;

/**
 * Characterization tests (current behavior, not verified-correct behavior) for
 * {@code BillingServiceImpl.postServicesIntoExistingBill} -- Pharmacy batch 2, the
 * downstream computation triggered by {@code WalkInSalesController.saveBill} when a
 * walk-in sale is added to an existing bill (research this session judged it the
 * single most financially substantial of Pharmacy's 14 live cross-module financial
 * calls). Scoped to the per-order-detail accumulation arithmetic (totalAmount /
 * taxAmount / netAmount / selfPayable / creditPayable) and the final
 * {@code BillingMaster} aggregate update + status derivation.
 *
 * Deliberately NOT covered in this batch, deferred to a follow-up batch: the payee
 * priority/sponsor-consolidation branch (production lines ~371-451, reached only when
 * an order-detail's payee list is non-empty -- every fixture here uses an empty payee
 * list to isolate the accumulation arithmetic from that separate concern) and the EHC
 * multi-service-package branch (isPackage()==true plus visitTypeId==OP).
 */
@ExtendWith(MockitoExtension.class)
@SuppressWarnings({ "rawtypes", "unchecked" })
@DisplayName("BillingServiceImpl.postServicesIntoExistingBill -- accumulation + bill-status characterization")
class BillingServiceImplPostServicesIntoExistingBillCharacterizationTest {

    @Mock private IBillingDao iBillingDao;
    @Mock private IOrderDetailsMasterService iOrderDetailsMasterService;
    @Mock private IOrderDetailsMasterDao iOrderDetailsMasterDao;
    @Mock private IPatientSponsorDetailsDao iPatientSponsorDetailsDao;
    @Mock private IWalkInSalesDao walkInSalesDao;

    private BillingServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new BillingServiceImpl();
        service.iBillingDao = iBillingDao;
        service.iOrderDetailsMasterService = iOrderDetailsMasterService;
        setPrivate(service, "iOrderDetailsMasterDao", iOrderDetailsMasterDao);
        service.iPatientSponsorDetailsDao = iPatientSponsorDetailsDao;
        service.WalkInSalesDao = walkInSalesDao;

        lenient().when(iOrderDetailsMasterDao.getOrderPayeeDetailsByOrderDetailsId(anyInt(), isNull()))
                .thenReturn(new com.param.global.common.Response<>("success", null, null, Collections.emptyList(), null));
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

    /** Builds a mocked OrderDetailsMaster with the given financial fields; everything else defaults safely. */
    private OrderDetailsMaster orderDetail(int orderDetailsId, BigDecimal ordTotalAmt, BigDecimal taxAmount,
            BigDecimal netAmount, BigDecimal selfPayable, BigDecimal creditPayable, BigDecimal packageConsumptionAmt) {
        OrderDetailsMaster m = org.mockito.Mockito.mock(OrderDetailsMaster.class);
        when(m.getOrderDetailsId()).thenReturn(orderDetailsId);
        when(m.getServiceId()).thenReturn(55);
        when(m.getServiceAmt()).thenReturn(new BigDecimal("100.00"));
        when(m.getOrderQty()).thenReturn(2);
        when(m.getOrdTotalAmt()).thenReturn(ordTotalAmt);
        when(m.getOrgUnitId()).thenReturn(0);
        when(m.getOrgId()).thenReturn(1);
        when(m.getCreatedBy()).thenReturn(99);
        when(m.getUpdatedBy()).thenReturn(99);
        when(m.getCreatedDate()).thenReturn(1000L);
        when(m.getUpdatedDate()).thenReturn(1000L);
        when(m.getTaxId()).thenReturn(5);
        when(m.getTaxPer()).thenReturn(new BigDecimal("5.00"));
        when(m.getTaxAmount()).thenReturn(taxAmount);
        when(m.getNetAmount()).thenReturn(netAmount);
        when(m.getSelfPayable()).thenReturn(selfPayable);
        when(m.getCreditPayable()).thenReturn(creditPayable);
        when(m.getPayeeId()).thenReturn(1);
        when(m.getPackageConsumptionAmt()).thenReturn(packageConsumptionAmt);
        when(m.isPackage()).thenReturn(false); // EHC branch out of scope this batch, see class javadoc
        return m;
    }

    private void stubOrderDetail(int orderDetailsId, Integer billId, OrderDetailsMaster orderDetailsMasterDto,
            List<com.param.global.dto.OrderDetailsPackageMapperDto> packageMapperList) throws Exception {
        when(iOrderDetailsMasterDao.getOrderDetailsByIdForPayeeUpdate(orderDetailsId)).thenReturn(orderDetailsMasterDto);
        when(iOrderDetailsMasterDao.getOrderDetailsPackageMapperByOrderDetailsId(orderDetailsId))
                .thenReturn(new com.param.global.common.Response<>("success", null, null, packageMapperList, null));

        AdmissionsForBatchProcessDto visit = new AdmissionsForBatchProcessDto();
        visit.setBillId(billId);
        visit.setVisitTypeId(1);
        when(iOrderDetailsMasterDao.getVisitDetailsForRenderingService(orderDetailsId))
                .thenReturn(new com.param.global.common.Response<>("success", visit));
        lenient().when(iOrderDetailsMasterDao.getOrderPayeeDetailsByOrderDetailsId(eq(orderDetailsId), isNull()))
                .thenReturn(new com.param.global.common.Response<>("success", null, null, Collections.emptyList(), null));
    }

    private BillingMaster existingBillingMaster(double netAmount, double gst, double totalBillAmount,
            double outstanding, double selfPayable, double creditPayable) {
        BillingMaster b = new BillingMaster();
        b.setNetAmount(netAmount);
        b.setGst(gst);
        b.setTotalBillAmount(totalBillAmount);
        b.setOutstanding(outstanding);
        b.setSelfPayable(selfPayable);
        b.setCreditPayable(creditPayable);
        return b;
    }

    @Test
    @DisplayName("single non-package order-detail: totalAmount uses ordTotalAmt; existing bill totals are ACCUMULATED (+=), not overwritten; billStatusId=2 when outstanding != netAmount")
    void singleNonPackageOrderDetail_accumulatesOntoExistingBillTotals_statusTwo() throws Exception {
        OrderDetailsMaster od = orderDetail(7001, new BigDecimal("500.00"), new BigDecimal("25.00"),
                new BigDecimal("525.00"), new BigDecimal("525.00"), new BigDecimal("0.00"), null);
        stubOrderDetail(7001, 900, od, Collections.emptyList());

        BillingMaster existing = existingBillingMaster(1000.0, 50.0, 2000.0, 800.0, 1000.0, 0.0);
        when(iBillingDao.getBillMasterObject(900)).thenReturn(existing);

        service.postServicesIntoExistingBill(Arrays.asList(7001));

        ArgumentCaptor<BillingMaster> captor = ArgumentCaptor.forClass(BillingMaster.class);
        verify(iBillingDao).updateBillMaster(captor.capture());
        BillingMaster updated = captor.getValue();

        assertThat(updated.getNetAmount()).isEqualTo(1525.0);   // 1000 + 525 (accumulated, not replaced)
        assertThat(updated.getGst()).isEqualTo(75.0);            // 50 + 25
        assertThat(updated.getTotalBillAmount()).isEqualTo(2500.0); // 2000 + 500 (ordTotalAmt, non-package formula)
        assertThat(updated.getOutstanding()).isEqualTo(1325.0);  // 800 + 525 -- note: outstanding is bumped by
                                                                   // netAmount, NOT totalAmount; pinned as-is.
        assertThat(updated.getSelfPayable()).isEqualTo(1525.0);  // 1000 + 525
        assertThat(updated.getCreditPayable()).isEqualTo(0.0);
        // outstanding(1325) != netAmount(1525) and outstanding != 0 -> falls to the "else" branch
        assertThat(updated.getBillStatusId()).isEqualTo(2);
    }

    @Test
    @DisplayName("package-consumption branch: totalAmount = packageMapper.serviceAmount * orderQty (NOT ordTotalAmt); billStatusId=1 when outstanding exactly equals netAmount")
    void packageConsumptionBranch_usesServiceAmountTimesQty_statusOne() throws Exception {
        OrderDetailsMaster od = orderDetail(7002, new BigDecimal("999.00") /* deliberately wrong if used */,
                new BigDecimal("10.00"), new BigDecimal("210.00"), new BigDecimal("210.00"), new BigDecimal("0.00"),
                new BigDecimal("50.00") /* > 0 -> package branch */);

        com.param.global.dto.OrderDetailsPackageMapperDto pkgLine = new com.param.global.dto.OrderDetailsPackageMapperDto();
        pkgLine.setIsPackageService('N');
        pkgLine.setStatus('A');
        pkgLine.setServiceAmount(new BigDecimal("100.00")); // * orderQty(2) = 200.00
        stubOrderDetail(7002, 901, od, new ArrayList<>(List.of(pkgLine)));

        BillingMaster existing = existingBillingMaster(500.0, 20.0, 500.0, 500.0, 500.0, 0.0);
        when(iBillingDao.getBillMasterObject(901)).thenReturn(existing);

        service.postServicesIntoExistingBill(Arrays.asList(7002));

        ArgumentCaptor<BillingMaster> captor = ArgumentCaptor.forClass(BillingMaster.class);
        verify(iBillingDao).updateBillMaster(captor.capture());
        BillingMaster updated = captor.getValue();

        assertThat(updated.getTotalBillAmount()).isEqualTo(700.0); // 500 + (100 * 2), NOT 500 + 999
        assertThat(updated.getNetAmount()).isEqualTo(710.0);       // 500 + 210
        assertThat(updated.getOutstanding()).isEqualTo(710.0);     // 500 + 210 -- equals netAmount exactly
        // outstanding(710) == netAmount(710) and selfPayable(710) > 0 -> billStatusId=1
        assertThat(updated.getBillStatusId()).isEqualTo(1);
    }

    @Test
    @DisplayName("outstanding lands exactly at 0 after accumulation -> billStatusId=3")
    void outstandingZeroAfterAccumulation_statusThree() throws Exception {
        OrderDetailsMaster od = orderDetail(7003, new BigDecimal("300.00"), new BigDecimal("15.00"),
                new BigDecimal("315.00"), new BigDecimal("315.00"), new BigDecimal("0.00"), null);
        stubOrderDetail(7003, 902, od, Collections.emptyList());

        // Starting outstanding is deliberately negative so that +315 lands exactly at 0 --
        // pins the literal formula/threshold, not a claim about a realistic starting value.
        BillingMaster existing = existingBillingMaster(0.0, 0.0, 0.0, -315.0, 0.0, 0.0);
        when(iBillingDao.getBillMasterObject(902)).thenReturn(existing);

        service.postServicesIntoExistingBill(Arrays.asList(7003));

        ArgumentCaptor<BillingMaster> captor = ArgumentCaptor.forClass(BillingMaster.class);
        verify(iBillingDao).updateBillMaster(captor.capture());
        assertThat(captor.getValue().getOutstanding()).isEqualTo(0.0);
        assertThat(captor.getValue().getBillStatusId()).isEqualTo(3);
    }

    @Test
    @DisplayName("two order-details on the SAME bill: contributions from BOTH iterations are summed, not last-one-wins")
    void twoOrderDetailsSameBill_bothContributionsSummed() throws Exception {
        OrderDetailsMaster od1 = orderDetail(7004, new BigDecimal("100.00"), new BigDecimal("5.00"),
                new BigDecimal("105.00"), new BigDecimal("105.00"), new BigDecimal("0.00"), null);
        OrderDetailsMaster od2 = orderDetail(7005, new BigDecimal("200.00"), new BigDecimal("10.00"),
                new BigDecimal("210.00"), new BigDecimal("210.00"), new BigDecimal("0.00"), null);
        stubOrderDetail(7004, 903, od1, Collections.emptyList());
        stubOrderDetail(7005, 903, od2, Collections.emptyList());

        BillingMaster existing = existingBillingMaster(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
        when(iBillingDao.getBillMasterObject(903)).thenReturn(existing);

        service.postServicesIntoExistingBill(Arrays.asList(7004, 7005));

        ArgumentCaptor<BillingMaster> captor = ArgumentCaptor.forClass(BillingMaster.class);
        verify(iBillingDao).updateBillMaster(captor.capture());
        BillingMaster updated = captor.getValue();

        // Confirms this method does NOT have this session's recurring "reassign instead of
        // accumulate" bug shape -- both order-details' amounts are present in the total, not
        // just the second (last) one's.
        assertThat(updated.getNetAmount()).isEqualTo(315.0);        // 105 + 210
        assertThat(updated.getTotalBillAmount()).isEqualTo(300.0);  // 100 + 200
        assertThat(updated.getGst()).isEqualTo(15.0);                // 5 + 10
    }

    @Test
    @DisplayName("NOTED, NOT FIXED: two order-details spanning DIFFERENT bills -- only the LAST order-detail's bill gets the final BillingMaster update, but it receives BOTH order-details' summed amounts")
    void twoOrderDetailsDifferentBills_onlyLastBillUpdated_withBothAmountsSummed() throws Exception {
        // billId and the running totals (taxAmount/netAmount/totalAmount/selfPayable/creditPayable)
        // are both method-scoped, not per-bill-scoped, in the production source. This test pins
        // that exact structural behavior; it is NOT confirmed reachable via the one call site this
        // session traced (WalkInSalesController.saveBill passes ids from a single walk-in sale,
        // which in ordinary usage resolve to one bill) -- logged as a structural finding for the
        // owner's awareness given the method is public/reusable, not elevated to an urgent finding
        // absent evidence of it firing in practice.
        OrderDetailsMaster od1 = orderDetail(7006, new BigDecimal("100.00"), new BigDecimal("5.00"),
                new BigDecimal("105.00"), new BigDecimal("105.00"), new BigDecimal("0.00"), null);
        OrderDetailsMaster od2 = orderDetail(7007, new BigDecimal("200.00"), new BigDecimal("10.00"),
                new BigDecimal("210.00"), new BigDecimal("210.00"), new BigDecimal("0.00"), null);
        stubOrderDetail(7006, 904 /* bill A */, od1, Collections.emptyList());
        stubOrderDetail(7007, 905 /* bill B, the LAST one in the list */, od2, Collections.emptyList());

        BillingMaster billB = existingBillingMaster(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
        when(iBillingDao.getBillMasterObject(905)).thenReturn(billB);

        service.postServicesIntoExistingBill(Arrays.asList(7006, 7007));

        // Only bill B (905, the last order-detail's bill) is ever fetched/updated -- bill A (904)
        // is never touched by getBillMasterObject/updateBillMaster at all.
        verify(iBillingDao).getBillMasterObject(905);
        verify(iBillingDao, org.mockito.Mockito.never()).getBillMasterObject(904);

        ArgumentCaptor<BillingMaster> captor = ArgumentCaptor.forClass(BillingMaster.class);
        verify(iBillingDao).updateBillMaster(captor.capture());
        // Bill B's aggregate ends up carrying BOTH order-details' amounts (105+210=315), even
        // though order-detail 7006 actually belongs to bill A.
        assertThat(captor.getValue().getNetAmount()).isEqualTo(315.0);
    }
}
