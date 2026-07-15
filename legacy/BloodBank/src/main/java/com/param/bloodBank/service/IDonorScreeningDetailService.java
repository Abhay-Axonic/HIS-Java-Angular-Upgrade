package com.param.bloodBank.service;

import com.param.bloodBank.dto.DonorScreeningDetailDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@SuppressWarnings({"rawtypes"})
public interface IDonorScreeningDetailService {

	public Response saveDonorScreeningDetail(DonorScreeningDetailDto donorScreeningDetailDto) throws ApplicationException;

	public Response updateDonorScreeningDeferralStatus(DonorScreeningDetailDto donorScreeningDetailDto) throws ApplicationException;

	public Response getDonorScreeningDetailByDonorId(Integer donorId, Integer organizationId, Integer unitId) throws ApplicationException;

	public Response searchDonor(DonorScreeningDetailDto donorScreeningDetailDto) throws ApplicationException;

	public Response getDonorRegisttrationAndScreeningDetails(DonorScreeningDetailDto donorScreeningDetailDto)throws ApplicationException;

}
