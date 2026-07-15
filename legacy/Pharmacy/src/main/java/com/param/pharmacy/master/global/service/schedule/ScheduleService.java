package com.param.pharmacy.master.global.service.schedule;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.common.dto.CommonResponse;
import com.param.entity.model.master.Schedule;
import com.param.pharmacy.master.global.dao.schedule.IScheduleDao;
import com.param.pharmacy.master.global.dto.schedule.ScheduleDetailsResponse;
import com.param.pharmacy.master.global.dto.schedule.ScheduleListResponse;
import com.param.pharmacy.master.global.dto.schedule.ScheduleRequest;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service("ScheduleService")
public class ScheduleService implements IScheduleService {

	@Autowired
	IScheduleDao ScheduleDao;
	
	
	@Autowired
	private SessionService sessionService;

	@Override
	@Transactional
	public List<ScheduleListResponse> getAll() {
		List<ScheduleListResponse> list = new ArrayList<ScheduleListResponse>();

		List<Schedule> lists = ScheduleDao.getAll();
		for (Schedule schedule : lists) {
			ScheduleListResponse scheduleResponse = new ScheduleListResponse();
			scheduleResponse.setId((Integer) schedule.getId());
			scheduleResponse.setCode((String) schedule.getCode());
			scheduleResponse.setName((String) schedule.getName());
			scheduleResponse.setStatus((Boolean) schedule.getIsActive());
			list.add(scheduleResponse);
		}
		return list;
	}

	@Override
	@Transactional
	public ScheduleDetailsResponse getById(Integer id) {
		ScheduleDetailsResponse response = new ScheduleDetailsResponse();

		Schedule schedule = ScheduleDao.getById(id);

		if (schedule != null) {
			response.setId((Integer) schedule.getId());
			response.setCode((String) schedule.getCode());
			response.setName((String) schedule.getName());
			response.setStatus((Boolean) schedule.getIsActive());
		} else {

		}
		return response;
	}

	@Override
	@Transactional
	public CommonResponse create(ScheduleRequest scheduleRequest) {

		Schedule schedule = new Schedule();
		
		schedule.setCode(scheduleRequest.getCode());
		schedule.setName(scheduleRequest.getName());
		schedule.setIsActive(scheduleRequest.getStatus());

		CommonResponse response = new CommonResponse();
	//	sessionService.setEntityDefaults(schedule);
		schedule.setCreateDefaults(scheduleRequest.getOrganizationId()!=null ? scheduleRequest.getOrganizationId() : sessionService.getOrganizationId(),scheduleRequest.getUnitId() !=null ? scheduleRequest.getUnitId() :sessionService.getUnitId() ,scheduleRequest.getUserId()!=null ? scheduleRequest.getUserId() : sessionService.getUserId());
		response.setId(ScheduleDao.create(schedule).longValue());
		response.setMessage("Schedule Inserted Successfully");

		return response;
	}

	@Override
	public CommonResponse update(ScheduleRequest scheduleRequest) {

		Schedule schedule = ScheduleDao.getById(scheduleRequest.getId());

		if (scheduleRequest.getCode() != null && scheduleRequest.getCode() != "") {
			schedule.setCode(scheduleRequest.getCode());
		}

		if (scheduleRequest.getName() != null && scheduleRequest.getName() != "") {
			schedule.setName(scheduleRequest.getName());
		}

		if (scheduleRequest.getStatus() != null) {
			schedule.setIsActive(scheduleRequest.getStatus());
		}

		schedule.setUpdatedDate(TimeZoneComponent.getDateByzone(schedule.getUnitId()));
		schedule.setUpdatedByUser(scheduleRequest.getUserId()!=null ? scheduleRequest.getUserId() : sessionService.getUserId());

		CommonResponse response = new CommonResponse();
		response.setId(ScheduleDao.update(schedule).longValue());
		response.setMessage("Schedule Updated Successfully");

		return response;
	}
}