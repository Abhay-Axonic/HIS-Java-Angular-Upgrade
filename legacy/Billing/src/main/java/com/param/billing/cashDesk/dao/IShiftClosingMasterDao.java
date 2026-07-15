package com.param.billing.cashDesk.dao;

import com.param.billing.cashDesk.dto.ShiftClosingMasterDto;
import com.param.billing.global.model.ShiftClosingMaster;
import com.param.global.common.Response;
import in.param.common.dao.IGenericDao;

@SuppressWarnings("rawtypes")
public interface IShiftClosingMasterDao extends IGenericDao<ShiftClosingMaster, Integer>{
	
	
	Response getCashReceipts(ShiftClosingMasterDto closingMasterDto);

	Response getBankReceipts(ShiftClosingMasterDto closingMasterDto);

	Response getChequeInHand(ShiftClosingMasterDto closingMasterDto);

	Response getCardReceipts(ShiftClosingMasterDto closingMasterDto);

	Response getCardInHand(ShiftClosingMasterDto closingMasterDto);

	Response getIntrimCash(ShiftClosingMasterDto closingMasterDto);

	Response getIntrimCheque(ShiftClosingMasterDto closingMasterDto);

	Response getIntrimCard(ShiftClosingMasterDto closingMasterDto);

	Response getCashHandoverDetails(ShiftClosingMasterDto closingMasterDto);

	Response saveShiftCloseBalance(ShiftClosingMasterDto closingMasterDto);

	Response getShiftClosingBalanceSheet(ShiftClosingMasterDto closingMasterDto);

	Response saveShiftClosedetails(ShiftClosingMasterDto closingMasterDto);

	Response getRefundList(ShiftClosingMasterDto closingMasterDto);

	Response getshiftClosingRecords(ShiftClosingMasterDto closingMasterDto);

	Response validateInterimByShiftClosing(ShiftClosingMasterDto closingMasterDto);

	Response validateShiftClosingBalance(ShiftClosingMasterDto closingMasterDto);

}
