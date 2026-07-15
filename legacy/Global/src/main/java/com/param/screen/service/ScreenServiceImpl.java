package com.param.screen.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.actionmaster.model.ActionMaster;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.screen.dao.IScreenDao;

@Service
public class ScreenServiceImpl implements IScreenService{

	@Autowired
	private IScreenDao screenDao;

	@Override
	@Transactional
	public Response getAllScreens(Integer subModuleId) {
		return screenDao.getAllScreens(subModuleId);
	}

}
