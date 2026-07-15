package com.param.global.dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.UserMasterDto;
import com.param.global.dto.UserSSODto;
import com.param.global.dto.UserSSORequestDto;
import com.param.privilege.service.PrivilegeService;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes","unused","unchecked"})
public class SSODaoImpl implements ISSODao,ICommonConstants 
{
	final static Logger logger = Logger.getLogger(SSODaoImpl.class);


	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private DozerBeanMapper mapper;
	
	@Autowired
	private PrivilegeService privilegeService;
	
	
	InputStream inputStream;

	
	@Override
	public Response validateToken(UserSSORequestDto userSSORequestDto) throws ApplicationException 
	{
		try{
			
			Session session = sessionFactory.getCurrentSession();
			
			UserSSORequestDto userSSOResponseDto = (UserSSORequestDto) session
					             .getNamedQuery("VALIDATE_USER_TOKEN")
					             .setParameter("token", userSSORequestDto.getToken())
					             .setResultTransformer(Transformers.aliasToBean(UserSSORequestDto.class)).uniqueResult();
			if(userSSOResponseDto!=null){
				if(System.currentTimeMillis() < userSSOResponseDto.getExpiry())
				{
					userSSOResponseDto.setRedirectURL(userSSORequestDto.getRedirectURL());
					UserSSODto dto=(UserSSODto)session.get(UserSSODto.class,userSSOResponseDto.getUserId() );
					dto.setExpiry(userSSOResponseDto.getExpiry() + 3600000);
					session.update(dto);
					
					userSSOResponseDto.setExpiry(userSSOResponseDto.getExpiry() + 3600000);
					
					List<Integer> roleIds = session
							.getNamedQuery("GET_ROLE_IDS_BY_USER_ID")
							.setParameter("userId", userSSOResponseDto.getUserId())
							.list(); 
					
					UserMasterDto userMasterDto = (UserMasterDto)session
							.createQuery("SELECT userMst.organizationId as organizationId,userMst.unitId as unitId from UserMaster userMst where userMst.userId=:userId")
							.setParameter("userId", userSSOResponseDto.getUserId())
							.setResultTransformer(Transformers.aliasToBean(UserMasterDto.class))
							.uniqueResult(); 
					
					Map<String,Object> responseObject = (Map<String,Object>) privilegeService.getPrivilegesByRoleIdList(roleIds,userMasterDto.getOrganizationId(),userMasterDto.getUnitId()).getObject();
					List<JSONObject> privilegeList = (List<JSONObject>)responseObject.get("modules");
					userSSOResponseDto.setPermissions(privilegeList);
					
				}
				else
				{
					userSSOResponseDto.setRedirectURL(this.getPropValues());
					userSSOResponseDto.setExpiry(null);
				}
				userSSOResponseDto.setLastLogin(null);
				userSSOResponseDto.setToken(null);
				
				return new Response(SUCCESS, null, null, null,userSSOResponseDto);
			}else
			{
				userSSORequestDto.setToken(null);
				userSSORequestDto.setRedirectURL(this.getPropValues());
				return new Response(ERROR, null, "Invalid Token!", null,userSSORequestDto);
			}
		}catch(HibernateException he){
			throw new HibernateException(he.getMessage());
		}catch(Exception e){
		logger.error("validateToken failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@Override
	public Response clearSSODetails(UserSSORequestDto userSSORequestDto) throws ApplicationException 
	{
		try{
			
			Session session = sessionFactory.getCurrentSession();
			
			UserSSODto userSSODto = (UserSSODto) session.get(UserSSODto.class, userSSORequestDto.getUserId());
					            
			if(userSSODto!=null){
				session.delete(userSSODto);
				return new Response(SUCCESS, null,"Cleared SSO Details Successfully", null,null);
			}else
			{
				return new Response(ERROR, null, "Invalide user id !", null,null);
			}
		}catch(HibernateException he){
			throw new HibernateException(he.getMessage());
		}catch(Exception e){
		logger.error("clearSSODetails failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	

	public String getPropValues() throws IOException {
		String loginURL=null;
		try {
			
			Properties prop = new Properties();
			String propFileName = "config.properties";
 
			inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
 
			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
			}
 
			Date time = new Date(System.currentTimeMillis());
 
			// get the property value and print it out
			loginURL = prop.getProperty("loginURL");
		    return loginURL;
		} catch (Exception e) {
		logger.error("getPropValues failed", e);
		} finally {
			inputStream.close();
		}
		return loginURL;
	}
	
	
}
