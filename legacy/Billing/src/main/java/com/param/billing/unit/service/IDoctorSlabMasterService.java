package com.param.billing.unit.service;

import java.util.List;

import javax.transaction.Transactional;

import com.param.billing.unit.dto.DoctorSlabDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@Transactional
@SuppressWarnings("rawtypes")
public interface IDoctorSlabMasterService {

	Response doctorSlabSave(List<DoctorSlabDto> doctorSlabDto)throws ApplicationException;

	Response getdoctorSlabList(DoctorSlabDto doctorSlabDto)throws ApplicationException;

	Response getdoctorSlabListByDoctorId(DoctorSlabDto doctorSlabDto)throws ApplicationException;

	Response updateSlabStatusByDoctorId(DoctorSlabDto doctorSlabDto)throws ApplicationException;

}
