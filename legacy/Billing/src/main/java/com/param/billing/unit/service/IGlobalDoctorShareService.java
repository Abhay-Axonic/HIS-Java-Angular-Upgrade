package com.param.billing.unit.service;

import javax.transaction.Transactional;

import com.param.billing.unit.dto.DoctorShareReqDto;
import com.param.billing.unit.dto.GlobalDocShareServiceRateDto;
import com.param.billing.unit.dto.IndividualDoctorShareReqDto;
import com.param.global.common.Response;
import com.param.service.dto.CommonDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")

public interface IGlobalDoctorShareService {
	
	
	@Transactional
	public  Response saveGlobalDoctorShare(DoctorShareReqDto doctorShareReqDto)throws ApplicationException;
	
	@Transactional
	public  Response getDoctorGradeMaster(Integer orgId, Integer orgUnitId)throws ApplicationException;


	@Transactional
	public Response<DoctorShareReqDto, Integer> getGlobalDoctorShareDetailsByDepartmentId(
			DoctorShareReqDto doctorShareReqDto);

	@Transactional
	public Response getGlobalDoctorShareMasterList(DoctorShareReqDto doctorShareReqDto);

	@Transactional
	public Response getRatesForSelectedService(GlobalDocShareServiceRateDto doctorShareReqDto);

	@Transactional
	public Response<DoctorShareReqDto, Integer> getIndividualDoctorReferralDetailsByDepartmentId(
			IndividualDoctorShareReqDto doctorShareReqDto);
	
	@Transactional
	public Response<DoctorShareReqDto, Integer> getIndividualDoctorReferralDetailsByReferralId(
			IndividualDoctorShareReqDto doctorShareReqDto);
	@Transactional
	public Response<CommonDto, Integer> getGlobalDoctorShareDepartmentMasterListSearch(
			DoctorShareReqDto doctorShareReqDto);
	@Transactional
	public Response<DoctorShareReqDto, Integer> getIndividualDoctorReferralDetailsByDepartmentIdSearch(
			IndividualDoctorShareReqDto doctorShareReqDto);
	@Transactional
	public Response<DoctorShareReqDto, Integer> duplicateCheckForGlobalDoctorShare(DoctorShareReqDto doctorShareReqDto);

	public Response<DoctorShareReqDto, Integer> updateStatusByReferralId(IndividualDoctorShareReqDto doctorShareReqDto);
	
	
	
	/*@Transactional
	public Response searchCompanyContract(ServiceForCompnayContarctReqDto reqDto) throws ServiceException;*/
}
