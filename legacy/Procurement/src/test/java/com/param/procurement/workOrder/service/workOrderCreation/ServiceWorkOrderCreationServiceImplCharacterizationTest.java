package com.param.procurement.workOrder.service.workOrderCreation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.param.global.billing.dao.IVendorServiceRateConfigDao;
import com.param.global.common.Response;
import com.param.global.pharmacy.dto.PendingServicesForWorkOrderCreationDto;
import com.param.global.pharmacy.dto.VendorRateDto;
import com.param.procurement.workOrder.dao.workOrderCreation.INewWorkOrderCreationDao;
import com.param.procurement.workOrder.dao.workOrderCreation.IServiceWorkOrderCreationDao;

/**
 * Characterization tests (current behavior, not verified-correct behavior) for
 * {@code ServiceWorkOrderCreationServiceImpl.getDetails} -- Procurement's originally
 * flagged live financial call ({@code IVendorServiceRateConfigDao.getVendorRatesListForService}).
 *
 * Confirmed via research: this call has NO arithmetic/selection logic on the Java side
 * at all -- the returned vendor-rate list is placed unmodified onto the DTO and
 * serialized to the client as-is. Any rate comparison/selection happens in the
 * AngularJS frontend, out of scope here. This test therefore pins the two things that
 * ARE real Java-layer logic: the billingBedCategoryId fallback ternary, and that the
 * vendor list is passed through unchanged.
 */
@ExtendWith(MockitoExtension.class)
@SuppressWarnings({ "rawtypes", "unchecked" })
@DisplayName("ServiceWorkOrderCreationServiceImpl.getDetails -- vendor-rate pass-through characterization")
class ServiceWorkOrderCreationServiceImplCharacterizationTest {

    @Mock private IServiceWorkOrderCreationDao iServiceWorkOrderCreationDao;
    @Mock private IVendorServiceRateConfigDao iVendorServiceRateConfigDao;
    @Mock private INewWorkOrderCreationDao iNewWorkOrderCreationDao;

    private ServiceWorkOrderCreationServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new ServiceWorkOrderCreationServiceImpl();
        service.iServiceWorkOrderCreationDao = iServiceWorkOrderCreationDao;
        service.iVendorServiceRateConfigDao = iVendorServiceRateConfigDao;
        service.iNewWorkOrderCreationDao = iNewWorkOrderCreationDao;
    }

    @Test
    @DisplayName("billingBedCategoryId already set on the row: kept as-is, NOT overwritten by the request's opBillingBedCategoryId")
    void billingBedCategoryId_alreadySet_keptAsIs() throws Exception {
        PendingServicesForWorkOrderCreationDto requestDto = new PendingServicesForWorkOrderCreationDto();
        requestDto.setUnitId(1);
        requestDto.setOpBillingBedCategoryId(99);

        PendingServicesForWorkOrderCreationDto row = new PendingServicesForWorkOrderCreationDto();
        row.setBillingBedCategoryId(5); // already set

        when(iServiceWorkOrderCreationDao.getDetails(requestDto))
                .thenReturn(new Response<>("success", null, null, List.of(row), null));

        List<VendorRateDto> vendorRates = List.of();
        when(iVendorServiceRateConfigDao.getVendorRatesListForService(any())).thenReturn(vendorRates);

        service.getDetails(requestDto);

        assertThat(row.getBillingBedCategoryId()).isEqualTo(5);
        assertThat(row.getUnitId()).isEqualTo(1); // always overwritten from requestDto, unconditionally
    }

    @Test
    @DisplayName("billingBedCategoryId NOT set on the row: falls back to the request's opBillingBedCategoryId")
    void billingBedCategoryId_notSet_fallsBackToOpBillingBedCategoryId() throws Exception {
        PendingServicesForWorkOrderCreationDto requestDto = new PendingServicesForWorkOrderCreationDto();
        requestDto.setUnitId(1);
        requestDto.setOpBillingBedCategoryId(99);

        PendingServicesForWorkOrderCreationDto row = new PendingServicesForWorkOrderCreationDto();
        // billingBedCategoryId left null

        when(iServiceWorkOrderCreationDao.getDetails(requestDto))
                .thenReturn(new Response<>("success", null, null, List.of(row), null));
        when(iVendorServiceRateConfigDao.getVendorRatesListForService(any())).thenReturn(List.of());

        service.getDetails(requestDto);

        assertThat(row.getBillingBedCategoryId()).isEqualTo(99);
    }

    @Test
    @DisplayName("vendor-rate list is passed through onto the row completely unchanged -- no filtering, sorting, or selection")
    void vendorRateList_passedThroughUnchanged() throws Exception {
        PendingServicesForWorkOrderCreationDto requestDto = new PendingServicesForWorkOrderCreationDto();
        requestDto.setUnitId(1);
        requestDto.setOpBillingBedCategoryId(99);

        PendingServicesForWorkOrderCreationDto row = new PendingServicesForWorkOrderCreationDto();
        when(iServiceWorkOrderCreationDao.getDetails(requestDto))
                .thenReturn(new Response<>("success", null, null, List.of(row), null));

        VendorRateDto vendor1 = new VendorRateDto();
        vendor1.setVendorId(1);
        vendor1.setVendorName("Vendor A");
        vendor1.setRate(150.0);
        VendorRateDto vendor2 = new VendorRateDto();
        vendor2.setVendorId(2);
        vendor2.setVendorName("Vendor B");
        vendor2.setRate(120.0); // cheaper, but nothing in Java picks it -- confirms no selection logic
        List<VendorRateDto> vendorRates = List.of(vendor1, vendor2);
        when(iVendorServiceRateConfigDao.getVendorRatesListForService(row)).thenReturn(vendorRates);

        service.getDetails(requestDto);

        assertThat(row.getVendorList()).containsExactly(vendor1, vendor2);
        verify(iVendorServiceRateConfigDao).getVendorRatesListForService(row);
    }
}
