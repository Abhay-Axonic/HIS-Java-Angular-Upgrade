package com.param.actionmaster.dao;

import java.util.List;

import com.param.actionmaster.model.ActionMaster;

public interface IActionMasterDao {

	public void addActionMaster(ActionMaster actionMaster);

	public List<ActionMaster> listAllActionMster();
}
