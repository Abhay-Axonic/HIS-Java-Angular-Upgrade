package com.param.global.unit.dao;

import com.param.global.common.Response;
import com.param.global.model.MRoomRentChargesConfig;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IMRoomRentChargesConfigDao extends IGenericDao<MRoomRentChargesConfig, Integer>{
	public Response getRoomRentChargesByOrgUnitBedCat(int orgId, int unitId, int bedCategoryId)throws ApplicationException;
}
