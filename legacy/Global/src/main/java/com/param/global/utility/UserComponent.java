package com.param.global.utility;

import java.util.*;

import javax.annotation.PostConstruct;

import com.param.global.billing.dto.POSMachineDeviceMasterDto;
import com.param.global.common.Response;
import com.param.global.dto.*;
import com.param.global.dto.global.InstructionMasterDto;
import com.param.global.dto.sync.hl7ConfigListDto;
import com.param.global.org.common.dto.PriorityMasterDto;
import com.param.global.org.common.dto.ReasonMasterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.param.global.service.IUserMasterService;

@Component
public class UserComponent {

	private static List<String> ALLOWED_ORIGINS;
	private static List<UserMasterDto> users;
	private static Map<String,String> AWS_CONFIG;
	private static List<ProductionReleaseEmailNotificationConfigDto> productionReleaseEmailNotificationConfigDtoList;
	private static List<hl7ConfigListDto> hl7ConfigList;

	@Autowired
	IUserMasterService iUserMasterService;
	
	@PostConstruct
	public void init() {
		users = iUserMasterService.getListOfUsers();
	}
	
	public static List<UserMasterDto> getUsers(){
		return users;
	}
	
	public static void updateUserDetailsForAuth(UserMasterDto userMasterDto){

		try {
			UserMasterDto masterDto = users.stream().filter(user -> user.getUserId().equals(userMasterDto.getUserId())).findAny().orElse(null);
			if (masterDto != null && masterDto.getUserId() != null)
				users.remove(masterDto);

			if (Objects.equals(userMasterDto.getStatus(), "A")) {
				users.add(userMasterDto);
			}
		}catch (Exception e){
			throw e;
		}
		
	}

	@PostConstruct
	public void initAllDomains() {
		initDomainCahe();
	}
	public void initDomainCahe() {
		String allDomains = iUserMasterService.getAllowedDomainsList();
		ALLOWED_ORIGINS = new ArrayList<>(Arrays.asList(allDomains.split(",")));
	}
	public static List<String> getAllowedDomains() {
		return ALLOWED_ORIGINS;
	}

	public void refreshDomainCache() {
		initDomainCahe();
	}

	@PostConstruct
	public void initAwsConfig() {
		initAwsCfg();
	}
	public void initAwsCfg() {
		AWS_CONFIG = iUserMasterService.getawsConfigMap();
	}
	public static Map<String,String> getAwsConfig() {
		return AWS_CONFIG;
	}
	public void refreshAwsConfig() {
		initAwsConfig();
	}


	@PostConstruct
	public void initProductionReleaseNotification() {
		initProductionReleaseNotificationNew();
	}
	public void initProductionReleaseNotificationNew() {
		Response response = iUserMasterService.sendNotificationOnEmailOfReleaseDetails();
	}

	@PostConstruct
	public void initHl7Config() {
		hl7ConfigList = iUserMasterService.gethl7ConfigList();
	}

	public static List<hl7ConfigListDto> gethl7ConfigList() {
		return hl7ConfigList;
	}


	public static void refreshDomainCacheForAutomationFlow(String httpDomain, String httpsDomain) {
		synchronized (ALLOWED_ORIGINS) {
			ALLOWED_ORIGINS.add(getfilteredDomain(httpDomain));
			ALLOWED_ORIGINS.add(getfilteredDomain(httpsDomain));
		}
	}

	private static String  getfilteredDomain(String domain){
		String plainDomain = domain.replace("//", "");
		String [] domainAray = plainDomain.split(":");
		String finalIp = "";
		if(domainAray.length>1) {
			finalIp = domainAray[0] + ":" + domainAray[1];
			return finalIp;
		}else{
			return finalIp;

		}
	}

}
