package com.param.billing.ipd.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.global.billing.controller.BillingException;
import com.param.global.billing.dao.IpdChargesDao;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

@Service
@SuppressWarnings("rawtypes")
public class IpdChargesServiceImpl implements IpdChargesService, ICommonConstants{

	@Autowired
	private IpdChargesDao ipdChargesDao;
	
	@Override
	@Transactional
	public Response getAdmissionsById(int admissionId) throws BillingException {
		try{
			return ipdChargesDao.getAdmissionsById(admissionId);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getAdmissionsByIdForUpdatePayee(int admissionId) throws BillingException {
		try{
			return ipdChargesDao.getAdmissionsByIdForUpdatePayee(admissionId);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getAdmissionsDetailsByAdmissionIdForCancelledBills(int admissionId) {
		try{
			return ipdChargesDao.getAdmissionsDetailsByAdmissionIdForCancelledBills(admissionId);
		}catch(Exception e){
			
			throw e;
		}
	}

}
