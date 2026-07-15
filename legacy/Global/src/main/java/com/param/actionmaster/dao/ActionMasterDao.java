package com.param.actionmaster.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.param.actionmaster.model.ActionMaster;

@Component
public class ActionMasterDao implements IActionMasterDao {

	@Autowired
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public void addActionMaster(ActionMaster actionMaster) {
		Session session = this.sessionFactory.getCurrentSession();
		session.persist(actionMaster);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ActionMaster> listAllActionMster() {
		Session session = this.sessionFactory.getCurrentSession();
		List<ActionMaster> actionMasterList = session.createQuery("from ActionMaster").list();

		return actionMasterList;
	}

}
