package com.param.billing.cashDesk.dao;

import java.util.List;

import com.param.billing.cashDesk.dto.CashHandoverDetailsDto;
import com.param.billing.cashDesk.dto.DenominationMasterDto;
import com.param.billing.global.model.CashHandoverMaster;
import com.param.global.common.Response;
import com.param.global.dto.DenominationDetailsMasterDto;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface ICashHandoverMasterDao extends IGenericDao<CashHandoverMaster, Integer>{
	
	Response getListOfDenominations(DenominationMasterDto denominationMasterDto)throws ApplicationException;

	Response saveCashHandoverMaster(CashHandoverDetailsDto cashHandoverDetailsDto)throws ApplicationException;

	Response validateUser(CashHandoverDetailsDto cashHandoverDetailsDto)throws ApplicationException;

	Response saveDenominationmaster(List<DenominationDetailsMasterDto> denominationDetailsMasterDto)throws ApplicationException;

	Response getListOfDenominationDetails(DenominationDetailsMasterDto denominationDetailsMasterDto)throws ApplicationException;

	Response getcashOpeningTransactionList(CashHandoverDetailsDto cashHandoverDetailsDto)throws ApplicationException;

	Response checkShiftClosingStatus(CashHandoverDetailsDto cashHandoverDetailsDto)throws ApplicationException;

	Response updateShiftCloseFlag(Integer handoverBy)throws ApplicationException;

	Response updateStatusToComplete(CashHandoverDetailsDto cashHandoverDetailsDto)throws ApplicationException;

	Response inActiveAndActiveEntry(CashHandoverDetailsDto cashHandoverDetailsDto)throws ApplicationException;


}
