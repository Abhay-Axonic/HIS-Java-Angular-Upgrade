package com.param.duoAuthentication.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.adt.master.global.model.UserMaster;
import com.param.duoAuthentication.dto.DuoApplicationPropertiesDto;
import com.param.duoAuthentication.model.DuoApplicationProperties;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IUserMasterDao;
import com.param.global.org.dto.OrganizationMasterDto;

import in.param.common.dao.GenericDao;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes","unused","unchecked"})
public class DuoAuthenticationDaoImpl extends GenericDao<DuoApplicationProperties, Integer> implements IDuoAuthenticationDao,ICommonConstants 
{
	final static Logger logger = Logger.getLogger(DuoAuthenticationDaoImpl.class);

	public DuoAuthenticationDaoImpl() {
		super(DuoApplicationProperties.class);
	}

	@Autowired
	private SessionFactory SessionFactory;

	@Override
	public Response getApplicationPropertyData(Integer organizationId, Integer unitId) {
		try {

			StringBuilder query = new StringBuilder("select" + 
					"	duo.api_host as \"apiHost\"," + 
					"	duo.client_id as \"clientId\" ," + 
					"	duo.client_secret as \"clientSecret\" ," + 
					"	duo.failmode as \"failmode\"," + 
					"	duo.redirect_uri as \"redirectUri\"" + 
					" from " + 
					"	m_duo_application_properties duo" + 
					" where duo.organization_id ="+organizationId + 
					" and duo.unit_id ="+unitId);


			List<DuoApplicationPropertiesDto> list = SessionFactory.getCurrentSession()
					.createSQLQuery(query.toString())
					.setResultTransformer(Transformers.aliasToBean(DuoApplicationPropertiesDto.class)).list();
			return new Response(SUCCESS, null, null, list, null);
		}catch(HibernateException he){
		logger.error("getApplicationPropertyData failed", he);
		}catch(Exception e){
		logger.error("getApplicationPropertyData failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
}
