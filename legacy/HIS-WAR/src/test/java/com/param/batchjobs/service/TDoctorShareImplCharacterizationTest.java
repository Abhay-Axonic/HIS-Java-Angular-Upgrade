package com.param.batchjobs.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.param.billing.global.transaction.dao.IBillingDetailsDao;
import com.param.billing.global.transaction.dao.IBillingMasterDao;
import com.param.billing.global.transaction.dao.IDoctorReferralShareDao;
import com.param.billing.global.transaction.dao.IDoctorShareDao;
import com.param.billing.global.transaction.dto.TDoctorShareRespDto;
import com.param.billing.unit.dao.IMDoctorReferralShareDao;
import com.param.global.common.Response;
import com.param.global.dao.IReferalDetailsDao;

/**
 * Characterization tests (current behavior, not verified-correct behavior) for
 * {@code TDoctorShareImpl.getDoctorShareService()} -- the reachable half of this class
 * (manually invoked via {@code GET /doNotHit/docShare}; NOT the live midnight-scheduled
 * doctor-share batch, which is a different class, {@code ITDoctorShareService}). The
 * sibling method {@code getDoctorReferralShareService()} is confirmed dead code (zero
 * callers anywhere in the codebase) and is intentionally not characterized here.
 *
 * Confirmed findings pinned by these tests (see docs write-up for owner decision):
 *  (a) pagination requests maxResults growing by +1000 each page while startPoint only
 *      advances by the PREVIOUS page's maxResults -- page 2 asks for (startPoint=1000,
 *      maxResults=2000), i.e. a page twice as large as the first, not a fixed page size.
 *  (b) the "mark as posted" call (iBillingDetailsDao...) is commented out in the source,
 *      so this method never marks processed rows as posted -- confirmed unreachable.
 *  (c) unitId is hardcoded to the literal 0 on every call, regardless of caller context.
 *  (d) rows with a null sharePer are silently skipped (no save, no log).
 */
@ExtendWith(MockitoExtension.class)
@SuppressWarnings({ "rawtypes", "unchecked" })
@DisplayName("TDoctorShareImpl.getDoctorShareService -- pagination, mark-as-posted, and share-calculation characterization")
class TDoctorShareImplCharacterizationTest {

    @Mock private IDoctorShareDao iDoctorShareDao;
    @Mock private IDoctorReferralShareDao iDoctorReferralShareDao;
    @Mock private IBillingMasterDao iBillingMasterDao;
    @Mock private IBillingDetailsDao iBillingDetailsDao;
    @Mock private IMDoctorReferralShareDao iMDoctorReferralShareDao;
    @Mock private IReferalDetailsDao iReferalDetailsDao;

    private TDoctorShareImpl service;

    @BeforeEach
    void setUp() throws Exception {
        service = new TDoctorShareImpl();
        setPrivate(service, "iDoctorShareDao", iDoctorShareDao);
        setPrivate(service, "iDoctorReferralShareDao", iDoctorReferralShareDao);
        setPrivate(service, "iBillingMasterDao", iBillingMasterDao);
        setPrivate(service, "iBillingDetailsDao", iBillingDetailsDao);
        setPrivate(service, "iMDoctorReferralShareDao", iMDoctorReferralShareDao);
        setPrivate(service, "iReferalDetailsDao", iReferalDetailsDao);
    }

    private static void setPrivate(Object target, String fieldName, Object value) throws Exception {
        Field f = target.getClass().getDeclaredField(fieldName);
        f.setAccessible(true);
        f.set(target, value);
    }

    @SuppressWarnings("unchecked")
    private static Response page(String code, List<TDoctorShareRespDto> rows) {
        return new Response("success", code, null, rows, null);
    }

    private static TDoctorShareRespDto row(Double sharePer, double totalAmount, double discount) {
        TDoctorShareRespDto dto = new TDoctorShareRespDto();
        dto.setSharePer(sharePer);
        dto.setTotalAmount(totalAmount);
        dto.setDiscount(discount);
        return dto;
    }

