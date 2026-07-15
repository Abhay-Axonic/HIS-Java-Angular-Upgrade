package com.param.billing.cashDesk.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.billing.cashDesk.dao.ICashHandoverMasterDao;
import com.param.billing.cashDesk.dao.ICashInOutDao;
import com.param.billing.cashDesk.dto.CashHandoverDetailsDto;
import com.param.billing.cashDesk.dto.DenominationMasterDto;
import com.param.billing.global.model.CashHandoverMaster;
import com.param.billing.global.model.CashInOut;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.DenominationDetailsMasterDto;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({ "rawtypes",})
public  class CashHandoverMasterServiceImpl implements ICashHandoverMasterService,ICommonConstants{
	
	
	@Autowired
	private ICashHandoverMasterDao iCashHandoverMasterDao;
	
	@Autowired
	ICashInOutDao iCashInOutDao;

	@Override
	public Response getListOfDenominations(DenominationMasterDto denominationMasterDto) {
		try {
			
			return iCashHandoverMasterDao.getListOfDenominations(denominationMasterDto);
	
	} catch (Exception e) {
		
		throw e;
		//return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	}

	
	@Override
	public Response saveCashHandoverMaster(CashHandoverDetailsDto cashHandoverDetailsDto) throws ApplicationException{
		try {
			Response saveResponse = null;
				if(cashHandoverDetailsDto.getId()==null) {
			
				/*Response checkShiftResponse = iCashHandoverMasterDao.checkShiftClosingStatus(cashHandoverDetailsDto);
				
				if(checkShiftResponse.getListObject().size()>0) {
					
					return new Response<>(ERROR, COMMON_ERROR_CODE, " shift already Opened ", null, null);
					
				}else {*/
					
					saveResponse = iCashHandoverMasterDao.saveCashHandoverMaster(cashHandoverDetailsDto);
					if(saveResponse.getStatus().equals(SUCCESS)) {
					
						CashHandoverMaster mstr = (CashHandoverMaster)saveResponse.getObject();
						for (DenominationDetailsMasterDto dto : cashHandoverDetailsDto.getDenominationDetailsMasterList()) {
							dto.setCashHandoverTransactionId(mstr.getId());
						}
					 iCashHandoverMasterDao.saveDenominationmaster(cashHandoverDetailsDto.getDenominationDetailsMasterList());
					}
				/*}*/
				
				}else {
					 saveResponse = iCashHandoverMasterDao.saveCashHandoverMaster(cashHandoverDetailsDto);
					if(saveResponse.getStatus().equals(SUCCESS)) {
					
						CashHandoverMaster mstr = (CashHandoverMaster)saveResponse.getObject();
						for (DenominationDetailsMasterDto dto : cashHandoverDetailsDto.getDenominationDetailsMasterList()) {
							dto.setCashHandoverTransactionId(mstr.getId());
						}
					  iCashHandoverMasterDao.saveDenominationmaster(cashHandoverDetailsDto.getDenominationDetailsMasterList());
				}
			}
			return saveResponse;

		} catch (Exception e) {
			
			throw e;
		}
	}

	/*@Override
	@Transactional
	public Response validateUser(CashHandoverDetailsDto cashHandoverDetailsDto) {
		try {
			
			return iCashHandoverMasterDao.validateUser(cashHandoverDetailsDto);
			
			
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}*/


/*	@Override
	@Transactional
	public Response saveDenominationmaster(List<DenominationDetailsMasterDto> denominationDetailsMasterDto) {
		try {
			
			return iCashHandoverMasterDao.saveDenominationmaster(denominationDetailsMasterDto);
			
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}	
	}
*/

	@Override
	public Response getListOfDenominationDetails(DenominationDetailsMasterDto denominationDetailsMasterDto) {
		
		try {
			
			return iCashHandoverMasterDao.getListOfDenominationDetails(denominationDetailsMasterDto);
			
		} catch (Exception e) {
			
			throw e;
		}	
	}


	@Override
	public Response getcashOpeningTransactionList(CashHandoverDetailsDto cashHandoverDetailsDto) {
		try {
			
			return iCashHandoverMasterDao.getcashOpeningTransactionList(cashHandoverDetailsDto);
			
		} catch (Exception e) {
			
			throw e;
		}	
	}


	@Override
	public Response updateStatusToComplete(CashHandoverDetailsDto cashHandoverDetailsDto) throws ApplicationException {
		try {
			Response validateRes = iCashHandoverMasterDao.validateUser(cashHandoverDetailsDto);
			Response res =null;
			if(validateRes.getStatus().equals(SUCCESS)) {
				res =  iCashHandoverMasterDao.updateStatusToComplete(cashHandoverDetailsDto);
			}else {
				return new Response<>(ERROR, COMMON_ERROR_CODE, "Invalid Security Code", null, null);
			}
			return res;
		} catch (Exception e) {
			
			throw e;
		}	
	}


	@Override
	public Response saveCashInOut(CashHandoverDetailsDto cashHandoverDetailsDto) throws ApplicationException {

		try {
			
			if(cashHandoverDetailsDto.getCashTransactionTypeId() == 2){
				Response validateRes = iCashHandoverMasterDao.validateUser(cashHandoverDetailsDto);
				if(validateRes.getStatus().equals(ERROR)) {
					return new Response<>(ERROR, COMMON_ERROR_CODE, "Invalid Security Code", null, null);
				}
			}
			
					Response saveResponse = iCashInOutDao.saveCashInOut(cashHandoverDetailsDto);
					if(saveResponse.getStatus().equals(SUCCESS)) {
					
						CashInOut mstr = (CashInOut)saveResponse.getObject();
						for (DenominationDetailsMasterDto dto : cashHandoverDetailsDto.getDenominationDetailsMasterList()) {
							dto.setCashInoutId(mstr.getId());
						}
					  iCashHandoverMasterDao.saveDenominationmaster(cashHandoverDetailsDto.getDenominationDetailsMasterList());
				}
			return saveResponse;

		} catch (Exception e) {
			
			throw e;
		}
	
	}


	@Override
	public Response inActiveAndActiveEntry(CashHandoverDetailsDto cashHandoverDetailsDto) throws ApplicationException {

		try {
				return iCashHandoverMasterDao.inActiveAndActiveEntry(cashHandoverDetailsDto);
		} catch (Exception e) {
			
			throw e;
		}	
	
	}


	@Override
	public Response getcashInOutList(CashHandoverDetailsDto cashHandoverDetailsDto) throws ApplicationException {
		try {
			
			return iCashInOutDao.getcashInOutList(cashHandoverDetailsDto);
			
		} catch (Exception e) {
			
			throw e;
		}	
	}


	@Override
	public Response getUserInhandCash(CashHandoverDetailsDto cashHandoverDetailsDto) throws ApplicationException {
		try {
			
			return iCashInOutDao.getUserInhandCash(cashHandoverDetailsDto);
			
		} catch (Exception e) {
			
			throw e;
		}	
	
	}


	
}


