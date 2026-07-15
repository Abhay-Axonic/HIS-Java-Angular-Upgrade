package com.param.bloodBank.service;

import org.springframework.transaction.annotation.Transactional;

import com.param.bloodBank.dao.DonorScreeningMasterDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@Transactional
@SuppressWarnings("rawtypes")
public interface IDonorScreeningMasterService {

	Response saveDonorScreenigMaster(DonorScreeningMasterDto donorScreeningMasterDto) throws ApplicationException;

	Response getAllDonorScreeningList(DonorScreeningMasterDto donorScreeningMasterDto) throws ApplicationException;

	Response updateDonorScreeningMaster(DonorScreeningMasterDto donorScreeningMasterDto) throws ApplicationException;

	Response updateDonorScreeningMasterStatus(DonorScreeningMasterDto donorScreeningMasterDto) throws ApplicationException;

	Response getDonorScreeningListByGenderId(DonorScreeningMasterDto donorScreeningMasterDto)throws ApplicationException;

	Response getDefferedTypeMaster()throws ApplicationException;

	Response getAllDonorScreeningListPaginationList(DonorScreeningMasterDto donorScreeningMasterDto)throws ApplicationException;

	Response getAllDonorScreeningListCount(DonorScreeningMasterDto donorScreeningMasterDto)throws ApplicationException;

}
