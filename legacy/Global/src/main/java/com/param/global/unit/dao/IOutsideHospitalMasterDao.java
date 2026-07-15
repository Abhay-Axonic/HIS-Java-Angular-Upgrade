package com.param.global.unit.dao;

import com.param.global.common.Response;
import com.param.global.unit.dto.OutsideHospitalMasterDto;

import in.param.exception.ApplicationException;

@SuppressWarnings({"rawtypes"})

public interface IOutsideHospitalMasterDao {

	public Response saveOutsideHospitalMaster(OutsideHospitalMasterDto outsideHospitalMasterDto) throws ApplicationException;

	public Response getOutsideHospitalMasterByName(OutsideHospitalMasterDto outsideHospitalMasterDto) throws ApplicationException;

	public Response getOutsideHospitalMasterByNameNotId(OutsideHospitalMasterDto outsideHospitalMasterDto) throws ApplicationException;

	public Response updateOutsideHospitalMaster(OutsideHospitalMasterDto outsideHospitalMasterDto) throws ApplicationException;

	public Response updateOutsideHospitalMasterStatus(OutsideHospitalMasterDto outsideHospitalMasterDto) throws ApplicationException;

	public Response getOutsideHospitalMasterById(Integer outsideHospitalId, Integer organizationId, Integer unitId)throws ApplicationException;

	public Response getOutsideHospitalMasterCount(OutsideHospitalMasterDto outsideHospitalMasterDto)throws ApplicationException;

	public Response getActiveOutsideHospitalMaster(Integer organizationId)throws ApplicationException;

	public Response getOutsideHospitalMasterList(OutsideHospitalMasterDto outsideHospitalMasterDto)throws ApplicationException;
}
