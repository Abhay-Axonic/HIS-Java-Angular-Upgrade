package com.param.workbench.service;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.List;
import java.util.Random;

import javax.transaction.Transactional;

import com.itextpdf.html2pdf.HtmlConverter;
import com.param.global.common.*;
import com.param.global.service.IUnitApplicationBillingConfigurationService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.dao.IRoomModalityMasterDao;
import com.param.global.dao.IScheduleLogsDao;
import com.param.global.dao.IUnitApplicationBillingConfigurationDao;
import com.param.global.dto.RadiologyOrdersListDto;
import com.param.global.dto.TemplateTransactionDto;
import com.param.global.org.dao.IGlobalOrganisationMasterDao;
import com.param.global.org.dto.OrganizationMasterDto;
import com.param.global.service.IOrderDetailsMasterService;
import com.param.global.service.IPatientRegistrationService;
import com.param.global.unit.dto.SmsUtilityConfigInfoDto;
import com.param.global.unit.service.ISmsUtilityConfigService;
import com.param.utility.TimeZoneComponent;
import com.param.workbench.dao.IRISWorkbenchDao;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.nodes.Entities;
import org.apache.log4j.Logger;

import in.param.exception.ApplicationException;


@Service
@SuppressWarnings("rawtypes")
public class RISWorkbenchServiceImpl implements IRISWorkbenchService ,ICommonConstants ,IError {

	private static final Logger logger = Logger.getLogger(RISWorkbenchServiceImpl.class);

	@Autowired
	IRISWorkbenchDao  iRISWorkbenchDao;
	@Autowired
	IPatientRegistrationService iPatientRegistrationService ;
	@Autowired
	IOrderDetailsMasterService iOrderDetailsMasterService;
	@Autowired
	IRoomModalityMasterDao iRoomModalityMasterDao;
	
	@Autowired
	ISmsUtilityConfigService smsUtilityService;
	
	@Autowired
	IGlobalOrganisationMasterDao iGlobalOrganisationMasterDao;
	
	@Autowired
	IScheduleLogsDao iScheduleLogsDao;
	
	@Autowired
	IUnitApplicationBillingConfigurationDao iUnitApplicationBillingConfigurationDao;
	
	@Autowired
	DocUploadUtility docUploadUtility;

	@Autowired
	private IUnitApplicationBillingConfigurationService unitApplicationBillingConfigurationService;

