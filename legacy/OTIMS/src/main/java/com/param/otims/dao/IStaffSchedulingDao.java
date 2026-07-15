package com.param.otims.dao;

import java.util.List;
import com.param.global.common.Response;
import com.param.global.otims.dto.StaffSchedulingDto;
import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IStaffSchedulingDao {

	Response saveStaffScheduling(List<StaffSchedulingDto> list) throws ApplicationException;

	Response checkStaffScheduling(StaffSchedulingDto dto)throws ApplicationException;

	Response getStaffScheduling(StaffSchedulingDto staffSchedulingDto)throws ApplicationException;

	Response deactivateStaffSchedule(StaffSchedulingDto staffSchedulingDto)throws ApplicationException;

	Response getStaffListByRoleId(StaffSchedulingDto staffSchedulingDto)throws ApplicationException;

	Response checkStaffAvailableOrNot(StaffSchedulingDto staffSchedulingDto)throws ApplicationException;

	Response getOtimsRoles(StaffSchedulingDto staffSchedulingDto)throws ApplicationException;

	Response getStaffListByRoleTypeId(StaffSchedulingDto staffSchedulingDto)throws ApplicationException;

}
