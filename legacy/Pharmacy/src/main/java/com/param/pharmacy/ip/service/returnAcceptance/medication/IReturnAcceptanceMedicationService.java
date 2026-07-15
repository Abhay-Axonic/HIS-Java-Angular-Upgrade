package com.param.pharmacy.ip.service.returnAcceptance.medication;

import java.util.List;

import com.param.global.billing.controller.BillingException;
import com.param.global.common.Response;
import com.param.global.dto.global.CommonDropdown;
import com.param.pharmacy.ip.dto.returnAcceptance.ReturnAcceptanceRequest;
import com.param.pharmacy.ip.dto.returnAcceptance.ReturnAcceptanceResponse;
import com.param.pharmacy.ip.dto.returnAcceptance.ReturnAcceptanceSave;

public interface IReturnAcceptanceMedicationService {
	
	CommonDropdown getDropdownList(Integer unitId);
	
	List<ReturnAcceptanceResponse> returnAcceptanceSearch(ReturnAcceptanceRequest returnAcceptanceRequest);

	ReturnAcceptanceResponse returnAcceptanceDetails(Integer id, Integer unitId);
	
	Object patientReturnAcceptanceSave(ReturnAcceptanceSave returnAcceptanceSave) throws BillingException, Exception;

	Response patientReturnAcceptanceStatus(ReturnAcceptanceSave returnAcceptanceSave);
}