package com.param.billing.unit.service;

import javax.transaction.Transactional;

import com.param.billing.unit.dto.IndividualDoctorShareReqDto;
import com.param.global.common.Response;
import com.param.service.dto.CommonDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")

public interface IIndividualDoctorShareService {
	
	
	@Transactional
	public  Response saveIndividualDoctorShare(IndividualDoctorShareReqDto individualDoctorShareReqDto)throws ApplicationException;

	@Transactional
	public Response getDoctorShareDetailsByDepartmentIdAndDoctorId(IndividualDoctorShareReqDto doctorShareReqDto);

	@Transactional
	public Response getDoctorShareDepartmentMasterList(IndividualDoctorShareReqDto doctorShareReqDto);

	@Transactional
	public Response<IndividualDoctorShareReqDto, Integer> saveReferralShare(IndividualDoctorShareReqDto individualDoctorShareReqDto);
	@Transactional
	public Response<CommonDto, Integer> getDoctorShareDepartmentMasterListSearch(
			IndividualDoctorShareReqDto doctorShareReqDto);

	
}
