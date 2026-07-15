package com.param.ER.controller;

import com.param.global.dto.PrefixMasterDto;
import com.param.global.org.common.dto.PriorityMasterDto;
import com.param.global.utility.ErrorCorrelationUtil;
import com.param.global.utility.UserComponent;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.AdmissionDto;
import com.param.global.dto.PatientSearchDto;
import com.param.global.dto.PatientSponsorDetailsDto;
import com.param.global.service.IUnknownPatientRegistrationService;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/adt")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class UnknownPatientRegistrationController implements ICommonConstants {

	final static Logger logger = Logger.getLogger(UnknownPatientRegistrationController.class);

	@Autowired
	IUnknownPatientRegistrationService iUnknownPatientRegistrationService;

	@RequestMapping(value = "/saveUnknownPatient", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveUnknownPatient(@RequestBody AdmissionDto unAdmissionDto) {
		try {
		
			return iUnknownPatientRegistrationService.saveUnknownPatient(unAdmissionDto);
			
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/getActivePriorityList", method = RequestMethod.GET)
	public @ResponseBody Response getPriorityMasterList() {
		try {

			return iUnknownPatientRegistrationService.getPriorityMasterList();

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/patientSearchByMultipleCriteria", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response patientSearchByMultipleCriteria(@RequestBody PatientSearchDto patientSearchDto) {
		try {
			if((patientSearchDto.getUhIdNumber() =="" || patientSearchDto.getUhIdNumber() == null) && (patientSearchDto.getPatientName() =="" || patientSearchDto.getPatientName() == null)
				&& patientSearchDto.getGenderName() == null && patientSearchDto.getGenderId() == null && (patientSearchDto.getMobileNo() == "" || patientSearchDto.getMobileNo()==null)
					&& (patientSearchDto.getDob() == null  && (patientSearchDto.getBirthDate() == "" || patientSearchDto.getBirthDate() == null)) && patientSearchDto.getIdentificationTypeId() == null
					&& (patientSearchDto.getIdentificationNo() == "" || patientSearchDto.getIdentificationNo() == null) &&(patientSearchDto.getWardId() == null)&&(patientSearchDto.getDoctorId()==null)) {
				 return new Response(ERROR, ALREADY_EXIST_CODE, "Please enter atleast one search criteria..!", null, null);
			
			}
			return iUnknownPatientRegistrationService.patientSearchByMultipleCriteria(patientSearchDto);

		} catch (Exception e) {
			
		}
		
		//return new Response(ERROR, null,"Please enter search criteria..!", null, null);

		
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/patientSearchByMultipleCriteriaGrowthChart", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response patientSearchByMultipleCriteriaGrowthChart(@RequestBody PatientSearchDto patientSearchDto) {
		try {
			if((patientSearchDto.getUhIdNumber() =="" || patientSearchDto.getUhIdNumber() == null) && (patientSearchDto.getPatientName() =="" || patientSearchDto.getPatientName() == null)
					&& patientSearchDto.getGenderName() == null && patientSearchDto.getGenderId() == null && (patientSearchDto.getMobileNo() == "" || patientSearchDto.getMobileNo()==null)
					&& (patientSearchDto.getDob() == null  && (patientSearchDto.getBirthDate() == "" || patientSearchDto.getBirthDate() == null)) && patientSearchDto.getIdentificationTypeId() == null
					&& (patientSearchDto.getIdentificationNo() == "" || patientSearchDto.getIdentificationNo() == null) &&(patientSearchDto.getWardId() == null)&&(patientSearchDto.getDoctorId()==null)) {
				return new Response(ERROR, ALREADY_EXIST_CODE, "Please enter atleast one search criteria..!", null, null);

			}
			return iUnknownPatientRegistrationService.patientSearchByMultipleCriteriaGrowthChart(patientSearchDto);

		} catch (Exception e) {
			String correlationId = ErrorCorrelationUtil.newCorrelationId();
			logger.error(ErrorCorrelationUtil.tag(correlationId, "patientSearchByMultipleCriteriaGrowthChart failed"), e);
			return new Response(ERROR, null, ErrorCorrelationUtil.clientMessage(correlationId), null, null);
		}
	}

	@RequestMapping(value = "/patientSearchByMultipleCriteriaWithDeathPatient", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response patientSearchByMultipleCriteriaWithDeathPatient(@RequestBody PatientSearchDto patientSearchDto) {
		try {
			if((patientSearchDto.getUhIdNumber() =="" || patientSearchDto.getUhIdNumber() == null) && (patientSearchDto.getPatientName() =="" || patientSearchDto.getPatientName() == null)
				&& patientSearchDto.getGenderName() == null && patientSearchDto.getGenderId() == null && (patientSearchDto.getMobileNo() == "" || patientSearchDto.getMobileNo()==null)
					&& (patientSearchDto.getDob() == null  && (patientSearchDto.getBirthDate() == "" || patientSearchDto.getBirthDate() == null)) && patientSearchDto.getIdentificationTypeId() == null
					&& (patientSearchDto.getIdentificationNo() == "" || patientSearchDto.getIdentificationNo() == null)) {
				 return new Response(ERROR, ALREADY_EXIST_CODE, "Please enter atleast one search criteria..!", null, null);
			
			}
			return iUnknownPatientRegistrationService.patientSearchByMultipleCriteriaWithDeathPatient(patientSearchDto);

		} catch (Exception e) {
			
		}
		
		//return new Response(ERROR, null,"Please enter search criteria..!", null, null);

		
		return new Response(ERROR, null, null, null, null);
	}
	
	
	@RequestMapping(value = "/patientSearchByMultipleCriteriaForWalkInSales", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response patientSearchByMultipleCriteriaForWalkInSales(@RequestBody PatientSearchDto patientSearchDto) {
		try {
			if((patientSearchDto.getUhIdNumber() =="" || patientSearchDto.getUhIdNumber() == null) && (patientSearchDto.getPatientName() =="" || patientSearchDto.getPatientName() == null)
				&& patientSearchDto.getGenderName() == null && patientSearchDto.getGenderId() == null && (patientSearchDto.getMobileNo() == "" || patientSearchDto.getMobileNo()==null)
					&& (patientSearchDto.getDob() == null  && (patientSearchDto.getBirthDate() == "" || patientSearchDto.getBirthDate() == null)) && patientSearchDto.getIdentificationTypeId() == null
					&& (patientSearchDto.getIdentificationNo() == "" || patientSearchDto.getIdentificationNo() == null)) {
				 return new Response(ERROR, ALREADY_EXIST_CODE, "Please enter atleast one search criteria..!", null, null);
			}
			patientSearchDto.setFromMrdScreen(true); // for walkin sales search screen 
			return iUnknownPatientRegistrationService.patientSearchByMultipleCriteriaForWalkInSales(patientSearchDto);

		} catch (Exception e) {
			
		}
		//return new Response(ERROR, null,"Please enter search criteria..!", null, null);
		return new Response(ERROR, null, null, null, null);
	}
	
	
	
	
	@RequestMapping(value = "/patientSearchByMultipleCriteriaForIPD", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response patientSearchByMultipleCriteriaForIPD(@RequestBody PatientSearchDto patientSearchDto) {
		try {
			if((patientSearchDto.getUhIdNumber() =="" || patientSearchDto.getUhIdNumber() == null) && (patientSearchDto.getPatientName() =="" || patientSearchDto.getPatientName() == null)
					&& patientSearchDto.getGenderName() == null && patientSearchDto.getGenderId() == null && (patientSearchDto.getMobileNo() == "" || patientSearchDto.getMobileNo()==null)
						&& (patientSearchDto.getDob() == null  && (patientSearchDto.getBirthDate() == "" || patientSearchDto.getBirthDate() == null)) && patientSearchDto.getIdentificationTypeId() == null
						&& (patientSearchDto.getIdentificationNo() == "" || patientSearchDto.getIdentificationNo() == null)) {
					 return new Response(ERROR, ALREADY_EXIST_CODE, "Please enter atleast one search criteria..!", null, null);



			}
			return iUnknownPatientRegistrationService.patientSearchByMultipleCriteriaForIPD(patientSearchDto);

		} catch (Exception e) {

		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getListOfEncounterIdByPatientId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getListOfEncounterIdByPatientId(@RequestBody PatientSearchDto patientSearchDto) {
		try {
			return iUnknownPatientRegistrationService.getListOfEncounterIdByPatientId(patientSearchDto);

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getListOfDocumentForBookView", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getListOfDocumentForBookView(@RequestBody PatientSearchDto patientSearchDto) {
		try {
			
			return iUnknownPatientRegistrationService.getListOfDocumentForBookView(patientSearchDto);

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/searchListOfPatientsByWardId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response searchListOfPatientsByWardId(@RequestBody PatientSearchDto patientSearchDto) {
		try {
			/*if(patientSearchDto.getUhIdNumber()=="" && patientSearchDto.getPatientName()=="" && 
					patientSearchDto.getGenderName()==null && patientSearchDto.getGenderId()==null 
					&& (patientSearchDto.getMobileNo()=="" || patientSearchDto.getMobileNo()==null)
					&& (patientSearchDto.getDob()==null && patientSearchDto.getBirthDate()=="") 
					&& patientSearchDto.getIdentificationTypeId()==null 
					&& patientSearchDto.getIdentificationNo()=="") {
				 return new Response(ERROR, ALREADY_EXIST_CODE, "Please enter atleast one search criteria..!", null, null);
			
			}*/
			return iUnknownPatientRegistrationService.searchListOfPatientsByWardId(patientSearchDto);

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/patientSearchByForTransferICU", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response patientSearchByForTransferICU(@RequestBody PatientSearchDto patientSearchDto) {
		try {
			/*if(patientSearchDto.getUhIdNumber()=="" && patientSearchDto.getPatientName()=="" && 
					patientSearchDto.getGenderName()==null && patientSearchDto.getGenderId()==null 
					&& (patientSearchDto.getMobileNo()=="" || patientSearchDto.getMobileNo()==null)
					&& (patientSearchDto.getDob()==null && patientSearchDto.getBirthDate()=="") 
					&& patientSearchDto.getIdentificationTypeId()==null 
					&& patientSearchDto.getIdentificationNo()=="") {
				 return new Response(ERROR, ALREADY_EXIST_CODE, "Please enter atleast one search criteria..!", null, null);
			
			}*/
			
			return iUnknownPatientRegistrationService.patientSearchByForTransferICU(patientSearchDto);

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/patientSearchByForOPBilling", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response patientSearchByForOPBilling(@RequestBody PatientSearchDto patientSearchDto) {
		try {
			if(patientSearchDto.getUhIdNumber()=="" && patientSearchDto.getPatientName()=="" && 
					patientSearchDto.getGenderName()==null && patientSearchDto.getGenderId()==null 
					&& (patientSearchDto.getMobileNo()=="" || patientSearchDto.getMobileNo()==null)
					&& (patientSearchDto.getDob()==null && patientSearchDto.getBirthDate()=="") 
					&& patientSearchDto.getIdentificationTypeId()==null 
					&& patientSearchDto.getIdentificationNo()=="") {
				 return new Response(ERROR, ALREADY_EXIST_CODE, "Please enter atleast one search criteria..!", null, null);
			
			}
			return iUnknownPatientRegistrationService.patientSearchByMultipleCriteriaForOPBilling(patientSearchDto);

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getMedicolegalDetailsForIcon", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getMedicolegalDetailsForIcon(@RequestBody AdmissionDto admissionDto) {
		try {
			
			return iUnknownPatientRegistrationService.getMedicolegalDetailsForIcon(admissionDto);

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/updateMedicolegalDetails", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateMedicolegalDetails(@RequestBody AdmissionDto admissionDto) {
		try {
			
			return iUnknownPatientRegistrationService.updateMedicolegalDetails(admissionDto);

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getPatientListMedicolegalDetailsMLCWorklist", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getPatientListMedicolegalDetailsMLCWorklist(@RequestBody AdmissionDto admissionDto) {
		try {
			
			return iUnknownPatientRegistrationService.getPatientListMedicolegalDetailsMLCWorklist(admissionDto);

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getMedicolegalDetailsMLCWorklistCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getMedicolegalDetailsMLCWorklistCount(@RequestBody AdmissionDto admissionDto){
		
		try {
			return iUnknownPatientRegistrationService.getMedicolegalDetailsMLCWorklistCount(admissionDto);
			
		} catch (Exception e) {
			
			return new Response<>(ERROR, null, null, null, null);
		}
	}
	
	
	@RequestMapping(value = "/searchListOfPatientsByDoctorIdForIOChart", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response searchListOfPatientsByDoctorIdForIOChart(@RequestBody PatientSearchDto patientSearchDto) {
		try {
			/*if(patientSearchDto.getUhIdNumber()=="" && patientSearchDto.getPatientName()=="" && 
					patientSearchDto.getGenderName()==null && patientSearchDto.getGenderId()==null 
					&& (patientSearchDto.getMobileNo()=="" || patientSearchDto.getMobileNo()==null)
					&& (patientSearchDto.getDob()==null && patientSearchDto.getBirthDate()=="") 
					&& patientSearchDto.getIdentificationTypeId()==null 
					&& patientSearchDto.getIdentificationNo()=="") {
				 return new Response(ERROR, ALREADY_EXIST_CODE, "Please enter atleast one search criteria..!", null, null);
			
			}*/
			return iUnknownPatientRegistrationService.searchListOfPatientsByDoctorIdForIOChart(patientSearchDto);

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/patientSearchByMultipleCriteria/global", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response patientSearchByMultipleCriteriaGlobal(@RequestBody PatientSearchDto patientSearchDto) {
		try {
			if(patientSearchDto.getVisitTypeId()==null) {
				 return new Response(ERROR, ALREADY_EXIST_CODE, "Please enter atleast one search criteria..!", null, null);
			
			}
			return iUnknownPatientRegistrationService.patientSearchByMultipleCriteriaGlobal(patientSearchDto);

		} catch (Exception e) {
			
		}
		
		//return new Response(ERROR, null,"Please enter search criteria..!", null, null);

		
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getIpRenderedDateChange", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getIpRenderedDateChange(@RequestBody PatientSearchDto patientSearchDto) {
		try {
			return iUnknownPatientRegistrationService.getIpRenderedDateChange(patientSearchDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@RequestMapping(value = "/patientSearchByMultipleCriteriaForICUAndER", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response patientSearchByMultipleCriteriaForICUAndER(@RequestBody PatientSearchDto patientSearchDto) {
		try {
			if((patientSearchDto.getUhIdNumber() =="" || patientSearchDto.getUhIdNumber() == null) && (patientSearchDto.getPatientName() =="" || patientSearchDto.getPatientName() == null)
					&& patientSearchDto.getGenderName() == null && patientSearchDto.getGenderId() == null && (patientSearchDto.getMobileNo() == "" || patientSearchDto.getMobileNo()==null)
					&& (patientSearchDto.getDob() == null  && (patientSearchDto.getBirthDate() == "" || patientSearchDto.getBirthDate() == null)) && patientSearchDto.getIdentificationTypeId() == null
					&& (patientSearchDto.getIdentificationNo() == "" || patientSearchDto.getIdentificationNo() == null)) {
				return new Response(ERROR, ALREADY_EXIST_CODE, "Please enter atleast one search criteria..!", null, null);
			}
			return iUnknownPatientRegistrationService.patientSearchByMultipleCriteriaForICUAndER(patientSearchDto);

		} catch (Exception e) {
			throw e;
		}
//		return new Response(ERROR, null, null, null, null);
	}
	@RequestMapping(value = "/patientSearchByMultipleCriteria/nabh", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response patientSearchByMultipleCriteriaNabh(@RequestBody PatientSearchDto patientSearchDto) {
		try {
			if((patientSearchDto.getUhIdNumber() =="" || patientSearchDto.getUhIdNumber() == null) && (patientSearchDto.getPatientName() =="" || patientSearchDto.getPatientName() == null)
				&& patientSearchDto.getGenderName() == null && patientSearchDto.getGenderId() == null && (patientSearchDto.getMobileNo() == "" || patientSearchDto.getMobileNo()==null)
					&& (patientSearchDto.getDob() == null  && (patientSearchDto.getBirthDate() == "" || patientSearchDto.getBirthDate() == null)) && patientSearchDto.getIdentificationTypeId() == null
					&& (patientSearchDto.getIdentificationNo() == "" || patientSearchDto.getIdentificationNo() == null) &&(patientSearchDto.getWardId() == null)&&(patientSearchDto.getDoctorId()==null)) {
				 return new Response(ERROR, ALREADY_EXIST_CODE, "Please enter atleast one search criteria..!", null, null);
			
			}
			return iUnknownPatientRegistrationService.patientSearchByMultipleCriteriaNabh(patientSearchDto);

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	

	@RequestMapping(value = "/patientSearchForIPDNutritionalAssessment", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response patientSearchForIPDNutritionalAssessment(@RequestBody PatientSearchDto patientSearchDto) {
		try {
			if((patientSearchDto.getUhIdNumber() =="" || patientSearchDto.getUhIdNumber() == null) && (patientSearchDto.getPatientName() =="" || patientSearchDto.getPatientName() == null)
					&& patientSearchDto.getGenderName() == null && patientSearchDto.getGenderId() == null && (patientSearchDto.getMobileNo() == "" || patientSearchDto.getMobileNo()==null)
						&& (patientSearchDto.getDob() == null  && (patientSearchDto.getBirthDate() == "" || patientSearchDto.getBirthDate() == null)) && patientSearchDto.getIdentificationTypeId() == null
						&& (patientSearchDto.getIdentificationNo() == "" || patientSearchDto.getIdentificationNo() == null)) {
					 return new Response(ERROR, ALREADY_EXIST_CODE, "Please enter atleast one search criteria..!", null, null);

			}
			return iUnknownPatientRegistrationService.patientSearchForIPDNutritionalAssessment(patientSearchDto);

		} catch (Exception e) {
			String errorMsg = UnknownPatientRegistrationController.class.getName()+" :"+e.getMessage();
			return new Response(ERROR, null, null, null, null);

		}
	}

	
	@RequestMapping(value = "/patientSearchByICUAndER", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response patientSearchByICUAndER(@RequestBody PatientSearchDto patientSearchDto) {
		try {
			if((patientSearchDto.getUhIdNumber() =="" || patientSearchDto.getUhIdNumber() == null) && (patientSearchDto.getPatientName() =="" || patientSearchDto.getPatientName() == null)
					&& patientSearchDto.getGenderName() == null && patientSearchDto.getGenderId() == null && (patientSearchDto.getMobileNo() == "" || patientSearchDto.getMobileNo()==null)
						&& (patientSearchDto.getDob() == null  && (patientSearchDto.getBirthDate() == "" || patientSearchDto.getBirthDate() == null)) && patientSearchDto.getIdentificationTypeId() == null
						&& (patientSearchDto.getIdentificationNo() == "" || patientSearchDto.getIdentificationNo() == null)) {
					 return new Response(ERROR, ALREADY_EXIST_CODE, "Please enter atleast one search criteria..!", null, null);



			}
			return iUnknownPatientRegistrationService.patientSearchByICUAndER(patientSearchDto);

		} catch (Exception e) {

		}
		return new Response(ERROR, null, null, null, null);
	}

    @RequestMapping(value = "/getAppointmentsByPatientId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Response getAppointmentsByPatientId(@RequestBody PatientSearchDto patientSearchDto) {
        try {

            return iUnknownPatientRegistrationService.getAppointmentsByPatientId(patientSearchDto);

        } catch (Exception e) {

        }
        return new Response(ERROR, null, null, null, null);
    }


	@RequestMapping(value = "/updateMLCFLag", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateMLCFLag(@RequestBody AdmissionDto admissionDto) {
		try {

			return iUnknownPatientRegistrationService.updateMLCFLag(admissionDto);

		} catch (Exception e) {

		}
		return new Response(ERROR, null, null, null, null);
	}

}