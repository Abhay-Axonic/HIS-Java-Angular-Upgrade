package com.param.role.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.adt.master.global.model.RoleMaster;

import com.param.role.dao.IRoleDao;

@Service
public class RoleService implements IRoleService {

	@Autowired
	private IRoleDao roleDAO;

	@Override
	@Transactional

	public List<RoleMaster> getAllRoles(Integer organizationId,Integer unitId) {
		return this.roleDAO.getAllRoles(organizationId,unitId);
	}

}
