package com.param.lis.transaction.service;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.entity.lis.transaction.LabResultMaster;
import com.param.entity.lis.transaction.LabSampleDetailsMaster;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.SmsUtility;
import com.param.global.dao.IUnitApplicationBillingConfigurationDao;
import com.param.global.dto.OrderMasterDto;
import com.param.global.dto.TemplateTransactionDto;
import com.param.global.dto.UnitApplicationConfigurationDto;
import com.param.global.opd.service.IEncounterMasterService;
import com.param.global.org.dao.IGlobalOrganisationMasterDao;
import com.param.global.org.service.IGlobalUnitMasterService;
import com.param.global.service.IOrderMasterService;
import com.param.lis.global.common.CheckReportType;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.ITransactionConstants;
import com.param.lis.global.common.Response;
import com.param.lis.global.common.SearchDto;
import com.param.lis.reports.service.IBiochemReportService;
import com.param.lis.reports.service.SendReportToMail;
import com.param.lis.transaction.dao.IBioChemResultEntryDao;
import com.param.lis.transaction.dao.IBioChemistryDao;
import com.param.lis.transaction.dao.IBiochemReportReleaseDao;
import com.param.lis.transaction.dto.BioChemParamDto;
import com.param.lis.transaction.dto.LabResultDetailsViewDto;
import com.param.lis.transaction.dto.LabResultMasterDto;
import com.param.lis.transaction.dto.LabSampleDetailsMasterDto;
import com.param.lis.transaction.dto.SearchCommonDto;
import com.param.lis.transaction.dto.SingParamTestResDto;
import com.param.lis.unit.dao.IParameterMasterDao;
import com.param.lis.unit.dto.HelpValueMasterDto;
import com.param.lis.unit.dto.TPanelMasterDto;
import com.param.lis.unit.dto.TestMasterDto;
import com.param.lis.unit.service.IPanelTestService;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({ "rawtypes", "unchecked","unused" })
public class BiochemReportReleaseServiceImpl implements IBiochemReportReleaseService, ICommonConstants, ITransactionConstants, IError {

	@Autowired
	IBiochemReportReleaseDao iBiochemReportReleaseDao;

	@Autowired
	IBioChemResultEntryDao iBioChemResultEntryDao;

	@Autowired
	IParameterMasterDao iParameterMasterDao;

	@Autowired
	IBioChemistryDao iBioChemistryDao;

	@Autowired
	IBiochemReportService iBiochemReportService;

	@Autowired
	IGlobalUnitMasterService iGlobalUnitMasterService;

	@Autowired
	IUnitApplicationBillingConfigurationDao iUnitApplicationBillingConfigurationDao;

	@Autowired
	SmsUtility smsUtility;

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	IOrderMasterService iOrderMasterService;

	@Autowired
	private IPanelTestService iPanelTestService;
	
	
	@Autowired
	private IEncounterMasterService iEncounterMasterService;
	
	@Autowired
	IGlobalOrganisationMasterDao iGlobalOrganisationMasterDao;

	final static Logger logger = Logger.getLogger(BiochemReportReleaseServiceImpl.class);

