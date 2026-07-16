package com.param.ipd.service;

import com.param.opd.coversheet.dao.IMedicationHistoryDao;
import com.param.opd.coversheet.dto.ICPCSystemDTO;
import com.param.opd.coversheet.dto.MedicationHistoryDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.param.global.common.DocUploadUtility;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.dao.IPatientRegistrationDao;
import com.param.global.dao.IScheduleLogsDao;
import com.param.global.dto.MyLifeConnectionLinkDto;
import com.param.global.dto.sync.ScheduleAppointmentReqDto;
import com.param.global.opd.dao.IEncounterMasterDao;
import com.param.global.org.dao.IGlobalOrganisationMasterDao;
import com.param.global.org.dto.OrganizationMasterDto;
import com.param.global.unit.dao.IUserRoleMasterDao;
import com.param.global.unit.dto.ButtonMasterDto;
import com.param.ipd.dto.SmartDocumentSummary;
import com.param.opd.coversheet.dto.PatientMedicalHistoryDto;
import com.param.opd.coversheet.dto.PatientPersonalHistoryDetailsDto;
import com.param.opd.encounter.dto.EncounterDocDetailsDto;
import com.param.utility.TimeZoneComponent;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import com.param.entity.model.ipd.ComplaintAdmission;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.ipd.dao.IComplaintAdmissionDao;
import com.param.ipd.dto.ComplaintAdmissionDto;

import in.param.exception.ApplicationException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@SuppressWarnings("rawtypes")
public class ComplaintAdmissionServiceImpl implements IComplaintAdmissionService, ICommonConstants {

	@Autowired 
	IComplaintAdmissionDao iComplaintAdmissionDao;

	@Autowired
	private IMedicationHistoryDao iMedicationHistoryDao;

	@Autowired
	private IEncounterMasterDao iEncounterMasterDao;

	@Autowired
	IUserRoleMasterDao iUserRoleMasterDao;

	@Autowired
	DocUploadUtility docUploadUtility;

	@Autowired
	IGlobalOrganisationMasterDao iGlobalOrganisationMasterDao;

	@Autowired
	IPatientRegistrationDao iPatientRegistrationDao;

	@Autowired
	private Gson gson;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	IScheduleLogsDao iScheduleLogsDao;

	@Autowired
	IPatientRegistrationDao iPatientMasterDao;

	@Autowired
	IScheduleLogsDao isheduleDao;

