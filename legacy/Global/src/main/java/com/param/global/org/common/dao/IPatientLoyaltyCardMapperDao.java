package com.param.global.org.common.dao;

import com.param.entity.org.model.PatientLoyaltyCardMapper;
import com.param.global.common.Response;
import com.param.global.org.common.dto.PatientLoyaltyCardMapperDto;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings({"rawtypes"})
public interface IPatientLoyaltyCardMapperDao extends IGenericDao<PatientLoyaltyCardMapper, Integer>{

	
	public Response savePatientLoyaltiCardMapper(PatientLoyaltyCardMapperDto dto) throws ApplicationException;

	public Response getAvailableActiveDepositWithCardNumber(PatientLoyaltyCardMapperDto dto)
			throws ApplicationException;
	
	public Response updatePatientByLocaltiCard(PatientLoyaltyCardMapperDto dto)
			throws ApplicationException;
	
	public Response updatePatientPatientCagtegory(PatientLoyaltyCardMapperDto dto)
			throws ApplicationException;
	
	public Response updateLoyaltyCardAmount(PatientLoyaltyCardMapperDto dto)throws ApplicationException;
	
	public Response updateLoyaltyCardAmountByCard(PatientLoyaltyCardMapperDto dto)throws ApplicationException;
	
	public Response getLoyaltiCardDetailsByPatient(PatientLoyaltyCardMapperDto dto) throws ApplicationException;

	public Response getPatientDetailsByLoyaltiCardNo(PatientLoyaltyCardMapperDto dto)throws ApplicationException;

//	public Response generateOTP(PatientLoyaltyCardMapperDto dto)throws ApplicationException;
	
	public Response updateOTP(PatientLoyaltyCardMapperDto dto)throws ApplicationException;
	
	public Response getLoyaltyCardPatientList(int orgId,int unitId) throws ApplicationException;
	
	public Response getLoyaltyCardConfiguration(PatientLoyaltyCardMapperDto dto) throws ApplicationException;

	public Response getLoyaltiDetailsByMapprId(PatientLoyaltyCardMapperDto dto)throws ApplicationException;

	public Response updateOTPCount(PatientLoyaltyCardMapperDto dto, int i)throws ApplicationException;

	public Response resetOTPDetails(PatientLoyaltyCardMapperDto dto)throws ApplicationException;

	public Response addUpdatedLoyaltyCardAmount(PatientLoyaltyCardMapperDto loyaltyCarddto) throws ApplicationException;

	public Response getUnitName(PatientLoyaltyCardMapperDto dto)throws ApplicationException;

	public Response unBlockOTPDetails(PatientLoyaltyCardMapperDto dto)throws ApplicationException;

}
