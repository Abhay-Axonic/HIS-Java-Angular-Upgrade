package com.param.screen.service;

import java.util.List;

import com.param.actionmaster.model.ActionMaster;
import com.param.global.common.Response;

public interface IScreenService {

	@SuppressWarnings("rawtypes")
	public Response getAllScreens(Integer subModuleId);

}
