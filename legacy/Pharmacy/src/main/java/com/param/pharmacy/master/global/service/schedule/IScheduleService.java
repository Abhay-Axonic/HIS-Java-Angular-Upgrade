package com.param.pharmacy.master.global.service.schedule;

import java.util.List;

import com.param.common.dto.CommonResponse;
import com.param.pharmacy.master.global.dto.schedule.ScheduleDetailsResponse;
import com.param.pharmacy.master.global.dto.schedule.ScheduleListResponse;
import com.param.pharmacy.master.global.dto.schedule.ScheduleRequest;

public interface IScheduleService {

	List<ScheduleListResponse> getAll();

	ScheduleDetailsResponse getById(Integer id);

	CommonResponse create(ScheduleRequest scheduleRequest);

	CommonResponse update(ScheduleRequest scheduleRequest);

}
