package com.param.pharmacy.master.unit.dao.applicationConfiguration;

import java.util.List;

import com.param.service.global.SessionService;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.common.dao.AbstractDao;
import com.param.entity.model.master.ApplicationConfiguration;

@Repository("ApplicationConfigurationDao")
public class ApplicationConfigurationDao extends AbstractDao<Integer, ApplicationConfiguration>
		implements IApplicationConfigurationDao {

	@Autowired
	private SessionService sessionService;

	@Override
	public List<ApplicationConfiguration> getAll(Integer unitId) {
		String hql = "SELECT applicationConfiguration FROM ApplicationConfiguration AS applicationConfiguration"
				+ " where applicationConfiguration.isActive=true AND applicationConfiguration.isDeleted=false and applicationConfiguration.unit.unitId = :unitId"
				+ " ORDER BY applicationConfiguration.id DESC";

		Query query = getSession().createQuery(hql);
		query.setParameter("unitId", unitId!=null ? unitId : sessionService.getUnitId());
		return query.list();
	}

	/*@Override
	public ApplicationConfiguration getApplicationConfiguration(Integer unitId) {
		String hql = "SELECT applicationConfiguration FROM ApplicationConfiguration AS applicationConfiguration "
				+ "WHERE applicationConfiguration.unit.unitId = :unitId AND applicationConfiguration.isActive = true AND applicationConfiguration.isDeleted = false ";
		Query query = getSession().createQuery(hql);
		query.setParameter("unitId", sessionService.getUnitId());

		return (ApplicationConfiguration) query.uniqueResult();
	}*/

	@Override
	public Long getCountApplicationConfiguration(Integer unitId) {
		String hql = " SELECT count(applicationConfiguration) from ApplicationConfiguration as applicationConfiguration "
				+ "WHERE applicationConfiguration.unit.unitId = :unitId  AND applicationConfiguration.isActive = true AND applicationConfiguration.isDeleted = false ";
		Query query = getSession().createQuery(hql);
		query.setParameter("unitId", unitId!=null ? unitId : sessionService.getUnitId());
		Long count = (Long) query.uniqueResult();
		return count;
	}

	/*@Override
	public Long getCountApplicationConfiguration() {
		String hql = " SELECT count(applicationConfiguration) from ApplicationConfiguration as applicationConfiguration "
				+ "WHERE applicationConfiguration.unit.unitId = :unitId AND applicationConfiguration.isActive = true AND applicationConfiguration.isDeleted = false ";
		Query query = getSession().createQuery(hql);
		query.setParameter("unitId", sessionService.getUnitId());
		Long count = (Long) query.uniqueResult();
		return count;
	}*/

	@Override
	public ApplicationConfiguration saveApplicationConfiguration(ApplicationConfiguration applicationConfiguration) {
		getSession().save(applicationConfiguration);
		return applicationConfiguration;
	}

	@Override
	public Integer update(ApplicationConfiguration applicationConfiguration) {
		getSession().update(applicationConfiguration);
		return applicationConfiguration.getId();
	}

	@Override
	public ApplicationConfiguration getApplicationConfiguration(Integer id) {
		String hql = " SELECT applicationConfiguration from ApplicationConfiguration as applicationConfiguration "
				+ "WHERE applicationConfiguration.id = :id AND applicationConfiguration.isActive = true AND applicationConfiguration.isDeleted = false ";
		Query query = getSession().createQuery(hql);
		query.setParameter("id", id);
		return (ApplicationConfiguration) query.uniqueResult();
	}

	@Override
	public ApplicationConfiguration getApplicationConfigurationByUnitId(Integer unitId) {
		String hql = " SELECT applicationConfiguration from ApplicationConfiguration as applicationConfiguration "
				+ "WHERE applicationConfiguration.unit.unitId = :unitId AND applicationConfiguration.isActive = true AND applicationConfiguration.isDeleted = false ";
		Query query = getSession().createQuery(hql);
		query.setParameter("unitId", unitId);
		return (ApplicationConfiguration) query.uniqueResult();
	}
}
