package com.param.otims.service;

import java.util.List;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.otims.dto.StaffSchedulingDto;

import in.param.exception.ApplicationException;

@Transactional
@SuppressWarnings("rawtypes")
public interface IStaffSchedulingService {

	Response saveStaffScheduling(StaffSchedulingDto staffSchedulingDto)throws ApplicationException;

	Response getStaffScheduling(StaffSchedulingDto staffSchedulingDto)throws ApplicationException;

	Response deactivateStaffSchedule(StaffSchedulingDto staffSchedulingDto)throws ApplicationException;

	Response getStaffListByRoleId(StaffSchedulingDto staffSchedulingDto)throws ApplicationException;

	Response checkStaffAvailableOrNot(StaffSchedulingDto staffSchedulingDto)throws ApplicationException;

	Response getOTIMSStaffList(StaffSchedulingDto staffSchedulingDto)throws ApplicationException;

	Response checkStaffListAvailableOrNot(List<StaffSchedulingDto> staffSchedulingDto)throws ApplicationException;


}
