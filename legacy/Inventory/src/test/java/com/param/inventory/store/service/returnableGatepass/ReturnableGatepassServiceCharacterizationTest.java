package com.param.inventory.store.service.returnableGatepass;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
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

import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.ReturnableGatepass;
import com.param.entity.model.inventory.ReturnableGatepassDetail;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.Tax;
import com.param.global.dto.TaxComponentDetailsDto;
import com.param.global.org.dao.ITaxComponentDetailsDao;
import com.param.inventory.store.dao.returnableGatepass.IReturnableGatepassDao;
import com.param.inventory.store.dto.returnableGatepass.ReturnableGatepassRequest;
import com.param.procurement.purchase.dao.goodReceiptNote.IGoodReceiptNoteDao;

/**
 * WAVE-1 CHARACTERIZATION TEST -- DRAFT, NOT YET REVIEWED OR APPROVED.
 *
 * Targets {@link ReturnableGatepassService#submitReturnableGatepass}, entry point #2 in
 * docs/DECISION_NEEDED_ShadowTestHarnessApproach.md Section 1 (tax computation via
 * {@code ITaxComponentDetailsDao}) -- the same formula, confirmed via grep, is duplicated in
 * Procurement's {@code GoodReceiptNoteService.java} (~line 1752-1776), not covered by this test.
 *
 * STRUCTURAL NOTE, different from the wave-1 co-pay-split test: the tax-amount computation here is
 * NOT its own callable method -- it is inlined directly inside this large @Transactional method,
 * which also mutates gatepass/detail status fields and calls 3 other DAOs. There is no way to unit-test
 * just the formula in isolation without either (a) exercising the whole method with mocked
 * dependencies, as done here, or (b) extracting the tax block into its own method first (a real,
 * separate refactor decision, not made in this draft). Because of this, these tests necessarily also
 * pin some non-tax behavior of the surrounding method (which details get skipped, when the DAO write
 * happens at all) as a side effect of reaching the tax computation at all.
 *
 * Per Decision 4 (owner-approved): assertions use exact equality, not RULES.md's $0.01 tolerance --
 * wave 1 pins current behavior precisely, it does not judge whether that behavior is correct.
 * The formula itself, {@code (natAmount / 100) * taxComponentPercentage}, runs in {@code double} per
 * docs/AUDIT_BillingRisk.md RISK-1 -- these tests reproduce the exact same expression to derive their
 * expected values, which is appropriate for a characterization test (pinning current arithmetic, not
 * verifying it is the mathematically "right" answer) but means a change to operation order elsewhere
 * in a refactor could still be behaviorally different in the last bit even if "obviously equivalent."
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ReturnableGatepassService.submitReturnableGatepass -- tax computation characterization (current behavior, not verified-correct behavior)")
class ReturnableGatepassServiceCharacterizationTest {

    @Mock
    private IReturnableGatepassDao returnableGatepassDao;
    @Mock
    private IGoodReceiptNoteDao grnDao;
    @Mock
    private ITaxComponentDetailsDao taxComponentDetailsDao;

    private ReturnableGatepassService service;

    @BeforeEach
    void setUp() {
        service = new ReturnableGatepassService();
        // Package-private field injection (same-package test, no Spring context needed) -- matches
        // the fields' actual (default) visibility in ReturnableGatepassService.java. sessionService
        // and the other 4 unused @Autowired fields are left null: submitReturnableGatepass never
        // touches them, confirmed by reading the method body.
        service.ReturnableGatepassDao = returnableGatepassDao;
        service.grnDao = grnDao;
        service.iTaxComponentDetailsDao = taxComponentDetailsDao;
    }

    private ReturnableGatepassRequest request(Integer gatepassId, Integer userId) {
        ReturnableGatepassRequest req = new ReturnableGatepassRequest();
        req.setGatepassId(Arrays.asList(gatepassId));
        req.setStatusId(1);
        req.setApprovalStatusId(2);
        req.setUserId(userId);
        return req;
    }

    private ReturnableGatepassDetail detail(Integer detailId, boolean isDeleted, Double totalAmt,
            Double totalTaxAmount, Tax tax, Integer organizationId, Integer itemId) {
        ReturnableGatepassDetail d = new ReturnableGatepassDetail();
        d.setId(detailId);
        d.setIsDeleted(isDeleted);
        d.setTotalAmt(totalAmt);
        d.setTotalTaxAmount(totalTaxAmount);
        d.setTax(tax);
        d.setOrganizationId(organizationId);
        Batch batch = new Batch();
        Item item = new Item();
        item.setId(itemId);
        batch.setItem(item);
        d.setBatch(batch);
        return d;
    }

    private ReturnableGatepass gatepass(Integer unitId, List<ReturnableGatepassDetail> details) {
        ReturnableGatepass g = new ReturnableGatepass();
        g.setUnitId(unitId);
        g.setReturnableGatepassDetails(details);
        return g;
    }

    private TaxComponentDetailsDto taxComponent(Integer id, String name, Double percentage) {
        TaxComponentDetailsDto dto = new TaxComponentDetailsDto();
        dto.setTaxComponentId(id);
        dto.setTaxComponentName(name);
        dto.setTaxComponentPercentage(percentage);
        return dto;
    }

    @Nested
    @DisplayName("a single tax component (e.g. a flat GST rate)")
    class SingleTaxComponent {

        @Test
        @DisplayName("computes taxComponentAmount as (totalAmt / 100) * taxComponentPercentage, exactly reproducing current double arithmetic")
        void computesTaxComponentAmount_matchingCurrentFormula() throws Exception {
            Tax tax = new Tax(5);
            tax.setTaxPercentage(18.0);
            ReturnableGatepassDetail detail = detail(55, false, 1000.0, 180.0, tax, 1, 7);
            ReturnableGatepass gatepass = gatepass(0, Arrays.asList(detail));

            when(returnableGatepassDao.getReturnableGatepass(1)).thenReturn(gatepass);
            when(grnDao.getTaxDetails(5)).thenReturn(Arrays.asList(taxComponent(100, "GST", 18.0)));

            service.submitReturnableGatepass(request(1, 42));

            ArgumentCaptor<TaxComponentDetailsDto> captor = ArgumentCaptor.forClass(TaxComponentDetailsDto.class);
            verify(taxComponentDetailsDao).saveTaxComponentDetails(captor.capture());

            // Pins the exact current double result -- not asserting this is the "mathematically
            // correct" tax amount, only that it is what this code currently produces.
            assertThat(captor.getValue().getTaxComponentAmount()).isEqualTo((1000.0 / 100) * 18.0);
        }

        @Test
        @DisplayName("copies the parent detail's totalTaxAmount and the component's own percentage/name/id onto the saved dto, unmodified")
        void copiesTaxComponentMetadataOntoSavedDto() throws Exception {
            Tax tax = new Tax(5);
            tax.setTaxPercentage(18.0);
            ReturnableGatepassDetail detail = detail(55, false, 1000.0, 180.0, tax, 1, 7);
            ReturnableGatepass gatepass = gatepass(0, Arrays.asList(detail));

            when(returnableGatepassDao.getReturnableGatepass(1)).thenReturn(gatepass);
            when(grnDao.getTaxDetails(5)).thenReturn(Arrays.asList(taxComponent(100, "CGST", 9.0)));

            service.submitReturnableGatepass(request(1, 42));

            ArgumentCaptor<TaxComponentDetailsDto> captor = ArgumentCaptor.forClass(TaxComponentDetailsDto.class);
            verify(taxComponentDetailsDao).saveTaxComponentDetails(captor.capture());
            TaxComponentDetailsDto saved = captor.getValue();

            assertThat(saved.getTaxComponentId()).isEqualTo(100);
            assertThat(saved.getTaxComponentName()).isEqualTo("CGST");
            assertThat(saved.getTaxComponentPercentage()).isEqualTo(9.0);
            assertThat(saved.getTaxAmount()).isEqualTo(180.0); // detail's own totalTaxAmount, copied through as-is
            assertThat(saved.getTaxPercentage()).isEqualTo(18.0); // the Tax master's overall rate, distinct from the component's own rate
            assertThat(saved.getUnitId()).isEqualTo(0);
            assertThat(saved.getItemId()).isEqualTo(7);
            assertThat(saved.getCreatedBy()).isEqualTo(42);
        }
    }

    @Nested
    @DisplayName("multiple tax components on one detail (e.g. CGST + SGST split)")
    class MultipleTaxComponents {

        @Test
        @DisplayName("saves one dto per component, each computed independently off the same totalAmt")
        void savesOneDtoPerComponent() throws Exception {
            Tax tax = new Tax(5);
            tax.setTaxPercentage(18.0);
            ReturnableGatepassDetail detail = detail(55, false, 2000.0, 360.0, tax, 1, 7);
            ReturnableGatepass gatepass = gatepass(0, Arrays.asList(detail));

            when(returnableGatepassDao.getReturnableGatepass(1)).thenReturn(gatepass);
            when(grnDao.getTaxDetails(5)).thenReturn(Arrays.asList(
                    taxComponent(101, "CGST", 9.0),
                    taxComponent(102, "SGST", 9.0)));

            service.submitReturnableGatepass(request(1, 42));

            ArgumentCaptor<TaxComponentDetailsDto> captor = ArgumentCaptor.forClass(TaxComponentDetailsDto.class);
            verify(taxComponentDetailsDao, times(2)).saveTaxComponentDetails(captor.capture());

            List<TaxComponentDetailsDto> saved = captor.getAllValues();
            assertThat(saved).hasSize(2);
            assertThat(saved.get(0).getTaxComponentAmount()).isEqualTo((2000.0 / 100) * 9.0);
            assertThat(saved.get(1).getTaxComponentAmount()).isEqualTo((2000.0 / 100) * 9.0);
        }
    }

    @Nested
    @DisplayName("edge cases in current behavior")
    class EdgeCases {

        @Test
        @DisplayName("skips tax processing entirely for a detail with no tax components configured")
        void skipsTaxProcessing_whenNoTaxComponentsExist() throws Exception {
            Tax tax = new Tax(5);
            ReturnableGatepassDetail detail = detail(55, false, 1000.0, 0.0, tax, 1, 7);
            ReturnableGatepass gatepass = gatepass(0, Arrays.asList(detail));

            when(returnableGatepassDao.getReturnableGatepass(1)).thenReturn(gatepass);
            when(grnDao.getTaxDetails(5)).thenReturn(new ArrayList<>());

            service.submitReturnableGatepass(request(1, 42));

            verify(taxComponentDetailsDao, never()).saveTaxComponentDetails(any());
        }

        @Test
        @DisplayName("skips a soft-deleted detail entirely -- no tax lookup, no save, not even touched")
        void skipsSoftDeletedDetail() throws Exception {
            Tax tax = new Tax(5);
            tax.setTaxPercentage(18.0);
            ReturnableGatepassDetail detail = detail(55, /* isDeleted */ true, 1000.0, 180.0, tax, 1, 7);
            ReturnableGatepass gatepass = gatepass(0, Arrays.asList(detail));

            when(returnableGatepassDao.getReturnableGatepass(1)).thenReturn(gatepass);

            service.submitReturnableGatepass(request(1, 42));

            verify(grnDao, never()).getTaxDetails(any());
            verify(taxComponentDetailsDao, never()).saveTaxComponentDetails(any());
        }

        @Test
        @DisplayName("looks up tax details with a fallback id of 0 when the detail's tax reference is null, rather than skipping the lookup")
        void fallsBackToZero_whenTaxReferenceIsNull() {
            ReturnableGatepassDetail detail = detail(55, false, 1000.0, 0.0, /* tax */ null, 1, 7);
            ReturnableGatepass gatepass = gatepass(0, Arrays.asList(detail));

            when(returnableGatepassDao.getReturnableGatepass(1)).thenReturn(gatepass);
            when(grnDao.getTaxDetails(0)).thenReturn(new ArrayList<>());

            service.submitReturnableGatepass(request(1, 42));

            verify(grnDao).getTaxDetails(0);
        }

        @Test
        @DisplayName("always updates the gatepass itself, even when it has no processable details")
        void alwaysUpdatesGatepass() {
            ReturnableGatepass gatepass = gatepass(0, new ArrayList<>());

            when(returnableGatepassDao.getReturnableGatepass(1)).thenReturn(gatepass);

            service.submitReturnableGatepass(request(1, 42));

            verify(returnableGatepassDao).updateReturnableGatepass(gatepass);
        }
    }
}
