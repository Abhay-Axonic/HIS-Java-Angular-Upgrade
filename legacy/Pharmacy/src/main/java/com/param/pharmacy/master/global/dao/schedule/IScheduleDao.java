package com.param.pharmacy.master.global.dao.schedule;

import java.util.List;

import com.param.entity.model.master.Schedule;

public interface IScheduleDao {

	List<Schedule> getAll();

	Schedule getById(Integer id);

	Integer create(Schedule schedule);

	Integer update(Schedule schedule);

}
