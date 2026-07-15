package com.param.actionmaster.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.actionmaster.dao.IActionMasterDao;
import com.param.actionmaster.model.ActionMaster;

@Service
public class ActionMasterServiceImpl implements ActionMasterService {

	@Autowired
	private IActionMasterDao actionMasterDao;

	@Transactional
	public void addActionMaster(ActionMaster actionMaster) {

		this.actionMasterDao.addActionMaster(actionMaster);

	}
}
