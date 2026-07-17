package com.param.pharmacy.op.service.salesReturn;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.param.common.dto.CommonResponse;
import com.param.entity.model.master.Tax;
import com.param.entity.model.master.TaxComponent;
import com.param.entity.model.pharmacy.Return;
import com.param.entity.model.pharmacy.ReturnDetail;
import com.param.entity.model.pharmacy.SaleDetail;
import com.param.entity.model.pharmacy.SaleReturnDetailTaxComponenet;
import com.param.entity.model.master.TaxDetail;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.service.global.IGlobalService;
import com.param.pharmacy.op.dao.salesReturn.ISalesReturnDao;
import com.param.pharmacy.op.dto.salesReturn.RequestSaleReturnSave;
import com.param.pharmacy.op.dto.salesReturn.RequestSaleReturnSaveItem;
import com.param.service.global.SessionService;

/**
 * Characterization tests (current behavior, not verified-correct behavior) for
 * {@code SalesReturnService.saveSalesReturn} -- Pharmacy batch 3, the third of the
 * remaining 14 live financial calls: {@code GlobalDao.getTaxDetail(...)}, used to
 * back-calculate the tax portion of a refund amount for a sales return line.
 *
 * Scoped to the tax-component calculation loop only (production source lines
 * ~638-690); return-header/bill-number generation and the final response are
 * exercised only as much as needed to reach and complete that loop without an NPE.
 */
@ExtendWith(MockitoExtension.class)
@SuppressWarnings({ "rawtypes", "unchecked" })
@DisplayName("SalesReturnService.saveSalesReturn -- tax-component calculation characterization")
class SalesReturnServiceCharacterizationTest {

    @Mock private ISalesReturnDao salesReturnDao;
    @Mock private IGlobalDao globalDao;
    @Mock private IGlobalService globalService;
    @Mock private SessionService sessionService;

    private SalesReturnService service;

