package com.param.adt.transfer.service;

import javax.transaction.Transactional;

import com.param.adt.common.Response;
import com.param.adt.master.global.dto.BedMasterDto;
import com.param.adt.transfer.dto.TransferDto;

import in.param.exception.ApplicationException;
import org.springframework.web.bind.annotation.RequestBody;

@SuppressWarnings("rawtypes")
public interface IBedToBedTransferService {
	
	@Transactional
	Response getInitiateTransferRequestList(TransferDto transferDto) throws ApplicationException;

	@Transactional
	Response getInitiateTransferRequestCount(TransferDto transferDto);

	@Transactional
	Response getBedListByWardId(BedMasterDto bedMasterDto)throws ApplicationException;
	@Transactional
	Response getTransferListByAdmissionId(Integer admissionId) throws ApplicationException;
}
