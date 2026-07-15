package com.param.adt.discharge.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.param.global.common.DocUploadUtility;
import com.param.global.dao.IUnitApplicationBillingConfigurationDao;
import com.param.global.dto.MyLifeConnectionLinkDto;
import com.param.global.org.dao.IGlobalOrganisationMasterDao;
import com.param.global.org.dto.OrganizationMasterDto;
import com.param.global.service.IPatientRegistrationService;
import com.param.workbench.service.IRISWorkbenchService;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.param.adt.discharge.dto.DischargeSummaryDto;
import com.param.adt.discharge.service.IDischargeSummaryService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.AdmissionDto;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/adt/discharge")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DischargeSummaryController implements ICommonConstants{

	
	
	@Autowired
	private IDischargeSummaryService iDischargeSummaryService;

	@Autowired
	IGlobalOrganisationMasterDao iGlobalOrganisationMasterDao;


	@Autowired
	IUnitApplicationBillingConfigurationDao iUnitApplicationBillingConfigurationDao;

	@Autowired
	DocUploadUtility docUploadUtility;

	@Autowired
	RestTemplate restTemplate;
	
	@RequestMapping(value = "/savePatientDischargeSummary", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response savePatientDischargeSummary(@RequestBody DischargeSummaryDto dischargeSummaryDto) {
		try {
			return iDischargeSummaryService.savePatientDischargeSummary(dischargeSummaryDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	
	
	@RequestMapping(value = "/getDischargeSummaryTemplateByPatientId/{patientId}/{organizationId}/{unitId}", method = RequestMethod.GET)
	public Response getDischargeSummaryTemplateByPatientId(@PathVariable ("patientId") Integer patientId ,
												 @PathVariable ("organizationId") Integer organizationId, 
												 @PathVariable ("unitId") Integer unitId) {
		try {
			return iDischargeSummaryService.getDischargeSummaryTemplateByPatientId(patientId, organizationId, unitId);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	
	@RequestMapping(value = "/getFieldTemplateSummaryByTemplateId/{templateId}/{organizationId}/{unitId}", method = RequestMethod.GET)
	public Response getFieldTemplateSummaryByTemplateId(@PathVariable ("templateId") Integer templateId ,
												 @PathVariable ("organizationId") Integer organizationId, 
												 @PathVariable ("unitId") Integer unitId) {
		try {
			return iDischargeSummaryService.getFieldTemplateSummaryByTemplateId(templateId, organizationId, unitId);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getPatientDischargeSummaryList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getPatientDischargeSummaryList(@RequestBody DischargeSummaryDto dischargeSummaryDto) {
		try {
			return iDischargeSummaryService.getPatientDischargeSummaryList(dischargeSummaryDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getFieldDischargeSummaryByTemplateIdAndPatientId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getPatientDischargeSummaryFieldList(@RequestBody DischargeSummaryDto dischargeSummaryDto) {
		try {
			return iDischargeSummaryService.getPatientDischargeSummaryFieldList(dischargeSummaryDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	
	@RequestMapping(value = "/getAdmissionReason", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getAdmissionReason(@RequestBody DischargeSummaryDto dischargeSummaryDto) {
		try {
			return iDischargeSummaryService.getAdmissionReason(dischargeSummaryDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/updateDischargeSummaryReportStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateDischargeSummaryReportStatus(@RequestBody DischargeSummaryDto dischargeSummaryDto) {
		try {
			return iDischargeSummaryService.updateDischargeSummaryReportStatus(dischargeSummaryDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getSavedDischargeSummaryByAdmissionId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getSavedDischargeSummaryByAdmissionId(@RequestBody DischargeSummaryDto dischargeSummaryDto) {
		try {
			return iDischargeSummaryService.getSavedDischargeSummaryByAdmissionId(dischargeSummaryDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/dischargeSummaryCard", method = RequestMethod.POST)
	public Response getDischargeSummaryPdfFile(HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws IOException {
		try {
			DischargeSummaryDto dischargeSummaryDto=new DischargeSummaryDto();
			/*String reportData=servletRequest.getParameter("reportParam");
			Gson g = new Gson();
			DischargeSummaryDto dto = g.fromJson(reportData, DischargeSummaryDto.class);*/
			dischargeSummaryDto.setDischargeSummaryId(Integer.parseInt(servletRequest.getParameter("dischargeSummaryId")));
			dischargeSummaryDto.setAdmissionId(Integer.parseInt(servletRequest.getParameter("admissionId")));
			dischargeSummaryDto.setUnitId(Integer.parseInt(servletRequest.getParameter("orgUnitId")));
			dischargeSummaryDto.setOrganizationId(Integer.parseInt(servletRequest.getParameter("orgId")));
			dischargeSummaryDto.setHeaderType(Integer.parseInt(servletRequest.getParameter("headerType")));
			//Response res=iDischargeSummaryService.getPatientDataForDischargeSummary(dischargeSummaryDto);
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
			byteStream=iDischargeSummaryService.getDischargeSummaryPdf(dischargeSummaryDto);
			OutputStream output = null;
			 servletResponse.setContentType("application/pdf");
			output = servletResponse.getOutputStream();
		       output.write(byteStream.toByteArray());
		       output.close();
		       byteStream.close();
//			String filePath = uploadUtility.uploadDoctorCoverSheet(inputStream, appointmentListDto);
		    return null;
			}catch(Exception e){
				
			}
		return null;
	}
	
	@RequestMapping(value = "/getDischargeSummaryCardForMyLife/{admissionId}", method = RequestMethod.GET)
	public Response getDischargeSummaryCardForMyLife(@PathVariable ("admissionId") Integer admissionId) throws IOException {
		try {
			DischargeSummaryDto dischargeSummaryDto=new DischargeSummaryDto();
			dischargeSummaryDto.setAdmissionId(admissionId);
			Integer dischargeSummaryId=iDischargeSummaryService.getDischargeSummaryIdByAdmissionId(admissionId);
			if(dischargeSummaryId!=null) {
				dischargeSummaryDto.setDischargeSummaryId(dischargeSummaryId);
				ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
				byteStream=iDischargeSummaryService.getDischargeSummaryPdf(dischargeSummaryDto);
			    byteStream.close();
			   return new Response(SUCCESS, SUCCESS_CODE, null, null, byteStream.toByteArray()); 
			}else {
				return new Response<>(ERROR, ERROR_CODE, "Discharge summary not available", null, null); 
			}
			}catch(Exception e){
				
			}
		return null;
	}
	
	@RequestMapping(value="/searchDischargeSummaryByPatientName", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response searchActiveServices(@RequestBody DischargeSummaryDto dischargeSummaryDto) {
		try {
			return iDischargeSummaryService.searchPatientDischargeSummary(dischargeSummaryDto);
		}catch (Exception e) {
			
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getFollowUpAppointment", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getFollowUpAppointment(@RequestBody DischargeSummaryDto dischargeSummaryDto) {
		try {
			return iDischargeSummaryService.getFollowUpAppointment(dischargeSummaryDto);
		}catch (Exception e) {
			
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/getPatientDischargeSummaryTemplateData", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getPatientDischargeSummaryTemplateData(@RequestBody DischargeSummaryDto dischargeSummaryDto) {
		try {
			return iDischargeSummaryService.getPatientDischargeSummaryTemplateData(dischargeSummaryDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/saveDischargeSumTabColumnsPrintMapper", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveDischargeSumTabColumnsPrintMapper(@RequestBody DischargeSummaryDto dischargeSummaryDto) {
		try {
			return iDischargeSummaryService.saveDischargeSumTabColumnsPrintMapper(dischargeSummaryDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value="/getDischargeSummaryPrintType", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getDischargeSummaryPrintType(@RequestBody DischargeSummaryDto dischargeSummaryDto) {
		try {
			return iDischargeSummaryService.getDischargeSummaryPrintType(dischargeSummaryDto);
		}catch (Exception e) {
			
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/updateExtendPdd", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateExtendPdd(@RequestBody AdmissionDto dto) {
		try {
			return iDischargeSummaryService.updateExtendPdd(dto);
		}catch (Exception e) {
			
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/updateAdmittedPatientList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateAdmittedPatientList(@RequestBody AdmissionDto dto) {
		try {
			return iDischargeSummaryService.updateAdmittedPatientList(dto);
		}catch (Exception e) {
			
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/sendDischargeSummaryReportToMyLife" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response sendDischargeSummaryReportToMyLife(@RequestBody DischargeSummaryDto dto){
		try{

			return iDischargeSummaryService.sendDischargeSummaryReportToMyLife(dto);

		}catch(Exception e){
			e.printStackTrace();
		}
		return new Response(ERROR, null, null, null,null);
	}
}
