package com.param.billing.cashDesk.service;

import com.param.billing.cashDesk.dto.ShiftClosingMasterDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IShiftClosingMasterService {

	
	Response getCashReceipts(ShiftClosingMasterDto closingMasterDto)throws ApplicationException;

	Response getBankReceipts(ShiftClosingMasterDto closingMasterDto)throws ApplicationException;

	Response getChequeInHand(ShiftClosingMasterDto closingMasterDto)throws ApplicationException;

	Response getCardReceipts(ShiftClosingMasterDto closingMasterDto)throws ApplicationException;

	Response getCardInHand(ShiftClosingMasterDto closingMasterDto)throws ApplicationException;

	Response getIntrimCash(ShiftClosingMasterDto closingMasterDto)throws ApplicationException;

	Response getIntrimCheque(ShiftClosingMasterDto closingMasterDto)throws ApplicationException;

	Response getIntrimCard(ShiftClosingMasterDto closingMasterDto)throws ApplicationException;

	Response getCashHandoverDetails(ShiftClosingMasterDto closingMasterDto)throws ApplicationException;

	Response saveShiftCloseBalance(ShiftClosingMasterDto closingMasterDto)throws ApplicationException;

	Response getShiftClosingBalanceSheet(ShiftClosingMasterDto closingMasterDto)throws ApplicationException;

	Response saveShiftClosedetails(ShiftClosingMasterDto closingMasterDto)throws ApplicationException;

	Response getRefundList(ShiftClosingMasterDto closingMasterDto)throws ApplicationException;

	Response getshiftClosingRecords(ShiftClosingMasterDto closingMasterDto)throws ApplicationException;

	Response validateInterimByShiftClosing(ShiftClosingMasterDto closingMasterDto)throws ApplicationException;

	Response validateShiftClosingBalance(ShiftClosingMasterDto closingMasterDto)throws ApplicationException;


}