	@Override
	@Transactional
	public Response getReportReleaseWorkList(BioChemParamDto bioChemParamDto) throws ApplicationException {
		try {
			return iBiochemReportReleaseDao.getReportReleaseWorkList(bioChemParamDto);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getReportReleaseWorkListCount(BioChemParamDto bioChemParamDto) throws ApplicationException {
		try {
			return iBiochemReportReleaseDao.getReportReleaseWorkListCount(bioChemParamDto);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getReportReleaseWorkListDetails(List<BioChemParamDto> bioChemParamDtoList) throws ApplicationException {
		try {
			
			
			
		  List<SingParamTestResDto> testMasterDtoList = new ArrayList<>();
          for (Iterator iterator = bioChemParamDtoList.iterator(); iterator.hasNext();) 
          {
            BioChemParamDto bioChemParamDto = (BioChemParamDto) iterator.next();
            Response<SingParamTestResDto, Integer> resultEnterDateRes =  iBioChemistryDao.getResultValidatedByDateTime(bioChemParamDto);
            
			Response<TestMasterDto, Integer> response = null;
			if (bioChemParamDto.getTestType().equals(SINGLE_PARAM_TEST)) {
				SingParamTestResDto singParamTestResDto = new SingParamTestResDto();
				

                Integer panelId = bioChemParamDto.getPanelId()==null?0:bioChemParamDto.getPanelId();
                Response<TPanelMasterDto, Integer> panelMstRes =  iPanelTestService.getPanelDescByPanelId(panelId, bioChemParamDto.getOrgId(), bioChemParamDto.getAssingnedFrom());

				response = iBioChemResultEntryDao.getSingleParamTestResult(bioChemParamDto);
				TestMasterDto testMasterDto = response.getObject();
				singParamTestResDto.setTestId(testMasterDto.getTestId());
				singParamTestResDto.setTestType(testMasterDto.getTestType());
				singParamTestResDto.setTestDesc(testMasterDto.getTestDesc());
				singParamTestResDto.setTestCode(testMasterDto.getTestCode());
				singParamTestResDto.setTechniqueName(testMasterDto.getTechniqueName());
				singParamTestResDto.setLabSampleNo(bioChemParamDto.getLabSampleNo());
				singParamTestResDto.setProRelease(testMasterDto.getProRelease());
                singParamTestResDto.setPanelAlies( panelMstRes.getObject()!=null?panelMstRes.getObject().getPanelAlies():"");
                singParamTestResDto.setPanelCode(panelMstRes.getObject()!=null?panelMstRes.getObject().getPanelCode():"");
                singParamTestResDto.setResultEnterByUser(resultEnterDateRes.getObject().getResultEnterByUser());
                singParamTestResDto.setResultEnterDatetime(resultEnterDateRes.getObject().getResultEnterDatetime());
                singParamTestResDto.setResultValidatedByUser(resultEnterDateRes.getObject().getResultValidatedByUser());
                singParamTestResDto.setResultValidatedDatetime(resultEnterDateRes.getObject().getResultValidatedDatetime());
                singParamTestResDto.setFootsNotes(resultEnterDateRes.getObject().getFootsNotes());
                singParamTestResDto.setInternalNotes(resultEnterDateRes.getObject().getInternalNotes());
                singParamTestResDto.setIsOnHoldParameter(resultEnterDateRes.getObject().getIsOnHoldParameter());
                singParamTestResDto.setSrfId(resultEnterDateRes.getObject().getSrfId());
                singParamTestResDto.setIcmrId(resultEnterDateRes.getObject().getIcmrId());
                singParamTestResDto.setLabSampleDtlsId(bioChemParamDto.getLabSampleDtlsId());
                singParamTestResDto.setMachineId(resultEnterDateRes.getObject().getMachineId()!=null ? resultEnterDateRes.getObject().getMachineId(): null );
                
				Response<LabResultDetailsViewDto, Integer> labResDetails = iBiochemReportReleaseDao
						.getReportReleaseWorkListDetails(bioChemParamDto);
				
				LabResultMaster labResultMaste=	(LabResultMaster) sessionFactory.getCurrentSession().get(LabResultMaster.class, bioChemParamDto.getLabResultId());
				if(labResultMaste.getResultEnterDatetime()!=null)
				singParamTestResDto.setSampleTestTime(new Date(labResultMaste.getResultEnterDatetime()));
				
				if(labResultMaste.getResultAuthorisedDatetime()!=null)
				singParamTestResDto.setTestReleaseTime(new Date(labResultMaste.getResultAuthorisedDatetime()));
				
				
				LabSampleDetailsMaster labSampleDetailsMaster=	(LabSampleDetailsMaster) sessionFactory.getCurrentSession().get(LabSampleDetailsMaster.class, labResultMaste.getLabSampleDtlsId());
				if(labSampleDetailsMaster.getSampleCollectionDatetime()!=null)
				singParamTestResDto.setSampleCollectionTime(new Date(labSampleDetailsMaster.getSampleCollectionDatetime()));
				singParamTestResDto.setTestId(labSampleDetailsMaster.getTestId());
				singParamTestResDto.setAssignUnit(labSampleDetailsMaster.getOrgUnitId());
				singParamTestResDto.setAssingnedFrom((labSampleDetailsMaster.getOrgUnitId()));
				singParamTestResDto.setPerformTestId(labSampleDetailsMaster.getPerformTestId());
				
				if (labResDetails.getListObject() != null) 
				{
					for (Iterator iterator1 = labResDetails.getListObject().iterator(); iterator1.hasNext();) 
					{
						LabResultDetailsViewDto labResultDetailsViewDto = (LabResultDetailsViewDto) iterator1.next();
						List<HelpValueMasterDto> listHelpValueMasterDto = (List<HelpValueMasterDto>) iParameterMasterDao
								.getHelpValueByParameter(labResultDetailsViewDto.getOrgId(),
										bioChemParamDto.getAssingnedFrom(),
										labResultDetailsViewDto.getParameterId()).getListObject();
						
						labResultDetailsViewDto.setListHelpValueMaster(listHelpValueMasterDto);
						singParamTestResDto.setLabResultDetailsViewDto(labResultDetailsViewDto);
					}
				}

                testMasterDtoList.add(singParamTestResDto);

			} else if (bioChemParamDto.getTestType().equals(MULTY_PARAM_TEST)) 
			{
				
					SingParamTestResDto singParamTestResDto = new SingParamTestResDto();
					Response testRes = iBioChemResultEntryDao.getMultyParameterTestDetails(bioChemParamDto);
					if (testRes.getObject() != null) {
						singParamTestResDto = (SingParamTestResDto) testRes.getObject();
					}
				
			   /* Integer panelId = bioChemParamDto.getPanelId()==null?0:bioChemParamDto.getPanelId();
	            Response<TPanelMasterDto, Integer> panelMstRes =  iPanelTestService.getPanelDescByPanelId(panelId, bioChemParamDto.getOrgId(), bioChemParamDto.getAssingnedFrom());
			  
			  response = iBioChemResultEntryDao.getMultyParamTestResult(bioChemParamDto);
              TestMasterDto testMasterDto = response.getObject();
              SingParamTestResDto singParamTestResDto = new SingParamTestResDto();
              List<MultyParamTestHeaderDto> listMultyParamTestHeaderDto = new ArrayList<>();

              singParamTestResDto.setTestId(testMasterDto.getTestId());
              singParamTestResDto.setTestType(testMasterDto.getTestType());
              singParamTestResDto.setTestDesc(testMasterDto.getTestDesc());
              singParamTestResDto.setTestCode(testMasterDto.getTestCode());
              singParamTestResDto.setTechniqueName(testMasterDto.getTechniqueName());
              singParamTestResDto.setProRelease(testMasterDto.getProRelease());
              singParamTestResDto.setLabSampleNo(bioChemParamDto.getLabSampleNo());
              singParamTestResDto.setPanelAlies(panelMstRes.getObject()!=null?panelMstRes.getObject().getPanelAlies():"");
              singParamTestResDto.setPanelCode(panelMstRes.getObject()!=null?panelMstRes.getObject().getPanelCode():"");
              singParamTestResDto.setResultEnterByUser(resultEnterDateRes.getObject().getResultEnterByUser());
              singParamTestResDto.setResultEnterDatetime(resultEnterDateRes.getObject().getResultEnterDatetime());
              singParamTestResDto.setResultValidatedByUser(resultEnterDateRes.getObject().getResultValidatedByUser());
              singParamTestResDto.setResultValidatedDatetime(resultEnterDateRes.getObject().getResultValidatedDatetime());
              singParamTestResDto.setLabSampleDtlsId(bioChemParamDto.getLabSampleDtlsId());

				// Group By HeaderId
				Map<Integer, List<TestParamMpprDto>> testParamMpprDto = testMasterDto.getListTestParamMppr().stream()
						.collect(Collectors.groupingBy(TestParamMpprDto::getHeaderId));

				for (Map.Entry<Integer, List<TestParamMpprDto>> entry : testParamMpprDto.entrySet()) {
					MultyParamTestHeaderDto multyParamTestHeaderDto = new MultyParamTestHeaderDto();
					for (Iterator iterator2 = entry.getValue().iterator(); iterator2.hasNext();) {
						List<LabResultDetailsViewDto> listLabResultDetailsViewDto = new ArrayList<>();
						TestParamMpprDto tstParamMpprDto = (TestParamMpprDto) iterator2.next();
						multyParamTestHeaderDto.setHeaderId(tstParamMpprDto.getHeaderId());
						multyParamTestHeaderDto.setHeaderCode(tstParamMpprDto.getHeaderDesc());
						multyParamTestHeaderDto.setHeaderDesc(tstParamMpprDto.getHeaderDesc());

						bioChemParamDto.setHeaderId(multyParamTestHeaderDto.getHeaderId());
						Response<LabResultDetailsViewDto, Integer> labResDetails = iBiochemReportReleaseDao
								.getReportReleaseWorkListDetails(bioChemParamDto);
						
						LabResultMaster labResultMaste=	(LabResultMaster) sessionFactory.getCurrentSession().get(LabResultMaster.class, bioChemParamDto.getLabResultId());
						if(labResultMaste.getResultEnterDatetime()!=null)
						  singParamTestResDto.setSampleTestTime(new Date(labResultMaste.getResultEnterDatetime()));
						if(labResultMaste.getResultAuthorisedDatetime()!=null)
						  singParamTestResDto.setTestReleaseTime(new Date(labResultMaste.getResultAuthorisedDatetime()));
						
						
						LabSampleDetailsMaster labSampleDetailsMaster=	(LabSampleDetailsMaster) sessionFactory.getCurrentSession().get(LabSampleDetailsMaster.class, labResultMaste.getLabSampleDtlsId());
						if(labSampleDetailsMaster.getSampleCollectionDatetime()!=null)
						  singParamTestResDto.setSampleCollectionTime(new Date(labSampleDetailsMaster.getSampleCollectionDatetime()));

						if (labResDetails.getListObject() != null) {
							for (Iterator iterator1 = labResDetails.getListObject().iterator(); iterator1.hasNext();) {
								LabResultDetailsViewDto labResultDetailsViewDto = (LabResultDetailsViewDto) iterator1
										.next();
								List<HelpValueMasterDto> listHelpValueMasterDto = (List<HelpValueMasterDto>) iParameterMasterDao
										.getHelpValueByParameter(labResultDetailsViewDto.getOrgId(),
												bioChemParamDto.getAssingnedFrom(),
												labResultDetailsViewDto.getParameterId())
										        .getListObject();
								labResultDetailsViewDto.setListHelpValueMaster(listHelpValueMasterDto);
								
								if(labResultDetailsViewDto.getIsFormula()=='Y')
	                              {
	                                   Response<FormulaDetailsDto, Integer> formulaResponse =   iFormulaMasterService.getFormulaByParameter(labResultDetailsViewDto.getParameterId(), bioChemParamDto.getOrgId(), bioChemParamDto.getAssingnedFrom());
	                                   if(formulaResponse.getCode().equals(SUCCESS_200)) {
	                                     labResultDetailsViewDto.setParameterFormula(formulaResponse.getListObject().get(0).getFormula());
	                                     labResultDetailsViewDto.setListFormulaDetails(formulaResponse.getListObject());
	                                   }
	                                   else {
	                                     return new Response(ERROR, ERR_500, "Formula Not Found..", null, null);
	                                   }
	                              }
								
								
								listLabResultDetailsViewDto.add(labResultDetailsViewDto);
							}

						}
						multyParamTestHeaderDto.setListLabResultDetailsViewDto(listLabResultDetailsViewDto);
					}
				      listMultyParamTestHeaderDto.add(multyParamTestHeaderDto);
				}
				  singParamTestResDto.setListMultyParamTestHeaderDto(listMultyParamTestHeaderDto);*/
	              testMasterDtoList.add(singParamTestResDto);
			}
          }
			return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, testMasterDtoList, null);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response saveReportReleaseWorkList(List<LabResultMasterDto> listLabResultMasterDto, String contextPath) throws Exception {
		try {
			
			
			
			//Code for Send Sms
			String senderId = null;
			UnitApplicationConfigurationDto unitApplicationConfigurationDto = new UnitApplicationConfigurationDto();
			unitApplicationConfigurationDto.setOrganizationId(listLabResultMasterDto != null && !listLabResultMasterDto.isEmpty() ? listLabResultMasterDto.get(0).getOrgId() : 0);
			unitApplicationConfigurationDto.setUnitId(listLabResultMasterDto != null && !listLabResultMasterDto.isEmpty() ? listLabResultMasterDto.get(0).getOrgUnitId() : 0);
			com.param.global.common.Response  appConfigRes = iUnitApplicationBillingConfigurationDao.getUnitApplicationBillingConfigByOrgUnit(unitApplicationConfigurationDto);
		    UnitApplicationConfigurationDto appDto = (UnitApplicationConfigurationDto)appConfigRes.getObject();
			if(appDto != null){
				senderId = appDto.getSenderId();
			}
			else{
				return new Response(SUCCESS, SUCCESS_200, "Unit application configuraion not yet defined.!!", null, null);
			}
			
			Date date = TimeZoneComponent.getDateByzone(listLabResultMasterDto!=null && listLabResultMasterDto.size()>0 
					?listLabResultMasterDto.get(0).getOrgUnitId() : null);
			List<LabResultMasterDto> newResLsit = new ArrayList<>();
		  for (Iterator iterator = listLabResultMasterDto.iterator(); iterator.hasNext();) 
		  {
            LabResultMasterDto labResultMasterDto = (LabResultMasterDto) iterator.next();
            Set<Integer> reportType = new HashSet<>();
            if (labResultMasterDto == null) {
                logger.info("INFO : labResultMasterDto is NULL");
                return new Response(SUCCESS, SUCCESS, ERR_500_MESSAGE, null, null);
            } else {
                
                labResultMasterDto.setUpdatedDate(date.getTime());
                labResultMasterDto.setUpdatedBy(labResultMasterDto.getCreatedBy());
                labResultMasterDto.setResultAuthorisedBy((labResultMasterDto.getResultAuthorisedBy()!=null && labResultMasterDto.getResultAuthorisedBy()>0)?labResultMasterDto.getResultAuthorisedBy():labResultMasterDto.getCreatedBy());
                labResultMasterDto.setResultAuthorisedDatetime(date);
                labResultMasterDto.setResultAuthorisedFlag('Y');
                for (Iterator iterator1 = labResultMasterDto.getListLabSampleResultDetailsMaster().iterator(); iterator1
                        .hasNext();) {
                    LabResultDetailsViewDto labResultDetailsViewDto = (LabResultDetailsViewDto) iterator1.next();
                    labResultDetailsViewDto.setOrgId(labResultMasterDto.getOrgId());
                    labResultDetailsViewDto.setUpdatedBy(labResultMasterDto.getCreatedBy());
                    labResultDetailsViewDto.setUpdatedDate(date.getTime());
                    CheckReportType.reportType(labResultDetailsViewDto);
//                    reportType.add(labResultDetailsViewDto.getReportType());
                    if(labResultDetailsViewDto.getReportType() !=null) {
                    	  reportType.add(labResultDetailsViewDto.getReportType());
                    }
                }
                labResultMasterDto.setReportType(Collections.max(reportType));
              }
            Response reportRelease = iBiochemReportReleaseDao.saveReportReleaseWorkList(labResultMasterDto);
             if(reportRelease.getCode().equals(SUCCESS_200))
             {
            	 BioChemParamDto bioChemParamDto = new BioChemParamDto();
					bioChemParamDto.setLabResultId(labResultMasterDto.getLabTestResId() != null ? labResultMasterDto.getLabTestResId() : 0);
					bioChemParamDto.setDeptId(labResultMasterDto.getSubDeptId() != null ? labResultMasterDto.getSubDeptId() : 0);
					bioChemParamDto.setOrgId(labResultMasterDto.getOrgId() != null ? labResultMasterDto.getOrgId() : 0);
					bioChemParamDto.setOrgUnitId(labResultMasterDto.getOrgUnitId() != null ? labResultMasterDto.getOrgUnitId() : 0);
					bioChemParamDto.setAssingnedFrom(labResultMasterDto.getAssingnedFrom() != null ? labResultMasterDto.getAssingnedFrom() : 0);
					bioChemParamDto.setTestId(labResultMasterDto.getTestId() != null ? labResultMasterDto.getTestId(): 0);
					bioChemParamDto.setTestType(labResultMasterDto.getTestType() != null ? labResultMasterDto.getTestType() : 0);
					bioChemParamDto.setLabSampleDtlsId(labResultMasterDto.getLabSampleDtlsId() != null ? labResultMasterDto.getLabSampleDtlsId() : 0);
					
					LabSampleDetailsMasterDto labSampleDetailsMasterDto = new LabSampleDetailsMasterDto();
					labSampleDetailsMasterDto.setLabSampleDtlsId(labResultMasterDto.getLabSampleDtlsId() != null ? labResultMasterDto.getLabSampleDtlsId() : 0);
					labSampleDetailsMasterDto.setIsOnHoldParameter(labResultMasterDto.getIsOnHoldParameter());
					labSampleDetailsMasterDto.setUpdatedDate(date);
					labSampleDetailsMasterDto.setCreatedBy(labResultMasterDto.getCreatedBy() != null ? labResultMasterDto.getCreatedBy() : 1);
					labSampleDetailsMasterDto.setUpdatedBy(labResultMasterDto.getUpdatedBy() != null ? labResultMasterDto.getUpdatedBy() : 1);
					
					 List<BioChemParamDto> bioChemParamDtoList = new ArrayList<>();
                  bioChemParamDtoList.add(bioChemParamDto);
					
                  iOrderMasterService.assignReviewApplicableDate(labResultMasterDto.getOrderDetailsId() != null ? labResultMasterDto.getOrderDetailsId() : 0);
                  
                  /*if(labResultMasterDto.getPatientMail()!=null)
                  {
                  String  StringFileNameDate = GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId).getTime(),"_dd_MM_yyyy_HH_mm_ss");
                  String fileName = labResultMasterDto.getUhid().toUpperCase().trim()+StringFileNameDate+".pdf";
					if (labResultMasterDto.getTestType()==MULTY_PARAM_TEST) 
					{
					  
						Response<SingParamTestResDto, Integer> multiParamTestRes = this.getReportReleaseWorkListDetails(bioChemParamDtoList);
						SingParamTestResDto singParamTestResDto = multiParamTestRes.getListObject().get(0);
						//SendReportToMail.sendMail(labResultMasterDto.getPatientMail(), fileName, MultiParamTestReportUtility.generateMultiParamReportToMail(labResultMasterDto, singParamTestResDto));
						SendReportToMail.sendMail(labResultMasterDto.getPatientMail(), fileName, this.getLabReport(labResultMasterDto.getLabTestResId(), labResultMasterDto.getOrgId(), labResultMasterDto.getOrgUnitId()));
						
						if(labResultMasterDto.getDoctorMail()!=null){
							//System.out.println(labResultMasterDto.getDoctorMail());
   							//SendReportToMail.sendMail(labResultMasterDto.getDoctorMail(), fileName, MultiParamTestReportUtility.generateMultiParamReportToMail(labResultMasterDto, singParamTestResDto));
							SendReportToMail.sendMail(labResultMasterDto.getDoctorMail(), fileName, this.getLabReport(labResultMasterDto.getLabTestResId(), labResultMasterDto.getOrgId(), labResultMasterDto.getOrgUnitId()));
   						}
						
					} else if (labResultMasterDto.getTestType()==SINGLE_PARAM_TEST) 
					{
						Response<SingParamTestResDto, Integer> singleParamTestRes = this
								.getReportReleaseWorkListDetails(bioChemParamDtoList);
						SingParamTestResDto singParamTestResDto = singleParamTestRes.getListObject().get(0);
						//SendReportToMail.sendMail(labResultMasterDto.getPatientMail(), fileName, SingleParamTestReportUtility.generateSingleParamReportToMail(labResultMasterDto, singParamTestResDto));
						SendReportToMail.sendMail(labResultMasterDto.getPatientMail(), fileName, this.getLabReport(labResultMasterDto.getLabTestResId(), labResultMasterDto.getOrgId(), labResultMasterDto.getOrgUnitId()));
						if(labResultMasterDto.getDoctorMail()!=null){
							//System.out.println(labResultMasterDto.getDoctorMail());
   							//SendReportToMail.sendMail(labResultMasterDto.getDoctorMail(), fileName, SingleParamTestReportUtility.generateSingleParamReportToMail(labResultMasterDto, singParamTestResDto));
							SendReportToMail.sendMail(labResultMasterDto.getDoctorMail(), fileName, this.getLabReport(labResultMasterDto.getLabTestResId(), labResultMasterDto.getOrgId(), labResultMasterDto.getOrgUnitId()));
   						}
						
					}
                  }*/
                  if(labResultMasterDto.getMobileNo()!=null);	
				    // smsUtility.sendLabReportReleaseMessage(labResultMasterDto.getPatientDetails(), labResultMasterDto.getMobileNo(), senderId);
				     LabSampleDetailsMaster dltsMstr = (LabSampleDetailsMaster)reportRelease.getObject();
				     LabResultMasterDto resultDto = new LabResultMasterDto();
				     resultDto.setLabSampleDtlsId(dltsMstr.getLabSampleDtlsId());
				     resultDto.setDoctorId(dltsMstr.getDoctorId());
				     resultDto.setPatientId(dltsMstr.getPatientId());
				     resultDto.setTestDesc(labResultMasterDto.getTestDesc());
				     newResLsit.add(resultDto);
             }
            
             
             //for feedbackform
             TemplateTransactionDto templateDto = new TemplateTransactionDto();
             templateDto.setLabSampleDtlsId(labResultMasterDto.getLabSampleDtlsId() != null ? labResultMasterDto.getLabSampleDtlsId() : 0);
             iEncounterMasterService.checkFeedBackFormSubmitted(templateDto);
			}
		  return new Response(SUCCESS, SUCCESS_200, "Reports Release Successfully.", newResLsit,null);
			
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}
	
	  @Override
	  @Transactional
	  public Response releaseNormanReports(List<LabResultMasterDto> listLabResultMasterDto)
	      throws ApplicationException {
	    try {
	    	Date date = TimeZoneComponent.getDateByzone(listLabResultMasterDto!=null && listLabResultMasterDto.size()>0 
	    			? listLabResultMasterDto.get(0).getOrgUnitId() : null);
	    	String unitLogo = iGlobalUnitMasterService.getUnitImage(listLabResultMasterDto.get(0).getOrgUnitId());
	    	
			//Code for Send Sms
			String senderId = null;
			UnitApplicationConfigurationDto unitApplicationConfigurationDto = new UnitApplicationConfigurationDto();
			unitApplicationConfigurationDto.setOrganizationId(listLabResultMasterDto.get(0).getOrgId());
			unitApplicationConfigurationDto.setUnitId(listLabResultMasterDto.get(0).getOrgUnitId());
			com.param.global.common.Response  appConfigRes = iUnitApplicationBillingConfigurationDao.getUnitApplicationBillingConfigByOrgUnit(unitApplicationConfigurationDto);
		    UnitApplicationConfigurationDto appDto = (UnitApplicationConfigurationDto)appConfigRes.getObject();
			if(appDto != null){
				senderId = appDto.getSenderId();
			}
			else{
				return new Response(ERROR, null, "Unit application configuraion not yet defined.!!", null, null);
			}
	    	
          for (Iterator iterator = listLabResultMasterDto.iterator(); iterator.hasNext();) 
          {
                LabResultMasterDto labResultMasterDto = (LabResultMasterDto) iterator.next();
                labResultMasterDto.setUpdatedDate(date.getTime());
                labResultMasterDto.setUpdatedBy(labResultMasterDto.getCreatedBy());
                labResultMasterDto.setResultAuthorisedBy((labResultMasterDto.getResultAuthorisedBy()!=null && labResultMasterDto.getResultAuthorisedBy()>0)?labResultMasterDto.getResultAuthorisedBy():labResultMasterDto.getCreatedBy());
                labResultMasterDto.setResultAuthorisedDatetime(date);
                labResultMasterDto.setResultAuthorisedFlag('Y');
                Response reportRelease =  iBiochemReportReleaseDao.releaseNormanReports(labResultMasterDto);
                
                
                
                if(reportRelease.getCode().equals(SUCCESS_200))
                {
               	 BioChemParamDto bioChemParamDto = new BioChemParamDto();
   					bioChemParamDto.setLabResultId(labResultMasterDto.getLabTestResId());
   					bioChemParamDto.setDeptId(labResultMasterDto.getSubDeptId());
   					bioChemParamDto.setOrgId(labResultMasterDto.getOrgId());	
   					bioChemParamDto.setOrgUnitId(labResultMasterDto.getOrgUnitId());
   					bioChemParamDto.setTestId(labResultMasterDto.getTestId());
   					bioChemParamDto.setTestType(labResultMasterDto.getTestType());
   					bioChemParamDto.setAssingnedFrom(labResultMasterDto.getAssingnedFrom());
   					bioChemParamDto.setLabSampleDtlsId(labResultMasterDto.getLabSampleDtlsId());
   					
   					LabSampleDetailsMasterDto labSampleDetailsMasterDto = new LabSampleDetailsMasterDto();
   					labSampleDetailsMasterDto.setLabSampleDtlsId(labResultMasterDto.getLabSampleDtlsId());
   					labSampleDetailsMasterDto.setUpdatedDate(date);
   					labSampleDetailsMasterDto.setCreatedBy(labResultMasterDto.getCreatedBy());
   					labSampleDetailsMasterDto.setUpdatedBy(labResultMasterDto.getUpdatedBy());
   					
   					 List<BioChemParamDto> bioChemParamDtoList = new ArrayList<>();
                     bioChemParamDtoList.add(bioChemParamDto);
   					
                     iOrderMasterService.assignReviewApplicableDate(labResultMasterDto.getOrderDetailsId());
                     
                     if(labResultMasterDto.getPatientMail()!=null)
                     {
                     String  StringFileNameDate = GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(labResultMasterDto.getOrgUnitId()).getTime(),"_dd_MM_yyyy_HH_mm_ss");
                     String fileName = labResultMasterDto.getUhid().toUpperCase().trim()+StringFileNameDate+".pdf";
   					if (labResultMasterDto.getTestType()==MULTY_PARAM_TEST) 
   					{
   					  
   						Response<SingParamTestResDto, Integer> multiParamTestRes = this.getReportReleaseWorkListDetails(bioChemParamDtoList);
   						SingParamTestResDto singParamTestResDto = multiParamTestRes.getListObject().get(0);
   						singParamTestResDto.setUnitLogo(unitLogo);
   						//SendReportToMail.sendMail(labResultMasterDto.getPatientMail(), fileName, MultiParamTestReportUtility.generateMultiParamReportToMail(labResultMasterDto, singParamTestResDto));
   						SendReportToMail.sendMail(labResultMasterDto.getPatientMail(), fileName, this.getLabReport(labResultMasterDto.getLabTestResId(), labResultMasterDto.getOrgId(), labResultMasterDto.getOrgUnitId()));
   						
   					   
   					    
   						if(labResultMasterDto.getDoctorMail()!=null){
   							//System.out.println(labResultMasterDto.getDoctorMail());
   							//SendReportToMail.sendMail(labResultMasterDto.getDoctorMail(), fileName, MultiParamTestReportUtility.generateMultiParamReportToMail(labResultMasterDto, singParamTestResDto));
   							SendReportToMail.sendMail(labResultMasterDto.getDoctorMail(), fileName, this.getLabReport(labResultMasterDto.getLabTestResId(), labResultMasterDto.getOrgId(), labResultMasterDto.getOrgUnitId()));
   						}
   						
   						
   					} else if (labResultMasterDto.getTestType()==SINGLE_PARAM_TEST) 
   					{
   						Response<SingParamTestResDto, Integer> singleParamTestRes = this
   								.getReportReleaseWorkListDetails(bioChemParamDtoList);
   						SingParamTestResDto singParamTestResDto = singleParamTestRes.getListObject().get(0);
   						singParamTestResDto.setUnitLogo(unitLogo);
   						SendReportToMail.sendMail(labResultMasterDto.getPatientMail(), fileName, this.getLabReport(labResultMasterDto.getLabTestResId(), labResultMasterDto.getOrgId(), labResultMasterDto.getOrgUnitId()));
   						//SendReportToMail.sendMail(labResultMasterDto.getPatientMail(), fileName, SingleParamTestReportUtility.generateSingleParamReportToMail(labResultMasterDto, singParamTestResDto));
   						if(labResultMasterDto.getDoctorMail()!=null){
   							//System.out.println(labResultMasterDto.getDoctorMail());
   							//SendReportToMail.sendMail(labResultMasterDto.getDoctorMail(), fileName, SingleParamTestReportUtility.generateSingleParamReportToMail(labResultMasterDto, singParamTestResDto));
   							SendReportToMail.sendMail(labResultMasterDto.getDoctorMail(), fileName, this.getLabReport(labResultMasterDto.getLabTestResId(), labResultMasterDto.getOrgId(), labResultMasterDto.getOrgUnitId()));
   						}
   					}
                   }
                     if(labResultMasterDto.getMobileNo()!=null);	
					    // smsUtility.sendLabReportReleaseMessage(labResultMasterDto.getPatientDetails(), labResultMasterDto.getMobileNo(), senderId);
					     
					     //for feedbackform
			             TemplateTransactionDto templateDto = new TemplateTransactionDto();
			             templateDto.setLabSampleDtlsId(labResultMasterDto.getLabSampleDtlsId());
			             iEncounterMasterService.checkFeedBackFormSubmitted(templateDto);
					    
                }
            }
		  return new Response(SUCCESS, SUCCESS_200, "Reports Release Successfully.", null, null);
        } catch (Exception e) {
            logger.error("Exception", e);
            throw e;
        }
	  }
	
	

  @Override
  @Transactional
  public Response searchPatient(SearchCommonDto searchCommonDto) throws ApplicationException {
    try {
      return iBiochemReportReleaseDao.searchPatient(searchCommonDto);
  } catch (Exception e) {
      logger.error("Exception", e);
      throw e;
  }
  }

  @Override
  @Transactional
  public Response getFilteredReportReleaseWorkList(SearchDto searchDto)
      throws ApplicationException {
    try {
      return iBiochemReportReleaseDao.getFilteredReportReleaseWorkList(searchDto);
  } catch (Exception e) {
      logger.error("Exception", e);
      throw e;
  }
  }

	public ByteArrayOutputStream getLabReport(int labResultId, int orgId, Integer unitId) {
		
		 try {
			 
			  String json = "{\r\n" + 
			  		"	                 \"appName\": \"HIS\",\r\n" + 
			  		"	                 \"appSecretKey\": \"f306b6eccd9d5069d2dc71afe139271a\",\r\n" + 
			  		"	                 \"reportDesignFilePath\": \"pathologyReport.rpt\",\r\n" + 
			  		"	                 \"reportFormat\": \"PDF\",\r\n" + 
			  		"	                 \"exportFileName\": \"PatientBill\",\r\n" + 
			  		"	                 \"reportParams\": [\r\n" + 
			  		"	                   {\r\n" + 
			  		"	                   \"paramName\": \"labtesresid\",\r\n" + 
			  		"	                   \"paramType\": \"Integer\",\r\n" + 
			  		"	                   \"paramValue\": "+labResultId+"\r\n" + 
			  		"	                   },\r\n" +
			  		"	                   {\r\n" + 
				  		"	                   \"paramName\": \"isOriginal\",\r\n" + 
				  		"	                   \"paramType\": \"Integer\",\r\n" + 
				  		"	                   \"paramValue\": 1\r\n" + 
				  		"	                   }\r\n" +
			  		"	                 ]\r\n" + 
			  		"}";
			 ByteArrayOutputStream byteOutPutStream = new ByteArrayOutputStream();
				CloseableHttpClient httpClient = HttpClientBuilder.create().build();
				String API_URL = iUnitApplicationBillingConfigurationDao.getURLForPdfGeneration(orgId, unitId);

				if (API_URL == null)
					return null;

				HttpPost post = new HttpPost(API_URL);
				post.addHeader("Content-Type", "application/json");
				post.setEntity(new StringEntity(json));

				HttpResponse result = httpClient.execute(post);
				HttpEntity resEntity = result.getEntity();
				resEntity.writeTo(byteOutPutStream);
				return byteOutPutStream;
		  } catch (Exception e) {
		      logger.error("Exception", e);
		      return null;
		  }
		
		
	}

	@Override
	@Transactional
	public Response ConsolidatedReleaseWorkList(SearchDto searchDto) throws ApplicationException {
		  try {
	            List<LabResultMasterDto>  list  =  new ArrayList<>();
	            Integer count = 0;
	            Response res=  iBiochemReportReleaseDao.ConsolidatedReleaseWorkList(searchDto);
	            if(res.getListObject()!= null && res.getListObject().size()>0) {
	                list.addAll(res.getListObject());
	                count = (Integer) res.getObject();
	            }
	            
	            Response res1 =  iBiochemReportReleaseDao.consolidatedSerologyReleaseSearch(searchDto);
	            
	            if(res1.getListObject()!= null && res1.getListObject().size()>0) {
	                list.addAll(res1.getListObject());
	                count = count + (Integer) res1.getObject();
	            }
	            
	            return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, list, count);
		  } catch (Exception e) {
		      logger.error("Exception", e);
		      throw e;
		  }
	}
	
	@Override
	@Transactional
	public Response ConsolidatedReleaseWorkTypeSearchList(SearchDto searchDto) throws ApplicationException {
		  try {
	            List<LabResultMasterDto>  list  =  new ArrayList<>();
	            Integer count = 0;
	            Response res=  iBiochemReportReleaseDao.ConsolidatedReleaseWorkTypeSearchList(searchDto);
	            if(res.getListObject()!= null && res.getListObject().size()>0) {
	                list.addAll(res.getListObject());
	                count = (Integer) res.getObject();
	            }
	            
	            Response res1 =  iBiochemReportReleaseDao.consolidatedSerologyReleaseTypeSearch(searchDto);
	            
	            if(res1.getListObject()!= null && res1.getListObject().size()>0) {
	                list.addAll(res1.getListObject());
	                count = count + (Integer) res1.getObject();
	            }
	            
	            return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, list, count);
		  } catch (Exception e) {
		      logger.error("Exception", e);
		      throw e;
		  }
	}

	@Override
	@Transactional
	public Response getFilteredReportReleaseTypeSearchWorkList(SearchDto searchDto) throws ApplicationException {
		  try {
		      return iBiochemReportReleaseDao.getFilteredReportReleaseTypeSearchWorkList(searchDto);
		  } catch (Exception e) {
		      logger.error("Exception", e);
		      throw e;
		  }
		  }

	@Override
	@Transactional
	public Response getDoctorIdByLabSampleDetailsId(Integer labSampleDtlsId) throws ApplicationException {
		try {
			return iBiochemReportReleaseDao.getDoctorIdByLabSampleDetailsId(labSampleDtlsId);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getOrderIdByOrderNumber(List<String> orderNos,Integer unitId) throws ApplicationException {
		try {
			return iBiochemReportReleaseDao.getOrderIdByOrderNumber(orderNos,unitId);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getCompanyIdByOrderId(Integer orderId) throws ApplicationException {
		try {
			return iBiochemReportReleaseDao.getCompanyIdByOrderId(orderId);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getLabSampleDetailsIdByOrderId(List<SearchDto> searchDto) throws ApplicationException {
		try {
				List<LabResultMasterDto> labSampleList = new ArrayList<>();
				for (SearchDto searchDto2 : searchDto) {
					List<SearchDto>  ser = new ArrayList<>();
					Response res= iBiochemReportReleaseDao.getLabSampleDetailsIdByOrderId(searchDto2);
					if(res.getListObject() != null && res.getListObject().size()>0) {
						ser.addAll(res.getListObject());
						LabResultMasterDto dto = new LabResultMasterDto();
						List<Integer> detailsId = new ArrayList<>();
						for (SearchDto sarDto : ser) {
							detailsId.add(sarDto.getLabSampleDtlsId());
						}
						dto.setOrderId(searchDto2.getOrderId());
						dto.setLabSampleDtlsIdsList(detailsId);
						labSampleList.add(dto);
					}
				}
			 
			 return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, labSampleList, null);
			
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public ByteArrayOutputStream getReportByQRCode(Integer labDetailsId, int unitId, int orgId) throws Exception {
		try {
				String rptString = "{\"appName\": \"HIS\"," + 
						" \"appSecretKey\": \"f306b6eccd9d5069d2dc71afe139271a\"," + 
						"\"reportDesignFilePath\": \"Multireportprint.rpt\"," + 
						"\"reportFormat\": \"PDF\"," + 
						"\"exportFileName\": \"pathologyReport\"," + 
						"\"reportParams\": [" + 
						"{" + 
						" \"paramName\": \"lab_sample_dtls_id\"," + 
						" \"paramType\": \"String\"," + 
						" \"paramValue\":"+labDetailsId.toString()+ 
						" }," + 
						" {" + 
						" \"paramName\": \"isOriginal\"," + 
						" \"paramType\": \"Integer\"," + 
						" \"paramValue\": 1" + 
						" }," + 
						" {" + 
						" \"paramName\": \"isHeader\"," + 
						"\"paramType\": \"Integer\"," + 
						"\"paramValue\": 1" + 
						"}" + 
						"]}";
				String API_URL = iUnitApplicationBillingConfigurationDao.getURLForPdfGeneration(orgId, unitId);
				ByteArrayOutputStream byteOutPutStream = new ByteArrayOutputStream();
				HttpPost pst = new HttpPost(API_URL);
				pst.addHeader("Content-Type", "application/json");
				pst.setEntity(new StringEntity(rptString));
				CloseableHttpClient httpClient = HttpClientBuilder.create().build();
				HttpResponse result = httpClient.execute(pst);
				HttpEntity resEntity = result.getEntity();
				resEntity.writeTo(byteOutPutStream);
				return byteOutPutStream;
		} catch (Exception e) {
				logger.error("Exception", e);
				throw e;
		}
	}

	@Override
	@Transactional
	public Response updateQAPathInLabsampleDetails(ByteArrayOutputStream bos, String docPath, Integer labSampleDtlsId, String LabAPIUrl)
			throws ApplicationException {
		try {
			return iBiochemReportReleaseDao.updateQAPathInLabsampleDetails(bos,docPath,labSampleDtlsId,LabAPIUrl);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public com.param.global.common.Response getOrganiazationFolderPath(Integer orgId) throws ApplicationException {
		try {
			return iGlobalOrganisationMasterDao.getOrganiazationFolderPath(orgId);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response checkDueAmount(OrderMasterDto orderDto) throws ApplicationException {
		try {
			
			Boolean reportSend = true;
			Integer count = 0;
			 if(orderDto.getPaymentEntitlementId() != null && (orderDto.getPaymentEntitlementId() == INSURANCE || orderDto.getPaymentEntitlementId() == COMPANY )) {
             	
				 Response respayee = iBiochemReportReleaseDao.getPayee(orderDto);
				 
				 
				 if(respayee.getObject()!= null && !respayee.getObject().equals(count)) {
	             		Response res = iBiochemReportReleaseDao.checkOutStandingAmount(orderDto.getEncounterId());
	             		if(res.getObject() != null && res.getObject().equals(count)) {
	             			reportSend= true;
	             		}else {
	             			reportSend= false;
	             		}
	             	}
             	
                 
			 }else {
            	 
             	if(orderDto.getBlockReportPrint() =='Y') {
             		Response res = iBiochemReportReleaseDao.checkOutStandingAmount(orderDto.getEncounterId());
             		if(res.getObject() != null && res.getObject().equals(count)) {
             			reportSend= true;
             		}else {
             			reportSend= false;
             		}
             	}
             }
			
			 return new Response(SUCCESS, SUCCESS_200, null, null, reportSend);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getCheckIsParameterCritical(List<Integer> labSampleDetlsIds) throws ApplicationException {
		try {
			return iBiochemReportReleaseDao.getCheckIsParameterCritical(labSampleDetlsIds);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getLabExclusion(Integer labSampleDtlsId, Integer unitId, Integer orgId) throws ApplicationException {
		try {
			return iBiochemReportReleaseDao.getLabExclusion(labSampleDtlsId,unitId,orgId);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getbucketTypeForUpload(Integer orgId,Integer unitId) throws ApplicationException {
		try {
			return iBiochemReportReleaseDao.getbucketTypeForUpload(orgId,unitId);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getAwsCreds() throws ApplicationException {
		try {
			return iBiochemReportReleaseDao.getAwsCreds();
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Integer getgetEmpId(Integer createdBy) throws ApplicationException {
		try {
			Integer employeeId = (Integer) sessionFactory.getCurrentSession().createSQLQuery(
					"select em.employee_id  as \"employeeId\" from public.m_employee_master em where em.user_id =:userId")
					.setParameter("userId",createdBy).uniqueResult();
			return employeeId;
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}
	
	
}
