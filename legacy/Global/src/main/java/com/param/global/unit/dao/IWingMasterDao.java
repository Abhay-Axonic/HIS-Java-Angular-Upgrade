package com.param.global.unit.dao;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;

import com.param.adt.master.unit.model.WingMaster;
import com.param.global.common.Response;
import com.param.global.unit.dto.WingMasterDto;

@SuppressWarnings("rawtypes")
public interface IWingMasterDao extends IGenericDao<WingMaster, Integer>{
	
	public Response getWingMasterListByOrgAndUnit(WingMasterDto wingMasterDto)throws ApplicationException;
	

	public Response saveWingMaster(WingMasterDto wingMasterDto) throws ApplicationException;

	public Response updateWingMaster(WingMasterDto wingMasterDto) throws ApplicationException;

	public Response updateWingMasterStatus(WingMasterDto wingMasterDto) throws ApplicationException;

	public Response getWingMasterList(WingMasterDto wingMasterDto) throws ApplicationException;

	public Response getWingMasterListCount(WingMasterDto wingMasterDto) throws ApplicationException;

	public Response getWingMasterListById(Integer wingId) throws ApplicationException;
	
	public Response getWingMasterListByName(WingMasterDto wingMasterDto) throws ApplicationException;
	
	public Response getWingMasterListByNameNotById(WingMasterDto wingMasterDto) throws ApplicationException;

}
