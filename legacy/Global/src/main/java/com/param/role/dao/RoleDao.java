package com.param.role.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.param.adt.master.global.model.RoleMaster;

@Controller
public class RoleDao implements IRoleDao {

	@Autowired
	private SessionFactory sessionFactory;

	@SuppressWarnings("unchecked")
	@Override
	public List<RoleMaster> getAllRoles(Integer organizationId,Integer unitId) {
		Session session = this.sessionFactory.getCurrentSession();
		Query query = session.createQuery("from RoleMaster where organisation_id=:organizationId and unit_id=:unitId and status='A' ")
				.setParameter("organizationId", organizationId)
				.setParameter("unitId", unitId);
		List<RoleMaster> roleList = (List<RoleMaster>) query.list();
		return roleList;
	}

}
