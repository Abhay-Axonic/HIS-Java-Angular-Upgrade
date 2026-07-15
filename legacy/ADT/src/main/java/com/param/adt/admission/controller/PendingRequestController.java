package com.param.adt.admission.controller;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.google.gson.Gson;
import com.param.adt.admission.service.IAdmissionRequestService;
import com.param.adt.admission.service.IBedAllocationService;
import com.param.adt.admission.service.IPendingRequestService;
import com.param.adt.common.ICommonConstants;
import com.param.adt.common.Response;
import com.param.global.dto.AdmissionNoteDto;
import com.param.service.global.SessionService;
import com.param.service.global.model.CookieObjectDto;

@Controller
@RequestMapping("/adt")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class PendingRequestController implements ICommonConstants {
	@Autowired
	private IPendingRequestService iPendingRequestService;

	@Autowired
	IAdmissionRequestService iAdmissionRequestService;

	@Autowired
	IBedAllocationService iBedAllocationService;
	
	@Autowired
	SessionService sessionService;
	

	@RequestMapping(value = "/getPendingAdmissionRequestDetails", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getPendingAdmissionRequestDetails(@RequestBody AdmissionNoteDto admissionNoteDto) {

		try {
			return iPendingRequestService.getPendingAdmissionRequestDetails(admissionNoteDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/getPendingAdmissionCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getPendingAdmissionCount(@RequestBody AdmissionNoteDto admissionNoteDto) {

		try {
			return iPendingRequestService.getPendingAdmissionCount(admissionNoteDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/getAvtivePatientCategoryList", method = RequestMethod.GET)
	public @ResponseBody Response getAvtivePatientCategoryList(HttpServletRequest request) {
		try {
			
			if(request.getCookies()!=null)
        	{
        		 List<Cookie> cookies = Arrays.stream(request.getCookies())
                         .filter(cookie -> cookie.getName().equals("cookieObject")).collect(Collectors.toList());
        		 if (cookies != null && cookies.size() > 0) {
                     sessionService.setCookieObjectDto(new Gson().fromJson(java.net.URLDecoder.decode(cookies.get(0).getValue(), StandardCharsets.UTF_8.name()), CookieObjectDto.class));
                 }
        	}

			return  iPendingRequestService.getAvtivePatientCategoryList(sessionService.getOrganizationId());

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/getAvtivePaymentEntitlementList", method = RequestMethod.GET)
	public @ResponseBody Response getAvtivePaymentEntitlementList() {
		try {

			return iPendingRequestService.getAvtivePaymentEntitlementList();

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/cancelPendingAdmissionRequest",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response cancelPendingAdmissionRequest(@RequestBody AdmissionNoteDto admissionNoteDto) {
		try {
			return iPendingRequestService.cancelPendingAdmissionRequest(admissionNoteDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/updatePendingAdmissionRequestDetails", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updatePendingAdmissionRequestDetails(@RequestBody AdmissionNoteDto admissionNoteDto) {
		try {
			return iPendingRequestService.updatePendingAdmissionRequestDetails(admissionNoteDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/updateReserveBedDetails", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateReserveBedDetails(@RequestBody AdmissionNoteDto admissionNoteDto) {
		try {
			return iPendingRequestService.updateReserveBedDetails(admissionNoteDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/uploadAdmissionDocuments", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response uploadAdmissionDocuments(@RequestBody AdmissionNoteDto admissionNoteDto) {
		try {
			return iPendingRequestService.uploadAdmissionDocuments(admissionNoteDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/getOPDAdmissionRequestDetails", method = RequestMethod.GET)
	public @ResponseBody Response getOPDAdmissionRequestDetails(@RequestParam ("patientId") Integer patientId, 
																@RequestParam ("encounterId") Integer encounterId,
																//@RequestParam ("visitTypeId") Integer visitTypeId,
																@RequestParam ("organizationId") Integer organizationId,
																@RequestParam ("unitId") Integer unitId) {

		try {
			return iPendingRequestService.getOPDAdmissionRequestDetails(encounterId, patientId,organizationId,unitId);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/getDayCareAdmissionRequestDetails", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getDayCareAdmissionRequestDetails(@RequestBody AdmissionNoteDto admissionNoteDto) {
		try {
			return iPendingRequestService.getDayCareAdmissionRequestDetails(admissionNoteDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/getAdmissionRequestDetailsForBookView", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getAdmissionRequestDetailsForBookView(@RequestBody AdmissionNoteDto admissionNoteDto) {
		try {
			return iPendingRequestService.getAdmissionRequestDetailsForBookView(admissionNoteDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/getDataByDateFromPendingRequest", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE )
	public @ResponseBody Response getOpAdmissionListForIPDCoversheetByDate(@RequestBody AdmissionNoteDto dto) {
		try
		{
			return iPendingRequestService.getOpAdmissionListForIPDCoversheetByDate(dto);
		}catch(Exception e)
		{

		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/getOpAdmissionListForCoversheet/getUniqueDateFromPendingRequest", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE )
	public @ResponseBody Response getUniqueDateForIPDCoversheet(@RequestBody AdmissionNoteDto dto) {
		try
		{
			return iPendingRequestService.getUniqueDateForIPDCoversheet(dto);
		}catch(Exception e)
		{

		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

    @RequestMapping(value = "/getActivePatientCategoryList/{orgId}", method = RequestMethod.GET)
    public @ResponseBody Response getActivePatientCategoryList(@PathVariable("orgId") Integer organizationId) {
        try {
            return  iPendingRequestService.getAvtivePatientCategoryList(organizationId);

        } catch (Exception e) {

        }
        return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
    }

    @RequestMapping(value = "/getPatientRegistrationTypes", method = RequestMethod.GET)
    public @ResponseBody Response getPatientRegistrationTypes() {
        try {
            return  iPendingRequestService.getPatientRegistrationTypes();

        } catch (Exception e) {

        }
        return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
    }
}
