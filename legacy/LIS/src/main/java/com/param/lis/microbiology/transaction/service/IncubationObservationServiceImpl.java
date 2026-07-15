package com.param.lis.microbiology.transaction.service;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.ITransactionConstants;
import com.param.lis.global.common.Response;
import com.param.lis.global.common.SearchDto;
import com.param.lis.microbiology.transaction.dao.ISensitivityTestingDao;
import com.param.lis.microbiology.transaction.dao.IincubationObservationDao;
import com.param.lis.microbiology.transaction.dto.MicrobioParamDto;
import com.param.lis.microbiology.transaction.dto.SensitivityTestResultDetailsMasterDto;
import com.param.lis.microbiology.transaction.dto.TMicroIncubationMasterDto;
import com.param.lis.transaction.dto.LabSampleDetailsMasterDto;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class IncubationObservationServiceImpl
		implements IincubationObservationService, ICommonConstants, ITransactionConstants, IError {

	@Autowired
	IincubationObservationDao iincubationObservationDao;

	@Autowired
	IMicrobiologyService iMicrobiologyService;
	
	@Autowired
	ISensitivityTestingDao iSensitivityTestingDao;

	final static Logger logger = Logger.getLogger(MicrobiologyServiceImpl.class);

	@Override
	@Transactional
	public Response getIncubationObservationList(MicrobioParamDto microbioParamDto) throws ApplicationException {
		try {
			return iincubationObservationDao.getIncubationObservationList(microbioParamDto);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getIncubationObservationListCount(MicrobioParamDto microbioParamDto) throws ApplicationException {
		try {
			return iincubationObservationDao.getIncubationObservationListCount(microbioParamDto);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response saveIncubationTrasaction(TMicroIncubationMasterDto tMicroIncubationMasterDto)
			throws ApplicationException {
		try {
			Response<TMicroIncubationMasterDto, Integer> tMicroIncubationMasterRes = iincubationObservationDao.saveIncubationTrasaction(tMicroIncubationMasterDto);
			
			if(tMicroIncubationMasterDto.getSensitivityTestResultDetailsMasterDto() != null  && tMicroIncubationMasterDto.getSensitivityTestResultDetailsMasterDto().size()>0){
				iSensitivityTestingDao.saveCultureTest(tMicroIncubationMasterDto,tMicroIncubationMasterDto.getSensitivityTestResultDetailsMasterDto());
			}
			
			if (tMicroIncubationMasterRes.getCode().equals(SUCCESS_200)) {
				LabSampleDetailsMasterDto labSampleDetailsMasterDto = new LabSampleDetailsMasterDto();
				labSampleDetailsMasterDto.setPerformedUnit(tMicroIncubationMasterDto.getOrgUnitId());
				labSampleDetailsMasterDto.setAssignUnit(tMicroIncubationMasterDto.getOrgUnitId());
				labSampleDetailsMasterDto.setCreatedBy(tMicroIncubationMasterDto.getCreatedBy());
				labSampleDetailsMasterDto.setOrgId(tMicroIncubationMasterDto.getOrgId());
				labSampleDetailsMasterDto.setOrgUnitId(tMicroIncubationMasterDto.getOrgUnitId());
				labSampleDetailsMasterDto.setLabSampleDtlsId(tMicroIncubationMasterDto.getLabSampleDtlsId());
				labSampleDetailsMasterDto.setCreatedDate(new Date(tMicroIncubationMasterDto.getCreatedDate()));
				if (tMicroIncubationMasterDto.getIsCompleted().equals('Y')) {
					
					if(tMicroIncubationMasterDto.getCultureTest() !=  null && tMicroIncubationMasterDto.getCultureTest() == true){
						labSampleDetailsMasterDto.setSampleStatusId(REPORT_RELEASED);	
					}else{
						labSampleDetailsMasterDto.setSampleStatusId(INCUBATION_OBJSERVATION_COMPLETED);
					}
					
					Response<LabSampleDetailsMasterDto, Integer> sampleRes = iMicrobiologyService
							.updateLabSampleDetailsMaster(labSampleDetailsMasterDto);
					if (sampleRes.getCode().equals(SUCCESS_200)) {
						return new Response(SUCCESS, SUCCESS_200, "Result Saved Successfully.", null, null);
					} else {
						return new Response(ERROR, ERR_500, "Failed to Save Result.", null, null);
					}

				} else if (tMicroIncubationMasterDto.getPartially().equals('Y')) {
					
					labSampleDetailsMasterDto.setSampleStatusId(PARTIAL_REPORT_RELEASED);
					Response<LabSampleDetailsMasterDto, Integer> sampleRes = iMicrobiologyService
							.updateLabSampleDetailsMaster(labSampleDetailsMasterDto);
					if (sampleRes.getCode().equals(SUCCESS_200)) {
						return new Response(SUCCESS, SUCCESS_200, "Result Saved Sucessfully.", null, null);
					} else {
						return new Response(ERROR, ERR_500, "Failed to Save Result.", null, null);
					}
				
					
					
				}else {
					labSampleDetailsMasterDto.setSampleStatusId(DURING_OBJSERVATION);
					Response<LabSampleDetailsMasterDto, Integer> sampleRes = iMicrobiologyService
							.updateLabSampleDetailsMaster(labSampleDetailsMasterDto);
					if (sampleRes.getCode().equals(SUCCESS_200)) {
						return new Response(SUCCESS, SUCCESS_200, "Result Saved Sucessfully.", null, null);
					} else {
						return new Response(ERROR, ERR_500, "Failed to Save Result.", null, null);
					}
				}
			} else {
				return new Response(ERROR, ERR_500, "Failed to Save Result.", null, null);
			}

		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}

	}

	@Override	
	@Transactional
	public Response updateIncubationTransaction(TMicroIncubationMasterDto tMicroIncubationMasterDto)
			throws ApplicationException {
		try {
			Response<TMicroIncubationMasterDto, Integer> tMicroIncubationMasterRes = iincubationObservationDao .updateIncubationTransaction(tMicroIncubationMasterDto);
			
			if(tMicroIncubationMasterDto.getSensitivityTestResultDetailsMasterDto() != null  && tMicroIncubationMasterDto.getSensitivityTestResultDetailsMasterDto().size()>0){
				iSensitivityTestingDao.saveCultureTest(tMicroIncubationMasterDto,tMicroIncubationMasterDto.getSensitivityTestResultDetailsMasterDto());
			}
			
			if (tMicroIncubationMasterRes.getCode().equals(SUCCESS_200)) {
				LabSampleDetailsMasterDto labSampleDetailsMasterDto = new LabSampleDetailsMasterDto();
				labSampleDetailsMasterDto.setPerformedUnit(tMicroIncubationMasterDto.getOrgUnitId());
				labSampleDetailsMasterDto.setAssignUnit(tMicroIncubationMasterDto.getOrgUnitId());
				labSampleDetailsMasterDto.setOrgId(tMicroIncubationMasterDto.getOrgId());
				labSampleDetailsMasterDto.setOrgUnitId(tMicroIncubationMasterDto.getOrgUnitId());
				labSampleDetailsMasterDto.setLabSampleDtlsId(tMicroIncubationMasterDto.getLabSampleDtlsId());
				labSampleDetailsMasterDto.setCreatedBy(tMicroIncubationMasterDto.getCreatedBy());
				labSampleDetailsMasterDto.setCreatedDate(new Date(tMicroIncubationMasterDto.getCreatedDate()));
				if (tMicroIncubationMasterDto.getIsCompleted().equals('Y')) {
					
					if(tMicroIncubationMasterDto.getCultureTest() !=  null && tMicroIncubationMasterDto.getCultureTest() == true){
						labSampleDetailsMasterDto.setSampleStatusId(REPORT_RELEASED);	
					}else{
						labSampleDetailsMasterDto.setSampleStatusId(INCUBATION_OBJSERVATION_COMPLETED);
					}
					
					Response<LabSampleDetailsMasterDto, Integer> sampleRes = iMicrobiologyService
							.updateLabSampleDetailsMaster(labSampleDetailsMasterDto);
					if (sampleRes.getCode().equals(SUCCESS_200)) {
						return new Response(SUCCESS, SUCCESS_200, "Result Saved Sucessfully.", null, null);
					} else {
						return new Response(ERROR, ERR_500, "Failed to Save Result.", null, null);

					}

				} else if (tMicroIncubationMasterDto.getPartially().equals('Y')) {
					
					labSampleDetailsMasterDto.setSampleStatusId(PARTIAL_REPORT_RELEASED);
					Response<LabSampleDetailsMasterDto, Integer> sampleRes = iMicrobiologyService
							.updateLabSampleDetailsMaster(labSampleDetailsMasterDto);
					if (sampleRes.getCode().equals(SUCCESS_200)) {
						return new Response(SUCCESS, SUCCESS_200, "Result Saved Sucessfully.", null, null);
					} else {
						return new Response(ERROR, ERR_500, "Failed to Save Result.", null, null);
					}
				
					
					
				} else {
					labSampleDetailsMasterDto.setSampleStatusId(DURING_OBJSERVATION);
					Response<LabSampleDetailsMasterDto, Integer> sampleRes = iMicrobiologyService
							.updateLabSampleDetailsMaster(labSampleDetailsMasterDto);
					if (sampleRes.getCode().equals(SUCCESS_200)) {
						return new Response(SUCCESS, SUCCESS_200, "Result Saved Sucessfully.", null, null);
					} else {
						return new Response(ERROR, ERR_500, "Failed to Save Result.", null, null);
					}
				}
			} else {
				return new Response(ERROR, ERR_500, "Failed to Save Result.", null, null);
			}

		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getIncubationDetails(Integer tIncubationId, Integer orgId, Integer orgUnitId)
			throws ApplicationException {
		try {
			 Response res = iincubationObservationDao.getIncubationDetails(tIncubationId, orgId, orgUnitId);
			 TMicroIncubationMasterDto tMicroIncubationMasterDto =  new TMicroIncubationMasterDto();
			 if(res.getObject() != null){
				 
				  tMicroIncubationMasterDto = (TMicroIncubationMasterDto) res.getObject();
				 Response res1 =  iincubationObservationDao.getCultureTestResult(tMicroIncubationMasterDto);
				 
				 if(res1.getListObject() != null && res1.getListObject().size()>0){
					 
					 List<SensitivityTestResultDetailsMasterDto> listTMicroIncubationDetailsMasterList = res1.getListObject();
					 
					 tMicroIncubationMasterDto.setSensitivityResultId(listTMicroIncubationDetailsMasterList.getFirst().getSensitivityResultId());
					 
					 tMicroIncubationMasterDto.setSensitivityTestResultDetailsMasterDto(listTMicroIncubationDetailsMasterList);
				 }
				 
				 
			 }
			 
			 return new Response(SUCCESS, SUCCESS_200, null, null, tMicroIncubationMasterDto);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getIncubationObservationListSearch(SearchDto dto) throws ApplicationException {
		try {
			return iincubationObservationDao.getIncubationObservationListSearch(dto);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	

}