	@Override
	public Response getComplaintAdmission(ComplaintAdmissionDto complaintAdmissionDto) throws ApplicationException {
		try {
			    MedicationHistoryDto medicationHistoryDto = new MedicationHistoryDto();
			    medicationHistoryDto.setOrganizationId(complaintAdmissionDto.getOrganizationId());
			    medicationHistoryDto.setUnitId(complaintAdmissionDto.getUnitId());
			    Response icpcFavResponse = iMedicationHistoryDao .getFavriteListOfICPCSystemForCoversheet(medicationHistoryDto);
			    List<ICPCSystemDTO> icpcFavMasterDtos = (icpcFavResponse != null && icpcFavResponse.getListObject() != null) ? icpcFavResponse.getListObject() : Collections.emptyList();
			    Response complaintResponse = iComplaintAdmissionDao.getComplaintAdmission(complaintAdmissionDto);
			    List<ComplaintAdmissionDto> listComplaintAdmissionDto = (complaintResponse != null && complaintResponse.getListObject() != null) ? (List<ComplaintAdmissionDto>) complaintResponse.getListObject() : Collections.emptyList();
			    return new Response(SUCCESS, SUCCESS_CODE, null, listComplaintAdmissionDto, null,null,icpcFavMasterDtos);
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response saveComplaintAdmission(ComplaintAdmissionDto complaintAdmissionDto)throws ApplicationException {
		try {
				return iComplaintAdmissionDao.saveComplaintAdmission(complaintAdmissionDto);
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response editComplaintAppointment(ComplaintAdmissionDto complaintAdmissionDto)throws ApplicationException {
		try {
				return iComplaintAdmissionDao.editComplaintAppointment(complaintAdmissionDto);
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateComplaintAdmission(ComplaintAdmissionDto complaintAdmissionDto)throws ApplicationException {
		try {
				return iComplaintAdmissionDao.updateComplaintAdmission(complaintAdmissionDto);
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@Override
	public Response updateSaveNurseReviewedFlag(ComplaintAdmission complaintAdmission)
			throws ApplicationException {
		return iComplaintAdmissionDao.updateSaveNurseReviewedFlag(complaintAdmission);
	}

	@Override
	public Response updateComplaintAdmissionStatusToRemoveComplaint(ComplaintAdmissionDto complaintAdmissionDto)
			throws ApplicationException {
		try {
			
			return iComplaintAdmissionDao.updateComplaintAdmissionStatusToRemoveComplaint(complaintAdmissionDto);
	
	} catch (Exception e) {
		
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	/*@Override
	public Response getComplaintAppointmentByPatientId(ComplaintAdmissionDto complaintAdmissionDto)
			throws ApplicationException {
		try {
			
			return iComplaintAdmissionDao.getComplaintAppointmentByPatientId(complaintAdmissionDto);
	
	} catch (Exception e) {
		
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}*/

	@Override
	public Response saveResolvedComplaintAdmission(ComplaintAdmissionDto complaintAdmissionDto)
			throws ApplicationException {
	try {
			
			return iComplaintAdmissionDao.saveResolvedComplaintAdmission(complaintAdmissionDto);
	
	} catch (Exception e) {
		
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActiveComplaintsForIPDCoversheetByDate(ComplaintAdmissionDto dto) throws ApplicationException {
		try{
			List<ComplaintAdmissionDto> resDtoList =  new ArrayList<>();
			for (String date : dto.getDates()){
				List<ComplaintAdmissionDto> complaintsList = new ArrayList<>();
				ComplaintAdmissionDto resDto = new ComplaintAdmissionDto();
				resDto.setAddedDate(date);
				if(dto.getActiveCompliantList() != null && dto.getActiveCompliantList().size()>0){
					
					for (ComplaintAdmissionDto complaint : dto.getActiveCompliantList()) {

						if (date.equals(complaint.getCreatedDate1())) {
							complaint.setAddedDate(date);
							complaintsList.add(complaint);
						}
					}
					resDto.setActiveCompliantList(complaintsList);
				}
				resDtoList.add(resDto);
			}

			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, resDtoList, null);
		}
		catch (Exception e){

		}

		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getUniqueDateForIPDCoversheet(ComplaintAdmissionDto dto) throws ApplicationException {
		try{
			List<String>distinctDates= new ArrayList<>();
			ComplaintAdmissionDto resDto = dto;

			if(dto.getActiveCompliantList() != null && dto.getActiveCompliantList().size()>0){
				for (ComplaintAdmissionDto complaint : dto.getActiveCompliantList()) {

					if (!distinctDates.contains(complaint.getCreatedDate1())) {
						distinctDates.add(complaint.getCreatedDate1());
					}
				}

				if(distinctDates != null && distinctDates.size()>0){
					Collections.sort(distinctDates, Comparator.reverseOrder());
					resDto.setDates(distinctDates);
					return getActiveComplaintsForIPDCoversheetByDate(resDto);
				}
//				return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, resDto);
			}
			else{
				return null;
			}
		}
		catch (Exception e){

		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	@Transactional
	public Response generateSummaryDocument(ComplaintAdmissionDto dto) throws ApplicationException {
		try{

			Response res = iEncounterMasterDao.getAllCoversheetDataByPatientId(dto.getPatientId());


			ButtonMasterDto buttonDto = new ButtonMasterDto();
			buttonDto.setDoctorId(dto.getDoctorId());
			buttonDto.setHospitalId(dto.getOrganizationId());
			buttonDto.setUnitId(dto.getUnitId());
			buttonDto.setTabId(42);
			buttonDto.setSpecialityId(dto.getSpecialityId());
			Response buttonConfigRes = iUserRoleMasterDao.getSingleButtonById(buttonDto);

			ButtonMasterDto buttonConfig = (ButtonMasterDto)buttonConfigRes.getObject();

			String callBackUrl = iEncounterMasterDao.getAiLetterCallbackUrl();
//			callBackUrl = "https://a604d5c5db8e.ngrok-free.app/HIS-WAR/";

//			 callBackUrl = callBackUrl +"api/ipd/saveMyLatestMedicalSummaryCallBack";
			    Response globalRes=isheduleDao.getConnectionStringForMyLife();
			    MyLifeConnectionLinkDto connectionLinkDto = (MyLifeConnectionLinkDto) globalRes.getObject();
			    String mlLink = connectionLinkDto.getMyLifeLink();
			System.out.println(" mlLink "+mlLink);
//			    mlLink = "https://3c32-122-170-198-68.ngrok-free.app/MyLifeGlobal/api";
				callBackUrl = mlLink +"/saveMyLatestMedicalSummaryCallBackML";


				if ((SUCCESS).equals(res.getStatus()) && res.getListObject()!=null && res.getListObject().size()>0) {
				List<EncounterDocDetailsDto> coversheetData = res.getListObject();
				List<String> docList = coversheetData.stream().map(EncounterDocDetailsDto :: getDocumentPath).collect(Collectors.toList());
				List<String> latestSummaryReport = coversheetData.stream().map(EncounterDocDetailsDto :: getLatestMedicalSummaryReport).collect(Collectors.toList());

//				String latestSummaryReport = getLatestSummaryReportfromML(dto,mlLink);

				ObjectMapper mapper = new ObjectMapper();
				Map<String, List<String>> coverSheetReportList =  new HashMap<>();

				coverSheetReportList.put("coverSheetReportList",docList);
				String  jsonResult = buttonConfig.getJsonConfiguration();

				JSONObject jsonObject = new JSONObject(jsonResult);
				JSONObject urlObject = jsonObject.getJSONObject("urlObject");
				String url = urlObject.getString("URL");
				JSONObject payload = jsonObject.getJSONObject("aiPayload");
				payload.put("Input_from_application", dto.getParseCasesheetHtml());
				Map<String,Object> doctorCoverPageDtoMap = new HashMap<>();
				doctorCoverPageDtoMap.put("appointmentId", dto.getEncounterId());
				doctorCoverPageDtoMap.put("patientId", dto.getPatientId());
				doctorCoverPageDtoMap.put("organizationId", dto.getOrganizationId());
				doctorCoverPageDtoMap.put("unitId", dto.getUnitId());
				doctorCoverPageDtoMap.put("uuid", dto.getUuid());
				doctorCoverPageDtoMap.put("applicationId", 2);
				doctorCoverPageDtoMap.put("encounterId", dto.getEncounterId());

				ObjectMapper objectMapper = new ObjectMapper();
				String mapAsString = objectMapper.writeValueAsString(doctorCoverPageDtoMap);
				JSONArray summaryListJson = new JSONArray(docList);//
				payload.put("summary_list", docList!=null && docList.size()>0 ? summaryListJson : "No");
				payload.put("latest_medical_summary",latestSummaryReport!=null && latestSummaryReport.size()>0 ? latestSummaryReport.getFirst() : "No");
//				payload.put("latest_medical_summary",latestSummaryReport!=null  ? latestSummaryReport : "No");

				payload.put("CallBackUrl", callBackUrl);
				payload.put("doctorCoverPageDto", mapAsString);
				OkHttpClient client = new OkHttpClient().newBuilder()
						.connectTimeout(300, TimeUnit.SECONDS)
						.readTimeout(300, TimeUnit.SECONDS)
						.writeTimeout(300, TimeUnit.SECONDS)
						.build();
				MediaType mediaType = MediaType.parse("application/json");
				System.out.println(payload.toString());
				RequestBody body = RequestBody.create(mediaType, payload.toString());
				Request request = new Request.Builder()
						.url(url)
						.method("POST", body)
						.build();
				okhttp3.Response response = client.newCall(request).execute();
				if (response.code() == 200) {
					String responseBody = response.body().string();
					System.out.println(responseBody);
					JSONObject jsonObject1 = new JSONObject(responseBody);
					return new Response<>(SUCCESS, SUCCESS_CODE, "Data sent to AI", null, null);

				}
		   }
		} catch (Exception e) {
           e.printStackTrace();
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);}

	@Override
	@Transactional
	public Response saveMyLatestMedicalSummaryCallBack(SmartDocumentSummary dto) throws ApplicationException {

	try {
		EncounterDocDetailsDto encounterDocDetailsDto = new EncounterDocDetailsDto();

		String patientDetails = dto.getDoctorCoverPageDtojsonString();
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> map = mapper.readValue(patientDetails, Map.class);

		Integer patientId = (Integer) map.get("patientId");

		Integer organizationId = (Integer) map.get("organizationId");
		Integer unitId = (Integer)  map.get("unitId");

		Integer encounterId = (Integer)  map.get("encounterId");

		String uuid = (String) map.get("uuid");

		ScheduleAppointmentReqDto reqDto = new ScheduleAppointmentReqDto();
		reqDto.setUuid(uuid);
		reqDto.setHospitalId(organizationId);
		patientId= iPatientMasterDao.getPatientIdByUuidAndHospitalId(reqDto);


		if(patientId==null){
			return new Response(ERROR, ERROR_CODE, "Patient Id is missing", null, null);
		}

		encounterDocDetailsDto.setPatientId(patientId);
		encounterDocDetailsDto.setOrganisationId(organizationId);
		encounterDocDetailsDto.setUnitId(unitId !=null ?unitId:organizationId);


		Response orgRes = iGlobalOrganisationMasterDao.getOrganiazationFolderPath(encounterDocDetailsDto.getOrganisationId());
		OrganizationMasterDto organizationMasterDto = (OrganizationMasterDto) orgRes.getObject();
		String folderPath = organizationMasterDto.getOrganizationFolder();

		MultipartFile multipart = convert(dto.getBase64PdfForClinicalReport(), "Latest_Medical_Summary_Report.pdf");
		String url = docUploadUtility.uploadEncounterDoc(multipart, folderPath, encounterDocDetailsDto);
		Integer pkdocId = iEncounterMasterDao.getLatestSummaryDocumentPKId(patientId,69);

			if (pkdocId != null && pkdocId > 0) {
				iEncounterMasterDao.updateSummaryDocument(pkdocId,url);

				if(dto.getNewComprehensiveClinicalReport()!=null){
					Response res = iPatientRegistrationDao.updateLatestMedicalSummaryReport(patientId, dto.getNewComprehensiveClinicalReport());

//					if(res.getObject()!=null){
//						String uuid = res.getObject().toString();
////						syncSummaryDocumentToMylife(dto);
//					}
				}
				return new Response(SUCCESS, SUCCESS_CODE, "Latest Medical Summary Report uploaded successfully", null, url);
			}

			Date date = TimeZoneComponent.getDateByzone(encounterDocDetailsDto.getUnitId());
			String dateTime = GlobalCommonDateUtils.getStringDate(date, "dd-M-yyyy HH:mm:ss");
			if (encounterDocDetailsDto.getUnitId() > 0)
			{
				encounterDocDetailsDto.setDocumentPath(url);
				encounterDocDetailsDto.setCreatedDate(dateTime);
				encounterDocDetailsDto.setUpdatedDate(dateTime);
				encounterDocDetailsDto.setUploadDoccumentDate(encounterDocDetailsDto.getUploadDoccumentDate());
				encounterDocDetailsDto.setDocumentTypeId(69);
				encounterDocDetailsDto.setFileName("Latest_Medical_Summary_Report.pdf");
				encounterDocDetailsDto.setEncounterId(encounterId);
				encounterDocDetailsDto.setUnitId(unitId);
				iEncounterMasterDao.saveDocumentAgainstEncounter(encounterDocDetailsDto);
			}
			if(dto.getNewComprehensiveClinicalReport()!=null){
				Response res = iPatientRegistrationDao.updateLatestMedicalSummaryReport(patientId, dto.getNewComprehensiveClinicalReport());
//				if(res.getObject()!=null){
//					String uuid = res.getObject().toString();
////					syncSummaryDocumentToMylife(uuid,url);
//				}
			}



	return new Response(SUCCESS, SUCCESS_CODE, "Latest Medical Summary Report uploaded successfully", null, url);
		}catch (Exception e){
			throw new ApplicationException(e.getMessage(), e);
		}
	}


	public  MultipartFile convert(String base64, String fileName) throws IOException {
		byte[] decodedBytes = Base64.getDecoder().decode(base64);
		return new MultipartFile() {

			@Override
			public String getName() {
				return fileName;
			}

			@Override
			public String getOriginalFilename() {
				return fileName;
			}

			@Override
			public String getContentType() {
				if (fileName.endsWith(".png")) return "image/png";
				else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) return "image/jpeg";
				else if (fileName.endsWith(".pdf")) return "application/pdf";
				else return "application/octet-stream";
			}

			@Override
			public boolean isEmpty() {
				return decodedBytes == null || decodedBytes.length == 0;
			}

			@Override
			public long getSize() {
				return decodedBytes.length;
			}

			@Override
			public byte[] getBytes() throws IOException {
				return decodedBytes;
			}

			@Override
			public ByteArrayInputStream getInputStream() throws IOException {
				return new ByteArrayInputStream(decodedBytes);
			}

			@Override
			public void transferTo(java.io.File dest) throws IOException, IllegalStateException {
				java.nio.file.Files.write(dest.toPath(), decodedBytes);
			}
		};
	}


	private String getLatestSummaryReportfromML(ComplaintAdmissionDto dto,String mlLink) {
		try {
			Map<String, String> requestMap = new HashMap<>();
			requestMap.put("uuid", dto.getUuid());

			String MY_LIFE_URI = null;
			URI urlMyLife = null;
			Response responseMyLife = null;
			Response commonResponseMyLife = null;
			Integer channelId = null;
			String token = null;

			MY_LIFE_URI = mlLink;

			RestTemplate restTemplate = new RestTemplate();
			String urlWithParams = MY_LIFE_URI + "/generateToken" + "?userName=" + "ClinivantageAdminUser";

			Map<String, String> mapResponse = restTemplate.getForObject(urlWithParams, Map.class);

			if (mapResponse != null) {
				token = mapResponse.get("token");
			}


			urlMyLife = URI.create(MY_LIFE_URI + "/getSummaryTextByUUID");
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer " + token);
			headers.set("userName", "ClinivantageAdminUser");
			headers.set("channelId", (channelId != null && channelId != 0) ? channelId.toString() : "1");
			HttpEntity<Map<String, String>> request =
					new HttpEntity<>(requestMap, headers);
			String stringResponse = restTemplate.postForObject(urlMyLife, request, String.class);
			commonResponseMyLife = gson.fromJson(stringResponse, Response.class);
			String summaryStringList = (String) commonResponseMyLife.getObject();
			return summaryStringList;
		}catch(Exception e){

		}
		return null;
	}

	@Override
	@Transactional
	public Response getAISummaryData(ComplaintAdmissionDto dto) throws ApplicationException {
		return iComplaintAdmissionDao.getAISummaryData(dto);
	}






}
