package com.param.billing.ipd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.param.billing.ipd.service.IpdChargesService;
import com.param.global.billing.controller.BillingException;
import com.param.global.common.Response;

@RestController
@RequestMapping(value = "/api/ipd")
@SuppressWarnings({"rawtypes"})
public class IpdChargesController {

	@Autowired
	private IpdChargesService ipdChargesService;
	
	/**
	 * METHOD TO GET ADMISSION DETAILS BY PATIENT ID
	 * @param admissionId
	 * @return
	 */
	@RequestMapping(value = "/admissions/patient/{admissionId}")
	public Response getAdmissionsByPatientId(@PathVariable("admissionId")int admissionId){
		try{
			return ipdChargesService.getAdmissionsById(admissionId);
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/admissions/patient/updatePayee/{admissionId}")
	public Response getAdmissionsByPatientIdForUpdatePayee(@PathVariable("admissionId")int admissionId){
		try{
			return ipdChargesService.getAdmissionsByIdForUpdatePayee(admissionId);
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/admissionDetails/cancelledBills/{admissionId}")
	public Response getAdmissionsDetailsByAdmissionIdForCancelledBills(@PathVariable("admissionId")int admissionId){
		try{
			return ipdChargesService.getAdmissionsDetailsByAdmissionIdForCancelledBills(admissionId);
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}
}
