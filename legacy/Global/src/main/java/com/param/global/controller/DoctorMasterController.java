package com.param.global.controller;

import javax.servlet.http.HttpServletRequest;

import io.minio.errors.*;
import in.param.exception.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import org.apache.log4j.Logger;

import com.param.assetManagement.maintenance.dto.VendorDto;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.ISmsUtilityConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.common.ServletUtils;
import com.param.global.dto.DoctorDocumentMapperDto;
import com.param.global.dto.DoctorMasterDto;
import com.param.global.dto.DoctorSpecialityMapperDto;
import com.param.global.dto.UserMasterDto;
import com.param.global.org.dto.GeneralLedgerMasterDto;
import com.param.global.service.IDoctorMasterService;
import com.param.global.unit.dto.SmsUtilityConfigInfoDto;
import com.param.global.unit.dto.UserIndetificationDetailsDto;
import com.param.global.unit.dto.UserRoleMasterDto;
import com.param.global.unit.service.ISmsUtilityConfigService;
import com.param.utility.TimeZoneComponent;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/*FUNCTIONALITY : DOCTOR MASTER
DATE:09/04/2018
CREATED BY: JYOTI*/
@RestController
@RequestMapping("api/global")
@SuppressWarnings({ "rawtypes", "unchecked" ,"unused"})
public class DoctorMasterController implements ICommonConstants{

	private static final Logger logger = Logger.getLogger(DoctorMasterController.class);

	@Autowired
	private IDoctorMasterService iDoctorMasterService;
	
	
	@Autowired
	private ISmsUtilityConfigService smsUtilityService;
	
