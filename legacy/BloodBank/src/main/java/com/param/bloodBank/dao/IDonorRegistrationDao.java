package com.param.bloodBank.dao;

import com.param.bloodBank.dto.DonorRegistrationDto;
import com.param.bloodBank.dto.DonorScreeningDetailDto;
import com.param.global.common.Response;
import com.param.global.model.DonorFingerPrintInterface;

import in.param.exception.ApplicationException;

@SuppressWarnings({"rawtypes"})
public interface IDonorRegistrationDao {

	Response checkUniqueDonor(DonorRegistrationDto donorRegistrationDto);

	Response saveDonorRegistration(DonorRegistrationDto donorRegistrationDto);

	Response saveDonorDetails(DonorRegistrationDto donorRegistrationDto);

	Response updateDonor(DonorRegistrationDto donorRegistrationDto);

	Response donorSearchNormalByDonorId(Integer patientId, Integer organizationId, Integer unitId);

	Response updateDonorImagePath(DonorRegistrationDto donorRegistrationDto);

	Response getDonorDetails(DonorRegistrationDto patientRegistrationDto);

	Response donorSearchNormal(DonorRegistrationDto donorRegistrationDto);

	Response checKPatientAvailability(DonorRegistrationDto donorRegistrationDto);

	Response saveDonorThumb(DonorFingerPrintInterface fingerInterface);

	Long getNextValDonorId() throws ApplicationException;

	Response getDonorRegistrationDetails(DonorScreeningDetailDto donorScreeningDetailDto)throws ApplicationException;
}
