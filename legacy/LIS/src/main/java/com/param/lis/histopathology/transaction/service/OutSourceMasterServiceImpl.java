package com.param.lis.histopathology.transaction.service;


import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.param.entity.lis.micro.MicrobioResultEntryMaster;
import com.param.entity.lis.transaction.LabSampleDetailsMaster;
import com.param.global.common.ISmsUtilityConstants;
import com.param.global.common.ServiceException;
import com.param.global.mail.IMailingService;
import com.param.global.opd.dao.IEncounterMasterDao;
import com.param.global.unit.dto.SmsUtilityConfigInfoDto;
import com.param.global.unit.service.ISmsUtilityConfigService;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.ITransactionConstants;
import com.param.lis.global.common.Response;
import com.param.lis.global.common.SearchDto;
import com.param.lis.histopathology.transaction.dao.IOutSourceMasterDao;
import com.param.lis.histopathology.transaction.dto.OutSourceMasterDto;
import com.param.lis.microbiology.transaction.service.IMicrobiologyService;
import com.param.lis.transaction.dto.LabDashBoardDto;
import com.param.lis.transaction.dto.LabResultMasterDto;
import com.param.lis.transaction.dto.LabSampleDetailsMasterDto;
import com.param.lis.transaction.dto.SearchCommonDto;
import com.param.opd.encounter.dto.PatientDocDto;
import com.param.opd.encounter.dto.PatientDocReqDto;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings(
{ "rawtypes", "unchecked" })
public class OutSourceMasterServiceImpl  implements IOutSourceMasterService, ICommonConstants,ITransactionConstants ,IError
{
	
	final static Logger logger = Logger.getLogger(OutSourceMasterServiceImpl.class);
	
	@Autowired
	IOutSourceMasterDao iOutSourceMasterDao;
	
	  @Autowired
	  IMicrobiologyService iMicrobiologyService;

	  @Autowired
		IEncounterMasterDao iEncounterMasterDao;
	  
	  @Autowired 
		IMailingService iMailingService;
	  
	  @Autowired
	  private ISmsUtilityConfigService smsUtilityService;
	  
	  @Autowired
	  private SessionFactory SessionFactory;
	
