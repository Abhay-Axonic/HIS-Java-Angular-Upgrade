package com.param.duosecurity;

import com.duosecurity.Client;

import com.duosecurity.exception.DuoException;
import com.duosecurity.model.Token;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.param.duoAuthentication.dto.DuoApplicationPropertiesDto;
import com.param.duoAuthentication.service.IDuoAuthenticationService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.IUserLoginConstants;
import com.param.global.common.Response;
import com.param.global.common.ServletUtils;
import com.param.global.dto.LdapConfigurationMasterDto;
import com.param.global.dto.UserMasterDto;
import com.param.global.service.IUserMasterService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
//@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/")
public class LoginController implements ICommonConstants , IUserLoginConstants{
	final static Logger logger = Logger.getLogger(LoginController.class);

	
	@Autowired
	IUserMasterService iUserMasterService;
	
	@Autowired
	IDuoAuthenticationService iDuoAuthenticationService;
	
	private String apiHost;

	private String clientId;

	private String clientSecret;

	private String redirectUri;

	private String failmode;

	private Map<String, String> stateMap;

	private Client duoClient;

	static final String ORIGIN = "Origin";

	/**
	 * Create and initialize the Duo Client.
	 *
	 * @throws DuoException For problems creating the Clients
	 */
	
	
	@RequestMapping(value= "/ldapUserCredentialCheck" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody  Response ldapUserCredentialCheck(@RequestBody DuoApplicationPropertiesDto duoApplicationPropertiesDto){
		try{
			Response res= iUserMasterService.getLdapConfigurationData(duoApplicationPropertiesDto);
			 if (!validateLdapUser(res.getObject(), duoApplicationPropertiesDto)) {

				 return new Response(ERROR, null, null, null, null);
					 
			    }else {
			    	return new Response(SUCCESS, SUCCESS_CODE,null, null, null);
			    }
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
//	@PostConstruct
	public void initializeDuoClient(DuoApplicationPropertiesDto duoApplicationPropertiesDto,  HttpServletRequest request) throws DuoException {
		Properties systemPropery = null;
		systemPropery = new Properties();
		try {
//			systemPropery.load(this.getClass().getResourceAsStream("application.properties"));
			DuoApplicationPropertiesDto dto= new DuoApplicationPropertiesDto();
		    Response res=iDuoAuthenticationService.getApplicationPropertyData(duoApplicationPropertiesDto.getOrganizationId(), duoApplicationPropertiesDto.getUnitId());
			/*clientId = systemPropery.getProperty("duo.clientId");
			clientSecret = systemPropery.getProperty("duo.clientSecret");
			apiHost = systemPropery.getProperty("duo.api.host");
			redirectUri = systemPropery.getProperty("duo.redirect.uri");
			failmode = systemPropery.getProperty("duo.failmode");*/
//		    dto = (DuoApplicationPropertiesDto)res.getListObject();
		    List<DuoApplicationPropertiesDto> dtolist=new ArrayList<DuoApplicationPropertiesDto>();
		    dtolist=res.getListObject();
		    for (DuoApplicationPropertiesDto d : dtolist) {
//		    	
		    	dto.setClientId(d.getClientId());
		    	dto.setClientSecret(d.getClientSecret());
		    	dto.setApiHost(d.getApiHost());
		    	dto.setRedirectUri(ServletUtils.getBaseUrl(request)+"/duo-callback");
		    	dto.setFailmode(d.getFailmode());
			}
		    clientId = dto.getClientId();
			clientSecret = dto.getClientSecret();
			apiHost = dto.getApiHost();
			redirectUri =dto.getRedirectUri();
			failmode = dto.getFailmode();

		} catch (Exception e) {
		logger.error("initializeDuoClient failed", e);
		}

		
		stateMap = new HashMap<String, String>();
		duoClient = new Client.Builder(clientId, clientSecret, apiHost, redirectUri).build();

		/*
		 * Example of setting optional fields duoClient = new Client.Builder(clientId,
		 * clientSecret, apiHost, redirectUri) .setUseDuoCodeAttribute(false)
		 * .setCACerts(customCerts) .appendUserAgentInfo("custom string") .build();
		 */
	}

	/*@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String index() {
		return "index";
	}
	
	@RequestMapping(value = "/success", method = RequestMethod.GET)
	public String success() {
		return "success";
	}*/

	/**
	 * POST handler for login page.
	 *
	 * @param username The username of the user trying to authenticate
	 * @param password The password of the user trying to authenticate
	 *
	 * @return ModelAndView A model that contains information about where to
	 *         redirect next
	 * @throws NamingException 
	 */
@SuppressWarnings({ "unchecked", "rawtypes" })
	//	@CrossOrigin(origins = "https://api-97712f61.duosecurity.com")
	@RequestMapping(value = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ModelAndView login(@RequestBody DuoApplicationPropertiesDto duoApplicationPropertiesDto, HttpServletRequest request,
			HttpServletResponse response) throws DuoException, NamingException {
		// Perform fake primary authentication
//		if(res.getStatus() == SUCCESS) {
			String ldapStatus="";
			 DirContext dirContext = null;
			    NamingEnumeration<?> results = null;
		/*	if (duoApplicationPropertiesDto.getUserName()!=null && duoApplicationPropertiesDto.getPassword() !=null) {
				
				Response res= iUserMasterService.getLdapConfigurationData(duoApplicationPropertiesDto);
				 if (!validateLdapUser(res.getObject(), duoApplicationPropertiesDto)) {
					/*ModelAndView model = new ModelAndView(ServletUtils.getBaseUrl(request)+"/indexDuo");
			      model.addObject("message", "Invalid Credentials");
			      return model;*/
					/* ModelAndView model = new ModelAndView("/inalidUser");
				      model.addObject("message", "Invalid Credentials");
				      return model;*/ 
					 
					/* ModelAndView model = new ModelAndView("/invalidateUser");
//				     model.setViewName("redirect:#/login");
				      return model;*/
/*			    Response res= iUserMasterService.getLdapConfigurationData(duoApplicationPropertiesDto);
			    if (!validateLdapUser(res.getObject(), duoApplicationPropertiesDto)) {
					 String loginUrl =ServletUtils.getBaseUrl(request);
					 View msg = null;
					 ModelAndView model = new ModelAndView("/invalidateUser");
					 model.addObject("loginUrl", loginUrl);
					 model.addObject("msg", ERROR);
//					 model.setViewName(ERROR);

					return model;
					 
			    }
*/
			initializeDuoClient(duoApplicationPropertiesDto, request);
			// Step 2: Call Duo health check
			try {
					duoClient.healthCheck();
				
			} catch (DuoException exception) {
			logger.error("login failed", exception);
				// If the health check failed AND the integration is configured to fail open
				// then render
				// the welcome page. If the integarion is configured to fail closed return an
				// error
				if ("OPEN".equalsIgnoreCase(failmode)) {
					ModelAndView model = new ModelAndView("/welcome");
					model.addObject("token", "Login Successful, but 2FA Not Performed."
							+ "Confirm application.properties values are correct and that Duo is reachable");
					return model;
				}
				else {
					ModelAndView model = new ModelAndView("/indexDuo");
					model.addObject("message", "2FA Unavailable."
							+ "Confirm application.properties values are correct and that Duo is reachable");
					return model;
				}
			}

			// Step 3: Generate and save a state variable
			String stateUser = duoClient.generateState();
			// Store the state to remember the session and username
			stateMap.put(stateUser, duoApplicationPropertiesDto.getUserName());

			// Step 4: Create the authUrl and redirect to it
			String authUrl = duoClient.createAuthUrl(duoApplicationPropertiesDto.getUserName(), stateUser);
			ModelAndView model = new ModelAndView("/redirect");

			model.addObject("authURL", authUrl);
			return model;
//		}
//		else {
//			ModelAndView model = new ModelAndView("/indexDuo");
//			model.addObject("message", "2FA Unavailable."
//					+ "Confirm application.properties values are correct and that Duo is reachable");
//			return model;
//		}
	}

	/**
	 * GET handler for duo-callback page.
	 *
	 * @param duoCode An authentication session transaction id
	 * @param state   A random string returned from Duo used to maintain state
	 *
	 * @return ModelAndView A model that contains information about where to
	 *         redirect next
	 */
	@RequestMapping(value = "/duo-callback", method = RequestMethod.GET)
	public ModelAndView duoCallback(@RequestParam("duo_code") String duoCode, @RequestParam("state") String stateUser)
			throws DuoException {
		// Step 5: Validate state returned from Duo is the same as the one saved
		// previously.
		// If it isn't return an error
		if (!stateMap.containsKey(stateUser)) {
			ModelAndView model = new ModelAndView("/indexDuo");
			//model.addObject("message", "Session Expired");
			return model;
		}
		// Remove state from the list of valid sessions
		String username = stateMap.remove(stateUser);

		// Step 6: Exchange the auth duoCode for a Token object
		Token token = duoClient.exchangeAuthorizationCodeFor2FAResult(duoCode, username);

		// If the auth was successful, render the welcome page otherwise return an error
		if (authWasSuccessful(token)) {
			ModelAndView model = new ModelAndView("/success");
			model.addObject("token", tokenToJson(token));	
			return model;
		} else {
			ModelAndView model = new ModelAndView("/indexDuo");
			model.addObject("message", "2FA Failed");
			return model;
		}
	}

	private static String tokenToJson(Token token) throws DuoException {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(token);
		} catch (JsonProcessingException jpe) {
			throw new DuoException("Could not convert token to JSON");
		}
	}

	/**
	 * Exception handler.
	 *
	 * @param ex A DuoException
	 *
	 * @return ModelAndView A model object that contains the error message from ex
	 */
	@ExceptionHandler({ DuoException.class })
	public ModelAndView handleException(DuoException ex) {
		ModelAndView model = new ModelAndView("/indexDuo");
		model.addObject("message", ex.getMessage());
		return model;
	}

	private boolean validateUser(String username, String password) {
		if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
			return false;
		}
		return true;
	}

	private boolean authWasSuccessful(Token token) {
		if (token != null && token.getAuth_result() != null) {
			return "ALLOW".equalsIgnoreCase(token.getAuth_result().getStatus());
		}
		return false;
	}
	
	private SearchControls getSimpleSearchControls() {
	    SearchControls searchControls = new SearchControls();
	    searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
	    searchControls.setTimeLimit(30000);
	    //String[] attrIDs = {"objectGUID"};
	    //searchControls.setReturningAttributes(attrIDs);
	    return searchControls;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private boolean validateLdapUser(Object object, DuoApplicationPropertiesDto duoApplicationPropertiesDto) throws NamingException {
		try {
			LdapConfigurationMasterDto ldapDto= (LdapConfigurationMasterDto) object;
			@SuppressWarnings("unused")
			LdapContext ctx = null;
			Hashtable env = new Hashtable();
			env.put(Context.INITIAL_CONTEXT_FACTORY, ldapDto.getInitialContextFactory());
			env.put(Context.SECURITY_AUTHENTICATION, ldapDto.getSecurityAuthentication());
			env.put(Context.SECURITY_PRINCIPAL, duoApplicationPropertiesDto.getUserName());
			// env.put(Context.SECURITY_CREDENTIALS, ldapDto.getSecurityCredentials());
			env.put(Context.SECURITY_CREDENTIALS, duoApplicationPropertiesDto.getPassword());
			env.put(Context.PROVIDER_URL, ldapDto.getProviderUrl());
			// ctx = new InitialLdapContext(env, null);

			DirContext context = new InitialDirContext(env);
			SearchControls searchCtrls = new SearchControls();
			searchCtrls.setSearchScope(SearchControls.SUBTREE_SCOPE);
			searchCtrls.setReturningAttributes(new String[]{"sn","givenName", "samAccountName"});
			String filter = "(&(objectclass=user)(sAMAccountName="+duoApplicationPropertiesDto.getUserName()+"))";
			String searchBase = "dc=MRRCH, dc=COM";
			NamingEnumeration values = context.search(searchBase, filter, getSimpleSearchControls());
			int totalResults = 0;
			//Loop through the search results
			while (values.hasMoreElements())
			{
				SearchResult sr = (SearchResult)values.next();
				totalResults++;
				Attributes attrs = sr.getAttributes();
			}
			
			if (values.hasMore()) {
			context.close();
			return true;
			} else {
			return false;
			}
			}catch (Exception e) {
			logger.error("validateLdapUser failed", e);
			return false;
			}



			}
	
	
	
}
