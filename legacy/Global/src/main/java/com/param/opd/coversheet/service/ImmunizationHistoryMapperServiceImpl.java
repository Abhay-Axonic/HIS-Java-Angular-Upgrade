package com.param.opd.coversheet.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.param.exception.ApplicationException;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.opd.coversheet.dao.IImmunizationHistoryMapperDao;
import com.param.opd.coversheet.dto.ImmunizationHistoryMapperDto;
import com.param.opd.coversheet.model.ImmunizationHistoryMapper;

@Service
@SuppressWarnings({ "unused", "unchecked" })
public class ImmunizationHistoryMapperServiceImpl implements
		IImmunizationHistoryMapperService,ICommonConstants {

	@Autowired
	private IImmunizationHistoryMapperDao iImmunizationHistoryMapperDao;
	
	@Override
	@Transactional
	public Response saveImmunizationHistoryMapper(
			ImmunizationHistoryMapperDto immunizationHistoryMapperDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		Response immunizationHistoryMapperDtoResponse = iImmunizationHistoryMapperDao.updateImmunizationHistoryForOldData(immunizationHistoryMapperDto);
		if(immunizationHistoryMapperDtoResponse.getStatus()==SUCCESS){
			return iImmunizationHistoryMapperDao.saveImmunizationHistoryMapper(immunizationHistoryMapperDto);
		}else{
			return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
		}
		
	}

	@Override
	@Transactional
	public Response getListOfImmunizationHistoryMapper(
			ImmunizationHistoryMapperDto immunizationHistoryMapperDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iImmunizationHistoryMapperDao.getListOfImmunizationHistoryMapper(immunizationHistoryMapperDto);
	}

	@Override
	@Transactional
	public Response getOldImmunizationHistoryMapper(
			ImmunizationHistoryMapperDto immunizationHistoryMapperDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iImmunizationHistoryMapperDao.getOldImmunizationHistoryMapper(immunizationHistoryMapperDto);
	}

	@Override
	@Transactional
	public Response getCurrentImmunizationHistoryMapper(
			ImmunizationHistoryMapperDto immunizationHistoryMapperDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iImmunizationHistoryMapperDao.getCurrentImmunizationHistoryMapper(immunizationHistoryMapperDto);
	}

	@Override
	@Transactional
	public Response updateImmunizationHistoryMapper(
			ImmunizationHistoryMapperDto immunizationHistoryMapperDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iImmunizationHistoryMapperDao.updateImmunizationHistoryMapper(immunizationHistoryMapperDto);
	}

	@Override
	@Transactional
	public Response updateSaveNurseReviewedFlag(
			ImmunizationHistoryMapper immunizationHistoryMapper)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iImmunizationHistoryMapperDao.updateSaveNurseReviewedFlag(immunizationHistoryMapper);
	}

	@Override
	public Response getCurrentImmunizationHistoryMapperForCoversheet(
			ImmunizationHistoryMapperDto immunizationHistoryMapperDto) {
		// TODO Auto-generated method stub
		return iImmunizationHistoryMapperDao.getCurrentImmunizationHistoryMapperForCoversheet(immunizationHistoryMapperDto);
	}

}
