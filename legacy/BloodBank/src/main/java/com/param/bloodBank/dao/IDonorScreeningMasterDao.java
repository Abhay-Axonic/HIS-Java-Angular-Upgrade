package com.param.bloodBank.dao;

import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IDonorScreeningMasterDao {

	Response saveDonorScreenigMaster(DonorScreeningMasterDto donorScreeningMasterDto) throws ApplicationException;

	Response getAllDonorScreeningList(DonorScreeningMasterDto donorScreeningMasterDto) throws ApplicationException;

	Response updateDonorScreeningMaster(DonorScreeningMasterDto donorScreeningMasterDto) throws ApplicationException;

	Response updateDonorScreeningMasterStatus(DonorScreeningMasterDto donorScreeningMasterDto)  throws ApplicationException;

	Response getDonorScreeningListByGenderId(DonorScreeningMasterDto donorScreeningMasterDto)throws ApplicationException;

	Response getDefferedTypeMaster()throws ApplicationException;

	Response getAllDonorScreeningListPaginationList(DonorScreeningMasterDto donorScreeningMasterDto)throws ApplicationException;

	Response getAllDonorScreeningListCount(DonorScreeningMasterDto donorScreeningMasterDto)throws ApplicationException;

}
