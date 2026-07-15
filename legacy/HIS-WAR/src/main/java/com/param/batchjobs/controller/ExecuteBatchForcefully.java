package com.param.batchjobs.controller;

import java.util.ArrayList;
import java.util.List;

import com.param.global.common.ServerIdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.batchjobs.scheduler.RevertAllocatedQuantity;
import com.param.batchjobs.service.IDoctorShare;
import com.param.billing.common.Response;
import com.param.global.common.ICommonConstants;
import com.param.global.common.ServiceException;
import com.param.global.opd.service.IAppointmentSchedulingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/doNotHit")
public class ExecuteBatchForcefully implements ICommonConstants{

	private static final Logger logger = LoggerFactory.getLogger(ExecuteBatchForcefully.class);

	@Autowired
	IDoctorShare doctorShare;
	
	@Autowired
	IAppointmentSchedulingService iAppointmentSchedulingService;

	@Autowired
	private ServerIdentityService serverIdentityService;
	
	@Autowired
	private RevertAllocatedQuantity roomRentBatchChargesServ;
	
	@RequestMapping(value = "/docShare", method = RequestMethod.GET)
	public @ResponseBody Response docShare() {
		try {
				doctorShare.getDoctorShareService();
				return new Response<>(SUCCESS, null, null, null, null);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/roomRent", method = RequestMethod.GET)
	public @ResponseBody Response roomRent() {
		try {

			String serverId = serverIdentityService.getServerId();
			System.out.println(serverId);
			/*roomRentBatchChargesServ.ForceFullyMidNightBatch();*/
				return new Response<>(SUCCESS, null, null, null, null);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	@RequestMapping(value = "/closeAppointment", method = RequestMethod.GET)
	public @ResponseBody Response closeAppointment() {
		try {
				//roomRentBatchChargesServ.postRoomRentCharges();
				iAppointmentSchedulingService.batchJobToUpdateAppointmentStatus();
				return new Response<>(SUCCESS, null, null, null, null);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/template", method = RequestMethod.GET)
	public @ResponseBody Response template() {
		try {
			List<Integer> unitId = new ArrayList<Integer>();
			unitId.add(186);
				roomRentBatchChargesServ.template(unitId);
				return new Response<>(SUCCESS, null, null, null, null);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
}
