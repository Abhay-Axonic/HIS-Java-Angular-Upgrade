package com.param.adt.admission.controller;

import java.io.ByteArrayOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.param.adt.admission.service.IAdmissionRequestService;
import com.param.adt.common.IADTConstants;
import com.param.adt.common.ICommonConstants;
import com.param.adt.common.Response;
import com.param.adt.master.global.dto.VisitMasterDto;
import com.param.global.dto.AdmissionNoteDto;
import com.param.global.dto.DoctorSpecialityMapperDto;
import com.param.global.dto.PatientRegistrationDto;

import net.glxn.qrgen.javase.QRCode;

@Controller
@RequestMapping("/adt")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class AdmissionRequestController implements ICommonConstants, IADTConstants {
	
	@Autowired
	IAdmissionRequestService iAdmissionRequestService;

	@RequestMapping(value = "/saveAdmissionRequest", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public synchronized @ResponseBody Response saveAdmissionRequest(@RequestBody AdmissionNoteDto admissionNoteDto) {
		try {
			return iAdmissionRequestService.saveAdmissionRequest(admissionNoteDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/getPatientListLike", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getPatientListLike(@RequestBody PatientRegistrationDto patientRegistrationDto) {
		try {
			return iAdmissionRequestService.getPatientListLike(patientRegistrationDto);
		} catch (Exception e) {
			
		}
		
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/getPatientVisitDetailsById", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getPatientVisitDetailsById(@RequestBody VisitMasterDto visitMasterDto) {
		try {

			return iAdmissionRequestService.getPatientVisitDetailsById(visitMasterDto);
		} catch (Exception e) {
			
		}
		
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/getDoctorListAganistSpeciality", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getDoctorListAganistSpeciality(
			@RequestBody DoctorSpecialityMapperDto doctorSpecialityMapper) {
		try {

			return iAdmissionRequestService.getDoctorListAganistSpeciality(doctorSpecialityMapper);
		} catch (Exception e) {
			
		}
	
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/getDoctorsListAganistSpecialities", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getDoctorsListAganistSpecialities(
			@RequestBody DoctorSpecialityMapperDto doctorSpecialityMapper) {
		try {

			return iAdmissionRequestService.getDoctorsListAganistSpecialitys(doctorSpecialityMapper);
		} catch (Exception e) {
			
		}
	
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/getNephrologyDoctorsList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getNephrologyDoctorsList(
			@RequestBody DoctorSpecialityMapperDto doctorSpecialityMapper) {
		try {

			return iAdmissionRequestService.getNephrologyDoctorsList(doctorSpecialityMapper);
		} catch (Exception e) {
			
		}
	
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/getPastAdmissionDetails", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public synchronized @ResponseBody Response getPastAdmissionDetails(@RequestBody AdmissionNoteDto admissionNoteDto) {
		try {
			return iAdmissionRequestService.getPastAdmissionDetails(admissionNoteDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/getPatientListLikeForERMapOfBed", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getPatientListLikeForERMapOfBed(@RequestBody PatientRegistrationDto patientRegistrationDto) {
		try {
			return iAdmissionRequestService.getPatientListLikeForERMapOfBed(patientRegistrationDto);
		} catch (Exception e) {
			
		}
		
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	
	@RequestMapping(value = "/br/{barcodeNumber}")
	public void barcode(@PathVariable("barcodeNumber")String barcodeNumber,HttpServletRequest request,HttpServletResponse response){
		try{
			
			 ByteArrayOutputStream stream = QRCode
				      .from(barcodeNumber)
				      .withSize(250, 250)
				      .stream();
			response.setContentType("image/png");
			response.setHeader("Content-Disposition", "inline; filename=randomNumber.PNG");
			ServletOutputStream output = response.getOutputStream();
			output.write(stream.toByteArray());
			output.close();
		}catch(Exception e){
			
		}
	}

	@RequestMapping(value="/getDataByDateFromAdmission", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE )
	public @ResponseBody Response getDataForIPDCoversheetByDate(@RequestBody AdmissionNoteDto dto) {
		try
		{
			return iAdmissionRequestService.getAdmissionForIPDCoversheetByDate(dto);
		}catch(Exception e)
		{

		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/getEncounterListForCoversheet/getUniqueDateFromAdmission", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE )
	public @ResponseBody Response getUniqueDateForIPDCoversheet(@RequestBody AdmissionNoteDto dto) {
		try
		{
			return iAdmissionRequestService.getUniqueDateForIPDCoversheet(dto);
		}catch(Exception e)
		{

		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}
	
	}

