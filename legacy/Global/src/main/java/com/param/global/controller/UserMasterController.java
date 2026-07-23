package com.param.global.controller;

import java.security.KeyFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.param.global.utility.UserComponent;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.UserLoginLogDto;
import com.param.global.dto.UserMasterDto;
import com.param.global.org.dto.OrganizationMasterDto;
import com.param.global.service.IUserMasterService;

import java.security.*;
import java.security.spec.*;
import javax.crypto.Cipher;
import java.util.Base64;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({"rawtypes","unchecked"})


@Controller
@RequestMapping("/adt")
public class UserMasterController implements ICommonConstants 
{
	final static Logger logger = Logger.getLogger(UserMasterController.class);

	
	
	@Autowired
	IUserMasterService iUserMasterService;

	@Autowired
	UserComponent userComponent;
	
	@RequestMapping(value= "/userLogin" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody  Response userLogin(@RequestBody UserMasterDto userMasterDto,HttpServletRequest request){
		try{
//			userComponent.refreshDomainCache();
			dencryptDtoKeysUserNamePassword(userMasterDto);
			if(userMasterDto.getIsDuoUserLogin() == true){
				return iUserMasterService.userLoginForDuoAuthentication(userMasterDto,request);
			}else {
				return iUserMasterService.userLogin(userMasterDto,request);
			}
			
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}


	@RequestMapping(value="/generateToken",method = RequestMethod.GET)
	public @ResponseBody Map<String,String> generateToken(@RequestParam(required=false) Integer userId, @RequestParam Integer unitId) {
		return iUserMasterService.generateToken(userId, unitId);
	}
	
	@RequestMapping(value="/organizationMasterList/{url}",method = RequestMethod.GET)
	public @ResponseBody Response organizationList(@PathVariable("url") String url){
		
		return iUserMasterService.getOrganizationList(url);
	}
	
	@RequestMapping(value="/getUnitByOrganization",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response unitByOrganizationList(@RequestBody OrganizationMasterDto organizationMasterDto){
		//System.out.println(organizationMasterDto.getOrganizationId());
		return iUserMasterService.getUnitListByOrganization(organizationMasterDto);
	}
	
	@RequestMapping(value="/getUnitByCredentials",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getUnitByCredentials(@RequestBody OrganizationMasterDto organizationMasterDto){
		if(organizationMasterDto.getIsDuoUserLogin() == true) {
			return iUserMasterService.getUnitByCredentialsForDuoLogin(organizationMasterDto);
		}
		else {
			return iUserMasterService.getUnitByCredentials(organizationMasterDto);
		}
	}

	@RequestMapping(value = "/validateUserName" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response validateUserName(@RequestBody UserMasterDto userMasterDto){
		try{
			return iUserMasterService.validateUniqueUserName(userMasterDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}

	@RequestMapping(value = "/getUsersByName" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public  @ResponseBody Response getUsersByName(@RequestBody UserMasterDto userMasterDto){
		try{
			return iUserMasterService.getUsersByName(userMasterDto);
		}catch(Exception e){
			
			throw e;
		}
	}
	
	@RequestMapping(value = "/getUsersByNameOnType" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public  @ResponseBody Response getUsersByNameOnType(@RequestBody UserMasterDto userMasterDto){
		try{
			return iUserMasterService.getUsersByNameOnType(userMasterDto);
		}catch(Exception e){
			
			throw e;
		}
	}
	
	
	@RequestMapping(value = "/forgotPasswordGeneration" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public  @ResponseBody Response forgotPasswordGeneration(@RequestBody UserMasterDto userMasterDto ){
		try{
			return iUserMasterService.forgotPasswordGeneration(userMasterDto);
		}catch(Exception e){
			
			throw e;
		}
	}
	
	@RequestMapping(value = "/checkEmailAvailability" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public  @ResponseBody Response checkEmailAvailability(@RequestBody UserMasterDto userMasterDto){
		try{
			return iUserMasterService.checkEmailAvailability(userMasterDto);
		}catch(Exception e){
			
			throw e;
		}
	}

	@RequestMapping(value = "/generateSMSOtp" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public  @ResponseBody Response generateSMSOtp(@RequestBody UserMasterDto userMasterDto,HttpServletRequest request){
		try{
			return iUserMasterService.generateSMSOtp(userMasterDto,request);
		}catch(Exception e){

			throw e;
		}
	}

	@RequestMapping(value = "/validateSMSOtp" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public  @ResponseBody Response validateSMSOtp(@RequestBody UserMasterDto userMasterDto,HttpServletRequest request){
		try{
			return iUserMasterService.validateSMSOtp(userMasterDto,request);
		}catch(Exception e){

			throw e;
		}
	}
	
	@RequestMapping(value = "/otpVerification" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public  @ResponseBody Response otpVerification(@RequestBody UserMasterDto userMasterDto){
		try{
			return iUserMasterService.otpVerification(userMasterDto);
		}catch(Exception e){
			
			throw e;
		}
	}
	
	@RequestMapping(value = "/resetPassword" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public  @ResponseBody Response resetPassword(@RequestBody UserMasterDto userMasterDto){
		try{
			// Security fix, docs/DECISION_NEEDED_ResetPasswordMissingIdentityBinding.md --
			// this endpoint is exclusively a self-service "change my own password" flow
			// (confirmed: the only live frontend caller always sends the caller's own
			// userId). Never trust the body's userId -- bind to the JWT-authenticated
			// caller's own identity instead. NOTE: this closes the "just call the API
			// with someone else's ID" exploit path; it does not by itself address
			// docs/DECISION_NEEDED_HardcodedJwtSigningKey.md, which undermines the
			// trustworthiness of the JWT identity this check relies on -- both should
			// be resolved together.
			String authenticatedUsername = SecurityContextHolder.getContext().getAuthentication().getName();
			UserMasterDto authenticatedUser = UserComponent.getUsers().stream()
					.filter(u -> u.getUserName() != null && u.getUserName().equalsIgnoreCase(authenticatedUsername))
					.findAny().orElse(null);
			if (authenticatedUser == null || !authenticatedUser.getUserId().equals(userMasterDto.getUserId())) {
				return new Response(ERROR, null, "You are not authorized to change this user's password.", null, null);
			}
			return iUserMasterService.resetPassword(userMasterDto);
		}catch(Exception e){

			throw e;
		}
	}
	
	@RequestMapping(value = "/resendUserName" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public  @ResponseBody Response resendUserName(@RequestBody UserMasterDto userMasterDto){
		try{
			return iUserMasterService.resendUserName(userMasterDto);
		}catch(Exception e){
			
			throw e;
		}
	}
	
	@RequestMapping(value = "/resendUserNameAndPassword" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public  @ResponseBody Response resendUserNameAndPassword(@RequestBody UserMasterDto userMasterDto){
		try{
			return iUserMasterService.resendUserNameAndPassword(userMasterDto);
		}catch(Exception e){
			
			throw e;
		}
	}
	
	@RequestMapping(value = "/getUnitName" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public  @ResponseBody Response getUnitName(@RequestBody UserMasterDto userMasterDto){
		try{
			
			return iUserMasterService.getUnitName(userMasterDto);
		}catch(Exception e){
			
			throw e;
		}
	}
	@RequestMapping(value = "/getPassword" , method = RequestMethod.GET )
	public  @ResponseBody Response getPass(){
		try{
			
			return iUserMasterService.getPass();
		}catch(Exception e){
			
			throw e;
		}
	}
	
	@RequestMapping(value = "/getUserNameById" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public  @ResponseBody Response getUserNameById(@RequestBody UserMasterDto userMasterDto){
		try{
			
			return iUserMasterService.getUserNameById(userMasterDto);
		}catch(Exception e){
			
			throw e;
		}
	}
	
	@RequestMapping(value= "/userLoginForVitalUploader" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody  Response userLoginForVitalUploader(@RequestBody UserMasterDto userMasterDto){
		try{
			return iUserMasterService.userLoginForVitalUploader(userMasterDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}

	@RequestMapping(value = "/getPasswordAndUserName" , method = RequestMethod.GET )
	public  @ResponseBody Response getPasswordAndUserName(){
		try{
			
			return iUserMasterService.getPasswordAndUserName();
		}catch(Exception e){
			
			throw e;
		}
	}
	@RequestMapping(value = "/saveUserLogDetails" , method = RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public  @ResponseBody Response saveUserLogDetails(@RequestBody UserLoginLogDto dto){
		try{
			
			return iUserMasterService.saveUserLogDetails(dto);
		}catch(Exception e){
			
			throw e;
		}
	}
	@RequestMapping(value = "/checkOtherLogin" , method = RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public  @ResponseBody Response checkOtherLogin(@RequestBody UserLoginLogDto dto){
		try{
			
			return iUserMasterService.checkOtherLoginByMacAddress(dto);
		}catch(Exception e){
			
			throw e;
		}
	}
	@RequestMapping(value = "/updatePreviousLogin" , method = RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public  @ResponseBody Response updatePreviousLogin(@RequestBody UserLoginLogDto dto){
		try{
			
			return iUserMasterService.updatePreviousLogin(dto);
		}catch(Exception e){
			
			throw e;
		}
	}
	
	
	@RequestMapping(value = "/getPharmacyUserRoleType" , method = RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public  @ResponseBody Response getPharmacyUserRoleType(@RequestBody UserLoginLogDto dto){
		try{
			
			return iUserMasterService.getPharmacyUserRoleType(dto);
		}catch(Exception e){
			
			throw e;
		}
	}
	
	
	@RequestMapping(value = "/updatePassword" , method = RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public  @ResponseBody Response updatePassword(@RequestBody UserMasterDto dto){
		try{
			
			return iUserMasterService.updatePassword(dto);
		}catch(Exception e){
			
			throw e;
		}
	}
	
	@RequestMapping(value = "/getUserListForWebAppByOrgIdAndUnitId" , method = RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public  @ResponseBody Response getUserListForWebAppByOrgIdAndUnitId(@RequestBody UserMasterDto dto){
		try{
			
			return iUserMasterService.getUserListForWebAppByOrgIdAndUnitId(dto);
		}catch(Exception e){
			
			throw e;
		}
	}
	
	@RequestMapping(value = "/getCookieDataForNewEMR" , method = RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public  @ResponseBody Response getCookieDataForNewEMR(@RequestBody UserMasterDto userMasterDto){
		try{
			
			return iUserMasterService.getCookieDataForNewEMR(userMasterDto);
		}catch(Exception e){
			
			throw e;
		}
	}
	
	@RequestMapping(value= "/saveSubscriptionDetails" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody  Response saveSubscriptionDetails(@RequestBody UserMasterDto userMasterDto){
		try{
			
				return iUserMasterService.saveSubscriptionDetails(userMasterDto);


		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}

	@RequestMapping(value= "/getSubscriptionDetails" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody  Response getSubscriptionDetails(@RequestBody UserMasterDto userMasterDto){
		try{
			
				return iUserMasterService.getSubscriptionDetails(userMasterDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}

	@RequestMapping(value="/generateTokenForWebUser",method = RequestMethod.GET)
	public @ResponseBody Map<String,String> generateTokenForWebUser(@RequestParam Integer unitId) {
		Map<String, String> responseMap = new HashMap<>();
		Response res = iUserMasterService.getWebUserId(unitId);
		if(res.getObject() != null){
			UserMasterDto organizationUserDetails = (UserMasterDto)res.getObject();

			if(organizationUserDetails.getWebAppUseId() != null ){
				return iUserMasterService.generateToken(organizationUserDetails.getWebAppUseId(), unitId);
			}else{
				responseMap.put("error", "Web user not configured");
				return responseMap;
			}

		}else{
			responseMap.put("error", "Web user not configured");
			return responseMap;
		}


	}


	private void dencryptDtoKeysUserNamePassword(UserMasterDto dto){
		try {

			byte[] keyBytes = Base64.getDecoder().decode(privateKeyForEncryption);

			PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
			KeyFactory factory = KeyFactory.getInstance("RSA");
			PrivateKey privateKey = factory.generatePrivate(spec);

			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, privateKey);

			dto.setUserName(
					new String(
							cipher.doFinal(
									Base64.getDecoder().decode(dto.getUserName())
							)
					)
			);


			dto.setPassword(
					new String(
							cipher.doFinal(
									Base64.getDecoder().decode(dto.getPassword())
							)
					)
			);


		}catch (Exception e){
		logger.error("dencryptDtoKeysUserNamePassword failed", e);
		}
	}
}
