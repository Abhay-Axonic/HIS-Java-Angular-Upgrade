package com.param.global.org.common.service;

import com.param.global.common.Response;
import com.param.global.org.common.dto.PatientLoyaltyCardMapperDto;

import in.param.exception.ApplicationException;

@SuppressWarnings({ "rawtypes" })
public interface IPatientLoyaltyCardMapperService {

	public Response savePatientLoyaltiCardMapper(PatientLoyaltyCardMapperDto dto) throws ApplicationException;

	public Response getAvailableActiveDepositWithCardNumber(PatientLoyaltyCardMapperDto dto)
			throws ApplicationException;

	public Response getLoyaltiCardDetailsByPatient(PatientLoyaltyCardMapperDto dto) throws ApplicationException;

	public Response getPatientDetailsByLoyaltiCardNo(PatientLoyaltyCardMapperDto dto)throws ApplicationException;

	public Response generateOTP(PatientLoyaltyCardMapperDto dto)throws ApplicationException;
	
	public Response getLoyaltyCardPatientList(int orgId,int unitId)throws ApplicationException;
	
	public Response updatePatientByLocaltiCard(PatientLoyaltyCardMapperDto dto)
			throws ApplicationException;
	
	public Response updatePatientPatientCagtegory(PatientLoyaltyCardMapperDto dto)
			throws ApplicationException;

	public Response validateOTP(PatientLoyaltyCardMapperDto dto)throws ApplicationException;

}
