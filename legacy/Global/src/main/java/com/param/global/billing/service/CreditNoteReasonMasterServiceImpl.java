package com.param.global.billing.service;

import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.billing.dao.ICreditNoteReasonMasterDao;
import com.param.global.billing.dto.CreditNoteReasonMasterDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;

@SuppressWarnings({"rawtypes"})
@Service
public class CreditNoteReasonMasterServiceImpl implements ICreditNoteReasonMasterService, ICommonConstants{

	final static Logger logger = Logger.getLogger(CreditNoteReasonMasterServiceImpl.class);

	@Autowired
	ICreditNoteReasonMasterDao iCreditNoteReasonMasterDao;

	@Override
	public Response saveCreditNoteReasonMaster(CreditNoteReasonMasterDto creditNoteReasonMasterDto) throws ApplicationException {
		try {
			Response response=iCreditNoteReasonMasterDao.getCreditNoteReasonMasterByName(creditNoteReasonMasterDto);

			if(response.getListObject().size()>0 && response.getListObject()!=null){
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				return iCreditNoteReasonMasterDao.saveCreditNoteReasonMaster(creditNoteReasonMasterDto);
			}

		} catch (Exception e) {
			logger.error("saveCreditNoteReasonMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response updateCreditNoteReasonMaster(CreditNoteReasonMasterDto creditNoteReasonMasterDto) throws ApplicationException {
		try {
			Response response=iCreditNoteReasonMasterDao.getCreditNoteReasonMasterByNameNotId(creditNoteReasonMasterDto);

			if(response.getListObject().size()>0 && response.getListObject()!=null){
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				return iCreditNoteReasonMasterDao.updateCreditNoteReasonMaster(creditNoteReasonMasterDto);
			}

		} catch (Exception e) {
			logger.error("updateCreditNoteReasonMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response updateCreditNoteReasonMasterStatus(CreditNoteReasonMasterDto creditNoteReasonMasterDto) throws ApplicationException {
		try {
			return iCreditNoteReasonMasterDao.updateCreditNoteReasonMasterStatus(creditNoteReasonMasterDto);
		} catch (Exception e) {
			logger.error("updateCreditNoteReasonMasterStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response getCreditNoteReasonMasterList(CreditNoteReasonMasterDto creditNoteReasonMasterDto) throws ApplicationException {
		try {
			return iCreditNoteReasonMasterDao.getCreditNoteReasonMasterList(creditNoteReasonMasterDto);
		} catch (Exception e) {
			logger.error("getCreditNoteReasonMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response getCreditNoteReasonMasterById(Integer creditNoteReasonId, Integer organisationId, Integer unitId) throws ApplicationException {
		try {
			return iCreditNoteReasonMasterDao.getCreditNoteReasonMasterById(creditNoteReasonId, organisationId, unitId);
		} catch (Exception e) {
			logger.error("getCreditNoteReasonMasterById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response getCreditNoteReasonMasterCount(CreditNoteReasonMasterDto creditNoteReasonMasterDto) throws ApplicationException {
		try {
			return iCreditNoteReasonMasterDao.getCreditNoteReasonMasterCount(creditNoteReasonMasterDto);
		} catch (Exception e) {
			logger.error("getCreditNoteReasonMasterCount failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response getActiveCreditNoteReasonMasterList(CreditNoteReasonMasterDto creditNoteReasonMasterDto) throws ApplicationException {
		try {
			return iCreditNoteReasonMasterDao.getActiveCreditNoteReasonMasterList(creditNoteReasonMasterDto);
		} catch (Exception e) {
			logger.error("getActiveCreditNoteReasonMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response getCreditNoteReasonMasterByName(CreditNoteReasonMasterDto creditNoteReasonMasterDto) throws ApplicationException {
		try {
			return iCreditNoteReasonMasterDao.getCreditNoteReasonMasterByName(creditNoteReasonMasterDto);
		} catch (Exception e) {
			logger.error("getCreditNoteReasonMasterByName failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response getCreditNoteReasonMasterByNameNotId(CreditNoteReasonMasterDto creditNoteReasonMasterDto) throws ApplicationException {
		try {
			return iCreditNoteReasonMasterDao.getCreditNoteReasonMasterByNameNotId(creditNoteReasonMasterDto);
		} catch (Exception e) {
			logger.error("getCreditNoteReasonMasterByNameNotId failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

}
