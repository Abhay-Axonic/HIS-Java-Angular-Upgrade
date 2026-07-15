package com.param.role.service;

import java.util.List;

import com.param.adt.master.global.model.RoleMaster;


public interface IRoleService {

	public List<RoleMaster> getAllRoles(Integer organizationId,Integer unitId);

}
