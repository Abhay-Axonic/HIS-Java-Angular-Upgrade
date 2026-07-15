package com.param.screen.dao;

import java.util.List;

import org.hibernate.*;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.param.actionmaster.model.ActionMaster;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.privilege.dto.ScreenDto;
import com.param.privilege.dto.SubModuleDto;
import com.param.screen.constants.Constants;


@Component
public class ScreenDaoImpl implements IScreenDao,ICommonConstants {

	@Autowired
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Response getAllScreens(Integer subModuleId) {
		Session session = this.sessionFactory.getCurrentSession();
		List<ScreenDto> screenDtoList = session
				.getNamedQuery("GET_SCREEN_LIST_BY_SUB_MODULE_ID")
				.setParameter("subModuleId", subModuleId)
				.setResultTransformer(Transformers.aliasToBean(ScreenDto.class)).list();
		return new Response(SUCCESS, null, null, screenDtoList, null);
	}

}
