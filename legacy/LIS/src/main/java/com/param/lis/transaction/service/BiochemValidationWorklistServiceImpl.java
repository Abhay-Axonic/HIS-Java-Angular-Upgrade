package com.param.lis.transaction.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.param.lis.global.common.*;
import com.param.lis.transaction.dao.IClinicalInterventionProcedureDao;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.entity.lis.transaction.LabSampleDetailsMaster;
import com.param.lis.transaction.dao.IBioChemResultEntryDao;
import com.param.lis.transaction.dao.IBioChemistryDao;
import com.param.lis.transaction.dao.IBiochemValidationWorklistDao;
import com.param.lis.transaction.dto.BioChemParamDto;
import com.param.lis.transaction.dto.LabResultDetailsViewDto;
import com.param.lis.transaction.dto.LabResultMasterDto;
import com.param.lis.transaction.dto.MultyParamTestHeaderDto;
import com.param.lis.transaction.dto.SingParamTestResDto;
import com.param.lis.unit.dao.IParameterMasterDao;
import com.param.lis.unit.dto.HelpValueMasterDto;
import com.param.lis.unit.dto.TPanelMasterDto;
import com.param.lis.unit.dto.TestMasterDto;
import com.param.lis.unit.dto.TestParamMpprDto;
import com.param.lis.unit.service.IPanelTestService;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class BiochemValidationWorklistServiceImpl
		implements IBiochemValidationWorklistService, ICommonConstants, ITransactionConstants, IError {

	@Autowired
	IBiochemValidationWorklistDao iBiochemValidationWorklistDao;

	@Autowired
	IBioChemResultEntryDao iBioChemResultEntryDao;

	@Autowired
	IParameterMasterDao iParameterMasterDao;
	
	@Autowired
    private IPanelTestService iPanelTestService;
	
	@Autowired
	IBioChemistryDao iBioChemistryDao;
	    
    @Autowired
    SessionFactory sessionFactory;

	@Autowired
	IClinicalInterventionProcedureDao iClinicalInterventionProcedureDao;

	final static Logger logger = Logger.getLogger(IBiochemValidationWorklistService.class);

	@Override
	@Transactional
	public Response getValidationWorkList(BioChemParamDto bioChemParamDto) throws ApplicationException {
		try {
			return iBiochemValidationWorklistDao.getValidationWorkList(bioChemParamDto);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getValidationWorkListCount(BioChemParamDto bioChemParamDto) throws ApplicationException {
		try {
			return iBiochemValidationWorklistDao.getValidationWorkListCount(bioChemParamDto);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getValidationWorkListDetails(List<BioChemParamDto> bioChemParamDtoList) throws ApplicationException {
		try {
		  
		    List<SingParamTestResDto> testMasterDtoList = new ArrayList<>();
		for (Iterator iterator = bioChemParamDtoList.iterator(); iterator.hasNext();) {
          BioChemParamDto bioChemParamDto = (BioChemParamDto) iterator.next();
          Response<SingParamTestResDto, Integer> resultEnterDateRes =  iBioChemistryDao.getResultEnteryByDateTime(bioChemParamDto);
          Response<TestMasterDto, Integer> response = null;
          if (bioChemParamDto.getTestType().equals(SINGLE_PARAM_TEST)) {
            
            Integer panelId = bioChemParamDto.getPanelId()==null?0:bioChemParamDto.getPanelId();
            Response<TPanelMasterDto, Integer> panelMstRes =  iPanelTestService.getPanelDescByPanelId(panelId, bioChemParamDto.getOrgId(), bioChemParamDto.getAssingnedFrom());  
             
            
              SingParamTestResDto singParamTestResDto = new SingParamTestResDto();
              response = iBioChemResultEntryDao.getSingleParamTestResult(bioChemParamDto);
              TestMasterDto testMasterDto = response.getObject();
              singParamTestResDto.setTestId(testMasterDto.getTestId());
              singParamTestResDto.setTestDesc(testMasterDto.getTestDesc());
              singParamTestResDto.setTestCode(testMasterDto.getTestCode());
              singParamTestResDto.setTestType(testMasterDto.getTestType());
              singParamTestResDto.setTechniqueName(testMasterDto.getTechniqueName());
              singParamTestResDto.setLabSampleNo(bioChemParamDto.getLabSampleNo());
              singParamTestResDto.setPanelAlies( panelMstRes.getObject()!=null?panelMstRes.getObject().getPanelAlies():"");
              singParamTestResDto.setPanelCode(panelMstRes.getObject()!=null?panelMstRes.getObject().getPanelCode():"");
              singParamTestResDto.setResultEnterByUser(resultEnterDateRes.getObject().getResultEnterByUser());
              singParamTestResDto.setResultEnterDatetime(resultEnterDateRes.getObject().getResultEnterDatetime());
              singParamTestResDto.setFootsNotes(resultEnterDateRes.getObject().getFootsNotes());
              singParamTestResDto.setInternalNotes(resultEnterDateRes.getObject().getInternalNotes());
              singParamTestResDto.setSrfId(resultEnterDateRes.getObject().getSrfId());
              singParamTestResDto.setIcmrId(resultEnterDateRes.getObject().getIcmrId());
              singParamTestResDto.setIsOnHoldParameter(resultEnterDateRes.getObject().getIsOnHoldParameter());
              singParamTestResDto.setLabSampleDtlsId(bioChemParamDto.getLabSampleDtlsId());
              singParamTestResDto.setMachineId(resultEnterDateRes.getObject().getMachineId()!=null ?resultEnterDateRes.getObject().getMachineId() : null );

              Response<LabResultDetailsViewDto, Integer> labResDetails = iBiochemValidationWorklistDao
                      .getValidationWorkListDetails(bioChemParamDto);
              LabSampleDetailsMaster labSampleDetailsMaster= (LabSampleDetailsMaster) sessionFactory.getCurrentSession().get(LabSampleDetailsMaster.class, bioChemParamDto.getLabSampleDtlsId());
              if(labSampleDetailsMaster!=null) {
	              singParamTestResDto.setTestId(labSampleDetailsMaster.getTestId());
	              singParamTestResDto.setAssignUnit(labSampleDetailsMaster.getOrgUnitId());
	              singParamTestResDto.setPerformTestId(labSampleDetailsMaster.getPerformTestId()!=null ?labSampleDetailsMaster.getPerformTestId() : labSampleDetailsMaster.getTestId() );
              }              
      
              if (labResDetails.getListObject() != null) {
                  for (Iterator iterator1 = labResDetails.getListObject().iterator(); iterator1.hasNext();) {
                      LabResultDetailsViewDto labResultDetailsViewDto = (LabResultDetailsViewDto) iterator1.next();
                      List<HelpValueMasterDto> listHelpValueMasterDto = (List<HelpValueMasterDto>) iParameterMasterDao
                              .getHelpValueByParameter(bioChemParamDto.getOrgId(),
                                      bioChemParamDto.getAssingnedFrom(),
                                      labResultDetailsViewDto.getParameterId()).getListObject();
                      labResultDetailsViewDto.setListHelpValueMaster(listHelpValueMasterDto);
                      singParamTestResDto.setLabResultDetailsViewDto(labResultDetailsViewDto);
                  }
              }

              testMasterDtoList.add(singParamTestResDto);

          } else if (bioChemParamDto.getTestType().equals(MULTY_PARAM_TEST)) {
        	  
        	  
        		
				SingParamTestResDto singParamTestResDto = new SingParamTestResDto();
				Response testRes = iBioChemResultEntryDao.getMultyParameterTestDetails(bioChemParamDto);
				if (testRes.getObject() != null) {
					singParamTestResDto = (SingParamTestResDto) testRes.getObject();
				}

            
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
	public Response saveValidationWorkList(List<LabResultMasterDto> listLabResultMasterDto) throws Exception {
		try {
		  for (Iterator iterator = listLabResultMasterDto.iterator(); iterator.hasNext();) {
            LabResultMasterDto labResultMasterDto = (LabResultMasterDto) iterator.next();
            Set<Integer> reportType = new HashSet<>();
            if (labResultMasterDto == null) {
                logger.info("INFO : labResultMasterDto is NULL");
                return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
            } else {
                
                labResultMasterDto.setUpdatedDate(TimeZoneComponent.getDateByzone(labResultMasterDto.getOrgUnitId()).getTime());
                labResultMasterDto.setUpdatedBy(labResultMasterDto.getCreatedBy());
                labResultMasterDto.setResultValidatedBy(labResultMasterDto.getCreatedBy());
                labResultMasterDto.setResultValidatedDatetime(TimeZoneComponent.getDateByzone(labResultMasterDto.getOrgUnitId()));
                for (Iterator iterator1 = labResultMasterDto.getListLabSampleResultDetailsMaster().iterator(); iterator1
                        .hasNext();) {
                    LabResultDetailsViewDto labResultDetailsViewDto = (LabResultDetailsViewDto) iterator1.next();
                    labResultDetailsViewDto.setUpdatedBy(labResultMasterDto.getCreatedBy());
                    labResultDetailsViewDto.setUpdatedDate(TimeZoneComponent.getDateByzone(labResultMasterDto.getOrgUnitId()).getTime());
                    labResultDetailsViewDto = CheckReportType.reportType(labResultDetailsViewDto);
                    if(labResultDetailsViewDto.getReportType() !=null) {
                    	reportType.add(labResultDetailsViewDto.getReportType());
                    }
                    
                }
                labResultMasterDto.setReportType(Collections.max(reportType));
              }
            }
		  return iBiochemValidationWorklistDao.saveValidationWorkList(listLabResultMasterDto);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getPrivousResultBySample(BioChemParamDto bioChemParamDto) throws ApplicationException {
		try {
			List<SingParamTestResDto> listSingParamTestResDto = new ArrayList<>();
			Response<TestMasterDto, Integer> response = null;
			bioChemParamDto.setAssingnedFrom(bioChemParamDto.getOrgUnitId());
			 Response<SingParamTestResDto, Integer> resultEnterDateRes =  iBioChemistryDao.getResultReleaseByDateTime(bioChemParamDto);
			 if(resultEnterDateRes.getObject()!=null)
			 {
			if (bioChemParamDto.getTestType().equals(SINGLE_PARAM_TEST)) {
				SingParamTestResDto singParamTestResDto = new SingParamTestResDto();
				
				response = iBioChemResultEntryDao.getSingleParamTestResult(bioChemParamDto);
				if(response.getObject()!=null)
				{
					TestMasterDto testMasterDto = response.getObject();
					singParamTestResDto.setTestId(testMasterDto.getTestId());
					singParamTestResDto.setTestDesc(testMasterDto.getTestDesc());
					singParamTestResDto.setTestCode(testMasterDto.getTestCode());
				    singParamTestResDto.setTestType(bioChemParamDto.getTestType());
					singParamTestResDto.setTechniqueName(testMasterDto.getTechniqueName());
					singParamTestResDto.setResultEnterByUser(resultEnterDateRes.getObject().getResultEnterByUser());
		            singParamTestResDto.setResultEnterDatetime(resultEnterDateRes.getObject().getResultEnterDatetime());
		            singParamTestResDto.setResultValidatedBy(resultEnterDateRes.getObject().getResultValidatedBy());
		            singParamTestResDto.setResultValidatedByUser(resultEnterDateRes.getObject().getResultValidatedByUser());
		            singParamTestResDto.setResultAuthorisedBy(resultEnterDateRes.getObject().getResultAuthorisedBy());
		            singParamTestResDto.setResultAuthorisedDatetime(resultEnterDateRes.getObject().getResultAuthorisedDatetime());
		            singParamTestResDto.setIsOnHoldParameter(resultEnterDateRes.getObject().getIsOnHoldParameter());

					Response<LabResultDetailsViewDto, Integer> labResDetails = iBiochemValidationWorklistDao
							.getPrivousResultBySample(bioChemParamDto);
					
			
					if (labResDetails.getListObject() != null) {
						for (Iterator iterator = labResDetails.getListObject().iterator(); iterator.hasNext();) {
							LabResultDetailsViewDto labResultDetailsViewDto = (LabResultDetailsViewDto) iterator.next();
							List<HelpValueMasterDto> listHelpValueMasterDto = (List<HelpValueMasterDto>) iParameterMasterDao
									.getHelpValueByParameter(bioChemParamDto.getOrgId(),
											bioChemParamDto.getOrgUnitId(),
											labResultDetailsViewDto.getParameterId()).getListObject();
							labResultDetailsViewDto.setListHelpValueMaster(listHelpValueMasterDto);
							singParamTestResDto.setLabResultDetailsViewDto(labResultDetailsViewDto);
						}
					}
					listSingParamTestResDto.add(singParamTestResDto);
					return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, listSingParamTestResDto, null);
				}
				

			} else if (bioChemParamDto.getTestType().equals(MULTY_PARAM_TEST)) {
				response = iBioChemResultEntryDao.getMultyParamTestResult(bioChemParamDto);
				if(response.getObject()!=null)
				{
				 TestMasterDto testMasterDto = response.getObject();
				 SingParamTestResDto singParamTestResDto = new SingParamTestResDto();
				 List<MultyParamTestHeaderDto> listMultyParamTestHeaderDto = new ArrayList<>();

				singParamTestResDto.setTestId(testMasterDto.getTestId());
				singParamTestResDto.setTestDesc(testMasterDto.getTestDesc());
				singParamTestResDto.setTestCode(testMasterDto.getTestCode());
				singParamTestResDto.setTechniqueName(testMasterDto.getTechniqueName());
				singParamTestResDto.setTestType(bioChemParamDto.getTestType());
				singParamTestResDto.setResultEnterByUser(resultEnterDateRes.getObject().getResultEnterByUser());
	            singParamTestResDto.setResultEnterDatetime(resultEnterDateRes.getObject().getResultEnterDatetime());
	            singParamTestResDto.setResultValidatedBy(resultEnterDateRes.getObject().getResultValidatedBy());
                singParamTestResDto.setResultValidatedByUser(resultEnterDateRes.getObject().getResultValidatedByUser());
                singParamTestResDto.setResultAuthorisedBy(resultEnterDateRes.getObject().getResultAuthorisedBy());
                singParamTestResDto.setResultAuthorisedDatetime(resultEnterDateRes.getObject().getResultAuthorisedDatetime());

				// Group By HeaderId
				Map<Integer, List<TestParamMpprDto>> testParamMpprDto = testMasterDto.getListTestParamMppr().stream()
						.collect(Collectors.groupingBy(TestParamMpprDto::getHeaderId));
				

				for (Map.Entry<Integer, List<TestParamMpprDto>> entry : testParamMpprDto.entrySet())
				{
					MultyParamTestHeaderDto multyParamTestHeaderDto = new MultyParamTestHeaderDto();
					for (Iterator iterator = entry.getValue().iterator(); iterator.hasNext();) 
					{
						List<LabResultDetailsViewDto> listLabResultDetailsViewDto = new ArrayList<>();
						TestParamMpprDto tstParamMpprDto = (TestParamMpprDto) iterator.next();
						multyParamTestHeaderDto.setHeaderId(tstParamMpprDto.getHeaderId());
						multyParamTestHeaderDto.setHeaderCode(tstParamMpprDto.getHeaderDesc());
						multyParamTestHeaderDto.setHeaderDesc(tstParamMpprDto.getHeaderDesc());

						bioChemParamDto.setHeaderId(multyParamTestHeaderDto.getHeaderId());
						Response<LabResultDetailsViewDto, Integer> labResDetails = iBiochemValidationWorklistDao
								.getPrivousResultBySample(bioChemParamDto);
						
						
						if (labResDetails.getListObject() != null) 
						{
							for (Iterator iterator1 = labResDetails.getListObject().iterator(); iterator1.hasNext();)
							{
								LabResultDetailsViewDto labResultDetailsViewDto = (LabResultDetailsViewDto) iterator1.next();
								List<HelpValueMasterDto> listHelpValueMasterDto = (List<HelpValueMasterDto>) iParameterMasterDao
										.getHelpValueByParameter(bioChemParamDto.getOrgId(),
												bioChemParamDto.getOrgUnitId(),
												labResultDetailsViewDto.getParameterId()).getListObject();
								labResultDetailsViewDto.setListHelpValueMaster(listHelpValueMasterDto);
								labResultDetailsViewDto.setParamPrintOrder(tstParamMpprDto.getParaSequence());
								listLabResultDetailsViewDto.add(labResultDetailsViewDto);
							}
						}
						multyParamTestHeaderDto.setListLabResultDetailsViewDto(listLabResultDetailsViewDto);
					}
					listMultyParamTestHeaderDto.add(multyParamTestHeaderDto);
				}
				singParamTestResDto.setListMultyParamTestHeaderDto(listMultyParamTestHeaderDto);
				listSingParamTestResDto.add(singParamTestResDto);
				return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, listSingParamTestResDto, null);
			 }
			}
			}
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	@Transactional
	public Response labValidationWorklistSearch(SearchDto searchDto) throws ApplicationException {
		try {
			return iBiochemValidationWorklistDao.labValidationWorklistSearch(searchDto);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response consolidatedWorklistSearch(SearchDto searchDto) throws ApplicationException {
		try {
			List<LabResultMasterDto>  list  =  new ArrayList<>();
			Integer count = 0;
			Response res =  iBiochemValidationWorklistDao.consolidatedWorklistSearch(searchDto);
			if(res.getListObject()!= null && res.getListObject().size()>0) {
				list.addAll(res.getListObject());
				count = (Integer) res.getObject();
			}
			
			Response res1 =  iBiochemValidationWorklistDao.consolidatedSerologyValidationSearch(searchDto);
			
			if(res1.getListObject()!= null && res1.getListObject().size()>0) {
				list.addAll(res1.getListObject());

				count = count + (Integer) res1.getObject();
			}
//			else {
			if (list != null && !list.isEmpty()) {
				List<Integer> labSampleDtlIds = list.stream().map(x -> x.getLabSampleDtlsId()).collect(Collectors.toList());
				if (labSampleDtlIds != null && !labSampleDtlIds.isEmpty()) {
					ClinicalInterventionProcedureDto interventionDto = new ClinicalInterventionProcedureDto();
					interventionDto.setLabSampleDtlsIds(labSampleDtlIds);
					interventionDto.setUnitId(searchDto.getOrgUnitId());
					interventionDto.setOrganizationId(searchDto.getOrgId());
					Response interventionResponse = iClinicalInterventionProcedureDao.getInterventionDetails(interventionDto);
					if (interventionResponse.getStatus().equals(SUCCESS) && interventionResponse.getListObject() != null && !interventionResponse.getListObject().isEmpty()) {
						List<ClinicalInterventionProcedureDto> interventionList = interventionResponse.getListObject();

						Map<Integer, ClinicalInterventionProcedureDto> interventionMap =
								interventionList.stream()
										.collect(Collectors.toMap(
												ClinicalInterventionProcedureDto::getLabSampleDtlsId,
												Function.identity(),
												(existing, duplicate) -> duplicate
										));

						list.forEach(labRecord -> {
							ClinicalInterventionProcedureDto intervention =
									interventionMap.get(labRecord.getLabSampleDtlsId());

							if (labRecord != null) {
								labRecord.setClinicalInterventionProcedureId(intervention.getClinicalInterventionProcedureId());
								labRecord.setIsClinicalInterventionRequired(intervention.getIsClinicalInterventionRequired());
								labRecord.setInterventionEndDate(intervention.getInterventionEndDate());
								labRecord.setInterventionCompletedDoctorId(intervention.getInterventionCompletedDoctorId());
							}
						});
					}
				}
			}
//			}
			
			return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, list, count);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response labValidationWorklistTypeSearch(SearchDto searchDto) throws ApplicationException {
		try {
			return iBiochemValidationWorklistDao.labValidationWorklistTypeSearch(searchDto);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response consolidatedWorklistTypeSearch(SearchDto searchDto) throws ApplicationException {
		try {
			List<LabResultMasterDto>  list  =  new ArrayList<>();
			Integer count = 0;
			Response res =  iBiochemValidationWorklistDao.consolidatedWorklistTypeSearch(searchDto);
			if(res.getListObject()!= null && res.getListObject().size()>0) {
				list.addAll(res.getListObject());
				count = (Integer) res.getObject();
			}
			
			Response res1 =  iBiochemValidationWorklistDao.consolidatedSerologyValidationTypeSearch(searchDto);
			
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

}
