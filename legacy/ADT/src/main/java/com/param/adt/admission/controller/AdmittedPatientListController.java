package com.param.adt.admission.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.param.adt.admission.service.IAdmittedPatientListService;
import com.param.adt.common.ICommonConstants;
import com.param.adt.common.Response;
import com.param.adt.master.unit.dto.WardMasterDto;
import com.param.global.dto.AdmissionDto;

import java.util.List;

@Controller
@RequestMapping("/adt")
@SuppressWarnings({"rawtypes","unchecked"})
public class AdmittedPatientListController implements ICommonConstants
{

	@Autowired
	IAdmittedPatientListService iAdmittedPatientListService;
	
	
	@RequestMapping(value="/getAdmittedPatientList" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getAdmittedPatientList(@RequestBody AdmissionDto admissionDto){
		try{			
			return iAdmittedPatientListService.getAdmittedPatientList(admissionDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null,null);
	}
	
	@RequestMapping(value="/getActiveWardListByFloorId" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getActiveWardListByFloorId(@RequestBody WardMasterDto wardMasterDto){
		try{			
			return iAdmittedPatientListService.getActiveWardListByFloorId(wardMasterDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null,null);
	}
	
	@RequestMapping(value="/getKinDetailsByAdmissionId" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getKinDetailsByAdmissionId(@RequestBody AdmissionDto admissionDto){
		try{			
			return iAdmittedPatientListService.getKinDetailsByAdmissionId(admissionDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null,null);
	}
	
	@RequestMapping(value="/getAdmittedPatientByFloorWard" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getAdmittedPatientByFloorWard(@RequestBody AdmissionDto admissionDto){
		try{			
			
			return iAdmittedPatientListService.getAdmittedPatientByFloorWard(admissionDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null,null);
	}
	
	@RequestMapping(value="/serachPatientForMapOfBed" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response serachPatientForMapOfBed(@RequestBody AdmissionDto admissionDto){
		try{			
			
			return iAdmittedPatientListService.serachPatientForMapOfBed(admissionDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null,null);
	}
	
	@RequestMapping(value="/doctorDesk/ipPatientListByDocId" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getIpPatientListByDocId(@RequestBody AdmissionDto admissionDto){
		try{			
			return iAdmittedPatientListService.getIpPatientListByDocId(admissionDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null,null);
	}

	@RequestMapping(value="/doctorDesk/ipPatientListByDocIdBySearch" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response ipPatientListByDocIdBySearch(@RequestBody AdmissionDto admissionDto){
		try{
			return iAdmittedPatientListService.ipPatientListByDocIdBySearch(admissionDto);
		}catch(Exception e){

		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null,null);
	}
	@RequestMapping(value="/doctorDesk/ipPatientListByDocIdForCancelDischarge" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response ipPatientListByDocIdForCancelDischarge(@RequestBody AdmissionDto admissionDto){
		try{			
			return iAdmittedPatientListService.ipPatientListByDocIdForCancelDischarge(admissionDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null,null);
	}
	
	@RequestMapping(value="/getAdmittedPatientListForEMR" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getAdmittedPatientListForEMR(@RequestBody AdmissionDto admissionDto){
		try{			
			return iAdmittedPatientListService.getAdmittedPatientListForEMR(admissionDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null,null);
	}
	
	@RequestMapping(value="/getAdmittedPatientListForIPDNursingWorklist" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getAdmittedPatientListForIPDNursingWorklist(@RequestBody AdmissionDto admissionDto){
		try{			
			return iAdmittedPatientListService.getAdmittedPatientListForIPDNursingWorklist(admissionDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null,null);
	}
	
	@RequestMapping(value="/getListOfERPatients" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getListOfERPatients(@RequestBody AdmissionDto admissionDto){
		try{			
			return iAdmittedPatientListService.getListOfERPatients(admissionDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null,null);
	}
	
	@RequestMapping(value="/getListOfDayCarePatientsForNursing" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getListOfDayCarePatientsForNursing(@RequestBody AdmissionDto admissionDto){
		try{			
			return iAdmittedPatientListService.getListOfDayCarePatientsForNursing(admissionDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null,null);
	}
	
	@RequestMapping(value="/getListOfDayCareNursingAccepted" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getListOfDayCareNursingAccepted(@RequestBody AdmissionDto admissionDto){
		try{			
			return iAdmittedPatientListService.getListOfDayCareNursingAccepted(admissionDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null,null);
	}
	
	@RequestMapping(value="/doctorDesk/getHemodialysisByDoctorList" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getHemodialysisByDoctorList(@RequestBody AdmissionDto admissionDto){
		try{			
			return iAdmittedPatientListService.getHemodialysisByDoctorList(admissionDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null,null);
	}
	
	@RequestMapping(value="/getAdmittedPatientListForIPDNursingWorklistForNursingPage" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getAdmittedPatientListForIPDNursingWorklistForNursingPage(@RequestBody AdmissionDto admissionDto){
		try{			
			return iAdmittedPatientListService.getAdmittedPatientListForIPDNursingWorklistForNursingPage(admissionDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null,null);
	}
	@RequestMapping(value="/getAdmittedPatientListForIPDNursingWorklistForNursingPageCount" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getAdmittedPatientListForIPDNursingWorklistForNursingPageCount(@RequestBody AdmissionDto admissionDto){
		try{			
			return iAdmittedPatientListService.getAdmittedPatientListForIPDNursingWorklistForNursingPageCount(admissionDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null,null);
	}
	@RequestMapping(value="/getAdmittedPatientListPagination" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getAdmittedPatientListPagination(@RequestBody AdmissionDto admissionDto){
		try{			
			return iAdmittedPatientListService.getAdmittedPatientListPagination(admissionDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null,null);
	}
	@RequestMapping(value="/getAdmittedPatientListPaginationCount" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getAdmittedPatientListPaginationCount(@RequestBody AdmissionDto admissionDto){
		try{			
			return iAdmittedPatientListService.getAdmittedPatientListPaginationCount(admissionDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null,null);
	}
	
	/*
	 * @RequestMapping(value="/doctorDesk/getLoginDoctorSpecialityId" , method =
	 * RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	 * public @ResponseBody Response getLoginDoctorSpecialityId(@RequestBody
	 * AdmissionDto admissionDto){ try{ return
	 * iAdmittedPatientListService.getLoginDoctorSpecialityId(admissionDto);
	 * }catch(Exception e){  } return new Response(ERROR, null,
	 * COMMON_ERROR_MESSAGE, null,null); }
	 */
	
	@RequestMapping(value="/admittedPatientListUniversalSearch" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response admittedPatientListUniversalSearch(@RequestBody AdmissionDto admissionDto){
		try{			
			return iAdmittedPatientListService.admittedPatientListUniversalSearch(admissionDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null,null);
	}
	
	@RequestMapping(value="/admittedPatientListUniversalSearchForDietOrder" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response admittedPatientListUniversalSearchForDietOrder(@RequestBody AdmissionDto admissionDto){
		try{			
			return iAdmittedPatientListService.admittedPatientListUniversalSearchForDietOrder(admissionDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null,null);
	}
	
	@RequestMapping(value="/getAdmittedPatientListForVitalUploader" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getAdmittedPatientListForVitalUploader(@RequestBody AdmissionDto admissionDto){
		try{			
			return iAdmittedPatientListService.getAdmittedPatientListForVitalUploader(admissionDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null,null);
	}
	
	@RequestMapping(value="/doctorApp/ipPatientListByDocId" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getIpPatientListForDoctorApp(@RequestBody AdmissionDto admissionDto){
		try{			
			return iAdmittedPatientListService.getIpPatientListForDoctorApp(admissionDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null,null);
	}
	
	@RequestMapping(value="/getAdmittedPatientListForNewBorn" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getAdmittedPatientListForNewBorn(@RequestBody AdmissionDto admissionDto){
		try{			
			return iAdmittedPatientListService.getAdmittedPatientListForNewBorn(admissionDto);
			
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null,null);
	}
	
	
	@RequestMapping(value="/editAdmissionDetailsGetById" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response editAdmissionDetailsGetByIda(@RequestBody AdmissionDto admissionDto){
		try{			
			
			return iAdmittedPatientListService.editAdmissionDetailsGetById(admissionDto);
			
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null,null);
	}
	
	@RequestMapping(value="/admissionCancellation" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response admissionCancellation(@RequestBody AdmissionDto admissionDto){
		try{			
			return iAdmittedPatientListService.admissionCancellation(admissionDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null,null);
	}
	
	@RequestMapping(value="/getPatientDetailsForWorklistForWebPage" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getPatientDetailsForWorklistForWebPage(@RequestBody AdmissionDto admissionDto){
		try{			
			return iAdmittedPatientListService.getPatientDetailsForWorklistForWebPage(admissionDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null,null);
	}
	
	@RequestMapping(value="/getAdmittedPatientListForDietOrder" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getAdmittedPatientListForDietOrder(@RequestBody AdmissionDto admissionDto){
		try{			
			return iAdmittedPatientListService.getAdmittedPatientListForDietOrder(admissionDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null,null);
	}

	@RequestMapping(value="/savePatientOrdersWorkbench" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response savePatientOrdersWorkbench(@RequestBody List<AdmissionDto> admissionDtoList){
		try{
			return iAdmittedPatientListService.savePatientOrdersWorkbench(admissionDtoList);
		}catch(Exception e){
			return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null,null);
		}
	}

	@RequestMapping(value="/saveTriageCurrentDetails" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveTriageCurrentDetails(@RequestBody AdmissionDto admissionDto){
		try{
			return iAdmittedPatientListService.saveTriageCurrentDetails(admissionDto);
		}catch(Exception e){

		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null,null);
	}
	
}

