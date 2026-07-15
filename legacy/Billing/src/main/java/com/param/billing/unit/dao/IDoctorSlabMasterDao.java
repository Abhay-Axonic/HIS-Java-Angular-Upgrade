package com.param.billing.unit.dao;

import com.param.billing.unit.dto.DoctorSlabDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IDoctorSlabMasterDao {
	
	Response doctorSlabSave(DoctorSlabDto dto)throws ApplicationException;

	Response getdoctorSlabList(DoctorSlabDto doctorSlabDto)throws ApplicationException;

	Response getdoctorSlabListByDoctorId(DoctorSlabDto doctorSlabDto)throws ApplicationException;

	Response updateSlabStatusByDoctorId(DoctorSlabDto doctorSlabDto)throws ApplicationException;

}