    @BeforeEach
    void setUp() {
        service = new SalesReturnService();
        setPrivate(service, "salesReturnDao", salesReturnDao);
        service.GlobalDao = globalDao;
        service.GlobalService = globalService;
        setPrivate(service, "sessionService", sessionService);

        lenient().when(globalService.getBillNoByGeneratedCode(anyInt(), any(), anyInt())).thenReturn("RET-001");
        // Simulates persistence assigning an id, same as a real Hibernate save would --
        // otherwise returnObject.getId() stays null and the method's own final line
        // (response.setId(returnObject.getId().longValue())) throws, unrelated to what
        // this test is characterizing.
        lenient().when(salesReturnDao.salesReturnDetailsSave(any(Return.class))).thenAnswer(inv -> {
            Return r = inv.getArgument(0);
            r.setId(9001);
            return r;
        });
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

    // ReturnDetail has no public getter for its tax-component list (only the
    // add(...) mutator), so tests read the field directly to verify what was added.
    @SuppressWarnings("unchecked")
    private static List<SaleReturnDetailTaxComponenet> taxComponentsOf(ReturnDetail returnDetail) {
        try {
            java.lang.reflect.Field f = ReturnDetail.class.getDeclaredField("saleReturnDetailTaxComponenets");
            f.setAccessible(true);
            List<SaleReturnDetailTaxComponenet> list = (List<SaleReturnDetailTaxComponenet>) f.get(returnDetail);
            return list != null ? list : List.of();
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    private RequestSaleReturnSaveItem item(long saleDetailId, double refundAmount, double taxPercentage, Double discount) {
        RequestSaleReturnSaveItem item = new RequestSaleReturnSaveItem();
        item.setSaleDetailId(saleDetailId);
        item.setSaleId(1L);
        item.setReturnQuantity(1);
        item.setRefundAmount(refundAmount);
        item.setReturnTaxAmount(0.0);
        item.setReason("damaged");
        item.setTaxPercentage(taxPercentage);
        item.setDiscount(discount);
        return item;
    }

    private RequestSaleReturnSave request(List<RequestSaleReturnSaveItem> items) {
        RequestSaleReturnSave inner = new RequestSaleReturnSave();
        inner.setItem(items);

        RequestSaleReturnSave outer = new RequestSaleReturnSave();
        outer.setStoreId(1);
        outer.setUnitId(0);
        outer.setOrganizationId(1);
        outer.setUserId(99);
        outer.setSaleId(500L);
        outer.setTotalRefundAmount(0.0);
        outer.setStatusId(1);
        outer.setReuquestSaleReturnList(Arrays.asList(inner));
        return outer;
    }

    private SaleDetail saleDetailWithTaxId(int taxId) {
        Tax tax = mock(Tax.class);
        when(tax.getId()).thenReturn(taxId);
        SaleDetail saleDetail = mock(SaleDetail.class);
        when(saleDetail.getId()).thenReturn(1L);
        when(saleDetail.getTax()).thenReturn(tax);
        return saleDetail;
    }

    @Test
    @DisplayName("single tax component: calTaxAmt = (refundAmount * component's own taxPercentage) / (100 + item's taxPercentage); the item's own discount field has NO effect on the result")
    void singleTaxComponent_pinsExactFormula_discountFieldHasNoEffect() throws Exception {
        SaleDetail saleDetail = saleDetailWithTaxId(7);
        when(salesReturnDao.getSaleDetailById(1L, 0)).thenReturn(saleDetail);
        when(salesReturnDao.getTaxId(1L)).thenReturn(7);

        TaxComponent gst = new TaxComponent(50);
        TaxDetail taxDetail = new TaxDetail(gst, 18.0); // component's own percentage: 18%
        when(globalDao.getTaxDetail(7)).thenReturn(Arrays.asList(taxDetail));

        // refundAmount=1180, item.taxPercentage=18 (matches the component's percentage in this
        // test) -> calTaxAmt = (1180 * 18) / (100 + 18) = 21240 / 118 = 180.0 exactly.
        RequestSaleReturnSaveItem it = item(1L, 1180.0, 18.0, 500.0 /* discount: should have no effect */);
        CommonResponse result = service.saveSalesReturn(request(Arrays.asList(it)));

        assertThat(result).isNotNull();
        ArgumentCaptor<Return> captor = ArgumentCaptor.forClass(Return.class);
        org.mockito.Mockito.verify(salesReturnDao).salesReturnDetailsSave(captor.capture());
        ReturnDetail rd = captor.getValue().getReturnDetailList().getFirst();
        List<SaleReturnDetailTaxComponenet> components = taxComponentsOf(rd);
        assertThat(components).hasSize(1);
        assertThat(components.getFirst().getAmount()).isEqualTo(180.0);
        assertThat(components.getFirst().getPercentage()).isEqualTo(18.0);

        // Re-run with a different discount value, same everything else -- confirms getDiscount()
        // (read into a local variable in the production loop, per source) has zero effect on the
        // computed calTaxAmt. Not a fix, just pinning the dead-variable behavior precisely.
        SalesReturnService service2 = new SalesReturnService();
        setPrivate(service2, "salesReturnDao", salesReturnDao);
        service2.GlobalDao = globalDao;
        service2.GlobalService = globalService;
        setPrivate(service2, "sessionService", sessionService);
        RequestSaleReturnSaveItem itNoDiscount = item(1L, 1180.0, 18.0, null);
        CommonResponse result2 = service2.saveSalesReturn(request(Arrays.asList(itNoDiscount)));
        assertThat(result2).isNotNull();
        ArgumentCaptor<Return> captor2 = ArgumentCaptor.forClass(Return.class);
        org.mockito.Mockito.verify(salesReturnDao, org.mockito.Mockito.times(2)).salesReturnDetailsSave(captor2.capture());
        ReturnDetail rd2 = captor2.getValue().getReturnDetailList().getFirst();
        List<SaleReturnDetailTaxComponenet> components2 = taxComponentsOf(rd2);
        assertThat(components2.getFirst().getAmount()).isEqualTo(180.0);
    }

    @Test
    @DisplayName("multiple tax components (e.g. CGST+SGST split): every component in the TaxDetail list gets its OWN SaleReturnDetailTaxComponenet added, not just the first")
    void multipleTaxComponents_allAddedNotJustFirst() throws Exception {
        SaleDetail saleDetail = saleDetailWithTaxId(8);
        when(salesReturnDao.getSaleDetailById(1L, 0)).thenReturn(saleDetail);
        when(salesReturnDao.getTaxId(1L)).thenReturn(8);

        TaxComponent cgst = new TaxComponent(51);
        TaxComponent sgst = new TaxComponent(52);
        TaxDetail cgstDetail = new TaxDetail(cgst, 9.0);
        TaxDetail sgstDetail = new TaxDetail(sgst, 9.0);
        when(globalDao.getTaxDetail(8)).thenReturn(Arrays.asList(cgstDetail, sgstDetail));

        // item.taxPercentage=18 (the combined rate); refundAmount=1180.
        // CGST component: (1180*9)/(100+18) = 90.0 ; SGST component: same math = 90.0.
        RequestSaleReturnSaveItem it = item(1L, 1180.0, 18.0, null);
        service.saveSalesReturn(request(Arrays.asList(it)));

        ArgumentCaptor<Return> captor = ArgumentCaptor.forClass(Return.class);
        org.mockito.Mockito.verify(salesReturnDao).salesReturnDetailsSave(captor.capture());
        ReturnDetail rd = captor.getValue().getReturnDetailList().getFirst();
        List<SaleReturnDetailTaxComponenet> components = taxComponentsOf(rd);
        assertThat(components).hasSize(2);
        List<Double> amounts = new ArrayList<>();
        for (SaleReturnDetailTaxComponenet c : components) {
            amounts.add(c.getAmount());
        }
        assertThat(amounts).containsExactlyInAnyOrder(90.0, 90.0);
    }

    @Test
    @DisplayName("with TWO items in one request, each tax component's ReturnDetail back-reference correctly ends up pointing at its OWN item's ReturnDetail, despite the constructor argument looking otherwise")
    void twoItems_taxComponentBackReferenceCorrectlyPointsAtItsOwnReturnDetail() throws Exception {
        // Initially suspected as a bug while reading the source: each tax component is
        // constructed with `new ReturnDetail(returnObject.getReturnDetailList().getFirst().getId())`
        // as its ReturnDetail argument -- literally "the first return-detail added so far",
        // not the current item's own `returnDetail` local variable. With two or more items
        // that argument looks wrong for every item after the first. A first version of this
        // test asserted exactly that bug and FAILED -- checking why revealed
        // `ReturnDetail.addSaleDetailTaxComponenet(...)` (which is what actually attaches the
        // component, immediately after construction) unconditionally calls
        // `saleReturnDetailTaxComponenet.setReturnDetail(this)`, overwriting whatever the
        // constructor was given. The mismatched constructor argument is discarded, dead
        // construction, not a live defect -- final state is correct. Logged as a
        // characterization note (not a manifest finding) precisely because a real test caught
        // an initially-plausible-looking false theory before it was written up as a bug.
        SaleDetail saleDetail1 = saleDetailWithTaxId(7);
        SaleDetail saleDetail2 = saleDetailWithTaxId(7);
        when(salesReturnDao.getSaleDetailById(1L, 0)).thenReturn(saleDetail1);
        when(salesReturnDao.getSaleDetailById(2L, 0)).thenReturn(saleDetail2);
        when(salesReturnDao.getTaxId(anyLong())).thenReturn(7);

        TaxComponent gst = new TaxComponent(50);
        TaxDetail taxDetail = new TaxDetail(gst, 18.0);
        when(globalDao.getTaxDetail(7)).thenReturn(Arrays.asList(taxDetail));

        RequestSaleReturnSaveItem item1 = item(1L, 1180.0, 18.0, null);
        RequestSaleReturnSaveItem item2 = item(2L, 590.0, 18.0, null);
        service.saveSalesReturn(request(Arrays.asList(item1, item2)));

        ArgumentCaptor<Return> captor = ArgumentCaptor.forClass(Return.class);
        org.mockito.Mockito.verify(salesReturnDao).salesReturnDetailsSave(captor.capture());
        Return savedReturn = captor.getValue();
        assertThat(savedReturn.getReturnDetailList()).hasSize(2);

        ReturnDetail firstItemsReturnDetail = savedReturn.getReturnDetailList().get(0);
        ReturnDetail secondItemsReturnDetail = savedReturn.getReturnDetailList().get(1);

        List<SaleReturnDetailTaxComponenet> firstItemsComponents = taxComponentsOf(firstItemsReturnDetail);
        List<SaleReturnDetailTaxComponenet> secondItemsComponents = taxComponentsOf(secondItemsReturnDetail);
        assertThat(firstItemsComponents).hasSize(1);
        assertThat(secondItemsComponents).hasSize(1);

        // Each component's back-reference is its OWN item's ReturnDetail, not item 1's for both.
        assertThat(firstItemsComponents.getFirst().getReturnDetail()).isSameAs(firstItemsReturnDetail);
        assertThat(secondItemsComponents.getFirst().getReturnDetail()).isSameAs(secondItemsReturnDetail);
    }
}
