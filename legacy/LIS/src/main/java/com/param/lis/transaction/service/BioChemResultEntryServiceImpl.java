package com.param.lis.transaction.service;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import com.param.global.common.ServiceException;
import com.param.global.common.SmsUtility;
import com.param.global.dao.IUnitApplicationBillingConfigurationDao;
import com.param.global.dto.TemplateTransactionDto;
import com.param.global.dto.UnitApplicationConfigurationDto;
import com.param.global.opd.service.IEncounterMasterService;
import com.param.global.service.IOrderMasterService;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.ITransactionConstants;
import com.param.lis.global.common.Response;
import com.param.lis.global.common.SearchDto;
import com.param.lis.reports.service.SendReportToMail;
import com.param.lis.transaction.dao.IBioChemResultEntryDao;
import com.param.lis.transaction.dao.IBiochemReportReleaseDao;
import com.param.lis.transaction.dto.BioChemParamDto;
import com.param.lis.transaction.dto.LabResultDetailsViewDto;
import com.param.lis.transaction.dto.LabResultMasterDto;
import com.param.lis.transaction.dto.LabSampleMasterDto;
import com.param.lis.transaction.dto.SingParamTestResDto;
import com.param.lis.unit.dao.IParameterMasterDao;
import com.param.lis.unit.dto.ParamMultiTextualRangeMasterDto;
import com.param.lis.unit.dto.ParamRefrengMasterDto;
import com.param.lis.unit.dto.ParamTextualRanageMasterDto;
import com.param.lis.unit.dto.ParameterMasterDto;
import com.param.lis.unit.dto.TPanelMasterDto;
import com.param.lis.unit.dto.TestMasterDto;
import com.param.lis.unit.service.IPanelTestService;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class BioChemResultEntryServiceImpl
		implements IBioChemResultEntryService, ICommonConstants, ITransactionConstants, IError {

	@Autowired
	IBioChemResultEntryDao iBioChemResultEntryDao;

	@Autowired
	private IPanelTestService iPanelTestService;

	@Autowired
	IParameterMasterDao iParameterMasterDao;
	
	@Autowired
	IBiochemReportReleaseDao iBiochemReportReleaseDao;
	
	@Autowired
	SmsUtility smsUtility;
	
	@Autowired
	IOrderMasterService iOrderMasterService;
	
	@Autowired
	SessionFactory sessionFactory;

	 @Autowired
	 IEncounterMasterService iEncounterMasterService;

	@Autowired
	IUnitApplicationBillingConfigurationDao iUnitApplicationBillingConfigurationDao;

	
	final static Logger logger = Logger.getLogger(BioChemResultEntryServiceImpl.class);

	@Override
	@Transactional
	public Response getResultEntryList(BioChemParamDto bioChemParamDto) throws ApplicationException {
		try {
			return iBioChemResultEntryDao.getResultEntryList(bioChemParamDto);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getResultEntryListCount(BioChemParamDto bioChemParamDto) throws ApplicationException {
		try {
			return iBioChemResultEntryDao.getResultEntryListCount(bioChemParamDto);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getResultEntryDetails(List<BioChemParamDto> bioChemParamDtoList) throws ApplicationException {
		try {

			List<SingParamTestResDto> testMasterDtoList = new ArrayList<>();
			for (Iterator iterator = bioChemParamDtoList.iterator(); iterator.hasNext();) {
				BioChemParamDto bioChemParamDto = (BioChemParamDto) iterator.next();
				Response<TestMasterDto, Integer> response = null;
				if (bioChemParamDto.getTestType().equals(SINGLE_PARAM_TEST)) {
					Integer panelId = bioChemParamDto.getPanelId() == null ? 0 : bioChemParamDto.getPanelId();
					Response<TPanelMasterDto, Integer> panelMstRes = iPanelTestService.getPanelDescByPanelId(panelId,
							bioChemParamDto.getOrgId(), bioChemParamDto.getOrgUnitId());

					SingParamTestResDto singParamTestResDto = new SingParamTestResDto();
					LabResultDetailsViewDto labResultDetailsViewDto = new LabResultDetailsViewDto();
					response = iBioChemResultEntryDao.getSingleParamTestResult(bioChemParamDto);
					if(response.getObject() == null){
						return new Response(ERROR, ERR_500, response.getMessage(), null, null);
					}
					TestMasterDto testMasterDto = response.getObject();
					singParamTestResDto.setTestId(testMasterDto.getTestId());
					singParamTestResDto.setServiceId(testMasterDto.getServiceId());
					singParamTestResDto.setTestDesc(testMasterDto.getTestDesc());
					singParamTestResDto.setTestCode(testMasterDto.getTestCode());
					singParamTestResDto.setTestType(testMasterDto.getTestType());
					singParamTestResDto.setTechniqueName(testMasterDto.getTechniqueName());
					labResultDetailsViewDto.setReportType(testMasterDto.getReportTypeId());
					singParamTestResDto.setLabSampleNo(bioChemParamDto.getLabSampleNo());
					singParamTestResDto.setPanelAlies(
							panelMstRes.getObject() != null ? panelMstRes.getObject().getPanelAlies() : "");
					singParamTestResDto.setPanelCode(
							panelMstRes.getObject() != null ? panelMstRes.getObject().getPanelCode() : "");
					singParamTestResDto.setLabSampleDtlsId(bioChemParamDto.getLabSampleDtlsId());
					LabSampleDetailsMaster labSampleDetailsMaster= (LabSampleDetailsMaster) sessionFactory.getCurrentSession().get(LabSampleDetailsMaster.class, bioChemParamDto.getLabSampleDtlsId());
					if(labSampleDetailsMaster!=null) {
					singParamTestResDto.setTestId(labSampleDetailsMaster.getTestId());
					singParamTestResDto.setAssignUnit(labSampleDetailsMaster.getOrgUnitId());
                    singParamTestResDto.setIsOnHoldParameter(labSampleDetailsMaster.getIsOnHoldParameter());
					singParamTestResDto.setPerformTestId(labSampleDetailsMaster.getPerformTestId()!= null ? labSampleDetailsMaster.getPerformTestId() : null);
					singParamTestResDto.setSrfId(labSampleDetailsMaster.getSrfId());
					}
					for (Iterator iterator1 = testMasterDto.getListParameterMasterDto().iterator(); iterator1
							.hasNext();) {
						ParameterMasterDto parameterMasterDto = (ParameterMasterDto) iterator1.next();
						labResultDetailsViewDto.setCreatedBy(0);
						labResultDetailsViewDto.setTestId(testMasterDto.getTestId());
						labResultDetailsViewDto.setParameterId(parameterMasterDto.getParameterId());
						labResultDetailsViewDto.setParamName(parameterMasterDto.getParameterName());
						labResultDetailsViewDto.setTechniqueName(parameterMasterDto.getTechniqueName());
						labResultDetailsViewDto.setListHelpValueMaster(parameterMasterDto.getListHelpValueMaster());

						if (parameterMasterDto.getRefrangeTypeId() == REFERENCE_VALUE) {
							
							
							Optional<ParamRefrengMasterDto> paramRefrengMstDto = null;
							 ParamRefrengMasterDto paramRefrengMasterDto = null;
							 if(parameterMasterDto.getAgeGroupReqiured()!= null && parameterMasterDto.getGenderReqiured()!= null && parameterMasterDto.getAgeGroupReqiured() == 'Y' && parameterMasterDto.getGenderReqiured() == 'Y'){
								 
								 paramRefrengMstDto = parameterMasterDto
											.getListParamRefrengMaster().stream()
											.filter(ageGrp -> (bioChemParamDto.getPatientAgeDays() >= ageGrp.getAgeFromDay()
													&& bioChemParamDto.getPatientAgeDays() <= ageGrp.getAgeToDay()
													&& ageGrp.getIsDeleted() == 'N' && ageGrp.getGenderId()
															.intValue() == bioChemParamDto.getGenderId().intValue()))
											.findFirst();
								  paramRefrengMasterDto = paramRefrengMstDto.orElse(null);
		    				  }else  if(parameterMasterDto.getAgeGroupReqiured()!= null &&  parameterMasterDto.getAgeGroupReqiured() == 'Y'){
		    					
		    					  paramRefrengMstDto = parameterMasterDto
											.getListParamRefrengMaster().stream()
											.filter(ageGrp -> (bioChemParamDto.getPatientAgeDays() >= ageGrp.getAgeFromDay()
													&& bioChemParamDto.getPatientAgeDays() <= ageGrp.getAgeToDay()
													&& ageGrp.getIsDeleted() == 'N'))
											.findFirst();
		    					  paramRefrengMasterDto = paramRefrengMstDto.orElse(null);
		    				  }else  if(parameterMasterDto.getGenderReqiured()!= null && parameterMasterDto.getGenderReqiured() == 'Y'){
		    					  paramRefrengMstDto = parameterMasterDto
											.getListParamRefrengMaster().stream()
											.filter(ageGrp -> ( ageGrp.getGenderId().intValue() == bioChemParamDto.getGenderId().intValue()))
											.findFirst();
		    					  paramRefrengMasterDto = paramRefrengMstDto.orElse(null);
		    				  }else {
		    					  paramRefrengMasterDto = parameterMasterDto.getListParamRefrengMaster().getFirst();
		    				  }
							

							
							if (paramRefrengMasterDto != null) {
								labResultDetailsViewDto.setParameterId(paramRefrengMasterDto.getParameterId());
								labResultDetailsViewDto.setIsFormula(parameterMasterDto.getIsFormula());
								labResultDetailsViewDto.setIsEditable(parameterMasterDto.getIsEditable()!=null ? parameterMasterDto.getIsEditable(): 'N');
								labResultDetailsViewDto.setParameterMin(paramRefrengMasterDto.getMinValue());
								labResultDetailsViewDto.setParameterMax(paramRefrengMasterDto.getMaxValue());
								labResultDetailsViewDto.setParamAbnrmlMax(paramRefrengMasterDto.getMoreThan());
								labResultDetailsViewDto.setParamAbnrmlMin(paramRefrengMasterDto.getLessThan());
								labResultDetailsViewDto.setOriginalParamAbnrmlMax(paramRefrengMasterDto.getMoreThan());
								labResultDetailsViewDto.setOriginalParamAbnrmlMin(paramRefrengMasterDto.getLessThan());
								labResultDetailsViewDto.setOriginalParameterMax(paramRefrengMasterDto.getMaxValue());
								labResultDetailsViewDto.setOriginalParameterMin(paramRefrengMasterDto.getMinValue());
								labResultDetailsViewDto.setDefaultParamUnit(parameterMasterDto.getUnitName());
								labResultDetailsViewDto.setParamName(parameterMasterDto.getParameterName());
								labResultDetailsViewDto.setParamUnit(parameterMasterDto.getUnitName());
								labResultDetailsViewDto.setRefrangeTypeId(parameterMasterDto.getRefrangeTypeId());
								// labResultDetailsViewDto.setDeltaDaysId(parameterMasterDto.getDeltaDaysId());
								labResultDetailsViewDto.setDeltaPer(parameterMasterDto.getDeltaPer());
								labResultDetailsViewDto.setParamPrecision(parameterMasterDto.getParamPrecision());
								labResultDetailsViewDto.setHeaderId(0);
								labResultDetailsViewDto.setOrgId(testMasterDto.getOrgId());
								labResultDetailsViewDto.setOrgUnitId(testMasterDto.getOrgUnitId());
								labResultDetailsViewDto.setParamPrintOrder(0);
								labResultDetailsViewDto.setRemarks(paramRefrengMasterDto.getRemark());
                                labResultDetailsViewDto.setLabResultId(bioChemParamDto.getLabResultId());
							}
						} else if (parameterMasterDto.getRefrangeTypeId() == TEXTUAL_RANGE) {
							
							Optional<ParamTextualRanageMasterDto> ParamTextualRanageMstDto = null;
							ParamTextualRanageMasterDto paramTextualRanageMstDto = null;
							 if(parameterMasterDto.getAgeGroupReqiured()!= null && parameterMasterDto.getGenderReqiured()!= null && parameterMasterDto.getAgeGroupReqiured() == 'Y' && parameterMasterDto.getGenderReqiured() == 'Y'){
								 
								 ParamTextualRanageMstDto = parameterMasterDto
											.getListParamTextualRanageMaster().stream()
											.filter(ageGrp -> (bioChemParamDto.getPatientAgeDays() >= ageGrp.getAgeFromDay()
													&& bioChemParamDto.getPatientAgeDays() <= ageGrp.getAgeToDay()
													&& ageGrp.getIsDeleted() == 'N' && ageGrp.getGenderId()
															.intValue() == bioChemParamDto.getGenderId().intValue()))
											.findFirst();
								 paramTextualRanageMstDto = ParamTextualRanageMstDto.orElse(null);
		    				  }else  if(parameterMasterDto.getAgeGroupReqiured()!= null &&  parameterMasterDto.getAgeGroupReqiured() == 'Y'){
		    					
		    					  ParamTextualRanageMstDto = parameterMasterDto
											.getListParamTextualRanageMaster().stream()
											.filter(ageGrp -> (bioChemParamDto.getPatientAgeDays() >= ageGrp.getAgeFromDay()
													&& bioChemParamDto.getPatientAgeDays() <= ageGrp.getAgeToDay()
													&& ageGrp.getIsDeleted() == 'N'))
											.findFirst();
		    					  paramTextualRanageMstDto = ParamTextualRanageMstDto.orElse(null);
		    				  }else  if(parameterMasterDto.getGenderReqiured()!= null && parameterMasterDto.getGenderReqiured() == 'Y'){
		    					  ParamTextualRanageMstDto = parameterMasterDto
											.getListParamTextualRanageMaster().stream()
											.filter(ageGrp -> ( ageGrp.getGenderId().intValue() == bioChemParamDto.getGenderId().intValue()))
											.findFirst();
		    					  paramTextualRanageMstDto = ParamTextualRanageMstDto.orElse(null);
		    				  }else {
		    					  paramTextualRanageMstDto = parameterMasterDto.getListParamTextualRanageMaster().getFirst();
		    				  }
							if (paramTextualRanageMstDto != null) {
								labResultDetailsViewDto.setParameterId(paramTextualRanageMstDto.getParameterId());
								labResultDetailsViewDto.setIsFormula(parameterMasterDto.getIsFormula());
								labResultDetailsViewDto.setIsEditable(parameterMasterDto.getIsEditable()!=null ? parameterMasterDto.getIsEditable(): 'N');
								labResultDetailsViewDto.setTextualRangeId(paramTextualRanageMstDto.getTextualRangeId());
								labResultDetailsViewDto.setOriginalTextualRangeName(paramTextualRanageMstDto.getTextualRangeName());
								labResultDetailsViewDto
										.setTextualRangeName(paramTextualRanageMstDto.getTextualRangeName());
								labResultDetailsViewDto.setRefrangeTypeId(parameterMasterDto.getRefrangeTypeId());
								labResultDetailsViewDto.setDefaultParamUnit(parameterMasterDto.getUnitName());
								labResultDetailsViewDto.setParamUnit(parameterMasterDto.getUnitName());
								labResultDetailsViewDto.setDeltaDaysId(parameterMasterDto.getDeltaDaysId());
								labResultDetailsViewDto.setRefrangeTypeId(parameterMasterDto.getRefrangeTypeId());
								// labResultDetailsViewDto.setDeltaPer(parameterMasterDto.getDeltaPer());
								labResultDetailsViewDto.setParamPrecision(parameterMasterDto.getParamPrecision());
								labResultDetailsViewDto.setHeaderId(0);
								labResultDetailsViewDto.setOrgId(testMasterDto.getOrgId());
								labResultDetailsViewDto.setOrgUnitId(testMasterDto.getOrgUnitId());
								labResultDetailsViewDto.setParamPrintOrder(0);
								labResultDetailsViewDto.setRemarks(paramTextualRanageMstDto.getRemark());
                                labResultDetailsViewDto.setLabResultId(bioChemParamDto.getLabResultId());

							}
						} else if (parameterMasterDto.getRefrangeTypeId() == MULTIVALUED_RANGE) {
							
							
							Optional<ParamMultiTextualRangeMasterDto> paramMultiTextualRangeMstDto = null;
							ParamMultiTextualRangeMasterDto paramMultiTextualRangeMasterDto = null;
							 if(parameterMasterDto.getAgeGroupReqiured()!= null && parameterMasterDto.getGenderReqiured()!= null && parameterMasterDto.getAgeGroupReqiured() == 'Y' && parameterMasterDto.getGenderReqiured() == 'Y'){
								 
								 paramMultiTextualRangeMstDto = parameterMasterDto
											.getListParamMultiTextualRangeMaster().stream()
											.filter(ageGrp -> (bioChemParamDto.getPatientAgeDays() >= ageGrp.getAgeFromDay()
													&& bioChemParamDto.getPatientAgeDays() <= ageGrp.getAgeToDay()
													&& ageGrp.getIsDeleted() == 'N' && ageGrp.getGenderId()
															.intValue() == bioChemParamDto.getGenderId().intValue()))
											.findFirst();
								 paramMultiTextualRangeMasterDto = paramMultiTextualRangeMstDto.orElse(null);
		    				  }else  if(parameterMasterDto.getAgeGroupReqiured()!= null &&  parameterMasterDto.getAgeGroupReqiured() == 'Y'){
		    					
		    					  paramMultiTextualRangeMstDto = parameterMasterDto
											.getListParamMultiTextualRangeMaster().stream()
											.filter(ageGrp -> (bioChemParamDto.getPatientAgeDays() >= ageGrp.getAgeFromDay()
													&& bioChemParamDto.getPatientAgeDays() <= ageGrp.getAgeToDay()
													&& ageGrp.getIsDeleted() == 'N'))
											.findFirst();
		    					  paramMultiTextualRangeMasterDto = paramMultiTextualRangeMstDto.orElse(null);
		    				  }else  if(parameterMasterDto.getGenderReqiured()!= null && parameterMasterDto.getGenderReqiured() == 'Y'){
		    					  paramMultiTextualRangeMstDto = parameterMasterDto
											.getListParamMultiTextualRangeMaster().stream()
											.filter(ageGrp -> ( ageGrp.getGenderId().intValue() == bioChemParamDto.getGenderId().intValue()))
											.findFirst();
		    					  paramMultiTextualRangeMasterDto = paramMultiTextualRangeMstDto.orElse(null);
		    				  }else {
		    					  paramMultiTextualRangeMasterDto = parameterMasterDto.getListParamMultiTextualRangeMaster().getFirst();
		    				  }
							if (paramMultiTextualRangeMasterDto != null) {
								labResultDetailsViewDto
										.setParameterId(paramMultiTextualRangeMasterDto.getParameterId());
								labResultDetailsViewDto.setIsFormula(parameterMasterDto.getIsFormula());
								labResultDetailsViewDto.setIsEditable(parameterMasterDto.getIsEditable()!=null ? parameterMasterDto.getIsEditable(): 'N');
								labResultDetailsViewDto.setMultitextaulRange(paramMultiTextualRangeMasterDto.getMultitextaulRange());
								labResultDetailsViewDto.setOriginalMultitextaulRange(paramMultiTextualRangeMasterDto.getMultitextaulRange());
								labResultDetailsViewDto.setRefrangeTypeId(parameterMasterDto.getRefrangeTypeId());
								labResultDetailsViewDto.setDefaultParamUnit(parameterMasterDto.getUnitName());
								labResultDetailsViewDto.setParamUnit(parameterMasterDto.getUnitName());
								labResultDetailsViewDto.setDeltaDaysId(parameterMasterDto.getDeltaDaysId());
								labResultDetailsViewDto.setRefrangeTypeId(parameterMasterDto.getRefrangeTypeId());
								labResultDetailsViewDto.setParamPrecision(parameterMasterDto.getParamPrecision());
								labResultDetailsViewDto.setHeaderId(0);
								labResultDetailsViewDto.setOrgId(testMasterDto.getOrgId());
								labResultDetailsViewDto.setOrgUnitId(testMasterDto.getOrgUnitId());
								labResultDetailsViewDto.setParamPrintOrder(0);
                                labResultDetailsViewDto.setLabResultId(bioChemParamDto.getLabResultId());

								labResultDetailsViewDto.setRemarks(paramMultiTextualRangeMasterDto.getRemark());
							}
						}
					}
					singParamTestResDto.setLabResultDetailsViewDto(labResultDetailsViewDto);
					testMasterDtoList.add(singParamTestResDto);

				} else if (bioChemParamDto.getTestType().equals(MULTY_PARAM_TEST)) {

					
					SingParamTestResDto singParamTestResDto = new SingParamTestResDto();
					Response testRes = iBioChemResultEntryDao.getMultyParameterTestResult(bioChemParamDto);
					if (testRes.getObject() != null) {
						singParamTestResDto = (SingParamTestResDto) testRes.getObject();
					}else {
						return new Response(ERROR, ERR_500, testRes.getMessage(), null, null);
					}
					 
					testMasterDtoList.add(singParamTestResDto);
				}
			}
			return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, testMasterDtoList, null);

		} catch (

		Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response saveEntryDetails(List<LabResultMasterDto> listLabResultMasterDto) throws ApplicationException {
		try {
			
			iBioChemResultEntryDao.checkLabResultIsExistAndUpdateStatus(listLabResultMasterDto);
			return iBioChemResultEntryDao.saveEntryDetails(listLabResultMasterDto);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getSingleParamTestResult(BioChemParamDto bioChemParamDto) throws ApplicationException {
		try {
			return iBioChemResultEntryDao.getSingleParamTestResult(bioChemParamDto);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getMultyParamTestResult(BioChemParamDto bioChemParamDto) throws ApplicationException {
		try {
			return iBioChemResultEntryDao.getMultyParamTestResult(bioChemParamDto);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response labResultEntrySearch(SearchDto searchDto) throws ApplicationException {
		try {
			return iBioChemResultEntryDao.labResultEntrySearch(searchDto);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getTestMachineResult(Map<String, Integer> sampleIds) throws ApplicationException {
		try {
			return iBioChemResultEntryDao.getTestMachineResult(sampleIds);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response saveReleaseEntryDetails(List<LabResultMasterDto> listLabResultMasterDto)
			throws ApplicationException {
		try {
			
			List<LabResultMasterDto> finalList = new ArrayList<>();
			//Code for Send Sms
			 iBioChemResultEntryDao.checkLabResultIsExistAndUpdateStatus(listLabResultMasterDto);
			
			 for (LabResultMasterDto labResultMasterDto : listLabResultMasterDto) {
				
	            if (labResultMasterDto == null) {
	                logger.info("INFO : labResultMasterDto is NULL");
	                return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	            } else {
	            labResultMasterDto.setLabTestResId(null);
	            Response reportRelease =  iBioChemResultEntryDao.saveReleaseEntryDetails(labResultMasterDto); 
	             if(reportRelease.getCode().equals(SUCCESS_200))
	             {
	            	 LabResultMaster mstr =  (LabResultMaster) reportRelease.getObject();
	            	 labResultMasterDto.setLabTestResId(mstr.getLabTestResId());
	                  iOrderMasterService.assignReviewApplicableDate(labResultMasterDto.getOrderDetailsId());
	                  labResultMasterDto.setDoctorId(mstr.getLabSampleDetailsMaster().getDoctorId());
	                  labResultMasterDto.setLabSampleDtlsId(mstr.getLabSampleDetailsMaster().getLabSampleDtlsId());
	                  finalList.add(labResultMasterDto);
	                  
	                  
	                  //for feedbackform
			             TemplateTransactionDto templateDto = new TemplateTransactionDto();
			             templateDto.setLabSampleDtlsId(mstr.getLabSampleDetailsMaster().getLabSampleDtlsId());
			             iEncounterMasterService.checkFeedBackFormSubmitted(templateDto);
				}
			  }
		  }
		  
		  return new Response(SUCCESS, SUCCESS_200, "Reports Release Successfully.", finalList, null);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}
	
	public ByteArrayOutputStream getLabReport(int labResultId, int orgId, Integer unitId) throws Exception {
		
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
		      throw e;
		  }
		
		
	}

	@SuppressWarnings("unused")
	@Override
	@Transactional
	public Response sendEmail(List<LabResultMasterDto> list) throws Exception {
		try {
			
			String senderId = null;
			UnitApplicationConfigurationDto unitApplicationConfigurationDto = new UnitApplicationConfigurationDto();
			unitApplicationConfigurationDto.setOrganizationId(list.getFirst().getOrgId());
			unitApplicationConfigurationDto.setUnitId(list.getFirst().getOrgUnitId());
			com.param.global.common.Response  appConfigRes = iUnitApplicationBillingConfigurationDao.getUnitApplicationBillingConfigByOrgUnit(unitApplicationConfigurationDto);
		    UnitApplicationConfigurationDto appDto = (UnitApplicationConfigurationDto)appConfigRes.getObject();
			if(appDto != null){
				senderId = appDto.getSenderId();
			}
			else{
				return new Response(ERROR, null, "Unit application configuraion not yet defined.!!", null, null);
			}
		  for (Iterator iterator = list.iterator(); iterator.hasNext();) 
		  {
            LabResultMasterDto labResultMasterDto = (LabResultMasterDto) iterator.next();
            
                  if(labResultMasterDto.getPatientMail()!=null)
                  {
                  String  StringFileNameDate = GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(labResultMasterDto.getOrgUnitId()).getTime(),"_dd_MM_yyyy_HH_mm_ss");
                  String fileName = labResultMasterDto.getUhid().toUpperCase().trim()+StringFileNameDate+".pdf";
					if (labResultMasterDto.getTestType()==MULTY_PARAM_TEST) 
					{
					  
						SendReportToMail.sendMail(labResultMasterDto.getPatientMail(), fileName, this.getLabReport(labResultMasterDto.getLabTestResId(), labResultMasterDto.getOrgId(), labResultMasterDto.getOrgUnitId()));
						
						if(labResultMasterDto.getDoctorMail()!=null){
							SendReportToMail.sendMail(labResultMasterDto.getDoctorMail(), fileName, this.getLabReport(labResultMasterDto.getLabTestResId(), labResultMasterDto.getOrgId(), labResultMasterDto.getOrgUnitId()));
   						}
						
					} else if (labResultMasterDto.getTestType()==SINGLE_PARAM_TEST) 
					{
						SendReportToMail.sendMail(labResultMasterDto.getPatientMail(), fileName, this.getLabReport(labResultMasterDto.getLabTestResId(), labResultMasterDto.getOrgId(), labResultMasterDto.getOrgUnitId()));
						if(labResultMasterDto.getDoctorMail()!=null){
							SendReportToMail.sendMail(labResultMasterDto.getDoctorMail(), fileName, this.getLabReport(labResultMasterDto.getLabTestResId(), labResultMasterDto.getOrgId(), labResultMasterDto.getOrgUnitId()));
   						}
						
					}
                  }
//                  if(labResultMasterDto.getMobileNo()!=null);	
//				     smsUtility.sendLabReportReleaseMessage(labResultMasterDto.getPatientDetails(), labResultMasterDto.getMobileNo(), senderId);
             }
            
		  
		  return new Response(SUCCESS, SUCCESS_200, "Reports Release Successfully.", null, null);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response consolidatedResultEntrySearch(SearchDto searchDto) throws ApplicationException {
		try {
			return iBioChemResultEntryDao.consolidatedResultEntrySearch(searchDto);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response consolidatedResultEntryTypeSearch(SearchDto searchDto) throws ApplicationException {
		try {
			return iBioChemResultEntryDao.consolidatedResultEntryTypeSearch(searchDto);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response labResultEntryTypeSearch(SearchDto searchDto) throws ApplicationException {
		try {
			return iBioChemResultEntryDao.labResultEntryTypeSearch(searchDto);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getParameterValueByMachineId(BioChemParamDto bioChemParamDtoList) throws ApplicationException {
		try {
			
			return  iBioChemResultEntryDao.getParameterValueByMachineId(bioChemParamDtoList);
			 
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

    @Override
    @Transactional
    public Response<LabResultMasterDto, Integer> updateEntryDetails(List<LabResultMasterDto> listLabResultMasterDto)
            throws ApplicationException {
        try {
            return iBioChemResultEntryDao.updateEntryDetails(listLabResultMasterDto);
        } catch (Exception e) {
            logger.error("Exception", e);
            throw e;
        }
    }

	@Override
	@Transactional
	public Response<LabSampleMasterDto, Integer> barcodeScannerListSearch(SearchDto searchDto) {
		try {
			return iBioChemResultEntryDao.barcodeScannerListSearch(searchDto);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response<LabResultMasterDto, Integer> barcodeScannerListSave(
			List<LabResultMasterDto> listLabResultMasterDto) {
		try {
			return iBioChemResultEntryDao.barcodeScannerListSave(listLabResultMasterDto);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response<LabSampleMasterDto, Integer> getSampleRecollection(SearchDto searchDto) {
		try {
			return iBioChemResultEntryDao.getSampleRecollection(searchDto);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response<LabResultMasterDto, Integer> saveInterfaceResult(LabResultMasterDto labResultMasterDto) {
		try {
			
			return iBioChemResultEntryDao.saveInterfaceResult(labResultMasterDto);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

    @Override
    @Transactional
    public List<Integer> getLabSampleDetailIdsByPatientId(LabResultMasterDto labResultMasterDto, List<Integer> patientIds) {
        try {

            return iBioChemResultEntryDao.getLabSampleDetailIdsByPatientId(labResultMasterDto, patientIds);
        } catch (Exception e) {
            logger.error("Exception", e);
            throw e;
        }
    }

    @Override
    @Transactional
    public Response getLabSampleDetailIdsByMobileNo(LabResultMasterDto labResultMasterDto) {
        List<Integer> labSampleDetailsIds = new ArrayList<>();
        List<TemplateTransactionDto> RisReportsList = new ArrayList<>();
        try {
            List<Integer> patientIds = iBioChemResultEntryDao.getLabSampleDetailIdsByMobileNo(labResultMasterDto);

            if(patientIds != null && !patientIds.isEmpty()) {
                labSampleDetailsIds = iBioChemResultEntryDao.getLabSampleDetailIdsByPatientId(labResultMasterDto, patientIds);
//                RisReportsList = iBioChemResultEntryDao.getRISReportsByPatientId(labResultMasterDto, patientIds);
            }
            return new Response(SUCCESS, SUCCESS_200, SUCCESS,labSampleDetailsIds, null, RisReportsList);
        } catch (Exception e) {
            logger.error("Exception", e);
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public List<TemplateTransactionDto> getRISReportsByPatientId(LabResultMasterDto labResultMasterDto, List<Integer> patientIds) {
        try {

            return iBioChemResultEntryDao.getRISReportsByPatientId(labResultMasterDto, patientIds);
        } catch (Exception e) {
            logger.error("Exception", e);
            throw e;
        }
    }


}