	@Override
	@Transactional
	public Response getRadiologyWorkbenchWorklist(RadiologyOrdersListDto radiologyOrdersListDto)
			throws ApplicationException {
		
		try {
			return iRISWorkbenchDao.getRadiologyWorkbenchWorklist(radiologyOrdersListDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getRadiologyWorkbenchWorklistPagination(RadiologyOrdersListDto radiologyOrdersListDto)
			throws ApplicationException {
		
		try {
			return iRISWorkbenchDao.getRadiologyWorkbenchWorklistPagination(radiologyOrdersListDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}
	/*@Override
	@Transactional
	public Response acceptRadiologyRequest(RadiologyOrdersListDto radiologyOrdersListDto) throws ApplicationException {
		try {

			Response res = iRISWorkbenchDao.acceptRadiologyRequest(radiologyOrdersListDto);
			Response res1 = iRISWorkbenchDao.saveRadiologyMapper(radiologyOrdersListDto);
			if (res.getStatus().equals(SUCCESS)) {

				if (radiologyOrdersListDto.getVisitTypeId() != 1
						|| (sessionService.getCookieObjectAppConfig().getIsPreOrPostBilling() == 'N'
								&& radiologyOrdersListDto.getVisitTypeId() == 1)) {
					List<Integer> orderDetailsId = new LinkedList<>();
					orderDetailsId.add(radiologyOrdersListDto.getOrderDetailsId());
					iOrderDetailsMasterService.renderAndPostRates(orderDetailsId);
				}

				if (radiologyOrdersListDto.getIsPACSRequired() == 'Y') {

					// get host and port number
//					Long currentDate=TimeZoneComponent.getDateByzone(unitId).getTime();
//					Response configRes=	iScheduleLogsDao.getConnectionStringForMyLife();

					
					 * MyLifeConnectionLinkDto dto =null; if(configRes.getObject()!=null){
					 * dto=(MyLifeConnectionLinkDto)configRes.getObject(); }
					 
					Header header = new Header();
					header.setMsh17_countrycode(radiologyOrdersListDto.getCountryCode());
					header.setMsh3_sendingapplication("HIS");
					header.setMsh12_versionid("1");
					header.setMsh13_sequencenumber("HIS001");
					header.setMsh4_sendingfacility("MPSHAH");
					header.setMsh5_receivingapplicaiton("MPSHAH");
					header.setMsh6_receivingfacility("TRS");

					header.setMsh7_datetime(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "yyyyMMddHHmmss"));
					header.setMsh11_processingid(radiologyOrdersListDto.getOrderNo());
					header.setMsh12_versionid("1");

					PID patientDemographics = new PID();

					patientDemographics.setPid2_patientid(radiologyOrdersListDto.getPatientId().toString());
					ArrayList<String> identifier = new ArrayList<>();
					identifier.add((radiologyOrdersListDto.getIdentificationNumber() != null
							&& radiologyOrdersListDto.getIdentificationNumber() != "")
									? radiologyOrdersListDto.getIdentificationNumber()
									: "123456");
					patientDemographics.setPid3_patientidentifier(identifier);
					patientDemographics.setPid5_patientname(radiologyOrdersListDto.getPatientName());

					// get patient Details by patient ID
					Response patientResponse = iPatientRegistrationService
							.getPatientDetailsById(radiologyOrdersListDto.getPatientId());
					if (patientResponse.getObject() != null) {
						PatientDetailsDto patientDtoRes = new PatientDetailsDto();
						patientDtoRes = (PatientDetailsDto) patientResponse.getObject();
						//System.out.println("patient Name=" + radiologyOrdersListDto.getPatientName());
						header.setMsh17_countrycode(
								patientDtoRes.getCountryCode() != null ? patientDtoRes.getCountryCode() : "IND");

						patientDemographics.setPid10_race(
								patientDtoRes.getRaceName() != null ? patientDtoRes.getRaceName() : "Asia");
						patientDemographics.setPid11_patientaddress(
								patientDtoRes.getAddress() != null ? patientDtoRes.getAddress() : "");
						patientDemographics.setPid16_maritalstatus(
								patientDtoRes.getMaritalStatus() != null ? patientDtoRes.getMaritalStatus() : "");
						patientDemographics.setPid28_nationality(
								patientDtoRes.getNationalityName() != null ? patientDtoRes.getNationalityName()
										: "Indiain");
						patientDemographics.setPid12_countrycode(
								patientDtoRes.getCountryCode() != null ? patientDtoRes.getCountryCode() : "IND");

						if (patientDtoRes.getDobPacs() != null && patientDtoRes.getDobPacs() != "") {
//							Long dobLong=GlobalCommonDateUtils.getLongDateFromStrigDate(patientDtoRes.getDobPacs());
							patientDemographics.setPid7_dateofbirth(patientDtoRes.getDobPacs());
						}

						patientDemographics.setPid8_administrativesex(
								patientDtoRes.getGenderCode() != null ? patientDtoRes.getGenderCode() : "M");

					}

					PV1 patientVisit = new PV1();

					patientVisit.setPv1_2_patientclass(radiologyOrdersListDto.getPatientCategory() != null
							? radiologyOrdersListDto.getPatientCategory()
							: "National");
					patientVisit.setPv1_19_visitnumber(
							radiologyOrdersListDto.getVisitNumber() != null ? radiologyOrdersListDto.getVisitNumber()
									: "");
					patientVisit.setPv1_4_admissiontype(
							radiologyOrdersListDto.getVisitName() != null ? radiologyOrdersListDto.getVisitName()
									: "OP");

					ORC orderDetails = new ORC();
					orderDetails.setOrc_1_ordercontrol("NW"); // for new order it shold be "NW"
					orderDetails.setOrc_2_placerordernumber(radiologyOrdersListDto.getOrderNo());
					orderDetails.setOrc_5_orderstatus("Renderd");
					orderDetails.setOrc_9_datetimeoftransaction(
							GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "yyyyMMddHHmmss"));
//					orderDetails.setOrc_17_enteringorganization("Organization Name="+sessionService.getCookieObjectDto().getOrgNme()+"Unit Name="+sessionService.getCookieObjectDto().getUnitNme() );

					RoomModalityMasterDto roomModalityDto = new RoomModalityMasterDto();
					roomModalityDto.setModalityId(radiologyOrdersListDto.getModalityId());
					roomModalityDto.setOrganizationId(sessionService.getOrganizationId());
					roomModalityDto.setUnitId(sessionService.getUnitId());
					if (radiologyOrdersListDto.getModalityId() != null) {
						Response modalityRes = iRoomModalityMasterDao.getModalityDetailsByModalityId(roomModalityDto);
						if (modalityRes.getListObject() != null && modalityRes.getListObject().size() > 0) {
							roomModalityDto = (RoomModalityMasterDto) modalityRes.getListObject().get(0);
						}
					}

					OBR observation = new OBR();
					observation.setObr_2_placerordernumber(radiologyOrdersListDto.getOrderNo());
					observation.setObr_3_fillerordernumber(radiologyOrdersListDto.getOrderNo());
					observation.setObr_5_priority(radiologyOrdersListDto.getPriorityId().toString());
					observation.setObr_4_universalserviceidentifier(radiologyOrdersListDto.getAccessionNumber() != null
							? radiologyOrdersListDto.getAccessionNumber()
							: "-");
					observation.setObr_5_priority("2");
					observation.setObr_6_requesteddatetime(
							GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "yyyyMMddHHmmss"));
					observation.setObr_12_dangercode("Green");
//					u observation.setObr_24_diagnosticservicesectorid(roomModalityDto.getModalityDesc()==null ? "CT" : roomModalityDto.getModalityDesc() +"@"+roomModalityDto.getRoomName()==null ? "AECT" : roomModalityDto.getRoomName()); //it should be modalityName@BedName ex. CT@AECT
//					 observation.setObr_24_diagnosticservicesectorid(roomModalityDto.getModalityDesc()+"@"+roomModalityDto.getRoomName());
					observation.setObr_24_diagnosticservicesectorid("CT@AECT");
					RadOrder radOrder = new RadOrder();
					radOrder.setHeader(header);
					radOrder.setObservation(observation);
					radOrder.setOrderDetails(orderDetails);
					radOrder.setPatientDemographics(patientDemographics);
					radOrder.setPatientVisit(patientVisit);

					// generating json

					String json = radOrder.GetRadOrderJson(radOrder);
					//System.out.println("PACS Json=" + json);

					// validating json
					String status = radOrder.validateRadOrder(json);

					if (status.equals("true")) {
						// send json
						Client client = new Client();
//						 if(dto.getPacsHost()!=null && dto.getPacsPort()!=null){
						//System.out.println("length=" + json.length());
						// sendtowebroot
//							    String response= client.SendString(json, dto.getPacsHost(), Integer.parseInt(dto.getPacsPort()));
						String response = client.SendtoWebhook(json, "WebReceive");
						//System.out.println("Response =" + response);
						
						 * String response= client.SendtoWebhook(json, "WebReceive");
						 * //System.out.println("Response ="+response);
						 
//						 }						

					} else {
						//System.out.println("do nothing");
					}
				} else {

				}

			}

//			return iEncounterMasterDao.acceptRadiologyRequest(radiologyOrdersListDto);
			return res;

		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	

	@Override
	@Transactional
	public Response rejectRadiologyRequest(RadiologyOrdersListDto radiologyOrdersListDto) throws ApplicationException {
		try {

			return iRISWorkbenchDao.saveRadiologyMapper(radiologyOrdersListDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}*/

	@Override
	@Transactional
	public Response getRadiologyResultEntrylist(RadiologyOrdersListDto radiologyOrdersListDto)
			throws ApplicationException {
		try {
			return iRISWorkbenchDao.getRadiologyResultEntrylist(radiologyOrdersListDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}
	

	@Override
	@Transactional
	public Response getradiologyReportReleaseWorkList(RadiologyOrdersListDto radiologyOrdersListDto)
			throws ApplicationException {
		try {
			return iRISWorkbenchDao.getradiologyReportReleaseWorkList(radiologyOrdersListDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getradiologyWorkListForPrinting(RadiologyOrdersListDto radiologyOrdersListDto)
			throws ApplicationException {
		try {
			return iRISWorkbenchDao.getradiologyWorkListForPrinting(radiologyOrdersListDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public ByteArrayOutputStream printRadiologyReport(RadiologyOrdersListDto dto) throws ApplicationException {
		try {
			return iRISWorkbenchDao.printRadiologyReport(dto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public ByteArrayOutputStream labPrintReport(RadiologyOrdersListDto dto) throws ApplicationException {
		try {
			return iRISWorkbenchDao.labPrintReport(dto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}
	
	@Override
	@Transactional
	public Response printReportTest(RadiologyOrdersListDto dto) throws ApplicationException {
		try {
			Response res= iRISWorkbenchDao.printReportTest(dto);

			/*if(res.getStatus().equals(SUCCESS) && dto.getIsFinalRelease() != null && dto.getIsFinalRelease() == 'Y' && res.getObject()!=null) {
			RadiologyOrdersListDto resDto = (RadiologyOrdersListDto) res.getObject();
			SmsUtilityConfigInfoDto emailinfo = new SmsUtilityConfigInfoDto();
			emailinfo.setUnitId(dto.getUnitId());
			emailinfo.setOrganizationId(dto.getOrganizationId());
			emailinfo.setPatientId(dto.getPatientId());
			emailinfo.setRisHeader(resDto.getHeader() != null ? resDto.getHeader() : "");
			emailinfo.setRisBody(resDto.getTemplateContent());
			emailinfo.setRisFooter(resDto.getMailFooter());
			emailinfo.setTemplateTransactionId(dto.getTemplateTransactionId());
			smsUtilityService.getEmailUtilityByTriggerId(ISmsUtilityConstants.RIS_REPORT, emailinfo);
			}*/

			return res;
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response printReportTestLab(RadiologyOrdersListDto dto) throws ApplicationException {
		try {
			return iRISWorkbenchDao.printReportTestLab(dto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response sendRadiologyReportOnMail(List<RadiologyOrdersListDto> list, String Url) throws Exception {
		try {
			//iMailingService.sentRegistrationMailToPatient(patientRegistrationDto);
			String pattern = "yyyy-MM-dd HH:mm:ss";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
			String date1 = simpleDateFormat.format(TimeZoneComponent.getDateByzone(list!=null && list.size()>0 ? list.get(0).getUnitId():null));
			com.param.global.common.Response res1 = null;
			
			Response orgRes = iGlobalOrganisationMasterDao
					.getOrganiazationFolderPath(list.get(0).getOrganizationId());
			OrganizationMasterDto organizationMasterDto = (OrganizationMasterDto) orgRes.getObject();

			for (RadiologyOrdersListDto dto : list) {
				SmsUtilityConfigInfoDto emailinfo = new SmsUtilityConfigInfoDto();
				emailinfo.setUnitId(dto.getUnitId());
				emailinfo.setOrganizationId(dto.getOrganizationId());
				emailinfo.setPatientId(dto.getPatientId());
				if(dto.getCreatedBy()!=null) {
					emailinfo.setEmployeeId(dto.getCreatedBy());
				}
				emailinfo.setTemplateTransactionIds(dto.getTemplateTransactionIds());
				if(dto.getDoctorId()!=null) {
					emailinfo.setConsultingDoctorId(dto.getDoctorId());
					emailinfo.setAdmitingDoctorId(dto.getDoctorId());
					emailinfo.setReferringDoctorId(dto.getDoctorId());
					emailinfo.setTreatingDoctorId(dto.getDoctorId());
				}
				emailinfo.setDateTime(date1);
				emailinfo.setTestDesc(dto.getTemplateDesc());
				emailinfo.setCompanyId(dto.getCompanyId()!=null ? dto.getCompanyId(): null);
				emailinfo.setInsuranceId(dto.getInsuranceId()!=null ? dto.getInsuranceId(): null);
				emailinfo.setTemplateTransactionId(dto.getTemplateTransactionId());
				
				
				String docPath = "";
				
				int lowerLimit = 97;
				int upperLimit = 122;
				Random random = new Random();
				StringBuffer r = new StringBuffer(10);
				for (int i = 0; i < 10; i++) {
					int nextRandomChar = lowerLimit
							+ (int) (random.nextFloat() * (upperLimit - lowerLimit + 1));
					r.append((char) nextRandomChar);
				}
				
				if(dto.getHtmlContent() != null) {
					try {
						byte[] pdfBytes = convertHtmlToPdf(dto.getHtmlContent());

						ByteArrayOutputStream outputStream = new ByteArrayOutputStream(pdfBytes.length);
						outputStream.write(pdfBytes, 0, pdfBytes.length);

						docPath = docUploadUtility.updateRISPdfFile(outputStream,
								organizationMasterDto.getOrganizationFolder(), r.toString(), dto.getOrganizationId(), dto.getUnitId());

						emailinfo.setRptOrHtml(0);//html
					}
					catch (IOException ex){
						ex.printStackTrace();
					}
					
				}else {
					emailinfo.setLabReportUrl(dto.getRadiologyReportUrl());
					emailinfo.setRptOrHtml(1);//rpt
				}

					emailinfo.setReportMsgUrl(docPath);
					emailinfo.setToMobileNumber(dto.getNewWhatsAppNo() != null ? dto.getNewWhatsAppNo() : "");
					smsUtilityService.getSmsUtilityByTriggerId(ISmsUtilityConstants.RIS_REPORT,emailinfo);
					emailinfo.setReportUrl(docPath);

				res1 = smsUtilityService.getEmailUtilityByTriggerId(ISmsUtilityConstants.RIS_REPORT, emailinfo);
			}
			return new Response<>(SUCCESS, null, (res1!=null && res1.getStatus().equals(SUCCESS) && res1.getMessage()!=null)?res1.getMessage(): null, null, null);
		} catch (Exception e) {
//			logger.error("Exception", e);
			throw e;
		}
	}

	private static String cleanHtml(String htmlContent) {
		org.jsoup.nodes.Document document = Jsoup.parse(htmlContent);
		document.outputSettings(new OutputSettings().syntax(OutputSettings.Syntax.xml));
		document.outputSettings().escapeMode(Entities.EscapeMode.xhtml);
		return document.html();
	}

	 public  byte[] convertHtmlToPdf(String htmlContent) throws Exception {
		 try {
			 String cleanedHtmlContent = cleanHtml(htmlContent);
			 ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             InputStream htmlInputStream = new ByteArrayInputStream(cleanedHtmlContent.getBytes());
			 HtmlConverter.convertToPdf(htmlInputStream, outputStream);
			 byte[] pdfBytes = outputStream.toByteArray();
			 return pdfBytes;
		 }
		 catch (Exception e) {
			 System.err.println("Error during HTML to PDF conversion: " + e.getMessage());
			 e.printStackTrace();
			 throw new IOException("Failed to convert HTML to PDF", e);
		 }
	 }

	
	@Override
	@Transactional
	public Response updateTemplateTransForRadiologyReportUrl(RadiologyOrdersListDto radiologyOrdersListDto, String str)
			throws ApplicationException, Exception {
		try {
			return iRISWorkbenchDao.updateTemplateTransForRadiologyReportUrl(radiologyOrdersListDto, str);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}
	
    @Override
    @Transactional
    public Response getOrganiazationFolderPath(Integer organizationId) throws ApplicationException, Exception {
        try {
            return iGlobalOrganisationMasterDao.getOrganiazationFolderPath(organizationId);
        } catch (Exception e) {
            throw e;
        }
    }


    @Override
    @Transactional
    public Response updateRISPdfPathByTemplateTransactionId(String docPath,
    		TemplateTransactionDto dto) throws ApplicationException, Exception {
        try {
            return iGlobalOrganisationMasterDao.updateRISPdfPathByTemplateTransactionId(docPath, dto);
        } catch (Exception e) {
            throw e;
        }
    }

	@Override
	 @Transactional
	public Response getConnectionStringForMyLife() throws ApplicationException, Exception {
		 try {
	            return iScheduleLogsDao.getConnectionStringForMyLife();
	        } catch (Exception e) {
	            throw e;
	        }
	    }

	@Override
	@Transactional
	public Response getMyLifeDetail(Integer id) throws ApplicationException, Exception {
		try {
            return iScheduleLogsDao.getMyLifeDetail(id);
        } catch (Exception e) {
            throw e;
        }
    }

	@Override
	@Transactional
	public Response checkDueAmount(RadiologyOrdersListDto orderDto) throws ApplicationException, Exception {
		try {

			Boolean reportSend = true;
			Integer count = 0;
			if(orderDto.getPaymentEntitlementId() != null && (orderDto.getPaymentEntitlementId() == INSURANCE || orderDto.getPaymentEntitlementId() == COMPANY )) {

				Response respayee = iRISWorkbenchDao.getPayee(orderDto);


				if(respayee.getObject()!= null && !respayee.getObject().equals(count)) {
					Response res = iRISWorkbenchDao.checkOutStandingAmount(orderDto.getEncounterId());
					if(res.getObject() != null && res.getObject().equals(count)) {
						reportSend= true;
					}else {
						reportSend= false;
					}
				}


			}else {

				if(orderDto.getBlockReportPrint() != null && orderDto.getBlockReportPrint() =='Y') {
					Response res = iRISWorkbenchDao.checkOutStandingAmount(orderDto.getEncounterId());
					if(res.getObject() != null && res.getObject().equals(count)) {
						reportSend= true;
					}else {
						reportSend= false;
					}
				}
			}

			return new Response(SUCCESS, SUCCESS_200, null, null, reportSend);
		} catch (Exception e) {

			throw e;
		}
	}


}
