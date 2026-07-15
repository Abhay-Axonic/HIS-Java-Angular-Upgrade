package com.param.bloodBank.dao;

import com.param.bloodBank.dto.BloodBagCollectionDto;
import com.param.bloodBank.dto.DonorScreeningDetailDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@SuppressWarnings({"rawtypes"})
public interface IDonorScreeningDetailDao {

	public Response saveDonorScreeningDetail(DonorScreeningDetailDto donorScreeningDetailDto) throws ApplicationException;

	public Response saveDonorScreeningMapper(DonorScreeningDetailDto donorScreeningDetailDto) throws ApplicationException;

	public Response updateDonorScreeningDeferralStatus(DonorScreeningDetailDto donorScreeningDetailDto) throws ApplicationException;

	public Response getDonorScreeningDetailByDonorId(Integer donorId, Integer organizationId, Integer unitId) throws ApplicationException;

	public Response searchDonor(DonorScreeningDetailDto donorScreeningDetailDto) throws ApplicationException;

	public Response registreredDonorSearch(DonorScreeningDetailDto donorScreeningDetailDto) throws ApplicationException;

	public Response updateScreeningFlag(DonorScreeningDetailDto donorScreeningDetailDto) throws ApplicationException;

	public Response getOnlyAcceptedDonorscreening(BloodBagCollectionDto bloodBagCollectionDto)throws ApplicationException;

	public Response getRegistreredAutologousDonor(BloodBagCollectionDto bloodBagCollectionDto)throws ApplicationException;

}
