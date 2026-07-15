package com.param.pharmacy.nursing.workstation.service.drugAdministration;

import java.text.ParseException;

import org.springframework.util.MultiValueMap;

import com.param.common.dto.CommonResponse;
import com.param.global.common.Response;
import com.param.global.pharmacy.dto.AntibioticTestDoseDto;
import com.param.pharmacy.nursing.workstation.dto.drugAdministration.DrugAdministrationDropdown;
import com.param.pharmacy.nursing.workstation.dto.drugAdministration.DrugAdministrationRequest;
import com.param.pharmacy.nursing.workstation.dto.drugAdministration.DrugAdministrationResponse;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IDrugAdministrationService {
	
	DrugAdministrationDropdown drugAdministrationDropdown();
	
	DrugAdministrationResponse drugAdministrationSearch(DrugAdministrationRequest request);
	
	CommonResponse drugAdministrationSave(DrugAdministrationRequest request) throws ParseException;
	
	CommonResponse drugAdministrationUpdate(DrugAdministrationRequest request) throws ParseException;

	DrugAdministrationDropdown  getreasonDropdownList(Integer orgId);
	
	Response getBatchDetailsByPresDetailsId(Integer id)throws ParseException;

	Response saveAntibioticTestDose(AntibioticTestDoseDto dto)throws ApplicationException;

	Response getAntibioticTestDose(AntibioticTestDoseDto dto)throws ApplicationException;

	Response checkObservation(AntibioticTestDoseDto dto)throws ApplicationException;
	
}