package com.param.global.unit.service;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.unit.dao.ICounterMasterDao;
import com.param.global.unit.dto.CounterMasterDto;

import in.param.exception.ApplicationException;

@SuppressWarnings({"rawtypes"})
@Service
public class CounterMasterServiceImpl implements ICounterMasterService, ICommonConstants{

	private static final Logger logger = Logger.getLogger(CounterMasterServiceImpl.class);

	@Autowired
	ICounterMasterDao iCounterMasterDao;

	@Override
	@Transactional
	public Response saveCounterMaster(CounterMasterDto counterMasterDto) throws ApplicationException {

		Response res=iCounterMasterDao.getCounterMasterByName(counterMasterDto);
		try {
			if(res.getListObject()!=null && res.getListObject().size()>0){

				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				return iCounterMasterDao.saveCounterMaster(counterMasterDto);
			}

		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}


	@Override
	@Transactional
	public Response updateCounterMaster(CounterMasterDto counterMasterDto) throws ApplicationException {

		Response res=iCounterMasterDao.getCounterMasterByNameNotId(counterMasterDto);
		try {
			if(res.getListObject()!=null && res.getListObject().size()>0){

				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				return iCounterMasterDao.updateCounterMaster(counterMasterDto);
			}

		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response updateCounterMasterStatus(CounterMasterDto counterMasterDto) throws ApplicationException {
		try {
				return iCounterMasterDao.updateCounterMasterStatus(counterMasterDto);

		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}


	@Override
	@Transactional
	public Response getCounterMasterById(Integer counterId, Integer organizationId, Integer unitId)
			throws ApplicationException {
		try {
			return iCounterMasterDao.getCounterMasterById(counterId, organizationId, unitId);

		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getCounterMasterList(CounterMasterDto counterMasterDto) throws ApplicationException {
		try {
			return iCounterMasterDao.getCounterMasterList(counterMasterDto);

		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getCounterMasterCount(CounterMasterDto counterMasterDto) throws ApplicationException {
		try {
			return iCounterMasterDao.getCounterMasterCount(counterMasterDto);

		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getActiveCounterMaster(Integer organizationId, Integer unitId) throws ApplicationException {
		try {
			return iCounterMasterDao.getActiveCounterMaster(organizationId, unitId);

		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getCounterMasterByName(CounterMasterDto counterMasterDto) throws ApplicationException {
		try {
			return iCounterMasterDao.getCounterMasterByName(counterMasterDto);

		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getCounterMasterByNameNotId(CounterMasterDto counterMasterDto) throws ApplicationException {
		try {
			return iCounterMasterDao.getCounterMasterByNameNotId(counterMasterDto);

		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}


	@Override
	@Transactional
	public Response getCountersList(Integer organizationId, Integer unitId) throws ApplicationException {
		try {
			return iCounterMasterDao.getCountersList(organizationId, unitId);

		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

}