    @Test
    @DisplayName("share calculation: amount = totalAmount - discount; docShare = sharePer/100 * amount; hospShare = amount - docShare; dueShareAmt = docShare")
    void shareCalculation_appliesFormulaAndSetsFieldsOnDto() {
        TDoctorShareRespDto dto = row(20.0, 1000.0, 100.0); // amount = 900, docShare = 180, hospShare = 720

        when(iDoctorShareDao.getDataFromBillingDtls(0, 1000, 0)).thenReturn(page("200", List.of(dto)));
        when(iDoctorShareDao.getDataFromBillingDtls(1000, 2000, 0)).thenReturn(page("404", null));
        when(iDoctorShareDao.saveDoctorShareData(dto)).thenReturn(page("200", null));

        service.getDoctorShareService();

        assertThat(dto.getDocShare()).isEqualTo(180.0);
        assertThat(dto.getHospShare()).isEqualTo(720.0);
        assertThat(dto.getDueShareAmt()).isEqualTo(180.0);
        verify(iDoctorShareDao, times(1)).saveDoctorShareData(dto);
    }

    @Test
    @DisplayName("a row with null sharePer is silently skipped -- no save call, no error, no log")
    void nullSharePer_rowSkippedEntirely_noSaveCalled() {
        TDoctorShareRespDto skipped = row(null, 500.0, 0.0);

        when(iDoctorShareDao.getDataFromBillingDtls(0, 1000, 0)).thenReturn(page("200", List.of(skipped)));
        when(iDoctorShareDao.getDataFromBillingDtls(1000, 2000, 0)).thenReturn(page("404", null));

        service.getDoctorShareService();

        verify(iDoctorShareDao, never()).saveDoctorShareData(any());
        assertThat(skipped.getDocShare()).isNull();
    }

    @Test
    @DisplayName("MAJOR FINDING: mark-as-posted is commented out in source -- never invoked even after a successful save")
    void markAsPostedNeverInvoked_evenOnSuccessfulSave() {
        TDoctorShareRespDto dto = row(10.0, 200.0, 0.0);

        when(iDoctorShareDao.getDataFromBillingDtls(0, 1000, 0)).thenReturn(page("200", List.of(dto)));
        when(iDoctorShareDao.getDataFromBillingDtls(1000, 2000, 0)).thenReturn(page("404", null));
        when(iDoctorShareDao.saveDoctorShareData(dto)).thenReturn(page("success", null));

        service.getDoctorShareService();

        verify(iBillingDetailsDao, never()).updateBillDetailsIsDoctorSharePostedByBillId(any());
    }

    @Test
    @DisplayName("pagination: maxResults grows by +1000 each page while startPoint only advances by the PREVIOUS page's maxResults; unitId hardcoded to 0")
    void paginationParameters_maxResultsGrowsFasterThanStartPointAdvances_unitIdAlwaysZero() {
        TDoctorShareRespDto pageOneRow = row(5.0, 100.0, 0.0);
        TDoctorShareRespDto pageTwoRow = row(5.0, 200.0, 0.0);

        when(iDoctorShareDao.getDataFromBillingDtls(anyInt(), anyInt(), eq(0)))
                .thenReturn(page("200", List.of(pageOneRow)))
                .thenReturn(page("200", List.of(pageTwoRow)))
                .thenReturn(page("404", null));
        when(iDoctorShareDao.saveDoctorShareData(any())).thenReturn(page("success", null));

        service.getDoctorShareService();

        ArgumentCaptor<Integer> startPointCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> maxResultsCaptor = ArgumentCaptor.forClass(Integer.class);
        ArgumentCaptor<Integer> unitIdCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(iDoctorShareDao, times(3)).getDataFromBillingDtls(
                startPointCaptor.capture(), maxResultsCaptor.capture(), unitIdCaptor.capture());

        assertThat(startPointCaptor.getAllValues()).containsExactly(0, 1000, 2000);
        // NOT a fixed page size: page 2 requests twice as many rows (2000) as page 1 (1000).
        assertThat(maxResultsCaptor.getAllValues()).containsExactly(1000, 2000, 3000);
        assertThat(unitIdCaptor.getAllValues()).containsExactly(0, 0, 0);
    }
}
