package com.param.privilege.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.param.privilege.constants.Constants;
import com.param.privilege.model.Privilege;

@Component
public class PrivilegeDaoImpl implements IPrivilegeDao {

	@Autowired
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public Integer addPrivilege(Privilege p) {
		Session session = this.sessionFactory.getCurrentSession();
		Integer id= (Integer) session.save(p);
        return id;
	}

	@Override
	public void updatePrivilege(Privilege p) {
		Session session = this.sessionFactory.getCurrentSession();
		session.update(p);

	}
	
	@Override
	public void updatePrivilegeByRoleId(Privilege p) {
		Session session = this.sessionFactory.getCurrentSession();
		
		Query query = session.createQuery(Constants.PRIVILEGE_ACCESS_BY_ROLE_ID_QUERY)
				.setParameter("roleid", p.getRoleId())
				.setParameter("organizationId", p.getOrganisationId())
				.setParameter("unitId", p.getUnitId());
		
		@SuppressWarnings("unchecked")
		Privilege privilege = (Privilege) query.uniqueResult();
		
		if (null != privilege) {
				privilege.setRoleId(p.getRoleId());
				privilege.setModules(p.getModules());
				privilege.setModuleType(p.getModuleType());
				
				session.update(privilege);
			}
		

	}
	

	@SuppressWarnings("unchecked")
	@Override
	public List<Privilege> listPrivileges(Integer organizationId,Integer unitId) {
		Session session = this.sessionFactory.getCurrentSession();
		List<Privilege> PrivilegesList = session.createQuery("from Privilege where organisationId=:organizationId and unitId=:unitId")
				.setParameter("organizationId", organizationId)
				.setParameter("unitId",unitId)
				.list();

		return PrivilegesList;
	}

	@Override
	public Privilege getPrivilegeById(int id) {
		Session session = this.sessionFactory.getCurrentSession();
		Privilege privilege = (Privilege) session.get(Privilege.class, Integer.valueOf(id));
		return privilege;
	}
	
	@Override
	public List<Privilege> getPrivilegeByRoleId(int organizationId,int unitId,int id) {
		
			Session session = this.sessionFactory.getCurrentSession();
			Query query = session.createQuery(Constants.PRIVILEGE_ACCESS_BY_ROLE_ID_QUERY);
			query.setParameter("roleid", id);
			query.setParameter("organizationId", organizationId);
			query.setParameter("unitId", unitId);
			
			@SuppressWarnings("unchecked")
			List<Privilege> privilegeList = (List<Privilege>) query.list();
			return privilegeList;
	}

	@Override
	public void removePrivilege(int id) {
		Session session = this.sessionFactory.getCurrentSession();
		Privilege p = (Privilege) session.load(Privilege.class, Integer.valueOf(id));
		if (null != p) {
			session.delete(p);
		}

	}
	
	@Override
	public void removePrivilegeByRoleId(int organizationId,int unitId,int id) {
		Session session = this.sessionFactory.getCurrentSession();
		Query query = session.createQuery(Constants.PRIVILEGE_ACCESS_BY_ROLE_ID_QUERY);
		query.setParameter("organizationId", organizationId);
		query.setParameter("unitId", unitId);
		query.setParameter("roleid", id);
		
		@SuppressWarnings("unchecked")
		List<Privilege> privilegeList = (List<Privilege>) query.list();
		
		for (Privilege privilege : privilegeList) {
		
			if (null != privilege) {
				session.delete(privilege);
			}

		}
		
	}


}