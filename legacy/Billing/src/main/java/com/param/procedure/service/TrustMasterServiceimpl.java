package com.param.procedure.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.billing.common.ICommonConstants;
import com.param.billing.common.IError;
import com.param.global.common.Response;
import com.param.procedure.dao.ITrustMasterDao;
import com.param.service.dto.TrustMasterDto;

import in.param.exception.ApplicationException;



@Service
@SuppressWarnings({ "rawtypes","unchecked" })
public class TrustMasterServiceimpl implements ITrustMasterService, ICommonConstants, IError{

	@Autowired
	private ITrustMasterDao iTrustMasterDao;

	final static Logger logger = Logger.getLogger(TrustMasterServiceimpl.class);

	@Override
	public Response saveTrust(TrustMasterDto trustMasterDto) throws ApplicationException {

		try{
			
	    iTrustMasterDao.saveTrust(trustMasterDto);
		
			return new Response<>(SUCCESS, null, null, null, null);
		}catch(Exception e){
			throw e;
		}
	}

	
	@Override
	public Response getTrustMasterById(Integer orgId, Integer unitId, Integer trustId) throws ApplicationException {
		try {
			return iTrustMasterDao.getTrustMasterById(orgId, unitId, trustId);
		} catch (Exception e) {
			
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}


	@Override
	public Response updateTrustMaster(TrustMasterDto trustMasterDto) throws ApplicationException {
		try{
			
		    iTrustMasterDao.updateTrustMaster(trustMasterDto);
			
				return new Response<>(SUCCESS, null, null, null, null);
			}catch(Exception e){
				throw e;
			}
		}

	@Override
	public Response activeInactiveTrustMaster(Integer orgId, Integer trustId, Character trustStatus)
			throws ApplicationException {
		try {
			return iTrustMasterDao.activeInactiveTrustMaster(orgId, trustId, trustStatus);
		} catch (Exception e) {
			
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}

	@Override
	public Response listTrustMaster(TrustMasterDto trustMasterDto)
			throws ApplicationException {
		try {
			return iTrustMasterDao.listTrustMaster(trustMasterDto);
		} catch (Exception e) {
			
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}


	@Override
	public Response<TrustMasterDto, Integer> getTotalTrustMasterRecord(TrustMasterDto trustMasterDto)
			throws ApplicationException {
		try {
			return iTrustMasterDao.getTotalTrustMasterRecord(trustMasterDto);
		} catch (Exception e) {
			
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}

	@Override
	public Response getActiveTrustList(TrustMasterDto trustMasterDto) throws ApplicationException {
		try {
			return iTrustMasterDao.getActiveTrustList(trustMasterDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@Override
	public Response getVisitType(Integer trustId,Integer orgId, Integer unitId ) throws ApplicationException {
		try {
			return iTrustMasterDao.getVisitType(trustId,orgId, unitId );
		} catch (Exception e) {
			
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}
	
	}
