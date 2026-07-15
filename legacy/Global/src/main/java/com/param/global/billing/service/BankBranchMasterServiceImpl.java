package com.param.global.billing.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.billing.dao.IBankBranchMasterDao;
import com.param.global.billing.dto.BankBranchMasterDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings("rawtypes")
public class BankBranchMasterServiceImpl implements IBankBranchMasterService, ICommonConstants {

	final static Logger logger = Logger.getLogger(BankBranchMasterServiceImpl.class);

	@Autowired
	IBankBranchMasterDao iBankBranchMasterDao;

	@Override
	public Response saveBankBranchMaster(BankBranchMasterDto bankBranchMasterDto) throws ApplicationException {
		Response response = iBankBranchMasterDao.getBankBranchMasterByName(bankBranchMasterDto);
		try {
			if (response.getListObject().size() > 0 && response.getListObject() != null) {
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else {
				return iBankBranchMasterDao.saveBankBranchMaster(bankBranchMasterDto);
			}
		} catch (Exception e) {
			logger.error("saveBankBranchMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getBankBranchMasterById(int branchId, int orgnisationId) throws ApplicationException {
		try {
			return iBankBranchMasterDao.getBankBranchMasterById(branchId, orgnisationId);

		} catch (Exception e) {
			logger.error("getBankBranchMasterById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getBankBranchMasterList(BankBranchMasterDto bankBranchMasterDto) throws ApplicationException {
		try {
			return iBankBranchMasterDao.getBankBranchMasterList(bankBranchMasterDto);
		} catch (Exception e) {
			logger.error("getBankBranchMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateBankBranchMaster(BankBranchMasterDto bankBranchMasterDto) throws ApplicationException {
		Response response = iBankBranchMasterDao.getBankBranchMasterByNameNotId(bankBranchMasterDto);
		try {
			if(response.getListObject().size()>0&&response.getListObject()!=null){
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
			return iBankBranchMasterDao.updateBankBranchMaster(bankBranchMasterDto);
			}

		} catch (Exception e) {
			logger.error("updateBankBranchMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateBankBranchMasterStatus(BankBranchMasterDto bankBranchMasterDto) throws ApplicationException {
		try {
			return iBankBranchMasterDao.updateBankBranchMasterStatus(bankBranchMasterDto);
		} catch (Exception e) {
			logger.error("updateBankBranchMasterStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getBankBranchMasterCount(BankBranchMasterDto bankBranchMasterDto) throws ApplicationException {
		try {
			return iBankBranchMasterDao.getBankBranchMasterCount(bankBranchMasterDto);

		} catch (Exception e) {
			logger.error("getBankBranchMasterCount failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getActiveBankBranchMasterList(BankBranchMasterDto bankBranchMasterDto) throws ApplicationException {
		try {
			return iBankBranchMasterDao.getActiveBankBranchMasterList(bankBranchMasterDto);

		} catch (Exception e) {
			logger.error("getActiveBankBranchMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

}
