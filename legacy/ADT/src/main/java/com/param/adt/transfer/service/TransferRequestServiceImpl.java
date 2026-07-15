package com.param.adt.transfer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.adt.common.ICommonConstants;
import com.param.adt.common.Response;
import com.param.adt.templet.dto.AcceptedTransferCareDto;
import com.param.adt.transfer.dao.ITransferRequestDao;
import com.param.adt.transfer.dto.TransferDto;

import in.param.exception.ApplicationException;
@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class TransferRequestServiceImpl implements ITransferRequestService,ICommonConstants{

	@Autowired
	ITransferRequestDao iTransferRequestDao;
	
	
	@Override
	public Response getTransferRequestList(TransferDto transferDto) throws ApplicationException {
		try {
			return iTransferRequestDao.getTransferRequestList(transferDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}


	@Override
	public Response saveTransferRequest(TransferDto transferDto) throws ApplicationException {
		try {
			return iTransferRequestDao.saveTransferRequest(transferDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}


	@Override
	public Response getTransferRequestListByAdmissionId(TransferDto transferDto) {
		try {
			return iTransferRequestDao.getTransferRequestListByAdmissionId(transferDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}


	@Override
	public Response acceptedTransferOfCareList(AcceptedTransferCareDto acceptedTransferCareDto) throws ApplicationException {
		try {
			return iTransferRequestDao.acceptedTransferOfCareList(acceptedTransferCareDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}


	@Override
	public Response updateclinicatnotes(AcceptedTransferCareDto acceptedTransferCareDto) throws ApplicationException {
		try {
			return iTransferRequestDao.updateclinicatnotes(acceptedTransferCareDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}


	@Override
	public Response allTransferOfcareNotes(AcceptedTransferCareDto acceptedTransferCareDto)
			throws ApplicationException {
		try {
			return iTransferRequestDao.allTransferOfcareNotes(acceptedTransferCareDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}


	@Override
	public Response saveLaborTransferRequest(TransferDto transferDto)
			throws ApplicationException {
		try {
			return iTransferRequestDao.saveLaborTransferRequest(transferDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}


	@Override
	public Response checkTransferRequestExistence(TransferDto transferDto) throws ApplicationException {
		try {
			return iTransferRequestDao.checkTransferRequestExistence(transferDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
}
