package com.param.global.unit.service;

import in.param.exception.ApplicationException;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.unit.dto.WingMasterDto;

@SuppressWarnings("rawtypes")
@Transactional
public interface IWingMasterService {

	
	public Response getWingMasterListByOrgAndUnit(WingMasterDto wingMasterDto)throws ApplicationException;

	public Response saveWingMaster(WingMasterDto wingMasterDto) throws ApplicationException;

	public Response updateWingMaster(WingMasterDto wingMasterDto) throws ApplicationException;

	public Response updateWingMasterStatus(WingMasterDto wingMasterDto) throws ApplicationException;

	public Response getWingMasterList(WingMasterDto wingMasterDto) throws ApplicationException;

	public Response getWingMasterListCount(WingMasterDto wingMasterDto) throws ApplicationException;

	public Response getWingMasterListById(Integer wingId) throws ApplicationException;
}
