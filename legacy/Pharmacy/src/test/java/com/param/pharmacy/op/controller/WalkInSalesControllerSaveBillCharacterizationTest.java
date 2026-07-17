package com.param.pharmacy.op.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import com.param.common.dto.CommonResponse;
import com.param.global.billing.service.IBillingService;
import com.param.global.common.Response;
import com.param.global.pharmacy.dto.WalkInSalesRequest;
import com.param.global.pharmacy.service.IWalkInSalesService;

/**
 * Characterization test (Pharmacy batch 2) for the controller-level trigger condition
 * that decides whether {@code postServicesIntoExistingBill} -- the most financially
 * substantial of Pharmacy's 14 live cross-module financial calls -- gets invoked at
 * all. The actual money math inside that method is characterized separately in
 * {@code BillingServiceImplPostServicesIntoExistingBillCharacterizationTest} (Global
 * module); this test only pins WalkInSalesController.saveBill's own routing logic.
 */
@ExtendWith(MockitoExtension.class)
@SuppressWarnings({ "rawtypes", "unchecked" })
@DisplayName("WalkInSalesController.saveBill -- postServicesIntoExistingBill trigger-condition characterization")
class WalkInSalesControllerSaveBillCharacterizationTest {

    @Mock private IWalkInSalesService walkInSalesService;
    @Mock private IBillingService globalBillingService;

    private WalkInSalesController controller;

    @BeforeEach
    void setUp() {
        controller = new WalkInSalesController();
        controller.WalkInSalesService = walkInSalesService;
        controller.globalBillingService = globalBillingService;
    }

    private WalkInSalesRequest request(boolean addToBill) {
        WalkInSalesRequest request = new WalkInSalesRequest();
        request.setAddToBill(addToBill);
        return request;
    }

    @Test
    @DisplayName("addToBill=true, saveBillComplete returns a non-null id and a non-empty ids list -> postServicesIntoExistingBill IS called, with exactly those ids")
    void addToBillTrue_withIds_callsPostServicesIntoExistingBill() throws Exception {
        CommonResponse walkinResponse = new CommonResponse();
        walkinResponse.setId(500L);
        walkinResponse.setIds(Arrays.asList(7001L, 7002L));
        walkinResponse.setMessage("saved");
        when(walkInSalesService.saveBillComplete(any(WalkInSalesRequest.class))).thenReturn(walkinResponse);
        when(globalBillingService.postServicesIntoExistingBill(anyList()))
                .thenReturn(new Response<>("success", null, "Added Successfully.", null, null));

        ResponseEntity<Object> result = controller.saveBill(request(true));

        ArgumentCaptor<java.util.List> captor = ArgumentCaptor.forClass(java.util.List.class);
        verify(globalBillingService).postServicesIntoExistingBill(captor.capture());
        assertThat(captor.getValue()).containsExactly(7001L, 7002L);
        assertThat(result.getStatusCode().value()).isEqualTo(200);
    }

    @Test
    @DisplayName("addToBill=false -> postServicesIntoExistingBill is NEVER called, even when ids are present")
    void addToBillFalse_neverCallsPostServicesIntoExistingBill() throws Exception {
        CommonResponse walkinResponse = new CommonResponse();
        walkinResponse.setId(500L);
        walkinResponse.setIds(Arrays.asList(7001L));
        when(walkInSalesService.saveBillComplete(any(WalkInSalesRequest.class))).thenReturn(walkinResponse);

        controller.saveBill(request(false));

        verify(globalBillingService, never()).postServicesIntoExistingBill(any());
    }

    @Test
    @DisplayName("addToBill=true but saveBillComplete returns an empty ids list -> postServicesIntoExistingBill is NEVER called")
    void addToBillTrue_emptyIds_neverCallsPostServicesIntoExistingBill() throws Exception {
        CommonResponse walkinResponse = new CommonResponse();
        walkinResponse.setId(500L);
        walkinResponse.setIds(Collections.emptyList());
        when(walkInSalesService.saveBillComplete(any(WalkInSalesRequest.class))).thenReturn(walkinResponse);

        controller.saveBill(request(true));

        verify(globalBillingService, never()).postServicesIntoExistingBill(any());
    }
}