	@RequestMapping(value="/getDoctorMasterListAutoFillSerach", method=RequestMethod.GET)
	public @ResponseBody Response listOfSurgeryMasterList(@RequestHeader(value="unit_id") int unitId, @RequestHeader(value="organization_id") int organizationId, 
			@RequestParam(value="doctor_name") String doctorName) {
		try
		{
			DoctorMasterDto doctorMasterDto= new DoctorMasterDto();
			doctorMasterDto.setUnitId(unitId);
			doctorMasterDto.setOrganizationId(organizationId);
			doctorMasterDto.setDoctorName(doctorName);
			return iDoctorMasterService.getDoctorMasterList(doctorMasterDto);
		}catch(Exception e)
		{
			logger.error("Error while fetching the doctor master list", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/doctorRegistration", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public synchronized @ResponseBody Response saveDoctorRegistration(@RequestBody DoctorMasterDto doctorMasterDto,HttpServletRequest request) {
		try {
			Response res = iDoctorMasterService.saveDoctorRegistration(doctorMasterDto);
			if(res.getStatus().equals(SUCCESS) && res.getResObject()!=null) {
					UserMasterDto userMasterDto = (UserMasterDto)res.getResObject();
					SmsUtilityConfigInfoDto emailinfo = new SmsUtilityConfigInfoDto();
					emailinfo.setUnitId(doctorMasterDto.getUnitId());
					emailinfo.setOrganizationId(doctorMasterDto.getOrganizationId());
					emailinfo.setAdmitingDoctorId(userMasterDto.getDoctorId());
					emailinfo.setPassword(userMasterDto.getPassword());
					emailinfo.setUserName(userMasterDto.getUserName());
					emailinfo.setApplicationLink(ServletUtils.getBaseUrl(request)+"/#/login");
					smsUtilityService.getEmailUtilityByTriggerId(ISmsUtilityConstants.DOCTOR_REGISTRATION, emailinfo);
			}
			return res;
		}catch (Exception e) {
			logger.error("Error while saving the doctor registration", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}





	@RequestMapping(value="/doctorDocumets", method=RequestMethod.POST, headers=("content-type=multipart/*"))
	public synchronized @ResponseBody Response saveDoctorDocuments(
	   		 @RequestParam("degreeId") Integer degreeId,
	   		 @RequestParam("qualificationId") Integer qualificationId,
	   		 @RequestParam("qualificationDesc") String qualificationDesc,
	   		 @RequestParam("registrationNumber") String registrationNumber,
	   		 @RequestParam("createdBy") Integer createdBy,
	   		 @RequestParam("createdDate") String createdDate,
	   		 @RequestParam("updatedDate") String updatedDate,
   		 @RequestParam("updatedBy") Integer updatedBy,
   		 @RequestParam("unitId") Integer unitId,
   		 @RequestParam("organizationId") Integer organizationId,
   		 @RequestParam("doctorId") Integer doctorId,
   		 @RequestParam("fileName") String fileName,
   		 @RequestParam("file") MultipartFile file)
	{
		try {
			
			
			return iDoctorMasterService.saveDoctorDocuments(degreeId,qualificationId,qualificationDesc,registrationNumber,createdBy,createdDate,
					updatedDate,updatedBy,unitId,organizationId,doctorId,fileName,file);
		}
		catch (Exception e) {
			logger.error("Error while saving the doctor documents", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/getDoctorDetails", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getDoctorDetails(@RequestBody DoctorMasterDto doctorMasterDto) {
		try {
			return iDoctorMasterService.getDoctorDetails(doctorMasterDto);
		}catch (Exception e) {
			logger.error("Error while fetching the doctor details", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/updateDoctorDetails", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public synchronized @ResponseBody Response updateDoctorDetails(@RequestBody DoctorMasterDto doctorMasterDto) {
		try {
			Integer globalDocId=iDoctorMasterService.getGlobalDocId(doctorMasterDto.getDoctorId());
			Response docUpdateRes=iDoctorMasterService.updateDoctorDetails(doctorMasterDto);
			if(globalDocId!=null) {
				Response globalDocIdUpdate=iDoctorMasterService.updateGlobalDocId(doctorMasterDto.getDoctorId(),globalDocId);
			}
			return docUpdateRes;
		}catch (Exception e) {
			logger.error("Error while updating the doctor details", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/updateDoctorStatus", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateDoctorStatus(@RequestBody DoctorMasterDto doctorMasterDto) {
		try {
			return iDoctorMasterService.updateDoctorStatus(doctorMasterDto);
		}catch (Exception e) {
			logger.error("Error while updating the doctor status", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}


	@RequestMapping(value="/searchDoctorDetailsByCriteria", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response searchDoctorDetailsByCriteria(@RequestBody DoctorMasterDto doctorMasterDto) {
		try {
			return iDoctorMasterService.searchDoctorDetailsByCriteria(doctorMasterDto);
		}catch (Exception e) {
			logger.error("Error while searching the doctor details by criteria", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/searchPlainDoctorDetailsByCriteria", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response searchPlainDoctorDetailsByCriteria(@RequestBody DoctorMasterDto doctorMasterDto) {
		try {
			return iDoctorMasterService.searchPlainDoctorDetailsByCriteria(doctorMasterDto);
		}catch (Exception e) {
			logger.error("Error while searching the plain doctor details by criteria", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	 @RequestMapping(value = "/doctor/image/{orgId}/{unitId}/{doctorId}", headers=("content-type=multipart/*"), method = RequestMethod.POST)
     public @ResponseBody Response saveDoctorImage(@PathVariable("orgId") int orgId,
                           @PathVariable("unitId") int unitId,
                           @PathVariable("doctorId") int doctorId,
                           @RequestParam("file") MultipartFile inputFile)
         {
	      try
	      {
	         return iDoctorMasterService.saveDoctorImage(orgId,unitId,doctorId,inputFile);
	       }catch(Exception e)
	      {
	           logger.error("Error while saving the doctor image", e);
	      }
	      return new Response<>(ERROR, null, null, null, null);
       }
	
	 @RequestMapping(value="/getDoctorNameById", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response getDoctorNameById(@RequestBody DoctorMasterDto doctorMasterDto) {
			try {
				return iDoctorMasterService.getDoctorNameById(doctorMasterDto);
			}catch (Exception e) {
				logger.error("Error while fetching the doctor name by id", e);
			}
			return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
		}
	 
	 @RequestMapping(value="/doctor/active", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response getActiveDocList(@RequestBody DoctorMasterDto doctorMasterDto) {
			try {
				return iDoctorMasterService.getActiveDocList(doctorMasterDto);
			}catch (Exception e) {
				logger.error("Error while fetching the active doctor list", e);
			}
			return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
		}
	 
	 @RequestMapping(value="/doctor/active/plain", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response getPlainDocList(@RequestBody DoctorMasterDto doctorMasterDto) {
			try {
				return iDoctorMasterService.getPlainDocList(doctorMasterDto);
			}catch (Exception e) {
				logger.error("Error while fetching the plain doctor list", e);
			}
			return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
		}
	 
	 
	 @RequestMapping(value="/getListOfDoctorDegreeDoccument", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
		public synchronized @ResponseBody Response getListOfDoctorDegreeDoccument(@RequestBody DoctorDocumentMapperDto doctorDocumentMapperDto) {
			try {
				return iDoctorMasterService.getListOfDoctorDegreeDoccument(doctorDocumentMapperDto);
			}catch (Exception e) {
				logger.error("Error while fetching the list of doctor degree documents", e);
			}
			return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
		}
	 
	 @RequestMapping(value="/getRMODoctorList", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
		public  @ResponseBody Response getRMODoctorList(@RequestBody DoctorMasterDto doctorMasterDto) {
			try {
				return iDoctorMasterService.getRMODoctorList(doctorMasterDto);
			}catch (Exception e) {
				logger.error("Error while fetching the RMO doctor list", e);
			}
			return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
		}
	 
	 @RequestMapping(value="/checkPinAvailability", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
		public  @ResponseBody Response validateDoctorPin(@RequestBody DoctorMasterDto doctorMasterDto) {
			try {
				return iDoctorMasterService.validateDoctorPin(doctorMasterDto);
			}catch (Exception e) {
				logger.error("Error while validating the doctor pin", e);
			}
			return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
		}
	 
	 @RequestMapping(value="/validateDoctorPinByDoctorId", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
		public  @ResponseBody Response validateDoctorPinByDoctorId(@RequestBody DoctorMasterDto doctorMasterDto) {
			try {
				return iDoctorMasterService.validateDoctorPinByDoctorId(doctorMasterDto);
			}catch (Exception e) {
				logger.error("Error while validating the doctor pin by doctor id", e);
			}
			return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
		}
	 
	 @RequestMapping(value="/appoitments/doctors",  method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
		public  @ResponseBody Response getDocotorListForAppoitments(@RequestBody DoctorSpecialityMapperDto doctorSpecialityMapperDto) {
			try {
				return iDoctorMasterService.getDocotorListForAppoitments(doctorSpecialityMapperDto.getUnitId(),doctorSpecialityMapperDto.getSpecialityId());
			}catch (Exception e) {
				logger.error("Error while fetching the doctor list for appointments", e);
			}
			return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
		}
	 
	 
		@RequestMapping(value="/doctor/count" ,  method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response getDoctorCount(@RequestBody DoctorMasterDto DoctorMasterDto ){
			try{
				return iDoctorMasterService.getDoctorCount(DoctorMasterDto);
			}catch(Exception e){
				logger.error("Error while fetching the doctor count", e);
			}
			return new Response<>(ERROR, null, null, null, null);
		}
	 
		@RequestMapping(value="/getStaffListByRoleId", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
		public  @ResponseBody Response getStaffListByRoleId(@RequestBody UserRoleMasterDto dto) {
			try {
				return iDoctorMasterService.getStaffListByRoleId(dto);
			}catch (Exception e) {
				logger.error("Error while fetching the staff list by role id", e);
			}
			return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
		}
		
		@RequestMapping(value="/doctorDetailsUniversalSearch", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response doctorDetailsUniversalSearch(@RequestBody DoctorMasterDto doctorMasterDto) {
			try {
				return iDoctorMasterService.doctorDetailsUniversalSearch(doctorMasterDto);
			}catch (Exception e) {
				logger.error("Error while performing the doctor details universal search", e);
			}
			return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
		}
		
		
		//get list of doctor whose slots are available
		@RequestMapping(value = "/getDoctorListBySpecialityId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response getDoctorListBySpecialityId(
				@RequestBody DoctorSpecialityMapperDto doctorSpecialityMapper) {
			try {
				return iDoctorMasterService.getDoctorListBySpecialityId(doctorSpecialityMapper);
			} catch (Exception e) {
				logger.error("Error while fetching the doctor list by speciality id", e);
			}
			return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
		}

        @RequestMapping(value="/storeDoctorEqucationDetails", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
        public @ResponseBody Response storeDoctorEqucationDetails(@RequestBody DoctorMasterDto doctorMasterDto) {
            try {
                return iDoctorMasterService.storeDoctorEqucationDetails(doctorMasterDto);
            }catch (Exception e) {
                logger.error("Error while storing the doctor education details", e);
            }
            return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
        }

        @RequestMapping(value="/updateDoctorEqucationDetails", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
        public @ResponseBody Response updateDoctorEqucationDetails(@RequestBody DoctorDocumentMapperDto doctorDocumentMapperDto) {
            try {
                return iDoctorMasterService.updateDoctorEqucationDetails(doctorDocumentMapperDto);
            }catch (Exception e) {
                logger.error("Error while updating the doctor education details", e);
            }
            return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
        }
        

		
		 @RequestMapping(value="/doctor/activeWithSpeciality", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
			public @ResponseBody Response getActiveDocListWithSpeciality(@RequestBody DoctorMasterDto doctorMasterDto) {
				try {
					return iDoctorMasterService.getActiveDocListWithSpeciality(doctorMasterDto);
				}catch (Exception e) {
					logger.error("Error while fetching the active doctor list with speciality", e);
				}
				return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
			}
		 
		 @RequestMapping(value="/checkDoctorRegNoAvailability", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
			public  @ResponseBody Response checkDoctorRegNoAvailability(@RequestBody DoctorMasterDto doctorMasterDto) {
				try {
					return iDoctorMasterService.checkDoctorRegNoAvailability(doctorMasterDto);
				}catch (Exception e) {
					logger.error("Error while checking the doctor registration number availability", e);
				}
				return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
			}
		 
		 @RequestMapping(value = "/getDoctorListForSwapDoctor", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
			public @ResponseBody Response getDoctorListForSwapDoctor(
					@RequestBody DoctorMasterDto doctorMasterDto) {
				try {
					return iDoctorMasterService.getDoctorListForSwapDoctor(doctorMasterDto);
				} catch (Exception e) {
					logger.error("Error while fetching the doctor list for swap doctor", e);
				}
				return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
			}
		 
		 @RequestMapping(value="/webappointment/getDoctors",  method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
			public  @ResponseBody Response getDoctorsForWebAppointment(@RequestBody DoctorSpecialityMapperDto doctorSpecialityMapperDto) {
				try {
					return iDoctorMasterService.getDoctorsForWebAppointment(doctorSpecialityMapperDto.getUnitId(),
							doctorSpecialityMapperDto.getSpecialityId(),
							doctorSpecialityMapperDto.getFlag());
				}catch (Exception e) {
					logger.error("Error while fetching the doctors for web appointment", e);
				}
				return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
			}
		 
		 @RequestMapping(value="/getDoctorRegistrationNumber", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
			public @ResponseBody Response getDoctorRegistrationNumber(@RequestBody DoctorMasterDto doctorMasterDto) {
				try {
					return iDoctorMasterService.getDoctorRegistrationNumber(doctorMasterDto);
				}catch (Exception e) {
					logger.error("Error while fetching the doctor registration number", e);
				}
				return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
			}
		 @RequestMapping(value="/getDoctorMasterListAutoFillSerach/doctorList", method=RequestMethod.GET)
			public @ResponseBody Response listOfSurgeryMasterList1(@RequestHeader(value="unit_id") int unitId, @RequestHeader(value="organization_id") int organizationId, 
					@RequestParam(value="doctor_name") String doctorName) {
				try
				{
					DoctorMasterDto doctorMasterDto= new DoctorMasterDto();
					doctorMasterDto.setUnitId(unitId);
					doctorMasterDto.setOrganizationId(organizationId);
					doctorMasterDto.setDoctorName(doctorName);
					return iDoctorMasterService.getDoctorMasterList1(doctorMasterDto);
				}catch(Exception e)
				{
					logger.error("Error while fetching the doctor master list", e);
				}
				return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}
		 
		 
		 @RequestMapping(value="/doctorIdentificationDocumets", method=RequestMethod.POST, headers=("content-type=multipart/*"))
			public synchronized @ResponseBody Response doctorIdentificationDocumets(
			   		 @RequestParam("identificationTypeId") Integer identificationTypeId,
			   		 @RequestParam("identificationNumber") String identificationNumber,
//			   		 @RequestParam("employeeId") Integer employeeId,
			   		 @RequestParam(value="employeeId", required = false) Integer employeeId,
			   		 @RequestParam("userId") Integer userId,
			   		 @RequestParam("datepickerExpiry") String datepickerExpiry,
			   		 @RequestParam("createdBy") Integer createdBy,
			   		 @RequestParam("createdDate") String createdDate,
			   		 @RequestParam("updatedDate") String updatedDate,
			   		 @RequestParam("updatedBy") Integer updatedBy,
			   		 @RequestParam("unitId") Integer unitId,
			   		 @RequestParam("organizationId") Integer organizationId,
			   		 @RequestParam("doctorId") Integer doctorId,
					 @RequestParam(value="fileName", required = false) String fileName,
					 @RequestParam(value="retrieveFileName", required = false) String retrieveFileName,
					 @RequestParam(value="doctorIndetificationFile", required = false) String doctorIndetificationFile,
			   		 @RequestParam(value="userIndetificationDetailsId", required = false) Integer userIndetificationDetailsId,
			   		 @RequestParam(value="file", required = false) MultipartFile file) throws ServerException, ApplicationException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
				try {
					
//					if(fileName.equals("undefined")){
//						fileName = null;
//					}
					return iDoctorMasterService.doctorIdentificationDocumets(identificationTypeId,identificationNumber,employeeId,userId,datepickerExpiry,createdBy,createdDate,
							updatedDate,updatedBy,unitId,organizationId,doctorId,fileName,retrieveFileName,doctorIndetificationFile,userIndetificationDetailsId,file);
				}
				catch (Exception e) {
					throw e;
				}
//				return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
			}
			
		 @RequestMapping(value="/getListOfDoctorIdentificationDocuumentList", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
			public synchronized @ResponseBody Response getListOfDoctorIdentificationDocuumentList(@RequestBody UserIndetificationDetailsDto userIndetificationDetailsDto) {
				try {
					return iDoctorMasterService.getListOfDoctorIdentificationDocuumentList(userIndetificationDetailsDto);
				}catch (Exception e) {
					logger.error("Error while fetching the list of doctor identification documents", e);
				}
				return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
			}
		 
		 @RequestMapping(value="/updateDoctorIdentificationDetails", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	        public @ResponseBody Response updateDoctorIdentificationDetails(@RequestBody UserIndetificationDetailsDto userIndetificationDetailsDto) {
	            try {
	                return iDoctorMasterService.updateDoctorIdentificationDetails(userIndetificationDetailsDto);
	            }catch (Exception e) {
	                logger.error("Error while updating the doctor identification details", e);
	            }
	            return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	        }
	        

			
		//get list of doctor whose slots are available
			@RequestMapping(value = "/getDoctorListByOrgId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
			public @ResponseBody Response getDoctorListByOrgId(@RequestBody DoctorSpecialityMapperDto doctorSpecialityMapper) {
				try {
					return iDoctorMasterService.getDoctorListByOrgId(doctorSpecialityMapper);
				} catch (Exception e) {
					logger.error("Error while fetching the doctor list by org id", e);
				}
				return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
			}
			
			
			@RequestMapping(value="/doctorRegistrationWithGLId", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
			public synchronized @ResponseBody Response doctorRegistrationWithGLId(@RequestBody DoctorMasterDto doctorMasterDto,HttpServletRequest request) {
				try {
					
					 GeneralLedgerMasterDto generalLedgerMasterDto = new GeneralLedgerMasterDto();
					 String GeneralLedgerName =null;
					 Response res = null;
					 GeneralLedgerName = doctorMasterDto.getFirstName()+" "+doctorMasterDto.getMiddleName()+" "+doctorMasterDto.getLastName();
					 generalLedgerMasterDto.setGeneralLedgerName(GeneralLedgerName);
					 generalLedgerMasterDto.setGeneralLedgerCode(GeneralLedgerName);
					 generalLedgerMasterDto.setCreatedBy(doctorMasterDto.getCreatedBy());
					 generalLedgerMasterDto.setCreatedDate(doctorMasterDto.getCreatedDate());
					 generalLedgerMasterDto.setUpdatedBy(doctorMasterDto.getUpdatedBy());
					 generalLedgerMasterDto.setUpdatedDate(doctorMasterDto.getUpdatedDate());
					 generalLedgerMasterDto.setStatus('A');
					 generalLedgerMasterDto.setOrganizationId(doctorMasterDto.getOrganizationId());
					 
					 Response resLedger = iDoctorMasterService.doctorRegistrationWithGLId(generalLedgerMasterDto);
					 if(resLedger != null && resLedger.getObject() != null) {
						  res = iDoctorMasterService.saveDoctorRegistration(doctorMasterDto);
						  if(res != null && res.getResObject() != null) {
							  Integer doctorId = null;
							  UserMasterDto userMasterDto =   (UserMasterDto) res.getResObject();
							  GeneralLedgerMasterDto generalLedgerMasterDtos =   (GeneralLedgerMasterDto) resLedger.getObject();
							  doctorId =userMasterDto.getDoctorId();
							  //update doctor master
							  doctorMasterDto.setDoctorId(doctorId);
							  doctorMasterDto.setGeneralLedgerId(generalLedgerMasterDtos.getGeneralLedgerId());
							 iDoctorMasterService.updateDoctorMasterLedgerId(doctorMasterDto);
									
						  }
					 }
					return res;
				}catch (Exception e) {
					logger.error("Error while registering the doctor with general ledger id", e);
				}
				return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
			}

	@RequestMapping(value="/getDoctorDetailsEncrypted", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public  @ResponseBody Response getDoctorDetailsEncrypted(@RequestBody DoctorMasterDto doctorMasterDto) {
		try {
			return iDoctorMasterService.getDoctorDetailsEncrypted(doctorMasterDto);
		}catch (Exception e) {
			logger.error("Error while fetching the encrypted doctor details", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

    @RequestMapping(value="/appointments/doctorsForWebUser",  method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
    public  @ResponseBody Response getDoctorListForAppointmentsWebUser(@RequestBody DoctorSpecialityMapperDto doctorSpecialityMapperDto) {
        try {
            return iDoctorMasterService.getDoctorListForAppointmentsWebUser(doctorSpecialityMapperDto.getUnitId(),doctorSpecialityMapperDto.getSpecialityId());
        }catch (Exception e) {
            logger.error("Error while fetching the doctor list for appointments web user", e);
        }
        return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
    }

	@RequestMapping(value="/getAvailableDoctorBySpeciality",  method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public  @ResponseBody Response getAvailableDoctorBySpeciality(@RequestBody DoctorSpecialityMapperDto doctorSpecialityMapperDto) {
		try {
			return iDoctorMasterService.getAvailableDoctorBySpeciality(doctorSpecialityMapperDto);
		}catch (Exception e) {
			logger.error("Error while fetching the available doctor by speciality", e);
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
			
			
}
