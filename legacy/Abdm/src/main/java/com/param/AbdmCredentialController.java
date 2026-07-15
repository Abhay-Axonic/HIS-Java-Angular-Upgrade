package com.param;

import java.io.BufferedReader;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import com.param.global.billing.controller.BillingException;
import com.param.global.common.Response;
import com.param.src.main.java.in.projecteka.fidelius.dercyprion.DecryptionRequest;
import com.param.src.main.java.in.projecteka.fidelius.encryotion.EncryptionRequest;
import com.param.src.main.java.in.projecteka.fidelius.keys.KeyMaterial;



@RequestMapping(value="/api/abdm")
@SuppressWarnings({"rawtypes"})
@RestController
public class AbdmCredentialController {

	private static final Logger logger = Logger.getLogger(AbdmCredentialController.class);

	@Autowired
	IAbdmCredentialControllerService iAbdmCredentialControllerService;
	
	
	@RequestMapping(value = "/getCredentialDetails", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getCredentialDetails(@RequestBody AbdmCredentialdDto abdmCredentialdDto) {
		try {
			return iAbdmCredentialControllerService.getCredentialDetails(abdmCredentialdDto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
			//throw new BillingException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/registerOrgnaisationInABDM", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response registerOrgnaisationInABDM(@RequestBody AbdmCredentialdDto abdmCredentialdDto) {
		try {
			return iAbdmCredentialControllerService.registerOrgnaisationInABDM(abdmCredentialdDto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
			//throw new BillingException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/addUpdateServicesInAbdm", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response addUpdateServicesInAbdm(@RequestBody AbdmCredentialdDto abdmCredentialdDto) {
		try {
			return iAbdmCredentialControllerService.addUpdateServicesInAbdm(abdmCredentialdDto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
			//throw new BillingException(e.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/generateOtp", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response generateOtpAadhar(@RequestBody AbdmCredentialdDto abdmCredentialdDto) {
		try {
			return iAbdmCredentialControllerService.generateOtpAadhar(abdmCredentialdDto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
			//throw new BillingException(e.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/aadhaar/verifyOTP", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response verifyOTPOtpAadhar(@RequestBody AbdmCredentialdDto abdmCredentialdDto) {
		try {
			  return iAbdmCredentialControllerService.verifyOTPOtpAadhar(abdmCredentialdDto);
			} catch (BillingException be) {
				throw be;
			} catch (Exception e) {
				
				throw e;
				//throw new BillingException(e.getMessage());
			}
		}
	@RequestMapping(value = "/addGetServiceAbdm", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response addGetServiceAbdm(@RequestBody AbdmCredentialdDto abdmCredentialdDto) {
		try {
			return iAbdmCredentialControllerService.addGetServiceAbdm(abdmCredentialdDto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
			//throw new BillingException(e.getMessage());
		}
	}
	
	
	
	@RequestMapping(value = "/aadhaar/resendAadhaarOtp", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response resendAadhaarOtp(@RequestBody AbdmCredentialdDto abdmCredentialdDto) {
		try {
			return iAbdmCredentialControllerService.resendAadhaarOtp(abdmCredentialdDto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
			//throw new BillingException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/aadhaar/checkAndGenerateMobileOTP", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response checkAndGenerateMobileOTP(@RequestBody AbdmCredentialdDto abdmCredentialdDto) {
		try {
			return iAbdmCredentialControllerService.checkAndGenerateMobileOTP(abdmCredentialdDto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
			//throw new BillingException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/aadhaar/checkHealthCardAlreadyExists", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response checkHealthCardAlreadyExists(@RequestBody AbdmCredentialdDto abdmCredentialdDto) {
		try {
			return iAbdmCredentialControllerService.checkHealthCardAlreadyExists(abdmCredentialdDto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
			//throw new BillingException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getpngCard", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getpngCard(@RequestBody AbdmCredentialdDto abdmCredentialdDto) {
		try {
			return iAbdmCredentialControllerService.getpngCard(abdmCredentialdDto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
			//throw new BillingException(e.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/auth/fetch-modes", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response fetchModesValidation(@RequestBody AbdmCredentialdDto abdmCredentialdDto) {
		try {
			return iAbdmCredentialControllerService.fetchModesValidation(abdmCredentialdDto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
			//throw new BillingException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/auth/on-fetch-modes", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response onFetchModes(@RequestBody AbdmCredentialdDto abdmCredentialdDto) {
		try {
			return iAbdmCredentialControllerService.onFetchModes(abdmCredentialdDto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
			//throw new BillingException(e.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/auth/init", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response authInit(@RequestBody AbdmCredentialdDto abdmCredentialdDto) {
		try {
			return iAbdmCredentialControllerService.authInit(abdmCredentialdDto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
			//throw new BillingException(e.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/auth/confirm", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response authConfirm(@RequestBody AbdmCredentialdDto abdmCredentialdDto) {
		try {
			return iAbdmCredentialControllerService.authConfirm(abdmCredentialdDto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
			//throw new BillingException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/auth/getProfileDetailsByAdharNumber", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getProfileDetailsByAdharNumber(@RequestBody AbdmCredentialdDto abdmCredentialdDto) {
		try {
			return iAbdmCredentialControllerService.getProfileDetailsByAdharNumber(abdmCredentialdDto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
			//throw new BillingException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/HIP-QRcodeShare", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response QRcodeShare(@RequestBody AbdmCredentialdDto abdmCredentialdDto) {
		try {
			return iAbdmCredentialControllerService.QRcodeShare(abdmCredentialdDto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
			//throw new BillingException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/saveDetailsOfAbdm", method = RequestMethod.POST)
	public @ResponseBody Response saveDetailsOfAbdm(HttpServletRequest request,HttpServletResponse response) throws Exception {
		try {
			System.out.println("Inside the saveDetailsOfAbdm"+request.toString());
			Response res = null;
			String callbackApiHeaderValue = request.getHeader("API-Name");
			String facilityId = request.getHeader("x-hip-id");
			String requestId = request.getHeader("request-id");
			String hiuId  = request.getHeader("X-HIU-ID");
			String timeStamp = request.getHeader("TIMESTAMP");
			
			String headerValue = "";
			String headerName = "";
			Enumeration<String> headerNames = request.getHeaderNames();
			headerName = headerNames.nextElement();
			headerValue = request.getHeader(headerName);
			System.out.println("Header: " + headerName + " = " + headerValue);
			
			System.out.println("API call success ");
			StringBuilder stringBuilder = new StringBuilder();
			BufferedReader reader = request.getReader();
			String line;
			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line);
			}

			String requestBody = stringBuilder.toString();
			System.out.println("My Request Body issss:" + requestBody);
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode jsonNode = objectMapper.readTree(requestBody);

			String responseRequestId = jsonNode.path("resp").path("requestId").asText();// //back call request id
			AbdmCredentialdDto ab = new AbdmCredentialdDto();
			ab.setRequestId(requestId);
			if (callbackApiHeaderValue.equals("on-share")) {
				ab.setShareJson(requestBody);
				ab.setHipId(facilityId);
				ab.setRequestId(requestId);
				System.out.println("My On share saveDetailsOfAbdm call: ");
				System.out.println("My On share Request Id is: "+requestId);
				ab.setEventId(27);
				res = iAbdmCredentialControllerService.saveShareResponse(ab);
			}
			else if (callbackApiHeaderValue.equals("on-generate-token")) {
				ab.setCallBackJsononGenerateToken(requestBody);
				ab.setHipId(facilityId);
				ab.setRequestId(requestId);
				ab.setEventId(39);
				res = iAbdmCredentialControllerService.saveOnGenerateToken(ab);
			} else if (callbackApiHeaderValue.equals("on_carecontext")) {
				Thread.sleep(1000);
				ab.setCallBackJsonOnAddContexts(requestBody);
				ab.setHipId(facilityId);
				ab.setRequestId(requestId);
				ab.setEventId(40);
				res = iAbdmCredentialControllerService.saveOnCarecontext(ab);
			}
			else if (callbackApiHeaderValue.equals("carecontext-on-notify")) {
				ab.setCarecontextOnNotifyJson(requestBody);
				ab.setHipId(facilityId);
				ab.setRequestId(requestId);
				ab.setEventId(49);
				res = iAbdmCredentialControllerService.saveCarecontextOnNotify(ab);
			}
			
			else if (callbackApiHeaderValue.equals("on-discover")) {
				ab.setHipId(facilityId);
				ab.setRequestId(requestId);
				ab.setEventId(30);
				ab.setDiscoverJson(requestBody); 
				System.out.println("My {callback_url}/api/v3/hiu/patient/care-context/on-discover  is: "+requestBody);
				res =  iAbdmCredentialControllerService.v3UserInitiatedLinkingPatientCareContextOnDiscover(ab);
			}
			else if (callbackApiHeaderValue.equals("on-init")) {
				ab.setHipId(facilityId);
				ab.setHiuId(hiuId);
				ab.setRequestId(requestId);
				ab.setEventId(33);
				ab.setDiscoverJson(requestBody); 
				System.out.println("My : {callback_url}/api/v3/hip/link/care-context/init  is: "+requestBody);
				res =  iAbdmCredentialControllerService.v3UserInitiatedLinkingLinkCareContextOnInit(ab);
			}
			else if (callbackApiHeaderValue.equals("on-init-hiu")) {
				//M3 on init
				
				ab.setHipId(facilityId);
				ab.setHiuId(hiuId);
				ab.setRequestId(requestId);
				ab.setEventId(33);
				ab.setOnInitJson(requestBody); 
				System.out.println("My : {callback_url}/api/v3/hiu/consent/request/on-init  is: "+requestBody);
				res =  iAbdmCredentialControllerService.v3ConsentRequestOnInit(ab);
			}
			else if (callbackApiHeaderValue.equals("sms-on-notify")) {
				ab.setHipId(facilityId);
				ab.setRequestId(requestId);
				ab.setEventId(35);
				ab.setTimeStamp(timeStamp);
				ab.setConsentsHipNotifyJson(requestBody); 
				System.out.println("My : {callback_url}/api/v3/consent/request/hip/notify  is: "+requestBody);
				res =  iAbdmCredentialControllerService.v3ConsentRequestHipOnNotify(ab);
			}else if (callbackApiHeaderValue.equals("health-information-request")) {
				ab.setHipId(facilityId);
				ab.setRequestId(requestId);
				ab.setEventId(36);
				ab.setHipRequestJson(requestBody); 
				res =  iAbdmCredentialControllerService.v3DataFlowHealthInformationHipOnRequest(ab);
			}
			else if (callbackApiHeaderValue.equals("on-confirm")) {
				ab.setHipId(facilityId);
				ab.setRequestId(requestId);
				ab.setEventId(34);
				ab.setConfirmJson(requestBody); 
				System.out.println("My : {callback_url}/api/v3/hip/link/care-context/confirm : "+requestBody);
				res =  iAbdmCredentialControllerService.v3UserInitiatedLinkingLinkCareContextOnConfirm(ab);
			}
			
			else if (callbackApiHeaderValue.equals("on-request")) {
				ab.setHipId(facilityId);
				ab.setHiuId(hiuId);
				ab.setRequestId(requestId);
				ab.setEventId(34);
				ab.setConfirmJson(requestBody); 
				res =  iAbdmCredentialControllerService.v3UserInitiatedLinkingLinkCareContextOnConfirm(ab);
			}
			else if (callbackApiHeaderValue.equals("on-fetch")) {
				ab.setHipId(facilityId);
				ab.setHiuId(hiuId);
				ab.setRequestId(requestId);
				ab.setEventId(45);
				ab.setOnFetchJson(requestBody); 
				System.out.println("On Fetch Response: "+requestBody);
				res =iAbdmCredentialControllerService.v3DataFlowHealthInformationRequest(ab);
			}
			else if (callbackApiHeaderValue.equals("on-request-hiu")) {
				ab.setHipId(facilityId);
				ab.setHiuId(hiuId);
				ab.setRequestId(requestId);
				ab.setEventId(46);
				ab.setOnRequestJson(requestBody); 
				System.out.println("On Request json Response: "+requestBody);
				res =  iAbdmCredentialControllerService.v3OnRequestHiu(ab);
			}
			else if (callbackApiHeaderValue.equals("notify")) {
				ab.setHipId(facilityId);
				ab.setHiuId(hiuId);
				ab.setTimeStamp(timeStamp);
				if(headerName.equals("request-id")) {
					requestId=headerValue;
					System.out.println("Header Request Id Notify call iss: "+requestId);
					ab.setRequestId(requestId);
				}
				ab.setEventId(43);
				ab.setNotifyConsentJson(requestBody); 
				res =  iAbdmCredentialControllerService.v3ConsentRequestNotify(ab);
			}
			else if (callbackApiHeaderValue.equals("on-status")) {
				ab.setHipId(facilityId);
				ab.setHiuId(hiuId);
				ab.setRequestId(requestId);
				ab.setConfirmJson(requestBody); 
				res =  iAbdmCredentialControllerService.v3HiuConsentRequestOnStatus(ab);
			}
			else if (callbackApiHeaderValue.equals("on-fetch-modes")) {
				ab.setRequestId(requestId);
				ab.setCallBackJsonoNFetchMode(requestBody);
				ab.setRequestIdFetch(responseRequestId);
				ab.setHipId(facilityId);
				res = iAbdmCredentialControllerService.saveAbdmCallBackDetails(ab);
			} else if (callbackApiHeaderValue.equals("on-init")) {
				ab.setRequestId(requestId);
				ab.setCallBackJsonoNOnInit(requestBody);
				ab.setRequestIdInit(responseRequestId);
				ab.setHipId(facilityId);
				res = iAbdmCredentialControllerService.updateOnInitCall(ab);

			} else if (callbackApiHeaderValue.equals("on-confirm")) {
				ab.setRequestId(requestId);
				ab.setCallBackJsonoNOnConfirm(requestBody);
				ab.setRequestIdConfirm(responseRequestId);
				ab.setHipId(facilityId);
				res = iAbdmCredentialControllerService.updateOnConfirm(ab);
			} else if (callbackApiHeaderValue.equals("on-share")) {
				ab.setShareJson(requestBody);
				ab.setHipId(facilityId);
				ab.setRequestId(requestId);
				System.out.println("My On share Request Id call: ");
				System.out.println("My On share Request Id is: "+requestId);
				res = iAbdmCredentialControllerService.saveShareResponse(ab);
			} else if (callbackApiHeaderValue.equals("discover")) {
				ab.setDiscoverJson(requestBody);
				ab.setHipId(facilityId);
				ab.setRequestId(requestId);
				res = iAbdmCredentialControllerService.saveDiscoverResponse(ab);
			}else if (callbackApiHeaderValue.equals("init")) {
				ab.setInitJson(requestBody);
				ab.setHipId(facilityId);
				ab.setRequestId(requestId);
				res = iAbdmCredentialControllerService.saveinitDiscoverResponse(ab);
			}
			else if (callbackApiHeaderValue.equals("confirm")) {
				ab.setConfirmJson(requestBody);
				ab.setHipId(facilityId);
				ab.setRequestId(requestId);
				res = iAbdmCredentialControllerService.saveConfirmDiscoverResponse(ab);
			}
			else if (callbackApiHeaderValue.equals("onAddContexts")) {
				ab.setOnAddContextsJson(requestBody);
				ab.setHipId(facilityId);
				ab.setRequestId(requestId);
				res = iAbdmCredentialControllerService.saveOnAddContextsResponse(ab);
			}
			else if (callbackApiHeaderValue.equals("links-context-on-notify")) {
				ab.setOnAddContextsJson(requestBody);
				ab.setHipId(facilityId);
				ab.setRequestId(requestId);
				res = iAbdmCredentialControllerService.saveLinksContextOnNotify(ab);
			}
			else if (callbackApiHeaderValue.equals("patients-sms-on-notify")) {
				ab.setOnAddContextsJson(requestBody);
				ab.setHipId(facilityId);
				ab.setRequestId(requestId);
				res = iAbdmCredentialControllerService.savePatientSmsOnNotify(ab);
			}
			else if (callbackApiHeaderValue.equals("consents-hip-notify")) {
				ab.setConsentsHipNotifyJson(requestBody);
				ab.setHipId(facilityId);
				ab.setRequestId(requestId);
				res = iAbdmCredentialControllerService.saveConsentsHipNotify(ab);
			}
			else if (callbackApiHeaderValue.equals("health-information-hip-request")) {
				ab.setHipRequestJson(requestBody);
				ab.setHipId(facilityId);
				ab.setRequestId(requestId);
				res = iAbdmCredentialControllerService.saveHealthInformationHipRequest(ab);
			}

			return res;

		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
		}
	}
	
	
	
	@RequestMapping(value = "/savePatientRegistrationFromABDMinHIS", method = RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response savePatientRegistrationFromABDMinHIS(@RequestBody AbdmCredentialdDto abdmCredentialdDto) {
		try {
			return iAbdmCredentialControllerService.savePatientRegistrationFromABDMinHIS(abdmCredentialdDto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
		}
	}
	
	
	@RequestMapping(value = "/savePatientRegistrationAfterConfirmCall", method = RequestMethod.POST ,consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response savePatientRegistrationAfterConfirmCall(@RequestBody AbdmCredentialdDto abdmCredentialdDto) {
		try {
			return iAbdmCredentialControllerService.savePatientRegistrationAfterConfirmCall(abdmCredentialdDto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
		}
	}
	

	@RequestMapping(value = "/getShareDetails", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getShareDetails(@RequestBody AbdmCredentialdDto abdmCredentialdDto) {
		try {
			return iAbdmCredentialControllerService.getShareDetails(abdmCredentialdDto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
		}
	}
	
	@RequestMapping(value = "/auth/getTranscationTableDetailsById", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getTranscationTableDetailsById(@RequestBody AbdmCredentialdDto abdmCredentialdDto) {
		try {
			return iAbdmCredentialControllerService.getTranscationTableDetailsById(abdmCredentialdDto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
		}
	}
	
	
	
	@RequestMapping(value = "/searchByHealthId ", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response searchByHealthId (@RequestBody AbdmCredentialdDto abdmCredentialdDto) {
		try {
			  return iAbdmCredentialControllerService.searchByHealthId(abdmCredentialdDto);
			} catch (BillingException be) {
				throw be;
			} catch (Exception e) {
				
				throw e;
			}
		}
	
	@RequestMapping(value = "/auth/init/SerachByHealthId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response authInitMethod(@RequestBody AbdmCredentialdDto abdmCredentialdDto) {
		try {
			  return iAbdmCredentialControllerService.authInitMethod(abdmCredentialdDto);
			} catch (BillingException be) {
				throw be;
			} catch (Exception e) {
				
				throw e;
			}
		}
	
	
	@RequestMapping(value = "/confirmWithMobileOtp ", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response confirmWithMobileOtp(@RequestBody AbdmCredentialdDto abdmCredentialdDto) {
		try {
			  return iAbdmCredentialControllerService.confirmWithMobileOtp(abdmCredentialdDto);
			} catch (BillingException be) {
				throw be;
			} catch (Exception e) {
				
				throw e;
			}
		}
	
	@RequestMapping(value = "/confirmWithAadhaarOtp ", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response confirmWithAadhaarOtp(@RequestBody AbdmCredentialdDto abdmCredentialdDto) {
		try {
			  return iAbdmCredentialControllerService.confirmWithAadhaarOtp(abdmCredentialdDto);
			} catch (BillingException be) {
				throw be;
			} catch (Exception e) {
				
				throw e;
			}
		}
	
	
	
	
	
	@RequestMapping(value = "/aadhaar/verifyMobileOTP", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response verifyMobileOTP(@RequestBody AbdmCredentialdDto abdmCredentialdDto) {
		try {
			return iAbdmCredentialControllerService.verifyMobileOTP(abdmCredentialdDto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
		}
	}
	
	
	
	
	
	@RequestMapping(value = "/aadhaar/createHealthIdWithPreVerified", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response createHealthIdWithPreVerified(@RequestBody AbdmCredentialdDto abdmCredentialdDto) {
		try {
			return iAbdmCredentialControllerService.createHealthIdWithPreVerified(abdmCredentialdDto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
		}
	}
	
	
	
	
	@RequestMapping(value = "/aadhaar/profileDetails", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response profileDetails(@RequestBody AbdmCredentialdDto abdmCredentialdDto) {
		try {
			return iAbdmCredentialControllerService.profileDetails(abdmCredentialdDto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
		}
	}
	
	
	
	@RequestMapping(value = "/aadhaar/suggestion", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response suggestion(@RequestBody AbdmCredentialdDto abdmCredentialdDto) {
		try {
			return iAbdmCredentialControllerService.suggestion(abdmCredentialdDto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
		}
	}
	
	
	
	@RequestMapping(value = "/aadhaar/isExist", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response isExist(@RequestBody AbdmCredentialdDto abdmCredentialdDto) {
		try {
			return iAbdmCredentialControllerService.isExist(abdmCredentialdDto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
		}
	}
	
	@RequestMapping(value = "/aadhaar/createPhrAddress", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response createPhrAddress(@RequestBody AbdmCredentialdDto abdmCredentialdDto) {
		try {
			return iAbdmCredentialControllerService.createPhrAddress(abdmCredentialdDto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
		}
	}
	
	
	@RequestMapping(value = "/aadhaar/getPngCardNew", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getPngCard(@RequestBody AbdmCredentialdDto abdmCredentialdDto) {
		try {
			return iAbdmCredentialControllerService.getPngCardNew(abdmCredentialdDto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
		}
	}
	
	
	@RequestMapping(value = "/add-contexts", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response addContexts(@RequestBody AbdmCredentialdDto abdmCredentialdDto) {
		try {
			return iAbdmCredentialControllerService.addContexts(abdmCredentialdDto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
		}
	}
	
	
	@RequestMapping(value = "/links/context/notify", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response consentsHipNotify(@RequestBody AbdmCredentialdDto abdmCredentialdDto) {
		try {
			return iAbdmCredentialControllerService.consentsHipNotify(abdmCredentialdDto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
		}
	}
	
	@RequestMapping(value = "/patients/sms/notify2", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response patientsSmsNotify2(@RequestBody AbdmCredentialdDto abdmCredentialdDto) {
		try {
			return iAbdmCredentialControllerService.patientsSmsNotify2(abdmCredentialdDto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
		}
	}
	
	@RequestMapping(value = "/searchByMobile", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response searchByMobile(@RequestBody AbdmCredentialdDto abdmCredentialdDto) throws Exception {
		try {
			return iAbdmCredentialControllerService.searchByMobile(abdmCredentialdDto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
		}
	}
	

	@RequestMapping(value = "/saveAbdmShareResponse", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveAbdmShareResponse(@RequestBody AbdmCredentialdDto abdmCredentialdDto)throws Exception {
		try {
			return iAbdmCredentialControllerService.saveAbdmShareResponse(abdmCredentialdDto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
		}
	}
	

	@RequestMapping(value = "/mobile/resendOtp", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response mobileResendOtp(@RequestBody AbdmCredentialdDto abdmCredentialdDto) throws Exception {
		try {
			return iAbdmCredentialControllerService.mobileResendOtp(abdmCredentialdDto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
			//throw new BillingException(e.getMessage());
		}
	}
	
	///login/request/otp
	@RequestMapping(value = "/login/otp", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response generateOtpForLogin(@RequestBody AbdmCredentialdDto abdmCredentialdDto) throws Exception{
		try {
			return iAbdmCredentialControllerService.generateOtpForAadhaarNumber(abdmCredentialdDto);
		} catch (Exception e) {
			throw e;
		}
	}
	
	///login/request/otp for mobile number only
		@RequestMapping(value = "/v3/profile/login/request/otp", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response v3ProfileLoginRequestOtp(@RequestBody AbdmCredentialdDto abdmCredentialdDto) throws Exception{
			try {
				return iAbdmCredentialControllerService.v3ProfileLoginRequestOtp(abdmCredentialdDto);
			} catch (Exception e) {
				throw e;
			}
		}


	// v3/profile/login/verify
	@RequestMapping(value = "/login/verify", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response verifyOtpForLogin(@RequestBody AbdmCredentialdDto abdmCredentialdDto) throws Exception {
		try {
			return iAbdmCredentialControllerService.verifyOtpForAadhaar(abdmCredentialdDto);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@RequestMapping(value = "/login/verify/user", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response verifyUserForLogin(@RequestBody AbdmCredentialdDto abdmCredentialdDto) throws Exception {
		try {
			return iAbdmCredentialControllerService.verifyUserForAbhaLogin(abdmCredentialdDto);
		} catch (Exception e) {
			throw e;
		}
	}

	@RequestMapping(value = "/profile/account", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getAbhaAccount(@RequestBody AbdmCredentialdDto abdmCredentialdDto) throws Exception {
		try {
			return iAbdmCredentialControllerService.getPofileDetails(abdmCredentialdDto);
		} catch (Exception e) {
			throw e;
		}
	}

	@RequestMapping(value = "/profile/account/abha-card", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getAbhaCard(@RequestBody AbdmCredentialdDto abdmCredentialdDto) throws Exception {
		try {
			return iAbdmCredentialControllerService.getAbhaCard(abdmCredentialdDto);
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	@RequestMapping(value = "/request/otp", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response requestOtpForAadhaar(@RequestBody AbdmCredentialdDto abdmCredentialdDto) throws Exception{
		try {
		
			return iAbdmCredentialControllerService.requestOtpForAadhaar(abdmCredentialdDto);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@RequestMapping(value = "/enrollment/enrol/byAadhaar", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response enrollmentEnrolByAadhaar(@RequestBody AbdmCredentialdDto abdmCredentialdDto) throws Exception{
		try {
			return iAbdmCredentialControllerService.enrollmentEnrolByAadhaar(abdmCredentialdDto);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@RequestMapping(value = "/enrollment/auth/byAbdm", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response enrollmentAuthByAbdm(@RequestBody AbdmCredentialdDto abdmCredentialdDto) throws Exception{
		try {
			return iAbdmCredentialControllerService.enrollmentAuthByAbdm(abdmCredentialdDto);

		} catch (Exception e) {
			throw e;
		}
	}

	@RequestMapping(value = "/enrollment/enrol/suggestion", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response enrollmentEnrolSuggestion(@RequestBody AbdmCredentialdDto abdmCredentialdDto) throws Exception{
		try {
			return iAbdmCredentialControllerService.enrollmentEnrolSuggestion(abdmCredentialdDto);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@RequestMapping(value = "/enrollment/enrol/abha-address", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response enrollmentEnrolABhaAddress(@RequestBody AbdmCredentialdDto abdmCredentialdDto) throws Exception{
		try {
			return iAbdmCredentialControllerService.enrollmentEnrolABhaAddress(abdmCredentialdDto);
		} catch (Exception e) {
			throw e;
		}
	}


	@RequestMapping(value = "/account/getCard", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getCard(@RequestBody AbdmCredentialdDto abdmCredentialdDto) throws Exception {
		try {
			return iAbdmCredentialControllerService.getCard(abdmCredentialdDto);
		} catch (Exception e) {
			throw e;
		}
	}

	//Not sure about the Payload as I got the payload for V! have set the values according to that
	@RequestMapping(value = "hid/benefit/createHealthId/demo/auth", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response hidBenifitCreateHealthIdDemoAuth(@RequestBody AbdmCredentialdDto abdmCredentialdDto) throws Exception{
		try {
			return iAbdmCredentialControllerService.hidBenifitCreateHealthIdDemoAuth(abdmCredentialdDto);
		} catch (Exception e) {
			throw e;
		}
	}
	
	@RequestMapping(value = "/enrollment/enrol/byDocument", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response enrollmentEnrolbyDocument(@RequestBody AbdmCredentialdDto abdmCredentialdDto) throws Exception {
		try {
			return iAbdmCredentialControllerService.enrollmentEnrolbyDocument(abdmCredentialdDto);
		} catch (Exception e) {
			throw e;
		}
	}

	
	@RequestMapping(value = "/v2/auth/init", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response authInitVersionNew(@RequestBody AbdmCredentialdDto abdmCredentialdDto) throws Exception {
		try {
			return iAbdmCredentialControllerService.authInitVersionNew(abdmCredentialdDto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
			//throw new BillingException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/v2/auth/confirmWithAadhaarOtp", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response authConfirmWithAadhaarOtp(@RequestBody AbdmCredentialdDto abdmCredentialdDto) throws Exception {
		try {
			return iAbdmCredentialControllerService.authConfirmWithAadhaarOtp(abdmCredentialdDto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
			//throw new BillingException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/v2/account/profile", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getAccountProfile(@RequestBody AbdmCredentialdDto abdmCredentialdDto) throws Exception {
		try {
			return iAbdmCredentialControllerService.getAccountProfile(abdmCredentialdDto);
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	@RequestMapping(value = "/v2/auth/confirmWithMobileOTP", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response v2AuthConfirmWithMobileOTP(@RequestBody AbdmCredentialdDto abdmCredentialdDto) throws Exception {
		try {
			return iAbdmCredentialControllerService.v2AuthConfirmWithMobileOTP(abdmCredentialdDto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
			//throw new BillingException(e.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/v3/phr/web/login/abha/search", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response v3PhrWebLoginAbhaSearch(@RequestBody AbdmCredentialdDto abdmCredentialdDto) throws Exception {
		try {
			return iAbdmCredentialControllerService.v3PhrWebLoginAbhaSearch(abdmCredentialdDto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
		}
	}
	
	@RequestMapping(value = "/v3/phr/web/login/abha/request/otp", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response v3PhrWebLoginAbhaRequestOtp(@RequestBody AbdmCredentialdDto abdmCredentialdDto) throws Exception {
		try {
			return iAbdmCredentialControllerService.v3PhrWebLoginAbhaRequestOtp(abdmCredentialdDto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
		}
	}
	
	
	@RequestMapping(value = "/v3/phr/web/login/abha/verify", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response v3PhrWebLoginAbhaVerify(@RequestBody AbdmCredentialdDto abdmCredentialdDto) throws Exception {
		try {
			return iAbdmCredentialControllerService.v3PhrWebLoginAbhaVerify(abdmCredentialdDto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
		}
	}
	
	
	@RequestMapping(value = "/v3/phr/web/login/profile/abha-profile", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response v3PhrWebloginProfileAbhaProfile(@RequestBody AbdmCredentialdDto abdmCredentialdDto) throws Exception {
		try {
			return iAbdmCredentialControllerService.v3PhrWebloginProfileAbhaProfile(abdmCredentialdDto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
		}
	}
	
	@RequestMapping(value = "/v3/phr/web/login/profile/abha/phr-card", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response v3PhrWebloginProfileAbhaPhrCard(@RequestBody AbdmCredentialdDto abdmCredentialdDto) throws Exception {
		try {
			return iAbdmCredentialControllerService.v3PhrWebloginProfileAbhaPhrCard(abdmCredentialdDto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
		}
	}
	
	
	@RequestMapping(value = "/v3/token/generate-token", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response v3TokenGenerateToken(@RequestBody AbdmCredentialdDto abdmCredentialdDto) throws Exception {
		try {
			return iAbdmCredentialControllerService.v3TokenGenerateToken(abdmCredentialdDto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
		}
	}
	
	@RequestMapping(value = "/v3/link/carecontext", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response v3LinkCarecontext(@RequestBody AbdmCredentialdDto abdmCredentialdDto) throws Exception {
		try {
			return iAbdmCredentialControllerService.v3LinkCarecontext(abdmCredentialdDto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
		}
	}
	
	@RequestMapping(value = "/v3/link/carecontextNew", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response v3LinkCarecontextNew(@RequestBody AbdmCredentialdDto abdmCredentialdDto) throws Exception {
		try {
			return iAbdmCredentialControllerService.v3LinkCarecontextNew(abdmCredentialdDto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
		}
	}
	

	@RequestMapping(value = "/v3/link/context/notify", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response v3LinkCarecontextNotify(@RequestBody AbdmCredentialdDto abdmCredentialdDto) throws Exception {
		try {
			return iAbdmCredentialControllerService.v3LinkCarecontextNotify(abdmCredentialdDto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
		}
	}
	
	@RequestMapping(value = "/v3/user-initiated-linking/patient/care-context/on-discover", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response v3UserInitiatedLinkingPatientCareContextOnDiscover(@RequestBody AbdmCredentialdDto abdmCredentialdDto) throws Exception {
		try {
			return iAbdmCredentialControllerService.v3UserInitiatedLinkingPatientCareContextOnDiscover(abdmCredentialdDto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
		}
	}
	
	@RequestMapping(value = "/v3/user-initiated-linking/link/care-context/on-init", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response v3UserInitiatedLinkingLinkCareContextOnInit(@RequestBody AbdmCredentialdDto abdmCredentialdDto) throws Exception {
		try {
			return iAbdmCredentialControllerService.v3UserInitiatedLinkingLinkCareContextOnInit(abdmCredentialdDto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
		}
	}
	
	@RequestMapping(value = "/v3/user-initiated-linking/link/care-context/on-confirm", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response v3UserInitiatedLinkingLinkCareContextOnConfirm(@RequestBody AbdmCredentialdDto abdmCredentialdDto) throws Exception {
		try {
			return iAbdmCredentialControllerService.v3UserInitiatedLinkingLinkCareContextOnConfirm(abdmCredentialdDto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
		}
	}
	
	@RequestMapping(value = "/v3/data-flow/health-information/hip/on-request", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response v3DataFlowHealthInformationHipOnRequest(@RequestBody AbdmCredentialdDto abdmCredentialdDto) throws Exception {
		try {
			return iAbdmCredentialControllerService.v3DataFlowHealthInformationHipOnRequest(abdmCredentialdDto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
		}
	}
	
	@RequestMapping(value = "/patient-hiu/data/notification", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response patientHiuDataNotification(@RequestBody AbdmCredentialdDto abdmCredentialdDto) throws Exception {
		try {
			return iAbdmCredentialControllerService.patientHiuDataNotification(abdmCredentialdDto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
		}
	}
	
	@RequestMapping(value = "/v3/data-flow/health-information/notify", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response v3DataFlowHealthInformationNotify(@RequestBody AbdmCredentialdDto abdmCredentialdDto) throws Exception {
		try {
			return iAbdmCredentialControllerService.v3DataFlowHealthInformationNotify(abdmCredentialdDto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
		}
	}
	@RequestMapping(value = "/v3/bridge/url", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response v3BridgeUrl(@RequestBody AbdmCredentialdDto abdmCredentialdDto) {
		try {
			return iAbdmCredentialControllerService.v3BridgeUrl(abdmCredentialdDto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new BillingException(e.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/v3/link/patient/links/sms/notify2", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response v3LinkPatientLinksSmsNotify2(@RequestBody AbdmCredentialdDto abdmCredentialdDto) {
		try {
			return iAbdmCredentialControllerService.v3LinkPatientLinksSmsNotify2(abdmCredentialdDto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new BillingException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/v3/consent/request/hip/on-notify", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response v3ConsentRequestHipOnNotify(@RequestBody AbdmCredentialdDto abdmCredentialdDto) {
		try {
			return iAbdmCredentialControllerService.v3ConsentRequestHipOnNotify(abdmCredentialdDto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new BillingException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "updateShareDetailsByTokenNumber", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateShareDetailsByTokenNumber(@RequestBody AbdmCredentialdDto abdmCredentialdDto) {
		try {
			return iAbdmCredentialControllerService.updateShareDetailsByTokenNumber(abdmCredentialdDto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new BillingException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getPatientReqDetails", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getPatientReqDetails(@RequestBody AbdmCredentialdDto abdmCredentialdDto) throws Exception {
		try {
			return iAbdmCredentialControllerService.getPatientReqDetails(abdmCredentialdDto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
		}
	}
	
	@RequestMapping(value = "/getEncounterNumberByPatientId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getEncounterNumberByPatientId (@RequestBody AbdmCredentialdDto abdmCredentialdDto) throws Exception {
		try {
			  return iAbdmCredentialControllerService.getEncounterNumberByPatientId(abdmCredentialdDto);
			} catch (BillingException be) {
				throw be;
			} catch (Exception e) {
				
				throw e;
			}
		}
	
	@RequestMapping(value = "/getAdmissionNumberByPatientId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getAdmissionNumberByPatientId (@RequestBody AbdmCredentialdDto abdmCredentialdDto) throws Exception {
		try {
			  return iAbdmCredentialControllerService.getAdmissionNumberByPatientId(abdmCredentialdDto);
			} catch (BillingException be) {
				throw be;
			} catch (Exception e) {
				
				throw e;
			}
		}
	
	
	@RequestMapping(value = "/v3/consent/request/init", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response v3ConsentRequestInit (@RequestBody AbdmCredentialdDto abdmCredentialdDto) throws Exception {
		try {
			  return iAbdmCredentialControllerService.v3ConsentRequestInit(abdmCredentialdDto);
			} catch (BillingException be) {
				throw be;
			} catch (Exception e) {
				
				throw e;
			}
		}
	
	
	@RequestMapping(value = "/v3/consent/request/status", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response v3ConsentRequestStatus (@RequestBody AbdmCredentialdDto abdmCredentialdDto) throws Exception {
		try {
			  return iAbdmCredentialControllerService.v3ConsentRequestStatus(abdmCredentialdDto);
			} catch (BillingException be) {
				throw be;
			} catch (Exception e) {
				
				throw e;
			}
		}
	
	@RequestMapping(value = "/v3/consent/request/hiu/on-notify", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response v3ConsentRequestHiuOnNotify (@RequestBody AbdmCredentialdDto abdmCredentialdDto) throws Exception {
		try {
			  return iAbdmCredentialControllerService.v3ConsentRequestHiuOnNotify(abdmCredentialdDto);
			} catch (BillingException be) {
				throw be;
			} catch (Exception e) {
				
				throw e;
			}
		}
	
	
	
	@RequestMapping(value = "/v3/consent/fetch", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response v3ConsentFetch (@RequestBody AbdmCredentialdDto abdmCredentialdDto) throws Exception {
		try {
			  return iAbdmCredentialControllerService.v3ConsentFetch(abdmCredentialdDto);
			} catch (BillingException be) {
				throw be;
			} catch (Exception e) {
				
				throw e;
			}
		}
	
	
	@RequestMapping(value = "/getGrantedHiTypes", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getGrantedHiTypes (@RequestBody AbdmCredentialdDto abdmCredentialdDto) throws Exception {
		try {
			  return iAbdmCredentialControllerService.getGrantedHiTypes(abdmCredentialdDto);
			} catch (BillingException be) {
				throw be;
			} catch (Exception e) {
				
				throw e;
			}
		}
	
	
	
	@RequestMapping(value = "/v3/data-flow/health-information/request", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response v3DataFlowHealthInformationRequest (@RequestBody AbdmCredentialdDto abdmCredentialdDto) throws Exception {
		try {
			  return iAbdmCredentialControllerService.v3DataFlowHealthInformationRequest(abdmCredentialdDto);
			} catch (BillingException be) {
				throw be;
			} catch (Exception e) {
				
				throw e;
			}
		}
	
	@RequestMapping(value = "/v3/health-information/notify", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response v3HealthInformationNotify (@RequestBody AbdmCredentialdDto abdmCredentialdDto) throws Exception {
		try {
			  return iAbdmCredentialControllerService.v3HealthInformationNotify(abdmCredentialdDto);
			} catch (BillingException be) {
				throw be;
			} catch (Exception e) {
				
				throw e;
			}
		}
	
	
	
	@RequestMapping(value = "/v3/request/hiu/on-notify", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response v3RequestHiuOnNotify (@RequestBody AbdmCredentialdDto abdmCredentialdDto) throws Exception {
		try {
			  return iAbdmCredentialControllerService.v3RequestHiuOnNotify(abdmCredentialdDto);
			} catch (BillingException be) {
				throw be;
			} catch (Exception e) {
				
				throw e;
			}
		}
	
	@RequestMapping(value = "/getConsentRequestDetails", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getConsentRequestDetails (@RequestBody AbdmCredentialdDto abdmCredentialdDto) throws Exception {
		try {
			  return iAbdmCredentialControllerService.getConsentRequestDetails(abdmCredentialdDto);
			} catch (BillingException be) {
				throw be;
			} catch (Exception e) {
				
				throw e;
			}
		}
	
	@RequestMapping(value = "/getCareContextCreatedById", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getCareContextCreatedById (@RequestBody AbdmCredentialdDto abdmCredentialdDto) throws Exception {
		try {
			  return iAbdmCredentialControllerService.getCareContextCreatedById(abdmCredentialdDto);
			} catch (BillingException be) {
				throw be;
			} catch (Exception e) {
				
				throw e;
			}
		}

	@RequestMapping(value = "/dataPush", method = RequestMethod.POST)
	public @ResponseBody Response dataPush(HttpServletRequest request,
			@org.springframework.web.bind.annotation.RequestBody String requestBody)
					throws Exception {
		try {
			System.out.println("recived Data from call back URL datapush form other HIU " + requestBody.toString());
			String headerValue = "";
			String headerName = "";
			String facilityId = request.getHeader("X-HIU-ID");
			String requestId = request.getHeader("request-id");
			String hiuId  = request.getHeader("X-HIU-ID");
			String timeStamp = request.getHeader("TIMESTAMP");
			
			try {
				System.out.println("My DatapushURL Data is: "+requestBody.toString());
				
				AbdmCredentialdDto abdmCredentialdDto = new AbdmCredentialdDto();
				abdmCredentialdDto.setHipId(facilityId);
				abdmCredentialdDto.setDataPushUrlJson(requestBody.toString());
				
				return iAbdmCredentialControllerService.getDatapushUrlsDetails(abdmCredentialdDto);
			} catch (Exception e) {
				throw e;
			}
		} catch (Exception ex) {
			throw ex;
		}
	}	
	
	@RequestMapping(value = "/getHiuDecryptedData", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getHiuDecryptedData (@RequestBody AbdmCredentialdDto abdmCredentialdDto) throws Exception {
		try {
			  return iAbdmCredentialControllerService.getHiuDecryptedData(abdmCredentialdDto);
			} catch (BillingException be) {
				throw be;
			} catch (Exception e) {
				
				throw e;
			}
		}
	
	@RequestMapping(value = "/checkAccessOfABDMOfUnit", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response checkAccessOfABDMOfUnit (@RequestBody AbdmCredentialdDto abdmCredentialdDto) throws Exception {
		try {
			  return iAbdmCredentialControllerService.checkAccessOfABDMOfUnit(abdmCredentialdDto);
			} catch (BillingException be) {
				throw be;
			} catch (Exception e) {
				
				throw e;
			}
		}
	
	@RequestMapping(value = "/getPatientDataByPatientId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getPatientDataByPatientId (@RequestBody AbdmCredentialdDto abdmCredentialdDto) throws Exception {
		try {
			  return iAbdmCredentialControllerService.getPatientDataByPatientId(abdmCredentialdDto);
			} catch (BillingException be) {
				throw be;
			} catch (Exception e) {
				
				throw e;
			}
		}
	
	
	@RequestMapping(value = "/v3/updateStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response v3UpdateStatus (@RequestBody AbdmCredentialdDto abdmCredentialdDto) throws Exception {
		try {
			  return iAbdmCredentialControllerService.v3UpdateStatus(abdmCredentialdDto);
			} catch (BillingException be) {
				throw be;
			} catch (Exception e) {
				
				throw e;
			}
		}
	
	
	@RequestMapping(value = "/v3/truncateFHIRDetails", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response truncateFHIRDetails(@RequestBody AbdmCredentialdDto abdmCredentialdDto) throws Exception {
		try {
			  return iAbdmCredentialControllerService.truncateFHIRDetails(abdmCredentialdDto);
			} catch (BillingException be) {
				throw be;
			} catch (Exception e) {
				
				throw e;
			}
		}
	
	
	@RequestMapping(value = "/v3/profile/account/abha/search", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response v3ProfileAccountAbhaSearch(@RequestBody AbdmCredentialdDto abdmCredentialdDto) throws Exception {
		try {
			  return iAbdmCredentialControllerService.v3ProfileAccountAbhaSearch(abdmCredentialdDto);
			} catch (BillingException be) {
				throw be;
			} catch (Exception e) {
				
				throw e;
			}
		}
	
	@RequestMapping(value = "/getLinkedCareContextById", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getLinkedCareContextById(@RequestBody AbdmCredentialdDto abdmCredentialdDto) throws Exception {
		try {
			return iAbdmCredentialControllerService.getLinkedCareContextById(abdmCredentialdDto);
		} catch (BillingException be) {
			throw be;
		} catch (Exception e) {
			
			throw e;
		}
	}
	
}
