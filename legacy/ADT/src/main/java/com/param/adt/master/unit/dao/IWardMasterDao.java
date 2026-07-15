package com.param.adt.master.unit.dao;

import com.param.adt.master.unit.dto.WardGenderMapperDto;
import com.param.adt.master.unit.dto.WardMasterDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IWardMasterDao {

	Response saveWardMasterMaster(WardMasterDto wardMasterDto) throws ApplicationException;

	Response getWardMasterById(int wardId, int orgId, int unitId) throws ApplicationException;

	Response getWardMasterList(WardMasterDto wardMasterDto) throws ApplicationException;

	Response updateWardMaster(WardMasterDto wardMasterDto) throws ApplicationException;

	Response updateWardMasterStatus(WardMasterDto wardMasterDto) throws ApplicationException;

	Response getWardMasterCount(WardMasterDto wardMasterDto) throws ApplicationException;

	Response getWardMasterByName(WardMasterDto wardMasterDto) throws ApplicationException;

	Response getWardMasterByNameNotById(WardMasterDto wardMasterDto) throws ApplicationException;

	Response getActiveWardMasterList(WardMasterDto wardMasterDto)throws ApplicationException;

	Response getWardListNotMappedToNursingStation(WardMasterDto wardMasterDto) throws ApplicationException;

	Response saveWardGenderMapper(WardGenderMapperDto wardGenderMapperDto)throws ApplicationException;

	Response getGenderListByWardId(int wardId)throws ApplicationException;

	Response updateWardGenderStatus(int wardId)throws ApplicationException;

	Response getActiveWardListForER(WardMasterDto wardMasterDto)throws ApplicationException;

	Response getWardForDirectTransfer(int genderId, int visitTypeId, int unitId);

	Response getWardByVisitType(WardMasterDto wardMasterDto)throws ApplicationException;
}
