package com.param.billing.global.transaction.service;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.billing.global.dto.BillPaymentCollectionReqDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.opd.service.IAppointmentSchedulingService;

@Service
@Transactional
@SuppressWarnings({"rawtypes", "unused"})
public class RazorPayBillService implements ICommonConstants {

	@Autowired
	private IBillingMasterService iBillingMasterService;
	
	@Autowired
	private IAppointmentSchedulingService iAppointmentSchedulingService;
	
	@Async
	public CompletableFuture<BillPaymentCollectionReqDto> settleBill(BillPaymentCollectionReqDto billCollReq)
			throws InterruptedException {
		try {
			billCollReq.setIsPaidAppointment('Y');
			iBillingMasterService.collectPayment(billCollReq);
			Response paidAppRes = iAppointmentSchedulingService
					.updateAppointmentPaidStatus(billCollReq.getAppointmentId());
		} catch (Exception e) {
			
		}
		Thread.sleep(1000L);
		return CompletableFuture.completedFuture(billCollReq);
	}
}
