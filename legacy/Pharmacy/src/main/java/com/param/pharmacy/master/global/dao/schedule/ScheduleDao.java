package com.param.pharmacy.master.global.dao.schedule;

import java.util.List;

import com.param.service.global.SessionService;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.master.Schedule;

@Repository("ScheduleDao")
public class ScheduleDao extends AbstractDao<Integer, Schedule> implements IScheduleDao {

	@Autowired
	SessionService sessionService;

	@Override
	public List<Schedule> getAll() {
		Criteria criteria = getSession().createCriteria(Schedule.class).createAlias("organization","organization");
		criteria.addOrder(Order.asc("id"));
		criteria.add(Restrictions.eq("isDeleted", false));
		criteria.add(Restrictions.eq("organization.organizationId",sessionService.getOrganizationId()));

		return criteria.list();
	}

	@Override
	public Schedule getById(Integer id) {
		Criteria criteria = getSession().createCriteria(Schedule.class).createAlias("organization","organization");
		criteria.add(Restrictions.eq("id", id));
		criteria.add(Restrictions.eq("isDeleted", false));
		criteria.add(Restrictions.eq("organization.organizationId",sessionService.getOrganizationId()));
		return (Schedule) criteria.uniqueResult();
	}

	@Override
	public Integer create(Schedule schedule) {
		getSession().save(schedule);
		return schedule.getId();
	}

	@Override
	public Integer update(Schedule schedule) {
		getSession().update(schedule);
		return schedule.getId();
	}
}