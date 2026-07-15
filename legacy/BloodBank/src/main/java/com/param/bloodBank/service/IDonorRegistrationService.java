package com.param.bloodBank.service;

import org.springframework.web.multipart.MultipartFile;

import com.param.bloodBank.dto.DonorRegistrationDto;
import com.param.global.common.Response;
import com.param.global.dto.SlotMasterDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IDonorRegistrationService {

	public Response registerDonor(DonorRegistrationDto patientRegistrationDto);

	public	Response updateDonorRegistration(DonorRegistrationDto patientRegistrationDto);

	public Response getDonorDetails(DonorRegistrationDto patientRegistrationDto);

	public Response donorSearch(DonorRegistrationDto patientRegistrationDto);

	public Response savePatientImage(int orgId, int unitId, int patientId, MultipartFile inputFile);

	public Response patientAutofillSearch(SlotMasterDto slotMasterDto);
	
	public Response saveDonorFingerPrint(DonorRegistrationDto dto)throws ApplicationException;

}
