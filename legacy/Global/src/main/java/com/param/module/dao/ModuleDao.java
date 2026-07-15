package com.param.module.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.org.dto.OrganizationMasterDto;
import com.param.privilege.dto.ModuleDto;

import javax.transaction.Transactional;

@Component
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ModuleDao implements IModuleDao,ICommonConstants {

	@Autowired
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public Response getAllModules() {
		Session session = this.sessionFactory.getCurrentSession();
		List<ModuleDto> moduleDtoList = session
				.getNamedQuery("GET_MODULE_LIST")
				.setResultTransformer(Transformers.aliasToBean(ModuleDto.class)).list();
		return new Response(SUCCESS, null, null, moduleDtoList, null);
	}

	@Transactional
	@Override
	public Response getZeroParentModule() {
		try {
//			Session session = this.sessionFactory.getCurrentSession();
			List<ModuleDto> moduleDtoList = sessionFactory.getCurrentSession().getNamedQuery("GET_MODULE_LIST_ZERO_PARENT")
					.setResultTransformer(Transformers.aliasToBean(ModuleDto.class)).list();
			return new Response(SUCCESS, null, null, moduleDtoList, null);
		}catch (Exception e){
			throw e;
		}
	}


	public Response getBIModuleList() {
		Session session = this.sessionFactory.getCurrentSession();
		List<ModuleDto> moduleDtoList = session
				.createSQLQuery("select "
						+ "	bi_action_mst.menu_id as \"moduleId\", "
						+ "	bi_action_mst.menu_name as \"moduleName\", "
						+ "	bi_action_mst.parent_menu_id as \"parentId\", "
						+ "	bi_action_mst.module_type as \"moduleType\" "
						+ " from "
						+ "	public.m_bi_action_master bi_action_mst "
						+ "where "
						+ "	bi_action_mst.is_deleted = 'N'")
				.setResultTransformer(Transformers.aliasToBean(ModuleDto.class)).list();
		return new Response(SUCCESS, null, null, moduleDtoList, null);
	}

	@Override
	public Response getAllModulesByOrgId(int orgId) throws JsonParseException, JsonMappingException, IOException {
		Session session = this.sessionFactory.getCurrentSession();
		OrganizationMasterDto organizationMasterDto = (OrganizationMasterDto) session
				.createSQLQuery("select "
						+ "		org_privileges as \"orgPrivileges\" "
						+ " from "
						+ "		public.m_organization_master "
						+ "	where "
						+ "		organization_id =:orgId ")
				.setParameter("orgId",orgId)
				.setResultTransformer(Transformers.aliasToBean(OrganizationMasterDto.class)).uniqueResult();
		
		if(organizationMasterDto!=null && organizationMasterDto.getOrgPrivileges()!=null && organizationMasterDto.getOrgPrivileges().length()>2)
		{
			final ObjectMapper objectMapper = new ObjectMapper();
			ModuleDto[] moduleList = objectMapper.readValue(organizationMasterDto.getOrgPrivileges(), ModuleDto[].class);
			List<ModuleDto> moduleDtoList = new ArrayList(Arrays.asList(moduleList));
			return new Response(SUCCESS, null, null, moduleDtoList, null);
		}else
		{
			return getAllModules();
		}
		
	}
	

}

