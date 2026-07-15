package com.param.adt.master.global.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.param.adt.common.ICommonConstants;
import com.param.adt.common.Response;
import com.param.adt.master.global.dto.ConsentMasterDto;
import com.param.adt.master.global.model.PatientConsentVideoDetails;
import com.param.adt.master.global.service.IConsentMasterService;
import com.param.global.common.ISmsUtilityConstants;
import com.param.global.dto.PatientConsentDocumentDetailsDto;
import com.param.global.dto.PatientConsentMapperDto;
import com.param.global.dto.PatientConsentVideoDetailsDto;
import com.param.global.unit.dto.SmsUtilityConfigInfoDto;
import com.param.global.unit.service.ISmsUtilityConfigService;

@Controller
@RequestMapping("/adt")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ConsentMasterController implements ICommonConstants
{
	@Autowired
	private IConsentMasterService iConsentMasterService;
	
	@Autowired
	private ISmsUtilityConfigService smsUtilityService;
	
	@RequestMapping(value = "/saveConsentMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveConsentMaster(@RequestBody ConsentMasterDto consentMasterDto) {
		try {

			return iConsentMasterService.addConsentMaster(consentMasterDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/getConsentMasterList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getConsentMasterList(@RequestBody ConsentMasterDto consentMasterDto) {
		try {
			return iConsentMasterService.getConsentMasterList(consentMasterDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/updateConsentMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateConsentMaster(@RequestBody ConsentMasterDto consentMasterDto) {
		try {
			return iConsentMasterService.updateConsentMaster(consentMasterDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/getConsentById", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getConsentById(@RequestBody ConsentMasterDto consentMasterDto) {
		try {

			return iConsentMasterService.getConsentById(consentMasterDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/updateStatusForConsent", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateStatusForConsent(@RequestBody ConsentMasterDto consentMasterDto) {
		try {

			return iConsentMasterService.updateStatusForConsent(consentMasterDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/getActiveConsentList/{orgId}/{unitId}", method = RequestMethod.GET)
	public @ResponseBody Response getActiveConsentList(@PathParam("orgId") Integer orgId,@PathParam("unitId")Integer unitId) {
		try {
			return iConsentMasterService.getActiveConsentList(orgId,unitId);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getConsentCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getConsentCount(@RequestBody ConsentMasterDto consentMasterDto) {
		try {

			return iConsentMasterService.getConsentCount(consentMasterDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getConsentListByConsentTypeId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getConsentListByConsentTypeId(@RequestBody ConsentMasterDto consentMasterDto) {
		try {

			return iConsentMasterService.getConsentListByConsentTypeId(consentMasterDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	 @RequestMapping(value = "/uploadConsentDoc", headers=("content-type=multipart/*"), method = RequestMethod.POST)
     public @ResponseBody Response saveDocumentAgainstEncounter(
    		 @RequestParam("consentMasterId") Integer consentMasterId,
    		 @RequestParam("documentName") String documentName,
    		 @RequestParam("organizationId") Integer organizationId,
    		 @RequestParam("unitId") Integer unitId,
    		 @RequestParam("file") MultipartFile file
    		 )
         {
	      try
	      {
	    	  ConsentMasterDto consentMasterDto = new ConsentMasterDto();
	    	  consentMasterDto.setConsentMasterId(consentMasterId);
	    	  consentMasterDto.setDocumentName(documentName);
	    	  consentMasterDto.setOrganizationId(organizationId);
	    	  consentMasterDto.setUnitId(unitId);
	    	  consentMasterDto.setFile(file);
	         return iConsentMasterService.saveDocumentConsentMaster(consentMasterDto);
	       }catch(Exception e)
	      {
	           
	      }
	      return new Response<>(ERROR, null, null, null, null);
       }
	 
	 
	 
	 /**@Code Added by Ganesh */
		@RequestMapping(value = "/patientConsent", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response savePatientConsent(@RequestBody List<PatientConsentMapperDto> list) {
			try {

				return iConsentMasterService.savePatientConsent(list);
			} catch (Exception e) {
				
			}
			return new Response(ERROR, null, null, null, null);
		}

		@RequestMapping(value = "/consentTemplate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response getPatientConsent(@RequestBody PatientConsentMapperDto dto) {
			try {
				return iConsentMasterService.getPatientConsent(dto);
			} catch (Exception e) {
				
			}
			return new Response(ERROR, null, null, null, null);
		}
		
		
		 @RequestMapping(value = "/uploadpatientConsentDoc", headers=("content-type=multipart/*"), method = RequestMethod.POST)
	     public @ResponseBody Response uploadpatientConsentDoc(
	    		 @RequestParam("consentTransactionId") Integer consentTransactionId,
	    		 @RequestParam("documentName") String documentName,
	    		 @RequestParam("documentTypeId") Integer documentTypeId,
	    		 @RequestParam("createdBy") Integer createdBy,
	    		 @RequestParam("updatedBy") Integer updatedBy,
	    		 @RequestParam("unitId") Integer unitId,
	    		 @RequestParam("organizationId") Integer organizationId,
	    		 @RequestParam("file") MultipartFile file
	    		 )
	         {
		      try
		      {
		    	  PatientConsentDocumentDetailsDto dto = new PatientConsentDocumentDetailsDto();
		    	  dto.setConsentTransactionId(consentTransactionId);
		    	  dto.setDocumentName(documentName);
		    	  dto.setDocumentTypeId(documentTypeId);
		    	  dto.setCreatedBy(createdBy);
		    	  dto.setUpdatedBy(updatedBy);
		    	  dto.setUnitId(unitId);
		    	  dto.setOrganizationId(organizationId);
		    	  dto.setFile(file);
		         return iConsentMasterService.uploadpatientConsentDoc(dto);
		       }catch(Exception e)
		      {
		           
		      }
		      return new Response<>(ERROR, null, null, null, null);
	       }
		 
		 
		 @RequestMapping(value = "/uploadpatientConsentVideo", headers=("content-type=multipart/*"), method = RequestMethod.POST)
	     public @ResponseBody Response uploadpatientConsentVideo(
	    		 @RequestParam("consentTransactionId") Integer consentTransactionId,
	    		 @RequestParam("organizationId") Integer organizationId,
	    		 @RequestParam("videoName") String videoName,
	    		 @RequestParam("createdBy") Integer createdBy,
	    		 @RequestParam("updatedBy") Integer updatedBy,
	    		 @RequestParam("unitId") Integer unitId,
	    		 @RequestParam("file") MultipartFile file
	    		 )
	         {
		      try
		      {
		    	  PatientConsentVideoDetailsDto dto = new PatientConsentVideoDetailsDto();
		    	  
		    	  dto.setConsentTransactionId(consentTransactionId);
		    	  dto.setVideoName(videoName);
		    	  dto.setCreatedBy(createdBy);
		    	  dto.setUpdatedBy(updatedBy);
		    	  dto.setUnitId(unitId);
		    	  dto.setOrganizationId(organizationId);
		    	  dto.setConsentTransactionId(consentTransactionId);
		    	  dto.setOrganizationId(organizationId);
		    	  dto.setFile(file);
		         return iConsentMasterService.uploadpatientConsentVideo(dto);
		       }catch(Exception e)
		      {
		           
		      }
		      return new Response<>(ERROR, null, null, null, null);
	       }
		
		 
		 @RequestMapping(value = "/getConsentListByAdmissionAndEncounterId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
			public @ResponseBody Response getConsentListByAdmissionAndEncounterId(@RequestBody PatientConsentMapperDto PatientConsentMapperDto) {
				try {

					return iConsentMasterService.getConsentListByAdmissionAndEncounterId(PatientConsentMapperDto);
				} catch (Exception e) {
					
				}
				return new Response(ERROR, null, null, null, null);
			}
		 
		 @RequestMapping(value = "/cancelConsentPrint", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
			public  @ResponseBody Response cancelConsentPrint( @RequestBody PatientConsentMapperDto dto) {
				try {
					Integer consentTransactionId = dto.getConsentTransactionId();
					return iConsentMasterService.cancelConsentPrint(consentTransactionId);
				} catch (Exception e) {
					
				}
				return new Response(ERROR, null, null, null, null);
			}
		 
		 @RequestMapping(value = "/patientConsentPrintReport", method = RequestMethod.POST)
			public Response getPatientConsentPrintPdfFile(HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws IOException {
				try {
					PatientConsentMapperDto dto=new PatientConsentMapperDto();
					
					dto.setConsentTransactionId(Integer.parseInt(servletRequest.getParameter("consentTransactionId")));
					dto.setAdmissionId(Integer.parseInt(servletRequest.getParameter("admissionId")));
					dto.setEncounterId(Integer.parseInt(servletRequest.getParameter("encounterId")));
					
					ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
					byteStream=iConsentMasterService.getPatientConsentPrintPdfFile(dto);
					OutputStream output = null;
					 servletResponse.setContentType("application/pdf");
					output = servletResponse.getOutputStream();
				       output.write(byteStream.toByteArray());
				       output.close();
				       byteStream.close();
				    return null;
					}catch(Exception e){
						
					}
				return null;
			}
		 
		 @RequestMapping(value = "/getConsentDocsListByMapperId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
			public @ResponseBody Response getConsentDocsListByMapperId(@RequestBody PatientConsentMapperDto PatientConsentMapperDto) {
				try {
					return iConsentMasterService.getConsentDocsListByMapperId(PatientConsentMapperDto);
				} catch (Exception e) {
					
				}
				return new Response(ERROR, null, null, null, null);
			}
		 
		 @RequestMapping(value = "/getConsentVideoListByMapperId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
			public @ResponseBody Response getConsentVideoListByMapperId(@RequestBody PatientConsentMapperDto PatientConsentMapperDto) {
				try {
					return iConsentMasterService.getConsentVideoListByMapperId(PatientConsentMapperDto);
				} catch (Exception e) {
					
				}
				return new Response(ERROR, null, null, null, null);
			}
		 
		 @RequestMapping(value = "/deactiveConsentVideo", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
			public @ResponseBody Response deactiveConsentVideo(@RequestBody PatientConsentVideoDetails dto) {
				try {
					return iConsentMasterService.deactiveConsentVideo(dto);
				} catch (Exception e) {
					
				}
				return new Response(ERROR, null, null, null, null);
			}
		 
		 @RequestMapping(value = "/deactiveConsentDocs", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
			public @ResponseBody Response deactiveConsentDocs(@RequestBody PatientConsentDocumentDetailsDto dto) {
				try {
					return iConsentMasterService.deactiveConsentDocs(dto);
				} catch (Exception e) {
					
				}
				return new Response(ERROR, null, null, null, null);
			}
		 
		 @RequestMapping(value = "/triggerConsentMail", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
			public @ResponseBody Response triggerConsentMail(@RequestBody PatientConsentMapperDto dto) {
				try {
					SmsUtilityConfigInfoDto smsInfo = new SmsUtilityConfigInfoDto();
					smsInfo.setUnitId(dto.getUnitId());
					smsInfo.setPatientId(dto.getPatientId());
					smsInfo.setOrganizationId(dto.getOrganizationId());
					smsInfo.setConsentTransactionId(dto.getConsentTransactionId());
					smsInfo.setConsentMasterName(dto.getConsentMasterName());
					smsInfo.setConsentTypeMasterName(dto.getConsentTypeMasterName());
					smsInfo.setTemplateType(dto.getTemplateType());
					smsInfo.setFormTemplateId(dto.getFormTemplateId());
					smsInfo.setDocumentName(dto.getDocumentName());
					smsInfo.setDocumentPath(dto.getDocumentPath());
					smsInfo.setEncounterId(dto.getEncounterId());
					smsInfo.setAdmissionId(dto.getAdmissionId());
					smsInfo.setAge(dto.getAge());
					smsInfo.setGender(dto.getGenderName());
					smsInfo.setAdmissionId(dto.getAdmissionId());
					smsInfo.setMobileNo(dto.getMobileNumber());
					Response res = iConsentMasterService.getConsentHtmlString(dto.getConsentTransactionId());
					
					if(res.getObject() != null) {
						PatientConsentMapperDto finalDto = (PatientConsentMapperDto) res.getObject();
						
						if(finalDto.getTemplateFormUrl() != null ) {
							smsInfo.setHtmlTemplateData(finalDto.getTemplateFormUrl());
						}
					}
					
					if(dto.getVisitTypeId() == OP) {
						smsInfo.setEncounterNo(dto.getVisitNumber());
						
					}else {
						smsInfo.setAdmissionNo(dto.getVisitNumber());
					}
					//smsInfo.setVisitNumber(dto.getVisitNumber());
					smsUtilityService.getEmailUtilityByTriggerId(ISmsUtilityConstants.CONSENT_PRINT, smsInfo);
					
					return new Response<>(SUCCESS, SUCCESS_CODE, "Send Mail", null, null);
				} catch (Exception e) {
					
				}
				return new Response(ERROR, null, null, null, null);
			}
		 
		 
	/*	 @RequestMapping(value = "/triggerConsentMail", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
			public @ResponseBody Response addSample(@RequestBody String sampleMasterDto) {
				
				
				Response<PatientConsentMapperDto, Integer> response = null;
				try {
					
					JsonCheck.mapJsonToObject(sampleMasterDto, PatientConsentMapperDto.class);
					
					//  response = iSampleMasterService.addSampleMaster(sampleMasterDto); return
					 // response;
					 
					return null;
				} catch (Exception e) {
					
					return new Response(ERROR, null, null, null, null);
				}
			}
		 
		 */
		 
		 
		 @RequestMapping(value = "/getConsentHtmlString", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
			public @ResponseBody Response getConsentHtmlString(@RequestBody PatientConsentDocumentDetailsDto dto) {
				try {
					return iConsentMasterService.getConsentHtmlString(dto.getConsentTransactionId());
				} catch (Exception e) {
					
				}
				return new Response(ERROR, null, null, null, null);
			}
		 
		 
		 @RequestMapping(value = "/replaceConsentAnotations", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
			public @ResponseBody Response replaceConsentAnotations(@RequestBody PatientConsentMapperDto dto) {
				try {
					return iConsentMasterService.replaceConsentAnotations(dto);
				} catch (Exception e) {
					
				}
				return new Response(ERROR, null, null, null, null);
			}
}
