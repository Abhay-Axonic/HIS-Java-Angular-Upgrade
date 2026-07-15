package com.param.submodule.dao;

import java.util.List;

import org.hibernate.*;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.param.actionmaster.model.ActionMaster;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.privilege.dto.ModuleDto;
import com.param.privilege.dto.SubModuleDto;
import com.param.submodule.constants.Constants;

@Component
public class SubModuleDao implements ISubModuleDao,ICommonConstants {

	@Autowired
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public List<ActionMaster> getAllSubModulesByModuleName(String moduleName) {
		Session session = this.sessionFactory.getCurrentSession();
		Query query = session.createQuery(Constants.SUBMODULE_ACCESS_BY_MODULE_NAME_QUERY);
		query.setParameter("module", moduleName);
		@SuppressWarnings("unchecked")
		List<ActionMaster> actionMasterList = (List<ActionMaster>) query.list();
		return actionMasterList;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Response getAllSubModulesByModuleId(Integer moduleId) {
		Session session = this.sessionFactory.getCurrentSession();
		List<SubModuleDto> subModuleDtoList = session
				.getNamedQuery("GET_SUB_MODULE_LIST_BY_MODULE_ID")
				.setParameter("moduleId", moduleId)
				.setResultTransformer(Transformers.aliasToBean(SubModuleDto.class)).list();
		return new Response(SUCCESS, null, null, subModuleDtoList, null);
		
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Response getAllSubModule() {
		Session session = this.sessionFactory.getCurrentSession();
		List<SubModuleDto> subModuleDtoList = session
				.getNamedQuery("GET_ALL_SUB_MODULE_LIST")
				.setResultTransformer(Transformers.aliasToBean(SubModuleDto.class)).list();
		return new Response(SUCCESS, null, null, subModuleDtoList, null);
		
		
	}
	
	public List<ActionMaster> getAllSubModules() {
		Session session = this.sessionFactory.getCurrentSession();
		Query query = session.createQuery(Constants.SUBMODULE_ACCESS_BY_TYPE);
		
		@SuppressWarnings("unchecked")
		List<ActionMaster> actionMasterList = (List<ActionMaster>) query.list();
		return actionMasterList;
	}

}