	@Override
	@Transactional
	public Response listOutSourceMaster(OutSourceMasterDto outSourceMasterDto) throws ApplicationException 
	{
		try
		{
			return iOutSourceMasterDao.listOutSourceMaster(outSourceMasterDto);
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getOutSourceMasterCount(OutSourceMasterDto outSourceMasterDto) throws ApplicationException 
	{
		try
		{
			return iOutSourceMasterDao.getOutSourceMasterCount(outSourceMasterDto);
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response saveOutSourceMasterDetails(List<OutSourceMasterDto> listOutSourceMasterDto) throws ApplicationException 
	{
		
		try
		{
		  if(!listOutSourceMasterDto.isEmpty())
		  {
			  
			Date dt= TimeZoneComponent.getDateByzone(listOutSourceMasterDto.get(0).getOrgUnitId());
			  Response<LabSampleDetailsMaster, Integer> sampleStatusRes =null;
			  for (Iterator iterator =listOutSourceMasterDto.iterator(); iterator.hasNext();) 
			  {
				 
				  OutSourceMasterDto outSourceMasterDto = (OutSourceMasterDto) iterator.next();
				  Response<MicrobioResultEntryMaster, Integer> 
				  microResultRes =  iOutSourceMasterDao.saveOutSourceMasterDetails(outSourceMasterDto);
				  if(microResultRes.getCode().equals(SUCCESS_200))
					 {
						LabSampleDetailsMasterDto labSampleDetailsMasterDto 
						= new LabSampleDetailsMasterDto();
						labSampleDetailsMasterDto.setPerformedUnit(outSourceMasterDto.getOrgUnitId());
						labSampleDetailsMasterDto.setOrgUnitId(outSourceMasterDto.getOrgUnitId());
						labSampleDetailsMasterDto.setAssignUnit(outSourceMasterDto.getOrgUnitId());
						labSampleDetailsMasterDto.setCreatedBy(outSourceMasterDto.getCreatedBy());
						//labSampleDetailsMasterDto.setSampleStatusId(OUT_SOURCE_SAMPLE);
						
                        if(outSourceMasterDto.getSampleStatus().equals(1)) {
                            labSampleDetailsMasterDto.setSampleStatusId(OUT_SOURCE_SAMPLE);
                        }else if(outSourceMasterDto.getSampleStatus().equals(3)) {
                            labSampleDetailsMasterDto.setSampleStatusId(OUT_SOURCE_REPORT_RELEASE);
                        }

						labSampleDetailsMasterDto.setCreatedDate(dt);
						labSampleDetailsMasterDto.setLabSampleDtlsId(outSourceMasterDto.getLabSampleDtlsId());
						sampleStatusRes = iMicrobiologyService
								.updateLabSampleDetailsMaster(labSampleDetailsMasterDto);
						  if(sampleStatusRes.getCode().equals(SUCCESS_200))
							{
								//return new Response(SUCCESS, SUCCESS_200, "Out Source Details Added 								Successfully.", null, null);
							}
						  PatientDocReqDto patientDocReqDto = new PatientDocReqDto();
						  patientDocReqDto.setLabSampleDtlsId(outSourceMasterDto.getLabSampleDtlsId());
						  patientDocReqDto.setOrganisationId(outSourceMasterDto.getOrgId());
						  patientDocReqDto.setUnitId(outSourceMasterDto.getOrgUnitId());
						  patientDocReqDto.setPatientId(outSourceMasterDto.getPatientId());
						  String mailData=(outSourceMasterDto.getTextData()!= null ? outSourceMasterDto.getTextData() : " ");
						  
						  com.param.global.common.Response res = iEncounterMasterDao.getPatientDocDetails(patientDocReqDto);
						  if(res.getListObject()!= null && res.getListObject().size()>0) {
							  List<PatientDocDto> docList = new ArrayList<PatientDocDto>();
							  docList.addAll(res.getListObject());
							  
							  for (PatientDocDto patientDocDto : docList) {
								  
								  mailData = mailData+ System.lineSeparator()+ "\n<a href='"+patientDocDto.getDocumentPath()+"'>"+patientDocDto.getDocumentPath()+"</a>\n"+ System.lineSeparator();
							  }
							  if(outSourceMasterDto.getPatientEmail()!= null && mailData != null ) {
								  iMailingService.sendOutSourceMail(outSourceMasterDto.getPatientEmail(),mailData);

							  }
						  }
					 }
				 
			    }
			
			
			  return new Response(SUCCESS, SUCCESS_200, "Out Source Details Added Successfully.", null,  null);
		  }
		  else 
		  {
			  return new Response(ERROR, ERR_500, "Failed to Added Out Source Details.", null, null);
		  }
			
		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}
	
	
	}
	
	 @Override
	 @Transactional
	  public String uploadDocumentData(MultipartFile inputFile) {
	    try {
	      String templateData = null;
	      if(!inputFile.isEmpty())
	      {
	        File destinationFile = new File(inputFile.getOriginalFilename());
	        FileInputStream fis = new FileInputStream(destinationFile.getPath());
	        XWPFDocument xdoc = new XWPFDocument(OPCPackage.open(fis));
	        XWPFWordExtractor extractor = new XWPFWordExtractor(xdoc);
	        templateData =  extractor.getText();
	        extractor.close();
	        return templateData;
	      }
	      else {
	        return templateData;
	      }
	    } catch (Exception e)
	    {
	        logger.error("Exception uploading document data", e);
	        throw new ServiceException(e.getMessage());
	    }
	  }

	@Override
	@Transactional
	public Response searchOutSourcelistPatient(SearchCommonDto searchCommonDto) throws ApplicationException {
		try
		{
			return iOutSourceMasterDao.searchOutSourcelistPatient(searchCommonDto);
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getFilteredOutSourceList(SearchDto searchDto) throws ApplicationException {
		try
		{
			return iOutSourceMasterDao.getFilteredOutSourceList(searchDto);
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getFilteredOutSourceCommonList(SearchDto searchDto) throws ApplicationException {
		try
		{
			return iOutSourceMasterDao.getFilteredOutSourceCommonList(searchDto);
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response<SearchDto, Integer> sendOutsourceReportOnMail(List<SearchDto> list)
			throws ApplicationException {
		try {
			//iMailingService.sentRegistrationMailToPatient(patientRegistrationDto);
			String pattern = "yyyy-MM-dd HH:mm:ss";
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
			String date1 = simpleDateFormat.format(TimeZoneComponent.getDateByzone(list!=null && list.size()>0 ? list.get(0).getOrgUnitId():null));
			com.param.global.common.Response res1 = null;
			for (SearchDto dto : list) {
				SmsUtilityConfigInfoDto emailinfo = new SmsUtilityConfigInfoDto();
				emailinfo.setUnitId(dto.getOrgUnitId());
				emailinfo.setOrganizationId(dto.getOrgId());
				emailinfo.setPatientId(dto.getPatientId());
				if(dto.getCreatedBy()!=null) {
					
					Integer employeeId = (Integer) SessionFactory.getCurrentSession().createSQLQuery(
							"select em.employee_id  as \"employeeId\" from public.m_employee_master em where em.user_id =:userId")
							.setParameter("userId", dto.getCreatedBy()).uniqueResult();
				emailinfo.setEmployeeId(employeeId); 
				}
				emailinfo.setLabSampleDtlsIdsList(dto.getLabSampleDtlsIds());
				if(dto.getDoctorId()!=null) {
					emailinfo.setConsultingDoctorId(dto.getDoctorId());
					emailinfo.setAdmitingDoctorId(dto.getDoctorId());
					emailinfo.setReferringDoctorId(dto.getDoctorId());
					emailinfo.setTreatingDoctorId(dto.getDoctorId());
				}
				emailinfo.setDateTime(date1);
				emailinfo.setTestDesc(dto.getTestDesc());
				emailinfo.setCompanyId(dto.getCompanyId()!=null ? dto.getCompanyId(): null);
				emailinfo.setInsuranceId(dto.getInsuranceId()!=null ? dto.getInsuranceId(): null);
//				emailinfo.setRptFileName("Multireportprint.rpt");
				String urlString="";
				String urlStringMsg="";
				Response res2= iOutSourceMasterDao.getOutsourceReportUrl(emailinfo.getLabSampleDtlsIdsList(), dto.getOrgUnitId(), dto.getOrgId());
				if(res2.getListObject() != null && res2.getListObject().size() > 0) {
					
					List<SmsUtilityConfigInfoDto> litUrls=(List<SmsUtilityConfigInfoDto>)res2.getListObject();	
				for (SmsUtilityConfigInfoDto listDto : litUrls) {
					String s1=listDto.getDocumentPath();
					String part1 = s1.replace("https://s3.ap-south-1.amazonaws.com/his-docs/", "");
//					String part2 = part1.replace("patient/documents/clinical/episodic-documents/op-coversheet/","");
					String s="<a href='"+listDto.getDocumentPath()+"'>"+part1+" <a></br></br>";
					urlString +=s;
					String sms=listDto.getDocumentPath();
//					sms=sms.replaceAll("\\s+",System.getProperty("line.separator"));
					urlStringMsg =sms;
//					urlStringMsg +=listDto.getDocumentPath();
//					 String newline = System.lineSeparator();
//					urlString+=listDto.getDocumentPath()+"/n";
					
					emailinfo.setReportMsgUrl(urlStringMsg);
					emailinfo.setToMobileNumber(dto.getNewWhatsAppNo() != null && !dto.getNewWhatsAppNo().trim().isEmpty() ? dto.getNewWhatsAppNo() : null);
					smsUtilityService.getSmsUtilityByTriggerId(ISmsUtilityConstants.OUTSOURCE_REPORT,emailinfo);
				}
				emailinfo.setReportUrl(urlString);
				
				}else {
					emailinfo.setRptFileName("Multireportprint.rpt");
				}

				if(dto.getSendOnlyWhatsApp() == null || !dto.getSendOnlyWhatsApp()){
					res1 = smsUtilityService.getEmailUtilityByTriggerId(ISmsUtilityConstants.OUTSOURCE_REPORT, emailinfo);
				}
			}
			return new Response<>(SUCCESS, null, (res1!=null && res1.getStatus().equals(SUCCESS) && res1.getMessage()!=null)?res1.getMessage(): null, null, null);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	}




	

}
