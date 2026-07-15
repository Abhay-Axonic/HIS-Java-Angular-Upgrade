package com.param.service.services;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.billing.common.ICommonConstants;
import com.param.billing.common.IError;
import com.param.global.common.Response;
import com.param.service.dao.IFundInFlowMasterDao;
import com.param.service.dto.FundInFlowDetailsDto;
import com.param.service.dto.TrustMasterDto;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({"rawtypes","unused","unchecked"})
public class FundInFlowMasterServiceImpl implements IFundInFlowMasterService, ICommonConstants, IError{
	
	@Autowired
	IFundInFlowMasterDao iFundInFlowMasterDao;
	
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	@Transactional
	public Response getTrustList(Integer orgId, Integer unitId) throws ApplicationException {
		try {
			return iFundInFlowMasterDao.getTrustList(orgId, unitId);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	}

	@Override
	@Transactional
	public Response getTrustDetailsById(TrustMasterDto trustMasterDto) throws ApplicationException {
		try {
			return iFundInFlowMasterDao.getTrustDetailsById(trustMasterDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	
	}

	@Override
	@Transactional
	public Response saveFundDeatils(FundInFlowDetailsDto fundInFlowDetailsDto) throws ApplicationException {
		// TODO Auto-generated method stub
		try {
			
			return iFundInFlowMasterDao.saveFundDeatils(fundInFlowDetailsDto);
		
//			return new Response<>(SUCCESS, null, null, null, null);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	
	}

	@Override
	@Transactional
	public Response getCollectionList(Integer orgId, Integer unitId,FundInFlowDetailsDto fundInFlowDetailsDto) throws ApplicationException {
		try {
			return iFundInFlowMasterDao.getCollectionList(orgId, unitId,fundInFlowDetailsDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	}

	@Override
	@Transactional
	public Response getFundCollectionListByTrust(Integer orgId, Integer unitId,
			FundInFlowDetailsDto fundInFlowDetailsDto) throws ApplicationException {
		try {
			return iFundInFlowMasterDao.getFundCollectionListByTrust(orgId, unitId,fundInFlowDetailsDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	}

	@Override
	@Transactional
	public Response getfundInflowDetailsByTrust(FundInFlowDetailsDto fundInFlowDetailsDto) throws ApplicationException {
		// TODO Auto-generated method stub
		try {
			return iFundInFlowMasterDao.getfundInflowDetailsByTrust(fundInFlowDetailsDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	}

	@Override
	@Transactional
	public Response getAllfundInflowDetailsByTrust(FundInFlowDetailsDto fundInFlowDetailsDto)
			throws ApplicationException {
		try {
			return iFundInFlowMasterDao.getAllfundInflowDetailsByTrust(fundInFlowDetailsDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	}

	@Override
	@Transactional
	public Response getInflowDetailsByFundInflowId(FundInFlowDetailsDto fundInFlowDetailsDto)
			throws ApplicationException {
		try {
			return iFundInFlowMasterDao.getInflowDetailsByFundInflowId(fundInFlowDetailsDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	}

}
