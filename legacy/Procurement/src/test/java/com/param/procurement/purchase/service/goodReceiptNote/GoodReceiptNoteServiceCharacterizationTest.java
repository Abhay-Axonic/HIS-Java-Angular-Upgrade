package com.param.procurement.purchase.service.goodReceiptNote;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.param.entity.model.master.Item;
import com.param.entity.model.master.Tax;
import com.param.entity.model.procurement.GoodReceiptNote;
import com.param.entity.model.procurement.GoodReceiptNoteDetail;
import com.param.global.dto.TaxComponentDetailsDto;
import com.param.global.org.dao.ITaxComponentDetailsDao;
import com.param.procurement.purchase.dao.goodReceiptNote.IGoodReceiptNoteDao;
import com.param.procurement.purchase.dto.goodReceiptNote.GRNStatusDto;

/**
 * WAVE-1 CHARACTERIZATION TEST -- DRAFT, NOT YET REVIEWED OR APPROVED.
 *
 * Targets {@link GoodReceiptNoteService#approveGoodReceiptNote}, Procurement's copy of entry point #2
 * in docs/DECISION_NEEDED_ShadowTestHarnessApproach.md Section 1 (tax computation via
 * ITaxComponentDetailsDao) -- the sibling of Inventory's ReturnableGatepassServiceCharacterizationTest
 * (submitReturnableGatepass), which the original audit grouped with this one as "the same tax
 * computation pattern." Re-reading both current sources side by side surfaced that they are NOT the
 * same computation -- see the "DIVERGENCE FROM INVENTORY'S VERSION" test group below, which pins the
 * differences precisely so neither can silently drift closer together or further apart without a
 * test failing.
 *
 * CONFIRMED DIVERGENCES FROM Inventory's ReturnableGatepassService#submitReturnableGatepass (originally
 * 3, flagged per Decision 5 of docs/DECISION_NEEDED_ShadowTestHarnessApproach.md -- #2 has since been
 * fixed, owner-approved; #1 and #3 remain open, sitting in the slower joint-triage list):
 *
 * 1. DISCOUNT NETTING (still open): this method computes commTaxAmount = totalAmount - totalDiscount
 *    (defaulting to 0 when totalDiscount is null) before taxing. Inventory's version taxes the raw
 *    totalAmt with NO discount subtraction at all -- despite ReturnableGatepassDetail having its own
 *    populated totalDiscount field (confirmed via grep: set from item.getTotalDiscount() at 12 call
 *    sites elsewhere in ReturnableGatepassService.java), which the tax-computation block simply never
 *    reads. Whether "goods receipt" and "goods return" tax should legitimately differ this way, or
 *    whether one of the two is a bug, is a real business question -- not decided here.
 *
 * 2. NULL-TAX-REFERENCE HANDLING -- FIXED 2026-07-15 (docs/DECISION_NEEDED_GrnNullTaxReferenceCrash.md,
 *    owner-approved). Previously: this method called `detail.getTax().getId()` directly, with no null
 *    check and no surrounding try/catch at that specific line, so a detail with a null Tax reference
 *    threw an uncaught NullPointerException out of the whole method, aborting the entire batch
 *    approval. Now: guards with `detail.getTax()!=null ? detail.getTax().getId() : 0`, the exact line
 *    already live in Inventory's parallel flow (same DAO interface/method). See
 *    NullTaxReference#fallsBackToZero_whenTaxIsNull_insteadOfCrashing below, which now pins the fixed
 *    (graceful-skip) behavior instead of the crash.
 *
 * 3. NO SOFT-DELETE GUARD -- FIXED 2026-07-15 (docs/DECISION_NEEDED_TriagePendingFindingsFixes.md,
 *    finding 2b, owner-approved). Previously: Inventory skips details where `getIsDeleted()` is true,
 *    but this method's loop had no equivalent check -- every detail was processed unconditionally.
 *    Now: the entire loop body is wrapped in `if (!detail.getIsDeleted())`, the exact pattern already
 *    live in Inventory's parallel flow -- `GoodReceiptNoteDetail` inherits `getIsDeleted()` from
 *    `CommonEntity` via `BaseEntity`, the same chain `ReturnableGatepassDetail` uses. See
 *    NoDeletedGuard#skipsDeletedDetails_insteadOfProcessingAll below.
 *
 * Per Decision 4 (owner-approved): assertions use exact equality, not RULES.md's $0.01 tolerance.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("GoodReceiptNoteService.approveGoodReceiptNote -- tax computation characterization (current behavior, not verified-correct behavior)")
class GoodReceiptNoteServiceCharacterizationTest {

    @Mock
    private IGoodReceiptNoteDao grnDao;
    @Mock
    private ITaxComponentDetailsDao taxComponentDetailsDao;

    private GoodReceiptNoteService service;

    @BeforeEach
    void setUp() {
        service = new GoodReceiptNoteService();
        // Package-private field injection (same-package test, no Spring context needed) -- matches
        // the fields' actual (default) visibility in GoodReceiptNoteService.java. sessionService and
        // the other unused @Autowired fields are left null: approveGoodReceiptNote never touches them,
        // confirmed by reading the method body.
        service.grnDao = grnDao;
        service.iTaxComponentDetailsDao = taxComponentDetailsDao;
    }

    private GRNStatusDto request(Integer grnId, Integer userId) {
        GRNStatusDto req = new GRNStatusDto();
        req.setGrnIdList(Arrays.asList(grnId));
        req.setStatusId(1);
        req.setApprovalStatusId(2);
        req.setUserId(userId);
        return req;
    }

    private GoodReceiptNoteDetail detail(Integer detailId, Double totalAmount, Double totalDiscount,
            Double taxAmount, Tax tax, Integer itemId) {
        GoodReceiptNoteDetail d = new GoodReceiptNoteDetail();
        d.setId(detailId);
        d.setTotalAmount(totalAmount);
        d.setTotalDiscount(totalDiscount);
        d.setTaxAmount(taxAmount);
        d.setTax(tax);
        Item item = new Item();
        item.setId(itemId);
        d.setItem(item);
        return d;
    }

    private GoodReceiptNote goodReceiptNote(Integer unitId, Integer organizationId, List<GoodReceiptNoteDetail> details) {
        GoodReceiptNote grn = new GoodReceiptNote();
        grn.setUnitId(unitId);
        grn.setOrganizationId(organizationId);
        grn.setGoodReceiptNoteDetailsList(details);
        return grn;
    }

    private TaxComponentDetailsDto taxComponent(Integer id, String name, Double percentage) {
        TaxComponentDetailsDto dto = new TaxComponentDetailsDto();
        dto.setTaxComponentId(id);
        dto.setTaxComponentName(name);
        dto.setTaxComponentPercentage(percentage);
        return dto;
    }

    @Nested
    @DisplayName("the actual formula, discount already netted out")
    class TaxFormula {

        @Test
        @DisplayName("computes taxComponentAmount as (totalAmount - totalDiscount) * (taxComponentPercentage / 100)")
        void computesTaxComponentAmount_netOfDiscount() throws Exception {
            Tax tax = new Tax(5);
            tax.setTaxPercentage(18.0);
            GoodReceiptNoteDetail detail = detail(55, 1000.0, 100.0, 162.0, tax, 7);
            GoodReceiptNote grn = goodReceiptNote(0, 1, Arrays.asList(detail));

            when(grnDao.getGoodReceiptNote(1)).thenReturn(grn);
            when(grnDao.getTaxDetails(5)).thenReturn(Arrays.asList(taxComponent(100, "GST", 18.0)));

            service.approveGoodReceiptNote(request(1, 42));

            ArgumentCaptor<TaxComponentDetailsDto> captor = ArgumentCaptor.forClass(TaxComponentDetailsDto.class);
            verify(taxComponentDetailsDao).saveTaxComponentDetails(captor.capture());

            // Pins the exact current double result -- (1000.0 - 100.0) * (18.0 / 100), reproducing
            // this method's own operation order, not asserting it is the "mathematically correct"
            // tax amount.
            Double commTaxAmount = 1000.0 - 100.0;
            assertThat(captor.getValue().getTaxComponentAmount()).isEqualTo(commTaxAmount * (18.0 / 100));
        }

        @Test
        @DisplayName("treats a null totalDiscount as zero, not as a missing/skip condition")
        void treatsNullDiscountAsZero() throws Exception {
            Tax tax = new Tax(5);
            tax.setTaxPercentage(18.0);
            GoodReceiptNoteDetail detail = detail(55, 1000.0, /* totalDiscount */ null, 180.0, tax, 7);
            GoodReceiptNote grn = goodReceiptNote(0, 1, Arrays.asList(detail));

            when(grnDao.getGoodReceiptNote(1)).thenReturn(grn);
            when(grnDao.getTaxDetails(5)).thenReturn(Arrays.asList(taxComponent(100, "GST", 18.0)));

            service.approveGoodReceiptNote(request(1, 42));

            ArgumentCaptor<TaxComponentDetailsDto> captor = ArgumentCaptor.forClass(TaxComponentDetailsDto.class);
            verify(taxComponentDetailsDao).saveTaxComponentDetails(captor.capture());

            assertThat(captor.getValue().getTaxComponentAmount()).isEqualTo(1000.0 * (18.0 / 100));
        }

        @Test
        @DisplayName("does not clamp at zero -- a discount larger than the total amount produces a negative tax component, unguarded")
        void doesNotClampNegativeCommTaxAmount() throws Exception {
            Tax tax = new Tax(5);
            tax.setTaxPercentage(18.0);
            // Discount (1200) exceeds totalAmount (1000) -- a real, if unusual, data state.
            GoodReceiptNoteDetail detail = detail(55, 1000.0, 1200.0, 0.0, tax, 7);
            GoodReceiptNote grn = goodReceiptNote(0, 1, Arrays.asList(detail));

            when(grnDao.getGoodReceiptNote(1)).thenReturn(grn);
            when(grnDao.getTaxDetails(5)).thenReturn(Arrays.asList(taxComponent(100, "GST", 18.0)));

            service.approveGoodReceiptNote(request(1, 42));

            ArgumentCaptor<TaxComponentDetailsDto> captor = ArgumentCaptor.forClass(TaxComponentDetailsDto.class);
            verify(taxComponentDetailsDao).saveTaxComponentDetails(captor.capture());

            Double expectedNegative = (1000.0 - 1200.0) * (18.0 / 100);
            assertThat(expectedNegative).isNegative();
            assertThat(captor.getValue().getTaxComponentAmount()).isEqualTo(expectedNegative);
        }
    }

    @Nested
    @DisplayName("multiple tax components on one detail")
    class MultipleTaxComponents {

        @Test
        @DisplayName("saves one dto per component, each computed independently off the same net amount")
        void savesOneDtoPerComponent() throws Exception {
            Tax tax = new Tax(5);
            tax.setTaxPercentage(18.0);
            GoodReceiptNoteDetail detail = detail(55, 2000.0, 0.0, 360.0, tax, 7);
            GoodReceiptNote grn = goodReceiptNote(0, 1, Arrays.asList(detail));

            when(grnDao.getGoodReceiptNote(1)).thenReturn(grn);
            when(grnDao.getTaxDetails(5)).thenReturn(Arrays.asList(
                    taxComponent(101, "CGST", 9.0),
                    taxComponent(102, "SGST", 9.0)));

            service.approveGoodReceiptNote(request(1, 42));

            ArgumentCaptor<TaxComponentDetailsDto> captor = ArgumentCaptor.forClass(TaxComponentDetailsDto.class);
            verify(taxComponentDetailsDao, times(2)).saveTaxComponentDetails(captor.capture());

            List<TaxComponentDetailsDto> saved = captor.getAllValues();
            assertThat(saved).hasSize(2);
            assertThat(saved.get(0).getTaxComponentAmount()).isEqualTo(2000.0 * (9.0 / 100));
            assertThat(saved.get(1).getTaxComponentAmount()).isEqualTo(2000.0 * (9.0 / 100));
        }
    }

    @Nested
    @DisplayName("DIVERGENCE FROM INVENTORY'S VERSION -- flagged during test authoring, not fixed, needs an owner decision")
    class NullTaxReference {

        @Test
        @DisplayName("FIXED 2026-07-15 (docs/DECISION_NEEDED_GrnNullTaxReferenceCrash.md, owner-approved): falls back to id=0 instead of crashing, matching Inventory's existing pattern -- taxDetailsList comes back empty, tax processing is skipped for that line, and the rest of the batch (including the goodReceiptNote status update) still completes")
        void fallsBackToZero_whenTaxIsNull_insteadOfCrashing() throws Exception {
            GoodReceiptNoteDetail detail = detail(55, 1000.0, 0.0, 0.0, /* tax */ null, 7);
            GoodReceiptNote grn = goodReceiptNote(0, 1, Arrays.asList(detail));

            when(grnDao.getGoodReceiptNote(1)).thenReturn(grn);
            // No stub for getTaxDetails(0) -- Mockito's default answer for a List-returning method
            // is an empty list, matching Inventory's confirmed real-DAO behavior for an unmatched id.

            service.approveGoodReceiptNote(request(1, 42));

            verify(grnDao).getTaxDetails(0); // the fallback fired, not a crash
            verify(taxComponentDetailsDao, never()).saveTaxComponentDetails(any()); // nothing to save, no tax components for id=0
            verify(grnDao).updateGoodReceiptNote(grn); // the rest of the batch still completes
        }
    }

    @Nested
    @DisplayName("NoDeletedGuard -- confirms this method now mirrors Inventory's isDeleted skip")
    class NoDeletedGuard {

        @Test
        @DisplayName("FIXED 2026-07-15 (docs/DECISION_NEEDED_TriagePendingFindingsFixes.md, finding 2b, owner-approved): skips a detail marked isDeleted, still processes the rest of the batch normally")
        void skipsDeletedDetails_insteadOfProcessingAll() throws Exception {
            Tax tax = new Tax(5);
            tax.setTaxPercentage(18.0);
            GoodReceiptNoteDetail detail1 = detail(55, 1000.0, 0.0, 180.0, tax, 7);
            detail1.setIsDeleted(true);
            GoodReceiptNoteDetail detail2 = detail(56, 500.0, 0.0, 90.0, tax, 8);
            detail2.setIsDeleted(false);
            GoodReceiptNote grn = goodReceiptNote(0, 1, Arrays.asList(detail1, detail2));

            when(grnDao.getGoodReceiptNote(1)).thenReturn(grn);
            when(grnDao.getTaxDetails(5)).thenReturn(Arrays.asList(taxComponent(100, "GST", 18.0)));

            service.approveGoodReceiptNote(request(1, 42));

            // Only detail2 (not deleted) is processed: 1 tax component x 1 detail = 1 save, not 2.
            verify(taxComponentDetailsDao, times(1)).saveTaxComponentDetails(any());
            // The rest of the batch (the good-receipt-note status update itself) still completes.
            verify(grnDao).updateGoodReceiptNote(grn);
        }
    }

    @Nested
    @DisplayName("edge cases")
    class EdgeCases {

        @Test
        @DisplayName("skips tax processing entirely for a detail with no tax components configured")
        void skipsTaxProcessing_whenNoTaxComponentsExist() throws Exception {
            Tax tax = new Tax(5);
            GoodReceiptNoteDetail detail = detail(55, 1000.0, 0.0, 0.0, tax, 7);
            GoodReceiptNote grn = goodReceiptNote(0, 1, Arrays.asList(detail));

            when(grnDao.getGoodReceiptNote(1)).thenReturn(grn);
            when(grnDao.getTaxDetails(5)).thenReturn(new ArrayList<>());

            service.approveGoodReceiptNote(request(1, 42));

            verify(taxComponentDetailsDao, never()).saveTaxComponentDetails(any());
        }

        @Test
        @DisplayName("always updates the good receipt note itself, even with no processable tax components")
        void alwaysUpdatesGoodReceiptNote() throws Exception {
            Tax tax = new Tax(5);
            GoodReceiptNoteDetail detail = detail(55, 1000.0, 0.0, 0.0, tax, 7);
            GoodReceiptNote grn = goodReceiptNote(0, 1, Arrays.asList(detail));

            when(grnDao.getGoodReceiptNote(1)).thenReturn(grn);
            when(grnDao.getTaxDetails(5)).thenReturn(new ArrayList<>());

            service.approveGoodReceiptNote(request(1, 42));

            verify(grnDao).updateGoodReceiptNote(grn);
        }
    }
}
