package com.param.billing.cashDesk.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.billing.cashDesk.dao.ICashHandoverMasterDao;
import com.param.billing.cashDesk.dao.IShiftClosingMasterDao;
import com.param.billing.cashDesk.dto.ShiftClosingMasterDto;
import com.param.billing.common.ICommonConstants;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings("rawtypes")
public class ShiftClosingMasterServiceImpl implements IShiftClosingMasterService,ICommonConstants {
	
	@Autowired
	private IShiftClosingMasterDao closingMasterDao;
	
	@Autowired
	private ICashHandoverMasterDao iCashHandoverMasterDao;
	
	@Override
	@Transactional
	public Response getCashReceipts(ShiftClosingMasterDto closingMasterDto) {
		
		try {
			
			return closingMasterDao.getCashReceipts(closingMasterDto);
	
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
		}
	
	}

	@Override
	@Transactional
	public Response getBankReceipts(ShiftClosingMasterDto closingMasterDto) {
		try {
			
			return closingMasterDao.getBankReceipts(closingMasterDto);
	
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
		}
	}

	@Override
	@Transactional
	public Response getChequeInHand(ShiftClosingMasterDto closingMasterDto) {
		try {
			
			return closingMasterDao.getChequeInHand(closingMasterDto);
	
		} catch (Exception e) {
			
			throw e;
		//	return new Response<>(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
		}
	}

	@Override
	@Transactional
	public Response getCardReceipts(ShiftClosingMasterDto closingMasterDto) {
		try {
			
			return closingMasterDao.getCardReceipts(closingMasterDto);
	
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
		}
	}

	@Override
	@Transactional
	public Response getCardInHand(ShiftClosingMasterDto closingMasterDto) {
		try {
			
			return closingMasterDao.getCardInHand(closingMasterDto);
	
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
		}
	}

	@Override
	@Transactional
	public Response getIntrimCash(ShiftClosingMasterDto closingMasterDto) {
		try {
			
			return closingMasterDao.getIntrimCash(closingMasterDto);
	
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
		}
	}

	@Override
	@Transactional
	public Response getIntrimCheque(ShiftClosingMasterDto closingMasterDto) {
		try {
			
			return closingMasterDao.getIntrimCheque(closingMasterDto);
	
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
		}

	}

	@Override
	@Transactional
	public Response getIntrimCard(ShiftClosingMasterDto closingMasterDto) {
		try {
			
			return closingMasterDao.getIntrimCard(closingMasterDto);
	
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
		}
	}

	@Override
	@Transactional
	public Response getCashHandoverDetails(ShiftClosingMasterDto closingMasterDto) {
		try {
			
			return closingMasterDao.getCashHandoverDetails(closingMasterDto);
	
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
		}
	}

	@Override
	@Transactional
	public Response saveShiftCloseBalance(ShiftClosingMasterDto closingMasterDto) {
		try {
			
			return closingMasterDao.saveShiftCloseBalance(closingMasterDto);
	
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
		}
	}

	@Override
	@Transactional
	public Response getShiftClosingBalanceSheet(ShiftClosingMasterDto closingMasterDto) {
		try {
			
			return closingMasterDao.getShiftClosingBalanceSheet(closingMasterDto);
	
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
		}
	}

	@Override
	@Transactional
	public Response saveShiftClosedetails(ShiftClosingMasterDto closingMasterDto) {
		try {
			
			Response res =  closingMasterDao.saveShiftClosedetails(closingMasterDto);
			
			if(res.getStatus().equals(SUCCESS)) {
				
				iCashHandoverMasterDao.updateShiftCloseFlag(closingMasterDto.getHandoverBy());
				
			}else {
				
				return new Response<>(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
			}
			return res;
			
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
		}
	}

	@Override
	@Transactional
	public Response getRefundList(ShiftClosingMasterDto closingMasterDto) {
		try {
			
			return closingMasterDao.getRefundList(closingMasterDto);
	
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
		}
	}

	@Override
	@Transactional
	public Response getshiftClosingRecords(ShiftClosingMasterDto closingMasterDto) {
		try {
			
			return closingMasterDao.getshiftClosingRecords(closingMasterDto);
	
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
		}
	}

	@Override
	@Transactional
	public Response validateInterimByShiftClosing(ShiftClosingMasterDto closingMasterDto) throws ApplicationException {
		try {
			
			return closingMasterDao.validateInterimByShiftClosing(closingMasterDto);
	
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
		}
	}

	@Override
	@Transactional
	public Response validateShiftClosingBalance(ShiftClosingMasterDto closingMasterDto) throws ApplicationException {
		try {
			
			return closingMasterDao.validateShiftClosingBalance(closingMasterDto);
	
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
		}
	}

}